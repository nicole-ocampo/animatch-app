package com.mobdeve.s15.group13.project;

/*
Class: matchesModel
Description: This class is the model class for the data to be passed on to the recycler view for the matches activity.
 */

public class matchesModel {
    private String poster;
    private String title, genre, rating;
    private String synopsis, episode, status;
    private int uid;

    public matchesModel(String poster, String title, String genre, String rating, String synopsis, String episode, String status, int uid){
        this.poster = poster;
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.synopsis = synopsis;
        this.episode = episode;
        this.status = status;
        this.uid = uid;
    }

    public String getPoster() {
        return poster;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getRating() {
        return rating;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getEpisode() {
        return episode;
    }

    public String getStatus() {
        return status;
    }

    public int getUid() {
        return uid;
    }
}
