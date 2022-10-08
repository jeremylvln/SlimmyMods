package fr.iamblueslime.computerthings;

import fr.iamblueslime.computerthings.client.ClientEventListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(KeepMyKeys.MODID)
public class KeepMyKeys {
    public static final String MODID = "keepmykeys";

    public KeepMyKeys() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "client only", (s, b) -> b));
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient));
    }

    private void setupClient(final FMLClientSetupEvent ignoredEvent) {
        MinecraftForge.EVENT_BUS.register(new ClientEventListener());
    }
}
