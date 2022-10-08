package fr.iamblueslime.computerthings.init;

import fr.iamblueslime.computerthings.ComputerThings;
import fr.iamblueslime.computerthings.item.CardItem;
import fr.iamblueslime.computerthings.item.ElectronicPadItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ComputerThings.MODID);

    public static final RegistryObject<Item> MAGNETIC_CARD_WHITE = ITEMS.register(
            "magnetic_card_white", () -> new CardItem(CardItem.Type.MAGNETIC, DyeColor.WHITE.getFireworkColor()));
    public static final RegistryObject<Item> MAGNETIC_CARD_ORANGE = ITEMS.register(
            "magnetic_card_orange", () -> new CardItem(CardItem.Type.MAGNETIC, DyeColor.ORANGE.getFireworkColor()));
    public static final RegistryObject<Item> MAGNETIC_CARD_MAGENTA = ITEMS.register(
            "magnetic_card_magenta", () -> new CardItem(CardItem.Type.MAGNETIC, DyeColor.MAGENTA.getFireworkColor()));
    public static final RegistryObject<Item> MAGNETIC_CARD_LIGHT_BLUE = ITEMS.register(
            "magnetic_card_light_blue", () -> new CardItem(CardItem.Type.MAGNETIC, DyeColor.LIGHT_BLUE.getFireworkColor()));
    public static final RegistryObject<Item> MAGNETIC_CARD_YELLOW = ITEMS.register(
            "magnetic_card_yellow", () -> new CardItem(CardItem.Type.MAGNETIC, DyeColor.YELLOW.getFireworkColor()));
    public static final RegistryObject<Item> MAGNETIC_CARD_LIME = ITEMS.register(
            "magnetic_card_lime", () -> new CardItem(CardItem.Type.MAGNETIC, DyeColor.LIME.getFireworkColor()));
    public static final RegistryObject<Item> MAGNETIC_CARD_PINK = ITEMS.register(
            "magnetic_card_pink", () -> new CardItem(CardItem.Type.MAGNETIC, DyeColor.PINK.getFireworkColor()));
    public static final RegistryObject<Item> MAGNETIC_CARD_GRAY = ITEMS.register(
            "magnetic_card_gray", () -> new CardItem(CardItem.Type.MAGNETIC, DyeColor.GRAY.getFireworkColor()));
    public static final RegistryObject<Item> MAGNETIC_CARD_LIGHT_GRAY = ITEMS.register(
            "magnetic_card_light_gray", () -> new CardItem(CardItem.Type.MAGNETIC, DyeColor.LIGHT_GRAY.getFireworkColor()));
    public static final RegistryObject<Item> MAGNETIC_CARD_CYAN = ITEMS.register(
            "magnetic_card_cyan", () -> new CardItem(CardItem.Type.MAGNETIC, DyeColor.CYAN.getFireworkColor()));
    public static final RegistryObject<Item> MAGNETIC_CARD_PURPLE = ITEMS.register(
            "magnetic_card_purple", () -> new CardItem(CardItem.Type.MAGNETIC, DyeColor.PURPLE.getFireworkColor()));
    public static final RegistryObject<Item> MAGNETIC_CARD_BLUE = ITEMS.register(
            "magnetic_card_blue", () -> new CardItem(CardItem.Type.MAGNETIC, DyeColor.BLUE.getFireworkColor()));
    public static final RegistryObject<Item> MAGNETIC_CARD_BROWN = ITEMS.register(
            "magnetic_card_brown", () -> new CardItem(CardItem.Type.MAGNETIC, DyeColor.BROWN.getFireworkColor()));
    public static final RegistryObject<Item> MAGNETIC_CARD_GREEN = ITEMS.register(
            "magnetic_card_green", () -> new CardItem(CardItem.Type.MAGNETIC, DyeColor.GREEN.getFireworkColor()));
    public static final RegistryObject<Item> MAGNETIC_CARD_RED = ITEMS.register(
            "magnetic_card_red", () -> new CardItem(CardItem.Type.MAGNETIC, DyeColor.RED.getFireworkColor()));
    public static final RegistryObject<Item> MAGNETIC_CARD_BLACK = ITEMS.register(
            "magnetic_card_black", () -> new CardItem(CardItem.Type.MAGNETIC, DyeColor.BLACK.getFireworkColor()));

    public static final RegistryObject<Item> RFID_CARD_WHITE = ITEMS.register(
            "rfid_card_white", () -> new CardItem(CardItem.Type.RFID, DyeColor.WHITE.getFireworkColor()));
    public static final RegistryObject<Item> RFID_CARD_ORANGE = ITEMS.register(
            "rfid_card_orange", () -> new CardItem(CardItem.Type.RFID, DyeColor.ORANGE.getFireworkColor()));
    public static final RegistryObject<Item> RFID_CARD_MAGENTA = ITEMS.register(
            "rfid_card_magenta", () -> new CardItem(CardItem.Type.RFID, DyeColor.MAGENTA.getFireworkColor()));
    public static final RegistryObject<Item> RFID_CARD_LIGHT_BLUE = ITEMS.register(
            "rfid_card_light_blue", () -> new CardItem(CardItem.Type.RFID, DyeColor.LIGHT_BLUE.getFireworkColor()));
    public static final RegistryObject<Item> RFID_CARD_YELLOW = ITEMS.register(
            "rfid_card_yellow", () -> new CardItem(CardItem.Type.RFID, DyeColor.YELLOW.getFireworkColor()));
    public static final RegistryObject<Item> RFID_CARD_LIME = ITEMS.register(
            "rfid_card_lime", () -> new CardItem(CardItem.Type.RFID, DyeColor.LIME.getFireworkColor()));
    public static final RegistryObject<Item> RFID_CARD_PINK = ITEMS.register(
            "rfid_card_pink", () -> new CardItem(CardItem.Type.RFID, DyeColor.PINK.getFireworkColor()));
    public static final RegistryObject<Item> RFID_CARD_GRAY = ITEMS.register(
            "rfid_card_gray", () -> new CardItem(CardItem.Type.RFID, DyeColor.GRAY.getFireworkColor()));
    public static final RegistryObject<Item> RFID_CARD_LIGHT_GRAY = ITEMS.register(
            "rfid_card_light_gray", () -> new CardItem(CardItem.Type.RFID, DyeColor.LIGHT_GRAY.getFireworkColor()));
    public static final RegistryObject<Item> RFID_CARD_CYAN = ITEMS.register(
            "rfid_card_cyan", () -> new CardItem(CardItem.Type.RFID, DyeColor.CYAN.getFireworkColor()));
    public static final RegistryObject<Item> RFID_CARD_PURPLE = ITEMS.register(
            "rfid_card_purple", () -> new CardItem(CardItem.Type.RFID, DyeColor.PURPLE.getFireworkColor()));
    public static final RegistryObject<Item> RFID_CARD_BLUE = ITEMS.register(
            "rfid_card_blue", () -> new CardItem(CardItem.Type.RFID, DyeColor.BLUE.getFireworkColor()));
    public static final RegistryObject<Item> RFID_CARD_BROWN = ITEMS.register(
            "rfid_card_brown", () -> new CardItem(CardItem.Type.RFID, DyeColor.BROWN.getFireworkColor()));
    public static final RegistryObject<Item> RFID_CARD_GREEN = ITEMS.register(
            "rfid_card_green", () -> new CardItem(CardItem.Type.RFID, DyeColor.GREEN.getFireworkColor()));
    public static final RegistryObject<Item> RFID_CARD_RED = ITEMS.register(
            "rfid_card_red", () -> new CardItem(CardItem.Type.RFID, DyeColor.RED.getFireworkColor()));
    public static final RegistryObject<Item> RFID_CARD_BLACK = ITEMS.register(
            "rfid_card_black", () -> new CardItem(CardItem.Type.RFID, DyeColor.BLACK.getFireworkColor()));

    public static final RegistryObject<Item> ELECTRONIC_PAD = ITEMS.register(
            "electronic_pad", ElectronicPadItem::new);

    public static final RegistryObject<Item> MAGNETIC_CARD_READER_ITEM = fromBlock(ModBlocks.MAGNETIC_CARD_READER);
    public static final RegistryObject<Item> RFID_ANTENNA_ITEM = fromBlock(ModBlocks.RFID_ANTENNA);
    public static final RegistryObject<Item> RFID_WRITER_ITEM = fromBlock(ModBlocks.RFID_WRITER);
    public static final RegistryObject<Item> ELECTRONIC_LOCK_ITEM = fromBlock(ModBlocks.ELECTRONIC_LOCK);
    public static final RegistryObject<Item> ELECTRONIC_PAD_DESIGNER_ITEM = fromBlock(ModBlocks.ELECTRONIC_PAD_DESIGNER);

    @SuppressWarnings("unchecked")
    public static final Supplier<RegistryObject<Item>[]> ALL_MAGNETIC_CARDS = () -> new RegistryObject[] {
            ModItems.MAGNETIC_CARD_WHITE, ModItems.MAGNETIC_CARD_ORANGE, ModItems.MAGNETIC_CARD_MAGENTA,
            ModItems.MAGNETIC_CARD_LIGHT_BLUE, ModItems.MAGNETIC_CARD_YELLOW, ModItems.MAGNETIC_CARD_LIME,
            ModItems.MAGNETIC_CARD_PINK, ModItems.MAGNETIC_CARD_GRAY, ModItems.MAGNETIC_CARD_LIGHT_GRAY,
            ModItems.MAGNETIC_CARD_CYAN, ModItems.MAGNETIC_CARD_PURPLE, ModItems.MAGNETIC_CARD_BLUE,
            ModItems.MAGNETIC_CARD_BROWN, ModItems.MAGNETIC_CARD_GREEN, ModItems.MAGNETIC_CARD_RED,
            ModItems.MAGNETIC_CARD_BLACK
    };
    @SuppressWarnings("unchecked")
    public static final Supplier<RegistryObject<Item>[]> ALL_RFID_CARDS = () -> new RegistryObject[] {
            ModItems.RFID_CARD_WHITE, ModItems.RFID_CARD_ORANGE, ModItems.RFID_CARD_MAGENTA,
            ModItems.RFID_CARD_LIGHT_BLUE, ModItems.RFID_CARD_YELLOW, ModItems.RFID_CARD_LIME,
            ModItems.RFID_CARD_PINK, ModItems.RFID_CARD_GRAY, ModItems.RFID_CARD_LIGHT_GRAY,
            ModItems.RFID_CARD_CYAN, ModItems.RFID_CARD_PURPLE, ModItems.RFID_CARD_BLUE,
            ModItems.RFID_CARD_BROWN, ModItems.RFID_CARD_GREEN, ModItems.RFID_CARD_RED,
            ModItems.RFID_CARD_BLACK
    };

    private static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(ComputerThings.TAB)));
    }
}
