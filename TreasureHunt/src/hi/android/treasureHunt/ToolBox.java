package hi.android.treasureHunt;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;

public class ToolBox {

	/**
	 * Calculates md5 hash from a String array, the returned md5 
	 * hash is used for getting/sending data from web server.
	 * 
	 * @param input : String array, representing parameters for connection with web
	 * @return md5 hash string
	 */
	public static String getWebHash(String input[]){
		int salt = 0;
		String encryptedString = "";
		for(String item : input){
			encryptedString +=item;
			for(int i=0; i<item.length();i++)
				if(i%2==0) 
					salt += (byte) item.charAt(i);
				
		}
		salt *= 97;
		Log.d("ToolBox", String.valueOf(salt)); 
		encryptedString += String.valueOf(salt)+"z";
		encryptedString = getMd5Hash(encryptedString); 
		Log.d("ToolBox",encryptedString);
		return encryptedString;
	}
					
	
	/**
	 * Calculates md5 hash for given input
	 * @param input : String to be converted into md5
	 * @return md5 hash of input (32 characters)
	 */
    private static String getMd5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1,messageDigest);
            String md5 = number.toString(16);
            while (md5.length() < 32)
                md5 = "0" + md5;
            return md5;
        } catch(NoSuchAlgorithmException e) {
            Log.e("MD5", e.getMessage());
            return null;
        }
    }
}
