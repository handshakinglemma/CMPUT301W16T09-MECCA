package mecca.meccurator;

/**
 * Each bid is associated with a bid list and this bid list is associated with one art item.
 */
public class Bid {

    protected String bidder;
    protected float rate;

    /* each bid has a bidder who made the bid and a rate */
    public Bid(String bidder, float rate) {
        this.bidder = bidder;
        this.rate = rate;
    }

    public String getBidder() {
        return bidder;
    }

    public void setBidder(String bidder) {
        this.bidder = bidder;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    @Override
    public String toString(){
        // Right, now this is customized for the MyListingsActivity's All listings listview
        //TODO customize this for the listview calling it
        // ie. some listviews should show different attributes
        return bidder + " " + "$" + rate;
    }
}
