package mx.com.sportsworld.sw.web.task;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.Context;
import android.os.AsyncTask;

import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.json.RoutineJsonParser;
import mx.com.sportsworld.sw.net.resource.Resource;
import mx.com.sportsworld.sw.pojo.RoutinePojo;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.web.HttpPostClient;

/**
 * The Class NewRoutineTask.
 */
public class NewRoutineTask extends AsyncTask<RoutinePojo, Void, RoutinePojo> {

	/** The post. */
	HttpPostClient post = null;
	
	/** The result. */
	String result = "";
	
	/** The pojo res. */
	RoutinePojo pojoRes = null;
	
	/** The routine parser. */
	RoutineJsonParser routineParser = null;
	
	/** The response. */
	ResponseInterface response = null;
	
	/** The m context. */
	public static Context mContext;

	/**
	 * Instantiates a new new routine task.
	 * 
	 * @param response
	 *            the response
	 */
	public NewRoutineTask(ResponseInterface response) {
		this.response = response;
	}

	// Completamos el d�a y obtenemos la siguiente rutina

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected RoutinePojo doInBackground(RoutinePojo... arg0) {
		// TODO Auto-generated method stub

		post = new HttpPostClient(Resource.URL_API_BASE
				+ "/routine/week/day/update/");

		routineParser = new RoutineJsonParser();

		for (RoutinePojo pojo : arg0) {
			post.postData("user_id", pojo.getId_user());
			post.postData("routine_id", pojo.getIdRoutine());
			post.postData("week", pojo.getWeek_id());
			post.postData("day", pojo.getDay_id());
			pojo.setJson(post.postExecute(mContext));

			if (pojo.getJson().equals("TimeOut")) {
				pojoRes = new RoutinePojo();
				pojoRes.setStatus(false);
				pojoRes.setMessage("TimeOut");
				break;
			}
			pojoRes = routineParser.parseJson(pojo);
			pojoRes = routineParser.parseWDJson(pojoRes);
			if (pojoRes.isStatus())
				saveNewDays(pojoRes);
		}

		return pojoRes;

	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(RoutinePojo result) {
		response.onResultResponse(result);
	}

	/**
	 * Save new days.
	 * 
	 * @param pojo
	 *            the pojo
	 */
	public void saveNewDays(RoutinePojo pojo) {

		int day = Integer.parseInt(SportsWorldPreferences
				.getRoutinetDay(mContext)) + 1;
		int week = Integer.parseInt(SportsWorldPreferences
				.getRoutineWeek(mContext));

		if (day > 5) {
			day = 1;
			week += 1;
		}

		SportsWorldPreferences.setRoutineDay(mContext, day + "");
		SportsWorldPreferences.setRoutineWeek(mContext, week + "");

	}

}
