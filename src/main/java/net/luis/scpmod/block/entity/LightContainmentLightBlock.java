package net.luis.scpmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class LightContainmentLightBlock extends Block {

    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    public LightContainmentLightBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.STONE)
                .noOcclusion()
                .sound(SoundType.STONE)
                .lightLevel(state -> state.getValue(LIT) ? 15 : 0)
                .isSuffocating((s, l, p) -> false)
                .isViewBlocking((s, l, p) -> false)
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, true)); // default ON
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    // Optional: override onUse if you want to toggle manually
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            boolean current = state.getValue(LIT);
            level.setBlock(pos, state.setValue(LIT, !current), 3);
        }
        return InteractionResult.SUCCESS;
    }
}
