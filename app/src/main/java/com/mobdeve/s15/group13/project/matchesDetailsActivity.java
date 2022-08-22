package com.mobdeve.s15.group13.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
Class: matchesDetailsActivity
Description: This class implements the activity_view_matches.xml layout. This class presents additional information
about the anime show the user has matched with.
 */

public class matchesDetailsActivity extends AppCompatActivity {
    private String poster, title, rating, genre;
    private String synopsis, episode, status;
    private int uid;

    private ImageView posterIv;
    private TextView titleTv, ratingTv, genreTv;
    private TextView synopsisTv, episodeTv, statusTv;

    private Button done, remove;

    private final String FILE_NAME = "SettingsActivity";
    private SharedPreferences sp;
    private String username;
    private String userId;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_matches);

        this.sp = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        this.username = this.sp.getString("LOGGED_USER", null);
        this.userId = this.sp.getString("LOGGED_USER_ID", null);

        Intent i = getIntent();
        this.poster = i.getStringExtra("MATCHES_POSTER");
        this.title = i.getStringExtra("MATCHES_TITLE");
        this.rating = i.getStringExtra("MATCHES_RATING");
        this.genre = i.getStringExtra("MATCHES_GENRE");

        this.synopsis = i.getStringExtra("MATCHES_SYNOPSIS");
        this.episode = i.getStringExtra("MATCHES_EPISODE");
        this.status = i.getStringExtra("MATCHES_STATUS");
        this.uid= i.getIntExtra("MATCHES_ID", 0);
        this.position= i.getIntExtra("VIEW_POS", 0);

        this.posterIv = findViewById(R.id.matchesv2_animeImageIv);
        this.titleTv = findViewById(R.id.matchesv2_titleTv);
        this.ratingTv = findViewById(R.id.matchesv2_ShowRatingTv);
        this.genreTv = findViewById(R.id.matchesv2_ShowGenreTv);
        this.synopsisTv = findViewById(R.id.matchesv2_synopsistv);
        this.episodeTv = findViewById(R.id.matchesv2_EpisodesNumberTv);
        this.statusTv = findViewById(R.id.matchesv2_StatusDetailTv);

        this.titleTv.setText(title);
        this.ratingTv.setText(rating);
        this.genreTv.setText(genre);
        this.synopsisTv.setText(synopsis);
        this.episodeTv.setText(episode);
        this.statusTv.setText(status);

        Glide.with(this)
                .load(poster)
                .into(posterIv);

        this.done = findViewById(R.id.doneBtn);
        this.remove = findViewById(R.id.removeBtn);

        initialiseButtons();
    }

    private void initialiseButtons(){
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ratingsIntent = new Intent(matchesDetailsActivity.this, ratingsActivity.class);

                ratingsIntent.putExtra("USERID_KEY", userId);
                ratingsIntent.putExtra("UID_KEY", uid);

                setResult(Activity.RESULT_OK, ratingsIntent);
                startActivity(ratingsIntent);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionReference matchesRef= firebaseHelper.getMatchesCollectionReference();
                Query query = matchesRef.whereEqualTo("userId", userId);

                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<matchListModel> p = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult())
                                p.add(document.toObject(matchListModel.class));

                            // Step 1: Get the matchlist
                            ArrayList<Integer> temp = p.get(0).getMatchlist();

                            // Step 2: Remove the UID of the anime show in the match list.
                            temp.remove(Integer.valueOf(uid));
                            Map<String, Object> data1 = new HashMap<>();
                            data1.put("userId", userId);
                            data1.put("matchlist", temp);

                            // Step 3: Update the document
                            matchesRef.document(userId)
                                    .update(data1)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("TAG", "DocumentSnapshot successfully written!");
                                            Toast.makeText(
                                                    matchesDetailsActivity.this,
                                                    "Anime successfully removed from your watchlist!",
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            Intent returnIntent = new Intent();
                                            returnIntent.putExtra("delete", true);
                                            returnIntent.putExtra("position", position);
                                            setResult(Activity.RESULT_OK, returnIntent);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(
                                                    matchesDetailsActivity.this,
                                                    "There was an error removing this from your matches. Please try again.",
                                                    Toast.LENGTH_LONG
                                            ).show();
                                            Log.w("TAG", "Error writing document", e);
                                        }
                                    });
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("back",true);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
