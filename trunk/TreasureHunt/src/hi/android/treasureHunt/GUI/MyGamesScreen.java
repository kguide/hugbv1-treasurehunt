package hi.android.treasureHunt.GUI;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class MyGamesScreen extends Activity {
    
	private static final int ADVANCED_ID = Menu.FIRST;
	private static final int HELP_ID = Menu.FIRST + 1;
	private static final int SETTINGS_ID = Menu.FIRST + 2;
	private static final int PLAY_GAME_ID = 0;
	private static final int SEE_DETAILS_ID = 1;
	private static final int DELETE_GAME_ID = 2;
	
	private ListView listView;
	private ArrayList<Game> arrayList = new ArrayList<Game>();
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_games_screen);
        
        //Add games to ArrayList
        arrayList.add(new Game("testGame1"));
        arrayList.add(new Game("testGame2"));
        arrayList.add(new Game("testGame3"));
        arrayList.add(new Game("testGame4"));
        
        this.listView = (ListView) findViewById(R.id.listViewMyGames);
        
        //Load items from arrayList to listView and sets context listeners
        initListView();
    }
    
    private void initListView() {

    	loadGamesFromArrayToView();

        /* Add Context-Menu listener to the ListView. */
        listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

             @Override
             public void onCreateContextMenu(ContextMenu menu, View v,
                       ContextMenuInfo menuInfo) {
            	    menu.add(0, PLAY_GAME_ID, 0, R.string.play);
                    menu.add(0, SEE_DETAILS_ID, 0, R.string.seeDetails);
                    menu.add(0, DELETE_GAME_ID, 0, R.string.delete);
             }
       }); 
   } 
    
    @Override
    public boolean onContextItemSelected(MenuItem aItem) {
         AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) aItem.getMenuInfo();

         /* Switch on the ID of the item, to get what the user selected. */
         switch (aItem.getItemId()) {
              case PLAY_GAME_ID:
            	  
                   /* Get the selected item out of the Adapter by its position. */
                   //Game selectedGame = (Game) listView.getAdapter().getItem(menuInfo.position);
                   /* Remove it from the list.*/
                   //arrayList.remove(selectedGame);
            	  /* Reload the ListView once the selected item has been removed.*/
                   //loadGamesFromArrayToView();
            	  //Here we need to call the first digital mock-up screen.
                   return true; /* true means: "we handled the event". */
         }
         return false;
    } 
    
    private void loadGamesFromArrayToView() {
        listView.setAdapter(new ArrayAdapter<Game>(this,
                  android.R.layout.simple_list_item_1, arrayList));
   }  
    
    @Override 
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	menu.add(0,ADVANCED_ID,0,R.string.menuAdvanced);
    	menu.add(0,HELP_ID,0,R.string.menuHelp);
    	menu.add(0,SETTINGS_ID,0,R.string.menuSettings);
    	return true;
    }
    
    /** Test class to see if the Context menu selection works. This is clearly not an implementation of THE game class. */
    protected class Game {

         protected String name;

         protected Game(String name) {
              this.name = name;
         }

         /** The ListView is going to display the toString() return-value! */
         public String toString() {
              return name;
         }
    }    
}