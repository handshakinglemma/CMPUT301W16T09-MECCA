package mecca.meccurator;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;

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

        solo.clickOnButton("Sign Up");
        solo.assertCurrentActivity("Did not open AddNewUserActivity", AddNewUserActivity.class);

        solo.enterText((EditText) solo.getView(R.id.enterUsername), "UserTest1");
        solo.enterText((EditText) solo.getView(R.id.enterEmail), "Email@Test1");
        solo.clickOnButton("Save");
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

    //US 01.01.01
    //As an owner, I want to add a thing in my things, each denoted with a clear, suitable description.
    public void testAddNewItem() {
        solo.clickOnButton("My Listings");
        solo.assertCurrentActivity("My Listings button did not work.", ViewMyListingsActivity.class);

        View v = solo.getView(R.id.additembutton);
        solo.clickOnView(v);
        solo.assertCurrentActivity("Add New Item button did not work", AddNewUserActivity.class);

        solo.enterText((EditText) solo.getView(mecca.meccurator.R.id.enterTitle), "ArtworkTitleTest1");
        solo.enterText((EditText) solo.getView(R.id.enterArtist), "ArtworkArtistTest1");
        solo.enterText((EditText) solo.getView(R.id.enterDescription), "ArtworkDescriptionTest1");
        solo.enterText((EditText) solo.getView(R.id.enterMinPrice), "1.00");
        solo.enterText((EditText) solo.getView(R.id.enterLengthDimensions), "1");
        solo.enterText((EditText) solo.getView(R.id.enterArtist), "1");
        solo.clickOnButton("Save");
        solo.assertCurrentActivity("Add Item button did not work.", ViewMyListingsActivity.class);
    }

    //US 01.02.01
    public void testListing() {
        testAddNewItem();
        solo.assertCurrentActivity("Add Item button did not work.", ViewMyListingsActivity.class);

        View v = solo.getView(R.id.additembutton);
        solo.clickOnView(v);
        solo.assertCurrentActivity("Add New Item button did not work", AddNewUserActivity.class);

        solo.enterText((EditText) solo.getView(mecca.meccurator.R.id.enterTitle), "ArtworkTitleTest2");
        solo.enterText((EditText) solo.getView(R.id.enterArtist), "ArtworkArtistTest2");
        solo.enterText((EditText) solo.getView(R.id.enterDescription), "ArtworkDescriptionTest2");
        solo.enterText((EditText) solo.getView(R.id.enterMinPrice), "2.00");
        solo.enterText((EditText) solo.getView(R.id.enterLengthDimensions), "2");
        solo.enterText((EditText) solo.getView(R.id.enterArtist), "2");
        solo.clickOnButton("Save");

        solo.assertCurrentActivity("Add Item button did not work.", ViewMyListingsActivity.class);

        assertTrue(solo.searchText("ArtworkTitleTest1"));
        assertTrue(solo.searchText("Available"));
        assertTrue(solo.searchText("ArtworkDescriptionTest1"));

        assertTrue(solo.searchText("ArtworkTitleTest2"));
        assertTrue(solo.searchText("ArtworkDescriptionTest2"));
    }

    //US 01.03.01
    public void testViewItem() {
        testAddNewItem();
        solo.assertCurrentActivity("Add Item button did not work.", ViewMyListingsActivity.class);

        //TEST THIS LINE AND CHECK INTENT
        solo.clickLongInList(0);
        solo.assertCurrentActivity("Edit Item long click did not work", EditItemActivity.class);

        assertTrue(solo.searchEditText("ArtworkTitleTest1"));
        assertTrue(solo.searchEditText("Available"));
        assertTrue(solo.searchEditText("ArtworkDescriptionTest1"));
    }

    //US 05.05.01
    public void testViewItemBids() {
        testViewItem();
        solo.assertCurrentActivity("Edit Item long click did not work", EditItemActivity.class);

        solo.clickOnButton("Item Bids");
        solo.assertCurrentActivity("Cannot view bids on Item", ViewItemBidsActivity.class);
    }
    //US 01.04.01
    public void testEditItem() {
        testAddNewItem();
        solo.assertCurrentActivity("Add Item button did not work.", ViewMyListingsActivity.class);
        //TEST THIS LINE AND CHECK INTENT
        solo.clickLongInList(0);
        solo.assertCurrentActivity("Edit Item long click did not work", EditItemActivity.class);

        solo.enterText((EditText) solo.getView(mecca.meccurator.R.id.enterTitle), "ArtworkTitleTestEdit");
        solo.enterText((EditText) solo.getView(R.id.enterArtist), "ArtworkArtistTestEdit");
        solo.enterText((EditText) solo.getView(R.id.enterDescription), "ArtworkDescriptionTestEdit");
        solo.enterText((EditText) solo.getView(R.id.enterMinPrice), "5.00");
        solo.enterText((EditText) solo.getView(R.id.enterLengthDimensions), "5");
        solo.enterText((EditText) solo.getView(R.id.enterArtist), "5");
        solo.clickOnButton("Save");
        solo.assertCurrentActivity("Add Item button did not work.", ViewMyListingsActivity.class);
    }

    //US 01.05.01
    public void testDeleteItem() {
        testAddNewItem();
        solo.assertCurrentActivity("Add Item button did not work.", ViewMyListingsActivity.class);
        //TEST THIS LINE AND CHECK INTENT
        solo.clickLongInList(0);
        solo.assertCurrentActivity("Edit Item long click did not work", EditItemActivity.class);

        solo.clickOnButton("Delete");
        solo.assertCurrentActivity("Delete button did not work", ViewMyListingsActivity.class);
    }

    //US 03.03.01
    public void testFindUsername() {
        testAddNewItem();
        solo.assertCurrentActivity("Add Item button did not work.", ViewMyListingsActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("Go Back button did not work", HomeActivity.class);

        solo.clickOnButton("Logout");
        solo.assertCurrentActivity("Log out button did not work", ViewLoginActivity.class);

        solo.clickOnButton("Sign Up");
        solo.assertCurrentActivity("Did not open AddNewUserActivity", AddNewUserActivity.class);

        solo.enterText((EditText) solo.getView(R.id.enterUsername), "UserTest2");
        solo.enterText((EditText) solo.getView(R.id.enterEmail), "Email@Test2");
        solo.clickOnButton("Save");
        solo.assertCurrentActivity("Set up button did not work.", ViewLoginActivity.class);

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

    }

    //US 04.01.01 and 04.02.01
    public void testSearchResults() {
        testAddNewItem();
        solo.assertCurrentActivity("Add Item button did not work.", ViewMyListingsActivity.class);

        solo.goBack();
        solo.assertCurrentActivity("Go Back button did not work", HomeActivity.class);

        solo.clickOnButton("Logout");
        solo.assertCurrentActivity("Log out button did not work", ViewLoginActivity.class);

        solo.clickOnButton("Sign Up");
        solo.assertCurrentActivity("Did not open AddNewUserActivity", AddNewUserActivity.class);

        solo.enterText((EditText) solo.getView(R.id.enterUsername), "UserTest2");
        solo.enterText((EditText) solo.getView(R.id.enterEmail), "Email@Test2");
        solo.clickOnButton("Save");
        solo.assertCurrentActivity("Set up button did not work.", ViewLoginActivity.class);

        solo.enterText((EditText) solo.getView(R.id.username), "UserTest2");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Log in button did not work.", HomeActivity.class);

        View search = solo.getView(R.id.ViewSearchButtonID);
        solo.enterText((EditText) solo.getView(R.id.editText2), "ArtworkDescriptionTest1");
        solo.clickOnView(search);
        solo.assertCurrentActivity("Did not search keyword", ViewSearchActivity.class);

        assertTrue(solo.searchText("ArtworkTitleTest1"));
        assertTrue(solo.searchText("ArtworkDescriptionTest1"));
        assertTrue(solo.searchText("Avaiablle"));
    }
}
