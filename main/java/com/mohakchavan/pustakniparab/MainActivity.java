package com.mohakchavan.pustakniparab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.mohakchavan.pustakniparab.FireBaseHelper.BaseAuthenticator;
import com.mohakchavan.pustakniparab.IssueModule.AddIssues;
import com.mohakchavan.pustakniparab.IssueModule.Returns;
import com.mohakchavan.pustakniparab.NameModule.AddPerson;
import com.mohakchavan.pustakniparab.NameModule.Search_Name;
import com.mohakchavan.pustakniparab.NameModule.View_All;
import com.mohakchavan.pustakniparab.Services.Network_Service;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    Activity context;
    BaseAuthenticator authenticator;

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

        NavigationView navigationView = findViewById(R.id.navigationView);
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_tv)).setText(authenticator.getCurrentUser().getDisplayName());
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
}