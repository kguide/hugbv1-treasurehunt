package hi.android.treasureHunt.GUI;

import hi.android.treasureHunt.Control.Controller;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class HintScreen extends Activity {
	Controller controller = Controller.getInstance();  
	private LocationManager locationManager=null;
	private LocationListener locationListener=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
				super.onCreate(savedInstanceState);
				updateView();
				
	    		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

	    		locationListener = new MyLocationListener();

	    		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
	    				locationListener); 
	}

	private void updateView() {
		setContentView(R.layout.show_hint);
		TextView myTextBox = (TextView) findViewById(R.id.hintTexti);
		myTextBox.setText(controller.getCurrentHint());
	}

	private void checkIfWithinLocation(Location location) {
		double newLocationLatitude = location.getLatitude();
		double newLocationLongitude = location.getLongitude();
		double targetLocationLatitude = (double) controller.game.getNextCoordinate().getLatitude();
		double targetLocationLongitude = (double) controller.game.getNextCoordinate().getLongitude();
		boolean withinRadius = controller.checkGPSRadius(newLocationLatitude, newLocationLongitude, targetLocationLatitude, targetLocationLongitude, 250);
		
		if(withinRadius){
				controller.game.incrementCoordinate();
				updateView();
				if(controller.game.isLastCoordinate()){
					
				}
		}
 }
	private class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location location) {
				checkIfWithinLocation(location);
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}

	}

}


