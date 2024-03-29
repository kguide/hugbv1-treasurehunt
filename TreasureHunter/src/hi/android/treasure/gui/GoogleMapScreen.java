package hi.android.treasure.gui;

import hi.android.treasure.control.Controller;
import hi.android.treasure.control.Game.Coordinate;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.Canvas; 
import android.graphics.Paint; 
import android.graphics.Bitmap; 
import android.graphics.BitmapFactory; 
import android.graphics.Paint.Style; 

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;
 
public class GoogleMapScreen extends MapActivity 
{    

    public static final int PLAYER_POS_ID = Menu.FIRST;
    public static final int HELP_ID = Menu.FIRST+1;
    public static final int CURRENT_POS_ID = Menu.FIRST+2;
    
  
    Controller controller = Controller.getInstance();
    Context context = this;
    Sounder mySoundHandler;

    double currLat;
    double currLong;
	
    private OurOverlay ourOverlay;
    private OurOverlay oldHint;
    private MapOverlay playerPos;
    
    private static final double TRIGGER_RADIUS = 40.0; // in meters.
	
    private LocationManager locationManager=null;
    private LocationListener locationListener=null;

    private MapView mapView; 
    private MapController mapController;
    private GeoPoint p;
    private GeoPoint p2;
    private GeoPoint playerPosition;

    private class OurOverlay extends ItemizedOverlay<OverlayItem> {
	
	private List<OverlayItem> items;
	private Drawable marker;
	
	public OurOverlay(Drawable myMarker) {
	    super(myMarker);
	    items = new ArrayList<OverlayItem>();
	    marker = myMarker;
	    populate();
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
		mySoundHandler.addSound(R.raw.cork);
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
	    setLastFocusedIndex(-1); 
	    populate();
	}
	
	public void addItemNotDelete(OverlayItem item) {
	    items.add(item);
	    setLastFocusedIndex(-1); 
	    populate();
	}

	
    }




 class MapOverlay extends com.google.android.maps.Overlay
    {
	private List<Bitmap> items;
       
	private int animcounter;
	private long deltaTimer;


	public MapOverlay() {
	    animcounter = 0;
	    deltaTimer = 0;
	    items = new ArrayList<Bitmap>();
	    items.add(BitmapFactory.decodeResource(getResources(), R.drawable.badg1));
	    items.add(BitmapFactory.decodeResource(getResources(), R.drawable.badg2));
	    items.add(BitmapFactory.decodeResource(getResources(), R.drawable.badg3));
	    items.add(BitmapFactory.decodeResource(getResources(), R.drawable.badg3));
	    items.add(BitmapFactory.decodeResource(getResources(), R.drawable.badg2));
	    items.add(BitmapFactory.decodeResource(getResources(), R.drawable.badg1));
	    items.add(BitmapFactory.decodeResource(getResources(), R.drawable.badg4));

	}
        @Override
            public boolean draw(Canvas canvas, MapView mapView, 
                                boolean shadow, long when) 
        {
	    super.draw(canvas, mapView, shadow);
	    Point screenPts = new Point();
	    mapView.getProjection().toPixels(playerPosition, screenPts);
	    canvas.drawBitmap(items.get(animcounter), screenPts.x-25, screenPts.y-50, null); 
	    if (deltaTimer > when ) return true;
	    animcounter++;
	    animcounter = animcounter % 7;
	    deltaTimer = when + 80;
	    return true;
        }
 }

    
    /** Called when the activity is first created. */
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


	//******* Load sounds
	mySoundHandler = new Sounder(this);
	
	mySoundHandler.addSound(R.raw.zoom1);
	mySoundHandler.addSound(R.raw.tada);
	mySoundHandler.playSound(R.raw.zoom1);

        mySoundHandler.addSound(R.raw.cork);
	mySoundHandler.addSound(R.raw.hint);
	mySoundHandler.playSound(R.raw.tada);
	mySoundHandler.playSound(R.raw.hint);
	mySoundHandler.playSound(R.raw.cork);

	//stop loading sounds

	
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
	icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
	ourOverlay = new OurOverlay(icon);
	
