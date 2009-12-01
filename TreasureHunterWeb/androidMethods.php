<?php
class Controller {

	function __construct() {
		require('dbConnect.php');
	}

	
	/**
	* Index function checks which method is called for, fetches the parameters and executes the method asked for
	*
	*
	**/
	function index(){
		if(isset($_GET['method'])) {
			$method = $_GET['method'];
			$method = strtoupper($method);
		}		
		switch($method) { 
		
			case "LOGIN":		
				if(!(isset($_GET['username']) && isset($_GET['password']) && isset($_GET['request']) )) {
					echo json_encode(array("error"=>"parameters not set correctly"));
					return;
				}	
				$params['password'] = $_GET['password'];
				$params['username'] = $_GET['username'];
				$md5Hash = $_GET['request'];	
				echo $this->logIn($params,$md5Hash);
				break;		
				
			case "USERNAMEEXISTS":
				if(!(isset($_GET['username'])))
					echo json_encode(array("error"=>"parameters not set correctly"));
				else
					echo $this->usernameExists($_GET['username']);
				break;
				
			case "CREATEUSER":
				if(!(isset($_GET['password']) && isset($_GET['username'])) ) 
					echo json_encode(array("error"=>"parameters not set correctly"));
				else {					
					$params['username'] = $_GET['username'];
					$params['password'] = $_GET['password'];
					echo $this->createUser($params);
				}
				break;
				
			case "GETGAMEINFO":
				if(!(isset($_GET['gameId'])))
					echo json_encode(array("error"=>"parameters not set correctly"));			
				else 
					echo $this->getGameInfo($_GET['gameId']);		
				break;
				
			case "GETMYGAMES":
				if(!(isset($_GET['userId']))) 
					echo json_encode(array("error"=>"parameters not set correctly"));					
				else 
					echo $this->getMyGames($_GET['userId']);		
				break;
			case "SEARCHBYNAME":
				if(!(isset($_GET['search']))) 
					echo json_encode(array("error"=>"parameters not set correctly"));
				else
					echo $this->searchByName($_GET['search']);
				break;
			case "REMOVEUSERFROMGAME":
				if(!(isset($_GET['userId']))  && !(isset($_GET['gameId'])))
					echo json_encode(array("error"=>"parameters not set correctly"));
				else {
					$params['userId'] = $_GET['userId'];
					$params['gameId'] = $_GET['gameId'];
					echo $this->removeUserFromGame($params);					
				}
				break;
			case "FINISHEDGAME":
				if(!(isset($_GET['userId']))  && !(isset($_GET['gameId']))) {
					echo json_encode(array("error"=>"parameters not set correctly"));
				}
				else {					
					$userId = $_GET['userId'];
					$gameId = $_GET['gameId'];
					echo $this->finishedGame($userId,$gameId);					
				}
				break;
			case "ADDUSERTOGAME":
				if(!(isset($_GET['userId']))  && !(isset($_GET['gameId']))) {
					echo json_encode(array("error"=>"parameters not set correctly"));
				}
				else {					
					$userId = $_GET['userId'];
					$gameId = $_GET['gameId'];
					echo $this->addUserToGame($userId,$gameId);					
				}
				break;
			default:
				echo "Method not found";
		}
	}
	
	/*
	*	Used to logIn a user with a given username and password
	*
	* @param param['username'] : username to login
	* @param password : user password
	* @param salt: THE md5 hash function
	*
	* @returns JSON encoded string
	* if login successfull then return information about user, [ fields userId,username ]
	* else return JSON string with field the node error and a message
	*/
	function logIn($params,$md5Hash) {
		$SQLerror = "";
		$affectedRows = 0;

		// check if hash is correct
		if(!$this->androidHashCheck($params,$md5Hash)) {
			return json_encode(array("error"=>"hash failure"));
		}
		else {
			$params['password'] = md5($params['password']); // since DB doesn't store passwords un-encrypted
			$query = "SELECT * FROM users WHERE username = '" . $params['username'] . "' AND password = '" .$params['password']."'";
			$result = mysql_query($query) or $SQLerror = $query.mysql_error()." ".$query;
			$affectedRows = mysql_affected_rows();

			if($SQLerror != "")
				return json_encode(array("error"=>$SQLerror));
			elseif($affectedRows == 1) {
				$row = mysql_fetch_row($result);
				$userInfo = array("userId" => $row[0], "username" => $row[1]); // if login successfull return json string with user information
				return json_encode($userInfo);
				}
			else
				return json_encode(array("error"=>"Bad username password combination")); // 0 affected rows 
		}
	}

