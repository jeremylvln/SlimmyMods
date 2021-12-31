package fr.iamblueslime.keepmykeys.logic;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Map;

public record ControlProfile(Type type, String title, File file, @Nullable ParsedOptions parsedOptions) {
    public ControlProfile(Type type, String title, File file) {
        this(type, title, file, null);
    }

    public ControlProfile withParsedOptions(ParsedOptions parsedOptions) {
        return new ControlProfile(this.type, this.title, this.file, parsedOptions);
    }

    public enum Type {
        VANILLA,
        CURSE_FORGE;
    }

    public record ParsedOptions(String version, Map<String, String> keyBinds) {}
    public record Stats(int matchingKeyBinds) {}
}
