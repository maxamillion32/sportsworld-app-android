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
import mx.com.sportsworld.sw.pojo.UserPojo;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.provider.SportsWorldContract;
import mx.com.sportsworld.sw.web.HttpPostClient;

/**
 * The Class ActiveRoutineTask.
 */
public class ActiveRoutineTask extends
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
	
	/** The usr pojo. */
	UserPojo usrPojo;

	/**
	 * Instantiates a new active routine task.
	 * 
	 * @param response
	 *            the response
	 */
	public ActiveRoutineTask(ResponseInterface response) {
		this.response = response;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected RoutinePojo doInBackground(RoutinePojo... params) {
		post = new HttpPostClient(Resource.URL_API_BASE + "/routine/save/");
		parser = new JsonParser();

		for (RoutinePojo pojo : params) {
			post.postData("user_id", pojo.getId_user());
			post.postData("routine_id", pojo.getIdRoutine());
			pojo.setJson(post.postExecute(mContext));

			if (pojo.getJson().equals("TimeOut")) {
				pojo = new RoutinePojo();
				pojo.setStatus(false);
				pojo.setMessage("TimeOut");
				break;
			}

			pojoRes = parser.parseJson(pojo);

			usrPojo = new UserPojo();
			usrPojo.setId_user(pojo.getId_user());
			usrPojo.setJson(pojoRes.getData());
			usrPojo = parser.parseSpecialJson(usrPojo);

			updateUser(usrPojo);

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

	// Actualizamos el username

	/**
	 * Update user.
	 * 
	 * @param pojo
	 *            the pojo
	 */
	public void updateUser(UserPojo pojo) {
		final ContentValues values = new ContentValues();
		values.put(SportsWorldContract.User.ROUTINE_ID, pojo.getmRoutineId());
		SportsWorldPreferences.setRoutineId(mContext, pojo.getmRoutineId());
		final ContentResolver resolver = mContext.getContentResolver();
		String where = SportsWorldContract.User._ID + "=?";
		String[] whereArgs = new String[] { String.valueOf(pojo.getId_user()) };

		int res = resolver.update(
				SportsWorldContract.User.buildUserUri(pojo.getId_user()),
				values, where, whereArgs);

		Log.i("LogIron", res + "");
	}

}
