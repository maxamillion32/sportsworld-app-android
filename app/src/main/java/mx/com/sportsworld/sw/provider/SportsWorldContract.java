package mx.com.sportsworld.sw.provider;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.List;

import mx.com.sportsworld.sw.provider.SportsWorldDatabase.Tables;

/**
 * The Class SportsWorldContract.
 */
public final class SportsWorldContract {

	/** The Constant CONTENT_AUTHORITY. */
	public static final String CONTENT_AUTHORITY = "mx.com.sportsworld.sw.provider";
	/* package *//** The Constant BASE_CONTENT_URI. */
	static final Uri BASE_CONTENT_URI = Uri.parse("content://"
					+ CONTENT_AUTHORITY);
	/* package *//** The Constant PATH_USER. */
	static final String PATH_USER = Tables.USER;
	/* package *//** The Constant PATH_GUEST. */
	static final String PATH_GUEST = Tables.GUEST;
	/* package *//** The Constant PATH_NEWS_ARTICLE. */
	static final String PATH_NEWS_ARTICLE = Tables.NEWS_ARTICLE;
	/* package *//** The Constant PATH_FINANCIAL_INFO. */
	static final String PATH_FINANCIAL_INFO = Tables.FINANCIAL_INFO;
	/* package *//** The Constant PATH_BRANCH. */
	static final String PATH_BRANCH = Tables.BRANCH;
	/* package *//** The Constant PATH_GYM_CLASS. */
	static final String PATH_GYM_CLASS = Tables.GYM_CLASS;
	/* package *//** The Constant PATH_ROUTINE. */
	static final String PATH_ROUTINE = Tables.ROUTINE;
	/* package *//** The Constant PATH_EXERCISE. */
	static final String PATH_EXERCISE = Tables.EXERCISE;
	/* package *//** The Constant PATH_TRAININ. */
	static final String PATH_TRAININ = Tables.TRAININ;
	/* package *//** The Constant PATH_WEEK. */
	static final String PATH_WEEK = "week";
	/* package *//** The Constant PATH_DAY. */
	static final String PATH_DAY = "day";
	/* package *//** The Constant PATH_WEEK_DAY_RELATIONSHIP. */
	static final String PATH_WEEK_DAY_RELATIONSHIP = Tables.WEEK_DAY_RELATIONSHIP;

	/**
	 * Instantiates a new sports world contract.
	 */
	private SportsWorldContract() {
		/* do nothing */
	}

	/* package *//**
	 * The Interface UserColumns.
	 */
	interface UserColumns extends BaseColumns {
		
		/** The Constant MEMBER_NUMBER. */
		public static final String MEMBER_NUMBER = "member_number";
		
		/** The Constant NAME. */
		public static final String NAME = "name";
		
		/** The Constant AGE. */
		public static final String AGE = "age";
		
		/** The Constant GENDER_ID. */
		public static final String GENDER_ID = "gender_id";
		
		/** The Constant GENDER. */
		public static final String GENDER = "gender";
		
		/** The Constant HEIGHT. */
		public static final String HEIGHT = "height";
		
		/** The Constant WEIGHT. */
		public static final String WEIGHT = "weight";
		
		/** The Constant ROUTINE_ID. */
		public static final String ROUTINE_ID = "routine_id";
		
		/** The Constant REGISTRATION_DATE. */
		public static final String REGISTRATION_DATE = "registration_date";
		
		/** The Constant EMAIL. */
		public static final String EMAIL = "email";
		
		/** The Constant BIRTH_DATE. */
		public static final String BIRTH_DATE = "dob";
		
		/** The Constant MEMBER_TYPE. */
		public static final String MEMBER_TYPE = "member_type";
		
		/** The Constant MAINTEINMENT. */
		public static final String MAINTEINMENT = "mainteiment";
		
		/** The Constant CLUB_ID. */
		public static final String CLUB_ID = "club_id";
		
		/** The Constant CLUB_NAME. */
		public static final String CLUB_NAME = "club_name";
		
