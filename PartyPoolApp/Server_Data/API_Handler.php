<?php
/*
 *    All Requests For The Web Service Are Forwarded To The API_Handler. This Then
 *    Calls Wayne's Base Class Via The PartyPool API Constructor Which Seperates 
 *    All The Recieved Parameters And Handles Various Header Information Such As 
 *    The Request Type And Server URL.
*/

    require "PartyPool_API.php";

    $service = new PartyPool_API();
    $service -> handleRawRequest();
?>
