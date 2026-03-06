package net.luis.scpmod.entity.animations;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class ModAnimationDefinitions {
    public class flesh_monsterAnimation {
        public static final AnimationDefinition walk = AnimationDefinition.Builder.withLength(1.0F).looping()
                .addAnimation("jaw", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("r_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("l_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("upper_tentical_1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, -65.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.6667F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, -65.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, -65.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("upper_tentical_1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(-5.0F, -4.0F, 1.25F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.6667F, KeyframeAnimations.posVec(-4.0F, -4.0F, 2.25F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(-5.0F, -4.0F, 1.25F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("r_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("r_hand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -0.5F, -0.25F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("forearm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.125F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.375F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.625F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.875F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.125F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.375F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.625F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.875F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("hand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 1.2F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, 1.2F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.625F, KeyframeAnimations.posVec(0.0F, 1.2F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, 1.2F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("r_forearm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.125F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("r_forearm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.25F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("r_arm2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("r_arm2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("mid_tentical_1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(54.26F, 11.83F, -10.39F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.6667F, KeyframeAnimations.degreeVec(66.76F, 11.83F, -10.39F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(54.26F, 11.83F, -10.39F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("mid_tentical_1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(-1.2F, -3.0F, -3.5F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.6667F, KeyframeAnimations.posVec(-1.2F, -4.0F, -4.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(-1.2F, -3.0F, -3.5F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("lower_tentical_1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(62.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.6667F, KeyframeAnimations.degreeVec(80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(62.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("lower_tentical_1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -3.3F, -3.6F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.6667F, KeyframeAnimations.posVec(0.0F, -4.3F, -3.85F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -3.3F, -3.6F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -10.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("upper_tentical_2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, -42.5F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, -42.5F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, -42.5F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("upper_tentical_2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, -2.0F, 2.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.posVec(-3.0F, -2.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(-3.0F, -2.0F, 2.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("mid_tentical_2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("mid_tentical_2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.8F, -2.7F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -2.55F, -3.45F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -1.8F, -2.7F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("lower_tentical_2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.5F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("lower_tentical_2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.0F, -2.9F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -3.5F, -3.5F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -2.0F, -2.9F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("upper_tentical_3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(35.9498F, 8.5272F, -15.5366F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(48.4499F, 8.5286F, -15.5383F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(35.95F, 8.53F, -15.54F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("upper_tentical_3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(-1.2F, -1.0F, -3.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.posVec(-1.2F, -2.0F, -3.8F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(-1.2F, -1.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("mid_tentical_3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("mid_tentical_3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.75F, -3.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -1.9F, -3.2F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -1.75F, -3.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("lower_tentical_3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("lower_tentical_3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.25F, -2.5F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -0.35F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -1.25F, -2.5F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("upper_tentical_4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(16.8227F, -23.9275F, 26.341F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(-3.1787F, -23.9287F, 26.3405F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(16.82F, -23.93F, 26.34F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("upper_tentical_4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(2.5F, -2.25F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.posVec(1.75F, -2.25F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(2.5F, -2.25F, -1.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("mid_tentical_4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(55.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("mid_tentical_4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.75F, -3.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, -3.0F, -3.6F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -1.75F, -3.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("lower_tentical_4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("lower_tentical_4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.7F, -2.65F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, -3.45F, -3.6F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -1.7F, -2.65F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("upper_tentical_5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(-21.2677F, 7.5208F, 46.086F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(1.7917F, 8.1986F, 31.97F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(-21.27F, 7.52F, 46.09F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("upper_tentical_5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(4.05F, -2.35F, 2.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.posVec(3.05F, -1.35F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(4.05F, -2.35F, 2.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("mid_tentical_5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(52.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(62.0756F, -7.9185F, 6.1267F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(52.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("mid_tentical_5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.75F, -3.6F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.posVec(0.8F, -3.55F, -3.7F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -2.75F, -3.6F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("lower_tetical_5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(67.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(42.4728F, -1.6887F, 1.8437F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(67.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("lower_tetical_5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -3.7F, -3.6F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.posVec(0.2F, -1.85F, -2.8F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -3.7F, -3.6F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("upper_tentical_6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(14.7822F, -2.5759F, 9.6658F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-17.25F, 2.9932F, 9.5459F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(14.7822F, -2.5759F, 9.6658F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("upper_tentical_6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(1.0F, -0.5F, -1.3F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.7917F, KeyframeAnimations.posVec(1.0F, -0.5F, 1.7F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(1.0F, -0.5F, -1.3F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("mid_tentical_6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.5833F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("mid_tentical_6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.75F, -2.75F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, -3.75F, -4.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -1.75F, -2.75F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("lower_tentical_6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.5833F, KeyframeAnimations.degreeVec(72.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("lower_tentical_6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.0F, -2.9F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5833F, KeyframeAnimations.posVec(0.0F, -4.1F, -3.65F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -2.0F, -2.9F), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();

        public static final AnimationDefinition idel = AnimationDefinition.Builder.withLength(3.0F).looping()
                .addAnimation("upper_tentical_1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, -65.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.875F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, -65.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, -65.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("upper_tentical_1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(-5.0F, -4.0F, 1.25F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.875F, KeyframeAnimations.posVec(-4.0F, -4.0F, 2.25F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.posVec(-5.0F, -4.0F, 1.25F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("r_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.875F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("r_hand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.875F, KeyframeAnimations.posVec(0.0F, -0.5F, -0.25F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("forearm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.6667F, KeyframeAnimations.degreeVec(0.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(2.3333F, KeyframeAnimations.degreeVec(0.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.375F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.7917F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.25F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.6667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(2.0417F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(2.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(2.6667F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5417F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.9167F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.7917F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.75F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("hand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 1.2F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5417F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.9167F, KeyframeAnimations.posVec(0.0F, 1.2F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.375F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.7917F, KeyframeAnimations.posVec(0.0F, 1.2F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.75F, KeyframeAnimations.posVec(0.0F, 1.2F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("r_forearm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("r_forearm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.25F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("r_arm2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0417F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("r_arm2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0417F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("mid_tentical_1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(54.26F, 11.83F, -10.39F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.6667F, KeyframeAnimations.degreeVec(66.76F, 11.83F, -10.39F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(54.26F, 11.83F, -10.39F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("mid_tentical_1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(-1.2F, -3.0F, -3.5F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.6667F, KeyframeAnimations.posVec(-1.2F, -4.0F, -4.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.posVec(-1.2F, -3.0F, -3.5F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("lower_tentical_1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(62.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(2.125F, KeyframeAnimations.degreeVec(80.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(62.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("lower_tentical_1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -3.3F, -3.6F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.125F, KeyframeAnimations.posVec(0.0F, -4.3F, -3.85F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, -3.3F, -3.6F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("upper_tentical_2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, -42.5F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.5417F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, -42.5F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, -42.5F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("upper_tentical_2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, -2.0F, 2.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5417F, KeyframeAnimations.posVec(-3.0F, -2.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.posVec(-3.0F, -2.0F, 2.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("mid_tentical_2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.5417F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("mid_tentical_2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.8F, -2.7F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5417F, KeyframeAnimations.posVec(0.0F, -2.55F, -3.45F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, -1.8F, -2.7F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("lower_tentical_2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.5417F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("lower_tentical_2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.0F, -2.9F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.5417F, KeyframeAnimations.posVec(0.0F, -3.5F, -3.5F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, -2.0F, -2.9F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("upper_tentical_3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(35.9498F, 8.5272F, -15.5366F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.3333F, KeyframeAnimations.degreeVec(48.4499F, 8.5286F, -15.5383F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(35.95F, 8.53F, -15.54F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("upper_tentical_3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(-1.2F, -1.0F, -3.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.3333F, KeyframeAnimations.posVec(-1.2F, -2.0F, -3.8F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.posVec(-1.2F, -1.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("mid_tentical_3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.3333F, KeyframeAnimations.degreeVec(42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("mid_tentical_3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.75F, -3.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.3333F, KeyframeAnimations.posVec(0.0F, -1.9F, -3.2F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, -1.75F, -3.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("lower_tentical_3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.3333F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("lower_tentical_3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.25F, -2.5F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.3333F, KeyframeAnimations.posVec(0.0F, -0.35F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, -1.25F, -2.5F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("upper_tentical_4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(16.8227F, -23.9275F, 26.341F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.7917F, KeyframeAnimations.degreeVec(-3.1787F, -23.9287F, 26.3405F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(16.82F, -23.93F, 26.34F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("upper_tentical_4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(2.5F, -2.25F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.7917F, KeyframeAnimations.posVec(1.75F, -2.25F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.posVec(2.5F, -2.25F, -1.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("mid_tentical_4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.7917F, KeyframeAnimations.degreeVec(55.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("mid_tentical_4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.75F, -3.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.7917F, KeyframeAnimations.posVec(0.0F, -3.0F, -3.6F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, -1.75F, -3.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("lower_tentical_4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.7917F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("lower_tentical_4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.7F, -2.65F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.7917F, KeyframeAnimations.posVec(0.0F, -3.45F, -3.6F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, -1.7F, -2.65F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("upper_tentical_5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(-21.2677F, 7.5208F, 46.086F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.3333F, KeyframeAnimations.degreeVec(1.7917F, 8.1986F, 31.97F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(-21.27F, 7.52F, 46.09F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("upper_tentical_5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(4.05F, -2.35F, 2.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.3333F, KeyframeAnimations.posVec(3.05F, -1.35F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.posVec(4.05F, -2.35F, 2.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("mid_tentical_5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(52.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.3333F, KeyframeAnimations.degreeVec(62.0756F, -7.9185F, 6.1267F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(52.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("mid_tentical_5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.75F, -3.6F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.3333F, KeyframeAnimations.posVec(0.8F, -3.55F, -3.7F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, -2.75F, -3.6F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("lower_tetical_5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(67.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.3333F, KeyframeAnimations.degreeVec(42.4728F, -1.6887F, 1.8437F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(67.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("lower_tetical_5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -3.7F, -3.6F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.3333F, KeyframeAnimations.posVec(0.2F, -1.85F, -2.8F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, -3.7F, -3.6F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("upper_tentical_6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(14.7822F, -2.5759F, 9.6658F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.5417F, KeyframeAnimations.degreeVec(-17.25F, 2.9932F, 9.5459F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(14.7822F, -2.5759F, 9.6658F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("upper_tentical_6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(1.0F, -0.5F, -1.3F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.0417F, KeyframeAnimations.posVec(1.0F, -0.5F, 1.7F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.posVec(1.0F, -0.5F, -1.3F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("mid_tentical_6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.75F, KeyframeAnimations.degreeVec(65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("mid_tentical_6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.75F, -2.75F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, -3.75F, -4.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, -1.75F, -2.75F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("lower_tentical_6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(1.75F, KeyframeAnimations.degreeVec(72.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(3.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("lower_tentical_6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.0F, -2.9F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, -4.1F, -3.65F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, -2.0F, -2.9F), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();

        public static final AnimationDefinition attack = AnimationDefinition.Builder.withLength(0.75F)
                .addAnimation("jaw", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("r_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("l_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("upper_tentical_1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, -65.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(-42.5F, 0.0F, -65.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.degreeVec(43.3333F, 0.0F, -65.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, -65.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("upper_tentical_1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(-5.0F, -4.0F, 1.25F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.25F, KeyframeAnimations.posVec(-3.25F, -4.0F, 3.25F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.4167F, KeyframeAnimations.posVec(-3.64F, -4.0F, -3.69F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.75F, KeyframeAnimations.posVec(-5.0F, -4.0F, 1.25F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("r_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("r_hand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("forearm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("hand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("r_forearm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("r_forearm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("r_arm2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("r_arm2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("mid_tentical_1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(54.26F, 11.83F, -10.39F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("mid_tentical_1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(-1.2F, -3.0F, -3.5F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("lower_tentical_1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(62.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("lower_tentical_1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -3.3F, -3.6F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("upper_tentical_2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, -42.5F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(-60.0F, 0.0F, -42.5F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.4167F, KeyframeAnimations.degreeVec(45.0F, 0.0F, -42.5F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, -42.5F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("upper_tentical_2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, -2.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.25F, KeyframeAnimations.posVec(-1.75F, -4.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.4167F, KeyframeAnimations.posVec(-1.75F, -3.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.75F, KeyframeAnimations.posVec(-3.0F, -2.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("mid_tentical_2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("mid_tentical_2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.8F, -2.7F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("lower_tentical_2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("lower_tentical_2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.0F, -2.9F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("upper_tentical_3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(35.9498F, 8.5272F, -15.5366F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(13.4498F, 8.5272F, -15.5366F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.4167F, KeyframeAnimations.degreeVec(53.4498F, 8.5272F, -15.5366F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(35.9498F, 8.5272F, -15.5366F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("upper_tentical_3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(-1.2F, -1.0F, -3.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.posVec(-1.2F, -1.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.posVec(-1.2F, -2.1F, -4.67F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.posVec(-1.2F, -1.0F, -3.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("mid_tentical_3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("mid_tentical_3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.75F, -3.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("lower_tentical_3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("lower_tentical_3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.25F, -2.5F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("upper_tentical_4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(16.8227F, -23.9275F, 26.341F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(-5.6773F, -23.9275F, 26.341F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.4167F, KeyframeAnimations.degreeVec(63.0589F, -9.5151F, 30.6968F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(16.8227F, -23.9275F, 26.341F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("upper_tentical_4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(2.5F, -2.25F, -1.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.posVec(1.25F, -2.25F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.posVec(2.25F, -3.25F, -5.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.posVec(2.5F, -2.25F, -1.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("mid_tentical_4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("mid_tentical_4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.75F, -3.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("lower_tentical_4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("lower_tentical_4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.7F, -2.65F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("upper_tentical_5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(-21.2677F, 7.5208F, 46.086F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(-36.2677F, 7.5208F, 46.086F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.4167F, KeyframeAnimations.degreeVec(47.0657F, 7.5208F, 46.086F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(-21.2677F, 7.5208F, 46.086F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("upper_tentical_5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(4.05F, -2.35F, 2.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.posVec(3.75F, -2.65F, 3.0F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.posVec(3.85F, -2.55F, -5.33F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.posVec(4.05F, -2.35F, 2.0F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("mid_tentical_5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(52.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("mid_tentical_5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.75F, -3.6F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("lower_tetical_5", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(67.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("lower_tetical_5", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -3.7F, -3.6F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("upper_tentical_6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(14.7822F, -2.5759F, 9.6658F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.25F, KeyframeAnimations.degreeVec(-5.2178F, -2.5759F, 9.6658F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.4167F, KeyframeAnimations.degreeVec(59.7822F, -2.5759F, 9.6658F), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.75F, KeyframeAnimations.degreeVec(14.7822F, -2.5759F, 9.6658F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("upper_tentical_6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(1.0F, -0.5F, -1.3F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25F, KeyframeAnimations.posVec(1.0F, -0.5F, -0.3F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4167F, KeyframeAnimations.posVec(1.0F, -1.75F, -5.3F), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75F, KeyframeAnimations.posVec(1.0F, -0.5F, -1.3F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("mid_tentical_6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("mid_tentical_6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.75F, -2.75F), AnimationChannel.Interpolations.LINEAR)
                ))
                .addAnimation("lower_tentical_6", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                ))
                .addAnimation("lower_tentical_6", new AnimationChannel(AnimationChannel.Targets.POSITION,
                        new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -2.0F, -2.9F), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
    }

        public class shy_guy_animation {
                public static final AnimationDefinition sad_idle = AnimationDefinition.Builder.withLength(6.0F).looping()
                        .addAnimation("neck", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(3.4583F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.7083F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("neck", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -16.5F, 18.0F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("torso", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("torso", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -17.25F, 16.5F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("upper_chest", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("upper_chest", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("lower_chest", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("lower_chest", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("waist", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("waist", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -14.75F, 7.5F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("arms", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -16.25F, 16.5F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("r_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-14.6535F, -50.8906F, -40.0127F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(-13.9244F, -48.4675F, -40.9646F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(-14.65F, -50.89F, -40.01F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_bisep", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_forearm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-274.7535F, -49.9164F, 188.5823F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(-276.1395F, -44.3019F, 191.5454F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(-274.7535F, -49.9164F, 188.5823F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_forearm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.4F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(5.0292F, -36.9825F, -17.3633F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(-4.9925F, -36.9873F, 1.3735F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(5.0292F, -36.9825F, -17.3633F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_hand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.25F, 0.0F, 0.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_thumb", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_lower_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(26.5465F, -50.3198F, -19.5869F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(31.5968F, -47.8867F, -26.2635F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(26.5465F, -50.3198F, -19.5869F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_lower_thumb", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -40.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_thumb", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.75F, -0.9F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_indexfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -12.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(2.7471F, -12.1991F, -12.7936F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(0.0F, -12.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_indexfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.25F, 0.0F, -0.4F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_indexfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -32.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -57.5F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -32.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_indexfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.15F, -0.15F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.posVec(0.5F, -0.05F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.posVec(0.15F, -0.15F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_middlefinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(1.0848F, -4.8812F, -12.5462F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_middlefinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.1F, 0.0F, -0.4F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_middlefinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -40.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -52.5F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -40.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_middlefinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(-0.6F, -0.05F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_ringfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(0.5414F, -2.4407F, -12.5115F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_ringfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.2F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_ringfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -42.71F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_ringfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.05F, -0.1F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.posVec(0.25F, -0.1F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.posVec(0.05F, -0.1F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(-2.6914F, 4.2154F, -32.5991F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.1F, 0.0F, 0.1F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -16.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -42.5F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -16.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(18.8292F, -57.8924F, 49.5612F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(17.6384F, -55.5164F, 50.9854F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(18.83F, -57.89F, 49.56F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_forearm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-72.4794F, -52.393F, -207.9607F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(-71.8091F, -46.8794F, -209.2747F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(-72.4794F, -52.393F, -207.9607F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_forearm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(1.5F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-11.3462F, -41.0117F, 19.8808F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(-9.9253F, -43.9251F, 17.214F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(-11.3462F, -41.0117F, 19.8808F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_thumb", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.75F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_lower_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-38.0087F, -32.1764F, 34.741F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(-43.8776F, -22.3017F, 47.4745F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(-38.0087F, -32.1764F, 34.741F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_lower_thumb", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.35F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_thumb", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_indexfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(-0.9845F, -7.4355F, 7.564F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(0.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_indexfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 50.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 62.5F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 50.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_indexfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.2F, -0.1F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.posVec(0.3F, -0.3F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.posVec(0.2F, -0.1F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_middlefinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(-0.5414F, -2.4407F, 12.5115F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_middlefinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 40.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 60.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 40.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_middlefinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.3F, -0.1F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.posVec(0.4F, -0.2F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.posVec(0.3F, -0.1F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_ringfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.3721F, 2.9103F, 7.4717F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(0.8722F, 2.8015F, 17.4836F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(0.3721F, 2.9103F, 7.4717F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_ringfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 25.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 32.5F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 25.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_ringfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 5.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(0.6543F, 4.9571F, 17.5283F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(0.0F, 5.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 20.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 40.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 20.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.posVec(0.3F, -0.1F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-97.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 12.0F, -13.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-15.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.25F, KeyframeAnimations.degreeVec(-23.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(-15.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.5F, 0.8F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_lower_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.25F, KeyframeAnimations.degreeVec(62.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_lower_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.3F, -0.05F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_foot", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(75.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.25F, KeyframeAnimations.degreeVec(60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(75.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_foot", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.25F, -0.15F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(-0.7F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_lower_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_lower_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.15F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_foot", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(72.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(67.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(72.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_foot", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.75F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("neck_top", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("neck_top", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.5F, -1.8F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.posVec(0.0F, -0.7F, -1.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.0F, KeyframeAnimations.posVec(0.0F, -0.5F, -1.8F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("upper_tumm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("upper_tumm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.posVec(0.0F, -0.2F, 0.2F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("lower_tumm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.5F, KeyframeAnimations.posVec(0.0F, -0.1F, 0.3F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("shy_guy", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -11.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("angry_face", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("angry_face", new AnimationChannel(AnimationChannel.Targets.SCALE,
                                new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.6F, 0.5F, 0.7F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .build();

                public static final AnimationDefinition sad_walk = AnimationDefinition.Builder.withLength(1.0F).looping()
                        .addAnimation("neck", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("neck", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -16.5F, 18.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("torso", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("torso", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -17.25F, 16.5F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("upper_chest", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("upper_chest", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("lower_chest", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("lower_chest", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("waist", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.125F, KeyframeAnimations.degreeVec(-29.9764F, 1.2497F, 2.1654F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-29.9764F, -1.2497F, -2.1654F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.7083F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.875F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-30.0036F, -0.0002F, -0.0047F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("waist", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -14.75F, 7.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("arms", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -16.25F, 16.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-20.6143F, -52.0824F, -36.3551F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_bisep", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_forearm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-279.0673F, -41.9304F, 187.7383F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_forearm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.25F, 0.25F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("r_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-7.7781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_hand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.25F, 0.0F, 0.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_thumb", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_lower_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(53.9602F, -53.627F, -62.2156F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_lower_thumb", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -40.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_thumb", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.75F, -0.9F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_indexfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(4.3361F, -11.7351F, -20.4458F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_indexfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.25F, 0.0F, -0.4F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_indexfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -65.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_indexfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.45F, 0.05F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_middlefinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(1.507F, -4.768F, -17.5627F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_middlefinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.1F, 0.0F, -0.4F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_middlefinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -75.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_middlefinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(-0.6F, 0.05F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_ringfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.9572F, -2.3096F, -22.5193F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_ringfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.2F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_ringfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -60.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_ringfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.3F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("r_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-0.8704F, 4.9238F, -10.0374F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.1F, 0.0F, 0.1F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -51.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(18.8292F, -57.8924F, 49.5612F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_forearm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-79.036F, -38.4344F, -199.2895F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_forearm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(1.5F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(6.9326F, -34.3927F, -12.1479F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_thumb", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.75F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_lower_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-38.0087F, -32.1764F, 34.741F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_lower_thumb", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.35F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_thumb", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_indexfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_indexfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 60.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_indexfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.2F, -0.1F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_middlefinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-0.3265F, -2.4786F, 7.5071F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_middlefinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 65.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_middlefinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.3F, -0.1F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_ringfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.3721F, 2.9103F, 7.4717F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_ringfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 57.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_ringfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 5.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 45.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-72.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(-72.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-72.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 1.5F, -12.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 1.5F, -12.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 1.5F, -12.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-10.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.degreeVec(9.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5833F, KeyframeAnimations.degreeVec(-31.89F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.75F, KeyframeAnimations.degreeVec(-25.33F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-10.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.5F, 0.8F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -0.5F, 0.8F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_lower_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.degreeVec(42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5833F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75F, KeyframeAnimations.degreeVec(27.48F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_lower_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.3F, -0.05F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -0.3F, -0.05F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_foot", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5833F, KeyframeAnimations.degreeVec(42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.6667F, KeyframeAnimations.degreeVec(35.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_foot", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.05F, 0.05F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -0.05F, 0.05F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -0.05F, 0.05F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.25F, KeyframeAnimations.degreeVec(-23.46F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(-0.7F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.posVec(-0.7F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(-0.7F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_lower_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.0833F, KeyframeAnimations.degreeVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_lower_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.15F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.15F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.15F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_foot", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.0833F, KeyframeAnimations.degreeVec(43.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.1667F, KeyframeAnimations.degreeVec(34.57F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75F, KeyframeAnimations.degreeVec(-19.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_foot", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.05F, 0.1F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.05F, 0.1F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.05F, 0.1F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("neck_top", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("neck_top", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.5F, -1.8F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("upper_tumm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("upper_tumm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("lower_tumm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("lower_tumm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("shy_guy", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("shy_guy", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 1.5F, 12.0F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("upper_half", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.4167F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5833F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.7083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("upper_half", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.125F, KeyframeAnimations.posVec(0.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.5833F, KeyframeAnimations.posVec(-0.7F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.7083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("angry_face", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("angry_face", new AnimationChannel(AnimationChannel.Targets.SCALE,
                                new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.6F, 0.5F, 0.6F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .build();

                public static final AnimationDefinition getting_mad = AnimationDefinition.Builder.withLength(9.7917F).looping()
                        .addAnimation("neck", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.2083F, KeyframeAnimations.degreeVec(17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.5F, KeyframeAnimations.degreeVec(-65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.5833F, KeyframeAnimations.degreeVec(-59.88F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.75F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.9167F, KeyframeAnimations.degreeVec(-24.6657F, -4.2085F, -9.0795F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(9.0417F, KeyframeAnimations.degreeVec(-25.0612F, 4.2635F, 9.0851F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(9.1667F, KeyframeAnimations.degreeVec(-24.6657F, -4.2085F, -9.0795F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(9.2917F, KeyframeAnimations.degreeVec(-25.0612F, 4.2635F, 9.0851F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(9.4167F, KeyframeAnimations.degreeVec(-24.6657F, -4.2085F, -9.0795F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(9.5417F, KeyframeAnimations.degreeVec(-25.0612F, 4.2635F, 9.0851F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(9.6667F, KeyframeAnimations.degreeVec(-25.3982F, -0.0078F, 0.0349F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("neck", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -16.5F, 18.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.2083F, KeyframeAnimations.posVec(0.0F, -16.5F, 18.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.375F, KeyframeAnimations.posVec(0.0F, -15.83F, 24.99F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.5F, KeyframeAnimations.posVec(0.0F, -17.0F, 29.75F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.6667F, KeyframeAnimations.posVec(0.0F, -15.54F, 23.61F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.75F, KeyframeAnimations.posVec(0.0F, -16.75F, 17.75F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.875F, KeyframeAnimations.posVec(0.0F, -16.75F, 17.75F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("torso", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-17.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("torso", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -17.25F, 16.5F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("upper_chest", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.2083F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.5F, KeyframeAnimations.degreeVec(-42.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.75F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("upper_chest", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.5F, KeyframeAnimations.posVec(0.0F, 0.75F, -1.75F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("lower_chest", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.2083F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.5F, KeyframeAnimations.degreeVec(-25.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.75F, KeyframeAnimations.degreeVec(5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("lower_chest", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("waist", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("waist", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.01F, -14.57F, 7.63F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.0417F, KeyframeAnimations.posVec(0.02F, -14.49F, 7.89F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0833F, KeyframeAnimations.posVec(-0.16F, -9.15F, 12.48F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.8333F, KeyframeAnimations.posVec(-0.31F, -4.17F, 13.95F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.9583F, KeyframeAnimations.posVec(-0.5F, 1.12F, 12.88F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.0F, KeyframeAnimations.posVec(-0.5F, 1.15F, 12.85F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("arms", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(8.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.5F, KeyframeAnimations.degreeVec(-82.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.75F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.9167F, KeyframeAnimations.degreeVec(52.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("arms", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -16.25F, 16.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.2083F, KeyframeAnimations.posVec(0.0F, -16.25F, 16.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.5F, KeyframeAnimations.posVec(0.0F, -16.0F, 21.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.75F, KeyframeAnimations.posVec(0.0F, -16.25F, 15.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-20.6143F, -52.0824F, -36.3551F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.2083F, KeyframeAnimations.degreeVec(-20.61F, -52.08F, -36.36F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.75F, KeyframeAnimations.degreeVec(-20.61F, -52.08F, -36.36F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.9167F, KeyframeAnimations.degreeVec(-39.4353F, -41.8595F, -10.9032F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_bisep", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.2083F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.75F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_forearm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-279.0673F, -41.9304F, 187.7383F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.2083F, KeyframeAnimations.degreeVec(-279.07F, -41.93F, 187.74F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.5417F, KeyframeAnimations.degreeVec(-276.4711F, -42.6606F, 188.973F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.7083F, KeyframeAnimations.degreeVec(-275.1527F, -42.7572F, 187.2772F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.75F, KeyframeAnimations.degreeVec(-279.07F, -41.93F, 187.74F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.7917F, KeyframeAnimations.degreeVec(-271.8222F, -49.0409F, 191.6861F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.9167F, KeyframeAnimations.degreeVec(-442.4019F, -27.5039F, 358.126F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_forearm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.25F, 0.25F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("r_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-7.7781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.2917F, KeyframeAnimations.degreeVec(7.2219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.3333F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.375F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.4167F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.4583F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.5F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.5417F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.5833F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.625F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.6667F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.7083F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.75F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.7917F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.8333F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.875F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.9167F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.9583F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.0F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.0417F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.0833F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.125F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.1667F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.2083F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.25F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.2917F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.3333F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.375F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.4167F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.4583F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.5F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.5417F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.5833F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.625F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.6667F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.7083F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.75F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.7917F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.8333F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.875F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.9167F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.9583F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.0F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.0417F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.0833F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.125F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.1667F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.2083F, KeyframeAnimations.degreeVec(4.7219F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.25F, KeyframeAnimations.degreeVec(-15.2781F, -31.4612F, 6.7058F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.75F, KeyframeAnimations.degreeVec(-15.28F, -31.46F, 6.71F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.9167F, KeyframeAnimations.degreeVec(-12.991F, 0.1116F, -1.4303F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_hand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.25F, 0.0F, 0.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.75F, KeyframeAnimations.posVec(0.25F, 0.0F, 0.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_thumb", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_lower_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(53.9602F, -53.627F, -62.2156F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_lower_thumb", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -40.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_thumb", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.75F, -0.9F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_indexfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(4.3361F, -11.7351F, -20.4458F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_indexfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.25F, 0.0F, -0.4F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_indexfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -65.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_indexfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.45F, 0.05F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_middlefinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(1.507F, -4.768F, -17.5627F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_middlefinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.1F, 0.0F, -0.4F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_middlefinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -75.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_middlefinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(-0.6F, 0.05F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_ringfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.9572F, -2.3096F, -22.5193F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_ringfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -0.2F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_ringfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -60.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_ringfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.3F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("r_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-0.8704F, 4.9238F, -10.0374F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.1F, 0.0F, 0.1F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -51.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(18.8292F, -57.8924F, 49.5612F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.2083F, KeyframeAnimations.degreeVec(18.83F, -57.89F, 49.56F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.75F, KeyframeAnimations.degreeVec(18.83F, -57.89F, 49.56F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.9167F, KeyframeAnimations.degreeVec(34.8893F, -52.1685F, 30.0483F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_forearm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-79.036F, -38.4344F, -199.2895F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.2083F, KeyframeAnimations.degreeVec(-79.04F, -38.43F, -199.29F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.5417F, KeyframeAnimations.degreeVec(-82.6007F, -38.401F, -199.8554F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.7083F, KeyframeAnimations.degreeVec(-83.8052F, -38.5034F, -198.2335F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.75F, KeyframeAnimations.degreeVec(-79.04F, -38.43F, -199.29F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.7917F, KeyframeAnimations.degreeVec(-80.4856F, -46.3158F, -207.5156F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.9167F, KeyframeAnimations.degreeVec(80.0284F, -30.6714F, -367.3015F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_forearm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(1.5F, 0.0F, -0.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(6.9326F, -34.3927F, -12.1479F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.2917F, KeyframeAnimations.degreeVec(6.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.3333F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.375F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.4167F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.4583F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.5F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.5417F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.5833F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.625F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.6667F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.7083F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.75F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.7917F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.8333F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.875F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.9167F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.9583F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.0F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.0417F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.0833F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.125F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.1667F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.2083F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.25F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.2917F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.3333F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.375F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.4167F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.4583F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.5F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.5417F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.5833F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.625F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.6667F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.7083F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.75F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.7917F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.8333F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.875F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.9167F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(7.9583F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.0F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.0417F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.0833F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.125F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.1667F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.2083F, KeyframeAnimations.degreeVec(-8.07F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.25F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.75F, KeyframeAnimations.degreeVec(21.93F, -34.39F, -12.15F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.9167F, KeyframeAnimations.degreeVec(17.9899F, -3.7305F, -0.5491F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_hand", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(8.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_thumb", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.75F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_lower_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-38.0087F, -32.1764F, 34.741F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_lower_thumb", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.35F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_thumb", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_indexfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_indexfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 60.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_indexfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.2F, -0.1F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_middlefinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-0.3265F, -2.4786F, 7.5071F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_middlefinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 65.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_middlefinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.3F, -0.1F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_ringfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.3721F, 2.9103F, 7.4717F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_ringfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 57.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_ringfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 5.0F, 10.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 45.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.2F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-72.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.0F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("legs", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 1.8F, -12.4F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.0417F, KeyframeAnimations.posVec(0.0F, 1.67F, -12.25F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 1.61F, -12.05F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 1.57F, -11.83F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 1.43F, -11.61F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 0.97F, -10.45F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, 0.87F, -10.18F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 0.7F, -9.79F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.6667F, KeyframeAnimations.posVec(0.0F, -0.27F, -7.51F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.7083F, KeyframeAnimations.posVec(0.0F, -0.47F, -7.09F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, -0.57F, -6.69F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.7917F, KeyframeAnimations.posVec(0.0F, -0.69F, -6.23F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -1.27F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.125F, KeyframeAnimations.posVec(0.0F, -1.51F, -2.55F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.2083F, KeyframeAnimations.posVec(0.0F, -1.62F, -1.65F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.25F, KeyframeAnimations.posVec(0.0F, -1.75F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.2917F, KeyframeAnimations.posVec(0.0F, -1.8F, -0.59F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.375F, KeyframeAnimations.posVec(0.0F, -1.91F, 0.37F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.4167F, KeyframeAnimations.posVec(0.0F, -1.97F, 0.88F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.4583F, KeyframeAnimations.posVec(0.0F, -1.98F, 1.41F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.5417F, KeyframeAnimations.posVec(0.0F, -1.95F, 2.41F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.5833F, KeyframeAnimations.posVec(0.0F, -2.05F, 2.85F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, -1.97F, 4.87F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.875F, KeyframeAnimations.posVec(0.0F, -1.88F, 6.28F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.9167F, KeyframeAnimations.posVec(0.0F, -1.85F, 6.72F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.9583F, KeyframeAnimations.posVec(0.0F, -1.75F, 7.29F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.0833F, KeyframeAnimations.posVec(0.0F, -1.56F, 8.57F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.375F, KeyframeAnimations.posVec(0.0F, -1.02F, 11.35F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.4167F, KeyframeAnimations.posVec(0.0F, -0.93F, 11.63F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, -0.76F, 12.31F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.75F, KeyframeAnimations.posVec(0.0F, -0.15F, 14.01F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.9167F, KeyframeAnimations.posVec(0.0F, 0.13F, 14.74F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(2.9583F, KeyframeAnimations.posVec(0.0F, 0.27F, 14.9F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.3F, 15.1F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-10.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.5F, 0.8F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.9583F, KeyframeAnimations.posVec(0.0F, -0.23F, 0.84F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, -0.4F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_lower_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.0F, KeyframeAnimations.degreeVec(-60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_lower_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.3F, -0.05F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_foot", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.0F, KeyframeAnimations.degreeVec(27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_foot", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.05F, 0.05F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.0F, KeyframeAnimations.degreeVec(-10.0269F, -1.6067F, 1.9156F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(-0.7F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.0F, KeyframeAnimations.posVec(-0.7F, -0.3F, 0.7F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_lower_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.0F, KeyframeAnimations.degreeVec(-60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_lower_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.15F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_foot", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.0F, KeyframeAnimations.degreeVec(28.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_foot", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.05F, 0.1F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, -0.05F, 0.45F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("neck_top", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.2083F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.5F, KeyframeAnimations.degreeVec(-50.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.75F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("neck_top", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -0.5F, -1.8F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.2083F, KeyframeAnimations.posVec(0.0F, -0.5F, -1.8F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.3333F, KeyframeAnimations.posVec(0.0F, -0.01F, 2.26F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.5F, KeyframeAnimations.posVec(0.0F, -2.25F, 6.95F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.6667F, KeyframeAnimations.posVec(0.0F, -0.51F, 2.87F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.75F, KeyframeAnimations.posVec(0.0F, -0.5F, -1.8F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("upper_tumm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.5F, KeyframeAnimations.degreeVec(-15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("upper_tumm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.5F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.85F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("lower_tumm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("lower_tumm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("shy_guy", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("shy_guy", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 1.5F, 12.0F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("upper_half", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(3.75F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.0F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.25F, KeyframeAnimations.degreeVec(-19.2781F, 6.8432F, 18.8711F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.5F, KeyframeAnimations.degreeVec(-55.4131F, -0.0084F, 0.0362F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(4.75F, KeyframeAnimations.degreeVec(-19.2726F, -6.8592F, -18.7994F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(5.0F, KeyframeAnimations.degreeVec(9.5869F, -0.0084F, 0.0362F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(5.25F, KeyframeAnimations.degreeVec(-19.2781F, 6.8432F, 18.8711F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(5.5F, KeyframeAnimations.degreeVec(-55.4131F, -0.0084F, 0.0362F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(5.75F, KeyframeAnimations.degreeVec(-19.2726F, -6.8592F, -18.7994F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.0F, KeyframeAnimations.degreeVec(9.5869F, -0.0084F, 0.0362F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.25F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.2083F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.5F, KeyframeAnimations.degreeVec(-20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("upper_half", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.5F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.0833F, KeyframeAnimations.posVec(0.0F, 0.52F, 1.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 1.3F, 1.59F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.4167F, KeyframeAnimations.posVec(0.0F, 1.95F, 2.06F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, 4.64F, 4.61F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.4167F, KeyframeAnimations.posVec(0.0F, 7.97F, 6.21F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.6667F, KeyframeAnimations.posVec(0.0F, 9.62F, 6.6F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(1.9583F, KeyframeAnimations.posVec(0.0F, 11.35F, 6.61F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(2.125F, KeyframeAnimations.posVec(0.0F, 12.32F, 6.48F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(2.3333F, KeyframeAnimations.posVec(0.0F, 13.44F, 6.4F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(2.5833F, KeyframeAnimations.posVec(0.0F, 14.77F, 6.06F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(2.8333F, KeyframeAnimations.posVec(0.0F, 15.6F, 5.72F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 16.0F, 5.67F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(3.5F, KeyframeAnimations.posVec(0.0F, 16.0F, 5.67F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("chest", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("chest", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("angry_face", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.2917F, KeyframeAnimations.posVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.7917F, KeyframeAnimations.posVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("angry_face", new AnimationChannel(AnimationChannel.Targets.SCALE,
                                new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.7F, 0.6F, 0.7F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(6.2917F, KeyframeAnimations.scaleVec(0.7F, 0.6F, 0.7F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(8.25F, KeyframeAnimations.scaleVec(1.1F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("jaw", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(8.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(8.9167F, KeyframeAnimations.posVec(0.0F, -6.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("jaw", new AnimationChannel(AnimationChannel.Targets.SCALE,
                                new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("l_bisep", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(8.2083F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(8.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .build();

                public static final AnimationDefinition mad_run = AnimationDefinition.Builder.withLength(1.0F).looping()
                        .addAnimation("shy_guy", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 7.25F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -3.0F, 7.25F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 7.25F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, -3.79F, 7.25F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 7.25F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("upper_half", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("upper_half", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 2.75F, 1.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 2.75F, 1.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 2.75F, 1.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("neck", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("angry_face", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.posVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("jaw", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -6.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -6.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -6.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-129.4452F, -40.4633F, 30.7487F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(-129.4452F, -40.4633F, 30.7487F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-129.4452F, -40.4633F, 30.7487F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_forearm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(99.7494F, 1.0696F, -62.4925F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(99.7494F, 1.0696F, -62.4925F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(99.7494F, 1.0696F, -62.4925F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, -15.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, -15.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, -15.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_lower_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(36.8282F, -48.2179F, -6.6705F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(36.8282F, -48.2179F, -6.6705F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(36.8282F, -48.2179F, -6.6705F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -45.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -45.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -45.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_thumb", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.5F, -0.75F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.posVec(0.5F, -0.75F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(0.5F, -0.75F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_indexfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_indexfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -37.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -37.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -37.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_middlefinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.5414F, -2.4407F, -12.5115F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.5414F, -2.4407F, -12.5115F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.5414F, -2.4407F, -12.5115F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_middlefinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -27.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -27.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -27.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_middlefinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(-0.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.posVec(-0.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(-0.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_ringfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-0.5414F, 2.4407F, -12.5115F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(-0.5414F, 2.4407F, -12.5115F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-0.5414F, 2.4407F, -12.5115F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_ringfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -32.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -32.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -32.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-1.3096F, 7.3854F, -10.0845F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(-1.3096F, 7.3854F, -10.0845F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-1.3096F, 7.3854F, -10.0845F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(126.4395F, -36.802F, -25.0419F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(126.4395F, -36.802F, -25.0419F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(126.4395F, -36.802F, -25.0419F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_forearm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-94.8821F, 1.0809F, 64.9539F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(-94.8821F, 1.0809F, 64.9539F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-94.8821F, 1.0809F, 64.9539F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_forearm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.5F, 0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.posVec(0.5F, 0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(0.5F, 0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 15.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 15.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 15.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_thumb", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.1F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.1F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.1F, -0.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_lower_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-12.5F, -40.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(-12.5F, -40.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-12.5F, -40.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 35.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 35.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 35.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_indexfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_indexfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_middlefinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-0.4344F, -2.462F, 10.0093F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(-0.4344F, -2.462F, 10.0093F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-0.4344F, -2.462F, 10.0093F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_middlefinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 42.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 42.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 42.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_ringfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.4344F, 2.462F, 10.0093F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.4344F, 2.462F, 10.0093F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.4344F, 2.462F, 10.0093F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_ringfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 40.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 40.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 40.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(1.9516F, 7.243F, 15.1235F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(1.9516F, 7.243F, 15.1235F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(1.9516F, 7.243F, 15.1235F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 35.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 35.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 35.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("waist", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.125F, KeyframeAnimations.degreeVec(70.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.degreeVec(70.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75F, KeyframeAnimations.degreeVec(-24.375F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_lower_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75F, KeyframeAnimations.degreeVec(-37.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_foot", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75F, KeyframeAnimations.degreeVec(60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.125F, KeyframeAnimations.degreeVec(-25.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.degreeVec(-25.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75F, KeyframeAnimations.degreeVec(71.5625F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_lower_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.125F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_foot", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.125F, KeyframeAnimations.degreeVec(57.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.degreeVec(57.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .build();

                public static final AnimationDefinition mad_attack = AnimationDefinition.Builder.withLength(1.0F).looping()
                        .addAnimation("shy_guy", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 7.25F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 7.25F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("upper_half", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("upper_half", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 2.75F, 1.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 2.75F, 1.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("neck", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.degreeVec(-7.0196F, 16.1675F, 8.0203F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("angry_face", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(15.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("jaw", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -6.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, -6.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-129.4452F, -40.4633F, 30.7487F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.degreeVec(-159.3344F, 54.3366F, 30.7833F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(-100.1864F, -66.3417F, 14.4962F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-129.4452F, -40.4633F, 30.7487F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_forearm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(99.7494F, 1.0696F, -62.4925F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.degreeVec(98.4101F, -5.0635F, -25.4563F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(99.4509F, 0.3067F, -58.3793F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(99.7494F, 1.0696F, -62.4925F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, -15.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(-61.1751F, 10.0703F, 26.5123F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-32.5F, 0.0F, -15.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_lower_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(36.8282F, -48.2179F, -6.6705F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(36.8282F, -48.2179F, -6.6705F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -45.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -45.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_thumb", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.5F, -0.75F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(0.5F, -0.75F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_indexfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_indexfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -37.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -37.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_middlefinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.5414F, -2.4407F, -12.5115F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.5414F, -2.4407F, -12.5115F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_middlefinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -27.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -27.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_middlefinger", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(-0.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(-0.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_ringfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-0.5414F, 2.4407F, -12.5115F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-0.5414F, 2.4407F, -12.5115F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_ringfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -32.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -32.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-1.3096F, 7.3854F, -10.0845F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-1.3096F, 7.3854F, -10.0845F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_upper_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -35.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(126.4395F, -36.802F, -25.0419F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.degreeVec(81.4448F, -19.6759F, 31.5559F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(93.8054F, 18.0742F, 57.4568F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75F, KeyframeAnimations.degreeVec(118.302F, -32.9009F, 1.5143F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(126.4395F, -36.802F, -25.0419F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -1.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_forearm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-94.8821F, 1.0809F, 64.9539F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.degreeVec(-86.1803F, 11.8725F, 134.7671F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(-91.4988F, 9.5426F, 98.9716F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-94.8821F, 1.0809F, 64.9539F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_forearm", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.5F, 0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(0.5F, 0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_hand", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 15.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.degreeVec(55.0F, 0.0F, 15.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(17.7778F, 0.0F, 15.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(22.5F, 0.0F, 15.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_thumb", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.1F, -0.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.1F, -0.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_lower_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-12.5F, -40.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-12.5F, -40.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_thumb", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 35.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 35.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_indexfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_indexfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_middlefinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(-0.4344F, -2.462F, 10.0093F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(-0.4344F, -2.462F, 10.0093F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_middlefinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 42.5F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 42.5F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_ringfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.4344F, 2.462F, 10.0093F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.4344F, 2.462F, 10.0093F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_ringfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 40.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 40.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(1.9516F, 7.243F, 15.1235F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(1.9516F, 7.243F, 15.1235F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_upper_pinkyfinger", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 35.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 35.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("neck_top", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 22.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("upper_chest", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("lower_chest", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, -15.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("upper_tumm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 12.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("lower_tumm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("waist", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.2083F, KeyframeAnimations.degreeVec(2.5095F, 4.9952F, 0.2187F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.5F, KeyframeAnimations.degreeVec(2.5029F, -1.5345F, -0.067F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(0.75F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_lower_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("l_foot", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
                                new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                                new Keyframe(0.2083F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.6F), AnimationChannel.Interpolations.LINEAR)
                        ))
                        .addAnimation("r_lower_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .addAnimation("r_foot", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                                new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                                new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
                        ))
                        .build();
        }

}
