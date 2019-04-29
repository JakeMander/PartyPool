package com.MONT3.partypoolapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FileDialog extends AppCompatDialogFragment {

    private ArrayList<Song> songReference;
    private Activity activityReference;

    public View getViewByPosition(int position, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition =firstListItemPosition + listView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition ) {
            return listView.getAdapter().getView(position, listView.getChildAt(position), listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        activityReference = (MainScreenParty) getContext();
        songReference = ((MainScreenParty)activityReference).getSongs();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_file,null);
        final ListView list = view.findViewById(R.id.customFileList);

        final CustomFileListAdapter customFileListAdapter = new CustomFileListAdapter();
        list.setAdapter(customFileListAdapter);

        builder.setView(view)
                .setTitle("Choose The Songs To Add To The Pool!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View v;
                        CheckBox cb;
                        ArrayList<Song> newSongs = new ArrayList<>();
                        for (int i=0;i<list.getCount();i++)
                        {
                            v = getViewByPosition(i,list);
                            cb = v.findViewById(R.id.checkBox);

                            long newId;
                            String newTitle;
                            String newArtist;

                            if(cb.isChecked())
                            {
                                newId = songReference.get(i).getID();
                                newTitle = songReference.get(i).getTitle();
                                newArtist = songReference.get(i).getArtist();

                                newSongs.add(new Song(newId,newTitle,newArtist));
                            }
                        }
                        ((MainScreenParty) activityReference).setSongs(newSongs);
                        Toast.makeText(getActivity(), "Added "+ newSongs.size() + " songs", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();


    }

    class CustomFileListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return songReference.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.custom_list_file_directory,null);
            CheckBox checkBox = convertView.findViewById(R.id.checkBox);
            checkBox.setChecked(songReference.get(position).getIsAdded());
            checkBox.setText(songReference.get(position).getTitle() + " - " + songReference.get(position).getArtist());
            return convertView;
        }
    }
}


