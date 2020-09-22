package com.mohakchavan.pustakniparab;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.mohakchavan.pustakniparab.FireBaseHelper.BaseAuthenticator;
import com.mohakchavan.pustakniparab.Services.Network_Service;

import java.util.Objects;

public class Welcome extends Activity {

    private Activity context;
    private BaseAuthenticator authenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        context = Welcome.this;

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                }
            }
        };

        getPermissions();
        Network_Service.checkInternetToProceed(context);
        authenticator = new BaseAuthenticator(context);

        FirebaseUser currentUser = authenticator.getCurrentUser();
        if (currentUser == null) {
            signInUser();
        } else {
            thread.start();
        }
    }

    private void signInUser() {
        String userIdToken = Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(context)).getIdToken();
        if (userIdToken != null) {
            authenticator.authenticateUsingGoogle(userIdToken, new BaseAuthenticator.OnCompleteSignIn() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Hello " + authenticator.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(context, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(context, "Authentication Failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.defaultAndroidClientId))
                    .requestEmail().build();
            GoogleSignInClient client = GoogleSignIn.getClient(context, options);
            startActivityForResult(client.getSignInIntent(), getResources().getInteger(R.integer.SIGN_IN));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == getResources().getInteger(R.integer.SIGN_IN)) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                authenticator.authenticateUsingGoogle(account.getIdToken(), new BaseAuthenticator.OnCompleteSignIn() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Hello " + authenticator.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(context, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(context, "Authentication Failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } catch (ApiException e) {
                Toast.makeText(context, "code " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, getResources().getInteger(R.integer.AccessNetworkCode));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.INTERNET}, getResources().getInteger(R.integer.InternetRequestCode));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == getResources().getInteger(R.integer.AccessNetworkCode) && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Please grant the permission to access network state.", Toast.LENGTH_SHORT).show();
            getPermissions();

        } else if (requestCode == getResources().getInteger(R.integer.InternetRequestCode) && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Please grant the permission to use internet.", Toast.LENGTH_SHORT).show();
            getPermissions();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        finish();
    }

}
