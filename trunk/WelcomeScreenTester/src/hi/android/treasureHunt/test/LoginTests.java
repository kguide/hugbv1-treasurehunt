package hi.android.treasureHunt.test;

import junit.framework.TestCase;
import hi.android.treasureHunt.Controller;

public class LoginTests extends TestCase {
	
	Controller controller;
	String username;
	String password;
	
	public void setUp(){
		controller = new Controller();
	}
	
	public void testUsernametest1PWhvalurShouldTrue(){
		username="test1";
		password="hvalur";
		
		assertTrue(controller.player.logIn(username, password));
	}
	
	public void testUsernametest2PWfuglinnShouldTrue(){
		username="test2";
		password="fuglinn";
		
		assertTrue(controller.player.logIn(username, password));
	}

	public void testUsernamebullPWnopassShouldfalse(){
		username="bull";
		password="nopass";
		
		assertFalse(controller.player.logIn(username, password));
	}
	

}
