package com.mohakchavan.pustakniparab.NameModule;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mohakchavan.pustakniparab.Adapters.View_All_Adapter;
import com.mohakchavan.pustakniparab.DBHelper;
import com.mohakchavan.pustakniparab.FireBaseHelper.BaseHelper;
import com.mohakchavan.pustakniparab.FireBaseHelper.NamesHelper;
import com.mohakchavan.pustakniparab.Models.Names;
import com.mohakchavan.pustakniparab.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Search_Name extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText ed_name;
    TextView tv_result;
    Spinner sp_id;
    String serial_name, filterString;
    DBHelper helper;
    private boolean doNotChangeOnce;
    private NamesHelper namesHelper;
    private Activity context;
    private List<Names> namesList;
    private View_All_Adapter view_all_adapter;
    private RecyclerView.AdapterDataObserver adapterDataObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_name);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.searchName);
        context = Search_Name.this;
        ed_name = findViewById(R.id.ac_sn_ed_name);
        tv_result = findViewById(R.id.ac_sn_tv_search_result);
        sp_id = findViewById(R.id.ac_sn_sp_id);
        helper = new DBHelper(Search_Name.this);
        namesHelper = new NamesHelper(context);
        filterString = "";
        doNotChangeOnce = false;

        recyclerView = findViewById(R.id.ac_sn_rv_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        ed_name.requestFocus();
        getAllNames();

        sp_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!doNotChangeOnce) {
                    filterString = parent.getItemAtPosition(position).toString().trim();
                    if (filterString.contentEquals("Select Name ID")) {
                        view_all_adapter.getFilter().filter("");
                    } else {
                        view_all_adapter.getFilter().filter(filterString);
                    }
                } else {
                    doNotChangeOnce = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ed_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterString = ed_name.getText().toString().trim();
                if (filterString.isEmpty()) {
                    sp_id.setEnabled(true);
                    doNotChangeOnce = false;
                } else {
                    doNotChangeOnce = true;
                    sp_id.setEnabled(false);
                    sp_id.setSelection(0, true);
                }
                view_all_adapter.getFilter().filter(filterString);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void getAllNames() {
        namesHelper.getAllNamesContinuous(new BaseHelper.onCompleteRetrieval() {
            @Override
            public void onComplete(Object data) {
                List<String> nameIds = new ArrayList<>();
                if (data != null)
                    namesList = (List<Names>) data;
                for (Names n : namesList) {
                    nameIds.add(String.valueOf(n.getSer_no()));
                }
                nameIds.add(0, "Select Name ID");

                // Populate the spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, nameIds);
                sp_id.setAdapter(adapter);

                // Populate the Recycler View
                view_all_adapter = new View_All_Adapter(context, namesList);
                view_all_adapter.getFilter().filter(filterString);
                adapterDataObserver = new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        if (view_all_adapter.getItemCount() > 0) {
                            tv_result.setText("Results");
                            tv_result.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                        } else {
                            tv_result.setText(getText(R.string.noRecordsFound).subSequence(0, getText(R.string.noRecordsFound).length() - 1));
                            tv_result.setTextColor(ContextCompat.getColor(context, R.color.Red));
                        }
                    }
                };
                view_all_adapter.registerAdapterDataObserver(adapterDataObserver);
                recyclerView.setAdapter(view_all_adapter);
            }
        });
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
        view_all_adapter.unregisterAdapterDataObserver(adapterDataObserver);
    }
}
