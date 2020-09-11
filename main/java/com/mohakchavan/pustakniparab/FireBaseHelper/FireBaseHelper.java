package com.mohakchavan.pustakniparab.FireBaseHelper;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.mohakchavan.pustakniparab.Models.Names;
import com.mohakchavan.pustakniparab.R;

public class FireBaseHelper {
    private DatabaseReference rootRef;
    private DatabaseReference namesRef;
    private Activity context;

    public FireBaseHelper(Activity activity) {
        context = activity;
        rootRef = FirebaseDatabase.getInstance().getReference();
        namesRef = rootRef.child(activity.getResources().getString(R.string.names));
    }

    public DatabaseReference getNamesRef() {
        return namesRef;
    }

    public void addNewPerson(final Names newPersonDetails) {
        namesRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                long nextChildCount = (currentData.getChildrenCount()) + 1;
                if (!currentData.hasChild(String.valueOf(nextChildCount))) {
                    newPersonDetails.setSer_no(nextChildCount);
                    currentData.child(String.valueOf(nextChildCount)).setValue(newPersonDetails);
                    return Transaction.success(currentData);
                }else {
                    Toast.makeText(context, "This userId is already present. Please try again.", Toast.LENGTH_SHORT).show();
                    return Transaction.abort();
                }
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if (committed){
                Toast.makeText(context, "Person Successfully added.", Toast.LENGTH_SHORT).show();}
                else {
                    Toast.makeText(context, "Some Error Occurred. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
