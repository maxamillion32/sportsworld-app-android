package mx.com.sportsworld.sw.provider;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.ArrayList;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import mx.com.sportsworld.sw.provider.SportsWorldContract.Branch;
import mx.com.sportsworld.sw.provider.SportsWorldContract.BranchColumns;
import mx.com.sportsworld.sw.provider.SportsWorldContract.Exercise;
import mx.com.sportsworld.sw.provider.SportsWorldContract.ExerciseColumns;
import mx.com.sportsworld.sw.provider.SportsWorldContract.FinancialInfo;
import mx.com.sportsworld.sw.provider.SportsWorldContract.Guest;
import mx.com.sportsworld.sw.provider.SportsWorldContract.GuestColumns;
import mx.com.sportsworld.sw.provider.SportsWorldContract.GymClass;
import mx.com.sportsworld.sw.provider.SportsWorldContract.GymClassColumns;
import mx.com.sportsworld.sw.provider.SportsWorldContract.NewsArticle;
import mx.com.sportsworld.sw.provider.SportsWorldContract.NewsArticleColumns;
import mx.com.sportsworld.sw.provider.SportsWorldContract.Routine;
import mx.com.sportsworld.sw.provider.SportsWorldContract.RoutineColumns;
import mx.com.sportsworld.sw.provider.SportsWorldContract.Trainin;
import mx.com.sportsworld.sw.provider.SportsWorldContract.TraininColumns;
import mx.com.sportsworld.sw.provider.SportsWorldContract.User;
import mx.com.sportsworld.sw.provider.SportsWorldContract.UserColumns;
import mx.com.sportsworld.sw.provider.SportsWorldContract.WeekDayRelationship;
import mx.com.sportsworld.sw.provider.SportsWorldContract.WeekDayRelationshipColumns;
import mx.com.sportsworld.sw.provider.SportsWorldDatabase.Tables;

/**
 * The Class SportsWorldProvider.
 */
public class SportsWorldProvider extends ContentProvider {

	/** The Constant CODE_ALL_USERS. */
	private static final int CODE_ALL_USERS = 1;
	
	/** The Constant CODE_SINGLE_USER. */
	private static final int CODE_SINGLE_USER = 2;
	
	/** The Constant CODE_ALL_NEWS_ARTICLES. */
	private static final int CODE_ALL_NEWS_ARTICLES = 10;
	
	/** The Constant CODE_SINGLE_NEWS_ARTICLE. */
	private static final int CODE_SINGLE_NEWS_ARTICLE = 11;
	
	/** The Constant CODE_ALL_FINANCIAL_INFO. */
	private static final int CODE_ALL_FINANCIAL_INFO = 20;
	
	/** The Constant CODE_ALL_BRANCHES. */
	private static final int CODE_ALL_BRANCHES = 30;
	
	/** The Constant CODE_SINGLE_BRANCH. */
	private static final int CODE_SINGLE_BRANCH = 31;
	
	/** The Constant CODE_ALL_GYM_CLASSES. */
	private static final int CODE_ALL_GYM_CLASSES = 40;
	
	/** The Constant CODE_SINGLE_GYM_CLASS. */
	private static final int CODE_SINGLE_GYM_CLASS = 41;
	
	/** The Constant CODE_ALL_ROUTINES. */
	private static final int CODE_ALL_ROUTINES = 50;
	
	/** The Constant CODE_SINGLE_ROUTINE. */
	private static final int CODE_SINGLE_ROUTINE = 51;
	
	/** The Constant CODE_ALL_EXERCISES. */
	private static final int CODE_ALL_EXERCISES = 60;
	
	/** The Constant CODE_ALL_EXERCISES_ON_WEEK_DAY. */
	private static final int CODE_ALL_EXERCISES_ON_WEEK_DAY = 61;
	
	/** The Constant CODE_SINGLE_EXERCISE. */
	private static final int CODE_SINGLE_EXERCISE = 62;
	
	/** The Constant CODE_ALL_WEEK_DAY_RELATIONSHIP. */
	private static final int CODE_ALL_WEEK_DAY_RELATIONSHIP = 70;
	
