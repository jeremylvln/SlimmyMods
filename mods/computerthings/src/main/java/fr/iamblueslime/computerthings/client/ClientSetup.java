package fr.iamblueslime.computerthings.client;

import fr.iamblueslime.computerthings.ComputerThings;
import fr.iamblueslime.computerthings.client.render.ElectronicLockRenderer;
import fr.iamblueslime.computerthings.client.render.MagneticCardReaderRenderer;
import fr.iamblueslime.computerthings.client.screen.ElectronicPadDesignerScreen;
import fr.iamblueslime.computerthings.init.ModBlockEntities;
import fr.iamblueslime.computerthings.init.ModItems;
import fr.iamblueslime.computerthings.init.ModMenus;
import fr.iamblueslime.computerthings.item.CardItem;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = ComputerThings.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenus.ELECTRONIC_PAD_DESIGNER.get(), ElectronicPadDesignerScreen::new);
        });
    }

    @SubscribeEvent
    public static void registerEntityRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.MAGNETIC_CARD_READER.get(), MagneticCardReaderRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.ELECTRONIC_LOCK.get(), ElectronicLockRenderer::new);
    }

    @SubscribeEvent
    public static void registerItemColors(final ColorHandlerEvent.Item event) {
        for (RegistryObject<Item> magneticCard : ModItems.ALL_MAGNETIC_CARDS.get())
            event.getItemColors().register((itemStack, tintIndex) -> ((CardItem) itemStack.getItem()).getColor(), magneticCard.get());
        for (RegistryObject<Item> rfidCard : ModItems.ALL_RFID_CARDS.get())
            event.getItemColors().register((itemStack, tintIndex) -> tintIndex == 1 ? ((CardItem) itemStack.getItem()).getColor() : -1, rfidCard.get());
    }
}
