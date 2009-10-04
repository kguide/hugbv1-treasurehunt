package hi.android.treasureHunt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginScreen extends Activity {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);
		
		Button loginButton = (Button) findViewById(R.id.logInButtonLogInScreen);
		loginButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent startPlayScreen = new Intent(LoginScreen.this,WelcomeScreen.class);
				startActivity(startPlayScreen);
				// TODO Connect login method and validate input				
			}
		});
		
	}
}
