package com.mohakchavan.pustakniparab.IssueModule;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.mohakchavan.pustakniparab.Adapters.All_Issues_Adapter;
import com.mohakchavan.pustakniparab.Helpers.FireBaseHelper.BaseHelper;
import com.mohakchavan.pustakniparab.Helpers.FireBaseHelper.IssuesHelper;
import com.mohakchavan.pustakniparab.Models.Issues;
import com.mohakchavan.pustakniparab.R;
import com.mohakchavan.pustakniparab.Services.ProgressBarService;

import java.util.ArrayList;
import java.util.Objects;

public class View_All_Issues extends AppCompatActivity {

    RecyclerView va_viewAll;
    TextView tv_state;
    private Activity context;
    private IssuesHelper issuesHelper;
    private All_Issues_Adapter all_issues_adapter;

    @Override
    protected void onResume() {
        super.onResume();
        final ProgressBarService progressBarService = new ProgressBarService("Retrieving Issues...");
        progressBarService.show(getSupportFragmentManager(), "Progress Bar Dialog");
        issuesHelper.getAllIssuesContinuous(
                new BaseHelper.onCompleteRetrieval() {
                    @Override
                    public void onComplete(Object data) {
                        progressBarService.dismiss();
                        if (data != null) {
                            populateWithIssues((ArrayList<Issues>) data);
                        }
                    }
                },
                new BaseHelper.onFailure() {
                    @Override
                    public void onFail(Object data) {
                        progressBarService.dismiss();
                        if (!((DatabaseError) data).getMessage().isEmpty()) {
                            Toast.makeText(context, ((DatabaseError) data).getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, getString(R.string.someError), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_issues);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.allIssues);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = View_All_Issues.this;
        issuesHelper = new IssuesHelper(context);
        tv_state = findViewById(R.id.ac_vai_tv_state);

        va_viewAll = findViewById(R.id.ac_vai_rv_allIssues);
        va_viewAll.setHasFixedSize(true);
        va_viewAll.setLayoutManager(new LinearLayoutManager(context));

        tv_state.setVisibility(View.GONE);
    }

    private void populateWithIssues(ArrayList<Issues> data) {
        if (data.isEmpty()) {
            tv_state.setVisibility(View.VISIBLE);
            tv_state.setText(R.string.noRecordsFound);
            tv_state.setTextColor(ContextCompat.getColor(context, R.color.Red));
        } else {
            tv_state.setVisibility(View.GONE);
            ArrayList<Issues> reversed = new ArrayList<>(data.size());
            for (int i = data.size() - 1; i > 0; i--) {
                reversed.add(data.get(i));
            }
            all_issues_adapter = new All_Issues_Adapter(context, reversed);
            all_issues_adapter.setForJustDisplay(true);
            va_viewAll.setAdapter(all_issues_adapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (issuesHelper != null) {
            issuesHelper.removeAllNamesListener();
        }
    }
}