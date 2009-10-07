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
			
			System.out.print(line);
			
		} catch (MalformedURLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}
}
