package fr.iamblueslime.computerthings.datagen;

import fr.iamblueslime.computerthings.init.ModBlocks;
import net.minecraft.data.DataGenerator;

public class ModLootTableProvider extends ALootTableProvider {
    public ModLootTableProvider(DataGenerator pGenerator) {
        super(pGenerator);
    }

    protected void addTables() {
        this.lootTables.put(ModBlocks.MAGNETIC_CARD_READER.get(), createSimpleTable("magnetic_card_reader",
                ModBlocks.MAGNETIC_CARD_READER.get()));
        this.lootTables.put(ModBlocks.RFID_ANTENNA.get(), createSimpleTable("rfid_antenna",
                ModBlocks.RFID_ANTENNA.get()));
        this.lootTables.put(ModBlocks.RFID_WRITER.get(), createSimpleTable("rfid_writer",
                ModBlocks.RFID_WRITER.get()));
        this.lootTables.put(ModBlocks.ELECTRONIC_LOCK.get(), createSimpleTable("electronic_lock",
                ModBlocks.ELECTRONIC_LOCK.get()));
        this.lootTables.put(ModBlocks.ELECTRONIC_PAD_DESIGNER.get(), createSimpleTable("electronic_pad_designer",
                ModBlocks.ELECTRONIC_PAD_DESIGNER.get()));
    }
}
