package fr.iamblueslime.ledmc;

import fr.iamblueslime.ledmc.peripheral.PeripheralDriverManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LedMC.MODID)
public class LedMC {
    public static final String MODID = "ledmc";

    public LedMC() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "client only", (s, b) -> b));
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient));
    }

    private void setupClient(final FMLClientSetupEvent ignoredEvent) {
        PeripheralDriverManager.INSTANCE.loadDrivers();
    }
}
