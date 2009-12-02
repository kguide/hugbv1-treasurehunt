package hi.android.treasure.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class PlayScreen extends Activity {
    
	private static final int ADVANCED_ID = Menu.FIRST;
	private static final int HELP_ID = Menu.FIRST + 1;
	private static final int SETTINGS_ID = Menu.FIRST + 2;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_screen);

        Button myGamesButton = (Button) findViewById(R.id.ButtonPlayScreenMyGames);
        myGamesButton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent openFindAGameIntent = new Intent(PlayScreen.this,MyGamesScreen.class);
				startActivity(openFindAGameIntent);
			}
		});
        
        Button findButton = (Button) findViewById(R.id.ButtonPlayScreenFindGame);
        findButton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent openFindAGameIntent = new Intent(PlayScreen.this,FindAGameScreen.class);
				startActivity(openFindAGameIntent);
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
    		Intent startHelpScreen = new Intent(PlayScreen.this,HelpScreen.class);
//    		
    		startHelpScreen.putExtra("helpStringId", R.string.HelpscreenPlayScreen);
    		startActivity(startHelpScreen);
    		
    	}
    	return true;
    }
}