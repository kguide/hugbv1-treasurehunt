package hi.android.treasureHunt;


public class Controller {

	public Player player;
	
	public Controller(){
		player = new Player();
	}
	
	public class Player{
		
		DAL dal = new DAL();
		
		public boolean logIn(String username, String password){
			return dal.verifyUser(username, password);
			}
	}
}
