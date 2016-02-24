package mx.com.sportsworld.sw.json;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import org.json.JSONObject;

import android.util.Log;

import mx.com.sportsworld.sw.pojo.RoutinePojo;

// TODO: Auto-generated Javadoc

/**
 * The Class RoutineJsonParser.
 */
public class RoutineJsonParser extends JsonParser {

	/** The Constant KEY_WEEK_ID. */
	private static final String KEY_WEEK_ID = "week";
	
	/** The Constant KEY_DAY_ID. */
	private static final String KEY_DAY_ID = "day";
	
	/** The object. */
	JSONObject object = null;
	
	/** The got data. */
	boolean gotData = true;

	/**
	 * Parses the wd json.
	 *
	 * @param pojo the pojo
	 * @return the routine pojo
	 */
	public RoutinePojo parseWDJson(RoutinePojo pojo) {

		try {
			object = new JSONObject(pojo.getData());
			Log.i("LogIron", object.toString());
		} catch (Exception ex) {
			Log.i("Kokusho Error", ex.toString());
			gotData = false;
		}
		if (gotData) {
			pojo.setWeek_id(object.optString(KEY_WEEK_ID));
			pojo.setDay_id(object.optString(KEY_DAY_ID));
		}

		return pojo;
	}

}
