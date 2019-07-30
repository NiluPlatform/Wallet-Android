package tech.nilu.wallet;

import android.os.Build;
import android.support.annotation.Keep;

public class JniHandler {
    @Keep
    static public String getBuildBoard() {
        return Build.BOARD;
    }

    @Keep
    static public String getBuildBrand() {
        return Build.BRAND;
    }

    @Keep
    static public String getBuildCpuABI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return (Build.SUPPORTED_ABIS.length > 0) ? Build.SUPPORTED_ABIS[0] : Build.CPU_ABI;
        return Build.CPU_ABI;
    }

    @Keep
    static public String getBuildDevice() {
        return Build.DEVICE;
    }

    @Keep
    static public String getBuildManufacturer() {
        return Build.MANUFACTURER;
    }

    @Keep
    static public String getBuildModel() {
        return Build.MODEL;
    }

    @Keep
    static public String getBuildProduct() {
        return Build.PRODUCT;
    }

    @Keep
    static public String getBuildSerial() {
        try {
            return Build.class.getField("SERIAL").get(null).toString();
        } catch (Exception e) {
            return "serial";
        }
    }
}
