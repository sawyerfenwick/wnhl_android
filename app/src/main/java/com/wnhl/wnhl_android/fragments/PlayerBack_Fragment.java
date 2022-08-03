package com.wnhl.wnhl_android.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.wnhl.wnhl_android.DataBaseHelper;
import com.wnhl.wnhl_android.SharedPrefHelper;
import com.wnhl.wnhl_android.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class PlayerBack_Fragment extends Fragment {

    ImageView playerPicture;
    TextView playerName;
    TextView playerNumber;
    TextView playerInfo;
    TextView desc;
    TextView info;
    TableRow tableRow;
    TableLayout tableLayout;
    ImageView logo;

    DataBaseHelper dataBaseHelper;
    SharedPrefHelper sharedPrefHelper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_player_back, container, false);

        dataBaseHelper = new DataBaseHelper(getContext());

        playerPicture = (ImageView) v.findViewById(R.id.playerImage);
        playerName = (TextView)v.findViewById(R.id.playerName);
        playerNumber = (TextView)v.findViewById(R.id.playerNumber);
        playerInfo = (TextView)v.findViewById(R.id.playerInfo);
        desc = (TextView)v.findViewById(R.id.desc);
        info = (TextView)v.findViewById(R.id.playerInfo);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            int id = bundle.getInt("player_id", -1);
            String name = dataBaseHelper.getPlayerName(id);
            String content = dataBaseHelper.getPlayerContent(id);
            String team = dataBaseHelper.getPlayerCurrTeam(id);
            int number = dataBaseHelper.getPlayerNumber(id);
            int teamID = dataBaseHelper.getPlayerCurrTeamNumber(id);
            if(dataBaseHelper.getFeaturedMedia(id) == 0){
                playerPicture.setImageDrawable(this.getActivity().getDrawable(R.drawable.wnhl_logo));
                Toast.makeText(getActivity(),"No Picture Found", Toast.LENGTH_SHORT).show();
            }
            else{
                Picasso.get().load(dataBaseHelper.getPlayerPicture(id)).into(playerPicture);
                //getPlayerImage(dataBaseHelper.getFeaturedMedia(id));
            }
            playerInfo.setText(name + " | # " + number + "\n" + team);
            desc.setText(content);
            desc.setGravity(Gravity.CENTER);

            try{
                v = init(id, v);
            } catch(JSONException e){
                e.printStackTrace();
            }

        }
        return v;
    }//onCreateView
    /**
     * Creates the Player Table with their Stats
     *
     * @param id Player ID
     * @throws JSONException
     */
    public View init(int id, View v) throws JSONException {
        //Initiate and set up Player Table
        tableLayout = (TableLayout) v.findViewById(R.id.playerTable);
        tableRow = new TableRow(getContext());
        info = new TextView(getContext());
        info.setText("Season");
        info.setPadding(3,0,3,0);
        info.setGravity(Gravity.CENTER);
        info.setTypeface(info.getTypeface(), Typeface.BOLD);
        tableRow.addView(info);
        info = new TextView(getContext());
        info.setText("Team");
        info.setPadding(3,0,3,0);
        info.setGravity(Gravity.CENTER);
        info.setTypeface(info.getTypeface(), Typeface.BOLD);
        tableRow.addView(info);
        info = new TextView(getContext());
        info.setText("P");
        info.setPadding(3,0,3,0);
        info.setGravity(Gravity.CENTER);
        info.setTypeface(info.getTypeface(), Typeface.BOLD);
        tableRow.addView(info);
        info = new TextView(getContext());
        info.setText("G");
        info.setPadding(3,0,3,0);
        info.setGravity(Gravity.CENTER);
        info.setTypeface(info.getTypeface(), Typeface.BOLD);
        tableRow.addView(info);
        info = new TextView(getContext());
        info.setText("A");
        info.setPadding(3,0,3,0);
        info.setGravity(Gravity.CENTER);
        info.setTypeface(info.getTypeface(), Typeface.BOLD);
        tableRow.addView(info);
        info = new TextView(getContext());
        info.setText("S%");
        info.setPadding(3,0,3,0);
        info.setGravity(Gravity.CENTER);
        info.setTypeface(info.getTypeface(), Typeface.BOLD);
        tableRow.addView(info);
        info = new TextView(getContext());
        info.setText("SV%");
        info.setPadding(3,0,3,0);
        info.setGravity(Gravity.CENTER);
        info.setTypeface(info.getTypeface(), Typeface.BOLD);
        tableRow.addView(info);
        info = new TextView(getContext());
        info.setText("GP");
        info.setPadding(3,0,3,0);
        info.setGravity(Gravity.CENTER);
        info.setTypeface(info.getTypeface(), Typeface.BOLD);
        tableRow.addView(info);
        tableLayout.addView(tableRow);

        //Find players of specific team

        String playerData = "";

        playerData = dataBaseHelper.getPlayerData(id);

        JSONObject object = new JSONObject(playerData);
        JSONObject data = object.getJSONObject("3");

        ArrayList<String> keys = new ArrayList<>();

        for(int i = 0; i < data.names().length()-1; i++){
            keys.add(data.names().getString(i));
        }

        Collections.sort(keys);
        Collections.reverse(keys);

        for(int i = 0; i < keys.size(); i++){
            JSONObject stats = data.getJSONObject(keys.get(i));
            tableRow = new TableRow(getContext());
            info = new TextView(getContext());
            info.setText(stats.getString("name"));
            info.setGravity(Gravity.CENTER);
            info.setPadding(3,0,3,0);
            tableRow.addView(info);
            info = new TextView(getContext());
            info.setText(stats.getString("team"));
            info.setGravity(Gravity.CENTER);
            info.setPadding(3,0,3,0);
            tableRow.addView(info);
            info = new TextView(getContext());
            info.setText(stats.getString("p"));
            info.setGravity(Gravity.CENTER);
            info.setPadding(3,0,3,0);
            tableRow.addView(info);
            info = new TextView(getContext());
            info.setText(stats.getString("g"));
            info.setGravity(Gravity.CENTER);
            info.setPadding(3,0,3,0);
            tableRow.addView(info);
            info = new TextView(getContext());
            info.setText(stats.getString("a"));
            info.setGravity(Gravity.CENTER);
            info.setPadding(3,0,3,0);
            tableRow.addView(info);
            info = new TextView(getContext());
            info.setText(stats.getString("spercent"));
            info.setGravity(Gravity.CENTER);
            info.setPadding(3,0,3,0);
            tableRow.addView(info);
            info = new TextView(getContext());
            info.setText(stats.getString("svpercent"));
            info.setGravity(Gravity.CENTER);
            info.setPadding(3,0,3,0);
            tableRow.addView(info);
            info = new TextView(getContext());
            info.setText(stats.getString("gp"));
            info.setGravity(Gravity.CENTER);
            info.setPadding(3,0,3,0);
            tableRow.addView(info);
            tableLayout.addView(tableRow);
        }
        return v;
    }//init

    /**
     *
     * @param mediaID
     */
    private void getPlayerImage(int mediaID){
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root+"/WNHL");
        String fileName = "WNHL-"+mediaID+".jpg";
        File file = new File(myDir, fileName);
        if(file.exists()){
            Picasso.get().load(new File(myDir, fileName)).into(playerPicture);
        }
        else{
            playerPicture.setImageDrawable(this.getActivity().getDrawable(R.drawable.wnhl_logo));
            Toast.makeText(getActivity(),"No Picture Found.",Toast.LENGTH_SHORT).show();
        }
    }//getPlayerImage

}//PlayerBack_Fragment