		/** The Constant MEM_UNIQ_ID. */
		public static final String MEM_UNIQ_ID = "mem_uniq_id";
	}

	/**
	 * The Interface GuestColumns.
	 */
	interface GuestColumns extends BaseColumns {
		
		/** The Constant FACEBOOK_NUMBER. */
		public static final String FACEBOOK_NUMBER = "facebook_number";
		
		/** The Constant NAME. */
		public static final String NAME = "name";
		
		/** The Constant AGE. */
		public static final String AGE = "age";
		
		/** The Constant GENDER_ID. */
		public static final String GENDER_ID = "gender_id";
		
		/** The Constant HEIGHT. */
		public static final String HEIGHT = "height";
		
		/** The Constant WEIGHT. */
		public static final String WEIGHT = "weight";
		
		/** The Constant EMAIL. */
		public static final String EMAIL = "email";
	}

	/* package *//**
	 * The Interface NewsArticleColumns.
	 */
	interface NewsArticleColumns extends BaseColumns {
		
		/** The Constant TITLE. */
		public static final String TITLE = "title";
		
		/** The Constant RESUME. */
		public static final String RESUME = "resume";
		
		/** The Constant CONTENT. */
		public static final String CONTENT = "content";
		
		/** The Constant PERMANENT. */
		public static final String PERMANENT = "permanent";
		
		/** The Constant AUTHOR_NAME. */
		public static final String AUTHOR_NAME = "author_name";
		
		/** The Constant AUTHOR_ID. */
		public static final String AUTHOR_ID = "author_id";
		
		/** The Constant UN_ID. */
		public static final String UN_ID = "id_un";
		
		/** The Constant IMAGE_URL. */
		public static final String IMAGE_URL = "image_url";
		
		/** The Constant AVAILABLE_TODAY. */
		public static final String AVAILABLE_TODAY = "available_today";
		
		/** The Constant SERVER_ID. */
		public static final String SERVER_ID = "server_id";
		
		/** The Constant START_OF_AVAILABILITY. */
		public static final String START_OF_AVAILABILITY = "start_of_availability";
		
		/** The Constant END_OF_AVAILABILITY. */
		public static final String END_OF_AVAILABILITY = "end_of_availability";
		
		/** The Constant CATEGORY_ID. */
		public static final String CATEGORY_ID = "category_id";
		
		/** The Constant CATEGORY_NAME. */
		public static final String CATEGORY_NAME = "category_name";
	}

	/* package *//**
	 * The Interface FinancialInfoColumns.
	 */
	interface FinancialInfoColumns extends BaseColumns {
		
		/** The Constant COMPANY_NAME. */
		public static final String COMPANY_NAME = "company_name";
		
		/** The Constant LAST_TRADE. */
		public static final String LAST_TRADE = "last_trade";
		
		/** The Constant CHANGE. */
		public static final String CHANGE = "change";
		
		/** The Constant RESULT. */
		public static final String RESULT = "result";
	}

	/* package *//**
	 * The Interface BranchColumns.
	 */
	interface BranchColumns extends BaseColumns {
		
		/** The Constant LATITUDE. */
		public static final String LATITUDE = "latitude";
		
		/** The Constant LONGITUDE. */
		public static final String LONGITUDE = "longitude";
		
		/** The Constant DISTANCE. */
		public static final String DISTANCE = "distance";
		
		/** The Constant STATE_ID. */
		public static final String STATE_ID = "state_id";
		
		/** The Constant ADDRESS. */
		public static final String ADDRESS = "address";
		
		/** The Constant SCHEDULE. */
		public static final String SCHEDULE = "schedule";
		
		/** The Constant UN_ID. */
		public static final String UN_ID = "un_id";
		
		/** The Constant KEY. */
		public static final String KEY = "key";
		
		/** The Constant D_COUNT. */
		public static final String D_COUNT = "d_count";
		
		/** The Constant NAME. */
		public static final String NAME = "name";
		
