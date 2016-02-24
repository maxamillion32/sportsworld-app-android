package mx.com.sportsworld.sw.provider;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import mx.com.sportsworld.sw.provider.SportsWorldContract.Branch;
import mx.com.sportsworld.sw.provider.SportsWorldContract.Exercise;
import mx.com.sportsworld.sw.provider.SportsWorldContract.FinancialInfo;
import mx.com.sportsworld.sw.provider.SportsWorldContract.GuestColumns;
import mx.com.sportsworld.sw.provider.SportsWorldContract.GymClass;
import mx.com.sportsworld.sw.provider.SportsWorldContract.NewsArticleColumns;
import mx.com.sportsworld.sw.provider.SportsWorldContract.Routine;
import mx.com.sportsworld.sw.provider.SportsWorldContract.Trainin;
import mx.com.sportsworld.sw.provider.SportsWorldContract.UserColumns;
import mx.com.sportsworld.sw.provider.SportsWorldContract.WeekDayRelationship;

/**
 * The Class SportsWorldDatabase.
 */
public class SportsWorldDatabase extends SQLiteOpenHelper {

	/** The Constant DATABASE_NAME. */
	public static final String DATABASE_NAME = "sportsworld.db";
	
	/** The Constant DATABASE_VERSION. */
	private static final int DATABASE_VERSION = 42;
	
	/** The db use. */
	static SQLiteDatabase dbUse;

	/**
	 * Instantiates a new sports world database.
	 * 
	 * @param context
	 *            the context
	 */
	public SportsWorldDatabase(Context context) {
		
		super(context, DATABASE_NAME, null /* factory */, DATABASE_VERSION);
	}

