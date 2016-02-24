package mx.com.sportsworld.sw.web;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.io.BufferedReader;
import java.net.HttpURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;

/**
 * The Class HttpGetClient.
 */
public class HttpGetClient {
	
	/** The url. */
	String url = "";
	
	/** The data get. */
	String dataGet = "";
	
	/** The reader. */
	BufferedReader reader = null;
	
	/** The conn. */
	HttpURLConnection conn = null;
	
	/** The m context. */
	public static Context mContext;
	
	/** The wait time. */
	public int waitTime=20000;

	/**
	 * Instantiates a new http get client.
	 * 
	 * @param url
	 *            the url
	 * @param context
	 *            the context
	 */
	public HttpGetClient(String url, Context context) {
		this.url = url;
		mContext = context;
	}

	/**
	 * Excute get.
	 * 
	 * @return the string
	 */
	public String excuteGet() {
		String result = "";
		String resultUTF8 = "";

		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, waitTime);
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		HttpGet httpGet = new HttpGet(url);
		Log.i("LogIron", url);
		
		if (SportsWorldPreferences.getAuthToken(mContext) == null)
			httpGet.addHeader("secret_key",
					"02b7f1c16c25c3d156756163a8d1f6db4da2ff34");
		else
			httpGet.addHeader("secret_key",
					SportsWorldPreferences.getAuthToken(mContext));
		try {
			HttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			result = "TimeOut";
		}
		Log.i("LogIron", result);
		try {
			resultUTF8 = new String(result.getBytes("UTF-8"), "UTF-8");
		} catch (Exception ex) {
			resultUTF8 = result;
		}
		return resultUTF8;
	}

}
