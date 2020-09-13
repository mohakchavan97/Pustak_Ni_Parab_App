package com.mohakchavan.pustakniparab.Services;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.mohakchavan.pustakniparab.DBHelper;
import com.mohakchavan.pustakniparab.NameModule.AddPerson;
import com.mohakchavan.pustakniparab.Models.Names;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Export_Service extends Service {

    DBHelper helper;
    boolean status = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        helper = new DBHelper(Export_Service.this);
        List<Names> namesList = helper.getAllNames();

        Toast.makeText(getApplicationContext(), "Export Started", Toast.LENGTH_SHORT).show();


        try {
//            String rootPath = Environment.getRootDirectory().getAbsolutePath().concat("/Pustak_Ni_Parab/");
//            String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/Pustak_Ni_Parab/");
            String rootPath = getApplicationContext().getExternalFilesDir(null).getAbsolutePath().concat("/Pustak_Ni_Parab/");
            File f = new File(rootPath);
            if (!f.exists())
                f.mkdirs();

            f = new File(rootPath + "names.csv");
            if (f.exists())
                f.delete();
            f.createNewFile();
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(f));


            // writing data to the file
//            OutputStreamWriter writer = new OutputStreamWriter(getApplicationContext().openFileOutput("names.csv", getApplicationContext().MODE_PRIVATE));
//            writer.write("SER_NO,FNAME,LNAME,BLK,STRT,AREA,CALL");
//            Toast.makeText(getApplicationContext(), "Exporting Started", Toast.LENGTH_SHORT).show();
            SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyHHmmss");
            writer.write(String.valueOf(namesList.size()) + "," + format.format(new Date()).toString() + "\n");
            for (int i = 0; i < namesList.size(); i++) {
                StringBuilder builder = new StringBuilder();
                Names names = new Names();
                names = namesList.get(i);
                builder.append(names.getSer_no()).append(",")
                        .append(names.getFirstName()).append(",")
                        .append(names.getLastName()).append(",")
                        .append(names.getBlkOrFltNo()).append(",")
                        .append(names.getStreetName()).append(",")
                        .append(names.getLocalityOrArea()).append(",")
                        .append(names.getContact()).append("\n");
                writer.write(builder.toString());
            }
            writer.close();

            //sending the file
            Uri path = FileProvider.getUriForFile(getApplicationContext(), "com.mohakchavan.pustakniparab.exportFile", f);
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, path);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setType("text/csv");
//            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Activity activity = AddPerson.activity;
            activity.startActivity(Intent.createChooser(shareIntent, "Export..."));

            status = true;

        } catch (IOException e) {
//            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Some Error Occured.\nExporting Stopped", Toast.LENGTH_SHORT).show();
            Log.e("Error", e.getMessage());
        }


//        try {
////            String rootPath= Environment.getExternalStorageDirectory().getAbsolutePath().concat("/"+String.valueOf(R.string.app_name)+"/");
////            File file=new File(rootPath);
////            if (!file.exists())
////                file.mkdirs();
////
////            File f=new File(rootPath+"names.csv");
////            if (f.exists())
////                f.delete();
////            f.createNewFile();
//
//
//        }catch (){
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (status)
            Toast.makeText(getApplicationContext(), "Names Exported Successfully", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Names NOT Exported", Toast.LENGTH_SHORT).show();
    }
}
