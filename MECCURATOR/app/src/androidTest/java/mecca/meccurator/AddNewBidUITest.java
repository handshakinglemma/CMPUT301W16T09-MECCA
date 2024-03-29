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

    public void BidOnItem() {
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
        solo.assertCurrentActivity("Did not return to ViewSearchActivity", HomeActivity.class);
        solo.clickOnButton("Logout");
        solo.assertCurrentActivity("Log out button did not work", ViewLoginActivity.class);
    }

    public void AcceptBid() {
        BidOnItem();
        logIn("UserTest1");
        solo.clickOnButton("My Listings");
        solo.assertCurrentActivity("Did not open My listings", ViewMyListingsActivity.class);

        View spinner = solo.getView(Spinner.class, 0);
        solo.clickOnView(spinner);
        solo.clickOnView(solo.getView(TextView.class, 2));
        solo.clickLongInList(0);
        solo.assertCurrentActivity("Did not open Item profile", EditItemActivity.class);

        solo.clickOnButton("Item Bids");
        solo.assertCurrentActivity("Did not open Item Bids", ViewItemBidsActivity.class);

        solo.clickLongInList(0);
        solo.assertCurrentActivity("Did not view Bid", PlacePickerActivity.class);

        solo.clickOnButton("Accept");
        solo.waitForActivity("PlacePickerActivity", 2000);
        solo.clickLongOnScreen(339, 838);

        solo.waitForActivity("HomeActivity", 2000);
        solo.assertCurrentActivity("Did not open home activity", HomeActivity.class);
    }

    //This tst case tests the following use cases:
    // First its makes a bid: US 05.01.01
    //Then views the bids on it in order to accept it: US 05.05.01
    // Then in order to reset the bid, it views a list of its items being borrowed: US 06.02.01
    // it resets the bid to Available: US 07.01.01
    public void testAcceptingResettingGeoLocationBid() {
        AcceptBid();
        solo.assertCurrentActivity("Did not open home activity", HomeActivity.class);

        solo.clickOnButton("My Listings");
        solo.assertCurrentActivity("Did not open My Listings", ViewMyListingsActivity.class);

        solo.pressSpinnerItem(0, 4);
        solo.waitForText("Borrowed Items");
        solo.clickLongInList(0);
        solo.assertCurrentActivity("Did not open Item profile", EditItemActivity.class);

        solo.clickOnButton("Set Available");
        solo.assertCurrentActivity("ViewMyListingsActivity", ViewMyListingsActivity.class);

        deleteItem();

    }

    //US 05.07.01
    public void testDeclineButton() {
        BidOnItem();
        logIn("UserTest1");

        solo.clickOnButton("Notifications");
        solo.assertCurrentActivity("Did not open Notifications Activity", ViewNotificationsActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Did not go back to home Activity", HomeActivity.class);

        solo.clickOnButton("My Listings");
        solo.assertCurrentActivity("Did not open My listings", ViewMyListingsActivity.class);

        View spinner = solo.getView(Spinner.class, 0);
        solo.clickOnView(spinner);
        solo.clickOnView(solo.getView(TextView.class, 2));
        solo.clickLongInList(0);
        solo.assertCurrentActivity("Did not open Item profile", EditItemActivity.class);

        solo.clickOnButton("Item Bids");
        solo.assertCurrentActivity("Did not open Item Bids", ViewItemBidsActivity.class);

        solo.clickLongInList(0);
        solo.assertCurrentActivity("Did not view Bid", EditBidStatusActivity.class);

        solo.clickOnButton("Decline");
        solo.assertCurrentActivity("Did not open Item profile", EditItemActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("ViewMyListingsActivity", ViewMyListingsActivity.class);

        deleteItem();
    }

    //US 06.02.02
    public void testViewBorrowed() {
        solo.clickOnButton("My Listings");
        solo.assertCurrentActivity("Did not open my listings", ViewMyListingsActivity.class);

        View spinner = solo.getView(Spinner.class, 0);
        solo.clickOnView(spinner);
        solo.waitForText("Bidded Items");
        solo.clickOnView(solo.getView(TextView.class, 3));

        assertTrue(solo.searchText("Borrowed Items"));
        assertEquals(false, solo.searchText("Available"));
        assertEquals(false, solo.searchText("Bidded"));
    }

    public void testGeoLocationBorrower() {
        AcceptBid();
        solo.assertCurrentActivity("Did not open home activity", HomeActivity.class);

        solo.clickOnButton("Logout");
        solo.assertCurrentActivity("Log out button did not work", ViewLoginActivity.class);

        logIn("UserTest2");
        solo.clickOnButton("My Borrowed Items");
        solo.assertCurrentActivity("Did not take me to my borrowed items", BorrowedItemsActivity.class);

        assertTrue(solo.searchText("ArtworkTitleTest1"));
        solo.clickLongInList(0);
        solo.waitForActivity("ViewMeetupLocationActivity", 2000);
        solo.assertCurrentActivity("Opened Meetup location", ViewMeetupLocationActivity.class);

        solo.goBackToActivity("BorrowedItemsActivity");
        solo.goBack();
        solo.assertCurrentActivity("Did not go back to HomeActivity", HomeActivity.class);

        solo.clickOnButton("Logout");
        solo.assertCurrentActivity("Log out button did not work", ViewLoginActivity.class);

        logIn("UserTest1");

        solo.clickOnButton("My Listings");
        solo.assertCurrentActivity("Did not open My Listings", ViewMyListingsActivity.class);

        solo.pressSpinnerItem(0, 4);
        solo.waitForText("Borrowed Items");
        solo.clickLongInList(0);
        solo.assertCurrentActivity("Did not open Item profile", EditItemActivity.class);

        solo.clickOnButton("Set Available");
        solo.assertCurrentActivity("ViewMyListingsActivity", ViewMyListingsActivity.class);

        deleteItem();
    }
}
