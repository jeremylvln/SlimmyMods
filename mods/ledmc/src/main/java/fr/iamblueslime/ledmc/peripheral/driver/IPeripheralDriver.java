package fr.iamblueslime.ledmc.peripheral.driver;

import fr.iamblueslime.ledmc.peripheral.controller.IPeripheralController;

public interface IPeripheralDriver {
    void init() throws RuntimeException;
    void destroy() throws RuntimeException;

    boolean isCompatible();
}
