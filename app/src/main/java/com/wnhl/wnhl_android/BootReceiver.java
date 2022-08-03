package com.wnhl.wnhl_android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * BootReceiver is called upon reboot of the phone and is effectively the memory.
 * When the phone is rebooted (i.e. shut down or restarted), the notifications are killed and no longer set.
 * This will use data set in the saved preferences to rebuild the alarms.
 *
 * @author Sawyer Fenwick | Daniel Figueroa
 * @version 1.0
 */
public class BootReceiver extends BroadcastReceiver {
    SharedPrefHelper sharedPrefHelper = new SharedPrefHelper();
    DataBaseHelper dataBaseHelper;
    Context context;

    /**
     * This method effectively acts as a constructor for this class as its called. When it receives
     * data, it then instantiates and sets a notification using the given information and helper classes
     * though specifically on reboot for this receiever.
     * It takes two parameters being a Context object as well as an Intent.
     *
     * @param context the context for the receiver
     * @param intent the intent sent to the receiver
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarms again using teams.
            try {
                // Fetch the context to be used for the setting of alarms
                this.context = context;
                // The database helper object will be used for conveniently getting data from the database
                dataBaseHelper = new DataBaseHelper(context);
                List<Integer> teamIds = dataBaseHelper.getAllTeamIDs();
                List<String> teamNames = dataBaseHelper.getAllTeamNames();
                // Loop through all the teams and determine whether their "all games" notification has been
                // toggled
                for(int i = 0; i < teamNames.size(); i++){
                    String finalTeamName = teamNames.get(i);
                    // Effectively checks whether the team's button has been selected
                    boolean clicked = sharedPrefHelper.getSharedPreferencesBool(context.getApplicationContext(),
                            finalTeamName, false);
                    if(clicked){
                        List<Integer> teamGames = dataBaseHelper.getEventIDsForTeam(teamIds.get(i));
                        // If the team has been opted in for all their games, setAlarm for them all on reboot
                        // as this will loop through all their games
                        for(int j = 0; j < teamGames.size(); j++) {
                            setAlarm(teamGames.get(j));
                        }
                    }
                }
                // Finally, check for any individual games meant to be registered.
                List<Integer> gameIds = dataBaseHelper.getEventIDs();
                for(int i = 0; i < gameIds.size(); i++){
                    if(sharedPrefHelper.getSharedPreferencesInt(context.getApplicationContext(),String.valueOf(gameIds.get(i)),0) > 0){
                        setAlarm(gameIds.get(i));
                    }
                }

            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        }
    }//onReceive

    /**
     * Set alarm is a method which given an id, creates the Calendar object and fetches
     * database information to create the notification in the startAlarm method. The date is offset
     * by an hour decrement to let the user know an hour in advance
     *
     * @param id the id of the game which is used to get its time and date from the database.
     */
    public void setAlarm(int id) throws ParseException {
        // Takes the date and time from their columns in the row to make a date object
        String strDate = dataBaseHelper.getGamesColumnDate(id);
        String time = dataBaseHelper.getGamesColumnTime(id);
        String fullDate = strDate + " " + time;
        // The string is constructed to match this format for the Date object
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = dateFormat.parse(fullDate);
        // Create a valid Calendar object given the concocted date
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // Decrement an hour as to warn the user in advance
        c.add(Calendar.HOUR_OF_DAY, -1);
        startAlarm(c, id);
    }//setAlarm

    /**
     * startAlarm is a method which given an id and Calendar object,
     * creates the notification by calling the AlertReceiver class given the intent instantiated with
     * proper data for the game and its time.
     *
     * @param c the Calendar object used as the specific time the alarm is to go off
     * @param id the id of the game which is used to get its time and date from the database.
     */
    public void startAlarm(Calendar c, int id) {
        Calendar trueDate = (Calendar) c.clone();
        // Returns the time to the actual time of the game
        trueDate.add(Calendar.HOUR_OF_DAY, 1);
        // Creating a calendar object based on the current time for hour offset
        Calendar currentOffsetHour = Calendar.getInstance();
        currentOffsetHour.add(Calendar.HOUR_OF_DAY, 1);
        // Check if the game is to happen less than an hour from now
        if(currentOffsetHour.after(trueDate)){
            // Using the actual time the game is scheduled, make the alarm 10 minutes prior
            trueDate.add(Calendar.MINUTE, -10);
            // Check if it less than 10 minutes to the game in which the notification is not made
            // but otherwise if the current time is less before the 10 minutes before the game,
            // the alarm is made
            if(Calendar.getInstance().before(trueDate)) {
                sharedPrefHelper.putSharedPreferencesInt(context.getApplicationContext(), String.valueOf(id), id);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                // This will refer to the AlertReceiver class to make the notification itself
                Intent intent = new Intent(context, AlertReceiver.class);
                intent.putExtra("id", id);
                intent.putExtra("time", "10 minutes");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, trueDate.getTimeInMillis(), pendingIntent);
            }
        }
        // Checking if the time is prior to the current time. Does not account for the hour prior
        // to the game
        else if (c.after(Calendar.getInstance())) {
            sharedPrefHelper.putSharedPreferencesInt(context.getApplicationContext(),String.valueOf(id), id);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            // This will refer to the AlertReceiver class to make the notification itself
            Intent intent = new Intent(context, AlertReceiver.class);
            intent.putExtra("id", id);
            intent.putExtra("time", "1 hour");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
        // Other the game is in the past in which don't add it as an alarm additionally if it is
        // within 10 minutes.
    }//startAlarm
}//BootReceiver