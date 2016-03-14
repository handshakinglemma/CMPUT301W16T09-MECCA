package mecca.meccurator;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;

/**
 * Created by cjvenhuis on 2016-02-27.
 */
public class testBidList extends ActivityInstrumentationTestCase2 {
    public testBidList() {
        super(HomeActivity.class);
    }

    public void testAddBid() {
        Bid bid = new Bid("Mercy", 5);
        BidList bidList = new BidList();
        // test that the bid is in the list once it's been added
        bidList.addBid(bid);
        assertTrue(bidList.hasBid(bid));
    }

    public void testHasBid() {
        Bid bid = new Bid("Mercy", 5);
        BidList bidList = new BidList();
        // assert that it doesn't have a bid
        assertFalse(bidList.hasBid(bid));
        // assert that it does once we add one
        bidList.addBid(bid);
        assertTrue(bidList.hasBid(bid));
    }

    public void testDeclineBid() {
        Bid bid1 = new Bid("Mercy", 5);
        Bid bid2 = new Bid("Emma", 6);
        Bid bid3 = new Bid("Chaitali", 9);
        BidList bidList = new BidList();
        bidList.addBid(bid1);
        // Test that a bid can be deleted
        bidList.declineBid(0);
        assertFalse(bidList.hasBid(bid1));
        // Test that just one bid will be deleted
        bidList.addBid(bid2);
        bidList.addBid(bid3);
        bidList.declineBid(1);
        assertTrue(bidList.hasBid(bid2));
    }

    public void testDeclineAllBids() {
        Bid bid1 = new Bid("Mercy", 5);
        Bid bid2 = new Bid("Emma", 6);
        Bid bid3 = new Bid("Chaitali", 9);
        BidList bidList = new BidList();
        // add a bunch of bids
        bidList.addBid(bid1);
        bidList.addBid(bid2);
        bidList.addBid(bid3);
        // Remove all bids
        bidList.declineAllBids();
        // Use has bid to check if all bids are removed
        assertFalse(bidList.hasBid(bid1));
        assertFalse(bidList.hasBid(bid2));
        assertFalse(bidList.hasBid(bid3));
    }

    public void testGetBid() {
        Bid bid1 = new Bid("Mercy", 5);
        Bid bid2 = new Bid("Emma", 6);
        BidList bidList = new BidList();
        bidList.addBid(bid1);
        bidList.addBid(bid2);
        // get the bid and make sure they're equal
        assertEquals(bid2, bidList.getBid(1));
        assertEquals(bid1, bidList.getBid(0));
    }

    public void testGetAllBids() {
        Bid bid1 = new Bid("Mercy", 5);
        Bid bid2 = new Bid("Emma", 6);
        BidList bidList = new BidList();
        bidList.addBid(bid1);
        bidList.addBid(bid2);

        ArrayList<Bid> expected = new ArrayList();
        expected.add(bid1);
        expected.add(bid2);

        assertEquals(expected, bidList.getAllBids());
    }

    public void testGetBidCount() {
        Bid bid1 = new Bid("Mercy", 5);
        Bid bid2 = new Bid("Emma", 6);
        BidList bidList = new BidList();
        bidList.addBid(bid1);
        bidList.addBid(bid2);
        // assert that the amount we have added is the same we are counting
        int bidCount = 2;
        assertEquals(bidCount, bidList.getBidCount());
    }

}
