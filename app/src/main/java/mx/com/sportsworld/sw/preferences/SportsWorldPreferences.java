package mx.com.sportsworld.sw.preferences;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * This class saves and loads our app's global preferences.
 * 
 */
public final class SportsWorldPreferences {

	private static final String PREF_AUTH_KEY = "auth_key";
	private static final String PREF_USERNAME = "username";
	private static final String PREF_PASSWORD = "password";
	private static final String PREF_CURRENT_USER_ID = "current_user_id";
	private static final String PREF_CURRENT_WEEK_ID = "current_week_id";
	private static final String PREF_CURRENT_DAY_ID = "current_day_id";
	private static final String PREF_ROUTINE_DAY = "routine_day";
	private static final String PREF_LOG_OUT = "log_out";
	private static final String PREF_ROUTINE_WEEK = "routine_week";
	private static final String PREF_ROUTINE_ID = "routine_id";
	private static final String PREF_IF_ROUTINE = "routine_active";
	private static final String PREF_NEW_ROUTINE_DAY = "new_routine_day";
	private static final String PREF_NEW_ROUTINE_WEEK = "new_routine_week";
	private static final String PREF_ROUTINE_NAME = "routine_name";
	private static final String PREF_TWITTER = "log_twitter";
	private static final String PREF_BRANCH_SORT = "sort_branch";
	private static final String PREF_CALENDAR_VISIBLE = "caldenar_show";
	private static final String PREF_CLUB_ADVICE = "club_advice";
	private static final String PREF_GUEST_HEIGH = "guest_heigh";
	private static final String PREF_GUEST_WIDTH = "guest_width";
	private static final String PREF_GUEST_ID = "guest_id";
	private static final String PREF_GUEST_GENDER = "guest_gender";
	private static final String PREF_GUEST_NAME = "guest_name";
	private static final String PREF_GUEST_AGE = "guest_age";
	private static final String PREF_SELECTED_GOAL = "goal_selected";
	private static final String PREF_SELECTED_LEVEL = "level_selected";
	private static final String PREF_BRANCH_TYPE = "brach_type";
	private static final String PREF_FIRST_TIME = "first_time";
	private static final String PREF_INFO_ROUTINE = "info_routine";
	private static final String PREF_SALDO_ACTUAL = "saldo_actual";
	private static final String PREF_PUNTOS_REDIMIDOS = "puntos_redimidos";
	private static final String PREF_ID_CLUB = "id_club";
	// Twitter
	private static final String PREF_TWITTER_LOGGED = "twitter_pref";
	private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	private static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
	

	// private static final long NO_CURRENT_USER = -1L;
	private static final long NO_CURRENT_DAY_ID = 0L;
	private static final long NO_CURRENT_WEEK_ID = 0L;

	// private static String
	// authDefaultToken="02b7f1c16c25c3d156756163a8d1f6db4da2ff34";

	private SportsWorldPreferences() {
	}

	/**
	 * 
	 * @param context
	 * @return The authToken or {@code null} if there is no authToken.
	 */

	public static String getInfoRoutine(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getString(PREF_INFO_ROUTINE, null);
	}

