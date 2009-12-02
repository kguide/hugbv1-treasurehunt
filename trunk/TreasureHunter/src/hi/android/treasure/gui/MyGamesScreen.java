package hi.android.treasure.gui;

import hi.android.treasure.control.Controller;
import hi.android.treasure.control.Game;

import java.util.ArrayList;

import android.app.ListActivity;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class MyGamesScreen extends ListActivity {
    
	Controller controller = Controller.getInstance();
	Context context = this;
	
	private static final int ADVANCED_ID = Menu.FIRST;
	private static final int HELP_ID = Menu.FIRST + 1;
	private static final int SETTINGS_ID = Menu.FIRST + 2;
	
	private static final int PLAY_GAME_ID = 0;
//	private static final int SEE_DETAILS_ID = 1;
	private static final int DELETE_GAME_ID = 2;
	
	private ListView listView;
	private ArrayList<Game> arrayOfGames = new ArrayList<Game>();
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_games_screen);
        
        //Add games to ArrayList
        arrayOfGames = controller.getUsersGamesOnAndroid(context);
        this.listView = getListView();
        
        //Load items from arrayList to listView and sets context listeners
        initListView();
        
        //Set up buttons and listeners
        Button updateGamesButton = (Button) findViewById(R.id.ButtonMyGamesScreenUpdateMyGames);
        updateGamesButton.setOnClickListener(new Button.OnClickListener() {												
			@Override
			public void onClick(View v) {
				arrayOfGames = controller.getUsersGamesOnServer(context);
				initListView();
		}
	});	
        Button sendGamesButton = (Button) findViewById(R.id.ButtonMyGamesScreenSendGames);
        sendGamesButton.setOnClickListener(new Button.OnClickListener() {												
			@Override
			public void onClick(View v) {
				controller.sendFinishedGamesOnline(context); //This also deletes the games from the phone
				arrayOfGames = controller.getUsersGamesOnAndroid(context);
				initListView();
		}
	});
    }
    
    private void initListView() {

    	loadGamesFromArrayToView();

        /* Add Context-Menu listener to the ListView. */
        listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

             @Override
             public void onCreateContextMenu(ContextMenu menu, View v,
                       ContextMenuInfo menuInfo) {
            	    menu.add(0, PLAY_GAME_ID, 0, R.string.play);
//                    menu.add(0, SEE_DETAILS_ID, 0, R.string.seeDetails);
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
                   Game selectedGame = (Game) listView.getAdapter().getItem(menuInfo.position);
                   controller.game = selectedGame;
                   Intent startGoogleMapScreen = new Intent(MyGamesScreen.this,GoogleMapScreen.class);
                   
                   startActivity(startGoogleMapScreen);
                   return true;
              case DELETE_GAME_ID:
            	  
            	  /*Delete a selected game from androidDB.*/
                  Game selectedGameToDelete = (Game) listView.getAdapter().getItem(menuInfo.position);
                  controller.deleteGameFromAndroid(selectedGameToDelete.getGameId(),context);
                  arrayOfGames = controller.getUsersGamesOnAndroid(context);
  				  initListView();
  				  controller.removeUserFromSelectedGame(selectedGameToDelete.getGameId(),context);
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

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	case HELP_ID:
    		Intent startHelpScreen = new Intent(MyGamesScreen.this,HelpScreen.class);
//    		
    		startHelpScreen.putExtra("helpStringId", R.string.HelpScreenGameScreen);
    		startActivity(startHelpScreen);
    		
    	}
    	
    	
    	return true;
    }


}