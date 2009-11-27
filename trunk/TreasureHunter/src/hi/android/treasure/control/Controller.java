package hi.android.treasure.control;

import hi.android.treasure.data.DataAccess;
import hi.android.treasure.data.GpsTool;

import java.util.ArrayList;

import android.content.Context;


/**
 * Handles every control aspect of the project. 
 */
public class Controller {

	
	public Player player;
	public Game game;
	public String gameNameToSearchFor;
	
	//Singleton creation of control
	protected Controller() {}  // private so other classes can't instantiate 
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

		boolean playerExistsLocal = DataAccess.verifyUserExistsOnAndroid(username, context);
		if(playerExistsLocal){
			if(DataAccess.verifyUserOnAndroid(username, password, context)){
				player = DataAccess.getPlayerOnAndroid(username, context);
				return true;
			}
			else{
				return false;
			}
		}
		else{
			if(DataAccess.verifyUserOnline(username, password)){
				player = DataAccess.getPlayerOnServer(username, password);
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
		return DataAccess.createUserOnline(username, password);
		}
	
	/**
	 * Gets current hint for active game.
	 * 
	 * @return : string that contains the hint
	 */
	public String getCurrentHintText(){
		String currentHintText = this.game.getCurrentHintText();
		return currentHintText;
	}
	
	/**
	 * Check if gps coordinates are within certain radius of another gps coords.
	 * 
	 * @param la1 : latitude point 1
	 * @param lo1 : longitude point 1
	 * @param la2 : latitude point 2	 
	 * @param lo2 : longitude point 2
	 * @param radius : radius in meters
	 * @return : true if point 2 is within radius of point 1 else false
	 */
	public boolean checkGPSRadius(double la1, double lo1, double la2, double lo2, double radius) {
		return (radius>GpsTool.getDistance(la1, lo1, la2, lo2));
	}
	
	/**
	 * Loads a game with the selected gameId to the local variable game. 
	 * @param gameId : The gameId of the game we are getting.
	 * @param context : The context from the GUI who called. This should not be necessary but we have been unable to work around it.
	 * @return : Boolean indicating whether a game with the selected gameId was found.
	 */
	public boolean getGame(int gameId, Context context){
		this.game = DataAccess.getGame(gameId, this.player.getId(),context);
		return game != null ? true:false;	//The java ternary operator. (condition) ? returnValueIfTrue : returnValueIfFalse; 
	}
		
	public ArrayList<Game> getUsersGamesOnAndroid(Context context){
		return DataAccess.getPlayersGamesOnAndroid(this.player.getId(),context);
	}

	public ArrayList<Game> getUsersGamesOnServer(Context context) {
		ArrayList<Game> arrayOfGames = DataAccess.getPlayersGamesOnServer(this.player.getId(), context);
		return arrayOfGames;
	}

	public ArrayList<Game> getGamesOnServerByName(String name,Context context) {
		ArrayList<Game> listOfGames = DataAccess.getGamesOnServerByName(name,context);
		return listOfGames;
	}
	public void removeUserFromSelectedGame(int gameId, Context context){
		DataAccess.removeUserFromSelectedGameOnline(gameId,player.getId());
}
	public void deleteGameFromAndroid(int gameId,Context context) {
		
		DataAccess.deleteGameFromAndroid( gameId,context);
	}

	public void sendFinishedGamesOnline(Context context) {
		DataAccess.sendFinishedGamesOnline(player.getId(),context);
	}

	public void signPlayerInGameOnline() {
		DataAccess.signPlayerInGameOnline(this.game.getGameId(), this.player.getId());
	}
}


