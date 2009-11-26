package hi.android.treasure.control;

import hi.android.treasure.data.DataAccess;

import java.util.ArrayList;

import android.content.Context;


public class Game {
    private int gameId;
    private String gameName;
    private boolean gameFinished;
    //	private Player gameCreator;
    //	public ArrayList<Player> players;
    public ArrayList<Hint> hints = new ArrayList<Hint>();
    public ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
    private int currentCoordinateId;
    private int numberOfCoordinates;
    private int lastHintSolved;  // used to keep track of the last solved index, so we can control how we traverse the hints.
    private int currentView;  //used for traversing back and forth through hints and coords in the gui.
    //	private int currentHintId;
    
    
    public Game() {
	this.currentView = 0;
	this.lastHintSolved = 0;
    }
    
    /**
     * Represents a hint in the Treasure Hunt game.
     */
    public class Hint {
	private int gameId;
	private int coordinateId;
	private int hintId; 		
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
	private double latitude;
	private double longitude ;


	public Coordinate(int gameId, int coordinateId, float latitude, float longitude){
	    this.gameId = gameId;
	    this.coordinateId = coordinateId;
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
	public void setLatitude(double latitude) {
	    this.latitude = latitude;
	}
	public double getLatitude() {
	    return latitude;
	}
	public void setLongitude(double longitude) {
	    this.longitude = longitude;
	}
	public double getLongitude() {
	    return longitude;
	}
    }

    public void save(int playerId, Context context) {
	//Before saving game, we check to see if the current coordinate is the last one. If yes, the game is finished.
	gameFinished = isLastCoordinate();
	DataAccess.saveGameOnAndroid(this, playerId,  context);
    }

    public void addCoordinate(int gameId, int coordinateId, float latitude, float longitude){
	Coordinate coordinate = new Coordinate(gameId, coordinateId, latitude, longitude);
	coordinates.add(coordinate);
	this.numberOfCoordinates = coordinateId; 
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

    public Coordinate getNextCoordinate() {
	return this.coordinates.get(currentCoordinateId + 1);
    }

    public String toString(){
	if (gameFinished) {
	    return gameName + " - DONE";
	}else{
	    return gameName + " - ACTIVE";
	}
    }

    public int getCurrentCoordinateId() {
	return currentCoordinateId;
    }

    public void incrementCoordinate(){
	if(currentCoordinateId == numberOfCoordinates){
	    // There are no more coordinates, do nothing
	}
	else{
	    currentCoordinateId++;
	    lastHintSolved = currentCoordinateId-1;
	}
    }

    public String getNextHintText() {
	return hints.get(currentCoordinateId).getHintText();

    }

    public boolean isLastCoordinate(){
	return currentCoordinateId == numberOfCoordinates ? true:false;
    }

    public void setGameFinished(boolean gameFinished) {
	this.gameFinished = gameFinished;
    }

    public boolean isGameFinished() {
	return gameFinished;
    }


    /*
     * functions for the view prev/next in hintScreen, 
     */


	public void setToLast() {
	    currentCoordinateId = numberOfCoordinates - 1;
	    lastHintSolved = currentCoordinateId;
	}


    /**
     * sets the view to current hint
     */
    public void setViewToCurrent() {
	currentView = lastHintSolved;
    }	
    
    
    /**
     * sets the index for next solved hint, if available.
     * @return : Boolean indicating whether a next solved hint is available.
     */
    public boolean setNextHintView() {
	if (currentView == lastHintSolved ) {
	    return false;
	}
	currentView++;
	return true;
    }
    
    /**
     * sets the index for previous solved hint, if available.
     * @return : Boolean indicating whether a previous solved hint is available.
     */
    public boolean setPrevHintView() {
	if (currentView == 0) {
	    return false;
	}
	currentView--;
	return true;
    }
    
    /**
     * returns the current view number 
     * @return : int, number of hint being viewed
     */
    public int getCurrentHintViewNr() {
    	return currentView;
    }

    /**
     * returns the current view hint String
     * @return : String the current view hint
     */
    public String getCurrentHintView() {
    	return hints.get(currentView).getHintText();
    }
    
    /**
     * returns the current view hint Coordinate 
     * @return : Coordinate object for the current Coordinate
     */
    public Coordinate getCurrentCoordinateView() {
	return this.coordinates.get(currentView);
    }
}