	/** The Constant CODE_SINGLE_WEEK_DAY_RELATIONSHIP. */
	private static final int CODE_SINGLE_WEEK_DAY_RELATIONSHIP = 71;
	
	/** The Constant CODE_ALL_TRAININ. */
	private static final int CODE_ALL_TRAININ = 80;
	
	/** The Constant CODE_SINGLE_TAININ. */
	private static final int CODE_SINGLE_TAININ = 81;
	
	/** The Constant CODE_ALL_GUEST. */
	private static final int CODE_ALL_GUEST = 90;
	
	/** The Constant CODE_SINGLE_GUEST. */
	private static final int CODE_SINGLE_GUEST = 91;
	
	/** The Constant sUriMatcher. */
	private static final UriMatcher sUriMatcher = buildUriMatcher();
	
	/** The m open helper. */
	private static SportsWorldDatabase mOpenHelper;
	
	/** The succes. */
	boolean succes = false;
	
	/** The context. */
	private Context context;
	
	/** The Lock. */
	public static String Lock = "dblock";

	/**
	 * Builds the uri matcher.
	 * 
	 * @return the uri matcher
	 */
	private static UriMatcher buildUriMatcher() {
		final String authority = SportsWorldContract.CONTENT_AUTHORITY;
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		matcher.addURI(authority, SportsWorldContract.PATH_USER, CODE_ALL_USERS);
		matcher.addURI(authority, SportsWorldContract.PATH_USER + "/#",
				CODE_SINGLE_USER);
		matcher.addURI(authority, SportsWorldContract.PATH_GUEST,
				CODE_ALL_GUEST);
		matcher.addURI(authority, SportsWorldContract.PATH_GUEST + "/#",
				CODE_SINGLE_GUEST);
		matcher.addURI(authority, SportsWorldContract.PATH_NEWS_ARTICLE,
				CODE_ALL_NEWS_ARTICLES);
		matcher.addURI(authority, SportsWorldContract.PATH_NEWS_ARTICLE + "/#",
				CODE_SINGLE_NEWS_ARTICLE);
		matcher.addURI(authority, SportsWorldContract.PATH_FINANCIAL_INFO,
				CODE_ALL_FINANCIAL_INFO);
		matcher.addURI(authority, SportsWorldContract.PATH_BRANCH,
				CODE_ALL_BRANCHES);
		matcher.addURI(authority, SportsWorldContract.PATH_BRANCH + "/#",
				CODE_SINGLE_BRANCH);
		matcher.addURI(authority, SportsWorldContract.PATH_GYM_CLASS,
				CODE_ALL_GYM_CLASSES);
		matcher.addURI(authority, SportsWorldContract.PATH_GYM_CLASS + "/#",
				CODE_SINGLE_GYM_CLASS);
		matcher.addURI(authority, SportsWorldContract.PATH_ROUTINE,
				CODE_ALL_ROUTINES);
		matcher.addURI(authority, SportsWorldContract.PATH_ROUTINE + "/#",
				CODE_SINGLE_ROUTINE);
		matcher.addURI(authority, SportsWorldContract.PATH_TRAININ,
				CODE_ALL_TRAININ);
		matcher.addURI(authority, SportsWorldContract.PATH_TRAININ + "/#",
				CODE_SINGLE_TAININ);
		matcher.addURI(authority, SportsWorldContract.PATH_EXERCISE,
				CODE_ALL_EXERCISES);
		matcher.addURI(authority, SportsWorldContract.PATH_EXERCISE + "/"
				+ SportsWorldContract.PATH_WEEK + "/#/"
				+ SportsWorldContract.PATH_DAY + "/#",
				CODE_ALL_EXERCISES_ON_WEEK_DAY);
		matcher.addURI(authority, SportsWorldContract.PATH_EXERCISE + "/#",
				CODE_SINGLE_EXERCISE);
		matcher.addURI(authority,
				SportsWorldContract.PATH_WEEK_DAY_RELATIONSHIP,
				CODE_ALL_WEEK_DAY_RELATIONSHIP);
		matcher.addURI(authority,
				SportsWorldContract.PATH_WEEK_DAY_RELATIONSHIP + "/#",
				CODE_SINGLE_WEEK_DAY_RELATIONSHIP);
		return matcher;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		final int match = sUriMatcher.match(uri);
		switch (match) {
		case CODE_ALL_USERS:
			return User.CONTENT_TYPE;
		case CODE_SINGLE_USER:
			return User.CONTENT_ITEM_TYPE;
		case CODE_ALL_GUEST:
			return Guest.CONTENT_TYPE;
		case CODE_SINGLE_GUEST:
			return Guest.CONTENT_ITEM_TYPE;
		case CODE_ALL_NEWS_ARTICLES:
			return NewsArticle.CONTENT_TYPE;
		case CODE_SINGLE_NEWS_ARTICLE:
			return NewsArticle.CONTENT_ITEM_TYPE;
		case CODE_ALL_FINANCIAL_INFO:
			return FinancialInfo.CONTENT_TYPE;
		case CODE_ALL_BRANCHES:
			return Branch.CONTENT_TYPE;
		case CODE_SINGLE_BRANCH:
			return Branch.CONTENT_ITEM_TYPE;
		case CODE_ALL_GYM_CLASSES:
			return GymClass.CONTENT_TYPE;
		case CODE_SINGLE_GYM_CLASS:
			return GymClass.CONTENT_ITEM_TYPE;
		case CODE_ALL_ROUTINES:
			return Routine.CONTENT_TYPE;
		case CODE_SINGLE_ROUTINE:
			return Routine.CONTENT_ITEM_TYPE;
		case CODE_ALL_TRAININ:
			return Trainin.CONTENT_TYPE;
		case CODE_SINGLE_TAININ:
			return Trainin.CONTENT_ITEM_TYPE;
		case CODE_ALL_EXERCISES_ON_WEEK_DAY:
			/* Falls through */
		case CODE_ALL_EXERCISES:
			return Exercise.CONTENT_TYPE;
		case CODE_SINGLE_EXERCISE:
			return Exercise.CONTENT_ITEM_TYPE;
		case CODE_ALL_WEEK_DAY_RELATIONSHIP:
			return WeekDayRelationship.CONTENT_TYPE;
		case CODE_SINGLE_WEEK_DAY_RELATIONSHIP:
			return WeekDayRelationship.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown uri: " + uri);
		}
	}

