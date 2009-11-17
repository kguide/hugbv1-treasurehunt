package hi.android.treasure.gui;

import hi.android.treasure.control.Controller;
import hi.android.treasure.control.Game.Coordinate;

import java.util.List;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.MapView.LayoutParams;
 
public class GoogleMapScreen extends MapActivity 
{    
    Controller controller = Controller.getInstance();
    Context context = this;
	
    private static final double TRIGGER_RADIUS = 500.0; // in meters.
	
    private LocationManager locationManager=null;
    private LocationListener locationListener=null;

    private MapView mapView; 
    private MapController mc;
    private GeoPoint p;
 
    class MapOverlay extends com.google.android.maps.Overlay
    {
        @Override
	    public boolean draw(Canvas canvas, MapView mapView, 
				boolean shadow, long when) 
        {
	    
            super.draw(canvas, mapView, shadow);
            
	    Paint paint = new Paint();
	    
            //---translate the GeoPoint to screen pixels---
            Point screenPts = new Point();
            mapView.getProjection().toPixels(p, screenPts);
	    
	    //---add the marker---
	    //Bitmap bmp = BitmapFactory.decodeResource(
	    //    getResources(), R.drawable.icon); 
	    //canvas.drawBitmap(bmp, screenPts.x, screenPts.y-50, null); 
	    paint.setStrokeWidth(1);
	    paint.setARGB(255, 255, 0, 0);
	    paint.setStyle(Paint.Style.STROKE);
	    canvas.drawText("X", screenPts.x, screenPts.y, paint);
	    //canvas.drawBitmap(bmp, myScreenCoords.x, myScreenCoords.y, paint);
            return true;
        }
    } 
    
    /** Called when the activity is first created. */
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
	double params[] = null;
	
	Bundle bundle = this.getIntent().getExtras();
	if (bundle != null ) {
	    params = bundle.getDoubleArray("myLocation");
	}
	
	//Begin Display Map        
        setContentView(R.layout.google_maps);
	
        mapView = (MapView) findViewById(R.id.googleMapsMapview);
        LinearLayout zoomLayout = (LinearLayout)findViewById(R.id.googleMapsZoom);  
        View zoomView = mapView.getZoomControls(); 
	
        zoomLayout.addView(
			   zoomView, 
			   new LinearLayout.LayoutParams(
							 LayoutParams.WRAP_CONTENT, 
							 LayoutParams.WRAP_CONTENT)); 
        mapView.displayZoomControls(true);
        
	
	mc = mapView.getController();
	
	
	Coordinate coordinate = controller.game.getCurrentCoordinate();
	double latitude = coordinate.getLatitude();
	double longitude = coordinate.getLongitude();
	
	if (params != null) {
	    latitude = params[0];
	    longitude = params[1];
	}
	
	p = new GeoPoint(
			 (int) (latitude * 1E6), 
			 (int) (longitude * 1E6));
	
	mc.animateTo(p);
	mc.setZoom(16); 
	mapView.invalidate();
	
	MapOverlay mapOverlay = new MapOverlay();
	List<Overlay> listOfOverlays = mapView.getOverlays();
	listOfOverlays.clear();
	listOfOverlays.add(mapOverlay);        
	
	mapView.invalidate();
        
	//End Display Map           
	
	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	
	locationListener = new MyLocationListener();
	
	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
					       locationListener);
	
        
    }
    private class MyLocationListener implements LocationListener {
    	public void onLocationChanged(Location location) {
	    if(
	       controller.checkGPSRadius(  //check if we are within the radius.
					 location.getLatitude(),location.getLongitude(),
					 controller.game.getCurrentCoordinate().getLatitude(),
					 controller.game.getCurrentCoordinate().getLongitude(), 
					 TRIGGER_RADIUS)) {			    
		controller.game.incrementCoordinate();
		controller.game.save(controller.player.getId(),context);
		
		Intent startHintScreen = new Intent(GoogleMapScreen.this,HintScreen.class);
		startActivity(startHintScreen);
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