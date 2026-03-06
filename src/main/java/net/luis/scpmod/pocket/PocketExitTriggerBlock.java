package net.luis.scpmod.pocket;

import net.luis.scpmod.worldgen.dimension.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class PocketExitTriggerBlock extends Block {
    public PocketExitTriggerBlock() {
        super(BlockBehaviour.Properties.of()
                .noCollission()
                .noOcclusion()
                .strength(-1.0F, 3600000.0F));
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide) return;
        if (!(entity instanceof ServerPlayer sp)) return;

        // Only exits from pocket dimension (adjust key if yours differs)
        if (!level.dimension().equals(ModDimensions.POCKET_DIM_LEVEL)) return;

        if (!PocketReturnData.hasReturn(sp)) return;

        PocketReturnData.Return ret = PocketReturnData.read(sp);
        ServerLevel dest = sp.server.getLevel(ret.dim());
        if (dest == null) return;

        BlockPos safe = findSafeReturnSpot(dest, ret.pos());

        sp.teleportTo(dest,
                safe.getX() + 0.5,
                safe.getY(),
                safe.getZ() + 0.5,
                ret.yaw(),
                ret.pitch()
        );

        PocketReturnData.clear(sp);
        PocketReturnData.endPocketSession(sp);
    }

    private static BlockPos findSafeReturnSpot(ServerLevel level, BlockPos base) {
        // Make sure chunks are loaded
        level.getChunk(base);

        for (int dy = 0; dy <= 6; dy++) {
            BlockPos p = base.above(dy);
            if (isStandable(level, p)) return p;
        }

        // Fallback: drop them on top of terrain near the original XZ
        BlockPos hm = level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, base);
        BlockPos p = hm.above();
        return isStandable(level, p) ? p : base.above(1);
    }

    @Override public RenderShape getRenderShape(BlockState s) { return RenderShape.INVISIBLE; }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        return -1.0F;
    }

    private static boolean isStandable(ServerLevel level, BlockPos feet) {
        // needs: feet + head air, and block below solid enough
        if (!level.getBlockState(feet).getCollisionShape(level, feet).isEmpty()) return false;
        if (!level.getBlockState(feet.above()).getCollisionShape(level, feet.above()).isEmpty()) return false;

        BlockPos below = feet.below();
        return !level.getBlockState(below).getCollisionShape(level, below).isEmpty();
    }
}

