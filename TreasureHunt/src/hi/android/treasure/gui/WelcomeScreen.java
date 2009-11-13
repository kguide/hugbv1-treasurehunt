package hi.android.treasure.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class WelcomeScreen extends Activity {
    
	private static final int ADVANCED_ID = Menu.FIRST;
	private static final int HELP_ID = Menu.FIRST + 1;
	private static final int SETTINGS_ID = Menu.FIRST + 2;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomescreen);
        
        Button playButton = (Button) findViewById(R.id.ButtonWelcomeScreenPlay);
        playButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent startPlayScreen = new Intent(WelcomeScreen.this,PlayScreen.class);
				startActivity(startPlayScreen);
			}		
		});  
        
        Button statsButton = (Button) findViewById(R.id.ButtonWelcomeScreenStatistics);
        statsButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent startStatsScreen = new Intent(WelcomeScreen.this,StatsScreen.class);
				startActivity(startStatsScreen);
			}
		}); 
        
        Button logOutButton = (Button) findViewById(R.id.ButtonWelcomeScreenLogOut);
        logOutButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent startStatsScreen = new Intent(WelcomeScreen.this,LoginScreen.class);
				startActivity(startStatsScreen);
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