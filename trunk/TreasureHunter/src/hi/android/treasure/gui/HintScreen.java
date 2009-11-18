package hi.android.treasure.gui;

import hi.android.treasure.control.Controller;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.widget.TextView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import java.lang.System;

public class HintScreen extends Activity {
    
    public static final int MAP_ID = Menu.FIRST;
    public  static final int CURRENT_ID = Menu.FIRST+1;
    
    private Button prevHintButton;
    private Button nextHintButton;

    private TextView hintNr;

    Controller controller = Controller.getInstance();  
    Context context = this;

    
    
      
    @Override
	public void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	setContentView(R.layout.show_hint);
	updateView();



    	prevHintButton = (Button) findViewById(R.id.backHint);
	prevHintButton.setOnClickListener(new Button.OnClickListener() {	
		@Override
		    public void onClick(View v) {
		    controller.game.setPrevHintView();
			updateView();
		    
		}
	    });	
	
	nextHintButton = (Button) findViewById(R.id.nextHint);
	nextHintButton.setOnClickListener(new Button.OnClickListener() {	
		@Override
		    public void onClick(View v) {
		    controller.game.setNextHintView();
			updateView();
		    
		}
	    });



    }
    
    public void updateView() {

	TextView myTextBox = (TextView) findViewById(R.id.hintTexti);
	myTextBox.setText(controller.getCurrentHint());
	hintNr = (TextView) findViewById(R.id.hintNr);
	hintNr.setText("Hint Nr: " +  ((String)Integer.toString(controller.game.getCurrentHintViewNr()+1)));
	//controller.game.setToLast();
    }
 
    @Override 
	public boolean onCreateOptionsMenu(Menu menu){
	boolean result = super.onCreateOptionsMenu(menu);
    	menu.add(0,MAP_ID,0,R.string.showHintOnMap);
    	menu.add(0,CURRENT_ID,0,R.string.showCurrentHint);
    	return result;
    }


    
   
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case MAP_ID:
	    Intent hintMap = new Intent(HintScreen.this,GoogleMapScreen.class);
	    double posList[] = new double[2];
	    posList[0] = controller.game.getCurrentCoordinateView().getLatitude();
	    posList[1] = controller.game.getCurrentCoordinateView().getLongitude();
	    System.out.println(posList[0] + " " + posList[1]);
	    hintMap.putExtra("myLocation",posList);
	    startActivity(hintMap);
	    break;
	case CURRENT_ID:
	    controller.game.setViewToCurrent();
	    updateView();
	    break;
	default : 
	    return super.onOptionsItemSelected(item);
	}
	return true; /* true means: "we handled the event". */
    } 
    
    
}



