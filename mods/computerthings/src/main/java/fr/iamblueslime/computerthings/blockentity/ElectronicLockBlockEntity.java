package fr.iamblueslime.computerthings.blockentity;

import fr.iamblueslime.computerthings.block.ElectronicLockBlock;
import fr.iamblueslime.computerthings.init.ModBlockEntities;
import fr.iamblueslime.computerthings.init.ModItems;
import fr.iamblueslime.computerthings.item.ElectronicPadItem;
import fr.iamblueslime.computerthings.logic.computer.IArgumentHandle;
import fr.iamblueslime.computerthings.logic.electroniclock.ElectronicPadData;
import fr.iamblueslime.computerthings.logic.electroniclock.ElectronicPadEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ElectronicLockBlockEntity extends APeripheralBlockEntity {
    private static final String NBT_STATE = "State";
    private static final String NBT_PAD = "Pad";
    private static final String NBT_CLICKED_LIST = "Clicked";

    private final Direction direction;
    private final Map<Integer, Integer> clickedButtonsTimeouts = new HashMap<>();

    private ElectronicLockBlock.EnumState state = ElectronicLockBlock.EnumState.IDLE;
    private ItemStack pad = ItemStack.EMPTY;
    private Optional<ElectronicPadData> padData = Optional.empty();
    private boolean dirtyState = true;

    public ElectronicLockBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ELECTRONIC_LOCK.get(), pos, state);

        this.direction = state.getValue(ElectronicLockBlock.FACING);

        this.setHasEventQueue();
        this.computerMethodRegistry.register("acceptInputs", this::onMethodAcceptInputs);
        this.computerMethodRegistry.register("setIdle", this::onMethodSetIdle);
        this.computerMethodRegistry.register("setRejected", this::onMethodSetRejected);
        this.computerMethodRegistry.register("getState", this::onMethodGetState);
    }

    public boolean doClickButton(Player player, Vec3 hitPos) {
        if (this.pad.isEmpty())
            return false;

        Optional<Integer> padPosition = ElectronicPadEntry.getPadPositionFromHitPoint(hitPos, this.direction);

        if (padPosition.isEmpty())
            return false;

        if (this.clickedButtonsTimeouts.containsKey(padPosition.get()))
            return false;

        this.pushEvent(new Object[] { player.getName(), padPosition });
        this.clickedButtonsTimeouts.put(padPosition.get(), 20);

        BlockState blockState = this.level.getBlockState(this.worldPosition);
        this.level.sendBlockUpdated(this.worldPosition, blockState, blockState, 2);
        return true;
    }

    public void doPadMovement(ItemStack itemStack) {
        if (!this.pad.isEmpty()) {
            this.dropContent();
            BlockState blockState = this.level.getBlockState(this.worldPosition);
            this.level.sendBlockUpdated(this.worldPosition, blockState, blockState, 2);
            return;
        }

        if (!itemStack.is(ModItems.ELECTRONIC_PAD.get()))
            return;

        this.pad = itemStack.copy();
        this.pad.setCount(1);

        this.padData = Optional.of(ElectronicPadItem.getPadData(this.pad));
        itemStack.shrink(1);

        BlockState blockState = this.level.getBlockState(this.worldPosition);
        this.level.sendBlockUpdated(this.worldPosition, blockState, blockState, 2);
    }

    public void dropContent() {
        if (!this.pad.isEmpty())
            Containers.dropItemStack(this.level, this.worldPosition.getX() + 0.5D, this.worldPosition.getY(), this.worldPosition.getZ() + 0.5D, this.pad);
        this.pad = ItemStack.EMPTY;
        this.padData = Optional.empty();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.state = ElectronicLockBlock.EnumState.values()[tag.getInt(NBT_STATE)];

        if (tag.contains(NBT_PAD)) {
            this.pad = ItemStack.of(tag.getCompound(NBT_PAD));
            this.padData = Optional.of(ElectronicPadItem.getPadData(this.pad));
        }

        this.dirtyState = true;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(NBT_STATE, this.state.ordinal());

        if (!this.pad.isEmpty())
            tag.put(NBT_PAD, this.pad.serializeNBT());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        CompoundTag tag = packet.getTag();

        if (tag != null && tag.contains(NBT_PAD)) {
            this.pad = ItemStack.of(tag.getCompound(NBT_PAD));
            this.padData = Optional.of(ElectronicPadItem.getPadData(this.pad));

            int[] clickedArray = tag.getIntArray(NBT_CLICKED_LIST);
            this.clickedButtonsTimeouts.clear();

            for (int i = 0; i < clickedArray.length; i += 1)
                if (clickedArray[i] > 0)
                    this.clickedButtonsTimeouts.put(i, clickedArray[i]);
        } else {
            this.pad = ItemStack.EMPTY;
            this.padData = Optional.empty();
            this.clickedButtonsTimeouts.clear();
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();

        if (!this.pad.isEmpty())
            tag.put(NBT_PAD, this.pad.serializeNBT());

        int[] clickedArray = new int[ElectronicPadData.ENTRIES_NB];
        this.clickedButtonsTimeouts.forEach((position, ticks) -> clickedArray[position] = ticks);
        tag.putIntArray(NBT_CLICKED_LIST, clickedArray);

        return tag;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public Optional<ElectronicPadData> getPadData() {
        return this.padData;
    }

    public boolean isClicked(int index) {
        return this.clickedButtonsTimeouts.containsKey(index);
    }

    @Override
    public String getPeripheralName() {
        return "electronic_lock";
    }

    private Object[] onMethodAcceptInputs(IArgumentHandle args) throws Exception {
        boolean flag = args.getBoolean(0);

        if (flag)
            this.state = ElectronicLockBlock.EnumState.WAITING_INPUT;
        else
            this.state = ElectronicLockBlock.EnumState.IDLE;

        this.dirtyState = true;
        return new Object[0];
    }

    private Object[] onMethodSetIdle(IArgumentHandle args) {
        this.state = ElectronicLockBlock.EnumState.IDLE;
        this.dirtyState = true;
        return new Object[0];
    }

    private Object[] onMethodSetRejected(IArgumentHandle args) {
        this.state = ElectronicLockBlock.EnumState.REJECTED;
        this.dirtyState = true;
        return new Object[0];
    }

    private Object[] onMethodGetState(IArgumentHandle args) {
        return new Object[] { this.state.name() };
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ElectronicLockBlockEntity blockEntity) {
        blockEntity.clickedButtonsTimeouts.forEach((position, ticks) -> blockEntity.clickedButtonsTimeouts.put(position, ticks - 1));

        if (blockEntity.clickedButtonsTimeouts.values().removeIf(ticks -> ticks <= 0)) {
            BlockState blockState = level.getBlockState(pos);
            level.playSound(null, pos, SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundSource.BLOCKS, 0.3F, 0.5F);
            level.gameEvent(GameEvent.BLOCK_UNPRESS, pos);
            level.sendBlockUpdated(pos, blockState, blockState, 2);
        }

        blockEntity.tickCommandQueue();

        if (blockEntity.dirtyState) {
            state = state.setValue(ElectronicLockBlock.STATE, blockEntity.state);
            setChanged(level, pos, state);
            level.setBlock(pos, state, 2);
            blockEntity.dirtyState = false;
        }
    }
}
