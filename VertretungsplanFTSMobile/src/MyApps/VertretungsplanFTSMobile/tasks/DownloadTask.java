package MyApps.VertretungsplanFTSMobile.tasks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import MyApps.VertretungsplanFTSMobile.listener.DownloadListener;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadTask extends AsyncTask<String, Integer, List<String>>{
	private static final String TAG = "DownloadTask";
	private DownloadListener listener;

	public DownloadTask(DownloadListener pListener){
		listener = pListener;
	}
	
	/**
	 * Laedt den Vertretungsplan vom Server runter.
	 * @param server - Server mit Pfad zum Vertretungsplan
	 * @param username - Benutzername
	 * @param password - Passwort
	 * @return String[] - ungefilterter Vertretungsplan
	 */
	private List<String> download(String server,String username,String password){
		Log.i(TAG, "downloading: "+server);
		ArrayList<String> data = new ArrayList<String>();
		String buffer="";
		try{
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(server);
			HttpResponse response = client.execute(get);
	        HttpEntity entity = response.getEntity();
	        HttpPost post = new HttpPost(server);
	        client.getCredentialsProvider().setCredentials(new AuthScope(null, -1),new UsernamePasswordCredentials(username,password));
	        response = client.execute(post);
	        entity = response.getEntity();
			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"));
			while((buffer=reader.readLine()) != null){
//				Log.e(TAG, "input: "+buffer);
				data.add(buffer);
			}

			return data;
		}catch(Exception e){
//			Log.e("error Verbindungsaufbau",e.toString());
			listener.onFailure(e);
			return null;
		}
	}
	
	@Override
	protected List<String> doInBackground(String... params) {
		return download(params[0],params[1],params[2]);
	}
	
	@Override
	protected void onPostExecute(List<String> data) {
		if(data != null){
			listener.onSuccess(data);
		}
    }
}
