package mx.com.sportsworld.sw.service;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.SparseArray;

import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
import mx.com.sportsworld.sw.model.Exercise;
import mx.com.sportsworld.sw.model.Routine;
import mx.com.sportsworld.sw.model.WeekDayRelationship;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.resource.RoutineResource;
import mx.com.sportsworld.sw.parser.ExerciseParser;
import mx.com.sportsworld.sw.parser.RoutineParser;
import mx.com.sportsworld.sw.parser.WeekDayRelationshipParser;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.provider.BatchOperation;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

/**
 * Get Routine details from server and save them on database.
 * 
 */
public class GetRoutineService extends SportsWorldWebApiService<Routine> {

	/** The Constant EXTRA_ROUTINE_ID. */
	public static final String EXTRA_ROUTINE_ID = "com.upster.extra.ROUTINE_ID";
	
	/** The Constant COL_USER_INDEX_WEIGHT. */
	private static final int COL_USER_INDEX_WEIGHT = 0;
	
	/** The Constant COL_USER_INDEX_GENDER_ID. */
	private static final int COL_USER_INDEX_GENDER_ID = 1;
	
	/** The Constant COL_USER_INDEX_AGE. */
	private static final int COL_USER_INDEX_AGE = 2;
	
	/** The Constant COL_USER_ROUTINE_ID. */
	private static final int COL_USER_ROUTINE_ID = 3;
	
	/** The Constant COLS_USER. */
	private static final String[] COLS_USER = buildColumns();
	
	/** The next routine. */
	private static boolean nextRoutine = false;

	/**
	 * Builds the columns.
	 * 
	 * @return the string[]
	 */
	private static String[] buildColumns() {
		final SparseArray<String> colsMap = new SparseArray<String>();
		colsMap.put(COL_USER_INDEX_WEIGHT, SportsWorldContract.User.WEIGHT);
		colsMap.put(COL_USER_INDEX_GENDER_ID,
				SportsWorldContract.User.GENDER_ID);
		colsMap.put(COL_USER_INDEX_AGE, SportsWorldContract.User.AGE);
		colsMap.put(COL_USER_ROUTINE_ID, SportsWorldContract.User.ROUTINE_ID);

		final int colCount = colsMap.size();
		final String[] cols = new String[colCount];
		for (int i = 0; i < colCount; i++) {
			cols[i] = colsMap.get(i);
		}

		return cols;
	}

