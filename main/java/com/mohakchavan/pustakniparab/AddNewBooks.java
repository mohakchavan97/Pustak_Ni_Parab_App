package com.mohakchavan.pustakniparab;

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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mohakchavan.pustakniparab.Helpers.FireBaseHelper.BaseHelper;
import com.mohakchavan.pustakniparab.Helpers.FireBaseHelper.NewBooksHelper;
import com.mohakchavan.pustakniparab.Models.NewBooks;
import com.mohakchavan.pustakniparab.Services.ProgressBarService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddNewBooks extends AppCompatActivity {

    private Activity context;
    private Button nb_btn_sub, nb_btn_reset;
    private TextView nb_tv_newBookDate;
    private EditText nb_ed_totBooks, nb_ed_perName, nb_ed_bookLang;
    private Spinner nb_sp_bookLang;
    private LinearLayout nb_ll_lang;
    private NewBooksHelper newBooksHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_books);

        context = AddNewBooks.this;
        nb_btn_sub = findViewById(R.id.nb_btn_sub);
        nb_btn_reset = findViewById(R.id.nb_btn_reset);
        nb_tv_newBookDate = findViewById(R.id.nb_tv_newBookDate);
        nb_ed_totBooks = findViewById(R.id.nb_ed_totBooks);
        nb_ed_perName = findViewById(R.id.nb_ed_perName);
        nb_sp_bookLang = findViewById(R.id.nb_sp_bookLang);
        nb_ed_bookLang = findViewById(R.id.nb_ed_bookLang);
        nb_ll_lang = findViewById(R.id.nb_ll_lang);
        newBooksHelper = new NewBooksHelper(context);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.addNewBooks);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.dateFormat), Locale.ENGLISH);
        final DatePickerDialog dateDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, month, dayOfMonth);
                        nb_tv_newBookDate.setText(formatter.format(cal.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        nb_tv_newBookDate.setText(formatter.format(calendar.getTime()));
        nb_tv_newBookDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                dateDialog.show();
            }
        });

        populateLangIds();
        nb_sp_bookLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                if (selected.contentEquals("Other")) {
                    nb_ed_bookLang.setText("");
                    nb_ll_lang.setVisibility(View.VISIBLE);
                } else if (selected.contentEquals(getString(R.string.firstLangItem))) {
                    nb_ed_bookLang.setText("");
                    nb_ll_lang.setVisibility(View.GONE);
                } else {
                    nb_ed_bookLang.setText(selected);
                    nb_ll_lang.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nb_btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllFields();
                nb_tv_newBookDate.setText(formatter.format(calendar.getTime()));
            }
        });

        nb_btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String perName = nb_ed_perName.getText().toString().trim(),
                        totBooks = nb_ed_totBooks.getText().toString().trim(),
                        bookLang = nb_ed_bookLang.getText().toString().trim(),
                        bookDate = nb_tv_newBookDate.getText().toString().trim();

                if (perName.isEmpty()) {
                    nb_ed_perName.setError("Please Enter Person Name");
                    nb_ed_perName.requestFocus();
                } else if (totBooks.isEmpty()) {
                    nb_ed_totBooks.setError("Please Enter Total Books");
                    nb_ed_totBooks.requestFocus();
                } else if (bookLang.isEmpty()) {
                    Toast.makeText(context, "Please select proper Language or specify other language.", Toast.LENGTH_SHORT).show();
                    nb_sp_bookLang.requestFocus();
                } else if (bookDate.isEmpty()) {
                    Toast.makeText(context, "Please set proper Date.", Toast.LENGTH_SHORT).show();
                } else {
                    disableAllFields();
                    final ProgressBarService progressBarService = new ProgressBarService("Adding Books...");
                    progressBarService.show(getSupportFragmentManager(), "Progress Bar Dialog");
                    newBooksHelper.addNewRecord(new NewBooks(perName.toUpperCase(), totBooks.toUpperCase(), bookLang.toUpperCase(), bookDate.toUpperCase()),
                            new BaseHelper.onCompleteTransaction() {
                                @Override
                                public void onComplete(boolean committed, Object data) {
                                    progressBarService.dismiss();
                                    if (committed) {
                                        resetAllFields();
                                        nb_tv_newBookDate.setText(formatter.format(calendar.getTime()));
                                        Toast.makeText(context, "Record added successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, getString(R.string.someError), Toast.LENGTH_SHORT).show();
                                    }
                                    enableAllFields();
                                }
                            });
                }
            }
        });
    }

    private void resetAllFields() {
        nb_ed_perName.setText("");
        nb_ed_totBooks.setText("");
        nb_ed_bookLang.setText("");
        populateLangIds();
    }

    private void disableAllFields() {
        nb_ed_perName.setEnabled(false);
        nb_ed_totBooks.setEnabled(false);
        nb_ed_bookLang.setEnabled(false);
        nb_sp_bookLang.setEnabled(false);
        nb_tv_newBookDate.setEnabled(false);
        nb_btn_sub.setEnabled(false);
        nb_btn_reset.setEnabled(false);
    }

    private void enableAllFields() {
        nb_ed_perName.setEnabled(true);
        nb_ed_totBooks.setEnabled(true);
        nb_ed_bookLang.setEnabled(true);
        nb_sp_bookLang.setEnabled(true);
        nb_tv_newBookDate.setEnabled(true);
        nb_btn_sub.setEnabled(true);
        nb_btn_reset.setEnabled(true);
    }

    private void populateLangIds() {
        List<String> langs = new ArrayList<>();
        Collections.addAll(langs, getResources().getStringArray(R.array.languages));
        langs.add(0, getString(R.string.firstLangItem));
        nb_sp_bookLang.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, langs));
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
}