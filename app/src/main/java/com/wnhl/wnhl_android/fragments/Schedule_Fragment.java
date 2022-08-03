package com.wnhl.wnhl_android.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wnhl.wnhl_android.DataBaseHelper;
import com.wnhl.wnhl_android.PastGameScheduleAdapter;
import com.wnhl.wnhl_android.R;
import com.wnhl.wnhl_android.ScheduleAdapter;
import com.wnhl.wnhl_android.SharedPrefHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Creates the RecyclerView and loads it with Schedule "Bubbles"
 *
 * @author Sawyer Fenwick | Daniel Figueroa
 * @version 1.0
 */
public class Schedule_Fragment extends Fragment {

    RecyclerView recyclerView;
    DataBaseHelper dataBaseHelper;
    SharedPrefHelper sharedPrefHelper;
    ScheduleAdapter scheduleAdapter;
    PastGameScheduleAdapter PastGameScheduleAdapter;
    TextView textView;
    ImageView logo;
    DateFormat dateFormat;

    /**
     * Creates the Schedule Fragment
     * Gets all Schedule information from the Future Games Table
     * Builds the Schedule Bubbles using the Schedule Adapter class
     * Fills the RecyclerView with Games
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
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);

        //These lists contain all information to make Schedule "Bubbles"
        List<String> titles, dates, times;    //all event Titles, Dates, and Times
        List<Integer> venues, eventIDs, hscore, ascore;   //all event Venues, Event IDs, and Scores

        //Check Date for Playoffs
        dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        if (dateFormat.format(date).equals("02") | dateFormat.format(date).equals("03")){
            logo = (ImageView) v.findViewById(R.id.teamsLogoView);
            logo.setImageDrawable(getActivity().getDrawable(R.drawable.wnhl_championship));
        }

        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        textView = (TextView)v.findViewById(R.id.title);
        dataBaseHelper = new DataBaseHelper(getContext());
        sharedPrefHelper = new SharedPrefHelper();

        int currSeason = sharedPrefHelper.getSharedPreferencesInt(getContext(), "current_season",
                -1);

        if(dataBaseHelper.getStandingsID(currSeason) == 0){
            currSeason = sharedPrefHelper.getSharedPreferencesInt(getContext(), "previous_season",
                    -1);
        }

        eventIDs = dataBaseHelper.getEventIDs();
        titles = dataBaseHelper.getEventTitles();
        venues = dataBaseHelper.getEventVenues();
        dates = dataBaseHelper.getEventDates();
        times = dataBaseHelper.getEventTimes();

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        String todayDate = dateFormat.format(today);
        Iterator<Integer> iterator = eventIDs.iterator();
        Iterator<String> titleIterator = titles.iterator();
        Iterator<Integer> venuesIterator = venues.iterator();
        Iterator<String> dateIterator = dates.iterator();
        Iterator<String> timeIterator = times.iterator();

        int id = 0;

        while(iterator.hasNext()){
            id = iterator.next();
            titleIterator.next();
            venuesIterator.next();
            dateIterator.next();
            timeIterator.next();
            try {
                if (compareTime(dataBaseHelper.getEventDate(id), todayDate)) {
                    iterator.remove();
                    titleIterator.remove();
                    dateIterator.remove();
                    timeIterator.remove();
                    venuesIterator.remove();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            if(today.after(dateFormat.parse(dataBaseHelper.getEventDate(eventIDs.get(eventIDs.size()-1))))){
                textView.setText(dataBaseHelper.getSeasonName(currSeason)+"\n Regular Season");
                PastGameScheduleAdapter = new PastGameScheduleAdapter(getContext(), eventIDs, titles, dates, venues);
                recyclerView.setAdapter(PastGameScheduleAdapter);
            }
            else{
                scheduleAdapter = new ScheduleAdapter(getContext(), eventIDs, titles, dates, times, venues);
                recyclerView.setAdapter(scheduleAdapter);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }//onCreateView

    public boolean compareTime(String date1, String date2){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date11 = null;
        try {
            date11 = sdf.parse(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date21 = null;
        try {
            date21 = sdf.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int result = date11.compareTo(date21);

        if (result == 0) {
            return false;
        } else if (result > 0) {
            return false;
        } else if (result < 0) {
            return true;
        } else {
            return false;
        }
    }
}//Schedule_Fragment