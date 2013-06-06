package MyApps.VertretungsplanFTSMobile.view;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.http.auth.InvalidCredentialsException;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import MyApps.VertretungsplanFTSMobile.R;
import MyApps.VertretungsplanFTSMobile.adapter.TableAdapter;
import MyApps.VertretungsplanFTSMobile.dialog.ErrorDialog;
import MyApps.VertretungsplanFTSMobile.handler.TableHandler;
import MyApps.VertretungsplanFTSMobile.helper.Constants;
import MyApps.VertretungsplanFTSMobile.listener.ErrorDialogListener;
import MyApps.VertretungsplanFTSMobile.listener.TableListener;
import MyApps.VertretungsplanFTSMobile.model.TimeTable;
import MyApps.VertretungsplanFTSMobile.model.TimeTableRow;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;

public class ActivityTimeTable extends SherlockActivity implements TableListener, ErrorDialogListener{
	private final String TAG = "ActivityTimeTable";
	
	private TableHandler tableHandler;
	private TableLayout table;
	private TableAdapter adapter;
	private ErrorDialog dialog;
	
	private static List<TimeTable> tables;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_table);
    	tableHandler = new TableHandler();
    	table = (TableLayout)findViewById(R.id.uniScrollTableLayout);
    	adapter = new TableAdapter(this, table);
    	dialog = new ErrorDialog(this);
    	dialog.setErrorDialogListener(this);
    	
    	if(savedInstanceState == null || tables == null){
    		ActivitySettings.load(getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE));
        	update();
    	}else{
    		adapter.update(filterTimeTables(tables, ActivitySettings.getGrade()));
    	}
//    	"plan", "qpwoei"
    }
    
	@Override
	public void onSuccess(final List<TimeTable> pTables) {
		if(pTables.size() > 0){
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					tables = pTables;
					setProgressBarVisibility(View.INVISIBLE);
					Log.i(TAG,""+tables.size()+" tables successfully downloaded!");
					adapter.update(filterTimeTables(tables, ActivitySettings.getGrade()));
				}
			});
		}else{
			onFailure(new Exception(getString(R.string.error_table_size_zero)));
		}
	}
	
	@Override
	public void onFailure(final Throwable e) {
		Log.e(TAG,"failure "+e);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				setProgressBarVisibility(View.INVISIBLE);
				String error;
				if(e instanceof InvalidCredentialsException){
					error = getResources().getString(R.string.error_dialog_invalid_login);
				}else if(e instanceof UnknownHostException){
					error = getResources().getString(R.string.error_dialog_unknown_host);
				}else{
					error = e.toString();
				}
		
				dialog.show(error);
			}
		});
	}
	
	private List<TimeTable> filterTimeTables(List<TimeTable> pTables, String grade){
		List<TimeTable> filteredTables = new LinkedList<TimeTable>();
		for(TimeTable table:pTables){
			//Check if timeTable is in the past
			try {
				Date date = table.getParsedDate();
				Date currentDate = Calendar.getInstance(Locale.GERMAN).getTime();
				if(date != null && date.before(currentDate)){
					continue;
				}
			} catch (ParseException e) {}
			
			//no grade selected
			if(grade.equals("")){
				filteredTables.add(table);
				continue;
			}
			//check for selected grade
			boolean checked = false;
			TimeTable filteredTable = new TimeTable();
			filteredTable.setHeader(table.getHeader());
			filteredTable.setDate(table.getDate());
			
			for(TimeTableRow row:table.getTableRows()){
				if(row.getGrades().toLowerCase(Locale.GERMAN).contains(grade.toLowerCase(Locale.GERMAN))){
					checked = true;
				}else if(checked && !row.getGrades().equals("")){
					checked = false;
				}
				
				if(checked){
					filteredTable.addTableRow(row);
				}
			}
			if(filteredTable.getTableRows().size() > 0){
				filteredTables.add(filteredTable);
			}
		}
		return filteredTables;
	}
	
	private void update(){
		table.removeAllViews();
		String user = ActivitySettings.getUser();
    	String password = ActivitySettings.getPassword();
    	
    	if(user.equals("") || password.equals("")){
    		showSettingsActivity();
    	}else{
    		downloadTables(user, password);
    	}
	}
	
	private void downloadTables(String user, String password){
		setProgressBarVisibility(View.VISIBLE);
		tableHandler.downloadTables(this, Constants.URL_PREFIX, Constants.URL_SUFFIX, user, password);
	}
	
	private void setProgressBarVisibility(final int visibility){
		findViewById(R.id.progressBar).setVisibility(visibility);
	}
	
	private void showSettingsActivity(){
		Intent intentSettings = new Intent(this,ActivitySettings.class);
		startActivityForResult(intentSettings, 0);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode == Constants.RESULT_UPDATE || tables == null){
			update();
		}else{
			adapter.update(filterTimeTables(tables, ActivitySettings.getGrade()));
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.menu_activity_table, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.menu_settings:
	        	showSettingsActivity();
	            break;
	        case R.id.menu_reload:
	        	update();
	        	break;
	        case R.id.menu_hompage:
	        	Uri uri = Uri.parse(Constants.HOMEPAGE);
				Intent intentHomepage = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intentHomepage); 
	        	break;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onButtonClick(int button) {
		if(button == ErrorDialogListener.BUTTON_RETRY){
			update();
		}else{
			showSettingsActivity();
		}
	}
}
