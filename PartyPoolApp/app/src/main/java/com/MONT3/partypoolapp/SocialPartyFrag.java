package com.MONT3.partypoolapp;


import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class SocialPartyFrag extends Fragment {

    String[] peepleOnMenu = {"DAVE SMITH","DAVE SMITH","DAVE SMITH", "KEVIN JONES", "JAMES BARNS","HARRY STARK","BOBBY BATES","DAVE SMITH", "KEVIN JONES", "JAMES BARNS","HARRY STARK","BOBBY BATES"};
    int[] images = {R.drawable.profile_1,R.drawable.profile_1, R.drawable.profile_1,  R.drawable.profile_2,R.drawable.profile_3, R.drawable.profile_4, R.drawable.profile_5,R.drawable.profile_1, R.drawable.profile_2,R.drawable.profile_3, R.drawable.profile_4, R.drawable.profile_5};

    public SocialPartyFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_social_party, container, false);

        ListView listView = (ListView) view.findViewById(R.id.people_list);

        CustomAdapter customAdapter=new CustomAdapter();

        listView.setAdapter(customAdapter);


        return view;
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return images.length;
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
            view = getLayoutInflater().inflate(R.layout.custom_list_peopl,null);

            ImageView imageView_profile=(ImageView)view.findViewById(R.id.imageView_profile);
            TextView textView_name =(TextView)view.findViewById(R.id.textView_name);


            imageView_profile.setImageResource(images[i]);
            textView_name.setText(peepleOnMenu[i]);

            return view;
        }
    }

}
