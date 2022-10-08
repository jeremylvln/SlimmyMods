package fr.iamblueslime.computerthings.datagen;

import fr.iamblueslime.computerthings.ComputerThings;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = ComputerThings.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneratorListener {
    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();

        if (event.includeServer()) {
            generator.addProvider(new ModRecipeProvider(generator));
            generator.addProvider(new ModLootTableProvider(generator));
        }

        if (event.includeClient()) {
            generator.addProvider(new ModBlockStateProvider(generator, event.getExistingFileHelper()));
            generator.addProvider(new ModItemModelProvider(generator, event.getExistingFileHelper()));
            generator.addProvider(new ModLanguageProvider(generator));
        }
    }
}
