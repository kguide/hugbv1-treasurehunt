<?php
class Game_model extends model {

var $gameName;
var $created;
var $ownerId;

	function __construct(){
		parent::Model();
		$this->load->helper('date');
		$this->load->database();
	}

	function addGame(){
			$this->ownerId = $this->User_model->getLoggedInUserId();
			$jd = $this->input->post('jsonData');	
			$jd= json_decode($jd);	
			$gameName = $jd->{'gamename'};
			$hints = $jd->{'hints'};
			$lat = $jd->{'lat'};
			$lng = $jd->{'lng'};
			
			$this->gameName = $gameName;
			$this->created = mdate("%Y-%m-%d");
			$this->db->insert('games',$this);	
			
			
			$query = $this->db->get_where('games',array('gameName' => $gameName));
			foreach($query->result() as $row)
				$gameId = $row->gameId;
				
			for($i = 0 ; $i < count($hints); $i++) {
				$hintArr['hintId'] = 1;
				$hintArr['gameId'] = $gameId;
				$hintArr['coordinateId'] = $i+1;		
				$hintArr['hintText'] = $hints[$i];
				$this->db->insert('gameHints',$hintArr);	
			}
				
			for($i = 0 ; $i < count($lat); $i++) {
				$coordinate['coordinateId'] = $i+1;
				$coordinate['gameId'] = $gameId;
				$coordinate['latitude'] = round($lat[$i], 6);
				$coordinate['longitude'] = round($lng[$i], 6);		
				$this->db->insert('gameCoordinates',$coordinate);	
			}
					
			// insert gamescore currently only a fixed number 2000
			$gameScore['gameId'] = $gameId;
			$gameScore['score'] = 2000;
			$this->db->insert('gameScores',$gameScore);		
			
			return true;
	}

	/**
	* Used to get info on all games created by userId.
	* Returns Active Record result set of all games user has created.
	* 
	* @param:
	*	$userId : Userid to look up games for
	*
	* @return: 
	*	$array :  Active Record result set with gamenames ordered by gameId descending
	*
	**/
	function getListOfGames($userId){
		$this->db->select('gameId, gameName,active')->from('games')->where('ownerId',$userId)->order_by('gameId', "desc"); ;
		$query = $this->db->get();
		if($query->num_rows() > 0) {
			$i=0;
			foreach($query->result() as $row) {
				$result[$i++] = $row;
			}		
			return $result;
		}
		return -1;
	}

	/**
	* Used to get info about a given games user has been member of.
	* Returns names of all games user has created as a SQL result
	* 
	* @param:
	*	$userId : Userid to look up games for
	*
	* @return: 
	*	$array :  SQL result object with gamens ordered by date joined
	**/
	function getPlayedGamesResult($userId){
	//	$this->db->select('gameId, gameName,active')->from('games')->where('ownerId',$userId);
		$query = $this->db->query("SELECT A.gameId,B.gameName, B.active, A.finished FROM gameMembers A, games B WHERE A.userId =".$userId." AND A.gameId = B.gameId");
		if($query->num_rows() > 0) {
			$i=0;
			foreach($query->result_array() as $row) {
				$result[$i++] = $row;
			}		
			return $result;
		}
		else
			return -1;
	}
	

	/**
	* Used to get info on highscores for given number of users.
	* 
	* @param:
	*	$userId : Userid to look up games for
	*
	* @return: 
	*	$array :  SQL result set with gamenames ordered by gameId descending
	**/
	function getHighscoresResult($n){
		$query = $this->db->query("SELECT A.username,B.score ".
							"FROM users A, userScores B ".
							"WHERE B.userId = A.userId ".
							"ORDER BY score DESC LIMIT 0,".$n);

		if($query->num_rows() > 0) {
			$i=0;
			foreach($query->result_array() as $row) {
				$result[$i++] = $row;
			}		
			return $result;
		}
		else
			return -1;
	}
}
?>