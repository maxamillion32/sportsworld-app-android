package mx.com.sportsworld.sw.web.task;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;

import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.json.JsonParser;
import mx.com.sportsworld.sw.net.resource.Resource;
import mx.com.sportsworld.sw.pojo.AwardItemPojo;
import mx.com.sportsworld.sw.pojo.AwardPojo;
import mx.com.sportsworld.sw.web.HttpPostClient;

/**
 * The Class ChangePointsTask.
 */
public class ChangePointsTask extends AsyncTask<AwardPojo, Void, AwardPojo> {
	
	/** The post. */
	HttpPostClient post = null;
	
	/** The result. */
	String result = "";
	
	/** The parser. */
	JsonParser parser;
	
	/** The result pojo. */
	AwardPojo resultPojo = null;
	
	/** The response. */
	ResponseInterface response = null;
	
	/** The ctx. */
	Activity ctx;
	
	/** The lis award items. */
	public List<AwardItemPojo> lisAwardItems = new ArrayList<AwardItemPojo>();

	/**
	 * Instantiates a new change points task.
	 * 
	 * @param response
	 *            the response
	 * @param act
	 *            the act
	 */
	public ChangePointsTask(ResponseInterface response, Activity act) {
		this.response = response;
		ctx = act;
	}

	// Canjeamos los puntos
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected AwardPojo doInBackground(AwardPojo... params) {
		// TODO Auto-generated method stub
		post = new HttpPostClient(Resource.URL_API_BASE + "/loyalty/redem/");
		parser = new JsonParser();
		resultPojo = new AwardPojo();

		for (AwardPojo awardPojo : params) {
			for (AwardItemPojo pojo : awardPojo.getItems()) {
				post.postData("user_id", awardPojo.getId_user());
				post.postData("reward_id", pojo.getIdLealtadPremios());
				post.postData("origin", "4");
				pojo.setJson(post.postExecute(ctx));

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
				if (pojo.getMessage().equals("Success Transaction"))
					lisAwardItems.add(pojo);
			}
		}
		resultPojo.setItems(lisAwardItems);
		return resultPojo;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(AwardPojo result) {
		// TODO Auto-generated method stub
		response.onResultResponse(result);
	}

}
