package com.MONT3.partypoolapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class TestActivity extends AppCompatActivity implements DialogListener{

private Button buttonJoin;
private Button buttonCreate;

private View mSplashActivity;
private View mProgressSpinner;

//  Checks Only One Async Task Can Be Fired Off From The UI. We Don't Want Multiple Instances
//  Running At Once.
private PartyPasswordCheck mCheckPasswordTask = null;
private CreateParty mCreatePartyTask = null;

//  Validate If User Should Be Informed Of An Error Through The
private String passwordGenError = null;
NetworkAccess networkAccess = null;

private PartyModes selectedMode = null;
private String password;

//  References To Party Confirm Dialog Box In Order To Manipulate Dialog From Async Process.
private RadioButton radioParty = null;
private  RadioButton radioContinuous = null;

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

        //  Initialise Our networkAccess Singleton To Provide Access To All Functionality Required
        //  To Send And Receive HTTP Requests.
        networkAccess = NetworkAccess.getNetworkAccess();

        //  Assign All Our UI Components To Their Respective Object In The Resource Files So We Can
        //  Access And Manipulate Their Properties.
        buttonJoin = findViewById(R.id.button2);
        buttonCreate = findViewById(R.id.button3);

        mSplashActivity = findViewById(R.id.splash_page_form);
        mProgressSpinner = findViewById(R.id.progressBar);

        //  Assign A Listener To Each Button
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

    //  Open The Initial Dialog That Allows The User To Select Their Mode Of Choice For The Party.
    public void openCreateDialog() {
        DialogFragment createDialog = new CreateDialog();
        createDialog.show(getSupportFragmentManager(),"create dialog");
    }

    //  Open The Dialog Box Which Will Allow The User To Enter The Password Of The Party They Wish
    //  To Join.
    public void openJoinDialog() {
        JoinDialog joinDialog = new JoinDialog();
        joinDialog.show(getSupportFragmentManager(),"join dialog");
    }

    //  Open The Dialog Which Allows Users Creating A Password To Finalise Their Decision. The User
    //  Is Shown Their Unique Password And The Mode They Have Selected.
    public void openPartyConfirmDialog(PartyModes partyIn, String passwordIn) {
        PartyPasswordDialog confirmDialog = new PartyPasswordDialog().newInstance(partyIn, passwordIn);
        confirmDialog.show(getSupportFragmentManager(), "confirm dialog");
    }

    //  Interface Methods That Will Allow The UI Thread To Manipulate Dialog Views. This Will Allow
    //  Us To Evaluate Which Mode Has Been Selected And Run The Async Method To Check The Password
    //  As Soon As The Dialog Is Closed.
    @Override
    public void onDialogPositiveClickModeSelect(DialogFragment dialog) {

        radioContinuous = (RadioButton) dialog.getDialog().findViewById
                (R.id.continuousRadio);
        radioParty = (RadioButton) dialog.getDialog().findViewById
                (R.id.partyRadio);

        if (mCheckPasswordTask != null)
        {
            return;
        }


         /*  Generate A New Password And Evaluate Against The Database To Test For It's Validity.
         *  All Passwords Need To Be Unique To Identify The Party And Act As A Reference. If By
         *  Chance, A Password That Is Generate Matches And Already Existed Function, Generate A
         *  Brand New Password. */

        mCheckPasswordTask = new PartyPasswordCheck();
        mCheckPasswordTask.execute((Void) null);
        showProgress(true);
    }

    @Override
    public void onDialogNegativeClickModeSelect(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }

    @Override
    public void onDialogPositiveClickCreateParty(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClickCreateParty(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            //  Determine Visibility Of Scroll View Based On Current Visibility. I.e. If Shown, Hide
            //  And Vice Versa.
            mSplashActivity.setVisibility(show ? View.GONE : View.VISIBLE);
            mSplashActivity.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSplashActivity.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressSpinner.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressSpinner.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressSpinner.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
        else {
            mProgressSpinner.setVisibility(show ? View.VISIBLE : View.GONE);
            mSplashActivity.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    //  Password Checks Need To Be Done Away From The UI Thread. AsyncTask Provides A Simple
    //  Means Of Doing So For Short Networking Functions Such As The Checks We Require On A Seperate
    //  Thread.
    public class PartyPasswordCheck extends AsyncTask <Void, Void, Boolean> {
        String passwordToCheck;
        String[] status;

        PartyPasswordCheck() {
            passwordToCheck = null;
            status = null;
        }

        //  Randomly Generate Our Password So Clients Can Create A Party That Can Be Uniquely
        //  Identified.
        public String GeneratePartyPassword(int length) {

            StringBuilder password = new StringBuilder();
            char[] passwordCharacterSet =
                    "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
            int charsetLength = passwordCharacterSet.length;

            Random randomNumberGenerator = new Random();

            for (int i = 0; i < length; i++) {
                char randomCharacter = passwordCharacterSet[randomNumberGenerator.nextInt
                        (charsetLength)];
                password.append(randomCharacter);
            }

            return password.toString();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean keepRegen = false;

            //  We Need To Keep Attempting To Generate A Password If We Don't Have A Unique Password
            //  Or An Error Occurs (Chances Are The Error Will Keep Repeating, So Cancel The Async
            //  Task And Inform The User That Something Has Gone Wrong).
            do {
                boolean isError = false;

                //  Verify Uniqueness of Password Against Database To Prevent Like Passwords
                //  Occuring.
                passwordToCheck = GeneratePartyPassword(4);
                status = networkAccess.CheckPassword(passwordToCheck);

                //  We Need To Handle Instances Where The Password Is Not Unique, Or The Request
                //  To The Server Has Encountered Issues.
                if (status[0].equals("NO")) {

                    //  If An Exception Is Thrown, Do Not Continue Looping, Instead, Exit The Loop
                    //  And Inform The Client Via A Toast That There Is An Issue. The Client Can
                    //  Then Decide If They Wish To Try Again.
                    if (!status[1].equals("PASSWORD ALREADY GENERATED")) {
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
                    keepRegen = false;
                }

            } while (keepRegen);

            //  Password Is Valid And No Exception Has Been Encountered. Return True To Indicate
            //  Password Can Be Used By UI And Generate A New Dialog.
            return true;
        }

        @Override
        protected void onPostExecute (final Boolean passwordGenOk) {
            mCheckPasswordTask = null;
            showProgress(false);

            if (passwordGenOk) {
                password = passwordToCheck;

                if(radioParty.isChecked())
                {
                    openPartyConfirmDialog(PartyModes.PARTY, password);
                }

                else if(radioContinuous.isChecked())
                {
                    //choice = 2;
                    Toast.makeText(getBaseContext(),"Selected option: Continuous Mode",
                            Toast.LENGTH_SHORT).show();
                }
            }

            else {
                Toast.makeText(getApplicationContext(), "Password Generation Has Failed: " +
                                passwordGenError,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled () {
            mCheckPasswordTask = null;
            showProgress(false);
        }
    }

    //  Once We Verify Our Password Is Unique And The User Is Happy With Their Decision, The
    //  Party Needs To Be Created On The Database Side.
    public class CreateParty extends AsyncTask <Void, Void, Boolean> {

        String password;
        PartyModes mode;
        String admin;
        String [] status;

        public CreateParty(String passwordIn, PartyModes modeIn, String adminIn) {
            password = passwordIn;
            mode = modeIn;
            admin = adminIn;
            status = null;
        }

        @Override
        protected Boolean doInBackground (Void... params) {
            status = networkAccess.CreateParty(password, mode.toString(), admin);

            if (status[0].equals("NO")) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute (final Boolean partyCreationOk) {
            mCreatePartyTask = null;
            showProgress(false);

            if (selectedMode == PartyModes.PARTY)
            {
                //  TODO: Load Party Actvitiy.
            }

            else
            {
                // TODO: Load Continuous Activity.
            }
        }

        @Override
        protected  void onCancelled () {
            mCreatePartyTask = null;
            showProgress(false);
        }
    }
}
