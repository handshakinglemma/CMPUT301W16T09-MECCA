package mecca.meccurator;

import android.support.test.espresso.ViewAssertion;
import android.test.ActivityInstrumentationTestCase2;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * UI tests for Home Activity.
 * Created by cjvenhuis on 2016-02-27.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomeActivityUITest {

    @Rule
    public final ActivityTestRule<HomeActivity> homeActivityActivityTestRule =
            new ActivityTestRule<>(HomeActivity.class);

    /*@Test
    public void checkHomeDescription() {
        onView(withId(R.id.homeDescription))
                .check((matches(withText("Hello"))));
    }*/
}
