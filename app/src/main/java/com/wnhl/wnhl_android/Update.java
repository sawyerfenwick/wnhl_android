package com.wnhl.wnhl_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Update extends AppCompatActivity {

    DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
    RequestQueue queue;
    ProgressDialog dialog;

    List<String> slugList = new ArrayList<>();
    List<String> players = new ArrayList<>();
    List<Integer> ids = new ArrayList<>();

    private int numberOfTopRequests = 5;   //initial requests
    private int numberOfCalendarRequests; //calendar requests
    private int numberOfEventRequests;  //event requests
    private int numberOfPlayerRequests; //player requests
    private final boolean hasRequestFailed = false;   //checking failed volley requests

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

    boolean updateAll = false;

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wnhl_update);
        getSupportActionBar().hide();

        queue = Volley.newRequestQueue(getApplicationContext());

        Button playerButton = findViewById(R.id.playerButton);
        Button gamesButton = findViewById(R.id.gamesButton);
        Button teamButton = findViewById(R.id.teamButton);
        Button standingsButton = findViewById(R.id.standingsButton);
        Button everythingButton = findViewById(R.id.everythingButton);

        playerButton.setOnClickListener(v -> updatePlayers());
        gamesButton.setOnClickListener(v -> updateGames());
        teamButton.setOnClickListener(v -> updateTeams());
        standingsButton.setOnClickListener(v -> updateStandings());
        everythingButton.setOnClickListener(v -> updateEverything());
    }//onCreate

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

    public void updatePlayers(){
        if(connected()){
            dialog = ProgressDialog.show(this, "",
                    "Updating Players.\nPlease Wait, This Will Take A Minute...", true);
            dialog.show();
            dataBaseHelper.dropTable(DataBaseHelper.PLAYER_TABLE);

            queue.add(createStatsRequest());
        }
        else{
            Toast.makeText(this,"No Internet Connection. Please Try Again Later.", Toast.LENGTH_LONG).show();
        }
    }

    public void updateGames(){
        if(connected()){
            dialog = ProgressDialog.show(this, "",
                    "Updating Game Schedule.\nPlease Wait...\nDo Not Close The App...", true);
            dialog.show();

            dataBaseHelper.dropTable(DataBaseHelper.GAMES_TABLE);

            slugList.clear();

            for(int i = 0; i < dataBaseHelper.getAllTeamNames().size(); i++){
                try {
                    slugList.add(dataBaseHelper.getTeamsColumnSlug(dataBaseHelper.getAllTeamIDs().get(i)));
                } catch (JSONException e) {
                    Toast.makeText(this,"Error Updating. Please Try Again Later.",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
            startCalendarRequest();
        }
        else{
            Toast.makeText(this,"No Internet Connection. Please Try Again Later.", Toast.LENGTH_LONG).show();
        }
    }

    public void updateTeams(){
        if(connected()){
            dialog = ProgressDialog.show(this, "",
                    "Updating Teams.\nPlease wait...", true);
            dialog.show();

            dataBaseHelper.dropTable(DataBaseHelper.TEAMS_TABLE);

            queue.add(createTeamRequest());
        }
        else {
            Toast.makeText(this,"No Internet Connection. Please Try Again Later.", Toast.LENGTH_LONG).show();
        }
    }

    public void updateStandings(){
        if(connected()){
            dialog = ProgressDialog.show(this, "",
                    "Updating Standings.\nPlease wait...", true);
            dialog.show();

            dataBaseHelper.dropTable(DataBaseHelper.STANDINGS_TABLE);

            queue.add(createStandingsRequest());
        }
        else{
            Toast.makeText(this,"No Internet Connection. Please Try Again Later.", Toast.LENGTH_LONG).show();
        }

    }

    public void updateEverything(){
        if(connected()){
            updateAll = true;
            dialog = ProgressDialog.show(this, "",
                    "Updating WNHL Application.\nPlease Wait, This Will Take A Minute...", true);
            dialog.show();

            dataBaseHelper.dropTable(DataBaseHelper.PLAYER_TABLE);
            dataBaseHelper.dropTable(DataBaseHelper.GAMES_TABLE);
            dataBaseHelper.dropTable(DataBaseHelper.TEAMS_TABLE);
            dataBaseHelper.dropTable(DataBaseHelper.STANDINGS_TABLE);
            dataBaseHelper.dropTable(DataBaseHelper.SEASONS_TABLE);
            dataBaseHelper.dropTable(DataBaseHelper.VENUES_TABLE);

            queue.add(createTeamRequest());
            queue.add(createStatsRequest());
            queue.add(createVenuesRequest());
            queue.add(createSeasonsRequest());
            queue.add(createStandingsRequest());
        }
        else{
            Toast.makeText(this,"No Internet Connection. Please Try Again Later.", Toast.LENGTH_LONG).show();
        }

    }//updateEverything

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
                            if(!Objects.requireNonNull(data.names()).getString(i).equals("0")){
                                players.add(Objects.requireNonNull(data.names()).getString(i));
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this,"Error Updating. Please Try Again Later.",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    if(updateAll){
                        if(numberOfTopRequests == 0){
                            if(!hasRequestFailed){
                                startLowerRequests();
                            }
                            else{
                                //At least one request failed
                                Toast.makeText(this,"Error Updating. Please Try Again Later.",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    }
                    else{
                        startPlayerUpdate();
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
                Toast.makeText(this,"Error Updating. Please Try Again Later.",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            if(updateAll){
                if(numberOfTopRequests == 0){
                    if(!hasRequestFailed){
                        startLowerRequests();
                    }
                    else{
                        //At least one request failed
                        Toast.makeText(this,"Error Updating. Please Try Again Later.",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }
            else{
                numberOfTopRequests = 5;
                dialog.dismiss();
                Toast.makeText(this,"Team Data Updated!", Toast.LENGTH_SHORT).show();
            }
        }, Throwable::printStackTrace);
        teamRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return teamRequest;
    }//createTeamRequest

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
                    JSONObject venues = response.getJSONObject(i);
                    venueName = venues.getString("name");
                    venueID = venues.getInt("id");
                    dataBaseHelper.addVenue(venueID, venueName);
                }
            } catch (JSONException e) {
                Toast.makeText(this,"Error Updating. Please Try Again Later.",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            if(numberOfTopRequests == 0){
                if(!hasRequestFailed){
                    startLowerRequests();
                }
                else{
                    //At least one request failed
                    Toast.makeText(this,"Error Updating. Please Try Again Later.",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
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

                    JSONObject seasons = response.getJSONObject(i);
                    seasonName = seasons.getString("name");
                    seasonID = seasons.getInt("id");
                    dataBaseHelper.addSeason(seasonID, seasonName);
                    if(i == response.length()-2){
                        SharedPrefHelper.putSharedPreferencesInt(this,
                                "previous_season", seasonID);
                    }
                    if(i == response.length()-1){
                        SharedPrefHelper.putSharedPreferencesInt(this,
                                "current_season",seasonID);
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(this,"Error Updating. Please Try Again Later.",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            if(numberOfTopRequests == 0){
                if(!hasRequestFailed){
                    startLowerRequests();
                }
                else{
                    //At least one request failed
                    Toast.makeText(this,"Error Updating. Please Try Again Later.",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
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
                        Toast.makeText(this,"Error Updating. Please Try Again Later.",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    if(updateAll){
                        if(numberOfTopRequests == 0){
                            if(!hasRequestFailed){
                                startLowerRequests();
                            }
                            else{
                                //At least one request failed
                                Toast.makeText(this,"Error Updating. Please Try Again Later.",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    }
                    else{
                        numberOfTopRequests = 5;
                        dialog.dismiss();
                        Toast.makeText(this,"Standings Data Updated!", Toast.LENGTH_SHORT).show();
                    }
                }, Throwable::printStackTrace);
        standingsRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return standingsRequest;
    }//createStandingsRequest

    public void startLowerRequests(){
        startPlayerUpdate();
        startCalendarRequest();
    }

    public void startPlayerUpdate(){
        numberOfPlayerRequests = players.size();
        for(int i = 0; i < players.size(); i++){
            getPlayers(players.get(i));
        }
    }

    public void startCalendarRequest(){
        numberOfCalendarRequests = slugList.size();
        for(int i = 0; i < slugList.size(); i++){
            queue.add(createCalendarsRequest(slugList.get(i)));
        }
    }

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
                        if(updateAll){
                            if(numberOfEventRequests == 0 & numberOfPlayerRequests == 0){
                                updateAll = false;
                                dialog.dismiss();
                                Toast.makeText(this,"Update Complete!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            if(numberOfEventRequests == 0){
                                numberOfTopRequests = 5;
                                dialog.dismiss();
                                Toast.makeText(this,"Game Schedule Updated!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, Throwable::printStackTrace);
            eventRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(eventRequest);
        }
    }//getEvents

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
                        Toast.makeText(this,"Error Updating. Please Try Again Later.",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    if(updateAll){
                        if(numberOfEventRequests == 0 & numberOfPlayerRequests == 0){
                            updateAll = false;
                            dialog.dismiss();
                            Toast.makeText(this,"Update Complete!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        if(numberOfPlayerRequests == 0){
                            numberOfTopRequests = 5;
                            dialog.dismiss();
                            Toast.makeText(this,"Player Data Updated!",Toast.LENGTH_SHORT).show();
                        }
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
                        Toast.makeText(this,"Error Updating. Please Try Again Later.",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }, error -> {
            //ADD NULL TO THE DATABASE DUH DOI
        });
        queue.add(mediaRequest);
    }//getMediaURL
}
