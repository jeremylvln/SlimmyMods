package fr.iamblueslime.computerthings.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class KeyBindMap extends HashMap<String, String> {
    private final Set<String> acceptedKeys;
    private final Map<ControlProfile, Integer> matchingKeyBindsPerProfile;

    public KeyBindMap(Set<String> acceptedKeys) {
        this.acceptedKeys = acceptedKeys;
        this.matchingKeyBindsPerProfile = new HashMap<>();
    }

    public void foldControlProfile(ControlProfile controlProfile) {
        Map<String, String> notPresentKeyBinds = controlProfile.parsedOptions().keyBinds().entrySet().stream()
                .filter((entry) -> this.acceptedKeys.contains(entry.getKey()) && !this.containsKey(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        this.matchingKeyBindsPerProfile.put(controlProfile, notPresentKeyBinds.size());
        this.putAll(notPresentKeyBinds);
    }

    public int getMatchingKeysForProfile(ControlProfile controlProfile) {
        return this.matchingKeyBindsPerProfile.get(controlProfile);
    }

    public List<ControlProfile> getControlProfilesWithoutMatch() {
        return this.matchingKeyBindsPerProfile.entrySet().parallelStream()
                .filter((entry) -> entry.getValue() == 0)
                .map(Entry::getKey)
                .toList();
    }
}
