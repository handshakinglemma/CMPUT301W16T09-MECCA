package mecca.meccurator;

import java.util.ArrayList;

/**
 * Created by chaitali on 16-02-27.
 */
public class BidList {

    private ArrayList<Bid> allBids = new ArrayList<Bid>();



    public void addBid(Bid bid){
        allBids.add(bid);
    }

    public boolean hasBid(Bid bid){
        return allBids.contains(bid);
    }

    public void declineBid(int index){
        allBids.remove(index);

    }

    public void declineAllBids(){
        allBids.removeAll(allBids);

    }

    public Bid getBid(int index){
        return allBids.get(index);
    }

    public ArrayList<Bid> getAllBids(){
        return allBids;
    }

    public int getBidCount(){
        return allBids.size();
    }


}
