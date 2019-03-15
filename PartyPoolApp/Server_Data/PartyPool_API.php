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

class PartyPool_API extends RestService
{

    //  Login Credentials For Our Commerce3 Database.
    private $connString = "host=localhost port=5432 dbname=pr_partypool user=pr_partypool password=kJVjC7z";

    public function __construct()
    {
        //  Pass In An Empty String To Wayne's Service Class. We Do Not Need To Compare The Root Of The URL.
        parent::__construct("");
        echo "PARENT CONSTRUCTED";
    }

    public function performGet($url, $parameters, $requestBody, $accept)
    {
        //  Group Functionality By Number Of Parameters For Easier Reading And More Logical Structure.
        switch (count($parameters)) {

            case 1:
                break;

            case 2:

                //  TODO: Remove Once App Is Running.
                //  Test Function For Use In Browser. Checks Connection And Runs A Simple SELECT PostgreSQL Query
                //  To Retrieve A TestUser Password.
                if (strtoupper($parameters[0]) == "TESTLOGIN")
                {
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
                                die (pg_last_error($conn));
                            }

                            //  While pg_fetch_row Has Rows To Read, Read The Next Returned Row And Echo Fields.
                            if (pg_num_rows($result) != 0)
                            {
                                while ($row = pg_fetch_row($result))
                                {
                                    echo "USERNAME: $row[0]\t PASSWORD: $row[1]";
                                }

                                echo "Number Of Returned Results: " . pg_num_rows($result) . "";
                            }

                            else
                            {
                                echo "No Rows Were Returned";
                            }
                        }

                        catch (Exception $e)
                        {
                            echo "ERROR WITH QUERY: $e";
                        }

                        finally
                        {
                            pg_close($conn);
                            echo "Connection Closed";
                        }
                    }

                    //  We Have Been Unable To Establish A Connection To The Database. Terminate Execution Of Script.
                    else
                    {
                        die("Connection Has Failed");
                    }
                }

                else
                {
                    echo "Invalid Parameter";
                }
            break;

            default:
                echo "Invalid Number Of Parameters Supplied";
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
                    echo "POST LOGIN DETECTED";

                    //  Split The Credentials Passed Over Via The POST Body So The Service Can Handle Each Credential
                    //  Individually.
                    $jsonCredentials = json_decode($requestBody);

                    //  If The Decoded JSON Data Has The Correct Key Values, Assign Associated Name Value To Variables
                    //  For Processing.
                    if (isset($jsonCredentials -> {'username'}) && isset($jsonCredentials -> {'password'}))
                    {
                        $username = $jsonCredentials->{'username'};
                        $password = $jsonCredentials->{'password'};

                        echo "Received Username:" . $username . "";
                        echo "Received Password:" . $password . "";

                        //  Pass In The Username And Password So We Can Check The Database For A Matching Pair.
                        try
                        {
                            $conn = pg_connect($this->connString);
                            $sql = 'SELECT username FROM users WHERE username = $1 AND password = $2';
                            pg_prepare($conn, "Login", $sql);
                            $result = pg_execute($conn, "Login", array($username, $password));

                            if (!$result)
                            {
                                die("SQL Query Has Failed");
                            }

                            //  A Valid Login Should Only Yield One Matching Pair Of Data. Any Other Responses Are Either
                            //  Invalid Credentials, Or Is A Invalid Query.
                            if (pg_num_rows($result) != 1)
                            {
                                echo "Login Has Failed";
                            }

                            else
                            {
                                $row = pg_fetch_row($result);
                                echo "Login Successful:";
                                echo "USERNAME: $row[0]";
                            }

                            pg_close($conn);
                        }

                        catch(Exception $e)
                        {
                            die("An Error Has Occurred During The Login Process: ".$e);
                        }

                    }

                    else
                    {
                        echo "JSON Keys Not Found In Body";
                    }

                }

                else
                {
                    echo"Invalid Parameter";
                }
            break;

            default:
                echo "Invalid Number of Parameters Supplied";
                break;
        }
    }
}
?>