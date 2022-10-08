package fr.iamblueslime.computerthings;

import fr.iamblueslime.computerthings.client.ClientSetup;
import fr.iamblueslime.computerthings.init.*;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ComputerThings.MODID)
public class ComputerThings {
    public static final String MODID = "computerthings";

    public static CreativeModeTab TAB = new CreativeModeTab(MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.MAGNETIC_CARD_WHITE.get(), 1);
        }
    };

    public ComputerThings() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(eventBus);
        ModItems.ITEMS.register(eventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(eventBus);
        ModMenus.MENUS.register(eventBus);

        ModIntegrations.load();

        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        modbus.addListener(ModNetwork::init);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> eventBus.addListener(ClientSetup::init));
    }
}
