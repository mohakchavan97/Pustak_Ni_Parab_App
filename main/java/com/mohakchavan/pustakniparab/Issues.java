package com.mohakchavan.pustakniparab;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mohakchavan.pustakniparab.Services.Network_Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Issues extends AppCompatActivity {

    EditText is_ed_bookName, is_ed_bookPrice, is_ed_authPub, is_ed_issrName, is_ed_issrAddr, is_ed_issrCont;
    Spinner is_sp_nameId;
    TextView is_tv_issDate;
    private Activity context;

    @Override
    protected void onResume() {
        super.onResume();
        Network_Service.checkInternetToProceed(Issues.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);

        context = Issues.this;
        is_ed_bookName = findViewById(R.id.is_ed_bookName);
        is_ed_bookPrice = findViewById(R.id.is_ed_bookPrice);
        is_ed_authPub = findViewById(R.id.is_ed_authPub);
        is_sp_nameId = findViewById(R.id.is_sp_nameId);
        is_ed_issrName = findViewById(R.id.is_ed_issrName);
        is_ed_issrAddr = findViewById(R.id.is_ed_issrAddr);
        is_ed_issrCont = findViewById(R.id.is_ed_issrCont);
        is_tv_issDate = findViewById(R.id.is_tv_issDate);

        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
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


    }
}