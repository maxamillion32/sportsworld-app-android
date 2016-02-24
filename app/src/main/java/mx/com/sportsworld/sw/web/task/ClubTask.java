/**
 * 
 */
package mx.com.sportsworld.sw.web.task;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;

import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.json.JsonParser;
import mx.com.sportsworld.sw.net.resource.Resource;
import mx.com.sportsworld.sw.parser.ClubParser;
import mx.com.sportsworld.sw.pojo.BranchItemPojo;
import mx.com.sportsworld.sw.pojo.BranchPojo;
import mx.com.sportsworld.sw.web.HttpGetClient;

/**
 * The Class ClubTask.
 * 
 * @author Jose Torres Fuentes 10/09/2013 Ironbit
 */
public class ClubTask extends AsyncTask<BranchPojo, Void, BranchPojo> {

	/** The url. */
	String url = Resource.URL_API_BASE + "/clubsupster/";
	
	/** The http client. */
	HttpGetClient httpClient = null;
	
	/** The response. */
	ResponseInterface response = null;
	
	/** The parser. */
	JsonParser parser;
	
	/** The ctx. */
	Activity ctx;
	
	/** The result clubs. */
	List<BranchItemPojo> resultClubs;
	
	/** The result pojo. */
	BranchPojo resultPojo;
	
	/** The club parser. */
	ClubParser clubParser;

	/**
	 * Instantiates a new club task.
	 * 
	 * @param response
	 *            the response
	 * @param act
	 *            the act
	 */
	public ClubTask(ResponseInterface response, Activity act) {
		this.response = response;
		ctx = act;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected BranchPojo doInBackground(BranchPojo... params) {
		// TODO Auto-generated method stub
		resultClubs = new ArrayList<BranchItemPojo>();
		parser = new JsonParser();
		resultPojo = new BranchPojo();
		clubParser = new ClubParser();

		for (BranchPojo pojo : params) {
			url += pojo.getmLatitude() + "/" + pojo.getmLongitude() + "/0/";
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
			resultPojo = clubParser.parse(pojo.getData());
			resultPojo.setStatus(true);

		}
		return resultPojo;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(BranchPojo result) {
		// TODO Auto-generated method stub
		response.onResultResponse(result);
	}
}
