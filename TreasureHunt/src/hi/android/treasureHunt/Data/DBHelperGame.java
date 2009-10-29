package hi.android.treasureHunt.Data;
import hi.android.treasureHunt.Control.Game;
import hi.android.treasureHunt.Control.Game.Coordinate;
import hi.android.treasureHunt.Control.Game.Hint;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperGame {

    public static final String DEVICE_ALERT_ENABLED_ZIP = "DAEZ99";
    public static final String DB_NAME = "treasureHuntDB2";
    public static final String DB_TABLE_GAME = "game";
    public static final String DB_TABLE_COORDINATE = "coordinate";
    public static final String DB_TABLE_HINT = "hint";
    public static final int DB_VERSION = 3;

    private static final String[] GAME_COLS = new String[] { "gameId", "gameName", "currentCoordinate"};
    private static final String[] COORDINATE_COLS = new String[] { "gameId", "coordinateId", "latitude","longitude"};
    private static final String[] HINT_COLS = new String[] { "gameId", "coordinateId", "hintId", "hintText"};
    
    private SQLiteDatabase db;
    private final DBOpenHelper dbOpenHelper;

    private static class DBOpenHelper extends SQLiteOpenHelper {

        private static final String DB_CREATE_GAME = "CREATE TABLE "
            + DBHelperGame.DB_TABLE_GAME
            + " (gameId INTEGER PRIMARY KEY, gameName TEXT UNIQUE NOT NULL, currentCoordinate INTEGER);";

        private static final String DB_CREATE_COORDINATE = "CREATE TABLE "
            + DBHelperGame.DB_TABLE_COORDINATE
            + " (gameId INTEGER NOT NULL, coordinateId INTEGER NOT NULL, latitude FLOAT NOT NULL, longitude FLOAT NOT NULL);";

        private static final String DB_CREATE_HINT = "CREATE TABLE "
            + DBHelperGame.DB_TABLE_HINT
            + " (gameId INTEGER NOT NULL, coordinateId INTEGER NOT NULL, hintId INTEGER NOT NULL, hintText TEXT NOT NULL);";

        
        public DBOpenHelper(final Context context) {
            super(context, DBHelperGame.DB_NAME, null, DBHelperGame.DB_VERSION);
        }

        @Override
        public void onCreate(final SQLiteDatabase db) {
            try {
                db.execSQL(DBOpenHelper.DB_CREATE_GAME);
                db.execSQL(DBOpenHelper.DB_CREATE_COORDINATE);
                db.execSQL(DBOpenHelper.DB_CREATE_HINT);
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
            db.execSQL("DROP TABLE IF EXISTS " + DBHelperGame.DB_TABLE_GAME);
            db.execSQL("DROP TABLE IF EXISTS " + DBHelperGame.DB_TABLE_COORDINATE);
            db.execSQL("DROP TABLE IF EXISTS " + DBHelperGame.DB_TABLE_HINT);
            onCreate(db);
        }
    }


    public DBHelperGame(final Context context) {
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

    public void insertGame(final Game game) {
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
        this.db.insert(DBHelperGame.DB_TABLE_GAME, null, values);
    }
    
    private void insertCoordinate(final Game.Coordinate coordinate) {
        ContentValues values = new ContentValues();
        values.put("gameId", coordinate.getGameId());
        values.put("coordinateId", coordinate.getCoordinateId());
        values.put("latitude", coordinate.getLatitude());
        values.put("longitude", coordinate.getLongitude());
        this.db.insert(DBHelperGame.DB_TABLE_COORDINATE, null, values);
    }
    
    private void insertHint(final Game.Hint hint) {
        ContentValues values = new ContentValues();
        values.put("gameId", hint.getGameId());
        values.put("coordinateId", hint.getCoordinateId());
        values.put("hintId", hint.getHintId());
        values.put("hintText", hint.getHintText());
        this.db.insert(DBHelperGame.DB_TABLE_HINT, null, values);
    }

    public void updateGame(final Game game) {
    	// updates each coordinate in game.
    	for (Coordinate coordinate : game.coordinates) {
			updateCoordinate(coordinate);
		}
    	// updates each hint in game
    	for(Hint hint : game.hints){
    		updateHint(hint);
    	}
    	
        ContentValues values = new ContentValues();
        values.put("gameName", game.getGameName());
        values.put("currentCoordinate", game.getCurrentCoordinateId());
        try{
        	this.db.update(DBHelperGame.DB_TABLE_GAME, values, "gameId=" + game.getGameId(), null);
        }catch (SQLException e) {
    		e.printStackTrace();
    	}
        
    }
    
    private void updateCoordinate(final Game.Coordinate coordinate) {
		ContentValues values = new ContentValues();
        values.put("latitude", coordinate.getLatitude());
        values.put("longitude", coordinate.getLongitude());
    	try{
            this.db.update(DBHelperGame.DB_TABLE_COORDINATE, values, "gameId=" + coordinate.getGameId() + " AND coordinateId=" + coordinate.getCoordinateId(), null);
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
    		this.db.update(DBHelperGame.DB_TABLE_HINT, values, "gameId=" + hint.getGameId() +  " AND coordinateId=" + hint.getCoordinateId() + 
        			   " AND hintId=" + hint.getHintId(), null);
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }

    public void deleteGame(final int gameId) {
    	try{
    		this.db.delete(DBHelperGame.DB_TABLE_COORDINATE, "gameId=" + gameId, null);
        	this.db.delete(DBHelperGame.DB_TABLE_HINT, "gameId=" + gameId, null);
            this.db.delete(DBHelperGame.DB_TABLE_GAME, "gameId=" + gameId, null);
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
            cGame = this.db.query(true, DBHelperGame.DB_TABLE_GAME, DBHelperGame.GAME_COLS, "gameId = '" + gameId + "'", null, null, null, null,
                null);
            if (cGame.getCount() > 0) {
                cGame.moveToFirst();
                game.setGameId(cGame.getInt(0));
                game.setGameName(cGame.getString(1));
                game.setCurrentCoordinateId(cGame.getInt(2));
            }
            
            cCoordinate = this.db.query(true, DBHelperGame.DB_TABLE_COORDINATE, DBHelperGame.COORDINATE_COLS, "gameId = '" + gameId + "'", null, null, null, null,
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
            
            cHint = this.db.query(true, DBHelperGame.DB_TABLE_HINT, DBHelperGame.HINT_COLS, "gameId = '" + gameId + "'", null, null, null, null,
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
 
    public ArrayList<Game> getUsersGames(){
    	ArrayList<Game> arrayOfGames = new ArrayList<Game>();
    	
    	Cursor cGame = null;
        Game game;
        try {
        	//Gets all the rows from the Game table.
            cGame = this.db.query(true, DBHelperGame.DB_TABLE_GAME, DBHelperGame.GAME_COLS, null, null, null, null, null,
                null);
            if (cGame.getCount() > 0) {
                cGame.moveToFirst();
                do {
					int currentGameId = cGame.getInt(0);
                	game = getGame(currentGameId);
                	arrayOfGames.add(game);
                	cGame.moveToNext();
				} while (cGame.isLast());

            }
        } catch (SQLException e) {
        	e.printStackTrace();
        } finally {
            if (cGame != null && !cGame.isClosed()) {
                cGame.close();
            }
        }
    	return arrayOfGames;
    }
    
    public boolean exists(final int gameId) {
        Cursor c = null;
        try {
            c = this.db.query(true, DBHelperGame.DB_TABLE_GAME, DBHelperGame.GAME_COLS, "gameId = '" + gameId + "'", null, null, null, null,
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


}
