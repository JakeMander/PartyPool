<?php
/**
 * Created by PhpStorm.
 * User: jmman
 * Date: 22/03/2019
 * Time: 16:19
 *
 * Class To Manage Communication Of JSON Data With Front End.
 */

class JsonResponse
{
    public $jsonResponse = array();

    public function __construct ($statusIn, $messageIn, $dataIn)
    {
        $this -> jsonResponse[0] = $statusIn;
        $this -> jsonResponse[1] = $messageIn;
        $this -> jsonResponse[2] = $dataIn;
    }

    public function getStatus()
    {
        return $this -> jsonResponse[0];
    }

    public function getMessage()
    {
        return $this -> jsonResponse[1];
    }

    public function getData()
    {
        return $this -> jsonResponse[2];
    }

    public function setStatus($statusIn)
    {
        $this -> jsonResponse[0] = $statusIn;
    }

    public function setMessage($messageIn)
    {
        $this -> jsonResponse[1] = $messageIn;
    }

    public function setData($dataIn)
    {
        $this -> jsonResponse[2] = $dataIn;
    }
}