		/** The Constant VIDEO_URL. */
		public static final String VIDEO_URL = "video_url";
		
		/** The Constant URL360. */
		public static final String URL_360 = "url_360";
		
		/** The Constant PRE_ORDER. */
		public static final String PRE_ORDER = "pre_order";
		
		/** The Constant STATE_NAME. */
		public static final String STATE_NAME = "state_name";
		
		/** The Constant PREVENTA. */
		public static final String PREVENTA = "preventa";
		
		/** The Constant PHONE. */
		public static final String PHONE = "phone";
		
		/** The Constant TYPE. */
		public static final String TYPE = "type";
		
		/** The Constant FAVORITE. */
		public static final String FAVORITE = "favorite";
		
		/** The Constant FACILITIES. */
		public static final String FACILITIES = "facilities";
		
		/** The Constant FAC_URL_IMGS. */
		public static final String FAC_URL_IMGS = "rutaimagen";
		
		/** The Constant IMAGES_URLS. */
		public static final String IMAGES_URLS = "images_urls";
		
		/** The Constant WILL_DELETE. */
		public static final String WILL_DELETE = "will_delete";
	}

	/* package *//**
	 * The Interface GymClassColumns.
	 */
	interface GymClassColumns extends BaseColumns {
		
		/** The Constant IDEAL_CAPACITY. */
		public static final String IDEAL_CAPACITY = "ideal_capacity";
		
		/** The Constant IN_HIGH_DEMAND. */
		public static final String IN_HIGH_DEMAND = "in_high_demand";
		
		/** The Constant NAME. */
		public static final String NAME = "name";
		
		/** The Constant SALON. */
		public static final String SALON = "salon";
		
		/** The Constant CLUB. */
		public static final String CLUB = "club";
		
		/** The Constant RESERVATIONS_COUNT. */
		public static final String RESERVATIONS_COUNT = "reservations_count";
		
		/** The Constant CURRENT_CAPACITY. */
		public static final String CURRENT_CAPACITY = "current_capacity";
		
		/** The Constant MAXIMUM_CAPACITY. */
		public static final String MAXIMUM_CAPACITY = "maximum_capacity";
		
		/** The Constant AVAILABLE_FROM. */
		public static final String AVAILABLE_FROM = "available_from";
		
		/** The Constant CONFIRMED_RESERVATIONS. */
		public static final String CONFIRMED_RESERVATIONS = "confirmed_reservations";
		
		/** The Constant STARTS_AT. */
		public static final String STARTS_AT = "starts_at";
		
		/** The Constant FINISH_AT. */
		public static final String FINISH_AT = "finish_at";
		
		/** The Constant COACH_NAME. */
		public static final String COACH_NAME = "coach_name";
		
		/** The Constant FACILITY_PROGRAMED_ACTIVITY_ID. */
		public static final String FACILITY_PROGRAMED_ACTIVITY_ID = "facility_programed_activity_id";
		
		/** The Constant AGENDAR_CLASES. */
		public static final String AGENDAR_CLASES = "agendar_clases";
	}

	/* package *//**
	 * The Interface RoutineColumns.
	 */
	interface RoutineColumns extends BaseColumns {
		
		/** The Constant NAME. */
		public static final String NAME = "routine_name";
		
		/** The Constant NUTRITION_ADVICE. */
		public static final String NUTRITION_ADVICE = "nutrition_advice";
	}

	/**
	 * The Interface TraininColumns.
	 */
	interface TraininColumns extends BaseColumns {
		
		/** The Constant EXERCISE_NAME. */
		public static final String EXERCISE_NAME = "exercise";
		
		/** The Constant EXCERCISE_IMAGE. */
		public static final String EXCERCISE_IMAGE = "image";
	}

	/* package *//**
	 * The Interface ExerciseColumns.
	 */
	interface ExerciseColumns extends BaseColumns {
		
		/** The Constant ID_TRAINING. */
		public static final String ID_TRAINING = "trainin_fk";
		
