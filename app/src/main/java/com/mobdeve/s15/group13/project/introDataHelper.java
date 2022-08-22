package com.mobdeve.s15.group13.project;

import java.util.ArrayList;

/*
Class: introDataHelper
Description: This class populates the text to be displayed in the main activity.
 */

public class introDataHelper {

    public static ArrayList<introModel> load_data(){
        ArrayList<introModel> data = new ArrayList<>();

        data.add(new introModel( "Animatch",
                "Looking for your next anime? AniMatch will help you find one more show to watch, all tailored for you.",
                R.drawable.st_bg
        ));

        data.add(new introModel ("Easy to use!",
                "Found an interesting anime? Swipe right to add it to your watchlist. Don’t want it? Swipe left. If you want to learn more about our suggestion, tap it once.",
                R.drawable.st_bgb
        ));

        data.add(new introModel( "Start matching now!",
                "You’re one swipe away from finding your new favourite!",
                R.drawable.st_bgc

        ));

        return data;
    }
}
