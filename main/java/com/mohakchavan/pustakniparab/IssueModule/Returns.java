package com.mohakchavan.pustakniparab.IssueModule;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mohakchavan.pustakniparab.Adapters.All_Issues_Adapter;
import com.mohakchavan.pustakniparab.FireBaseHelper.BaseHelper;
import com.mohakchavan.pustakniparab.FireBaseHelper.IssuesHelper;
import com.mohakchavan.pustakniparab.Models.Issues;
import com.mohakchavan.pustakniparab.R;

import java.util.List;
import java.util.Objects;

public class Returns extends AppCompatActivity {

    private IssuesHelper issuesHelper;
    private Activity context;
    private RecyclerView rt_rv_issList;
    private List<Issues> issuesList;
    private All_Issues_Adapter all_issues_adapter;
    private TextView rt_tv_search_result;
    private RecyclerView.AdapterDataObserver adapterDataObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returns);

        context = Returns.this;
        issuesHelper = new IssuesHelper(context);
        rt_tv_search_result = findViewById(R.id.rt_tv_search_result);

        rt_rv_issList = findViewById(R.id.rt_rv_issList);
        rt_rv_issList.setHasFixedSize(true);
        rt_rv_issList.setLayoutManager(new LinearLayoutManager(context));

        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.returns));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        issuesHelper.getAllIssuesContinuous(new BaseHelper.onCompleteRetrieval() {
            @Override
            public void onComplete(Object data) {
                if (data != null)
                    issuesList = (List<Issues>) data;
                all_issues_adapter = new All_Issues_Adapter(context, issuesList);
                adapterDataObserver = new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        if (all_issues_adapter.getItemCount() > 0) {
                            rt_tv_search_result.setText("Results");
                            rt_tv_search_result.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                        } else {
                            rt_tv_search_result.setText(getText(R.string.noRecordsFound).subSequence(0, getText(R.string.noRecordsFound).length() - 1));
                            rt_tv_search_result.setTextColor(ContextCompat.getColor(context, R.color.Red));
                        }
                    }
                };
                all_issues_adapter.registerAdapterDataObserver(adapterDataObserver);
                rt_rv_issList.setAdapter(all_issues_adapter);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
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
        issuesHelper.removeAllNamesListener();
        all_issues_adapter.unregisterAdapterDataObserver(adapterDataObserver);
    }
}