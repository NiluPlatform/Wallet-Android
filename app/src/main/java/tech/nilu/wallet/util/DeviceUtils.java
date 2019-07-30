package tech.nilu.wallet.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class DeviceUtils {
    public static String getAppVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            if (manager != null) {
                PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
                return String.valueOf(info.versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "0";
    }

    public static String getAppVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            if (manager != null) {
                PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
                return info.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "0";
    }
}
