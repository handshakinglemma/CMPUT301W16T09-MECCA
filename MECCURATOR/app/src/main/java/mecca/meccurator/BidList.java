package mecca.meccurator;

import java.util.ArrayList;

/**
 * A BidList object keeps track of all bids placed on a single art item.
 */
public class BidList {

    private ArrayList<Bid> allBids = new ArrayList<Bid>();



    public void addBid(Bid bid){

        if(allBids == null){
            allBids = new ArrayList<>();
        }
        allBids.add(bid);
    }

    /* check if a BidList contains a certain bid */
    public boolean hasBid(Bid bid){
        return allBids.contains(bid);
    }

    /* lets a user decline a bid on their item */
    public void declineBid(int index){
        allBids.remove(index);
    }

    /* lets a user decline ALL bids on their item */
    public void declineAllBids(){
        allBids.removeAll(allBids);
    }

    /* get a specific bid */
    public Bid getBid(int index){
        return allBids.get(index);
    }

    /* get all bids */
    public ArrayList<Bid> getAllBids(){
        return allBids;
    }

    /* return the number of bids in the BidList */
    public int getBidCount(){
        return allBids.size();
    }
}
