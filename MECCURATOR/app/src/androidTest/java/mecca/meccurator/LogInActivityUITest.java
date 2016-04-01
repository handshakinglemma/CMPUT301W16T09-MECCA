package mecca.meccurator;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by mercywoldmariam on 2016-03-30.
 */
public class LogInActivityUITest
        extends ActivityInstrumentationTestCase2{

    Solo solo;

    public LogInActivityUITest(){
        super(ViewLoginActivity.class);
    }

    protected void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        solo.assertCurrentActivity("Set up method did not work.", ViewLoginActivity.class);
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    //US 03.01.01
    public void testSignUpButton() {
        solo.clickOnButton("Sign Up");
        solo.assertCurrentActivity("Did not open AddNewUserActivity", AddNewUserActivity.class);

        solo.enterText((EditText) solo.getView(R.id.enterUsername), "UserTest1");
        solo.enterText((EditText) solo.getView(R.id.enterEmail), "Email@Test1");
        solo.clickOnButton("Save");
        //solo.goBack();

        //TO DO
        //TEST USER IS ADDED TO LIST

        solo.assertCurrentActivity("Set up button did not work.", ViewLoginActivity.class);

        //Testing Login works
        solo.enterText((EditText) solo.getView(R.id.username), "UserTest1");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Log in button did not work.", HomeActivity.class);
    }

    //US 03.02.01
    public void testEditUserButton() {
        testSignUpButton();
        solo.assertCurrentActivity("Log in button did not work.", HomeActivity.class);

        View v = solo.getView(R.id.username2);
        solo.clickOnView(v);
        solo.assertCurrentActivity("View My profile button did not work", EditUserActivity.class);

        solo.enterText((EditText) solo.getView(R.id.enterUsername), "UserTest2");
        solo.enterText((EditText) solo.getView(R.id.enterEmail), "Email@Test2");
        solo.clickOnButton("Save");
        solo.assertCurrentActivity("Edit User button did not work", HomeActivity.class);

        assertTrue(solo.searchButton("UserTest2"));
    }
}