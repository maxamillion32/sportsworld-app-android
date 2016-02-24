package mx.com.sportsworld.sw.web.task;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.app.Activity;
import android.os.AsyncTask;

import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.json.JsonParser;
import mx.com.sportsworld.sw.net.resource.Resource;
import mx.com.sportsworld.sw.parser.AwardProfileParser;
import mx.com.sportsworld.sw.pojo.AwardProfilePojo;
import mx.com.sportsworld.sw.web.HttpGetClient;

/**
 * The Class AwardProfileTask.
 */
public class AwardProfileTask extends
		AsyncTask<AwardProfilePojo, Void, AwardProfilePojo> {

	/** The http client. */
	HttpGetClient httpClient = null;
	
	/** The response. */
	ResponseInterface response = null;
	
	/** The parser. */
	JsonParser parser;
	
	/** The ctx. */
	Activity ctx;
	
	/** The result pojo. */
	AwardProfilePojo resultPojo;
	
	/** The url. */
	String url;
	
	/** The profile parser. */
	AwardProfileParser profileParser;

	/**
	 * Instantiates a new award profile task.
	 * 
	 * @param response
	 *            the response
	 * @param act
	 *            the act
	 */
	public AwardProfileTask(ResponseInterface response, Activity act) {
		this.response = response;
		ctx = act;
	}

	
	// Consultamos los puntos del usuario
	
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected AwardProfilePojo doInBackground(AwardProfilePojo... arg0) {
		resultPojo = new AwardProfilePojo();
		profileParser = new AwardProfileParser();
		parser= new JsonParser();
		url = Resource.URL_API_BASE + "/loyalty/user/points/";

		for (AwardProfilePojo pojo : arg0) {
			url += pojo.getId_user();
			httpClient = new HttpGetClient(url, ctx);
			pojo.setJson(httpClient.excuteGet());

			if (pojo.getJson().equals("TimeOut")) {
				resultPojo.setStatus(false);
				resultPojo.setMessage("TimeOut");
				break;
			}

			pojo = parser.parseJson(pojo);

			if (!pojo.isStatus()) {
				resultPojo.setStatus(false);
				resultPojo.setMessage("TimeOut");
				break;
			}
			resultPojo = profileParser.parse(pojo.getData());
			resultPojo.setStatus(true);
		}
		return resultPojo;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(AwardProfilePojo result) {
		response.onResultResponse(result);
	}

}
