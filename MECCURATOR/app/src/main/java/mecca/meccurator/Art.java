package mecca.meccurator;

import java.math.BigDecimal;
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
    private BigDecimal minprice;

    public Art(String workTitle, String workArtist, String artStatus) {
        
        title = workTitle;
        artist = workArtist;
        status = artStatus;

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

    public BigDecimal getMinprice() {
        return minprice;
    }

    public void setMinprice(BigDecimal minprice) {
        this.minprice = minprice;
    }
}
