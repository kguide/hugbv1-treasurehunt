package hi.android.treasureHunt.Data;

import hi.android.treasureHunt.Control.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * Handles all data access. 
 */

public class DAL {
	
	private static DBHelperPlayer playerDB;
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
			Log.d("DAL",connectionString);
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
		// TODO Add md5Hash to the connection string once server side can accept it
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
	
	
	


}

