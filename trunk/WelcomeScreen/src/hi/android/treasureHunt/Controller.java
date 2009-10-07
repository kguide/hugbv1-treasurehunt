package hi.android.treasureHunt;

/**
 * Handles every control aspect of the project. 
 */
public class Controller {

	public Player player;
	
	public Controller(){
		player = new Player();
	}
	
	/**
	 * This class handles everything a player in our game needs
	 * to do. 
	 */
	public class Player{
		
		DAL dal = new DAL();
		
		/**
		 * This method allows users to log in and become players.
		 * 
		 * @param username : String representing the users username
		 * @param password : String representing the users password
		 * @return : Boolean tells if the logIn was successful.
		 */
		public boolean logIn(String username, String password){
			return dal.verifyUser(username, password);
			}
	}
}
