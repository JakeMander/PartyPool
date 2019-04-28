<?php

 /**
 * Created by PhpStorm.
 * User: Jake Mander
 * Date: 03/03/2019
 * Time: 19:42

 *  NOTES:
 *  Parameters Should, In A Standard HTTP GET Request, Be Sent To The Web Service In The Form:
 *  www.commerce3.derby.ac.uk/~partypool?action=value&subaction=value
 *
 *  Where ? Signifies The Start Of The GET Parameters, And & Is Used To
 *  Chain Multiple Parameters Together. These Are Assembled Into "Key":"Value" Pairs.
 *
 *  Wayne's Code Has Been Written In Such A Way, That The htaccess File Rewrites The URL In Such A Way, That The
 *  String After ? Are Assigned To An Associative Array Key With The Value Of 'q'. Explode is Then Used To Split
 *  These Parameters By "\" Through Accessing The String Stored In GET['q']. If The URL Is Stored In The Format:
 *
 *  https://computing.derby.ac.uk/~partypool/x/y/z
 *
 *  Then q Stores "x/y/z" Then Explodes The String Via "/" To Create An Array With Values x, y and z. We Can Then
 *  Use The Values To Determine What Action We Are To Take, And Which Values We Can Supply To Any SQL Queries We May
 *  Need To Make.
 */

require "RestService.php";
require "Database_Classes/User.php";
require "JsonResponse.php";

class PartyPool_API extends RestService
{

    //  Login Credentials For Our Commerce3 Database.
    private $connString = "host=localhost port=5432 dbname=pr_partypool user=pr_partypool password=kJVjC7z";

    public function __construct()
    {
        //  Pass In An Empty String To Wayne's Service Class. We Do Not Need To Compare The Root Of The URL.
        parent::__construct("");
    }

    public function performGet($url, $parameters, $requestBody, $accept)
    {
        //  Group Functionality By Number Of Parameters For Easier Reading And More Logical Structure.
        switch (count($parameters))
        {
            case 2:
                //  TODO: Remove Once App Is Running.
                //  Test Function For Use In Browser. Checks Connection And Runs A Simple SELECT PostgreSQL Query
                //  To Retrieve A TestUser Password.
                if (strtoupper($parameters[0]) == "TESTLOGIN")
                {
                    if ($parameters[1] == null)
                    {
                        die (json_encode(new JsonResponse("NO", "NO USERNAME SUPPLIED", null)));
                    }

                    $userValue = $parameters[1];
                    $conn = pg_connect($this->connString);

                    //  Test To See If A Connection Can Be Established
                    if ($conn)
                    {
                        //  Attempt To Retrieve Data From The Database And Handle Any Exceptions That Occur.
                        try
                        {
                            //  Statement To Retrieve The Associated Password Of A Supplied User.
                            $sql = 'SELECT * FROM users WHERE username = $1 ORDER BY username';
                            //  Ensure Our SQL Is Sanitised! Prepare The SQL Statement And Bind The Parameters We Supply
                            //  Via The Array To The $1.
                            pg_prepare($conn, "TestUserCheck", $sql);
                            $result = pg_execute($conn, "TestUserCheck", array($userValue));

                            //  If Query Fails, Return The Error Message Returned By Database.
                            if (!result)
                            {
                                die (json_encode(new JsonResponse("NO", "QUERY AS FAILED",
                                    null)));
                            }

                            //  While pg_fetch_row Has Rows To Read, Read The Next Returned Row And Echo Fields.
                            else if (pg_num_rows($result) != 0)
                            {
                                $users = array();

                                while ($row = pg_fetch_row($result))
                                {
                                    $user = new User($row[0], $row[1]);

                                    echo "Row[0]:".$row[0];
                                    echo "Row[1]:".$row[1];

                                    array_push($users, $user);
                                    echo"User1: ".$user->getUsername();
                                    echo"User2: ".$user->getPassword();

                                }

                                echo json_encode(new JsonResponse("YES", "OK", $users));
                            }

                            else
                            {
                                echo json_encode(new JsonResponse("NO", "NO VALUES RETURNED FROM QUERY",
                                    null));
                            }
                        }

                        catch (Exception $e)
                        {
                            echo json_encode(new JsonResponse("NO","ERROR WITH QUERY: $e",
                                null));
                        }

                        finally
                        {
                            pg_close($conn);
                        }
                    }

                    //  We Have Been Unable To Establish A Connection To The Database. Terminate Execution Of Script.
                    else
                    {
                        die(json_encode(new JsonResponse("NO","CONNECTION HAS FAILED", null)));
                    }
                }

                //  Check A PartyPassword For Uniqueness Then Inform The Client.
                else if (strtoupper($parameters[0]) == "CHECKPASSWORD")
                {
                    if ($parameters[1] == null)
                    {
                        die (json_encode(new JsonResponse("NO", "NO PASSWORD SUPPLIED", null)));
                    }

                    $partyPassword = $parameters[1];
                    $conn = pg_connect($this -> connString);

                    if ($conn)
                    {
                        try
                        {
                            $sql = 'SELECT * FROM party WHERE password = $1';
                            pg_prepare($conn, "PasswordCheck", $sql);
                            $result = pg_execute($conn, "PasswordCheck", array($partyPassword));

                            if (!result)
                            {
                                die (json_encode(new JsonResponse("NO", "QUERY HAS FAILED", null)));
                            }

                            //  We Need To Inform The Client In The Instance A Requested Party Password Is Already Taken.
                            if (pg_num_rows($result) != 0)
                            {
                                echo (json_encode(new JsonResponse("NO", "PASSWORD ALREADY GENERATED", null)));
                            }

                            else
                            {
                                echo(json_encode(new JsonResponse("YES", "PASSWORD IS VALID", $partyPassword)));
                            }
                        }

                        catch (Exception $e)
                        {
                            die(json_encode("NO", "ERROR WITH QUERY", $e));
                        }

                        finally
                        {
                            pg_close($conn);
                        }
                    }

                    else
                    {
                        die(json_encode(new JsonResponse("NO","CONNECTION HAS FAILED", null)));
                    }
                }

                else
                {
                    die (json_encode(new JsonResponse("NO","UNKNOWN PARAMETER SUPPLIED", null)));
                }
            break;

            default:
                die (json_encode(new JsonResponse("NO", "INVALID NUMBER OF PARAMETERS SUPPLIED", null)));
                break;
        }
    }

