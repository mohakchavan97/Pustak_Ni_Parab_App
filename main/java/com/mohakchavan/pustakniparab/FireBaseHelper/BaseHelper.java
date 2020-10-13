package com.mohakchavan.pustakniparab.FireBaseHelper;

import android.app.Activity;
import android.widget.Toast;

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

public class BaseHelper {

    private DatabaseReference baseRef;
    private Activity context;

    public DatabaseReference getBaseRef() {
        return baseRef;
    }

    public BaseHelper(Activity context) {
        this.context = context;
        baseRef = FirebaseDatabase.getInstance().getReference().child(this.context.getResources().getString(R.string.basePoint));
    }

    public void getAllBaseDataContinuous(final onCompleteRetrieval onCompleteRetrieval) {
        Network_Service.checkInternetToProceed(context);
        baseRef.addValueEventListener(new ValueEventListener() {
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
                onCompleteRetrieval.onComplete(baseData);
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
        });
    }

    public interface onCompleteTransaction {
        void onComplete(boolean committed, Object data);
    }

    public interface onCompleteRetrieval {
        void onComplete(Object data);
    }

    public interface onDeletion {
        void onDelete(boolean deleted);
    }
}
