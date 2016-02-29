package mecca.meccurator;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by cjvenhuis on 2016-02-27.
 */
public class testArtList extends ActivityInstrumentationTestCase2 {
    public testArtList() {
        super(HomeActivity.class);
    }

    public void testAddItem() {

        ArtList myArt = new ArtList();

        // Assumes owner is logged in and status is set to available, with no borrower
        //Art art = new Art("The Scream", "Edvard Munch", "available");
        //myArt.addItem(art);

        // check if allArt contains the new item
        //assertTrue(myArt.hasItem(art));

    }

    // Test that hasItem(item) returns true if the item is in ArtList
    public void testHasItem() {

        ArtList myArt = new ArtList();
        //Art art1 = new Art("The Scream", "Edvard Munch", "available");
        //Art art2 = new Art("Mona Lisa", "Leonardo da Vinci", "available");

        // check that ArtList is empty
        //assertFalse(myArt.hasItem(art1));
        //assertFalse(myArt.hasItem(art2));

        // add items to myArt
        //myArt.addItem(art1);
        //myArt.addItem(art2);

        // check that ArtList has items in it
        //assertTrue(myArt.hasItem(art1));
        //assertTrue(myArt.hasItem(art2));
    }

    public void testUpdateItem() {}

    public void testDeleteItem() {

        ArtList myArt = new ArtList();
        //Art art = new Art("The Scream", "Edvard Munch", "available");
        //myArt.addItem(art);
        //check if actually added to listings
        //assertTrue(myArt.hasItem(art));

        //myArt.deleteItem(art);
        //check if item actually deleted
        //assertFalse(myArt.hasItem(art));

        //check multiple deletes
        //Art art2 = new Art("The Dream", "Edvard Munch", "available");
        //Art art3 = new Art("The Seam", "Edvard Munch", "available");
        //myArt.addItem(art2);
        //myArt.addItem(art3);
        //check if actually added to listings
        //assertTrue(myArt.hasItem(art2));
        //assertTrue(myArt.hasItem(art3));
        //delete item
        //myArt.deleteItem(art2);
        //myArt.deleteItem(art3);
        //assertFalse(myArt.hasItem(art2));
        //assertFalse(myArt.hasItem(art3));

    }
}
