package mx.com.sportsworld.sw.web.task;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.json.JsonParser;
import mx.com.sportsworld.sw.model.Routine;
import mx.com.sportsworld.sw.net.resource.Resource;
import mx.com.sportsworld.sw.parser.RoutineParser;
import mx.com.sportsworld.sw.pojo.RoutinePojo;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.provider.SportsWorldContract;
import mx.com.sportsworld.sw.web.HttpGetClient;

/**
 * The Class RoutineTask.
 */
public class RoutineTask extends AsyncTask<RoutinePojo, Void, Routine> {

	/** The ctx. */
	public static Context ctx = null;
	
	/** The m account mngr. */
	SportsWorldAccountManager mAccountMngr = null;
	
	/** The Constant COLS_USER. */
	private static final String[] COLS_USER = buildColumns();
	
	/** The url. */
	String url = "";
	
	/** The http client. */
	HttpGetClient httpClient = null;
	
	/** The data. */
	String data = "";
	
	/** The pojo. */
	RoutinePojo pojo = new RoutinePojo();
	
	/** The pojo res. */
	RoutinePojo pojoRes = new RoutinePojo();
	
	/** The rputine parser. */
	RoutineParser rputineParser = new RoutineParser();
	
	/** The response. */
	ResponseInterface response = null;
	
	/** The parser. */
	JsonParser parser;

	/**
	 * Instantiates a new routine task.
	 * 
	 * @param response
	 *            the response
	 */
	public RoutineTask(ResponseInterface response) {
		this.response = response;
	}

	/**
	 * Builds the columns.
	 * 
	 * @return the string[]
	 */
	private static String[] buildColumns() {
		final SparseArray<String> colsMap = new SparseArray<String>();
		colsMap.put(0, SportsWorldContract.User.WEIGHT);
		colsMap.put(1, SportsWorldContract.User.GENDER_ID);
		colsMap.put(2, SportsWorldContract.User.AGE);
		colsMap.put(3, SportsWorldContract.User.ROUTINE_ID);

		final int colCount = colsMap.size();
		final String[] cols = new String[colCount];
		for (int i = 0; i < colCount; i++) {
			cols[i] = colsMap.get(i);
		}

		return cols;
	}

	// Obtenemos una rutina sin guardarla en bd
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	protected Routine doInBackground(RoutinePojo... params) {

		Routine routine = null;
		parser = new JsonParser();

		for (RoutinePojo dato : params) {
			buldUrl(dato);
			httpClient = new HttpGetClient(url, ctx);
			pojo.setJson(httpClient.excuteGet());

			if (pojo.getJson().equals("TimeOut")) {
				long a = 0;
				routine = new Routine(a, null, null, null, null, null);
				routine.setStatus(false);
				routine.setMessage("TimeOut");
				break;
			}

			pojo = parser.parseJson(pojo);

			if (!pojo.isStatus()) {
				long a = 0;
				routine = new Routine(a, null, null, null, null, null);
				routine.setStatus(false);
				routine.setMessage("TimeOut");
				break;
			}
			routine = parseRoutine(pojo.getData());
			routine.setStatus(pojo.isStatus());
		}

		return routine;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Routine result) {
		response.onResultResponse(result);
	}

	/**
	 * Buld url.
	 * 
	 * @param pojo
	 *            the pojo
	 */
	public void buldUrl(RoutinePojo pojo) {
		Cursor cursor = null;
		final SportsWorldAccountManager accountMngr = getAccountManager();
		final long userId = Long.parseLong(accountMngr.getCurrentUserId());
		try {

			final Uri userUri = SportsWorldContract.User.buildUserUri(String
					.valueOf(userId));
			cursor = ctx.getContentResolver()
					.query(userUri, COLS_USER, null/* selection */,
							null /* selectionArgs */, null /* sortOrder */);

			if (!cursor.moveToFirst()) {
				getAccountManager().logOut();
				throw new RuntimeException(
						"User not found!. Log out and the crash."
								+ " This should never happen.");
			}
			int age = 0;
			double weight = 0;
			long genderId = 0;

			if (!SportsWorldPreferences.getGuestId(ctx).equals("")) {
				age = Integer.parseInt(SportsWorldPreferences.getGuestAge(ctx));
				weight = Integer.parseInt(SportsWorldPreferences
						.getGuestWeight(ctx));
				if (SportsWorldPreferences.getGuestGender(ctx).equals(
						"Femenino"))
					genderId = 12;
				else
					genderId = 13;
			} else {

				age = cursor.getInt(2);
				weight = cursor.getDouble(0);
				genderId = cursor.getLong(1);
			}

			url = Resource.URL_API_BASE + "/routine/details/";
			url += pojo.getId_user();
			url += "/" + pojo.getIdRoutine() + "/" + age + "/" + (int) weight
					+ "/" + genderId + "/" + pojo.getWeek_id() + "/"
					+ pojo.getDay_id();
			Log.i("LogIron", url);

		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * Gets the account manager.
	 * 
	 * @return the account manager
	 */
	protected SportsWorldAccountManager getAccountManager() {
		if (mAccountMngr == null) {
			mAccountMngr = new SportsWorldAccountManager(
					ctx.getApplicationContext());
		}
		return mAccountMngr;
	}

	/**
	 * Parses the routine.
	 * 
	 * @param jsonStr
	 *            the json str
	 * @return the routine
	 */
	public Routine parseRoutine(String jsonStr) {
		Routine routine = null;
		try {
			routine = rputineParser.parse(new JSONObject(jsonStr));
		} catch (Exception ex) {
			Log.i("LogIron", ex.toString());
		}
		return routine;

	}

}
