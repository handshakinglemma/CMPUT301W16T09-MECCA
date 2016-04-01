package mecca.meccurator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import io.searchbox.annotations.JestId;

/**
 * Each art object has its own bid list attribute to keep track of the bids that have been
 * placed on it. Possible statuses are: available, bidded, or borrowed.
 */
public class Art {

    /* attributes for each art object */
    @JestId
    protected String id;
    private String status;
    private String owner;
    private String borrower;
    private String description;
    private String artist;
    private String title;
    protected transient Bitmap thumbnail;
    protected String thumbnailBase64;
    private String dimensions;
    protected BidList bids;
    private float minprice;

    public Art(String status, String owner, String borrower, String description,
               String artist, String title, String dimensions, float minprice, Bitmap thumbnail) {
        this.status = status;
        this.owner = owner;
        this.borrower = borrower;
        this.description = description;
        this.artist = artist;
        this.title = title;
        this.thumbnail = thumbnail;
        this.dimensions = dimensions;
        this.minprice = minprice;
        this.bids = getBidLists();

    }

    /* some standard getters and setters */

    public String getId() {
        return id;
    }

    // Set in AddNewItemActivity
    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public void addThumbnail(Bitmap newThumbnail){
        if (newThumbnail != null) {
            thumbnail = newThumbnail;
            ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
            newThumbnail.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);

            byte[] b = byteArrayBitmapStream.toByteArray();
            thumbnailBase64 = Base64.encodeToString(b, Base64.DEFAULT);
        }
    }

    public Bitmap getThumbnail(){
        if (thumbnail == null && thumbnailBase64 != null) {
            byte[] decodeString = Base64.decode(thumbnailBase64, Base64.DEFAULT);
            thumbnail = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        }
        return thumbnail;
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

    // Can eventually use this to set SeachResultActivity's listview view?
    public String seachResultString(){
        return title + description + owner + status;
    }

    // Can eventually use this to set BorrowedItemsActivity's listview view?
    public String borrowedString(){
        return title + description + borrower;
    }

    // Can eventually use this to set MyListingsActivity's All listings listview view?
    public String myListingsString(){
        return title + description + status;
    }

    // Can eventually use this to set MyListingsActivity's My borrowed items listview view?
    public String myborrowedString(){
        return title + description + owner;
    }

    @Override
    public String toString(){
        // Right, now this is customized for the MyListingsActivity's All listings listview
        //TODO customize this for the listview calling it
        // ie. some listviews should show different attributes
        String viewString = title;



        viewString += "\n" + description;
        return viewString;
    }

    public void setBids(BidList bids){
        this.bids = bids;
    }

    public ArrayList<Bid> getBids(){

        if(bids == null) {
            bids = new BidList();
        }

        return bids.getAllBids();

    }

    public BidList getBidLists(){

        if(bids == null) {
            bids = new BidList();
        }

        return bids;

    }
}
