package com.mohakchavan.pustakniparab.Helpers;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;

import androidx.core.view.ViewCompat;

import com.google.android.material.snackbar.Snackbar;
import com.mohakchavan.pustakniparab.R;

public class SnackBarHelper {

    public static void configureSnackbar(Context context, Snackbar snackbar) {
        setSnackbarMargins(context, snackbar);
        setSnackbarBackground(context, snackbar);
        ViewCompat.setElevation(snackbar.getView(), 7f);
    }

    private static void setSnackbarMargins(Context context, Snackbar snackbar) {
        float density = context.getResources().getDisplayMetrics().density;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackbar.getView().getLayoutParams();
        params.setMargins((int) (10 * density), (int) (10 * density), (int) (10 * density), (int) (10 * density));
        snackbar.getView().setLayoutParams(params);
    }

    private static void setSnackbarBackground(Context context, Snackbar snackbar) {
        snackbar.getView().setBackground(context.getDrawable(R.drawable.snackbar_background));
        snackbar.setTextColor(Color.WHITE);
        snackbar.setActionTextColor(Color.WHITE);
    }

}
