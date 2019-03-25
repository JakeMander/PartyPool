package com.MONT3.partypoolapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

//import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class AccountCreation extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    //private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private EditText mRePasswordView;
    private View mProgressView;
    private View mSignUpFormView;


    //  On Creation Of The Activity, Set The View To The "account_creation", And Assign All The
    //  View Components (I.e. Text Boxes, Buttons etc...) To Their Respective UI Reference.
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation);

        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.create_username);
        mPasswordView = (EditText) findViewById(R.id.create_password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptSignUp();
                    return true;
                }
                return false;
            }
        });

        mRePasswordView = (EditText) findViewById(R.id.create_re_password);
        mRePasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptSignUp();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.sign_up_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });

        mSignUpFormView = findViewById(R.id.signup_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptSignUp() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mRePasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String repassword = mRePasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
            if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;

        }else if(!password.equals(repassword)){
                mRePasswordView.setError(getString(R.string.error_password_not_match));
                focusView = mRePasswordView;
                cancel = true;
            }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        else if (!isEmailValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }

        else {
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String username) {
        return (username.length() >= 6 && username.length() <= 18);

    }

    private boolean isPasswordValid(String password) {
        return password.length() > 6 && password.length() < 18;
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

            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSignUpFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private String mError;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
            mError = "";
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                //  Define The Location Of Our Web Service Function And Create A Connection To
                //  The Web Service.
                final URL url = new URL
                        ("https://computing.derby.ac.uk/~partypool/CREATEACCOUNT");
                HttpURLConnection testConn = (HttpsURLConnection) url.openConnection();

                //  Define Our Request As "POST" and Enable Output And Input. Allow For HTTP Read/
                //  Write And Specify The Type Of Data We Are Handling.
                testConn.setRequestMethod("POST");
                testConn.setDoOutput(true);
                testConn.setDoInput(true);
                testConn.setRequestProperty( "Content-type", "application/json");

                //  Build Our JSON Object Based On The Password And Username We Have Received.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("username", mUsername);
                jsonObject.accumulate("password", mPassword);

                //  Send Our HTTP Request Off To The Webservice Via A BufferedWriter.
                OutputStream output = testConn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter
                        (output, "UTF-8"));

                writer.write(jsonObject.toString());
                writer.flush();
                writer.close();

                //  Receive HTTP Request Via A Buffered Reader.
                String line = "";
                String result = "";

                InputStream inputStream = testConn.getInputStream();
                BufferedReader streamReader = new BufferedReader(new InputStreamReader
                        (inputStream, "iso-8859-1"));

                while ((line = streamReader.readLine()) != null) {
                    result += line;
                }

                //  Creates A General JSON Object To Hold All The JSON Data. We Can Then Parse
                //  Each Individual Component From This.
                JSONObject receivedCredentials = new JSONObject(result);
                JSONArray jsonResponse = receivedCredentials.getJSONArray("jsonResponse");

                String status = jsonResponse.getString(0);

                //  If First Index Is No, Login Has Failed. Display A Toast To Inform User And
                //  Detail Reason.
                if (status.equals("NO"))
                {
                    mError = jsonResponse.getString(1);
                    return false;
                }
            }

            catch (IOException e) {
                e.printStackTrace();
                mError = e.toString();
            }

            catch (JSONException e){
                e.printStackTrace();
                mError = e.toString();
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent myIntent = new Intent(AccountCreation.this,TestActivity.class);
                myIntent.putExtra("CREATEDATA", mUsername);
                AccountCreation.this.startActivity(myIntent);
            }

            else {
                mPasswordView.setError("Create Account Has Failed: " + mError);
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

