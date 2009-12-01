<?php 
class User_model extends Model{

var $username;
var $password;
var $lastLogin;
var $registerDate;

function __construct(){
	parent::Model();
	$this->load->helper('date');
	$this->load->database();
}


/**
* createUser(): Takes username & password as http post input
* @after: User has been created in the user database table and has been initialized in the userScore table
**/
function createUser(){
	$this->username = $this->input->post('username');
	$this->password = md5($this->input->post('password'));
	$this->lastLogin = mdate("%Y-%m-%d");	
	$this->registerDate = $this->lastLogin;

	// start database transaction
	$this->db->trans_start();
	$this->db->insert('users',$this);
	// Get the userId provided for the new user, result will always be one record due to constraints
	$query = $this->db->select('userId')->where('username',$this->username)->where('password',$this->password)->get('users');
	$userId = $query->row(0)->userId;
	
	// Now insert the user into the score table
	$this->db->insert('userScores',Array('userId' => $userId));
	$this->db->trans_complete();

}

function loginSuccessfull(){
	$params['username'] = $this->input->post('username');
	$params['password'] = md5($this->input->post('password'));
	$this->db->select('userId')->from('users')->where('username',$params['username'])->where('password',$params['password']);
	$query = $this->db->get();
	if($query->num_rows() == 1) {	
		$row=$query->row_array();
		$params['userId']=$row['userId'];
		$this->session->set_userdata('username', $params['username']);
		$this->session->set_userdata('userId', $params['userId']);
		return true;		
		}
	else 
		return false;	
	}
	
function logOut(){
		$this->session->sess_destroy();
		redirect('/','refresh');
}

function loggedIn(){
	if($this->session->userdata('username') == '')
		return false;
	else
		return true;
}

function getLoggedInUserId(){
	if($this->loggedIn())
		return $this->session->userdata('userId');
	else
		return -1;
	}
}
?>