package com.wnhl.wnhl_android;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Players extends AppCompatActivity {

    List<String> playerNames;
    List<Integer> playerIDs;
    DataBaseHelper dataBaseHelper;
    LinearLayout layout;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wnhl_players);
        getSupportActionBar().setTitle("");
        ColorDrawable colorDrawable
                = new ColorDrawable(getResources().getColor(R.color.orange));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        dataBaseHelper = new DataBaseHelper(this);
        playerNames = new ArrayList<>();

        layout = (LinearLayout)findViewById(R.id.playerButtonHolder);
        playerNames = dataBaseHelper.getAllPlayers();
        playerIDs = dataBaseHelper.getAllPlayerIDs();

        Button b;
        Drawable arrow = getResources().getDrawable(R.drawable.ic_arrow_right_white, null);
        for(int i = 0; i < playerNames.size(); i++){
            final int id = playerIDs.get(i);
            b = new Button(this);
            b.setId(i);
            b.setText("" + playerNames.get(i));
            b.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            b.setTextColor(getResources().getColor(R.color.white,null));
            b.setBackgroundColor(getResources().getColor(R.color.orange,null));
            b.setCompoundDrawablesWithIntrinsicBounds(null,null, arrow,null);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Players.this, WNHL_Player.class);
                    i.putExtra("player_id", id);
                    startActivity(i);
                }
            });
            layout.addView(b);
        }
    }//onCreate

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.player_search,menu);

        MenuItem menuItem = menu.findItem(R.id.player_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Players");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               for(int i = 0; i < playerNames.size(); i++){
                   Button b = layout.findViewById(i);
                   if(b.getText().toString().toLowerCase().contains(newText)){
                       b.setVisibility(View.VISIBLE);
                   }
                   else{
                       b.setVisibility(View.GONE);
                   }
               }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }//onCreateOptionsMenu
}//Players
