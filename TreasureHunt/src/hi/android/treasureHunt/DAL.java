package hi.android.treasureHunt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

/**
 * Handles all data access for our project. 
 */

public class DAL {

	/**
	 * Verifies that the username and password match one of the players in the database.
	 * 
	 * @param username : String representing the users username
	 * @param password : String representing the users password
	 * @return  : Boolean tells if the username and password match one of the players.
	 */
	public static boolean verifyUser(String username, String password){
		try {
			String arr[] = new String[2]; // Construct array of parameters for the md5 hash method
			arr[0] = password;
			arr[1] = username;
			String md5Hash = ToolBox.getWebHash(arr);
			String connectionString = "http://hgphoto.net/treasure/controller.php?method=login&username=" + username + "&password=" + password + "&request=" + md5Hash; 
			Log.d("DAL",connectionString);
			URL url = new URL(connectionString);		//Location of server-script.
			URLConnection connection = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String replyString = reader.readLine();
			
			return replyString.equals("true") ? true : false;  		//Java ternary operator (condition) ? 'return if true' : 'return if false' 
			
		} catch (MalformedURLException e) {
			Log.e("User Verification", e.getMessage());
			return false;
		} catch (IOException e) {
			Log.e("User Verification", e.getMessage());
			return false;
		}
	}
}