	/*
	 * {@inheritDoc}
	 */
	/* (non-Javadoc)
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		context = getContext();
		mOpenHelper = new SportsWorldDatabase(context);
		return true;
	}

	/**
	 * Apply the given set of {@link ContentProviderOperation}, executing inside
	 * a {@link SQLiteDatabase} transaction. All changes will be rolled back if
	 * any single one fails.
	 * 
	 * @param operations
	 *            the operations
	 * @return the content provider result[]
	 * @throws OperationApplicationException
	 *             the operation application exception
	 */
	@Override
	public ContentProviderResult[] applyBatch(
			ArrayList<ContentProviderOperation> operations)
			throws OperationApplicationException {
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			final int numOperations = operations.size();
			final ContentProviderResult[] results = new ContentProviderResult[numOperations];
			for (int i = 0; i < numOperations; i++) {
				results[i] = operations.get(i).apply(this /* provider */,
						results /* backRefs */, i /* numBackRefs */);
			}
			db.setTransactionSuccessful();
			return results;
		} finally {
			db.endTransaction();
		}
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		final String id;
		final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		final int match = sUriMatcher.match(uri);

		switch (match) {

		case CODE_SINGLE_USER:
			id = User.getUserId(uri);
			queryBuilder.appendWhere(UserColumns._ID + " = " + id);
			/* Falls through */
		case CODE_ALL_USERS:
			queryBuilder.setTables(Tables.USER);
			break;

		case CODE_SINGLE_GUEST:
			id = Guest.getGuestId(uri);
			queryBuilder.appendWhere(GuestColumns.FACEBOOK_NUMBER + " = " + id);
			/* Falls through */
		case CODE_ALL_GUEST:
			queryBuilder.setTables(Tables.GUEST);
			break;

		case CODE_SINGLE_NEWS_ARTICLE:
			id = NewsArticle.getNewsArticleId(uri);
			queryBuilder.appendWhere(NewsArticleColumns._ID + " = " + id);
			/* Falls through */
		case CODE_ALL_NEWS_ARTICLES:
			queryBuilder.setTables(Tables.NEWS_ARTICLE);
			break;

		case CODE_ALL_FINANCIAL_INFO:
			queryBuilder.setTables(Tables.FINANCIAL_INFO);
			break;

		case CODE_SINGLE_BRANCH:
			id = Branch.getBranchId(uri);
			queryBuilder.appendWhere(BranchColumns._ID + " = " + id);
			/* Falls through */
		case CODE_ALL_BRANCHES:
			queryBuilder.setTables(Tables.BRANCH);
			break;

		case CODE_ALL_GYM_CLASSES:
			queryBuilder.setTables(Tables.GYM_CLASS);
			break;

		case CODE_SINGLE_ROUTINE:
			id = Routine.getRoutineId(uri);
			queryBuilder.appendWhere(RoutineColumns._ID + " = " + id);
			/* Falls through */

		case CODE_ALL_ROUTINES:
			queryBuilder.setTables(Tables.ROUTINE);
			break;

		case CODE_SINGLE_TAININ:
			id = Trainin.getTraininId(uri);
			queryBuilder.appendWhere(TraininColumns._ID + " = " + id);

		case CODE_ALL_TRAININ:
			queryBuilder.setTables(Tables.TRAININ);
			break;

		case CODE_SINGLE_EXERCISE:
			id = Exercise.getExerciseId(uri);
			queryBuilder.appendWhere(ExerciseColumns._ID + " = " + id);
			/* Falls through */

		case CODE_ALL_EXERCISES:
			queryBuilder.setTables(Tables.EXERCISE);
			break;

		case CODE_ALL_EXERCISES_ON_WEEK_DAY:
			final String weekId = Exercise.getWeekId(uri);
			final String dayId = Exercise.getDayId(uri);
			queryBuilder.setTables(Tables.EXERCISE);
			queryBuilder.appendWhere(Tables.EXERCISE + "."
					+ WeekDayRelationshipColumns.WEEK_ID + " = " + weekId
					+ " AND " + Tables.EXERCISE + "."
					+ WeekDayRelationshipColumns.DAY_ID + " = " + dayId);
			break;

		case CODE_SINGLE_WEEK_DAY_RELATIONSHIP:
			id = WeekDayRelationship.getWeekDayRelationshipId(uri);
			queryBuilder.appendWhere(WeekDayRelationship._ID + " = " + id);
			/* Falls through */
		case CODE_ALL_WEEK_DAY_RELATIONSHIP:
			queryBuilder.setTables(Tables.WEEK_DAY_RELATIONSHIP);
			break;

		default:
			// throw new IllegalArgumentException("Unknown uri: " + uri);
			Log.i("LogIron", "Uri desconocida " + uri);
		}

