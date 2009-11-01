package hi.android.treasureHunt.GUI;

import hi.android.treasureHunt.Control.Controller;
import hi.android.treasureHunt.Control.Game;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class SearchResultGamesScreen extends Activity {
    
	Controller controller = Controller.getInstance();
	Context context = this;
	
	private static final int ADVANCED_ID = Menu.FIRST;
	private static final int HELP_ID = Menu.FIRST + 1;
	private static final int SETTINGS_ID = Menu.FIRST + 2;
	private static final int PLAY_GAME_ID = 0;
	
	private ListView listView;
	private ArrayList<Game> arrayOfGames = new ArrayList<Game>();
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_games_screen);
        
        
        //Add games information to ArrayList
        String searchCriteria = controller.gameNameToSearchFor;
        arrayOfGames = controller.getGamesOnServerByName(searchCriteria, context);
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
                   Game selectedGame = (Game) listView.getAdapter().getItem(menuInfo.position);
                   controller.getGame(selectedGame.getGameId(), context);
                   
                   // TODO This is not something that a GUI object should do. Needs to be fixed !
                   controller.game.save(context);
                   
                   Intent startGoogleMapScreen = new Intent(SearchResultGamesScreen.this,GoogleMapScreen.class);
                   
                   startActivity(startGoogleMapScreen);


                   return true; /* true means: "we handled the event". */
         }
         return false;
    } 
    
    private void loadGamesFromArrayToView() {
        listView.setAdapter(new ArrayAdapter<Game>(this,
                  android.R.layout.simple_list_item_1, arrayOfGames));
   }  
    
    @Override 
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	menu.add(0,ADVANCED_ID,0,R.string.menuAdvanced);
    	menu.add(0,HELP_ID,0,R.string.menuHelp);
    	menu.add(0,SETTINGS_ID,0,R.string.menuSettings);
    	return true;
    }

}