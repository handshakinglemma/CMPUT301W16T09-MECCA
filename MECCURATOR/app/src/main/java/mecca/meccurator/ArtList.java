package mecca.meccurator;

import java.util.ArrayList;

/**
 * ArtList object keeps track of all art listings entered into the system
 */
public class ArtList {

    //TODO: singleton
    public static ArrayList<Art> allArt = new ArrayList<Art>();


    private ArrayList<Art> AllArt = new ArrayList<Art>();

    private int size;

    public void addItem(Art artwork) {

        if(AllArt == null){

            ArtList AllArt = new ArtList();

        }
        AllArt.add(artwork);
    }

    public Art getItem(int index){
        return AllArt.get(index);
    }

    public boolean hasItem(Art art) {
        return AllArt.contains(art);
    }

    public void deleteItem(Art art) {
        AllArt.remove(art);
        
    }

    public ArrayList<Art> getArtwork(){
        return AllArt;
    }

    public void remove(int pos) {
        AllArt.remove(pos);
    }
}
