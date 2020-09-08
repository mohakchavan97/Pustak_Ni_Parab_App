package com.mohakchavan.pustakniparab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //    EditText ma_ed_serial;
    EditText ma_ed_fname, ma_ed_lname, ma_ed_blk, ma_ed_strt, ma_ed_area, ma_ed_call;
    TextView ma_tv_error;
    Button ma_btn_sub, ma_btn_all, ma_btn_reset;
    private DBHelper helper;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = MainActivity.this;
        ma_ed_area = findViewById(R.id.ac_main_ed_area);
        ma_ed_blk = findViewById(R.id.ac_main_ed_blk);
        ma_ed_call = findViewById(R.id.ac_main_ed_call);
        ma_ed_fname = findViewById(R.id.ac_main_ed_fname);
        ma_ed_lname = findViewById(R.id.ac_main_ed_lname);
//        ma_ed_serial = findViewById(R.id.ac_main_ed_serial);
        ma_ed_strt = findViewById(R.id.ac_main_ed_street);
        ma_btn_sub = findViewById(R.id.ac_main_btn_sub);
        ma_btn_reset = findViewById(R.id.ac_main_btn_reset);
//        ma_btn_all = findViewById(R.id.ac_main_btn_all);
        ma_tv_error = findViewById(R.id.ac_main_tv_error);

        helper = new DBHelper(MainActivity.this);
        ma_ed_fname.requestFocus();

//        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/Pustak_Ni_Parab/");
//        File f = new File(rootPath + "names.csv");
//        ma_tv_error.setVisibility(View.VISIBLE);
//        ma_tv_error.setText(Environment.getStorageState(f));

        int i = 0;
//        i = helper.getinsertedser();
//        if (i != 0) {
//        ma_ed_serial.setText(String.valueOf(i));
//            enableall();
//        ma_ed_serial.setFocusable(false);
//        } else {
//            ma_tv_error.setText("Some Error Occured");
//            ma_tv_error.setVisibility(View.VISIBLE);
//            disableall();
//        }


        ma_btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean state = true;
                String area = ma_ed_area.getText().toString(),
                        fname = ma_ed_fname.getText().toString(),
                        lname = ma_ed_lname.getText().toString(),
//                            ser = ma_ed_serial.getText().toString(),
                        blk = ma_ed_blk.getText().toString(),
                        call = ma_ed_call.getText().toString(),
                        strt = ma_ed_strt.getText().toString();
                if (area.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Locality/Area Empty", Toast.LENGTH_SHORT).show();
                    ma_ed_area.requestFocus();
                    state = false;
                } else if (fname.isEmpty()) {
                    Toast.makeText(MainActivity.this, "First Name Empty", Toast.LENGTH_SHORT).show();
                    ma_ed_fname.requestFocus();
                    state = false;
                } /*else if (ser.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Serial No Empty", Toast.LENGTH_SHORT).show();
                        ma_ed_serial.requestFocus();
                        state = false;
                    }*/ else if (strt.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Street Name Empty", Toast.LENGTH_SHORT).show();
                    ma_ed_strt.requestFocus();
                    state = false;
                }
                if (lname.isEmpty())
                    lname = "NULL";
                if (blk.isEmpty())
                    blk = "NULL";
                if (call.isEmpty())
                    call = "NULL";
                else if (!(call.length() == 10) || !(call.matches("^[6-9][0-9]{9}$"))) {
                    Toast.makeText(MainActivity.this, "Enter Proper 10 digit Contact No", Toast.LENGTH_SHORT).show();
                    ma_ed_call.requestFocus();
                    state = false;
                }


                if (state) {
                    if (helper.addName(fname.toUpperCase(), lname.toUpperCase(), blk.toUpperCase(), strt.toUpperCase(), area.toUpperCase(), call.toUpperCase())) {
                        Toast.makeText(MainActivity.this, "Record Inserted", Toast.LENGTH_SHORT).show();
                        resetAllFields();
//                            ma_ed_serial.setText(String.valueOf(helper.getinsertedser()));
                        new AlertDialog.Builder(MainActivity.this).setTitle("Serial No :\t" + String.valueOf(helper.getinsertedser())
                                + "\nFull Name :\t" + helper.getinsertedname()).setPositiveButton("OK", null).show();
                    } else
                        Toast.makeText(MainActivity.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                }
            }
        });


        ma_btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetAllFields();
            }
        });


//        ma_btn_all.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, View_All.class);
//                startActivity(intent);
//            }
//        });


    }

    private void resetAllFields() {
        ma_ed_area.setText(getString(R.string.defaultArea));
        ma_ed_fname.setText("");
        ma_ed_fname.requestFocus();
        ma_ed_lname.setText("");
//                            ma_ed_serial.setText("");
        ma_ed_blk.setText("");
        ma_ed_call.setText("");
        ma_ed_strt.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();

        if (item_id == R.id.menu_all) {
//            Toast.makeText(MainActivity.this, "Selected View All", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, View_All.class));
        }

        if (item_id == R.id.menu_del) {
//            Toast.makeText(MainActivity.this, "Selected Delete", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, Delete_Name.class));
        }

        if (item_id == R.id.menu_search) {
//            Toast.makeText(MainActivity.this, "Selected " + item.getTitle(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, Search_Name.class));
        }

        if (item_id == R.id.menu_export) {
            startService(new Intent(getApplicationContext(), Export_Service.class));
        }

        if (item_id == R.id.menu_import) {
            startService(new Intent(getApplicationContext(), Import_Service.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void enableall() {
//        ma_ed_serial.setFocusable(true);
        ma_ed_strt.setFocusable(true);
        ma_ed_lname.setFocusable(true);
        ma_ed_fname.setFocusable(true);
        ma_ed_call.setFocusable(true);
        ma_ed_blk.setFocusable(true);
        ma_ed_area.setFocusable(true);
        ma_btn_sub.setClickable(true);
        ma_btn_all.setClickable(true);
    }

    private void disableall() {
//        ma_ed_serial.setFocusable(false);
        ma_ed_strt.setFocusable(false);
        ma_ed_lname.setFocusable(false);
        ma_ed_fname.setFocusable(false);
        ma_ed_call.setFocusable(false);
        ma_ed_blk.setFocusable(false);
        ma_ed_area.setFocusable(false);
        ma_btn_sub.setClickable(false);
        ma_btn_all.setClickable(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
