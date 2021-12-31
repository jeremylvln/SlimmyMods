package fr.iamblueslime.keepmykeys.logic.supplier;

import fr.iamblueslime.keepmykeys.logic.ControlProfile;

import java.io.IOException;
import java.util.List;

@FunctionalInterface
public interface ControlProfileSupplier {
    List<ControlProfile> get() throws IOException;
}
