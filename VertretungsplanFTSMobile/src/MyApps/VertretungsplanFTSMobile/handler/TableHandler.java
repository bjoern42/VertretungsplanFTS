package MyApps.VertretungsplanFTSMobile.handler;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.auth.InvalidCredentialsException;

import MyApps.VertretungsplanFTSMobile.listener.DownloadListener;
import MyApps.VertretungsplanFTSMobile.listener.TableListener;
import MyApps.VertretungsplanFTSMobile.model.TimeTable;
import MyApps.VertretungsplanFTSMobile.tasks.DownloadTask;

public class TableHandler implements DownloadListener{
	private final int DAYS = 4;
	
	private List<TimeTable> tables = new LinkedList<TimeTable>();
	private TableListener listener;
	private int counter;
	private DownloadTask tasks[] = new DownloadTask[DAYS];
	
	public void downloadTables(TableListener pListener, String urlPrefix, String urlSuffix, String username, String password){
		tables.clear();
		listener = pListener;
		for(int i=1; i<tasks.length; i++){
			tasks[i] = new DownloadTask(this);
			tasks[i].execute(urlPrefix+i+urlSuffix,username,password);
		}
	}
	
	@Override
	public void onSuccess(List<String> html) {
		TimeTable table = new TimeTable(html.iterator());
		if(!table.isInvalidLogin()){
			tables.add(table);
			checkIfCompleted();
		}else{
			cancelTasks();
			listener.onFailure(new InvalidCredentialsException());
		}
	}
	
	@Override
	public void onFailure(Throwable e) {
		cancelTasks();
		listener.onFailure(e);
	}
	
	private void cancelTasks(){
		for(int i=1; i<tasks.length; i++){
			tasks[i].cancel(true);
		}
	}
	
	private void checkIfCompleted(){
		counter++;
		if(counter >= tasks.length-1){
			Collections.sort(tables);
			listener.onSuccess(tables);
			counter = 0;
		}
	}
}
