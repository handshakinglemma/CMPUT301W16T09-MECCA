package mecca.meccurator;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;

/**
 * Tests for ArtList.
 * Created by cjvenhuis on 2016-02-27.
 */
public class testArtList extends ActivityInstrumentationTestCase2 {
    public testArtList() {
        super(HomeActivity.class);
    }

    public void testAddItem() {

         ArtList myArt = new ArtList();

         // Assumes owner is logged in and status is set to available, with no borrower
         Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                 "Mercy", "Taste the Rainbow", "8x11 in", 1);
         myArt.addItem(art);

         // check if allArt contains the new item
         assertTrue(myArt.hasItem(art));

    }

    public void testGetItem() {
        ArtList myArt = new ArtList();
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        myArt.addItem(art);

        // check if we get art using getItem
        Art returned = myArt.getItem(0);
        assertEquals(returned, art);
    }

    // Test that hasItem(item) returns true if the item is in ArtList
    public void testHasItem() {

        ArtList myArt = new ArtList();
        Art art1 = new Art("available", "Mercy", "", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        Art art2 = new Art("available", "Colleen", "", "A Giraffe eating an Apple",
                "Mercy", "Who made gravity?", "8x11 in", 1);

        // check that ArtList is empty
        assertFalse(myArt.hasItem(art1));
        assertFalse(myArt.hasItem(art2));

        // add items to myArt
        myArt.addItem(art1);
        myArt.addItem(art2);

        // check that ArtList has items in it
        assertTrue(myArt.hasItem(art1));
        assertTrue(myArt.hasItem(art2));
    }

    public void testDeleteItem() {

        ArtList myArt = new ArtList();
        Art art = new Art("available", "Mercy", "", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        myArt.addItem(art);
        //check if actually added to listings
        assertTrue(myArt.hasItem(art));

        myArt.deleteItem(art);
        //check if item actually deleted
        assertFalse(myArt.hasItem(art));

        //check multiple deletes
        Art art2 = new Art("available", "Mercy", "", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        Art art3 = new Art("available", "Colleen", "", "A Giraffe eating an Apple",
                "Mercy", "Who made Gravity?", "8x11 in", 1);

        myArt.addItem(art2);
        myArt.addItem(art3);
        //check if actually added to listings
        assertTrue(myArt.hasItem(art2));
        assertTrue(myArt.hasItem(art3));
        // Check if we can delete just one
        myArt.deleteItem(art2);
        assertFalse(myArt.hasItem(art2));
        assertTrue(myArt.hasItem(art3));
    }

    public void testGetArtwork() {
        ArtList myArt = new ArtList();
        Art art1 = new Art("available", "Mercy", "", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        Art art2 = new Art("available", "Colleen", "", "A Giraffe eating an Apple",
                "Mercy", "Who made Gravity?", "8x11 in", 1);

        myArt.addItem(art1);
        myArt.addItem(art2);

        ArtList returned = new ArtList();
        returned.addItem(art1);
        // test that artlists are not equal
        assertFalse(myArt.getArtwork() == returned.getArtwork());
        // test that they are equal
        returned.addItem(art2);
        assertEquals(myArt.getArtwork(), returned.getArtwork());
    }

    public void testRemove() {
        ArtList myArt = new ArtList();
        Art art1 = new Art("available", "Mercy", "", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        Art art2 = new Art("available", "Colleen", "", "A Giraffe eating an Apple",
                "Mercy", "Who made Gravity?", "8x11 in", 1);
        myArt.addItem(art1);
        myArt.addItem(art2);

        myArt.remove(1);

        assertTrue(myArt.hasItem(art1));
        assertFalse(myArt.hasItem(art2));

        myArt.remove(0);
        assertFalse(myArt.hasItem(art1));
    }
}
