package net.luis.scpmod.event;

import net.luis.scpmod.SCPMod;
import net.luis.scpmod.anomaly.AnomalyFieldSavedData;
import net.luis.scpmod.capability.DrinkCounterCapability;
import net.luis.scpmod.capability.MadnessCapability;
import net.luis.scpmod.capability.MadnessProvider;
import net.luis.scpmod.command.*;
import net.luis.scpmod.entity.custom.CursedPlayer;
import net.luis.scpmod.facility.building.FacilityManager;
import net.luis.scpmod.facility.construction.ConstructionSite;
import net.luis.scpmod.item.ModItems;
import net.luis.scpmod.network.*;
import net.luis.scpmod.pda.PDADataHandler;
import net.luis.scpmod.pocket.PocketDimensionData;
import net.luis.scpmod.pocket.PocketReturnData;
import net.luis.scpmod.research.ResearchManager;
import net.luis.scpmod.research.ResearchStage;
import net.luis.scpmod.worldgen.dimension.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

import static net.luis.scpmod.entity.custom.CursedPlayer.spawnSCP5131;

@Mod.EventBusSubscriber(modid = SCPMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)

public class ForgeEventBusEvents {

    private static final String POCKET_LAST_XZ   = "scpmod_pocket_last_exit_xz";
    private static final String POCKET_LAST_POS  = "scpmod_pocket_last_pos";
    private static final String POCKET_HINT_CD   = "scpmod_pocket_hint_cd";
    private static final String POCKET_TREND     = "scpmod_pocket_trend";
    private static final String POCKET_LAST_BAND = "scpmod_pocket_last_band";
    private static final String POCKET_LAST_VERT = "scpmod_pocket_last_vert";

    private static final int    MIN_MOVE_TO_SAMPLE = 2;   // blocks (manhattan)
    private static final int    TREND_MAX = 6;            // smoothing strength
    private static final int    TREND_EMIT = 3;           // how strong before "closer/farther" fires

    private static final double[] BANDS = new double[] { 96, 64, 48, 36, 28, 22, 16, 12, 8, 5, 3 };

    private static final int VERTICAL_OFF = 6;            // ~ two floors
    private static final double XZ_FOR_VERTICAL_HINT = 14.0;

