package MyApps.VertretungsplanFTSMobile.view;

import MyApps.VertretungsplanFTSMobile.R;
import MyApps.VertretungsplanFTSMobile.helper.Constants;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Klasse, die die Einstellungen verwaltet.<p>
 * @author Bjoern Eschle
 * @version 1.2
 */
public final class ActivitySettings extends Activity{
	private Editor editor;
	private EditText etGrade, etUser, etPassword;
	private static String grade, user, password;
	
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		editor = getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE).edit();
		
		etUser = (EditText) findViewById(R.id.editTextUsername);
		etPassword = (EditText) findViewById(R.id.editTextPassword);
		etGrade = (EditText) findViewById(R.id.editTextGrade);
		
		load(getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE));
		etUser.setText(getUser());
		etPassword.setText(getPassword());
		etGrade.setText(getGrade());
	}
	
	/**
	 * Laedt die Einstellungen.
	 * @param SharedPreferences - SharedPreferences von Einstellungen: "VertretungsplanFTSEinstellungen"
	 */
	public static void load(SharedPreferences preferences){
		setGrade(preferences.getString(Constants.EXTRA_GRADE, ""));
		setUser(preferences.getString(Constants.EXTRA_USERNAME, ""));
		setPassword(preferences.getString(Constants.EXTRA_PASSWORD, ""));
	}
	
	private boolean save(){
		boolean update = false;
		String grade = etGrade.getText().toString();
		String username = etUser.getText().toString();
		String password = etPassword.getText().toString();
		if(!getUser().equals(username)|| !getPassword().equals(password)){
			update = true;
		}
		setGrade(grade);
		setUser(username);
		setPassword(password);
		editor.putString(Constants.EXTRA_GRADE, getGrade());
		editor.putString(Constants.EXTRA_USERNAME, getUser());
		editor.putString(Constants.EXTRA_PASSWORD, getPassword());
		editor.commit();
		return update;
	}
	
	@Override
	public void onBackPressed(){
		finish();
	}

	public void onClickSave(View view){
		if(save()){
			setResult(Constants.RESULT_UPDATE);
		}
		finish();
	}
	
	public static String getGrade() {
		return grade;
	}
	private static void setGrade(String pGrade) {
		grade = pGrade;
	}
	public static String getUser() {
		return user;
	}
	private static void setUser(String pUser) {
		user = pUser;
	}
	public static String getPassword() {
		return password;
	}
	private static void setPassword(String pPassword) {
		password = pPassword;
	}
}
