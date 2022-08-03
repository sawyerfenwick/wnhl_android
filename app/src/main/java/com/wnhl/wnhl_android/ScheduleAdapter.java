package com.wnhl.wnhl_android;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    List<String> title, date, time;
    List<Integer> venue, eventIDs;
    Context context;
    DataBaseHelper dataBaseHelper;
    SharedPrefHelper sharedPrefHelper;
    DateFormat dateFormat;
    Date dateObj;
    Boolean startSoon = false;
    String d = "";

    /**
     *
     * @param context
     * @param eventIDs
     * @param title
     * @param date
     * @param time
     * @param venue
     */
    public ScheduleAdapter(Context context, List<Integer> eventIDs, List<String> title,
                           List<String> date, List<String> time, List<Integer> venue){
        this.context = context;
        this.eventIDs = eventIDs;
        this.title = title;
        this.date = date;
        this.time = time;
        this.venue = venue;
    }//constructor

    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.schedule_cardview, parent, false);
        return new ViewHolder(v);
    }//onCreateViewHolder

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.ViewHolder holder, int position) {
        dataBaseHelper = new DataBaseHelper(context);
        sharedPrefHelper = new SharedPrefHelper();

        String headlineSplit[] = title.get(position).split("vs");
        String uri;
        int imageID;
        int home = dataBaseHelper.getHomeTeam(eventIDs.get(position));
        int away = dataBaseHelper.getAwayTeam(eventIDs.get(position));

        switch (home){
            case 940:
                //Steelers
                uri = "@drawable/steelers_nobackground";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.home.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            case 1370:
                //Townline
                uri = "@drawable/townline_nobackground";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.home.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            case 1371:
                //CrownRoom
                uri = "@drawable/crownroom_nobackground";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.home.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            case 1810:
                //DainCity
                uri = "@drawable/dusters_nobackground";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.home.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            case 1822:
                //Lincoln
                uri = "@drawable/legends_nobackground";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.home.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            case 1824:
                //Merrit
                uri = "@drawable/islanders";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.home.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            default:
                uri = "@drawable/wnhl_logo";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.home.setImageDrawable(context.getResources().getDrawable(imageID,null));
        }

        switch (away){
            case 940:
                //Steelers
                uri = "@drawable/steelers_nobackground";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.away.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            case 1370:
                //Townline
                uri = "@drawable/townline_nobackground";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.away.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            case 1371:
                //CrownRoom
                uri = "@drawable/crownroom_nobackground";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.away.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            case 1810:
                //DainCity
                uri = "@drawable/dusters_nobackground";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.away.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            case 1822:
                //Lincoln
                uri = "@drawable/legends_nobackground";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.away.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            case 1824:
                //Merrit
                uri = "@drawable/islanders";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.away.setImageDrawable(context.getResources().getDrawable(imageID,null));
                break;
            default:
                uri = "@drawable/wnhl_logo";
                imageID = context.getResources().getIdentifier(uri, null,
                        context.getPackageName());
                holder.away.setImageDrawable(context.getResources().getDrawable(imageID,null));
        }

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateObj = dateFormat.parse(date.get(position));
            dateFormat = new SimpleDateFormat("EEE MMM d, yyyy");
            d = dateFormat.format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.day.setText(d);
        try {
            dateFormat = new SimpleDateFormat("hh:mm:ss");
            dateObj = dateFormat.parse(time.get(position));
            dateFormat = new SimpleDateFormat("h:mm aa");
            d = dateFormat.format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.scoretime.setText(d);
        holder.location.setText(dataBaseHelper.getVenueName(venue.get(position)));
        holder.headline.setText(title.get(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check = sharedPrefHelper.getSharedPreferencesInt(context,
                        String.valueOf(eventIDs.get(position)),0);
                if(check != 0){
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle(title.get(position))
                            .setPositiveButton("Directions",null)
                            .setNeutralButton("Go Back",null)
                            .setNegativeButton("Cancel Reminder",null).show();
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positiveButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            getDirections(dataBaseHelper.getVenueName(venue.get(position)));
                            dialog.dismiss();
                        }
                    });
                    Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    negativeButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            sharedPrefHelper.removeSharedPreferencesInt(context,
                                    String.valueOf(eventIDs.get(position)));
                            cancelAlarm(eventIDs.get(position));
                            Toast.makeText(context, "Reminder Cancelled", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
                else{
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle(title.get(position))
                            .setPositiveButton("Directions",null)
                            .setNeutralButton("Go Back",null)
                            .setNegativeButton("Remind Me",null).show();
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positiveButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            getDirections(dataBaseHelper.getVenueName(venue.get(position)));
                            dialog.dismiss();
                        }
                    });
                    Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    negativeButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            sharedPrefHelper.putSharedPreferencesInt(context,
                                    String.valueOf(eventIDs.get(position)),eventIDs.get(position));
                            try {
                                setAlarm(eventIDs.get(position));
                                if(!startSoon){
                                    Toast.makeText(context, "Reminder Set", Toast.LENGTH_SHORT).show();
                                }
                                startSoon = false;
                            } catch (ParseException e) {
                                Toast.makeText(context, "Error Setting Reminder", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
    }//onBindViewHolder

    /**
     * Opens Google Maps and searches for the Venue
     *
     * @param venueName name of location to search for
     */
    private void getDirections(String venueName) {
        String direct = "";
        if(venueName.equals("Pelham - Duliban")){
            venueName = "Pelham - Accipter";
        }
        if(!venueName.equals("")){
            try {
                String parts[] = venueName.split("-");
                if(venueName.contains("Arena") | venueName.contains("arena")){
                    direct = "google.navigation:q=" + parts[1] + ", +" + parts[0] + "+Canada";
                }
                else{
                    direct = "google.navigation:q=" + parts[1] + "+Arena, +" + parts[0] + "+Canada";
                }
                Uri gmmIntentUri = Uri.parse(direct);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            }
            catch (Exception e){
                Toast.makeText(context, "No Arena Data", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(context, "No Arena Data", Toast.LENGTH_SHORT).show();
        }
    }//getDirections

    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return title.size();
    }//getItemCount

    /**
     *
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView day, scoretime, location, headline;
        ImageView home, away;
        CardView cardView;

        /**
         *
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.date);
            scoretime = itemView.findViewById(R.id.scoretime);
            location = itemView.findViewById(R.id.locate);
            headline = itemView.findViewById(R.id.gameHeadline);
            home = itemView.findViewById(R.id.home);
            away = itemView.findViewById(R.id.away);
            cardView = itemView.findViewById(R.id.card_view);
        }//constructor
    }//ViewHolder

    /**
     *
     * @param id
     * @throws ParseException
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
        // Copies c
        Calendar trueDate = (Calendar) c.clone();
        // Returns the time to the actual time of the game
        trueDate.add(Calendar.HOUR_OF_DAY, 1);

        // Creating a calendar object based on the current time for 10 minute offset
        Calendar currentOffsetMinutes = Calendar.getInstance();
        currentOffsetMinutes.add(Calendar.MINUTE, 10);

        // Creating a calendar object based on the current time for hour offset
        Calendar currentOffsetHour = Calendar.getInstance();
        currentOffsetHour.add(Calendar.HOUR_OF_DAY, 1);
        // Check if the game is to happen less than an hour from now
        if(currentOffsetMinutes.after(trueDate)){
            // A simple text will be able to notify the user all the same about game
            startSoon = true;
            Toast.makeText(context, "Game begins in less than 10 minutes.", Toast.LENGTH_SHORT).show();
        }else if(currentOffsetHour.after(trueDate)){
            // Using the actual time the game is scheduled, make the alarm 10 minutes prior
            trueDate.add(Calendar.MINUTE, -10);
            sharedPrefHelper.putSharedPreferencesInt(context.getApplicationContext(),String.valueOf(id), id);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            // This will refer to the AlertReceiver class to make the notification itself
            Intent intent = new Intent(context, AlertReceiver.class);
            intent.putExtra("id", id);
            intent.putExtra("time", "10 minutes");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, trueDate.getTimeInMillis(), pendingIntent);
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
        else{
            Toast.makeText(context, "Game has already begun or concluded.", Toast.LENGTH_SHORT).show();
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
        AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        intent.putExtra("id", id);
        intent.putExtra("time", "1 hour");
        PendingIntent pIntent = PendingIntent.getBroadcast(context.getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pIntent);
        // Repeating because the time value may be different and it is never certain, try both kinds of the intent
        intent.removeExtra("time");
        intent.putExtra("time", "10 minutes");
        pIntent = PendingIntent.getBroadcast(context.getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pIntent);
    }//cancelAlarm
}//ScheduleAdapter
