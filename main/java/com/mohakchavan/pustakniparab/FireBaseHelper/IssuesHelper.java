package com.mohakchavan.pustakniparab.FireBaseHelper;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.mohakchavan.pustakniparab.Models.Issues;
import com.mohakchavan.pustakniparab.R;
import com.mohakchavan.pustakniparab.Services.Network_Service;

public class IssuesHelper {

    private DatabaseReference issuesRef;
    private Activity context;

    public IssuesHelper(Activity context) {
        this.context = context;
        issuesRef = new BaseHelper().getRootRef().child(context.getResources().getString(R.string.issues));
    }

    public void addNewIssue(final Issues newIssueDetails, final BaseHelper.onCompleteTransaction onCompleteTransaction) {
        Network_Service.checkInternetToProceed(context);
        issuesRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                if (currentData.getValue() != null) {
                    if (currentData.hasChild(context.getResources().getString(R.string.totalRecords)) && currentData.child(context.getResources().getString(R.string.totalRecords)).getValue() != null) {
                        long currentTotal = currentData.child(context.getResources().getString(R.string.totalRecords)).getValue(Long.class);
                        ++currentTotal;
                        if (!currentData.hasChild(String.valueOf(currentTotal))) {
                            newIssueDetails.setIssueNo(currentTotal);
                            currentData.child(String.valueOf(currentTotal)).setValue(newIssueDetails);
                            currentData.child(context.getResources().getString(R.string.totalRecords)).setValue(currentTotal);
                            return Transaction.success(currentData);
                        } else {
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "The issue ID is already present. Please try again.", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(context, "Some Error Occurred. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return Transaction.abort();
                }
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                onCompleteTransaction.onComplete(committed, newIssueDetails);
            }
        });
    }
}
