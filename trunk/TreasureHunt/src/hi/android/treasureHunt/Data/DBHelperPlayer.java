package hi.android.treasureHunt.Data;
import hi.android.treasureHunt.Control.Player;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperPlayer {

    public static final String DEVICE_ALERT_ENABLED_ZIP = "DAEZ99";
    public static final String DB_NAME = "treasureHuntDB";
    public static final String DB_TABLE = "players";
    public static final int DB_VERSION = 3;

    private static final String[] COLS = new String[] { "playerId", "playerName", "playerPassword", "playerScore", "playerRank"};

    private SQLiteDatabase db;
    private final DBOpenHelper dbOpenHelper;

    private static class DBOpenHelper extends SQLiteOpenHelper {

        private static final String DB_CREATE = "CREATE TABLE "
            + DBHelperPlayer.DB_TABLE
            + " (playerId INTEGER PRIMARY KEY, playerName TEXT UNIQUE NOT NULL, playerPassword TEXT NOT NULL, playerScore INTEGER, playerRank INTEGER);";

        public DBOpenHelper(final Context context) {
            super(context, DBHelperPlayer.DB_NAME, null, DBHelperPlayer.DB_VERSION);
        }

        @Override
        public void onCreate(final SQLiteDatabase db) {
            try {
                db.execSQL(DBOpenHelper.DB_CREATE);
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
            db.execSQL("DROP TABLE IF EXISTS " + DBHelperPlayer.DB_TABLE);
            onCreate(db);
        }
    }


    public DBHelperPlayer(final Context context) {
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

    public void insert(final Player player) {
        ContentValues values = new ContentValues();
        values.put("playerId", player.getId());
        values.put("playerName", player.getName());
        values.put("playerPassword", player.getPassword());
        values.put("playerRank", player.getRank());
        values.put("playerScore", player.getScore());
        this.db.insert(DBHelperPlayer.DB_TABLE, null, values);
    }

    public void update(final Player player) {
        ContentValues values = new ContentValues();
        values.put("playerName", player.getName());
        values.put("playerPassword", player.getPassword());
        values.put("playerRank", player.getRank());
        values.put("playerScore", player.getScore());
        this.db.update(DBHelperPlayer.DB_TABLE, values, "playerId=" + player.getId(), null);
    }

    public void delete(final int id) {
        this.db.delete(DBHelperPlayer.DB_TABLE, "playerId=" + id, null);
    }

    public Player get(final int playerId) {
        Cursor c = null;
        Player player = null;
        try {
            c = this.db.query(true, DBHelperPlayer.DB_TABLE, DBHelperPlayer.COLS, "playerId = '" + playerId + "'", null, null, null, null,
                null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                player = new Player();
                player.setId(c.getInt(0));
                player.setName(c.getString(0));
                player.setPassword(c.getString(1));
                player.setScore(c.getInt(1));
                player.setRank(c.getInt(2));
            }
        } catch (SQLException e) {
//            Log.v(Constants.LOGTAG, DBHelper.CLASSNAME, e);
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
        return player;
    }
    
    public Player get(final String playerName) {
        Cursor c = null;
        Player player = null;
        try {
            c = this.db.query(true, DBHelperPlayer.DB_TABLE, DBHelperPlayer.COLS, "playerName = '" + playerName + "'", null, null, null, null,
                null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                player = new Player();
                player.setId(c.getInt(0));
                player.setName(c.getString(1));
                player.setPassword(c.getString(2));
                player.setScore(c.getInt(1));
                player.setRank(c.getInt(2));
            }
        } catch (SQLException e) {
//            Log.v(Constants.LOGTAG, DBHelper.CLASSNAME, e);
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
        return player;
    }
    
    public boolean exists(final String playerName) {
        Cursor c = null;
        try {
            c = this.db.query(true, DBHelperPlayer.DB_TABLE, DBHelperPlayer.COLS, "playerName = '" + playerName + "'", null, null, null, null,
                null);
            if (c.getCount() > 0) {
            	return true;
            }
            return false;
        } catch (SQLException e) {
//            Log.v(Constants.LOGTAG, DBHelper.CLASSNAME, e);
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
        return false;
    }


}
