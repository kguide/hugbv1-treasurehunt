package hi.android.treasureHunt.GUI;

import hi.android.treasureHunt.Control.Controller;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
        
        // Set up spinner object
        Spinner spinner = (Spinner) findViewById(R.id.SpinnerFindAGameScreenCategories);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.selectableSearchByItems, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        final EditText searchCriteriaTextET = (EditText) findViewById(R.id.EditTextFindAGameScreenSearchfield);
        
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
}