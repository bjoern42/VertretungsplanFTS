package MyApps.VertretungsplanFTSMobile.listener;

import java.util.List;

import MyApps.VertretungsplanFTSMobile.model.TimeTable;

public interface TableListener {
	public void onSuccess(List<TimeTable> tables);
	public void onFailure(Throwable e);
}
