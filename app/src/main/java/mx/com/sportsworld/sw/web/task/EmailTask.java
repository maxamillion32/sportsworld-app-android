package mx.com.sportsworld.sw.web.task;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.json.JsonParser;
import mx.com.sportsworld.sw.net.resource.Resource;
import mx.com.sportsworld.sw.pojo.EmailPojo;
import mx.com.sportsworld.sw.web.HttpGetClient;

import android.content.Context;
import android.os.AsyncTask;

/**
 * The Class EmailTask.
 */
public class EmailTask extends AsyncTask<EmailPojo, Void, EmailPojo> {

	/** The response. */
	ResponseInterface response = null;
	
	/** The parser. */
	JsonParser parser;
	
	/** The ctx. */
	public static Context ctx = null;
	
	/** The http client. */
	HttpGetClient httpClient = null;
	
	/** The data. */
	String data = "";

	/**
	 * Instantiates a new email task.
	 * 
	 * @param response
	 *            the response
	 */
	public EmailTask(ResponseInterface response) {
		this.response = response;
	}

	// C�digo para env�ar email
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected EmailPojo doInBackground(EmailPojo... emailPojo) {
		// TODO Auto-generated method stub

		parser = new JsonParser();
		EmailPojo result = null;

		for (EmailPojo pojo : emailPojo) {
			String url = buildUrl(pojo);
			httpClient = new HttpGetClient(url, ctx);
			pojo.setJson(httpClient.excuteGet());
			result = (EmailPojo) parser.parseJson(pojo);

		}
		return result;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(EmailPojo result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	/**
	 * Builds the url.
	 * 
	 * @param pojo
	 *            the pojo
	 * @return the string
	 */
	public String buildUrl(EmailPojo pojo) {
		String url = Resource.URL_API_BASE + "/export/routine/";
		url += pojo.getId_user() + "/" + pojo.getMemunic_id() + "/";

		return url;

	}

}