	/**
	 * Instantiates a new gets the routine service.
	 */
	public GetRoutineService() {
		super(GetRoutineService.class.getName());
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.service.SportsWorldWebApiService#makeApiCall(android.content.Intent)
	 */
	@Override
	public RequestResult<Routine> makeApiCall(Intent intent)
			throws IOException, JSONException {
		final SportsWorldAccountManager accountMngr = getAccountManager();
		final long userId = Long.parseLong(accountMngr.getCurrentUserId());
		final long routineId = intent.getLongExtra(EXTRA_ROUTINE_ID, -1L);

		if (routineId == -1L) {
			throw new IllegalArgumentException(
					"You must pass a EXTRA_LOCATION_ROUTINE_ID long extra");
		}

		Cursor cursor = null;
		try {

			final Uri userUri = SportsWorldContract.User.buildUserUri(String
					.valueOf(userId));
			cursor = getContentResolver()
					.query(userUri, COLS_USER, null/* selection */,
							null /* selectionArgs */, null /* sortOrder */);

			if (!cursor.moveToFirst()) {
				getAccountManager().logOut();
				throw new RuntimeException(
						"User not found!. Log out and the crash."
								+ " This should never happen.");
			}
			int age = 0;
			double weight = 0;
			long genderId = 0;
			boolean doingRoutineId = false;
			boolean isMember = false;

			if (!SportsWorldPreferences.getGuestId(getApplicationContext())
					.equals("")) {
				age = Integer.parseInt(SportsWorldPreferences
						.getGuestAge(getApplicationContext()));
				weight = Integer.parseInt(SportsWorldPreferences
						.getGuestWeight(getApplicationContext()));
				if (SportsWorldPreferences.getGuestGender(
						getApplicationContext()).equals("Femenino"))
					genderId = 12;
				else
					genderId = 13;
			} else {

				age = cursor.getInt(COL_USER_INDEX_AGE);
				weight = cursor.getDouble(COL_USER_INDEX_WEIGHT);
				genderId = cursor.getLong(COL_USER_INDEX_GENDER_ID);
				doingRoutineId = (cursor.getLong(COL_USER_ROUTINE_ID) != -1L);
				isMember = accountMngr.isLoggedInAsMember();
			}
			if (SportsWorldPreferences.getRoutineId(getApplicationContext()) != 0)
				doingRoutineId = true;
			else
				doingRoutineId = false;

			return RoutineResource.getRoutine(getApplicationContext(),
					((isMember && doingRoutineId) ? userId : 0L), routineId,
					age, weight, genderId);

		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.service.SportsWorldWebApiService#proccessRequestResult(android.content.Intent, com.sportsworld.android.net.RequestResultParser.RequestResult)
	 */
	@SuppressWarnings("static-access")
	@Override
	public boolean proccessRequestResult(Intent intent,
			RequestResult<Routine> result) {

		final List<Routine> routines = result.getData();

		if (routines == null) {
			maySendResult(intent, Activity.RESULT_CANCELED, result,
					ERROR_CONNECTION);

			return false;
		}

		if (routines.size() == 0)

			return true;

		final ContentResolver cr = getContentResolver();
		final BatchOperation batchOperation = new BatchOperation(
				SportsWorldContract.CONTENT_AUTHORITY, cr);

		final ContentValues values = new ContentValues();
		final int routinesCount = routines.size();
		final RoutineParser routineParser = new RoutineParser();
		final ExerciseParser exerciseParser = new ExerciseParser();
		final WeekDayRelationshipParser weekDayRelationshipParser = new WeekDayRelationshipParser();

		final ContentProviderOperation deleteAllRoutines = ContentProviderOperation
				.newDelete(SportsWorldContract.Routine.CONTENT_URI)
				.withYieldAllowed(true).build();
		final ContentProviderOperation deleteAllExcercises = ContentProviderOperation
				.newDelete(SportsWorldContract.Exercise.CONTENT_URI)
				.withYieldAllowed(true).build();
		final ContentProviderOperation deleteAllTrainin = ContentProviderOperation
				.newDelete(SportsWorldContract.Trainin.CONTENT_URI)
				.withYieldAllowed(true).build();

		batchOperation.add(deleteAllRoutines);
		batchOperation.add(deleteAllExcercises);
		batchOperation.add(deleteAllTrainin);

		if (routinesCount > 0) {

			final Routine routine = routines.get(0); // User can have only one
														// routine.
			SportsWorldPreferences.setRoutineName(getApplicationContext(),
					routine.getmRoutine_name());
			final ContentProviderOperation deleteAllWeekDayRelationShip = ContentProviderOperation
					.newDelete(
							SportsWorldContract.WeekDayRelationship.CONTENT_URI)
					.withYieldAllowed(true).build();
			batchOperation.add(deleteAllWeekDayRelationShip);

			long activeWeekId = Exercise.NO_CURRENT_WEEK_ID;
			long activeDayId = Exercise.NO_CURRENT_DAY_ID;

			final List<WeekDayRelationship> weekDayRelationshipList = routine
					.getWeekDayRelationship();
			final int weekDayRelationshipCount = (weekDayRelationshipList == null) ? 0
					: weekDayRelationshipList.size();
			long lastWeek = 0;
			int numWeeks = 0;
			int numDay = 1;
			for (int j = 0; j < weekDayRelationshipCount; j++) {
				final WeekDayRelationship weekDayRelationship = weekDayRelationshipList
						.get(j);
				weekDayRelationshipParser.parse(weekDayRelationship, values);

				if (lastWeek != weekDayRelationship.getWeekId()) {
					numWeeks++;
					numDay = 1;
				}
				if (lastWeek == weekDayRelationship.getWeekId())
					numDay++;

				if (nextRoutine) {
					activeWeekId = weekDayRelationship.getWeekId();
					activeDayId = weekDayRelationship.getDayId();
					SportsWorldPreferences.setNewRoutineDay(
							getApplicationContext(), activeDayId + "");
					SportsWorldPreferences.setNewRoutineWeek(
							getApplicationContext(), activeWeekId + "");
					nextRoutine = false;
				}

				if (weekDayRelationship.isActive()) {
					activeWeekId = weekDayRelationship.getWeekId();
					activeDayId = weekDayRelationship.getDayId();
					SportsWorldPreferences.setCurrentWeekIdDayId(
							getApplicationContext(), activeWeekId, activeDayId);
					nextRoutine = true;

					SportsWorldPreferences.setRoutineDay(
							getApplicationContext(), numDay + "");
					SportsWorldPreferences.setRoutineWeek(
							getApplicationContext(), numWeeks + "");

				}

				final ContentProviderOperation insertWeekDayRelationshipOp = ContentProviderOperation
						.newInsert(
								SportsWorldContract.WeekDayRelationship.CONTENT_URI)
						.withYieldAllowed(true).withValues(values).build();
				batchOperation.add(insertWeekDayRelationshipOp);
				values.clear();

				lastWeek = weekDayRelationship.getWeekId();
			}

			routineParser.parse(routine, values);

			final ContentProviderOperation insertRoutineOp = ContentProviderOperation
					.newInsert(SportsWorldContract.Routine.CONTENT_URI)
					.withYieldAllowed(true).withValues(values).build();
			batchOperation.add(insertRoutineOp);
			values.clear();

			for (int a = 0; a < routine.getmTraining().size(); a++) {

				routineParser.parse(routine.getmTraining().get(a), values);

				final ContentProviderOperation insertTraininDB = ContentProviderOperation
						.newInsert(SportsWorldContract.Trainin.CONTENT_URI)
						.withYieldAllowed(true).withValues(values).build();
				batchOperation.add(insertTraininDB);
				values.clear();
			}
			List<Exercise> exercisesOnDb = new ArrayList<Exercise>();

			if ((activeWeekId == Exercise.NO_CURRENT_WEEK_ID)
					|| (activeDayId == Exercise.NO_CURRENT_DAY_ID)) {
				final ContentProviderOperation deleteAllExercises = ContentProviderOperation
						.newDelete(SportsWorldContract.Exercise.CONTENT_URI)
						.withYieldAllowed(true).build();
				batchOperation.add(deleteAllExercises);
			} else {
				Cursor cursor = null;
				try {
					final Uri uri = SportsWorldContract.Exercise
							.buildExerciseWeekDay("0", "0");
					cursor = cr.query(uri,
							new String[] { SportsWorldContract.Exercise._ID },
							null /* selection */, null /* selectionArgs */,
							SportsWorldContract.Exercise._ID + " ASC");
					exercisesOnDb = exerciseParser.parse(cursor);
				} finally {
					if (cursor != null) {
						cursor.close();
					}
				}
			}
			for (int a = 0; a < routine.getmTraining().size(); a++) {

				final List<Long> idsOndb = new ArrayList<Long>();
				int exercisesOnDbCount = exercisesOnDb.size();
				for (int i = 0; i < exercisesOnDbCount; i++) {
					idsOndb.add(exercisesOnDb.get(i).getId());
				}

				final List<Exercise> exercises = routine.getmTraining().get(a)
						.getListExcercise();
				final int exerciseCount = (exercises == null) ? 0 : exercises
						.size();

				exerciseParser.countChilds = 0;
				for (int j = 0; j < exerciseCount; j++) {

					final Exercise exercise = exercises.get(j);

					exerciseParser.countChilds = exerciseParser.countChilds + 1;
					exerciseParser.parse(exercise, values);

					if (idsOndb.contains(exercise.getId())) {

						values.remove(SportsWorldContract.Exercise.DONE);

						final Uri exerciseUri = SportsWorldContract.Exercise
								.buildExerciseUri(exercise.getId());
						final ContentProviderOperation insertExerciseOp = ContentProviderOperation
								.newUpdate(exerciseUri).withYieldAllowed(true)
								.withValues(values).build();
						batchOperation.add(insertExerciseOp);

					} else {

						final ContentProviderOperation insertExerciseOp = ContentProviderOperation
								.newInsert(
										SportsWorldContract.Exercise.CONTENT_URI)
								.withYieldAllowed(true).withValues(values)
								.build();
						batchOperation.add(insertExerciseOp);

					}

					values.clear();
				}
			}

		}
		batchOperation.execute();
		return true;
	}
}
