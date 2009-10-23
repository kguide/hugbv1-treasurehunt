package hi.android.treasureHunt.GUI;

import hi.android.treasureHunt.Control.Controller;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowHint extends Activity {
	Controller controller = Controller.getInstance();  //
	@Override
	public void onCreate(Bundle savedInstanceState){
				super.onCreate(savedInstanceState);
				setContentView(R.layout.show_hint);
				TextView myTextBox = (TextView) findViewById(R.id.hintTexti);
				myTextBox.setText(controller.getCurrentHint());
	}
}