		/** The Constant MUSCLE_WORKED. */
		public static final String MUSCLE_WORKED = "muscle_worked";
		
		/** The Constant CURRENT. */
		public static final String CURRENT = "current";
		
		/** The Constant CIRCUIT_ID. */
		public static final String CIRCUIT_ID = "circuit_id";
		
		/** The Constant EXAMPLE_IMAGES_MEN_URLS. */
		public static final String EXAMPLE_IMAGES_MEN_URLS = "example_images_men_urls";
		
		/** The Constant EXAMPLE_IMAGES_WOMEN_URLS. */
		public static final String EXAMPLE_IMAGES_WOMEN_URLS = "example_images_women_urls";
		
		/** The Constant MINIMUM_VALUE. */
		public static final String MINIMUM_VALUE = "minimum_value";
		
		/** The Constant MAXIMUM_VALUE. */
		public static final String MAXIMUM_VALUE = "maximum_value";
		
		/** The Constant MINIMUM_WEIGHT_LB. */
		public static final String MINIMUM_WEIGHT_LB = "minimum_weight_lb";
		
		/** The Constant MAXIMUM_WEIGHT_LB. */
		public static final String MAXIMUM_WEIGHT_LB = "maximum_weight_lb";
		
		/** The Constant UNIT. */
		public static final String UNIT = "unit";
		
		/** The Constant SPORTS_CATEGORY_ID. */
		public static final String SPORTS_CATEGORY_ID = "sports_category_id";
		
		/** The Constant EXERCISE_NAME. */
		public static final String EXERCISE_NAME = "exercise_name";
		
		/** The Constant INSTRUCTIONS. */
		public static final String INSTRUCTIONS = "instructions";
		
		/** The Constant MEASUREMENT_UNIT_TYPE_ID. */
		public static final String MEASUREMENT_UNIT_TYPE_ID = "measurement_unit_type_id";
		
		/** The Constant CIRCUIT_NAME. */
		public static final String CIRCUIT_NAME = "circuito";
		
		/** The Constant VIDEO_URL_ID. */
		public static final String VIDEO_URL_ID = "video_url_id";
		
		/** The Constant SERIES. */
		public static final String SERIES = "series";
		
		/** The Constant SPORT_ID. */
		public static final String SPORT_ID = "sport_id";
		
		/** The Constant DONE. */
		public static final String DONE = "done";
	}

	/* package *//**
	 * The Interface WeekDayRelationshipColumns.
	 */
	interface WeekDayRelationshipColumns extends BaseColumns {
		
		/** The Constant WEEK_ID. */
		public static final String WEEK_ID = "week_id";
		
		/** The Constant DAY_ID. */
		public static final String DAY_ID = "day_id";
		
		/** The Constant ACTIVE. */
		public static final String ACTIVE = "active";
	}

	/**
	 * The Class User.
	 */
	public static abstract class User implements UserColumns {

		/** The Constant CONTENT_URI. */
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(PATH_USER).build();

		/* package *//** The Constant CONTENT_TYPE. */
		static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
				+ "/vnd." + CONTENT_AUTHORITY + "." + Tables.USER;

		/* package *//** The Constant CONTENT_ITEM_TYPE. */
		static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
				+ "/vnd." + CONTENT_AUTHORITY + "." + Tables.USER;

		/* package *//** The Constant DEFAULT_SORT. */
		static final String DEFAULT_SORT = UserColumns.NAME
				+ " COLLATE NOCASE ASC";

		/**
		 * Builds the user uri.
		 * 
		 * @param userId
		 *            the user id
		 * @return the uri
		 */
		public static Uri buildUserUri(String userId) {
			return CONTENT_URI.buildUpon().appendPath(userId).build();
		}

		/**
		 * Gets the user id.
		 * 
		 * @param uri
		 *            the uri
		 * @return the user id
		 */
		public static String getUserId(Uri uri) {
			return uri.getLastPathSegment();
		}

	}
	
	/**
	 * The Class Guest.
	 */
	public static abstract class Guest implements GuestColumns {

