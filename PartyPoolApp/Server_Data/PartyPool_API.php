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
                echo "TWO";
            break;

            default:
                json_encode(new JsonResponse("NO","Invalid Number Of Parameters Supplied", null));
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