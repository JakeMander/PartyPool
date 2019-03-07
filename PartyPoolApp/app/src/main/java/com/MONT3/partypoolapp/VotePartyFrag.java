package com.MONT3.partypoolapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class VotePartyFrag extends Fragment implements View.OnClickListener {
private Button testButton;

    public VotePartyFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_vote_party, container,false);
        testButton = v.findViewById(R.id.testButton);
        testButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.testButton:
                Toast.makeText(getActivity(),"Test Works MuthaFucka",Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
