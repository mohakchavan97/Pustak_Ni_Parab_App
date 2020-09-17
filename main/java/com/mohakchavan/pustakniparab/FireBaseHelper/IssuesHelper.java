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
import com.google.firebase.database.ValueEventListener;
import com.mohakchavan.pustakniparab.Models.Issues;
import com.mohakchavan.pustakniparab.R;
import com.mohakchavan.pustakniparab.Services.Network_Service;

import java.util.ArrayList;
import java.util.List;

public class IssuesHelper {

    private DatabaseReference issuesRef;
    private Activity context;
    private ValueEventListener allIssuesListener;

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

    public void addReturnedIssues(final List<Issues> checkedIssues, final BaseHelper.onCompleteTransaction onCompleteTransaction) {
        Network_Service.checkInternetToProceed(context);
        issuesRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                if (currentData.getValue() != null) {
                    for (Issues issue : checkedIssues) {
                        if (currentData.hasChild(String.valueOf(issue.getIssueNo())) && currentData.child(String.valueOf(issue.getIssueNo())).getValue() != null) {
                            currentData.child(String.valueOf(issue.getIssueNo())).setValue(issue);
                        } else {
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "Some data missing. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return Transaction.abort();
                        }
                    }
                    return Transaction.success(currentData);
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
                onCompleteTransaction.onComplete(committed, currentData);
            }
        });
    }

    public void getAllIssuesContinuous(final BaseHelper.onCompleteRetrieval onCompleteRetrieval) {
        Network_Service.checkInternetToProceed(context);
        setAllIssuesListener(onCompleteRetrieval);
        issuesRef.orderByChild("issueNo").addValueEventListener(allIssuesListener);
    }

    private void setAllIssuesListener(final BaseHelper.onCompleteRetrieval onCompleteRetrieval) {
        allIssuesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Issues> issues = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if (snap.getKey() != null && !snap.getKey().isEmpty() && !snap.getKey().contentEquals(context.getResources().getString(R.string.totalRecords))) {
                        Issues issue = snap.getValue(Issues.class);
                        issues.add(issue);
                    }
                }
                onCompleteRetrieval.onComplete(issues);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Some Error Occurred while retrieving data.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
    }

    public void removeAllNamesListener() {
        issuesRef.removeEventListener(allIssuesListener);
//        isContinuousListenerAttached = false;
    }
}
