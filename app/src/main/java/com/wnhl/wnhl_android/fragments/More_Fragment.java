package com.wnhl.wnhl_android.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wnhl.wnhl_android.Notifications;
import com.wnhl.wnhl_android.Players;
import com.wnhl.wnhl_android.R;
import com.wnhl.wnhl_android.Stats;
import com.wnhl.wnhl_android.Update;

/**
 * Creates the More Page with the following operations:
 * Players
 * Statistics
 * YouTube
 * WNHL Fantasy
 * Notification Settings
 *
 * @author Sawyer Fenwick | Daniel Figueroa
 * @version 1.0
 */
public class More_Fragment extends Fragment implements View.OnClickListener {

    /**
     * Creates the More Fragment
     * Sets OnClick listeners for the buttons
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_more_, container, false);

        Button b = (Button) v.findViewById(R.id.fantasyButton);
        Button b1 = (Button) v.findViewById(R.id.youtubeButton);
        Button b2 = (Button) v.findViewById(R.id.twitterButton);
        Button b3 = (Button) v.findViewById(R.id.updateButton);
        Button b4 = (Button) v.findViewById(R.id.playerButton);
        Button b5 = (Button) v.findViewById(R.id.notifButton);
        Button b6 = (Button) v.findViewById(R.id.statsButton);

        b.setOnClickListener(this);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);

        return v;
    }//onCreateView

    /**
     * Overrides the onClick method to redirect to new Intents
     *
     * @param v The View
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fantasyButton:
                redirectFantasy();
                break;
            case R.id.youtubeButton:
                openYouTube();
                break;
            case R.id.twitterButton:
                openTwitter();
                break;
            case R.id.updateButton:
                openUpdatePage();
                break;
            case R.id.playerButton:
                openPlayerPage();
                break;
            case R.id.notifButton:
                openNotifPage();
                break;
            case R.id.statsButton:
                openStatsPage();
                break;
        }
    }//onClick

    /**
     * Opens the Player Page
     */
    private void openPlayerPage() {
        Intent i = new Intent(getActivity(), Players.class);
        startActivity(i);
    }//openPlayerPage

    /**
     * Opens the Notifications Page
     */
    private void openNotifPage() {
        Intent i = new Intent(getActivity(), Notifications.class);
        startActivity(i);
    }//openNotifPage

    /**
     * Opens the Statistics Page
     */
    private void openStatsPage() {
        Intent i = new Intent(getActivity(), Stats.class);
        startActivity(i);
    }//openStatsPage

    private void openUpdatePage(){
        Intent i = new Intent(getActivity(), Update.class);
        startActivity(i);
    }

    /**
     * Redirects to the Fantasy Google Doc
     */
    private void redirectFantasy(){
        String url = "https://docs.google.com/spreadsheets/d/e/2PACX-1vQ8bY-Of5YbJHk0VTj0LxWyQLYkK2dzWea-2fjd899X3qWMXGysbmE2UhqCdsFBLtJ233WjsGA_IMYJ/pubhtml?gid=0&single=true";

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(appIntent);
    }//redirectFantasy

    /**
     * Redirects to the YouTube WNHL Homepage
     */
    private void openYouTube() {
        String url = "https://www.youtube.com/channel/UCklG51DEXWN6RodvW8Mj3cg/featured";

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(appIntent);
    }//openYouTube

    /**
     * Redirects to the Twitter WNHL Homepage
     */
    private void openTwitter(){
        String url = "https://twitter.com/wnhl2?lang=en";

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(appIntent);
    }
}//More_Fragment