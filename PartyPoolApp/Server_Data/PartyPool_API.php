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
    
    //  Login Details Required For Database Communication
    private $username = "pr_partypool";
    private $password = "kJVjC7z";
    private $host = "localhost";
    
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
    //  Waynes Code Has Been Written In Such A Way, That The htaccess File Will
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
        if (parameters["bob" == "1"])
        {
            echo "VALUE WORKS";
        }
        
        else
        {
            echo "FAILED";
        }
    }
}
?>