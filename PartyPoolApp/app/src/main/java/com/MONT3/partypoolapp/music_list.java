package com.MONT3.partypoolapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;



/**
 * A simple {@link Fragment} subclass.
 */
public class music_list extends Fragment {

    public music_list() {
        // Required empty public constructor
    }

    String[] songName = {"Afria","Afria","Afria","Afria","Afria","Afria","Afria","Afria","Africa","Africa","Afria","Africa","Africa"};
    String[] songArtist = {"Toto","Toto","Toto","Toto","Toto","Toto","Toto","Toto","Toto","toto","Toto","Toto","toto"};
    int[] songAblumArt = {R.drawable.toto,R.drawable.toto,R.drawable.toto,R.drawable.toto,R.drawable.toto,R.drawable.toto,R.drawable.toto,R.drawable.toto,R.drawable.toto,R.drawable.toto,R.drawable.toto,R.drawable.toto,R.drawable.toto};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);

        ListView listView = (ListView) view.findViewById(R.id.music_list);


        CustomMusicListAdapter customMusicListAdapter = new CustomMusicListAdapter();
        listView.setAdapter(customMusicListAdapter);

    return view;
    }
    class CustomMusicListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return songAblumArt.length;
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


            imageView_albumArt.setImageResource(songAblumArt[i]);
            textView_songArtist.setText(songArtist[i]);
            textView_songName.setText(songName[i]);


            return view;
        }

    }
}