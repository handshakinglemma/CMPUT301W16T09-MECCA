package mecca.meccurator;

import java.math.BigDecimal;

/**
 * Created by chaitali on 16-02-27.
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
