package com.mohakchavan.pustakniparab;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mohakchavan.pustakniparab.FireBaseHelper.BaseHelper.onCompleteTransaction;
import com.mohakchavan.pustakniparab.FireBaseHelper.NamesHelper;
import com.mohakchavan.pustakniparab.Models.Names;
import com.mohakchavan.pustakniparab.Services.Export_Service;
import com.mohakchavan.pustakniparab.Services.ImportFile_Service;
import com.mohakchavan.pustakniparab.Services.Network_Service;

import org.json.JSONException;
import org.json.JSONObject;

public class AddPerson extends AppCompatActivity {

    //    EditText ma_ed_serial;
    EditText ma_ed_fname, ma_ed_lname, ma_ed_blk, ma_ed_strt, ma_ed_area, ma_ed_call;
    TextView ma_tv_error;
    Button ma_btn_sub, ma_btn_all, ma_btn_reset;
    private DBHelper helper;
    private NamesHelper namesHelper;
    public static Activity activity;
    private boolean isExportOrImport;
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference childRef = rootRef.child("number");

    @Override
    protected void onResume() {
        super.onResume();
        Network_Service.checkInternetToProceed(AddPerson.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        activity = AddPerson.this;
        getSupportActionBar().setTitle(R.string.addPerson);
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

        helper = new DBHelper(activity);
        namesHelper = new NamesHelper(activity);
        ma_ed_fname.requestFocus();

        enableAll();

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
                disableAll();
                String area = ma_ed_area.getText().toString(),
                        fname = ma_ed_fname.getText().toString(),
                        lname = ma_ed_lname.getText().toString(),
//                            ser = ma_ed_serial.getText().toString(),
                        blk = ma_ed_blk.getText().toString(),
                        call = ma_ed_call.getText().toString(),
                        strt = ma_ed_strt.getText().toString();
                if (area.isEmpty()) {
                    Toast.makeText(AddPerson.this, "Locality/Area Empty", Toast.LENGTH_SHORT).show();
                    ma_ed_area.requestFocus();
                    state = false;
                } else if (fname.isEmpty()) {
                    Toast.makeText(AddPerson.this, "First Name Empty", Toast.LENGTH_SHORT).show();
                    ma_ed_fname.requestFocus();
                    state = false;
                } /*else if (ser.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Serial No Empty", Toast.LENGTH_SHORT).show();
                        ma_ed_serial.requestFocus();
                        state = false;
                    }*/ else if (strt.isEmpty()) {
                    Toast.makeText(AddPerson.this, "Street Name Empty", Toast.LENGTH_SHORT).show();
                    ma_ed_strt.requestFocus();
                    state = false;
                }
                if (lname.isEmpty())
//                    lname = "NULL";
                    lname = "";
                if (blk.isEmpty())
//                    blk = "NULL";
                    blk = "";
                if (call.isEmpty())
//                    call = "NULL";
                    call = "";
                else if (!(call.length() == 10) || !(call.matches("^[6-9][0-9]{9}$"))) {
                    Toast.makeText(AddPerson.this, "Enter Proper 10 digit Contact No", Toast.LENGTH_SHORT).show();
                    ma_ed_call.requestFocus();
                    state = false;
                }


                if (state) {

                    //region Code to add data to Local DB (currently commented)
                    /*if (helper.addName(fname.toUpperCase(), lname.toUpperCase(), blk.toUpperCase(), strt.toUpperCase(), area.toUpperCase(), call.toUpperCase())) {
                        Toast.makeText(AddPerson.this, "Record Inserted in DB", Toast.LENGTH_SHORT).show();
                        resetAllFields();
//                            ma_ed_serial.setText(String.valueOf(helper.getinsertedser()));
                        new AlertDialog.Builder(AddPerson.this).setTitle("Serial No :\t" + String.valueOf(helper.getinsertedser())
                                + "\nFull Name :\t" + helper.getinsertedname()).setPositiveButton("OK", null).show();
                    } else {
                        Toast.makeText(AddPerson.this, "Some Error Occured in DB", Toast.LENGTH_SHORT).show();
                    }*/
                    //endregion

                    Toast.makeText(activity, "Inserting Data...", Toast.LENGTH_SHORT).show();
                    namesHelper.addNewPerson(new Names(fname.toUpperCase(), lname.toUpperCase(), blk.toUpperCase(), strt.toUpperCase(), area.toUpperCase(), call.toUpperCase()), new onCompleteTransaction() {
                        @Override
                        public void onComplete(boolean committed, Object data) {
                            if (committed) {
                                resetAllFields();
                                new AlertDialog.Builder(activity)
                                        .setTitle("Serial No :\t" + ((Names) data).getSer_no() + "\nFull Name :\t" + ((Names) data).getFirstName() + " " + ((Names) data).getLastName())
                                        .setPositiveButton("OK", null).show();
                                Toast.makeText(activity, "Person Added Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, "Some Error Occurred. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                enableAll();
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
        enableAll();
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
        switch (item.getItemId()) {

            case R.id.menu_all:
//            Toast.makeText(MainActivity.this, "Selected View All", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddPerson.this, View_All.class));
                break;

            case R.id.menu_del:
//            Toast.makeText(MainActivity.this, "Selected Delete", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddPerson.this, Delete_Name.class));
                break;

            case R.id.menu_search:
//            Toast.makeText(MainActivity.this, "Selected " + item.getTitle(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddPerson.this, Search_Name.class));
                break;

            //region Extra temporary code (commented)
            /*case R.id.menu_export:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, getResources().getInteger(R.integer.WritePermissionRequestCode));
                } else {
                    startExporting();
                }
                break;

            case R.id.menu_import:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, getResources().getInteger(R.integer.ReadPermissionRequestCode));
                } else {
                    startImporting();
                }
                break;

            case R.id.menu_net:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, getResources().getInteger(R.integer.InternetRequestCode));
                } else {
//                    testInternet();
//                    readDatabase();
                    writeDatabase();
                }
                break;*/
            //endregion
        }
        return super.onOptionsItemSelected(item);
    }

    private void writeDatabase() {
        childRef.setValue(5555);
    }

    private void readDatabase() {
        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int val = snapshot.getValue(Integer.class);
                Toast.makeText(AddPerson.this, "Value: " + val, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddPerson.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void testInternet() {
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://jsonplaceholder.typicode.com/todos/1",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(AddPerson.this, "Got Response", Toast.LENGTH_SHORT).show();
                        try {
                            Toast.makeText(AddPerson.this, "Response is: " + response.getString("title"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddPerson.this, "Some Error Occured.", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void startExporting() {
        isExportOrImport = true;
        startService(new Intent(getApplicationContext(), Export_Service.class));
    }

    private void startImporting() {
        isExportOrImport = true;
//            startService(new Intent(getApplicationContext(), Import_Service.class));
        startService(new Intent(getApplicationContext(), ImportFile_Service.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == getResources().getInteger(R.integer.WritePermissionRequestCode)) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startExporting();
            } else {
                Toast.makeText(activity, "Please grant the permission to access the storage.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == getResources().getInteger(R.integer.ReadPermissionRequestCode)) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startImporting();
            } else {
                Toast.makeText(activity, "Please grant the permission to access the storage.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == getResources().getInteger(R.integer.InternetRequestCode)) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                testInternet();
            } else {
                Toast.makeText(activity, "Please grant the permission to access the storage.", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == getResources().getInteger(R.integer.ImportRequestCode) && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                Toast.makeText(activity, "Uri: " + uri, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void enableAll() {
//        ma_ed_serial.setFocusable(true);
        ma_ed_strt.setEnabled(true);
        ma_ed_lname.setEnabled(true);
        ma_ed_fname.setEnabled(true);
        ma_ed_call.setEnabled(true);
        ma_ed_blk.setEnabled(true);
        ma_ed_area.setEnabled(true);
        ma_btn_sub.setEnabled(true);
        ma_btn_reset.setEnabled(true);
    }

    private void disableAll() {
//        ma_ed_serial.setFocusable(false);
        ma_ed_strt.setEnabled(false);
        ma_ed_lname.setEnabled(false);
        ma_ed_fname.setEnabled(false);
        ma_ed_call.setEnabled(false);
        ma_ed_blk.setEnabled(false);
        ma_ed_area.setEnabled(false);
        ma_btn_sub.setEnabled(false);
        ma_btn_reset.setEnabled(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isExportOrImport)
            finish();
    }
}
