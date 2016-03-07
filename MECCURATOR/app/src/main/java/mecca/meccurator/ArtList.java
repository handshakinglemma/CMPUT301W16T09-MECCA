package mecca.meccurator;

import java.util.ArrayList;

/**
 * Created by chaitali on 16-02-27.
 * 
 */
public class ArtList {

    //TODO: singleton
    public static ArrayList<Art> allArt = new ArrayList<Art>();

    private int size;

    public void addItem(Art artwork) {

        allArt.add(artwork);
    }

    public boolean hasItem(Art art) {
        return allArt.contains(art);
    }


    public void updateItem() {

    }

    public void deleteItem(Art art) {
        allArt.remove(art);
        
    }

    public Art getItem(int index){
        return allArt.get(index);
    }


    public void remove(int pos) {
        allArt.remove(pos);
    }
}
