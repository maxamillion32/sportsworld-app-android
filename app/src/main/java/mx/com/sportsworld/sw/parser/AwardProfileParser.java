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

import mx.com.sportsworld.sw.pojo.AwardProfileItemPojo;
import mx.com.sportsworld.sw.pojo.AwardProfilePojo;

/**
 * The Class AwardProfileParser.
 */
public class AwardProfileParser {

	/** The Constant KEY_PUNTOS. */
	private static final String KEY_PUNTOS = "puntos";
	
	/** The Constant KEY_ID_PERSONA. */
	private static final String KEY_ID_PERSONA = "idpersona";
	
	/** The Constant KEY_PERSONA. */
	private static final String KEY_PERSONA = "persona";

	/**
	 * Parses the.
	 * 
	 * @param object
	 *            the object
	 * @return the award profile item pojo
	 * @throws JSONException
	 *             the jSON exception
	 */
	public AwardProfileItemPojo parse(JSONObject object) throws JSONException {
		AwardProfileItemPojo resultParser = new AwardProfileItemPojo();

		resultParser.setPuntos(object.optString(KEY_PUNTOS));
		resultParser.setIdPersona(object.optString(KEY_ID_PERSONA));
		resultParser.setPersona(object
				.optString(KEY_PERSONA));
		return resultParser;
	}

	/**
	 * Parses the.
	 * 
	 * @param jsonData
	 *            the json data
	 * @return the award profile pojo
	 */
	public AwardProfilePojo parse(String jsonData) {
		AwardProfilePojo pojo = new AwardProfilePojo();
		try {
			JSONArray jsonArray = new JSONArray(jsonData);
			pojo.setListItems(parse(jsonArray));
		} catch (Exception ex) {

		}
		return pojo;
	}

	/**
	 * Parses the.
	 * 
	 * @param array
	 *            the array
	 * @return the list
	 * @throws JSONException
	 *             the jSON exception
	 */
	public List<AwardProfileItemPojo> parse(JSONArray array)
			throws JSONException {
		final int count = array.length();
		final List<AwardProfileItemPojo> pojo = new ArrayList<AwardProfileItemPojo>(
				count);
		for (int i = 0; i < count; i++) {
			pojo.add(parse(array.getJSONObject(i)));
		}
		return pojo;
	}

}
