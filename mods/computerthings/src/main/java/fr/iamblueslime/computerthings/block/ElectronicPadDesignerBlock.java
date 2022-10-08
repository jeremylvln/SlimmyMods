package fr.iamblueslime.computerthings.block;

import fr.iamblueslime.computerthings.menu.ElectronicPadDesignerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ElectronicPadDesignerBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private static final Component CONTAINER_TITLE = new TranslatableComponent("computerthings.container.electronic_pad_designer");
    private static final VoxelShape RENDER_SHAPE = Shapes.box(0.0, 0.8, 0.0, 1.0, 1.0, 1.0);

    public ElectronicPadDesignerBlock() {
        super(BlockBehaviour.Properties.of(Material.GLASS)
                .strength(0.5F));
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(level, pos));
            return InteractionResult.CONSUME;
        }
    }

    @javax.annotation.Nullable
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new SimpleMenuProvider((containerId, playerInventory, player) ->
                new ElectronicPadDesignerMenu(containerId, playerInventory, ContainerLevelAccess.create(pLevel, pPos)),
                CONTAINER_TITLE);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        for (Direction direction : context.getNearestLookingDirections()) {
            BlockState blockstate;
            if (direction.getAxis() != Direction.Axis.Y) {
                blockstate = this.defaultBlockState().setValue(FACING, direction.getOpposite());
                if (blockstate.canSurvive(context.getLevel(), context.getClickedPos())) {
                    return blockstate;
                }
            }
        }

        return null;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.below();
        return level.getBlockState(blockpos).isFaceSturdy(level, blockpos, Direction.UP);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing == state.getValue(FACING).getOpposite() && !state.canSurvive(level, currentPos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter reader, BlockPos pos) {
        return RENDER_SHAPE;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
