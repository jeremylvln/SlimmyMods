package fr.iamblueslime.computerthings.blockentity;

import fr.iamblueslime.computerthings.block.MagneticCardReaderBlock;
import fr.iamblueslime.computerthings.block.RFIDWriterBlock;
import fr.iamblueslime.computerthings.init.ModBlockEntities;
import fr.iamblueslime.computerthings.item.CardItem;
import fr.iamblueslime.computerthings.logic.computer.IArgumentHandle;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class RFIDWriterBlockEntity extends APeripheralBlockEntity {
    private static final String NBT_STATE = "State";
    private static final String NBT_CARD = "Card";
    private static final String NBT_DATA_TO_WRITE = "DataToWrite";
    private static final String NBT_OPERATION_TICKS = "OperationTicks";

    private static final int TICKS_NEEDED_TO_WRITE = 20 * 5;

    private RFIDWriterBlock.EnumState state = RFIDWriterBlock.EnumState.IDLE;
    private String dataToWrite;
    private ItemStack card = ItemStack.EMPTY;
    private int operationTicks = 0;
    private boolean dirtyState = true;

    public RFIDWriterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RFID_WRITER.get(), pos, state);

        this.setHasEventQueue();
        this.computerMethodRegistry.register("writeData", this::onMethodWriteData);
        this.computerMethodRegistry.register("getProgress", this::onMethodGetProgress);
        this.computerMethodRegistry.register("getState", this::onMethodGetState);
    }

    public void doCardPlace(ItemStack stack) {
        if (!this.card.isEmpty()) {
            this.doCardTake();
            return;
        }

        this.card = stack.copy();
        stack.shrink(1);
        this.dirtyState = true;
    }

    public void doCardTake() {
        if (this.state == RFIDWriterBlock.EnumState.BUSY || this.card.isEmpty())
            return;

        this.dropContent();
        this.dirtyState = true;
    }

    public void dropContent() {
        if (!this.card.isEmpty())
            Containers.dropItemStack(this.level, this.worldPosition.getX() + 0.5D, this.worldPosition.getY(), this.worldPosition.getZ() + 0.5D, this.card);
        this.card = ItemStack.EMPTY;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.state = RFIDWriterBlock.EnumState.values()[tag.getInt(NBT_STATE)];

        if (tag.contains(NBT_CARD))
            this.card = ItemStack.of(tag.getCompound(NBT_CARD));

        if (this.state == RFIDWriterBlock.EnumState.WAITING_CARD)
            this.dataToWrite = tag.getString(NBT_DATA_TO_WRITE);
        else if (this.state == RFIDWriterBlock.EnumState.BUSY)
            this.operationTicks = tag.getInt(NBT_OPERATION_TICKS);


        this.dirtyState = true;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(NBT_STATE, this.state.ordinal());

        if (!this.card.isEmpty())
            tag.put(NBT_CARD, this.card.serializeNBT());

        if (this.state == RFIDWriterBlock.EnumState.WAITING_CARD)
            tag.putString(NBT_DATA_TO_WRITE, this.dataToWrite);
        else if (this.state == RFIDWriterBlock.EnumState.BUSY)
            tag.putInt(NBT_OPERATION_TICKS, this.operationTicks);
    }

    @Override
    public String getPeripheralName() {
        return "rfid_writer";
    }

    private Object[] onMethodWriteData(IArgumentHandle args) throws Exception {
        this.dataToWrite = String.valueOf(args.getString(0));
        this.state = RFIDWriterBlock.EnumState.WAITING_CARD;
        this.dirtyState = true;
        return new Object[0];
    }

    private Object[] onMethodGetProgress(IArgumentHandle args) {
        return new Object[] { this.operationTicks, TICKS_NEEDED_TO_WRITE };
    }

    private Object[] onMethodGetState(IArgumentHandle args) {
        return new Object[] { this.state.name() };
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, RFIDWriterBlockEntity blockEntity) {
        blockEntity.tickCommandQueue();

        if (blockEntity.state == RFIDWriterBlock.EnumState.WAITING_CARD && !blockEntity.card.isEmpty()) {
            blockEntity.state = RFIDWriterBlock.EnumState.BUSY;
            blockEntity.operationTicks = 0;
            blockEntity.dirtyState = true;
        } else if (blockEntity.state == RFIDWriterBlock.EnumState.BUSY) {
            blockEntity.operationTicks += 1;

            if (blockEntity.operationTicks >= TICKS_NEEDED_TO_WRITE) {
                CardItem.setData(blockEntity.card, blockEntity.dataToWrite);
                blockEntity.state = RFIDWriterBlock.EnumState.IDLE;
                blockEntity.operationTicks = 0;
                blockEntity.dirtyState = true;
            }
        }

        if (blockEntity.dirtyState) {
            state = state.setValue(RFIDWriterBlock.STATE, blockEntity.state).setValue(RFIDWriterBlock.FILLED, !blockEntity.card.isEmpty());
            setChanged(level, pos, state);
            level.setBlock(pos, state, 2);
            blockEntity.dirtyState = false;
        }
    }
}
