package net.luis.scpmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.luis.scpmod.entity.custom.SCP_999_Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class SCP_999Renderer extends GeoEntityRenderer<SCP_999_Entity> {
    private static final ResourceLocation BASE  =
            ResourceLocation.fromNamespaceAndPath("scpmod","textures/entity/scp_999.png");
    private static final ResourceLocation SHELL =
            ResourceLocation.fromNamespaceAndPath("scpmod","textures/entity/scp_999_shell.png");

    public SCP_999Renderer(EntityRendererProvider.Context ctx) {
        super(ctx, new SCP_999_Model());

        this.addRenderLayer(new GeoRenderLayer<SCP_999_Entity>(this) {
            @Override
            public void render(PoseStack poseStack,
                               SCP_999_Entity animatable,
                               BakedGeoModel model,
                               RenderType renderType,
                               MultiBufferSource bufferSource,
                               VertexConsumer buffer,
                               float partialTick,
                               int packedLight,
                               int packedOverlay) {

                RenderType shellType = RenderType.entityTranslucent(SHELL);
                VertexConsumer shellBuf = bufferSource.getBuffer(shellType);

                float a = animatable.getShellAlpha();
                getRenderer().reRender(
                        model, poseStack, bufferSource, animatable,
                        shellType, shellBuf,
                        partialTick, packedLight, packedOverlay,
                        1f, 1f, 1f, a
                );
            }
        });
    }

    @Override
    public RenderType getRenderType(SCP_999_Entity animatable,
                                    ResourceLocation texture,
                                    MultiBufferSource bufferSource,
                                    float partialTick) {
        return RenderType.entityCutout(BASE);
    }
}
