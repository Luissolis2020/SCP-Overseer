package net.luis.scpmod.entity.client;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class Mat173Model<T extends Entity> extends HierarchicalModel<T> {
	private final ModelPart PlayerHeight;
	private final ModelPart Him;
	private final ModelPart Torso;
	private final ModelPart Faces;
	private final ModelPart bone;
	private final ModelPart SecondSmallTripodLeg;
	private final ModelPart FrontMainTripodLeg;
	private final ModelPart LargeRearTripodLeg;
	private final ModelPart TinyArmLeft1;
	private final ModelPart TinyArmLeft2;
	private final ModelPart ArmBack;
	private final ModelPart LargeArmRight;

	public Mat173Model(ModelPart root) {
		this.PlayerHeight = root.getChild("PlayerHeight");
		this.Him = root.getChild("Him");
		this.Torso = this.Him.getChild("Torso");
		this.Faces = this.Torso.getChild("Faces");
		this.bone = this.Faces.getChild("bone");
		this.SecondSmallTripodLeg = this.Him.getChild("SecondSmallTripodLeg");
		this.FrontMainTripodLeg = this.Him.getChild("FrontMainTripodLeg");
		this.LargeRearTripodLeg = this.Him.getChild("LargeRearTripodLeg");
		this.TinyArmLeft1 = this.Him.getChild("TinyArmLeft1");
		this.TinyArmLeft2 = this.Him.getChild("TinyArmLeft2");
		this.ArmBack = this.Him.getChild("ArmBack");
		this.LargeArmRight = this.Him.getChild("LargeArmRight");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition PlayerHeight = partdefinition.addOrReplaceChild("PlayerHeight", CubeListBuilder.create().texOffs(-28, -14).addBox(-8.0F, -32.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(-28, -14).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(-16.0F, 24.0F, 16.0F));

		PartDefinition Him = partdefinition.addOrReplaceChild("Him", CubeListBuilder.create(), PartPose.offset(0.0F, 23.0F, -9.0F));

		PartDefinition LowerAbdomen_r1 = Him.addOrReplaceChild("LowerAbdomen_r1", CubeListBuilder.create().texOffs(0, 36).addBox(-5.0F, -4.0F, -4.0F, 7.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, -15.0F, 8.0F, -2.9275F, -1.1688F, 2.9178F));

		PartDefinition Torso = Him.addOrReplaceChild("Torso", CubeListBuilder.create(), PartPose.offset(-1.0F, -18.0F, 9.9F));

		PartDefinition LowerTorso_r1 = Torso.addOrReplaceChild("LowerTorso_r1", CubeListBuilder.create().texOffs(1, 19).addBox(-4.0F, -12.0F, -6.0F, 9.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.8F, 0.0F, 0.3F, -0.0991F, -0.5648F, 0.1034F));

		PartDefinition LowerTorso_r2 = Torso.addOrReplaceChild("LowerTorso_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -6.0F, 8.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.3F, 0.0F, -0.3491F, 0.0F));

		PartDefinition Faces = Torso.addOrReplaceChild("Faces", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Face4_r1 = Faces.addOrReplaceChild("Face4_r1", CubeListBuilder.create().texOffs(36, 18).addBox(-3.0F, -5.0F, -3.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.4F, -10.0F, 0.0F, -0.1879F, -0.7881F, -0.0401F));

		PartDefinition Face3_r1 = Faces.addOrReplaceChild("Face3_r1", CubeListBuilder.create().texOffs(40, 0).addBox(-3.0F, -4.0F, -5.0F, 6.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4F, -6.0F, 6.9F, -0.0229F, -0.1289F, 0.176F));

		PartDefinition Face2_r1 = Faces.addOrReplaceChild("Face2_r1", CubeListBuilder.create().texOffs(30, 36).addBox(-3.0F, -5.0F, -3.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.6F, -7.8F, -3.0F, 0.0F, 0.1745F, 0.0F));

		PartDefinition bone = Faces.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(3.6F, -5.5F, -4.5F));

		PartDefinition Face1_r1 = bone.addOrReplaceChild("Face1_r1", CubeListBuilder.create().texOffs(0, 50).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.163F, -0.5899F, 0.2594F));

		PartDefinition SecondSmallTripodLeg = Him.addOrReplaceChild("SecondSmallTripodLeg", CubeListBuilder.create().texOffs(46, 69).addBox(-0.5F, 11.0F, -6.25F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -14.0F, 6.0F, 0.0F, -0.829F, 0.0F));

		PartDefinition FrontMidLegSmall_r1 = SecondSmallTripodLeg.addOrReplaceChild("FrontMidLegSmall_r1", CubeListBuilder.create().texOffs(66, 6).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, -4.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition FrontUpperLegSmall_r1 = SecondSmallTripodLeg.addOrReplaceChild("FrontUpperLegSmall_r1", CubeListBuilder.create().texOffs(62, 62).addBox(-1.0F, -3.0F, -2.0F, 2.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, -1.0F, -0.6109F, 0.0F, 0.0F));

		PartDefinition FrontMainTripodLeg = Him.addOrReplaceChild("FrontMainTripodLeg", CubeListBuilder.create().texOffs(66, 50).addBox(-1.0F, -3.0F, -1.3F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.0873F, 0.0F));

		PartDefinition FrontMidLegLarge_r1 = FrontMainTripodLeg.addOrReplaceChild("FrontMidLegLarge_r1", CubeListBuilder.create().texOffs(64, 14).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 1.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition FrontUpperLeg_r1 = FrontMainTripodLeg.addOrReplaceChild("FrontUpperLeg_r1", CubeListBuilder.create().texOffs(58, 32).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -12.0F, 4.0F, -0.6109F, 0.0F, 0.0F));

		PartDefinition LargeRearTripodLeg = Him.addOrReplaceChild("LargeRearTripodLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 9.0F));

		PartDefinition MidBackTri_r1 = LargeRearTripodLeg.addOrReplaceChild("MidBackTri_r1", CubeListBuilder.create().texOffs(34, 62).addBox(-3.0F, -5.0F, -2.0F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.7F, -3.0F, 7.2F, 3.1375F, -1.3088F, -3.1413F));

		PartDefinition MidBackTri_r2 = LargeRearTripodLeg.addOrReplaceChild("MidBackTri_r2", CubeListBuilder.create().texOffs(18, 62).addBox(-3.0F, -4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -8.0F, 6.7F, 2.8236F, -1.2947F, -2.8159F));

		PartDefinition UpperBackTri_r1 = LargeRearTripodLeg.addOrReplaceChild("UpperBackTri_r1", CubeListBuilder.create().texOffs(24, 50).addBox(-5.0F, -4.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -14.0F, 6.5F, 2.564F, -1.2565F, -2.5448F));

		PartDefinition TinyArmLeft1 = Him.addOrReplaceChild("TinyArmLeft1", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 9.5F));

		PartDefinition cube_r1 = TinyArmLeft1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(50, 14).addBox(-1.0F, -1.0F, -1.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.8F, -18.0F, 0.2F, 0.0F, 0.0F, 0.48F));

		PartDefinition cube_r2 = TinyArmLeft1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(66, 0).addBox(-2.0F, -3.0F, -2.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.4F, -17.4F, 0.3F, 0.0F, 0.0F, -0.0436F));

		PartDefinition cube_r3 = TinyArmLeft1.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(36, 32).addBox(-1.0F, -2.0F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -18.0F, -0.3F, 0.0F, 0.0F, 0.1745F));

		PartDefinition TinyArmLeft2 = Him.addOrReplaceChild("TinyArmLeft2", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 13.1F));

		PartDefinition cube_r4 = TinyArmLeft2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(50, 16).addBox(-2.0F, -1.0F, 0.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.3F, -18.6F, -0.1F, -0.0814F, -0.2947F, 0.0994F));

		PartDefinition cube_r5 = TinyArmLeft2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(40, 14).addBox(-2.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -18.0F, -0.3F, 0.0F, -0.3054F, -0.1745F));

		PartDefinition ArmBack = Him.addOrReplaceChild("ArmBack", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.3F, -18.2F, 17.4F, -0.7149F, -1.2447F, 0.7418F));

		PartDefinition cube_r6 = ArmBack.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(64, 29).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.9F, 1.2F, 0.9F, -0.2F, -0.2324F, 0.547F));

		PartDefinition cube_r7 = ArmBack.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(64, 23).addBox(-2.0F, -4.0F, -2.0F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1F, 1.6F, 0.5F, 0.0F, -0.3054F, -0.1745F));

		PartDefinition cube_r8 = ArmBack.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(66, 56).addBox(-2.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6F, 0.8F, 0.8F, -0.0814F, -0.2947F, 0.0994F));

		PartDefinition LargeArmRight = Him.addOrReplaceChild("LargeArmRight", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.1F, -18.7F, 9.2F, 0.0F, 0.0F, -0.1309F));

		PartDefinition LargeArmBase_r1 = LargeArmRight.addOrReplaceChild("LargeArmBase_r1", CubeListBuilder.create().texOffs(58, 45).addBox(-5.0F, -2.0F, -2.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.3F, 0.9F, -2.6F, 0.1585F, -0.0735F, -1.1403F));

		PartDefinition LargeArmBase_r2 = LargeArmRight.addOrReplaceChild("LargeArmBase_r2", CubeListBuilder.create().texOffs(46, 62).addBox(-4.0F, -3.0F, -3.0F, 5.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.1F, 0.5F, -1.8F, 0.1186F, -0.1284F, -0.7494F));

		PartDefinition LargeArmBase_r3 = LargeArmRight.addOrReplaceChild("LargeArmBase_r3", CubeListBuilder.create().texOffs(0, 62).addBox(-3.0F, -4.0F, -4.0F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, -0.9F, 0.0812F, -0.1546F, -0.4863F));

		PartDefinition LargeArmBase_r4 = LargeArmRight.addOrReplaceChild("LargeArmBase_r4", CubeListBuilder.create().texOffs(48, 50).addBox(-2.0F, -5.0F, -5.0F, 4.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0154F, -0.1739F, -0.0886F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.Him.yRot = (float) Math.toRadians(netHeadYaw); // Rotate SCP-173's body
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Him.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return Him;
	}
}