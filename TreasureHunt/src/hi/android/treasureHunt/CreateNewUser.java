package hi.android.treasureHunt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateNewUser extends Activity {
	
	Controller controller = new Controller();

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
				
				if(controller.createNewUser(username, password))
				{
					Intent startWelcomeScreen = new Intent(CreateNewUser.this,WelcomeScreen.class);
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
}