package com.MONT3.partypoolapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class TestActivity extends AppCompatActivity implements CreateDialog.DialogListener{
private Button buttonJoin;
private Button buttonCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        TextView tv = findViewById(R.id.textView2);

        //  If User Has Reached Splash Page From The Login Screen, Pull Data From The Associated
        //  Intent, Else Pull The Data From The Create Account Screen Instead.
        if (getIntent().hasExtra("LOGINDATA")) {
            tv.setText("Welcome, " + getIntent().getStringExtra("LOGINDATA"));
        }

        else {
            tv.setText("Welcome, " + getIntent().getStringExtra("CREATEDATA"));
        }

        buttonJoin = findViewById(R.id.button2);
        buttonCreate = findViewById(R.id.button3);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateDialog();
            }
        });
        buttonJoin.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                openJoinDialog();
            }
        });
    }
    public void openCreateDialog() {
        DialogFragment createDialog = new CreateDialog();
        createDialog.show(getSupportFragmentManager(),"create dialog");
    }

    public void openJoinDialog() {
        JoinDialog joinDialog = new JoinDialog();
        joinDialog.show(getSupportFragmentManager(),"join dialog");
    }

    public void openPartyConfirmDialog(String modeIn) {
        PartyPasswordDialog confirmDialog = new PartyPasswordDialog().newInstance(modeIn);
        confirmDialog.show(getSupportFragmentManager(), "confirm dialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

        RadioButton radioContinuous = (RadioButton) dialog.getDialog().findViewById
                (R.id.continuousRadio);
        RadioButton radioParty = (RadioButton) dialog.getDialog().findViewById
                (R.id.partyRadio);

        if(radioParty.isChecked())
        {
            String test = GeneratePartyPassword(4);
            openPartyConfirmDialog("PARTY");
        }

        else if(radioContinuous.isChecked())
        {
            //choice = 2;
            Toast.makeText(this,"Selected option: Continuous Mode",Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }

    //  Generate A Random Password
    public String GeneratePartyPassword(int length) {

        StringBuilder password = new StringBuilder();
        char[] passwordCharacterSet =
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        int charsetLength = passwordCharacterSet.length;
        Random randomNumberGenerator = new Random();

        /*
         *  ToDo: Run A Database Query To Check Password Is Unique. If Not, Keep Running Password
         *  ToDo: Generation Until One Is Found.
         *                                                                                        */
        for (int i = 0; i < length; i++) {
            char randomCharacter = passwordCharacterSet[randomNumberGenerator.nextInt
                    (charsetLength++)];
            password.append(randomCharacter);
        }

        return password.toString();
    }

    public class UserLoginTask extends AsyncTask <void, void, Boolean> {

    }
}
