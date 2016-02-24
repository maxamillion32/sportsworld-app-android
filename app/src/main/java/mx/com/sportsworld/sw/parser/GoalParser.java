package mx.com.sportsworld.sw.parser;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.com.sportsworld.sw.model.Goal;

/**
 * The Class GoalParser.
 */
public class GoalParser implements JsonParser<Goal> {

    /** The Constant KEY_DESCRIPTION. */
    private static final String KEY_DESCRIPTION = "descripcion";
    
    /** The Constant KEY_ID. */
    private static final String KEY_ID = "idpregunta";

    /* (non-Javadoc)
     * @see com.sportsworld.android.parser.JsonParser#parse(org.json.JSONObject)
     */
    @Override
    public Goal parse(JSONObject object) throws JSONException {
        final String description = object.optString(KEY_DESCRIPTION);
        final long id = object.optLong(KEY_ID);
        return new Goal(description, id);
    }

    /* (non-Javadoc)
     * @see com.sportsworld.android.parser.JsonParser#parse(org.json.JSONArray)
     */
    @Override
    public List<Goal> parse(JSONArray array) throws JSONException {
        final int count = array.length();
        final List<Goal> goals = new ArrayList<Goal>(count);
        for (int i = 0; i < count; i++) {
            goals.add(parse(array.getJSONObject(i)));
        }
        return goals;
    }

}
