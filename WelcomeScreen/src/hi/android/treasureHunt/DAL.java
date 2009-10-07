package hi.android.treasureHunt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DAL {

	public boolean verifyUser(String username, String password){
		
		try {
			URL url = new URL("http://hgphoto.net/treasure/");
			URLConnection connection = url.openConnection();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			
			line = reader.readLine();
			
		} catch (MalformedURLException e) {
			System.out.print("MalformedURLException: " + e.getMessage());
			return false;
		} catch (IOException e) {
			System.out.print("IOException: " + e.getMessage());
			return false;
		}
		
		return true;
	}
}
