package com.mohakchavan.pustakniparab.NameModule;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mohakchavan.pustakniparab.Adapters.View_All_Adapter;
import com.mohakchavan.pustakniparab.DBHelper;
import com.mohakchavan.pustakniparab.FireBaseHelper.BaseHelper;
import com.mohakchavan.pustakniparab.FireBaseHelper.NamesHelper;
import com.mohakchavan.pustakniparab.Models.Names;
import com.mohakchavan.pustakniparab.R;
import com.mohakchavan.pustakniparab.Services.ProgressBarService;

import java.util.List;
import java.util.Objects;

public class View_All extends AppCompatActivity {

    RecyclerView va_viewall;
    View_All_Adapter adapter;
    TextView tv_state;
    List<Names> namesList;
    private Activity context;
    private DBHelper helper;
    private NamesHelper namesHelper;

    @Override
    protected void onResume() {
        super.onResume();
        final ProgressBarService progressBarService = new ProgressBarService("Retrieving Names...");
        progressBarService.show(getSupportFragmentManager(), "Progress Bar Dialog");
        namesHelper.getAllNamesContinuous(new BaseHelper.onCompleteRetrieval() {
            @Override
            public void onComplete(Object data) {
                progressBarService.dismiss();
                populateWithNames((List<Names>)data);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.allNames);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = View_All.this;
        helper = new DBHelper(context);
        namesHelper = new NamesHelper(context);
        tv_state = findViewById(R.id.ac_va_tv_state);

        va_viewall = findViewById(R.id.ac_va_rv_viewall);
        va_viewall.setHasFixedSize(true);
        va_viewall.setLayoutManager(new LinearLayoutManager(context));

//        namesList = helper.getAllNames();

        //region Transferred to onResume
        /*final ProgressBarService progressBarService = new ProgressBarService("Retrieving Names...");
        progressBarService.show(getSupportFragmentManager(), "Progress Bar Dialog");
        namesHelper.getAllNamesContinuous(new BaseHelper.onCompleteRetrieval() {
            @Override
            public void onComplete(Object data) {
                progressBarService.dismiss();
                populateWithNames((List<Names>) data);
            }
        });*/
        //endregion
    }

    private void populateWithNames(List<Names> data) {
        namesList = data;
        if (namesList.isEmpty()) {
            tv_state.setVisibility(View.VISIBLE);
            tv_state.setText(R.string.noRecordsFound);
        } else {
            tv_state.setVisibility(View.GONE);
            adapter = new View_All_Adapter(context, namesList);
            va_viewall.setAdapter(adapter);
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
        namesHelper.removeAllNamesListener();
    }
}
