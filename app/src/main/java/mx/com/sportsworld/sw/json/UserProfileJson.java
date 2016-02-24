package mx.com.sportsworld.sw.json;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import org.json.JSONObject;

import android.util.Log;

import mx.com.sportsworld.sw.pojo.UserPojo;

// TODO: Auto-generated Javadoc

/**
 * The Class UserProfileJson.
 */
public class UserProfileJson {

	/** The str json. */
	String strJson = "";
	
	/** The pojo. */
	UserPojo pojo = null;
	
	/** The object. */
	JSONObject object = null;

	/** The Constant KEY_USER_ID. */
	private static final String KEY_USER_ID = "user_id";
	
	/** The Constant KEY_ID_CLUB. */
	private static final String KEY_ID_CLUB = "idclub";
	
	/** The Constant KEY_WEIGHT. */
	private static final String KEY_WEIGHT = "weight";
	
	/** The Constant KEY_CLUB_NAME. */
	private static final String KEY_CLUB_NAME = "club";
	
	/** The Constant KEY_MEMBER_NUMBER. */
	private static final String KEY_MEMBER_NUMBER = "membernumber";
	
	/** The Constant KEY_ID_ROUTINE. */
	private static final String KEY_ID_ROUTINE = "idroutine";
	
	/** The Constant KEY_NAME. */
	private static final String KEY_NAME = "name";
	
	/** The Constant KEY_GENDER. */
	private static final String KEY_GENDER = "gender";
	
	/** The Constant KEY_AGE. */
	private static final String KEY_AGE = "age";
	
	/** The Constant KEY_GENDER_ID. */
	private static final String KEY_GENDER_ID = "gender_id";
	
	/** The Constant KEY_EMAIL. */
	private static final String KEY_EMAIL = "mail";
	
	/** The Constant KEY_REGISTER_DATE. */
	private static final String KEY_REGISTER_DATE = "register_date";
	
	/** The Constant KEY_HEIGHT. */
	private static final String KEY_HEIGHT = "tallest";
	
	/** The Constant KEY_MEM_UNIQ_ID. */
	private static final String KEY_MEM_UNIQ_ID = "memunic_id";
	
	/** The Constant KEY_BIRTH_DATE. */
	private static final String KEY_BIRTH_DATE = "dob";
	
	/** The Constant KEY_MEM_TYPE. */
	private static final String KEY_MEM_TYPE = "member_type";
	
	/** The Constant KEY_MANTEIMENT. */
	private static final String KEY_MANTEIMENT = "mainteiment";
	
	/** The Constant SECRET_KEY. */
	private static final String SECRET_KEY="secret_key";
	
	/** The Constant NO_ROUTINE. */
	private static final int NO_ROUTINE = -1;

	/**
	 * Instantiates a new user profile json.
	 *
	 * @param json the json
	 */
	public UserProfileJson(String json) {
		strJson = json;
		pojo= new UserPojo();
	}

	/**
	 * Parses the.
	 *
	 * @return the user pojo
	 */
	public UserPojo parse() {
		try {
			object = new JSONObject(strJson);
			pojo.setmUserId(object.getString(KEY_USER_ID));
			pojo.setmIdClub(object.optInt(KEY_ID_CLUB));
			pojo.setmWeight(object.optDouble(KEY_WEIGHT, 0d));
			pojo.setmClubName(object.optString(KEY_CLUB_NAME));
			pojo.setmMemberNumber(object.optInt(KEY_MEMBER_NUMBER));
			pojo.setmRoutineId(object.optInt(KEY_ID_ROUTINE, NO_ROUTINE));
			pojo.setmName(object.optString(KEY_NAME));
			pojo.setmGender(object.optString(KEY_GENDER));
			pojo.setmRegisterDate(object.getString(KEY_REGISTER_DATE));
			pojo.setmAge(object.optInt(KEY_AGE));
			pojo.setmGenderId(object.optInt(KEY_GENDER_ID));
			pojo.setmBirthDate(object.optString(KEY_BIRTH_DATE));
			pojo.setmMemberType(object.optString(KEY_MEM_TYPE));
			pojo.setmMainteinment(object.optString(KEY_MANTEIMENT));
			pojo.setmEmail(object.optString(KEY_EMAIL));
			pojo.setmHeight(object.optDouble(KEY_HEIGHT, 0d));
			pojo.setmMemUniqId(object.optLong(KEY_MEM_UNIQ_ID));
			pojo.setSecret_key(object.optString(SECRET_KEY));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("LogIron", e.toString());
		}
		return pojo;
	}
	
}
