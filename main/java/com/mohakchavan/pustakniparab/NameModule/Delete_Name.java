package com.mohakchavan.pustakniparab.NameModule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mohakchavan.pustakniparab.DBHelper;
import com.mohakchavan.pustakniparab.Models.Names;
import com.mohakchavan.pustakniparab.R;
import com.mohakchavan.pustakniparab.Adapters.View_All_Adapter;

import java.util.List;

public class Delete_Name extends AppCompatActivity {

    RecyclerView recyclerView;
    Spinner spn_ser;
    TextView tv_name2, tv_ser2, tv_blk, tv_strt, tv_area, tv_call, tv_result;
    EditText ed_name;
    Button btn_submit, btn_cancel, btn_search_serial;
    List<String> ser_list;
    String serial_name;
    private DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_name);

        helper = new DBHelper(Delete_Name.this);
        spn_ser = findViewById(R.id.ac_dn_spn_ser);

//        tv_area = findViewById(R.id.ac_dn_tv_area);
//        tv_blk = findViewById(R.id.ac_dn_tv_blk);
//        tv_call = findViewById(R.id.ac_dn_tv_call);
//        tv_name2 = findViewById(R.id.ac_dn_tv_name2);
//        tv_ser2 = findViewById(R.id.ac_dn_tv_ser2);
//        tv_strt = findViewById(R.id.ac_dn_tv_strt);

        tv_result = findViewById(R.id.ac_dn_tv_search_result);
        btn_search_serial = findViewById(R.id.ac_dn_btn_search_ser);
        btn_submit = findViewById(R.id.ac_dn_btn_sub);
        btn_cancel = findViewById(R.id.ac_dn_btn_can);

        disableButtons();

        ser_list = helper.getAllSerials();
        ser_list.add(0, "No Serial Selected");

        if (ser_list.isEmpty()) {
//            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Delete_Name.this, R.array.serials, android.R.layout.simple_spinner_item);
//            ser_list.add("No Names Entered");
            Toast.makeText(Delete_Name.this, "No Names Entered", Toast.LENGTH_LONG).show();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Delete_Name.this, android.R.layout.simple_spinner_dropdown_item, ser_list);
        spn_ser.setAdapter(adapter);
        spn_ser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                serial_name = adapterView.getItemAtPosition(i).toString();
                disableButtons();
//                if (!(serial.contentEquals("No Serial Selected"))) {
//                    ed_name.setFocusable(false);
//                } else {
//                    ed_name.setFocusable(true);
//                }
//                tv_ser2.setText(serial);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        recyclerView = findViewById(R.id.ac_dn_rv_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Delete_Name.this));

        btn_search_serial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(serial_name.contentEquals("No Serial Selected"))) {

                    List<Names> namesList = helper.getSerialDetails(serial_name);
                    tv_result.setText("Serial Search Result");

                    View_All_Adapter view_all_adapter = new View_All_Adapter(Delete_Name.this, namesList);
                    recyclerView.setAdapter(view_all_adapter);

                    enableButtons();

//                    tv_area.setText(names.getArea());
//                    tv_blk.setText(names.getBlk());
//                    tv_call.setText(String.valueOf(names.getCall()));
//                    tv_name2.setText(new StringBuilder().append(names.getFname()).append(" ").append(names.getLname()).toString());
//                    tv_ser2.setText(String.valueOf(names.getSer_no()));
//                    tv_strt.setText(names.getStrt());


                } else {
                    Toast.makeText(Delete_Name.this, "Please Select Serial No", Toast.LENGTH_SHORT).show();
                    disableButtons();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final LayoutInflater inflater = LayoutInflater.from(Delete_Name.this);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(serial_name.contentEquals("No Serial Selected")))
                    new AlertDialog.Builder(Delete_Name.this).setTitle("Confirm Delete Serial No " + serial_name + " ?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //  new Intent
                                    if (helper.deleteName(serial_name)) {
                                        Toast.makeText(Delete_Name.this, "Name Deleted", Toast.LENGTH_LONG).show();
                                        onBackPressed();
                                    } else
                                        Toast.makeText(Delete_Name.this, "SOme Error Occured", Toast.LENGTH_LONG).show();
                                }
                            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //
//                            Toast.makeText(Delete_Name.this, "NO Selected", Toast.LENGTH_SHORT).show();
                        }
                    }).show();
                else {
                    Toast.makeText(Delete_Name.this, "Please Select Serial No", Toast.LENGTH_SHORT).show();
                    spn_ser.requestFocus();
                }
            }
        });

    }

    private void disableButtons() {
        btn_submit.setClickable(false);
        btn_submit.setVisibility(View.INVISIBLE);
        btn_cancel.setVisibility(View.INVISIBLE);
        btn_cancel.setClickable(false);
    }

    private void enableButtons() {
        btn_submit.setClickable(true);
        btn_submit.setVisibility(View.VISIBLE);
        btn_cancel.setVisibility(View.VISIBLE);
        btn_cancel.setClickable(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Delete_Name.this, AddPerson.class));
    }


}
