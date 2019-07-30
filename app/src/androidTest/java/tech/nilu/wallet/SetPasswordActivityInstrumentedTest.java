package tech.nilu.wallet;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import tech.nilu.wallet.ui.password.SetPasswordActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SetPasswordActivityInstrumentedTest {
    private final String password = "1234567890";
    private final String correctConfirm = "1234567890";
    private final String incorrectConfirm = "0987654321";

    @Rule
    public ActivityTestRule<SetPasswordActivity> rule = new ActivityTestRule<>(SetPasswordActivity.class);

    @Test
    public void setPassword_Success() {
        Log.e("@Test", "Performing setPassword success");
        onView(withId(R.id.passwordText))
                .perform(typeText(password));
        onView(withId(R.id.confirmPasswordText))
                .perform(typeText(correctConfirm));
        onView(withId(R.id.action_done))
                .perform(click());
    }

    @Test
    public void setPassword_Fail() {
        Log.e("@Test", "Performing setPassword fail");
        onView(withId(R.id.passwordText))
                .perform(typeText(password));
        onView(withId(R.id.confirmPasswordText))
                .perform(typeText(incorrectConfirm));
        onView(withId(R.id.action_done))
                .perform(click());
    }
}
