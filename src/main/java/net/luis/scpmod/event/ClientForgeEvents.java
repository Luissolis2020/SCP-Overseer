package net.luis.scpmod.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.luis.scpmod.SCPMod;
import net.luis.scpmod.client.BuildPreviewState;
import net.luis.scpmod.client.CameraScreenshotScheduler;
import net.luis.scpmod.client.ClientPreviewState;
import net.luis.scpmod.client.FacilityConnectorSocketRenderer;
import net.luis.scpmod.client.GhostStructureRenderer;
import net.luis.scpmod.client.gui.BuildingScreen;
import net.luis.scpmod.command.ZoneDebugRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = SCPMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents {

    // Store locked rotation when building placement starts
    private static float lockedYaw = 0.0F;
    private static float lockedPitch = 0.0F;

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) return;
        if (!ClientPreviewState.isActive()) return;

        Minecraft mc = Minecraft.getInstance();
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();

        GhostStructureRenderer.renderPreview(
                poseStack,
                buffer,
                ClientPreviewState.getPreviewPos(),
                ClientPreviewState.getTemplate(),
                ClientPreviewState.getSettings()
        );

        buffer.endBatch(); // Force drawing
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            ZoneDebugRenderer.render(event.getPoseStack(), event.getPartialTick(), event.getCamera());
            FacilityConnectorSocketRenderer.render(event);
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (ClientPreviewState.isActive()) {
            if (event.getKey() == GLFW.GLFW_KEY_ESCAPE && event.getAction() == GLFW.GLFW_PRESS) {
                if (ClientPreviewState.getPreviewState() == BuildPreviewState.PLACEMENT_LOCKED) {
                    ClientPreviewState.setPreviewState(BuildPreviewState.FREE_VIEW);
                } else if (ClientPreviewState.getPreviewState() == BuildPreviewState.FREE_VIEW) {
                    ClientPreviewState.deactivate();
                    mc.mouseHandler.grabMouse();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        CameraScreenshotScheduler.tick();
        boolean isLockedState = ClientPreviewState.getPreviewState() == BuildPreviewState.PLACEMENT_LOCKED;
        boolean isBuildScreenOpen = mc.screen instanceof net.luis.scpmod.client.gui.BuildingScreen;

        if (isLockedState && isBuildScreenOpen) {
            // Lock view only while the build screen is open
            if (lockedYaw == 0.0F && lockedPitch == 0.0F) {
                lockedYaw = mc.player.getYRot();
                lockedPitch = mc.player.getXRot();
            }

            mc.player.input.forwardImpulse = 0.0F;
            mc.player.input.leftImpulse = 0.0F;
            mc.player.input.jumping = false;
            mc.player.setSprinting(false);

            mc.player.setYRot(lockedYaw);
            mc.player.setXRot(lockedPitch);
            mc.player.yHeadRot = lockedYaw;
            mc.player.yBodyRot = lockedYaw;
        } else {
            // Player is no longer locked — clean up!
            lockedYaw = 0.0F;
            lockedPitch = 0.0F;
        }

        if (ClientPreviewState.getPreviewState() == BuildPreviewState.FREE_VIEW) {
            double distanceSq = mc.player.position().distanceToSqr(
                    ClientPreviewState.getPreviewPos().getX() + 0.5,
                    ClientPreviewState.getPreviewPos().getY() + 0.5,
                    ClientPreviewState.getPreviewPos().getZ() + 0.5
            );

            double maxDistanceSq = 100.0;

            if (distanceSq > maxDistanceSq) {
                mc.player.sendSystemMessage(net.minecraft.network.chat.Component.literal("Facility preview canceled: too far away."));
                ClientPreviewState.deactivate();
            }
        }
    }

}
