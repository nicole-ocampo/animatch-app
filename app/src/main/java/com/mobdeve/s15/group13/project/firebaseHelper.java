package com.mobdeve.s15.group13.project;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class firebaseHelper {
    private static FirebaseFirestore firebaseFirestoreInstance = null;
    private static CollectionReference animeRef = null;
    private static CollectionReference usersRef = null;
    private static CollectionReference matchesRef = null;
    private static CollectionReference finishedRef = null;

    public final static String
            USERS_COLLECTION = "users",
            ANIME_COLLECTION = "anime",
            MATCHES_COLLECTION = "matches",
            FINISHED_COLLECTION = "finished";

    public static FirebaseFirestore getFirestoreInstance() {
        if(firebaseFirestoreInstance == null) {
            firebaseFirestoreInstance = FirebaseFirestore.getInstance();
        }
        return firebaseFirestoreInstance;
    }

    public static CollectionReference getUserCollectionReference() {
        if(usersRef == null) {
            usersRef = getFirestoreInstance().collection(USERS_COLLECTION);
        }
        return usersRef;
    }

    public static CollectionReference getAnimeCollectionReference() {
        if(animeRef == null) {
            animeRef = getFirestoreInstance().collection(ANIME_COLLECTION);
        }
        return animeRef;
    }

    public static CollectionReference getMatchesCollectionReference() {
        if(matchesRef == null) {
            matchesRef = getFirestoreInstance().collection(MATCHES_COLLECTION);
        }
        return matchesRef;
    }

    public static CollectionReference getFinishedCollectionReference() {
        if(finishedRef == null) {
            finishedRef = getFirestoreInstance().collection(FINISHED_COLLECTION);
        }
        return finishedRef;
    }

}
