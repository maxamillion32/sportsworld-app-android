package mx.com.sportsworld.sw.web.task;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.json.JsonParser;
import mx.com.sportsworld.sw.net.resource.Resource;
import mx.com.sportsworld.sw.pojo.RoutinePojo;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.provider.SportsWorldContract;
import mx.com.sportsworld.sw.web.HttpPostClient;

/**
 * The Class UpdateRoutineTask.
 */
public class UpdateRoutineTask extends
		AsyncTask<RoutinePojo, Void, RoutinePojo> {

	/** The post. */
	HttpPostClient post = null;
	
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

	/**
	 * Instantiates a new update routine task.
	 * 
	 * @param response
	 *            the response
	 */
	public UpdateRoutineTask(ResponseInterface response) {
		this.response = response;
	}

	// Obtenemos el status de la rutina
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected RoutinePojo doInBackground(RoutinePojo... arg0) {
		// TODO Auto-generated method stub

		post = new HttpPostClient(Resource.URL_API_BASE
				+ "/routine/status/update/");
		parser = new JsonParser();

		for (RoutinePojo pojo : arg0) {
			post.postData("user_id", pojo.getId_user());
			post.postData("routine_id", pojo.getIdRoutine());
			post.postData("status_id", "4");
			pojo.setJson(post.postExecute(mContext));
			pojoRes = parser.parseJson(pojo);

			if (pojoRes.isStatus())
				updateUser(pojo.getId_user());

		}

		return pojoRes;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(RoutinePojo result) {
		restartWeekDay();
		response.onResultResponse(result);
	}

	/**
	 * Update user.
	 * 
	 * @param user
	 *            the user
	 */
	public void updateUser(String user) {
		final ContentValues values = new ContentValues();
		values.put(SportsWorldContract.User.ROUTINE_ID, "-1");
		values.put(SportsWorldContract.User.ROUTINE_ID, "-1");

		final ContentResolver resolver = mContext.getContentResolver();
		String where = SportsWorldContract.User._ID + "=?";
		String[] whereArgs = new String[] { String.valueOf(user) };

		int res = resolver.update(SportsWorldContract.User.buildUserUri(user),
				values, where, whereArgs);

		Log.i("LogIron", res + "");
		SportsWorldPreferences.setCurrentWeekIdDayId(mContext, 0, 0);
	}

	/**
	 * Restart week day.
	 */
	public void restartWeekDay() {
		SportsWorldPreferences.setRoutineDay(mContext, "1");
		SportsWorldPreferences.setRoutineWeek(mContext, "1");
	}
}
