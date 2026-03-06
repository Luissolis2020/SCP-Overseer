package net.luis.scpmod.block;

import net.luis.scpmod.block.entity.ResearchTableBlockEntity;
import net.luis.scpmod.item.AnomalySampleItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class ResearchTableBlock extends BaseEntityBlock {

    public ResearchTableBlock(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof ResearchTableBlockEntity table)) return InteractionResult.PASS;

        ItemStack held = player.getItemInHand(hand);

        // Insert sample if holding one
        if (!held.isEmpty() && held.getItem() instanceof AnomalySampleItem) {
            if (player instanceof ServerPlayer sp && table.tryInsertSample(sp, held)) {
                return InteractionResult.CONSUME;
            }
        }

        // Otherwise show status (prototype)
        if (player instanceof ServerPlayer sp) {
            table.sendStatusTo(sp);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ResearchTableBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return (lvl, p, s, be) -> {
            if (be instanceof ResearchTableBlockEntity table) {
                table.tickServer();
            }
        };
    }
}

