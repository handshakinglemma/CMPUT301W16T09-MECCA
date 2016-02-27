package mecca.meccurator;

import java.math.BigDecimal;

/**
 * Created by chaitali on 16-02-27.
 */
public class Bid {

    private BigDecimal amount;
    private String bidder;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getBidder() {
        return bidder;
    }

    public void setBidder(String bidder) {
        this.bidder = bidder;
    }
}
