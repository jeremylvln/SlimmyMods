package fr.iamblueslime.computerthings.init;

import fr.iamblueslime.computerthings.ComputerThings;
import fr.iamblueslime.computerthings.menu.ElectronicPadDesignerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS, ComputerThings.MODID);

    public static final RegistryObject<MenuType<ElectronicPadDesignerMenu>> ELECTRONIC_PAD_DESIGNER = MENUS.register(
            "electronic_pad_designer", () -> new MenuType<>(ElectronicPadDesignerMenu::new));
}