		if (sortOrder == null) {

			switch (match) {

			case CODE_ALL_USERS:
				sortOrder = User.DEFAULT_SORT;
				break;

			case CODE_ALL_GUEST:
				sortOrder = Guest.DEFAULT_SORT;
				break;

			case CODE_ALL_NEWS_ARTICLES:
				sortOrder = NewsArticle.DEFAULT_SORT;
				break;

			case CODE_ALL_BRANCHES:
				sortOrder = Branch.DEFAULT_SORT;
				break;

			case CODE_ALL_GYM_CLASSES:
				sortOrder = GymClass.DEFAULT_SORT;
				break;

			case CODE_ALL_ROUTINES:
				sortOrder = Routine.DEFAULT_SORT;
				break;

			case CODE_ALL_TRAININ:
				sortOrder = Trainin.DEFAULT_SORT;
				break;
			case CODE_ALL_EXERCISES:
				sortOrder = Exercise.DEFAULT_SORT;
				break;

			case CODE_ALL_WEEK_DAY_RELATIONSHIP:
				sortOrder = WeekDayRelationship.DEFAULT_SORT;
				break;

			default:
				/* Do nothing */
				break;

			}

		} else {
			if (sortOrder.equals("73"))
				sortOrder = Branch.DISTANCE_SORT;
		}

