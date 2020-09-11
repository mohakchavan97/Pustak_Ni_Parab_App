package com.mohakchavan.pustakniparab.FireBaseHelper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BaseHelper {

    private DatabaseReference rootRef;

    public DatabaseReference getRootRef() {
        return rootRef;
    }

    public BaseHelper() {
        rootRef = FirebaseDatabase.getInstance().getReference();
    }

    public interface onCompleteTransaction {
        void onComplete(boolean committed, Object data);
    }
}
