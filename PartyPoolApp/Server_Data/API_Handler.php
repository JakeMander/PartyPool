<?php
/*
 *    All Requests For The Web Service Are Forwarded To The API_Handler. This Then
 *    Calls Wayne's Base Class Via The PartyPool API Constructor Which Seperates 
 *    All The Recieved Parameters And Handles Various Header Information Such As 
 *    The Request Type And Server URL.
 *
 *    The htacess File Is Currently Non-Functional At This Time Due To Security.
 *    Will Try And Resolve With Dave.
*/

    require "PartyPool_API.php";
   
    echo"<p>PartyPool_API Instantiated</p>";
    $service = new PartyPool_API();
    echo"<p>handleRawRequest Called</p>";
    $service -> handleRawRequest();
?>
