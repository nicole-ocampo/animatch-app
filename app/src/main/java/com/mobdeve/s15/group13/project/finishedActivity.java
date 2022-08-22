package com.mobdeve.s15.group13.project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/*
Class: finishedActivity
Description: This class implements the activity_matches.xml layout, with the content implementing the matching_v2_layout.
This class presents the list of shows the user has finished watching.
 */

public class finishedActivity extends AppCompatActivity {

    private ImageButton homeBtn, matchesBtn, finishedBtn, accountBtn;
    private TextView toSetTv;
    private RecyclerView matchesRv;

    private TextView header;
    private final String FILE_NAME = "SettingsActivity";
    private SharedPreferences sp;
    private String username, userId;


    private ArrayList<finishedModel> finishedToRender = new ArrayList<>();
    private finishedAdapter adapter = new finishedAdapter(finishedToRender);

    private TextView homeLabel;
    private TextView matchesLabel;
    private TextView finishedLabel;
    private TextView accountLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        this.header = findViewById(R.id.matchesv1HeadingTv);
        header.setText("Finished Shows");
        this.toSetTv = findViewById(R.id.matchesToSetTextTv);
        this.toSetTv.setVisibility(View.GONE);

        this.sp = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        this.username = this.sp.getString("LOGGED_USER", null);
        this.userId = this.sp.getString("LOGGED_USER_ID", null);

        this.homeBtn = findViewById(R.id.matchlist_HomeIb);
        this.matchesBtn = findViewById(R.id.matchlist_MatchesIb);
        this.finishedBtn = findViewById(R.id.matchlist_FinishedIb);
        this.accountBtn = findViewById(R.id.matchlist_AccountIb);

        this.homeLabel = findViewById(R.id.matchesv1_HomeTv);
        this.matchesLabel = findViewById(R.id.matchesv1_MatchesTv);
        this.finishedLabel = findViewById(R.id.matchesv1_FinishedTv);
        this.accountLabel = findViewById(R.id.matchesv1_AccountTv);

        this.matchesRv = findViewById(R.id.matchesRv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        matchesRv.setLayoutManager(linearLayoutManager);

        initialiseNavbar();
        refresh();

    }


    private void initialiseNavbar(){
        homeLabel.setTextColor(Color.parseColor("#A6A6A6"));
        matchesLabel.setTextColor(Color.parseColor("#A6A6A6"));
        finishedLabel.setTextColor(Color.parseColor("#3F9EDF"));
        accountLabel.setTextColor(Color.parseColor("#A6A6A6"));
        finishedBtn.setColorFilter(Color.parseColor("#3F9EDF"));

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(finishedActivity.this, matchingActivity.class);
                startActivity(i);
                finish();
            }
        });

        matchesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(finishedActivity.this, matchesActivity.class);
                startActivity(i);
                finish();
            }
        });

        finishedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Toast.makeText(
                        finishedActivity.this,
                        "You're already here.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        accountBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(finishedActivity.this, accountActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    // This method prepares the recycler view and populates it with the shows the user has finished.
    protected void refresh(){
        CollectionReference finishedRef = firebaseHelper.getFinishedCollectionReference();
        Query query = finishedRef.whereEqualTo("userId", userId);
        finishedToRender.clear();

        ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Loading your finished list...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();

        // Step 1: GET FINISHED WATCHED LIST
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<finishedListModel> p = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult())
                        p.add(document.toObject(finishedListModel.class));

                    ArrayList<HashMap<String, String>> temp = p.get(0).getFinishedlist();
                    CollectionReference animeRef = firebaseHelper.getAnimeCollectionReference();

                    // If the user has not finished any shows yet, set text to inform the user.
                    if (temp.size() == 0){
                        toSetTv.setVisibility(View.VISIBLE);
                        toSetTv.setText("You have not finished any shows...yet.");
                        dialog.dismiss();
                    } else {
                        // Step 2: For every anime watched, get the details and map it into finished model.
                        for (int i = 0; i < temp.size(); i++) {
                            String uid = temp.get(i).get("uid");
                            String persorating = temp.get(i).get("rating");
                            String notes = temp.get(i).get("notes");

                            Log.d("TAG", "uid rendering: "+ uid);
                            Query query_anime = animeRef.whereEqualTo("uid", Integer.valueOf(uid));
                            query_anime.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            ShortDetailsModel sd = document.toObject(ShortDetailsModel.class);

                                            String sdposter;
                                            String sdtitle, sdrating, sdgenre, sdnotes;
                                            String sdsynopsis, sdepisode, sdstatus;

                                            sdtitle = sd.getTitle();
                                            sdrating = sd.getRating();
                                            sdgenre = sd.getGenre();
                                            sdposter = sd.getImg_url();
                                            sdsynopsis = sd.getSynopsis();
                                            sdepisode = Long.toString(sd.getEpisodes());
                                            sdstatus = sd.getStatus();

                                            finishedModel finishinstance = new finishedModel(sdposter, sdtitle, sdgenre, sdrating, persorating, notes, sdsynopsis, sdepisode, sdstatus);
                                            finishedToRender.add(finishinstance);
                                        }
                                        matchesRv.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                        dialog.dismiss();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }


}
