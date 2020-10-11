package com.mohakchavan.pustakniparab;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.mohakchavan.pustakniparab.FireBaseHelper.BaseAuthenticator;
import com.mohakchavan.pustakniparab.IssueModule.AddIssues;
import com.mohakchavan.pustakniparab.IssueModule.Returns;
import com.mohakchavan.pustakniparab.NameModule.AddPerson;
import com.mohakchavan.pustakniparab.NameModule.Search_Name;
import com.mohakchavan.pustakniparab.NameModule.View_All;
import com.mohakchavan.pustakniparab.Services.Network_Service;
import com.mohakchavan.pustakniparab.Services.ProgressBarService;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    Activity context;
    BaseAuthenticator authenticator;
    NavigationView navigationView;

    @Override
    protected void onResume() {
        super.onResume();
        Network_Service.checkInternetToProceed(MainActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        authenticator = new BaseAuthenticator(context);
        drawerLayout = findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);

        navigationView = findViewById(R.id.navigationView);
        ImageView headerImageView = ((ImageView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_iv));
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_name)).setText(authenticator.getCurrentUser().getDisplayName());
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_email)).setText(authenticator.getCurrentUser().getEmail());
        Picasso.get()
                .load(authenticator.getCurrentUser().getPhotoUrl())
                .placeholder(R.drawable.ic_round_person)
                .into(headerImageView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_issues:
                        startActivity(new Intent(context, AddIssues.class));
                        break;

                    case R.id.nav_return_issue:
                        startActivity(new Intent(context, Returns.class));
                        break;

                    case R.id.nav_addPerson:
                        Intent intent = new Intent(context, AddPerson.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_allNames:
                        startActivity(new Intent(context, View_All.class));
                        break;

                    case R.id.nav_searchName:
                        startActivity(new Intent(context, Search_Name.class));
                        break;

                    case R.id.nav_add_books:
                        startActivity(new Intent(context, AddNewBooks.class));
                        break;

                    case R.id.nav_add_user:
                        copyUserDetailsToClipBoard();
                        break;

                    case R.id.nav_sign_out:
                        final ProgressBarService progressBarService = new ProgressBarService("Signing Out...");
                        progressBarService.show(getSupportFragmentManager(), "Progress Bar Dialog");
                        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(getString(R.string.defaultAndroidClientId))
                                .requestEmail().build();
                        GoogleSignInClient client = GoogleSignIn.getClient(context, options);
                        client.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressBarService.dismiss();
                                authenticator.signOut();
                                finish();
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressBarService.dismiss();
                                        Toast.makeText(context, R.string.someError, Toast.LENGTH_SHORT).show();
                                    }
                                });
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START, true);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    private void copyUserDetailsToClipBoard() {
        JSONObject jsonObject = new JSONObject();
        ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        try {
            jsonObject.put("userName", authenticator.getCurrentUser().getDisplayName());
            jsonObject.put("userEmail", authenticator.getCurrentUser().getEmail());
            jsonObject.put("userUid", authenticator.getCurrentUser().getUid());
            jsonObject.put("userPhoto", authenticator.getCurrentUser().getPhotoUrl());
            manager.setPrimaryClip(ClipData.newPlainText("userDetails", jsonObject.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(context, "User details copied to clipboard.", Toast.LENGTH_SHORT).show();
    }
}