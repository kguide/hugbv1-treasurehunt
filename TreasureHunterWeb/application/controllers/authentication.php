<?php

class Authentication extends Controller {

	function __construct() {
		parent::Controller();
		$this->load->library('form_validation');
	}

	function index(){
		$this->login();
	}

	function login(){
		$module['content']['login'] = 'login_view';
		$this->View_model->getDefaults($module);
		$this->load->view('index',$module);				
	}

	function register(){
		$module['content']['register'] = 'register_view';
		$this->View_model->getDefaults($module);
		$this->load->view('index',$module);		

	}

	function logout(){
		$this->session->sess_destroy();
		redirect('/','refresh');		
	}


	function validateLogin(){		
		$this->form_validation->set_rules('username','Username','required|valid_email');
		if ($this->form_validation->run() == TRUE && $this->User_model->loginSuccessfull()) {
				redirect('game/createGame/','');										
		}
		else {
				// Try to log in again
				$module['content']['login'] = 'login_view';	
				$module['login']['error'] = 'Wrong username/password combination, try again.';
				$this->View_model->getDefaults($module);
				$this->load->view('index',$module);	
		}					
	
	}

	function validateRegistration(){
		$this->form_validation->set_rules('username','Username','required|valid_email');
		$this->form_validation->set_rules('username2','Username repeated','required|matches[username]');
		$this->form_validation->set_rules('password2','Username repeated','required|matches[password]');
		
		if ($this->form_validation->run() == TRUE) {
			$this->User_model->createUser();
			$this->sendConfirmationMail();
			redirect('/','refresh');
			}
		else {
			redirect('login/register','');
			}						
	}	

	private function sendConfirmationMail(){
		$this->load->library('email');		
		$this->email->from('hgphoto@hgphoto.net', 'Treasure Hunter');
		$this->email->to('hordurg83@gmail.com');
		$this->email->subject('Email Test');
		$this->email->message('Welcome to Treasure Hunter, follow this link to activate your account\n ...');
		$this->email->send();
		//echo $this->email->print_debugger()
	}

}
?>