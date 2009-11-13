package hi.android.treasure.control;

import hi.android.treasure.data.PlayerDB;
import android.content.Context;
/**
 * This class handles everything a player in our game needs
 * to do. 
 */
public class Player {
	private int id;
	private String name;
	private String password;
	private int score;
	private int rank;
	
	public Player(){
		
	}
	
	public Player(int id, String name, int score, int rank){
		this.setId(id);
		this.setName(name);
		this.setScore(score);
		this.setRank(rank);
	}

	public void setId(int PlayerId) {
		this.id = PlayerId;
	}

	public int getId() {
		return id;
	}

	public void setName(String username) {
		this.name = username;
	}

	public String getName() {
		return name;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getRank() {
		return rank;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void save(Context context){
		PlayerDB playerDB = new PlayerDB(context);
		if(playerDB.exists(this.name)){
			playerDB.update(this);
		}
		else{
			playerDB.insert(this);
		}
	}

	public void delete(Context context){
		PlayerDB playerDB = new PlayerDB(context);
		playerDB.delete(this.id);
	}
}