	Drawable icon2 = getResources().getDrawable(R.drawable.donehint);
	icon2.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
	
	oldHint = new OurOverlay(icon2);
	int numFinished = controller.game.getCurrentCoordinateId();

	while ( numFinished > 1) {
	    p2 = new GeoPoint((int) (controller.game.coordinates.get(numFinished).getLatitude() * 1E6), 
			      (int) (controller.game.coordinates.get(numFinished).getLongitude() * 1E6));
	    
	    oldHint.addItemNotDelete(new OverlayItem(p2,"old","Already Solved."));	
	    numFinished--;
	}
	
	
	
	
	
	//Drawable icon3 = getResources().getDrawable(R.drawable.player);
	//icon3.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
	playerPos = new MapOverlay();
	
	ourOverlay.addItem(new OverlayItem(p,"hint",controller.game.getCurrentHintText()));
	playerPosition=new GeoPoint((int) (1 * 1E6), 
			(int) (1 * 1E6));
	
	List<Overlay> listOfOverlays = mapView.getOverlays();
	listOfOverlays.clear();
	listOfOverlays.add(ourOverlay);
	listOfOverlays.add(oldHint);
	listOfOverlays.add(playerPos);
     
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
	    currLat = location.getLatitude();
	    currLong = location.getLongitude();

	    playerPosition = new GeoPoint((int) (currLat * 1E6), 
			    (int) (currLong * 1E6));
	    //Log.d("mapscreenInfo", "lat:"+latitude +",long:" + longitude );
	    //check if we are within the radius.
	    if(controller.checkGPSRadius(currLat,currLong,
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
			
			boolean isGameFinished = controller.game.isGameFinished();
			if(!isGameFinished){
			    mySoundHandler.playSound(R.raw.hint);
				// Display a 'Atta boy, completed a hint' string.
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage("Great, you just completed a hint !")
				    .setCancelable(false)
				    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog, int id) {
						mySoundHandler.addSound(R.raw.cork);
						dialog.cancel();
					    }
					});
				AlertDialog alert = builder.create();
				alert.show();
			}
			else{
			    mySoundHandler.playSound(R.raw.tada);
				// Display a 'Congratulation, you have finished the game !.
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage("Congratulation, you have finished the game !")
				    .setCancelable(false)
				    .setPositiveButton("Exit game", new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						// Exit game if this is the last hint
						    finish();
					    }
					});
				AlertDialog alert = builder.create();
				alert.show();
			}
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
 @Override 
	public boolean onCreateOptionsMenu(Menu menu){
	boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0,HELP_ID,0,"Help");
		menu.add(0,PLAYER_POS_ID,0,"Show player");
    	menu.add(0,CURRENT_POS_ID,0,"Show current hint");
    	return result;
    }


    
   
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case PLAYER_POS_ID:
	    mySoundHandler.playSound(R.raw.zoom1);
	    mapController.animateTo(new GeoPoint((int) (currLat * 1E6), 
						 (int) ( currLong * 1E6)));
	    
	    break;
	case CURRENT_POS_ID:
	    mySoundHandler.playSound(R.raw.zoom1);
	    mapController.animateTo(new GeoPoint((int) (controller
							.game.getCurrentCoordinate()
							.getLatitude() * 1E6), 
						 (int) (controller
							.game.getCurrentCoordinate()
							.getLongitude() * 1E6)));
	    break;
	    
	case HELP_ID:
		String helpString = "Playing a game of Treasure Hunter is very easy. When you start you will receive a starting location. ";
		helpString += "You don't have to go to that location but it should help you realize where the game is intended to be played from. ";
		helpString += "If you tap on the icon with the first location you should receive a hint that should give you a clue how to get ";
		helpString += "to the next location. Once you figure out the hint and go to the next location the game will automatically tell you ";
		helpString += "that you have completed a hint. An icon will then appear on the new location and you can tap on that location to ";
		helpString += "get the next hint. Rinse and repeat until all locations have been visited. Good luck !";
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(helpString)
		       .setCancelable(false)
		       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
		break;
	default : 
	    return super.onOptionsItemSelected(item);
	}
	return true; /* true means: "we handled the event". */
    } 
        
}
