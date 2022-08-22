package com.mobdeve.s15.group13.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


/*
Class: loginActivity
Description: This class implements the log_in.xml layout. This class allows the user to log in to the application.
 */

public class loginActivity extends AppCompatActivity {

    private EditText usernameEt, passwordEt;
    private Button loginBtn;
    private final String TAG="Log in: ";

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private final String FILE_NAME = "SettingsActivity";

    private String username, pw, userId;
    private String dbpw;
    private boolean isNewSession = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        this.usernameEt = findViewById(R.id.li_usernameEt);
        this.passwordEt = findViewById(R.id.li_passwordEt);
        this.loginBtn = findViewById(R.id.li_logInBtn);

        this.sp = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        this.editor = this.sp.edit();

        CollectionReference usersRef= firebaseHelper.getUserCollectionReference();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameEt.getText().toString();
                pw = passwordEt.getText().toString();

                if ((username.length() == 0 && pw.length() == 0)){
                    Toast.makeText(
                            loginActivity.this,
                            "Please answer all the fields.",
                            Toast.LENGTH_SHORT
                    ).show();
                }
                else {
                    Query query = usersRef.whereEqualTo("username", username);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (task.getResult().isEmpty()) {
                                    Log.d("TAG", "No user found");
                                    Toast.makeText(
                                            loginActivity.this,
                                            "User not found.",
                                            Toast.LENGTH_LONG
                                    ).show();
                                } else {
                                    Log.d("TAG", querySnapshot.getDocuments().get(0).getId());
                                    userModel u = querySnapshot.getDocuments().get(0).toObject(userModel.class);

                                    dbpw = u.getPassword();
                                    userId = u.getUserId();

                                    if (username.isEmpty() || pw.isEmpty()) {
                                        Log.d(TAG, "A field is empty.");
                                        Toast.makeText(
                                                loginActivity.this,
                                                "Please do not leave any fields blank.",
                                                Toast.LENGTH_LONG
                                        ).show();
                                    } else {
                                        if (pw.equals(dbpw) == true) { // If passwords match, start session
                                            isNewSession = true;
                                            Intent i = new Intent(loginActivity.this, matchingActivity.class);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            Toast.makeText(
                                                    loginActivity.this,
                                                    "Username or password is incorrect.",
                                                    Toast.LENGTH_LONG
                                            ).show();
                                        }
                                    }
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

        if (this.isNewSession == true) {
            this.editor.putString("LOGGED_USER", username);
            this.editor.putString("LOGGED_USER_ID", userId);
            this.editor.apply();

            Log.d("TAG", "onPause: data written.");
            Log.d("TAG", "Logged user:\n" + username);
        }

    }
}
