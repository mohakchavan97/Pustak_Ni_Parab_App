package com.mohakchavan.pustakniparab.Helpers.FireBaseHelper;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.mohakchavan.pustakniparab.R;

public class BaseAuthenticator {

    private Activity context;
    private static GoogleSignInOptions.Builder options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail().requestProfile();
    private static GoogleSignInClient client;
    private static GoogleSignInAccount googleAccount;
    private User user;

    public BaseAuthenticator(Activity context) {
        this.context = context;
        options.requestIdToken(context.getResources().getString(R.string.defaultAndroidClientId));
        client = GoogleSignIn.getClient(this.context, options.build());
        googleAccount = GoogleSignIn.getLastSignedInAccount(this.context);
        if (googleAccount != null) {
            setGoogleUserAccount(googleAccount);
        }
    }

    public boolean isValidUserAccount() {
        return googleAccount != null && !googleAccount.isExpired();
    }

    public void setGoogleUserAccount(GoogleSignInAccount account) {
        user = new User(account.getId(), account.getDisplayName(), account.getEmail(), account.getPhotoUrl().toString());
    }

    public Intent getIntentForSignIn() {
        return client.getSignInIntent();
    }

    public String getUserUid() {
        return user.uId;
    }

    public String getUserDisplayName() {
        return user.displayName;
    }

    public String getUserEmail() {
        return user.email;
    }

    public String getUserPhotoUrl() {
        return user.photoUrl;
    }

    public Task<Void> signOut() {
        return client.signOut();
    }

    private class User {

        private final String uId, displayName, email, photoUrl;

        private User(String userId, String displayName, String userEmail, String photoUrl) {
            this.uId = userId;
            this.displayName = displayName;
            this.email = userEmail;
            this.photoUrl = photoUrl;
        }
    }
}