package fr.iamblueslime.computerthings.logic.supplier;

import fr.iamblueslime.computerthings.logic.ControlProfile;

import java.io.IOException;
import java.util.List;

@FunctionalInterface
public interface ControlProfileSupplier {
    List<ControlProfile> get() throws IOException;
}
