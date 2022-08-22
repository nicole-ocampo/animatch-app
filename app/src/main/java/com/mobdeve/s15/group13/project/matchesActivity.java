package com.mobdeve.s15.group13.project;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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

/*
Class: matchesActivity
Description: This class implements the activity_matches.xml layout. This class presents the current watchlist or
matches of the logged user.
 */


public class matchesActivity extends AppCompatActivity {

    private ImageButton homeBtn, matchesBtn, finishedBtn, accountBtn;
    private RecyclerView matchesRv;

    private final String FILE_NAME = "SettingsActivity";
    private SharedPreferences sp;
    private String username;
    private String userId;

    private TextView header;
    private TextView toSetTv;

    private TextView homeLabel;
    private TextView matchesLabel;
    private TextView finishedLabel;
    private TextView accountLabel;


    // Refresh the recycler view if and only if the user deleted a show
    // Clicking finished show leads to a new activity (finishedActivity)
    // Pressing back should not refresh the recycler view
    private ActivityResultLauncher<Intent> matchesLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        refresh();
                    }
                }
            }
    );

    private ArrayList<matchesModel> matchesToRender = new ArrayList<>();
    private matchesAdapter adapter = new matchesAdapter(matchesToRender, matchesLauncher);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        this.homeBtn = findViewById(R.id.matchlist_HomeIb);
        this.matchesBtn = findViewById(R.id.matchlist_MatchesIb);
        this.finishedBtn = findViewById(R.id.matchlist_FinishedIb);
        this.accountBtn = findViewById(R.id.matchlist_AccountIb);
        this.matchesRv = findViewById(R.id.matchesRv);

        this.homeLabel = findViewById(R.id.matchesv1_HomeTv);
        this.matchesLabel = findViewById(R.id.matchesv1_MatchesTv);
        this.finishedLabel = findViewById(R.id.matchesv1_FinishedTv);
        this.accountLabel = findViewById(R.id.matchesv1_AccountTv);

        this.sp = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        this.username = this.sp.getString("LOGGED_USER", null);
        this.userId = this.sp.getString("LOGGED_USER_ID", null);

        Log.d("Matches Activity", "logged user id: " + userId);

        this.header = findViewById(R.id.matchesv1HeadingTv);
        header.setText("Watchlist");
        this.toSetTv = findViewById(R.id.matchesToSetTextTv);
        this.toSetTv.setVisibility(View.GONE);

        initialiseNavbar();
        refresh();

    }

    private void initialiseNavbar(){
        homeLabel.setTextColor(Color.parseColor("#A6A6A6"));
        matchesLabel.setTextColor(Color.parseColor("#3F9EDF"));
        finishedLabel.setTextColor(Color.parseColor("#A6A6A6"));
        accountLabel.setTextColor(Color.parseColor("#A6A6A6"));
        matchesBtn.setColorFilter(Color.parseColor("#3F9EDF"));

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(matchesActivity.this, matchingActivity.class);
                startActivity(i);
                finish();

            }
        });

        matchesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Toast.makeText(
                        matchesActivity.this,
                        "You're already here.",
                        Toast.LENGTH_SHORT
                ).show();
            }

        });

        finishedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(matchesActivity.this, finishedActivity.class);
                startActivity(i);
                finish();
            }
        });

        accountBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(matchesActivity.this, accountActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    // This method prepares the recycler view and populates it with the shows the user has matched with.
    protected void refresh(){
        CollectionReference matchesRef= firebaseHelper.getMatchesCollectionReference();

        Query query = matchesRef.whereEqualTo("userId", userId);
        matchesToRender.clear();

        ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Loading your watchlist...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();

        // Step 1: GET MATCH LIST
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<matchListModel> p = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult())
                        p.add(document.toObject(matchListModel.class));


                    ArrayList<Integer> temp = p.get(0).getMatchlist();
                    CollectionReference animeRef = firebaseHelper.getAnimeCollectionReference();
                    if (temp.size() == 0){
                        toSetTv.setVisibility(View.VISIBLE);
                        toSetTv.setText("Your matchlist is empty. Start swiping to find a match!");
                        dialog.dismiss();
                    } else {
                        // Step 2: Get more information about each anime in the watchlist
                        for (int i = 0; i < temp.size(); i++) {
                            Query query_anime = animeRef.whereEqualTo("uid", temp.get(i));
                            query_anime.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            ShortDetailsModel sd = document.toObject(ShortDetailsModel.class);

                                            String sdposter;
                                            String sdtitle, sdrating, sdgenre;
                                            String sdsynopsis, sdepisode, sdstatus;
                                            int sduid;

                                            sdtitle = sd.getTitle();
                                            sdrating = sd.getRating();
                                            sdgenre = sd.getGenre();
                                            sdposter = sd.getImg_url();

                                            sdsynopsis = sd.getSynopsis();
                                            sdepisode = Long.toString(sd.getEpisodes());
                                            sdstatus = sd.getStatus();
                                            sduid = sd.getUid();

                                            matchesModel matchinstance = new matchesModel(sdposter, sdtitle, sdgenre, sdrating, sdsynopsis, sdepisode, sdstatus, sduid);
                                            matchesToRender.add(matchinstance);
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


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        matchesRv.setLayoutManager(linearLayoutManager);
    }


}
