package fr.iamblueslime.ledmc.peripheral.driver;

import com.logitech.gaming.LogiLED;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class LogitechPeripheralDriver implements IPeripheralDriver {
    public static final int VENDOR_ID = 0x046D;
    public static final int[] PRODUCT_IDS = new int[] {
            0xC22D, // G510
    };

    private static final Boolean[] COMPATIBLE_OS = {
            SystemUtils.IS_OS_WINDOWS_VISTA, SystemUtils.IS_OS_WINDOWS_7,
            SystemUtils.IS_OS_WINDOWS_8, SystemUtils.IS_OS_WINDOWS_10
    };

    @Override
    public void init() throws RuntimeException {
        try {
            InputStream libStream = LogitechPeripheralDriver.class.getResourceAsStream("/com/logitech/gaming/LogitechLedJNI.dll");
            File tmpLib = File.createTempFile("LogitechLed.dll", "");
            FileOutputStream outStream = new FileOutputStream(tmpLib);
            libStream.transferTo(outStream);

            outStream.close();
            libStream.close();

            System.load(tmpLib.getAbsolutePath());
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load native LogitechLed library");
        }

        if (!LogiLED.LogiLedInit()) {
            throw new RuntimeException("Failed to connect to Logitech Gaming Software");
        }

        if (!LogiLED.LogiLedSaveCurrentLighting()) {
            throw new RuntimeException("Failed to save current keyboard lightning");
        }

        if (!LogiLED.LogiLedSetTargetDevice(LogiLED.LOGI_DEVICETYPE_PERKEY_RGB)) {
            throw new RuntimeException("Failed to set target device type");
        }
    }

    @Override
    public void destroy() throws RuntimeException {
        LogiLED.LogiLedRestoreLighting();
        LogiLED.LogiLedShutdown();
    }

    @Override
    public boolean isCompatible() {
        return Arrays.stream(COMPATIBLE_OS).anyMatch((b) -> b);
    }
}
