package net.luis.scpmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
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

public class CorrodedLightBlock extends Block {

    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    // Quiet baseline: 1–6 seconds between "checks"
    private static final int QUIET_MIN_DELAY = 20;
    private static final int QUIET_MAX_DELAY = 120;

    // Most ticks do nothing; occasional flip
    private static final float QUIET_TOGGLE_CHANCE = 0.20f;

    // Rare burst flicker
    private static final float BURST_START_CHANCE = 0.03f;
    private static final int BURST_MIN_TOGGLES = 2;
    private static final int BURST_MAX_TOGGLES = 6;
    private static final int BURST_MIN_DELAY = 2;
    private static final int BURST_MAX_DELAY = 6;

    public CorrodedLightBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.BEDROCK)
                .noOcclusion()
                .sound(SoundType.STONE)
                .lightLevel(state -> state.getValue(LIT) ? 15 : 0)
                .isSuffocating((s, l, p) -> false)
                .isViewBlocking((s, l, p) -> false)
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.isClientSide) {
            scheduleQuiet((ServerLevel) level, pos, level.random);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getBlockState(pos).getBlock() != this) return;

        // Rarely start a burst: quick toggles for a short time
        if (random.nextFloat() < BURST_START_CHANCE) {
            int toggles = BURST_MIN_TOGGLES + random.nextInt(BURST_MAX_TOGGLES - BURST_MIN_TOGGLES + 1);
            runBurst(level, pos, state, random, toggles);
            return;
        }

        // Otherwise, mostly quiet: sometimes flip once, then wait a while
        if (random.nextFloat() < QUIET_TOGGLE_CHANCE) {
            boolean current = state.getValue(LIT);
            level.setBlock(pos, state.setValue(LIT, !current), 3);
        }

        scheduleQuiet(level, pos, random);
    }

    private void runBurst(ServerLevel level, BlockPos pos, BlockState state, RandomSource random, int remainingToggles) {
        // Toggle immediately
        boolean current = state.getValue(LIT);
        level.setBlock(pos, state.setValue(LIT, !current), 3);

        // If more toggles remain, schedule another quick tick
        if (remainingToggles > 1) {
            int delay = BURST_MIN_DELAY + random.nextInt(BURST_MAX_DELAY - BURST_MIN_DELAY + 1);
            // Store remaining toggles implicitly by chaining: next tick will decide burst again.
            // To make it deterministic per-burst, you'd use a BlockEntity—this is the lightweight version.
            level.scheduleTick(pos, this, delay);
        } else {
            scheduleQuiet(level, pos, random);
        }
    }

    private void scheduleQuiet(ServerLevel level, BlockPos pos, RandomSource random) {
        int delay = QUIET_MIN_DELAY + random.nextInt(QUIET_MAX_DELAY - QUIET_MIN_DELAY + 1);
        level.scheduleTick(pos, this, delay);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        return InteractionResult.PASS;
    }
}