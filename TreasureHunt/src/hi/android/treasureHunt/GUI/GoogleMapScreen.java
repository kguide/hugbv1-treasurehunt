package hi.android.treasureHunt.GUI;

import hi.android.treasureHunt.Control.Controller;
import hi.android.treasureHunt.Control.Game.Coordinate;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
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
    MapView mapView; 
    MapController mc;
    GeoPoint p;
 
    class MapOverlay extends com.google.android.maps.Overlay
    {
        @Override
        public boolean draw(Canvas canvas, MapView mapView, 
        boolean shadow, long when) 
        {
            super.draw(canvas, mapView, shadow);                   
 
            //---translate the GeoPoint to screen pixels---
            Point screenPts = new Point();
            mapView.getProjection().toPixels(p, screenPts);
 
            //---add the marker---
            Bitmap bmp = BitmapFactory.decodeResource(
                getResources(), R.drawable.icon); 
           
            canvas.drawBitmap(bmp, screenPts.x, screenPts.y-50, null);         
            return true;
        }
    } 

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.google_maps);
        
        mapView = (MapView) findViewById(R.id.googleMapsMapview);
        LinearLayout zoomLayout = (LinearLayout)findViewById(R.id.googleMapsZoom);  
        View zoomView = mapView.getZoomControls(); 
 
        zoomLayout.addView(zoomView, 
            new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, 
                LayoutParams.WRAP_CONTENT)); 
        mapView.displayZoomControls(true);
        
     
            mc = mapView.getController();

            Coordinate coordinate = controller.game.getCurrentCoordinate();
            float latitude = coordinate.getLatitude();
            float longitude = coordinate.getLongitude();
            
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

    }
 
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}