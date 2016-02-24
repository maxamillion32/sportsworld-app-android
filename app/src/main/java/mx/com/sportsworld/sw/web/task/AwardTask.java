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
import mx.com.sportsworld.sw.parser.AwardParser;
import mx.com.sportsworld.sw.pojo.AwardPojo;
import mx.com.sportsworld.sw.web.HttpGetClient;

/**
 * The Class AwardTask.
 */
public class AwardTask extends AsyncTask<AwardPojo, Void, AwardPojo> {

	/** The http client. */
	HttpGetClient httpClient = null;
	
	/** The response. */
	ResponseInterface response = null;
	
	/** The parser. */
	JsonParser parser;
	
	/** The ctx. */
	Activity ctx;
	
	/** The result pojo. */
	AwardPojo resultPojo;
	
	/** The url. */
	String url;
	
	/** The award parser. */
	AwardParser awardParser;

	/**
	 * Instantiates a new award task.
	 * 
	 * @param response
	 *            the response
	 * @param act
	 *            the act
	 */
	public AwardTask(ResponseInterface response, Activity act) {
		this.response = response;
		ctx=act;
	}

	//Consultamos las recompenzas
	
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected AwardPojo doInBackground(AwardPojo... arg0) {
		url = Resource.URL_API_BASE + "/loyalty/rewards/";
		awardParser = new AwardParser();
		resultPojo = new AwardPojo();
		parser= new JsonParser(); 
		for (AwardPojo pojo : arg0) {

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

			resultPojo = awardParser.parse(pojo.getData());
			resultPojo.setStatus(true);
		}

		return resultPojo;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(AwardPojo result) {
		response.onResultResponse(result);
	}

}
