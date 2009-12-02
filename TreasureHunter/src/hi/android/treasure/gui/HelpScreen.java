package hi.android.treasure.gui;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HelpScreen extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_screen);
        Bundle bundle = getIntent().getExtras();
        int helpStringId = bundle.getInt("helpStringId");
       

        TextView helpText = (TextView)findViewById(R.id.ViewHelpScreen);
         
         helpText.setText(helpStringId);
	}
	
}