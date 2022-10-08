package fr.iamblueslime.computerthings.block;

import fr.iamblueslime.computerthings.blockentity.MagneticCardReaderBlockEntity;
import fr.iamblueslime.computerthings.init.ModBlockEntities;
import fr.iamblueslime.computerthings.item.CardItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

// TODO: Add light emissions on leds
public class MagneticCardReaderBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<EnumState> STATE = EnumProperty.create("state", EnumState.class);

    private static final VoxelShape NORTH_AABB = Block.box(5.0D, 4.0D, 14.0D, 11.0D, 12.0D, 16.0D);
    private static final VoxelShape SOUTH_AABB = Block.box(5.0D, 4.0D, 0.0D, 11.0D, 12.0D, 2.0D);
    private static final VoxelShape WEST_AABB = Block.box(14.0D, 4.0D, 5.0D, 16.0D, 12.0D, 11.0D);
    private static final VoxelShape EAST_AABB = Block.box(0.0D, 4.0D, 5.0D, 2.0D, 12.0D, 11.0D);

    public MagneticCardReaderBlock() {
        super(BlockBehaviour.Properties.of(Material.DECORATION)
                .strength(0.5F));
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(STATE, EnumState.IDLE));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!itemStack.isEmpty() && itemStack.getItem() instanceof CardItem && ((CardItem) itemStack.getItem()).type == CardItem.Type.MAGNETIC) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof MagneticCardReaderBlockEntity) {
                if (((MagneticCardReaderBlockEntity) blockEntity).doCardSwipe(player, itemStack)) {
                    // TODO: play sound
                    // level.playSound(player, pos, SoundEvents.WOODEN_BUTTON_CLICK_ON, SoundSource.BLOCKS, 0.3F, 0.6F);
                    level.gameEvent(player, GameEvent.BLOCK_PRESS, pos);
                    level.scheduleTick(pos, this, 1);
                }
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MagneticCardReaderBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, ModBlockEntities.MAGNETIC_CARD_READER.get(), MagneticCardReaderBlockEntity::serverTick);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case EAST -> EAST_AABB;
            case WEST -> WEST_AABB;
            case SOUTH -> SOUTH_AABB;
            default -> NORTH_AABB;
        };
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
        Direction direction = state.getValue(FACING);
        BlockPos blockpos = pos.relative(direction.getOpposite());
        return level.getBlockState(blockpos).isFaceSturdy(level, blockpos, direction);
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
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STATE);
    }

    public enum EnumState implements StringRepresentable {
        IDLE("idle"),
        WAITING_CARD("waiting_card"),
        WAITING_CARD_WRITE("waiting_card_write"),
        BUSY("busy"),
        REJECTED("rejected");

        public final String name;

        EnumState(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
