package mecca.meccurator;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by cjvenhuis on 2016-02-27.
 */
public class testUser extends ActivityInstrumentationTestCase2 {
    public testUser() {
        super(HomeActivity.class);
    }

    public void testGetUsername() {
        /*User user = new User("coolperson", "Emma", "7801234567", "emma@sunshine.com");
        assertEquals("coolperson", user.getUsername());

        String returned = user.getUsername();
        assertEquals(returned, user.getUsername());*/
    }

    public void testSetUsername() {
        /*User user = new User("coolperson", "Emma", "7801234567", "emma@sunshine.com");
        assertEquals("coolperson", user.getUsername());

        user.setUsername("awesomeperson");
        assertEquals("awesomeperson", user.getUsername());*/
    }

    public void testGetName() {
        /*User user = new User("coolperson", "Emma", "7801234567", "emma@sunshine.com");
        assertEquals("Emma", user.getName());

        String returned = user.getName();
        assertEquals(returned, user.getName());*/
    }

    public void testSetName() {
        /*User user = new User("coolperson", "Emma", "7801234567", "emma@sunshine.com");
        assertEquals("Emma", user.getName());

        user.setName("McDonald");
        assertEquals("McDonald", user.getName());*/
    }

    public void testGetPhoneNumber() {
        /*User user = new User("coolperson", "Emma", "7801234567", "emma@sunshine.com");
        assertEquals("7801234567", user.getPhoneNumber());

        String returned = user.getPhoneNumber();
        assertEquals(returned, user.getPhoneNumber());*/
    }

    public void testSetPhoneNumber() {
        /*User user = new User("coolperson", "Emma", "7801234567", "emma@sunshine.com");
        assertEquals("7801234567", user.getPhoneNumber());

        user.setPhoneNumber("7807654321");
        assertEquals("7807654321", user.getPhoneNumber());*/
    }

    public void testGetPhoto() {
        /*User user = new User("coolperson", "Emma", "7801234567", "emma@sunshine.com");
        Blob photo = new Blob();
        assertEquals(photo, user.getPhoto());

        String returned = user.getEmail();
        assertEquals(returned, user.getEmail());*/
    }

    public void testSetPhoto() {
        /*User user = new User("coolperson", "Emma", "7801234567", "emma@sunshine.com");
        Blob photo = new Blob();
        assertEquals(photo, user.getPhoto());

        Blob picture = new Blob();
        user.setPhoto(picture);
        assertEquals(picture, user.getPhoto());*/
    }

    public void testGetEmail() {
        /*User user = new User("coolperson", "Emma", "7801234567", "emma@sunshine.com");
        assertEquals("emma@sunshine.com", user.getEmail());

        String returned = user.getEmail();
        assertEquals(returned, user.getEmail());*/
    }

    public void testSetEmail() {
        /*User user = new User("coolperson", "Emma", "7801234567", "emma@sunshine.com");
        assertEquals("emma@sunshine.com", user.getEmail());

        user.setEmail("mcdonald@sunshine.com");
        assertEquals("mcdonald@sunshine.com", user.getEmail());*/
    }
}
