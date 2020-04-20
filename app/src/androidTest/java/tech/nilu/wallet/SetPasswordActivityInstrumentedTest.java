package tech.nilu.wallet;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import tech.nilu.wallet.ui.password.SetPasswordActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

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
