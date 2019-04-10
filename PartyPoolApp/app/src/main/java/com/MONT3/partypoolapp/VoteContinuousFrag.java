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
public class VoteContinuousFrag extends Fragment implements View.OnClickListener {
    private ImageView albumOne;
    private ImageView albumTwo;
    private ImageView albumThree;
    private ImageView albumFour;
    private TextView luckyTxt;

    public VoteContinuousFrag()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_vote_continuous, container,false);
        albumOne = v.findViewById(R.id.ContinuousAlbumOne);
        albumOne.setOnClickListener(this);
        albumTwo = v.findViewById(R.id.ContinuousAlbumTwo);
        albumTwo.setOnClickListener(this);
        albumThree = v.findViewById(R.id.ContinuousAlbumThree);
        albumThree.setOnClickListener(this);
        albumFour = v.findViewById(R.id.ContinuousAlbumFour);
        albumFour.setOnClickListener(this);
        luckyTxt = v.findViewById(R.id.luckyTextView);
        luckyTxt.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.ContinuousAlbumOne:
                Toast.makeText(getActivity(), "You voted for Album One!",
                        Toast.LENGTH_LONG).show();
                break;

            case R.id.ContinuousAlbumTwo:
                Toast.makeText(getActivity(), "You voted for Album Two!",
                        Toast.LENGTH_LONG).show();
                break;

            case R.id.ContinuousAlbumThree:
                Toast.makeText(getActivity(), "You voted for Album Three!",
                        Toast.LENGTH_LONG).show();
                break;

            case R.id.ContinuousAlbumFour:
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
