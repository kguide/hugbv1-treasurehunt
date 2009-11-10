package hi.android.treasureHunt.Data;

import hi.android.treasureHunt.Control.Game;
import hi.android.treasureHunt.Control.Player;

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

public class DAL {
	
	private static DBHelperPlayer playerDB;
	private static DBHelperGame gameDB;
	private static String domainString = "http://hgphoto.net/treasure/";
	/**
	 * Handles calling the server given a connection string.
	 * 
	 * @param connectionString : The selected connection string.
	 * @return : String containing the servers reply.
	 */
	private static String serverReply(String connectionString){
		URL url;
		try {
			url = new URL(connectionString);
			URLConnection connection = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String replyString = reader.readLine();
			return replyString;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "";
		}		
		catch (IOException e) {
			e.printStackTrace();
			return "";
		}


	}
	
	/**
	 * Creates a md5 hash string that the server will recognize and will allow the server to verify if the request comes 
	 * from a known source. The two parameters are used to create the md5 hash string.
	 * 
	 * @param username : Used to create the md5 hash string.
	 * @param password : Used to create the md5 hash string.
	 * @return : The md5 hash string that gets create from the two parameters.
	 */
	private static String md5HashFunction(String username, String password){
		String arr[] = new String[2]; // Construct array of parameters for the md5 hash method
		arr[0] = password;
		arr[1] = username;
		
		// Creates an encryption string so that the server can verify that the request in authentic.
		String md5Hash = ToolBox.getWebHash(arr);
		return md5Hash;
	}
	
	/**
	 * Verifies that the username and password match one of the players in the server database.
	 * 
	 * @param username : String representing the users username
	 * @param password : String representing the users password
	 * @return  : Boolean tells if the username and password match one of the players.
	 */
	public static boolean verifyUserOnline(String username, String password){
		try {
			String md5Hash = md5HashFunction(username, password);
			String connectionString = domainString + "controller.php?method=login&username=" + username + "&password=" + password + "&request=" + md5Hash; 
			String JSONReplyString = serverReply(connectionString);
			
			JSONObject jsonObject = new JSONObject(JSONReplyString);
			return jsonObject.has("error") ? false : true;  		//Java ternary operator (condition) ? 'return if true' : 'return if false' 
			
		} catch (JSONException e) {
			Log.e("JSONException", e.getMessage());
			return false;
		}
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
		//String md5Hash = md5HashFunction(username, password);
		String connectionString = domainString +  "controller.php?method=createuser&request=eitthvad&username="+username+"&password="+password; 
		String replyString = serverReply(connectionString);		
		return replyString.equals("true") ? true : false;  		//Java ternary operator (condition) ? 'return if true' : 'return if false' 

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
		try {
			String md5Hash = md5HashFunction(username, password);
			String connectionString = domainString + "controller.php?method=login&username=" + username + "&password=" + password + "&request=" + md5Hash; 
			String JSONReplyString = serverReply(connectionString);		
			
			JSONObject jsonObject = new JSONObject(JSONReplyString);
			
			Player player = new Player();
			player.setName(jsonObject.getString("username"));
			player.setId(Integer.parseInt(jsonObject.getString("userId")));
//			player.setScore(jsonObject.getInt("score"));
//			player.setRanking(jsonObject.getInt("rank"));
			return player;

		} catch (JSONException e) {
			Log.e("JSONException", e.getMessage());
			return null;
		}
	}

	public static Game getGame(int gameId, int playerId, Context context) {
		Game game = null;
		if(verifyGameExistsOnAndroid(gameId, context)){
			game = getGameOnAndroid(gameId,context);
		}
		else{
			game = getGameOnServer(gameId);
			game.save(playerId, context);
		}
		return game;
	}