	public static void setInfoRoutine(Context context, String routine) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putString(PREF_INFO_ROUTINE, routine);
		prefEditor.apply();
	}

	public static String getBranchType(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getString(PREF_BRANCH_TYPE, null);
	}

	public static void setBranchType(Context context, String branch) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putString(PREF_BRANCH_TYPE, branch);
		prefEditor.apply();
	}

	public static int getPuntosRedimidos(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getInt(PREF_PUNTOS_REDIMIDOS, 0);
	}

	public static void setPuntosRedimidos(Context context, int puntos) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putInt(PREF_PUNTOS_REDIMIDOS, puntos);
		prefEditor.apply();
	}

	public static int getIdClub(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getInt(PREF_ID_CLUB, 0);
	}

	public static void setIdClub(Context context, int club) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putInt(PREF_ID_CLUB, club);
		prefEditor.apply();
	}

	public static int getSaldoActual(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getInt(PREF_SALDO_ACTUAL, 0);
	}

	public static void setSaldoActual(Context context, int saldo) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putInt(PREF_SALDO_ACTUAL, saldo);
		prefEditor.apply();
	}

	public static String getSelectedGoal(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getString(PREF_SELECTED_GOAL, null);
	}

	public static void setSelectedGoal(Context context, String goal) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putString(PREF_SELECTED_GOAL, goal);
		prefEditor.apply();
	}

	public static String getSelectedLevel(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getString(PREF_SELECTED_LEVEL, null);
	}

	public static void setSelectedLevel(Context context, String level) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putString(PREF_SELECTED_LEVEL, level);
		prefEditor.apply();
	}

	public static String getAuthToken(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getString(PREF_AUTH_KEY, null);
	}

	public static void setAuthToken(Context context, String authKey) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putString(PREF_AUTH_KEY, authKey);
		prefEditor.apply();
	}

	public static String getUsername(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getString(PREF_USERNAME, null);
	}

	public static void setUsername(Context context, String username) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putString(PREF_USERNAME, username);
		prefEditor.apply();
	}

	public static String getPassword(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getString(PREF_PASSWORD, null);
	}

	public static void setPassword(Context context, String password) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putString(PREF_PASSWORD, password);
		prefEditor.apply();
	}

	public static String getRoutineName(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getString(PREF_ROUTINE_NAME, null);
	}

	public static void setRoutineName(Context context, String name) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putString(PREF_ROUTINE_NAME, name);
		prefEditor.apply();
	}

	public static long getRoutineId(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getLong(PREF_ROUTINE_ID, 0);
	}

	public static void setRoutineId(Context context, long name) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putLong(PREF_ROUTINE_ID, name);
		prefEditor.apply();
	}

	public static boolean isRoutine(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getBoolean(PREF_IF_ROUTINE, false);
	}

	public static void setRoutine(Context context, boolean isRoutine) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putBoolean(PREF_IF_ROUTINE, isRoutine);
		prefEditor.apply();
	}

	public static void setLogOut(Context context, boolean logOut) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putBoolean(PREF_LOG_OUT, logOut);
		prefEditor.apply();
	}

	public static boolean getLogOut(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getBoolean(PREF_LOG_OUT, false);
	}

	public static void setTwitterAct(Context context, boolean logOut) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putBoolean(PREF_TWITTER, logOut);
		prefEditor.apply();
	}

	public static boolean getTwitterAct(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getBoolean(PREF_TWITTER, false);
	}

	public static void clearAllPreferences(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		sharedPref.edit().clear().apply();
	}

	public static void setCurrentUserId(Context context, String localDbId) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putString(PREF_CURRENT_USER_ID, localDbId);
		prefEditor.apply();
	}

	public static String getCurrentUserId(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getString(PREF_CURRENT_USER_ID, null);
	}

	public static void setCurrentWeekIdDayId(Context context, long weekId,
			long dayId) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putLong(PREF_CURRENT_WEEK_ID, weekId);
		prefEditor.putLong(PREF_CURRENT_DAY_ID, dayId);
		prefEditor.apply();
	}

	public static long getCurrentWeekId(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getLong(PREF_CURRENT_WEEK_ID, NO_CURRENT_WEEK_ID);
	}

	public static long getCurrentDayId(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getLong(PREF_CURRENT_DAY_ID, NO_CURRENT_DAY_ID);
	}

	private static SharedPreferences getSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static void setRoutineWeek(Context context, String week) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putString(PREF_ROUTINE_WEEK, week);
		prefEditor.apply();
	}

	public static String getSortListBranchPref(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getString(PREF_BRANCH_SORT, null);
	}

	public static void setSortListBranchPref(Context context, String sort) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putString(PREF_BRANCH_SORT, sort);
		prefEditor.apply();
	}

	public static boolean isCalendarShow(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getBoolean(PREF_CALENDAR_VISIBLE, false);
	}

	public static void setCalendarShow(Context context, boolean visible) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putBoolean(PREF_CALENDAR_VISIBLE, visible);
		prefEditor.apply();
	}

	public static boolean isChckBoxAdvice(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getBoolean(PREF_CLUB_ADVICE, true);
	}

	public static void setChckBoxAdvice(Context context, boolean advice) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putBoolean(PREF_CLUB_ADVICE, advice);
		prefEditor.apply();
	}

	public static String getRoutineWeek(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getString(PREF_ROUTINE_WEEK, "1");
	}

	public static void setRoutineDay(Context context, String day) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putString(PREF_ROUTINE_DAY, day);
		prefEditor.apply();
	}

	public static String getRoutinetDay(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getString(PREF_ROUTINE_DAY, "1");
	}

	public static void setNewRoutineWeek(Context context, String week) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putString(PREF_NEW_ROUTINE_WEEK, week);
		prefEditor.apply();
	}

	public static String getNewRoutineWeek(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getString(PREF_NEW_ROUTINE_WEEK, "0");
	}

	public static void setNewRoutineDay(Context context, String day) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putString(PREF_NEW_ROUTINE_DAY, day);
		prefEditor.apply();
	}

	public static String getNewRoutinetDay(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getString(PREF_NEW_ROUTINE_DAY, "0");
	}

	public static void setGuestHeigh(Context context, String heigh) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putString(PREF_GUEST_HEIGH, heigh);
		prefEditor.apply();
	}

	public static String getGuestHeigh(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getString(PREF_GUEST_HEIGH, "0");
	}

	public static void setGuestWeight(Context context, String width) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putString(PREF_GUEST_WIDTH, width);
		prefEditor.apply();
	}

	public static String getGuestWeight(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getString(PREF_GUEST_WIDTH, "0");
	}

	public static void setGuestGender(Context context, String gender) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putString(PREF_GUEST_GENDER, gender);
		prefEditor.apply();
	}

	public static String getGuestGender(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getString(PREF_GUEST_GENDER, "0");
	}

	public static void setGuestId(Context context, String id) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putString(PREF_GUEST_ID, id);
		prefEditor.apply();
	}

	public static String getGuestId(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getString(PREF_GUEST_ID, "");
	}

	public static void setGuestName(Context context, String name) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putString(PREF_GUEST_NAME, name);
		prefEditor.apply();
	}

	public static String getGuestName(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getString(PREF_GUEST_NAME, "");
	}

	public static void setGuestAge(Context context, String age) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putString(PREF_GUEST_AGE, age);
		prefEditor.apply();
	}

	public static String getGuestAge(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getString(PREF_GUEST_AGE, "0");
	}

	public static boolean isFirstTime(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getBoolean(PREF_FIRST_TIME, true);
	}

	public static void setFirstTime(Context context, boolean visible) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putBoolean(PREF_FIRST_TIME, visible);
		prefEditor.apply();
	}
	
	//Twitter
	public static boolean isLoggedTwitter(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getBoolean(PREF_TWITTER_LOGGED, false);
	}

	public static void setLoggedTwitter(Context context, boolean login) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putBoolean(PREF_TWITTER_LOGGED, login);
		prefEditor.apply();
	}
	
	public static String getKeyOauthToken(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getString(PREF_KEY_OAUTH_TOKEN, "");
	}

	public static void setKeyOauthToken(Context context, String token) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putString(PREF_KEY_OAUTH_TOKEN, token);
		prefEditor.apply();
	}
	
	public static String getKeyOauthSecret(Context context) {
		final SharedPreferences sharedPref = getSharedPreferences(context);
		return sharedPref.getString(PREF_KEY_OAUTH_SECRET, "");
	}

	public static void setKeyOauthSecret(Context context, String secret) {
		final SharedPreferences.Editor prefEditor = getSharedPreferences(
				context).edit();
		prefEditor.putString(PREF_KEY_OAUTH_SECRET, secret);
		prefEditor.apply();
	}
}
