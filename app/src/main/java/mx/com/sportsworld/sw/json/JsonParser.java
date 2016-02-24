package mx.com.sportsworld.sw.json;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import mx.com.sportsworld.sw.pojo.AwardItemPojo;
import mx.com.sportsworld.sw.pojo.AwardPojo;
import mx.com.sportsworld.sw.pojo.AwardProfilePojo;
import mx.com.sportsworld.sw.pojo.BranchItemPojo;
import mx.com.sportsworld.sw.pojo.BranchPojo;
import mx.com.sportsworld.sw.pojo.ClassPojo;
import mx.com.sportsworld.sw.pojo.DevicePojo;
import mx.com.sportsworld.sw.pojo.MainPojo;
import mx.com.sportsworld.sw.pojo.ProfileUserPojo;
import mx.com.sportsworld.sw.pojo.RoutinePojo;
import mx.com.sportsworld.sw.pojo.UserHistoryPojo;
import mx.com.sportsworld.sw.pojo.UserPojo;

// TODO: Auto-generated Javadoc

/**
 * The Class JsonParser.
 */
public class JsonParser {
	
	/** The Constant KEY_SUCCESS. */
	public static final String KEY_SUCCESS = "status";
	
	/** The Constant KEY_MESSAGE. */
	public static final String KEY_MESSAGE = "message";
	
	/** The Constant ARRAY_DATA. */
	public static final String ARRAY_DATA = "data";
	
	/** The Constant NO_ROUTINE. */
	private static final int NO_ROUTINE = -1;
	
	/** The Constant NEW_ID_ROUTINE. */
	private static final String NEW_ID_ROUTINE= "new_routine_id";
	
	/** The Constant KEY_UPDATE. */
	public static final String KEY_UPDATE = "update";

	/** The object. */
	JSONObject object = null;
	
	/** The got data. */
	boolean gotData = true;

	/**
	 * Parses the json.
	 *
	 * @param pojo the pojo
	 * @return the class pojo
	 */
	public ClassPojo parseJson(ClassPojo pojo) {

		try {
			object = new JSONObject(pojo.getJson());
			Log.i("LogIron", object.toString());
		} catch (JSONException e) {
			gotData = false;
		}
		if (gotData) {
			pojo.setStatus(object.optBoolean(KEY_SUCCESS));
			pojo.setMessage(object.optString(KEY_MESSAGE));
			pojo.setData(object.optString(ARRAY_DATA));
		}

		return pojo;
	}
	
	
	
	/**
	 * Parses the json.
	 *
	 * @param pojo the pojo
	 * @return the main pojo
	 */
	public MainPojo parseJson(MainPojo pojo) {

		try {
			object = new JSONObject(pojo.getJson());
			Log.i("LogIron", object.toString());
		} catch (JSONException e) {
			gotData = false;
		}
		if (gotData) {
			pojo.setStatus(object.optBoolean(KEY_SUCCESS));
			pojo.setMessage(object.optString(KEY_MESSAGE));
			pojo.setData(object.optString(ARRAY_DATA));
		}

		return pojo;
	}
	
	/**
	 * Parses the json.
	 *
	 * @param pojo the pojo
	 * @return the branch pojo
	 */
	public BranchPojo parseJson(BranchPojo pojo) {

		try {
			object = new JSONObject(pojo.getJson());
			Log.i("LogIron", object.toString());
		} catch (JSONException e) {
			gotData = false;
		}
		if (gotData) {
			pojo.setStatus(object.optBoolean(KEY_SUCCESS));
			pojo.setMessage(object.optString(KEY_MESSAGE));
			pojo.setData(object.optString(ARRAY_DATA));
		}

		return pojo;
	}

	/**
	 * Parses the json.
	 *
	 * @param pojo the pojo
	 * @return the branch item pojo
	 */
	public BranchItemPojo parseJson(BranchItemPojo pojo) {

		try {
			object = new JSONObject(pojo.getJson());
			Log.i("LogIron", object.toString());
		} catch (JSONException e) {
			gotData = false;
		}
		if (gotData) {
			pojo.setStatus(object.optBoolean(KEY_SUCCESS));
			pojo.setMessage(object.optString(KEY_MESSAGE));
			pojo.setData(object.optString(ARRAY_DATA));
		}

		return pojo;
	}
	
	/**
	 * Parses the json.
	 *
	 * @param pojo the pojo
	 * @return the award pojo
	 */
	public AwardPojo parseJson(AwardPojo pojo) {

		try {
			object = new JSONObject(pojo.getJson());
			Log.i("LogIron", object.toString());
		} catch (JSONException e) {
			gotData = false;
		}
		if (gotData) {
			pojo.setStatus(object.optBoolean(KEY_SUCCESS));
			pojo.setMessage(object.optString(KEY_MESSAGE));
			pojo.setData(object.optString(ARRAY_DATA));
		}

		return pojo;
	}
	
