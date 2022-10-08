package fr.iamblueslime.computerthings.init;

import fr.iamblueslime.computerthings.ComputerThings;
import fr.iamblueslime.computerthings.blockentity.ElectronicLockBlockEntity;
import fr.iamblueslime.computerthings.blockentity.MagneticCardReaderBlockEntity;
import fr.iamblueslime.computerthings.blockentity.RFIDAntennaBlockEntity;
import fr.iamblueslime.computerthings.blockentity.RFIDWriterBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ComputerThings.MODID);

    public static final RegistryObject<BlockEntityType<MagneticCardReaderBlockEntity>> MAGNETIC_CARD_READER = BLOCK_ENTITIES.register(
            "magnetic_card_reader", () -> BlockEntityType.Builder.of(MagneticCardReaderBlockEntity::new, ModBlocks.MAGNETIC_CARD_READER.get()).build(null));
    public static final RegistryObject<BlockEntityType<RFIDAntennaBlockEntity>> RFID_ANTENNA = BLOCK_ENTITIES.register(
            "rfid_antenna", () -> BlockEntityType.Builder.of(RFIDAntennaBlockEntity::new, ModBlocks.RFID_ANTENNA.get()).build(null));
    public static final RegistryObject<BlockEntityType<RFIDWriterBlockEntity>> RFID_WRITER = BLOCK_ENTITIES.register(
            "rfid_writer", () -> BlockEntityType.Builder.of(RFIDWriterBlockEntity::new, ModBlocks.RFID_WRITER.get()).build(null));
    public static final RegistryObject<BlockEntityType<ElectronicLockBlockEntity>> ELECTRONIC_LOCK = BLOCK_ENTITIES.register(
            "electronic_lock", () -> BlockEntityType.Builder.of(ElectronicLockBlockEntity::new, ModBlocks.ELECTRONIC_LOCK.get()).build(null));
}
