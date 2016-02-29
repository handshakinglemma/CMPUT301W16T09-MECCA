package mecca.meccurator;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by cjvenhuis on 2016-02-27.
 */
public class testArt extends ActivityInstrumentationTestCase2 {
    public testArt() {
        super(HomeActivity.class);
    }

    public void testSetStatus() {
        Art art = new Art("The Scream", "Edvard Munch", "available");

        // test that it can be unavailable
        art.setStatus("unavailable");
        assertEquals("unavailable", art.getStatus());

        // test that it can be available
        art.setStatus("available");
        assertEquals("available", art.getStatus());

        // test that it can be nothing else
        //art.setStatus("Nope");
        // Unnecessary because it shouldn't be possible to set it to
        // anything else.
    }

    public void testGetStatus() {
        Art art = new Art("The Scream", "Edvard Munch", "available");
        art.setStatus("unavailable");

        assertEquals(art.getStatus(), "unavailable");

        art.setStatus("available");
        assertEquals(art.getStatus(), "avaialable");
    }

    public void testSetOwner() {
        Art art = new Art("The Scream", "Edvard Munch", "available");
    }

    public void testGetOwner() {
        Art art = new Art("The Scream", "Edvard Munch", "available");
    }

    public void testSetDescription() {
        Art art = new Art("The Scream", "Edvard Munch", "available");
    }

    public void testGetDescription() {}

    public void testSetBorrower() {}

    public void testGetBorrower() {}

    public void testSetTitle() {}

    public void testGetTitle() {}

    public void testSetArtist() {}

    public void testGetArtist() {}

    public void testSetPhoto() {}

    public void testGetPhoto() {}

    public void testSetDimensions() {}

    public void testGetDimensions() {}

    public void testSetMinprice() {}

    public void testGetMinprice() {}
}
