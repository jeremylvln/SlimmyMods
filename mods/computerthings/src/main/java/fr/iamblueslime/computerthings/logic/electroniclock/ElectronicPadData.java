package fr.iamblueslime.computerthings.logic.electroniclock;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.util.INBTSerializable;

public class ElectronicPadData implements INBTSerializable<ListTag> {
    public static final int ENTRIES_NB = 12;

    public static final ElectronicPadData ITEMS = new Builder()
            .with(0, new ItemStack(Blocks.OAK_LOG))
            .with(1, new ItemStack(Blocks.SAND))
            .with(2, new ItemStack(Blocks.COBBLESTONE))
            .with(3, new ItemStack(Blocks.QUARTZ_BLOCK))
            .with(4, new ItemStack(Blocks.PRISMARINE_BRICKS))
            .with(5, new ItemStack(Blocks.BEDROCK))
            .with(6, new ItemStack(Blocks.GLOWSTONE))
            .with(7, new ItemStack(Blocks.MAGMA_BLOCK))
            .with(8, new ItemStack(Blocks.GLASS))
            .with(9, new ItemStack(Blocks.RED_TERRACOTTA))
            .with(10, new ItemStack(Blocks.COAL_BLOCK))
            .with(11, new ItemStack(Blocks.GREEN_TERRACOTTA))
            .build();

    private final ElectronicPadEntry[] entries;

    public ElectronicPadData() {
        this(new ElectronicPadEntry[ENTRIES_NB]);
    }

    private ElectronicPadData(ElectronicPadEntry[] entries) {
        this.entries = entries;
    }

    @Override
    public ListTag serializeNBT() {
        ListTag list = new ListTag();

        for (int i = 0; i < ENTRIES_NB; i += 1)
            if (this.entries[i] != null)
                list.add(this.entries[i].serializeNBT());

        return list;
    }

    @Override
    public void deserializeNBT(ListTag list) {
        for (int i = 0; i < ENTRIES_NB; i += 1) {
            this.entries[i] = new ElectronicPadEntry(i);
            this.entries[i].deserializeNBT(list.getCompound(i));
        }
    }

    public ElectronicPadEntry getEntry(int index) {
        return this.entries[index];
    }

    public static class Builder {
        private final ElectronicPadEntry[] entries;

        public Builder() {
            this.entries = new ElectronicPadEntry[ENTRIES_NB];
        }

        public ElectronicPadData build() {
            if (!this.validate())
                throw new IllegalStateException("A pad entry cannot be null");

            return new ElectronicPadData(this.entries);
        }


        public Builder with(int pos, ItemStack stack) {
            this.entries[pos] = new ElectronicPadEntry(pos, stack);
            return this;
        }

        public boolean validate() {
            for (int i = 0; i < ENTRIES_NB; i += 1)
                if (this.entries[i] == null)
                    return false;

            return true;
        }
    }
}