		/** The Constant CONTENT_URI. */
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(PATH_GUEST).build();
		
		/* package *//** The Constant CONTENT_TYPE. */
		static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
				+ "/vnd." + CONTENT_AUTHORITY + "." + Tables.GUEST;

		/* package *//** The Constant CONTENT_ITEM_TYPE. */
		static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
				+ "/vnd." + CONTENT_AUTHORITY + "." + Tables.GUEST;

		/* package *//** The Constant DEFAULT_SORT. */
		static final String DEFAULT_SORT = GuestColumns.NAME
				+ " COLLATE NOCASE ASC";

		/**
		 * Builds the guest uri.
		 * 
		 * @param userId
		 *            the user id
		 * @return the uri
		 */
		public static Uri buildGuestUri(String userId) {
			return CONTENT_URI.buildUpon().appendPath(userId).build();
		}

		/**
		 * Gets the guest id.
		 * 
		 * @param uri
		 *            the uri
		 * @return the guest id
		 */
		public static String getGuestId(Uri uri) {
			return uri.getLastPathSegment();
		}

	}

	/**
	 * The Class NewsArticle.
	 */
	public static abstract class NewsArticle implements NewsArticleColumns {

		/** The Constant CONTENT_URI. */
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(PATH_NEWS_ARTICLE).build();

		/* package *//** The Constant CONTENT_TYPE. */
		static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
				+ "/vnd." + CONTENT_AUTHORITY + "." + Tables.NEWS_ARTICLE;

		/* package *//** The Constant CONTENT_ITEM_TYPE. */
		static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
				+ "/vnd." + CONTENT_AUTHORITY + "." + Tables.NEWS_ARTICLE;

		/* package *//** The Constant DEFAULT_SORT. */
		static final String DEFAULT_SORT = NewsArticleColumns.START_OF_AVAILABILITY
				+ " DESC, " + NewsArticleColumns.PERMANENT + " DESC";

		/**
		 * Builds the news article uri.
		 * 
		 * @param newsArticleId
		 *            the news article id
		 * @return the uri
		 */
		public static Uri buildNewsArticleUri(String newsArticleId) {
			return CONTENT_URI.buildUpon().appendPath(newsArticleId).build();
		}

		/**
		 * Gets the news article id.
		 * 
		 * @param uri
		 *            the uri
		 * @return the news article id
		 */
		public static String getNewsArticleId(Uri uri) {
			return uri.getLastPathSegment();
		}

	}

	/**
	 * The Class FinancialInfo.
	 */
	public static abstract class FinancialInfo implements FinancialInfoColumns {

		/** The Constant CONTENT_URI. */
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(PATH_FINANCIAL_INFO).build();

		/* package *//** The Constant CONTENT_TYPE. */
		static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
				+ "/vnd." + CONTENT_AUTHORITY + "." + Tables.FINANCIAL_INFO;

	}

	/**
	 * The Class Branch.
	 */
	public static abstract class Branch implements BranchColumns {

		/** The Constant CONTENT_URI. */
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(PATH_BRANCH).build();

		/* package *//** The Constant CONTENT_TYPE. */
		static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
				+ "/vnd." + CONTENT_AUTHORITY + "." + Tables.BRANCH;

		/* package *//** The Constant CONTENT_ITEM_TYPE. */
		static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
				+ "/vnd." + CONTENT_AUTHORITY + "." + Tables.BRANCH;

		/* package *//** The Constant DEFAULT_SORT. */
		static final String DEFAULT_SORT = BranchColumns.NAME
				+ " ASC," + BranchColumns.TYPE + " DESC";

		/* package *//** The Constant DISTANCE_SORT. */
		public static final String DISTANCE_SORT = BranchColumns.DISTANCE
				+ " ASC," + BranchColumns.TYPE + " DESC";

		/**
		 * Builds the branch uri.
		 * 
		 * @param branchId
		 *            the branch id
		 * @return the uri
		 */
		public static Uri buildBranchUri(String branchId) {
			return CONTENT_URI.buildUpon().appendPath(branchId).build();
		}