	/**
	 * Parses the json.
	 *
	 * @param pojo the pojo
	 * @return the award item pojo
	 */
	public AwardItemPojo parseJson(AwardItemPojo pojo) {

		try {
			object = new JSONObject(pojo.getJson());
			Log.i("LogIron", object.toString());
		} catch (JSONException e) {
			gotData = false;
		}
		if (gotData) {
			pojo.setStatus(object.optBoolean(KEY_SUCCESS));
			pojo.setMessage(object.optString(KEY_MESSAGE));
			pojo.setData(object.optString(ARRAY_DATA));
		}

		return pojo;
	}
	
	/**
	 * Parses the json.
	 *
	 * @param pojo the pojo
	 * @return the award profile pojo
	 */
	public AwardProfilePojo parseJson(AwardProfilePojo pojo) {

		try {
			object = new JSONObject(pojo.getJson());
			Log.i("LogIron", object.toString());
		} catch (JSONException e) {
			gotData = false;
		}
		if (gotData) {
			pojo.setStatus(object.optBoolean(KEY_SUCCESS));
			pojo.setMessage(object.optString(KEY_MESSAGE));
			pojo.setData(object.optString(ARRAY_DATA));
		}

		return pojo;
	}
	
	/**
	 * Parses the json.
	 *
	 * @param pojo the pojo
	 * @return the user history pojo
	 */
	public UserHistoryPojo parseJson(UserHistoryPojo pojo) {


		try {
			object = new JSONObject(pojo.getJson());
			Log.i("LogIron", object.toString());
		} catch (JSONException e) {
			gotData = false;
		}
		if (gotData) {
			pojo.setStatus(object.optBoolean(KEY_SUCCESS));
			pojo.setMessage(object.optString(KEY_MESSAGE));
			pojo.setData(object.optString(ARRAY_DATA));
		}

		return pojo;
	}

	
	/**
	 * Parses the json.
	 *
	 * @param pojo the pojo
	 * @return the device pojo
	 */
	public DevicePojo parseJson(DevicePojo pojo) {

		try {
			object = new JSONObject(pojo.getJson());
			Log.i("LogIron", object.toString());
		} catch (JSONException e) {
			gotData = false;
		}
		if (gotData) {
			pojo.setStatus(object.optBoolean(KEY_SUCCESS));
			pojo.setMessage(object.optString(KEY_MESSAGE));
			pojo.setData(object.optString(ARRAY_DATA));
		}

		return pojo;
	}

	/**
	 * Parses the json.
	 *
	 * @param pojo the pojo
	 * @return the routine pojo
	 */
	public RoutinePojo parseJson(RoutinePojo pojo) {
		try {
			object = new JSONObject(pojo.getJson());
			Log.i("LogIron", object.toString());
		} catch (JSONException e) {
			gotData = false;
		}
		if (gotData) {
			pojo.setStatus(object.optBoolean(KEY_SUCCESS));
			pojo.setMessage(object.optString(KEY_MESSAGE));
			pojo.setData(object.optString(ARRAY_DATA));
			pojo.setJson("");
		}
		return pojo;
	}

	/**
	 * Parses the json.
	 *
	 * @param pojo the pojo
	 * @return the user pojo
	 */
	public UserPojo parseJson(UserPojo pojo){
		try {
			object = new JSONObject(pojo.getJson());
			Log.i("LogIron", object.toString());
		} catch (JSONException e) {
			gotData = false;
		}
		if (gotData) {
			pojo.setStatus(object.optBoolean(KEY_SUCCESS));
			pojo.setMessage(object.optString(KEY_MESSAGE));
			pojo.setData(object.optString(ARRAY_DATA));
			pojo.setJson("");
		}
		return pojo;
	}
	
	/**
	 * Parses the special json.
	 *
	 * @param pojo the pojo
	 * @return the user pojo
	 */
	public UserPojo parseSpecialJson(UserPojo pojo) {
		try {
			object = new JSONObject(pojo.getJson());
			Log.i("LogIron", object.toString());
			pojo.setmRoutineId(object.optInt(NEW_ID_ROUTINE, NO_ROUTINE));
			

		} catch (Exception ex) {
			Log.d("kokusho", ex.toString());
		}
		pojo.setJson("");

		return pojo;
	}
	
	/**
	 * Parses the json.
	 *
	 * @param pojo the pojo
	 * @return the profile user pojo
	 */
	public ProfileUserPojo parseJson(ProfileUserPojo pojo){
		try {
			object = new JSONObject(pojo.getJson());
			Log.i("LogIron", object.toString());
		} catch (JSONException e) {
			gotData = false;
		}
		if (gotData) {
			pojo.setStatus(object.optBoolean(KEY_SUCCESS));
			pojo.setMessage(object.optString(KEY_MESSAGE));
			pojo.setData(object.optString(ARRAY_DATA));
			pojo.setJson("");
		}
		return pojo;
	}

}
