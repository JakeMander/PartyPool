package com.MONT3.partypoolapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class CreateDialog extends DialogFragment {

    private RadioButton radioContinuous;
    private RadioButton radioParty;
    private DialogListener activityReference;

    /*  This Is The Interface That The Activity Must Implement To Recieve
     *  Callbacks From The Dialog Box. This Will Allow The Main Activity
     *  To Receive Data From The Dialog By Maintaining A Reference To The
     *  Dialog In Each Method. */

    public interface DialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    //  Override The Fragment.onAttatch() Method So We Maintain Our Reference
    //  Back To The Activity.
    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        //  Check To Ensure Activity Implements The DialogListener Interface.
        //  We Need The Activity To Implement The Interface Methods To Ensure Callbacks
        //  Will Be Received

        try {
            activityReference = (DialogListener) context;
        }

        catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() +
                    " Must Implement DialogListener Interface");
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);

        radioContinuous = view.findViewById(R.id.continuousRadio);
        radioParty = view.findViewById(R.id.partyRadio);

        builder.setView(view)
                .setTitle("Choose Voting Mode")

                //  Link Dialog Buttons To Activity So We Can Pass Data Back To Be Used
                //  In Second Dialog Box.
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activityReference.onDialogPositiveClick(CreateDialog.this);
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activityReference.onDialogNegativeClick(CreateDialog.this);
                    }
                });

        return builder.create();
    }

    public RadioButton getContinuousRadio() {
        return radioContinuous;
    }

    public RadioButton getPartyRadio() {
        return radioParty;
    }

}
