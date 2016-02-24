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

import android.content.ContentValues;

import mx.com.sportsworld.sw.model.Exercise;
import mx.com.sportsworld.sw.model.Routine;
import mx.com.sportsworld.sw.model.WeekDayRelationship;
import mx.com.sportsworld.sw.pojo.TraininPojo;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

/**
 * The Class RoutineParser.
 */
public class RoutineParser implements JsonParser<Routine>,
		ContentValuesParser<Routine> {

	/** The Constant KEY_ID. */
	private static final String KEY_ID = "idrutina";
	
	/** The Constant KEY_NAME. */
	private static final String KEY_NAME = "nombre";
	
	/** The Constant OBJECT_ZONES. */
	private static final String OBJECT_ZONES = "zones";
	
	/** The Constant ARRAY_TRAINING. */
	private static final String ARRAY_TRAINING = "training";
	
	/** The Constant KEY_NUTRITION_ADVICE. */
	private static final String KEY_NUTRITION_ADVICE = "nutrition";
	
	/** The Constant ARRAY_WEEK_DAY_RELATIONSHIP. */
	private static final String ARRAY_WEEK_DAY_RELATIONSHIP = "routine_duration";
	
	/** The Constant ROUTINE_NAME. */
	private static final String ROUTINE_NAME = "routine_name";

	/* (non-Javadoc)
	 * @see com.sportsworld.android.parser.JsonParser#parse(org.json.JSONObject)
	 */
	@Override
	public Routine parse(JSONObject object) throws JSONException {

		final long id = object.optLong(KEY_ID);
		final String name = object.optString(KEY_NAME);
		final String nutritionAdvice = object.optString(KEY_NUTRITION_ADVICE);
		final String routineName = object.optString(ROUTINE_NAME);

		long activeWeekId = Exercise.NO_CURRENT_WEEK_ID;
		long activeDayId = Exercise.NO_CURRENT_DAY_ID;
		final JSONArray jsonArrayWeekDayRelationship = object
				.optJSONArray(ARRAY_WEEK_DAY_RELATIONSHIP);
		List<WeekDayRelationship> weekDayRelationshipList = null;
		if (jsonArrayWeekDayRelationship != null) {
			final WeekDayRelationshipParser weekDayRelationshipParser = new WeekDayRelationshipParser();
			weekDayRelationshipList = weekDayRelationshipParser
					.parse(jsonArrayWeekDayRelationship);

			final int weekDayRelationshipCount = (weekDayRelationshipList == null) ? 0
					: weekDayRelationshipList.size();
			for (int j = 0; j < weekDayRelationshipCount; j++) {
				final WeekDayRelationship weekDayRelationship = weekDayRelationshipList
						.get(j);
				if (weekDayRelationship.isActive()) {
					activeWeekId = weekDayRelationship.getWeekId();
					activeDayId = weekDayRelationship.getDayId();
					break;
				}
			}

		}
		List<TraininPojo> traininList = null;
		final JSONArray jsonArrayTraining = object.optJSONArray(ARRAY_TRAINING);
		if (jsonArrayTraining != null) {
			final TraininParse trainingPArse = new TraininParse();
			traininList = trainingPArse.parse(jsonArrayTraining);
		}


		return new Routine(id, name, traininList, nutritionAdvice,
				weekDayRelationshipList, routineName);
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.parser.JsonParser#parse(org.json.JSONArray)
	 */
	@Override
	public List<Routine> parse(JSONArray array) throws JSONException {
		final int count = array.length();
		final List<Routine> routines = new ArrayList<Routine>(count);
		for (int i = 0; i < count; i++) {
			routines.add(parse(array.getJSONObject(i)));
		}
		return routines;
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.parser.ContentValuesParser#parse(java.lang.Object, android.content.ContentValues)
	 */
	@Override
	public ContentValues parse(Routine object, ContentValues values) {

		if (values == null) {
			values = new ContentValues();
		}

		values.put(SportsWorldContract.Routine._ID, object.getId());
		values.put(SportsWorldContract.Routine.NAME, object.getmRoutine_name());
		values.put(SportsWorldContract.Routine.NUTRITION_ADVICE,
				object.getNutritionAdvice());
		return values;
	}

	/**
	 * Parses the.
	 * 
	 * @param object
	 *            the object
	 * @param values
	 *            the values
	 * @return the content values
	 */
	public ContentValues parse(TraininPojo object, ContentValues values) {

		if (values == null) {
			values = new ContentValues();
		}
		values.put(SportsWorldContract.Trainin.EXERCISE_NAME,
				object.getmExcercise());
		values.put(SportsWorldContract.Trainin.EXCERCISE_IMAGE,
				object.getUrlImg());
		return values;
	}

}
