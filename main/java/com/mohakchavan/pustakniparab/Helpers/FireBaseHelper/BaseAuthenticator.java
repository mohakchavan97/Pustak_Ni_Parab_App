package com.mohakchavan.pustakniparab.Helpers.FireBaseHelper;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class BaseAuthenticator {

    private FirebaseAuth firebaseAuth;
    private Activity context;

    public BaseAuthenticator(Activity context) {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public void authenticateUsingGoogle(String userIdToken, final OnCompleteSignIn onCompleteSignIn) {
        AuthCredential credential = GoogleAuthProvider.getCredential(userIdToken, null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                onCompleteSignIn.onComplete(task);
            }
        });
    }

    public void signOut() {
        firebaseAuth.signOut();
    }

    public interface OnCompleteSignIn {
        void onComplete(Task<AuthResult> task);
    }
}