    @SubscribeEvent
    public static void attachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "madness"), new MadnessProvider());
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        FacilityDebugCommand.register(event.getDispatcher());
        FacilityPlaceCommand.register(event.getDispatcher());
        FacilityPreviewCommand.register(event.getDispatcher());
        FacilityConfirmCommand.register(event.getDispatcher());
        FacilityMoveCommand.register(event.getDispatcher());
        FacilityRotateCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(MadnessCapability.class);
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {

            CompoundTag persisted = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
            if (!persisted.getBoolean("scpmod_got_pda")) {

                ItemStack pda = new ItemStack(ModItems.PDA.get());

                boolean alreadyHas = player.getInventory().contains(pda);
                if (!alreadyHas) {
                    boolean inserted = player.getInventory().add(pda);
                    if (!inserted) {
                        player.drop(pda, false);
                    }
                }

                persisted.putBoolean("scpmod_got_pda", true);
                player.getPersistentData().put(Player.PERSISTED_NBT_TAG, persisted);
            }

            MadnessCapability madness = MadnessProvider.get(player);

            if (madness != null) {
                if (player.getPersistentData().contains("scpmod_madness")) {
                    madness.deserializeNBT(player.getPersistentData().getCompound("scpmod_madness"));
                }

                SCPModNetwork.sendToPlayer(new MadnessSyncPacket(madness.getMadnessLevel()), player);
            }

            if (CursedPlayer.isCursed(player)) {
                player.displayClientMessage(Component.literal("An eerie presence still lingers..."), true);
                SCPModNetwork.sendToPlayer(new SyncCursedPacket(true), player);
            }

            player.getCapability(DrinkCounterCapability.INSTANCE).ifPresent(counter -> {
                if (player.getPersistentData().contains("scpmod_drink_counter")) {
                    counter.loadFromNBT(player.getPersistentData().getCompound("scpmod_drink_counter"));
                }
            });

            player.getCapability(DrinkCounterCapability.INSTANCE).ifPresent(counter ->
                    SCPModNetwork.sendToPlayer(new DrinkCounterSyncPacket(counter.getDrinkCount()), player)
            );

            SCPModNetwork.syncPDAStateTo(player);
            ResearchManager.get(player).sync(player);

            FacilityManager manager = FacilityManager.get(player.serverLevel());
            List<ClientboundFacilityRoomsPacket.FacilityRoomData> roomData = manager.getRooms().stream()
                    .map(room -> {
                        int progress = 0;
                        boolean hasBuilder = false;

                        if (room.getConstructionSite() != null) {
                            ConstructionSite site = room.getConstructionSite();

                            int total = site.getTotalWorkUnits();
                            int completed = site.getCompletedWorkUnits();
                            progress = (total > 0) ? (completed * 100 / total) : 100;
                            hasBuilder = site.hasBuilder();
                        }

                        return new ClientboundFacilityRoomsPacket.FacilityRoomData(
                                room.blueprint.getDisplayName(),
                                room.getDisplayState(),
                                room.getOrigin(),
                                room.blueprint.id,
                                room.blueprint.width,
                                room.blueprint.height,
                                room.blueprint.depth,
                                room.isActive(),
                                progress,
                                hasBuilder
                        );
                    }).toList();

            //SCPModNetwork.sendToPlayer(new ClientboundFacilityRoomsPacket(roomData), player);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            MadnessCapability madness = MadnessProvider.get(player);

            if (madness != null) {
                CompoundTag savedData = madness.serializeNBT();
                player.getPersistentData().put("scpmod_madness", savedData);
            }

            player.getCapability(DrinkCounterCapability.INSTANCE).ifPresent(counter -> {
                CompoundTag savedData = counter.saveToNBT();
                player.getPersistentData().put("scpmod_drink_counter", savedData);
            });

            CompoundTag anomalies = player.getPersistentData().getCompound("scpmod_discovered_anomalies");
            player.getPersistentData().put("scpmod_discovered_anomalies", anomalies);

        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;

        CompoundTag originalPersisted = event.getOriginal().getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        event.getEntity().getPersistentData().put(Player.PERSISTED_NBT_TAG, originalPersisted.copy());

        Player newPlayer = event.getEntity();

        // clear pocket return data on death
        if (newPlayer instanceof ServerPlayer sp) {
            PocketReturnData.clear(sp);
            PocketReturnData.endPocketSession(sp);
        }

        newPlayer.getCapability(DrinkCounterCapability.INSTANCE).ifPresent(counter -> {
            counter.setDrinkCount(0);
            counter.setLastDrinkTime(0);
        });
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        System.out.println("[SCPMod] onServerStarted - initializing facility manager");
        ServerLevel level = event.getServer().overworld();
        FacilityManager.get(level);
        var data = AnomalyFieldSavedData.get(level);
        data.ensureGenerated(level);
    }


    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
                if (CursedPlayer.isCursed(player)) {
                    int curseTime = CursedPlayer.getCurseTimer(player);
                    if (curseTime < 4800) {
                        CursedPlayer.setCurseTimer(player, curseTime + 1);
                    }

                    if (curseTime == 250) {
                        spawnSCP5131(player, false, 40, 60);
                    }

                    if (curseTime == 600) {
                        spawnSCP5131(player, false, 40, 60);
                    }

                    if (curseTime == 1800) {
                        spawnSCP5131(player, false, 30, 50);
                        spawnSCP5131(player, false, 30, 50);
                    }

                    if (curseTime == 2000) {
                        spawnSCP5131(player, false, 15, 30);
                    }

                    if (curseTime == 2800) {
                        spawnSCP5131(player, false, 10, 30);
                        spawnSCP5131(player, false, 10, 30);
                        spawnSCP5131(player, false, 10, 30);
                    }

                    if (curseTime == 3600) {
                        spawnSCP5131(player, false, 10, 30);
                        spawnSCP5131(player, false, 10, 30);
                        spawnSCP5131(player, false, 10, 30);
                    }

                    if (curseTime == 3700 && !CursedPlayer.hasRealSpawned(player)) {
                        spawnSCP5131(player, true, 5, 10);
                        CursedPlayer.setRealSpawned(player, true);
                    }
                }

                tickPocketHotCold(player);
            }

            event.getServer().getAllLevels().forEach(serverLevel -> {
                FacilityManager.get(serverLevel).tick(serverLevel);
            });
        }
    }

    private static void tickPocketHotCold(ServerPlayer player) {
        if (!(player.level() instanceof ServerLevel pocketLevel)) return;
        if (!pocketLevel.dimension().equals(ModDimensions.POCKET_DIM_LEVEL)) return;

        PocketDimensionData data = PocketDimensionData.get(pocketLevel);
        if (data.exitPos == null) return;

        CompoundTag tag = player.getPersistentData();

        // ---- Cooldown gate
        int cd = tag.getInt(POCKET_HINT_CD);
        if (cd > 0) {
            tag.putInt(POCKET_HINT_CD, cd - 1);
            return;
        }

        BlockPos curPos = player.blockPosition();
        BlockPos exit   = data.exitPos;

        // ---- Init once (prevents first-tick weirdness)
        if (!tag.contains(POCKET_LAST_POS)) {
            tag.putLong(POCKET_LAST_POS, curPos.asLong());

            double initXZ = distXZ(curPos, exit);
            tag.putDouble(POCKET_LAST_XZ, initXZ);

            tag.putInt(POCKET_TREND, 0);
            tag.putInt(POCKET_LAST_BAND, bandIndex(initXZ));
            tag.putInt(POCKET_LAST_VERT, 0);

            tag.putInt(POCKET_HINT_CD, 40);
            return;
        }

        // ---- Only sample when they actually move a bit
        BlockPos lastPos = BlockPos.of(tag.getLong(POCKET_LAST_POS));
        int moved = curPos.distManhattan(lastPos);
        if (moved < MIN_MOVE_TO_SAMPLE) return;
        tag.putLong(POCKET_LAST_POS, curPos.asLong());

        // ---- Core distance: XZ only (multi-floor friendly)
        double xz = distXZ(curPos, exit);
        double lastXZ = tag.contains(POCKET_LAST_XZ) ? tag.getDouble(POCKET_LAST_XZ) : xz;
        tag.putDouble(POCKET_LAST_XZ, xz);

        double delta = lastXZ - xz; // + = closer, - = farther

        // ---- Trend smoothing (prevents flip-flop)
        int trend = tag.getInt(POCKET_TREND);

        // deadzone in blocks; smaller = more sensitive
        double deadzone = 0.35;

        int step;
        if (delta > deadzone) step = 1;
        else if (delta < -deadzone) step = -1;
        else step = 0;

        if (step == 0) {
            // decay toward 0
            if (trend > 0) trend--;
            else if (trend < 0) trend++;
        } else {
            trend += step;
        }

        trend = net.minecraft.util.Mth.clamp(trend, -TREND_MAX, TREND_MAX);
        tag.putInt(POCKET_TREND, trend);

        // ---- Band crossing detection
        int band = bandIndex(xz);
        int lastBand = tag.getInt(POCKET_LAST_BAND);
        boolean crossedBand = (band != lastBand);
        if (crossedBand) tag.putInt(POCKET_LAST_BAND, band);

        // ---- Very close feel
        boolean veryNear = xz <= 6.0;
        boolean near     = xz <= 24.0;

        // ---- Decide whether we should emit ANY feedback this sample
        boolean closerSignal  = trend >= TREND_EMIT;
        boolean fartherSignal = trend <= -TREND_EMIT;

        // If nothing meaningful and not near and no band-cross, stay quiet.
        if (!veryNear && !near && !crossedBand && !closerSignal && !fartherSignal) {
            setPocketCooldown(tag, xz);
            return;
        }

        // ---- SCP-ish vertical cue (rare, only when XZ close)
        maybeEmitVerticalHint(player, pocketLevel, tag, curPos, exit, xz);

        // ---- Main cue: prefer band crossing (feels "systemic"), else trend.
        // Text is intentionally sparse; sound is primary.
        boolean showText = veryNear
                || (near && pocketLevel.random.nextInt(3) == 0)
                || pocketLevel.random.nextInt(8) == 0;

        if (veryNear) {
            // Strong “you’re on top of it” signal
            player.playNotifySound(SoundEvents.AMETHYST_BLOCK_CHIME,
                    SoundSource.PLAYERS, 0.35f, 1.35f);

            if (showText) player.displayClientMessage(Component.literal("The air tightens. Something is *here*."), true);

        } else if (crossedBand) {
            // Band ping scales pitch slightly by band (closer => higher)
            float pitch = 0.90f + (float)((BANDS.length - 1 - band) * 0.04);
            pitch = net.minecraft.util.Mth.clamp(pitch, 0.85f, 1.35f);

            player.playNotifySound(SoundEvents.NOTE_BLOCK_CHIME.value(),
                    SoundSource.PLAYERS, 0.18f, pitch);

            if (showText && pocketLevel.random.nextInt(3) == 0) {
                player.displayClientMessage(Component.literal("A faint static grazes your hearing."), true);
            }

        } else if (closerSignal) {
            player.playNotifySound(SoundEvents.NOTE_BLOCK_CHIME.value(),
                    SoundSource.PLAYERS, 0.14f, 1.12f);

            if (showText) player.displayClientMessage(Component.literal("A low hum returns—then fades."), true);

            // reduce trend so it won’t spam “closer” every sample
            tag.putInt(POCKET_TREND, 1);

        } else if (fartherSignal) {
            player.playNotifySound(SoundEvents.NOTE_BLOCK_CHIME.value(),
                    SoundSource.PLAYERS, 0.14f, 0.88f);

            if (showText) player.displayClientMessage(Component.literal("The silence grows heavier."), true);

            tag.putInt(POCKET_TREND, -1);

        } else if (near) {
            // Near but neutral: *rare* hope ping, no text
            if (pocketLevel.random.nextInt(5) == 0) {
                player.playNotifySound(SoundEvents.AMETHYST_CLUSTER_PLACE,
                        SoundSource.PLAYERS, 0.08f, 1.05f);
            }
        }

        setPocketCooldown(tag, xz);
    }

    private static void maybeEmitVerticalHint(ServerPlayer player, ServerLevel level, CompoundTag tag,
                                              BlockPos curPos, BlockPos exit, double xz) {
        if (xz > XZ_FOR_VERTICAL_HINT) return;

        int dy = exit.getY() - curPos.getY();
        int absDy = Math.abs(dy);
        if (absDy < VERTICAL_OFF) return;

        // Don’t spam vertical hints: small cooldown “bucket”
        int lastVert = tag.getInt(POCKET_LAST_VERT);
        if (lastVert > 0) {
            tag.putInt(POCKET_LAST_VERT, lastVert - 1);
            return;
        }

        // Rare and creepy: 1 in 3 when eligible
        if (level.random.nextInt(3) != 0) return;

        tag.putInt(POCKET_LAST_VERT, 80); // ~4 seconds before another vertical hint

        if (dy > 0) {
            player.playNotifySound(SoundEvents.AMETHYST_BLOCK_CHIME,
                    SoundSource.PLAYERS, 0.10f, 1.22f);
            player.displayClientMessage(Component.literal("The vibration seems to come from above."), true);
        } else {
            player.playNotifySound(SoundEvents.AMETHYST_BLOCK_CHIME,
                    SoundSource.PLAYERS, 0.10f, 0.78f);
            player.displayClientMessage(Component.literal("It pulls downward… beneath the floor."), true);
        }
    }

    private static double distXZ(BlockPos a, BlockPos b) {
        double dx = a.getX() - b.getX();
        double dz = a.getZ() - b.getZ();
        return Math.sqrt(dx * dx + dz * dz);
    }

    // 0 = farthest band, increasing index = closer bands.
// If closer than last band, returns last index.
    private static int bandIndex(double xz) {
        for (int i = 0; i < BANDS.length; i++) {
            if (xz > BANDS[i]) return i; // still outside this band
        }
        return BANDS.length; // inside closest band (very close)
    }

    private static void setPocketCooldown(CompoundTag tag, double xzBlocks) {
        // Distance-scaled cooldown, but not extreme. Far away = quieter.
        int base = 35; // 1.75s
        int extra = (int) net.minecraft.util.Mth.clamp(xzBlocks * 0.25, 0.0, 70.0);
        tag.putInt(POCKET_HINT_CD, base + extra);
    }

}
