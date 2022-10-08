package fr.iamblueslime.computerthings.logic;

import fr.iamblueslime.computerthings.logic.supplier.ControlProfileSupplier;
import fr.iamblueslime.computerthings.logic.supplier.CurseForgeControlProfileSupplier;
import fr.iamblueslime.computerthings.logic.supplier.VanillaControlProfileSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ControlProfileRegistry {
    public static final ControlProfileRegistry INSTANCE = new ControlProfileRegistry();

    private static final Logger LOGGER = LogManager.getLogger();
    private static final List<ControlProfileSupplier> CONTROL_PROFILE_SUPPLIERS;
    private static final Predicate<String> KEY_BINDING_PREDICATE = (key) -> key.startsWith("key_");

    private final Options minecraftOptions;
    private final File baseOptionsFile;
    private ControlProfile.ParsedOptions baseParsedOptions;

    private ControlProfileRegistry() {
        this.minecraftOptions = Minecraft.getInstance().options;
        this.baseOptionsFile = this.minecraftOptions.getFile();
    }

    public List<ControlProfile> lookupForProfiles() {
        if (this.baseParsedOptions == null) {
            try {
                this.baseParsedOptions = this.readParsedOptionsFromFile(this.baseOptionsFile);
            } catch (IOException ex) {
                LOGGER.error("Failed to load base options file", ex);
                return Collections.emptyList();
            }
        }

        return CONTROL_PROFILE_SUPPLIERS.parallelStream()
                .map((supplier) -> {
                    List<ControlProfile> controlProfiles;

                    try {
                        controlProfiles = supplier.get();
                    } catch (IOException ex) {
                        return new ArrayList<ControlProfile>();
                    }

                    List<ControlProfile> controlProfilesWithOptions = new ArrayList<>();
                    for (ControlProfile controlProfile : controlProfiles) {
                        try {
                            controlProfilesWithOptions.add(controlProfile.withParsedOptions(this.readParsedOptionsFromFile(controlProfile.file())));
                        } catch (IOException ignored) {
                        }
                    }

                    return controlProfilesWithOptions;
                })
                .flatMap(Collection::stream)
                .sorted(Comparator.comparingLong((controlProfile) -> ((ControlProfile) controlProfile).file().lastModified()).reversed())
                .peek((controlProfile) -> LOGGER.info("Found control profile: " + controlProfile.file().getAbsolutePath()))
                .toList();
    }

    public KeyBindMap createKeyBindMapFromProfiles(List<ControlProfile> profiles) {
        KeyBindMap keyBindMap = new KeyBindMap(this.baseParsedOptions.keyBinds().keySet());
        profiles.forEach(keyBindMap::foldControlProfile);
        return keyBindMap;
    }

    public void applyKeyBindMap(KeyBindMap keyBindMap) {
        try {
            Map<String, String> optionsFileEntries = new HashMap<>(keyBindMap);
            optionsFileEntries.put("version", this.baseParsedOptions.version());

            String optionsFileContent = optionsFileEntries.entrySet().parallelStream()
                    .map((entry) -> entry.getKey() + ':' + entry.getValue())
                    .collect(Collectors.joining("\n"));

            Path tmpFile = Files.createTempFile("keepmykeys_", ".txt");
            Files.writeString(tmpFile, optionsFileContent);

            LOGGER.info("Loading key binds from temporary file: " + tmpFile);

            this.minecraftOptions.optionsFile = tmpFile.toFile();
            this.minecraftOptions.load();
            this.minecraftOptions.optionsFile = this.baseOptionsFile;
            this.minecraftOptions.save();
        } catch (IOException ex) {
            LOGGER.error("Failed to load key binds", ex);
        }
    }

    private ControlProfile.ParsedOptions readParsedOptionsFromFile(File optionsFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(optionsFile));
        List<String> lines = reader.lines().toList();

        Map<String, String> entries = lines.stream()
                .map((line) -> {
                    int index = line.indexOf(':');
                    return Pair.of(line.substring(0, index), line.substring(index + 1));
                })
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));

        String version = entries.get("version");

        Map<String, String> keyBindsEntries = entries.entrySet().stream()
                .filter((entry) -> KEY_BINDING_PREDICATE.test(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new ControlProfile.ParsedOptions(version, keyBindsEntries);
    }

    static {
        CONTROL_PROFILE_SUPPLIERS = new ArrayList<>();
        CONTROL_PROFILE_SUPPLIERS.add(new VanillaControlProfileSupplier());
        CONTROL_PROFILE_SUPPLIERS.add(new CurseForgeControlProfileSupplier());
    }
}
