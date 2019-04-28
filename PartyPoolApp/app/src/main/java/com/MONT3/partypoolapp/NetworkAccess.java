/**
 *  Singleton Class To Handle All Network Functionality. Encapsulates Network Functions Away From UI
 *  Which Are Then Called During The Async Tasks The UI Thread Makes.
 *
 *  This Class Should only Be Instantiated Once (We Only Need One Instance Of All These Functions As
 *  We Are Not Implementing Any Unique Class Data).
 */

package com.MONT3.partypoolapp;

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
import java.net.ProtocolException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class NetworkAccess {

    private static NetworkAccess instance = null;
    private static final String baseURL = "https://computing.derby.ac.uk/~partypool";
    private AccountSecurity hasher = AccountSecurity.InitialisePasswordHash();

    //  Private Constructor To Prevent Manual Initialisation.
    private NetworkAccess(){}

    //  Synchronised To Allow For Thread Safe Access.
    public static synchronized NetworkAccess getNetworkAccess(){

        //  If No Instance Has Already Been Created, Attempt To Instantiate A New Object.
        if(instance == null) {

            try {
                instance = new NetworkAccess();
            }

            catch (Exception e){
                e.printStackTrace();
            }
        }
        return instance;
    }

    public String[] Login(String userIn, String passwordIn) {

        String userPassword = passwordIn;
        String userUsername = userIn;

        //  Default Condition - Login Request Has Failed. Pass Error Back To UI And Display As Part
        // Of The EditTextView
        String[] status = {"NO", "Network Error: Unhandled", "NODATA"};

        //  Hash Our Password Using SHA-256 So The Input Matches With The Database End.
        try {
            userPassword = hasher.HashPassword(userPassword);

            //  Define The Location Of Our Web Service Function And Create A Connection To
            //  The Web Service.
            try {
                HttpURLConnection networkConnection = EstablishAuthenticationConnection(new URL
                                (baseURL + "/LOGIN"), "POST");

                //  Build Our JSON Object Based On The Password And Username We Have Received.
                try {
                    JSONObject jsonObject = BuildUserJSON(userUsername, userPassword);
                        //  Send Our HTTP Request Off To The Webservice Via A BufferedWriter.
                        try {
                            ConnectionWriter(networkConnection, jsonObject);
                            status = ConnectionReader(networkConnection);
                            networkConnection.disconnect();
                        } catch (IOException e) {
                            status[1] = "Network Read/Write Error: " + e;
                        }
                    } catch (JSONException e) {
                        status[1] = "JSON Error: " + e;
                    }
                } catch (ProtocolException e) {
                    status[1] = "Network Protocol Error:" + e;
                }
            } catch (MalformedURLException e) {
                status[1] = "Malformed URL Error: " + e;
            }
            catch (IOException e) {
                status[1] = "Network Read/Write Error: " + e;
            }
        catch (NoSuchAlgorithmException e) {
            status[1] = "Authentication Error: " + e;
        }
        return status;
    }

    public String[] CreateAccount(String userIn, String passwordIn) {

        String userPassword = passwordIn;
        String userUsername = userIn;
        String[] status = {"NO", "Network Error: Unhandled", "NODATA"};

        //  Hash Password Using SHA-256 For Server Side Security.
        try {
            userPassword = hasher.HashPassword(userPassword);

            //  Define The Location Of Our Web Service Function And Create A Connection To
            //  The Web Service.
            try {
                HttpURLConnection networkConnection = EstablishAuthenticationConnection(new URL
                        (baseURL + "/CREATEACCOUNT"), "POST");
                try {
                    //  Build Our JSON Object Based On The Password And Username We Have Received.
                    JSONObject jsonObject = BuildUserJSON(userIn, userPassword);
                    try {
                        ConnectionWriter(networkConnection, jsonObject);
                        status = ConnectionReader(networkConnection);
                        networkConnection.disconnect();
                        } catch (IOException e) {
                            status[1] = "Network Error: Read/Write Error" + e;
                        } catch (JSONException e) {
                            status[1] = "JSON Error" + e;
                        }
                } catch (JSONException e) {
                    status[1] = "JSON Error" + e;
                }
            } catch (MalformedURLException e) {
                status[1] = "Malformed URL Exception" + e;
            } catch (IOException e) {
                status[1] = "Network Error: Read/Write Error" + e;
            }
        } catch (NoSuchAlgorithmException e) {
            status[1] = "Authentication Error: " + e;
        }

        return status;
    }

    public String[] CheckPassword(String passwordIn) {
        String password = passwordIn;
        String[] status = {"NO", "Network Error: Unhandled", "NODATA"};

        try {

            HttpURLConnection connection =  EstablishAuthenticationConnection(new URL
                    (baseURL + "/CHECKPASSWORD/" + password), "GET");

            try {
                status = ConnectionReader(connection);
                connection.disconnect();
            } catch (IOException e) {
                status[1] = e.toString();
            }
            catch (JSONException e) {
                status[1] = e.toString();
            }
        }
        catch (MalformedURLException e) {
            status[1] = e.toString();
        }

        catch (IOException e) {
            status[1] = e.toString();
            return status;
        }

        return status;
    }

    //  Once Our Party Password Has Been Validated, And The User Is Happy With Their Decision,
    //  Send All The Details Off To The Web Service For Party Creation.
    //  TODO: Add Time Stamp For Party Creation, This Will Allow Us To Remove Any Outdated Party's
    //  TODO: From The Database When A Client Logs In If They Have Not Been Closed Properly.
    public String[] CreateParty(String passwordIn, String modeIn, String adminIn,
                                String timestampIn) {

        String password = passwordIn;
        String mode = modeIn;
        String admin = adminIn;
        String timestamp = timestampIn;

        String[] status = {"NO", "Network Error: Unhandled", "NODATA"};

        try {
            HttpURLConnection connection = EstablishAuthenticationConnection (new URL
                    (baseURL + "/CREATEPARTY"), "POST");
            try {
                JSONObject jsonObject = BuildPartyJSON(password, mode, admin, timestamp);
                try {
                    ConnectionWriter(connection, jsonObject);
                    status = ConnectionReader(connection);
                    connection.disconnect();
                } catch (IOException e) {
                    status[1] = e.toString();
                }
            } catch (JSONException e) {
                status[1] = e.toString();
            }
        } catch (MalformedURLException e) {
            status[1] = e.toString();
        }

        catch (IOException e) {
            status[1] = e.toString();
        }
        return status;
    }

    //  Establish The HTTP Connection For Any Authentication Requests Using The Supplied URL.
    //  Saves On Redundant Code.
    private static HttpURLConnection EstablishAuthenticationConnection(URL urlIn, String reqMethod)
                throws IOException{

        HttpURLConnection connection =  (HttpURLConnection) urlIn.openConnection();

        connection.setRequestMethod(reqMethod);
        connection.setRequestProperty("Content-type","application/json");
        return  connection;
    }

    //  Allows Network Class To Build A Standardised JSON Package Which Can Be Used To Transport
    //  User Data Across The Network. Saves On Code Duplication.
    private static JSONObject BuildUserJSON(String usernameIn, String passwordIn)
            throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("username", usernameIn);
        jsonObject.accumulate("password", passwordIn);
        return jsonObject;
    }

    //  Database Requires Party Mode, Party Password, Timestamp And Admin In Order To Uniquely
    //  Identify The Party, Define How The Party Is Set Up And Allow Out Of Date Parties To Be
    //  Deleted Upon Logging In To The Service.
    private static JSONObject BuildPartyJSON(String passwordIn, String modeIn, String adminIn,
            String timestampIn) throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("password", passwordIn);
        jsonObject.accumulate("mode", modeIn);
        jsonObject.accumulate("timestamp", timestampIn);
        jsonObject.accumulate("admin", adminIn);
        return jsonObject;
    }

    private static void ConnectionWriter(HttpURLConnection connectionIn,JSONObject jsonIn)
            throws IOException {

        OutputStream output = connectionIn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter
                (output, "UTF-8"));
        writer.write(jsonIn.toString());
        writer.flush();
        writer.close();
    }

    private static String[] ConnectionReader(HttpURLConnection connectionIn) throws IOException,
            JSONException{
        String line = "";
        String result = "";

        InputStream input = connectionIn.getInputStream();
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(input,
                "iso-8859-1"));

        while ((line = streamReader.readLine()) != null) {
            result += line;
        }

        JSONObject receivedCredentials = new JSONObject(result);
        JSONArray jsonResponse = receivedCredentials.getJSONArray("jsonResponse");

        String[] response = {jsonResponse.getString(0), jsonResponse.getString(1),
            jsonResponse.getString(2)};

        return response;
    }
}
