package tech.nilu.wallet;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

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
        //Context appContext = InstrumentationRegistry.getTargetContext();

        //assertEquals("tech.nilu.wallet", appContext.getPackageName());
    }

    @Test
    public void deviceUtils_GetAppVersionCode() {
        /*Context context = InstrumentationRegistry.getTargetContext();
        String appVersionCode = DeviceUtils.getAppVersionCode(context);
        Log.d("HelloTest", "deviceUtils_GetAppVersionCode: " + appVersionCode);
        assertThat(appVersionCode, is("14"));*/
    }
}
