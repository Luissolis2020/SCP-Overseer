package net.luis.scpmod.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "startSleeping", at = @At("HEAD"), cancellable = true)
    private void forceDaytimeSleep(BlockPos pPos, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity.getPersistentData().getBoolean("scp2295_force_sleep")) {

            // Get the bed block state
            BlockState bedState = entity.level().getBlockState(pPos);
            if (!(bedState.getBlock() instanceof BedBlock)) {
                return;
            }

            // Check if this is the foot of the bed
            if (bedState.getValue(BedBlock.PART) == BedPart.FOOT) {
                // Move to the head of the bed
                Direction bedFacing = bedState.getValue(BedBlock.FACING);
                pPos = pPos.relative(bedFacing); // Move in the facing direction
            }

            // Adjust position so the head is on the pillow
            Direction bedFacing = bedState.getValue(BedBlock.FACING);
            double headX = pPos.getX() + 0.5;
            double headY = pPos.getY() + 0.6875;
            double headZ = pPos.getZ() + 0.5;

            switch (bedFacing) {
                case NORTH:
                    headZ -= 0.4; // Move toward the pillow
                    break;
                case SOUTH:
                    headZ += 0.4;
                    break;
                case WEST:
                    headX -= 0.4;
                    break;
                case EAST:
                    headX += 0.4;
                    break;
                default:
                    break;
            }

            entity.setSleepingPos(pPos);
            entity.setPos(headX, headY, headZ);
            entity.setPose(Pose.SLEEPING);
            entity.setDeltaMovement(0, 0, 0);

            ci.cancel(); // Prevent default behavior
        }
    }
}


