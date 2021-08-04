package com.mohakchavan.pustakniparab.Helpers.FireBaseHelper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mohakchavan.pustakniparab.Models.BaseData;
import com.mohakchavan.pustakniparab.Models.Issues;
import com.mohakchavan.pustakniparab.Models.Names;
import com.mohakchavan.pustakniparab.Models.NewBooks;
import com.mohakchavan.pustakniparab.R;
import com.mohakchavan.pustakniparab.Services.Network_Service;

import java.util.Date;

public class BaseHelper {

    private DatabaseReference baseRef;
    private Activity context;
    private boolean isListenerAttached;
    private ValueEventListener baseDataListener;

    public DatabaseReference getBaseRef() {
        return baseRef;
    }

    public BaseHelper(Activity context) {
        this.context = context;
        isListenerAttached = false;
//        baseRef = FirebaseDatabase.getInstance().getReference().child(this.context.getResources().getString(R.string.basePoint));
        setBaseReference();
    }

    public void setBaseReference() {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getResources().getString(R.string.sharedPreferencesName), Context.MODE_PRIVATE);
        if (preferences.contains(context.getResources().getString(R.string.sharedDeveloperMode)) &&
                preferences.getBoolean(context.getResources().getString(R.string.sharedDeveloperMode), false)) {
            baseRef = FirebaseDatabase.getInstance().getReference().child(context.getResources().getString(R.string.testData));
        } else {
            baseRef = FirebaseDatabase.getInstance().getReference().child(context.getResources().getString(R.string.basePoint));
        }
    }

    public void isCurrentUserDeveloper(String userId, final BaseHelper.onCompleteRetrieval onCompleteRetrieval, final BaseHelper.onFailure onFailure) {
        baseRef.getRoot().child(context.getResources().getString(R.string.verifiedUsers))
                .child(userId).child(context.getResources().getString(R.string.isDeveloperKey))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean isDeveloper = false;
                        if (snapshot.exists()) {
                            isDeveloper = ((String) snapshot.getValue(String.class)).equalsIgnoreCase(Boolean.TRUE.toString());
                        }
                        onCompleteRetrieval.onComplete(isDeveloper);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        onFailure.onFail(error);
                    }
                });
    }

    public void getAllBaseDataContinuous(final BaseHelper.onCompleteRetrieval onCompleteRetrieval, final BaseHelper.onFailure onFailure) {
        if (Network_Service.checkInternetToProceed(context)) {
            setBaseDataListener(onCompleteRetrieval, onFailure);
            baseRef.orderByKey().addValueEventListener(baseDataListener);
        }
    }

    public void setBaseDataListener(final BaseHelper.onCompleteRetrieval onCompleteRetrieval, final BaseHelper.onFailure onFailure) {
        baseDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BaseData baseData = new BaseData();
                for (DataSnapshot s : snapshot.getChildren()) {
                    switch (s.getKey()) {
                        case "Issues":
                            for (DataSnapshot snap : s.getChildren()) {
                                if (snap.getKey() != null && !snap.getKey().isEmpty() && !snap.getKey().contentEquals(context.getResources().getString(R.string.totalRecords))) {
                                    Issues issue = snap.getValue(Issues.class);
                                    baseData.addIssue(issue);
                                }
                            }
                            break;
                        case "Names":
                            for (DataSnapshot snap : s.getChildren()) {
                                if (snap.getKey() != null && !snap.getKey().isEmpty() && !snap.getKey().contentEquals(context.getResources().getString(R.string.totalNames))) {
                                    Names name = snap.getValue(Names.class);
                                    baseData.addName(name);
                                }
                            }
                            break;
                        case "NewBooks":
                            for (DataSnapshot snap : s.getChildren()) {
                                if (snap.getKey() != null && !snap.getKey().isEmpty() && !snap.getKey().contentEquals(context.getResources().getString(R.string.totalNewBooks))) {
                                    NewBooks book = snap.getValue(NewBooks.class);
                                    baseData.addBook(book);
                                }
                            }
                            break;
                    }
                }
                isListenerAttached = true;
                onCompleteRetrieval.onComplete(baseData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                isListenerAttached = false;
                onFailure.onFail(error);
            }
        };
    }

    public void removeBaseDataListener() {
        if (isListenerAttached) {
            baseRef.removeEventListener(baseDataListener);
            isListenerAttached = false;
        }
    }

    public void updateDataChangedTimeStamp() {
        baseRef.child(context.getResources().getString(R.string.timeStamps))
                .child(context.getResources().getString(R.string.dataChangedTimeStamp))
                .setValue(new Date().getTime());
    }

    public interface onCompleteTransaction {
        void onComplete(boolean committed, Object data);
    }

    public interface onCompleteRetrieval {
        void onComplete(Object data);
    }

    public interface onFailure {
        void onFail(Object data);
    }

    public interface onDeletion {
        void onDelete(boolean deleted);
    }
}
