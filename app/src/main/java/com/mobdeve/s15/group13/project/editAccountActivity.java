package com.mobdeve.s15.group13.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/*
Class: editAccountActivity
Description: This class implements the edit_account.xml layout. This class allows the user to edit his
account information or delete his account. The only information allowed to be edited are the following:
username, email, password, and their display name. Birthdays are not allowed to be edited in order to strictly follow
the anime's rating descriptions.
 */

public class editAccountActivity extends AppCompatActivity {

    private EditText username;
    private EditText email;
    private EditText name;
    private EditText pw;
    private EditText confirmpw;
    private Button saveBtn;
    private Button deleteBtn;

    private String newUserName, newEmail, newName, newPw, newConfirm;
    private String oldUserName, oldEmail, oldName, oldPw;
    private boolean proceedEdit = false;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private final String FILE_NAME = "SettingsActivity";

    private String userId;
    private boolean isDeleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_account);

        this.email = findViewById(R.id.ea_emailEt);
        this.name = findViewById(R.id.ea_nameEt);
        this.pw = findViewById(R.id.ea_passwordEt);
        this.confirmpw = findViewById(R.id.ea_confirmEt);
        this.username = findViewById(R.id.ea_userNameEt);

        this.saveBtn = findViewById(R.id.ea_saveBtn);
        this.deleteBtn = findViewById(R.id.ea_deleteBtn);

        this.sp = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        this.userId = this.sp.getString("LOGGED_USER_ID", null);
        this.editor = this.sp.edit();


        // Get old details to override in case fields are left blank.
        CollectionReference usersRef= firebaseHelper.getUserCollectionReference();
        CollectionReference matchesRef= firebaseHelper.getMatchesCollectionReference();
        CollectionReference finishedRef = firebaseHelper.getFinishedCollectionReference();

        Query query = usersRef.whereEqualTo("userId", userId);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (task.getResult().isEmpty())
                        Log.d("TAG", "No user found");
                    else {
                        Log.d("TAG", querySnapshot.getDocuments().get(0).getId());
                        userModel u = querySnapshot.getDocuments().get(0).toObject(userModel.class);

                        oldEmail = u.getEmail();
                        oldUserName = u.getUsername();
                        oldName = u.getName();
                        oldPw = u.getPassword();

                        Log.d("TAG", "Old Email: " + oldEmail);
                        Log.d("TAG", "Old Username: " + oldUserName);
                        Log.d("TAG", "Old Name: " + oldName);
                    }
                }
                else
                    Log.d("TAG", "Error getting documents: ", task.getException());
            }
        });


        // Sets the logic for the save button
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedEdit = false;
                Map<String, Object> data1 = new HashMap<>();

                newEmail = email.getText().toString();
                newUserName = username.getText().toString();
                newName = name.getText().toString();
                newPw = pw.getText().toString();
                newConfirm = confirmpw.getText().toString();

                // If all fields are empty, do not do anything.
                // If email address is invalid, issue a warning.
                // If at least one field contains something, but some fields are empty, set old values as default values
                if (newEmail.length() == 0 && newName.length() == 0 && newUserName.length() == 0 && newPw.length() == 0){
                    Toast.makeText(
                            editAccountActivity.this,
                            "No fields to update.",
                            Toast.LENGTH_SHORT
                    ).show();
                } else if ((isValidEmailId(newEmail) == false) && newEmail.length() != 0){
                    Toast.makeText(
                            editAccountActivity.this,
                            "Email address is invalid.",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    // If the user is changing any other field except the password, proceed to editing
                    if ((newEmail.length() > 0 || newName.length() > 0 || newUserName.length() > 0) && newPw.length() == 0)
                        proceedEdit = true;

                    if (newEmail.length() == 0)
                        newEmail = oldEmail;

                    if (newUserName.length() == 0)
                        newUserName = oldUserName;

                    if (newName.length() == 0)
                        newName = oldName;

                    if (newPw.length() == 0)
                        newPw = oldPw;
                    else {
                        // If user is changing their password, make sure it matches.
                        if (newPw.equals(newConfirm) == false) {
                            Toast.makeText(
                                    editAccountActivity.this,
                                    "Passwords do not match.",
                                    Toast.LENGTH_SHORT
                            ).show();
                            proceedEdit = false;
                        } else
                            proceedEdit = true;
                    }
                }

                // app checks whether the user is updating their email with a new email
                if (newEmail.length() != 0 && (newEmail.equals(oldEmail) == false)){

                    // app checks whether the new email is already taken. if already taken, a toast appears as a warning and no changes are saved.
                    usersRef.whereEqualTo("email", newEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                Log.d("TAG", "oof" + querySnapshot.toString());
                                if (querySnapshot.isEmpty()) {

                                    // app checks whether the user is updating their username with a new one
                                    if(newUserName.length() != 0 && (newUserName.equals(oldUserName) == false)){

                                        // app checks whether the new username is already taken. if already taken, a toast appears as a warning and no changes are saved.
                                        usersRef.whereEqualTo("username", newUserName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    QuerySnapshot querySnapshot = task.getResult();
                                                    if (task.getResult().isEmpty()) {

                                                        if (proceedEdit == true) {
                                                            data1.put("username", newUserName);
                                                            data1.put("email", newEmail);
                                                            data1.put("name", newName);
                                                            data1.put("password", newPw);

                                                            usersRef.document(userId)
                                                                    .update(data1)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Log.d("TAG", "DocumentSnapshot successfully updated!");
                                                                            Toast.makeText(
                                                                                    editAccountActivity.this,
                                                                                    "Account successfully updated!",
                                                                                    Toast.LENGTH_SHORT
                                                                            ).show();
                                                                            Intent i = new Intent(editAccountActivity.this, accountActivity.class);
                                                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                            startActivity(i);
                                                                            finish();

                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Log.w("TAG", "Error writing document", e);
                                                                            Toast.makeText(
                                                                                    editAccountActivity.this,
                                                                                    "There was an error updating your account. Please try again.",
                                                                                    Toast.LENGTH_LONG
                                                                            ).show();
                                                                        }
                                                                    });
                                                        }

                                                    } else {
                                                        Toast.makeText(
                                                                editAccountActivity.this,
                                                                "Username is already taken.",
                                                                Toast.LENGTH_SHORT
                                                        ).show();

                                                    }
                                                } else
                                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                            }
                                        });
                                    } else if (newUserName.equals(oldUserName)){
                                        if (proceedEdit == true) {
                                            data1.put("username", newUserName);
                                            data1.put("email", newEmail);
                                            data1.put("name", newName);
                                            data1.put("password", newPw);

                                            usersRef.document(userId)
                                                    .update(data1)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("TAG", "DocumentSnapshot successfully updated!");
                                                            Toast.makeText(
                                                                    editAccountActivity.this,
                                                                    "Account successfully updated!",
                                                                    Toast.LENGTH_SHORT
                                                            ).show();
                                                            Intent i = new Intent(editAccountActivity.this, accountActivity.class);
                                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(i);
                                                            finish();

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w("TAG", "Error writing document", e);
                                                            Toast.makeText(
                                                                    editAccountActivity.this,
                                                                    "There was an error updating your account. Please try again.",
                                                                    Toast.LENGTH_LONG
                                                            ).show();
                                                        }
                                                    });
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                            editAccountActivity.this,
                                            "Email is already taken.",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            } else
                                Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    });
                } else if (newEmail.equals(oldEmail) ){

                    // app checks whether the user is updating their username with a new one
                    if(newUserName.length() != 0 && (newUserName.equals(oldUserName) == false)){

                        // app checks whether the new username is already taken. if already taken, a toast appears as a warning and no changes are saved.
                        usersRef.whereEqualTo("username", newUserName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (task.getResult().isEmpty()) {

                                        if (proceedEdit == true) {
                                            data1.put("username", newUserName);
                                            data1.put("email", newEmail);
                                            data1.put("name", newName);
                                            data1.put("password", newPw);

                                            usersRef.document(userId)
                                                    .update(data1)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("TAG", "DocumentSnapshot successfully updated!");
                                                            Toast.makeText(
                                                                    editAccountActivity.this,
                                                                    "Account successfully updated!",
                                                                    Toast.LENGTH_SHORT
                                                            ).show();
                                                            Intent i = new Intent(editAccountActivity.this, accountActivity.class);
                                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(i);
                                                            finish();

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w("TAG", "Error writing document", e);
                                                            Toast.makeText(
                                                                    editAccountActivity.this,
                                                                    "There was an error updating your account. Please try again.",
                                                                    Toast.LENGTH_LONG
                                                            ).show();
                                                        }
                                                    });
                                        }

                                    } else {
                                        Toast.makeText(
                                                editAccountActivity.this,
                                                "Username is already taken.",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                    }
                                } else
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        });
                    } else if (newUserName.equals(oldUserName)){

                        if (proceedEdit == true) {
                            data1.put("username", newUserName);
                            data1.put("email", newEmail);
                            data1.put("name", newName);
                            data1.put("password", newPw);

                            usersRef.document(userId)
                                    .update(data1)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("TAG", "DocumentSnapshot successfully updated!");
                                            Toast.makeText(
                                                    editAccountActivity.this,
                                                    "Account successfully updated!",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                            Intent i = new Intent(editAccountActivity.this, accountActivity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(i);
                                            finish();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("TAG", "Error writing document", e);
                                            Toast.makeText(
                                                    editAccountActivity.this,
                                                    "There was an error updating your account. Please try again.",
                                                    Toast.LENGTH_LONG
                                            ).show();
                                        }
                                    });
                        }
                    }
                }


                Log.d("TAG", "New Email: " + newEmail);
                Log.d("TAG", "New Username: " + newUserName);
                Log.d("TAG", "New Name: " + newName);



            }
        });


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alert = new AlertDialog.Builder(editAccountActivity.this)
                        .setIcon(getResources().getDrawable(R.drawable.ic_baseline_warning_24))
                        .setTitle("Delete account")
                        .setMessage("Are you sure you want to delete your account?")
                        .setPositiveButton("Yes, I'm sure.", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // Delete from shared preferences logged user
                                isDeleted = true;

                                // Delete user data
                                usersRef.document(userId)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "DocumentSnapshot Users successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error deleting users document", e);
                                            }
                                        });

                                // Delete matches data
                                matchesRef.document(userId)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "DocumentSnapshot Matches successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error deleting matches document", e);
                                            }
                                        });

                                // Delete finished watching data
                                finishedRef.document(userId)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "DocumentSnapshot finished successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error deleting finished document", e);
                                            }
                                        });

                                Log.d("TAG", "Account deleted!");


                                // Log out after deleting.
                                Intent i = new Intent(editAccountActivity.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    // Function to check whether an email address is valid or not
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Delete logged user from shared preferences if user deleted their accounts.
        if (this.isDeleted == true) {
            this.editor.putString("LOGGED_USER", null);
            this.editor.apply();

            Log.d("TAG", "onPause: data written. user account has been deleted.");
            Log.d("TAG", "Logged user:\n" + null);
        }

    }
}
