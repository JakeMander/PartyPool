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

//  Responses For Async Password Checker.
private UserLoginTask mCheckPasswordTask = null;
private String passwordGenError = null;
NetworkAccess networkAccess = NetworkAccess.getNetworkAccess();

private String password;
private Boolean passwordError = false;

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

    public void openPartyConfirmDialog(String modeIn, String passwordIn) {
        PartyPasswordDialog confirmDialog = new PartyPasswordDialog().newInstance(modeIn, passwordIn);
        confirmDialog.show(getSupportFragmentManager(), "confirm dialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

        RadioButton radioContinuous = (RadioButton) dialog.getDialog().findViewById
                (R.id.continuousRadio);
        RadioButton radioParty = (RadioButton) dialog.getDialog().findViewById
                (R.id.partyRadio);

        if (mCheckPasswordTask != null)
        {
            return;
        }

        mCheckPasswordTask = new UserLoginTask();
        mCheckPasswordTask.execute((Void) null);

        if(radioParty.isChecked())
        {
            openPartyConfirmDialog("PARTY", password);
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

        for (int i = 0; i < length; i++) {
            char randomCharacter = passwordCharacterSet[randomNumberGenerator.nextInt
                    (charsetLength++)];
            password.append(randomCharacter);
        }

        return password.toString();
    }

    public class UserLoginTask extends AsyncTask <Void, Void, Boolean> {
        String passwordToCheck;
        String[] status;

        UserLoginTask() {
            passwordToCheck = null;
            status = null;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean keepRegen = false;

            //  Whilst We Don't Have A Unique Password, Or Haven't Encountered An Error...
            do {
                boolean isError = false;

                //  Generate A Random Password And Check For Uniqueness Against The Database.
                passwordToCheck = GeneratePartyPassword(4);
                status = networkAccess.CheckPassword(passwordToCheck);

                //  We Need To Handle Instances Where The Password Is Not Unique, Or The Request
                //  To The Server Has Encountered Issues.
                if (status[0].equals("NO")) {

                    //  If An Exception Is Thrown, Do Not Continue Looping, Instead, Exit The Loop
                    //  And Inform The Client Via A Toast That There Is An Issue. The Client Can
                    //  Then Decide If They Wish To Try Again.
                    if (!status[1].equals("Password Already Generated")) {
                        passwordGenError = status[1];
                        return false;
                    }

                    //  If We Try To Generate A Password That Already Exists, We Need To Attempt To
                    //  Generate A New Password.
                    else
                    {
                        keepRegen = true;
                    }

                }

                //  Password Is Valid, Break The Loop And Hand Value Off To UI.
                else {
                    keepRegen = true;
                }

            } while (keepRegen);

            //  Password Is Valid And No Exception Has Been Encountered. Return True To Indicate
            //  Password Can Be Used By UI And Generate A New Dialog.
            return true;
        }

        @Override
        protected void onPostExecute (final Boolean passwordGenOk) {
            mCheckPasswordTask= null;

            // ToDo: If Success, Pass Back The Password And Prompt Program To Exit Loop.
            if (passwordGenOk) {
                password = passwordToCheck;
                passwordError = false;
            }

            //ToDo: Password Is Invalid/Bug Occurred Either Rerun Loop Or Stop If Bug And Notify
            //ToDO: Client.
            else {
                passwordError = true;
                Toast.makeText(getApplicationContext(), "Password Generation Has Failed: " +
                                passwordGenError,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled () {
            mCheckPasswordTask = null;
        }
    }
}
