<?php
/**
 * Created by PhpStorm.
 * User: Jake Mander
 * Date: 03/03/2019
 * Time: 18:44
 */

class User
{
    private $username;
    private $password;

    public function __construct($usernameIn, $passwordIn)
    {
        $username = $usernameIn;
        $password = $passwordIn;
    }

    public function getUsername()
    {
        return $this->username;
    }

    public function getPassword()
    {
        return $this->password;
    }
}