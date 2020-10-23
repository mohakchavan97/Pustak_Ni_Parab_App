package com.mohakchavan.pustakniparab.IssueModule;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mohakchavan.pustakniparab.Adapters.All_Issues_Adapter;
import com.mohakchavan.pustakniparab.Helpers.FireBaseHelper.BaseHelper;
import com.mohakchavan.pustakniparab.Helpers.FireBaseHelper.IssuesHelper;
import com.mohakchavan.pustakniparab.Helpers.FireBaseHelper.NamesHelper;
import com.mohakchavan.pustakniparab.Models.Issues;
import com.mohakchavan.pustakniparab.Models.Names;
import com.mohakchavan.pustakniparab.R;
import com.mohakchavan.pustakniparab.Services.ProgressBarService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
    private Menu menu;
    private boolean isAdapterDataObserverRegistered;
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
        isAdapterDataObserverRegistered = false;

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
                    if (filterString.contentEquals(getString(R.string.firstNameItem))) {
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

    private void getAllNameIds(final ProgressBarService progressBarService) {
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
                nameIds.add(0, getString(R.string.firstNameItem));
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, nameIds);
                rt_sp_issrId.setAdapter(adapter);
                progressBarService.dismiss();
            }
        });
    }

    private void getAllIssues() {
        issuesList = new ArrayList<>();
        final ProgressBarService progressBarService = new ProgressBarService("Retrieving Data...");
        progressBarService.show(getSupportFragmentManager(), "Progress Bar Dialog");
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
                isAdapterDataObserverRegistered = false;
                all_issues_adapter = new All_Issues_Adapter(context, issuesList);
                all_issues_adapter.getFilter().filter(filterString);
                all_issues_adapter.registerAdapterDataObserver(adapterDataObserver);
                all_issues_adapter.setListener(new All_Issues_Adapter.ChangeListener() {
                    @Override
                    public void onChange() {
                        boolean toShow = false;
                        for (Issues issue : all_issues_adapter.getFilteredIssuesList()) {
                            if (issue.isChecked()) {
                                toShow = true;
                                break;
                            }
                        }
                        hideMenuItems(toShow);
                    }
                });
                isAdapterDataObserverRegistered = true;
                rt_rv_issList.setAdapter(all_issues_adapter);
                getAllNameIds(progressBarService);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.return_action_menu, menu);
        this.menu = menu;
        hideMenuItems(false);
        return true;
    }

    private void hideMenuItems(boolean isVisible) {
        menu.findItem(R.id.act_done).setVisible(isVisible);
        menu.findItem(R.id.act_done).setEnabled(isVisible);
        menu.findItem(R.id.act_cancel).setVisible(isVisible);
        menu.findItem(R.id.act_cancel).setEnabled(isVisible);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.act_done:
                try {
                    submitSelectedReturns();
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                    Toast.makeText(context, getString(R.string.someError), Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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
        hideMenuItems(false);
        getAllIssues();
    }

    private void submitSelectedReturns() throws Exception {
        disableAll();
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.dateFormat), Locale.ENGLISH);
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_date_picker, null);
        final TextView cdp_tv_date = view.findViewById(R.id.cdp_tv_date);

        final List<String> dates = new ArrayList<>();
        for (Issues issue : all_issues_adapter.getFilteredIssuesList()) {
            if (issue.isChecked() && !dates.contains(issue.getIssueDate())) {
                dates.add(issue.getIssueDate());
            }
        }
        Collections.sort(dates, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                try {
                    return formatter.parse(o1).compareTo(formatter.parse(o2));
                } catch (ParseException ex) {
                    throw new IllegalArgumentException(ex);
                }
            }
        });

        cdp_tv_date.setText(dates.get(0));
        try {
            calendar.setTimeInMillis(formatter.parse(dates.get(0)).getTime() + 1000);
        } catch (ParseException ex) {
            throw new IllegalArgumentException(ex);
        }
        final DatePickerDialog dateDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, month, dayOfMonth);
                        cdp_tv_date.setText(formatter.format(cal.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        cdp_tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dateDialog.getDatePicker().setMinDate(formatter.parse(dates.get(0)).getTime() + 1000);
                } catch (ParseException ex) {
                    throw new IllegalArgumentException(ex);
                }
                dateDialog.show();
            }
        });
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setTitle("Select Return Date")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enableAll();
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        List<Issues> checkedIssues = new ArrayList<>();
                        for (Issues issue : all_issues_adapter.getFilteredIssuesList()) {
                            if (issue.isChecked()) {
                                issue.setIsReturned(getString(R.string.hasReturned));
                                issue.setRetDate(cdp_tv_date.getText().toString().trim().toUpperCase());
                                checkedIssues.add(issue);
                            }
                        }
                        final ProgressBarService progressBarService = new ProgressBarService("Returning Issues...");
                        progressBarService.show(getSupportFragmentManager(), "Progress Bar Dialog");
                        issuesHelper.addReturnedIssues(checkedIssues, new BaseHelper.onCompleteTransaction() {
                            @Override
                            public void onComplete(boolean committed, Object data) {
                                progressBarService.dismiss();
                                if (committed) {
                                    enableAll();
                                    resetReturns();
                                } else {
                                    Toast.makeText(context, getString(R.string.someError), Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        });
                    }
                }).setCancelable(false)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
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
        if (isAdapterDataObserverRegistered)
            all_issues_adapter.unregisterAdapterDataObserver(adapterDataObserver);
    }
}