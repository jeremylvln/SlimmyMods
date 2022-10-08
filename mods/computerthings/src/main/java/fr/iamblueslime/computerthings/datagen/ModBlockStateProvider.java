package fr.iamblueslime.computerthings.datagen;

import fr.iamblueslime.computerthings.ComputerThings;
import fr.iamblueslime.computerthings.block.ElectronicLockBlock;
import fr.iamblueslime.computerthings.block.ElectronicPadDesignerBlock;
import fr.iamblueslime.computerthings.block.MagneticCardReaderBlock;
import fr.iamblueslime.computerthings.block.RFIDWriterBlock;
import fr.iamblueslime.computerthings.blockentity.MagneticCardReaderBlockEntity;
import fr.iamblueslime.computerthings.init.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, ComputerThings.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.RFID_ANTENNA.get(), models().getExistingFile(modLoc("block/rfid_antenna")));

        horizontalBlock(ModBlocks.ELECTRONIC_PAD_DESIGNER.get(), models().getExistingFile(modLoc("block/electronic_pad_designer")));

        registerMagneticCardReaderStatesAndModels();
        registerRFIDWriterStatesAndModels();
        registerElectronicLockStatesAndModels();
    }

    private void registerMagneticCardReaderStatesAndModels() {
        ModelFile.ExistingModelFile parentModel = models().getExistingFile(modLoc("block/magnetic_card_reader"));
        ModelFile idleModel = models().getBuilder("block/magnetic_card_reader_idle")
                .parent(parentModel)
                .texture("green", modLoc("color/gray"))
                .texture("orange", modLoc("color/gray"))
                .texture("red", modLoc("color/gray"));
        ModelFile waitingCardModel = models().getBuilder("block/magnetic_card_reader_waiting_card")
                .parent(parentModel)
                .texture("green", modLoc("color/green"))
                .texture("orange", modLoc("color/gray"))
                .texture("red", modLoc("color/gray"));
        ModelFile waitingCardWriteModel = models().getBuilder("block/magnetic_card_reader_waiting_card_write")
                .parent(parentModel)
                .texture("green", modLoc("color/green_blink"))
                .texture("orange", modLoc("color/gray"))
                .texture("red", modLoc("color/gray"));
        ModelFile busyModel = models().getBuilder("block/magnetic_card_reader_busy")
                .parent(parentModel)
                .texture("green", modLoc("color/gray"))
                .texture("orange", modLoc("color/orange"))
                .texture("red", modLoc("color/gray"));
        ModelFile rejectedModel = models().getBuilder("block/magnetic_card_reader_rejected")
                .parent(parentModel)
                .texture("green", modLoc("color/gray"))
                .texture("orange", modLoc("color/gray"))
                .texture("red", modLoc("color/red"));

        VariantBlockStateBuilder builder = getVariantBuilder(ModBlocks.MAGNETIC_CARD_READER.get());

        for (Direction direction : MagneticCardReaderBlock.FACING.getPossibleValues()) {
            int yRot = getYRotByDirection(direction);

            builder.partialState()
                    .with(MagneticCardReaderBlock.FACING, direction)
                    .with(MagneticCardReaderBlock.STATE, MagneticCardReaderBlock.EnumState.IDLE)
                    .modelForState()
                    .modelFile(idleModel)
                    .rotationX(90)
                    .rotationY(yRot)
                    .addModel()
                    .partialState()
                    .with(MagneticCardReaderBlock.FACING, direction)
                    .with(MagneticCardReaderBlock.STATE, MagneticCardReaderBlock.EnumState.WAITING_CARD)
                    .modelForState()
                    .modelFile(waitingCardModel)
                    .rotationX(90)
                    .rotationY(yRot)
                    .addModel()
                    .partialState()
                    .with(MagneticCardReaderBlock.FACING, direction)
                    .with(MagneticCardReaderBlock.STATE, MagneticCardReaderBlock.EnumState.WAITING_CARD_WRITE)
                    .modelForState()
                    .modelFile(waitingCardWriteModel)
                    .rotationX(90)
                    .rotationY(yRot)
                    .addModel()
                    .partialState()
                    .with(MagneticCardReaderBlock.FACING, direction)
                    .with(MagneticCardReaderBlock.STATE, MagneticCardReaderBlock.EnumState.BUSY)
                    .modelForState()
                    .modelFile(busyModel)
                    .rotationX(90)
                    .rotationY(yRot)
                    .addModel()
                    .partialState()
                    .with(MagneticCardReaderBlock.FACING, direction)
                    .with(MagneticCardReaderBlock.STATE, MagneticCardReaderBlock.EnumState.REJECTED)
                    .modelForState()
                    .modelFile(rejectedModel)
                    .rotationX(90)
                    .rotationY(yRot)
                    .addModel();
        }
    }

    private void registerRFIDWriterStatesAndModels() {
        ModelFile.ExistingModelFile parentModel = models().getExistingFile(modLoc("block/rfid_writer"));
        ModelFile idleModel = models().getBuilder("block/rfid_writer_idle")
                .parent(parentModel)
                .texture("green", modLoc("color/gray"))
                .texture("orange", modLoc("color/gray"));
        ModelFile waitingCardModel = models().getBuilder("block/rfid_writer_waiting_card")
                .parent(parentModel)
                .texture("green", modLoc("color/green"))
                .texture("orange", modLoc("color/gray"));
        ModelFile busyModel = models().getBuilder("block/rfid_writer_busy")
                .parent(parentModel)
                .texture("green", modLoc("color/gray"))
                .texture("orange", modLoc("color/orange"));

        VariantBlockStateBuilder builder = getVariantBuilder(ModBlocks.RFID_WRITER.get());

        for (Direction direction : MagneticCardReaderBlock.FACING.getPossibleValues()) {
            int yRot = getYRotByDirection(direction);

            for (int filledIncr = 0; filledIncr <= 1; filledIncr += 1) {
                boolean filled = filledIncr == 1;

                builder.partialState()
                        .with(RFIDWriterBlock.FACING, direction)
                        .with(RFIDWriterBlock.STATE, RFIDWriterBlock.EnumState.IDLE)
                        .with(RFIDWriterBlock.FILLED, filled)
                        .modelForState()
                        .modelFile(idleModel)
                        .rotationX(90)
                        .rotationY(yRot)
                        .addModel()
                        .partialState()
                        .with(RFIDWriterBlock.FACING, direction)
                        .with(RFIDWriterBlock.STATE, RFIDWriterBlock.EnumState.WAITING_CARD)
                        .with(RFIDWriterBlock.FILLED, filled)
                        .modelForState()
                        .modelFile(waitingCardModel)
                        .rotationX(90)
                        .rotationY(yRot)
                        .addModel()
                        .partialState()
                        .with(RFIDWriterBlock.FACING, direction)
                        .with(RFIDWriterBlock.STATE, RFIDWriterBlock.EnumState.BUSY)
                        .with(RFIDWriterBlock.FILLED, filled)
                        .modelForState()
                        .modelFile(busyModel)
                        .rotationX(90)
                        .rotationY(yRot)
                        .addModel();
            }
        }
    }

    private void registerElectronicLockStatesAndModels() {
        ModelFile.ExistingModelFile parentModel = models().getExistingFile(modLoc("block/electronic_lock"));
        ModelFile idleModel = models().getBuilder("block/electronic_lock_idle")
                .parent(parentModel)
                .texture("green", modLoc("color/gray"))
                .texture("red", modLoc("color/gray"));
        ModelFile waitingInputModel = models().getBuilder("block/electronic_lock_waiting_input")
                .parent(parentModel)
                .texture("green", modLoc("color/green_blink"))
                .texture("red", modLoc("color/gray"));
        ModelFile rejectedModel = models().getBuilder("block/electronic_lock_rejected")
                .parent(parentModel)
                .texture("green", modLoc("color/gray"))
                .texture("red", modLoc("color/red"));

        VariantBlockStateBuilder builder = getVariantBuilder(ModBlocks.ELECTRONIC_LOCK.get());

        for (Direction direction : ElectronicLockBlock.FACING.getPossibleValues()) {
            int yRot = getYRotByDirection(direction);

            builder.partialState()
                    .with(ElectronicLockBlock.FACING, direction)
                    .with(ElectronicLockBlock.STATE, ElectronicLockBlock.EnumState.IDLE)
                    .modelForState()
                    .modelFile(idleModel)
                    .rotationX(90)
                    .rotationY(yRot)
                    .addModel()
                    .partialState()
                    .with(ElectronicLockBlock.FACING, direction)
                    .with(ElectronicLockBlock.STATE, ElectronicLockBlock.EnumState.WAITING_INPUT)
                    .modelForState()
                    .modelFile(waitingInputModel)
                    .rotationX(90)
                    .rotationY(yRot)
                    .addModel()
                    .partialState()
                    .with(ElectronicLockBlock.FACING, direction)
                    .with(ElectronicLockBlock.STATE, ElectronicLockBlock.EnumState.REJECTED)
                    .modelForState()
                    .modelFile(rejectedModel)
                    .rotationX(90)
                    .rotationY(yRot)
                    .addModel();
        }
    }

    private static int getYRotByDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> 0;
            case SOUTH -> 180;
            case EAST -> 90;
            case WEST -> 270;
            default -> (int) direction.toYRot();
        };
    }
}
