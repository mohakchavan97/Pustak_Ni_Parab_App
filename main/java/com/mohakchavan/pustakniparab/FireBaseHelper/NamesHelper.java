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
import com.mohakchavan.pustakniparab.Models.Names;
import com.mohakchavan.pustakniparab.R;

public class NamesHelper {

    private DatabaseReference namesRef;
    private Activity context;

    public NamesHelper(Activity context) {
        this.context = context;
        namesRef = new BaseHelper().getRootRef().child(context.getResources().getString(R.string.names));
    }

    public DatabaseReference getNamesRef() {
        return namesRef;
    }

    public void addNewPerson(final Names newPersonDetails, final BaseHelper.onCompleteTransaction onCompleteTransaction) {
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
                    Toast.makeText(context, "This userId is already present. Please try again.", Toast.LENGTH_SHORT).show();
                    return Transaction.abort();
                }
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                onCompleteTransaction.onComplete(committed, newPersonDetails);
            }
        });
    }
}


