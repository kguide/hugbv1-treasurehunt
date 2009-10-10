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
        
        addUserToTableLayout(tl, "Player name", 50000, 1200);
    }
    
    @Override 
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	menu.add(0,ADVANCED_ID,0,R.string.menuAdvanced);
    	menu.add(0,HELP_ID,0,R.string.menuHelp);
    	menu.add(0,SETTINGS_ID,0,R.string.menuSettings);
    	return true;
    }
    
    /**
     * Adds a user to the tableLayout.
     * 
     * Note: This method should be transfered to the Control part of this project once it is established.
     * 
     * @param tableLayout : TableLayout. The table layout that the user is going to be added to
     * @param name        : String. Name of user
     * @param score		  : Integer. Users score
     * @param ranking     : Integer. Users ranking
     */
    public void addUserToTableLayout(TableLayout tableLayout, String name, int score, int ranking){
        
    	TableRow tr = new TableRow(this);
        
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
        
        
        TextView twName = new TextView(this);
        twName.setText(name);
        twName.setLayoutParams(new LayoutParams(
        		LayoutParams.WRAP_CONTENT,
                  LayoutParams.WRAP_CONTENT));

        TextView twScore = new TextView(this);
        twScore.setText(Integer.toString(score));
        twScore.setLayoutParams(new LayoutParams(
        		LayoutParams.WRAP_CONTENT,
                  LayoutParams.WRAP_CONTENT));
        
        TextView twRanking = new TextView(this);
        twRanking.setText(Integer.toString(ranking));
        twRanking.setLayoutParams(new LayoutParams(
        		LayoutParams.WRAP_CONTENT,
                  LayoutParams.WRAP_CONTENT));
        
           
        /* Add TextViews to row. */
        tr.addView(twName);
        tr.addView(twScore);
        tr.addView(twRanking);
        
        /* Add row to TableLayout. */
        tableLayout.addView(tr,new TableLayout.LayoutParams(
        LayoutParams.FILL_PARENT,
        LayoutParams.WRAP_CONTENT));
    }
}