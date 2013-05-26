package MyApps.VertretungsplanFTSMobile.listener;

import java.util.List;

public interface DownloadListener {
	public void onSuccess(List<String> html);
	public void onFailure(Throwable e);
}
