package com.mohakchavan.pustakniparab;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;

public class CustomDatePicker extends DatePickerDialog {

    boolean isToSkipDismiss;

    public CustomDatePicker(@NonNull Context context, @Nullable OnDateSetListener listener, int year, int month, int dayOfMonth) {
        super(context, listener, year, month, dayOfMonth);
        isToSkipDismiss = false;
    }

    public void setNeutralButton(String buttonText) {
        setButton(BUTTON_NEUTRAL, buttonText, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar calendar = Calendar.getInstance();
                updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                isToSkipDismiss = true;
            }
        });
    }

    @Override
    public void dismiss() {
        if (!isToSkipDismiss) {
            super.dismiss();
        } else {
            isToSkipDismiss = false;
        }
    }
}
