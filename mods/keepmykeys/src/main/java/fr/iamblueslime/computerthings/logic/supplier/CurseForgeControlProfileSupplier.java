package fr.iamblueslime.computerthings.logic.supplier;

import fr.iamblueslime.computerthings.logic.ControlProfile;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class CurseForgeControlProfileSupplier implements ControlProfileSupplier {
    @Override
    public List<ControlProfile> get() throws IOException {
        File curseForgeFolder = null;

        if (SystemUtils.IS_OS_WINDOWS) {
            curseForgeFolder = Path.of(System.getProperty("user.home"), "curseforge").toFile();
        } else if (SystemUtils.IS_OS_MAC) {
            curseForgeFolder = Path.of(System.getProperty("user.home"), "Documents", "curseforge").toFile();
        } else if (SystemUtils.IS_OS_LINUX) {
            curseForgeFolder = Path.of(System.getProperty("user.home"), "curseforge").toFile();
        }

        if (curseForgeFolder == null || !curseForgeFolder.exists()) {
            return Collections.emptyList();
        }

        File instancesFolder = Path.of(curseForgeFolder.getPath(), "minecraft", "Instances").toFile();

        if (!instancesFolder.exists()) {
            return Collections.emptyList();
        }

        Map<String, File> optionsFiles = Files.list(instancesFolder.toPath())
                .filter(Files::isDirectory)
                .map(Path::toFile)
                .filter((instanceFolder) -> new File(instanceFolder, "options.txt").exists())
                .map((instanceFolder) -> Pair.of(instanceFolder.getName() + " (CurseForge)", new File(instanceFolder, "options.txt")))
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));

        return optionsFiles.entrySet().parallelStream()
                .map((optionsFile) -> new ControlProfile(
                        ControlProfile.Type.CURSE_FORGE,
                        optionsFile.getKey(),
                        optionsFile.getValue())
                )
                .toList();
    }
}
