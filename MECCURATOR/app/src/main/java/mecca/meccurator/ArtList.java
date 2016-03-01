package mecca.meccurator;

import java.util.ArrayList;

/**
 * Created by chaitali on 16-02-27.
 */
public class ArtList {

    //TODO: singleton
    public static ArrayList<Art> artwork;

    private int size;

    public void addItem(Art art) {

        if(artwork == null){
            artwork = new ArrayList<Art>();
        }

        artwork.add(art);
    }

    public void hasItem(Art art) {
        artwork.contains(art);
    }


    public void updateItem() {

    }

    public void deleteItem(Art art) {
        artwork.remove(art);
        
    }

    public ArrayList<Art> getArtwork(){
        return artwork;
    }

    public void remove(int pos) {
        artwork.remove(pos);
    }
}
