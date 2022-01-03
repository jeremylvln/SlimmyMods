package fr.iamblueslime.keepmykeys.datagen;

import fr.iamblueslime.keepmykeys.KeepMyKeys;
import fr.iamblueslime.keepmykeys.client.gui.ControlProfileSelectionList;
import fr.iamblueslime.keepmykeys.client.gui.ImportControlsFromButton;
import fr.iamblueslime.keepmykeys.client.gui.ImportSelectProfileScreen;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(DataGenerator gen) {
        super(gen, KeepMyKeys.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(ImportControlsFromButton.MESSAGE_COMPONENTS[0].getKey(), "Import key binds from Vanilla...");
        add(ImportControlsFromButton.MESSAGE_COMPONENTS[1].getKey(), "Import key binds from a modpack...");
        add(ImportSelectProfileScreen.TITLE_MESSAGE.getKey(), "Import key binds");
        add(ControlProfileSelectionList.NO_PROFILE_FOUND_MESSAGE.getKey(), "No profile found :(");
        add(ControlProfileSelectionList.PROFILES_TO_APPLY_MESSAGE.getKey(), "Profiles to apply");
        add(ControlProfileSelectionList.IGNORED_PROFILES_MESSAGE.getKey(), "Ignored profiles");
        add(ControlProfileSelectionList.MATCHING_KEYS_MESSAGE_KEY, "%s usable keys");
        add(ControlProfileSelectionList.PROFILE_LAST_MODIFIED_ON_MESSAGE_KEY, "Last modified on: %s");
        add(ControlProfileSelectionList.PROFILE_FILE_PATH_MESSAGE_KEY, "File path: %s");
    }
}
