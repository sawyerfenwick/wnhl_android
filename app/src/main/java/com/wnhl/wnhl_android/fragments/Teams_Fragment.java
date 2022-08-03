package com.wnhl.wnhl_android.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wnhl.wnhl_android.DataBaseHelper;
import com.wnhl.wnhl_android.R;
import com.wnhl.wnhl_android.WNHL_Teams;

import org.json.JSONException;

import java.util.List;

/**
 * Creates Buttons for each team in the current season with onClicks to start new Intents for
 * the individual team page
 *
 * @author Sawyer Fenwick | Daniel Figueroa
 * @version 1.0
 */
public class Teams_Fragment extends Fragment {

    DataBaseHelper dataBaseHelper;
    /**
     * Creates Buttons for each of the current season teams
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return v The View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dataBaseHelper = new DataBaseHelper(getContext());
        View v = inflater.inflate(R.layout.fragment_teams, container, false);
        List<String> teamNames = null;
        List<Integer> teamIDs = null;
        try {
            teamNames = dataBaseHelper.getAllTeamNames();
            teamIDs = dataBaseHelper.getAllTeamIDs();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String buttonID;
        int resID;
        int teamID;
        Button b;

        for(int i = 0; i < teamNames.size(); i++){
            buttonID = "button"+i;
            resID = getResources().getIdentifier(buttonID, "id", getActivity()
                    .getPackageName());
            teamID = teamIDs.get(i);
            b = (Button) v.findViewById(resID);
            b.setText(teamNames.get(i));
            b.setVisibility(View.VISIBLE);
            int finalTeamID = teamID;
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), WNHL_Teams.class);
                    i.putExtra("team_id", finalTeamID);
                    startActivity(i);
                }
            });
        }
        return v;
    }//onCreateView
}//Teams_Fragment