	/**
	* Checks if username exists in database, returns true if exists otherwise false
	* usage: usernameExists()
	* Required GET parameters: username, androidMD5 
	* username: desired username to check for
	* androidMD5: md5 hash from android
	**/
	function usernameExists($username) {		
		$query = "SELECT * FROM users WHERE username = '" . $username ."'";
			mysql_query($query) or $error = $query.mysql_error()." ".$query;
		$affectedRows = mysql_affected_rows();
		if($error != "")
			return json_encode(array("error"=>$SQLerror));
		elseif($affectedRows == 1)
			return "true";
		else
			return "false";
	}


	/**
	* usage: createUser()
	* Required GET parameters: username , password, androidMD5 
	* username: desired username
	* password: desired password
	* androidMD5: md5 hash from android
	**/
	function createUser($params){			
		// gather variables
		$currentDate = date("Y\-m\-d");
		$username = $params['username'];
		$password = md5($params['password']);
		
		//perform query
		$query = "INSERT INTO users (username,password,lastLogin,registerDate)".
						"VALUES ('".$username."','" .$password. "','" .$currentDate. "','" .$currentDate. "')";
		mysql_query($query) or $error = $query." ".mysql_error();
		//check results
		$affectedRows = mysql_affected_rows();
		
		// $query = "SELECT gameId FROM users WHERE username=".$username." AND password = ".$password;
		// $query = "INSERT INTO userScores (userId) VALUES (".$
		
		if(strlen($error) > strlen($query) && substr_compare($error,"Duplicate entry",strlen($query))) 
			return "false"; // false if username exists in DB
		elseif($error != "")
			return $error; // for debugging
		elseif($affectedRows == 1)
			return "true";
		else
			return "false";	
	}

	/**
	* androidHashCheck($parameters,$androidCrypt)
	* Checks if the android application is asking for permisson to run the code via dynamic hash function
	*
	* androidHashCheck Description.
	* For each string in $paramString array sum all odd indexed character ascii values, 
	* then multiply that sum by a 97 and adding character z to the end. Finally md5 encrypt by adding that 
	* sum to concatination of all strings in array
	*
	* $paramString - array of all parameters in alphabetic order
	* $androidCrypt - hash provided by android for comparison
	**/
	function androidHashCheck($paramString,$androidCrypt){
		$salt = 0;

		foreach($paramString as $string) {
			$encryptString .= $string; //concatenate all strings in array
			for($i=0;$i<strlen($string);$i++) {
				if($i%2==0)	// only find sum of every other character's ascii value
					$salt += ord($string[$i]);			
			}
		}
		$salt *= 97;
		$salt = strval($salt)."z";
		//echo $salt." ";
		$encryptString .= $salt;
		$encryptString = md5($encryptString);
		//echo $encryptString;
		if($encryptString == $androidCrypt) 
			return true;
		else
			return false;	
	}

	/**
	* 	Returns the name of a game with the selected id
	*
	*	@param id : The id to look up gamename for
	*	@return : The name of seleected game as  string
	*
	**/
	function getGameName($id) {
		$SQLerror = "";
		$query = "SELECT gameName FROM games WHERE gameId=$id";
		$result = mysql_query($query) or $SQLerror = $query.mysql_error()." ".$query;;
		$row = mysql_fetch_row($result);	
		if(mysql_affected_rows() > 0)
			return $row[0];
		else
			return "";	
	}


	/**
	* @params $userId : The userId to look up game info for
	* @return :  JsonString  on the form
	*		{ "games" : [ {
	*			"gameId": "1",
	*			"gameName": "name"
	*		} ] }
	**/
	function getMyGames($userId){
		$SQLerror = "";
		$jsonString = "";
		$query = "SELECT gameId FROM gameMembers where userId =".$userId." AND finished=0";
		$result = mysql_query($query) or $SQLerror = $query.mysql_error()." ".$query;
		if($SQLerror != "") {
			return json_encode(array("error"=>$SQLerror));
		}
		
		$affectedRows = mysql_affected_rows();	
		// Start of json string	
		$jsonString = "{ \"games\" : [";
		if($affectedRows > 0) {
			for($i=0;$i<$affectedRows;$i++){
				$row = mysql_fetch_row($result);
				$myGameInfo['gameId']=$row[0];
				$myGameInfo['gameName']=$this->getGameName($row[0]);		
				$jsonString .= json_encode($myGameInfo);
				if($i != $affectedRows-1)
					$jsonString .= ",";
				}
		}
		$jsonString .= "]}";
		return $jsonString;
	}

