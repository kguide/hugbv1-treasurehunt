package hi.android.treasure.data;
import hi.android.treasure.control.Game;
import hi.android.treasure.control.Game.Coordinate;
import hi.android.treasure.control.Game.Hint;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GameDB {

    public static final String DB_NAME = "treasureHunterDB";
    public static final String DB_TABLE_GAME = "gameTable";
    public static final String DB_TABLE_COORDINATE = "coordinate";
    public static final String DB_TABLE_HINT = "hint";
    public static final String DB_TABLE_PLAYERSGAMES = "playersGames";
    public static final int DB_VERSION = 3;

    private static final String[] GAME_COLS = new String[] { "gameId", "gameName", "currentCoordinate" , "gameFinished"};
    private static final String[] COORDINATE_COLS = new String[] { "gameId", "coordinateId", "latitude","longitude"};
    private static final String[] HINT_COLS = new String[] { "gameId", "coordinateId", "hintId", "hintText"};
//    private static final String[] PLAYERSGAMES_COLS = new String[] { "gameId", "playerId"};
    
    
    private SQLiteDatabase db;
    private final DBOpenHelper dbOpenHelper;

    private static class DBOpenHelper extends SQLiteOpenHelper {

        private static final String DB_CREATE_GAME = "CREATE TABLE "
            + GameDB.DB_TABLE_GAME
            + " (gameId INTEGER PRIMARY KEY, gameName TEXT UNIQUE NOT NULL, currentCoordinate INTEGER, gameFinished INTEGER);";

        private static final String DB_CREATE_COORDINATE = "CREATE TABLE "
            + GameDB.DB_TABLE_COORDINATE
            + " (gameId INTEGER NOT NULL, coordinateId INTEGER NOT NULL, latitude FLOAT NOT NULL, longitude FLOAT NOT NULL);";

        private static final String DB_CREATE_HINT = "CREATE TABLE "
            + GameDB.DB_TABLE_HINT
            + " (gameId INTEGER NOT NULL, coordinateId INTEGER NOT NULL, hintId INTEGER NOT NULL, hintText TEXT NOT NULL);";

        private static final String DB_CREATE_PLAYERSGAMES = "CREATE TABLE "
            + GameDB.DB_TABLE_PLAYERSGAMES
            + " (gameId INTEGER NOT NULL, playerId INTEGER NOT NULL);";
        
        public DBOpenHelper(final Context context) {
            super(context, GameDB.DB_NAME, null, GameDB.DB_VERSION);
        }

        @Override
        public void onCreate(final SQLiteDatabase db) {
            try {
                db.execSQL(DBOpenHelper.DB_CREATE_GAME);
                db.execSQL(DBOpenHelper.DB_CREATE_COORDINATE);
                db.execSQL(DBOpenHelper.DB_CREATE_HINT);
                db.execSQL(DBOpenHelper.DB_CREATE_PLAYERSGAMES);
            } catch (SQLException e) {
//                Log.e(Constants.LOGTAG, DBHelper.CLASSNAME, e);
            }
        }

        @Override
        public void onOpen(final SQLiteDatabase db) {
            super.onOpen(db);
        }

        @Override
        public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + GameDB.DB_TABLE_GAME);
            db.execSQL("DROP TABLE IF EXISTS " + GameDB.DB_TABLE_COORDINATE);
            db.execSQL("DROP TABLE IF EXISTS " + GameDB.DB_TABLE_HINT);
            db.execSQL("DROP TABLE IF EXISTS " + GameDB.DB_TABLE_PLAYERSGAMES);
            onCreate(db);
        }
    }


    public GameDB(final Context context) {
        this.dbOpenHelper = new DBOpenHelper(context);
        establishDb();
    }

    private void establishDb() {
        if (this.db == null) {
            this.db = this.dbOpenHelper.getWritableDatabase();
        }
    }

    public void cleanup() {
        if (this.db != null) {
            this.db.close();
            this.db = null;
        }
    }

    public void insertGame(final Game game, final int playerId) {
    	updatePlayersGames(game.getGameId(), playerId);
    	// inserts each coordinate in game.
    	for (Coordinate coordinate : game.coordinates) {
			insertCoordinate(coordinate);
		}
    	// inserts each hint in game
    	for(Hint hint : game.hints){
    		insertHint(hint);
    	}
    	
        ContentValues values = new ContentValues();
        values.put("gameId", game.getGameId());
        values.put("gameName", game.getGameName());
        values.put("currentCoordinate", game.getCurrentCoordinateId());
        values.put("gameFinished", 0);
        this.db.insert(GameDB.DB_TABLE_GAME, null, values);
    }
    
    private void insertCoordinate(final Game.Coordinate coordinate) {
        ContentValues values = new ContentValues();
        values.put("gameId", coordinate.getGameId());
        values.put("coordinateId", coordinate.getCoordinateId());
        values.put("latitude", coordinate.getLatitude());
        values.put("longitude", coordinate.getLongitude());
        this.db.insert(GameDB.DB_TABLE_COORDINATE, null, values);
    }
    
    private void insertHint(final Game.Hint hint) {
        ContentValues values = new ContentValues();
        values.put("gameId", hint.getGameId());
        values.put("coordinateId", hint.getCoordinateId());
        values.put("hintId", hint.getHintId());
        values.put("hintText", hint.getHintText());
        this.db.insert(GameDB.DB_TABLE_HINT, null, values);
    }

    public void updateGame(final Game game, final int playerId) {
    	
    	updatePlayersGames(game.getGameId(), playerId);
    	// updates each coordinate in game.
    	for (Coordinate coordinate : game.coordinates) {
			updateCoordinate(coordinate);
		}
    	// updates each hint in game
    	for(Hint hint : game.hints){
    		updateHint(hint);
    	}
    	
    	int gameFinished = game.isGameFinished()? 1 : 0;
    	
        ContentValues values = new ContentValues();
        values.put("gameName", game.getGameName());
        values.put("currentCoordinate", game.getCurrentCoordinateId());
        values.put("gameFinished", gameFinished);
        try{
        	this.db.update(GameDB.DB_TABLE_GAME, values, "gameId=" + game.getGameId(), null);
        }catch (SQLException e) {
    		e.printStackTrace();
    	}
        
    }
    
    private void updatePlayersGames(int gameId, int playerId) {

    	ContentValues values = new ContentValues();
        values.put("gameId", gameId);
        values.put("playerId", playerId);
        
        // If the player is already signed up for the selected game, we do nothing
    	if(!playerIsInGame(gameId, playerId)){
            this.db.insert(GameDB.DB_TABLE_PLAYERSGAMES, null, values);
    	}
	}

	private boolean playerIsInGame(int gameId, int playerId) {
    	Cursor cPlayersGames = null;
        //Gets all the rows from the Game table.
    	try{
            cPlayersGames = this.db.query(true, GameDB.DB_TABLE_PLAYERSGAMES, null, "gameId ='" + gameId + "' and playerId='" + playerId + "'",
            		null, null, null, null,null);
    	}
    	catch(Exception e){
    		e.getMessage();
    	}
        if (cPlayersGames.getCount() > 0){
        	return true;
        }
        else{
    		return false;        	
        }
	}

	private void updateCoordinate(final Game.Coordinate coordinate) {
		ContentValues values = new ContentValues();
        values.put("latitude", coordinate.getLatitude());
        values.put("longitude", coordinate.getLongitude());
    	try{
            this.db.update(GameDB.DB_TABLE_COORDINATE, values, "gameId=" + coordinate.getGameId() + " AND coordinateId=" + coordinate.getCoordinateId(), null);
    	}catch (SQLException e) {
    		e.printStackTrace();
    	}
    	
    }
    
    private void updateHint(final Game.Hint hint) {
        ContentValues values = new ContentValues();
        values.put("gameId", hint.getGameId());
        values.put("coordinateId", hint.getCoordinateId());
        values.put("hintId", hint.getHintId());
        values.put("hintText", hint.getHintText());
    	try{
    		this.db.update(GameDB.DB_TABLE_HINT, values, "gameId=" + hint.getGameId() +  " AND coordinateId=" + hint.getCoordinateId() + 
        			   " AND hintId=" + hint.getHintId(), null);
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }

    public void deleteGame(final int gameId) {
    	try{
    		this.db.delete(GameDB.DB_TABLE_COORDINATE, "gameId=" + gameId, null);
        	this.db.delete(GameDB.DB_TABLE_HINT, "gameId=" + gameId, null);
            this.db.delete(GameDB.DB_TABLE_GAME, "gameId=" + gameId, null);
            this.db.delete(GameDB.DB_TABLE_PLAYERSGAMES, "gameId=" + gameId, null);
    	}catch (SQLException e) {
    		e.printStackTrace();
    	}
    }
    
    public Game getGame(final int gameId) {
        Cursor cGame = null;
        Cursor cCoordinate = null;
        Cursor cHint = null;
        Game game = new Game();
        try {
            cGame = this.db.query(true, GameDB.DB_TABLE_GAME, GameDB.GAME_COLS, "gameId = '" + gameId + "'", null, null, null, null,
                null);
            if (cGame.getCount() > 0) {
                cGame.moveToFirst();
                game.setGameId(cGame.getInt(0));
                game.setGameName(cGame.getString(1));
                game.setCurrentCoordinateId(cGame.getInt(2));
                game.setGameFinished(1 == cGame.getInt(3)); // the game is finished if gameFinished == 1
            }
            
            cCoordinate = this.db.query(true, GameDB.DB_TABLE_COORDINATE, GameDB.COORDINATE_COLS, "gameId = '" + gameId + "'", null, null, null, null,
                    null);
            int numRows = cCoordinate.getCount();
            cCoordinate.moveToFirst();
            for (int i = 0; i < numRows; ++i) {
                int coordinateId = cCoordinate.getInt(1);
                float latitude = cCoordinate.getFloat(2);
                float longitude = cCoordinate.getFloat(3);
                game.addCoordinate(gameId, coordinateId, latitude, longitude);
                cCoordinate.moveToNext();
            }     
            
            cHint = this.db.query(true, GameDB.DB_TABLE_HINT, GameDB.HINT_COLS, "gameId = '" + gameId + "'", null, null, null, null,
                    null);
            int numRows2 = cHint.getCount();
            cHint.moveToFirst();
            for (int i = 0; i < numRows2; ++i) {
                int coordinateId = cHint.getInt(1);
                int hintId = cHint.getInt(2);
                String hintText = cHint.getString(3);
                game.addHint(gameId, coordinateId, hintId, hintText);
                cHint.moveToNext();
            }  
 
        } catch (SQLException e) {
        	e.printStackTrace();
        } finally {
            if (cGame != null && !cGame.isClosed()) {
                cGame.close();
            }
            if (cCoordinate != null && !cCoordinate.isClosed()) {
                cCoordinate.close();
            }
            if (cHint != null && !cHint.isClosed()) {
                cHint.close();
            }
        }
        return game;
    }
   
    public ArrayList<Game> getUsersGames(int playerId){
    	ArrayList<Game> arrayOfGames = new ArrayList<Game>();
    	
    	Cursor cPlayersGames = null;
        Game game;
        try {
        	//Gets all the rows from the Game table.
            cPlayersGames = this.db.query(true, GameDB.DB_TABLE_PLAYERSGAMES, null, "playerId='" + playerId + "'", null, null, null, null,
                null);
            if (cPlayersGames.getCount() > 0) {
                cPlayersGames.moveToFirst();
                do {
					int currentGameId = cPlayersGames.getInt(0);
                	game = getGame(currentGameId);
                	arrayOfGames.add(game);
                	cPlayersGames.moveToNext();
				} while (!cPlayersGames.isAfterLast());

            }
        } catch (SQLException e) {
        	e.printStackTrace();
        } finally {
            if (cPlayersGames != null && !cPlayersGames.isClosed()) {
                cPlayersGames.close();
            }
        }
    	return arrayOfGames;
    }
    
    public boolean exists(final int gameId) {
        Cursor c = null;
        try {
            c = this.db.query(true, GameDB.DB_TABLE_GAME, GameDB.GAME_COLS, "gameId = '" + gameId + "'", null, null, null, null,
                null);
            if (c.getCount() > 0) {
            	return true;
            }
            return false;
        } catch (SQLException e) {
        	e.printStackTrace();
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
        return false;
    }

	public ArrayList<Game> getFinishedGames(int playerId) {
    	
		ArrayList<Game> arrayOfGames = getUsersGames(playerId);
    	ArrayList<Game> finishedGames = new ArrayList<Game>();
    	
    	for (Game game : arrayOfGames) {
			if(game.isGameFinished()){
				finishedGames.add(game);
				deleteGame(game.getGameId());
			}
		}

    	return finishedGames;
	}


}
