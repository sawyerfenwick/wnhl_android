package com.wnhl.wnhl_android;

//Android Handling
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//Volley Request Handling
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

//JSON Handling
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//DateTime Handling
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * MainActivity acts as a splash page for the application.
 * If the app is being run for the first time it builds the wnhl database: wnhl.db
 * and populates the database using Volley requests from the SportsPress API associated with
 * the WordPress site.
 * On subsequent application runs, two tables are updated: Players and Standings since all
 * other data will remain the same throughout the season.
 * During the first week of a new season the application will update the following tables:
 * Standings
 * Players
 * Seasons
 * Venues
 * Teams
 * and will rewrite the Future Games Table.
 *
 * @author Sawyer Fenwick | Daniel Figueroa
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    RequestQueue queue;
    DataBaseHelper dataBaseHelper;

    List<Integer> ids = new ArrayList<>();
    List<String> slugList = new ArrayList<>();
    List<String> players = new ArrayList<>();
    List<Integer> eventIds = new ArrayList<>();

    private int numberOfTopRequests = 5;   //initial requests
    private int numberOfCalendarRequests; //calendar requests
    private int numberOfNewCalendarRequests; //new calendar requests
    private int numberOfEventRequests;  //event requests
    private int numberOfPlayerRequests; //player requests
    private int numberOfUpdateRequests; //event update requests
    private final boolean hasRequestFailed = false;   //checking failed volley requests

    TextView tv;        //loading message text view
    ProgressBar pb;     //progress bar
    ImageView logo;     //center logo

    //SportsPress API URLS
    String seasonsURL = "http://www.wnhlwelland.ca/wp-json/sportspress/v2/seasons/";
    String playersURL = "http://www.wnhlwelland.ca/wp-json/sportspress/v2/players/";
    String venuesURL = "http://www.wnhlwelland.ca/wp-json/sportspress/v2/venues/";
    String calendarURL = "http://www.wnhlwelland.ca/wp-json/sportspress/v2/calendars/";
    String eventsURL = "http://www.wnhlwelland.ca/wp-json/sportspress/v2/events/";
    String teamsURL = "http://www.wnhlwelland.ca/wp-json/sportspress/v2/teams/";
    String tablesURL = "http://www.wnhlwelland.ca/wp-json/sportspress/v2/tables/";
    String statsURL = "http://www.wnhlwelland.ca/wp-json/sportspress/v2/lists/1900";
    String mediaURL = "http://www.wnhlwelland.ca/wp-json/wp/v2/media/";

    //Volley requests will store data in these variables
    String teamName = "";
    String slug = "";
    String venueName = "";
    String seasonName = "";
    String playerName = "";
    String playerInfo = "";
    String leagues = "";
    String seasons = "";
    String prevTeams = "";
    String nat = "";
    String stats = "";
    int teamID;
    int venueID;
    int seasonID;
    int tableID;
    int playerNum;
    int currTeam;
    int currA;
    int currG;
    int currP;
    int mediaID;

    /**
     * Creates Main Activity
     * Determines which logo to display
     * Checks application run number
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wnhl_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        tv = (TextView)findViewById(R.id.loading);
        pb = (ProgressBar)findViewById(R.id.progressBar);
        dataBaseHelper = new DataBaseHelper(this);
        queue = Volley.newRequestQueue(MainActivity.this);
        //Check Date for Playoffs
        DateFormat dateFormat = new SimpleDateFormat("MM",Locale.CANADA);
        Date date = new Date();
        if (dateFormat.format(date).equals("02") | dateFormat.format(date).equals("03")){
            logo = (ImageView)findViewById(R.id.imageview);
            String uri = "@drawable/wnhl_championship";
            int resID = getResources().getIdentifier(uri, null, getPackageName());
            logo.setImageDrawable(ResourcesCompat.getDrawable(getResources(), resID, null));
        }
        checkFirstRun();
    }//onCreate

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (!Arrays.asList(grantResults).contains(PackageManager.PERMISSION_DENIED)) {
                //all permissions have been granted
                checkFirstRun();
            }
        }
    }

    /**
     * Checks if app is running for first time
     */
    private void checkFirstRun(){
        final String PREFS_NAME = "WNHL_Prefs";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        //Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        //Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        //Check for first run or upgrade
        if(currentVersionCode == savedVersionCode){
            //normal run - Update the DB
            updateDataBase();
        }
        else if(savedVersionCode == DOESNT_EXIST){
            //new install - Request File Permissions, Build DB
            buildDataBase();
        }
        else if(currentVersionCode > savedVersionCode){
            //upgrade - Update the DB
            updateDataBase();
        }
    }//checkFirstRun

    /**
     * Update the Players and Standings Table in the Database
     */
    private void updateDataBase(){
        pb.setVisibility(View.VISIBLE);
        tv.setText(R.string.update);
        if(connected()){
            //dataBaseHelper.addGame(1,"test vs test", 1371, 1370, -1,-1,"2021-10-07","22:00:00",15);
            updateSeasonsTable();
        }else{
            new Handler().postDelayed(() -> {
                startActivity(new Intent(MainActivity.this,
                        WNHL_Schedule.class));
                finish();
                Toast.makeText(getApplication(), "No Internet Connection.\nCannot Update WNHL App."
                        ,Toast.LENGTH_LONG).show();
            }, 2000);
        }
    }//updateDataBase

    /**
     * Check to see if games data has been updated
     *
     */
    public void updateSeasonsTable(){
        dataBaseHelper.dropTable("SEASONS_TABLE");
        dataBaseHelper.createSeasonsTable();
        numberOfUpdateRequests = 1;
        queue.add(updateSeasonsRequest());
    }//updateSeasonsTable

    /**
     * Update Games already in the Database
     */
    private void updateCurrentSeasonGames(){
        eventIds = dataBaseHelper.getEventIDs();
        numberOfUpdateRequests = eventIds.size();
        for(int i = 0; i < eventIds.size(); i++) {
            getEvents(eventIds.get(i));
        }
    }//updateCurrentSeasonGames

    /**
     * Create the Volley Requests required to download all data from SportsPress API
     */
    private void buildDataBase() {
        //loading bar visibility
        pb.setVisibility(View.VISIBLE);
        tv.setText(R.string.create);

        //download data
        queue.add(createTeamRequest());
        queue.add(createStatsRequest());
        queue.add(createVenuesRequest());
        queue.add(createSeasonsRequest());
        queue.add(createStandingsRequest());
    }//buildDataBase

    /**
     * Download JSONArray for Seasons, grab pertinent information and insert into database
     *
     * @return seasonsRequest
     */
    public JsonArrayRequest updateSeasonsRequest(){
        JsonArrayRequest seasonsRequest = new JsonArrayRequest(Request.Method.GET, seasonsURL,
                null, response -> {
            try {
                numberOfUpdateRequests--;
                for(int i = 0; i < response.length(); i++){
                    JSONObject seasons = response.getJSONObject(i);
                    seasonName = seasons.getString("name");
                    seasonID = seasons.getInt("id");
                    dataBaseHelper.addSeason(seasonID, seasonName);
                    if(i == response.length()-2){
                        SharedPrefHelper.putSharedPreferencesInt(MainActivity.this,
                                "previous_season", seasonID);
                    }
                    if(i == response.length()-1){
                        SharedPrefHelper.putSharedPreferencesInt(MainActivity.this,
                                "current_season",seasonID);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(numberOfUpdateRequests == 0){
                String gameDate = dataBaseHelper.getLastGameDate();
                try {
                    Date date=new SimpleDateFormat("yyyy-MM-dd").parse(gameDate);
                    Date now = new Date();
                    if (now.after(date)) {
                       //If today is after the last date, check if a new season calendar exists
                        dataBaseHelper.dropTable("GAMES_TABLE");
                        dataBaseHelper.createGamesTable();
                        getNewSeasonGames();
                    }
                    else{
                        updateCurrentSeasonGames();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, Throwable::printStackTrace);
        seasonsRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return seasonsRequest;
    }//createSeasonsRequest

    /**
     *
     */
    private void getNewSeasonGames(){
        slugList = dataBaseHelper.getAllTeamSlugs();

        numberOfNewCalendarRequests = slugList.size();
        for(int i = 0; i < slugList.size(); i++){
            queue.add(createNewCalendarRequest(slugList.get(i)));
        }
    }//getNewSeasonsGames

    /**
     * Download JSONArray for Schedule
     *
     * @param slug The slug for searching - www.wnhlwelland.ca/calendars/slug
     * @return calendarRequests
     */
    public JsonArrayRequest createNewCalendarRequest(String slug){
        String url = calendarURL + "?slug=" + slug;
        return new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                numberOfNewCalendarRequests--;
                JSONObject calendar = response.getJSONObject(0);
                JSONArray data = calendar.getJSONArray("data");
                for(int i = 0; i < data.length(); i++){
                    if(!ids.contains(data.getJSONObject(i).getInt("ID"))){
                        ids.add(data.getJSONObject(i).getInt("ID"));
                    }
                }
            } catch (JSONException e) {
                //do nothing
            }
            if(numberOfNewCalendarRequests == 0) {
                numberOfPlayerRequests = 0;
                numberOfEventRequests = ids.size();
                for(int i = 0; i < ids.size(); i++){
                    getEvents(ids.get(i));
                }
            }
        }, error -> {

        });
    }//createPastCalendarsRequest

    /**
     * Download JSONArray for Schedule
     *
     * @param slug The slug for searching - www.wnhlwelland.ca/calendars/slug
     * @return calendarRequests
     */
    public JsonArrayRequest createCalendarsRequest(String slug){
        String url = calendarURL + "?slug=" + slug;
        return new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                numberOfCalendarRequests--;
                JSONObject calendar = response.getJSONObject(0);
                JSONArray data = calendar.getJSONArray("data");
                for(int i = 0; i < data.length(); i++){
                    if(!ids.contains(data.getJSONObject(i).getInt("ID"))){
                        ids.add(data.getJSONObject(i).getInt("ID"));
                    }
                }
            } catch (JSONException e) {
                //do nothing
            }
            if(numberOfCalendarRequests == 0) {
                numberOfEventRequests = ids.size();
                for(int i = 0; i < ids.size(); i++){
                    getEvents(ids.get(i));
                }
            }
        }, error -> {

        });
    }//createPastCalendarsRequest

    /**
     * Download JSONObject for Statistics, grab pertinent information and insert into database
     *
     * @return statsRequest
     */
    public JsonObjectRequest createStatsRequest(){
        JsonObjectRequest statsRequest = new JsonObjectRequest(Request.Method.GET, statsURL,
                null,
                list -> {
                    try {
                        numberOfTopRequests--;
                        JSONObject data = list.getJSONObject("data");
                        for(int i = 0; i < data.length(); i++){
                            tv.setText(R.string.downloading);
                            if(!Objects.requireNonNull(data.names()).getString(i).equals("0")){
                                players.add(Objects.requireNonNull(data.names()).getString(i));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(numberOfTopRequests == 0){
                        if(!hasRequestFailed){
                            startLowerRequests();
                        }
                        else{
                            //At least one request failed
                            Toast.makeText(this,"Error Downloading Data.",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, Throwable::printStackTrace);
        statsRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return statsRequest;
    }//createStatsRequest

    /**
     * Download JSONArray for Teams, grab pertinent information and insert into the database
     *
     * @return teamRequest
     */
    public JsonArrayRequest createTeamRequest(){
        JsonArrayRequest teamRequest = new JsonArrayRequest(Request.Method.GET, teamsURL,
                null, response -> {
                    try {
                        numberOfTopRequests--;
                        for(int i = 0; i < response.length(); i++){
                            tv.setText(R.string.downloading);
                            JSONObject team = response.getJSONObject(i);
                            JSONObject title = team.getJSONObject("title");
                            slug = team.getString("slug");
                            slugList.add(slug);
                            teamName = title.getString("rendered");
                            teamID = team.getInt("id");
                            seasons = team.getJSONArray("seasons").toString();
                            dataBaseHelper.addTeam(teamID, teamName, slug, seasons);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(numberOfTopRequests == 0){
                        if(!hasRequestFailed){
                            startLowerRequests();
                        }
                        else{
                            //At least one request failed
                            Toast.makeText(this,"Error Downloading Data.",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, Throwable::printStackTrace);
        teamRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return teamRequest;
    }//createTeamRequest

    public void startLowerRequests(){
        numberOfCalendarRequests = slugList.size();
        for(int i = 0; i < slugList.size(); i++){
            queue.add(createCalendarsRequest(slugList.get(i)));
        }
        numberOfPlayerRequests = players.size();
        for(int i = 0; i < players.size(); i++){
            getPlayers(players.get(i));
        }
    }

    /**
     * Download JSONArray for Venues, grab pertinent information and insert into database
     *
     * @return venuesRequest
     */
    public JsonArrayRequest createVenuesRequest(){
        JsonArrayRequest venuesRequest = new JsonArrayRequest(Request.Method.GET, venuesURL,
                null, response -> {
                    try {
                        numberOfTopRequests--;
                        for(int i = 0; i < response.length(); i++){
                            tv.setText(R.string.downloading);
                            JSONObject venues = response.getJSONObject(i);
                            venueName = venues.getString("name");
                            venueID = venues.getInt("id");
                            dataBaseHelper.addVenue(venueID, venueName);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(numberOfTopRequests == 0){
                        if(!hasRequestFailed){
                            startLowerRequests();
                        }
                        else{
                            //At least one request failed
                            Toast.makeText(this,"Error Downloading Data.",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, Throwable::printStackTrace);
        venuesRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return venuesRequest;
    }//createVenuesRequest

    /**
     * Download JSONArray for Seasons, grab pertinent information and insert into database
     *
     * @return seasonsRequest
     */
    public JsonArrayRequest createSeasonsRequest(){
        JsonArrayRequest seasonsRequest = new JsonArrayRequest(Request.Method.GET, seasonsURL,
                null, response -> {
                    try {
                        numberOfTopRequests--;
                        for(int i = 0; i < response.length(); i++){
                            tv.setText(R.string.downloading);
                            JSONObject seasons = response.getJSONObject(i);
                            seasonName = seasons.getString("name");
                            seasonID = seasons.getInt("id");
                            dataBaseHelper.addSeason(seasonID, seasonName);
                            if(i == response.length()-2){
                                SharedPrefHelper.putSharedPreferencesInt(MainActivity.this,
                                        "previous_season", seasonID);
                            }
                            if(i == response.length()-1){
                                SharedPrefHelper.putSharedPreferencesInt(MainActivity.this,
                                        "current_season",seasonID);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(numberOfTopRequests == 0){
                        if(!hasRequestFailed){
                            startLowerRequests();
                        }
                        else{
                            //At least one request failed
                            Toast.makeText(this,"Error Downloading Data.",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, Throwable::printStackTrace);
        seasonsRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return seasonsRequest;
    }//createSeasonsRequest

    /**
     * Download JSONArray for Standings, grab pertinent information and insert into database
     *
     * @return standingsRequest
     */
    public JsonArrayRequest createStandingsRequest(){
        JsonArrayRequest standingsRequest = new JsonArrayRequest(Request.Method.GET, tablesURL,
                null,
                tables -> {
                    try {
                        numberOfTopRequests--;
                        String data;
                        for(int i = 0; i < tables.length(); i++){
                            tv.setText(R.string.downloading);
                            JSONObject table = tables.getJSONObject(i);
                            JSONObject titleOb = table.getJSONObject("title");
                            if(!titleOb.getString("rendered").equals("")){
                                tableID = table.getInt("id");
                                seasonID = table.getJSONArray("seasons")
                                        .getInt(table.getJSONArray("seasons")
                                                .length()-1);
                                try{
                                    data = table.getJSONObject("data").toString();
                                    dataBaseHelper.addStandings(tableID, data, seasonID);
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(numberOfTopRequests == 0){
                        if(!hasRequestFailed){
                            startLowerRequests();
                        }
                        else{
                            //At least one request failed
                            Toast.makeText(this,"Error Downloading Data.",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, Throwable::printStackTrace);
        standingsRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return standingsRequest;
    }//createStandingsRequest

    /**
     * Each player is an individual JSONObject in the form: www.wnhlwelland.ca/..../players/ID
     * where ID = pID. Get JSONObject and download the pertinent information, insert into database
     *
     * @param pID the ID of the player
     */
    private void getPlayers(String pID){
        int p = Integer.parseInt(pID);
        String url = playersURL + pID;

        JsonObjectRequest playerRequest = new JsonObjectRequest(Request.Method.GET, url,
                null,
                player -> {
                    try {
                        numberOfPlayerRequests--;
                        JSONObject title = player.getJSONObject("title");
                        playerName = title.getString("rendered");
                        JSONObject content = player.getJSONObject("content");
                        playerInfo = content.getString("rendered").replaceAll("<p>"
                                ,"");
                        playerInfo = playerInfo.replaceAll("</p>","");
                        playerInfo = playerInfo.replaceAll("&#8212;","—");
                        playerInfo = playerInfo.replaceAll("&#8217;","'");
                        playerInfo = playerInfo.replaceAll("&#8220;", "\"");
                        playerInfo = playerInfo.replaceAll("&#8221;","\"");
                        playerInfo = playerInfo.replaceAll("<p style=\"text-align: " +
                                "left;\">","");
                        leagues = player.getJSONArray("leagues").toString();
                        seasons = player.getJSONArray("seasons").toString();
                        mediaID = player.getInt("featured_media");
                        if(mediaID!=0){
                            getMediaURL(mediaID);
                        }
                        try{
                            playerNum = player.getInt("number");
                        }
                        catch(Exception e){
                            playerNum = -1;
                        }
                        prevTeams = player.getJSONArray("past_teams").toString();
                        try{
                            currTeam = player.getJSONArray("current_teams")
                                    .getInt(0);
                            if(currTeam == 0){
                                currTeam = player.getJSONArray("current_teams")
                                        .getInt(1);
                            }
                        }
                        catch(Exception e){
                            currTeam = -1;
                        }
                        nat = player.getJSONArray("nationalities").toString();
                        stats = player.getJSONObject("statistics").toString();

                        try{
                            JSONObject object = new JSONObject(stats);
                            JSONObject data = object.getJSONObject("3");

                            JSONObject playerStats = data.getJSONObject(String.valueOf(SharedPrefHelper.getSharedPreferencesInt(this,"current_season",-1)));
                            currG = playerStats.getInt("g");
                            currA = playerStats.getInt("a");
                            currP = Integer.parseInt(playerStats.getString("p"));
                        }
                        catch(Exception e){
                            currG = 0;
                            currA = 0;
                            currP = 0;
                        }
                        dataBaseHelper.addPlayer(p, playerName, playerInfo, leagues, seasons,
                                playerNum, prevTeams, currTeam, nat, currG, currA, currP,
                                stats, mediaID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(numberOfEventRequests == 0 & numberOfPlayerRequests == 0){
                        tv.setText(R.string.finishing);
                        new Handler().postDelayed(() -> {
                            tv.setText(R.string.complete);
                            startActivity(new Intent(MainActivity.this, WNHL_Schedule.class));
                            finish();
                        }, 1000);
                    }
                }, Throwable::printStackTrace);
        playerRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(playerRequest);
    }//getPlayers

    /**
     *
     * @param mediaID ID to locate player picture on WNHL site
     */
    private void getMediaURL(int mediaID){
        String url = mediaURL + mediaID;
        JsonObjectRequest mediaRequest = new JsonObjectRequest(Request.Method.GET, url,
                null,
                media -> {
                    try{
                        JSONObject guid = media.getJSONObject("guid");
                        dataBaseHelper.addFeaturedMedia(mediaID,guid.getString("rendered"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    //ADD NULL TO THE DATABASE DUH DOI
                });
        queue.add(mediaRequest);
    }//getMediaURL

    /**
     * Retrieves the Events from their specific Event URL
     *
     * @param eID the Event ID
     */
    private void getEvents(int eID) {
        if(!dataBaseHelper.gameInDB(eID)){
            String evID = String.valueOf(eID);
            String url = eventsURL + evID;
            JsonObjectRequest eventRequest = new JsonObjectRequest(Request.Method.GET, url,
                    null,
                    event -> {
                        int venue;
                        int home;
                        int away;
                        int hscore;
                        int ascore;
                        try {
                            numberOfEventRequests--;
                            JSONObject getTitle = event.getJSONObject("title");
                            JSONArray getVenue = event.getJSONArray("venues");
                            JSONArray getTeams = event.getJSONArray("teams");
                            JSONArray mainResults = event.getJSONArray("main_results");
                            try{
                                venue = getVenue.getInt(0);
                            }
                            catch(Exception e){
                                venue = -1;
                            }
                            try{
                                home = getTeams.getInt(0);
                            }
                            catch(Exception e){
                                home = -1;
                            }
                            try{
                                away = getTeams.getInt(1);
                            }
                            catch (Exception e){
                                away = -1;
                            }
                            try{
                                hscore = mainResults.getInt(0);
                            }
                            catch (Exception e){
                                hscore = -1;
                            }
                            try{
                                ascore = mainResults.getInt(1);
                            }
                            catch (Exception e){
                                ascore = -1;
                            }
                            String title = getTitle.getString("rendered");
                            String getDate = event.getString("date");
                            String[] parts = getDate.split("T");
                            String date = parts[0];
                            String time = parts[1];
                            dataBaseHelper.addGame(eID, title, home, away, hscore,
                                    ascore, date, time, venue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(numberOfEventRequests == 0 & numberOfPlayerRequests == 0){
                            tv.setText(R.string.finishing);
                            new Handler().postDelayed(() -> {
                            tv.setText(R.string.complete);
                            startActivity(new Intent(MainActivity.this, WNHL_Schedule.class));
                            finish();
                            }, 1000);
                        }
                    }, Throwable::printStackTrace);
            eventRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(eventRequest);
        }
        else{
            String evID = String.valueOf(eID);
            String url = eventsURL + evID;
            JsonObjectRequest eventRequest = new JsonObjectRequest(Request.Method.GET, url,
                    null,
                    event -> {
                        int venue;
                        int home;
                        int away;
                        int hscore;
                        int ascore;
                        try {
                            numberOfUpdateRequests--;
                            JSONObject getTitle = event.getJSONObject("title");
                            JSONArray getVenue = event.getJSONArray("venues");
                            JSONArray getTeams = event.getJSONArray("teams");
                            JSONArray mainResults = event.getJSONArray("main_results");
                            try{
                                venue = getVenue.getInt(0);
                            }
                            catch(Exception e){
                                venue = -1;
                            }
                            try{
                                home = getTeams.getInt(0);
                            }
                            catch(Exception e){
                                home = -1;
                            }
                            try{
                                away = getTeams.getInt(1);
                            }
                            catch (Exception e){
                                away = -1;
                            }
                            try{
                                hscore = mainResults.getInt(0);
                            }
                            catch (Exception e){
                                hscore = -1;
                            }
                            try{
                                ascore = mainResults.getInt(1);
                            }
                            catch (Exception e){
                                ascore = -1;
                            }
                            String title = getTitle.getString("rendered");
                            String getDate = event.getString("date");
                            String[] parts = getDate.split("T");
                            String date = parts[0];
                            String time = parts[1];
                            dataBaseHelper.updateGame(eID, title, home, away, hscore,
                                    ascore, date, time, venue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(numberOfUpdateRequests == 0){
                            tv.setText(R.string.finishing);
                            new Handler().postDelayed(() -> {
                                tv.setText(R.string.complete);
                                startActivity(new Intent(MainActivity.this, WNHL_Schedule.class));
                                finish();
                            }, 1000);
                        }
                    }, Throwable::printStackTrace);
            eventRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(eventRequest);
        }
    }//getEvents

    /**
     * Checks if phone is connected to the internet
     *
     * @return True if connected to internet
     */
    public boolean connected(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        //we are connected to a network
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }//connected
}//MainActivity