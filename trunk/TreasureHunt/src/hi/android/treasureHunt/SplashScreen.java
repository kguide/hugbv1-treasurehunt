package hi.android.treasureHunt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {
	
	private static final long SPLASHDELAY = 5000; // duration of splash	
	@Override
	public void onCreate(Bundle savedInstanceState){
		// Display splashScreen for n milliseconds set in SPLASHDELAY variable
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		new Handler().postDelayed(new Runnable(){
			public void run() {
				Intent startSplash = new Intent(SplashScreen.this, LoginScreen.class);
				SplashScreen.this.startActivity(startSplash);
				SplashScreen.this.finish();
			}
			
		}, SPLASHDELAY);
	}

}
