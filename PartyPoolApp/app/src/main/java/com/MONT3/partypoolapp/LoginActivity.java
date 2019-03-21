package com.MONT3.partypoolapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

/**
 * A login screen that offers login via email/password.
 */

public class LoginActivity extends AppCompatActivity {
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
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

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

        //  Hardcoded User Account. If Input Is Not Equal To This, Login Fails.
        else if(!email.equals("testuser") || !password.equals("password"))
        {
            mEmailView.setError("Incorrect Credentials");
            mPasswordView.setError("Incorrect Credentials");
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
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
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        //  Reference To The Server And The Function We Want To Invoke Via The LOGIN parameter.
        //  This Will Not Change, So Define It As final.
        private final String mUrl = "https://computing.derby.ac.uk/~partypool/LOGIN";

        private String mResponseMessage = "";
        private String mResponseBody = "";

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        //  Upon Initialisation And Execution Of The Asynchronous Task, We Need To Set Up Our
        //  Connection And Run Our Query Which Will Return The Credentials Requested From The Server.
        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                String line = "";
                String result = "";

                URL url = new URL("https://computing.derby.ac.uk/~partypool/LOGIN");
                HttpURLConnection testConn = (HttpsURLConnection) url.openConnection();

                testConn.setRequestMethod("POST");
                testConn.setDoOutput(true);
                testConn.setRequestProperty( "Content-type", "application/x-www-form-urlencoded");
                testConn.setRequestProperty( "Accept", "*/*" );



                InputStream inputStream = testConn.getInputStream();
                BufferedReader streamReader = new BufferedReader(new InputStreamReader
                        (inputStream,"iso-8859-1" ));

                while ((line = streamReader.readLine()) != null){
                    result += line;
                }
            }

            catch (IOException e)
            {
                e.printStackTrace();
            }

            try {
                //  Open Our Connection To Our Commerce3 Server.
                URL url = new URL(mUrl);
                HttpURLConnection connectionToServer = (HttpsURLConnection) url.openConnection();

                connectionToServer.setDoOutput(true);
                connectionToServer.setDoInput(true);
                connectionToServer.setRequestMethod("GET");
                connectionToServer.setRequestProperty("Content=Type", "application/json;" +
                        "charset=utf-8");

                //  Set The Body Of Our Post Request To The JSON Encoded Credentials. This Needs To
                //  Be In Proper JSON Format In Order For The Web Service To Read The Credentials
                //  Properly. Once Complete, Dispatch The Message Across The Network.
                try {
                    JSONObject credentials = buildLoginJSONRequest();
                    setPostRequestContent(connectionToServer, credentials);
                    connectionToServer.connect();

                    mResponseMessage = connectionToServer.getResponseMessage();
                    mResponseBody = receiveLoginResponse(connectionToServer);
                    connectionToServer.disconnect();
                }

                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            catch (MalformedURLException e) {
                e.printStackTrace();
            }

            catch (IOException e) {
                e.printStackTrace();
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent myIntent = new Intent(LoginActivity.this, TestActivity.class);
                myIntent.putExtra("PASSDATA",mEmail.toString());
                LoginActivity.this.startActivity(myIntent);
            }

            else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        //  Method To Build Our JSON Body. This Will Be Sent To The Web Service And Passed To The
        //  SQL Query.
        private JSONObject buildLoginJSONRequest() throws JSONException{
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("username", mEmail);
            jsonObject.accumulate("password", mPassword);

            return jsonObject;
        }

        //  This Function Dispatches Any JSONEncoded Data To The Server For Processing.
        private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonCredentials)
            throws IOException {

            //  Establish The Stream We Are Sending Our Data Across As Well As The Writer Which Is
            //  Encoding The Data We Provide It.
            OutputStream output = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter
                    (output, "UTF-8"));

            writer.write(jsonCredentials.toString());
            writer.flush();
            writer.close();
            output.close();
        }

        //  Returns The Response Received From The HTTPS Request.
        private String receiveLoginResponse (HttpURLConnection conn) throws IOException {

            String line = "";
            String result = "";

            //  Set Up A Stream Ready To Receive The HTTP Response.
            InputStream inputStream = conn.getInputStream();
            BufferedReader loginRequestReader = new BufferedReader(new InputStreamReader
                    (inputStream, "iso-8859-1"));

            //  While There Is Data To Read, Read Each Line And Append To The Result.
            while ((line = loginRequestReader.readLine())!= null){
                result += line;
            }

            //  Clean Up Our Resources And Then Send Back Return The Response Body.
            loginRequestReader.close();
            inputStream.close();

            return result;
        }
    }
}

