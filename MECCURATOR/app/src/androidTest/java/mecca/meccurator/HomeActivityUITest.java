package mecca.meccurator;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.WindowManager;
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

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            }
        });

        solo.assertCurrentActivity("Set up method did not work.", ViewLoginActivity.class);

        solo.clickOnButton("Sign Up");
        solo.assertCurrentActivity("Did not open AddNewUserActivity", AddNewUserActivity.class);

        solo.enterText((EditText) solo.getView(R.id.enterUsername), "UserTest1");
        solo.enterText((EditText) solo.getView(R.id.enterEmail), "Email@Test1");
        solo.clickOnButton("Save");
        boolean saveView = solo.searchButton("Save");
        if(saveView){ solo.goBack(); }
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

    //US 04.01.01
    public void testMySearchButton() {
        View search = solo.getView(R.id.ViewSearchButtonID);

        solo.enterText((EditText) solo.getView(R.id.editText2), "Keyword1");
        solo.clickOnView(search);
        solo.assertCurrentActivity("Did not search keyword", ViewSearchActivity.class);

    }

    //WOW FACTOR Use Case: 11.01.01
    public void testWatchButton() {
        testNotificationsButton();
        solo.clickOnButton("Watch List");
        solo.assertCurrentActivity("Did not open watch list", ViewWatchListActivity.class);

        solo.enterText((EditText) solo.getView(R.id.editArtist), "WatchTest1");
        solo.clickOnButton("Save");
        solo.searchText("WatchTest1");
    }
}
