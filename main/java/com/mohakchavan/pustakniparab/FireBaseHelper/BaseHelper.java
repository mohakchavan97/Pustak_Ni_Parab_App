package com.mohakchavan.pustakniparab.FireBaseHelper;

import android.app.Activity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mohakchavan.pustakniparab.R;

public class BaseHelper {

    private DatabaseReference rootRef;
    private Activity context;

    public DatabaseReference getRootRef() {
        return rootRef;
    }

    public BaseHelper(Activity context) {
        this.context = context;
        rootRef = FirebaseDatabase.getInstance().getReference().child(this.context.getResources().getString(R.string.basePoint));
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
