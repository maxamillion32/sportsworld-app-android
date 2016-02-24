package mx.com.sportsworld.sw.parser;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import org.json.JSONException;
import org.json.JSONObject;

import mx.com.sportsworld.sw.pojo.BranchItemPojo;

/**
 * The Class AwardDirectionParser.
 */
public class AwardDirectionParser {

	/** The Constant KEY_ADDRESS. */
	private static final String KEY_ADDRESS = "direccion";
	
	/** The Constant KEY_SCHEDULE. */
	private static final String KEY_SCHEDULE = "horario";
	
	/** The Constant KEY_NAME. */
	private static final String KEY_NAME = "nombre";

	/**
	 * Parses the.
	 * 
	 * @param object
	 *            the object
	 * @return the branch item pojo
	 * @throws JSONException
	 *             the jSON exception
	 */
	public BranchItemPojo parse(JSONObject object) throws JSONException {
		BranchItemPojo resultParser = new BranchItemPojo();
		resultParser.setmAddress(object.optString(KEY_ADDRESS));
		resultParser.setmName(object.getString(KEY_NAME));
		resultParser.setmSchedule(object.getString(KEY_SCHEDULE));
		return resultParser;
	}

	/**
	 * Parses the.
	 * 
	 * @param jsonData
	 *            the json data
	 * @return the branch item pojo
	 */
	public BranchItemPojo parse(String jsonData) {
		BranchItemPojo pojo = new BranchItemPojo();
		try {
			JSONObject json = new JSONObject(jsonData);
			pojo = parse(json);
		} catch (Exception ex) {

		}
		return pojo;
	}

}