	function searchByName($search) {
		$SQLerror = "";
		$jsonString = "";
		$query = "SELECT gameId,gameName FROM games WHERE gameName like '%".$search."%'";
		$result = mysql_query($query) or $SQLerror = $query.mysql_error()." ".$query;
		$affectedRows = mysql_affected_rows();

		// Start of json string
		$jsonString = "{ \"games\" : [";
		for($i=0;$i<$affectedRows;$i++){
			$row = mysql_fetch_row($result);
			$gameFound['gameId']=$row[0];
			$gameFound['gameName']=$row[1];		
			$jsonString .= json_encode($gameFound);
			if($i != $affectedRows-1)
				$jsonString .= ",";
			}
		$jsonString .= "]}";
		return $jsonString;
	}


	/**
	* Returns 
	* "gameId":"id",
	* "gameName": "name"
	* to be used with json string
	**/
	function gameToJson($gameId) {

	$queryGame = "SELECT gameId,gameName FROM games WHERE gameId = $gameId";

	}


	function getGameInfo($gameId){
		$queryGame = "SELECT gameId,gameName FROM games WHERE gameId = $gameId";
		$queryCoordinates = "SELECT gameId,coordinateId,latitude,longitude FROM gameCoordinates where gameId=$gameId ORDER BY coordinateId ASC";
		$queryGameScore = "SELECT score FROM gameScores where gameId=$gameId;";
		$queryHints = "SELECT hintId,coordinateId,hintText FROM gameHints where gameId=$gameId ORDER BY coordinateId ASC";

		// Start of Json string 	
		
		// ### Get game id, name and score then output into json string
		$result = mysql_query($queryGame) or $SQLerror = $query.mysql_error()." ".$query;
		$row = mysql_fetch_row($result);

		echo "{";  
		$gameInfo['gameId'] = $row[0];
		$gameInfo['gameName'] = $row[1];	
			
		// get the gameScore
		$result = mysql_query($queryGameScore) or $SQLerror = $query.mysql_error()." ".$query;
		$row = mysql_fetch_row($result);	
		$gameInfo['gameScore'] = $row[0];
		$jsonString = json_encode($gameInfo);  // Need to cut { and } from the string to use it in a larger string
		echo substr($jsonString,1,strlen($jsonString)-2).",";
		
		// ### Get game coordinates and output into the json string
		$result = mysql_query($queryCoordinates) or $SQLerror = $query.mysql_error()." ".$query;
		$affectedRows = mysql_affected_rows();
		
		echo "\"coordinate\": [";
		for($i = 0;$i<$affectedRows;$i++) {
			$row = mysql_fetch_row($result);
				
			$coordinateInfo['coordinateId'] = $row[1];
			$coordinateInfo['latitude'] = $row[2];
			$coordinateInfo['longitude'] = $row[3];
			echo json_encode($coordinateInfo);
			if($i != $affectedRows-1)
				echo ",";
		}
		echo "],";

		// ### Get game hints and output into the json string
		$result = mysql_query($queryHints) or $SQLerror = $query.mysql_error()." ".$query;
		$affectedRows = mysql_affected_rows();
		//$row = mysql_fetch_row($result);
		//echo '###################'.$row[2];
		//echo $queryHints."<br>";
		echo "\"hint\": [";		
		for($i = 0;$i<$affectedRows;$i++) {
			$row = mysql_fetch_row($result);
			$hintInfo['hintId'] = $row[0];
			$hintInfo['coordinateId'] = $row[1];
			$hintInfo['hintText'] = $row[2];
			//$hintInfo['hintText'] = htmlentities($row[2]);
			echo json_encode($hintInfo);
			if($i != $affectedRows-1)
				echo ",";	
				
		}
		echo "]";
		echo "}";
	}

	function removeUserFromGame($params){
		$SQLerror = "";
		$query = "DELETE FROM gameMembers WHERE userId = ".$params['userId']." AND gameId = ".$params['gameId'];
		$query = mysql_query($query) or $SQLerror = $query.mysql_error()." ".$query;
		if($SQLerror != "")
			return json_encode(array("error"=>$SQLerror));
		elseif(mysql_affected_rows() == 1)
			return "true";
		else 
			return "false";
	}
	
