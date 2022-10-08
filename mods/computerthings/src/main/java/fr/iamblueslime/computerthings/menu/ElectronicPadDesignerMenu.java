package fr.iamblueslime.computerthings.menu;

import fr.iamblueslime.computerthings.init.ModItems;
import fr.iamblueslime.computerthings.init.ModMenus;
import fr.iamblueslime.computerthings.item.ElectronicPadItem;
import fr.iamblueslime.computerthings.logic.electroniclock.ElectronicPadData;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ElectronicPadDesignerMenu extends AbstractContainerMenu {
    private static final int PAD_INPUT_SLOT = 0;
    private static final int PAD_OUTPUT_SLOT = 1;
    private static final int ICON_INPUT_SLOT_START = 2;
    private static final int ICON_INPUT_SLOT_END = ICON_INPUT_SLOT_START + ElectronicPadData.ENTRIES_NB;
    private static final int PLAYER_INVENTORY_SLOT_START = ICON_INPUT_SLOT_END;
    private static final int PLAYER_INVENTORY_SLOT_END = PLAYER_INVENTORY_SLOT_START + (9 * 3);
    private static final int PLAYER_ROW_SLOT_START = PLAYER_INVENTORY_SLOT_END;
    private static final int PLAYER_ROW_SLOT_END = PLAYER_ROW_SLOT_START + 9;

    private final Player player;
    private final ContainerLevelAccess access;
    private final Container craftingSlots;

    public ElectronicPadDesignerMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public ElectronicPadDesignerMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(ModMenus.ELECTRONIC_PAD_DESIGNER.get(), containerId);

        this.player = playerInventory.player;
        this.access = access;
        this.craftingSlots = new SimpleContainer(ElectronicPadData.ENTRIES_NB + 2);

        // ? this.inputSlots.startOpen(playerInventory.player);

        this.addSlot(new PadSlot(this, this.craftingSlots, PAD_INPUT_SLOT, 127, 36));
        this.addSlot(new ResultSlot(this.craftingSlots, PAD_OUTPUT_SLOT, 127, 80));

        for (int y = 0; y < 4; y += 1)
            for (int x = 0; x < 3; x += 1)
                this.addSlot(new InputSlot(this.craftingSlots, ICON_INPUT_SLOT_START + (y * 3) + x, 26 + x * 26, 20 + y * 26));

        for (int y = 0; y < 3; y += 1)
            for (int x = 0; x < 9; x += 1)
                this.addSlot(new Slot(playerInventory, (y * 9) + x + 9, 8 + x * 18, 132 + y * 18));

        for (int x = 0; x < 9; x += 1)
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 190));
    }

    public void craftPad() {
        if (!this.isCorrectlyFilled())
            return;

        ItemStack result = new ItemStack(ModItems.ELECTRONIC_PAD.get(), 1);
        ElectronicPadData.Builder padDataBuilder = new ElectronicPadData.Builder();

        for (int i = 0; i < ElectronicPadData.ENTRIES_NB; i += 1) {
            padDataBuilder.with(i, this.getSlot(ICON_INPUT_SLOT_START + i).getItem().copy());
            this.getSlot(ICON_INPUT_SLOT_START + i).remove(1);
        }

        ElectronicPadItem.setPadData(result, padDataBuilder.build());
        this.getSlot(PAD_INPUT_SLOT).remove(1);
        this.getSlot(PAD_OUTPUT_SLOT).set(result);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, pos) -> this.clearContainer(player, this.craftingSlots));
    }

    @Override
    public boolean stillValid(Player player) {
        return this.craftingSlots.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack finalStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            finalStack = slotStack.copy();

            if (index == PAD_INPUT_SLOT || index == PAD_OUTPUT_SLOT) {
                if (!this.moveItemStackTo(slotStack, PLAYER_INVENTORY_SLOT_START, PLAYER_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= PLAYER_INVENTORY_SLOT_START) {
                if (slotStack.getItem() == ModItems.ELECTRONIC_PAD.get()) {
                    if (!this.moveItemStackTo(slotStack, PAD_INPUT_SLOT, PAD_INPUT_SLOT + 1, false))
                        return ItemStack.EMPTY;
                } else if (!this.moveItemStackTo(slotStack, ICON_INPUT_SLOT_START, ICON_INPUT_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                } else if (index < PLAYER_ROW_SLOT_START) {
                    if (!this.moveItemStackTo(slotStack, PLAYER_ROW_SLOT_START, PLAYER_ROW_SLOT_END, false))
                        return ItemStack.EMPTY;
                } else if (!this.moveItemStackTo(slotStack, PLAYER_INVENTORY_SLOT_START, PLAYER_INVENTORY_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty())
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();

            if (slotStack.getCount() == finalStack.getCount())
                return ItemStack.EMPTY;
        }

        return finalStack;
    }

    private void importPadConfiguration() {
        Slot slot = this.getSlot(PAD_INPUT_SLOT);

        if (!slot.hasItem())
            return;

        ElectronicPadData padData = ElectronicPadItem.getPadData(slot.getItem());

        if (padData == null)
            return;

        for (int i = 0; i < ElectronicPadData.ENTRIES_NB; i += 1) {
            Slot entrySlot = this.getSlot(ICON_INPUT_SLOT_START + i);

            if (!entrySlot.getItem().isEmpty())
                this.player.drop(entrySlot.getItem(), false);

            entrySlot.set(padData.getEntry(i).getStack());
        }

        ElectronicPadItem.setPadData(slot.getItem(), null);
        slot.setChanged();
    }

    public boolean isCorrectlyFilled() {
        for (int i = ICON_INPUT_SLOT_START; i < ICON_INPUT_SLOT_END; i += 1)
            if (!this.getSlot(i).hasItem())
                return false;

        return this.getSlot(PAD_INPUT_SLOT).hasItem() && !this.getSlot(PAD_OUTPUT_SLOT).hasItem();
    }

    static class PadSlot extends Slot {
        private final ElectronicPadDesignerMenu menu;

        public PadSlot(ElectronicPadDesignerMenu menu, Container container, int slot, int x, int y) {
            super(container, slot, x, y);
            this.menu = menu;
        }

        @Override
        public void setChanged() {
            super.setChanged();
            this.menu.importPadConfiguration();
        }

        public boolean mayPlace(ItemStack stack) {
            return stack.getItem() == ModItems.ELECTRONIC_PAD.get();
        }

        public int getMaxStackSize() {
            return 64;
        }
    }

    static class ResultSlot extends Slot {
        public ResultSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        public int getMaxStackSize() {
            return 1;
        }
    }

    static class InputSlot extends Slot {
        public InputSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        public int getMaxStackSize() {
            return 1;
        }
    }
}
