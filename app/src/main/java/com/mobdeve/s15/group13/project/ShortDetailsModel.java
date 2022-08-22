package com.mobdeve.s15.group13.project;

/*
Class: ShortDetailsModel
Description: This class is the model class for the collection, anime. The constructor
is set to blank as advised in the firebase documentation.
 */

public class ShortDetailsModel {

    private int uid;
    private String title;
    private String rating;
    private String genre;
    private String synopsis;
    private Long episodes;
    private String status;
    private String img_url;

    public ShortDetailsModel(){ }
    public int getUid() {
        return uid;
    }
    public String getTitle() {
        return title;
    }
    public String getRating() {
        return rating;
    }
    public String getGenre() {
        return genre;
    }
    public String getSynopsis() {
        return synopsis;
    }
    public Long getEpisodes() {
        return episodes;
    }
    public String getStatus() {
        return status;
    }
    public String getImg_url() {
        return img_url;
    }
}
