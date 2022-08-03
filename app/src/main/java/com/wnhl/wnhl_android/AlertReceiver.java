package com.wnhl.wnhl_android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;

/**
 * AlertReceiver inherits from BroadcastReceiver
 * Receives data from the notification classes and utilizes the NotificationHelper class as well
 * to broadcast the created notifications to the user.
 * @author Sawyer Fenwick | Daniel Figueroa
 * @version 1.0
 */
public class AlertReceiver extends BroadcastReceiver {
    SharedPrefHelper sharedPrefHelper = new SharedPrefHelper();
    /**
     * This method effectively acts as a constructor for this class as its called. When it receives
     * data, it then instantiates and sets a notification using the given information and helper classes.
     * It takes two parameters being a Context object as well as an Intent.
     *
     * @param context the context for the receiver
     * @param intent the intent sent to the receiver
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieves the information sent by the notification classes. The id of the games were sent
        Bundle extras = intent.getExtras();
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(extras.getInt("id"),extras.getString("time"));
        notificationHelper.getManager().notify(extras.getInt("id"), nb.build());
        sharedPrefHelper.removeSharedPreferencesInt(context, String.valueOf(extras.getInt("id")));
    }//onReceive
}//AlertReceiver