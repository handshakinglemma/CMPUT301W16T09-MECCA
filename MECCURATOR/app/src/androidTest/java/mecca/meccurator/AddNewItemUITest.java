package mecca.meccurator;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by mercywoldmariam on 2016-03-30.
 */
public class AddNewItemUITest extends ActivityInstrumentationTestCase2 {
    Solo solo;

    public AddNewItemUITest() {

       super(AddNewItemActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
        solo.assertCurrentActivity("Set up method did not work.", AddNewItemActivity.class);
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    //US 01.01.01
    //As an owner, I want to add a thing in my things, each denoted with a clear, suitable description.
    public void testAddNewItem() {

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

        solo.clickOnButton("MY LISTINGS");
        solo.assertCurrentActivity("My Listings button did not work.", ViewMyListingsActivity.class);

        solo.enterText((EditText) solo.getView(mecca.meccurator.R.id.enterTitle), "ArtworkTitleTest1");
        solo.enterText((EditText) solo.getView(R.id.enterArtist), "ArtworkArtistTest1");
        solo.enterText((EditText) solo.getView(R.id.enterDescription), "ArtworkDescriptionTest1");
        solo.enterText((EditText) solo.getView(R.id.enterMinPrice), "1.00");
        solo.enterText((EditText) solo.getView(R.id.enterLengthDimensions), "1");
        solo.enterText((EditText) solo.getView(R.id.enterArtist), "1");
        solo.clickOnButton("Save");
        solo.waitForActivity("ViewMyListingsActivity", 2000);
        solo.assertCurrentActivity("Add Item button did not work.", ViewMyListingsActivity.class);


    }
}