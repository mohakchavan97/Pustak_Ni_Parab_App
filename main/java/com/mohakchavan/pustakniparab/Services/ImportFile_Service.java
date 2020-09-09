package com.mohakchavan.pustakniparab.Services;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.mohakchavan.pustakniparab.MainActivity;
import com.mohakchavan.pustakniparab.R;

public class ImportFile_Service extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(ImportFile_Service.this, "Import Started", Toast.LENGTH_SHORT).show();
        Intent getFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        getFile.addCategory(Intent.CATEGORY_OPENABLE);
        getFile.setType("*/*");

//        For Samsung devices
//        Intent getFile = new Intent("com.sec.android.app.myfiles.PICK_DATA");
//        getFile.putExtra("CONTENT_TYPE", "*/*");
//        getFile.addCategory(Intent.CATEGORY_DEFAULT);


        Activity activity = MainActivity.activity;
        activity.startActivityForResult(getFile, getApplicationContext().getResources().getInteger(R.integer.ImportRequestCode));
        stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(ImportFile_Service.this, "Import Service destroyed", Toast.LENGTH_SHORT).show();
    }
}
