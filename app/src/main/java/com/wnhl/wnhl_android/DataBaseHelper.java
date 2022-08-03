package com.wnhl.wnhl_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * The DataBaseHelper class handles the creation of the database and db queries
 *
 * @author Sawyer Fenwick | Daniel Figueroa
 * @version 1.0
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    Context appContext;

    //TEAMS_TABLE
    public static final String TEAMS_TABLE = "TEAMS_TABLE";
    public static final String TEAMS_COLUMN_NAME = "TEAM_NAME";
    public static final String TEAMS_COLUMN_SLUG = "SLUG";
    public static final String TEAMS_SEASON_ID = "SEASON_ID";
    public static final String TEAMS_COLUMN_ID = "ID";
    //STANDINGS_TABLE
    public static final String STANDINGS_TABLE = "STANDINGS_TABLE";
    public static final String STANDINGS_COLUMN_ID = "ID";
    public static final String STANDINGS_COLUMN_DATA = "DATA";
    public static final String STANDINGS_COLUMN_SEASON = "SEASON";
    //GAMES TABLE
    public static final String GAMES_TABLE = "GAMES_TABLE";
    public static final String GAMES_COLUMN_ID = "EVENT_ID";
    public static final String GAMES_COLUMN_TITLE = "TITLE";
    public static final String GAMES_COLUMN_HOME = "HOME";
    public static final String GAMES_COLUMN_AWAY = "AWAY";
    public static final String GAMES_COLUMN_HOME_SCORE = "HOME_SCORE";
    public static final String GAMES_COLUMN_AWAY_SCORE = "AWAY_SCORE";
    public static final String GAMES_COLUMN_DATE = "DATE";
    public static final String GAMES_COLUMN_TIME = "TIME";
    public static final String GAMES_COLUMN_VENUE = "VENUE";
    //VENUES_TABLE
    public static final String VENUES_TABLE = "VENUES_TABLE";
    public static final String VENUES_COLUMN_ID = "ID";
    public static final String VENUES_COLUMN_NAME = "NAME";
    //PLAYER_TABLE
    public static final String PLAYER_TABLE = "PLAYER_TABLE";
    public static final String PLAYER_COLUMN_ID = "PLAYER_ID";
    public static final String PLAYER_COLUMN_NAME = "NAME";
    public static final String PLAYER_COLUMN_CONTENT = "CONTENT";
    public static final String PLAYER_COLUMN_LEAGUES = "LEAGUES";
    public static final String PLAYER_COLUMN_SEASONS = "SEASONS";
    public static final String PLAYER_COLUMN_PLAYER_NUMBER = "PLAYER_NUMBER";
    public static final String PLAYER_COLUMN_PREVIOUS_TEAMS = "PREVIOUS_TEAMS";
    public static final String PLAYER_COLUMN_CURRENT_TEAM = "CURRENT_TEAM";
    public static final String PLAYER_COLUMN_NATIONALITY = "NATIONALITY";
    public static final String PLAYER_COLUMN_CURR_GOALS = "CURRENT_GOALS";
    public static final String PLAYER_COLUMN_CURR_ASSISTS = "CURRENT_ASSISTS";
    public static final String PLAYER_COLUMN_CURR_POINTS = "CURRENT_POINTS";
    public static final String PLAYER_COLUMN_STATS = "STATS";
    public static final String PLAYER_COLUMN_MEDIA_ID = "MEDIA_ID";
    public static final String PLAYER_COLUMN_FEATURED_MEDIA = "FEATURED_MEDIA";
    //SEASONS_TABLE
    public static final String SEASONS_TABLE = "SEASONS_TABLE";
    public static final String SEASONS_COLUMN_ID = "SEASON_ID";
    public static final String SEASONS_COLUMN_NAME = "SEASON_NAME";

    /**
     * Calls the super constructor for SQLiteOpenHelper
     *
     * @param context Application Context
     */
    public DataBaseHelper(@Nullable Context context){
        super(context, "wnhl.db",null,1);
        appContext = context;
    }//Constructor

    /**
     * Creates the DataBase Tables if they do not exist
     *
     * @param db The DataBase
     */
    @Override
    public void onCreate(SQLiteDatabase db){
        //create table statements
        String createTeamsTableString = "CREATE TABLE IF NOT EXISTS " + TEAMS_TABLE + "(" +
                TEAMS_COLUMN_ID + " INTEGER PRIMARY KEY, " + TEAMS_COLUMN_NAME + " TEXT, " +
                TEAMS_COLUMN_SLUG + " TEXT, " + TEAMS_SEASON_ID + " TEXT);";

        String createStandingsTableString = "CREATE TABLE IF NOT EXISTS " + STANDINGS_TABLE + "(" +
                STANDINGS_COLUMN_ID + " INTEGER PRIMARY KEY, " + STANDINGS_COLUMN_DATA + " TEXT, "
                + STANDINGS_COLUMN_SEASON + " INTEGER);";

        String createGamesTableString = "CREATE TABLE IF NOT EXISTS " + GAMES_TABLE +
                "(" + GAMES_COLUMN_ID + " INTEGER PRIMARY KEY, " + GAMES_COLUMN_TITLE +
                " TEXT, " + GAMES_COLUMN_HOME + " INTEGER, " + GAMES_COLUMN_AWAY +
                " INTEGER, " + GAMES_COLUMN_HOME_SCORE + " INTEGER, " +
                GAMES_COLUMN_AWAY_SCORE + " INTEGER, " + GAMES_COLUMN_DATE + " TEXT, "
                + GAMES_COLUMN_TIME + " TEXT, " + GAMES_COLUMN_VENUE + " INTEGER);";

        String createVenuesTableString = "CREATE TABLE IF NOT EXISTS " + VENUES_TABLE + "(" +
                VENUES_COLUMN_ID + " INTEGER PRIMARY KEY, " + VENUES_COLUMN_NAME + " TEXT);";

        String createPlayerTableString = "CREATE TABLE IF NOT EXISTS " + PLAYER_TABLE + "(" +
                PLAYER_COLUMN_ID + " INTEGER PRIMARY KEY, " + PLAYER_COLUMN_NAME + " TEXT, " +
                PLAYER_COLUMN_CONTENT + " TEXT, " + PLAYER_COLUMN_LEAGUES + " INTEGER, " +
                PLAYER_COLUMN_SEASONS + " TEXT, " + PLAYER_COLUMN_PLAYER_NUMBER + " INTEGER, " +
                PLAYER_COLUMN_PREVIOUS_TEAMS + " TEXT, " + PLAYER_COLUMN_CURRENT_TEAM + " TEXT, " +
                PLAYER_COLUMN_NATIONALITY + " TEXT, " + PLAYER_COLUMN_CURR_GOALS + " INTEGER, " +
                PLAYER_COLUMN_CURR_ASSISTS + " INTEGER, " + PLAYER_COLUMN_CURR_POINTS + " INTEGER, "
                + PLAYER_COLUMN_STATS + " TEXT, " + PLAYER_COLUMN_MEDIA_ID + " INTEGER, "
                + PLAYER_COLUMN_FEATURED_MEDIA + " TEXT);";

        String createSeasonsTableString = "CREATE TABLE IF NOT EXISTS " + SEASONS_TABLE + "(" +
                SEASONS_COLUMN_ID + " INTEGER PRIMARY KEY, " + SEASONS_COLUMN_NAME + " TEXT);";

        //execute table commands
        db.execSQL(createTeamsTableString);
        db.execSQL(createStandingsTableString);
        db.execSQL(createGamesTableString);
        db.execSQL(createVenuesTableString);
        db.execSQL(createPlayerTableString);
        db.execSQL(createSeasonsTableString);
    }//onCreate

    public void createSeasonsTable(){
        SQLiteDatabase db = getWritableDatabase();

        String createSeasonsTableString = "CREATE TABLE IF NOT EXISTS " + SEASONS_TABLE + "(" +
                SEASONS_COLUMN_ID + " INTEGER PRIMARY KEY, " + SEASONS_COLUMN_NAME + " TEXT);";

        db.execSQL(createSeasonsTableString);
        db.close();
    }

    public void createGamesTable(){
        SQLiteDatabase db = getWritableDatabase();

        String createGamesTableString = "CREATE TABLE IF NOT EXISTS " + GAMES_TABLE +
                "(" + GAMES_COLUMN_ID + " INTEGER PRIMARY KEY, " + GAMES_COLUMN_TITLE +
                " TEXT, " + GAMES_COLUMN_HOME + " INTEGER, " + GAMES_COLUMN_AWAY +
                " INTEGER, " + GAMES_COLUMN_HOME_SCORE + " INTEGER, " +
                GAMES_COLUMN_AWAY_SCORE + " INTEGER, " + GAMES_COLUMN_DATE + " TEXT, "
                + GAMES_COLUMN_TIME + " TEXT, " + GAMES_COLUMN_VENUE + " INTEGER);";

        db.execSQL(createGamesTableString);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    /**
     * Adds a season to the Seasons Table
     *
     * @param id Season ID
     * @param name Season Name
     * @return TRUE if inserted
     */
    public boolean addSeason(int id, String name){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(SEASONS_COLUMN_ID, id);
        cv.put(SEASONS_COLUMN_NAME, name);

        long insert = db.insert(SEASONS_TABLE, null, cv);

        db.close();

        return insert != -1;
    }//addSeason

    /**
     * Adds Standings Data to the Standings Table
     *
     * @param id Standings ID
     * @param data Standings Data
     * @param season Season the Data corresponds to
     * @return TRUE if inserted
     */
    public boolean addStandings(int id, String data, int season){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(STANDINGS_COLUMN_ID, id);
        cv.put(STANDINGS_COLUMN_DATA, data);
        cv.put(STANDINGS_COLUMN_SEASON, season);

        long insert = db.insert(STANDINGS_TABLE, null, cv);

        db.close();

        return insert != -1;
    }//addStandings

    /**
     * Adds a game to the Games Table
     *
     * @param id Event ID
     * @param title Event Title
     * @param home Home Team ID
     * @param away Away Team ID
     * @param home_score Home Score
     * @param away_score Away Score
     * @param date Event Date
     * @param venue Event Venue ID
     *
     * @return TRUE if inserted
     */
    public boolean addGame(int id, String title, int home, int away, int home_score,
                           int away_score, String date, String time, int venue){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(GAMES_COLUMN_ID,id);
        cv.put(GAMES_COLUMN_TITLE, title);
        cv.put(GAMES_COLUMN_HOME, home);
        cv.put(GAMES_COLUMN_AWAY, away);
        cv.put(GAMES_COLUMN_HOME_SCORE, home_score);
        cv.put(GAMES_COLUMN_AWAY_SCORE, away_score);
        cv.put(GAMES_COLUMN_DATE, date);
        cv.put(GAMES_COLUMN_TIME, time);
        cv.put(GAMES_COLUMN_VENUE, venue);

        long insert = db.insert(GAMES_TABLE, null, cv);

        db.close();

        return insert != -1;
    }

    /**
     * Updates a game in the Games Table
     *
     * @param id Event ID
     * @param title Event Title
     * @param home Home Team ID
     * @param away Away Team ID
     * @param home_score Home Score
     * @param away_score Away Score
     * @param date Event Date
     * @param venue Event Venue ID
     *
     */
    public void updateGame(int id, String title, int home, int away, int home_score,
                           int away_score, String date, String time, int venue){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(GAMES_COLUMN_TITLE, title);
        cv.put(GAMES_COLUMN_HOME, home);
        cv.put(GAMES_COLUMN_AWAY, away);
        cv.put(GAMES_COLUMN_HOME_SCORE, home_score);
        cv.put(GAMES_COLUMN_AWAY_SCORE, away_score);
        cv.put(GAMES_COLUMN_DATE, date);
        cv.put(GAMES_COLUMN_TIME, time);
        cv.put(GAMES_COLUMN_VENUE, venue);

        db.update(GAMES_TABLE, cv, GAMES_COLUMN_ID+" = ?",
                new String[]{String.valueOf(id)});

        db.close();
    }

    /**
     * Adds Venue data to the Venue Table
     *
     * @param id Venue ID
     * @param name Venue Name
     * @return TRUE if inserted
     */
    public boolean addVenue(int id, String name){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(VENUES_COLUMN_ID, id);
        cv.put(VENUES_COLUMN_NAME, name);

        long insert = db.insert(VENUES_TABLE, null, cv);

        db.close();
        return insert != -1;
    }//addVenue

    /**
     * Adds a Player to the Player Table
     *
     * @param id Player ID
     * @param name Player Name
     * @param playerInfo Player Information
     * @param leagues Leagues the player is in
     * @param seasons Seasons the player is in
     * @param num Player Number
     * @param prevTeams JSONArray of past team IDs
     * @param currTeam Current Team ID
     * @param nationality Nationality of Player
     * @param currG Current # of Goals
     * @param currA Current # of Assists
     * @param stats Previous years Statistics
     * @return true if added
     */
    public boolean addPlayer(int id, String name, String playerInfo, String leagues, String seasons,
                             int num, String prevTeams, int currTeam, String nationality, int currG,
                             int currA, int currP, String stats, int mediaID)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PLAYER_COLUMN_ID, id);
        cv.put(PLAYER_COLUMN_NAME, name);
        cv.put(PLAYER_COLUMN_LEAGUES, leagues);
        cv.put(PLAYER_COLUMN_SEASONS, seasons);
        cv.put(PLAYER_COLUMN_PLAYER_NUMBER, num);
        cv.put(PLAYER_COLUMN_PREVIOUS_TEAMS, prevTeams);
        cv.put(PLAYER_COLUMN_CURRENT_TEAM, currTeam);
        cv.put(PLAYER_COLUMN_CONTENT, playerInfo);
        cv.put(PLAYER_COLUMN_NATIONALITY, nationality);
        cv.put(PLAYER_COLUMN_CURR_GOALS, currG);
        cv.put(PLAYER_COLUMN_CURR_ASSISTS, currA);
        cv.put(PLAYER_COLUMN_CURR_POINTS, currP);
        cv.put(PLAYER_COLUMN_STATS, stats);
        cv.put(PLAYER_COLUMN_MEDIA_ID, mediaID);

        long insert = db.insert(PLAYER_TABLE, null, cv);

        db.close();

        return insert != -1;
    }//addPlayer

    /**
     * Adds a team to the Team Table
     *
     * @param id Team ID
     * @param name Team name
     * @param slug Team slug - for SportsPress API
     * @param seasons Season ID
     * @return true if added
     */
    public boolean addTeam(int id, String name, String slug, String seasons){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TEAMS_COLUMN_ID, id);
        cv.put(TEAMS_COLUMN_NAME, name);
        cv.put(TEAMS_COLUMN_SLUG, slug);
        cv.put(TEAMS_SEASON_ID, seasons);

        long insert = db.insert(TEAMS_TABLE, null, cv);

        db.close();

        return insert != -1;
    }//addTeam

    /**
     * Updates player stats
     *
     * @param id player ID
     * @param currG players current goals
     * @param currA players current assists
     * @param currP players current points
     * @param stats players current stats JSONArray
     */
    public void updatePlayer(int id, String name, String playerInfo, String leagues, String seasons,
                             int num, String prevTeams, int currTeam, String nationality, int currG,
                             int currA, int currP, String stats, int mediaID) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PLAYER_COLUMN_ID, id);
        cv.put(PLAYER_COLUMN_NAME, name);
        cv.put(PLAYER_COLUMN_LEAGUES, leagues);
        cv.put(PLAYER_COLUMN_SEASONS, seasons);
        cv.put(PLAYER_COLUMN_PLAYER_NUMBER, num);
        cv.put(PLAYER_COLUMN_PREVIOUS_TEAMS, prevTeams);
        cv.put(PLAYER_COLUMN_CURRENT_TEAM, currTeam);
        cv.put(PLAYER_COLUMN_CONTENT, playerInfo);
        cv.put(PLAYER_COLUMN_NATIONALITY, nationality);
        cv.put(PLAYER_COLUMN_CURR_GOALS, currG);
        cv.put(PLAYER_COLUMN_CURR_ASSISTS, currA);
        cv.put(PLAYER_COLUMN_CURR_POINTS, currP);
        cv.put(PLAYER_COLUMN_STATS, stats);
        cv.put(PLAYER_COLUMN_MEDIA_ID, mediaID);

        db.update(PLAYER_TABLE, cv, PLAYER_COLUMN_ID+" = ?",
                new String[]{String.valueOf(id)});

        db.close();
    }//updatePlayer

    /**
     * Updates the Standings Table
     *
     * @param id Standings Table Row ID
     * @param data JSONArray of Standings Data
     */
    public void updateStandings(int id, String data){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(STANDINGS_COLUMN_DATA, data);

        db.update(STANDINGS_TABLE, cv, STANDINGS_COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

        db.close();
    }//updateStandings

    /**
     * Returns all the Team Names of the current Season
     *
     * @return A List of Team Names - for current seasons teams
     */
    public List<String> getAllTeamNames(){
        List<String> teamList = new ArrayList<>();
        String seasons;
        JSONArray seasonArray;

        String queryString = "SELECT * FROM " + TEAMS_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                try{
                    seasons = cursor.getString(3);
                    seasonArray = new JSONArray(seasons);
                    for(int i = 0; i < seasonArray.length(); i++){
                        if(seasonArray.getInt(i) == SharedPrefHelper.getSharedPreferencesInt(appContext,
                                "current_season",-1)){
                            teamList.add(cursor.getString(1));
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return teamList;
    }//getAllTeamNames

    /**
     * Gets all the IDs of Teams in the current Season
     *
     * @return A List of Team IDs - for the current season
     * @throws JSONException Unreadable
     */
    public List<Integer> getAllTeamIDs() throws JSONException {
        List<Integer> teamIDList = new ArrayList<>();
        String seasons;
        JSONArray seasonArray;

        String queryString = "SELECT * FROM " + TEAMS_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                seasons = cursor.getString(3);
                seasonArray = new JSONArray(seasons);
                for(int i = 0; i < seasonArray.length(); i++){
                    if(seasonArray.getInt(i) == SharedPrefHelper.getSharedPreferencesInt(appContext,
                            "current_season",-1)){
                        teamIDList.add(cursor.getInt(0));
                    }
                }
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return teamIDList;
    }//getAllTeamIDs

    /**
     * Gets the Name of the Team
     *
     * @param id the Teams ID
     * @return Name of the Team
     */
    public String getTeamName(int id){
        String name = "";

        String queryString = "SELECT " + TEAMS_COLUMN_NAME + " FROM " + TEAMS_TABLE + " WHERE "
                + TEAMS_COLUMN_ID + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            name = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return name;
    }//getTeamName

    public String getTeamsColumnSlug(int id){
        String slug = "";

        String queryString = "SELECT " + TEAMS_COLUMN_SLUG + " FROM " + TEAMS_TABLE + " WHERE "
                + TEAMS_COLUMN_ID + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            slug = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return slug;
    }

    /**
     * Gets all the Players for the current Season
     *
     * @return A List of all the Players - for the current Season
     */
    public List<String> getAllPlayers(){
        List<String> playerNames = new ArrayList<>();

        String queryString = "SELECT " + PLAYER_COLUMN_NAME + " FROM " + PLAYER_TABLE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst()){
            do{
                playerNames.add(cursor.getString(0));
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return playerNames;
    }//getAllPlayers

    /**
     * Checks if the Game exists already
     *
     * @param id the Game ID
     * @return TRUE if game is in Table
     */
    public boolean gameInDB(int id){
        String queryString = "SELECT " + GAMES_COLUMN_ID + " FROM " + GAMES_TABLE + " WHERE "
                + GAMES_COLUMN_ID + " = " + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            boolean b = cursor.getInt(0) == id;
            cursor.close();
            return b;
        }
        else{
            cursor.close();
            return false;
        }
    }//gameInDB

    /**
     * Gets the Player IDs of every player on the Team
     *
     * @param id the Team ID
     * @return All players on the Team
     */
    public List<Integer> getPlayerIDsOfTeam(int id){
        List<Integer> playerIDs = new ArrayList<>();

        String queryString = "SELECT " + PLAYER_COLUMN_ID + " FROM " + PLAYER_TABLE + " WHERE " +
                PLAYER_COLUMN_CURRENT_TEAM + " = " + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst()){
            do{
                playerIDs.add(cursor.getInt(0));
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return playerIDs;
    }//getPlayerIDsOfTeam

    /**
     * Gets all Player IDs
     *
     * @return a List of the Player IDs for the current Season
     */
    public List<Integer> getAllPlayerIDs(){
        List<Integer> playerIDs = new ArrayList<>();

        String queryString = "SELECT " + PLAYER_COLUMN_ID + " FROM " + PLAYER_TABLE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst()){
            do{
                playerIDs.add(cursor.getInt(0));
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return playerIDs;
    }//getAllPlayerIDs

    /**
     * Gets the Name of the Player
     *
     * @param id the Player ID
     * @return the Name of the Player
     */
    public String getPlayerName(int id) {
        String name = "";
        String queryString = "SELECT " + PLAYER_COLUMN_NAME + " FROM " + PLAYER_TABLE + " WHERE " +
                PLAYER_COLUMN_ID + " = " + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            name = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return name;
    }//getPlayerName

    public boolean addPlayerPicture(String media){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PLAYER_COLUMN_MEDIA_ID, media);

        long insert = db.insert(PLAYER_TABLE, null, cv);

        db.close();

        return insert != -1;
    }

    public int getFeaturedMedia(int id){
        int media = 0;
        String queryString = "SELECT " + PLAYER_COLUMN_MEDIA_ID + " FROM " + PLAYER_TABLE +
                " WHERE " + PLAYER_COLUMN_ID + " = " + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            media = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return media;
    }

    /**
     *
     * @param id id of media url
     * @return String Media URL
     */
    public String getPlayerPicture(int id){
        String pic = "";
        String queryString = "SELECT " + PLAYER_COLUMN_FEATURED_MEDIA + " FROM " + PLAYER_TABLE +
                " WHERE " + PLAYER_COLUMN_ID + " = " + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            pic = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return pic;
    }

    /**
     * Gets the Text for the About Section in the Player Card
     *
     * @param id the Player ID
     * @return the About Section for the Player Card
     */
    public String getPlayerContent(int id) {
        String content = "";
        String queryString = "SELECT " + PLAYER_COLUMN_CONTENT + " FROM " + PLAYER_TABLE +
                " WHERE " + PLAYER_COLUMN_ID + " = " + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            content = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return content;
    }//getPlayerContent

    /**
     * Gets the Team ID of the players Current Team
     *
     * @param id the Player ID
     * @return the Team ID of the player
     */
    public String getPlayerCurrTeam(int id) {
        String currTeam = "";
        int teamID;

        String queryString = "SELECT " + PLAYER_COLUMN_CURRENT_TEAM + " FROM " + PLAYER_TABLE +
                " WHERE " + PLAYER_COLUMN_ID + " = " + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            teamID = cursor.getInt(0);
            String queryString2 = "SELECT " + TEAMS_COLUMN_NAME + " FROM " + TEAMS_TABLE + " WHERE "
                    + TEAMS_COLUMN_ID + " = " + teamID;
            cursor = db.rawQuery(queryString2,null);
            if(cursor.moveToFirst()){
                currTeam = cursor.getString(0);
            }
        }

        cursor.close();
        db.close();

        return currTeam;
    }//getPlayerCurrTeam

    /**
     * Gets the Player Number
     *
     * @param id the Player ID
     * @return the Players Jersey Number
     */
    public int getPlayerNumber(int id) {
        int num = -1;

        String queryString = "SELECT " + PLAYER_COLUMN_PLAYER_NUMBER + " FROM " + PLAYER_TABLE +
                " WHERE " + PLAYER_COLUMN_ID + " = " + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            num = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return num;
    }//getPlayerNUmber

    /**
     * Gets the JSONArray of Player Stats
     *
     * @param id the Player ID
     * @return JSONArray of past years stats
     */
    public String getPlayerData(int id) {
        String playerData = "";

        String queryStringStats = "SELECT " + PLAYER_COLUMN_STATS + " FROM " + PLAYER_TABLE + " WHERE "
                + PLAYER_COLUMN_ID + " = " + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryStringStats, null);

        if(cursor.moveToFirst()){
            playerData = cursor.getString(0);
        }
        cursor.close();
        return playerData;
    }//getPlayerData

    /**
     * Gets the Seasons Column from the Standings Table
     *
     * @return a List of Seasons
     */
    public List<Integer> getStandingsSeasons(){
        List<Integer> seasons = new ArrayList<>();

        String queryString = "SELECT " + STANDINGS_COLUMN_SEASON + " FROM " + STANDINGS_TABLE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                seasons.add(cursor.getInt(0));
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return seasons;
    }//getStandingsSeasons

    /**
     * Gets the Standings Data for a Season
     *
     * @param season the Season ID
     * @return the Standings Data for that Season
     */
    public String getStandings(int season){
        String standings = "";

        String queryString = "SELECT " + STANDINGS_COLUMN_DATA + " FROM " + STANDINGS_TABLE +
                " WHERE " + STANDINGS_COLUMN_SEASON + " = " + season;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            standings = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return standings;
    }//getStandings

    /**
     * Gets the Name of the Season
     *
     * @param id Season ID
     * @return Name of the Season
     */
    public String getSeasonName(int id){
        String name = "";

        String queryString = "SELECT " + SEASONS_COLUMN_NAME + " FROM " + SEASONS_TABLE + " WHERE "
                + SEASONS_COLUMN_ID + " = " + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            name = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return name;
    }//getSeasonName

    /**
     * Gets the Date the Game takes place
     *
     * @param id the Game ID
     * @return Date of the Game
     */
    public String getEventDate(int id){
        String date = "";

        String queryString = "SELECT " + GAMES_COLUMN_DATE + " FROM " + GAMES_TABLE +
                " WHERE " + GAMES_COLUMN_ID + " = " + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            date = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return date;
    }//getEventDate

    /**
     * Get the Current Goals for a Player
     *
     * @param id the Player ID
     * @return Current Goals for a Player
     */
    public int getCurrentGoals(int id){
        int goals = 0;

        String queryString = "SELECT " + PLAYER_COLUMN_CURR_GOALS + " FROM " + PLAYER_TABLE +
                " WHERE " + PLAYER_COLUMN_ID + " = " + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            goals = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return goals;
    }//getCurrentGoals

    /**
     * Gets the Current Assists for a Player
     *
     * @param id the Player ID
     * @return The Current Assists for a Player
     */
    public int getCurrentAssists(int id){
        int assists = 0;

        String queryString = "SELECT " + PLAYER_COLUMN_CURR_ASSISTS + " FROM " + PLAYER_TABLE +
                " WHERE " + PLAYER_COLUMN_ID + " = " + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            assists = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return assists;
    }//getCurrentAssists

    /**
     * Gets the Current Points for a Player
     *
     * @param id the Player ID
     * @return the Current Points for a Player
     */
    public int getCurrentPoints(int id){
        int points = 0;

        String queryString = "SELECT " + PLAYER_COLUMN_CURR_POINTS + " FROM " + PLAYER_TABLE +
                " WHERE " + PLAYER_COLUMN_ID + " = " + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            points = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return points;
    }//getCurrentPoints

    /**
     * Gets the Player IDs Ordered by Goals Scored
     *
     * @return a List of Players ordered by Goals Scored
     */
    public List<Integer> getPlayerIDsForStatsG(){
        List<Integer> playerIDs = new ArrayList<>();

        String queryString = "SELECT " + PLAYER_COLUMN_ID + " FROM " + PLAYER_TABLE + " ORDER BY "
                + PLAYER_COLUMN_CURR_GOALS;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                playerIDs.add(cursor.getInt(0));
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return playerIDs;
    }//getPlayerIDsForStatsG

    /**
     * Gets the Player IDs Ordered by Points
     *
     * @return a List of Player IDs Ordered by Points
     */
    public List<Integer> getPlayerIDsForStatsP() {
        List<Integer> playerIDs = new ArrayList<>();

        String queryString = "SELECT " + PLAYER_COLUMN_ID + " FROM " + PLAYER_TABLE + " ORDER BY "
                + PLAYER_COLUMN_CURR_POINTS;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                playerIDs.add(cursor.getInt(0));
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return playerIDs;
    }//getPlayerIDsForStatsP

    /**
     * Gets the Player IDs Ordered by Assists
     *
     * @return a List of Player IDs Ordered by Assists
     */
    public List<Integer> getPlayerIDsForStatsA() {
        List<Integer> playerIDs = new ArrayList<>();

        String queryString = "SELECT " + PLAYER_COLUMN_ID + " FROM " + PLAYER_TABLE + " ORDER BY "
                + PLAYER_COLUMN_CURR_ASSISTS;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                playerIDs.add(cursor.getInt(0));
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return playerIDs;
    }//getPlayerIDsForStatsA

    /**
     * Gets the Players Current Team ID
     *
     * @param id the Player ID
     * @return the Team ID
     */
    public int getPlayerCurrTeamNumber(int id) {
        int teamID = 0;

        String queryString = "SELECT " + PLAYER_COLUMN_CURRENT_TEAM + " FROM " + PLAYER_TABLE +
                " WHERE " + PLAYER_COLUMN_ID + " = " + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            teamID = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return teamID;
    }//getPlayerCurrTeamNumber

    /**
     * Returns a List of Game Titles
     *
     * @return a List of Game Titles
     */
    public List<String> getEventTitles() {
        List<String> titles = new ArrayList<>();

        String queryString = "SELECT " + GAMES_COLUMN_TITLE + " FROM " + GAMES_TABLE
                + " ORDER BY " + GAMES_COLUMN_DATE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                titles.add(cursor.getString(0));
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return titles;
    }//getEventTitles

    /**
     * Returns a List of Venues
     *
     * @return a List of Venues
     */
    public List<Integer> getEventVenues() {
        List<Integer> venues = new ArrayList<>();

        String queryString = "SELECT " + GAMES_COLUMN_VENUE + " FROM " + GAMES_TABLE
                + " ORDER BY " + GAMES_COLUMN_DATE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                venues.add(cursor.getInt(0));
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return venues;
    }//getEventVenues

    /**
     * Returns a List of Event Dates
     *
     * @return a List of Event Dates
     */
    public List<String> getEventDates() {
        List<String> dates = new ArrayList<>();

        String queryString = "SELECT " + GAMES_COLUMN_DATE+ " FROM " + GAMES_TABLE
                + " ORDER BY " + GAMES_COLUMN_DATE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                dates.add(cursor.getString(0));
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return dates;
    }//getEventDates

    /**
     * Gets a List of Event Times
     *
     * @return a List of Event Times
     */
    public List<String> getEventTimes() {
        List<String> times = new ArrayList<>();

        String queryString = "SELECT " + GAMES_COLUMN_TIME + " FROM " + GAMES_TABLE
                + " ORDER BY " + GAMES_COLUMN_DATE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                times.add(cursor.getString(0));
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return times;
    }//getEventTimes

    /**
     * Gets the Name of a Venue
     *
     * @param id the Venue ID
     * @return Name of the Venue
     */
    public String getVenueName(int id) {
        String name = "";
        String queryString = "SELECT " + VENUES_COLUMN_NAME + " FROM " + VENUES_TABLE +
                " WHERE " + VENUES_COLUMN_ID + " = " + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            name = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return name;
    }//getVenueName

    /**
     * Gets a List of all Event IDs
     *
     * @return a List of Event IDs
     */
    public List<Integer> getEventIDs() {
        List<Integer> eventIDs = new ArrayList<>();

        String queryString = "SELECT " + GAMES_COLUMN_ID + " FROM " + GAMES_TABLE
                + " ORDER BY " + GAMES_COLUMN_DATE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                eventIDs.add(cursor.getInt(0));
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return eventIDs;
    }//getEventIDs

    /**
     * Gets the Home Team for a Game
     *
     * @param eventID the Game ID
     * @return the Home Teams ID
     */
    public int getHomeTeam(int eventID) {
        int id = -1;

        String queryString = "SELECT " + GAMES_COLUMN_HOME + " FROM " + GAMES_TABLE +
                " WHERE " + GAMES_COLUMN_ID + " = " + eventID;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            id = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return id;
    }//getHomeTeam

    /**
     * Gets the Away Team for a Game
     *
     * @param eventID the Game ID
     * @return the Away Teams ID
     */
    public int getAwayTeam(int eventID) {
        int id = -1;

        String queryString = "SELECT " + GAMES_COLUMN_AWAY + " FROM " + GAMES_TABLE +
                " WHERE " + GAMES_COLUMN_ID + " = " + eventID;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            id = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return id;
    }//getAwayTeam

    /**
     * Gets a List of Event IDs that a Team is part of
     *
     * @param teamID the Team ID
     * @return a List of Events the Team Plays
     */
    public List<Integer> getEventIDsForTeam(int teamID) {
        List<Integer> eventIDs = new ArrayList<>();

        String queryString = "SELECT " + GAMES_COLUMN_ID + " FROM " + GAMES_TABLE +
                " WHERE " + teamID + " IN (" + GAMES_COLUMN_HOME + "," +
                GAMES_COLUMN_AWAY + ")" + " ORDER BY " + GAMES_COLUMN_DATE;;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                eventIDs.add(cursor.getInt(0));
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return eventIDs;
    }//getEventIDsForTeam

    /**
     * Gets a List of Event Titles that a Team is part of
     *
     * @param teamID the Team ID
     * @return a List of Game Titles "Team A vs Team B"
     */
    public List<String> getEventTitlesForTeam(int teamID) {
        List<String> titles = new ArrayList<>();

        String queryString = "SELECT " + GAMES_COLUMN_TITLE + " FROM " + GAMES_TABLE +
                " WHERE " + teamID + " IN (" + GAMES_COLUMN_HOME + "," +
                GAMES_COLUMN_AWAY + ")"+ " ORDER BY " + GAMES_COLUMN_DATE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                titles.add(cursor.getString(0));
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return titles;
    }//getEventTitlesForTeam

    /**
     * Gets a List of Venues a Teams games take place at
     *
     * @param teamID the Team ID
     * @return a List of Venues the Team plays at
     */
    public List<Integer> getEventVenuesForTeam(int teamID) {
        List<Integer> venues = new ArrayList<>();

        String queryString = "SELECT " + GAMES_COLUMN_VENUE + " FROM " + GAMES_TABLE +
                " WHERE " + teamID + " IN (" + GAMES_COLUMN_HOME + "," +
                GAMES_COLUMN_AWAY + ")"+ " ORDER BY " + GAMES_COLUMN_DATE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                venues.add(cursor.getInt(0));
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return venues;
    }//getEventVenuesForTeam

    /**
     * Gets a List of Dates the Team plays on
     *
     * @param teamID the Team ID
     * @return a List of Dates the Team plays on
     */
    public List<String> getEventDatesForTeam(int teamID) {
        List<String> dates = new ArrayList<>();

        String queryString = "SELECT " + GAMES_COLUMN_DATE + " FROM " + GAMES_TABLE +
                " WHERE " + teamID + " IN (" + GAMES_COLUMN_HOME + "," +
                GAMES_COLUMN_AWAY + ")"+ " ORDER BY " + GAMES_COLUMN_DATE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                dates.add(cursor.getString(0));
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return dates;
    }//getEventDatesForTeam

    /**
     * Gets a List of Times the Teams games take place at
     *
     * @param teamID the Team ID
     * @return a List of Times the games take place at
     */
    public List<String> getEventTimesForTeam(int teamID) {
        List<String> times = new ArrayList<>();

        String queryString = "SELECT " + GAMES_COLUMN_TIME + " FROM " + GAMES_TABLE +
                " WHERE " + teamID + " IN (" + GAMES_COLUMN_HOME + "," +
                GAMES_COLUMN_AWAY + ")"+ " ORDER BY " + GAMES_COLUMN_DATE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                times.add(cursor.getString(0));
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return times;
    }//getEventTimesForTeam

    /**
     * Gets the Title of the Game
     *
     * @param id the Game ID
     * @return the Title of the Game
     */
    public String getGamesColumnTitle(int id){
        String title = "";
        String queryString = "SELECT " + GAMES_COLUMN_TITLE + " FROM " + GAMES_TABLE + " WHERE "
                + GAMES_COLUMN_ID + " = " + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            title = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return title;
    }//getGamesColumnTitle

    /**
     * Gets the Venue ID for a game
     *
     * @param id the Game ID
     * @return the Venue ID
     */
    public int getGamesColumnVenue(int id){
        String venueID = "";
        String queryString = "SELECT " + GAMES_COLUMN_VENUE + " FROM " + GAMES_TABLE + " WHERE "
                + GAMES_COLUMN_ID + " = " + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            venueID = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return Integer.parseInt(venueID);
    }//getGamesColumnVenue

    /**
     * Gets the Time for a game
     *
     * @param id the Game ID
     * @return the Time the game takes place
     */
    public String getGamesColumnTime(int id){
        String time = "";
        String queryString = "SELECT " + GAMES_COLUMN_TIME + " FROM " + GAMES_TABLE + " WHERE "
                + GAMES_COLUMN_ID + " = " + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            time = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return time;
    }//getGamesColumnTime

    /**
     * Gets the Date for a game
     *
     * @param id the Game ID
     * @return the Date of the game
     */
    public String getGamesColumnDate(int id){
        String date = "";
        String queryString = "SELECT " + GAMES_COLUMN_DATE + " FROM " + GAMES_TABLE + " WHERE "
                + GAMES_COLUMN_ID + " = " + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            date = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return date;
    }//getGamesColumnDate

    /**
     * Gets the ID Column of the Standings Table for Updating the Standings Table
     *
     * @param currSeason the Current WNHL Season
     * @return the ID of the Standings Data
     */
    public int getStandingsID(int currSeason) {
        int id = 0;

        String queryString = "SELECT " + STANDINGS_COLUMN_ID + " FROM " + STANDINGS_TABLE +
                " WHERE " + STANDINGS_COLUMN_SEASON + " = " + currSeason;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst()){
            id = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }//getStandingsID

    /**
     * Gets the score of the home team for a game
     *
     * @param eventID the Game ID
     * @return the Score of the home team
     */
    public int getHomeScore(int eventID) {
        int home = -1;

        String queryString = "SELECT " + GAMES_COLUMN_HOME_SCORE + " FROM " + GAMES_TABLE +
                " WHERE " + GAMES_COLUMN_ID + " = " + eventID;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            home = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return home;
    }//getHomeScore

    /**
     * Gets the score of the away team for a game
     *
     * @param eventID the Game ID
     * @return the Score of the away team
     */
    public int getAwayScore(int eventID){
        int away = -1;

        String queryString = "SELECT " + GAMES_COLUMN_AWAY_SCORE + " FROM " + GAMES_TABLE +
                " WHERE " + GAMES_COLUMN_ID + " = " + eventID;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            away = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return away;
    }//getAwayScore

    /**
     * Gets the time for a game
     *
     * @param id the Game ID
     * @return Time the Game takes place
     */
    public String getEventTime(int id) {
        String time = "";

        String queryString = "SELECT " + GAMES_COLUMN_TIME + " FROM " + GAMES_TABLE
                + " WHERE " + GAMES_COLUMN_ID + " = " + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            time = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return time;
    }//getEventTime

    /**
     * Gets the last game date in the database table
     *
     * @return String Date of last game
     */
    public String getLastGameDate(){
        String date = "";

        String queryString = "SELECT " + GAMES_COLUMN_DATE + " FROM " + GAMES_TABLE
                + " ORDER BY " + GAMES_COLUMN_ID + " DESC LIMIT 1";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            date = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return date;
    }//getLastGameDate

    /**
     * Updates the home and away score of the game
     *
     * @param id the Game ID
     * @param hscore the home team score
     * @param ascore the away team score
     * @param venue the venue ID
     */
    public void updateGame(int id, int hscore, int ascore, int venue, String date, String time) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(GAMES_COLUMN_HOME_SCORE, hscore);
        cv.put(GAMES_COLUMN_AWAY_SCORE, ascore);
        cv.put(GAMES_COLUMN_VENUE, venue);
        cv.put(GAMES_COLUMN_DATE, date);
        cv.put(GAMES_COLUMN_TIME, time);

        db.update(GAMES_TABLE, cv, GAMES_COLUMN_ID+" = ?",
                new String[]{String.valueOf(id)});

        db.close();
    }//updateGame

    public void addFeaturedMedia(int id, String media) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PLAYER_COLUMN_FEATURED_MEDIA, media);

        db.update(PLAYER_TABLE, cv, PLAYER_COLUMN_MEDIA_ID+" = ?",
                new String[]{String.valueOf(id)});

        db.close();
    }

    public void dropTable(String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(tableName,null,null);
        db.close();
    }

    public List<String> getAllTeamSlugs() {
        List<String> slugList = new ArrayList<>();
        String seasons;
        JSONArray seasonArray;

        String queryString = "SELECT * FROM " + TEAMS_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                try{
                    seasons = cursor.getString(3);
                    seasonArray = new JSONArray(seasons);
                    for(int i = 0; i < seasonArray.length(); i++){
                        if(seasonArray.getInt(i) == SharedPrefHelper.getSharedPreferencesInt(appContext,
                                "current_season",-1)){
                            slugList.add(cursor.getString(2));
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return slugList;
    }
}//DataBaseHelper