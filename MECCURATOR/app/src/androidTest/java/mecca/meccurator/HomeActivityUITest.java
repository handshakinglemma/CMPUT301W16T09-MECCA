package mecca.meccurator;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;


/**
 * UI tests for Home Activity.
 * Created on 2016-02-27.
 */

public class HomeActivityUITest extends
        ActivityInstrumentationTestCase2{
    Solo solo;
   /* Instrumentation instrumentation;
    Activity activity;*/

    public HomeActivityUITest(){
        super(ViewLoginActivity.class);
    }

    @Override
    protected void setUp(){
        solo = new Solo(getInstrumentation(), getActivity());
        /*instrumentation = getInstrumentation();
        activity = getActivity();*/
        solo.assertCurrentActivity("Set up method did not work.", ViewLoginActivity.class);

        solo.clickOnButton("SIGN UP");
        solo.assertCurrentActivity("Did not open AddNewUserActivity", AddNewUserActivity.class);

        solo.enterText((EditText) solo.getView(R.id.enterUsername), "UserTest1");
        solo.enterText((EditText) solo.getView(R.id.enterUsername), "Email@Test1");
        solo.clickOnButton("SAVE");
        solo.goBack();
        solo.assertCurrentActivity("Set up button did not work.", ViewLoginActivity.class);

        solo.enterText((EditText) solo.getView(R.id.username), "UserTest1");
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Log in button did not work.", HomeActivity.class);
    }

    @Override
    public void tearDown(){
        solo.finishOpenedActivities();
    }

    //US 05.03.01
    public void testNotificationsButton() {
        solo.clickOnButton("NOTIFICATIONS");
        //solo.clickOnView(solo.getView(R.id.ViewNotificationsButtonID));
        //solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.ViewNotificationsButtonID));
        solo.waitForActivity(ViewNotificationsActivity.class, 2000);
        solo.assertCurrentActivity("Did not open My Notifications", ViewNotificationsActivity.class);
    }

    //US 06.01.01
    public void testBorrowedButton() {
        solo.clickOnButton("MY BORROWED ITEMS");
        solo.assertCurrentActivity("Did not open My Borrowed Items", BorrowedItemsActivity.class);
    }

    //US 05.02.01
    public void testBidsButton() {
        solo.clickOnButton("MY BIDS");
        solo.assertCurrentActivity("Did not open My Bids", ViewMyBidsActivity.class);
    }

    //US 01.02.01
    public void testMyListingsButton() {
        solo.clickOnButton("MY LISTINGS");
        solo.assertCurrentActivity("Did not open My Listings", ViewMyListingsActivity.class);
    }
}
