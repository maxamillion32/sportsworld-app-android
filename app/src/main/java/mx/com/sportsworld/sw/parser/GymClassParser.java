package mx.com.sportsworld.sw.parser;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.ContentValues;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.com.sportsworld.sw.model.GymClass;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

/**
 * The Class GymClassParser.
 */
public class GymClassParser implements JsonParser<GymClass>,
		ContentValuesParser<GymClass> {

	/** The Constant KEY_IDEAL_CAPACITY. */
	private static final String KEY_IDEAL_CAPACITY = "capacidadideal";
	
	/** The Constant KEY_IN_HIGH_DEMAND. */
	private static final String KEY_IN_HIGH_DEMAND = "demand";
	
	/** The Constant KEY_NAME. */
	private static final String KEY_NAME = "clase";
	
	/** The Constant KEY_SALON. */
	private static final String KEY_SALON = "salon";
	
	/** The Constant KEY_CLUB. */
	private static final String KEY_CLUB = "club";
	
	/** The Constant KEY_RESERVATIONS_COUNT. */
	private static final String KEY_RESERVATIONS_COUNT = "reservacion";
	
	/** The Constant KEY_CURRENT_CAPACITY. */
	private static final String KEY_CURRENT_CAPACITY = "capacidadregistrada";
	
	/** The Constant KEY_MAXIMUM_CAPACITY. */
	private static final String KEY_MAXIMUM_CAPACITY = "capacidadmaxima";
	
	/** The Constant KEY_AVAILABLE_FROM. */
	private static final String KEY_AVAILABLE_FROM = "iniciovigencia";
	
	/** The Constant KEY_AVAILABLE_UNTIL. */
	private static final String KEY_AVAILABLE_UNTIL = "finvigencia";
	
	/** The Constant KEY_CONFIRMED_RESERVATIONS. */
	private static final String KEY_CONFIRMED_RESERVATIONS = "confirmados";
	
	/** The Constant KEY_STARTS_AT. */
	private static final String KEY_STARTS_AT = "inicio";
	
	/** The Constant KEY_FINISH_AT. */
	private static final String KEY_FINISH_AT = "fin";
	
	/** The Constant KEY_COACH_NAME. */
	private static final String KEY_COACH_NAME = "instructor";
	
	/** The Constant KEY_FACILITY_ACTIVITY_PROGRAMED_ID. */
	private static final String KEY_FACILITY_ACTIVITY_PROGRAMED_ID = "idinstalacionactividadprogramada";
	
	/** The Constant DATE_FORMAT. */
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	/** The Constant TIME_FORMAT. */
	private static final String TIME_FORMAT = "HH:mm:ss";
	
	/** The Constant AGENDAR_CLASES. */
	private static final String KEY_AGENDAR_CLASES = "agendar";

	/* (non-Javadoc)
	 * @see com.sportsworld.android.parser.JsonParser#parse(org.json.JSONObject)
	 */
	@Override
	public GymClass parse(JSONObject object) throws JSONException {

		final int idealCapacity = object.optInt(KEY_IDEAL_CAPACITY);
		final boolean inHighDemand = object.optBoolean(KEY_IN_HIGH_DEMAND);
		final String name = object.optString(KEY_NAME);
		final String salon = object.optString(KEY_SALON);
		final String club = object.optString(KEY_CLUB);
		final int reservationsCount = object.optInt(KEY_RESERVATIONS_COUNT);
		final int currentCapacity = object.optInt(KEY_CURRENT_CAPACITY);
		final int maximumCapacity = object.optInt(KEY_MAXIMUM_CAPACITY);
		final int confirmedReservationsCount = object
				.optInt(KEY_CONFIRMED_RESERVATIONS);
		final String coachName = object.optString(KEY_COACH_NAME);
		final String agendarClases = object.optString(KEY_AGENDAR_CLASES);
		final long instActProgId = object
				.optLong(KEY_FACILITY_ACTIVITY_PROGRAMED_ID);

		long availableFrom;
		try {
			final Date startsAtDate = new SimpleDateFormat(DATE_FORMAT,
					Locale.US).parse(object.optString(KEY_AVAILABLE_FROM));
			availableFrom = startsAtDate.getTime();
		} catch (ParseException e) {
			availableFrom = 0;
		}

		long availableUntil;
		try {
			final Date date = new SimpleDateFormat(DATE_FORMAT, Locale.US)
					.parse(object.optString(KEY_AVAILABLE_UNTIL));
			availableUntil = date.getTime();
		} catch (ParseException e) {
			availableUntil = 0;
		}

		long startsAt;
		try {
			final Date date = new SimpleDateFormat(TIME_FORMAT, Locale.US)
					.parse(object.optString(KEY_STARTS_AT));
			startsAt = date.getTime();
		} catch (ParseException e) {
			startsAt = 0;
		}

		long finishAt;
		try {
			final Date date = new SimpleDateFormat(TIME_FORMAT, Locale.US)
					.parse(object.optString(KEY_FINISH_AT));
			finishAt = date.getTime();
		} catch (ParseException e) {
			finishAt = 0;
		}

		return new GymClass(idealCapacity, inHighDemand, name, salon,club,
				reservationsCount, currentCapacity, maximumCapacity,
				availableFrom, availableUntil, confirmedReservationsCount,
				startsAt, finishAt, coachName, instActProgId
			    , agendarClases
				);

	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.parser.JsonParser#parse(org.json.JSONArray)
	 */
	@Override
	public List<GymClass> parse(JSONArray array) throws JSONException {
		final int count = array.length();
		final List<GymClass> gymClasses = new ArrayList<GymClass>(count);
		for (int i = 0; i < count; i++) {
			gymClasses.add(parse(array.getJSONObject(i)));
		}
		return gymClasses;
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.parser.ContentValuesParser#parse(java.lang.Object, android.content.ContentValues)
	 */
	@Override
	public ContentValues parse(GymClass object, ContentValues values) {
		if (values == null) {
			values = new ContentValues();
		}

		values.put(SportsWorldContract.GymClass.AVAILABLE_FROM,
				object.getAvailableFrom());
		values.put(SportsWorldContract.GymClass.COACH_NAME,
				object.getCoachName());
		values.put(SportsWorldContract.GymClass.SALON, object.getmSalon());
		values.put(SportsWorldContract.GymClass.CLUB, object.getmClub());
		values.put(SportsWorldContract.GymClass.CONFIRMED_RESERVATIONS,
				object.getConfirmedReservationsCount());
		values.put(SportsWorldContract.GymClass.CURRENT_CAPACITY,
				object.getCurrentCapacity());
		values.put(SportsWorldContract.GymClass.FACILITY_PROGRAMED_ACTIVITY_ID,
				object.getFacilityProgramedActivityId());
		values.put(SportsWorldContract.GymClass.IDEAL_CAPACITY,
				object.getIdealCapacity());
		values.put(SportsWorldContract.GymClass.IN_HIGH_DEMAND,
				object.isInHighDemand());
		values.put(SportsWorldContract.GymClass.MAXIMUM_CAPACITY,
				object.getMaximunCapacity());
		values.put(SportsWorldContract.GymClass.NAME, object.getName());
		values.put(SportsWorldContract.GymClass.RESERVATIONS_COUNT,
				object.getReservationsCount());
		values.put(SportsWorldContract.GymClass.STARTS_AT, object.getStartsAt());
		values.put(SportsWorldContract.GymClass.FINISH_AT,
				object.getmFinishAt());
		values.put(SportsWorldContract.GymClass.AGENDAR_CLASES,
				object.getmAgendarClase());
		return values;
	}

}
