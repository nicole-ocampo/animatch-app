package com.mobdeve.s15.group13.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

/*
Class: registerActivity
Description: This class implements the register_step1.xml layout. This class allows the user to register
by providing their email, username, display name, and password. This class makes sure that all fields are correct
and that the passwords match. Error handling is done in the next activity.
 */


public class registerActivity extends AppCompatActivity {
    private EditText emailEt, nameEt, usernameEt, passwordEt, confirmEt;
    private Button nextBtn;
    private final String TAG="Register Step 1: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_step1);

        this.emailEt = findViewById(R.id.rg_emailEt);
        this.nameEt = findViewById(R.id.rg_nameEt);
        this.usernameEt = findViewById(R.id.rg_usernameEt);
        this.passwordEt = findViewById(R.id.rg_passwordEt);
        this.confirmEt = findViewById(R.id.rg_confirmEt);
        this.nextBtn = findViewById(R.id.rg_nextBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, name, username, pw, confirmpw;

                email = emailEt.getText().toString();
                name = nameEt.getText().toString();
                username = usernameEt.getText().toString();
                pw = passwordEt.getText().toString();
                confirmpw = confirmEt.getText().toString();

                // Check whether any field is empty or if email address is invalid
                if (email.isEmpty() || name.isEmpty() || username.isEmpty() || pw.isEmpty() || confirmpw.isEmpty()){
                    Log.d(TAG, "A field is empty. ");
                    Toast.makeText(
                            registerActivity.this,
                            "Please do not leave any fields blank.",
                            Toast.LENGTH_LONG
                    ).show();
                }  else if (isValidEmailId(email)) {

                    // Check if pw matches, then proceed if true
                    if (pw.equals(confirmpw)){
                        Log.d(TAG, "Password matches");
                        Intent i = new Intent(registerActivity.this, nextRegisterActivity.class);

                        i.putExtra("USERNAME_KEY", username);
                        i.putExtra("EMAIL_KEY", email);
                        i.putExtra("NAME_KEY", name);
                        i.putExtra("PASSWORD_KEY", pw);

                        setResult(Activity.RESULT_OK, i);
                        startActivity(i);

                    } else {
                        Log.d(TAG, "Password mismatch");
                        Toast.makeText(
                                registerActivity.this,
                                "Passwords do not match.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }else{
                    Log.d(TAG, "Invalid email");
                    Toast.makeText(
                            registerActivity.this,
                            "Email address is not valid.",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });
    }

    // Function to check whether an email address is valid or not
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
}
