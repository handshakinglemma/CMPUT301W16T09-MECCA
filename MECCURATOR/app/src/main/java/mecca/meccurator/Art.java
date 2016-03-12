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
    protected BidList bids;
    private float minprice;


    public Art(String status, String owner, String borrower, String description,
               String artist, String title, String dimensions, float minprice) {
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

    //add a get length method

    public String getLength(){
        return getDimensions().substring(0, getDimensions().indexOf("x"));
    }

    public String getWidth(){
        return getDimensions().substring(getDimensions().indexOf("x"));
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

    @Override
    public String toString(){
        return title;
    }

    public void addBids(BidList bids){
        this.bids = bids;
    }

    public BidList getBids(){

        if(bids == null) {
            bids = new BidList();
        }

        return bids;

    }

}