		/**
		 * Gets the branch id.
		 * 
		 * @param uri
		 *            the uri
		 * @return the branch id
		 */
		public static String getBranchId(Uri uri) {
			return uri.getLastPathSegment();
		}

	}

	/**
	 * The Class GymClass.
	 */
	public static abstract class GymClass implements GymClassColumns {

		/** The Constant CONTENT_URI. */
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(PATH_GYM_CLASS).build();

		/* package *//** The Constant CONTENT_TYPE. */
		static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
				+ "/vnd." + CONTENT_AUTHORITY + "." + Tables.GYM_CLASS;

		/* package *//** The Constant CONTENT_ITEM_TYPE. */
		static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
				+ "/vnd." + CONTENT_AUTHORITY + "." + Tables.GYM_CLASS;

		/* package *//** The Constant DEFAULT_SORT. */
		static final String DEFAULT_SORT = GymClassColumns.STARTS_AT
				+ " ASC";

		/**
		 * Builds the gym class uri.
		 * 
		 * @param gymClassId
		 *            the gym class id
		 * @return the uri
		 */
		public static Uri buildGymClassUri(String gymClassId) {
			return CONTENT_URI.buildUpon().appendPath(gymClassId).build();
		}

		/**
		 * Gets the gym class id.
		 * 
		 * @param uri
		 *            the uri
		 * @return the gym class id
		 */
		public static String getGymClassId(Uri uri) {
			return uri.getLastPathSegment();
		}

	}

	/**
	 * The Class Routine.
	 */
	public static abstract class Routine implements RoutineColumns {

		/** The Constant CONTENT_URI. */
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(PATH_ROUTINE).build();

		/* package *//** The Constant CONTENT_TYPE. */
		static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
				+ "/vnd." + CONTENT_AUTHORITY + "." + Tables.ROUTINE;

		/* package *//** The Constant CONTENT_ITEM_TYPE. */
		static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
				+ "/vnd." + CONTENT_AUTHORITY + "." + Tables.ROUTINE;

		/* package *//** The Constant DEFAULT_SORT. */
		static final String DEFAULT_SORT = RoutineColumns.NAME
				+ " DESC";

		/**
		 * Builds the routine uri.
		 * 
		 * @param routineId
		 *            the routine id
		 * @return the uri
		 */
		public static Uri buildRoutineUri(String routineId) {
			return CONTENT_URI.buildUpon().appendPath(routineId).build();
		}

		/**
		 * Gets the routine id.
		 * 
		 * @param uri
		 *            the uri
		 * @return the routine id
		 */
		public static String getRoutineId(Uri uri) {
			return uri.getLastPathSegment();
		}

	}

	/**
	 * The Class Trainin.
	 */
	public static abstract class Trainin implements TraininColumns {

		/** The Constant CONTENT_URI. */
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(PATH_TRAININ).build();

		/* package *//** The Constant CONTENT_TYPE. */
		static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
				+ "/vnd." + CONTENT_AUTHORITY + "." + Tables.TRAININ;

		/* package *//** The Constant CONTENT_ITEM_TYPE. */
		static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
				+ "/vnd." + CONTENT_AUTHORITY + "." + Tables.TRAININ;

		/* package *//** The Constant DEFAULT_SORT. */
		static final String DEFAULT_SORT = Trainin._ID + " ASC";

		/**
		 * Builds the trainin uri.
		 * 
		 * @return the uri
		 */
		public static Uri buildTraininUri() {
			return CONTENT_URI.buildUpon().build();
		}

		/**
		 * Gets the trainin id.
		 * 
		 * @param uri
		 *            the uri
		 * @return the trainin id
		 */
		public static String getTraininId(Uri uri) {
			return uri.getLastPathSegment();
		}

	}

