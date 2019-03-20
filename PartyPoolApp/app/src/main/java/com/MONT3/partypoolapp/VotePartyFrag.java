package com.MONT3.partypoolapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class VotePartyFrag extends Fragment implements View.OnClickListener {
private ImageView albumOne;
private ImageView albumTwo;
private ImageView albumThree;
private ImageView albumFour;
private TextView luckyTxt;

    public VotePartyFrag()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_vote_party, container,false);
        albumOne = v.findViewById(R.id.partyAlbumOne);
        albumOne.setOnClickListener(this);
        albumTwo = v.findViewById(R.id.partyAlbumTwo);
        albumTwo.setOnClickListener(this);
        albumThree = v.findViewById(R.id.partyAlbumThree);
        albumThree.setOnClickListener(this);
        albumFour = v.findViewById(R.id.partyAlbumFour);
        albumFour.setOnClickListener(this);
        luckyTxt = v.findViewById(R.id.luckyTextView);
        luckyTxt.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.partyAlbumOne:
                Toast.makeText(getActivity(), "You voted for Album One!",
                        Toast.LENGTH_LONG).show();
            break;

            case R.id.partyAlbumTwo:
                Toast.makeText(getActivity(), "You voted for Album Two!",
                        Toast.LENGTH_LONG).show();
            break;

            case R.id.partyAlbumThree:
                Toast.makeText(getActivity(), "You voted for Album Three!",
                        Toast.LENGTH_LONG).show();
            break;

            case R.id.partyAlbumFour:
                Toast.makeText(getActivity(), "You voted for Album Four!",
                        Toast.LENGTH_LONG).show();
            break;
            case R.id.luckyTextView:
                Toast.makeText(getActivity(), "You're Feeling Lucky ?!",
                        Toast.LENGTH_LONG).show();
            break;

        }

    }
}
