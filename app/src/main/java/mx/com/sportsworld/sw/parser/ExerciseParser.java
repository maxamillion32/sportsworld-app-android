package mx.com.sportsworld.sw.parser;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.com.sportsworld.sw.model.Exercise;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

/**
 * The Class ExerciseParser.
 */
public class ExerciseParser implements JsonParser<Exercise>,
		ContentValuesParser<Exercise>, CursorParser<Exercise> {

	/** The Constant KEY_ACTIVITIES. */
	public static final String KEY_ACTIVITIES = "activities";
	
	/** The Constant KEY_WEEK_ID. */
	public static final String KEY_WEEK_ID = "week_id";
	
	/** The Constant KEY_DAY_ID. */
	public static final String KEY_DAY_ID = "day_id";
	
	/** The Constant KEY_MUSCLE_WORKED. */
	public static final String KEY_MUSCLE_WORKED = "exercise";
	
	/** The Constant STATUS_ACTIVE. */
	private static final String STATUS_ACTIVE = "Activa";
	
	/** The Constant KEY_ACTIVE. */
	private static final String KEY_ACTIVE = "status";
	
	/** The Constant KEY_CIRCUIT_ID. */
	private static final String KEY_CIRCUIT_ID = "idcircuito";
	
	/** The Constant OBJECT_ACTIVITY_IMAGES. */
	private static final String OBJECT_ACTIVITY_IMAGES = "activity_photos";
	
	/** The Constant ARRAY_ACTIVITY_IMAGES_WOMEN. */
	private static final String ARRAY_ACTIVITY_IMAGES_WOMEN = "woman";
	
	/** The Constant ARRAY_ACTIVITY_IMAGES_MEN. */
	private static final String ARRAY_ACTIVITY_IMAGES_MEN = "men";
	
	/** The Constant KEY_MINIMUM_VALUE. */
	private static final String KEY_MINIMUM_VALUE = "min";
	
	/** The Constant KEY_MAXIMUM_VALUE. */
	private static final String KEY_MAXIMUM_VALUE = "max";
	
	/** The Constant KEY_MINIMUM_WEIGHT_LB. */
	private static final String KEY_MINIMUM_WEIGHT_LB = "min-lb";
	
	/** The Constant KEY_MAXIMUM_WEIGHT_LB. */
	private static final String KEY_MAXIMUM_WEIGHT_LB = "max-lb";
	
	/** The Constant KEY_UNIT. */
	private static final String KEY_UNIT = "unidad";
	
	/** The Constant KEY_SPORTS_CATEGORY_ID. */
	private static final String KEY_SPORTS_CATEGORY_ID = "idcategoriadeportiva";
	
	/** The Constant KEY_NAME. */
	private static final String KEY_NAME = "ejercicio";
	
	/** The Constant KEY_INSTRUCTIONS. */
	private static final String KEY_INSTRUCTIONS = "indicacion";
	
	/** The Constant KEY_MEASUREMENT_UNIT_TYPE_ID. */
	private static final String KEY_MEASUREMENT_UNIT_TYPE_ID = "idtipounidadmedida";
	
	/** The Constant KEY_CIRCUIT_NAME. */
	private static final String KEY_CIRCUIT_NAME = "circuito";
	
	/** The Constant KEY_VIDEO_URL_ID. */
	private static final String KEY_VIDEO_URL_ID = "idactividaddeportivavideo";
	
	/** The Constant KEY_SERIES. */
	private static final String KEY_SERIES = "series";
	
	/** The Constant KEY_SPORT_ID. */
	private static final String KEY_SPORT_ID = "idactividaddeportiva";
	
	/** The Constant KEY_ID. */
	private static final String KEY_ID = "idrutinaserieactividad";
	
	/** The count childs. */
	public static int countChilds = 0;

	/* (non-Javadoc)
	 * @see com.sportsworld.android.parser.JsonParser#parse(org.json.JSONObject)
	 */
	@Override
	public Exercise parse(JSONObject object) throws JSONException {

		final long id = object.optLong(KEY_ID);
		final String muscleWorked = object.optString(KEY_MUSCLE_WORKED);
		final boolean active = STATUS_ACTIVE.equals(object
				.optString(KEY_ACTIVE));
		final long circuitId = object.optLong(KEY_CIRCUIT_ID);

		final JSONObject jsonObjExampleImages = object
				.optJSONObject(OBJECT_ACTIVITY_IMAGES);

		final JSONArray jsonArrExampleImagesMen = jsonObjExampleImages
				.optJSONArray(ARRAY_ACTIVITY_IMAGES_MEN);
		final int exampleImagesMenUrlsCount = jsonArrExampleImagesMen.length();
		final String[] exampleImagesMenUrls = new String[exampleImagesMenUrlsCount];
		for (int k = 0; k < exampleImagesMenUrlsCount; k++) {
			exampleImagesMenUrls[k] = jsonArrExampleImagesMen.optString(k);
		}

		final JSONArray jsonArrExampleImagesWomen = jsonObjExampleImages
				.optJSONArray(ARRAY_ACTIVITY_IMAGES_WOMEN);
		final int exampleImagesWomenUrlsCount = jsonArrExampleImagesWomen
				.length();
		final String[] exampleImagesWomenUrls = new String[exampleImagesWomenUrlsCount];
		for (int k = 0; k < exampleImagesWomenUrlsCount; k++) {
			exampleImagesWomenUrls[k] = jsonArrExampleImagesWomen.optString(k);
		}

		final double minimumValue = object.optDouble(KEY_MINIMUM_VALUE);
		final double maximumValue = object.optDouble(KEY_MAXIMUM_VALUE);
		final double minimumWeightLb = object.optDouble(KEY_MINIMUM_WEIGHT_LB);
		final double maximumWeightLb = object.optDouble(KEY_MAXIMUM_WEIGHT_LB);
		final String unit = object.optString(KEY_UNIT);
		final long sportsCategoryId = object.optLong(KEY_SPORTS_CATEGORY_ID);
		final String exerciseName = object.optString(KEY_NAME);
		final String instructions = object.optString(KEY_INSTRUCTIONS);
		final long measurementUnitTypeId = object
				.optLong(KEY_MEASUREMENT_UNIT_TYPE_ID);
		String circuitName = object.optString(KEY_CIRCUIT_NAME);
		if ("-".equals(circuitName)) {
			circuitName = null;
		}
		final long videoUrlId = object.optLong(KEY_VIDEO_URL_ID);
		final long series = object.optLong(KEY_SERIES);
		final long sportId = object.optLong(KEY_SPORT_ID);
		final long weekId = object.optLong(KEY_WEEK_ID);
		final long dayId = object.optLong(KEY_DAY_ID);

		return new Exercise(id, muscleWorked, active, circuitId,
				exampleImagesMenUrls, exampleImagesWomenUrls, minimumValue,
				maximumValue, minimumWeightLb, maximumWeightLb, unit,
				sportsCategoryId, exerciseName, instructions,
				measurementUnitTypeId, circuitName, videoUrlId, series,
				sportId, weekId, dayId);
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.parser.JsonParser#parse(org.json.JSONArray)
	 */
	@Override
	public List<Exercise> parse(JSONArray array) throws JSONException {
		final int count = array.length();
		final List<Exercise> exercises = new ArrayList<Exercise>(count);
		for (int i = 0; i < count; i++) {
			exercises.add(parse(array.getJSONObject(i)));
		}
		return exercises;
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.parser.ContentValuesParser#parse(java.lang.Object, android.content.ContentValues)
	 */
	@Override
	public ContentValues parse(Exercise object, ContentValues values) {

		if (values == null) {
			values = new ContentValues();
		}

		values.put(SportsWorldContract.Exercise.MUSCLE_WORKED,
				object.getMuscleWorked());
		values.put(SportsWorldContract.Exercise.CURRENT,
				object.getMuscleWorked());
		values.put(SportsWorldContract.Exercise.CIRCUIT_ID,
				object.getCircuitId());
		values.put(SportsWorldContract.Exercise.EXAMPLE_IMAGES_MEN_URLS,
				object.getExampleImagesMenUrlsJoined());
		values.put(SportsWorldContract.Exercise.EXAMPLE_IMAGES_WOMEN_URLS,
				object.getExampleImagesWomenUrlsJoined());
		values.put(SportsWorldContract.Exercise.MINIMUM_VALUE,
				object.getMinimumValue());
		values.put(SportsWorldContract.Exercise.MAXIMUM_VALUE,
				object.getMaximumValue());
		values.put(SportsWorldContract.Exercise.MINIMUM_WEIGHT_LB,
				object.getMinimumWeightLb());
		values.put(SportsWorldContract.Exercise.MAXIMUM_WEIGHT_LB,
				object.getMaximumWeightLb());
		values.put(SportsWorldContract.Exercise.UNIT, object.getUnit());
		values.put(SportsWorldContract.Exercise.SPORTS_CATEGORY_ID,
				object.getSportsCategoryId());
		if (object.getName().equals("Nutrici�n"))
			values.put(SportsWorldContract.Exercise.EXERCISE_NAME,
					object.getName());
		else
			values.put(SportsWorldContract.Exercise.EXERCISE_NAME, "S"
					+ countChilds + " " + object.getName());
		values.put(SportsWorldContract.Exercise.INSTRUCTIONS,
				object.getInstructions());
		values.put(SportsWorldContract.Exercise.MEASUREMENT_UNIT_TYPE_ID,
				object.getMeasurementUnitTypeId());
		values.put(SportsWorldContract.Exercise.CIRCUIT_NAME,
				object.getCircuitName());
		values.put(SportsWorldContract.Exercise.VIDEO_URL_ID,
				object.getVideoUrlId());
		values.put(SportsWorldContract.Exercise.SERIES, object.getSeries());
		values.put(SportsWorldContract.Exercise.SPORT_ID, object.getSportId());
		values.put(SportsWorldContract.Exercise.WEEK_ID, object.getWeekId());
		values.put(SportsWorldContract.Exercise.DAY_ID, object.getDayId());
		// values.put(SportsWorldContract.Exercise._ID, object.getId());

		return values;

	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.parser.CursorParser#parse(android.database.Cursor)
	 */
	@Override
	public List<Exercise> parse(Cursor cursor) {

		int oldPos = cursor.getPosition();
		cursor.moveToPosition(-1);

		final int indexId = cursor
				.getColumnIndex(SportsWorldContract.Exercise._ID);
		final int indexDone = cursor
				.getColumnIndex(SportsWorldContract.Exercise.DONE);
		final int indexMuscleWorked = cursor
				.getColumnIndex(SportsWorldContract.Exercise.MUSCLE_WORKED);
		final int indexActive = cursor
				.getColumnIndex(SportsWorldContract.Exercise.CURRENT);
		final int indexCircuitId = cursor
				.getColumnIndex(SportsWorldContract.Exercise.CIRCUIT_ID);
		final int indexExampleImagesMenUrls = cursor
				.getColumnIndex(SportsWorldContract.Exercise.EXAMPLE_IMAGES_MEN_URLS);
		final int indexExampleImagesWomenUrls = cursor
				.getColumnIndex(SportsWorldContract.Exercise.EXAMPLE_IMAGES_WOMEN_URLS);
		final int indexMinimumValue = cursor
				.getColumnIndex(SportsWorldContract.Exercise.MINIMUM_VALUE);
		final int indexMaximumValue = cursor
				.getColumnIndex(SportsWorldContract.Exercise.MAXIMUM_VALUE);
		final int indexMinimumWeightLb = cursor
				.getColumnIndex(SportsWorldContract.Exercise.MINIMUM_WEIGHT_LB);
		final int indexMaximumWeightLb = cursor
				.getColumnIndex(SportsWorldContract.Exercise.MAXIMUM_WEIGHT_LB);
		final int indexUnit = cursor
				.getColumnIndex(SportsWorldContract.Exercise.UNIT);
		final int indexSportsCategoryId = cursor
				.getColumnIndex(SportsWorldContract.Exercise.SPORTS_CATEGORY_ID);
		final int indexExerciseName = cursor
				.getColumnIndex(SportsWorldContract.Exercise.EXERCISE_NAME);
		final int indexInstructions = cursor
				.getColumnIndex(SportsWorldContract.Exercise.INSTRUCTIONS);
		final int indexMeasurementUnitType = cursor
				.getColumnIndex(SportsWorldContract.Exercise.MEASUREMENT_UNIT_TYPE_ID);
		final int indexCircuitName = cursor
				.getColumnIndex(SportsWorldContract.Exercise.CIRCUIT_NAME);
		final int indexVideoUrlId = cursor
				.getColumnIndex(SportsWorldContract.Exercise.VIDEO_URL_ID);
		final int indexSeries = cursor
				.getColumnIndex(SportsWorldContract.Exercise.SERIES);
		final int indexSportId = cursor
				.getColumnIndex(SportsWorldContract.Exercise.SPORT_ID);
		final int indexWeekId = cursor
				.getColumnIndex(SportsWorldContract.Exercise.WEEK_ID);
		final int indexDayId = cursor
				.getColumnIndex(SportsWorldContract.Exercise.DAY_ID);

		final int count = cursor.getCount();
		final List<Exercise> exercises = new ArrayList<Exercise>();
		for (int i = 0; i < count; i++) {

			cursor.moveToPosition(i);

			long id = 0;
			if (indexId >= 0) {
				id = cursor.getLong(indexId);
			}

			boolean done = false;
			if (indexDone >= 0) {
				done = (cursor.getInt(indexDone) == 1);
			}

			String muscleWorked = null;
			if (indexMuscleWorked >= 0) {
				muscleWorked = cursor.getString(indexMuscleWorked);
			}

			boolean active = false;
			if (indexActive >= 0) {
				active = STATUS_ACTIVE.equals(cursor.getString(indexActive));
			}

			long circuitId = 0L;
			if (indexCircuitId >= 0) {
				circuitId = cursor.getLong(indexCircuitId);
			}

			String[] exampleImagesMenUrls = new String[0];
			if (indexExampleImagesMenUrls >= 0) {
				final String joined = cursor
						.getString(indexExampleImagesMenUrls);
				if (joined != null) {
					exampleImagesMenUrls = TextUtils.split(joined,
							Exercise.DELIMITER);
				}
			}

			String[] exampleImagesWomenUrls = new String[0];
			if (indexExampleImagesWomenUrls >= 0) {
				final String joined = cursor
						.getString(indexExampleImagesWomenUrls);
				if (joined != null) {
					exampleImagesWomenUrls = TextUtils.split(joined,
							Exercise.DELIMITER);
				}
			}

			double minimumValue = 0d;
			if (indexMinimumValue >= 0) {
				minimumValue = cursor.getDouble(indexMinimumValue);
			}

			double maximumValue = 0d;
			if (indexMaximumValue >= 0) {
				maximumValue = cursor.getDouble(indexMaximumValue);
			}

			double minimumWeightLb = 0d;
			if (indexMinimumWeightLb >= 0) {
				minimumWeightLb = cursor.getDouble(indexMinimumWeightLb);
			}

			double maximumWeightLb = 0d;
			if (indexMaximumWeightLb >= 0) {
				maximumWeightLb = cursor.getDouble(indexMaximumWeightLb);
			}

			String unit = null;
			if (indexUnit >= 0) {
				unit = cursor.getString(indexUnit);
			}

			long sportsCategoryId = 0L;
			if (indexSportsCategoryId >= 0) {
				sportsCategoryId = cursor.getLong(indexSportsCategoryId);
			}

			String exerciseName = null;
			if (indexExerciseName >= 0) {
				exerciseName = cursor.getString(indexExerciseName);
			}

			String instructions = null;
			if (indexInstructions >= 0) {
				instructions = cursor.getString(indexInstructions);
			}

			long measurementUnitTypeId = 0L;
			if (indexMeasurementUnitType >= 0) {
				measurementUnitTypeId = cursor
						.getLong(indexMeasurementUnitType);
			}

			String circuitName = null;
			if (indexCircuitName >= 0) {
				circuitName = cursor.getString(indexCircuitName);
			}

			long videoUrlId = 0L;
			if (indexVideoUrlId >= 0) {
				videoUrlId = cursor.getLong(indexVideoUrlId);
			}

			long series = 0L;
			if (indexSeries >= 0) {
				series = cursor.getLong(indexSeries);
			}

			long sportId = 0L;
			if (indexSportId >= 0) {
				sportId = cursor.getLong(indexSportId);
			}

			long weekId = Exercise.NO_CURRENT_WEEK_ID;
			if (indexWeekId >= 0) {
				weekId = cursor.getLong(indexWeekId);
			}

			long dayId = Exercise.NO_CURRENT_DAY_ID;
			if (indexDayId >= 0) {
				weekId = cursor.getLong(indexDayId);
			}

			final Exercise exercise = new Exercise(id, muscleWorked, active,
					circuitId, exampleImagesMenUrls, exampleImagesWomenUrls,
					minimumValue, maximumValue, minimumWeightLb,
					maximumWeightLb, unit, sportsCategoryId, exerciseName,
					instructions, measurementUnitTypeId, circuitName,
					videoUrlId, series, sportId, weekId, dayId, done);

			exercises.add(exercise);

		}

		cursor.moveToPosition(oldPos);

		return exercises;

	}

}
