package tech.nilu.wallet;

import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import tech.nilu.wallet.ui.wallets.creation.CreateWalletActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateWalletInstrumentedTest {
    @Rule
    public ActivityTestRule<CreateWalletActivity> activityRule = new ActivityTestRule<CreateWalletActivity>(CreateWalletActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            return new Intent().putExtra(CreateWalletActivity.NETWORK, 512L);
        }
    };

    private String stringToBeTyped;

    @Before
    public void initValidString() {
        stringToBeTyped = "NoVd";
    }

    @Test
    public void setText_sameActivity() {
        onView(withId(R.id.nameText))
                .perform(typeText(stringToBeTyped), closeSoftKeyboard());
        onView(withId(R.id.createButton))
                .perform(click());
    }
}
