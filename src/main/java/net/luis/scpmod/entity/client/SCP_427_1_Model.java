package net.luis.scpmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.luis.scpmod.entity.animations.ModAnimationDefinitions;
import net.luis.scpmod.entity.custom.SCP427_1_Entity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class SCP_427_1_Model<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart fleash_beast;
	private final ModelPart Torso;
	private final ModelPart head;
	private final ModelPart upper_teeth;
	private final ModelPart toung;
	private final ModelPart lick;
	private final ModelPart jaw;
	private final ModelPart teeth;
	private final ModelPart arms;
	private final ModelPart l_arm;
	private final ModelPart base;
	private final ModelPart forearm;
	private final ModelPart arm;
	private final ModelPart hand;
	private final ModelPart r_arm;
	private final ModelPart r_base;
	private final ModelPart r_forearm;
	private final ModelPart r_arm2;
	private final ModelPart r_hand;
	private final ModelPart warts;
	private final ModelPart small;
	private final ModelPart med;
	private final ModelPart large;
	private final ModelPart tentcals;
	private final ModelPart l_tenticals;
	private final ModelPart tentical_1;
	private final ModelPart upper_tentical_1;
	private final ModelPart mid_tentical_1;
	private final ModelPart lower_tentical_1;
	private final ModelPart tentical_2;
	private final ModelPart upper_tentical_2;
	private final ModelPart mid_tentical_2;
	private final ModelPart lower_tentical_2;
	private final ModelPart tentical_3;
	private final ModelPart upper_tentical_3;
	private final ModelPart mid_tentical_3;
	private final ModelPart lower_tentical_3;
	private final ModelPart r_tenticals;
	private final ModelPart tentical_4;
	private final ModelPart upper_tentical_4;
	private final ModelPart mid_tentical_4;
	private final ModelPart lower_tentical_4;
	private final ModelPart tentical_5;
	private final ModelPart upper_tentical_5;
	private final ModelPart mid_tentical_5;
	private final ModelPart lower_tetical_5;
	private final ModelPart tentical_6;
	private final ModelPart upper_tentical_6;
	private final ModelPart mid_tentical_6;
	private final ModelPart lower_tentical_6;
	private final ModelPart r_leg;
	private final ModelPart l_leg;

	public SCP_427_1_Model(ModelPart root) {
		this.fleash_beast = root.getChild("fleash_beast");
		this.Torso = this.fleash_beast.getChild("Torso");
		this.head = this.Torso.getChild("head");
		this.upper_teeth = this.head.getChild("upper_teeth");
		this.toung = this.head.getChild("toung");
		this.lick = this.toung.getChild("lick");
		this.jaw = this.head.getChild("jaw");
		this.teeth = this.jaw.getChild("teeth");
		this.arms = this.Torso.getChild("arms");
		this.l_arm = this.arms.getChild("l_arm");
		this.base = this.l_arm.getChild("base");
		this.forearm = this.base.getChild("forearm");
		this.arm = this.forearm.getChild("arm");
		this.hand = this.arm.getChild("hand");
		this.r_arm = this.arms.getChild("r_arm");
		this.r_base = this.r_arm.getChild("r_base");
		this.r_forearm = this.r_base.getChild("r_forearm");
		this.r_arm2 = this.r_forearm.getChild("r_arm2");
		this.r_hand = this.r_arm2.getChild("r_hand");
		this.warts = this.Torso.getChild("warts");
		this.small = this.warts.getChild("small");
		this.med = this.warts.getChild("med");
		this.large = this.warts.getChild("large");
		this.tentcals = this.Torso.getChild("tentcals");
		this.l_tenticals = this.tentcals.getChild("l_tenticals");
		this.tentical_1 = this.l_tenticals.getChild("tentical_1");
		this.upper_tentical_1 = this.tentical_1.getChild("upper_tentical_1");
		this.mid_tentical_1 = this.upper_tentical_1.getChild("mid_tentical_1");
		this.lower_tentical_1 = this.mid_tentical_1.getChild("lower_tentical_1");
		this.tentical_2 = this.l_tenticals.getChild("tentical_2");
		this.upper_tentical_2 = this.tentical_2.getChild("upper_tentical_2");
		this.mid_tentical_2 = this.upper_tentical_2.getChild("mid_tentical_2");
		this.lower_tentical_2 = this.mid_tentical_2.getChild("lower_tentical_2");
		this.tentical_3 = this.l_tenticals.getChild("tentical_3");
		this.upper_tentical_3 = this.tentical_3.getChild("upper_tentical_3");
		this.mid_tentical_3 = this.upper_tentical_3.getChild("mid_tentical_3");
		this.lower_tentical_3 = this.mid_tentical_3.getChild("lower_tentical_3");
		this.r_tenticals = this.tentcals.getChild("r_tenticals");
		this.tentical_4 = this.r_tenticals.getChild("tentical_4");
		this.upper_tentical_4 = this.tentical_4.getChild("upper_tentical_4");
		this.mid_tentical_4 = this.upper_tentical_4.getChild("mid_tentical_4");
		this.lower_tentical_4 = this.mid_tentical_4.getChild("lower_tentical_4");
		this.tentical_5 = this.r_tenticals.getChild("tentical_5");
		this.upper_tentical_5 = this.tentical_5.getChild("upper_tentical_5");
		this.mid_tentical_5 = this.upper_tentical_5.getChild("mid_tentical_5");
		this.lower_tetical_5 = this.mid_tentical_5.getChild("lower_tetical_5");
		this.tentical_6 = this.r_tenticals.getChild("tentical_6");
		this.upper_tentical_6 = this.tentical_6.getChild("upper_tentical_6");
		this.mid_tentical_6 = this.upper_tentical_6.getChild("mid_tentical_6");
		this.lower_tentical_6 = this.mid_tentical_6.getChild("lower_tentical_6");
		this.r_leg = this.fleash_beast.getChild("r_leg");
		this.l_leg = this.fleash_beast.getChild("l_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition fleash_beast = partdefinition.addOrReplaceChild("fleash_beast", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Torso = fleash_beast.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(0, 70).addBox(-7.0F, -12.0F, -11.0F, 13.0F, 7.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(72, 50).addBox(-8.0F, 11.0F, 0.0F, 16.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(62, 70).addBox(-9.0F, 8.0F, -2.0F, 18.0F, 3.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(0, 50).addBox(-10.0F, 4.0F, -4.0F, 20.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-11.0F, 0.0F, -7.0F, 22.0F, 4.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(0, 24).addBox(-9.0F, -5.0F, -10.0F, 18.0F, 5.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -31.0F, -5.0F));

		PartDefinition head = Torso.addOrReplaceChild("head", CubeListBuilder.create().texOffs(78, 24).addBox(-5.0F, -4.5F, -4.5F, 10.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -9.5F, -14.5F));

		PartDefinition upper_teeth = head.addOrReplaceChild("upper_teeth", CubeListBuilder.create().texOffs(0, 126).addBox(-3.75F, 3.0F, -8.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(126, 4).addBox(-4.75F, 3.0F, -7.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(126, 6).addBox(-4.75F, 3.0F, -5.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 126).addBox(-4.75F, 3.0F, -3.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(126, 8).addBox(-4.75F, 3.0F, -1.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(126, 10).addBox(3.25F, 3.0F, -1.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 126).addBox(3.25F, 3.0F, -3.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 126).addBox(3.25F, 3.0F, -5.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 126).addBox(3.25F, 3.0F, -7.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(126, 0).addBox(-1.6F, 3.0F, -8.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(126, 2).addBox(0.45F, 3.0F, -8.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 126).addBox(2.55F, 3.0F, -8.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, 4.0F));

		PartDefinition toung = head.addOrReplaceChild("toung", CubeListBuilder.create().texOffs(84, 20).addBox(-2.0F, -0.5F, 0.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.1F, 1.0F));

		PartDefinition lick = toung.addOrReplaceChild("lick", CubeListBuilder.create().texOffs(88, 115).addBox(0.0F, -2.0F, 1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(116, 12).addBox(0.0F, -2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 2.0F, -3.0F));

		PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(84, 0).addBox(-5.0F, -0.5F, -9.5F, 10.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 5.0F));

		PartDefinition teeth = jaw.addOrReplaceChild("teeth", CubeListBuilder.create().texOffs(122, 9).addBox(-4.75F, 1.0F, -8.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(122, 37).addBox(-2.75F, 1.0F, -8.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(122, 40).addBox(-0.5F, 1.0F, -8.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(122, 43).addBox(1.5F, 1.0F, -8.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(122, 46).addBox(3.5F, 1.0F, -8.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(122, 62).addBox(3.5F, 1.0F, -6.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(64, 122).addBox(3.5F, 1.0F, -4.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(122, 65).addBox(3.5F, 1.0F, -2.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(68, 122).addBox(3.5F, 1.0F, -0.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(122, 28).addBox(-4.75F, 1.0F, -6.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(122, 31).addBox(-4.75F, 1.0F, -4.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(122, 34).addBox(-4.75F, 1.0F, -2.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(72, 122).addBox(-4.75F, 1.0F, -0.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -1.0F));

		PartDefinition arms = Torso.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offset(-6.0F, 4.0F, 2.0F));

		PartDefinition l_arm = arms.addOrReplaceChild("l_arm", CubeListBuilder.create(), PartPose.offset(19.6239F, -1.2604F, -0.779F));

		PartDefinition base = l_arm.addOrReplaceChild("base", CubeListBuilder.create().texOffs(104, 62).addBox(-2.6239F, -2.7396F, -0.221F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition forearm = base.addOrReplaceChild("forearm", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = forearm.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(104, 113).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.2761F, 1.2604F, 0.779F, -0.3491F, 0.0F, -0.6545F));

		PartDefinition arm = forearm.addOrReplaceChild("arm", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r2 = arm.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(82, 119).addBox(-1.0F, -3.0F, -1.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.2761F, -0.0396F, -1.721F, -2.2765F, -0.1334F, -0.5416F));

		PartDefinition hand = arm.addOrReplaceChild("hand", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r3 = hand.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(120, 121).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.2261F, 0.6604F, -2.221F, -0.6561F, 0.1641F, -0.1635F));

		PartDefinition r_arm = arms.addOrReplaceChild("r_arm", CubeListBuilder.create(), PartPose.offset(-7.6239F, -1.2604F, -0.779F));

		PartDefinition r_base = r_arm.addOrReplaceChild("r_base", CubeListBuilder.create().texOffs(104, 62).mirror().addBox(0.6239F, -2.7396F, -0.221F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition r_forearm = r_base.addOrReplaceChild("r_forearm", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r4 = r_forearm.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(104, 113).mirror().addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.2761F, 1.2604F, 0.779F, -0.3491F, 0.0F, 0.6545F));

		PartDefinition r_arm2 = r_forearm.addOrReplaceChild("r_arm2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r5 = r_arm2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(82, 119).mirror().addBox(0.0F, -3.0F, -1.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.2761F, -0.0396F, -1.721F, -2.2765F, 0.1334F, 0.5416F));

		PartDefinition r_hand = r_arm2.addOrReplaceChild("r_hand", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r6 = r_hand.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(120, 121).mirror().addBox(0.0F, -1.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.2261F, 0.6604F, -2.221F, -0.6561F, -0.1641F, 0.1635F));

		PartDefinition warts = Torso.addOrReplaceChild("warts", CubeListBuilder.create(), PartPose.offsetAndRotation(9.0F, -2.0F, -22.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition small = warts.addOrReplaceChild("small", CubeListBuilder.create().texOffs(124, 107).addBox(9.0F, 9.0F, 17.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(122, 68).addBox(11.0F, 8.0F, 19.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(60, 125).addBox(12.0F, 3.0F, 19.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(64, 125).addBox(10.0F, 14.0F, 19.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(68, 125).addBox(7.0F, 2.0F, 17.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(72, 125).addBox(7.0F, 9.0F, 17.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(76, 125).addBox(5.0F, 19.0F, 14.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(122, 95).addBox(7.0F, 19.0F, 22.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(122, 97).addBox(7.0F, 19.0F, 31.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(122, 99).addBox(9.0F, 16.0F, 34.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 123).addBox(2.0F, 1.0F, 35.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 125).addBox(4.0F, 5.0F, 35.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 125).addBox(9.0F, 1.0F, 34.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 125).addBox(8.0F, 5.0F, 34.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(36, 125).addBox(8.0F, 11.0F, 34.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(40, 125).addBox(8.0F, 12.0F, 34.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(44, 125).addBox(3.0F, 15.0F, 35.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(48, 125).addBox(5.0F, 17.0F, 35.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(52, 125).addBox(3.0F, 19.0F, 35.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 123).addBox(2.0F, 10.0F, 35.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 123).addBox(0.0F, 10.0F, 33.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(56, 125).addBox(-1.0F, 8.0F, 33.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(36, 123).addBox(0.0F, 4.0F, 33.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(40, 123).addBox(-3.0F, -1.0F, 31.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(44, 123).addBox(-3.0F, -1.0F, 25.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(108, 123).addBox(3.0F, -3.0F, 20.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(84, 125).addBox(9.0F, -2.0F, 20.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(88, 125).addBox(8.0F, -2.0F, 23.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(106, 125).addBox(4.0F, -3.0F, 25.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(110, 125).addBox(4.0F, -3.0F, 16.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(114, 125).addBox(0.0F, -1.0F, 19.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 125).addBox(-1.0F, -1.0F, 22.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(112, 123).addBox(3.0F, -1.0F, 14.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(116, 123).addBox(4.0F, -1.0F, 14.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 124).addBox(4.0F, 6.0F, 14.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(120, 124).addBox(2.0F, 4.0F, 14.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 121).addBox(1.0F, 4.0F, 11.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 123).addBox(0.0F, 1.0F, 11.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 124).addBox(2.0F, 17.0F, 14.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 124).addBox(-2.0F, 18.0F, 14.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 124).addBox(0.0F, 18.0F, 28.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 109).addBox(3.0F, 20.0F, 25.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 111).addBox(4.0F, 20.0F, 31.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 113).addBox(4.0F, 20.0F, 28.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 12).addBox(-4.0F, 11.0F, 30.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 55).addBox(-7.0F, 11.0F, 29.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 57).addBox(-9.0F, 5.0F, 29.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 59).addBox(-11.0F, 13.0F, 26.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 70).addBox(-11.0F, 10.0F, 21.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 72).addBox(-11.0F, 6.0F, 23.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 74).addBox(-11.0F, 6.0F, 19.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 76).addBox(-11.0F, 8.0F, 20.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 78).addBox(-11.0F, 8.0F, 15.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(80, 124).addBox(-11.0F, 4.0F, 16.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 80).addBox(-11.0F, 14.0F, 13.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 82).addBox(-11.0F, 13.0F, 17.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 115).addBox(-10.0F, 16.0F, 17.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 117).addBox(-10.0F, 16.0F, 21.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 119).addBox(-6.0F, 16.0F, 22.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 84).addBox(-11.0F, 3.0F, 21.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(94, 124).addBox(-8.0F, 2.0F, 16.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(98, 124).addBox(-9.0F, 2.0F, 11.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(102, 124).addBox(-6.0F, 2.0F, 14.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 14).addBox(-4.0F, 5.0F, 31.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 124).addBox(-4.0F, 1.0F, 26.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 16).addBox(12.0F, -1.0F, 20.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 18).addBox(14.0F, 2.0F, 21.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 124).addBox(13.0F, 17.0F, 23.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 20).addBox(13.0F, 17.0F, 31.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 22).addBox(14.0F, 15.0F, 32.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 24).addBox(14.0F, 13.0F, 32.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 26).addBox(14.0F, 10.0F, 32.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 49).addBox(13.0F, 9.0F, 32.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 51).addBox(13.0F, 7.0F, 32.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 53).addBox(13.0F, 2.0F, 32.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition med = warts.addOrReplaceChild("med", CubeListBuilder.create().texOffs(116, 28).addBox(5.0F, 12.0F, 13.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(88, 118).addBox(2.0F, 14.0F, 13.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(64, 119).addBox(1.0F, 16.0F, 10.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(70, 119).addBox(-2.0F, 15.0F, 10.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(56, 95).addBox(12.0F, 16.0F, 18.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(56, 98).addBox(-3.0F, 3.0F, 33.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(116, 34).addBox(-2.0F, 17.0F, 33.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(76, 119).addBox(-1.0F, 14.0F, 33.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(116, 37).addBox(-2.0F, 18.0F, 21.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(116, 62).addBox(-5.0F, 16.0F, 13.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(120, 101).addBox(-12.0F, 10.0F, 24.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(120, 103).addBox(-12.0F, 3.0F, 12.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(116, 31).addBox(11.0F, 1.0F, 33.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(122, 3).addBox(-1.0F, -2.0F, 28.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(76, 122).addBox(-7.0F, 1.0F, 25.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(122, 86).addBox(-5.0F, 1.0F, 28.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(122, 6).addBox(3.0F, -4.0F, 28.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(122, 89).addBox(2.0F, -4.0F, 33.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(122, 92).addBox(5.0F, -4.0F, 34.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(116, 40).addBox(10.0F, -1.0F, 30.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(116, 68).addBox(11.0F, -1.0F, 23.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r7 = med.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(116, 65).addBox(-2.0F, -1.0F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 19.0F, 20.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition large = warts.addOrReplaceChild("large", CubeListBuilder.create().texOffs(64, 115).addBox(8.0F, 4.0F, 15.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(72, 115).addBox(-1.0F, 3.0F, 9.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(120, 19).addBox(13.0F, -2.0F, 30.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(120, 117).addBox(8.0F, -4.0F, 25.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(48, 121).addBox(7.0F, -4.0F, 19.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(112, 20).addBox(13.0F, 12.0F, 19.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(96, 20).addBox(7.0F, 14.0F, 15.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(120, 105).addBox(-5.0F, 0.0F, 11.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(116, 15).addBox(-13.0F, 5.0F, 27.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(116, 17).addBox(-13.0F, 13.0F, 21.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(108, 119).addBox(-2.0F, 18.0F, 25.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(120, 113).addBox(0.0F, 18.0F, 20.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(52, 121).addBox(0.0F, 18.0F, 31.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(56, 121).addBox(7.0F, 19.0F, 29.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(60, 121).addBox(8.0F, 19.0F, 24.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(86, 121).addBox(11.0F, 18.0F, 24.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(90, 121).addBox(11.0F, 18.0F, 21.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(104, 20).addBox(4.0F, 9.0F, 35.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(80, 115).addBox(7.0F, 7.0F, 34.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(120, 109).addBox(9.0F, 12.0F, 17.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r8 = large.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(116, 26).addBox(-4.0F, -1.0F, 1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 14.0F, 33.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r9 = large.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(116, 24).addBox(-4.0F, -1.0F, 1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 9.0F, 33.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition tentcals = Torso.addOrReplaceChild("tentcals", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition l_tenticals = tentcals.addOrReplaceChild("l_tenticals", CubeListBuilder.create(), PartPose.offset(0.0F, 31.0F, 5.0F));

		PartDefinition tentical_1 = l_tenticals.addOrReplaceChild("tentical_1", CubeListBuilder.create().texOffs(72, 62).addBox(-2.0F, 12.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(25.0F, -33.0F, -12.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition upper_tentical_1 = tentical_1.addOrReplaceChild("upper_tentical_1", CubeListBuilder.create().texOffs(48, 95).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition mid_tentical_1 = upper_tentical_1.addOrReplaceChild("mid_tentical_1", CubeListBuilder.create().texOffs(96, 101).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, 0.0F));

		PartDefinition lower_tentical_1 = mid_tentical_1.addOrReplaceChild("lower_tentical_1", CubeListBuilder.create().texOffs(24, 112).addBox(-1.0F, -4.5F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.5F, 0.0F));

		PartDefinition tentical_2 = l_tenticals.addOrReplaceChild("tentical_2", CubeListBuilder.create().texOffs(78, 42).addBox(-2.0F, 12.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.0F, -40.0F, -6.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition upper_tentical_2 = tentical_2.addOrReplaceChild("upper_tentical_2", CubeListBuilder.create().texOffs(56, 101).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition mid_tentical_2 = upper_tentical_2.addOrReplaceChild("mid_tentical_2", CubeListBuilder.create().texOffs(104, 101).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, 0.0F));

		PartDefinition lower_tentical_2 = mid_tentical_2.addOrReplaceChild("lower_tentical_2", CubeListBuilder.create().texOffs(32, 112).addBox(-1.0F, -4.5F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.5F, 0.0F));

		PartDefinition tentical_3 = l_tenticals.addOrReplaceChild("tentical_3", CubeListBuilder.create().texOffs(84, 12).addBox(-2.0F, 12.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(26.0F, -27.0F, 4.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition upper_tentical_3 = tentical_3.addOrReplaceChild("upper_tentical_3", CubeListBuilder.create().texOffs(64, 101).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition mid_tentical_3 = upper_tentical_3.addOrReplaceChild("mid_tentical_3", CubeListBuilder.create().texOffs(48, 109).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, 0.0F));

		PartDefinition lower_tentical_3 = mid_tentical_3.addOrReplaceChild("lower_tentical_3", CubeListBuilder.create().texOffs(40, 112).addBox(-1.0F, -4.5F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.5F, 0.0F));

		PartDefinition r_tenticals = tentcals.addOrReplaceChild("r_tenticals", CubeListBuilder.create(), PartPose.offset(0.0F, 31.0F, 5.0F));

		PartDefinition tentical_4 = r_tenticals.addOrReplaceChild("tentical_4", CubeListBuilder.create().texOffs(88, 62).addBox(-2.0F, 12.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-25.0F, -32.0F, -9.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition upper_tentical_4 = tentical_4.addOrReplaceChild("upper_tentical_4", CubeListBuilder.create().texOffs(72, 101).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition mid_tentical_4 = upper_tentical_4.addOrReplaceChild("mid_tentical_4", CubeListBuilder.create().texOffs(0, 112).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, 0.0F));

		PartDefinition lower_tentical_4 = mid_tentical_4.addOrReplaceChild("lower_tentical_4", CubeListBuilder.create().texOffs(112, 101).addBox(-1.0F, -4.5F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.5F, 0.0F));

		PartDefinition tentical_5 = r_tenticals.addOrReplaceChild("tentical_5", CubeListBuilder.create().texOffs(94, 42).addBox(-2.0F, 12.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-22.0F, -37.0F, -2.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition upper_tentical_5 = tentical_5.addOrReplaceChild("upper_tentical_5", CubeListBuilder.create().texOffs(80, 101).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition mid_tentical_5 = upper_tentical_5.addOrReplaceChild("mid_tentical_5", CubeListBuilder.create().texOffs(8, 112).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, 0.0F));

		PartDefinition lower_tetical_5 = mid_tentical_5.addOrReplaceChild("lower_tetical_5", CubeListBuilder.create().texOffs(112, 112).addBox(-1.0F, -4.5F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.5F, 0.0F));

		PartDefinition tentical_6 = r_tenticals.addOrReplaceChild("tentical_6", CubeListBuilder.create().texOffs(100, 12).addBox(-2.0F, 12.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-25.0F, -23.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition upper_tentical_6 = tentical_6.addOrReplaceChild("upper_tentical_6", CubeListBuilder.create().texOffs(88, 101).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition mid_tentical_6 = upper_tentical_6.addOrReplaceChild("mid_tentical_6", CubeListBuilder.create().texOffs(16, 112).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, 0.0F));

		PartDefinition lower_tentical_6 = mid_tentical_6.addOrReplaceChild("lower_tentical_6", CubeListBuilder.create().texOffs(96, 113).addBox(-1.0F, -4.5F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.5F, 0.0F));

		PartDefinition r_leg = fleash_beast.addOrReplaceChild("r_leg", CubeListBuilder.create().texOffs(92, 84).addBox(-3.5F, -5.5F, -4.0F, 7.0F, 9.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 95).addBox(-2.5F, 3.5F, -3.0F, 6.0F, 11.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, -14.5F, 0.0F));

		PartDefinition l_leg = fleash_beast.addOrReplaceChild("l_leg", CubeListBuilder.create().texOffs(62, 84).addBox(-3.5F, -5.5F, -4.0F, 7.0F, 9.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(24, 95).addBox(-3.5F, 3.5F, -3.0F, 6.0F, 11.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, -14.5F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		SCP427_1_Entity mob = (SCP427_1_Entity) entity;

		if (mob.isAttacking()) {
			this.animate(mob.attackAnimationState, ModAnimationDefinitions.flesh_monsterAnimation.attack, ageInTicks, 1f);
		} else if (limbSwingAmount > 0.1f) {
			this.animateWalk(ModAnimationDefinitions.flesh_monsterAnimation.walk, limbSwing, limbSwingAmount, 1f, 7.0f);
		} else {
			this.animate(mob.idleAnimationState, ModAnimationDefinitions.flesh_monsterAnimation.idel, ageInTicks, 1f);
		}

	}

	private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeinTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30F);
		pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

		this.head.yRot = pNetHeadYaw * ((float) Math.PI / 180F);
		this.head.xRot = pHeadPitch * ((float) Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		fleash_beast.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return fleash_beast;
	}
}