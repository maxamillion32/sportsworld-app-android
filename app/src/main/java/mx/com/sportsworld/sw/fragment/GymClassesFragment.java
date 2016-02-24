
package mx.com.sportsworld.sw.fragment;

/**
 * @author Jose Torres Fuentes
 * Ironbit
 *
 */
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Reminders;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import mx.com.sportsworld.sw.BuildConfig;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.adapter.GymClassesAdapter;
import mx.com.sportsworld.sw.adapter.GymClassesAdapter.OnBookClassClickListener;
import mx.com.sportsworld.sw.app.SherlockWorkingExpandableListFragment;
import mx.com.sportsworld.sw.fragment.dialog.ProgressDialogFragment;
import mx.com.sportsworld.sw.pojo.ClassPojo;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.provider.SportsWorldContract;
import mx.com.sportsworld.sw.service.FetchBranchGymClassesService;
import mx.com.sportsworld.sw.utils.ConnectionUtils;
import mx.com.sportsworld.sw.utils.FacebookUtils;
import mx.com.sportsworld.sw.utils.GeneralUtils;
import mx.com.sportsworld.sw.web.task.BookClassTask;

// TODO: Auto-generated Javadoc
// TODO Life cycle and error messages / ProgressLoadView / Retry button

/**
 * Shows classes offered by a branch.
 *
 *
 */

