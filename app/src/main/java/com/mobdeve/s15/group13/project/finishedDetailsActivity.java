package com.mobdeve.s15.group13.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

/*
Class: finishedDetailsActivity
Description: This class implements the activity_view_finished.xml layout. This class presents additional information
about the anime show the user has finished. This also includes the notes the user has written about the show.
 */

public class finishedDetailsActivity extends AppCompatActivity {
    private String poster, title, rating, genre;
    private String synopsis, episode, status;
    private String personalrating, notes;

    private ImageView posterIv;
    private TextView titleTv, persoratingTv, ratingTv, genreTv;
    private TextView synopsisTv, episodeTv, statusTv;
    private TextView notesTv;

    private final String FILE_NAME = "SettingsActivity";
    private SharedPreferences sp;
    private String username;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_finished);

        this.sp = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        this.username = this.sp.getString("LOGGED_USER", null);
        this.userId = this.sp.getString("LOGGED_USER_ID", null);

        this.posterIv = findViewById(R.id.finished_animeImageIv);
        this.titleTv = findViewById(R.id.finished_titleTv);
        this.persoratingTv = findViewById(R.id.finished_showPersoratingTv);
        this.ratingTv = findViewById(R.id.finished_ShowRatingTv);
        this.genreTv = findViewById(R.id.finished_ShowGenreTv);
        this.synopsisTv = findViewById(R.id.finished_synopsistv);
        this.episodeTv = findViewById(R.id.finished_EpisodesNumberTv);
        this.statusTv = findViewById(R.id.finished_StatusDetailTv);
        this.notesTv = findViewById(R.id.actualNotesTv);

        Intent i = getIntent();
        this.poster = i.getStringExtra("FINISHED_POSTER");
        this.title = i.getStringExtra("FINISHED_TITLE");
        this.personalrating = i.getStringExtra("FINISHED_PERSONALRATING");
        this.rating = i.getStringExtra("FINISHED_RATING");
        this.genre = i.getStringExtra("FINISHED_GENRE");
        this.notes = i.getStringExtra("FINISHED_NOTES");
        this.synopsis = i.getStringExtra("FINISHED_SYNOPSIS");
        this.episode = i.getStringExtra("FINISHED_EPISODE");
        this.status = i.getStringExtra("FINISHED_STATUS");

        this.titleTv.setText(title);
        this.persoratingTv.setText(personalrating);
        this.ratingTv.setText(rating);
        this.genreTv.setText(genre);
        this.synopsisTv.setText(synopsis);
        this.genreTv.setText(genre);
        this.episodeTv.setText(episode);
        this.statusTv.setText(status);
        this.notesTv.setText(notes);

        Glide.with(this)
                .load(poster)
                .into(posterIv);


    }

}
