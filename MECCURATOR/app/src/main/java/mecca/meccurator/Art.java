package mecca.meccurator;

import java.sql.Blob;

/**
 * Created by chaitali on 16-02-27.
 */
public class Art {

    private String status;
    private String owner;
    private String borrower;
    private String description;
    private String artist;
    private String title;
    private Blob photo;
    private String dimensions;
    private float minprice;


    public Art(String status, String owner, String borrower, String description, String artist, String title, String dimensions, float minprice) {
        this.status = status;
        this.owner = owner;
        this.borrower = borrower;
        this.description = description;
        this.artist = artist;
        this.title = title;
        //this.photo = photo;
        this.dimensions = dimensions;
        this.minprice = minprice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if( status.equals("available") ) {
            this.status = status;
        } else if ( status.equals("unavailable") ) {
            this.status = status;
        } else {
            // throw an exception?
        }
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Blob getPhoto() {
        return photo;
    }

    public void setPhoto(Blob photo) {
        this.photo = photo;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public float getMinprice() {
        return minprice;
    }

    public void setMinprice(float minprice) {
        this.minprice = minprice;
    }

<<<<<<< HEAD
    @Override
    public String toString()
    {
        return title + "    " + artist + "  " + minprice;
    }
=======
    public String toString(){
        return title;
    }

>>>>>>> 6209c79eb1722cfb61cd61c4c2971e5639f6a284
}
