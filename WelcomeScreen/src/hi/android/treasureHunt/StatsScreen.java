package hi.android.treasureHunt;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class StatsScreen extends Activity {
    
	private static final int ADVANCED_ID = Menu.FIRST;
	private static final int HELP_ID = Menu.FIRST + 1;
	private static final int SETTINGS_ID = Menu.FIRST + 2;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.statistics_screen);
        
        TableLayout tl = (TableLayout) findViewById(R.id.tableLayoutStatisticsScreen);
        
        TableRow tr = new TableRow(this);
        
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
        
        
        TextView twName = new TextView(this);
        twName.setText("Player Name");
        twName.setLayoutParams(new LayoutParams(
        		LayoutParams.WRAP_CONTENT,
                  LayoutParams.WRAP_CONTENT));

        TextView twScore = new TextView(this);
        twScore.setText("50.000");
        twScore.setLayoutParams(new LayoutParams(
        		LayoutParams.WRAP_CONTENT,
                  LayoutParams.WRAP_CONTENT));
        
        TextView twRanking = new TextView(this);
        twRanking.setText("1.200");
        twRanking.setLayoutParams(new LayoutParams(
        		LayoutParams.WRAP_CONTENT,
                  LayoutParams.WRAP_CONTENT));
        
           
        /* Add TextViews to row. */
        tr.addView(twName);
        tr.addView(twScore);
        tr.addView(twRanking);
        
        /* Add row to TableLayout. */
        tl.addView(tr,new TableLayout.LayoutParams(
        LayoutParams.FILL_PARENT,
        LayoutParams.WRAP_CONTENT));
    }
    
    @Override 
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	menu.add(0,ADVANCED_ID,0,R.string.menuAdvanced);
    	menu.add(0,HELP_ID,0,R.string.menuHelp);
    	menu.add(0,SETTINGS_ID,0,R.string.menuSettings);
    	return true;
    }
}