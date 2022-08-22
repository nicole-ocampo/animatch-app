package com.mobdeve.s15.group13.project;

import java.util.ArrayList;
import java.util.HashMap;

/*
Class: finishedListModel
Description: This class is the model class for the document to be retrieved from the finished collection.
 */

public class finishedListModel {

    private String userId;
    private ArrayList<HashMap<String, String>> finishedlist;

    public finishedListModel(){ }
    public String getUserId() {
        return userId;
    }
    public ArrayList<HashMap<String, String>> getFinishedlist(){
        return finishedlist;
    }
}
