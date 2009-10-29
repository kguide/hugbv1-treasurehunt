package hi.android.treasureHunt.GUI;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import com.google.android.maps.MapView.LayoutParams;  
import android.view.View;
import android.widget.LinearLayout;
 
public class GoogleMapScreen extends MapActivity 
{    
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
            String coordinates[] = {"64.139889", "-21.915321"};
            double lat = Double.parseDouble(coordinates[0]);
            double lng = Double.parseDouble(coordinates[1]);
     
            p = new GeoPoint(
                (int) (lat * 1E6), 
                (int) (lng * 1E6));
     
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