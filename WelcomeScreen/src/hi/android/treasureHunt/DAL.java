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
	public boolean verifyUser(String username, String password){
		try {
			
			URL url = new URL("http://hgphoto.net/treasure/index2.php?username=" + username + "&password=" + password);		//Location of server-script.
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
