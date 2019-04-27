package com.MONT3.partypoolapp;

public class Song {
    private long id;
    private String title;
    private String artist;
    private Boolean added;


    public Song(long songID, String songTitle, String songArtist) {
        id = songID;
        title = songTitle;
        artist = songArtist;
        added = false;
    }

    public long getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public Boolean getIsAdded(){
        return added;
    }

    public void setIsAdded(Boolean b)
    {
        added=b;
    }
}
