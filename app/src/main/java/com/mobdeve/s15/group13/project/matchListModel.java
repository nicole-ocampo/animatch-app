package com.mobdeve.s15.group13.project;

import java.util.ArrayList;

/*
Class: matchListModel
Description: This class is the model class for the collection, matches. The constructor
is set to blank as advised in the firebase documentation.
 */

public class matchListModel {

    private String userId;
    private ArrayList<Integer> matchlist;

    public matchListModel(){ }
    public String getUserId() {
        return userId;
    }
    public ArrayList<Integer> getMatchlist() {
        return matchlist;
    }

}
