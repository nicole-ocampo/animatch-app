package com.mobdeve.s15.group13.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
Class: ratingsActivity
Description: This class implements the account_activity.xml layout. This class presents the account information
as well as options to proceed editing the logged user account, or to log out of the application.
 */

public class ratingsActivity extends AppCompatActivity {

    private EditText notes;
    private float ratingvalue;
    private final String TAG="ratings";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private String passedNotes;
    private final String FILE_NAME = "SettingsActivity";

    private Button submitRating, noRating;
    private RatingBar rbRatingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        String username, userId;
        int uid;

        username = getIntent().getStringExtra("USERNAME_KEY");
        userId = getIntent().getStringExtra("USERID_KEY");
        uid = getIntent().getIntExtra("UID_KEY", -1);

        Log.d(TAG, "username: " + username);
        Log.d(TAG, "anime uid: "+ uid);


        this.sp = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        this.editor = this.sp.edit();

        this.submitRating = findViewById(R.id.submitRatingBtn);
        this.noRating = findViewById(R.id.noRatingBtn);
        this.rbRatingBar = findViewById(R.id.ratingBar);
        this.notes = findViewById(R.id.ratings_notesEt);


        FirebaseFirestore dbRef;
        dbRef = FirebaseFirestore.getInstance();
        CollectionReference finishedRef= dbRef.collection("finished");
        DocumentReference userIdFinishedList = finishedRef.document(userId);
        CollectionReference matchesRef= dbRef.collection("matches");
        Query query = matchesRef.whereEqualTo("userId", userId);

        ratingvalue = (Float) rbRatingBar.getRating();


        rbRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingvalue = (Float) rbRatingBar.getRating();
                Float ratingVal = (Float) rating;
                Log.d(TAG, "rating: " + ratingvalue);
            }
        });

        submitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username;
                int uid;

                passedNotes = notes.getText().toString();

                username = getIntent().getStringExtra("USERNAME_KEY");
                uid = getIntent().getIntExtra("UID_KEY", -1);

                Map<String, String> finishedList = new HashMap<>();
                finishedList.put("rating", Float.toString(ratingvalue));
                finishedList.put("uid", Integer.toString(uid));

                if (passedNotes.length() == 0){
                    finishedList.put("notes", "no notes");
                } else{
                    finishedList.put("notes", passedNotes);
                }

                // Add to finished shows
                userIdFinishedList.update("finishedlist", FieldValue.arrayUnion(finishedList));

                // Remove from watchlist
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<matchListModel> p = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult())
                                p.add(document.toObject(matchListModel.class));

                            // Get the matchlist
                            ArrayList<Integer> temp = p.get(0).getMatchlist();

                            // Remove anime from matchlist
                            temp.remove(Integer.valueOf(uid));
                            Map<String, Object> data1 = new HashMap<>();
                            data1.put("userId", userId);
                            data1.put("matchlist", temp);

                            // Update the document
                            matchesRef.document(userId)
                                    .update(data1)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("TAG", "DocumentSnapshot successfully written!");

                                            Intent i = new Intent(ratingsActivity.this, finishedActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("TAG", "Error writing document", e);
                                        }
                                    });
                        }
                    }
                });

            }

        });

        noRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username;
                int uid;

                passedNotes = notes.getText().toString();

                username = getIntent().getStringExtra("USERNAME_KEY");
                uid = getIntent().getIntExtra("UID_KEY", -1);
                // Write to DB

                Map<String, String> finishedList = new HashMap<>();
                finishedList.put("rating", "none");
                finishedList.put("uid", Integer.toString(uid));

                if (passedNotes.length() == 0){
                    finishedList.put("notes", "no notes");
                } else{
                    finishedList.put("notes", passedNotes);
                }

                // Add to finished shows
                userIdFinishedList.update("finishedlist", FieldValue.arrayUnion(finishedList));

                // Remove from watchlist
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<matchListModel> p = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult())
                                p.add(document.toObject(matchListModel.class));

                            // get the matchlist arraylist
                            ArrayList<Integer> temp = p.get(0).getMatchlist();

                            // remove uid
                            temp.remove(Integer.valueOf(uid));
                            Map<String, Object> data1 = new HashMap<>();
                            data1.put("userId", userId);
                            data1.put("matchlist", temp);

                            // udpate
                            matchesRef.document(userId)
                                    .update(data1)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("TAG", "DocumentSnapshot successfully written!");
                                            Intent i = new Intent(ratingsActivity.this, finishedActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("TAG", "Error writing document", e);
                                        }
                                    });
                        }
                    }
                });
            }
        });
    }

}
