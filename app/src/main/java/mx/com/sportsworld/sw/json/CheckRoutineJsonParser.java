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
 * The Class CheckRoutineJsonParser.
 */
public class CheckRoutineJsonParser {

	/** The Constant KEY_ROUTINE_ID. */
	private static final String KEY_ROUTINE_ID = "routine_id";
	
	/** The object. */
	JSONObject object = null;
	
	/** The got data. */
	boolean gotData = true;
	
	/**
	 * Parses the wd json.
	 *
	 * @param pojo the pojo
	 * @return the int
	 */
	public int parseWDJson(RoutinePojo pojo) {

		int routineId=0;
		
		try {
			object = new JSONObject(pojo.getData());
		} catch (Exception ex) {
			Log.i("Kokusho Error", ex.toString());
			gotData = false;
		}
		if (gotData) {
			routineId=object.optInt(KEY_ROUTINE_ID);
		}

		return routineId;
	}


}