	/* package *//**
	 * The Interface Tables.
	 */
	interface Tables {
		/* package *//** The Constant USER. */
		static final String USER = "user";
		/* package *//** The Constant GUEST. */
		static final String GUEST = "guest";
		/* package *//** The Constant NEWS_ARTICLE. */
		static final String NEWS_ARTICLE = "news_article";
		/* package *//** The Constant FINANCIAL_INFO. */
		static final String FINANCIAL_INFO = "financial_info";
		/* package *//** The Constant BRANCH. */
		static final String BRANCH = "branch";
		/* package *//** The Constant GYM_CLASS. */
		static final String GYM_CLASS = "gym_class";
		/* package *//** The Constant ROUTINE. */
		static final String ROUTINE = "routine";
		/* package *//** The Constant TRAININ. */
		static final String TRAININ = "trainin";
		/* package *//** The Constant EXERCISE. */
		static final String EXERCISE = "exercise";
		/* package *//** The Constant WEEK_DAY_RELATIONSHIP. */
		static final String WEEK_DAY_RELATIONSHIP = "week_day_relationship";
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		//dropTables(db);
		db.beginTransaction();
		try {
			createTables(db);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	/**
	 * Creates the bd.
	 * 
	 * @param db
	 *            the db
	 */
	public static void createBD(SQLiteDatabase db) {
		dropTables(db);
		db.beginTransaction();
		
		try {
			createTables(db);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			Log.i("LogIron", e.toString());
		} finally {
			db.endTransaction();
		}

	}

	/**
	 * Creates the tables.
	 * 
	 * @param db
	 *            the db
	 */
	private static void createTables(SQLiteDatabase db) {
		
		final StringBuilder strBuilder = new StringBuilder();

		strBuilder.append("CREATE TABLE IF NOT EXISTS ").append(Tables.USER)
				.append("(").append(UserColumns._ID)
				.append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
				.append(UserColumns.MEMBER_NUMBER).append(" INTEGER,")
				.append(UserColumns.NAME).append(" TEXT COLLATE NOCASE,")
				.append(UserColumns.AGE).append(" INTEGER,")
				.append(UserColumns.GENDER_ID).append(" INTEGER,")
				.append(UserColumns.GENDER).append(" TEXT COLLATE NOCASE,")
				.append(UserColumns.HEIGHT).append(" REAL,")
				.append(UserColumns.WEIGHT).append(" REAL,")
				.append(UserColumns.ROUTINE_ID).append(" INTEGER,")
				.append(UserColumns.REGISTRATION_DATE).append(" INTEGER,")
				.append(UserColumns.BIRTH_DATE).append(" INTEGER,")
				.append(UserColumns.MEMBER_TYPE)
				.append(" TEXT COLLATE NOCASE,")
				.append(UserColumns.MAINTEINMENT)
				.append(" TEXT COLLATE NOCASE,").append(UserColumns.EMAIL)
				.append(" TEXT COLLATE NOCASE,").append(UserColumns.CLUB_ID)
				.append(" INTEGER,").append(UserColumns.CLUB_NAME)
				.append(" TEXT COLLATE NOCASE,")
				.append(UserColumns.MEM_UNIQ_ID).append(" INTEGER)");
		db.execSQL(strBuilder.toString());
		strBuilder.setLength(0);

		strBuilder.append("CREATE TABLE IF NOT EXISTS ").append(Tables.GUEST)
				.append("(").append(GuestColumns._ID)
				.append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
				.append(GuestColumns.NAME).append(" TEXT COLLATE NOCASE,")
				.append(GuestColumns.AGE).append(" INTEGER,")
				.append(GuestColumns.GENDER_ID).append(" INTEGER,")
				.append(GuestColumns.HEIGHT).append(" REAL,")
				.append(GuestColumns.WEIGHT).append(" REAL,")
				.append(GuestColumns.FACEBOOK_NUMBER)
				.append(" TEXT COLLATE NOCASE)");
		db.execSQL(strBuilder.toString());
		strBuilder.setLength(0);

		strBuilder.append("CREATE TABLE IF NOT EXISTS ")
				.append(Tables.NEWS_ARTICLE).append("(")
				.append(NewsArticleColumns._ID)
				.append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
				.append(NewsArticleColumns.SERVER_ID).append(" INTEGER,")
				.append(NewsArticleColumns.TITLE)
				.append(" TEXT COLLATE NOCASE,")
				.append(NewsArticleColumns.RESUME)
				.append(" TEXT COLLATE NOCASE,")
				.append(NewsArticleColumns.CONTENT)
				.append(" TEXT COLLATE NOCASE,")
				.append(NewsArticleColumns.PERMANENT).append(" BOOLEAN,")
				.append(NewsArticleColumns.AUTHOR_NAME)
				.append(" TEXT COLLATE NOCASE,")
				.append(NewsArticleColumns.AUTHOR_ID).append(" INTEGER,")
				.append(NewsArticleColumns.UN_ID).append(" INTEGER,")
				.append(NewsArticleColumns.IMAGE_URL).append(" TEXT,")
				.append(NewsArticleColumns.AVAILABLE_TODAY).append(" BOOLEAN,")
				.append(NewsArticleColumns.START_OF_AVAILABILITY)
				.append(" INTEGER,")
				.append(NewsArticleColumns.END_OF_AVAILABILITY)
				.append(" TEXT,").append(NewsArticleColumns.CATEGORY_ID)
				.append(" INTEGER,").append(NewsArticleColumns.CATEGORY_NAME)
				.append(" TEXT COLLATE NOCASE,").append("UNIQUE (")
				.append(NewsArticleColumns._ID)
				.append(") ON CONFLICT REPLACE)");
		db.execSQL(strBuilder.toString());
		strBuilder.setLength(0);

		strBuilder.append("CREATE TABLE IF NOT EXISTS ")
				.append(Tables.FINANCIAL_INFO).append("(")
				.append(FinancialInfo._ID)
				.append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
				.append(FinancialInfo.COMPANY_NAME)
				.append(" TEXT COLLATE NOCASE,")
				.append(FinancialInfo.LAST_TRADE).append(" REAL,")
				.append(FinancialInfo.CHANGE).append(" REAL,")
				.append(FinancialInfo.RESULT).append(" REAL)");
		db.execSQL(strBuilder.toString());
		strBuilder.setLength(0);
		
		strBuilder.append("CREATE TABLE IF NOT EXISTS ").append(Tables.BRANCH)
		//strBuilder.append("CREATE TABLE ").append(Tables.BRANCH)
				.append("(").append(Branch._ID).append(" INTEGER NOT NULL,")
				.append(Branch.LATITUDE).append(" REAL,")
				.append(Branch.LONGITUDE).append(" REAL,")
				.append(Branch.DISTANCE).append(" REAL,")
				.append(Branch.STATE_ID).append(" INTEGER,")
				.append(Branch.ADDRESS).append(" TEXT COLLATE NOCASE,")
				.append(Branch.SCHEDULE).append(" TEXT,").append(Branch.UN_ID)
				.append(" INTEGER,").append(Branch.KEY)
				.append(" TEXT COLLATE NOCASE,").append(Branch.D_COUNT)
				.append(" INTEGER,").append(Branch.NAME)
				.append(" TEXT COLLATE NOCASE,").append(Branch.VIDEO_URL)
				.append(" TEXT,").append(Branch.URL_360)
				.append(" TEXT,").append(Branch.PRE_ORDER).append(" INTEGER,")
				.append(Branch.STATE_NAME).append(" TEXT COLLATE NOCASE,")
				.append(Branch.PREVENTA).append(" INTEGER DEFAULT 0,")
				.append(Branch.PHONE).append(" TEXT COLLATE NOCASE,")
				.append(Branch.TYPE).append(" TEXT COLLATE NOCASE NOT NULL,")
				.append(Branch.FAVORITE).append(" INTEGER,")
				.append(Branch.FACILITIES).append(" TEXT,")
				.append(Branch.FAC_URL_IMGS).append(" TEXT,")
				.append(Branch.IMAGES_URLS).append(" TEXT,")
				.append(Branch.WILL_DELETE).append(" INTEGER DEFAULT 0,")
				.append("PRIMARY KEY (").append(Branch._ID).append(",")
				.append(Branch.TYPE).append(") ON CONFLICT REPLACE)")
				;
		db.execSQL(strBuilder.toString());
		
		Log.d("create table branch ", "string = " + strBuilder.toString());
		strBuilder.setLength(0);

		strBuilder.append("CREATE TABLE IF NOT EXISTS ")
				.append(Tables.GYM_CLASS).append("(").append(GymClass._ID)
				.append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
				.append(GymClass.IDEAL_CAPACITY).append(" INTEGER,")
				.append(GymClass.IN_HIGH_DEMAND).append(" INTEGER,")
				.append(GymClass.NAME).append(" TEXT COLLATE NOCASE,")
				.append(GymClass.SALON).append(" TEXT COLLATE NOCASE,")
				.append(GymClass.CLUB).append(" TEXT COLLATE NOCASE,")
				.append(GymClass.RESERVATIONS_COUNT).append(" INTEGER,")
				.append(GymClass.CURRENT_CAPACITY).append(" INTEGER,")
				.append(GymClass.MAXIMUM_CAPACITY).append(" INTEGER,")
				.append(GymClass.AVAILABLE_FROM).append(" INTEGER,")
				.append(GymClass.CONFIRMED_RESERVATIONS).append(" INTEGER,")
				.append(GymClass.STARTS_AT).append(" INTEGER,")
				.append(GymClass.FINISH_AT).append(" INTEGER,")
				.append(GymClass.COACH_NAME).append(" TEXT COLLATE NOCASE,")
				.append(GymClass.AGENDAR_CLASES).append(" TEXT COLLATE NOCASE,")
				.append(GymClass.FACILITY_PROGRAMED_ACTIVITY_ID)
				.append(" INTEGER)");
		db.execSQL(strBuilder.toString());
		Log.d("create table gym class ", "string = " + strBuilder.toString());
		strBuilder.setLength(0);

		strBuilder.append("CREATE TABLE IF NOT EXISTS ").append(Tables.ROUTINE)
				.append("(").append(Routine._ID)
				.append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
				.append(Routine.NAME).append(" TEXT COLLATE NOCASE,")
				.append(Routine.NUTRITION_ADVICE)
				.append(" TEXT COLLATE NOCASE)");
		db.execSQL(strBuilder.toString());
		strBuilder.setLength(0);

		strBuilder.append("CREATE TABLE IF NOT EXISTS ").append(Tables.TRAININ)
				.append("(").append(Trainin._ID)
				.append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
				.append(Trainin.EXERCISE_NAME).append(" TEXT COLLATE NOCASE,")
				.append(Trainin.EXCERCISE_IMAGE)
				.append(" TEXT COLLATE NOCASE)");
		db.execSQL(strBuilder.toString());
		strBuilder.setLength(0);

		strBuilder.append("CREATE TABLE IF NOT EXISTS ")
				.append(Tables.EXERCISE).append("(").append(Exercise._ID)
				.append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
				.append(Exercise.WEEK_ID)
				.append(" INTEGER NOT NULL DEFAULT 0,").append(Exercise.DAY_ID)
				.append(" INTEGER NOT NULL DEFAULT 0,")
				.append(Exercise.ID_TRAINING).append(" INTEGER,")
				.append(Exercise.MUSCLE_WORKED).append(" TEXT COLLATE NOCASE,")
				.append(Exercise.CURRENT).append(" INTEGER,")
				.append(Exercise.CIRCUIT_ID).append(" INTEGER,")
				.append(Exercise.EXAMPLE_IMAGES_MEN_URLS).append(" TEXT,")
				.append(Exercise.EXAMPLE_IMAGES_WOMEN_URLS).append(" TEXT,")
				.append(Exercise.MAXIMUM_VALUE).append(" REAL,")
				.append(Exercise.MINIMUM_VALUE).append(" REAL,")
				.append(Exercise.MAXIMUM_WEIGHT_LB).append(" REAL,")
				.append(Exercise.MINIMUM_WEIGHT_LB).append(" REAL,")
				.append(Exercise.UNIT).append(" TEXT COLLATE NOCASE,")
				.append(Exercise.SPORTS_CATEGORY_ID).append(" INTEGER,")
				.append(Exercise.EXERCISE_NAME).append(" TEXT COLLATE NOCASE,")
				.append(Exercise.INSTRUCTIONS).append(" TEXT,")
				.append(Exercise.MEASUREMENT_UNIT_TYPE_ID).append(" INTEGER,")
				.append(Exercise.CIRCUIT_NAME).append(" TEXT COLLATE NOCASE,")
				.append(Exercise.VIDEO_URL_ID).append(" INTEGER,")
				.append(Exercise.SERIES).append(" REAL,")
				.append(Exercise.SPORT_ID).append(" INTEGER,")
				.append(Exercise.DONE).append(" INTEGER NOT NULL DEFAULT 0)");
		db.execSQL(strBuilder.toString());
		strBuilder.setLength(0);

		strBuilder.append("CREATE TABLE IF NOT EXISTS ")
				.append(Tables.WEEK_DAY_RELATIONSHIP).append("(")
				.append(WeekDayRelationship._ID)
				.append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
				.append(WeekDayRelationship.WEEK_ID)
				.append(" INTEGER NOT NULL,")
				.append(WeekDayRelationship.DAY_ID)
				.append(" INTEGER NOT NULL,")
				.append(WeekDayRelationship.ACTIVE)
				.append(" INTEGER NOT NULL DEFAULT 0)");
		db.execSQL(strBuilder.toString());

	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("Log Entra a atualizar", " vieja" + oldVersion + " nueva" + newVersion);
		db.beginTransaction();
		try {
			dropTables(db);
			createTables(db);
			db.setTransactionSuccessful();
			Log.i("Log Termina a atualizar", " vieja" + oldVersion + " nueva" + newVersion);
		} finally {
			db.endTransaction();
		}
	}

	/**
	 * Drop tables.
	 * 
	 * @param db
	 *            the db
	 */
	public static void dropTables(SQLiteDatabase db) {
		try {
			Log.i("Log Elimina ", " tablas");
			db.beginTransaction();
			db.delete(Tables.USER, null, null);
			db.setTransactionSuccessful();
			db.endTransaction();
			db.beginTransaction();
			db.delete(Tables.BRANCH, null, null);
			db.setTransactionSuccessful();
			db.endTransaction();
			db.beginTransaction();
			db.delete(Tables.GYM_CLASS, null, null);
			db.setTransactionSuccessful();
			db.endTransaction();
			
		} catch (Exception ex) {
			Log.i("LogIron", ex.toString());
		}

	}

}
