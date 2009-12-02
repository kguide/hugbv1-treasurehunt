package hi.android.treasure.gui;

import hi.android.treasure.control.Controller;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginScreen extends Activity {
	
	Controller controller = Controller.getInstance();
	private static final int ADVANCED_ID = Menu.FIRST;
	private static final int HELP_ID = Menu.FIRST + 1;
	private static final int SETTINGS_ID = Menu.FIRST + 2;
	
	@Override	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);

		final EditText usernameET = (EditText) findViewById(R.id.EditTextLoginScreenUsername);
		final EditText passwordET = (EditText) findViewById(R.id.EditTextLoginScreenPassword);
		final TextView errorTV = (TextView) findViewById(R.id.TextViewLoginScreenError);
		final Context context = this;
		Button loginButton = (Button) findViewById(R.id.ButtonLogInScreenLogIn);
		loginButton.setOnClickListener(new Button.OnClickListener() {	
			@Override
			public void onClick(View v) {
				String username = usernameET.getText().toString();
				String password = passwordET.getText().toString();
				
				if(controller.logIn(username, password, context)){
					openPlayScreen();
				}
				else{
					errorTV.setText("Wrong username or password");
				}
		}
	});	

	Button createNewUserButton = (Button) findViewById(R.id.ButtonLogInScreenNewUser);
	createNewUserButton.setOnClickListener(new Button.OnClickListener() {												
		@Override
		public void onClick(View v) {
			Intent startCreateNewUserScreen = new Intent(LoginScreen.this,CreateNewUser.class);
			startActivity(startCreateNewUserScreen);
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
	    		Intent startHelpScreen = new Intent(LoginScreen.this,HelpScreen.class);
//	    		
	    		startHelpScreen.putExtra("helpStringId", R.string.HelpscreenLogIn);
	    		startActivity(startHelpScreen);
	    		
	    	}
	    	return true;
	    }
	public void openPlayScreen(){
		// We will have to pass the controller object in the intent as well. Probably using a bundle.
		Intent startPlayScreen = new Intent(LoginScreen.this,PlayScreen.class);
		startActivity(startPlayScreen);
	}
	
}
