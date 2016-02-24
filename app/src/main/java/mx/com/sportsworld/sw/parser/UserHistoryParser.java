package mx.com.sportsworld.sw.parser;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.com.sportsworld.sw.pojo.HistoryPojo;
import mx.com.sportsworld.sw.pojo.UserHistoryPojo;
import mx.com.sportsworld.sw.utils.GeneralUtils;

/**
 * The Class UserHistoryParser.
 */
public class UserHistoryParser {

	/** The Constant KEY_NOMBRE_EVENTO. */
	private static final String KEY_NOMBRE_EVENTO = "nombreevento";
	
	/** The Constant KEY_CLUB. */
	private static final String KEY_CLUB = "club";
	
	/** The Constant KEY_PUNTOS. */
	private static final String KEY_PUNTOS = "puntos";
	
	/** The Constant KEY_ID_PERSONA. */
	private static final String KEY_ID_PERSONA = "idpersona";
	
	/** The Constant KEY_FECHA_EVENTO. */
	private static final String KEY_FECHA_EVENTO = "fechaevento";
	
	/** The Constant KEY_IMPORTE. */
	private static final String KEY_IMPORTE = "importe";

	/**
	 * Parses the.
	 * 
	 * @param object
	 *            the object
	 * @return the history pojo
	 * @throws JSONException
	 *             the jSON exception
	 */
	public HistoryPojo parse(JSONObject object) throws JSONException {
		HistoryPojo resultParser = new HistoryPojo();

		resultParser.setNombreEvento(object.optString(KEY_NOMBRE_EVENTO));
		resultParser.setClub(object.optString(KEY_CLUB));
		resultParser.setPuntos(object.optString(KEY_PUNTOS));
		resultParser.setIdPersona(object.optString(KEY_ID_PERSONA));
		resultParser.setFechaEvento(stringToCalendar(object
				.optString(KEY_FECHA_EVENTO)));
		resultParser.setImporte(object.optString(KEY_IMPORTE));
		return resultParser;
	}

	/**
	 * String to calendar.
	 * 
	 * @param strCalendar
	 *            the str calendar
	 * @return the calendar
	 */
	public Calendar stringToCalendar(String strCalendar) {
		return GeneralUtils.stringToCalendar(strCalendar);
	}

	/**
	 * Parses the.
	 * 
	 * @param jsonData
	 *            the json data
	 * @return the user history pojo
	 */
	public UserHistoryPojo parse(String jsonData) {
		UserHistoryPojo usrHistory = new UserHistoryPojo();
		try {
			JSONArray jsonArray = new JSONArray(jsonData);
			usrHistory.setListItems(parse(jsonArray));
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
	public List<HistoryPojo> parse(JSONArray array) throws JSONException {
		final int count = array.length();
		final List<HistoryPojo> histories = new ArrayList<HistoryPojo>(count);
		for (int i = 0; i < count; i++) {
			if(i==20)
				break;
			histories.add(parse(array.getJSONObject(i)));
		}
		return histories;
	}
}
