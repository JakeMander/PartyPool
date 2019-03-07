package com.MONT3.partypoolapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class CreateDialog extends AppCompatDialogFragment {
    private RadioButton radioContinuous;
    private RadioButton radioParty;
    //private Integer choice;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);

        builder.setView(view)
                .setTitle("Choose Voting Mode")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    if(radioParty.isChecked())
                    {
                        //choice = 1;
                        Toast.makeText(getActivity(),"Selected option: Party Mode",Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(getContext(), MainScreenParty.class);
                        startActivity(myIntent);
                    }
                    else if(radioContinuous.isChecked())
                    {
                        //choice = 2;
                        Toast.makeText(getActivity(),"Selected option: Continuous Mode",Toast.LENGTH_SHORT).show();
                    }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        radioContinuous = view.findViewById(R.id.continuousRadio);
        radioParty = view.findViewById(R.id.partyRadio);

        return builder.create();
    }


}