	private function gameIdExists($gameId){
		$query = "select * from games where gameId=".$gameId;
		mysql_query($query);
		//echo $query." ".mysql_affected_rows()."<br>";
		if(mysql_affected_rows() == 1)
			return true;
		else
			return false;
	}
	
	private function userIdExists($userId){
		$query = "select * from users where userId=".$userId;
		mysql_query($query);
		//echo $query." ".mysql_affected_rows()."<br>";
		if(mysql_affected_rows() == 1)
			return true;
		else
			return false;
	}
	
	private function isUserMemberOfGame($userId,$gameId){
		$query = "select * from gameMembers where userId=".$userId." and gameId=".$gameId;
		mysql_query($query);
		//echo $query." ".mysql_affected_rows()."<br>";
		if(mysql_affected_rows() == 1)
			return true;
		else
			return false;	
	}
	
	
	
	/**
	* Adds user to game
	* @param $userId : ID number of the user who will be added as a member of a game
	* @param $gameId : ID of a game which the user will be added to
	*
	* @return  
	*
	**/	
	function addUserToGame($userId,$gameId){
		$SQLerror = '';
		$currentDate = date("Y\-m\-d");
		$queryCheckIfExists = "SELECT * FROM gameMembers WHERE userId=".$userId." AND gameId=".$gameId;
		
		// return if user is already member of that game
		mysql_query($queryCheckIfExists) or $SQLerror = $query.mysql_error()." ".$query;
		if($SQLerror!='')
			return json_encode(array("error"=>$SQLerror));		

		if(mysql_affected_rows() > 0)
			return "true";
		else {
			// Continue if user is not a member of that game
			$query = "INSERT INTO gameMembers (gameId,userId,joined)".
						"VALUES ('".$gameId."','" .$userId. "','" .$currentDate."')";

			$result = mysql_query($query) or $SQLerror = $query.mysql_error()." ".$query;
				if($SQLerror!='')
					return json_encode(array("error"=>$SQLerror));		

			if(mysql_affected_rows() == 1)
				return "true";
			else
				return "false";
		}
	}
			
	
	
	
	/**
	* Signals that user has finished a game, userscore and number of locations 
	* is updated, finally game will be marked as finished in the gameMembers table
	**/
	function finishedGame($userId, $gameId){			
		$SQLerror = "";
		$addToHighScore = true;

		// Don't continue if either gameId or userId doesn't exist in db
		if( ! ($this->gameIdExists($gameId) && $this->userIdExists($userId)))
			return json_encode(array("error"=>"gameId or userId doesn't exist"));
		// Don't continue if user is not a member of that game
		if( ! ($this->isUserMemberOfGame($userId,$gameId)))
			return json_encode(array("error"=>"user is not member of that game"));
			
		// Selects exactly one row if game is already finished otherwise it selects nothing
		$query = "SELECT finished FROM gameMembers WHERE userId = ".$userId." and gameId=".$gameId." and finished=TRUE";		
		$query = mysql_query($query) or $SQLerror = $query.mysql_error()." ".$query;
		if($SQLerror != "")
			return json_encode(array("error"=>$SQLerror));
		elseif(mysql_affected_rows() > 0)
			return "true";  //don't add to user's highscore
		
		if($addToHighScore) { // add game score and locations to user profile and update game to be in finished state						
			// Set game to finished state
			$query = "UPDATE gameMembers "
				."SET finished=TRUE "
				."WHERE userId = ".$userId." and gameId=".$gameId;
			mysql_query($query) or $SQLerror = $query.mysql_error()." ".$query;
			//echo $query."<br>";
			if($SQLerror != "")
				return json_encode(array("error"=>$SQLerror));

			// Add score and number of finished locations to user
			$query = "
				update userScores 
				SET 
				score = (
					select score from (select * from userScores) as x where userId=".$userId.")+
					(SELECT score FROM gameScores WHERE gameId=".$gameId.")
				,
				locations = (
					select locations from (select * from userScores) as x where userId=".$userId.")+	
					(SELECT COUNT(*) FROM gameCoordinates WHERE gameId=".$gameId.")
				where userId=1;";
			//echo $query."<br>";	
			mysql_query($query) or $SQLerror = $query.mysql_error()." ".$query;					
			if($SQLerror != "")
				return json_encode(array("error"=>$SQLerror));
			echo json_encode(array("result"=>true));
		}				
		else // will never get to this state either outputs sql error or finishes correctly before
			echo json_encode(array("result"=>true));
	}	
}