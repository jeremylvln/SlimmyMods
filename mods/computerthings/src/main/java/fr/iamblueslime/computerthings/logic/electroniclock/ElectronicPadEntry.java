package fr.iamblueslime.computerthings.logic.electroniclock;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Optional;

public class ElectronicPadEntry implements INBTSerializable<CompoundTag> {
    private static final String NBT_PAD_POSITION = "PadPosition";
    private static final String NBT_STACK = "Item";

    private int padPosition;
    private ItemStack stack;

    public ElectronicPadEntry(int padPosition) {
        this(padPosition, ItemStack.EMPTY);
    }

    public ElectronicPadEntry(int padPosition, ItemStack stack) {
        this.padPosition = padPosition;
        this.stack = stack;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt(NBT_PAD_POSITION, this.padPosition);
        tag.put(NBT_STACK, this.stack.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.padPosition = tag.getInt(NBT_PAD_POSITION);
        this.stack = ItemStack.of(tag.getCompound(NBT_STACK));
    }

    public int getPadPosition() {
        return this.padPosition;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public static Optional<Integer> getPadPositionFromHitPoint(Vec3 hitPos, Direction direction) {
        for (int i = 0; i < ElectronicPadData.ENTRIES_NB; i += 1) {
            Vec3 relative = getEntryRelativeVec(i, direction);
            AABB buttonAABB = new AABB(
                    relative.x, relative.y, relative.z,
                    relative.x + 0.0625D, relative.y + 0.0625D, relative.z + 0.0625D
            ).inflate(0.002D);

            if (buttonAABB.contains(hitPos))
                return Optional.of(i);
        }

        return Optional.empty();
    }

    public static Vec3 getEntryRelativeVec(int padPosition, Direction direction) {
        double padColumn = padPosition % 3;
        double padLine = Math.floor(padPosition / 3.0D);

        return switch (direction) {
            case SOUTH -> new Vec3(
                    0.375D + (padColumn * 0.0625D) + (padColumn * 0.03125),
                    0.59375D - (padLine * 0.0625D) - (padLine * 0.03125),
                    0.09375D
            );
            case NORTH -> new Vec3(
                    0.5625D - (padColumn * 0.0625D) - (padColumn * 0.03125),
                    0.59375D - (padLine * 0.0625D) - (padLine * 0.03125),
                    0.84375D
            );
            case WEST -> new Vec3(
                    0.84375D,
                    0.59375D - (padLine * 0.0625D) - (padLine * 0.03125),
                    0.375D + (padColumn * 0.0625D) + (padColumn * 0.03125)
            );
            case EAST -> new Vec3(
                    0.09375D,
                    0.59375D - (padLine * 0.0625D) - (padLine * 0.03125),
                    0.5625D - (padColumn * 0.0625D) - (padColumn * 0.03125)
            );
            default -> throw new IllegalStateException("Invalid direction");
        };
    }
}
