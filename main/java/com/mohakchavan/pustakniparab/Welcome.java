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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.mohakchavan.pustakniparab.Helpers.FireBaseHelper.BaseAuthenticator;
import com.mohakchavan.pustakniparab.Services.Network_Service;

public class Welcome extends Activity {

    private Activity context;
    private BaseAuthenticator authenticator;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        context = Welcome.this;

        thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Hello " + authenticator.getUserDisplayName(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        getPermissions();
        authenticator = new BaseAuthenticator(context);

        if (!authenticator.isValidUserAccount() && Network_Service.checkInternetToProceed(context)) {
            startActivityForResult(authenticator.getIntentForSignIn(), getResources().getInteger(R.integer.SIGN_IN));
        } else {
            thread.start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == getResources().getInteger(R.integer.SIGN_IN)) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                authenticator.setGoogleUserAccount(account);
                thread.start();
            } catch (ApiException e) {
                Toast.makeText(context, getString(R.string.someError), Toast.LENGTH_SHORT).show();
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
