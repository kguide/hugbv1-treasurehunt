package hi.android.treasureHunt.GUI;

import hi.android.treasureHunt.Control.Controller;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginScreen extends Activity {
	
	Controller controller = Controller.getInstance();

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
					openWelcomeScreen();
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

	public void openWelcomeScreen(){
		// We will have to pass the controller object in the intent as well. Probably using a bundle.
		Intent startWelcomeScreen = new Intent(LoginScreen.this,WelcomeScreen.class);
		startActivity(startWelcomeScreen);
	}
}
