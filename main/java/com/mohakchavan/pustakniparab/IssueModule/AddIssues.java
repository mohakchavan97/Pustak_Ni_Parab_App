package com.mohakchavan.pustakniparab.IssueModule;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.mohakchavan.pustakniparab.FireBaseHelper.BaseHelper;
import com.mohakchavan.pustakniparab.FireBaseHelper.IssuesHelper;
import com.mohakchavan.pustakniparab.FireBaseHelper.NamesHelper;
import com.mohakchavan.pustakniparab.Models.Issues;
import com.mohakchavan.pustakniparab.Models.Names;
import com.mohakchavan.pustakniparab.R;
import com.mohakchavan.pustakniparab.Services.Network_Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddIssues extends AppCompatActivity {

    EditText is_ed_bookName, is_ed_bookPrice, is_ed_authPub, is_ed_issrName, is_ed_issrAddr, is_ed_issrCont;
    Spinner is_sp_nameId;
    TextView is_tv_issDate, is_tv_nameId;
    Button is_btn_sub, is_btn_reset;
    private Activity context;
    private IssuesHelper issuesHelper;
    private NamesHelper namesHelper;
    List<Names> namesList;

    @Override
    protected void onResume() {
        super.onResume();
        Network_Service.checkInternetToProceed(AddIssues.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_issues);

        context = AddIssues.this;
        is_ed_bookName = findViewById(R.id.is_ed_bookName);
        is_ed_bookPrice = findViewById(R.id.is_ed_bookPrice);
        is_ed_authPub = findViewById(R.id.is_ed_authPub);
        is_sp_nameId = findViewById(R.id.is_sp_nameId);
        is_ed_issrName = findViewById(R.id.is_ed_issrName);
        is_ed_issrAddr = findViewById(R.id.is_ed_issrAddr);
        is_ed_issrCont = findViewById(R.id.is_ed_issrCont);
        is_tv_issDate = findViewById(R.id.is_tv_issDate);
        is_tv_nameId = findViewById(R.id.is_tv_nameId);
        is_btn_sub = findViewById(R.id.is_btn_sub);
        is_btn_reset = findViewById(R.id.is_btn_reset);
        namesHelper = new NamesHelper(context);
        issuesHelper = new IssuesHelper(context);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.issues);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        is_tv_issDate.setText(formatter.format(calendar.getTime()));
        is_tv_issDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar cal = Calendar.getInstance();
                                cal.set(year, month, dayOfMonth);
                                is_tv_issDate.setText(formatter.format(cal.getTime()));
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        populateNameIds();

        is_sp_nameId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedId = parent.getItemAtPosition(position).toString();
                if (!selectedId.contentEquals("Select Name ID")) {
                    setNameDetails(selectedId);
                    disableNameFields();
                } else if (selectedId.contentEquals("Select Name ID")) {
                    is_tv_nameId.setText("");
                    clearNameFields();
                    disableNameFields();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        is_btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllFields();
                is_tv_issDate.setText(formatter.format(calendar.getTime()));
            }
        });

        is_btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bkName = is_ed_bookName.getText().toString(),
                        bkPrice = is_ed_bookPrice.getText().toString(),
                        bkAuth = is_ed_authPub.getText().toString(),
                        isrId = is_tv_nameId.getText().toString(),
                        isrName = is_ed_issrName.getText().toString(),
                        isrAdr = is_ed_issrAddr.getText().toString(),
                        isrCont = is_ed_issrCont.getText().toString(),
                        isrDate = is_tv_issDate.getText().toString();
                if (bkName.isEmpty()) {
                    is_ed_bookName.setError("Please Enter Book Name");
                    is_ed_bookName.requestFocus();
                } else if (isrId.isEmpty()) {
                    Toast.makeText(context, "Please select proper Id", Toast.LENGTH_SHORT).show();
                    is_sp_nameId.requestFocus();
                } else if (isrName.isEmpty()) {
                    is_ed_issrName.setError("Please Enter Issuer Name");
                    is_ed_issrName.requestFocus();
                } else if (isrAdr.isEmpty()) {
                    is_ed_issrAddr.setError("Please Enter Issuer Address");
                    is_ed_issrAddr.requestFocus();
                } else if (isrDate.isEmpty()) {
                    Toast.makeText(context, "Please set Issue Date.", Toast.LENGTH_SHORT).show();
                } else if (!isrCont.isEmpty() && (isrCont.length() != 10 || !(isrCont.matches("^[6-9][0-9]{9}$")))) {
                    is_ed_issrCont.setError("Enter Proper 10 digit Contact No");
                    is_ed_issrCont.requestFocus();
                } else {
                    if (isrCont.isEmpty()) {
                        isrCont = "";
                    }
                    if (bkPrice.isEmpty()) {
                        bkPrice = "";
                    }
                    if (bkAuth.isEmpty()) {
                        bkAuth = "";
                    }
                    disableAllFields();
                    issuesHelper.addNewIssue(new Issues(bkName, bkPrice, bkAuth, isrId, isrName, isrAdr, isrCont, isrDate, getString(R.string.notReturned), ""),
                            new BaseHelper.onCompleteTransaction() {
                                @Override
                                public void onComplete(boolean committed, Object data) {
                                    if (committed) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                                                .setTitle("Assigned Issue ID:\t" + ((Issues) data).getIssueNo())
                                                .setPositiveButton("OK", null)
                                                .setCancelable(false);
                                        AlertDialog dialog = builder.create();
                                        dialog.setCanceledOnTouchOutside(false);
                                        dialog.show();
                                        resetAllFields();
                                        is_tv_issDate.setText(formatter.format(calendar.getTime()));
                                        Toast.makeText(context, "Issue added successfully.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Some Error Occurred. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                    enableAllFields();
                                }
                            });
                }
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

    private void populateNameIds() {
        namesHelper.getAllNamesOnce(new BaseHelper.onCompleteRetrieval() {
            @Override
            public void onComplete(Object data) {
                List<String> nameIds = new ArrayList<>();
                if (data != null)
                    namesList = (List<Names>) data;
                for (Names n : namesList) {
                    nameIds.add(String.valueOf(n.getSer_no()));
                }
                nameIds.add(0, "Select Name ID");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, nameIds);
                is_sp_nameId.setAdapter(adapter);
            }
        });
    }

    private void resetAllFields() {
        clearBookFields();
        clearNameFields();
        enableNameFields();
        populateNameIds();
    }

    private void clearBookFields() {
        is_ed_bookName.setText("");
        is_ed_bookPrice.setText("");
        is_ed_authPub.setText("");
    }

    private void clearNameFields() {
        is_ed_issrName.setText("");
        is_ed_issrAddr.setText("");
        is_ed_issrCont.setText("");
        is_tv_nameId.setText("");
    }

    private void disableNameFields() {
        is_ed_issrName.setEnabled(false);
        is_ed_issrAddr.setEnabled(false);
        is_ed_issrCont.setEnabled(false);
    }

    private void enableNameFields() {
        is_ed_issrName.setEnabled(true);
        is_ed_issrAddr.setEnabled(true);
        is_ed_issrCont.setEnabled(true);
    }

    private void enableAllFields() {
        is_ed_bookName.setEnabled(true);
        is_ed_bookPrice.setEnabled(true);
        is_ed_authPub.setEnabled(true);
        is_btn_sub.setEnabled(true);
        is_btn_reset.setEnabled(true);
        if (is_tv_nameId.getText().toString().contentEquals("Other")) {
            enableNameFields();
        }
    }

    private void disableAllFields() {
        is_ed_bookName.setEnabled(false);
        is_ed_bookPrice.setEnabled(false);
        is_ed_authPub.setEnabled(false);
        is_btn_sub.setEnabled(false);
        is_btn_reset.setEnabled(false);
        disableNameFields();
    }

    private void setNameDetails(String nameId) {
        is_tv_nameId.setText(nameId);
        Names name = new Names();
        for (Names n : namesList) {
            if (String.valueOf(n.getSer_no()).contentEquals(nameId)) {
                name = n;
                break;
            }
        }
        is_ed_issrName.setText(name.returnFullName());
        is_ed_issrAddr.setText(name.returnFullAddress());
        is_ed_issrCont.setText(name.getContact());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}