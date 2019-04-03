package com.MONT3.partypoolapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class JoinDialog extends AppCompatDialogFragment {
    private EditText editPassword;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_join, null);
        editPassword = view.findViewById(R.id.edit_password);
        builder.setView(view)
                .setTitle("Enter Room Password")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editPassword.getText().toString().equals("1234"))
                        {
                            Intent myIntent = new Intent(getContext(),MainScreenParty.class);
                            myIntent.putExtra("LoginType","Guest");
                            startActivity(myIntent);
                        }
                    }
                });


        return builder.create();
    }
}
