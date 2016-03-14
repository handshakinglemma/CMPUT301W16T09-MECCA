package mecca.meccurator;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Tests for Bid.
 * Created by cjvenhuis on 2016-02-27.
 */
public class testBid extends ActivityInstrumentationTestCase2{
    public testBid() {
        super(HomeActivity.class);
    }

    public void testGetRate() {
        String bidder = "venhuis";
        float rate = 9;
        Bid bid = new Bid("venhuis", rate);
        assertEquals(rate, bid.getRate());

        float returned = bid.getRate();
        assertEquals(rate, returned);
    }

    public void testSetRate() {
        String bidder = "venhuis";
        float rate = 9;
        Bid bid = new Bid("venhuis", rate);
        assertEquals(rate, bid.getRate());

        String newbidder = "emma";
        bid.setBidder(newbidder);
        assertEquals(newbidder,bid.getBidder());
    }

    public void testGetBidder() {
        String bidder = "venhuis";
        float rate = 9;
        Bid bid = new Bid("venhuis", rate);
        assertEquals(bidder, bid.getBidder());

        String returned = bid.getBidder();
        assertEquals(bidder, returned);
    }

    public void testSetBidder() {
        String bidder = "venhuis";
        float rate = 9;
        Bid bid = new Bid("venhuis", rate);
        assertEquals(rate, bid.getRate());

        float newrate = 15;
        bid.setRate(newrate);
        assertEquals(newrate,bid.getRate());
    }
}
