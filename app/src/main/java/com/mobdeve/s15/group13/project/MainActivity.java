package com.mobdeve.s15.group13.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

/*
Class: MainActivity
Description: This class implements the account_main.xml layout. This class is the landing page
when the app is initially installed, or when there is no user logged in.
 */


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView page1, page2, page3;
    private Button register, login;
    private final String TAG="Main: ";

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private final String FILE_NAME = "SettingsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Step 1: Populate the data for the introductory screens
        introAdapter adapter = new introAdapter(introDataHelper.load_data());

        this.recyclerView = findViewById(R.id.st_introRv);
        this.recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        this.recyclerView.setLayoutManager(linearLayoutManager);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        this.sp = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        // Step 2: Set up for the progress bar
        this.page1 = findViewById(R.id.page1Iv);
        this.page2 = findViewById(R.id.page2Iv);
        this.page3 = findViewById(R.id.page3iv);

        page1.setImageResource(R.drawable.ic_baseline_brightness_1_24);
        page2.setImageResource(R.drawable.ic_outline_brightness_1_24);
        page3.setImageResource(R.drawable.ic_outline_brightness_1_24);

        recyclerView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override public void onScrollChanged() {
                    int pos = linearLayoutManager.findFirstVisibleItemPosition();
                    if (pos == 0){
                        page1.setImageResource(R.drawable.ic_baseline_brightness_1_24);
                        page2.setImageResource(R.drawable.ic_outline_brightness_1_24);
                        page3.setImageResource(R.drawable.ic_outline_brightness_1_24);

                    } else if (pos ==1){
                        page2.setImageResource(R.drawable.ic_baseline_brightness_1_24);
                        page1.setImageResource(R.drawable.ic_outline_brightness_1_24);
                        page3.setImageResource(R.drawable.ic_outline_brightness_1_24);

                    } else if (pos ==2){
                        page3.setImageResource(R.drawable.ic_baseline_brightness_1_24);
                        page1.setImageResource(R.drawable.ic_outline_brightness_1_24);
                        page2.setImageResource(R.drawable.ic_outline_brightness_1_24);
                    }
                }
        });


        // Step 3: Initialise Buttons
        this.register = findViewById(R.id.st_registerBtn);
        this.login = findViewById(R.id.st_logInBtn);

        initialiseButtons();


    }

    private void initialiseButtons(){
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Register clicked");
                Intent i = new Intent(MainActivity.this, registerActivity.class);
                startActivity(i);
            }

        });

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Log in clicked");
                Intent i = new Intent(MainActivity.this, loginActivity.class);
                startActivity(i);
                finish();
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");

        // If there is an existing session, skip this layout and proceed to matching activity.
        String logged_user;
        logged_user = this.sp.getString("LOGGED_USER", null);
        Log.d("TAG", "logged user is " + logged_user);

        if (logged_user != null){
            Intent i = new Intent(MainActivity.this, matchingActivity.class);
            startActivity(i);
            finish();
        }
    }
    

}