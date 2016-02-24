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
import mx.com.sportsworld.sw.parser.AwardDirectionParser;
import mx.com.sportsworld.sw.pojo.BranchItemPojo;
import mx.com.sportsworld.sw.web.HttpGetClient;

/**
 * The Class BranchTask.
 */
public class BranchTask extends AsyncTask<BranchItemPojo, Void, BranchItemPojo> {

	/** The http client. */
	HttpGetClient httpClient = null;
	
	/** The response. */
	ResponseInterface response = null;
	
	/** The parser. */
	JsonParser parser = null;
	
	/** The result pojo. */
	BranchItemPojo resultPojo;
	
	/** The url. */
	String url;
	
	/** The direction parser. */
	AwardDirectionParser directionParser;
	
	/** The act. */
	Activity act;

	/**
	 * Instantiates a new branch task.
	 * 
	 * @param response
	 *            the response
	 * @param ctx
	 *            the ctx
	 */
	public BranchTask(ResponseInterface response, Activity ctx) {
		this.response = response;
		act = ctx;
	}

	//Obtenemos la direcci�n del club
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected BranchItemPojo doInBackground(BranchItemPojo... arg0) {
		// TODO Auto-generated method stub
		resultPojo = new BranchItemPojo();
		directionParser = new AwardDirectionParser();
		parser = new JsonParser();
		for (BranchItemPojo pojo : arg0) {
			createUrl(pojo);
			httpClient = new HttpGetClient(url, act);
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
			resultPojo = directionParser.parse(pojo.getData());
			resultPojo.setStatus(true);
		}
		return resultPojo;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(BranchItemPojo result) {
		// TODO Auto-generated method stub
		response.onResultResponse(result);
	}

	/**
	 * Creates the url.
	 * 
	 * @param pojo
	 *            the pojo
	 */
	public void createUrl(BranchItemPojo pojo) {
		url = Resource.URL_API_BASE + "/club/details_upster/";
		url += pojo.getId_club() + "/";
		url += pojo.getId_user() + "/";

	}
}
