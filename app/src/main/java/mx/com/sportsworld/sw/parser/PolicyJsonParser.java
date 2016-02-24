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

import mx.com.sportsworld.sw.model.Policy;

/**
 * The Class PolicyJsonParser.
 */
public class PolicyJsonParser implements JsonParser<Policy> {

    /** The Constant KEY_TYPE. */
    private static final String KEY_TYPE = "type";
    
    /** The Constant KEY_CONTENT. */
    private static final String KEY_CONTENT = "content";
    
    /** The Constant KEY_DATE. */
    private static final String KEY_DATE = "date";
    
    /** The Constant DATE_FORMAT. */
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    /* (non-Javadoc)
     * @see com.sportsworld.android.parser.JsonParser#parse(org.json.JSONObject)
     */
    @Override
    public Policy parse(JSONObject object) throws JSONException {
        final String type = object.optString(KEY_TYPE);
        final String content = object.optString(KEY_CONTENT);
        Date date;
        try {
            date = new SimpleDateFormat(DATE_FORMAT, Locale.US).parse(object.optString(KEY_DATE));
        } catch (ParseException e) {
            date = null;
        }
        final Policy policy = new Policy(type, content, date);
        return policy;
    }

    /* (non-Javadoc)
     * @see com.sportsworld.android.parser.JsonParser#parse(org.json.JSONArray)
     */
    @Override
    public List<Policy> parse(JSONArray array) throws JSONException {
        final int count = array.length();
        final List<Policy> policies = new ArrayList<Policy>(count);
        for (int i = 0; i < count; i++) {
            policies.add(parse(array.getJSONObject(i)));
        }
        return policies;
    }

}
