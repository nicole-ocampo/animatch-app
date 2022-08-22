package com.mobdeve.s15.group13.project;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Ref;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/*
Class: nextRegisterActivity
Description: This class implements the register_step2.xml layout. This class presents the final step of registration.
This also initialise the new user account into the database.
 */


public class nextRegisterActivity extends AppCompatActivity {

    private EditText birthdayEt;
    private Button registerBtn;
    private final String TAG="Register birhday: ";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private final String FILE_NAME = "SettingsActivity";
    private DatePickerDialog datepicker;

    private String userId;

    private boolean isRegisterSuccess;
    private int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_step2);

        final Calendar myCalendar = Calendar.getInstance();

        this.birthdayEt = findViewById(R.id.rg_birthdayEt);
        this.registerBtn = findViewById(R.id.rg_registerBtn);

        this.sp = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        this.editor = this.sp.edit();

        // Step 1: Prepare the date picker
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                birthdayEt.setText(sdf.format(myCalendar.getTime()));

            }

        };

        // Step 2: When the edit text is clicked, the date picker pops up
        datepicker = new DatePickerDialog(nextRegisterActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        birthdayEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                datepicker.show();
            }
        });

        // Step 3: Finalise Registration
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assume that register is not successful so it does not overwrite shared preferences
                isRegisterSuccess = false;

                String username, name, email, pw;
                String bday, month, day, birthyear;

                // Separate birthdate into month, day, and year
                bday = birthdayEt.getText().toString();
                ArrayList<Integer> matchlist = new ArrayList<>();
                ArrayList<HashMap<String, String>> finishedlist = new ArrayList<>();

                month = bday.substring(0,2);
                day = bday.substring(3,5);
                birthyear = bday.substring(6,10);

                // Check if user is under 13. If they are, do not proceed with registration.
                int cur_year = Calendar.getInstance().get(Calendar.YEAR);
                int int_birthyear = Integer.parseInt(birthyear);
                age = cur_year - int_birthyear;

                if (age < 13){
                    Toast.makeText(
                            nextRegisterActivity.this,
                            "Users below 13 are not allowed to register.",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {


                    // Get the previously logged information
                    username = getIntent().getStringExtra("USERNAME_KEY");
                    name = getIntent().getStringExtra("NAME_KEY");
                    email = getIntent().getStringExtra("EMAIL_KEY");
                    pw = getIntent().getStringExtra("PASSWORD_KEY");

                    // Check if user and email is taken. Because this is asynchronous, this must be done using
                    // nested methods.
                    CollectionReference usersRef = firebaseHelper.getUserCollectionReference();
                    CollectionReference matchesRef = firebaseHelper.getMatchesCollectionReference();
                    CollectionReference finishedRef = firebaseHelper.getFinishedCollectionReference();

                    // get random autogenerated id and add it to a user id field
                    userId = usersRef.document().getId();

                    // First, check if the email is taken
                    usersRef.whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                Log.d("TAG", "oof" + querySnapshot.toString());
                                if (querySnapshot.isEmpty()) {
                                    Log.d("TAG", "No account with this email found");

                                    // If there is no document with this email found, check for the registered username
                                    usersRef.whereEqualTo("username", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                QuerySnapshot querySnapshot = task.getResult();
                                                if (task.getResult().isEmpty()) {
                                                    Log.d("TAG", "No account with this username found");
                                                    // If email and username are not taken, write to DB.


                                                    Map<String, Object> matchesCollectionsData = new HashMap<>();
//                                                    matchesCollectionsData.put("username", username);
                                                    matchesCollectionsData.put("matchlist", matchlist);
                                                    matchesCollectionsData.put("userId", userId);

                                                    Map<String, Object> finishedCollectionsData = new HashMap<>();
//                                                    finishedCollectionsData.put("username", username);
                                                    finishedCollectionsData.put("finishedlist", finishedlist);
                                                    finishedCollectionsData.put("userId", userId);

                                                    Map<String, Object> data1 = new HashMap<>();
                                                    data1.put("username", username);
                                                    data1.put("name", name);
                                                    data1.put("email", email);
                                                    data1.put("password", pw);
                                                    data1.put("birthMonth", month);
                                                    data1.put("birthDate", day);
                                                    data1.put("birthYear", birthyear);
                                                    data1.put("userId", userId);


                                                    usersRef.document(userId)
                                                            .set(data1)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w(TAG, "Error writing document", e);
                                                                }
                                                            });

                                                    matchesRef.document(userId)
                                                            .set(matchesCollectionsData)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w(TAG, "Error writing document", e);
                                                                }
                                                            });

                                                    finishedRef.document(userId)
                                                            .set(finishedCollectionsData)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w(TAG, "Error writing document", e);
                                                                }
                                                            });

                                                    isRegisterSuccess = true;

                                                    // Finish registering and clear the activity stack
                                                    Intent i = new Intent(nextRegisterActivity.this, matchingActivity.class);
                                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(i);
                                                    datepicker.dismiss();
                                                    finish();

                                                } else {
                                                    Toast.makeText(
                                                            nextRegisterActivity.this,
                                                            "Username is already taken.",
                                                            Toast.LENGTH_SHORT
                                                    ).show();
                                                }
                                            } else
                                                Log.d("TAG", "Error getting documents: ", task.getException());
                                        }
                                    });
                                } else {
                                    Toast.makeText(
                                            nextRegisterActivity.this,
                                            "Email is already taken.",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            } else
                                Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    });
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();

        // User starts session upon registration
        if (isRegisterSuccess == true) {
            this.editor.putString("LOGGED_USER", getIntent().getStringExtra("USERNAME_KEY"));
            this.editor.putString("LOGGED_USER_ID", userId);
            this.editor.apply();

            Log.d(TAG, "onPause: data written.");
            Log.d(TAG, "logged user: " + getIntent().getStringExtra("USERNAME_KEY"));
            Log.d(TAG, "logged user id: " + userId);
        }
    }
}
