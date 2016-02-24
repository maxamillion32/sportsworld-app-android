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

import mx.com.sportsworld.sw.pojo.BranchItemPojo;
import mx.com.sportsworld.sw.pojo.BranchPojo;

/**
 * The Class ClubParser.
 */
public class ClubParser {

	/** The Constant KEY_LATITUDE. */
	private static final String KEY_LATITUDE = "latitud";
	
	/** The Constant KEY_LONGITUDE. */
	private static final String KEY_LONGITUDE = "longitud";
	
	/** The Constant KEY_DISTANCE. */
	private static final String KEY_DISTANCE = "distance";
	
	/** The Constant KEY_PHONE. */
	private static final String KEY_PHONE = "telefono";
	
	/** The Constant KEY_STATE_ID. */
	private static final String KEY_STATE_ID = "idestado";
	
	/** The Constant KEY_ADDRESS. */
	private static final String KEY_ADDRESS = "direccion";
	
	/** The Constant KEY_SCHEDULE. */
	private static final String KEY_SCHEDULE = "horario";
	
	/** The Constant KEY_UN_ID. */
	private static final String KEY_UN_ID = "idun";
	
	/** The Constant ALIAS_KEY_UN_ID. */
	private static final String ALIAS_KEY_UN_ID = "club_id";
	
	/** The Constant KEY_KEY. */
	private static final String KEY_KEY = "clave";
	
	/** The Constant KEY_D_COUNT. */
	private static final String KEY_D_COUNT = "dcount";
	
	/** The Constant KEY_NAME. */
	private static final String KEY_NAME = "nombre";
	
	/** The Constant ALIAS_KEY_NAME. */
	private static final String ALIAS_KEY_NAME = "club";
	
	/** The Constant KEY_VIDEO_URL. */
	private static final String KEY_VIDEO_URL = "rutavideo";
	
	/** The Constant KEY_URL_360. */
	private static final String KEY_URL_360 = "ruta360";
	
	/** The Constant KEY_PRE_ORDER. */
	private static final String KEY_PRE_ORDER = "preventa";
	
	/** The Constant KEY_STATE_NAME. */
	private static final String KEY_STATE_NAME = "estado";
	
	/** The Constant KEY_TYPE. */
	private static final String KEY_TYPE = "type";
	
	/** The Constant KEY_FAVORITE. */
	private static final String KEY_FAVORITE = "favorito";
	
	/** The Constant KEY_FACILITY_DESCRIPTION. */
	private static final String KEY_FACILITY_DESCRIPTION = "descripcion";
	
	/** The Constant EY_FACILITY_IMAGE. */
	private static final String EY_FACILITY_IMAGE = "rutaimagen";
	
	/** The Constant ARRAY_FACILITIES. */
	private static final String ARRAY_FACILITIES = "instalaciones";
	
	/** The Constant ARRAY_IMAGES_URL. */
	private static final String ARRAY_IMAGES_URL = "imagenes_club";
	
	/** The Constant ARRAY_MAREA. */
	private static final String ARRAY_MAREA = "m_area";
	
	/** The Constant ARRAY_DF. */
	private static final String ARRAY_DF = "df";

	/** The branchs. */
	private List<BranchItemPojo> branchs;

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
		resultParser.setmLatitude(object.optDouble(KEY_LATITUDE));
		resultParser.setmLongitude(object.optDouble(KEY_LONGITUDE));
		resultParser.setmDistance(object.optDouble(KEY_DISTANCE));
		resultParser.setmPhone(object.optString(KEY_PHONE));
		resultParser.setmStateId(object.optLong(KEY_STATE_ID));
		resultParser.setmAddress(object.optString(KEY_ADDRESS));
		resultParser.setmSchedule(object.optString(KEY_SCHEDULE));
		long unId = object.optLong(KEY_UN_ID, 0L);
		if (unId == 0) {
			unId = object.optLong(ALIAS_KEY_UN_ID);
		}
		resultParser.setmUnId(unId);
		resultParser.setmKey(object.optString(KEY_KEY));
		resultParser.setmDCount(object.optInt(KEY_D_COUNT));
		resultParser.setmVideoUrl(object.optString(KEY_VIDEO_URL));
		resultParser.setmVideoUrl(object.optString(KEY_URL_360));
		resultParser.setmPreOrder(object.optInt(KEY_PRE_ORDER));
		resultParser.setmName(object.optString(KEY_NAME));
		resultParser.setmType(object.optString(KEY_TYPE));
		return resultParser;
	}

	/**
	 * Parses the.
	 * 
	 * @param jsonData
	 *            the json data
	 * @return the branch pojo
	 */
	public BranchPojo parse(String jsonData) {
		BranchPojo resPojo = new BranchPojo();
		branchs = new ArrayList<BranchItemPojo>();
		try {
			JSONObject jsonObj = new JSONObject(jsonData);
			JSONArray jsonDf = new JSONArray(jsonObj.optString(ARRAY_DF));
			parse(jsonDf);
			JSONArray jsonArray = new JSONArray(jsonObj.optString(ARRAY_MAREA));
			parse(jsonArray);
			resPojo.setListBranch(branchs);
		} catch (Exception ex) {

		}
		return resPojo;
	}

	/**
	 * Parses the.
	 * 
	 * @param array
	 *            the array
	 * @throws JSONException
	 *             the jSON exception
	 */
	public void parse(JSONArray array) throws JSONException {
		final int count = array.length();
		for (int i = 0; i < count; i++) {
			branchs.add(parse(array.getJSONObject(i)));
		}
	}

}
