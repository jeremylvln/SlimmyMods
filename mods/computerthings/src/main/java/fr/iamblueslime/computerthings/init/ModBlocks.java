package fr.iamblueslime.computerthings.init;

import fr.iamblueslime.computerthings.ComputerThings;
import fr.iamblueslime.computerthings.block.*;
import fr.iamblueslime.computerthings.blockentity.RFIDAntennaBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ComputerThings.MODID);

    public static final RegistryObject<MagneticCardReaderBlock> MAGNETIC_CARD_READER = BLOCKS.register(
            "magnetic_card_reader", MagneticCardReaderBlock::new);
    public static final RegistryObject<RFIDAntennaBlock> RFID_ANTENNA = BLOCKS.register(
            "rfid_antenna", RFIDAntennaBlock::new);
    public static final RegistryObject<RFIDWriterBlock> RFID_WRITER = BLOCKS.register(
            "rfid_writer", RFIDWriterBlock::new);
    public static final RegistryObject<ElectronicLockBlock> ELECTRONIC_LOCK = BLOCKS.register(
            "electronic_lock", ElectronicLockBlock::new);
    public static final RegistryObject<ElectronicPadDesignerBlock> ELECTRONIC_PAD_DESIGNER = BLOCKS.register(
            "electronic_pad_designer", ElectronicPadDesignerBlock::new);
}
