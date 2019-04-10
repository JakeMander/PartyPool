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
public class mid_continuous_admin extends Fragment implements View.OnClickListener {

    private ImageView pausePlayButton;
    private Boolean inPlay = false;

    public mid_continuous_admin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mid_continuous_admin, container,false);
        pausePlayButton = v.findViewById(R.id.AdminContinuousPausePlay);
        pausePlayButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.AdminContinuousPausePlay:
                if(inPlay.equals(false))
                {
                    getActivity().startService(new Intent(getActivity(),MyService.class));
                    pausePlayButton.setImageResource(android.R.drawable.ic_media_pause);
                    inPlay = true;
                }
                else if(inPlay.equals(true))
                {
                    getActivity().stopService(new Intent(getActivity(),MyService.class));
                    pausePlayButton.setImageResource(android.R.drawable.ic_media_play);
                    inPlay = false;
                }

        }
    }
}
