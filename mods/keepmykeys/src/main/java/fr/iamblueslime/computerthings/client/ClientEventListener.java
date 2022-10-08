package fr.iamblueslime.computerthings.client;

import fr.iamblueslime.computerthings.KeepMyKeys;
import fr.iamblueslime.computerthings.client.gui.ImportControlsFromButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KeepMyKeys.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEventListener {
    @SubscribeEvent
    public void onPostInitScreen(final ScreenEvent.InitScreenEvent.Post event) {
        Screen screen = event.getScreen();
        if (!(screen instanceof ControlsScreen)) return;

        screen.renderables.stream()
                .map((widget) -> ((AbstractWidget) widget))
                .forEach((widget) -> widget.y += 20 + 4);

        int buttonX = screen.width / 2 - 155;
        int buttonY = screen.height / 6 - 12;

        screen.addRenderableWidget(new ImportControlsFromButton(screen, buttonX, buttonY, 310, 20));
    }
}
