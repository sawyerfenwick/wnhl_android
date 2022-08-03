package com.wnhl.wnhl_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Creates Individual Teams Pages
 */
public class WNHL_Teams extends AppCompatActivity {

    DataBaseHelper dataBaseHelper;

    ImageView logo;
    RelativeLayout background;
    TableLayout tableLayout;
    TableRow tableRow;
    TextView info;
    TextView titleView;
    int currSeason;
    int teamID;
    String uri = "";
    int imageID;

    /**
     * Creates the Individual Team page based on ID passed via EXTRA INTENT
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wnhl_team);
        getSupportActionBar().hide();
        SharedPrefHelper sharedPrefHelper;
        dataBaseHelper = new DataBaseHelper(this);
        sharedPrefHelper = new SharedPrefHelper();
        titleView = (TextView)findViewById(R.id.teamTitle);

        background = (RelativeLayout)findViewById(R.id.teamBackground);
        logo = (ImageView)findViewById(R.id.teamLogo);
        Window window = getWindow();
        teamID = getIntent().getIntExtra("team_id",-1);

        switch (teamID){
            case 940:
                //Steelers
                background.setBackgroundColor(getResources().getColor(R.color.steelers,null));
                uri = "@drawable/steelers_nobackground";
                window.setStatusBarColor(getResources().getColor(R.color.steelers,null));
                imageID = getResources().getIdentifier(uri, null, getPackageName());
                logo.setImageDrawable(getResources().getDrawable(imageID,null));
                break;
            case 1370:
                //Townline
                background.setBackgroundColor(getResources().getColor(R.color.townline,null));
                uri = "@drawable/townline_nobackground";
                window.setStatusBarColor(getResources().getColor(R.color.townline,null));
                imageID = getResources().getIdentifier(uri, null, getPackageName());
                logo.setImageDrawable(getResources().getDrawable(imageID,null));
                break;
            case 1371:
                //CrownRoom
                background.setBackgroundColor(getResources().getColor(R.color.crownroomkings,null));
                window.setStatusBarColor(getResources().getColor(R.color.crownroomkings,null));
                uri = "@drawable/crownroom_nobackground";
                imageID = getResources().getIdentifier(uri, null, getPackageName());
                logo.setImageDrawable(getResources().getDrawable(imageID,null));
                break;
            case 1810:
                //DainCity
                background.setBackgroundColor(getResources().getColor(R.color.dusters,null));
                window.setStatusBarColor(getResources().getColor(R.color.dusters,null));
                uri = "@drawable/dusters_nobackground";
                imageID = getResources().getIdentifier(uri, null, getPackageName());
                logo.setImageDrawable(getResources().getDrawable(imageID,null));
                break;
            case 1822:
                //Lincoln
                window.setStatusBarColor(getResources().getColor(R.color.lincoln,null));
                background.setBackgroundColor(getResources().getColor(R.color.lincoln,null));
                uri = "@drawable/legends_nobackground";
                imageID = getResources().getIdentifier(uri, null, getPackageName());
                logo.setImageDrawable(getResources().getDrawable(imageID,null));
                break;
            case 1824:
                //Merrit
                window.setStatusBarColor(getResources().getColor(R.color.merrit,null));
                background.setBackgroundColor(getResources().getColor(R.color.merrit,null));
                uri = "@drawable/islanders";
                imageID = getResources().getIdentifier(uri, null, getPackageName());
                logo.setImageDrawable(getResources().getDrawable(imageID,null));
                break;
        }
        currSeason = sharedPrefHelper.getSharedPreferencesInt(this,
                "current_season",-1);

        if(dataBaseHelper.getStandingsID(currSeason) == 0){
            currSeason = sharedPrefHelper.getSharedPreferencesInt(this, "previous_season",-1);
        }

        String teamName = dataBaseHelper.getTeamName(teamID);

        titleView.setText(teamName);

        try {
            init(teamID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<Integer> eventIDs, hscore, ascore, venues;
        List<String> titles, dates, times;

        eventIDs = dataBaseHelper.getEventIDsForTeam(teamID);
        titles = dataBaseHelper.getEventTitlesForTeam(teamID);
        venues = dataBaseHelper.getEventVenuesForTeam(teamID);
        dates = dataBaseHelper.getEventDatesForTeam(teamID);
        times = dataBaseHelper.getEventTimesForTeam(teamID);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        Date today = new Date();

        Iterator<Integer> iterator = eventIDs.iterator();
        Iterator<String> titleIterator = titles.iterator();
        Iterator<Integer> venuesIterator = venues.iterator();
        Iterator<String> dateIterator = dates.iterator();
        Iterator<String> timeIterator = times.iterator();

        while(iterator.hasNext()){
            int id = iterator.next();
            titleIterator.next();
            venuesIterator.next();
            dateIterator.next();
            timeIterator.next();
            try {
                String stringDate = dataBaseHelper.getEventDate(id) + " "
                        + dataBaseHelper.getEventTime(id);
                date = dateFormat.parse(stringDate);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (today.after(date)) {
                iterator.remove();
                titleIterator.remove();
                dateIterator.remove();
                timeIterator.remove();
                venuesIterator.remove();
            }
        }

        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(this, eventIDs, titles, dates, times, venues);

        eventIDs = dataBaseHelper.getEventIDsForTeam(teamID);
        titles = dataBaseHelper.getEventTitlesForTeam(teamID);
        venues = dataBaseHelper.getEventVenuesForTeam(teamID);
        dates = dataBaseHelper.getEventDatesForTeam(teamID);
        times = dataBaseHelper.getEventTimesForTeam(teamID);

        iterator = eventIDs.iterator();
        titleIterator = titles.iterator();
        venuesIterator = venues.iterator();
        dateIterator = dates.iterator();
        timeIterator = times.iterator();

        while(iterator.hasNext()){
            int id = iterator.next();
            titleIterator.next();
            venuesIterator.next();
            dateIterator.next();
            timeIterator.next();
            try {
                String stringDate = dataBaseHelper.getEventDate(id) + " "
                        + dataBaseHelper.getEventTime(id);
                date = dateFormat.parse(stringDate);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (today.before(date)) {
                iterator.remove();
                titleIterator.remove();
                dateIterator.remove();
                timeIterator.remove();
                venuesIterator.remove();
            }
        }

        PastGameScheduleAdapter pastGameScheduleAdapter = new PastGameScheduleAdapter(this,
                eventIDs, titles, dates, venues);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.teamRecyclerView);
        ConcatAdapter concatAdapter = new ConcatAdapter(pastGameScheduleAdapter, scheduleAdapter);
        recyclerView.setAdapter(concatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }//onCreate

    /**
     * Create the Team Table with Player data
     *
     * @param id
     * @throws JSONException
     */
    public void init(int id) throws JSONException {
        //Initialize and set up Standing Table
        tableLayout = (TableLayout) findViewById(R.id.teamStandings);
        tableRow = new TableRow(this);
        info = new TextView(this);
        info.setText("Pos");
        info.setTypeface(info.getTypeface(), Typeface.BOLD);
        info.setGravity(Gravity.CENTER);
        tableRow.addView(info);
        info = new TextView(this);
        info.setText("GP");
        info.setTypeface(info.getTypeface(), Typeface.BOLD);
        info.setGravity(Gravity.CENTER);
        tableRow.addView(info);
        info = new TextView(this);
        info.setText("W");
        info.setTypeface(info.getTypeface(), Typeface.BOLD);
        info.setGravity(Gravity.CENTER);
        tableRow.addView(info);
        info = new TextView(this);
        info.setText("L");
        info.setTypeface(info.getTypeface(), Typeface.BOLD);
        info.setGravity(Gravity.CENTER);
        tableRow.addView(info);
        info = new TextView(this);
        info.setText("T");
        info.setTypeface(info.getTypeface(), Typeface.BOLD);
        info.setGravity(Gravity.CENTER);
        tableRow.addView(info);
        info = new TextView(this);
        info.setText("PTS");
        info.setTypeface(info.getTypeface(), Typeface.BOLD);
        info.setGravity(Gravity.CENTER);
        tableRow.addView(info);
        info = new TextView(this);
        info.setText("GF");
        info.setTypeface(info.getTypeface(), Typeface.BOLD);
        info.setGravity(Gravity.CENTER);
        tableRow.addView(info);
        info = new TextView(this);
        info.setText("GA");
        info.setTypeface(info.getTypeface(), Typeface.BOLD);
        info.setGravity(Gravity.CENTER);
        tableRow.addView(info);
        tableLayout.addView(tableRow);

        JSONObject standings = new JSONObject(dataBaseHelper.getStandings(currSeason));

        for(int i = 0; i < standings.names().length(); i++){
            if(Integer.valueOf(standings.names().getString(i)) == teamID){
                JSONObject row = standings.getJSONObject(standings.names().getString(i));
                tableRow = new TableRow(this);
                info = new TextView(this);
                info.setText(row.getString("pos"));
                info.setGravity(Gravity.CENTER);
                tableRow.addView(info);
                info = new TextView(this);
                info.setText(row.getString("gp"));
                info.setGravity(Gravity.CENTER);
                tableRow.addView(info);
                info = new TextView(this);
                info.setText(row.getString("w"));
                info.setGravity(Gravity.CENTER);
                tableRow.addView(info);
                info = new TextView(this);
                info.setText(row.getString("l"));
                info.setGravity(Gravity.CENTER);
                tableRow.addView(info);
                info = new TextView(this);
                info.setText(row.getString("ties"));
                info.setGravity(Gravity.CENTER);
                tableRow.addView(info);
                info = new TextView(this);
                info.setText(row.getString("pts"));
                info.setGravity(Gravity.CENTER);
                tableRow.addView(info);
                info = new TextView(this);
                info.setText(row.getString("gf"));
                info.setGravity(Gravity.CENTER);
                tableRow.addView(info);
                info = new TextView(this);
                info.setText(row.getString("ga"));
                info.setGravity(Gravity.CENTER);
                tableRow.addView(info);
                tableLayout.addView(tableRow);
            }
        }

        //Initiate and set up Table
        tableLayout = (TableLayout) findViewById(R.id.teamTable);
        tableRow = new TableRow(this);
        info = new TextView(this);
        info.setText(" Player ");
        info.setTypeface(info.getTypeface(), Typeface.BOLD);
        info.setGravity(Gravity.CENTER);
        tableRow.addView(info);
        info = new TextView(this);
        info.setText(" P");
        info.setTypeface(info.getTypeface(), Typeface.BOLD);
        info.setGravity(Gravity.CENTER);
        tableRow.addView(info);
        info = new TextView(this);
        info.setText(" G ");
        info.setTypeface(info.getTypeface(), Typeface.BOLD);
        info.setGravity(Gravity.CENTER);
        tableRow.addView(info);
        info = new TextView(this);
        info.setText(" A ");
        info.setTypeface(info.getTypeface(), Typeface.BOLD);
        info.setGravity(Gravity.CENTER);
        tableRow.addView(info);
        tableLayout.addView(tableRow);

        //Find players of specific team

        List<Integer> playerIDs;
        playerIDs = dataBaseHelper.getPlayerIDsOfTeam(id);
        //Build table

        for (int i = 0; i < playerIDs.size(); i++) {
            tableRow = new TableRow(this);
            info = new TextView(this);
            info.setText(dataBaseHelper.getPlayerName(playerIDs.get(i)));
            info.setGravity(Gravity.CENTER);
            tableRow.addView(info);

            info = new TextView(this);
            info.setText(String.valueOf(dataBaseHelper.getCurrentPoints(playerIDs.get(i))));
            info.setGravity(Gravity.CENTER);
            tableRow.addView(info);
            info = new TextView(this);
            info.setText(String.valueOf(dataBaseHelper.getCurrentGoals(playerIDs.get(i))));
            info.setGravity(Gravity.CENTER);
            tableRow.addView(info);
            info = new TextView(this);
            info.setText(String.valueOf(dataBaseHelper.getCurrentAssists(playerIDs.get(i))));
            info.setGravity(Gravity.CENTER);
            tableRow.addView(info);
            tableLayout.addView(tableRow);
        }
    }//init
}//WNHL_Teams