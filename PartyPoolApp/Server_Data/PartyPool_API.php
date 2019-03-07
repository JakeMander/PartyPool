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

require "RestServive.php";

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
        
        parent::construct("");
    }
}
?>