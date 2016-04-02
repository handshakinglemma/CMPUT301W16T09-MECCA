package mecca.meccurator;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by mercywoldmariam on 2016-04-02.
 */
public class PhotoUITest extends ActivityInstrumentationTestCase2 {
    Solo solo;

    public PhotoUITest() {
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

    public void testTakePhoto() {
        solo.clickOnButton("My Listings");
        solo.assertCurrentActivity("My Listings button did not work.", ViewMyListingsActivity.class);

        View v = solo.getView(R.id.additembutton);
        solo.clickOnView(v);
        solo.assertCurrentActivity("Add New Item button did not work", AddNewItemActivity.class);

        solo.clickOnImageButton(0);
        solo.goBack();
        solo.waitForActivity("AddNewItemActivity.class", 2000);
        solo.assertCurrentActivity("Add New Item button did not work", AddNewItemActivity.class);
    }

    public void testDeleteButton() {
        solo.clickOnButton("My Listings");
        solo.assertCurrentActivity("My Listings button did not work.", ViewMyListingsActivity.class);

        View v = solo.getView(R.id.additembutton);
        solo.clickOnView(v);
        solo.assertCurrentActivity("Add New Item button did not work", AddNewItemActivity.class);

        assertTrue(solo.searchText("Cancel Photo"));
    }

    public void testViewButton() {
        //Test Owners has an Image view
        solo.clickOnButton("My Listings");
        solo.assertCurrentActivity("My Listings button did not work.", ViewMyListingsActivity.class);

        View v = solo.getView(R.id.additembutton);
        solo.clickOnView(v);
        solo.assertCurrentActivity("Add New Item button did not work", AddNewItemActivity.class);

        View image = solo.getView(R.id.imageView1);
        assertNotNull(image);

        solo.enterText((EditText) solo.getView(mecca.meccurator.R.id.enterTitle), "ArtworkTitleTest1");
        solo.enterText((EditText) solo.getView(R.id.enterArtist), "ArtworkArtistTest1");
        solo.enterText((EditText) solo.getView(R.id.enterDescription), "ArtworkDescriptionTest1");
        solo.enterText((EditText) solo.getView(R.id.enterMinPrice), "1.00");
        solo.enterText((EditText) solo.getView(R.id.enterLengthDimensions), "1");
        solo.enterText((EditText) solo.getView(R.id.enterWidthDimensions), "1");

        solo.clickOnButton("Save");
        solo.waitForActivity("ViewMyListingsActivity.class", 2000);
        solo.assertCurrentActivity("Add Item button did not work.", ViewMyListingsActivity.class);

        //Test borrower can see an image view
        solo.goBack();
        solo.assertCurrentActivity("Did not reach HomeActivity", HomeActivity.class);

        solo.clickOnButton("Logout");
        solo.assertCurrentActivity("Did not log out", ViewLoginActivity.class);

        logIn("UserTest2");
        solo.assertCurrentActivity("Did not reach HomeActivity", HomeActivity.class);

        View search = solo.getView(R.id.ViewSearchButtonID);
        solo.enterText((EditText) solo.getView(R.id.editText2), "ArtworkDescriptionTest1");
        solo.clickOnView(search);
        solo.assertCurrentActivity("Did not search keyword", ViewSearchActivity.class);

        solo.clickLongInList(0);
        solo.assertCurrentActivity("Did not open Item profile", AddNewBidActivity.class);

        View imageBorrower = solo.getView(R.id.imageView1);
        assertNotNull(imageBorrower);
    }


}
