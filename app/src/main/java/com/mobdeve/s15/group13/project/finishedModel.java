package com.mobdeve.s15.group13.project;

/*
Class: finishedModel
Description: This class is the model class for the data to be passed on to the recycler view for the finished activity.
 */

public class finishedModel {
    private String poster;
    private String title, genre, rating;
    private String persorating, notes;
    private String synopsis, episode, status;


    public finishedModel(String poster, String title, String genre, String rating, String persorating, String notes, String synopsis, String episode, String status){
        this.poster = poster;
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.persorating = persorating;
        this.notes = notes;
        this.synopsis = synopsis;
        this.episode = episode;
        this.status = status;
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

    public String getPersorating(){
        return persorating;
    }

    public String getNotes() { return notes; }

    public String getSynopsis() { return synopsis; }

    public String getEpisode() { return episode; }

    public String getStatus() { return status; }
}
