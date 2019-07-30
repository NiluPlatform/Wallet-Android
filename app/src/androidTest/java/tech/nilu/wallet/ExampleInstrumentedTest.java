package tech.nilu.wallet;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import tech.nilu.wallet.util.DeviceUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("tech.nilu.wallet", appContext.getPackageName());
    }

    @Test
    public void deviceUtils_GetAppVersionCode() {
        Context context = InstrumentationRegistry.getTargetContext();
        String appVersionCode = DeviceUtils.getAppVersionCode(context);
        Log.d("HelloTest", "deviceUtils_GetAppVersionCode: " + appVersionCode);
        assertThat(appVersionCode, is("14"));
    }
}
