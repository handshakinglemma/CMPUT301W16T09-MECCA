package mecca.meccurator;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Test for User.
 * Created by cjvenhuis on 2016-02-27.
 */
public class testUser extends ActivityInstrumentationTestCase2 {
    public testUser() {
        super(HomeActivity.class);
    }

    public void testGetUsername() {
        User user = new User("coolperson", "emma@sunshine.com", null, "none");
        assertEquals("coolperson", user.getUsername());

        String returned = user.getUsername();
        assertEquals(returned, user.getUsername());
    }

    public void testSetUsername() {
        User user = new User("coolperson", "emma@sunshine.com", null, "none");
        assertEquals("coolperson", user.getUsername());

        user.setUsername("awesomeperson");
        assertEquals("awesomeperson", user.getUsername());
    }

    public void testGetEmail() {
        User user = new User("coolperson", "emma@sunshine.com", null, "none");
        assertEquals("emma@sunshine.com", user.getEmail());

        String returned = user.getEmail();
        assertEquals(returned, user.getEmail());
    }

    public void testSetEmail() {
        User user = new User("coolperson", "emma@sunshine.com", null, "none");
        assertEquals("emma@sunshine.com", user.getEmail());

        user.setEmail("mcdonald@sunshine.com");
        assertEquals("mcdonald@sunshine.com", user.getEmail());
    }
}
