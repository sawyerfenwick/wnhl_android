package com.wnhl.wnhl_android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * The Notifications class acts as the page in the app for the user to select
 * preferences for games from a given team they wish to be alerted to.
 *
 * @author Sawyer Fenwick | Daniel Figueroa
 * @version 1.0
 */
public class Notifications extends AppCompatActivity {

    DataBaseHelper dataBaseHelper;
    SharedPrefHelper sharedPrefHelper;
    CheckBox cb;
    LinearLayout linearLayout;

    /**
     * Creates Notifications activity
     * Determines the team names from the database and creates checkboxes for them all
     * representing a selection for the teams and their games which will notify the user
     * to all their games if the box is selected for the pertinent team.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wnhl_notif);
        getSupportActionBar().hide();

        dataBaseHelper = new DataBaseHelper(this);

        linearLayout = (LinearLayout)findViewById(R.id.notifButtonHolder);

        List<String> teamNames = null;
        List<Integer> teamIDs = null;
        try {
            teamNames = dataBaseHelper.getAllTeamNames();
            teamIDs = dataBaseHelper.getAllTeamIDs();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < teamNames.size(); i++){
            int finalteamID = teamIDs.get(i);
            String finalTeamName = teamNames.get(i);
            boolean clicked = sharedPrefHelper.getSharedPreferencesBool(getApplication(),
                    finalTeamName, false);
            cb = new CheckBox(this);
            if(clicked){
                cb.setChecked(true);
            }
            cb.setText(finalTeamName);
            cb.setTextColor(getResources().getColor(R.color.white, null));
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean click = sharedPrefHelper.getSharedPreferencesBool(getApplication(),
                            finalTeamName, false);
                    if(click){
                        sharedPrefHelper.putSharedPreferencesBool(getApplication(),finalTeamName,
                                false);
                        cancelAll(dataBaseHelper.getEventIDsForTeam(finalteamID));
                        Toast.makeText(getApplication(), "Notifications for " + dataBaseHelper
                                .getTeamName(finalteamID) + " Disabled.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        sharedPrefHelper.putSharedPreferencesBool(getApplication(), finalTeamName,
                                true);
                        try {
                            addAll(dataBaseHelper.getEventIDsForTeam(finalteamID));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplication(), "Notifications for " + dataBaseHelper
                                .getTeamName(finalteamID) + " Enabled.",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            linearLayout.addView(cb);
        }
    }//onCreate

    /**
     * Cancels any active games given a list of ids and uses those to cancel the set notifications
     * through use of the id as each notification was given a unique id.
     * The method takes one parameter representing that of a list of event ids and will eliminate
     * the alarm if it is set.
     *
     * @param eventsIdList the list of event ids given which will be given as the list of a certain
     *                     team's game ids.
     */
    private void cancelAll(List<Integer> eventsIdList){
        for(int i = 0; i < eventsIdList.size(); i++){
            cancelAlarm(eventsIdList.get(i));
        }
    }//cancelAll

    /**
     * Sets all games as an alarm to be notified pertinent to the list of game ids given
     * The list of game ids are the parameter and will all be set as alarms. Each alarm is given a
     * unique id.
     *
     * @param eventsIdList the list of event ids given which will be given as the list of a certain
     *                     team's game ids.
     */
    private void addAll(List<Integer> eventsIdList) throws ParseException {
        for(int i = 0; i < eventsIdList.size(); i++){
            setAlarm(eventsIdList.get(i));
        }
    }//addAll

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
                sharedPrefHelper.putSharedPreferencesInt(getBaseContext().getApplicationContext(), String.valueOf(id), id);
                AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
                // This will refer to the AlertReceiver class to make the notification itself
                Intent intent = new Intent(getBaseContext(), AlertReceiver.class);
                intent.putExtra("id", id);
                intent.putExtra("time", "10 minutes");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), id, intent, 0);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, trueDate.getTimeInMillis(), pendingIntent);
            }
        }
        // Checking if the time is prior to the current time. Does not account for the hour prior
        // to the game
        else if (c.after(Calendar.getInstance())) {
            sharedPrefHelper.putSharedPreferencesInt(getBaseContext(),String.valueOf(id), id);
            // Create an alarm with the given date and unique id for them
            AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
            // This will refer to the AlertReceiver class to make the notification itself
            Intent intent = new Intent(getBaseContext() , AlertReceiver.class);
            intent.putExtra("id", id);
            intent.putExtra("time", "1 hour");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), id, intent, 0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
    }//startAlarm

    /**
     * cancelAlarm is a method which given an id,
     * cancels the notification by calling the AlertReceiver class given the intent instantiated with
     * proper data for the game and its time and deletes the alarm with the matching intent
     *
     * @param id the id of the game which is used to get its time and date from the database.
     *
     */
    public void cancelAlarm(int id) {
        sharedPrefHelper.removeSharedPreferencesInt(getBaseContext().getApplicationContext(),String.valueOf(id));
        AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getBaseContext(), AlertReceiver.class);
        intent.putExtra("id", id);
        intent.putExtra("time", "1 hour");
        PendingIntent pIntent = PendingIntent.getBroadcast(getBaseContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pIntent);
        // Repeating because the time value may be different and it is never certain, try both kinds of the intent
        intent.removeExtra("time");
        intent.putExtra("time", "10 minutes");
        pIntent = PendingIntent.getBroadcast(getBaseContext().getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pIntent);
    }//cancelAlarm
}//Notifications
