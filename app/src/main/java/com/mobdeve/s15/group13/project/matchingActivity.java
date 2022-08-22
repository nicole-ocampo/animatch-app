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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.daprlabs.cardstack.SwipeDeck;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


/*
Class: matchingActivity
Description: This class implements the matching_view.xml layout. This class presents the swipe deck that
allows the user to start swiping to find a match.
 */

public class matchingActivity extends AppCompatActivity {

    private SwipeDeck cardStack;
    private ArrayList<ShortDetailsModel> data;

    private ImageButton homeBtn;
    private ImageButton matchesBtn;
    private ImageButton finishedBtn;
    private ImageButton accountBtn;

    private TextView homeLabel;
    private TextView matchesLabel;
    private TextView finishedLabel;
    private TextView accountLabel;

    private final String FILE_NAME = "SettingsActivity";
    private SharedPreferences sp;
    private String username;
    private String userId;
    private boolean isLoggedOut = false;

    private TextView header;
    private int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_view);

        this.sp = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        this.username = this.sp.getString("LOGGED_USER", null);
        this.userId = this.sp.getString("LOGGED_USER_ID", null);

        this.header = findViewById(R.id.matchesv1HeadingTv);
        header.setText("Home");
        this.homeBtn = findViewById(R.id.yourAccount_HomeIb);
        this.matchesBtn = findViewById(R.id.yourAccount_MatchesIb);
        this.finishedBtn = findViewById(R.id.yourAccount_FinishedIb);
        this.accountBtn = findViewById(R.id.yourAccount_AccountIb);

        this.homeLabel = findViewById(R.id.matchesv1_HomeTv);
        this.matchesLabel = findViewById(R.id.matchesv1_MatchesTv);
        this.finishedLabel = findViewById(R.id.matchesv1_FinishedTv);
        this.accountLabel = findViewById(R.id.matchesv1_AccountTv);

        initialiseButtons();

        // Step 1: Prepare Swipe Deck
        data = new ArrayList<>();
        cardStack = findViewById(R.id.swipe_deck);
        final DeckAdapter adapter = new DeckAdapter(data, this);
        cardStack.setAdapter(adapter);

        // Step 2: Initialise Deck according to age and rating
        getAge();
        replenishdeck(adapter);

        // Step 3: Assign functionalities to each swipe activity
        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                Toast.makeText(matchingActivity.this, "Anime removed from your suggestions!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cardSwipedRight(int position) {
                Toast.makeText(matchingActivity.this, "Anime added to your watchlist!", Toast.LENGTH_SHORT).show();

                Log.d("TAG", "pos: "+position);
                int animeUid = adapter.getUid(position);
                Log.d("TAG", "anime uid: "+animeUid);

                CollectionReference matchesRef= firebaseHelper.getMatchesCollectionReference();

                DocumentReference userIdMatchlist = matchesRef.document(userId);
                userIdMatchlist.update("matchlist", FieldValue.arrayUnion(animeUid));
            }

            @Override
            public void cardsDepleted() {
                replenishdeck(adapter);
            }

            @Override
            public void cardActionDown() {
                Log.i("TAG", "CARDS MOVED DOWN");
            }

            @Override
            public void cardActionUp() {
                Log.i("TAG", "CARDS MOVED UP");
            }
        });

    }

    // This function gets the age of the logged user.
    private void getAge(){
        int year = Calendar.getInstance().get(Calendar.YEAR);
        CollectionReference usersRef= firebaseHelper.getUserCollectionReference();
        Query query = usersRef.whereEqualTo("userId", userId);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (task.getResult().isEmpty()) {
                        Log.d("TAG", "No user found");
                    } else {
                        Log.d("TAG", querySnapshot.getDocuments().get(0).getId());
                        userModel u = querySnapshot.getDocuments().get(0).toObject(userModel.class);

                        String birthyear = u.getBirthYear();
                        int byNum = Integer.parseInt(birthyear);
                        age = year - byNum;

                        Log.d("Current Age: ", String.valueOf(age));
                    }
                }
            }
        });
    }


    // This function prepares the navigation bar.
    private void initialiseButtons(){
        homeLabel.setTextColor(Color.parseColor("#3F9EDF"));
        matchesLabel.setTextColor(Color.parseColor("#A6A6A6"));
        finishedLabel.setTextColor(Color.parseColor("#A6A6A6"));
        accountLabel.setTextColor(Color.parseColor("#A6A6A6"));
        homeBtn.setColorFilter(Color.parseColor("#3F9EDF"));

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                        matchingActivity.this,
                        "You're already here.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(matchingActivity.this, accountActivity.class);
                startActivity(i);
                finish();

            }
        });

        matchesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(matchingActivity.this, matchesActivity.class);
                startActivity(i);
                finish();

            }
        });

        finishedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(matchingActivity.this, finishedActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    // This function is called if the user is 18+
    private void replenishdeck(DeckAdapter adapter){
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        CollectionReference animeRef= db.collection("anime");

        ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Loading your swipe deck...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();

        db.collection("matches").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                List<String> matchlist = (List<String>) document.get("matchlist");

                animeRef.limit(7000).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            List<ShortDetailsModel> shortDetailsModelList = new ArrayList<>();
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot d : list) {
                                ShortDetailsModel u = d.toObject(ShortDetailsModel.class);

                                // If the user is a minor, filter ratings
                                if (age < 18) {
                                    //Log.d("TAG", "User is a minor, filtering ratings...");
                                    String animeRating = u.getRating();
                                    if (!(animeRating != null && animeRating.contains("R")))
                                        shortDetailsModelList.add(u);
                                } else
                                    shortDetailsModelList.add(u);

                            }

                            int size = shortDetailsModelList.size();
                            ArrayList<ShortDetailsModel> randomAnimeList = new ArrayList<>();

                            // Randomizes the deck
                            for (int i = 0; i < size; i++) {
                                int randomNumber = new Random().nextInt(size);

                                ShortDetailsModel model = shortDetailsModelList.get(randomNumber);

                                if (!randomAnimeList.contains(model)) {
                                    randomAnimeList.add(model);
                                    data.add(model);
                                    String title = model.getTitle();
                                    adapter.notifyDataSetChanged();
                                }
                                if (randomAnimeList.size() == 25)
                                    break;

                            }
                            dialog.dismiss();
                            int size2 = randomAnimeList.size();
                            Log.i("TAG", "shortdetailsmodellist size: " + size2);
                        }

                    }
                });
            }
        });
    }
}

