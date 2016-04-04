package mecca.meccurator;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;

import com.robotium.solo.Solo;

/**
 * Created by mercywoldmariam on 2016-03-30.
 */
public class AddNewItemUITest extends ActivityInstrumentationTestCase2 {
    Solo solo;

    public AddNewItemUITest() {
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

    public void selectItem(Integer integer){
        ListView myListings = (ListView) solo.getView(R.id.oldArtListings);
        View artItem  = (View) myListings.getChildAt(integer);
        solo.clickLongOnView(artItem);
        solo.assertCurrentActivity("Edit Item long click did not work", EditItemActivity.class);
    }

    //US 01.01.01
    //As an owner, I want to add a thing in my things, each denoted with a clear, suitable description.
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

    public void DeleteItem() {
        //TEST THIS LINE AND CHECK INTENT
        selectItem(0);

        solo.clickOnButton("Delete");
        solo.assertCurrentActivity("Delete button did not work", ViewMyListingsActivity.class);
    }

    public void testAddNewItem() {
        AddNewItem();
        DeleteItem();
    }

    //US 01.02.01
    public void testListing() {
        AddNewItem();
        solo.assertCurrentActivity("Add Item button did not work.", ViewMyListingsActivity.class);

        View v = solo.getView(R.id.additembutton);
        solo.clickOnView(v);
        solo.assertCurrentActivity("Add New Item button did not work", AddNewItemActivity.class);

        solo.enterText((EditText) solo.getView(mecca.meccurator.R.id.enterTitle), "ArtworkTitleTest2");
        solo.enterText((EditText) solo.getView(R.id.enterArtist), "ArtworkArtistTest2");
        solo.enterText((EditText) solo.getView(R.id.enterDescription), "ArtworkDescriptionTest2");
        solo.enterText((EditText) solo.getView(R.id.enterMinPrice), "2.00");
        solo.enterText((EditText) solo.getView(R.id.enterLengthDimensions), "2");
        solo.enterText((EditText) solo.getView(R.id.enterWidthDimensions), "2");
        solo.clickOnButton("Save");
        solo.assertCurrentActivity("Add Item button did not work.", ViewMyListingsActivity.class);

        assertTrue(solo.searchText("ArtworkTitleTest1"));
        assertTrue(solo.searchText("Available"));
        assertTrue(solo.searchText("ArtworkDescriptionTest1"));

        assertTrue(solo.searchText("ArtworkTitleTest2"));
        assertTrue(solo.searchText("ArtworkDescriptionTest2"));

        DeleteItem();
        DeleteItem();
    }

    //US 01.03.01
    public void testViewItem() {
        AddNewItem();
        solo.assertCurrentActivity("Add Item button did not work.", ViewMyListingsActivity.class);

        //TEST THIS LINE AND CHECK INTENT
        solo.clickLongInList(0);
        solo.assertCurrentActivity("Edit Item long click did not work", EditItemActivity.class);

        assertTrue(solo.searchEditText("ArtworkTitleTest1"));
        assertTrue(solo.searchText("Available"));
        assertTrue(solo.searchEditText("ArtworkDescriptionTest1"));

        solo.goBack();
        DeleteItem();
    }

    //US 05.05.01
    public void testViewItemBids() {
        AddNewItem();
        solo.assertCurrentActivity("Add Item button did not work.", ViewMyListingsActivity.class);

        //TEST THIS LINE AND CHECK INTENT
        solo.clickLongInList(0);
        solo.assertCurrentActivity("Edit Item long click did not work", EditItemActivity.class);

        solo.clickOnButton("Item Bids");
        solo.assertCurrentActivity("Cannot view bids on Item", ViewItemBidsActivity.class);

        solo.goBack();
        solo.goBack();
        DeleteItem();
    }

    //US 01.04.01
    public void testEditItem() {
        AddNewItem();
        solo.assertCurrentActivity("Add Item button did not work.", ViewMyListingsActivity.class);
        //TEST THIS LINE AND CHECK INTENT
        selectItem(0);
        solo.assertCurrentActivity("Edit Item long click did not work", EditItemActivity.class);

        solo.clearEditText((EditText) solo.getView(mecca.meccurator.R.id.enterTitle));
        solo.enterText((EditText) solo.getView(mecca.meccurator.R.id.enterTitle), "ArtworkTitleTestEdit");
        solo.clearEditText((EditText) solo.getView(R.id.enterArtist));
        solo.enterText((EditText) solo.getView(R.id.enterArtist), "ArtworkArtistTestEdit");
        solo.clearEditText((EditText) solo.getView(R.id.enterDescription));
        solo.enterText((EditText) solo.getView(R.id.enterDescription), "ArtworkDescriptionTestEdit");
        solo.clearEditText((EditText) solo.getView(R.id.enterMinPrice));
        solo.enterText((EditText) solo.getView(R.id.enterMinPrice), "5.00");
        solo.clearEditText((EditText) solo.getView(R.id.enterLengthDimensions));
        solo.enterText((EditText) solo.getView(R.id.enterLengthDimensions), "5");
        solo.clearEditText((EditText) solo.getView(R.id.enterArtist));
        solo.enterText((EditText) solo.getView(R.id.enterArtist), "5");
        solo.clickOnButton("Save");
        solo.assertCurrentActivity("Add Item button did not work.", ViewMyListingsActivity.class);

        DeleteItem();
    }

    //US 01.05.01
    public void testDeleteItem() {
        AddNewItem();
        DeleteItem();
    }

    //US 03.03.01
    public void testFindUsername() {
        AddNewItem();
        solo.assertCurrentActivity("Add Item button did not work.", ViewMyListingsActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Go Back button did not work", HomeActivity.class);

        solo.clickOnButton("Logout");
        solo.assertCurrentActivity("Log out button did not work", ViewLoginActivity.class);

        solo.enterText((EditText) solo.getView(R.id.username), "UserTest2");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Log in button did not work.", HomeActivity.class);

        View search = solo.getView(R.id.ViewSearchButtonID);
        solo.enterText((EditText) solo.getView(R.id.editText2), "ArtworkDescriptionTest1");
        solo.clickOnView(search);
        solo.assertCurrentActivity("Did not search keyword", ViewSearchActivity.class);

        solo.clickLongInList(0);
        solo.assertCurrentActivity("Did not open Item profile", AddNewBidActivity.class);

        solo.clickOnButton("Contact Info");
        solo.assertCurrentActivity("Did not open contact info", ViewUserProfileActivity.class);
        solo.goBack();
        solo.goBack();
        solo.goBack();
        solo.assertCurrentActivity("Log in button did not work.", HomeActivity.class);

        solo.clickOnButton("Logout");
        solo.assertCurrentActivity("Log out button did not work", ViewLoginActivity.class);

        solo.enterText((EditText) solo.getView(R.id.username), "UserTest1");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Log in button did not work.", HomeActivity.class);

        solo.clickOnButton("My Listings");
        solo.assertCurrentActivity("Add Item button did not work.", ViewMyListingsActivity.class);
        //TEST THIS LINE AND CHECK INTENT
        DeleteItem();
    }

    //US 04.01.01 and 04.02.01
    public void testSearchResults() {
        AddNewItem();
        solo.waitForActivity("ViewMyListingActivity", 2000);
        solo.assertCurrentActivity("Add Item button did not work.", ViewMyListingsActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("Go Back button did not work", HomeActivity.class);
        solo.clickOnButton("Logout");
        solo.waitForActivity("ViewLoginActivity", 2000);
        solo.assertCurrentActivity("Log out button did not work", ViewLoginActivity.class);

        solo.enterText((EditText) solo.getView(R.id.username), "UserTest2");
        solo.clickOnButton("Login");
        solo.waitForActivity("HomeActivity", 2000);
        solo.assertCurrentActivity("Log in button did not work.", HomeActivity.class);

        View search = solo.getView(R.id.ViewSearchButtonID);
        solo.enterText((EditText) solo.getView(R.id.editText2), "ArtworkDescriptionTest1");
        solo.clickOnView(search);
        solo.waitForActivity("ViewSearchActivity", 2000);
        solo.assertCurrentActivity("Did not search keyword", ViewSearchActivity.class);

        assertTrue(solo.searchText("ArtworkTitleTest1"));
        assertTrue(solo.searchText("ArtworkDescriptionTest1"));
        assertTrue(solo.searchText("Available"));

        solo.goBack();
        solo.clickOnButton("My Listings");
        solo.assertCurrentActivity("Did not open my listings", ViewMyListingsActivity.class);
        DeleteItem();
    }

    //US 08.01.01
    public void testOffline() {
        solo.setWiFiData(false);

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

        solo.setWiFiData(true);
        DeleteItem();
    }
}
