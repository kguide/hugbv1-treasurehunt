<?php 
class Game extends Controller {

	function __construct(){
		parent::Controller();
		// All functions in this class require authentication
		if(!$this->User_model->loggedIn())
			redirect('/','refresh');		
		$this->load->model('Game_model');	
	}	
		
	function index(){
		redirect('/','refresh');
	}
		
	/**
	* Calls game model and asks it to deactivate a game with the given gameId
	*/
	function deactivate($gameId){
		$userId = $this->User_model->getLoggedInUserId();
		$this->db->where('gameId',$gameId)->where('ownerId',$userId)->update('games',array('active'=>0));					
		redirect('myGames/','');
	}
	
	/**
	* Calls game model and asks it to activate a game with the given gameId
	*/
	function activate($gameId){
		$userId = $this->User_model->getLoggedInUserId();
		$this->db->where('gameId',$gameId)->where('ownerId',$userId)->update('games',array('active'=>1));					
		redirect('myGames/','');
	}
	
	/**
	* Calls the create game view
	*/	
	function createGame(){
		$module['content']['view'] = 'createGame_view';
		$this->View_model->getDefaults($module);
		$this->load->view('index',$module);	
	}
		
	/**
	* Calls the Game model and asks for a array of all games which user has played on his mobile
	*/
	function playedGames(){
		$module['content']['playedGames'] = 'playedGames_view';
		$result = $this->Game_model->getPlayedGamesResult($this->User_model->getLoggedInUserId());
		$module['playedGames']['queryResult'] = $result;
		$this->View_model->getDefaults($module);
		$this->load->view('index',$module);			
	}
	
	/**
	* Calls the Game model and asks for a array of all games for the user which is loggedin 
	*/
	function myGames(){
		$module['content']['view'] = 'myGames_view';
		$module['myGames']['games'] = $this->Game_model->getListOfGames($this->User_model->getLoggedInUserId());
		$this->View_model->getDefaults($module);
		$this->load->view('index',$module);	
	
	}
	
	/**
	* Calls the Game model and asks it to put game into database 
	**/
	function addGame(){
		$this->load->Model('Game_model');
		if($this->Game_model->addGame()) {
			$module['content']['view'] = 'createGame_view';
			$this->View_model->getDefaults($module);
			$this->load->view('index',$module);	
			}
		else 
			redirect('/','refresh');
	}	
}
?>