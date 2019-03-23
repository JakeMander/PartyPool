<?php
/**
 * Created by PhpStorm.
 * User: Jake Mander
 * Date: 03/03/2019
 * Time: 18:44
 */

class User
{
    //  Provide The Name Of The Table To Be Accessed
    private $tableName = "users";

    //  Various Details To Be Populated In The Database. These Will Be Used For Account Creation/Login
    //  As Well As General Administration.
    public $username;
    public $password;

    public function __construct($usernameIn, $passwordIn)
    {
        $this -> username = $usernameIn;
        $this -> password = $passwordIn;
    }

    //  Accessors To Retrieve Users Information, As Well As Access The Appropriate Table In The API.
    public function getTable()
    {
        return $this->tableName;
    }

    public function getUsername()
    {
        return $this->username;
    }

    public function getPassword()
    {
        return $this->password;
    }

    public function setUsername($usernameIn)
    {
        $this->username = $usernameIn;
    }

    public function setPassword($passwordIn)
    {
        $this->password = $passwordIn;
    }
}

?>