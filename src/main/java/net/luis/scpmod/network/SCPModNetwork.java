package net.luis.scpmod.network;

import net.luis.scpmod.SCPMod;
import net.luis.scpmod.facility.building.FacilityManager;
import net.luis.scpmod.facility.building.FacilityRoomInstance;
import net.luis.scpmod.pda.Mission;
import net.luis.scpmod.pda.PDADataHandler;
import net.luis.scpmod.research.ResearchProjects;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.List;

public class SCPModNetwork {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void registerPackets() {
        INSTANCE.registerMessage(
                packetId++,
                MadnessSyncPacket.class,
                MadnessSyncPacket::encode,
                MadnessSyncPacket::decode,
                MadnessSyncPacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                SyncCursedPacket.class,
                SyncCursedPacket::encode,
                SyncCursedPacket::decode,
                SyncCursedPacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                DrinkCounterSyncPacket.class,
                DrinkCounterSyncPacket::encode,
                DrinkCounterSyncPacket::decode,
                DrinkCounterSyncPacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                ConfirmFacilityPlacementPacket.class,
                ConfirmFacilityPlacementPacket::encode,
                ConfirmFacilityPlacementPacket::new,
                ConfirmFacilityPlacementPacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                ActivateFacilityPreviewPacket.class,
                ActivateFacilityPreviewPacket::toBytes,
                ActivateFacilityPreviewPacket::new,
                ActivateFacilityPreviewPacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                ClientboundResearchSyncPacket.class,
                ClientboundResearchSyncPacket::encode,
                ClientboundResearchSyncPacket::new,
                ClientboundResearchSyncPacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                TakePhotoPacket.class,
                TakePhotoPacket::encode,
                TakePhotoPacket::new,
                TakePhotoPacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                ClientboundPDASyncPacket.class,
                ClientboundPDASyncPacket::encode,
                ClientboundPDASyncPacket::new,
                ClientboundPDASyncPacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                ClientboundFacilityRoomsPacket.class,
                ClientboundFacilityRoomsPacket::encode,
                ClientboundFacilityRoomsPacket::new,
                ClientboundFacilityRoomsPacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                RoomHighlightRequestPacket.class,
                RoomHighlightRequestPacket::toBytes,
                RoomHighlightRequestPacket::new,
                RoomHighlightRequestPacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                RoomHighlightPacket.class,
                RoomHighlightPacket::toBytes,
                RoomHighlightPacket::new,
                RoomHighlightPacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                TeleportToRoomPacket.class,
                TeleportToRoomPacket::toBytes,
                TeleportToRoomPacket::new,
                TeleportToRoomPacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                ToggleRoomActivePacket.class,
                ToggleRoomActivePacket::toBytes,
                ToggleRoomActivePacket::new,
                ToggleRoomActivePacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                ClientboundMissionSyncPacket.class,
                ClientboundMissionSyncPacket::encode,
                ClientboundMissionSyncPacket::new,
                ClientboundMissionSyncPacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                ClientboundMissionCompletePacket.class,
                ClientboundMissionCompletePacket::encode,
                ClientboundMissionCompletePacket::new,
                ClientboundMissionCompletePacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                ClientboundBlockScansPacket.class,
                ClientboundBlockScansPacket::encode,
                ClientboundBlockScansPacket::new,
                ClientboundBlockScansPacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                ClientboundResearchPointsPacket.class,
                ClientboundResearchPointsPacket::encode,
                ClientboundResearchPointsPacket::new,
                ClientboundResearchPointsPacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                BuyResearchUnlockPacket.class,
                BuyResearchUnlockPacket::encode,
                BuyResearchUnlockPacket::new,
                BuyResearchUnlockPacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                ClientboundResearchCatalogPacket.class,
                ClientboundResearchCatalogPacket::encode,
                ClientboundResearchCatalogPacket::new,
                ClientboundResearchCatalogPacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                RequestPreviewOverlapPacket.class,
                RequestPreviewOverlapPacket::encode,
                RequestPreviewOverlapPacket::new,
                RequestPreviewOverlapPacket::handle
        );

