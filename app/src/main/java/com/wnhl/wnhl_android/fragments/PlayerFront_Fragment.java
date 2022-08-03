package com.wnhl.wnhl_android.fragments;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.wnhl.wnhl_android.DataBaseHelper;
import com.wnhl.wnhl_android.R;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 *
 */
public class PlayerFront_Fragment extends Fragment {

    ImageView playerPicture;
    TextView playerName;
    TextView playerNumber;
    DataBaseHelper dataBaseHelper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player_front, container, false);
        dataBaseHelper = new DataBaseHelper(getContext());

//        if (ActivityCompat.checkSelfPermission(getContext(),
//                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
//        }

        playerPicture = (ImageView) v.findViewById(R.id.playerPicture);
        playerName = (TextView) v.findViewById(R.id.playerName);
        playerNumber = (TextView) v.findViewById(R.id.playerNumber);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            int id = bundle.getInt("player_id", -1);
            int number = dataBaseHelper.getPlayerNumber(id);
            playerName.setText(dataBaseHelper.getPlayerName(id));
            if(number<10){
                playerNumber.setText(" #" + dataBaseHelper.getPlayerNumber(id));
            }
            else{
                playerNumber.setText("#" + dataBaseHelper.getPlayerNumber(id));
            }

            if(dataBaseHelper.getFeaturedMedia(id) == 0){
                playerPicture.setImageDrawable(this.getActivity().getDrawable(R.drawable.wnhl_logo));
                Toast.makeText(getActivity(),"No Picture Found", Toast.LENGTH_SHORT).show();
            }
            else{
                Picasso.get().load(dataBaseHelper.getPlayerPicture(id)).into(playerPicture);
                //getPlayerImage(dataBaseHelper.getFeaturedMedia(id));
            }
        }

        return v;
    }

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
            Toast.makeText(getActivity(),"No Picture Found."+fileName,Toast.LENGTH_SHORT).show();
        }
    }//getPlayerImage
}//PlayerFront_Fragment
