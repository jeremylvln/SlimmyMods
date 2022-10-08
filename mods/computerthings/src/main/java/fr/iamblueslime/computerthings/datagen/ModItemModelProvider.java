package fr.iamblueslime.computerthings.datagen;

import fr.iamblueslime.computerthings.ComputerThings;
import fr.iamblueslime.computerthings.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, ComputerThings.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (RegistryObject<Item> magneticCard : ModItems.ALL_MAGNETIC_CARDS.get()) {
            singleTexture(magneticCard.get().getRegistryName().getPath(),
                    mcLoc("item/generated"),
                    "layer0", modLoc("item/magnetic_card"));
        }

        for (RegistryObject<Item> rfidCard : ModItems.ALL_RFID_CARDS.get()) {
            withExistingParent(rfidCard.get().getRegistryName().getPath(),
                    mcLoc("item/generated"))
                    .texture("layer0", modLoc("item/rfid_card"))
                    .texture("layer1", modLoc("item/rfid_card_overlay"));
        }

        singleTexture(ModItems.ELECTRONIC_PAD.get().getRegistryName().getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("item/electronic_pad"));

        withExistingParent(ModItems.MAGNETIC_CARD_READER_ITEM.get().getRegistryName().getPath(),
                modLoc("block/magnetic_card_reader"));
        withExistingParent(ModItems.RFID_ANTENNA_ITEM.get().getRegistryName().getPath(),
                modLoc("block/rfid_antenna"));
        withExistingParent(ModItems.RFID_WRITER_ITEM.get().getRegistryName().getPath(),
                modLoc("block/rfid_writer"));
        withExistingParent(ModItems.ELECTRONIC_LOCK_ITEM.get().getRegistryName().getPath(),
                modLoc("block/electronic_lock"));
        withExistingParent(ModItems.ELECTRONIC_PAD_DESIGNER_ITEM.get().getRegistryName().getPath(),
                modLoc("block/electronic_pad_designer"));
    }
}
