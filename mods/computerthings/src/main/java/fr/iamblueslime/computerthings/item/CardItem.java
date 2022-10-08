package fr.iamblueslime.computerthings.item;

import fr.iamblueslime.computerthings.ComputerThings;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class CardItem extends Item {
    public static final String TAG_DATA = "data";

    public final Type type;
    private final int color;

    public CardItem(Type type, int color) {
        super(new Item.Properties().stacksTo(1).tab(ComputerThings.TAB));
        this.type = type;
        this.color = color;
    }

    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag tag = itemStack.getTag();
        if (tag != null && tag.contains(TAG_DATA))
            tooltip.add(new TranslatableComponent("computerthings.card.written").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
    }

    public int getColor() {
        return this.color;
    }

    public static void setData(ItemStack itemStack, String data) {
        itemStack.getOrCreateTag().putString(TAG_DATA, data);
    }

    public static Optional<String> getData(ItemStack itemStack)  {
        CompoundTag tag = itemStack.getTag();
        if (tag == null || !tag.contains(TAG_DATA))
            return Optional.empty();
        return Optional.of(tag.getString(TAG_DATA));
    }

    public enum Type {
        MAGNETIC("magnetic"),
        RFID("rfid");

        public final String name;

        Type(String name) {
            this.name = name;
        }
    }
}
