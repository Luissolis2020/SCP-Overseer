package net.luis.scpmod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.luis.scpmod.facility.building.FacilityBlueprintLoader;
import net.luis.scpmod.mixin.StructureTemplateAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class GhostStructureRenderer {

    public static void renderPreview(PoseStack poseStack, MultiBufferSource buffer,
                                     BlockPos origin, StructureTemplate template, StructurePlaceSettings settings) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        // camera
        double camX = mc.gameRenderer.getMainCamera().getPosition().x;
        double camY = mc.gameRenderer.getMainCamera().getPosition().y;
        double camZ = mc.gameRenderer.getMainCamera().getPosition().z;

        // anchor to world
        poseStack.pushPose();
        poseStack.translate(origin.getX() - camX, origin.getY() - camY, origin.getZ() - camZ);

        if (template != null) {
            BlockRenderDispatcher blockRenderer = mc.getBlockRenderer();
            StructureTemplateAccessor accessor = (StructureTemplateAccessor) template;

            var palettes = accessor.getPalettes();
            if (palettes.isEmpty() || palettes.get(0).blocks().isEmpty()) {
                // no blocks to render, still show bbox so user can place
                renderBoundingBoxFromBlueprintLocal(poseStack, buffer, settings);
                poseStack.popPose();
                renderOverlapBoundingBoxes(poseStack, buffer);
                return;
            }

            List<StructureTemplate.StructureBlockInfo> blocks = palettes.get(0).blocks();

            for (StructureTemplate.StructureBlockInfo blockInfo : blocks) {
                BlockState state = blockInfo.state();
                if (state.isAir()) continue;

                // Match vanilla structure placement behavior: transform blockstate props
                // (facing/half/part/etc.) with the active preview mirror+rotation.
                state = state.mirror(settings.getMirror()).rotate(settings.getRotation());

                BlockPos local = StructureTemplate.calculateRelativePosition(settings, blockInfo.pos());

                poseStack.pushPose();
                poseStack.translate(local.getX(), local.getY(), local.getZ());

                blockRenderer.renderSingleBlock(state, poseStack, buffer, 15728640, 0);
                poseStack.popPose();
            }

            renderBoundingBoxLocal(poseStack, buffer, template, settings);
        } else {
            // dedicated server / no client template: draw bbox from blueprint size
            renderBoundingBoxFromBlueprintLocal(poseStack, buffer, settings);
        }

        poseStack.popPose();

        // overlaps are world-space AABBs; render them separately (camera-relative)
        renderOverlapBoundingBoxes(poseStack, buffer);
    }


    private static void renderBoundingBoxLocal(PoseStack poseStack, MultiBufferSource buffer, StructureTemplate template, StructurePlaceSettings settings) {
        Vec3i size = template.getSize();
        BlockPos sizePos = new BlockPos(size.getX(), size.getY(), size.getZ());

        // Transform the min/max corners like Minecraft does
        BlockPos min = StructureTemplate.transform(BlockPos.ZERO, settings.getMirror(), settings.getRotation(), BlockPos.ZERO);
        BlockPos max = StructureTemplate.transform(sizePos.offset(-1, -1, -1), settings.getMirror(), settings.getRotation(), BlockPos.ZERO);

        int minX = Math.min(min.getX(), max.getX());
        int minY = Math.min(min.getY(), max.getY());
        int minZ = Math.min(min.getZ(), max.getZ());

        int maxX = Math.max(min.getX(), max.getX()) + 1;
        int maxY = Math.max(min.getY(), max.getY()) + 1;
        int maxZ = Math.max(min.getZ(), max.getZ()) + 1;

        AABB box = new AABB(minX, minY, minZ, maxX, maxY, maxZ);

        LevelRenderer.renderLineBox(
                poseStack,
                buffer.getBuffer(RenderType.lines()),
                box,
                0.0f, 1.0f, 1.0f, 1.0f // cyan
        );
    }


    private static void renderOverlapBoundingBoxes(PoseStack poseStack, MultiBufferSource buffer) {
        Minecraft mc = Minecraft.getInstance();
        double camX = mc.gameRenderer.getMainCamera().getPosition().x;
        double camY = mc.gameRenderer.getMainCamera().getPosition().y;
        double camZ = mc.gameRenderer.getMainCamera().getPosition().z;

        for (AABB worldAabb : ClientPreviewState.getServerOverlaps()) {
            AABB aabb = worldAabb.move(-camX, -camY, -camZ);
            LevelRenderer.renderLineBox(poseStack, buffer.getBuffer(RenderType.lines()), aabb, 0.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    private static void renderBoundingBoxFromBlueprintLocal(PoseStack poseStack, MultiBufferSource buffer, StructurePlaceSettings settings) {
        var id = ClientPreviewState.getStructureId();
        var bp = FacilityBlueprintLoader.get(id);
        if (bp == null) return;

        int w = bp.width;
        int h = bp.height;
        int d = bp.depth;

        int maxX, maxZ;
        switch (settings.getRotation()) {
            case CLOCKWISE_90, COUNTERCLOCKWISE_90 -> { maxX = d; maxZ = w; }
            default -> { maxX = w; maxZ = d; }
        }

        AABB box = new AABB(0, 0, 0, maxX, h, maxZ);
        LevelRenderer.renderLineBox(poseStack, buffer.getBuffer(RenderType.lines()), box, 0.0f, 1.0f, 1.0f, 1.0f);
    }

}

