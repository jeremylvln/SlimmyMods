package fr.iamblueslime.computerthings.blockentity;

import fr.iamblueslime.computerthings.block.ElectronicLockBlock;
import fr.iamblueslime.computerthings.block.MagneticCardReaderBlock;
import fr.iamblueslime.computerthings.init.ModBlockEntities;
import fr.iamblueslime.computerthings.item.CardItem;
import fr.iamblueslime.computerthings.item.ElectronicPadItem;
import fr.iamblueslime.computerthings.logic.computer.IArgumentHandle;
import fr.iamblueslime.computerthings.logic.electroniclock.ElectronicPadData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class MagneticCardReaderBlockEntity extends APeripheralBlockEntity {
    public static final int ANIMATION_TICKS = 10;

    private static final String NBT_STATE = "State";
    private static final String NBT_DATA_TO_WRITE = "DataToWrite";
    private static final String NBT_SWIPE_ANIMATION_CARD = "SwipeAnimationCard";
    private static final String NBT_SWIPE_ANIMATION_TICKS = "SwipeAnimationTicks";

    private final Direction direction;

    private MagneticCardReaderBlock.EnumState state = MagneticCardReaderBlock.EnumState.IDLE;
    private String dataToWrite;
    private boolean dirtyState = true;

    private Optional<ItemStack> swipeAnimationCard = Optional.empty();
    private Optional<Integer> swipeAnimationTicks = Optional.empty();

    public MagneticCardReaderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MAGNETIC_CARD_READER.get(), pos, state);

        this.direction = state.getValue(ElectronicLockBlock.FACING);

        this.setHasEventQueue();
        this.computerMethodRegistry.register("readData", this::onMethodReadData);
        this.computerMethodRegistry.register("writeData", this::onMethodWriteData);
        this.computerMethodRegistry.register("setIdle", this::onMethodSetIdle);
        this.computerMethodRegistry.register("setBusy", this::onMethodSetBusy);
        this.computerMethodRegistry.register("setRejected", this::onMethodSetRejected);
        this.computerMethodRegistry.register("getState", this::onMethodGetState);
    }

    public boolean doCardSwipe(Player player, ItemStack stack) {
        Optional<String> cardData = CardItem.getData(stack);
        boolean swiped = false;

        if (this.state == MagneticCardReaderBlock.EnumState.WAITING_CARD) {
            this.pushEvent(new Object[] { player.getName(), cardData });
            swiped = true;
        } else if (this.state == MagneticCardReaderBlock.EnumState.WAITING_CARD_WRITE)  {
            CardItem.setData(stack, this.dataToWrite);
            this.state = MagneticCardReaderBlock.EnumState.IDLE;
            this.dirtyState = true;
            swiped = true;
        }

        if (swiped) {
            this.swipeAnimationCard = Optional.of(stack);
            this.swipeAnimationTicks = Optional.of(ANIMATION_TICKS);
            BlockState blockState = this.level.getBlockState(this.worldPosition);
            this.level.sendBlockUpdated(this.worldPosition, blockState, blockState, 2);
            this.setChanged();
        }

        return swiped;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.state = MagneticCardReaderBlock.EnumState.values()[tag.getInt(NBT_STATE)];

        if (this.state == MagneticCardReaderBlock.EnumState.WAITING_CARD_WRITE)
            this.dataToWrite = tag.getString(NBT_DATA_TO_WRITE);

        this.dirtyState = true;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(NBT_STATE, this.state.ordinal());

        if (this.state == MagneticCardReaderBlock.EnumState.WAITING_CARD_WRITE)
            tag.putString(NBT_DATA_TO_WRITE, this.dataToWrite);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        CompoundTag tag = packet.getTag();

        if (tag != null && tag.contains(NBT_SWIPE_ANIMATION_CARD)) {
            this.swipeAnimationCard = Optional.of(ItemStack.of(tag.getCompound(NBT_SWIPE_ANIMATION_CARD)));
            this.swipeAnimationTicks = Optional.of(tag.getInt(NBT_SWIPE_ANIMATION_TICKS));
        } else {
            this.swipeAnimationCard = Optional.empty();
            this.swipeAnimationTicks = Optional.empty();
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.swipeAnimationCard.ifPresent(card -> tag.put(NBT_SWIPE_ANIMATION_CARD, card.serializeNBT()));
        this.swipeAnimationTicks.ifPresent(ticks -> tag.putInt(NBT_SWIPE_ANIMATION_TICKS, ticks));
        return tag;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public Optional<ItemStack> getSwipeAnimationCard() {
        return this.swipeAnimationCard;
    }

    public Optional<Integer> getSwipeAnimationTicks() {
        return this.swipeAnimationTicks;
    }

    @Override
    public String getPeripheralName() {
        return "magnetic_card_reader";
    }

    private Object[] onMethodReadData(IArgumentHandle args) {
        this.state = MagneticCardReaderBlock.EnumState.WAITING_CARD;
        this.dirtyState = true;
        return new Object[0];
    }

    private Object[] onMethodWriteData(IArgumentHandle args) throws Exception {
        this.dataToWrite = String.valueOf(args.getString(0));
        this.state = MagneticCardReaderBlock.EnumState.WAITING_CARD_WRITE;
        this.dirtyState = true;
        return new Object[0];
    }

    private Object[] onMethodSetIdle(IArgumentHandle args) {
        this.state = MagneticCardReaderBlock.EnumState.IDLE;
        this.dirtyState = true;
        return new Object[0];
    }

    private Object[] onMethodSetBusy(IArgumentHandle args) {
        this.state = MagneticCardReaderBlock.EnumState.BUSY;
        this.dirtyState = true;
        return new Object[0];
    }

    private Object[] onMethodSetRejected(IArgumentHandle args) {
        this.state = MagneticCardReaderBlock.EnumState.REJECTED;
        this.dirtyState = true;
        return new Object[0];
    }

    private Object[] onMethodGetState(IArgumentHandle args) {
        return new Object[] { this.state.name() };
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, MagneticCardReaderBlockEntity blockEntity) {
        if (blockEntity.swipeAnimationTicks.isPresent()) {
            int newTicks = blockEntity.swipeAnimationTicks.get() - 1;
            System.out.println("new ticks " + newTicks);

            if (newTicks == 0) {
                blockEntity.swipeAnimationCard = Optional.empty();
                blockEntity.swipeAnimationTicks = Optional.empty();
            } else {
                blockEntity.swipeAnimationTicks = Optional.of(newTicks);
            }

            BlockState blockState = level.getBlockState(pos);
            level.sendBlockUpdated(pos, blockState, blockState, 2);
        }

        blockEntity.tickCommandQueue();

        if (blockEntity.dirtyState) {
            state = state.setValue(MagneticCardReaderBlock.STATE, blockEntity.state);
            setChanged(level, pos, state);
            level.setBlock(pos, state, 2);
            blockEntity.dirtyState = false;
        }
    }
}
