package hi.android.treasureHunt.Control;

import hi.android.treasureHunt.Data.DAL;
import android.content.Context;


/**
 * Handles every control aspect of the project. 
 */
public class Controller {

	
	public Player player;
	public Game game;
	
	//Singleton creation of control
	protected Controller() {}  // private so other classes cant instatiate this singleton.
    static private Controller INSTANCE = null;
	   
	   /**
	    * @return The unique instance of this class.
	    */
	 static public Controller getInstance() {  
		if(null == INSTANCE) {
			INSTANCE = new Controller();
		}
	    return INSTANCE;
	}

	//Singleton part ends
	  
	/**
	 * This method allows users to log in to the Android game. 
	 * 
	 * @param username : String representing the users username
	 * @param password : String representing the users password
	 * @return : Boolean. Indicates whether the username and password matched a user.
	 */
	public boolean logIn(String username, String password, Context context){

		boolean playerExistsLocal = DAL.verifyUserExistsOnAndroid(username, context);
		if(playerExistsLocal){
			if(DAL.verifyUserOnAndroid(username, password, context)){
				player = DAL.getPlayerOnAndroid(username, context);
				return true;
			}
			else{
				return false;
			}
		}
		else{
			if(DAL.verifyUserOnline(username, password)){
				player = DAL.getPlayerOnServer(username, password);
				player.setPassword(password);
				player.save(context);
				return true;
			}
			else{
				return false;
			}
		}
		}
	
	/**
	 * Creates a new user on the online server.
	 * 
	 * @param username : The new users username.
	 * @param password : The new users password.
	 * @return
	 */
	public boolean createUserOnline(String username, String password){
		return DAL.createUserOnline(username, password);
		}
	
	/**
	 * Gets current hint for active game.
	 * 
	 * @return : string that contains the hint
	 */
	public String getCurrentHint(){
		return "!";
		}
	
	
	
}

