package mx.com.sportsworld.sw.parser;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.com.sportsworld.sw.model.UserProfile;

/**
 * The Class UserProfileJsonParser.
 */
public class UserProfileJsonParser implements JsonParser<UserProfile> {

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
    
    /** The Constant KEY_REGISTER_DATE. */
    private static final String KEY_REGISTER_DATE = "register_date";
    
    /** The Constant KEY_GENDER_ID. */
    private static final String KEY_GENDER_ID = "gender_id";
    
    /** The Constant KEY_EMAIL. */
    private static final String KEY_EMAIL = "mail";
    
    /** The Constant KEY_HEIGHT. */
    private static final String KEY_HEIGHT = "tallest";
    
    /** The Constant KEY_MEM_UNIQ_ID. */
    private static final String KEY_MEM_UNIQ_ID = "memunic_id";
    
    /** The Constant DATE_FORMAT. */
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    
    /** The Constant NO_ROUTINE. */
    private static final int NO_ROUTINE = -1;

    /* (non-Javadoc)
     * @see com.sportsworld.android.parser.JsonParser#parse(org.json.JSONObject)
     */
    @Override
    public UserProfile parse(JSONObject object) throws JSONException {

        final String userId = object.getString(KEY_USER_ID);
        final int idClub = object.optInt(KEY_ID_CLUB);
        final double weight = object.optDouble(KEY_WEIGHT, 0d);
        final String clubName = object.optString(KEY_CLUB_NAME);
        final int memberNumber = object.optInt(KEY_MEMBER_NUMBER);
        final int idRoutine = object.optInt(KEY_ID_ROUTINE, NO_ROUTINE);
        final String name = object.optString(KEY_NAME);
        final String gender = object.optString(KEY_GENDER);
        final int age = object.optInt(KEY_AGE);
        final int genderId = object.optInt(KEY_GENDER_ID);
        final String email = object.optString(KEY_EMAIL);
        final double height = object.optDouble(KEY_HEIGHT, 0d);
        final long memUniqId = object.optLong(KEY_MEM_UNIQ_ID);

        Date registerDate;
        try {
            registerDate = new SimpleDateFormat(DATE_FORMAT, Locale.US).parse(object
                    .optString(KEY_REGISTER_DATE));
        } catch (ParseException e) {
            registerDate = null;
        }

        return new UserProfile(userId, idClub, weight, clubName, memberNumber, idRoutine, name, gender,
                age, registerDate, genderId, email, height, memUniqId);

    }

    /* (non-Javadoc)
     * @see com.sportsworld.android.parser.JsonParser#parse(org.json.JSONArray)
     */
    @Override
    public List<UserProfile> parse(JSONArray array) throws JSONException {
        final int count = array.length();
        final List<UserProfile> userProfiles = new ArrayList<UserProfile>(count);
        for (int i = 0; i < count; i++) {
            userProfiles.add(parse(array.getJSONObject(i)));
        }
        return userProfiles;
    }

}
