package com.mohakchavan.pustakniparab.Helpers.FireBaseHelper;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.mohakchavan.pustakniparab.Models.NewBooks;
import com.mohakchavan.pustakniparab.R;
import com.mohakchavan.pustakniparab.Services.Network_Service;

public class NewBooksHelper {

    private DatabaseReference newBooksRef;
    private Activity context;
    private BaseHelper baseHelper;

    public NewBooksHelper(Activity context) {
        this.context = context;
        baseHelper = new BaseHelper(this.context);
        newBooksRef = baseHelper.getBaseRef().child(context.getResources().getString(R.string.newBooks));
    }

    public void addNewRecord(final NewBooks newBooks, final BaseHelper.onCompleteTransaction onCompleteTransaction) {
        if (Network_Service.checkInternetToProceed(context)) {
            newBooksRef.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                    if (currentData.getValue() != null) {
                        if (currentData.hasChild(context.getResources().getString(R.string.totalNewBooks)) && currentData.child(context.getResources().getString(R.string.totalNewBooks)).getValue() != null) {
                            long currentTotal = currentData.child(context.getResources().getString(R.string.totalNewBooks)).getValue(Long.class);
                            ++currentTotal;
                            if (!currentData.hasChild(String.valueOf(currentTotal))) {
                                newBooks.setNewBookId(currentTotal);
                                currentData.child(String.valueOf(currentTotal)).setValue(newBooks);
                                currentData.child(context.getResources().getString(R.string.totalNewBooks)).setValue(currentTotal);
                                baseHelper.updateDataChangedTimeStamp();
                                return Transaction.success(currentData);
                            } else {
                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "The record is already present. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return Transaction.abort();
                            }
                        } else {
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "Some Error Occurred. Please Contact Developer.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return Transaction.abort();
                        }
                    } else {
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, context.getString(R.string.someError), Toast.LENGTH_SHORT).show();
                            }
                        });
                        return Transaction.abort();
                    }
                }

                @Override
                public void onComplete(@Nullable DatabaseError error, boolean committed,
                                       @Nullable DataSnapshot currentData) {
                    onCompleteTransaction.onComplete(committed, newBooks);
                }
            });
        }
    }
}
