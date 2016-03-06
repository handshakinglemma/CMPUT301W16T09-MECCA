package mecca.meccurator;

import java.sql.Blob;

/**
 * Created by svetlanna on 16-02-27.
 */
public class User {

    private String username;
    private String name;
    private String phoneNumber;
    private Blob photo;

    // TODO pass parameter photo
    public User(String username, String name, String phoneNumber) {
        this.username = username;
        this.name = name;
        this.phoneNumber = phoneNumber;
        //this.photo = photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Blob getPhoto() {
        return this.photo;
    }

    public void setPhoto(Blob photo) {
        this.photo = photo;
    }

    @Override
    public String toString(){
        return this.username;
    }

}
