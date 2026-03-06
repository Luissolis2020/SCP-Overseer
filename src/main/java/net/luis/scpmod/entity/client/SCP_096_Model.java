package net.luis.scpmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.luis.scpmod.SCPMod;
import net.luis.scpmod.entity.animations.ModAnimationDefinitions;
import net.luis.scpmod.entity.custom.SCP_096_Entity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedGeoModel;

public class SCP_096_Model extends DefaultedGeoModel<SCP_096_Entity> {

	public SCP_096_Model() {
		super(ResourceLocation.fromNamespaceAndPath(SCPMod.MOD_ID, "scp_096"));
	}

	@Override
	protected String subtype() {
		return "entity";
	}
}