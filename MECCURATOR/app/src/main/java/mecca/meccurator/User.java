package mecca.meccurator;

import java.sql.Blob;
import java.util.ArrayList;

/**
 * Created by svetlanna on 16-02-27.
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

    public void myBidsPlaced(ArtList myBids, String username){

        this.myBids = myBids;
        this.username = username;
    }

    public ArrayList<Art> getMyBidsPlaced(){

        if(myBids == null) {
            myBids = new ArtList();
        }
        return myBids.getArtwork();
    }

}
