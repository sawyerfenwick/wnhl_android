package com.wnhl.wnhl_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.wnhl.wnhl_android.fragments.More_Fragment;
import com.wnhl.wnhl_android.fragments.Schedule_Fragment;
import com.wnhl.wnhl_android.fragments.Standings_Fragment;
import com.wnhl.wnhl_android.fragments.Teams_Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Creates the Schedule Fragment
 */
public class WNHL_Schedule extends AppCompatActivity{

    BottomNavigationView bottomNavigationView;

    DataBaseHelper dataBaseHelper;
    FragmentManager fragmentManager = getSupportFragmentManager();

    Fragment scheduleFragment = new Schedule_Fragment();
    Fragment standingsFragment = new Standings_Fragment();
    Fragment teamsFragment = new Teams_Fragment();
    Fragment moreFragment = new More_Fragment();

    /**
     * Closes app if back pressed and nothing else is on BackStack
     */
    @Override
    public void onBackPressed(){
        if(fragmentManager.getBackStackEntryCount() == 1){
            finish();
        }
        else{
            super.onBackPressed();
        }
    }//onBackPressed

    /**
     * Creates the Schedule Fragment
     * Tracks BackStack
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ADD A FEATURE HERE THAT SAVES A VARIABLE TO THE SHARED PREFS TO SEE IF
        // DATA WAS DOWNLOADED SUCCESSFULLY
        super.onCreate(savedInstanceState);
        //Get current version code
        final String PREFS_NAME = "WNHL_Prefs";
        final String PREF_VERSION_CODE_KEY = "version_code";
        int currentVersionCode = BuildConfig.VERSION_CODE;
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        //Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        setContentView(R.layout.activity_wnhl_schedule);
        getSupportActionBar().hide();

        dataBaseHelper = new DataBaseHelper(this);
        addFragment(scheduleFragment, "Schedule");
        bottomNavigationView = findViewById(R.id.bottom_navbar);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId()){
                   case R.id.schedule:
                       addFragment(scheduleFragment, "Schedule");
                       break;
                   case R.id.more:
                       addFragment(moreFragment, "More");
                       break;
                   case R.id.standings:
                       addFragment(standingsFragment, "Standings");
                       break;
                   case R.id.teams:
                       addFragment(teamsFragment, "Teams");
                       break;
               }
               return true;
           }
        });
    }//onCreate

    /**
     * Adds Fragment to the BackStack
     *
     * @param fragment
     * @param name
     */
    private void addFragment(Fragment fragment, String name) {

        boolean fragmentPopped = fragmentManager.popBackStackImmediate(name, 0);

        if(!fragmentPopped && fragmentManager.findFragmentByTag(name) == null){
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.container, fragment, name);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(name);
            ft.commit();
        }

        /**
         * Tracks the BackStack to determine which menu item should be pressed
         */
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(fragmentManager.getBackStackEntryCount() > 0) {
                    FragmentManager.BackStackEntry bse =
                            fragmentManager.getBackStackEntryAt
                                    (fragmentManager.getBackStackEntryCount() - 1);
                    switch (bse.getName()) {
                        case "Schedule":
                            bottomNavigationView.getMenu().getItem(0).setChecked(true);
                            break;
                        case "Standings":
                            bottomNavigationView.getMenu().getItem(1).setChecked(true);
                            break;
                        case "Teams":
                            bottomNavigationView.getMenu().getItem(2).setChecked(true);
                            break;
                        case "More":
                            bottomNavigationView.getMenu().getItem(3).setChecked(true);
                            break;
                    }
                }
                else if(fragmentManager.getBackStackEntryCount() == 0){
                    bottomNavigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        });
    }//addFragment
}//WNHL_Schedule