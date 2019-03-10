<?php
/**
 * Created by PhpStorm.
 * User: Jake Mander
 * Date: 03/03/2019
 * Time: 19:42
 */

/*
 *  All Requests Made To The Server Will Go Through The PartyPool API. Each Requests Invokes The Base Class Method
 *  handleRawRequest Which Strips The Components Embedded In The URL And Passes The Parameters On To The handleRequest
 *  method. This Then Determines The Type Of The HTTP Request And Then Ovverides The Appropriate Function.
 *
 *
 *
 */

require "RestService.php";

class PartyPool_API extends RestService
{

    private $connString = "host=localhost port=5432 dbname=pr_partypool user=pr_partypool password=kJVjC7z";

    public function __construct()
    {
        //  Pass In An Empty String To Wayne's Service Class. We Do Not Need To
        //  Compare The Root Of The URL.
        
        parent::__construct("");
        echo "<p>PARENT CONSTRUCTED<p>";      
    }
    
    //  NOTES: Parameters Should, At Present, Be Sent To The Web Service In The 
    //  Form:
    //  
    //  www.commerce3.derby.ac.uk/~partypool?action=value&subaction=value
    //  
    //  Where ? Signifies The Start Of The GET Parameters, And & Is Used To
    //  Chain Multiple Parameters Together.
    //
    //  Wayne's Code Has Been Written In Such A Way, That The htaccess File Will
    //  Assign All Values After ? To A Value Called 'q'. This Can Then Be
    //  Accessed Via GET['q']. If The URL Is Stored In The Format:
    //  
    //  https://computing.derby.ac.uk/~partypool/x/y/z
    //  
    //  Then Q Stores "x/y/z" Then Explodes The String Via "/" To Create An
    //  Array With Values x, y and z.
    //  
    //  However, We Currently Do Not Have Access To .htaccess Files On The
    //  Server Due To Security. As Such, Use The Standard "?" Method To
    //  Construct Requests For Now.
    
    public function performGet($url, $parameters, $requestBody, $accept)
    {
        if ($parameters["ACTION"] == "TESTQUERY")
        {
            echo "<p>DATABASE QUERY REQUEST RECIEVED</p>";

            if (isset($parameters["USER"]))
            {
                $userValue = $parameters["USER"];
                $userData = array();

                $conn = pg_connect($this->connString);

                //  Test For Connection Error.
                if ($conn)
                {
                    echo "<p>CONNECTION ESTABLISHED</p>";

                    //  Attempt To Retrieve Data From The Database
                    try
                    {
                        echo "<p>RUNNING QUERY</p>";

                        //  Simple Test Query To Be Run.
                        //  TODO: SANITISE INPUT!!!
                        $result = pg_query($conn,"SELECT * FROM users WHERE username = '".$userValue."'ORDER BY username");

                        if (!result)
                        {
                            echo"ERROR: QUERY HAS FAILED";
                            exit;
                        }

                        while ($row = pg_fetch_row($result))
                        {
                            echo "<p>USERNAME: $row[0]\t PASSWORD: $row[1]</p>";
                        }
                    }

                    catch(Exception $e)
                    {
                         echo "ERROR WITH QUERY: $e";
                    }

                    finally 
                    {
                        $conn->close();
                    }
                }

                else
                {
                    echo"<p>CONNECTION FAILED</p>";
                }
                
                echo "<p>RETRIEVING USER DATA FOR $userValue</p>";

            }

            else
            {
                echo "<p>NO USER SPECIFIED</p>";
            }
        }
        
        else
        {
            echo "FAILED";
        }
    }
}
?>