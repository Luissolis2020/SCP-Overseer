package net.luis.scpmod.network;

import net.luis.scpmod.pda.Mission;
import net.luis.scpmod.pda.PDADataHandler;
import net.luis.scpmod.research.ModResearchEntries;
import net.luis.scpmod.research.ResearchManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.Set;
import java.util.function.Supplier;

public class TakePhotoPacket {
    private final ResourceLocation id;

    public TakePhotoPacket(ResourceLocation id) {
        this.id = id;
    }

    public TakePhotoPacket(FriendlyByteBuf buf) {
        this.id = buf.readResourceLocation();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(id);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) {
                return;
            }

            if (ModResearchEntries.get(id) == null) {
                player.displayClientMessage(
                        net.minecraft.network.chat.Component.literal("Invalid research target."),
                        true
                );
                return;
            }

            ResearchManager manager = ResearchManager.get(player);
            manager.advanceStage(player, id);

            Mission mission = PDADataHandler.loadMission(player);

            boolean missionChanged = false;
            boolean completedNow = false;
            Mission completedMission = null;

            if ("find_anomalies".equals(mission.getId())) {
                String key = id.toString();

                Set<String> discovered = PDADataHandler.getDiscoveredAnomalies(player);
                if (!discovered.contains(key)) {
                    PDADataHandler.markAnomalyDiscovered(player, key);
                    mission.incrementProgress();
                    missionChanged = true;

                    if (mission.isComplete()) {
                        if (!PDADataHandler.isTabUnlocked(player, "FACILITY")) {
                            PDADataHandler.unlockTab(player, "FACILITY");
                        }

                        completedNow = true;
                        completedMission = mission;

                        Mission next = new Mission(
                                "build_facility",
                                net.minecraft.network.chat.Component.literal("Establish Foundation Outpost"),
                                net.minecraft.network.chat.Component.literal("Place and build the Foundation Starter Facility."),
                                1
                        );

                        PDADataHandler.saveMission(player, next);
                    } else {
                        PDADataHandler.saveMission(player, mission);
                    }
                }
            }

            if (completedNow && completedMission != null) {
                SCPModNetwork.syncMissionCompleteTo(player,
                        completedMission.getId(),
                        completedMission.getTitle()
                );
            }

            SCPModNetwork.syncPDAStateTo(player, true);
        });

        ctx.get().setPacketHandled(true);
    }
}
