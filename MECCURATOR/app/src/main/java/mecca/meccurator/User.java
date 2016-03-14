package mecca.meccurator;

import java.sql.Blob;
import java.util.ArrayList;

/**
 * To use the app, each user must have a user object associated with them. The username of the user
 * must be unique.
 */
public class User {


    private String username;
    private String email;
    protected ArtList myBids;

    // TODO pass parameter photo
    public User(String username, String email) {
        this.username = username;
        this.email = email;
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
}
