package com.mohakchavan.pustakniparab;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class NewBooks extends AppCompatActivity {

    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_books);

        context = NewBooks.this;
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.newBooks);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}