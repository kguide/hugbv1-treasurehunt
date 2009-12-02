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

public class CreateNewUser extends Activity {
	
	Controller controller = Controller.getInstance();
	private static final int ADVANCED_ID = Menu.FIRST;
	private static final int HELP_ID = Menu.FIRST + 1;
	private static final int SETTINGS_ID = Menu.FIRST + 2;
	@Override	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_new_user);

		final EditText usernameET = (EditText) findViewById(R.id.EditTextCreateUserUsername);
		final EditText passwordET = (EditText) findViewById(R.id.EditTextCreateUserPassword);
		
		Button submittButton = (Button) findViewById(R.id.ButtonCraeteUserSubmit);
		submittButton.setOnClickListener(new Button.OnClickListener() {												
			@Override
			public void onClick(View v) {
				
				String username = usernameET.getText().toString();
				String password = passwordET.getText().toString();
				
				if(controller.createUserOnline(username, password))
				{
					Intent startWelcomeScreen = new Intent(CreateNewUser.this,PlayScreen.class);
					startActivity(startWelcomeScreen);
				}
				else
				{
					// Maybe we can have it so that a TextView which is initially hidden gets displayed here 
					// showing some kind of an error.
				}
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
	    		Intent startHelpScreen = new Intent(CreateNewUser.this,HelpScreen.class);
//	    		
	    		startHelpScreen.putExtra("helpStringId", R.string.HelpScreenCreateUser);
	    		startActivity(startHelpScreen);
	    		
	    	}
	    	return true;
	    }

}