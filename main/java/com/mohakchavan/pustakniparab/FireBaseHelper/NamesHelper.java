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
    private boolean isContinuousListenerAttached;

    public NamesHelper(Activity context) {
        this.context = context;
        namesRef = new BaseHelper().getRootRef().child(context.getResources().getString(R.string.names));
        isContinuousListenerAttached = false;
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
                if (currentData.getValue() != null) {
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
        if (!isContinuousListenerAttached) {
            setAllNamesListener(onCompleteRetrieval);
            namesRef.orderByChild("ser_no").addValueEventListener(allNamesListener);
        }
        isContinuousListenerAttached = true;
    }

    public void removeAllNamesListener() {
        namesRef.removeEventListener(allNamesListener);
        isContinuousListenerAttached = false;
    }

    public void getAllNamesOnce(BaseHelper.onCompleteRetrieval onCompleteRetrieval) {
        Network_Service.checkInternetToProceed(context);
        setAllNamesListener(onCompleteRetrieval);
        namesRef.orderByChild("ser_no").addListenerForSingleValueEvent(allNamesListener);
    }

    public void getNameDetails(String nameId, final BaseHelper.onCompleteRetrieval onCompleteRetrieval) {
        Network_Service.checkInternetToProceed(context);
        namesRef.child(nameId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Names names = snapshot.getValue(Names.class);
                    onCompleteRetrieval.onComplete(names);
                } else {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "No Data received. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Some Error Occurred. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void updatePerson(final Names updatedName, final BaseHelper.onCompleteTransaction onCompleteTransaction) {
        Network_Service.checkInternetToProceed(context);
        namesRef.child(String.valueOf(updatedName.getSer_no())).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                if (currentData.getValue() != null) {
                    if (currentData.child("ser_no").getValue(Long.class) != null &&
                            currentData.child("ser_no").getValue(Long.class) == updatedName.getSer_no()) {
                        currentData.setValue(updatedName);
                        return Transaction.success(currentData);
                    } else {
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
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
                onCompleteTransaction.onComplete(committed, currentData);
            }
        });
    }

    public void deleteName(String removalId, final BaseHelper.onDeletion onDeletion) {
        Network_Service.checkInternetToProceed(context);
        namesRef.child(removalId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onDeletion.onDelete(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onDeletion.onDelete(false);
            }
        });
    }
}


