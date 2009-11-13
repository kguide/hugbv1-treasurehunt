package hi.android.treasure.data;

import hi.android.treasure.control.Game;
import hi.android.treasure.control.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * Handles all data access. 
 */

public class DataAccess {
	
	private static DBHelperPlayer playerDB;
	private static DBHelperGame gameDB;
	
	/**
	 * Verifies that the username and password match one of the players in the server database.
	 * 
	 * @param username : String representing the users username
	 * @param password : String representing the users password
	 * @return  : Boolean tells if the username and password match one of the players.
	 */
	public static boolean verifyUserOnline(String username, String password){
		return ServerConnection.verifyUserOnline(username, password);
	}
	
	/**
	 * Verifies that the username matches one of the players on the local Android database
	 * 
	 * @param username : The selected username to be verified.
	 * @return boolean : tells if a user with the selected username exists on the Android database
	 */
	public static boolean verifyUserExistsOnAndroid(String username, Context context){ 
		playerDB = new DBHelperPlayer(context);
		if(playerDB.exists(username)){
			return true;
		}
		return false;
	}
	
	/**
	 * Verifies that the username and password match one of the players on the local Android database
	 * 
	 * @param username : The selected username to be verified.
	 * @param password : The selected password to be verified.
	 * @param context  : A context passed from the view. Shouldn't be here but I don't know how to create a DBHelper without a context. 
	 * @return boolean : tells if the user with the selected username has the selected password.
	 */
	public static boolean verifyUserOnAndroid(String username, String password, Context context){
		playerDB = new DBHelperPlayer(context);
		Player player = playerDB.get(username);
		if(player.getName().compareTo(username) == 0 && player.getPassword().compareTo(password) == 0){
			return true;	
		}
			return false;
	}
	
	/**
	 * Creates a new user on the online server.
	 * 
	 * @param username : The new users username
	 * @param password : The new users password.
	 * @return : Boolean indicating whether the operation was successful or not.
	 */
	public static boolean createUserOnline(String username,String password){
		return ServerConnection.createUserOnline(username, password);
	}

	/**
	 * Returns a player object from the Android database corresponding to a player with the selected username and password
	 * 
	 * @param username : The selected players username
	 * @param password : The selected players password
	 * @param context  : A context passed from the view. Shouldn't be here but I don't know how to create a DBHelper without a context. 
	 * @return : The player with the selected username and password
	 */
	public static Player getPlayerOnAndroid(String username, Context context){
		Player player = null;
		if(!verifyUserExistsOnAndroid(username, context)){
			return player;
		}
		
		playerDB = new DBHelperPlayer(context);
		player = playerDB.get(username);
		return player;
	}
	
	/**
     * Returns a player object from the online server with the selected username and password
	 * 
	 * @param username : The selected players username
	 * @param password : The selected players password
	 * @return Player : A player object corresponding to the selected username and password.
	 */
	public static Player getPlayerOnServer(String username, String password){ 
			Player player = ServerConnection.getPlayerOnServer(username, password);
			return player;
	}

	public static Game getGame(int gameId, int playerId, Context context) {
		Game game = null;
		if(verifyGameExistsOnAndroid(gameId, context)){
			game = getGameOnAndroid(gameId,context);
		}
		else{
			game = ServerConnection.getGameOnServer(gameId);
			game.save(playerId, context);
		}
		return game;
	}

	/**
	 * Returns a game object from the Android database corresponding to the selected gameId.
	 * 
	 * @param gameId : The returned games gameId.
	 * @param context : The context from the GUI who called. This should not be necessary but we have been unable to work around it.
	 * @return : A game object corresponding to the selected gameId. Note that if a game was not found that corresponded to the 
	 * selected gameId then this returned object will be null.
	 */
	private static Game getGameOnAndroid(int gameId, Context context) {
		gameDB = new DBHelperGame(context);
		return gameDB.getGame(gameId);
	}

	/**
	 * Checks to see if a game with the selected gameId exists on the Android database.
	 * @param gameId : The games gameId that we are checking if exists.
	 * @param context : The context from the GUI who called. This should not be necessary but we have been unable to work around it.
	 * @return : Boolean indicating whether a game with the selected gameId exists on the Android database.
	 */
	private static boolean verifyGameExistsOnAndroid(int gameId, Context context) {
		gameDB = new DBHelperGame(context);
		return gameDB.exists(gameId);
	}
 
	/**
	 * Saves a game object on the Android database.
	 * @param game : The game to be saved.
	 * @param context : The context from the GUI who called. This should not be necessary but we have been unable to work around it.
	 */
	public static void saveGameOnAndroid(Game game, int playerId, Context context){
		gameDB = new DBHelperGame(context);
		if(verifyGameExistsOnAndroid(game.getGameId(), context)){
			gameDB.updateGame(game, playerId);
		}
		else{
			gameDB.insertGame(game, playerId);
		}
		
	}

	public static ArrayList<Game> getPlayersGamesOnAndroid(int playerId, Context context){
		 gameDB = new DBHelperGame(context);
		return gameDB.getUsersGames(playerId);
	}

	public static ArrayList<Game> getPlayersGamesOnServer(int playerId, Context contex){
		ArrayList<Game> arrayOfGames = ServerConnection.getPlayersGamesOnServer(playerId, contex);
		return arrayOfGames;
	}

	public static ArrayList<Game> getGamesOnServerByName(String name, Context context) {
		ArrayList<Game> resultingGamesInformation = ServerConnection.getGamesOnServerByName(name, context);
		return resultingGamesInformation;
	}

	public static void deleteGameFromAndroid(int gameId,Context context) {
		DBHelperGame gameDB = new DBHelperGame(context);
		gameDB.deleteGame(gameId);
	}

	public static void removeUserFromSelectedGameOnline(int gameId, int userId) {
		ServerConnection.removeUserFromSelectedGameOnline(gameId, userId);
	}

	public static void sendFinishedGamesOnline(int playerId, Context context) {
		gameDB = new DBHelperGame(context);
		ArrayList<Game> finishedGames = gameDB.getFinishedGames(playerId);
		
		for (Game game : finishedGames) {
			ServerConnection.sendFinishedGameToServer(game.getGameId(), playerId);
		}
	}

}

