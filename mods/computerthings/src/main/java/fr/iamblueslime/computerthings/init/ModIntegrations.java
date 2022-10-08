package fr.iamblueslime.computerthings.init;

import fr.iamblueslime.computerthings.logic.computer.ComputerCraftIntegration;
import net.minecraftforge.fml.ModList;

public class ModIntegrations {
    public static void load() {
        if (ModList.get().isLoaded("computercraft")) {
            ComputerCraftIntegration.load();
        }
    }
}
