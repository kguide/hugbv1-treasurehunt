package hi.android.treasureHunt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginScreen extends Activity {
	
	Controller controller = new Controller();

	@Override	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);

		final EditText usernameET = (EditText) findViewById(R.id.EditTextLoginScreenUsername);
		final EditText passwordET = (EditText) findViewById(R.id.EditTextLoginScreenPassword);
		
	
		
		Button loginButton = (Button) findViewById(R.id.ButtonLogInScreenLogIn);
		loginButton.setOnClickListener(new Button.OnClickListener() {												
			@Override
			public void onClick(View v) {
				
				String username = usernameET.getText().toString();
				String password = passwordET.getText().toString();
				
				if(controller.logIn(username, password))
				{
					Intent startWelcomeScreen = new Intent(LoginScreen.this,WelcomeScreen.class);
					startActivity(startWelcomeScreen);
				}
				else
				{
					// Maybe we can have it so that a TextView which is initially hidden gets displayed here 
					// showing some kind of an error.
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
}
