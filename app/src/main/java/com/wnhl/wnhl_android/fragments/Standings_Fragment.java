package com.wnhl.wnhl_android.fragments;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.wnhl.wnhl_android.DataBaseHelper;
import com.wnhl.wnhl_android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Creates the Standings Fragment
 *
 * @author Sawyer Fenwick | Daniel Figueroa
 * @version 1.0
 */
public class Standings_Fragment extends Fragment {

    DataBaseHelper dataBaseHelper;
    TextView seasonTitle;
    TextView tableInfo;
    TableLayout tableLayout;
    TableRow tableRow;
    LinearLayout standingsHolder;
    String seasonName;
    List<Integer> seasonIDs;
    List<String> standingsData;
    int teamID;
    String teamName;

    /**
     * Creates the Standings Tables for all Standings data from the Standings Table
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return v The View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataBaseHelper = new DataBaseHelper(getContext());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_standings, container, false);

        standingsHolder = (LinearLayout)v.findViewById(R.id.standingsHolder);
        standingsData = new ArrayList<>();
        seasonIDs = dataBaseHelper.getStandingsSeasons();
        //Seasons are stored first to last, reverse them for most recent season
        Collections.reverse(seasonIDs);

        for(int i = 0; i < seasonIDs.size(); i++){
            if(i > 0){

            }
            else{
                //Set Table Titles
                seasonTitle = new TextView(getContext());
                seasonName = dataBaseHelper.getSeasonName(seasonIDs.get(i));
                seasonTitle.setTypeface(seasonTitle.getTypeface(), Typeface.BOLD);
                seasonTitle.setText(seasonName);
                seasonTitle.setTextColor(getResources().getColor(R.color.white,null));
                seasonTitle.setTextSize(30);
                seasonTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                standingsHolder.addView(seasonTitle);
                //Start constructing tables
                try {
                    JSONObject data = new JSONObject(dataBaseHelper.getStandings(seasonIDs.get(i)));
                    tableLayout = new TableLayout(getContext());
                    tableLayout.setLayoutParams(new TableLayout.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT
                    ));
                    //make table headers
                    tableLayout.setStretchAllColumns(true);
                    tableLayout.setBackground(getResources().getDrawable(R.color.white, null));
                    tableLayout.setPadding(10,5,10,5);
                    tableRow = new TableRow(getContext());
                    tableInfo = new TextView(getContext());
                    tableInfo.setText("Pos");
                    tableInfo.setTypeface(tableInfo.getTypeface(), Typeface.BOLD);
                    tableInfo.setGravity(Gravity.CENTER);
                    tableRow.addView(tableInfo);
                    tableInfo = new TextView(getContext());
                    tableInfo.setText("Team");
                    tableInfo.setTypeface(tableInfo.getTypeface(), Typeface.BOLD);
                    tableInfo.setGravity(Gravity.CENTER);
                    tableRow.addView(tableInfo);
                    tableInfo = new TextView(getContext());
                    tableInfo.setText("GP");
                    tableInfo.setTypeface(tableInfo.getTypeface(), Typeface.BOLD);
                    tableInfo.setGravity(Gravity.CENTER);
                    tableRow.addView(tableInfo);
                    tableInfo = new TextView(getContext());
                    tableInfo.setText("W");
                    tableInfo.setTypeface(tableInfo.getTypeface(), Typeface.BOLD);
                    tableInfo.setGravity(Gravity.CENTER);
                    tableRow.addView(tableInfo);
                    tableInfo = new TextView(getContext());
                    tableInfo.setText("L");
                    tableInfo.setTypeface(tableInfo.getTypeface(), Typeface.BOLD);
                    tableInfo.setGravity(Gravity.CENTER);
                    tableRow.addView(tableInfo);
                    tableInfo = new TextView(getContext());
                    tableInfo.setText("T");
                    tableInfo.setTypeface(tableInfo.getTypeface(), Typeface.BOLD);
                    tableInfo.setGravity(Gravity.CENTER);
                    tableRow.addView(tableInfo);
                    tableInfo = new TextView(getContext());
                    tableInfo.setText("PTS");
                    tableInfo.setTypeface(tableInfo.getTypeface(), Typeface.BOLD);
                    tableInfo.setGravity(Gravity.CENTER);
                    tableRow.addView(tableInfo);
                    tableInfo = new TextView(getContext());
                    tableInfo.setText("GF");
                    tableInfo.setTypeface(tableInfo.getTypeface(), Typeface.BOLD);
                    tableInfo.setGravity(Gravity.CENTER);
                    tableRow.addView(tableInfo);
                    tableInfo = new TextView(getContext());
                    tableInfo.setText("GA");
                    tableInfo.setTypeface(tableInfo.getTypeface(), Typeface.BOLD);
                    tableInfo.setGravity(Gravity.CENTER);
                    tableRow.addView(tableInfo);
                    tableLayout.addView(tableRow);
                    //Insert the data into the table
                    for(int j = 0; j < data.names().length()-1; j++) {
                        teamID = Integer.valueOf(data.names().getString(j));
                        teamName = dataBaseHelper.getTeamName(teamID);
                        JSONObject row = data.getJSONObject(data.names().getString(j));
                        tableRow = new TableRow(getContext());
                        tableInfo = new TextView(getContext());
                        tableInfo.setText(row.getString("pos"));
                        tableInfo.setGravity(Gravity.CENTER);
                        tableRow.addView(tableInfo);
                        tableInfo = new TextView(getContext());
                        tableInfo.setText(teamName);
                        tableInfo.setGravity(Gravity.CENTER);
                        tableRow.addView(tableInfo);
                        tableInfo = new TextView(getContext());
                        tableInfo.setText(row.getString("gp"));
                        tableInfo.setGravity(Gravity.CENTER);
                        tableRow.addView(tableInfo);
                        tableInfo = new TextView(getContext());
                        tableInfo.setText(row.getString("w"));
                        tableInfo.setGravity(Gravity.CENTER);
                        tableRow.addView(tableInfo);
                        tableInfo = new TextView(getContext());
                        tableInfo.setText(row.getString("l"));
                        tableInfo.setGravity(Gravity.CENTER);
                        tableRow.addView(tableInfo);
                        tableInfo = new TextView(getContext());
                        tableInfo.setText(row.getString("ties"));
                        tableInfo.setGravity(Gravity.CENTER);
                        tableRow.addView(tableInfo);
                        tableInfo = new TextView(getContext());
                        tableInfo.setText(row.getString("pts"));
                        tableInfo.setGravity(Gravity.CENTER);
                        tableRow.addView(tableInfo);
                        tableInfo = new TextView(getContext());
                        tableInfo.setText(row.getString("gf"));
                        tableInfo.setGravity(Gravity.CENTER);
                        tableRow.addView(tableInfo);
                        tableInfo = new TextView(getContext());
                        tableInfo.setText(row.getString("ga"));
                        tableInfo.setGravity(Gravity.CENTER);
                        tableRow.addView(tableInfo);
                        tableLayout.addView(tableRow);
                    }
                    standingsHolder.addView(tableLayout);
                    TextView tv = new TextView(getContext());
                    tv.setPadding(0,0,0,5);
                    standingsHolder.addView(tv);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return v;
    }//onCreateView
}//Standings_Fragment