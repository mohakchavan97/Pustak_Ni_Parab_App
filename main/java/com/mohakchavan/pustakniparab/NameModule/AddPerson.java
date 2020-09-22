package com.mohakchavan.pustakniparab.NameModule;

import android.app.Activity;
import android.content.DialogInterface;
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
import com.mohakchavan.pustakniparab.DBHelper;
import com.mohakchavan.pustakniparab.FireBaseHelper.BaseHelper;
import com.mohakchavan.pustakniparab.FireBaseHelper.BaseHelper.onCompleteTransaction;
import com.mohakchavan.pustakniparab.FireBaseHelper.NamesHelper;
import com.mohakchavan.pustakniparab.Models.Names;
import com.mohakchavan.pustakniparab.R;
import com.mohakchavan.pustakniparab.Services.Export_Service;
import com.mohakchavan.pustakniparab.Services.ImportFile_Service;
import com.mohakchavan.pustakniparab.Services.Network_Service;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class AddPerson extends AppCompatActivity {

    EditText ma_ed_fname, ma_ed_lname, ma_ed_blk, ma_ed_strt, ma_ed_area, ma_ed_call;
    private TextView ma_tv_error, ma_tv_id;
    Button ma_btn_sub, ma_btn_reset;
    private DBHelper helper;
    private NamesHelper namesHelper;
    public static Activity activity;
    private boolean isExportOrImport;
    private boolean viewMode;
    private Menu menu;
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

        ma_ed_area = findViewById(R.id.ac_main_ed_area);
        ma_ed_blk = findViewById(R.id.ac_main_ed_blk);
        ma_ed_call = findViewById(R.id.ac_main_ed_call);
        ma_ed_fname = findViewById(R.id.ac_main_ed_fname);
        ma_ed_lname = findViewById(R.id.ac_main_ed_lname);
        ma_ed_strt = findViewById(R.id.ac_main_ed_street);
        ma_btn_sub = findViewById(R.id.ac_main_btn_sub);
        ma_btn_reset = findViewById(R.id.ac_main_btn_reset);
//        ma_tv_error = findViewById(R.id.ac_main_tv_error);
        helper = new DBHelper(activity);
        namesHelper = new NamesHelper(activity);
        viewMode = getIntent().getBooleanExtra("view", false);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        if (!viewMode) {
            getSupportActionBar().setTitle(R.string.addPerson);
            ma_ed_fname.requestFocus();
            enableAll();
        } else {
            getSupportActionBar().setTitle(R.string.viewPerson);
            ma_tv_id = findViewById(R.id.ac_main_tv_id);
            findViewById(R.id.ac_main_ll_updt).setVisibility(View.VISIBLE);
            findViewById(R.id.ac_main_tv_header).setVisibility(View.GONE);
            findViewById(R.id.ac_main_ll_btns).setVisibility(View.GONE);
            disableAll();
            setNameFields(getIntent().getStringExtra("nameId"));
        }


//        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/Pustak_Ni_Parab/");
//        File f = new File(rootPath + "names.csv");
//        ma_tv_error.setVisibility(View.VISIBLE);
//        ma_tv_error.setText(Environment.getStorageState(f));


        ma_btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean state = true;
                String area = ma_ed_area.getText().toString().trim(),
                        fname = ma_ed_fname.getText().toString().trim(),
                        lname = ma_ed_lname.getText().toString().trim(),
//                            ser = ma_ed_serial.getText().toString(),
                        blk = ma_ed_blk.getText().toString().trim(),
                        call = ma_ed_call.getText().toString().trim(),
                        strt = ma_ed_strt.getText().toString().trim();
                if (area.isEmpty()) {
                    ma_ed_area.setError("Please Enter Locality/Area");
                    ma_ed_area.requestFocus();
                    state = false;
                } else if (fname.isEmpty()) {
                    ma_ed_fname.setError("Please Enter First Name");
                    ma_ed_fname.requestFocus();
                    state = false;
                } /*else if (ser.isEmpty()) {
            Toast.makeText(MainActivity.this, "Serial No Empty", Toast.LENGTH_SHORT).show();
            ma_ed_serial.requestFocus();
            state = false;
        }*/ else if (strt.isEmpty()) {
                    ma_ed_strt.setError("Please Enter Street Name");
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
                    ma_ed_call.setError("Enter Proper 10 digit Contact No");
                    ma_ed_call.requestFocus();
                    state = false;
                }

                if (state) {
                    Names names = new Names(fname.toUpperCase(), lname.toUpperCase(), blk.toUpperCase(), strt.toUpperCase(), area.toUpperCase(), call.toUpperCase());
                    if (!viewMode) {
                        submitNewPerson(names);
                    } else {
                        if (ma_tv_id.getText().toString().trim().isEmpty()) {
                            Toast.makeText(activity, "Some Error Occurred. Please try again.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            names.setSer_no(Long.parseLong(ma_tv_id.getText().toString()));
                            updatePerson(names);
                        }
                    }
                }
            }
        });


        ma_btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!viewMode)
                    resetAllFields();
                else
                    finish();
            }
        });

    }

    private void editMode() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.editName);
        enableAll();
        menu.findItem(R.id.act_edit).setVisible(false);
        menu.findItem(R.id.act_del).setVisible(false);
        findViewById(R.id.ac_main_ll_btns).setVisibility(View.VISIBLE);
        ma_btn_sub.setText("Update");
        ma_btn_reset.setText("Cancel");
    }

    private void updatePerson(Names updatedName) {
        disableAll();
        updatedName.setSer_no(Long.parseLong(Objects.requireNonNull(getIntent().getStringExtra("nameId"))));
        namesHelper.updatePerson(updatedName, new onCompleteTransaction() {
            @Override
            public void onComplete(boolean committed, Object data) {
                if (committed) {
                    Toast.makeText(activity, "Name details updated successfully.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(activity, "Some Error Occurred. Please try again.", Toast.LENGTH_SHORT).show();
                }
                enableAll();
            }
        });
    }

    private void setNameFields(String nameId) {
        if (nameId != null && !nameId.trim().isEmpty()) {
            namesHelper.getNameDetails(nameId, new BaseHelper.onCompleteRetrieval() {
                @Override
                public void onComplete(Object data) {
                    Names names = (Names) data;
                    ma_tv_id.setText(String.valueOf(names.getSer_no()));
                    ma_ed_area.setText(names.getLocalityOrArea());
                    ma_ed_blk.setText(names.getBlkOrFltNo());
                    ma_ed_call.setText(names.getContact());
                    ma_ed_fname.setText(names.getFirstName());
                    ma_ed_lname.setText(names.getLastName());
                    ma_ed_strt.setText(names.getStreetName());
                }
            });
        }
    }


    private void submitNewPerson(Names newName) {
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

        disableAll();

        namesHelper.addNewPerson(newName, new onCompleteTransaction() {
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
                enableAll();
            }
        });
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
        this.menu = menu;
//        if (!viewMode)
//            getMenuInflater().inflate(R.menu.menu, menu);
//        else
        if (viewMode)
            getMenuInflater().inflate(R.menu.name_edit_action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.act_edit:
                editMode();
                break;

            case R.id.act_del:
                new AlertDialog.Builder(activity)
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete this person's details?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                namesHelper.deleteName(ma_tv_id.getText().toString(), new BaseHelper.onDeletion() {
                                    @Override
                                    public void onDelete(boolean deleted) {
                                        Toast.makeText(activity, "Name details successfully deleted", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
