package mecca.meccurator;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Tests for UserList.
 * Created by cjvenhuis on 2016-02-27.
 */
public class testUserList extends ActivityInstrumentationTestCase2 {
    public testUserList() {
        super(HomeActivity.class);
    }

    public void testAddUser() {
        UserList users = new UserList();
        User user = new User("emma", "emma@ualberta.ca", null);
        // add user and assert that it's been added
        users.addUser(user);
        assertTrue(users.hasUser(user));
    }

    public void testHasUser() {
        UserList users = new UserList();
        User user = new User("emma", "emma@ualberta.ca", null);
        // assert that it has no users
        assertFalse(users.hasUser(user));
        // add a user
        users.addUser(user);
        // assert that it has user in it
        assertTrue(users.hasUser(user));
    }

    public void testUpdateUser() {
        // Nothing implemented to test
    }

    public void testDeleteUser() {
        UserList users = new UserList();
        User user = new User("emma", "emma@ualberta.ca", null);
        users.addUser(user);
        assertTrue(users.hasUser(user));

        users.deleteUser(user);
        assertFalse(users.hasUser(user));
    }

    public void testGetUsers() {
        /*UserList users = new UserList();
        User user = new User("emma", "emma@ualebrta.ca");
        users.addUser(user);

        ArrayList<User> returned = new ArrayList();
        returned.add(user);
        assertEquals(returned, users.getUsers());*/
    }

    public void testGetSize() {}
}
