package com.mobdeve.s15.group13.project;

/*
Class: introModel
Description: This class is the model class for the screens displayed in the main activity.
 */

public class introModel {
    private int img;
    private String header, details;

    public introModel(String header, String details, int img){
        this.header = header;
        this.details = details;
        this.img = img;
    }

    public String getHeader() { return header; }
    public String getDetails() { return details; }
    public int getImg() { return img; }
}