public class GymClassesFragment extends SherlockWorkingExpandableListFragment
		implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener,
		OnBookClassClickListener {

	/** The Constant TAG. */
	private static final String TAG = GymClassesFragment.class.getName();

	/** The Constant REQUEST_CODE_FETCH_GYM_CLASSES. */
	private static final int REQUEST_CODE_FETCH_GYM_CLASSES = 0;

	/** The Constant REQUEST_CODE_BOOK_GYM_CLASS. */
	private static final int REQUEST_CODE_BOOK_GYM_CLASS = 1;

	/** The Constant LOADER_ID_GYM_CLASSES. */
	private static final int LOADER_ID_GYM_CLASSES = 0;

	/** The Constant STATE_RETRY_SHOWN. */
	private static final String STATE_RETRY_SHOWN = "state_retry_shown";

	/** The Constant STATE_FETCHING_GYM_CLASSES. */
	private static final String STATE_FETCHING_GYM_CLASSES = "state_fetching_gym_classes";

	/** The Constant STATE_GYM_CLASSES_FETCHED. */
	private static final String STATE_GYM_CLASSES_FETCHED = "state_gym_classes_fetched";

	/** The Constant STATE_EXPANDED_GROUP_POSITION. */
	private static final String STATE_EXPANDED_GROUP_POSITION = "state_expanded_group_position";

	/** The Constant FRAG_ARG_BRANCH_ID. */
	private static final String FRAG_ARG_BRANCH_ID = "frag_arg_branch_id";

	/** The Constant NO_ARG_BRANCH_ID_PASSED. */
	private static final long NO_ARG_BRANCH_ID_PASSED = -1L;

	/** The Constant COL_INDEX_GYM_CLASSES_NAME. */
	private static final int COL_INDEX_GYM_CLASSES_NAME = 0;

	/** The Constant COL_INDEX_GYM_CLASSES_COACH_NAME. */
	private static final int COL_INDEX_GYM_CLASSES_COACH_NAME = 1;

	/** The Constant COL_INDEX_GYM_CLASSES_STARTS_AT. */
	private static final int COL_INDEX_GYM_CLASSES_STARTS_AT = 2;

	/** The Constant COL_INDEX_GYM_CLASSES_IN_HIGH_DEMAND. */
	private static final int COL_INDEX_GYM_CLASSES_IN_HIGH_DEMAND = 3;

	/** The Constant COL_INDEX_GYM_FACILITY_PROGRAMED_ACTIVITY_ID. */
	private static final int COL_INDEX_GYM_FACILITY_PROGRAMED_ACTIVITY_ID = 4;

	/** The Constant COL_INDEX_GYM_CLASSES_SALON. */
	private static final int COL_INDEX_GYM_CLASSES_SALON = 5;

	/** The Constant COL_INDEX_GYM_CLASSES_FINISH_AT. */
	private static final int COL_INDEX_GYM_CLASSES_FINISH_AT = 6;

	/** The Constant COL_INDEX_GYM_CLASSES_ID. */
	private static final int COL_INDEX_GYM_CLASSES_ID = 7;

	/** The Constant COL_INDEX_GYM_CLUB. */
	private static final int COL_INDEX_GYM_CLUB = 8;

	/** The Constant COL_INDEX_GYM_AGENDAR. */
	private static final int COL_INDEX_GYM_AGENDAR = 9;

	/** The Constant GYM_CLASSES_COLS. */
	private static final String[] GYM_CLASSES_COLS = buildGymClassesColumns();

	/** The m adapter. */
	private GymClassesAdapter mAdapter;

	/** The permissions. */
	public static List<String> PERMISSIONS = null;

	/** The Constant PERMISSION_PUBLISH_STREAM. */
	private static final String PERMISSION_PUBLISH_STREAM = "publish_stream";

	/** The m btn retry. */
	private Button mBtnRetry;

	/** The m fetching gym classes. */
	private boolean mFetchingGymClasses;

	/** The m gym classes fetched. */
	private boolean mGymClassesFetched;

	/** The m retry shown. */
	private boolean mRetryShown;

	/** The m expanded group position. */
	private int mExpandedGroupPosition;

	/** The m btn show calendar. */
	private Button mBtnShowCalendar;

	/** The facility programed activity id. */
	private long facilityProgramedActivityId;

	/** The alert face. */
	public AlertDialog alertFace;

	/** The user id. */
	private long userId;

	/** The class name. */
	public static String className = "";

	/** The coach name. */
	public static String coachName = "";

	/** The hour class. */
	public static String hourClass = "";

	/** The c. */
	public static Calendar c = null;

	/** The m cursor. */
	public static Cursor mCursor;

	/** The select date. */
	public static long selectDate;

	/** The progress. */
	private ProgressDialogFragment progress;

	/** The dialgo fragment. */
	Fragment dialgoFragment;

	/** The club name. */
	public static String clubName = "";

	/** The salon name. */
	public static String salonName = "";

	/** The sala. */
	public static String[] sala = new String[0];

	/** The m facebook ui helper. */
	private UiLifecycleHelper mFacebookUiHelper;

	/** The pending publish reauthorization. */
	private boolean pendingPublishReauthorization = false;

	/** The Constant PENDING_PUBLISH_KEY. */
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";

	/**
	 * On session state change.
	 *
	 * @param session the session
	 * @param state the state
	 * @param exception the exception
	 */
	private void onSessionStateChange(Session session, SessionState state,
									  Exception exception) {
		if (state.isOpened()) {
			if (pendingPublishReauthorization
					&& state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
				pendingPublishReauthorization = false;
				alertFace.show();
			}
		} else if (state.isClosed()) {

		}
	}

	/** The m facebook session callback. */
	private Session.StatusCallback mFacebookSessionCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
						 Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	/**
	 * Builds the gym classes columns.
	 *
	 * @return the string[]
	 */
	private static final String[] buildGymClassesColumns() {
		final SparseArray<String> colsMap = new SparseArray<String>();
		colsMap.put(COL_INDEX_GYM_CLASSES_NAME,
				SportsWorldContract.GymClass.NAME);
		colsMap.put(COL_INDEX_GYM_CLASSES_SALON,
				SportsWorldContract.GymClass.SALON);
		colsMap.put(COL_INDEX_GYM_CLASSES_COACH_NAME,
				SportsWorldContract.GymClass.COACH_NAME);
		colsMap.put(COL_INDEX_GYM_CLASSES_STARTS_AT,
				SportsWorldContract.GymClass.STARTS_AT);
		colsMap.put(COL_INDEX_GYM_CLASSES_FINISH_AT,
				SportsWorldContract.GymClass.FINISH_AT);
		colsMap.put(COL_INDEX_GYM_CLASSES_IN_HIGH_DEMAND,
				SportsWorldContract.GymClass.IN_HIGH_DEMAND);
		colsMap.put(COL_INDEX_GYM_FACILITY_PROGRAMED_ACTIVITY_ID,
				SportsWorldContract.GymClass.FACILITY_PROGRAMED_ACTIVITY_ID);
		colsMap.put(COL_INDEX_GYM_CLASSES_ID, SportsWorldContract.GymClass._ID);
		colsMap.put(COL_INDEX_GYM_CLUB, SportsWorldContract.GymClass.CLUB);
		colsMap.put(COL_INDEX_GYM_AGENDAR, SportsWorldContract.GymClass.AGENDAR_CLASES);
		final int colCount = colsMap.size();
		final String[] cols = new String[colCount];
		for (int i = 0; i < colCount; i++) {
			cols[i] = colsMap.get(i);
		}

		return cols;
	}

	/**
	 * Instantiates a new gym classes fragment.
	 */
	public GymClassesFragment() {
		/* Do nothing */
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public void onPrepareOptionsMenu(Menu menu) {

	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.gym_classes_menu, menu);

	}


	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.sort_filter_menu) {
			createDlgClass();
		}
		return true;
	}

	/**
	 * New instance.
	 *
	 * @return the gym classes fragment
	 */
	public static final GymClassesFragment newInstance() {
		return new GymClassesFragment();
	}

	/**
	 * New instance.
	 *
	 * @param branchId the branch id
	 * @param date the date
	 * @return the gym classes fragment
	 */
	public static final GymClassesFragment newInstance(long branchId, long date) {
		final GymClassesFragment f = new GymClassesFragment();
		final Bundle args = new Bundle();
		args.putLong(FRAG_ARG_BRANCH_ID, branchId);
		selectDate = date;
		f.setArguments(args);
		return f;
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.app.SherlockWorkingExpandableListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			pendingPublishReauthorization = savedInstanceState.getBoolean(
					PENDING_PUBLISH_KEY, false);
		}

		final View view = inflater.inflate(R.layout.fragment_gym_classes,
				null /* root */, false /* attachToRoot */);

		mBtnShowCalendar = (Button) view.findViewById(R.id.btn_show_calendar);
		mBtnShowCalendar.setOnClickListener(this);

		mBtnRetry = (Button) view.findViewById(R.id.btn_retry);
		mBtnRetry.setOnClickListener(this);

		return view;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);

		SportsWorldPreferences.setChckBoxAdvice(getActivity(), true);
		if (savedInstanceState != null) {
			mRetryShown = savedInstanceState.getBoolean(STATE_RETRY_SHOWN);
			mGymClassesFetched = savedInstanceState
					.getBoolean(STATE_GYM_CLASSES_FETCHED);
			mFetchingGymClasses = savedInstanceState
					.getBoolean(STATE_FETCHING_GYM_CLASSES);
			mExpandedGroupPosition = savedInstanceState.getInt(
					STATE_EXPANDED_GROUP_POSITION, -1);
			showFragmentDialog(true);
		} else {
			showFragmentDialog(false);

		}
		setListShown(true);
		setExclusiveExpand(true);
		mAdapter = new GymClassesAdapter(null /* cursor */, getActivity() /* context */);
		mAdapter.setOnBoockClickListener(this);
		setExpandableListAdapter(mAdapter, false);
		fetchBranchClasses();

		showSelectedDate();

		mFacebookUiHelper = new UiLifecycleHelper(getActivity() /* context */,
				mFacebookSessionCallback);
		mFacebookUiHelper.onCreate(savedInstanceState);

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(STATE_FETCHING_GYM_CLASSES, mFetchingGymClasses);
		outState.putBoolean(STATE_GYM_CLASSES_FETCHED, mGymClassesFetched);
		outState.putBoolean(STATE_RETRY_SHOWN, mRetryShown);
		outState.putInt(STATE_EXPANDED_GROUP_POSITION, mExpandedGroupPosition);
		if (mFacebookUiHelper != null)
			mFacebookUiHelper.onSaveInstanceState(outState);
		outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
		mFacebookUiHelper.onSaveInstanceState(outState);
	}

	/**
	 * Gets the branch id from arguments.
	 *
	 * @return the branch id from arguments
	 */
	private long getBranchIdFromArguments() {
		final Bundle args = getArguments();
		return (args == null) ? NO_ARG_BRANCH_ID_PASSED : args
				.getLong(FRAG_ARG_BRANCH_ID);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cursoLoad = null;

		cursoLoad = new CursorLoader(getActivity() /* context */,
				SportsWorldContract.GymClass.CONTENT_URI, GYM_CLASSES_COLS,
				null /* selection */, null /* selectionArgs */, null /* sortOrder */);

		return cursoLoad;

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}
		mAdapter.swapCursor(cursor);

	}

	/**
	 * Sets the list content.
	 */
	private void setListContent() {

		if (mGymClassesFetched) {
			setEmptyText(getString(R.string.empty_classes));
			getLoaderManager().initLoader(LOADER_ID_GYM_CLASSES,
					null /* loaderArgs */, this /* loaderCallback */);
			showRetryButton(!mGymClassesFetched);
		} else {
			setEmptyText(getString(R.string.empty_classes));
			getLoaderManager().initLoader(LOADER_ID_GYM_CLASSES,
					null /* loaderArgs */, this /* loaderCallback */);
		}

	}

	/**
	 * Show retry button.
	 *
	 * @param show the show
	 */
	public void showRetryButton(boolean show) {
		mBtnRetry.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_book:
				break;
			case R.id.btn_show_calendar:
				break;
			default:
				Log.i("LogIron", "No hay vista" + view.getId());
		}
	}

	/**
	 * Show selected date.
	 */
	@SuppressWarnings("deprecation")
	private void showSelectedDate() {
		mBtnShowCalendar.setText(DateUtils.formatDateTime(getActivity(),
				selectDate, DateUtils.FORMAT_24HOUR
						| DateUtils.FORMAT_SHOW_YEAR));
	}

	/**
	 * Fetch branch classes.
	 */
	private void fetchBranchClasses() {

		if (isResumed()) {
			setListShown(false);
		} else {
			setListShownNoAnimation(false);
		}

		mFetchingGymClasses = true;

		final Intent data = new Intent();
		final PendingIntent pendingIntent = getActivity().createPendingResult(
				REQUEST_CODE_FETCH_GYM_CLASSES, data, 0 /* flags */);
		final Intent fetchBranchGymClassesService = new Intent(getActivity(),
				FetchBranchGymClassesService.class);
		fetchBranchGymClassesService.putExtra(
				FetchBranchGymClassesService.EXTRA_PENDING_INTENT,
				pendingIntent);
		fetchBranchGymClassesService.putExtra(
				FetchBranchGymClassesService.EXTRA_BRANCH_ID,
				getBranchIdFromArguments());
		fetchBranchGymClassesService.putExtra(
				FetchBranchGymClassesService.EXTRA_DATE_MILLIS, selectDate);
		getActivity().startService(fetchBranchGymClassesService);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (progress != null)
			progress.dismiss();

		switch (requestCode) {

			case REQUEST_CODE_FETCH_GYM_CLASSES:
				mFetchingGymClasses = false;
				mGymClassesFetched = (resultCode == Activity.RESULT_OK);
				setListContent();
				break;

			case REQUEST_CODE_BOOK_GYM_CLASS:

				if (progress != null)
					progress.dismissAllowingStateLoss();

				final int message = (resultCode == Activity.RESULT_OK) ? R.string.success_book_class
						: R.string.error_book_class;

				Toast.makeText(getActivity(), getResources().getString(message),
						Toast.LENGTH_SHORT).show();

				break;

			default:
				mFacebookUiHelper.onActivityResult(requestCode, resultCode, data);

				super.onActivityResult(requestCode, resultCode, data);

		}

	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.adapter.GymClassesAdapter.OnBookClassClickListener#onBookClassClick(long, long, int, java.lang.String)
	 */
	@Override
	public void onBookClassClick(long facilityProgramedActivityId,
								 long gymClassId, int position, String beginHour) {
		if (isMember()) {
			@SuppressWarnings("deprecation")
			String hora = DateUtils.formatDateTime(getActivity(),
					Long.parseLong(beginHour), DateUtils.FORMAT_24HOUR
							| DateUtils.FORMAT_SHOW_TIME/* flags */);
			String[] split = GeneralUtils.splitString(hora, ":");
			Calendar setCalendar = Calendar.getInstance();
			setCalendar.setTimeInMillis(selectDate);
			int hourOfDay = Integer.parseInt(split[0]);
			setCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);

			Calendar now = GeneralUtils.generateCurrentDate();
			if (now.before(setCalendar)) {

				final SportsWorldAccountManager accountMngr = new SportsWorldAccountManager(
						getActivity() /* context */);
				if (!accountMngr.isLoggedInAsMember()) {

					Toast.makeText(
							getActivity(),
							getResources().getString(
									R.string.error_not_a_member),
							Toast.LENGTH_SHORT).show();
					return;
				}

				this.facilityProgramedActivityId = facilityProgramedActivityId;
				userId = Long.parseLong(accountMngr.getCurrentUserId());

				Log.d(TAG, "onBookClassClick - Class id = "
						+ facilityProgramedActivityId);

				showFragmentDialog(false);

				/*
				 * AsyncTask para agendar la clase
				 */
				BookClassTask bookTask = new BookClassTask(
						new ResponseInterface() {

							@SuppressLint("NewApi")
							@Override
							public void onResultResponse(Object obj) {
								// TODO -generated method stub
								final ClassPojo pojoRes = (ClassPojo) obj;

								progress.dismissAllowingStateLoss();
								if (pojoRes.isStatus()) {

									addToCalendar(pojoRes, false);
									if (pojoRes.getMessage().equals("interno")) {

									}
									if (pojoRes.getMessage().equals(
											"Successful Transaction")) {
										comaprtirFace(pojoRes);
									}

									return;
								} else if (!pojoRes.isStatus()
										&& !pojoRes.getMessage().equals(
										"TimeOut")
										&& !pojoRes.getMessage().equals(
										"interno")) {
									Toast.makeText(getActivity(),
											pojoRes.getMessage(),
											Toast.LENGTH_SHORT).show();
									return;

								} else {
									Toast.makeText(
											getActivity(),
											getResources()
													.getString(
															R.string.error_connection_server),
											Toast.LENGTH_SHORT).show();

								}

							}
						});
				BookClassTask.act = getActivity();
				ClassPojo pojo = new ClassPojo();
				pojo = fillBookPojo(pojo);
				Log.d("Aviso clase a club", "" + SportsWorldPreferences.isChckBoxAdvice(getActivity()));
				if (SportsWorldPreferences.isChckBoxAdvice(getActivity())) {
					if (isNetworkAvailable()) {
						bookTask.execute(pojo);
					} else {
						Toast.makeText(
								getActivity(),
								getResources().getString(
										R.string.error_connection),
								Toast.LENGTH_SHORT).show();
						progress.dismissAllowingStateLoss();
					}
				} else {
					addToCalendar(pojo, true);
					if (progress != null)
						progress.dismissAllowingStateLoss();
					comaprtirFace(pojo);

				}

			} else {

				Toast.makeText(
						getActivity(),
						getResources()
								.getString(R.string.error_book_class_time),
						Toast.LENGTH_SHORT).show();
			}
		} else {
//			Toast.makeText(getActivity(),
//					getResources().getString(R.string.error_not_a_member),
//					Toast.LENGTH_SHORT).show();
			@SuppressWarnings("deprecation")
			String hora = DateUtils.formatDateTime(getActivity(),
					Long.parseLong(beginHour), DateUtils.FORMAT_24HOUR
							| DateUtils.FORMAT_SHOW_TIME/* flags */);
			String[] split = GeneralUtils.splitString(hora, ":");
			Calendar setCalendar = Calendar.getInstance();
			setCalendar.setTimeInMillis(selectDate);
			int hourOfDay = Integer.parseInt(split[0]);
			setCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);

			Calendar now = GeneralUtils.generateCurrentDate();
			if (now.before(setCalendar)) {



				/*
				 * AsyncTask para agendar la clase
				 */
				BookClassTask bookTask = new BookClassTask(
						new ResponseInterface() {

							@SuppressLint("NewApi")
							@Override
							public void onResultResponse(Object obj) {
								// TODO -generated method stub
								final ClassPojo pojoRes = (ClassPojo) obj;

								progress.dismissAllowingStateLoss();
								if (pojoRes.isStatus()) {

									addToCalendar(pojoRes, false);
									if (pojoRes.getMessage().equals("interno")) {

									}
									if (pojoRes.getMessage().equals(
											"Successful Transaction")) {
										comaprtirFace(pojoRes);
									}

									return;
								} else if (!pojoRes.isStatus()
										&& !pojoRes.getMessage().equals(
										"TimeOut")
										&& !pojoRes.getMessage().equals(
										"interno")) {
									Toast.makeText(getActivity(),
											pojoRes.getMessage(),
											Toast.LENGTH_SHORT).show();
									return;

								} else {
									Toast.makeText(
											getActivity(),
											getResources()
													.getString(
															R.string.error_connection_server),
											Toast.LENGTH_SHORT).show();

								}

							}
						});
				BookClassTask.act = getActivity();
				ClassPojo pojo = new ClassPojo();
				pojo = fillBookPojo(pojo);
				Log.d("Aviso clase a club", "" + SportsWorldPreferences.isChckBoxAdvice(getActivity()));

				addToCalendar(pojo, true);
				if (progress != null)
					progress.dismissAllowingStateLoss();
				// TODO Arreglar compartir en facebook
				// comaprtirFace(pojo);



			} else {

				Toast.makeText(
						getActivity(),
						getResources()
								.getString(R.string.error_book_class_time),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.app.SherlockWorkingExpandableListFragment#onListGroupExpandListener(int)
	 */
	@Override
	public void onListGroupExpandListener(int groupPosition) {
		SportsWorldPreferences.setChckBoxAdvice(getActivity(), true);
		mExpandedGroupPosition = groupPosition;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		mFacebookUiHelper.onPause();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		mFacebookUiHelper.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}

		mFacebookUiHelper.onResume();
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.app.SherlockWorkingExpandableListFragment#onListGroupCollapseListener(int)
	 */
	@Override
	public void onListGroupCollapseListener(int groupPosition) {
		mExpandedGroupPosition = -1;
	}

	/**
	 * Fill book pojo.
	 *
	 * @param pojo the pojo
	 * @return the class pojo
	 */
	public ClassPojo fillBookPojo(ClassPojo pojo) {
		Date classDate = new Date(selectDate);
		final SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"yyyy/MM/dd", Locale.US);
		final String classDateStr = dateFormatter.format(classDate);

		pojo.setEmployed_id("0");
		pojo.setClassdate(classDateStr);
		final int origin = (BuildConfig.DEBUG ? 3 : 4);
		pojo.setOrigin(String.valueOf(origin));
		pojo.setIdconfirm("0");
		pojo.setConfirm("0");
		pojo.setIdinstactprg(String.valueOf(facilityProgramedActivityId));
		pojo.setId_user(String.valueOf(userId));
		String[] day = classDateStr.split("/");
		pojo.setYear(Integer.parseInt(day[0]));
		pojo.setMonth(Integer.parseInt(day[1]));
		pojo.setDay(Integer.parseInt(day[2]));
		pojo.setClassName(className);
		pojo.setCoachName(coachName);
		pojo.setSalonName(salonName);
		pojo.setClubName(clubName);
		String[] time = hourClass.split(":");
		pojo.setClassStarts(Integer.parseInt(time[0]));
		return pojo;
	}

	/**
	 * Creates the dlg class.
	 */
	private void createDlgClass() {

		final Dialog dialog = new Dialog(getActivity());
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		//dialog.
		dialog.setContentView(R.layout.alert_dialog_sort_class);
		//dialog.setTitle("Filtrado de clases.");
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Planer_Reg.ttf");

		final EditText txtSortClass = (EditText) dialog
				.findViewById(R.id.txtSortClass);

		final RadioButton radioClass = (RadioButton) dialog
				.findViewById(R.id.rdioBtnClass);
		final RadioButton radioInstructor = (RadioButton) dialog
				.findViewById(R.id.rdioBtnInstructor);
		final RadioButton radioRoom = (RadioButton) dialog
				.findViewById(R.id.rdioBtnRoom);

		Button btnCancelDlg = (Button) dialog
				.findViewById(R.id.btnCancelDlgClass);
		btnCancelDlg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		txtSortClass.setTypeface(font);
		radioClass.setTypeface(font);
		radioInstructor.setTypeface(font);
		radioRoom.setTypeface(font);
		btnCancelDlg.setTypeface(font);

		Button btnAcceptDlg = (Button) dialog
				.findViewById(R.id.btnSortDlgClass);
		btnAcceptDlg.setTypeface(font);
		btnAcceptDlg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (radioClass.isChecked())
					mCursor = getActivity()
							.getContentResolver()
							.query(SportsWorldContract.GymClass.CONTENT_URI,
									GYM_CLASSES_COLS,
									SportsWorldContract.GymClass.NAME
											+ " like '%"
											+ txtSortClass.getText().toString()
											+ "%' " /* selection */,
									null/* selectionArgs */, null /* sortOrder */);
				if (radioInstructor.isChecked())
					mCursor = getActivity()
							.getContentResolver()
							.query(SportsWorldContract.GymClass.CONTENT_URI,
									GYM_CLASSES_COLS,
									SportsWorldContract.GymClass.COACH_NAME
											+ " like '%"
											+ txtSortClass.getText().toString()
											+ "%' " /* selection */,
									null /* selectionArgs */, null /* sortOrder */);

				if (radioRoom.isChecked())
					mCursor = getActivity()
							.getContentResolver()
							.query(SportsWorldContract.GymClass.CONTENT_URI,
									GYM_CLASSES_COLS,
									SportsWorldContract.GymClass.SALON
											+ " like '%"
											+ txtSortClass.getText().toString()
											+ "%' " /* selection */,
									null /* selectionArgs */, null /* sortOrder */);
				if (mCursor == null)
					mCursor = getActivity().getContentResolver().query(
							SportsWorldContract.GymClass.CONTENT_URI,
							GYM_CLASSES_COLS, null /* selection */,
							null /* selectionArgs */, null /* sortOrder */);

				mAdapter.swapCursor(mCursor);
				mCursor = null;
				dialog.dismiss();
			}
		});

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = dialog.getWindow();
		lp.copyFrom(window.getAttributes());
		// This makes the dialog take up the full width
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);

		dialog.show();
	}

	/**
	 * Show fragment dialog.
	 *
	 * @param savedInstanceState the saved instance state
	 */
	public void showFragmentDialog(boolean savedInstanceState) {
		dialgoFragment = getFragmentManager().findFragmentByTag(
				ProgressDialogFragment.progressDialogTag);

		if (savedInstanceState == true) {
			dialgoFragment = getFragmentManager().findFragmentByTag(
					ProgressDialogFragment.progressDialogTag);
			progress = (ProgressDialogFragment) dialgoFragment;

		} else {

			if (dialgoFragment != null) {
				getFragmentManager().beginTransaction().remove(dialgoFragment)
						.commit();
			}
			progress = ProgressDialogFragment.newInstance(getActivity());
			progress.setCancelable(false);
			progress.show(getFragmentManager(),
					ProgressDialogFragment.progressDialogTag);
		}

	}

	/**
	 * Checks if is network available.
	 *
	 * @return true, if is network available
	 */
	public boolean isNetworkAvailable() {
		return ConnectionUtils.isNetworkAvailable(getActivity());
	}

	/**
	 * Adds the to calendar.
	 *
	 * @param pojo the pojo
	 * @param interno the interno
	 * @return true, if successful
	 */
	@SuppressLint("NewApi")
	public boolean addToCalendar(ClassPojo pojo, boolean interno) {
		boolean res = false;
		// Construct event details
		long startMillis = 0;
		long endMillis = 0;
		Uri event = null;
		Uri EVENTS_URI = Uri.parse(getCalendarUriBase() + "events");
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(pojo.getYear(), pojo.getMonth() - 1, pojo.getDay(),
				pojo.getClassStarts(), 00);
		startMillis = beginTime.getTimeInMillis();
		Calendar endTime = Calendar.getInstance();
		endTime.set(pojo.getYear(), pojo.getMonth() - 1, pojo.getDay(),
				pojo.getClassStarts() + 1, 00);
		endMillis = endTime.getTimeInMillis();
		// Insert Event
		ContentResolver cr = getActivity().getContentResolver();
		ContentValues values = new ContentValues();
		TimeZone timeZone = TimeZone.getDefault();
		values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
		values.put(CalendarContract.Events.DTSTART, startMillis);
		values.put(CalendarContract.Events.DTEND, endMillis);
		//values.put(CalendarContract.Reminders.MINUTES, 15);

		values.put(CalendarContract.Events.TITLE,
				"Clase " + pojo.getClassName());
		values.put(
				CalendarContract.Events.DESCRIPTION,
				"Clase: " + pojo.getClassName() + "\n" + "Club: "
						+ pojo.getClubName() + "\n" + "Salon: "
						+ pojo.getSalonName() + "\n" + "Entrenador: "
						+ pojo.getCoachName());
		values.put(CalendarContract.Events.CALENDAR_ID, 1);
		try {
			if (android.os.Build.VERSION.SDK_INT <= 10)
				event = cr.insert(EVENTS_URI, values);
			else
				event = cr.insert(CalendarContract.Events.CONTENT_URI, values);

			long eventID = Long.parseLong(event.getLastPathSegment());
			ContentValues reminders = new ContentValues();
			reminders.put(Reminders.EVENT_ID, eventID);
			reminders.put(Reminders.METHOD, Reminders.METHOD_ALERT);
			reminders.put(Reminders.MINUTES, 15);

			cr.insert(Reminders.CONTENT_URI, reminders);

			res = true;
			if (!interno)
				Toast.makeText(getActivity(),
						getResources().getString(R.string.success_book_class),
						Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(
						getActivity(),
						getResources().getString(
								R.string.success_book_class_phone),
						Toast.LENGTH_SHORT).show();
		} catch (Exception ex) {
			Log.i("kokusho", ex.toString());
		}
		return res;

	}

	/**
	 * Gets the calendar uri base.
	 *
	 * @return the calendar uri base
	 */
	@SuppressWarnings("deprecation")
	private String getCalendarUriBase() {

		String calendarUriBase = null;
		Uri calendars = Uri.parse("content://calendar/calendars");
		Cursor managedCursor = null;
		try {
			managedCursor = getActivity().managedQuery(calendars, null, null,
					null, null);
		} catch (Exception e) {
		}
		if (managedCursor != null) {
			calendarUriBase = "content://calendar/";
		} else {
			calendars = Uri.parse("content://com.android.calendar/calendars");
			try {
				managedCursor = getActivity().managedQuery(calendars, null,
						null, null, null);
			} catch (Exception e) {
			}
			if (managedCursor != null) {
				calendarUriBase = "content://com.android.calendar/";
			}
		}
		return calendarUriBase;
	}

	/**
	 * Checks if is member.
	 *
	 * @return true, if is member
	 */
	public boolean isMember() {
		final SportsWorldAccountManager accountMngr = new SportsWorldAccountManager(
				getActivity() /* context */);
		return accountMngr.isLoggedInAsMember();

	}

	/**
	 * Comaprtir face.
	 *
	 * @param pojoRes the pojo res
	 */
	public void comaprtirFace(ClassPojo pojoRes) {
		PERMISSIONS = Arrays.asList("publish_actions",
				PERMISSION_PUBLISH_STREAM);

		FacebookUtils.wherePost = "He agendado una clase en Upster";
		FacebookUtils.activity = getSherlockActivity();
		alertFace = FacebookUtils.buldAlertFace(
				"Agend� la clase " + pojoRes.getClassName()
						+ " desde la app Upster Android", getActivity(),
				false);

		Session session = Session.getActiveSession();

		if (session != null && session.isOpened()) {

			List<String> permissions = session.getPermissions();

			for(String perm : permissions)
			{
				Log.i("permisos",perm);
			}

			//Quitar remp: PERMISSIONS
			List<String> tempPerm = Arrays.asList("publish_actions");

			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}
			alertFace.show();
		} else if (SportsWorldPreferences.isLoggedTwitter(getActivity()))
			alertFace.show();

	}

	/**
	 * Checks if is subset of.
	 *
	 * @param subset the subset
	 * @param superset the superset
	 * @return true, if is subset of
	 */
	private boolean isSubsetOf(Collection<String> subset,
							   Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}

}
