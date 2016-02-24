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

import android.util.Log;

import mx.com.sportsworld.sw.model.Exercise;
import mx.com.sportsworld.sw.pojo.TraininPojo;

/**
 * The Class TraininParse.
 */
public class TraininParse implements JsonParser<TraininPojo> {

	/** The Constant KEY_ACTIVITIES. */
	private static final String KEY_ACTIVITIES = "activities";
	
	/** The Constant KEY_IMAGE. */
	private static final String KEY_IMAGE = "image";
	
	/** The Constant KEY_EXERCISE. */
	private static final String KEY_EXERCISE = "exercise";

	/** The active week id. */
	long activeWeekId = Exercise.NO_CURRENT_WEEK_ID;
	
	/** The active day id. */
	long activeDayId = Exercise.NO_CURRENT_DAY_ID;
	
	
	/* (non-Javadoc)
	 * @see com.sportsworld.android.parser.JsonParser#parse(org.json.JSONObject)
	 */
	@Override
	public TraininPojo parse(JSONObject object) throws JSONException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.parser.JsonParser#parse(org.json.JSONArray)
	 */
	@Override
	public List<TraininPojo> parse(JSONArray array) throws JSONException {
		final int count = array.length();
        final List<TraininPojo> trainingArray = new ArrayList<TraininPojo>(
                count);
        for (int i = 0; i < count; i++) {
        	JSONObject object=array.getJSONObject(i);
        	final String images = object.optString(KEY_IMAGE);
    		final String excercise = object.optString(KEY_EXERCISE);
    		
    		final JSONArray traininArray=object.getJSONArray(KEY_ACTIVITIES);
    		final List<Exercise> traininList = parseExcercises(traininArray);
    		trainingArray.add(new TraininPojo(traininList, images, excercise));
        }
		return trainingArray;
	}

	/**
	 * Parses the excercises.
	 * 
	 * @param jsonArrayExercises
	 *            the json array exercises
	 * @return the list
	 */
	public List<Exercise> parseExcercises(JSONArray jsonArrayExercises) {
		List<Exercise> exercises = null;
		
		if (jsonArrayExercises != null) {
			

			final ExerciseParser exerciseParser = new ExerciseParser();

			exercises = new ArrayList<Exercise>();
			
			try{
			
				for (int j = 0; j < jsonArrayExercises.length(); j++) {
					final JSONObject jsonObjExercise = jsonArrayExercises
							.getJSONObject(j);
					jsonObjExercise.put(ExerciseParser.KEY_WEEK_ID,
							activeWeekId);
					jsonObjExercise.put(ExerciseParser.KEY_DAY_ID, activeDayId);
				}
				exercises.addAll(exerciseParser.parse(jsonArrayExercises));

			
			}catch(Exception ex){
				Log.i("LogIron", ex.toString());
			}

		}
		return exercises;
	}

}
