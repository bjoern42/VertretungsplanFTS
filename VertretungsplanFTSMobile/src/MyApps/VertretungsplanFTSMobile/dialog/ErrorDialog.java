package MyApps.VertretungsplanFTSMobile.dialog;

import MyApps.VertretungsplanFTSMobile.R;
import MyApps.VertretungsplanFTSMobile.listener.ErrorDialogListener;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class ErrorDialog extends AlertDialog.Builder implements OnClickListener{
	private ErrorDialogListener listener;
	private AlertDialog dialog;
	
	public ErrorDialog(Context context) {
		super(context);
		setTitle(R.string.error_dialog_title);
		setNegativeButton(R.string.error_dialog_bt_retry_label, this);
		setPositiveButton(R.string.error_dialog_bt_settings_label, this);
	}

	public void setErrorDialogListener(ErrorDialogListener pListener){
		listener = pListener;
	}
	
	public void show(String text){
		setMessage(text);
		dialog = create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	@Override
	public void onClick(DialogInterface arg0, int which) {
		int button;
		if(which == AlertDialog.BUTTON_NEGATIVE){
			button = ErrorDialogListener.BUTTON_RETRY;
		}else{
			button = ErrorDialogListener.BUTTON_SETTINGS;
		}
		listener.onButtonClick(button);
	}
}
