package mx.com.sportsworld.sw.web.task;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.Context;
import android.os.AsyncTask;

import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.json.JsonParser;
import mx.com.sportsworld.sw.net.resource.Resource;
import mx.com.sportsworld.sw.pojo.MainPojo;
import mx.com.sportsworld.sw.web.HttpPostClient;

/**
 * The Class CloseSessionTask.
 */
public class CloseSessionTask extends AsyncTask<String, Void, MainPojo> {

	/** The post. */
	HttpPostClient post = null;
	
	/** The result. */
	String result = "";
	
	/** The response. */
	ResponseInterface response = null;
	
	/** The m context. */
	public static Context mContext;
	
	/** The parser. */
	JsonParser parser;
	
	/** The pojo. */
	MainPojo pojo = null;

	/**
	 * Instantiates a new close session task.
	 * 
	 * @param response
	 *            the response
	 */
	public CloseSessionTask(ResponseInterface response) {
		this.response = response;
	}

	// Cierras sesi�n
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected MainPojo doInBackground(String... logOut) {

		post = new HttpPostClient(Resource.URL_API_BASE + "/session/out/");
		parser = new JsonParser();
		pojo = new MainPojo();
		pojo.setJson(post.getExecute(mContext));

		if (pojo.getJson().equals("TimeOut")) {
			pojo.setMessage("TimeOut");
			pojo.setStatus(false);
			return pojo;
		}

		pojo = parser.parseJson(pojo);

		return pojo;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(MainPojo result) {
		response.onResultResponse(result);
	}

}
