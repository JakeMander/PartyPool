package com.MONT3.partypoolapp;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class music_list extends Fragment implements View.OnClickListener{
private ArrayList<Song> songReference;
private Activity activityReference;
private Button fileExplorer;
    public music_list() {
        // Required empty public constructor
    }

    String[] songName = {"Afria","Afria","Afria","Afria","Afria","Afria","Afria","Afria","Africa","Africa","Afria","Africa","Africa"};
    String[] songArtist = {"Toto","Toto","Toto","Toto","Toto","Toto","Toto","Toto","Toto","toto","Toto","Toto","toto"};
    //int[] songAblumArt = {R.drawable.toto,R.drawable.toto,R.drawable.toto,R.drawable.toto,R.drawable.toto,R.drawable.toto,R.drawable.toto,R.drawable.toto,R.drawable.toto,R.drawable.toto,R.drawable.toto,R.drawable.toto,R.drawable.toto};


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            activityReference = (MainScreenParty) context;
            songReference = ((MainScreenParty) activityReference).getSongs();
        }
        catch (Exception e)
        {

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);

        ListView listView = (ListView) view.findViewById(R.id.music_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song selectedSong = songReference.get(position);
                String selectedTitle = selectedSong.getTitle();
                Toast.makeText(getActivity(), "You have selected "+ selectedTitle, Toast.LENGTH_SHORT).show();
                ((MainScreenParty)activityReference).playSongFromList(selectedSong);
            }
        });

        CustomMusicListAdapter customMusicListAdapter = new CustomMusicListAdapter();
        listView.setAdapter(customMusicListAdapter);
        fileExplorer = view.findViewById(R.id.button_add_dir);
        fileExplorer.setOnClickListener(this);

    return view;
    }

    public void openFileDialog()
    {
        FileDialog dialog = new FileDialog();
        dialog.show(getFragmentManager(),"File Dialog");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.button_add_dir:
                openFileDialog();
        }
    }


    class CustomMusicListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return songReference.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.custom_list_songs, null);

            ImageView imageView_albumArt = (ImageView) view.findViewById(R.id.imageView_albumArt);
            TextView textView_songArtist = (TextView) view.findViewById(R.id.textView_songAritst);
            TextView textView_songName = (TextView) view.findViewById(R.id.textView_songName);


            imageView_albumArt.setImageResource(R.drawable.music_default);
            textView_songArtist.setText(songReference.get(i).getArtist());
            textView_songName.setText(songReference.get(i).getTitle());


            return view;
        }

    }
}