	/**
	 * Returns a game object from the online server corresponding to the selected gameId.
	 * 
	 * @param gameId : The returned games gameId.
	 * @param context : The context from the GUI who called. This should not be necessary but we have been unable to work around it.
	 * @return : A game object corresponding to the selected gameId. Note that if a game was not found that corresponded to the 
	 * selected gameId then this returned object will be null.
	 */
	private static Game getGameOnServer(int gameId) {

		String connectionString = domainString + "controller.php?method=getGameInfo&gameId=" + gameId;
		String JSONReplyString = serverReply(connectionString);		
		
		try {
			Game game = new Game();
			
			JSONObject jsonObject = new JSONObject(JSONReplyString);
			
			game.setGameName(jsonObject.getString("gameName"));
			game.setGameId(gameId);
			
			JSONArray coordinatesArray = jsonObject.getJSONArray("coordinate");
			
			for (int i = 0; i < coordinatesArray.length(); i++) {
				JSONObject currentCoordinate = coordinatesArray.getJSONObject(i);
				int currentCoordinateId = Integer.parseInt(currentCoordinate.getString("coordinateId"));
				float currentLatitude = Float.valueOf(currentCoordinate.getString("latitude")).floatValue();
				float currentLongitude = Float.valueOf(currentCoordinate.getString("longitude")).floatValue();
				game.addCoordinate(gameId,currentCoordinateId,currentLatitude,currentLongitude);
			}
			
			JSONArray hintsArray = jsonObject.getJSONArray("hint");
			
			for (int i = 0; i < hintsArray.length(); i++) {
				JSONObject currentHint = hintsArray.getJSONObject(i);
				int currentCoordinateId = Integer.parseInt(currentHint.getString("coordinateId"));
				int currentHintId = Integer.parseInt(currentHint.getString("hintId"));
				String currentHintText = currentHint.getString("hintText");
				game.addHint(gameId, currentCoordinateId, currentHintId, currentHintText);
			}
			return game;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}	
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
	public static void saveGame(Game game, int playerId, Context context){
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
		
		//This connection string outputs gameId and gameName for each game that the player with the selected playerId is signed in.
		String connectionString = domainString + "controller.php?method=getMyGames&userId=" + playerId;
		String JSONReplyString = serverReply(connectionString);	
		
		ArrayList<Game> arrayOfGames = new ArrayList<Game>();
		Game game;
		
		try {
			JSONObject jsonObject = new JSONObject(JSONReplyString);
			JSONArray myGamesInfo = jsonObject.getJSONArray("games");
			
			for (int i = 0; i < myGamesInfo.length(); i++) {
				JSONObject currentGame = myGamesInfo.getJSONObject(i);
				int currentGameId = Integer.parseInt(currentGame.getString("gameId"));
				
				game = getGameOnServer(currentGameId);
				game.save(playerId, contex);
				arrayOfGames.add(game);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return arrayOfGames;
	}

	public static ArrayList<Game> getGamesOnServerByName(String name, Context context) {
		String connectionString = domainString + "controller.php?method=searchByName&search=" + name;
		String JSONReplyString = serverReply(connectionString);
		
		ArrayList<Game> resultingGamesInformation = new ArrayList<Game>();
		Game game;
		
		try {
			JSONObject jsonObject = new JSONObject(JSONReplyString);
			JSONArray jsonArrayOfGames = jsonObject.getJSONArray("games");
			
			for (int i = 0; i < jsonArrayOfGames.length(); i++) {
				game = new Game();
				JSONObject currentGame = jsonArrayOfGames.getJSONObject(i);
				int currentGameId = Integer.parseInt(currentGame.getString("gameId"));
				String currentGameName = currentGame.getString("gameName");
				
				game.setGameId(currentGameId);
				game.setGameName(currentGameName);
				
				resultingGamesInformation.add(game);
		}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return resultingGamesInformation;
		
	}

	public static void deleteGameFromAndroid(int gameId,Context context) {
		DBHelperGame gameDB = new DBHelperGame(context);
		gameDB.deleteGame(gameId);
	}

	public static void removeUserFromSelectedGameOnline(int gameId, int userId) {
		String connectionString = domainString + "controller.php?method=removeUserFromGame&gameId=" + gameId+"&userId="+userId;
		@SuppressWarnings("unused")
		String replyString = serverReply(connectionString);	
		//replystring is not being used.
	}

	public static void sendFinishedGamesOnline(int playerId, Context context) {
		gameDB = new DBHelperGame(context);
		ArrayList<Game> finishedGames = gameDB.getFinishedGames(playerId);
		
		for (Game game : finishedGames) {
			sendFinishedGameToServer(game.getGameId(), playerId);
		}
	}

	private static void sendFinishedGameToServer(int gameId, int playerId) {
		
		String connectionString = domainString + "controller.php?method=finishedGame&userId=" + playerId + "&gameId=" + gameId;
		@SuppressWarnings("unused")
		String JSONReplyString = serverReply(connectionString);	
		// Server reply is not used right now.
	}
	
	
}

