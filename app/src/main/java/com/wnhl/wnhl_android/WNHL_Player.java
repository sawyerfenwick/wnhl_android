package com.wnhl.wnhl_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.wnhl.wnhl_android.fragments.PlayerBack_Fragment;
import com.wnhl.wnhl_android.fragments.PlayerFront_Fragment;

/**
 * Creates the Individual Player Card based on EXTRA INTENT player id
 */
public class WNHL_Player extends AppCompatActivity {
    boolean mShowingBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wnhl_player);
        getSupportActionBar().hide();

        int id = getIntent().getIntExtra("player_id", -1);
        Bundle bundle = new Bundle();
        bundle.putInt("player_id", id);

        FrameLayout frameLayout = findViewById(R.id.container);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard(bundle);
            }
        });

        PlayerFront_Fragment playerFront = new PlayerFront_Fragment();
        playerFront.setArguments(bundle);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, playerFront)
                    .commit();
        }
    }
    void flipCard(Bundle bundle) {
        PlayerBack_Fragment playerBack = new PlayerBack_Fragment();
        playerBack.setArguments(bundle);
        if (mShowingBack) {
            mShowingBack = false;
            getSupportFragmentManager().popBackStack();
        } else {
            mShowingBack = true;
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.animator.card_flip_right_enter,
                            R.animator.card_flip_right_exit,
                            R.animator.card_flip_left_enter,
                            R.animator.card_flip_left_exit)
                    .replace(R.id.container, playerBack)
                    .addToBackStack(null)
                    .commit();
        }
    }
}//WNHL_Player