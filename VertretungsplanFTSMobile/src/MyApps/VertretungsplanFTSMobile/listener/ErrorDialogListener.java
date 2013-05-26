package MyApps.VertretungsplanFTSMobile.listener;

public interface ErrorDialogListener {
	public final int BUTTON_RETRY = 1;
	public final int BUTTON_SETTINGS = 2;
	
	public void onButtonClick(int button);
}
