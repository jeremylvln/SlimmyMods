package fr.iamblueslime.computerthings.logic.supplier;

import fr.iamblueslime.computerthings.logic.ControlProfile;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class VanillaControlProfileSupplier implements ControlProfileSupplier {
    @Override
    public List<ControlProfile> get() {
        File dataFolder = null;

        if (SystemUtils.IS_OS_WINDOWS) {
            dataFolder = Path.of(System.getenv("APPDATA"), ".minecraft").toFile();
        } else if (SystemUtils.IS_OS_MAC) {
            dataFolder = Path.of(System.getProperty("user.home"), "Library", "Application Support", "minecraft").toFile();
        } else if (SystemUtils.IS_OS_LINUX) {
            dataFolder = Path.of(System.getProperty("user.home"), ".minecraft").toFile();
        }

        if (dataFolder == null || !dataFolder.exists()) {
            return Collections.emptyList();
        }

        File optionsFile = new File(dataFolder, "options.txt");

        if (!optionsFile.exists()) {
            return Collections.emptyList();
        }

        return Collections.singletonList(new ControlProfile(ControlProfile.Type.VANILLA, "Vanilla", optionsFile));
    }
}
