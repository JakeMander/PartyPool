package com.MONT3.partypoolapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.renderscript.ScriptGroup;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
            // Show a progress spinner, and kick off a background task to
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

        private final String mEmail;
        private String mPassword;
        private String mError;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
            mError = "";
        }

        //  Upon Initialisation And Execution Of The Asynchronous Task, We Need To Set Up Our
        //  Connection And Run Our Query Which Will Return The Credentials Requested From The Server.
        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                try {
                    mPassword = mHasher.HashPassword(mPassword);
                }

                catch (NoSuchAlgorithmException e){
                    e.printStackTrace();
                    mError = e.getMessage();
                    return false;
                }

                //  Define The Location Of Our Web Service Function And Create A Connection To
                //  The Web Service.
                final URL url = new URL("https://computing.derby.ac.uk/~partypool/LOGIN");
                HttpURLConnection serverConnection = (HttpsURLConnection) url.openConnection();

                //  Define Our Request As "POST" and Enable Output And Input. Allow For HTTP Read/
                //  Write And Specify The Type Of Data We Are Handling.
                serverConnection.setRequestMethod("POST");
                serverConnection.setDoOutput(true);
                serverConnection.setDoInput(true);
                serverConnection.setRequestProperty( "Content-type", "application/json");

                //  Build Our JSON Object Based On The Password And Username We Have Received.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("username", mEmail);
                jsonObject.accumulate("password", mPassword);

                //  Send Our HTTP Request Off To The Webservice Via A BufferedWriter.
                OutputStream output = serverConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter
                        (output, "UTF-8"));

                writer.write(jsonObject.toString());
                writer.flush();
                writer.close();

                //  Receive HTTP Request Via A Buffered Reader.
                String line = "";
                String result = "";

                InputStream inputStream = serverConnection.getInputStream();
                BufferedReader streamReader = new BufferedReader(new InputStreamReader
                        (inputStream, "iso-8859-1"));

                while ((line = streamReader.readLine()) != null) {
                    result += line;
                }

                //  Ensure Our Connection Is Properly Terminated.
                serverConnection.disconnect();

                //  Creates A General JSON Object To Hold All The JSON Data. We Can Then Parse
                //  Each Individual Component From This.
                JSONObject receivedCredentials = new JSONObject(result);
                JSONArray jsonResponse = receivedCredentials.getJSONArray("jsonResponse");

                String status = jsonResponse.getString(0);

                //  If First Index Is No, Login Has Failed. Set The Error Message For User Feedback.
                if (status.equals("NO"))
                {
                    mError = jsonResponse.getString(1);
                    return false;
                }
            }

            catch (IOException e) {
                e.printStackTrace();
                mError = e.toString();
                return false;
            }

            catch (JSONException e){
                e.printStackTrace();
                mError = e.toString();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent myIntent = new Intent(LoginActivity.this, TestActivity.class);
                myIntent.putExtra("LOGINDATA",mEmail.toString());
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

