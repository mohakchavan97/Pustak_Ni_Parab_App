package com.mohakchavan.pustakniparab.Services;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mohakchavan.pustakniparab.Helpers.FireBaseHelper.BaseMessaging;

public class CloudMessagingService extends FirebaseMessagingService {

    private static Activity context;

    public CloudMessagingService() {
    }

    public static void setContext(Activity applicationContext) {
        context = applicationContext;
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        //send the new token to database to store in the current user object.
        BaseMessaging baseMessaging = new BaseMessaging(context);
        baseMessaging.registerMessagingToken(s);
    }
}