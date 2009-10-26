package hi.android.treasureHunt.Control;

import java.util.ArrayList;

public class Game {
	private int gameId;
	private String gameName;
	private Player gameCreator;
	private ArrayList<Player> players;
	private ArrayList<Clue> clues;
	private ArrayList<Coordinate> coordinates;
	private int currClue;
	private int numberOfClues;
	private int guessCounter;
	
	/**
	 * Represents a clue in the Treasure Hunt game.
	 */
	public class Clue {
		private int clueType;
		private int gameId;
		private int clueId; 		
		private int subClueId;		
		private String clueText;
	}
	
	/**
	 * Represents a GPS coordinate
	 */
	public class Coordinate {
		private int gameId;
		private int coordinateId;		//Relative to a game
		private double latitude;
		private double longitude ;
	}

	
	/**
	 * Advances to the next hint.
	 * 
	 * @return : number of hints left
	 */
	public int nextHint() {
		if (currClue <= numberOfClues) {
			currClue++;
		}
		return numberOfClues - currClue;
	}
	
	/**
	 * gets the current hint
	 * 
	 * @return : String representation of current hint
	 */
	public String getCurrentHint() {
		return clues.get(currClue).clueText;
	}
	
	/**
	 * gets the current hint coordinates
	 * 
	 * @return : Coordinate: the current coordinates for current hint.
	 */
	public Coordinate getCurrentCoords() {
		return coordinates.get(currClue);
	}	
}	


