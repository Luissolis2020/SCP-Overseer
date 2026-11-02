package net.luis.scpmod.client;

import net.luis.scpmod.client.gui.BuildingScreen;
import net.luis.scpmod.network.RequestPreviewOverlapPacket;
import net.luis.scpmod.network.SCPModNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;

import java.util.Collections;
import java.util.List;

public class ClientPreviewState {
    private static boolean active = false;
    private static BlockPos previewPos = BlockPos.ZERO;
    private static StructureTemplate template = null;
    private static StructurePlaceSettings settings = new StructurePlaceSettings();
    private static ResourceLocation structureId;
    private static boolean socketLocked = false;

    private static BuildPreviewState previewState = BuildPreviewState.NONE;
    private static List<AABB> serverOverlaps = Collections.emptyList();
    private static boolean serverSaysOverlapping = false;

    private static long lastOverlapRequestMs = 0;

    public static void activate(BlockPos pos, StructureTemplate templateIn, StructurePlaceSettings settingsIn, ResourceLocation id) {
        activate(pos, templateIn, settingsIn, id, false);
    }

    public static void activate(BlockPos pos, StructureTemplate templateIn, StructurePlaceSettings settingsIn, ResourceLocation id, boolean lockToSocket) {
        active = true;
        previewPos = pos;
        template = templateIn;
        settings = settingsIn;
        structureId = id;
        socketLocked = lockToSocket;
        previewState = BuildPreviewState.PLACEMENT_LOCKED;
        requestOverlapUpdate();
    }

    public static void deactivate() {
        active = false;
        template = null;
        settings = new StructurePlaceSettings();
        structureId = null;
        socketLocked = false;
        previewState = BuildPreviewState.NONE;
        serverOverlaps = Collections.emptyList();
        serverSaysOverlapping = false;
    }

    public static boolean isActive() {
        return active && structureId != null;
    }

    public static BlockPos getPreviewPos() {
        return previewPos;
    }

    public static StructureTemplate getTemplate() {
        return template;
    }

    public static StructurePlaceSettings getSettings() {
        return settings;
    }

    public static ResourceLocation getStructureId() {
        return structureId;
    }

    public static void rotateClockwise() {
        if (socketLocked) return;
        settings.setRotation(switch (settings.getRotation()) {
            case NONE -> Rotation.CLOCKWISE_90;
            case CLOCKWISE_90 -> Rotation.CLOCKWISE_180;
            case CLOCKWISE_180 -> Rotation.COUNTERCLOCKWISE_90;
            case COUNTERCLOCKWISE_90 -> Rotation.NONE;
        });
        requestOverlapUpdate();
    }

    public static BuildPreviewState getPreviewState() {
        return previewState;
    }

    public static void setPreviewState(BuildPreviewState state) {
        previewState = state;

        if (state == BuildPreviewState.PLACEMENT_LOCKED) {
            Minecraft mc = Minecraft.getInstance();
            mc.execute(() -> {
                if (mc.screen == null) {
                    mc.setScreen(new BuildingScreen());
                }
            });
        }
    }

    public static void activateDummy(BlockPos pos, ResourceLocation id) {
        var mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.getSingleplayerServer() == null) {
            System.out.println("[ClientPreviewState] Error: StructureManager not available on client.");
            return;
        }

        StructureTemplate loaded = mc.getSingleplayerServer().getStructureManager().getOrCreate(id);

        active = true;
        previewPos = pos;
        structureId = id;
        template = loaded;
        settings = new StructurePlaceSettings();
        socketLocked = false;
        previewState = BuildPreviewState.PLACEMENT_LOCKED;
        requestOverlapUpdate();
    }

    public static void setServerOverlaps(List<AABB> overlaps, boolean overlapping) {
        serverOverlaps = overlaps == null ? Collections.emptyList() : overlaps;
        serverSaysOverlapping = overlapping;
    }

    public static List<AABB> getServerOverlaps() {
        return serverOverlaps;
    }

    public static boolean isServerSaysOverlapping() {
        return serverSaysOverlapping;
    }

    private static void requestOverlapUpdate() {
        if (!active || structureId == null) return;

        long now = System.currentTimeMillis();
        if (now - lastOverlapRequestMs < 75) return; // ~13/sec max
        lastOverlapRequestMs = now;

        Mirror mirror = settings.getMirror();
        if (mirror == null) mirror = Mirror.NONE;

        SCPModNetwork.sendToServer(new RequestPreviewOverlapPacket(
                previewPos,
                structureId,
                settings.getRotation(),
                mirror
        ));
    }

    public static void movePreview(int dx, int dy, int dz) {
        if (!active || socketLocked) return;
        previewPos = previewPos.offset(dx, dy, dz);
        requestOverlapUpdate();
    }

    public static boolean isSocketLocked() {
        return socketLocked;
    }

}
