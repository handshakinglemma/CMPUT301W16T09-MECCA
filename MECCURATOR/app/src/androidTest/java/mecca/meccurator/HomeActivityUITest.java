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

    public HomeActivityUITest(){
        super(ViewLoginActivity.class);
    }

    @Override
    protected void setUp(){
        solo = new Solo(getInstrumentation(), getActivity());

        solo.assertCurrentActivity("Set up method did not work.", ViewLoginActivity.class);

        solo.clickOnButton("Sign Up");
        solo.assertCurrentActivity("Did not open AddNewUserActivity", AddNewUserActivity.class);

        solo.enterText((EditText) solo.getView(R.id.enterUsername), "UserTest1");
        solo.enterText((EditText) solo.getView(R.id.enterEmail), "Email@Test1");
        solo.clickOnButton("Save");
        solo.goBack();
        solo.assertCurrentActivity("Set up button did not work.", ViewLoginActivity.class);

        solo.enterText((EditText) solo.getView(R.id.username), "UserTest1");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Log in button did not work.", HomeActivity.class);
    }

    @Override
    public void tearDown(){
        solo.finishOpenedActivities();
    }

    //US 05.03.01
    public void testNotificationsButton() {
        solo.clickOnButton("Notifications");
        solo.assertCurrentActivity("Did not open My Notifications", ViewNotificationsActivity.class);
    }

    //US 06.01.01
    public void testBorrowedButton() {
        solo.clickOnButton("My Borrowed Items");
        solo.assertCurrentActivity("Did not open My Borrowed Items", BorrowedItemsActivity.class);
    }

    //US 05.02.01
    public void testBidsButton() {
        solo.clickOnButton("My Bids");
        solo.assertCurrentActivity("Did not open My Bids", ViewMyBidsActivity.class);
    }

    //US 01.02.01
    public void testMyListingsButton() {
        solo.clickOnButton("My Listings");
        solo.assertCurrentActivity("Did not open My Listings", ViewMyListingsActivity.class);
    }
}
