package fr.iamblueslime.computerthings.datagen;

import fr.iamblueslime.computerthings.ComputerThings;
import fr.iamblueslime.computerthings.init.ModBlocks;
import fr.iamblueslime.computerthings.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(DataGenerator generator) {
        super(generator, ComputerThings.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + ComputerThings.MODID, "ComputerThings");

        add(ModBlocks.MAGNETIC_CARD_READER.get(), "Magnetic Card Reader");
        add(ModBlocks.RFID_ANTENNA.get(), "RFID Antenna");
        add(ModBlocks.RFID_WRITER.get(), "RFID Writer");
        add(ModBlocks.ELECTRONIC_LOCK.get(), "Electronic Lock");
        add(ModBlocks.ELECTRONIC_PAD_DESIGNER.get(), "Electronic Pad Designer");

        for (RegistryObject<Item> magneticCard : ModItems.ALL_MAGNETIC_CARDS.get())
            add(magneticCard.get(), "Magnetic Card");
        for (RegistryObject<Item> rfidCard : ModItems.ALL_RFID_CARDS.get())
            add(rfidCard.get(), "RFID Card");

        add(ModItems.ELECTRONIC_PAD.get(), "Electronic Pad");

        add("computerthings.card.written", "Written");
        add("computerthings.electronic_pad.contains", "Contains the followings:");
        add("computerthings.electronic_pad.entry", "- At position %d: %s");

        add("computerthings.container.electronic_pad_designer", "Electronic Pad Desginer");
    }
}
