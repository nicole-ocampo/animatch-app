package com.mobdeve.s15.group13.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

/*
Class: accountActivity
Description: This class implements the account_activity.xml layout. This class presents the account information
as well as options to proceed editing the logged user account, or to log out of the application.
 */

public class accountActivity extends AppCompatActivity {

    private Button editAccountBtn;
    private Button logoutBtn;

    private TextView nameTv, emailTv, usernameTv;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private final String FILE_NAME = "SettingsActivity";
    private String username, userId, name, email;
    private boolean isLoggedOut = false;

    private ImageButton homeBtn;
    private ImageButton matchesBtn;
    private ImageButton finishedBtn;
    private ImageButton accountBtn;

    private TextView homeLabel;
    private TextView matchesLabel;
    private TextView finishedLabel;
    private TextView accountLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);

        this.nameTv = findViewById(R.id.nameTv);
        this.emailTv = findViewById(R.id.emailTv);
        this.usernameTv = findViewById(R.id.usernameTv);

        this.sp = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        this.userId = this.sp.getString("LOGGED_USER_ID", null);
        this.editor = this.sp.edit();

        this.editAccountBtn = findViewById(R.id.editAccBtn);
        this.logoutBtn = findViewById(R.id.logOutBtn);

        this.homeBtn = findViewById(R.id.yourAccount_HomeIb);
        this.matchesBtn = findViewById(R.id.yourAccount_MatchesIb);
        this.finishedBtn = findViewById(R.id.yourAccount_FinishedIb);
        this.accountBtn = findViewById(R.id.yourAccount_AccountIb);

        this.homeLabel = findViewById(R.id.matchesv1_HomeTv);
        this.matchesLabel = findViewById(R.id.matchesv1_MatchesTv);
        this.finishedLabel = findViewById(R.id.matchesv1_FinishedTv);
        this.accountLabel = findViewById(R.id.matchesv1_AccountTv);

        // Instantiate navigation bar buttons
        initialiseNavbar();

        // Initialise content
        // Gets Logged User Details for display
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
                        showDetails(querySnapshot.getDocuments().get(0));
                    }
                }
                else
                    Log.d("TAG", "Error getting documents: ", task.getException());
            }
        });

        // Setting edit account button
        editAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(accountActivity.this, editAccountActivity.class);
                startActivity(i);
            }
        });

        // Setting log out button
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert = new AlertDialog.Builder(accountActivity.this)
                        .setIcon(getResources().getDrawable(R.drawable.ic_baseline_warning_24))
                        .setTitle("Log out")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes, I'm sure.", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete from shared preferences logged user
                                isLoggedOut = true;
                                Log.d("TAG", "Logging out...");

                                // logout
                                Intent i = new Intent(accountActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();

                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("Cancel", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

    }

    private void initialiseNavbar(){
        homeLabel.setTextColor(Color.parseColor("#A6A6A6"));
        matchesLabel.setTextColor(Color.parseColor("#A6A6A6"));
        finishedLabel.setTextColor(Color.parseColor("#A6A6A6"));
        accountLabel.setTextColor(Color.parseColor("#3F9EDF"));
        accountBtn.setColorFilter(Color.parseColor("#3F9EDF"));

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(accountActivity.this, matchingActivity.class);
                startActivity(i);
                finish();

            }
        });

        matchesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(accountActivity.this, matchesActivity.class);
                startActivity(i);
                finish();

            }
        });

        finishedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(accountActivity.this, finishedActivity.class);
                startActivity(i);
                finish();
            }
        });

        accountBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(
                        accountActivity.this,
                        "You're already here.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }


    private void showDetails(DocumentSnapshot user){
        userModel u = user.toObject(userModel.class);
        username = u.getUsername();
        name = u.getName();
        email = u.getEmail();

        Log.d("TAG", "name: " + name);
        Log.d("TAG", "email: " + email);
        Log.d("TAG", "username: " + username);

        nameTv.setText(name);
        usernameTv.setText("@" + username);
        emailTv.setText(email);
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (this.isLoggedOut == true) {
            this.editor.putString("LOGGED_USER", null);
            this.editor.apply();

            Log.d("TAG", "onPause: data written, user is now logged out.");
            Log.d("TAG", "Logged user:\n" + null);
        }

    }
}
