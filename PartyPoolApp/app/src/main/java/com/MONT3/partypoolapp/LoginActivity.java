package com.MONT3.partypoolapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

// A login screen that offers login via email/password.
public class LoginActivity extends AppCompatActivity {

    //  Keep track of the login task to ensure we can cancel it if requested.
    private UserLoginTask mAuthTask = null;

    // UI references.
    private View mProgressView;
    private View mLoginFormView;

    // Login References
    private AccountSecurity mHasher;
    private EditText mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mHasher = AccountSecurity.InitialisePasswordHash();

        Button button1 = (Button) findViewById(R.id.button);
        button1.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LoginActivity.this, AccountCreation.class);
                startActivity(intent1);
            }
        });

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    private void attemptLogin() {

        //  If No Asynchronous Task Is Set To Be Called Upon Credentials Being Successfully
        // Validated On The Client Side, Exit The Method.
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        /*
         *  Cancel Variable Is Used To Determine If Login Attempt Creates The Asynchronous
         *  Process That Makes The HTTP Request To Check Credentials Against The Database.
         *
         *  focusView Stores A Reference To The Component That Is To Be Selected To Receive
         *  Input On The Interface Upon The User Entering An Invalid Input.
        */

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Checks To See If The String Retrieved From The Username TextView Is Empty.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        //  Run The isEmailValid Method. This Defines Our Criteria For A Valid Email And Validaites
        //  Input Against It.
        else if (!isEmailValid(email)) {
            mEmailView.setError("Username should be 6-12 characters");
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }

        else {
            // Show a progress spinner, hide the activity and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    //  Method Defines Our Own Criteria For What Makes A Valid Username.
    private boolean isEmailValid(String email) {
        return (email.length() >= 6 && email.length() <= 12);
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private String mPassword;
        private String mError;

        UserLoginTask(String email, String password) {
            mUsername = email;
            mPassword = password;
            mError = "";
        }

        //  Run The Login Attempt In The Background. Notify onPostExecute Of Status Via Boolean
        //  Value in Order To Determine If App Progresses To Splash Page Or Prompts User To Attempt
        //  New Login.
        @Override
        protected Boolean doInBackground(Void... params) {
                NetworkAccess network = NetworkAccess.getNetworkAccess();
                String[] status = network.Login(mUsername, mPassword);

                //  If First Index Is No, Login Has Failed. Set The Error Message For User Feedback.
                if (status[0].equals("NO"))
                {
                    mError = status[1];
                    return false;
                }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            //  mAuthTask Needs To Be Reset In Order To Allow Further Async Calls To Be Made. Once
            //  Our Login Attempt Has Been Made Notify The showProgress Method So It Can Stop The
            //  Loading Animation.
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent myIntent = new Intent(LoginActivity.this, TestActivity.class);
                myIntent.putExtra("LOGINDATA",mUsername);
                LoginActivity.this.startActivity(myIntent);
            }

            else {
                mPasswordView.setError("Invalid Password: " + mError);
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

