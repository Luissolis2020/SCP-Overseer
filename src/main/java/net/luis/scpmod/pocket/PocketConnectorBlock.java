package net.luis.scpmod.pocket;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Locale;

public class PocketConnectorBlock extends Block {
    public enum ConnectorType implements StringRepresentable {
        ANY, HALL, ROOM, TRAP, EXIT, START, DEAD_END;

        @Override public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    public static final DirectionProperty FACING =
            DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

    public static final EnumProperty<ConnectorType> TYPE =
            EnumProperty.create("ctype", ConnectorType.class);

    public PocketConnectorBlock() {
        super(BlockBehaviour.Properties.of()
                .noOcclusion()
                .noCollission()
                .strength(0.0F));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(TYPE, ConnectorType.ANY));
    }

    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(FACING, TYPE);
    }

    @Override public RenderShape getRenderShape(BlockState s) { return RenderShape.INVISIBLE; }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        // Face the direction the player is looking (or use .getHorizontalDirection().getOpposite() if you want outward)
        Direction d = ctx.getHorizontalDirection();
        return this.defaultBlockState()
                .setValue(FACING, d)
                .setValue(TYPE, ConnectorType.ANY);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        if (player.isShiftKeyDown()) {
            // sneak-right-click: rotate facing
            state = state.setValue(FACING, state.getValue(FACING).getClockWise());
        } else {
            // right-click: cycle connector type
            state = state.cycle(TYPE);
        }
        level.setBlock(pos, state, 3);

        // Send a debug message to the player
        player.displayClientMessage(
                net.minecraft.network.chat.Component.literal(
                        "Connector set to FACING=" + state.getValue(FACING) +
                                " TYPE=" + state.getValue(TYPE)),
                true
        );

        return InteractionResult.CONSUME;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        if (rand.nextInt(14) != 0) return; // lower = more frequent
        Direction d = state.getValue(FACING);
        Vec3 base = Vec3.atCenterOf(pos).add(0, 0.25, 0);
        Vec3 dir  = Vec3.atLowerCornerOf(d.getNormal()).scale(0.35);

        // a short arrow “shaft”
        for (int i = 0; i < 5; i++) {
            Vec3 p = base.add(dir.scale(i * 0.35));
            level.addParticle(ParticleTypes.END_ROD, p.x, p.y, p.z, 0, 0, 0);
        }
        // arrow tip
        Vec3 tip = base.add(dir.scale(5 * 0.35));
        level.addParticle(ParticleTypes.CRIT, tip.x, tip.y, tip.z, 0, 0, 0);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }


}

