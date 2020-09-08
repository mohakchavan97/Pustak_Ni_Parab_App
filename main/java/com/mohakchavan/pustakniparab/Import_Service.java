package com.mohakchavan.pustakniparab;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Import_Service extends Service {

    DBHelper helper;
    public boolean status = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        helper = new DBHelper(Import_Service.this);
        List<String> list = helper.getAllSerials();
        String read;
        String[] line;

        Toast.makeText(Import_Service.this, "Import Started", Toast.LENGTH_SHORT).show();
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/Pustak_Ni_Parab/");
        File file = new File(rootPath + "names.csv");

        if (file.exists()) {
            try {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
                BufferedReader reader = new BufferedReader(isr);
                read = reader.readLine();
                line = read.split(",");
                int count = Integer.parseInt(line[0]), i = 0;
//                while (!(read.isEmpty())) {
                while (i < count) {
                    if (status || i == 0) {
                        read = reader.readLine();
                        line = read.split(",");
                    }
                    if (!(list.contains(line[0]))) {
                        if (helper.addName(line[0], line[1], line[2], line[3], line[4], line[5], line[6])) {
                            status = true;
//                            read = reader.readLine();
                        } else {
                            status = false;
                        }
                    } //else {
//                        read = reader.readLine();
//                    }
                    i++;
                }


//                Toast.makeText(Import_Service.this, String.valueOf(list.contains(line[0])), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(Import_Service.this, "No Backup Found.\nExport Not Done.", Toast.LENGTH_SHORT).show();
            status=false;
        }
        stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (status)
            Toast.makeText(Import_Service.this, "Names Imported Successfully", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(Import_Service.this, "Names NOT Imported", Toast.LENGTH_SHORT).show();
    }


}
