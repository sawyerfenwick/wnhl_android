package com.wnhl.wnhl_android;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Creates the Statistics Page
 */
public class Stats extends AppCompatActivity {

    DataBaseHelper dataBaseHelper;
    SharedPrefHelper sharedPrefHelper;
    TableLayout goals;
    TableLayout assists;
    TableLayout points;
    TextView title;
    TextView stats;
    TableRow tableRow;
    LinearLayout layout;
    List<Integer> playerIDs;
    int p;
    int g;
    int a;

    /**
     * Creates 3 Tables
     * Points
     * Goals
     * Assists
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wnhl_stats);
        getSupportActionBar().hide();

        dataBaseHelper = new DataBaseHelper(this);
        sharedPrefHelper = new SharedPrefHelper();

        layout = (LinearLayout)findViewById(R.id.statsHolder);

        title = new TextView(this);
        title.setText("Goals");
        title.setPadding(0,5,0,5);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setTextColor(getResources().getColor(R.color.white,null));
        title.setTextSize(20);
        layout.addView(title);
        //goals table

        goals = new TableLayout(this);
        goals.setLayoutParams(new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT
        ));
        goals.setStretchAllColumns(true);
        goals.setBackground(getResources().getDrawable(R.color.white, null));

        tableRow = new TableRow(this);
        stats = new TextView(this);
        stats.setText("Rank");
        stats.setGravity(Gravity.CENTER);
        stats.setTypeface(stats.getTypeface(), Typeface.BOLD);
        tableRow.addView(stats);
        stats = new TextView(this);
        stats.setText("Player");
        stats.setGravity(Gravity.CENTER);
        stats.setTypeface(stats.getTypeface(), Typeface.BOLD);
        tableRow.addView(stats);
        stats = new TextView(this);
        stats.setText("Team");
        stats.setGravity(Gravity.CENTER);
        stats.setTypeface(stats.getTypeface(), Typeface.BOLD);
        tableRow.addView(stats);
        stats = new TextView(this);
        stats.setText("G");
        stats.setGravity(Gravity.CENTER);
        stats.setTypeface(stats.getTypeface(), Typeface.BOLD);
        tableRow.addView(stats);
        //end goals table
        goals.addView(tableRow);

        playerIDs = new ArrayList<>();
        playerIDs = dataBaseHelper.getPlayerIDsForStatsG();
        Collections.reverse(playerIDs);

        for(int i = 0; i < playerIDs.size(); i++){

            g = dataBaseHelper.getCurrentGoals(playerIDs.get(i));

            tableRow = new TableRow(this);
            stats = new TextView(this);
            stats.setText(String.valueOf(i+1));
            stats.setGravity(Gravity.CENTER);
            tableRow.addView(stats);
            stats = new TextView(this);
            stats.setText(dataBaseHelper.getPlayerName(playerIDs.get(i)));
            stats.setGravity(Gravity.CENTER);
            tableRow.addView(stats);
            stats = new TextView(this);
            stats.setText(dataBaseHelper.getPlayerCurrTeam(playerIDs.get(i)));
            stats.setGravity(Gravity.CENTER);
            tableRow.addView(stats);
            stats = new TextView(this);
            stats.setText(String.valueOf(g));
            stats.setGravity(Gravity.CENTER);
            tableRow.addView(stats);
            //end goals table
            if(i > 9){
                tableRow.setVisibility(View.GONE);
            }
            goals.addView(tableRow);
        }

        layout.addView(goals);

        //Hide and Show Functionality
        TextView goalsMoreLess = new TextView(this);
        goalsMoreLess.setText("More...");
        goalsMoreLess.setGravity(Gravity.CENTER);
        goalsMoreLess.setBackgroundColor(getResources().getColor(R.color.white,null));
        goalsMoreLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goalsMoreLess.getText().equals("More...")){
                    goalsMoreLess.setText("Less...");
                    for(int i = 0; i < goals.getChildCount(); i++){
                        goals.getChildAt(i).setVisibility(View.VISIBLE);
                    }
                }
                else if(goalsMoreLess.getText().equals("Less...")){
                    goalsMoreLess.setText("More...");
                    for(int i = 11; i < goals.getChildCount(); i++){
                        goals.getChildAt(i).setVisibility(View.GONE);
                    }
                }
            }
        });

        layout.addView(goalsMoreLess);

        title = new TextView(this);
        title.setText("Assists");
        title.setPadding(0,5,0,5);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setTextColor(getResources().getColor(R.color.white,null));
        title.setTextSize(20);
        layout.addView(title);
        //assists table
        assists = new TableLayout(this);
        assists.setLayoutParams(new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT
        ));
        assists.setStretchAllColumns(true);
        assists.setBackground(getResources().getDrawable(R.color.white, null));

        tableRow = new TableRow(this);
        stats = new TextView(this);
        stats.setText("Rank");
        stats.setGravity(Gravity.CENTER);
        stats.setTypeface(stats.getTypeface(), Typeface.BOLD);
        tableRow.addView(stats);
        stats = new TextView(this);
        stats.setText("Player");
        stats.setGravity(Gravity.CENTER);
        stats.setTypeface(stats.getTypeface(), Typeface.BOLD);
        tableRow.addView(stats);
        stats = new TextView(this);
        stats.setText("Team");
        stats.setGravity(Gravity.CENTER);
        stats.setTypeface(stats.getTypeface(), Typeface.BOLD);
        tableRow.addView(stats);
        stats = new TextView(this);
        stats.setText("A");
        stats.setGravity(Gravity.CENTER);
        stats.setTypeface(stats.getTypeface(), Typeface.BOLD);
        tableRow.addView(stats);
        //end goals table
        assists.addView(tableRow);

        playerIDs = new ArrayList<>();
        playerIDs = dataBaseHelper.getPlayerIDsForStatsA();
        Collections.reverse(playerIDs);

        for(int i = 0; i < playerIDs.size(); i++){

            a = dataBaseHelper.getCurrentAssists(playerIDs.get(i));

            tableRow = new TableRow(this);
            stats = new TextView(this);
            stats.setText(String.valueOf(i+1));
            stats.setGravity(Gravity.CENTER);
            tableRow.addView(stats);
            stats = new TextView(this);
            stats.setText(dataBaseHelper.getPlayerName(playerIDs.get(i)));
            stats.setGravity(Gravity.CENTER);
            tableRow.addView(stats);
            stats = new TextView(this);
            stats.setText(dataBaseHelper.getPlayerCurrTeam(playerIDs.get(i)));
            stats.setGravity(Gravity.CENTER);
            tableRow.addView(stats);
            stats = new TextView(this);
            stats.setText(String.valueOf(a));
            stats.setGravity(Gravity.CENTER);
            tableRow.addView(stats);
            //end goals table
            if(i > 9){
                tableRow.setVisibility(View.GONE);
            }
            assists.addView(tableRow);

        }

        layout.addView(assists);
        //end assists table

        //Hide and Show Functionality
        TextView assistsMoreLess = new TextView(this);
        assistsMoreLess.setText("More...");
        assistsMoreLess.setGravity(Gravity.CENTER);
        assistsMoreLess.setBackgroundColor(getResources().getColor(R.color.white,null));
        assistsMoreLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(assistsMoreLess.getText().equals("More...")){
                    assistsMoreLess.setText("Less...");
                    for(int i = 11; i < assists.getChildCount(); i++){
                        assists.getChildAt(i).setVisibility(View.VISIBLE);
                    }
                }
                else if(assistsMoreLess.getText().equals("Less...")){
                    assistsMoreLess.setText("More...");
                    for(int i = 11; i < assists.getChildCount(); i++){
                        assists.getChildAt(i).setVisibility(View.GONE);
                    }
                }
            }
        });

        layout.addView(assistsMoreLess);

        title = new TextView(this);
        title.setText("Points");
        title.setPadding(0,5,0,5);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(getResources().getColor(R.color.white,null));
        title.setTextSize(20);
        layout.addView(title);
        //points table
        points = new TableLayout(this);
        points.setLayoutParams(new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT
        ));
        points.setStretchAllColumns(true);
        points.setBackground(getResources().getDrawable(R.color.white, null));

        tableRow = new TableRow(this);
        stats = new TextView(this);
        stats.setText("Rank");
        stats.setGravity(Gravity.CENTER);
        stats.setTypeface(stats.getTypeface(), Typeface.BOLD);
        tableRow.addView(stats);
        stats = new TextView(this);
        stats.setText("Player");
        stats.setGravity(Gravity.CENTER);
        stats.setTypeface(stats.getTypeface(), Typeface.BOLD);
        tableRow.addView(stats);
        stats = new TextView(this);
        stats.setText("Team");
        stats.setGravity(Gravity.CENTER);
        stats.setTypeface(stats.getTypeface(), Typeface.BOLD);
        tableRow.addView(stats);
        stats = new TextView(this);
        stats.setText("P");
        stats.setGravity(Gravity.CENTER);
        stats.setTypeface(stats.getTypeface(), Typeface.BOLD);
        tableRow.addView(stats);
        //end goals table
        points.addView(tableRow);

        playerIDs = new ArrayList<>();
        playerIDs = dataBaseHelper.getPlayerIDsForStatsP();
        Collections.reverse(playerIDs);

        for(int i = 0; i < playerIDs.size(); i++){

            p = dataBaseHelper.getCurrentPoints(playerIDs.get(i));

            tableRow = new TableRow(this);
            stats = new TextView(this);
            stats.setText(String.valueOf(i+1));
            stats.setGravity(Gravity.CENTER);
            tableRow.addView(stats);
            stats = new TextView(this);
            stats.setText(dataBaseHelper.getPlayerName(playerIDs.get(i)));
            stats.setGravity(Gravity.CENTER);
            tableRow.addView(stats);
            stats = new TextView(this);
            stats.setText(dataBaseHelper.getPlayerCurrTeam(playerIDs.get(i)));
            stats.setGravity(Gravity.CENTER);
            tableRow.addView(stats);
            stats = new TextView(this);
            stats.setText(String.valueOf(p));
            stats.setGravity(Gravity.CENTER);
            tableRow.addView(stats);
            //end goals table
            if(i > 9){
                tableRow.setVisibility(View.GONE);
            }
            points.addView(tableRow);
        }

        layout.addView(points);

        //Hide and Show Functionality
        TextView pointsMoreLess = new TextView(this);
        pointsMoreLess.setText("More...");
        pointsMoreLess.setBackgroundColor(getResources().getColor(R.color.white,null));
        pointsMoreLess.setGravity(Gravity.CENTER);
        pointsMoreLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pointsMoreLess.getText().equals("More...")){
                    pointsMoreLess.setText("Less...");
                    for(int i = 11; i < points.getChildCount(); i++){
                        points.getChildAt(i).setVisibility(View.VISIBLE);
                    }
                }
                else if(pointsMoreLess.getText().equals("Less...")){
                    pointsMoreLess.setText("More...");
                    for(int i = 11; i < points.getChildCount(); i++){
                        points.getChildAt(i).setVisibility(View.GONE);
                    }
                }
            }
        });
        layout.addView(pointsMoreLess);
    }//onCreate
}//Stats
