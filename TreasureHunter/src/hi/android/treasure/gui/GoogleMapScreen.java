package hi.android.treasure.gui;

import hi.android.treasure.control.Controller;
import hi.android.treasure.control.Game.Coordinate;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
 
public class GoogleMapScreen extends MapActivity 
{    
    Controller controller = Controller.getInstance();
    Context context = this;
    Sounder mySoundHandler;
	
    private OurOverlay ourOverlay;
    private OurOverlay oldHint;
    
    private static final double TRIGGER_RADIUS = 40.0; // in meters.
	
    private LocationManager locationManager=null;
    private LocationListener locationListener=null;

    private MapView mapView; 
    private MapController mapController;
    private GeoPoint p;
    private GeoPoint p2;

    private class OurOverlay extends ItemizedOverlay {
	
	private List<OverlayItem> items;
	private Drawable marker;
	
	public OurOverlay(Drawable myMarker) {
	    super(myMarker);
	    items = new ArrayList();
	    marker = myMarker;
	}
	
	@Override
	    protected OverlayItem createItem(int i) {
	    return(items.get(i));
	}
	
	@Override
	    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
	    super.draw(canvas, mapView, shadow);
	    boundCenterBottom(marker);
	}
	
	@Override
	    protected boolean onTap(int pIndex) {    
	    if (items.get(pIndex).getTitle().equals("hint")) {
	    AlertDialog.Builder markerHintDialog = new AlertDialog.Builder(context);
	    markerHintDialog.setMessage(items.get(pIndex).getSnippet())
		.setCancelable(false)
		.setPositiveButton("Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			    dialog.cancel();
			}
		    });
	    markerHintDialog.show();    	
	    }
	    return true;
	}
    
	@Override
	    public int size() {
	    return(items.size());
	}

	public void addItem(OverlayItem item) {
	    items.clear();
	    items.add(item);
	    populate();
	}
	
	public void addItemNotDelete(OverlayItem item) {
	    items.clear();
	    items.add(item);
	    populate();
	}

	public OverlayItem getCurrent() {
	    return items.get(0);
	}
    }
    
    /** Called when the activity is first created. */
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	//mySoundHandler = new Sounder(context);
	
	//mySoundHandler.addSound(R.raw.atta);
	//mySoundHandler.playSound(1);

	/* obsolete, used to pass parameters from hintscreen
	   double params[] = null;
	   Bundle bundle = this.getIntent().getExtras();
	   if (bundle != null ) {
	   params = bundle.getDoubleArray("myLocation");
	   }
	*/
	
	// Begin Display Map  
	//----------------------------------------------------------------------------------------------------------
	setContentView(R.layout.google_maps);
	
	mapView = (MapView) findViewById(R.id.googleMapsMapview);
	mapView.setBuiltInZoomControls(true);   	
        mapController = mapView.getController();
	
	// Get the first coordinate and move the map to it.
	//******************************************************************************************************
	Coordinate coordinate = controller.game.getCurrentCoordinate();
	double latitude = coordinate.getLatitude();
	double longitude = coordinate.getLongitude();
	
	/* used to get params from hint screen, obosolete
	   if (params != null) {
	   latitude = params[0];
	   longitude = params[1];
	   }
	*/
	p = new GeoPoint(
			 (int) (latitude * 1E6), 
			 (int) (longitude * 1E6));
	
	mapController.animateTo(p);
	mapController.setZoom(16);
	//******************************************************************************************************
	//----------------------------------------------------------------------------------------------------------
	
	
	// Add icon for first coordinate location 
	//----------------------------------------------------------------------------------------------------------
	Drawable icon = getResources().getDrawable(R.drawable.pushpin);
	icon.setBounds(0, 0, 1,1);
	
	ourOverlay = new OurOverlay(icon);
	
	oldHint = new OurOverlay(getResources().getDrawable(R.drawable.donehint));
	
	ourOverlay.addItem(new OverlayItem(p,"hint",controller.game.getCurrentHintView()));

	
	List<Overlay> listOfOverlays = mapView.getOverlays();
	listOfOverlays.clear();
	listOfOverlays.add(ourOverlay);
	listOfOverlays.add(oldHint);
     
	//----------------------------------------------------------------------------------------------------------
	
	mapView.invalidate();
	
	//End Display Map           
	
	// Set up the location listener to listen for new GPS locations
	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	locationListener = new MyLocationListener();
	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 100,locationListener);
	
	// Display the Welcome text.
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setMessage("Welcome to Treasure Hunter ! Please go to Help in the Menu if are new to the game.")
	    .setCancelable(false)
	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int id) {
			dialog.cancel();
		    }
		});
	AlertDialog alert = builder.create();
	alert.show();
    }
    
    private class MyLocationListener implements LocationListener {
	public void onLocationChanged(Location location) {
	    double latitude = controller.game.getNextCoordinate().getLatitude();
	    double longitude = controller.game.getNextCoordinate().getLongitude();
	    //Log.d("mapscreenInfo", "lat:"+latitude +",long:" + longitude );
	    //check if we are within the radius.
	    if(controller.checkGPSRadius(  
					 location.getLatitude(),location.getLongitude(),
					 latitude,
					 longitude, 
					 TRIGGER_RADIUS)) {			    
		
		//Log.d("mapscreenInfo", "inside If");
		
		double nextLat = controller.game.getNextCoordinate().getLatitude();
		double nextLong = controller.game.getNextCoordinate().getLongitude();
		
		Log.d("mapscreenInfo", "nextLat:" + location.getLatitude() + 
		      ", nextLong:" + location.getLongitude());
		p = new GeoPoint(
				 (int) (nextLat * 1E6), 
				 (int) (nextLong * 1E6));
		p2 = new GeoPoint(
				  (int) (controller
					 .game.getCurrentCoordinate()
					 .getLatitude() * 1E6), 
				  (int) (controller
					 .game.getCurrentCoordinate()
					 .getLongitude() * 1E6));
		
		oldHint.addItemNotDelete(new OverlayItem(p2,"old","Already Solved."));	
		controller.game.incrementCoordinate();
		controller.game.save(controller.player.getId(),context);
		
		// New marker added to map.
		ourOverlay.addItem(new OverlayItem(p,"hint",controller.game.getNextHintText()));
		
		// Update map to show the new location
		mapController.animateTo(p);
		
		// Display a 'Atta boy, completed a hint' string.
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Greate, you just completed a hint !")
		    .setCancelable(false)
		    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				// Exit game if this is the last hint
				boolean gameFinished = controller.game.isGameFinished();
				if(gameFinished){
				    finish();
				}
			    }
			});
		AlertDialog alert = builder.create();
		alert.show();
	    }
	}
	
	public void onProviderDisabled(String provider) {
	}	
	public void onProviderEnabled(String provider) {
	}
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
	
    }
    
    @Override
	protected boolean isRouteDisplayed() {
	return false;
    }
    
}
