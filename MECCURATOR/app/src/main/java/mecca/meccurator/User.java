package mecca.meccurator;

import java.sql.Blob;
import java.util.ArrayList;

import io.searchbox.annotations.JestId;

/**
 * To use the app, each user must have a user object associated with them. The username of the user
 * must be unique.
 */
public class User {


    private String username;
    private String email;
    //protected NotificationList notifications;
    protected ArrayList<String> allNotifications;



    @JestId
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // TODO pass parameter photo
    public User(String username, String email, ArrayList<String> allNotifications) {
        this.username = username;
        this.email = email;
        this.allNotifications = allNotifications;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString(){
        return this.username;
    }

    public ArrayList<String> getAllNotifications() {
        return allNotifications;
    }

    public void setAllNotifications(ArrayList<String> allNotifications) {
        this.allNotifications = allNotifications;
    }
}