	/**
	 * The Class Exercise.
	 */
	public static abstract class Exercise implements ExerciseColumns,
			WeekDayRelationshipColumns {

		/** The Constant CONTENT_URI. */
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(PATH_EXERCISE).build();

		/* package *//** The Constant CONTENT_TYPE. */
		static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
				+ "/vnd." + CONTENT_AUTHORITY + "." + Tables.EXERCISE;

		/* package *//** The Constant CONTENT_ITEM_TYPE. */
		static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
				+ "/vnd." + CONTENT_AUTHORITY + "." + Tables.EXERCISE;

		/* package *//** The Constant DEFAULT_SORT. */
		static final String DEFAULT_SORT = ExerciseColumns._ID;
																			// +" ASC";

		/**
																			 * Builds
																			 * the
																			 * exercise
																			 * uri
																			 * .
																			 * 
																			 * @param exerciseId
																			 *            the
																			 *            exercise
																			 *            id
																			 * @return 
																			 *         the
																			 *         uri
																			 */
																			public static Uri buildExerciseUri(String exerciseId) {
			return CONTENT_URI.buildUpon().appendPath(exerciseId).build();
		}

		/**
		 * Builds the exercise uri.
		 * 
		 * @param exerciseId
		 *            the exercise id
		 * @return the uri
		 */
		public static Uri buildExerciseUri(long exerciseId) {
			return CONTENT_URI.buildUpon()
					.appendPath(String.valueOf(exerciseId)).build();
		}

		/**
		 * Gets the exercise id.
		 * 
		 * @param uri
		 *            the uri
		 * @return the exercise id
		 */
		public static String getExerciseId(Uri uri) {
			return uri.getLastPathSegment();
		}

		/**
		 * Builds the exercise week day.
		 * 
		 * @param weekId
		 *            the week id
		 * @param dayId
		 *            the day id
		 * @return the uri
		 */
		public static Uri buildExerciseWeekDay(String weekId, String dayId) {
			return CONTENT_URI.buildUpon().appendPath(PATH_WEEK)
					.appendPath(weekId).appendPath(PATH_DAY).appendPath(dayId)
					.build();
		}

		/**
		 * Gets the week id.
		 * 
		 * @param uri
		 *            the uri
		 * @return the week id
		 */
		public static String getWeekId(Uri uri) {
			final List<String> paths = uri.getPathSegments();
			return paths.get(2);
		}

		/**
		 * Gets the day id.
		 * 
		 * @param uri
		 *            the uri
		 * @return the day id
		 */
		public static String getDayId(Uri uri) {
			return uri.getLastPathSegment();
		}

	}

	/**
	 * The Class WeekDayRelationship.
	 */
	public static abstract class WeekDayRelationship implements
			WeekDayRelationshipColumns {

		/** The Constant CONTENT_URI. */
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(PATH_WEEK_DAY_RELATIONSHIP).build();

		/* package *//** The Constant CONTENT_TYPE. */
		static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
				+ "/vnd."
				+ CONTENT_AUTHORITY
				+ "."
				+ Tables.WEEK_DAY_RELATIONSHIP;

		/* package *//** The Constant CONTENT_ITEM_TYPE. */
		static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
				+ "/vnd."
				+ CONTENT_AUTHORITY
				+ "."
				+ Tables.WEEK_DAY_RELATIONSHIP;

		/* package *//** The Constant DEFAULT_SORT. */
		static final String DEFAULT_SORT = WeekDayRelationship.WEEK_ID
				+ " ASC," + WeekDayRelationship.DAY_ID + " ASC";

		/**
		 * Builds the week day relationship uri.
		 * 
		 * @param exerciseId
		 *            the exercise id
		 * @return the uri
		 */
		public static Uri buildWeekDayRelationshipUri(String exerciseId) {
			return CONTENT_URI.buildUpon().appendPath(exerciseId).build();
		}

		/**
		 * Gets the week day relationship id.
		 * 
		 * @param uri
		 *            the uri
		 * @return the week day relationship id
		 */
		public static String getWeekDayRelationshipId(Uri uri) {
			return uri.getLastPathSegment();
		}

	}

}
