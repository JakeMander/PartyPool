package com.MONT3.partypoolapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class mid_party_admin extends Fragment implements View.OnClickListener {

 private ImageView pausePlayButton;
 private Boolean inPlay = false;

    public mid_party_admin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mid_party_admin, container,false);
        pausePlayButton = v.findViewById(R.id.AdminPartyPausePlay);
        pausePlayButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.AdminPartyPausePlay:
                if(inPlay.equals(false))
                {

                    pausePlayButton.setImageResource(android.R.drawable.ic_media_pause);
                    inPlay = true;
                }
                else if(inPlay.equals(true))
                {

                    pausePlayButton.setImageResource(android.R.drawable.ic_media_play);
                    inPlay = false;
                }

        }
    }
}
