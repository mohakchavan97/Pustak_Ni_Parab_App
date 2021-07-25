package com.mohakchavan.pustakniparab.Helpers.FireBaseHelper;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class BaseMessaging {

    FirebaseMessaging baseMessaging;
    Activity context;

    public BaseMessaging(Activity context) {
        this.context = context;
        baseMessaging = FirebaseMessaging.getInstance();
    }

    public void getAndRegisterToken() {
        baseMessaging.getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    registerMessagingToken(task.getResult());
                }
            }
        });
    }

    public void registerMessagingToken(String newToken) {
        String userId = new BaseAuthenticator(context).getCurrentUser().getUid();
        if (!userId.isEmpty() && !newToken.isEmpty()) {
            new BaseHelper(context).addFcmId(userId, newToken);
        }
    }
}
