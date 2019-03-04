<?php
// Base class for a RESTful web service.  Inherit from this class and override the performXXX
// methods to perform the functionality you require.

// This class was originally based on source code taken from http://blog.garethj.com/2009/02/17/building-a-restful-web-application-with-php/.
// However, it has been substantially modified now to work with different rewrite rules and
// to handle parameters correctly.
//
// This class relies on URL rewrite rules in the appropriate config file (.htaccess for Apache, web.config for IIS).  These map a URL
// in the form http://server/x/y/x in to the form http://server/api.php?q=x/y/z.  
//
// To do this, the following rules need to be inserted in the appropriate files:
//
// .htaccess:
//
// RewriteEngine on
// RewriteCond %{REQUEST_FILENAME} !-f
// RewriteCond %{REQUEST_FILENAME} !-d
// RewriteCond %{REQUEST_URI} !=/favicon.ico
// RewriteRule ^(.*)$ api.php?q=$1 [L,QSA]
//
// web.config:
// <system.webServer>
//   <rewrite>
//     <rules>
//       <rule name="Imported Rule" stopProcessing="true">
//         <match url="^(.*)$" ignoreCase="false" />
//         <conditions>
//           <add input="{REQUEST_FILENAME}" matchType="IsFile" ignoreCase="false" negate="true" />
//           <add input="{REQUEST_FILENAME}" matchType="IsDirectory" ignoreCase="false" negate="true" />
//           <add input="{URL}" pattern="^/favicon.ico$" ignoreCase="false" negate="true" />
//         </conditions>
//         <action type="Rewrite" url="api.php?q={R:1}" appendQueryString="true" />
//       </rule>
//     </rules>
//   </rewrite>
// </system.webServer>
//
// Feel free to use this code as you wish. 
//
// Wayne Rippin. Created: 26/3/14. Last updated: 4/3/15.

class RestService 
{
  private $supportedMethods;
  private $apiStringToMatch;

  // The parameter to the constructor should be a string that matches
  // the first parameter of any request (i.e. the root of
  // the URLs used by the API). Any call to the service
  // will be compared against this and if it does not match,
  // the call will be rejected.
  //
  // If you don't want this to happen, pass in an empty string.

  public function __construct($apiStringToMatch) 
  {
    $this->supportedMethods = "GET, PUT, POST, DELETE";
    $this->apiStringToMatch = $apiStringToMatch;
  }

  public function handleRawRequest() 
  {
    // Get The URL Of The Current PHP File.
    $url = $this->getFullUrl();

    //  JAKE NOTE: Method Determines Which Method The HTTP Request Has Been Submitted As.
    $method = $_SERVER['REQUEST_METHOD'];

    // JAKE NOTE: Method Gets Body Of The HTTP Request.
    $requestBody = file_get_contents('php://input');

    // Look for any parameters appended to the URL in the form
    // "q=xyz".  These will be the parameters that determine which
    // functions are used for each HTTP method.  For example, a URL 
    // In the form q=books/x/y will see 'books', 'x' and 'y' placed 
    // in the first three elements of the array $parameters
    //
    if (isset($_GET['q']))
    {
        //  JAKE NOTE: Split The URL Into It's Individual Components.
        $parameters = explode("/", $_GET['q']);
        if (strlen($this->apiStringToMatch) > 0 && count($parameters) > 0)
        {
            // JAKE NOTE: This Statement Checks Length Of The API If A Parameter Has Been Provided To apiStringToMatch.
            if (strcmp($this->apiStringToMatch, $parameters[0]) != 0)
            {
                $this->notImplementedResponse();
                return;
            }
        }
    }

    //  JAKE NOTE:  We Do Not Provide A String To apiStringToMatch, So Just Take All The URI Parameters And Assign Them
    //  To An Array.
    else
    {
        $parameters = array();
    }

    //  JAKE NOTE: The $_SERVER['HTTP_ACCEPT'] Associative Array Handles The "Accept" Header Embedded In Each HTTP
    //  Request. This Determines Which MIME Types (I.e. Text, Font, Image, Video, Model) The Client Can Handle.

    if (isset($_SERVER['HTTP_ACCEPT']))
    {
      $accept = $_SERVER['HTTP_ACCEPT'];
    }
    else 
    {
      $accept = "";
    }
    $this->handleRequest($url, $method, $parameters, $requestBody, $accept);
  }

  protected function getFullUrl() 
  {
    $protocol = $_SERVER['HTTPS'] == 'on' ? 'https' : 'http';
    $location = $_SERVER['REQUEST_URI'];
    return $protocol.'://'.$_SERVER['HTTP_HOST'].$location;
  }

  public function handleRequest($url, $method, $parameters, $requestBody, $accept) 
  {
    switch($method) 
    {
      case 'GET':
        $this->performGet($url, $parameters, $requestBody, $accept);
        break;
      case 'POST':
        $this->performPost($url, $parameters, $requestBody, $accept);
        break;
      case 'PUT':
        $this->performPut($url, $parameters, $requestBody, $accept);
        break;
      case 'DELETE':
        $this->performDelete($url, $parameters, $requestBody, $accept);
        break;
      default:
        $this->notImplementedResponse();
    }
  }

  protected function notImplementedResponse() 
  {
    // 501 Not Implemented 
    header('Allow: ' . $this->supportedMethods, true, 501);
  }


  protected function methodNotAllowedResponse() 
  {
    // 405 (Method Not Allowed)
    header('Allow: ' . $this->supportedMethods, true, 405);
  }

  protected function notFoundResponse()
  {
    header("HTTP/1.1 404 Not Found");
  }

  protected function noContentResponse()
  {
    header("HTTP/1.1 204 No Content");
  }

  // Override the following methods to provide the appropriate functionality

  public function performGet($url, $parameters, $requestBody, $accept) 
  {
    $this->methodNotAllowedResponse();
  }

  public function performPost($url, $parameters, $requestBody, $accept) 
  {
    $this->methodNotAllowedResponse();
  }

  public function performPut($url, $parameters, $requestBody, $accept) 
  {
    $this->methodNotAllowedResponse();
  }

  public function performDelete($url, $parameters, $requestBody, $accept) 
  {
    $this->methodNotAllowedResponse();
  }

}
?>