    public function performPost($url, $parameters, $requestBody, $accept)
    {
        switch (count($parameters))
        {
            case 1:
                if (strtoupper($parameters[0]) == "LOGIN")
                {
                    //  Split The Credentials Passed Over Via The POST Body So The Service Can Handle Each Credential
                    //  Individually.
                    $jsonCredentials = json_decode($requestBody);

                    //  If The Decoded JSON Data Has The Correct Key Values, Assign Associated Name Value To Variables
                    //  For Processing.
                    if (isset($jsonCredentials -> {'username'}) && isset($jsonCredentials -> {'password'}))
                    {
                        $username = $jsonCredentials->{'username'};
                        $password = $jsonCredentials->{'password'};

                        //  Pass In The Username And Password So We Can Check The Database For A Matching Pair.
                        try
                        {
                            $conn = pg_connect($this->connString);
                            $sql = 'SELECT username FROM users WHERE username = $1 AND password = $2';
                            pg_prepare($conn, "Login", $sql);
                            $result = pg_execute($conn, "Login", array($username, $password));

                            if (!$result)
                            {
                                //
                                die (json_encode(new JsonResponse("NO",
                                    "Query Has Failed", null)));
                            }

                            //  A Valid Login Should Only Yield One Matching Pair Of Data. Any Other Responses Are Either
                            //  Invalid Credentials, Or Is A Invalid Query.
                            if (pg_num_rows($result) != 1)
                            {
                                echo json_encode(new JsonResponse("NO",
                                    "You Have Entered An Incorrect Username Or Password", null));
                            }

                            else
                            {
                                $row = pg_fetch_row($result);
                                echo json_encode(new JsonResponse("YES",
                                    "OK", $row[0]));
                            }
                        }

                        catch(Exception $e)
                        {
                            die (json_encode(new JsonResponse("NO",
                            "Query Has Failed For The Following Reason".$e, null)));
                        }

                        finally
                        {
                            pg_close($conn);
                        }

                    }

                    else
                    {
                        echo (json_encode(new JsonResponse("NO", "Missing Credentials", null)));
                    }

                }

                else if (strtoupper($parameters[0]) == "CREATEACCOUNT")
                {
                    $jsonCredentials = json_decode($requestBody);

                    if (isset($jsonCredentials -> {'username'}) && isset($jsonCredentials -> {'password'}))
                    {
                        $username = $jsonCredentials->{'username'};
                        $password = $jsonCredentials->{'password'};

                        //  Check To See If The Username Already Exists. Username Should Be Unique Across The Database.
                        try
                        {
                            $conn = pg_connect($this->connString);
                            $sql = 'SELECT username FROM users WHERE username = $1';
                            pg_prepare($conn, "Login", $sql);
                            $result = pg_execute($conn, "Login", array($username));

                            if (!$result)
                            {
                                die (json_encode(new JsonResponse("NO", "Query Has Failed",
                                    null)));
                            }

                            //  A Valid Login Should Only Yield One Matching Pair Of Data. Any Other Responses Are Either
                            //  Invalid Credentials, Or Is An Invalid Query.
                            if (pg_num_rows($result) != 0)
                            {
                                echo json_encode(new JsonResponse("NO", "Username Is Already Taken",
                                    null));

                                pg_close($conn);
                            }

                            //  Username Is Unique And Valid. Insert The Username/Password Pair Into Our Database.
                            else
                            {
                                //  Close The Old Connection To Clear Up Previous Values.
                                pg_close($conn);

                                $conn = pg_connect($this->connString);
                                $sql = 'INSERT INTO users (username, password) VALUES ($1, $2)';
                                pg_prepare($conn, "Login", $sql);
                                $result = pg_execute($conn, "Login", array($username, $password));
                                echo json_encode(new JsonResponse("YES", "Account Created",
                                    null));

                                if (!$result)
                                {
                                    die (json_encode(new JsonResponse("NO", "Query Has Failed: ".pg_errormessage($conn),
                                        null)));
                                }
                            }
                        }

                        catch (Exception $e)
                        {
                            die(json_encode(new JsonResponse("NO", "Query Has Failed: ".$e,
                                null)));
                        }

                        finally
                        {
                            pg_close($conn);
                        }
                    }

                    else
                    {
                        echo (json_encode(new JsonResponse("NO", "Missing Credentials", null)));
                    }
                }

                //  Password Has Been Verified As Unique. Service Will Now Create A Party Which Will Contain Details
                //  Regarding Mode And Admin Permissions. The Password Will Act As A Foreign Key To Song Data.
                else if ($parameters[0] == "CREATEPARTY")
                {
                    $partyDetails = json_decode($requestBody);

                    //  Ensure All The Data Required To Update Our Database Has Been Set. Missing Data Will Cause
                    //  Exceptions On The DBMS.
                    if(isset($partyDetails ->{"password"}) && isset($partyDetails ->{"mode"})
                         && isset ($partyDetails ->{"timestamp"}) && isset ($partyDetails ->{"admin"}))
                    {

                        //  Extract The Values We Need To Update Our Party And Association Table From JSON.
                        $password = $partyDetails -> {"password"};
                        $mode = $partyDetails -> {"mode"};
                        $timestamp = $partyDetails -> {"timestamp"};
                        $admin = $partyDetails -> {"admin"};

                        //  Update Our Party Values Then Establish An Association Between The New Party And The User
                        //  Who Is Creating The Party (Admin). This Will Allow Us To Logically Manage User And Party
                        //  Details Separately.
                        try
                        {
                            $conn = pg_connect($this->connString);

                            //  Initialise Our SQL Statements We Will Use To Update Both The Party Table And The Party
                            //  Association Table.
                            $sqlInsertParty = 'INSERT INTO party VALUES ($1, $2, $3)';
                            $sqlInsertAssociation = 'INSERT INTO partyassociation VALUES ($1, $2)';

                            //  Begin A Transaction To Conform To ACID Principles. A Query Should Change All Or
                            //  Change Nothing!
                            pg_query($conn,"BEGIN");

                            //  Prepare Our Statements And Send Them Over To The DBMS So They Can Be Called.
                            pg_prepare($conn, "CreatePartyAddParty", $sqlInsertParty);
                            pg_prepare($conn, "CreatePartyAddAssociation", $sqlInsertAssociation);

                            //  Our Queries Must Be Executed Separately, So Store Each Result Separately So We Can
                            //  Evaluate If They Were Successful or Not.
                            $result1 = pg_execute($conn, "CreatePartyAddParty",
                                array($password, $mode, $timestamp));
                            $result2 = pg_execute($conn, "CreatePartyAddAssociation",
                                array($admin, $password));

                            //  If One Of The Queries Fails, Attempt To Rollback To The Previous Values Stored In
                            //  The Database.
                            if (!$result1 || !$result2)
                            {
                                if (pg_query($conn,"ROLLBACK"))
                                {
                                    die(json_encode(new JsonResponse("NO", "Query Has Failed Values Rolled Back",
                                        null)));
                                }

                                //  If The Rollback Fails, Inform The User And Kill The Script. This Allows Client Side
                                //  To Be Made Aware That The Party Has Not Been Created. The Server Will Handle The
                                //  Dead Transaction On The Server Side.
                                else
                                {
                                    die(json_encode(new JsonResponse("NO", "Rollback Has Failed: ", null)));
                                }
                            }

                            echo (json_encode(new JsonResponse("YES", "Changes Successfully Committed", null)));
                            pg_query($conn, "COMMIT");
                        }

                        catch (Exception $e)
                        {
                            die(json_encode(new JsonResponse("NO", "Query Has Failed: ".$e,
                                null)));
                        }

                        finally
                        {
                            pg_close($conn);
                        }

                    }

                    else {
                        die(json_encode(new JsonResponse("NO", "Required JSON Parameters Not Received: ", null)));
                    }
                }

                else
                {
                    echo (json_encode(new JsonResponse("NO", "Invalid Parameter", null)));
                }
            break;

            default:
                echo (json_encode(new JsonResponse("NO", "Invalid Number Of Parameters",
                    null)));
                break;
        }
    }
}
?>