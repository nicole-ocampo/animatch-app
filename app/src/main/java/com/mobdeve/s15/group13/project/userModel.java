package com.mobdeve.s15.group13.project;

/*
Class: userModel
Description: This class is the model class for the collection, user. The constructor
is set to blank as advised in the firebase documentation. A filled out constructor
is also prepared as an alternative.
 */

public class userModel {
    private String username;
    private String name;
    private String email;
    private String password;
    private String birthMonth;
    private String birthDate;
    private String birthYear;
    private String userId;

    public userModel() { }

    public userModel (String username, String name, String email, String password, String birthMonth, String birthDate, String birthYear, String userId){
        this.username = username;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthMonth = birthMonth;
        this.birthDate = birthDate;
        this.birthYear = birthYear;
    }

    public String getUsername() { return username; }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getBirthMonth() {
        return birthMonth;
    }
    public String getBirthDate() {
        return birthDate;
    }
    public String getBirthYear() {
        return birthYear;
    }
    public String getUserId(){ return userId;}
}
