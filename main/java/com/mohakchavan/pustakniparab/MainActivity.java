package com.mohakchavan.pustakniparab;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.mohakchavan.pustakniparab.Adapters.Dashboard_Adapter;
import com.mohakchavan.pustakniparab.FireBaseHelper.BaseAuthenticator;
import com.mohakchavan.pustakniparab.FireBaseHelper.BaseHelper;
import com.mohakchavan.pustakniparab.IssueModule.AddIssues;
import com.mohakchavan.pustakniparab.IssueModule.Returns;
import com.mohakchavan.pustakniparab.Models.BaseData;
import com.mohakchavan.pustakniparab.Models.DashBoard.DashBoard;
import com.mohakchavan.pustakniparab.Models.DashBoard.DashBoard_Data;
import com.mohakchavan.pustakniparab.Models.Issues;
import com.mohakchavan.pustakniparab.Models.NewBooks;
import com.mohakchavan.pustakniparab.NameModule.AddPerson;
import com.mohakchavan.pustakniparab.NameModule.Search_Name;
import com.mohakchavan.pustakniparab.NameModule.View_All;
import com.mohakchavan.pustakniparab.Services.Network_Service;
import com.mohakchavan.pustakniparab.Services.ProgressBarService;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Activity context;
    private BaseAuthenticator authenticator;
    private NavigationView navigationView;
    private RecyclerView main_rv_dash;
    private Dashboard_Adapter adapter;
    private BaseHelper baseHelper;
    private BaseData baseData;
    private List<DashBoard> boardList;

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
        baseHelper = new BaseHelper(context);
        drawerLayout = findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);

        main_rv_dash = findViewById(R.id.main_rv_dash);
        main_rv_dash.setHasFixedSize(true);
        main_rv_dash.setLayoutManager(new LinearLayoutManager(context));

        final ProgressBarService progressBarService = new ProgressBarService("Retrieving Data...");
        progressBarService.show(getSupportFragmentManager(), "Progress Bar Dialog");
        baseHelper.getAllBaseDataContinuous(new BaseHelper.onCompleteRetrieval() {
            @Override
            public void onComplete(Object data) {
//                Log.d("BaseRetrieval", String.valueOf(((DataSnapshot) data).getChildrenCount()));
//                Log.d("BaseRetrieval", ((DataSnapshot) data).getValue(BaseData.class).toString());
                baseData = (BaseData) data;
                boardList = new ArrayList<>();
                try {
                    calculateAndPopulateDashboard();
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    Toast.makeText(context, getString(R.string.someError), Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                progressBarService.dismiss();
            }
        });

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

    private void calculateAndPopulateDashboard() throws Exception {
        Date localDate = Calendar.getInstance(TimeZone.getTimeZone(getString(R.string.indianStandardTime))).getTime();
        final SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.dateFormat), Locale.ENGLISH);
        int currMonthIssues = 0, currMonthBooks = 0, notReturned = 0, totalReturned = 0;
        HashMap<String, Integer> monthIssues = new HashMap<>();
        HashMap<String, Integer> monthNewBooks = new HashMap<>();
        HashMap<String, Integer> monthReturns = new HashMap<>();
        for (Issues issue : baseData.getIssuesList()) {
            Date issDate = formatter.parse(issue.getIssueDate());
            if (DateFormat.format("MMM yyyy", issDate).toString().contentEquals(DateFormat.format("MMM yyyy", localDate))) {
                ++currMonthIssues;
            }
            String currMonYear = new StringBuilder(DateFormat.format("MMM", issDate).toString()).append(" ").append(DateFormat.format("yyyy", issDate).toString()).toString();
            if (monthIssues.containsKey(currMonYear)) {
                monthIssues.put(currMonYear, monthIssues.get(currMonYear).intValue() + 1);
            } else {
                monthIssues.put(currMonYear, 1);
            }
            if (issue.getIsReturned().contentEquals(getString(R.string.hasReturned)) && issue.getRetDate() != null && !issue.getRetDate().isEmpty()) {
                Date retDate = formatter.parse(issue.getRetDate());
                String retMonthYear = new StringBuilder(DateFormat.format("MMM", retDate).toString()).append(" ").append(DateFormat.format("yyyy", retDate).toString()).toString();
                if (monthReturns.containsKey(retMonthYear)) {
                    monthReturns.put(retMonthYear, monthReturns.get(retMonthYear).intValue() + 1);
                } else {
                    monthReturns.put(retMonthYear, 1);
                }
            }
            if (issue.getIsReturned().contentEquals(getString(R.string.notReturned))) {
                ++notReturned;
            }
            if (issue.getIsReturned().contentEquals(getString(R.string.hasReturned)) && issue.getRetDate() != null && !issue.getRetDate().isEmpty()) {
                ++totalReturned;
            }
        }
        for (NewBooks book : baseData.getNewBooksList()) {
            Date bookDate = formatter.parse(book.getRegisteredDate());
            if (DateFormat.format("MMM", bookDate).toString().contentEquals(DateFormat.format("MMM", localDate))) {
                currMonthBooks += Integer.parseInt(book.getTotalBooks());
            }
            String currMonYear = new StringBuilder(DateFormat.format("MMM", bookDate).toString()).append(" ").append(DateFormat.format("yyyy", bookDate).toString()).toString();
            if (monthNewBooks.containsKey(currMonYear)) {
                monthNewBooks.put(currMonYear, (monthNewBooks.get(currMonYear).intValue() + Integer.parseInt(book.getTotalBooks())));
            } else {
                monthNewBooks.put(currMonYear, Integer.parseInt(book.getTotalBooks()));
            }
        }

        boardList.add(new DashBoard(false, new DashBoard_Data[]{new DashBoard_Data(currMonthIssues, DateFormat.format("MMM", localDate).toString() + " Javak")
                , new DashBoard_Data(currMonthBooks, DateFormat.format("MMM", localDate).toString() + " Aavak")}));

        boardList.add(new DashBoard(true, new DashBoard_Data[]{getDashBoardDataFromHashMap(monthIssues, "Monthly Javak")}));

        boardList.add(new DashBoard(true, new DashBoard_Data[]{getDashBoardDataFromHashMap(monthNewBooks, "Monthly Aavak")}));
        boardList.add(new DashBoard(false, new DashBoard_Data[]{new DashBoard_Data(baseData.getIssuesList().size(), "Total Javak")
                , new DashBoard_Data(baseData.getTotalNewBooks(), "Total Aavak")}));
        boardList.add(new DashBoard(false, new DashBoard_Data[]{new DashBoard_Data(notReturned, "Total Not Returned")
                , new DashBoard_Data(Math.round(((double) totalReturned / (double) baseData.getIssuesList().size()) * 100), "Return Ratio (%)")}));

        boardList.add(new DashBoard(true, new DashBoard_Data[]{getDashBoardDataFromHashMap(monthReturns, "Monthly Returns")}));
        adapter = new Dashboard_Adapter(context, boardList);
        main_rv_dash.setAdapter(adapter);
    }

    private DashBoard_Data getDashBoardDataFromHashMap(HashMap<String, Integer> hashMap, String bottomLabel) {
        List<String> keys = new ArrayList<>(hashMap.keySet());
        Collections.sort(keys, new Comparator<String>() {
            final SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);

            @Override
            public int compare(String o1, String o2) {
                try {
                    return formatter.parse(o1).compareTo(formatter.parse(o2));
                } catch (ParseException ex) {
                    throw new IllegalArgumentException(ex);
                }
            }
        });

        List<DataPoint> dataPoints = new ArrayList<>();
        String bottomData = "";
        for (int i = 0; i < keys.size(); i++) {
            dataPoints.add(new DataPoint(i + 1, hashMap.get(keys.get(i))));
            bottomData += "," + keys.get(i);
        }
        bottomData += "," + bottomLabel;
        return new DashBoard_Data(bottomData, new BarGraphSeries<DataPoint>(dataPoints.toArray(new DataPoint[0])));
//        return new DashBoard_Data(bottomData, new LineGraphSeries<DataPoint>(dataPoints.toArray(new DataPoint[0])));
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