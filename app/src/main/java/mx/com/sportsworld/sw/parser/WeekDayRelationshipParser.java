package mx.com.sportsworld.sw.parser;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

import mx.com.sportsworld.sw.model.WeekDayRelationship;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

/**
 * The Class WeekDayRelationshipParser.
 */
public class WeekDayRelationshipParser implements
		JsonParser<WeekDayRelationship>,
		ContentValuesParser<WeekDayRelationship>,
		CursorParser<WeekDayRelationship> {

	/** The Constant KEY_WEEK_ID. */
	private static final String KEY_WEEK_ID = "week";
	
	/** The Constant KEY_DAY_ID. */
	private static final String KEY_DAY_ID = "day";
	
	/** The Constant KEY_ACTIVE. */
	private static final String KEY_ACTIVE = "active";

	/* (non-Javadoc)
	 * @see com.sportsworld.android.parser.JsonParser#parse(org.json.JSONObject)
	 */
	@Override
	public WeekDayRelationship parse(JSONObject object) throws JSONException {
		final long weekId = object.optLong(KEY_WEEK_ID);
		final long dayId = object.optLong(KEY_DAY_ID);
		final boolean active = object.optBoolean(KEY_ACTIVE);
		return new WeekDayRelationship(weekId, dayId, active);
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.parser.JsonParser#parse(org.json.JSONArray)
	 */
	@Override
	public List<WeekDayRelationship> parse(JSONArray array)
			throws JSONException {
		final int count = array.length();
		final List<WeekDayRelationship> weekDayRelationships = new ArrayList<WeekDayRelationship>(
				count);
		for (int i = 0; i < count; i++) {
			/*
			 * Unfortunately, we get an array of objects that simply contains
			 * the WeekDayRelationship object. Thus, we should get that
			 * anonymous json object and then get the weekday. Since anonymous
			 * is an object, we can't just ask for the WeekDayRelationship. We
			 * need a label for that.
			 */
			final JSONObject jsonObjectAnonymousObjects = array
					.getJSONObject(i);
			final List<String> keys = new ArrayList<String>();
			final Iterator<String> it = jsonObjectAnonymousObjects.keys();
			while (it.hasNext()) {
				keys.add(it.next());
			}
			final int keysCount = keys.size();
			for (int j = 0; j < keysCount; j++) {
				weekDayRelationships.add(parse(jsonObjectAnonymousObjects
						.getJSONObject(keys.get(j))));
			}
		}
		return weekDayRelationships;
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.parser.ContentValuesParser#parse(java.lang.Object, android.content.ContentValues)
	 */
	@Override
	public ContentValues parse(WeekDayRelationship object, ContentValues values) {
		if (values == null) {
			values = new ContentValues();
		}
		values.put(SportsWorldContract.WeekDayRelationship.WEEK_ID,
				object.getWeekId());
		values.put(SportsWorldContract.WeekDayRelationship.DAY_ID,
				object.getDayId());
		values.put(SportsWorldContract.WeekDayRelationship.ACTIVE,
				object.isActive());
		return values;
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.parser.CursorParser#parse(android.database.Cursor)
	 */
	@Override
	public List<WeekDayRelationship> parse(Cursor cursor) {

		final int oldPos = cursor.getPosition();
		cursor.moveToPosition(-1);

		final int indexWeekId = cursor
				.getColumnIndex(SportsWorldContract.WeekDayRelationship.WEEK_ID);
		final int indexDayId = cursor
				.getColumnIndex(SportsWorldContract.WeekDayRelationship.DAY_ID);
		final int indexActive = cursor
				.getColumnIndex(SportsWorldContract.WeekDayRelationship.ACTIVE);

		final List<WeekDayRelationship> weekDayRelationship = new ArrayList<WeekDayRelationship>();
		while (cursor.moveToNext()) {

			long weekId = -1L;
			if (indexWeekId >= 0) {
				weekId = cursor.getLong(indexWeekId);
			}

			long dayId = -1L;
			if (indexDayId >= 0) {
				dayId = cursor.getLong(indexDayId);
			}

			boolean active = false;
			if (indexActive >= 0) {
				active = (cursor.getInt(indexActive) == 1);
			}

			weekDayRelationship.add(new WeekDayRelationship(weekId, dayId,
					active));
		}

		cursor.moveToPosition(oldPos);

		return weekDayRelationship;
	}

}
