package mx.com.sportsworld.sw.web;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import android.content.Context;
import android.util.Log;

import mx.com.sportsworld.sw.pojo.DevicePojo;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;

/**
 * The Class HttpPostClient.
 */
public class HttpPostClient {
	
	/** The url post. */
	URL urlPost = null;
	
	/** The data post. */
	String dataPost = "";
	
	/** The first. */
	boolean first = true;
	
	/** The reader. */
	BufferedReader reader = null;
	
	/** The conn. */
	HttpURLConnection conn;
	
	/** The context. */
	Context context;
	
	/** The result ut f8. */
	String resultUTF8 = "";
	
	/** The wait time. */
	private int waitTime = 20000;

	/**
	 * Instantiates a new http post client.
	 * 
	 * @param url
	 *            the url
	 */
	public HttpPostClient(String url) {
		try {
			urlPost = new URL(url);
			Log.i("LogIron", url);
		} catch (MalformedURLException e) {
			Log.i("LogIron", "La Url esta mal");
		}
	}

	/**
	 * Post data.
	 * 
	 * @param id
	 *            the id
	 * @param value
	 *            the value
	 */
	public void postData(String id, String value) {
		try {
			if (first)
				dataPost = URLEncoder.encode(id, "UTF-8") + "="
						+ URLEncoder.encode(value, "UTF-8");
			else
				dataPost += "&" + URLEncoder.encode(id, "UTF-8") + "="
						+ URLEncoder.encode(value, "UTF-8");
			first = false;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Post execute.
	 * 
	 * @param header
	 *            the header
	 * @return the string
	 */
	public String postExecute(String header) {
		String text = "";
		try {

			conn = (HttpURLConnection) urlPost.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("Auth-Key", header.trim());
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			conn.setDoOutput(true);
			// conn.setConnectTimeout(waitTime);

			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));

			StringBuilder sb = new StringBuilder();
			String line = null;

			// Read Server Response
			while ((line = reader.readLine()) != null) {
				// Append server response in string
				sb.append(line);
			}

			text = sb.toString();

			Log.i("LogIron", text);
		} catch (java.net.SocketTimeoutException e) {
			text = "TimeOut";
		} catch (Exception e) {
			Log.i("LogIron", e.toString());
			text = "TimeOut";
			try {
				Log.i("LogIron", conn.getResponseCode() + "");
				Log.i("LogIron", conn.getResponseMessage() + "");
			} catch (Exception ex) {
				Log.i("LogIron", ex.toString());
			}
		} finally {

			if (conn != null) {
				conn.disconnect();
			}
		}

		try {
			resultUTF8 = new String(text.getBytes("UTF-8"), "UTF-8");
		} catch (Exception ex) {
			resultUTF8 = text;
		}
		return resultUTF8;

	}

	/**
	 * Post execute.
	 * 
	 * @param context
	 *            the context
	 * @return the string
	 */
	public String postExecute(Context context) {
		String text = "";
		try {

			conn = (HttpURLConnection) urlPost.openConnection();
			conn.setRequestProperty("secret_key",
					SportsWorldPreferences.getAuthToken(context));

			conn.setConnectTimeout(waitTime);
			conn.setDoOutput(true);

			OutputStreamWriter wr = new OutputStreamWriter(
					conn.getOutputStream());
			wr.write(dataPost);
			wr.flush();

			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));

			StringBuilder sb = new StringBuilder();
			String line = null;

			// Read Server Response
			while ((line = reader.readLine()) != null) {
				// Append server response in string
				sb.append(line);
			}

			text = sb.toString();

		} catch (java.net.SocketTimeoutException e) {
			text = "TimeOut";

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("LogIron", "error " + e.toString());
		}
		Log.i("LogIron", text);
		conn.disconnect();
		try {
			resultUTF8 = new String(text.getBytes("UTF-8"), "UTF-8");
		} catch (Exception ex) {
			resultUTF8 = text;
		}
		return resultUTF8;

	}

	/**
	 * Gets the execute.
	 * 
	 * @param context
	 *            the context
	 * @return the execute
	 */
	public String getExecute(Context context) {
		String text = "";
		String secretKey = "02b7f1c16c25c3d156756163a8d1f6db4da2ff34";
		try {

			if (SportsWorldPreferences.getAuthToken(context) != null)
				secretKey = SportsWorldPreferences.getAuthToken(context);

			conn = (HttpURLConnection) urlPost.openConnection();

			conn.setRequestProperty("secret_key", secretKey);

			conn.setRequestMethod("GET");

			conn.setConnectTimeout(waitTime);

			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));

			StringBuilder sb = new StringBuilder();
			String line = null;

			// Read Server Response
			while ((line = reader.readLine()) != null) {
				// Append server response in string
				sb.append(line);
			}

			text = sb.toString();

		} catch (java.net.SocketTimeoutException e) {
			text = "TimeOut";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("LogIron", "error " + e.toString());
		}
		Log.i("LogIron", text);
		try {
			resultUTF8 = new String(text.getBytes("UTF-8"), "UTF-8");
		} catch (Exception ex) {
			resultUTF8 = text;
		}
		return resultUTF8;

	}

	/**
	 * Gets the execute.
	 * 
	 * @param context
	 *            the context
	 * @param pojo
	 *            the pojo
	 * @return the execute
	 */
	public String getExecute(Context context, DevicePojo pojo) {
		String text = "";
		try {

			conn = (HttpURLConnection) urlPost.openConnection();
			conn.setRequestProperty("secret_key",
					"02b7f1c16c25c3d156756163a8d1f6db4da2ff34");
			conn.setRequestProperty("device", pojo.getDevice());
			conn.setRequestProperty("version", pojo.getVersion());

			conn.setRequestMethod("GET");

			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));

			StringBuilder sb = new StringBuilder();
			String line = null;

			// Read Server Response
			while ((line = reader.readLine()) != null) {
				// Append server response in string
				sb.append(line);
			}

			text = sb.toString();

			Log.i("LogIron", text);
			// } catch (java.net.SocketTimeoutException e) {
			// text = "TimeOut";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("LogIron", "error " + e.toString());
		}
		try {
			resultUTF8 = new String(text.getBytes("UTF-8"), "UTF-8");
		} catch (Exception ex) {
			resultUTF8 = text;
		}
		return resultUTF8;

	}

}
