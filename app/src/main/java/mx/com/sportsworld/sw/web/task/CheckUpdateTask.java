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
import mx.com.sportsworld.sw.pojo.DevicePojo;
import mx.com.sportsworld.sw.web.HttpPostClient;

/**
 * The Class CheckUpdateTask.
 */
public class CheckUpdateTask extends AsyncTask<DevicePojo, Void, DevicePojo> {

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
	DevicePojo pojo = null;

	/**
	 * Instantiates a new check update task.
	 * 
	 * @param response
	 *            the response
	 */
	public CheckUpdateTask(ResponseInterface response) {
		this.response = response;
	}

	// Revisamos si existe una versi�n nueva en tienda
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected DevicePojo doInBackground(DevicePojo... device) {
		pojo = new DevicePojo();
		post = new HttpPostClient(Resource.URL_API_BASE + "/version/");
		parser = new JsonParser();

		pojo.setJson(post.getExecute(mContext, device[0]));

		pojo = parser.parseJson(pojo);

		return pojo;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(DevicePojo result) {
		response.onResultResponse(result);
	}

}
