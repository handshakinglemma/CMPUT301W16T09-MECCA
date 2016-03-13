package mecca.meccurator;

import java.math.BigDecimal;

/**
 * Each bid is associated with a bid list and this bid list is associated with one art item
 */
public class Bid {

    protected String bidder;
    protected float rate;


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
}
