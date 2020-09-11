package com.mohakchavan.pustakniparab;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.mohakchavan.pustakniparab.Services.Network_Service;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onResume() {
        super.onResume();
        Network_Service.checkInternetToProceed(MainActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_issues:
                        startActivity(new Intent(MainActivity.this, Issues.class));
                        break;

                    case R.id.nav_addPerson:
                        Intent intent = new Intent(MainActivity.this, AddPerson.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_return_issue:
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}