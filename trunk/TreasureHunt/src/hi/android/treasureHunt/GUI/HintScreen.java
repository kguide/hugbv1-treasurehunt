package hi.android.treasureHunt.GUI;

import hi.android.treasureHunt.Control.Controller;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class HintScreen extends Activity {
	Controller controller = Controller.getInstance();  
	Context context = this;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
				super.onCreate(savedInstanceState);
				updateView();
	}

	private void updateView() {
		setContentView(R.layout.show_hint);
		TextView myTextBox = (TextView) findViewById(R.id.hintTexti);
		myTextBox.setText(controller.getCurrentHint());
	}

}


