package com.MONT3.partypoolapp;

import java.util.ArrayList;
import java.util.List;

public class Song {


    private String songName;
    private String songArtist;
    private String songID;
    private String filePath;

    List songData = new ArrayList();

    public String getSongName() {
        return songName;
    }
    public String getSongArtist() {
        return songArtist;
    }
    public String getSongID() {
        return songID;
    }
    public String getFilePath() {
        return filePath;
    }
}
