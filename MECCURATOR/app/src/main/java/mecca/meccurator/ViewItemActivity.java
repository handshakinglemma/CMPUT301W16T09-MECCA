package mecca.meccurator;

import java.math.BigDecimal;
import java.sql.Blob;

/**
 * Created by svetlanna on 16-02-27.
 */
public class ViewItemActivity {
    private BigDecimal bid;
    private Art item;
    private String usertype;
    private String status;
    private String owner;
    private String borrower;
    private String description;
    private String dimensions;
    private Blob photo;
    private BigDecimal minimumprice;
    private String artist;
    private String title;

    public BigDecimal getBid() {
        return bid;
    }

    public void setBid(BigDecimal bid) {
        this.bid = bid;
    }

    public Art getItem() {
        return item;
    }

    public void setItem(Art item) {
        this.item = item;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
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

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public Blob getPhoto() {
        return photo;
    }

    public void setPhoto(Blob photo) {
        this.photo = photo;
    }

    public BigDecimal getMinimumprice() {
        return minimumprice;
    }

    public void setMinimumprice(BigDecimal minimumprice) {
        this.minimumprice = minimumprice;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
