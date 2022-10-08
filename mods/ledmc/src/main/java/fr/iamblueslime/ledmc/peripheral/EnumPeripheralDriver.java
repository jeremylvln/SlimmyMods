package fr.iamblueslime.ledmc.peripheral;

import fr.iamblueslime.ledmc.peripheral.driver.IPeripheralDriver;
import fr.iamblueslime.ledmc.peripheral.driver.LogitechPeripheralDriver;

import java.util.function.Supplier;
import java.util.stream.IntStream;

public enum EnumPeripheralDriver {
    LOGITECH("Logitech", LogitechPeripheralDriver.VENDOR_ID, LogitechPeripheralDriver.PRODUCT_IDS, LogitechPeripheralDriver::new);

    public final String name;
    public final int vendorId;
    public final int[] productIds;
    public final Supplier<IPeripheralDriver> driverSupplier;

    EnumPeripheralDriver(String name, int vendorId, int[] productIds, Supplier<IPeripheralDriver> driverSupplier) {
        this.name = name;
        this.vendorId = vendorId;
        this.productIds = productIds;
        this.driverSupplier = driverSupplier;
    }

    public static EnumPeripheralDriver getDriverFromVendorAndProduct(int vendorId, int productId) {
        for (EnumPeripheralDriver driver : values()) {
            if (
                    driver.vendorId == vendorId &&
                    IntStream.of(driver.productIds).anyMatch((id) -> id == productId)
            ) {
                return driver;
            }
        }

        return null;
    }
}
