package com.mohakchavan.pustakniparab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class View_All extends AppCompatActivity {

    RecyclerView va_viewall;
    View_All_Adapter adapter;
    TextView tv_state;
    List<Names> namesList;
    private DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

//        namesList=new ArrayList<>();
        helper = new DBHelper(View_All.this);
        tv_state = findViewById(R.id.ac_va_tv_state);

        va_viewall = findViewById(R.id.ac_va_rv_viewall);
        va_viewall.setHasFixedSize(true);
        va_viewall.setLayoutManager(new LinearLayoutManager(View_All.this));
//        va_viewall.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                Toast.makeText(View_All.this, "Long Clicked", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });

        namesList = helper.getAllNames();
        if (namesList.isEmpty()) {
            tv_state.setText("No Records Inserted");
        } else {
            adapter = new View_All_Adapter(View_All.this, namesList);
            va_viewall.setAdapter(adapter);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(View_All.this, MainActivity.class));
    }
}
