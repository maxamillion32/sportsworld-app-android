package mx.com.sportsworld.sw.web.task;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.Context;
import android.os.AsyncTask;

import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.json.CheckRoutineJsonParser;
import mx.com.sportsworld.sw.json.JsonParser;
import mx.com.sportsworld.sw.net.resource.Resource;
import mx.com.sportsworld.sw.pojo.RoutinePojo;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.web.HttpGetClient;

/**
 * The Class CheckRoutineTask.
 */
public class CheckRoutineTask extends AsyncTask<String, Void, RoutinePojo> {

	/** The post. */
	HttpGetClient post = null;
	
	/** The result. */
	String result = "";
	
	/** The parser. */
	JsonParser parser;
	
	/** The pojo res. */
	RoutinePojo pojoRes = null;
	
	/** The response. */
	ResponseInterface response = null;
	
	/** The m context. */
	public static Context mContext;
	
	/** The json parser. */
	CheckRoutineJsonParser jsonParser;

	/**
	 * Instantiates a new check routine task.
	 * 
	 * @param response
	 *            the response
	 */
	public CheckRoutineTask(ResponseInterface response) {
		this.response = response;
	}

	// Revisamos un d�a espec�fico de la rutina
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected RoutinePojo doInBackground(String... arg0) {

		jsonParser = new CheckRoutineJsonParser();
		// TODO Auto-generated method stub
		post = new HttpGetClient(Resource.URL_API_BASE + "/routines/check/"
				+ arg0[0], mContext);
		parser = new JsonParser();
		pojoRes = new RoutinePojo();
		pojoRes.setJson(post.excuteGet());

		if (pojoRes.getJson().equals("TimeOut")) {
			pojoRes.setStatus(false);
			pojoRes.setMessage("TimeOut");
			return pojoRes;
		}

		pojoRes = parser.parseJson(pojoRes);

		int routineId = jsonParser.parseWDJson(pojoRes);
		if (SportsWorldPreferences.getRoutineId(mContext) != routineId) {
			SportsWorldPreferences.setRoutineId(mContext, routineId);

			pojoRes.setIdRoutine(routineId + "");
		} else
			pojoRes.setIdRoutine(SportsWorldPreferences.getRoutineId(mContext)
					+ "");

		return pojoRes;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(RoutinePojo result) {
		response.onResultResponse(result);
	}

}
