package mecca.meccurator;

import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;

/**
 * Tests for Art.
 * Created by cjvenhuis on 2016-02-27.
 */
public class testArt extends ActivityInstrumentationTestCase2 {
    public testArt() {
        super(HomeActivity.class);
    }

    // The Art class as of March 2nd:
    //public Art(String status, String owner, String borrower, String description,
    //           String artist, String title, String dimensions, float minprice) {

    Bitmap thumbnail;

    public void testSetStatus() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1, thumbnail);

        // test that it is unavailable
        assertEquals("unavailable", art.getStatus());

        // test that it can be set to something else
        art.setStatus("available");
        assertEquals("available", art.getStatus());
    }

    public void testGetStatus() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1, thumbnail);
        assertEquals("unavailable", art.getStatus());

        String returnedStatus = art.getStatus();
        assertEquals(returnedStatus, art.getStatus());
    }

    public void testSetOwner() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1, thumbnail);
        assertEquals("Mercy", art.getOwner());

        art.setOwner("Colleen");
        assertEquals("Colleen", art.getOwner());
    }

    public void testGetOwner() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1, thumbnail);
        assertEquals("Mercy", art.getOwner());

        String owner = art.getOwner();
        assertEquals(owner, art.getOwner());
    }

    public void testSetDescription() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1, thumbnail);
        assertEquals("A bunch of colourful scribbles", art.getDescription());

        art.setDescription("A gorgeous rainbow pouring from the sky");
        assertEquals("A gorgeous rainbow pouring from the sky", art.getDescription());
    }

    public void testGetDescription() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1, thumbnail);
        assertEquals("A bunch of colourful scribbles", art.getDescription());

        String description = art.getDescription();
        assertEquals(description, art.getDescription());
    }

    public void testSetBorrower() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1, thumbnail);
        assertEquals("Chaitali", art.getBorrower());

        art.setBorrower("Colleen");
        assertEquals("Colleen", art.getBorrower());
    }

    public void testGetBorrower() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1, thumbnail);
        assertEquals("Chaitali", art.getBorrower());

        String borrower = art.getBorrower();
        assertEquals(borrower, art.getBorrower());
    }

    public void testSetTitle() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1, thumbnail);
        assertEquals("Taste the Rainbow", art.getTitle());

        art.setTitle("Sky Paint");
        assertEquals("Sky Paint", art.getTitle());
    }

    public void testGetTitle() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1, thumbnail);
        assertEquals("Taste the Rainbow", art.getTitle());

        String title = art.getTitle();
        assertEquals(title, art.getTitle());
    }

    public void testSetArtist() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1, thumbnail);
        assertEquals("Mercy", art.getArtist());

        art.setArtist("Colleen");
        assertEquals("Colleen", art.getArtist());
    }

    public void testGetArtist() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1, thumbnail);
        assertEquals("Mercy", art.getArtist());

        String artist = art.getArtist();
        assertEquals(artist, art.getArtist());
    }
    //THIS WAS COMMENTED OUT TO BEGIN WITH
/*  Not yet implemented
    public void testSetPhoto() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        assertEquals("", art.get());

        art.set();
        assertEquals("", art.get());
    }

    public void testGetPhoto() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        assertEquals("", art.get());

        String  = art.get();
        assertEquals(, art.get());
    }
*/
    public void testSetDimensions() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1, thumbnail);
        assertEquals("8x11 in", art.getDimensions());

        art.setDimensions("11x8 in");
        assertEquals("11x8 in", art.getDimensions());
    }

    public void testGetDimensions() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1, thumbnail);
        assertEquals("8x11 in", art.getDimensions());

        String dimensions = art.getDimensions();
        assertEquals(dimensions, art.getDimensions());
    }

    public void testSetMinprice() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1, thumbnail);
        Float minprice1 = new Float(1);
        assertEquals(minprice1,art.getMinprice());

        art.setMinprice(2);
        Float minprice2 = new Float(2);
        assertEquals(minprice2,art.getMinprice());
    }

    public void testGetMinprice() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1, thumbnail);
        Float minprice1 = new Float(1);
        assertEquals(minprice1, art.getMinprice());

        Float minprice2 = art.getMinprice();
        assertEquals(minprice2, art.getMinprice());
    }

    public void testSetBids() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1, thumbnail);
        Bid bid1 = new Bid("Mercy", 5);
        Bid bid2 = new Bid("Emma", 6);
        BidList bidList = new BidList();
        bidList.addBid(bid1);
        bidList.addBid(bid2);

        ArrayList<Bid> expected = new ArrayList();

        // assert that bids are empty
        assertEquals(art.getBids(), expected);
        // add bids to bidList and expected
        expected.add(bid1);
        expected.add(bid2);
        art.setBids(bidList);
        // assert that they are equal
        assertEquals(art.getBids(), expected);
    }

    public void testGetBids() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1, thumbnail);
        Bid bid1 = new Bid("Mercy", 5);
        Bid bid2 = new Bid("Emma", 6);
        BidList bidList = new BidList();
        bidList.addBid(bid1);
        bidList.addBid(bid2);
        // set Bids to bidList
        art.setBids(bidList);
        // create a replica
        ArrayList<Bid> expected = new ArrayList();
        expected.add(bid1);
        expected.add(bid2);
        // check that it was gotten
        assertEquals(art.getBids(), expected);
    }

    public void testGetBidLists() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1, thumbnail);
        Bid bid1 = new Bid("Mercy", 5);
        Bid bid2 = new Bid("Emma", 6);
        BidList bidList = new BidList();
        bidList.addBid(bid1);
        art.setBids(bidList);
        // set a pointer to bids in art
        BidList expected = art.getBidLists();
        // add something to bids
        bidList.addBid(bid2);
        // make sure they're the same bidList
        assertEquals(art.getBidLists(), expected);
    }

    public void testGetThumbnail(){

        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1, thumbnail);

        assertEquals(art.getThumbnail(), null);

    }
}
