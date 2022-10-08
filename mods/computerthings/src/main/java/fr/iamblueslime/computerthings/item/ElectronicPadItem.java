package fr.iamblueslime.computerthings.item;

import fr.iamblueslime.computerthings.ComputerThings;
import fr.iamblueslime.computerthings.logic.electroniclock.ElectronicPadData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ElectronicPadItem extends Item {
    private static final String NBT_PAD = "Pad";

    public ElectronicPadItem() {
        super(new Item.Properties().tab(ComputerThings.TAB));
    }

    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (!itemStack.hasTag() || !itemStack.getTag().contains(NBT_PAD))
            return;

        ElectronicPadData padData = getPadData(itemStack);
        if (padData == null)
            return;

        tooltip.add(new TranslatableComponent("computerthings.electronic_pad.contains")
                .setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));

        for (int i = 0; i < ElectronicPadData.ENTRIES_NB; i += 1)
            if (padData.getEntry(i) != null)
                tooltip.add(new TranslatableComponent("computerthings.electronic_pad.entry", i + 1, padData.getEntry(i).getStack().getDisplayName())
                        .setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
    }

    public static void setPadData(ItemStack stack, ElectronicPadData padData) {
        if (padData == null) {
            stack.setTag(null);
            return;
        }

        if (!stack.hasTag())
            stack.setTag(new CompoundTag());
        stack.getOrCreateTag().put(NBT_PAD, padData.serializeNBT());
    }

    public static ElectronicPadData getPadData(ItemStack stack) {
        if (!stack.hasTag() || !stack.getTag().contains(NBT_PAD))
            return null;

        ElectronicPadData padData = new ElectronicPadData();
        padData.deserializeNBT(stack.getTag().getList(NBT_PAD, 10));
        return padData;
    }
}
