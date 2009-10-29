package hi.android.treasureHunt.Control;

import hi.android.treasureHunt.Data.DAL;

import java.util.ArrayList;

import android.content.Context;


public class Game {
	private int gameId;
	private String gameName;
//	private Player gameCreator;
//	public ArrayList<Player> players;
	public ArrayList<Hint> hints = new ArrayList<Hint>();
	public ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
	private int currentCoordinateId;
	
	/**
	 * Represents a hint in the Treasure Hunt game.
	 */
	public class Hint {
		private int gameId;
		private int coordinateId;
		private int hintId; 		
//		private int subClueId;		
		private String hintText;
		
		public Hint(int gameId, int coordinateId, int hintId, String hintText){
			this.gameId = gameId;
			this.coordinateId = coordinateId;
			this.hintId = hintId;
			this.hintText = hintText;
		}
		
		public void setGameId(int gameId) {
			this.gameId = gameId;
		}
		public int getGameId() {
			return gameId;
		}
		public void setHintId(int hintId) {
			this.hintId = hintId;
		}
		public int getHintId() {
			return hintId;
		}
		public void setHintText(String hintText) {
			this.hintText = hintText;
		}
		public String getHintText() {
			return hintText;
		}
		public void setCoordinateId(int coordinateId) {
			this.coordinateId = coordinateId;
		}
		public int getCoordinateId() {
			return coordinateId;
		}
	}
	
	/**
	 * Represents a GPS coordinate
	 */
	public class Coordinate {
		private int gameId;
		private int coordinateId;		//Relative to a game
		private float latitude;
		private float longitude ;
		
		public Coordinate(int gameId, int coordinate, float latitude, float longitude){
			this.gameId = gameId;
			this.coordinateId = coordinate;
			this.latitude = latitude;
			this.longitude = longitude;
		}
		
		public void setGameId(int gameId) {
			this.gameId = gameId;
		}
		public int getGameId() {
			return gameId;
		}
		public void setCoordinateId(int coordinateId) {
			this.coordinateId = coordinateId;
		}
		public int getCoordinateId() {
			return coordinateId;
		}
		public void setLatitude(float latitude) {
			this.latitude = latitude;
		}
		public float getLatitude() {
			return latitude;
		}
		public void setLongitude(float longitude) {
			this.longitude = longitude;
		}
		public float getLongitude() {
			return longitude;
		}
	}

	public void save(Context context) {
		DAL.saveGame(this, context);
	}

	public void addCoordinate(int gameId, int coordinateId, float latitude, float longitude){
		Coordinate coordinate = new Coordinate(gameId, coordinateId, latitude, longitude);
		coordinates.add(coordinate);
	}
	
	public void addHint(int gameId, int coordinateId, int hintId, String hintText){
		Hint hint = new Hint(gameId, coordinateId, hintId, hintText);
		hints.add(hint);
	}
	
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameName() {
		return gameName;
	}

	public void setCurrentCoordinateId(int currentCoordinate) {
		this.currentCoordinateId = currentCoordinate;
	}

	public Coordinate getCurrentCoordinate() {
		return this.coordinates.get(currentCoordinateId);
	}

	public String toString(){
		return gameName;
	}

	public int getCurrentCoordinateId() {
		return currentCoordinateId;
	}
}
