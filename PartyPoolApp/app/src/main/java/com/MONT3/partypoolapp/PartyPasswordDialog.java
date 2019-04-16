package com.MONT3.partypoolapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.TextView;

public class PartyPasswordDialog extends AppCompatDialogFragment {

    private TextView messageTwo = null;
    private TextView messageConfirm = null;

    //  To Pass In Our Mode And Password Values, Create A New Instance Of The Dialog Box That
    //  Places The Parameters In A Bundle. Extract These In The onCreateDialog Method.
    public static PartyPasswordDialog newInstance (String mode, String password) {
       PartyPasswordDialog newInstance = new PartyPasswordDialog();

       Bundle args = new Bundle();
       args.putString("MODE", mode);
       args.putString("PASSWORD", password);
       newInstance.setArguments(args);

       return newInstance;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String mode = getArguments().getString("MODE");
        final String password = getArguments().getString("PASSWORD");

        //  Builder Creates The Dialog Box. Context Of Our Current Activity Is Supplied To Pass
        //  Over Themes And Such.
        //
        //  We Then Pass The Inflater Associated With Our Current Activity Over To The Dialog Box.
        //  Again, This Is To Preserve Context And Themes.
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater dialogInflater = getActivity().getLayoutInflater();

        //  Associate The Style We Want To Render Onto our View Then Assign The Password
        //  Section Of Our Dialog Text To The Appropriate Handle So We Can Access The Text.
        View view = dialogInflater.inflate(R.layout.party_password_dialog, null);

        messageTwo = view.findViewById(R.id.passwordPlaceholder);
        messageConfirm = view.findViewById(R.id.messageConfirm);

        //  Replace Placeholder Values In String Resource With The Values We Pass In.
        String formattedConfirmMessage = getString(R.string.partyModeVerifyConfirm, mode);

        //  Set The Text Attributes To Our Received Text Values.
        messageTwo.setText(password);
        messageConfirm.setText(formattedConfirmMessage);


        //  Pass The Design We Want Our Dialog To Follow To The Builder So It Can Be Rendered
        //  As Part of A Dialog Box. Sets All The Header And Button Titles.
        dialogBuilder.setView(view)
                .setTitle("Password Generated")
                .setPositiveButton("Let's Party!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //  Determine Which Mode We Load Based On Previous Dialog Selection.
                        if (mode.equals("PARTY")) {
                            Intent myIntent = new Intent(getContext(), MainScreenParty.class);
                            myIntent.putExtra("LoginType","Admin");
                            startActivity(myIntent);
                        }

                        //  TODO: Change To Continuous Mode Once Implemented
                        else {
                            Intent loadContinuousMode = new Intent(getActivity(),
                                    MainScreenParty.class);
                            startActivity(loadContinuousMode);
                        }
                    }
                })
                .setNegativeButton("Wait! Not Yet!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return dialogBuilder.create();
    }
}
