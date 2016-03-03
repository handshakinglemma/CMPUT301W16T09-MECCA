package mecca.meccurator;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by cjvenhuis on 2016-02-27.
 */
public class testArt extends ActivityInstrumentationTestCase2 {
    public testArt() {
        super(HomeActivity.class);
    }

    // The Art class as of March 2nd:
    //public Art(String status, String owner, String borrower, String description,
    //           String artist, String title, String dimensions, float minprice) {

    public void testSetStatus() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                           "Mercy", "Taste the Rainbow", "8x11 in", 1);

        // test that it is unavailable
        assertEquals("unavailable", art.getStatus());

        // test that it can be set to something else
        art.setStatus("available");
        assertEquals("available", art.getStatus());

        // test that it can be nothing else
        //art.setStatus("Nope");
        // Unnecessary because it shouldn't be possible to set it to
        // anything else.
    }

    public void testGetStatus() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        assertEquals("unavailable", art.getStatus());

        String returnedStatus = art.getStatus();
        assertEquals(returnedStatus, art.getStatus());
    }

    public void testSetOwner() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        assertEquals("Mercy", art.getOwner());

        art.setOwner("Colleen");
        assertEquals("Colleen", art.getOwner());
    }

    public void testGetOwner() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        assertEquals("Mercy", art.getOwner());

        String owner = art.getOwner();
        assertEquals(owner, art.getOwner());
    }

    public void testSetDescription() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        assertEquals("A bunch of colourful scribbles", art.getDescription());

        art.setDescription("A gorgeous rainbow pouring from the sky");
        assertEquals("A gorgeous rainbow pouring from the sky", art.getDescription());
    }

    public void testGetDescription() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        assertEquals("A bunch of colourful scribbles", art.getDescription());

        String description = art.getDescription();
        assertEquals(description, art.getDescription());
    }

    public void testSetBorrower() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        assertEquals("Chaitali", art.getBorrower());

        art.setBorrower("Colleen");
        assertEquals("Colleen", art.getBorrower());
    }

    public void testGetBorrower() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        assertEquals("Chaitali", art.getBorrower());

        String borrower = art.getBorrower();
        assertEquals(borrower, art.getBorrower());
    }

    public void testSetTitle() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        assertEquals("Taste the Rainbow", art.getTitle());

        art.setTitle("Sky Paint");
        assertEquals("Sky Paint", art.getTitle());
    }

    public void testGetTitle() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        assertEquals("Taste the Rainbow", art.getTitle());

        String title = art.getTitle();
        assertEquals(title, art.getTitle());
    }

    public void testSetArtist() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        assertEquals("Mercy", art.getArtist());

        art.setArtist("Colleen");
        assertEquals("Colleen", art.getArtist());
    }

    public void testGetArtist() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        assertEquals("Mercy", art.getArtist());

        String artist = art.getArtist();
        assertEquals(artist, art.getArtist());
    }
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
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        assertEquals("8x11 in", art.getDimensions());

        art.setDimensions("11x8 in");
        assertEquals("11x8 in", art.getDimensions());
    }

    public void testGetDimensions() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        assertEquals("8x11 in", art.getDimensions());

        String dimensions = art.getDimensions();
        assertEquals(dimensions, art.getDimensions());
    }

    public void testSetMinprice() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        Float minprice1 = new Float(1);
        assertEquals(minprice1,art.getMinprice());

        art.setMinprice(2);
        Float minprice2 = new Float(2);
        assertEquals(minprice2,art.getMinprice());
    }

    public void testGetMinprice() {
        Art art = new Art("unavailable", "Mercy", "Chaitali", "A bunch of colourful scribbles",
                "Mercy", "Taste the Rainbow", "8x11 in", 1);
        Float minprice1 = new Float(1);
        assertEquals(minprice1, art.getMinprice());

        Float minprice2 = art.getMinprice();
        assertEquals(minprice2, art.getMinprice());
    }
}
