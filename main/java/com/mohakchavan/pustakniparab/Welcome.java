package com.mohakchavan.pustakniparab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Welcome extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(Welcome.this, NavigationDrawer.class);
                    startActivity(intent);
                }
            }
        };

        thread.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
