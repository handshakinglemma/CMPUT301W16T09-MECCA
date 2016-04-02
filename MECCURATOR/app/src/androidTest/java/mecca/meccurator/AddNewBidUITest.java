package mecca.meccurator;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.robotium.solo.Solo;

/**
 * Created by mercywoldmariam on 2016-04-02.
 */
public class AddNewBidUITest extends ActivityInstrumentationTestCase2 {

    Solo solo;

    public AddNewBidUITest() {
        super(ViewLoginActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            }
        });

        solo.clickOnButton("Sign Up");
        solo.assertCurrentActivity("Did not open AddNewUserActivity", AddNewUserActivity.class);

        solo.enterText((EditText) solo.getView(R.id.enterUsername), "UserTest1");
        solo.enterText((EditText) solo.getView(R.id.enterEmail), "Email@Test1");
        solo.clickOnButton("Save");
        boolean saveView = solo.searchButton("Save");
        if(saveView){ solo.goBack();}
        solo.assertCurrentActivity("Set up button did not work.", ViewLoginActivity.class);

        solo.clickOnButton("Sign Up");
        solo.assertCurrentActivity("Did not open AddNewUserActivity", AddNewUserActivity.class);

        solo.enterText((EditText) solo.getView(R.id.enterUsername), "UserTest2");
        solo.enterText((EditText) solo.getView(R.id.enterEmail), "Email@Test2");
        solo.clickOnButton("Save");
        saveView = solo.searchButton("Save");
        if(saveView){ solo.goBack();}
        solo.assertCurrentActivity("Set up button did not work.", ViewLoginActivity.class);

        solo.enterText((EditText) solo.getView(R.id.username), "UserTest1");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Log in button did not work.", HomeActivity.class);
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void logIn(String CurrentUser) {
        solo.enterText((EditText) solo.getView(R.id.username), CurrentUser);
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Log in button did not work.", HomeActivity.class);
    }
    public void AddNewItem() {
        solo.clickOnButton("My Listings");
        solo.assertCurrentActivity("My Listings button did not work.", ViewMyListingsActivity.class);

        View v = solo.getView(R.id.additembutton);
        solo.clickOnView(v);
        solo.assertCurrentActivity("Add New Item button did not work", AddNewItemActivity.class);

        solo.enterText((EditText) solo.getView(mecca.meccurator.R.id.enterTitle), "ArtworkTitleTest1");
        solo.enterText((EditText) solo.getView(R.id.enterArtist), "ArtworkArtistTest1");
        solo.enterText((EditText) solo.getView(R.id.enterDescription), "ArtworkDescriptionTest1");
        solo.enterText((EditText) solo.getView(R.id.enterMinPrice), "1.00");
        solo.enterText((EditText) solo.getView(R.id.enterLengthDimensions), "1");
        solo.enterText((EditText) solo.getView(R.id.enterWidthDimensions), "1");

        solo.clickOnButton("Save");
        solo.waitForActivity("ViewMyListingsActivity.class", 2000);
        solo.assertCurrentActivity("Add Item button did not work.", ViewMyListingsActivity.class);
    }

    public void selectItem(Integer integer){
        ListView myListings = (ListView) solo.getView(R.id.oldArtListings);
        View artItem  = (View) myListings.getChildAt(integer);
        solo.clickLongOnView(artItem);
        solo.assertCurrentActivity("Edit Item long click did not work", EditItemActivity.class);
    }
    public void deleteItem() {
        //TEST THIS LINE AND CHECK INTENT
        selectItem(0);

        solo.clickOnButton("Delete");
        solo.assertCurrentActivity("Delete button did not work", ViewMyListingsActivity.class);
    }

    public void testBidOnItem() {
        AddNewItem();
        solo.assertCurrentActivity("Add Item button did not work.", ViewMyListingsActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Go Back button did not work", HomeActivity.class);

        solo.clickOnButton("Logout");
        solo.assertCurrentActivity("Log out button did not work", ViewLoginActivity.class);

        logIn("UserTest2");

        View search = solo.getView(R.id.ViewSearchButtonID);
        solo.enterText((EditText) solo.getView(R.id.editText2), "ArtworkDescriptionTest1");
        solo.clickOnView(search);
        solo.assertCurrentActivity("Did not search keyword", ViewSearchActivity.class);

        solo.clickLongInList(0);
        solo.assertCurrentActivity("Did not open Item profile", AddNewBidActivity.class);

        solo.enterText((EditText) solo.getView(R.id.enterRate), "10.00");
        solo.clickOnButton("Place Bid");
        solo.waitForActivity("ViewSearchActivity", 2000);
        solo.assertCurrentActivity("Did not return to ViewSearchActivity", ViewSearchActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("Did not return to ViewSearchActivity", ViewSearchActivity.class);
        solo.clickOnButton("Logout");
        solo.assertCurrentActivity("Log out button did not work", ViewLoginActivity.class);
    }

    public void testNotificationButton() {
        testBidOnItem();
        logIn("UserTest1");

        solo.clickOnButton("Notifications");
        solo.assertCurrentActivity("Did not open Notifications Activity", ViewNotificationsActivity.class);

        assertTrue(solo.searchText("UserTest2"));
        assertTrue(solo.searchText("ArtworkTitleTest1"));
        solo.goBack();
        solo.assertCurrentActivity("Did not go back", HomeActivity.class);
    }

    public void testAcceptBid() {
        testNotificationButton();
        solo.clickOnButton("My Listings");
        solo.assertCurrentActivity("Did not open My listings", ViewMyListingsActivity.class);

        View spinner = solo.getView(Spinner.class, 0);
        solo.clickOnView(spinner);
        solo.clickOnView(solo.getView(TextView.class, 3));
        solo.clickLongInList(0);
        solo.assertCurrentActivity("Did not open Item profile", EditItemActivity.class);

        solo.clickOnButton("Item Bids");
        solo.assertCurrentActivity("Did not open Item Bids", ViewItemBidsActivity.class);

        solo.clickLongInList(0);
        solo.assertCurrentActivity("Did not view Bid", EditBidStatusActivity.class);

        solo.clickOnButton("Accept");
        solo.assertCurrentActivity("Did not accept the bid", EditItemActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("Did not open My listings", ViewMyListingsActivity.class);

        deleteItem();
    }

    //DECLINE BUTTON CRASHES
    /*public void testDeclineButton() {
        testNotificationButton();
        solo.clickOnButton("My Listings");
        solo.assertCurrentActivity("Did not open My listings", ViewMyListingsActivity.class);

        View spinner = solo.getView(Spinner.class, 0);
        solo.clickOnView(spinner);
        solo.clickOnView(solo.getView(TextView.class, 3));
        solo.clickLongInList(0);
        solo.assertCurrentActivity("Did not open Item profile", EditItemActivity.class);

        solo.clickOnButton("Item Bids");
        solo.assertCurrentActivity("Did not open Item Bids", ViewItemBidsActivity.class);

        solo.clickLongInList(0);
        solo.assertCurrentActivity("Did not view Bid", EditBidStatusActivity.class);

        //solo.clickOnButton("Decline");
        //FIND OUT WHY IT Fails
    }*/

    //US 06.02.02
    public void testViewBorrowed() {
        solo.clickOnButton("My Listings");
        solo.assertCurrentActivity("Did not open my listings", ViewMyListingsActivity.class);

        View spinner = solo.getView(Spinner.class, 0);
        solo.clickOnView(spinner);
        solo.waitForText("Bidded Items");
        solo.clickOnView(solo.getView(TextView.class, 4));

        assertTrue(solo.searchText("Borrowed Items"));
        assertEquals(false, solo.searchText("Available"));
        assertEquals(false, solo.searchText("Bidded"));
    }

    public void testResetAvailabilityButton() {
        testNotificationButton();
        solo.clickOnButton("My Listings");
        solo.assertCurrentActivity("Did not open My listings", ViewMyListingsActivity.class);

        View spinner = solo.getView(Spinner.class, 0);
        solo.clickOnView(spinner);
        solo.clickOnView(solo.getView(TextView.class, 3));
        solo.clickLongInList(0);
        solo.assertCurrentActivity("Did not open Item profile", EditItemActivity.class);

        solo.clickOnButton("Item Bids");
        solo.assertCurrentActivity("Did not open Item Bids", ViewItemBidsActivity.class);

        solo.clickLongInList(0);
        solo.assertCurrentActivity("Did not view Bid", EditBidStatusActivity.class);

        solo.clickOnButton("Accept");
        solo.assertCurrentActivity("Did not accept the bid", EditItemActivity.class);

        solo.clickOnView(solo.getView(R.id.item_bids));
        //solo.assertCurrentActivity();

    }


}
