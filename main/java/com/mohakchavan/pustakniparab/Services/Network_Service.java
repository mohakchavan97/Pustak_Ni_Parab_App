package com.mohakchavan.pustakniparab.Services;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;

public class Network_Service {

    public static boolean isNetworkAvailable(Activity context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());
                return capabilities != null;
            } else {
                NetworkInfo info = manager.getActiveNetworkInfo();
                return info != null;
            }
        }
        return false;
    }

    public static void checkInternetToProceed(final Activity context) {
        if (!isNetworkAvailable(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("No Internet Connection. Exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            context.finish();
                            System.exit(0);
                        }
                    }).setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            context.recreate();
                        }
                    }).setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }


}
