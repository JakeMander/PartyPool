package com.MONT3.partypoolapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SocialPartyFrag extends Fragment {


    public SocialPartyFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social_party, container, false);
        String[] peepleOnMenu = {"BEANS", "EGGS", "BACON","SAUSAGES"};

        ListView listView = (ListView) view.findViewById(R.id.people_list);


        ArrayAdapter<String> listviewAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1,peepleOnMenu);

        listView.setAdapter(listviewAdapter);
        // Inflate the layout for this fragment
        return view;
    }

}
