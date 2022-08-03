package com.wnhl.wnhl_android;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;

/**
 * Notification Helper is pivotal to the creation and detailing of the notifications themselves.
 * Sets all details about notifications and creates pertinent managers.
 * @author Sawyer Fenwick | Daniel Figueroa
 * @version 1.0
 */
public class NotificationHelper extends ContextWrapper {
    // Unique value for the notification channel
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    // Notification manager which is used to directly notify the user
    private NotificationManager mManager;
    // Database helper used to conveniently fetch pertinent data from the database
    DataBaseHelper dbHelper;


    /**
     * Constructor for the notification helper
     * The function takes in a single parameter being that of Context in a variable names base
     * which then establishes that as the context. Instantiates the database helper object as well.
     * Both of these elements are used by the notification methods to correctly instantiate them.
     *
     * @param base the context object for reference in the helper
     */
    public NotificationHelper(Context base) {
        super(base);
        dbHelper = new DataBaseHelper(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    } // constructor

    /**
     * The method is responsible for creating a notification channel which is then used to
     * define particular details of the notifications.
     * The method takes no parameters and returns nothing.
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        // The notification channel needs an ID and name which are previously instantiated
        // Additionally the importance it set to high such that the notifications themselves are
        // made visible in many areas of the phone
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }//createChannel

    /**
     * Retrieves the current notification manager.
     * If it does not exist, the method instantiates a new one and returns that
     *
     * @return mManager NotificationManager
     */
    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }//getManager

    /**
     * Creates the notification to be scheduled with all the functionality
     * The method sets the intents to fire on click of itself and its action button
     * Additionally defines other attributes of the notification itself such as icon, description etc.
     *
     * @param id the id use to mark the notification as unique and also fetch pertinent database info.
     * @return NotificationCompat.Builder
     */
    public NotificationCompat.Builder getChannelNotification(int id, String time) {
        // On click of the overall notification, it will take the user to the schedule
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("packageNotifyId", id);
        Intent mapIntent = null;

        // Creating the map intent for the action button's functionality
        int venueId = dbHelper.getGamesColumnVenue(id);
        String venueName = dbHelper.getVenueName(venueId);
        if(venueName.equals("Pelham - Duliban")){
            venueName = "Pelham - Accipter";
        }
        if(!venueName.equals("")){
            String parts[] = venueName.split("-");
            String direct = "google.navigation:q=" + parts[1] + "+Arena, +" + parts[0] + "+Canada";
            Uri gmmIntentUri = Uri.parse(direct);
            mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
        }
        mapIntent.putExtra("EXTRA_NOTIFICATION_ID", 0);
        PendingIntent mapPendingIntent =
                PendingIntent.getActivity(this, id, mapIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // The next few lines are to provide a function on click of the notification
        PendingIntent pIntent = PendingIntent.getActivity(this, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // Gets the title of the game from the db which is the most important detail.
        String notificationMessage = dbHelper.getGamesColumnTitle(id);
        // Constructing the message for the notification detailing the game's info
        notificationMessage = notificationMessage + " game starting in " + time + " at " + venueName;
        // As it is, it takes them to the home screen of the app.
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                // Setting the title of the notification
                .setContentTitle("WNHL")
                // This will make it so the text is not truncated and can be expanded to view fully
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationMessage))
                // Setting the content of the notification
                .setContentText(notificationMessage)
                // Setting the logo of the notification which will end up a singular white color
                .setSmallIcon(R.drawable.ic_menubar_teams_black)
                // Ensure the notification is present on the lock screen
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                // Ensuring the notification will drop down
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                // Setting the intent to fire when the notification is clicked
                .setContentIntent(pIntent)
                // Setting the notification to clear once it is clicked
                .setAutoCancel(true)
                // Including an action button on the notification
                .addAction(R.drawable.ic_action_map, "Directions", mapPendingIntent)
                // For some reason, this is needed for a dropdown to appear, but instantiates the notification
                // with a vibration
                .setVibrate(new long[] { 1000, 1000});
    }//getChannelNotification
}//NotificationHelper