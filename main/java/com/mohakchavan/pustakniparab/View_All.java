package com.mohakchavan.pustakniparab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mohakchavan.pustakniparab.FireBaseHelper.BaseHelper;
import com.mohakchavan.pustakniparab.FireBaseHelper.NamesHelper;
import com.mohakchavan.pustakniparab.Models.Names;

import java.util.List;

public class View_All extends AppCompatActivity {

    RecyclerView va_viewall;
    View_All_Adapter adapter;
    TextView tv_state;
    List<Names> namesList;
    private Activity context;
    private DBHelper helper;
    private NamesHelper namesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

//        namesList=new ArrayList<>();
        context = View_All.this;
        helper = new DBHelper(context);
        namesHelper = new NamesHelper(context);
        tv_state = findViewById(R.id.ac_va_tv_state);

        va_viewall = findViewById(R.id.ac_va_rv_viewall);
        va_viewall.setHasFixedSize(true);
        va_viewall.setLayoutManager(new LinearLayoutManager(context));
//        va_viewall.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                Toast.makeText(View_All.this, "Long Clicked", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });

//        namesList = helper.getAllNames();
        namesHelper.getAllNames(new BaseHelper.onCompleteRetrieval() {
            @Override
            public void onComplete(Object data) {
                namesList = (List<Names>) data;
                if (namesList.isEmpty()) {
                    tv_state.setText("No Records Found.");
                } else {
                    adapter = new View_All_Adapter(context, namesList);
                    va_viewall.setAdapter(adapter);
                }
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
        startActivity(new Intent(View_All.this, AddPerson.class));
    }
}
