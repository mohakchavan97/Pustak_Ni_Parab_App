package com.mohakchavan.pustakniparab;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Search_Name extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btn_search_name, btn_ok;
    EditText ed_name;
    TextView tv_result;
    String serial_name;
    DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_name);

        btn_search_name = findViewById(R.id.ac_sn_btn_search_name);
        btn_ok = findViewById(R.id.ac_sn_btn_ok);
        ed_name = findViewById(R.id.ac_sn_ed_name);
        tv_result = findViewById(R.id.ac_sn_tv_search_result);
        helper = new DBHelper(Search_Name.this);

        recyclerView = findViewById(R.id.ac_sn_rv_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Search_Name.this));

        btn_ok.setVisibility(View.INVISIBLE);
        btn_ok.setClickable(false);
        ed_name.requestFocus();

        btn_search_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serial_name = ed_name.getText().toString().trim().toUpperCase();
                if (!(serial_name.isEmpty())) {
                    List<Names> namesList = helper.getNameDetails(serial_name);
                    if (!(namesList.isEmpty())) {
                        tv_result.setTextColor(Color.parseColor("#008577"));
                        tv_result.setText("Name Search Result");
                        View_All_Adapter view_all_adapter = new View_All_Adapter(Search_Name.this, namesList);
                        recyclerView.setAdapter(view_all_adapter);
                        btn_ok.setVisibility(View.VISIBLE);
                        btn_ok.setClickable(true);
                    } else {
                        tv_result.setText("NO record found");
                        tv_result.setTextColor(Color.RED);
                        ed_name.setText("");
                        ed_name.requestFocus();
                        btn_ok.setVisibility(View.INVISIBLE);
                        btn_ok.setClickable(false);
                    }

                } else {
                    Toast.makeText(Search_Name.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                    ed_name.requestFocus();
                }
            }
        });

//        if (btn_ok.isClickable())
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Search_Name.this, MainActivity.class));
    }
}
