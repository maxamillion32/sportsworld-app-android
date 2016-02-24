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

import mx.com.sportsworld.sw.pojo.AwardItemPojo;
import mx.com.sportsworld.sw.pojo.AwardPojo;

/**
 * The Class AwardParser.
 */
public class AwardParser {

	/** The Constant KEY_PUNTOS. */
	private static final String KEY_PUNTOS = "puntos";
	
	/** The Constant KEY_DISPONIBILIDAD. */
	private static final String KEY_DISPONIBILIDAD = "disponibilidad";
	
	/** The Constant KEY_ID_LEALTAD_PREMIOS. */
	private static final String KEY_ID_LEALTAD_PREMIOS = "idlealtadpremio";
	
	/** The Constant KEY_IMAGEN. */
	private static final String KEY_IMAGEN = "imagen";
	
	/** The Constant KEY_PREMIO. */
	private static final String KEY_PREMIO = "premio";
	
	/** The Constant KEY_DESCRIPTION. */
	private static final String KEY_DESCRIPTION = "descripcion";

	/**
	 * Parses the.
	 * 
	 * @param object
	 *            the object
	 * @return the award item pojo
	 * @throws JSONException
	 *             the jSON exception
	 */
	public AwardItemPojo parse(JSONObject object) throws JSONException {
		AwardItemPojo resultParser = new AwardItemPojo();

		resultParser.setPuntos(object.optString(KEY_PUNTOS));
		resultParser.setDisponibilidad(object.optString(KEY_DISPONIBILIDAD));
		resultParser.setIdLealtadPremios(object
				.optString(KEY_ID_LEALTAD_PREMIOS));
		resultParser.setImagen(object.optString(KEY_IMAGEN));
		resultParser.setPremio(object.optString(KEY_PREMIO));
		resultParser.setDescription(object.optString(KEY_DESCRIPTION));
		return resultParser;
	}

	/**
	 * Parses the.
	 * 
	 * @param jsonData
	 *            the json data
	 * @return the award pojo
	 */
	public AwardPojo parse(String jsonData) {
		AwardPojo usrHistory = new AwardPojo();
		try {
			JSONArray jsonArray = new JSONArray(jsonData);
			usrHistory.setItems(parse(jsonArray));
		} catch (Exception ex) {

		}
		return usrHistory;
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
	public List<AwardItemPojo> parse(JSONArray array) throws JSONException {
		final int count = array.length();
		final List<AwardItemPojo> histories = new ArrayList<AwardItemPojo>(
				count);
		for (int i = 0; i < count; i++) {
			histories.add(parse(array.getJSONObject(i)));
		}
		return histories;
	}

}
