package net.luis.scpmod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.luis.scpmod.client.ClientPDADataCache;
import net.luis.scpmod.network.ClientboundFacilityRoomsPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = net.minecraftforge.api.distmarker.Dist.CLIENT, modid = "scpmod")
public class RoomHighlightRenderer {

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;
        HighlightRenderData.tick();

        if (!HighlightRenderData.isHighlightActive()) return;

        BlockPos pos = HighlightRenderData.getHighlightPos();
        ClientboundFacilityRoomsPacket.FacilityRoomData room = ClientPDADataCache.getRoomAt(pos);
        if (room == null) return;

        BlockPos origin = room.origin();
        int width = room.width();
        int height = room.height();
        int depth = room.depth();

        BlockPos end = origin.offset(width, height, depth);
        AABB box = new AABB(origin, end).inflate(-0.01); // shrink slightly to prevent Z-fighting

        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();

        double camX = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().x;
        double camY = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().y;
        double camZ = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().z;

        poseStack.translate(-camX, -camY, -camZ);

        LevelRenderer.renderLineBox(
                poseStack,
                Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines()),
                box,
                0.0F, 1.0F, 1.0F, // Cyan
                1.0F
        );

        poseStack.popPose();
    }
}
