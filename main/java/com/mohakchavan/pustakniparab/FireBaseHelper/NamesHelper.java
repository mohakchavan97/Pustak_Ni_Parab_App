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
import com.mohakchavan.pustakniparab.Models.Names;
import com.mohakchavan.pustakniparab.R;
import com.mohakchavan.pustakniparab.Services.Network_Service;

import java.util.ArrayList;
import java.util.List;

public class NamesHelper {

    private DatabaseReference namesRef;
    private Activity context;
    private ValueEventListener allNamesListener;

    public NamesHelper(Activity context) {
        this.context = context;
        namesRef = new BaseHelper().getRootRef().child(context.getResources().getString(R.string.names));
    }

    public DatabaseReference getNamesRef() {
        return namesRef;
    }

    public void addNewPerson(final Names newPersonDetails, final BaseHelper.onCompleteTransaction onCompleteTransaction) {
        Network_Service.checkInternetToProceed(context);
        namesRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                long nextChildCount = (currentData.getChildrenCount()) + 1;
                if (!currentData.hasChild(String.valueOf(nextChildCount))) {
                    newPersonDetails.setSer_no(nextChildCount);
                    currentData.child(String.valueOf(nextChildCount)).setValue(newPersonDetails);
                    return Transaction.success(currentData);
                } else {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "This userId is already present. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return Transaction.abort();
                }
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                onCompleteTransaction.onComplete(committed, newPersonDetails);
            }
        });
    }

    public void setAllNamesListener(final BaseHelper.onCompleteRetrieval onCompleteRetrieval) {
        allNamesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Names> namesList = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Names name = snap.getValue(Names.class);
                    namesList.add(name);
                }
                onCompleteRetrieval.onComplete(namesList);

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

    public void getAllNamesContinuous(final BaseHelper.onCompleteRetrieval onCompleteRetrieval) {
        Network_Service.checkInternetToProceed(context);
        setAllNamesListener(onCompleteRetrieval);
        namesRef.orderByChild("ser_no").addValueEventListener(allNamesListener);
    }

    public void removeAllNamesListener() {
        namesRef.removeEventListener(allNamesListener);
    }

    public void getAllNamesOnce(BaseHelper.onCompleteRetrieval onCompleteRetrieval) {
        Network_Service.checkInternetToProceed(context);
        setAllNamesListener(onCompleteRetrieval);
        namesRef.orderByChild("ser_no").addListenerForSingleValueEvent(allNamesListener);
    }
}


