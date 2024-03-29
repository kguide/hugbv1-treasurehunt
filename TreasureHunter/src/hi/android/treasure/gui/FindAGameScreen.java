package hi.android.treasure.gui;

import hi.android.treasure.control.Controller;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FindAGameScreen extends Activity {
    
	Controller controller = Controller.getInstance();
	
	private static final int ADVANCED_ID = Menu.FIRST;
	private static final int HELP_ID = Menu.FIRST + 1;
	private static final int SETTINGS_ID = Menu.FIRST + 2;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_a_game_screen);
        
        final EditText searchCriteriaTextET = (EditText) findViewById(R.id.EditTextFindAGameScreenSearchfield);
        
        Button top10GamesButton = (Button) findViewById(R.id.ButtonFindAGameScreenSearchMostPopular);
        top10GamesButton.setOnClickListener(new Button.OnClickListener() {												
			@Override
			public void onClick(View v) {
				controller.gameNameToSearchFor = "";
				Intent startSearchResultGamesScreen = new Intent(FindAGameScreen.this,SearchResultGamesScreen.class);
				startActivity(startSearchResultGamesScreen);
		}
	});
        
        //Right now this button only searches by game names
        Button searchByCriteriaButton = (Button) findViewById(R.id.ButtonFindAGameScreenSearchCriteria);
        searchByCriteriaButton.setOnClickListener(new Button.OnClickListener() {												
			@Override
			public void onClick(View v) {
				controller.gameNameToSearchFor = searchCriteriaTextET.getText().toString();
				Intent startSearchResultGamesScreen = new Intent(FindAGameScreen.this,SearchResultGamesScreen.class);
				startActivity(startSearchResultGamesScreen);
		}
	});	
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
	    		Intent startHelpScreen = new Intent(FindAGameScreen.this,HelpScreen.class);
//	    		
	    		startHelpScreen.putExtra("helpStringId", R.string.HelpscreenFindAGame);
	    		startActivity(startHelpScreen);
	    		
	    	}
	    	return true;
	    }
}