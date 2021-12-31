package fr.iamblueslime.keepmykeys.client;

import fr.iamblueslime.keepmykeys.KeepMyKeys;
import fr.iamblueslime.keepmykeys.client.gui.ImportControlsFromButton;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = KeepMyKeys.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEventListener {
    public static void init(final FMLClientSetupEvent ignoredEvent) {
        MinecraftForge.EVENT_BUS.register(new ClientEventListener());
    }

    @SubscribeEvent
    public void onPostInitScreen(final ScreenEvent.InitScreenEvent.Post event) {
        Screen screen = event.getScreen();
        if (!(screen instanceof ControlsScreen)) return;

        screen.renderables.stream()
                .filter((widget) -> !(widget instanceof Button) || ((Button) widget).getMessage() != CommonComponents.GUI_DONE)
                .map((widget) -> ((AbstractButton) widget))
                .forEach((widget) -> widget.y += 20 + 4);

        int buttonX = screen.width / 2 - 155;
        int buttonY = screen.height / 6 - 12;

        screen.addRenderableWidget(new ImportControlsFromButton(screen, buttonX, buttonY, 310, 20));
    }
}