		final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		final Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null /* groupBy */, null /* having */,
				sortOrder, null /* limit */);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;

	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {

		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Uri newUri = null;
		long rowId;
		final int match = sUriMatcher.match(uri);

		switch (match) {

		case CODE_ALL_USERS:
			SportsWorldDatabase.dropTables(db);
			rowId = db.insert(Tables.USER, null /* nullColumnHack */, values);
			newUri = ContentUris.withAppendedId(User.CONTENT_URI, rowId);
			break;
		case CODE_ALL_GUEST:
			SportsWorldDatabase.dropTables(db);
			rowId = db.insert(Tables.GUEST, null /* nullColumnHack */, values);
			newUri = ContentUris.withAppendedId(Guest.CONTENT_URI, rowId);
			break;

		case CODE_ALL_NEWS_ARTICLES:
			rowId = db.insert(Tables.NEWS_ARTICLE, null /* nullColumnHack */,
					values);
			newUri = ContentUris.withAppendedId(NewsArticle.CONTENT_URI, rowId);
			break;

		case CODE_ALL_FINANCIAL_INFO:
			rowId = db.insert(Tables.FINANCIAL_INFO, null /* nullColumnHack */,
					values);
			newUri = ContentUris.withAppendedId(FinancialInfo.CONTENT_URI,
					rowId);
			break;

		case CODE_ALL_BRANCHES:
			rowId = db.insert(Tables.BRANCH, null /* nullColumnHack */, values);
			newUri = ContentUris.withAppendedId(Branch.CONTENT_URI, rowId);
			break;

		case CODE_ALL_GYM_CLASSES:
			rowId = db.insert(Tables.GYM_CLASS, null /* nullColumnHack */,
					values);
			newUri = ContentUris.withAppendedId(GymClass.CONTENT_URI, rowId);
			break;

		case CODE_ALL_ROUTINES:
			rowId = db.insert(Tables.ROUTINE, null /* nullColumnHack */,
					values);
			newUri = ContentUris.withAppendedId(Routine.CONTENT_URI, rowId);
			break;

		case CODE_ALL_TRAININ:
			rowId = db.insert(Tables.TRAININ, null /* nullColumnHack */,
					values);
			newUri = ContentUris.withAppendedId(Trainin.CONTENT_URI, rowId);
			break;

		case CODE_ALL_EXERCISES:

			rowId = db.insert(Tables.EXERCISE, null /* nullColumnHack */,
					values);

			if (rowId != -1)
				newUri = ContentUris
						.withAppendedId(Exercise.CONTENT_URI, rowId);
			break;

		case CODE_ALL_WEEK_DAY_RELATIONSHIP:
			rowId = db.insert(Tables.WEEK_DAY_RELATIONSHIP,
					null /* nullColumnHack */, values);
			newUri = ContentUris.withAppendedId(
					WeekDayRelationship.CONTENT_URI, rowId);
			break;

		default:
			throw new IllegalArgumentException("Unknown uri: " + uri);

		}

		getContext().getContentResolver().notifyChange(uri, null /* observer */,
				false /* syncToNetwok */);

		return newUri;

	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		final int deletedRows;
		final String id;

		switch (match) {

		case CODE_SINGLE_USER:
			id = User.getUserId(uri);
			deletedRows = db.delete(Tables.USER, UserColumns._ID + "=?",
					new String[] { id });
			break;

		case CODE_ALL_USERS:
			deletedRows = db.delete(Tables.USER, selection, selectionArgs);
			break;

		case CODE_SINGLE_GUEST:
			id = Guest.getGuestId(uri);
			deletedRows = db.delete(Tables.GUEST, GuestColumns._ID + "=?",
					new String[] { id });
			break;

		case CODE_ALL_GUEST:
			deletedRows = db.delete(Tables.GUEST, selection, selectionArgs);
			break;

		case CODE_ALL_NEWS_ARTICLES:
			deletedRows = db.delete(Tables.NEWS_ARTICLE, selection,
					selectionArgs);
			break;

		case CODE_ALL_FINANCIAL_INFO:
			deletedRows = db.delete(Tables.FINANCIAL_INFO, selection,
					selectionArgs);
			break;

		case CODE_ALL_BRANCHES:
			deletedRows = db.delete(Tables.BRANCH, selection, selectionArgs);
			break;

		case CODE_ALL_GYM_CLASSES:
			deletedRows = db.delete(Tables.GYM_CLASS, selection, selectionArgs);
			break;

		case CODE_ALL_ROUTINES:
			deletedRows = db.delete(Tables.ROUTINE, selection, selectionArgs);
			break;

		case CODE_ALL_TRAININ:
			deletedRows = db.delete(Tables.TRAININ, selection, selectionArgs);
			break;

		case CODE_ALL_EXERCISES:
			deletedRows = db.delete(Tables.EXERCISE, selection, selectionArgs);
			break;

		case CODE_ALL_WEEK_DAY_RELATIONSHIP:
			deletedRows = db.delete(Tables.WEEK_DAY_RELATIONSHIP, selection,
					selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown uri: " + uri);

		}

		getContext().getContentResolver().notifyChange(uri, null /* observer */,
				false /* syncToNetwok */);

		return deletedRows;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		final String id;
		final int updatedRows;

		switch (match) {

		case CODE_SINGLE_USER:
			id = User.getUserId(uri);
			updatedRows = db.update(Tables.USER, values,
					UserColumns._ID + "=?", new String[] { id });
			break;

		case CODE_SINGLE_GUEST:
			id = User.getUserId(uri);
			updatedRows = db.update(Tables.GUEST, values, GuestColumns._ID
					+ "=?", new String[] { id });
			break;

		case CODE_SINGLE_BRANCH:
			id = Branch.getBranchId(uri);
			updatedRows = db.update(Tables.BRANCH, values, BranchColumns._ID
					+ "=?", new String[] { id });
			break;

		case CODE_ALL_BRANCHES:
			updatedRows = db.update(Tables.BRANCH, values, selection,
					selectionArgs);
			break;

		case CODE_SINGLE_GYM_CLASS:
			id = GymClass.getGymClassId(uri);
			updatedRows = db.update(Tables.GYM_CLASS, values,
					GymClassColumns._ID + "=?", new String[] { id });
			break;

		case CODE_ALL_GYM_CLASSES:
			updatedRows = db.update(Tables.GYM_CLASS, values, selection,
					selectionArgs);
			break;

		case CODE_SINGLE_EXERCISE:
			id = Exercise.getExerciseId(uri);
			updatedRows = db.update(Tables.EXERCISE, values,
					ExerciseColumns._ID + "=?", new String[] { id });
			break;

		case CODE_ALL_EXERCISES:
			updatedRows = db.update(Tables.EXERCISE, values, selection,
					selectionArgs);
			break;

		case CODE_SINGLE_TAININ:
			id = Trainin.getTraininId(uri);
			updatedRows = db.update(Tables.TRAININ, values, TraininColumns._ID
					+ "=?", new String[] { id });
			break;

		case CODE_ALL_TRAININ:
			updatedRows = db.update(Tables.TRAININ, values, selection,
					selectionArgs);
			break;

		case CODE_SINGLE_WEEK_DAY_RELATIONSHIP:
			id = WeekDayRelationship.getWeekDayRelationshipId(uri);
			updatedRows = db.update(Tables.WEEK_DAY_RELATIONSHIP, values,
					WeekDayRelationship._ID + "=?", new String[] { id });
			break;

		case CODE_ALL_WEEK_DAY_RELATIONSHIP:
			updatedRows = db.update(Tables.WEEK_DAY_RELATIONSHIP, values,
					selection, selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown uri: " + uri);

		}

		getContext().getContentResolver().notifyChange(uri, null /* observer */,
				false /* syncToNetwok */);

		return updatedRows;

	}
}
