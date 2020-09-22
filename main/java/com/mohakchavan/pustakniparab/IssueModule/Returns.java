package com.mohakchavan.pustakniparab.IssueModule;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mohakchavan.pustakniparab.Adapters.All_Issues_Adapter;
import com.mohakchavan.pustakniparab.FireBaseHelper.BaseHelper;
import com.mohakchavan.pustakniparab.FireBaseHelper.IssuesHelper;
import com.mohakchavan.pustakniparab.FireBaseHelper.NamesHelper;
import com.mohakchavan.pustakniparab.Models.Issues;
import com.mohakchavan.pustakniparab.Models.Names;
import com.mohakchavan.pustakniparab.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Returns extends AppCompatActivity {

    private IssuesHelper issuesHelper;
    private NamesHelper namesHelper;
    private Activity context;
    private RecyclerView rt_rv_issList;
    private List<Issues> issuesList;
    private All_Issues_Adapter all_issues_adapter;
    private TextView rt_tv_search_result;
    private EditText rt_ed_bkId, rt_ed_bkName, rt_ed_issrName;
    private Spinner rt_sp_issrId;
    private String filterString;
    private RecyclerView.AdapterDataObserver adapterDataObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returns);

        context = Returns.this;
        issuesHelper = new IssuesHelper(context);
        namesHelper = new NamesHelper(context);
        rt_tv_search_result = findViewById(R.id.rt_tv_search_result);
        rt_ed_bkId = findViewById(R.id.rt_ed_bkId);
        rt_ed_bkName = findViewById(R.id.rt_ed_bkName);
        rt_ed_issrName = findViewById(R.id.rt_ed_issrName);
        rt_sp_issrId = findViewById(R.id.rt_sp_issrId);
        filterString = "";

        rt_rv_issList = findViewById(R.id.rt_rv_issList);
        rt_rv_issList.setHasFixedSize(true);
        rt_rv_issList.setLayoutManager(new LinearLayoutManager(context));

        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.returns));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        getAllIssues();

        rt_sp_issrId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (rt_sp_issrId.isEnabled()) {
                    filterString = parent.getItemAtPosition(position).toString().trim();
                    if (filterString.contentEquals(getString(R.string.firstSpinnerItem))) {
                        all_issues_adapter.getFilter().filter("");
                    } else {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject().put("key", "nameId");
                            jsonObject.put("value", filterString);
                            filterString = jsonObject.toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        all_issues_adapter.getFilter().filter(filterString);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rt_ed_bkId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (rt_ed_bkId.isEnabled()) {
                    filterString = rt_ed_bkId.getText().toString().trim();
                    if (filterString.isEmpty()) {
                        rt_sp_issrId.setEnabled(true);
                        rt_ed_bkName.setEnabled(true);
                        rt_ed_issrName.setEnabled(true);
                    } else {
                        rt_sp_issrId.setEnabled(false);
                        rt_sp_issrId.setSelection(0, true);
                        rt_ed_bkName.setEnabled(false);
                        rt_ed_issrName.setEnabled(false);
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject().put("key", "bookId");
                            jsonObject.put("value", filterString);
                            filterString = jsonObject.toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    all_issues_adapter.getFilter().filter(filterString);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rt_ed_bkName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (rt_ed_bkName.isEnabled()) {
                    filterString = rt_ed_bkName.getText().toString().trim().toUpperCase();
                    if (filterString.isEmpty()) {
                        rt_sp_issrId.setEnabled(true);
                        rt_ed_bkId.setEnabled(true);
                        rt_ed_issrName.setEnabled(true);
                    } else {
                        rt_sp_issrId.setEnabled(false);
                        rt_sp_issrId.setSelection(0, true);
                        rt_ed_bkId.setEnabled(false);
                        rt_ed_issrName.setEnabled(false);
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject().put("key", "bookName");
                            jsonObject.put("value", filterString);
                            filterString = jsonObject.toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    all_issues_adapter.getFilter().filter(filterString);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rt_ed_issrName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (rt_ed_issrName.isEnabled()) {
                    filterString = rt_ed_issrName.getText().toString().trim().toUpperCase();
                    if (filterString.isEmpty()) {
                        rt_sp_issrId.setEnabled(true);
                        rt_ed_bkId.setEnabled(true);
                        rt_ed_bkName.setEnabled(true);
                    } else {
                        rt_sp_issrId.setEnabled(false);
                        rt_sp_issrId.setSelection(0, true);
                        rt_ed_bkId.setEnabled(false);
                        rt_ed_bkName.setEnabled(false);
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject().put("key", "issrName");
                            jsonObject.put("value", filterString);
                            filterString = jsonObject.toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    all_issues_adapter.getFilter().filter(filterString);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void getAllNameIds() {
        namesHelper.getAllNamesContinuous(new BaseHelper.onCompleteRetrieval() {
            @Override
            public void onComplete(Object data) {
                List<String> nameIds = new ArrayList<>();
                if (data != null) {
                    List<Names> namesList = (List<Names>) data;
                    for (Names n : namesList) {
                        nameIds.add(String.valueOf(n.getSer_no()));
                    }
                }
                nameIds.add(0, getString(R.string.firstSpinnerItem));
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, nameIds);
                rt_sp_issrId.setAdapter(adapter);
            }
        });
    }

    private void getAllIssues() {
        issuesList = new ArrayList<>();
        issuesHelper.getAllIssuesContinuous(new BaseHelper.onCompleteRetrieval() {
            @Override
            public void onComplete(Object data) {
                if (data != null) {
                    for (Issues issue : (List<Issues>) data) {
                        if (issue.getIsReturned().contentEquals(getString(R.string.notReturned))) {
                            issuesList.add(issue);
                        }
                    }
                }
                all_issues_adapter = new All_Issues_Adapter(context, issuesList);
                all_issues_adapter.getFilter().filter(filterString);
                all_issues_adapter.registerAdapterDataObserver(adapterDataObserver);
                rt_rv_issList.setAdapter(all_issues_adapter);
                getAllNameIds();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.return_action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.act_done:
                submitSelectedReturns();
                break;

            case R.id.act_cancel:
                resetReturns();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetReturns() {
        issuesHelper.removeAllNamesListener();
        namesHelper.removeAllNamesListener();
        if (all_issues_adapter != null)
            all_issues_adapter.unregisterAdapterDataObserver(adapterDataObserver);
        getAllIssues();
    }

    private void submitSelectedReturns() {
        disableAll();
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        List<Issues> checkedIssues = new ArrayList<>();
        for (Issues issue : all_issues_adapter.getFilteredIssuesList()) {
            if (issue.isChecked()) {
                issue.setIsReturned(getString(R.string.hasReturned));
                issue.setRetDate(formatter.format(calendar.getTime()));
                checkedIssues.add(issue);
            }
        }
        issuesHelper.addReturnedIssues(checkedIssues, new BaseHelper.onCompleteTransaction() {
            @Override
            public void onComplete(boolean committed, Object data) {
                if (committed) {
                    enableAll();
                    resetReturns();
                } else {
                    Toast.makeText(context, "Some Error Occurred. Please try again.", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });
    }

    private void enableAll() {
        rt_sp_issrId.setEnabled(true);
        rt_ed_issrName.setEnabled(true);
        rt_ed_bkId.setEnabled(true);
        rt_ed_bkName.setEnabled(true);
    }

    private void disableAll() {
        rt_sp_issrId.setEnabled(false);
        rt_ed_issrName.setEnabled(false);
        rt_ed_bkId.setEnabled(false);
        rt_ed_bkName.setEnabled(false);
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
        namesHelper.removeAllNamesListener();
        if (all_issues_adapter != null)
            all_issues_adapter.unregisterAdapterDataObserver(adapterDataObserver);
    }
}