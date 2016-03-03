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

        if(allArt == null){

            ArtList allArt = new ArtList();

        }
        allArt.add(artwork);
    }

    public void hasItem(Art art) {
        allArt.contains(art);
    }


    public void updateItem() {

    }

    public void deleteItem(Art art) {
        allArt.remove(art);
        
    }

    public ArrayList<Art> getArtwork(){
        return allArt;
    }

    public void remove(int pos) {
        allArt.remove(pos);
    }
}
