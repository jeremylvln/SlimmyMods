package fr.iamblueslime.ledmc.peripheral;

import fr.iamblueslime.ledmc.peripheral.driver.IPeripheralDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.usb.*;
import java.util.*;

public class PeripheralDriverManager {
    public static final PeripheralDriverManager INSTANCE = new PeripheralDriverManager();

    private static final Logger LOGGER = LogManager.getLogger();

    private final List<IPeripheralDriver> loadedDrivers;

    private PeripheralDriverManager() {
        this.loadedDrivers = new ArrayList<>();
    }

    public void loadDrivers() {
        List<IPeripheralDriver> loadedDrivers = this.listAvailableDrivers().parallelStream()
                .map((driver) -> driver.driverSupplier.get())
                .peek((driver) -> LOGGER.info("Initializing peripheral driver " + driver.getClass().getSimpleName()))
                .peek(IPeripheralDriver::init)
                .toList();

        this.loadedDrivers.addAll(loadedDrivers);
    }

    private Set<EnumPeripheralDriver> listAvailableDrivers() {
        LOGGER.info("Listing USB devices");

        try {
            Set<EnumPeripheralDriver> availableDrivers = this.listAvailableDriverForHub(UsbHostManager.getUsbServices().getRootUsbHub());
            LOGGER.info("Found " + availableDrivers.size() + " available drivers");
            return Collections.unmodifiableSet(availableDrivers);
        } catch (UsbException ex) {
            throw new RuntimeException("Failed to list usb devices", ex);
        }
    }

    @SuppressWarnings("unchecked")
    private Set<EnumPeripheralDriver> listAvailableDriverForHub(UsbHub usbHub) {
        Set<EnumPeripheralDriver> availableDrivers = new HashSet<>();

        for (UsbDevice device : (List<UsbDevice>) usbHub.getAttachedUsbDevices()) {
            if (device.isUsbHub()) {
                availableDrivers.addAll(this.listAvailableDriverForHub((UsbHub) device));
            } else {
                UsbDeviceDescriptor usbDeviceDescriptor = device.getUsbDeviceDescriptor();

                EnumPeripheralDriver driver = EnumPeripheralDriver.getDriverFromVendorAndProduct(
                        usbDeviceDescriptor.idVendor(), usbDeviceDescriptor.idProduct());

                availableDrivers.add(driver);
            }
        }

        return availableDrivers;
    }

    public List<IPeripheralDriver> getLoadedDrivers() {
        return this.loadedDrivers;
    }
}
