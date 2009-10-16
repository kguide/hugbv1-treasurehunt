package hi.android.treasureHunt;

/**
 * Handles every control aspect of the project. 
 */
public class Controller {

	public Player player;
	
	/**
	 * This class handles everything a player in our game needs
	 * to do. 
	 */
	public class Player{
				
		public int userID;
		public String username;
		public int score;
		public int ranking;
		
		public Player(int userID, String username, int score, int ranking){
			this.userID = userID;
			this.username = username;
			this.score = score;
			this.ranking = ranking;
		}
	}
	
	/**
	 * This method allows users to log in and become players.
	 * 
	 * @param username : String representing the users username
	 * @param password : String representing the users password
	 * @return : Boolean tells if the logIn was successful.
	 */
	public boolean logIn(String username, String password){
		return DAL.verifyUser(username, password);
		}
	public boolean createNewUser(String username, String password){
		return DAL.createNewUser(username,password);
		}
}
