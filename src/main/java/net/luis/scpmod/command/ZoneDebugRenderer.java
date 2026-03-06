package net.luis.scpmod.command;

import com.mojang.blaze3d.vertex.PoseStack;
import net.luis.scpmod.facility.building.FacilityManager;
import net.luis.scpmod.facility.building.FacilityRoomInstance;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class ZoneDebugRenderer {

    public static boolean SHOW_ZONES = false; // Set true via command

    public static void render(PoseStack poseStack, float partialTick, Camera camera) {
        if (!SHOW_ZONES) return;

        var level = Minecraft.getInstance().level;
        if (level == null) return;

        var player = Minecraft.getInstance().player;
        if (player == null) return;

        Vec3 camPos = camera.getPosition();

        ServerLevel serverLevel = Minecraft.getInstance().getSingleplayerServer().overworld();
        FacilityManager manager = FacilityManager.get(serverLevel);

        for (FacilityRoomInstance room : manager.getRooms()) {
            if (room.blueprint.zoneBounds == null) continue;

            for (var entry : room.blueprint.zoneBounds.entrySet()) {
                String zoneName = entry.getKey();
                int[] bounds = entry.getValue();
                if (bounds.length != 6) continue;

                BlockPos min = room.origin.offset(bounds[0], bounds[1], bounds[2]);
                BlockPos max = room.origin.offset(bounds[3], bounds[4], bounds[5]);
                AABB box = new AABB(min, max);

                double x = box.minX - camPos.x;
                double y = box.minY - camPos.y;
                double z = box.minZ - camPos.z;

                int color = zoneName.equalsIgnoreCase("containment") ? Color.RED.getRGB() : Color.BLUE.getRGB();

                Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(net.minecraft.client.renderer.RenderType.lines());

                LevelRenderer.renderLineBox(
                        poseStack,
                        Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(net.minecraft.client.renderer.RenderType.lines()),
                        x, y, z,
                        box.getXsize(), box.getYsize(), box.getZsize(),
                        (color >> 16 & 255) / 255f,
                        (color >> 8 & 255) / 255f,
                        (color & 255) / 255f,
                        1.0f
                );

                // Label position
                double labelX = (box.minX + box.maxX) / 2.0 - camPos.x;
                double labelY = box.maxY + 0.5 - camPos.y;
                double labelZ = (box.minZ + box.maxZ) / 2.0 - camPos.z;

                FormattedCharSequence sequence = Component.literal(zoneName).getVisualOrderText();
                int textWidth = Minecraft.getInstance().font.width(sequence);

                poseStack.pushPose();
                poseStack.translate(labelX, labelY, labelZ);

                // Rotate label to face the camera
                poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());

                // Scale and center the label
                poseStack.scale(0.02f, -0.02f, 0.02f);
                poseStack.translate(-textWidth / 2.0f, 0.0f, 0.0f);

                Minecraft.getInstance().font.drawInBatch(
                        sequence,
                        0.0f, 0.0f,
                        color, // color matches bounding box
                        false,
                        poseStack.last().pose(),
                        (MultiBufferSource) Minecraft.getInstance().renderBuffers().bufferSource(),
                        DisplayMode.NORMAL,
                        0,
                        15728880
                );

                poseStack.popPose();
            }
        }
    }
}