        INSTANCE.registerMessage(
                packetId++,
                ClientboundPreviewOverlapPacket.class,
                ClientboundPreviewOverlapPacket::encode,
                ClientboundPreviewOverlapPacket::new,
                ClientboundPreviewOverlapPacket::handle
        );

    }

    public static <T> void sendToPlayer(T packet, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    public static <T> void sendToServer(T packet) {
        INSTANCE.sendToServer(packet);
    }

    public static void syncPDAStateTo(ServerPlayer player) {
        syncPDAStateTo(player, true);
    }

    public static void syncPDAStateTo(ServerPlayer player, boolean sendMission) {
        var tabs = PDADataHandler.getAllUnlockedTabs(player);
        var sortMode = PDADataHandler.loadSCPSortMode(player);
        int research = PDADataHandler.getResearchPoints(player);
        var bought = PDADataHandler.getBoughtResearchUnlocks(player);
        sendToPlayer(new ClientboundPDASyncPacket(tabs, sortMode, research, bought), player);
        var catalog = ResearchProjects.getCatalogSnapshot().stream()
                .map(p -> new ClientboundResearchCatalogPacket.Entry(
                        p.id(),
                        p.category(),
                        p.name(),
                        p.desc(),
                        p.cost(),
                        p.sort(),
                        p.requires(),
                        p.tier(),
                        p.hiddenUntilPrereq(),
                        p.repeatable()
                ))
                .toList();
        sendToPlayer(new ClientboundResearchCatalogPacket(catalog), player);

        List<ClientboundFacilityRoomsPacket.FacilityRoomData> rooms = FacilityManager.get(player.serverLevel()).getRooms()
                .stream()
                .filter(room -> canAccessRoom(player, room))
                .map(room -> {
                    var site = room.constructionSite;
                    int progress = 0;
                    boolean hasBuilder = site != null && site.hasBuilder();

                    if (site != null) {
                        int total = site.getTotalWorkUnits();
                        int done = site.getCompletedWorkUnits();
                        if (total > 0) {
                            progress = (int) ((done / (float) total) * 100);
                            if (!room.isBuilt()) {
                                progress = Math.min(progress, 99);
                            }
                        }
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
                })
                .toList();

        sendToPlayer(new ClientboundFacilityRoomsPacket(rooms), player);
        sendToPlayer(
                new ClientboundBlockScansPacket(PDADataHandler.getScannedBlocksList(player)),
                player
        );
        if (sendMission) {
            var mission = PDADataHandler.loadMission(player);
            syncMissionTo(player, mission);
        }
    }

    public static boolean canAccessRoom(ServerPlayer p, FacilityRoomInstance r) {
        if (r.getOwner() == null) return true;                 // legacy/public rooms
        if (r.getOwner().equals(p.getUUID())) return true;     // owner
        return p.hasPermissions(2);                     // admin bypass
    }

    public static void syncMissionTo(ServerPlayer player, Mission mission) {
        sendToPlayer(new ClientboundMissionSyncPacket(mission), player);
    }

    public static void syncMissionCompleteTo(ServerPlayer player, String id, Component title) {
        sendToPlayer(new ClientboundMissionCompletePacket(id, title), player);
    }

    public static void syncResearchPointsTo(ServerPlayer player) {
        int research = PDADataHandler.getResearchPoints(player);
        sendToPlayer(new ClientboundResearchPointsPacket(research), player);
    }

    public static void syncFacilityRoomsTo(ServerPlayer player) {
        List<ClientboundFacilityRoomsPacket.FacilityRoomData> rooms = FacilityManager.get(player.serverLevel()).getRooms()
                .stream()
                .filter(room -> canAccessRoom(player, room))
                .map(room -> {
                    var site = room.constructionSite;
                    int progress = 0;
                    boolean hasBuilder = site != null && site.hasBuilder();

                    if (site != null) {
                        int total = site.getTotalWorkUnits();
                        int done = site.getCompletedWorkUnits();
                        if (total > 0) {
                            progress = (int) ((done / (float) total) * 100);
                            if (!room.isBuilt()) progress = Math.min(progress, 99);
                        }
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
                })
                .toList();

        sendToPlayer(new ClientboundFacilityRoomsPacket(rooms), player);
    }

}
