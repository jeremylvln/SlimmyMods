package fr.iamblueslime.keepmykeys.datagen;

import fr.iamblueslime.keepmykeys.KeepMyKeys;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = KeepMyKeys.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneratorListener {
    @SuppressWarnings("CommentedOutCode")
    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent event) {
        // DataGenerator generator = event.getGenerator();
        // if (event.includeClient()) {}
    }
}
