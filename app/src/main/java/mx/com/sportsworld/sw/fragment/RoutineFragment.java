package mx.com.sportsworld.sw.fragment;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
import mx.com.sportsworld.sw.activity.MainboardActivity;
import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.adapter.ExerciseAdapter;
import mx.com.sportsworld.sw.adapter.ExerciseAdapter.OnExerciseValueChangedListener;
import mx.com.sportsworld.sw.adapter.ImageGalleryAdapter;
import mx.com.sportsworld.sw.app.SherlockWorkingExpandableListFragment;
import mx.com.sportsworld.sw.fragment.dialog.FragmentDialogRoutineInfo;
import mx.com.sportsworld.sw.fragment.dialog.FragmentFullImageDialog;
import mx.com.sportsworld.sw.fragment.dialog.ProgressDialogFragment;
import mx.com.sportsworld.sw.fragment.dialog.WeekDayPickerDialogFragment;
import mx.com.sportsworld.sw.imgloader.ImageCache;
import mx.com.sportsworld.sw.model.Exercise;
import mx.com.sportsworld.sw.model.ExerciseGroup;
import mx.com.sportsworld.sw.model.Routine;
import mx.com.sportsworld.sw.model.UserProfile;
import mx.com.sportsworld.sw.parser.ExerciseGroupParser;
import mx.com.sportsworld.sw.pojo.EmailPojo;
import mx.com.sportsworld.sw.pojo.RoutinePojo;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.provider.SportsWorldContract;
import mx.com.sportsworld.sw.service.GetRoutineService;
import mx.com.sportsworld.sw.utils.ConnectionUtils;
import mx.com.sportsworld.sw.utils.FacebookUtils;
import mx.com.sportsworld.sw.web.task.ActiveRoutineTask;
import mx.com.sportsworld.sw.web.task.CheckRoutineTask;
import mx.com.sportsworld.sw.web.task.EmailTask;
import mx.com.sportsworld.sw.web.task.NewRoutineTask;
import mx.com.sportsworld.sw.web.task.RoutineTask;
import mx.com.sportsworld.sw.web.task.UpdateRoutineTask;

// TODO: Auto-generated Javadoc

/**
 * Shows a routine.
 * 
 * 
 */
public class RoutineFragment extends SherlockWorkingExpandableListFragment
		implements LoaderCallbacks<Cursor>, View.OnClickListener,
		OnExerciseValueChangedListener, OnClickListener {

	/** The Constant FRAG_TAG_WEEK_DAY_PICKER. */
	private static final String FRAG_TAG_WEEK_DAY_PICKER = "frag_tag_week_day_picker";

	/** The Constant THUMB_FOLDER. */
	private static final String THUMB_FOLDER = "exercise_thumbs";

	/** The Constant STATE_INDICATION_STRIP_SHOWN. */
	private static final String STATE_INDICATION_STRIP_SHOWN = "state_indication_strip_shown";

	/** The Constant STATE_CURRENTLY_SHOWING_A_MALE_IN_IMAGES. */
	private static final String STATE_CURRENTLY_SHOWING_A_MALE_IN_IMAGES = "state_currently_showing_a_male_on_images";

	/** The Constant STATE_SELECTED_EXERCISE_GROUP_POSITION. */
	private static final String STATE_SELECTED_EXERCISE_GROUP_POSITION = "state_selected_exercise_group_position";

	/** The Constant STATE_SELECTED_EXERCISE_CHILD_POSITION. */
	private static final String STATE_SELECTED_EXERCISE_CHILD_POSITION = "state_selected_exercise_child_position";

	/** The Constant STATE_CURRENT_IMAGE_INDEX. */
	private static final String STATE_CURRENT_IMAGE_INDEX = "state_current_image_index";

	/** The Constant STATE_EXPANDED_GROUPS. */
	private static final String STATE_EXPANDED_GROUPS = "state_expanded_groups";

	/** The Constant STATE_CUR_CHECKED_INDEX. */
	private static final String STATE_CUR_CHECKED_INDEX = "state_cur_checked_index";

	/** The Constant REQUEST_CODE_GET_ROUTINE. */
	private static final int REQUEST_CODE_GET_ROUTINE = 0;

	/** The Constant LOADER_ID_USER. */
	private static final int LOADER_ID_USER = 0;

	/** The Constant LOADER_ID_EXERCISE. */
	private static final int LOADER_ID_EXERCISE = 1;

	/** The Constant LOADER_ID_TRAININ. */
	private static final int LOADER_ID_TRAININ = 2;

	/** The Constant FRAG_ARG_ROUTINE_ID. */
	private static final String FRAG_ARG_ROUTINE_ID = "frag_arg_routine_id";

	/** The Constant COLS_USER. */
	private static final String[] COLS_USER = buildUserColumns();

	/** The Constant COL_INDEX_USER_GENDER_ID. */
	private static final int COL_INDEX_USER_GENDER_ID = 0;

	/** The Constant COL_INDEX_USER_MEMUNIC. */
	private static final int COL_INDEX_USER_MEMUNIC = 1;

	/** The Constant COLS_EXERCISE. */
	private static final String[] COLS_EXERCISE = buildExerciseColumns();

	/** The Constant COLS_TRAININ. */
	private static final String[] COLS_TRAININ = buildTraininColumns();

	/** The Constant COL_INDEX_EXERCISE_MUSCLE_WORKED. */
	private static final int COL_INDEX_EXERCISE_MUSCLE_WORKED = 0;

	/** The Constant COL_INDEX_EXERCISE_ACTIVE. */
	private static final int COL_INDEX_EXERCISE_ACTIVE = 1;

	/** The Constant COL_INDEX_EXERCISE_CIRCUIT_NAME. */
	private static final int COL_INDEX_EXERCISE_CIRCUIT_NAME = 2;

	/** The Constant COL_INDEX_EXERCISE_EXAMPLE_IMAGES_MEN_URLS. */
	private static final int COL_INDEX_EXERCISE_EXAMPLE_IMAGES_MEN_URLS = 3;

	/** The Constant COL_INDEX_EXERCISE_EXAMPLE_IMAGES_WOMEN_URLS. */
	private static final int COL_INDEX_EXERCISE_EXAMPLE_IMAGES_WOMEN_URLS = 4;

	/** The Constant COL_INDEX_EXERCISE_MAXIMUM_VALUE. */
	private static final int COL_INDEX_EXERCISE_MAXIMUM_VALUE = 5;

	/** The Constant COL_INDEX_EXERCISE_MINIMUM_VALUE. */
	private static final int COL_INDEX_EXERCISE_MINIMUM_VALUE = 6;

	/** The Constant COL_INDEX_EXERCISE_SERIES. */
	private static final int COL_INDEX_EXERCISE_SERIES = 7;

	/** The Constant COL_INDEX_EXERCISE_UNIT. */
	private static final int COL_INDEX_EXERCISE_UNIT = 8;

	/** The Constant COL_INDEX_EXERCISE_NAME. */
	private static final int COL_INDEX_EXERCISE_NAME = 9;

	/** The Constant COL_INDEX_DONE. */
	private static final int COL_INDEX_DONE = 10;

	/** The Constant COL_INDEX_ID. */
	private static final int COL_INDEX_ID = 11;

	/** The Constant COL_INDEX_EXERCISE_MAXIMUM_LB_VALUE. */
	private static final int COL_INDEX_EXERCISE_MAXIMUM_LB_VALUE = 12;

	/** The Constant COL_INDEX_EXERCISE_MINIMUM_LB_VALUE. */
	private static final int COL_INDEX_EXERCISE_MINIMUM_LB_VALUE = 13;

	/** The Constant COL_TRAININ_ID. */
	private static final int COL_TRAININ_ID = 0;

	/** The Constant COL_TRAININ_IMAGE. */
	private static final int COL_TRAININ_IMAGE = 1;

	/** The Constant COL_TRAININ_NAME. */
	private static final int COL_TRAININ_NAME = 2;

	/** The m btn change week day. */
	private Button mBtnChangeWeekDay;
	private Button btn_icon_gender;
	private Button btn_icon_weight;

	/** The m btn show instructions. */
	private ImageButton mBtnShowInstructions;

	/** The m txt times. */
	private TextView mTxtTimes;

	/** The m txt min. */
	private TextView mTxtMin;

	/** The m txt max. */
	private TextView mTxtMax;

	/** The m example image width. */
	private int mExampleImageWidth;

	/** The m example image height. */
	private int mExampleImageHeight;

	/** The m currently showing a male on images. */
	private boolean mCurrentlyShowingAMaleOnImages;

	/** The m first time. */
	public boolean mFirstTime;

	/** The m selected exercise group position. */
	private int mSelectedExerciseGroupPosition;

	/** The m selected exercise child position. */
	private int mSelectedExerciseChildPosition;

	/** The m info selected exercise group position. */
	private int mInfoSelectedExerciseGroupPosition;

	/** The m info selected exercise child position. */
	private int mInfoSelectedExerciseChildPosition;

	/** The m adapter. */
	private ExerciseAdapter mAdapter;

	/** The m img cache. */
	private ImageCache mImgCache;

	/** The m image gallery adapter. */
	private ImageGalleryAdapter mImageGalleryAdapter;

	/** The m pager. */
	private ViewPager mPager;

	/** The m listener. */
	private OnMoreDetailsClickListener mListener;

	/** The m mgv unit type. */
	private ImageView mMgvUnitType;

	/** The m mgv unit type min. */
	private ImageView mMgvUnitTypeMin;

	/** The m mgv unit type max. */
	private ImageView mMgvUnitTypeMax;

	/** The m lnr indication strip. */
	private View mLnrIndicationStrip;

	/** The m expanded groups. */
	private ArrayList<Integer> mExpandedGroups;

	/** The btn active. */
	private Button btnActive;

	/** The btn complete. */
	private Button btnComplete;

	/** The m account mngr. */
	private static SportsWorldAccountManager mAccountMngr;

	/** The status routine. */
	private static boolean statusRoutine;

	/** The exercise groups. */
	List<ExerciseGroup> exerciseGroups;

	/** The complete all. */
	boolean completeAll = true;

	/** The end activity. */
	static boolean endActivity = true;

	/** The alert face. */
	public AlertDialog alertFace = null;

	/** The kg min. */
	private static String kgMin = "";

	/** The kg max. */
	private static String kgMax = "";

	/** The lb min. */
	private static String lbMin = "";

	/** The lb max. */
	private static String lbMax = "";

	/** The memunic_id. */
	private static String memunic_id = "";

	/** The curr day. */
	private int currDay;

	/** The curr week. */
	private int currWeek;

	/** The units kg. */
	private boolean unitsKG = false;

	/** The img menu female. */
	private boolean imgMenuFemale;

	/** The img header female. */
	private boolean imgHeaderFemale;

	/** The block menu units. */
	private boolean blockMenuUnits = false;

	/** The progress. */
	public static ProgressDialogFragment progress;

	/** The full image fragment. */
	public static FragmentFullImageDialog fullImageFragment;

	/** The dialgo fragment. */
	Fragment dialgoFragment;

	/** The image fragment. */
	Fragment imageFragment;

	/** The view. */
	View view;

	/** The parent view pressed. */
	View parentViewPressed;

	/** The child selected. */
	public int parentSelected = 0, childSelected = 0;

	/** The first selected. */
	public boolean firstSelected = true;

	/** The expandable list view. */
	ExpandableListView expandableListView;

	/** The block gender. */
	public boolean blockGender = false;

	/** The waitface. */
	public boolean waitface = false;

	/** The change list. */
	public boolean changeList = false;

	/** The change units. */
	public boolean changeUnits = true;

	/** The res. */
	Routine res = null;

	/** The Constant PERMISSION_PUBLISH_STREAM. */
	private static final String PERMISSION_PUBLISH_STREAM = "publish_stream";

	/** The permissions. */
	public static List<String> PERMISSIONS = null;

	/** The m facebook ui helper. */
	private UiLifecycleHelper mFacebookUiHelper;

	/** The new fragment. */
	public static FragmentDialogRoutineInfo tourFragment;

	/** The pending publish reauthorization. */
	private boolean pendingPublishReauthorization = false;

	/** The Constant PENDING_PUBLISH_KEY. */
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";

	/**
	 * On session state change.
	 * 
	 * @param session
	 *            the session
	 * @param state
	 *            the state
	 * @param exception
	 *            the exception
	 */
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			if (pendingPublishReauthorization
					&& state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
				pendingPublishReauthorization = false;
				waitface = true;
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
	 * Builds the user columns.
	 * 
	 * @return the string[]
	 */
	private static String[] buildUserColumns() {
		final SparseArray<String> colsMap = new SparseArray<String>();
		colsMap.put(COL_INDEX_USER_GENDER_ID,
				SportsWorldContract.User.GENDER_ID);
		colsMap.put(COL_INDEX_USER_MEMUNIC,
				SportsWorldContract.User.MEM_UNIQ_ID);

		final int colCount = colsMap.size();
		final String[] cols = new String[colCount];
		for (int i = 0; i < colCount; i++) {
			cols[i] = colsMap.get(i);
		}

		return cols;
	}

	/**
	 * Builds the trainin columns.
	 * 
	 * @return the string[]
	 */
	private static String[] buildTraininColumns() {
		final SparseArray<String> colsMap = new SparseArray<String>();
		colsMap.put(COL_TRAININ_ID, SportsWorldContract.Trainin._ID);
		colsMap.put(COL_TRAININ_NAME, SportsWorldContract.Trainin.EXERCISE_NAME);
		colsMap.put(COL_TRAININ_IMAGE,
				SportsWorldContract.Trainin.EXCERCISE_IMAGE);

		final int colCount = colsMap.size();
		final String[] cols = new String[colCount];
		for (int i = 0; i < colCount; i++) {
			cols[i] = colsMap.get(i);
		}

		return cols;
	}

	/**
	 * Builds the exercise columns.
	 * 
	 * @return the string[]
	 */
	private static String[] buildExerciseColumns() {
		final SparseArray<String> colsMap = new SparseArray<String>();
		colsMap.put(COL_INDEX_EXERCISE_MUSCLE_WORKED,
				SportsWorldContract.Exercise.MUSCLE_WORKED);
		colsMap.put(COL_INDEX_EXERCISE_ACTIVE,
				SportsWorldContract.Exercise.CURRENT);
		colsMap.put(COL_INDEX_EXERCISE_CIRCUIT_NAME,
				SportsWorldContract.Exercise.CIRCUIT_NAME);
		colsMap.put(COL_INDEX_EXERCISE_EXAMPLE_IMAGES_MEN_URLS,
				SportsWorldContract.Exercise.EXAMPLE_IMAGES_MEN_URLS);
		colsMap.put(COL_INDEX_EXERCISE_EXAMPLE_IMAGES_WOMEN_URLS,
				SportsWorldContract.Exercise.EXAMPLE_IMAGES_WOMEN_URLS);
		colsMap.put(COL_INDEX_EXERCISE_MAXIMUM_VALUE,
				SportsWorldContract.Exercise.MAXIMUM_VALUE);
		colsMap.put(COL_INDEX_EXERCISE_MINIMUM_VALUE,
				SportsWorldContract.Exercise.MINIMUM_VALUE);
		colsMap.put(COL_INDEX_EXERCISE_SERIES,
				SportsWorldContract.Exercise.SERIES);
		colsMap.put(COL_INDEX_EXERCISE_UNIT, SportsWorldContract.Exercise.UNIT);
		colsMap.put(COL_INDEX_EXERCISE_NAME,
				SportsWorldContract.Exercise.EXERCISE_NAME);
		colsMap.put(COL_INDEX_DONE, SportsWorldContract.Exercise.DONE);
		colsMap.put(COL_INDEX_ID, SportsWorldContract.Exercise._ID);
		colsMap.put(COL_INDEX_EXERCISE_MAXIMUM_LB_VALUE,
				SportsWorldContract.Exercise.MAXIMUM_WEIGHT_LB);
		colsMap.put(COL_INDEX_EXERCISE_MINIMUM_LB_VALUE,
				SportsWorldContract.Exercise.MINIMUM_WEIGHT_LB);

		final int colCount = colsMap.size();
		final String[] cols = new String[colCount];
		for (int i = 0; i < colCount; i++) {
			cols[i] = colsMap.get(i);
		}

		return cols;
	}

	/**
	 * New instance.
	 * 
	 * @param routineId
	 *            the routine id
	 * @param isRoutine
	 *            the is routine
	 * @return the routine fragment
	 */
	public static RoutineFragment newInstance(long routineId, boolean isRoutine) {
		final RoutineFragment f = new RoutineFragment();
		final Bundle args = new Bundle();
		args.putLong(FRAG_ARG_ROUTINE_ID, routineId);
		f.setArguments(args);
		statusRoutine = isRoutine;
		return f;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockFragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnMoreDetailsClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.getClass().getName()
					+ " must implement "
					+ OnMoreDetailsClickListener.class.getName() + ".");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sportsworld.android.app.SherlockWorkingExpandableListFragment#
	 * onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 * android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_routine, null /* root */,
				false /* attachToRoot */);

		if (savedInstanceState == null) {
			showFragmentsDialog(false);
		} else {
			showFragmentsDialog(true);
			pendingPublishReauthorization = savedInstanceState.getBoolean(
					PENDING_PUBLISH_KEY, false);

		}
		SportsWorldPreferences.setInfoRoutine(getActivity(), null);

		imgMenuFemale = SportsWorldPreferences.getGuestGender(getActivity())
				.equals("Femenino");
		imgHeaderFemale = imgMenuFemale;

		mMgvUnitType = (ImageView) view.findViewById(R.id.mgv_repeat_times);
		mMgvUnitType.setImageDrawable(getResources().getDrawable(
				R.drawable.repeticiones));

		mMgvUnitTypeMin = (ImageView) view.findViewById(R.id.mgv_unit_type_min);
		mMgvUnitTypeMax = (ImageView) view.findViewById(R.id.mgv_unit_type_max);
		mLnrIndicationStrip = view.findViewById(R.id.lnr_indication_strip);

		mPager = (ViewPager) view.findViewById(R.id.pager);
		mPager.setOnClickListener(this);
		mTxtTimes = (TextView) view.findViewById(R.id.txt_times);
		mTxtMin = (TextView) view.findViewById(R.id.txt_min);
		mTxtMax = (TextView) view.findViewById(R.id.txt_max);

		mBtnChangeWeekDay = (Button) view
				.findViewById(R.id.btn_change_week_day);

		btn_icon_gender = (Button) view.findViewById(R.id.btn_icon_gender);
		btn_icon_gender.setOnClickListener(this);

		if (imgMenuFemale)
			btn_icon_gender.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.fotos_hombre));
		else
			btn_icon_gender.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.fotos_mujer));

		btn_icon_weight = (Button) view.findViewById(R.id.btn_icon_weight);
		btn_icon_weight.setOnClickListener(this);

		mBtnShowInstructions = (ImageButton) view
				.findViewById(R.id.btn_show_instructions);
		mBtnChangeWeekDay.setOnClickListener(this);
		mBtnShowInstructions.setOnClickListener(this);

		btnActive = (Button) view.findViewById(R.id.btnActivar);
		btnActive.setOnClickListener(this);
		btnComplete = (Button) view.findViewById(R.id.btnCompletar);
		btnComplete.setOnClickListener(this);

		if (!statusRoutine)
			btnActive.setText("Iniciar Rutina");
		else
			btnActive.setText("Cancelar Rutina");

		if (!SportsWorldPreferences.getGuestId(getActivity()).equals("")) {
			btnActive.setVisibility(View.GONE);
			btnComplete.setVisibility(View.GONE);
		}
		if (SportsWorldPreferences.getRoutineId(getActivity()) == 0)
			btnComplete.setVisibility(View.GONE);

		return view;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setHasOptionsMenu(true);
		setRetainInstance(true);

		ExerciseAdapter.group = 0;
		ExerciseAdapter.child = 0;

		mMgvUnitTypeMin.setImageDrawable(getResources().getDrawable(
				R.drawable.img_pesomin_lbs));
		mMgvUnitTypeMax.setImageDrawable(getResources().getDrawable(
				R.drawable.img_pesomin_lbs));

		mExpandedGroups = new ArrayList<Integer>();
		mSelectedExerciseGroupPosition = -1;
		mSelectedExerciseChildPosition = -1;
		if (savedInstanceState != null) {

			mSelectedExerciseGroupPosition = savedInstanceState
					.getInt(STATE_SELECTED_EXERCISE_GROUP_POSITION);
			mSelectedExerciseChildPosition = savedInstanceState
					.getInt(STATE_SELECTED_EXERCISE_CHILD_POSITION);

			mCurrentlyShowingAMaleOnImages = savedInstanceState
					.getBoolean(STATE_CURRENTLY_SHOWING_A_MALE_IN_IMAGES);

			mLnrIndicationStrip.setVisibility(savedInstanceState
					.getInt(STATE_INDICATION_STRIP_SHOWN));

			mExpandedGroups = savedInstanceState
					.getIntegerArrayList(STATE_EXPANDED_GROUPS);

			/*
			 * We are losing the current page index. I'm not sure if this is a
			 * bug on ViewPager, on our code or this is the way it should work.
			 * Anyway, we'll save it on onSaveInstanceState, recover it here and
			 * set it on onLoadFinished.
			 */
		}

		mImgCache = ImageCache.findOrCreateCache(getActivity(), THUMB_FOLDER);

		mAdapter = new ExerciseAdapter(getActivity() /* context */);
		mAdapter.setOnExerciseValueChangedListener(this);

		mAccountMngr = new SportsWorldAccountManager((Context) getActivity());

		setListShown(false);
		setExclusiveExpand(false);
		setExpandableListAdapter(mAdapter, true);
		expandableListView = getExpandableListView();
		expandableListView.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);
		final ViewTreeObserver pagerViewTreeObserver = mPager
				.getViewTreeObserver();
		if (pagerViewTreeObserver.isAlive()) {
			pagerViewTreeObserver
					.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

						@SuppressWarnings("deprecation")
						// We use the new method when supported
						@SuppressLint("NewApi")
						// We check which build version we are using.
						@Override
						public void onGlobalLayout() {
							if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
								mPager.getViewTreeObserver()
										.removeGlobalOnLayoutListener(this);
							} else {
								mPager.getViewTreeObserver()
										.removeOnGlobalLayoutListener(this);
							}

							mExampleImageWidth = mPager.getWidth();
							mExampleImageHeight = mPager.getHeight();

							mImageGalleryAdapter = new ImageGalleryAdapter(
									getChildFragmentManager(), getActivity(),
									mImgCache, mExampleImageWidth,
									mExampleImageHeight, true);

							mPager.setAdapter(mImageGalleryAdapter);
							getLoaderManager()
									.initLoader(LOADER_ID_USER,
											null /* loaderArgs */,
											RoutineFragment.this /* loaderCallback */);

						}

					});

			mFacebookUiHelper = new UiLifecycleHelper(
					getActivity() /* context */, mFacebookSessionCallback);
			mFacebookUiHelper.onCreate(savedInstanceState);

		}

		final ViewTreeObserver expandableListViewViewTreeObserver = expandableListView
				.getViewTreeObserver();
		if (expandableListViewViewTreeObserver.isAlive()) {
			expandableListViewViewTreeObserver
					.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

						@SuppressWarnings("deprecation")
						// We use the new method when supported
						@SuppressLint("NewApi")
						// We check which build version we are using.
						@Override
						public void onGlobalLayout() {
							if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
								expandableListView.getViewTreeObserver()
										.removeGlobalOnLayoutListener(this);
							} else {
								expandableListView.getViewTreeObserver()
										.removeOnGlobalLayoutListener(this);
							}

							expandableListView.setChildIndicatorBounds(
									expandableListView.getRight() - 40,
									expandableListView.getWidth());

						}

					});
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_EXERCISE_CHILD_POSITION,
				mSelectedExerciseChildPosition);
		outState.putInt(STATE_SELECTED_EXERCISE_GROUP_POSITION,
				mSelectedExerciseGroupPosition);
		outState.putInt(STATE_CURRENT_IMAGE_INDEX, mPager.getCurrentItem());
		outState.putBoolean(STATE_CURRENTLY_SHOWING_A_MALE_IN_IMAGES,
				mCurrentlyShowingAMaleOnImages);
		outState.putInt(STATE_INDICATION_STRIP_SHOWN,
				mLnrIndicationStrip.getVisibility());
		outState.putIntegerArrayList(STATE_EXPANDED_GROUPS, mExpandedGroups);
		outState.putInt(STATE_CUR_CHECKED_INDEX, getExpandableListView()
				.getCheckedItemPosition());

		if (mFacebookUiHelper != null)
			mFacebookUiHelper.onSaveInstanceState(outState);
		outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
		mFacebookUiHelper.onSaveInstanceState(outState);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		super.onStop();
		mImgCache.close();
	}

	/**
	 * Gets the routine id from args.
	 * 
	 * @return the routine id from args
	 */
	private long getRoutineIdFromArgs() {
		return getArguments().getLong(FRAG_ARG_ROUTINE_ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		mFacebookUiHelper.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		mFacebookUiHelper.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode != REQUEST_CODE_GET_ROUTINE) {
			super.onActivityResult(requestCode, resultCode, data);
			return;
		}

		if (resultCode == Activity.RESULT_OK) {
			getLoaderManager().initLoader(LOADER_ID_EXERCISE,
					null /* loaderArgs */, this /* loaderCallback */);
			getLoaderManager().initLoader(LOADER_ID_TRAININ, null, this);
		}
		if (resultCode == Activity.RESULT_CANCELED && waitface == false) {
			Toast.makeText(getActivity(),
					getResources().getString(R.string.error_connection_server),
					Toast.LENGTH_SHORT).show();
			backMenu(getActivity());
		}
		mFacebookUiHelper.onActivityResult(requestCode, resultCode, data);

		progress.dismissAllowingStateLoss();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int,
	 * android.os.Bundle)
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		switch (id) {

		case LOADER_ID_USER:

			final SportsWorldAccountManager accountMngr = new SportsWorldAccountManager(
					getActivity() /* context */);
			final Uri userUri = SportsWorldContract.User
					.buildUserUri(accountMngr.getCurrentUserId());
			return new CursorLoader(getActivity() /* context */, userUri,
					COLS_USER, null /* selection */, null /* selectionArgs */,
					null /* sortOrder */);

		case LOADER_ID_EXERCISE:
			final Uri uri = SportsWorldContract.Exercise.buildExerciseWeekDay(
					String.valueOf("0"), String.valueOf("0"));
			return new CursorLoader(getActivity() /* context */, uri,
					COLS_EXERCISE, null /* selection */,
					null /* selectionArgs */, null /* sortOrder */);

		case LOADER_ID_TRAININ:
			final Uri traininUri = SportsWorldContract.Trainin
					.buildTraininUri();
			return new CursorLoader(getActivity() /* context */, traininUri,
					COLS_TRAININ, null /* selection */,
					null /* selectionArgs */, null /* sortOrder */);
		default:
			throw new IllegalArgumentException("Unknown loader id: " + id);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android
	 * .support.v4.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		switch (loader.getId()) {

		case LOADER_ID_USER:

			if (!cursor.moveToFirst()) {
				new SportsWorldAccountManager(getActivity() /* context */)
						.logOut();
				throw new RuntimeException(
						"User not found!. Log out and the crash.");
			}

			mCurrentlyShowingAMaleOnImages = (cursor
					.getLong(COL_INDEX_USER_GENDER_ID) == UserProfile.GENDER_MALE);
			getRoutine();
			memunic_id = cursor.getString(COL_INDEX_USER_MEMUNIC);
			break;

		case LOADER_ID_EXERCISE:

			if (isResumed()) {
				setListShownNoAnimation(true);
			} else {
				setListShown(true);
			}
			ExerciseAdapter.group = 0;
			ExerciseAdapter.child = 0;

			mAdapter.setExerciseGroups(null);
			exerciseGroups = new ExerciseGroupParser().parse(cursor);
			mAdapter.setExerciseGroups(exerciseGroups);

			mBtnChangeWeekDay.setVisibility(View.VISIBLE);
			onWeekDayChange(SportsWorldPreferences.getRoutineWeek(getActivity()
					.getApplicationContext()),
					SportsWorldPreferences.getRoutinetDay(getActivity()
							.getApplicationContext()));

			getLoaderManager().destroyLoader(LOADER_ID_EXERCISE);
			expandableListView.expandGroup(0);
			populateIndicationStrip(true, 0);
			expandableListView.setSelectedGroup(0);
			break;
		case LOADER_ID_TRAININ:

			while (cursor.moveToNext()) {
				List<String> group = new ArrayList<String>(1);
				mImageGalleryAdapter.setUrls(group);
				populateIndicationStrip(false, 0);
			}

			break;
		default:
			throw new IllegalArgumentException("Unknown loader id: "
					+ loader.getId());

		}

	}

	/**
	 * Gets the routine.
	 * 
	 * @return the routine
	 */
	private void getRoutine() {

		final Intent data = new Intent();
		final Activity activity = getActivity();
		final PendingIntent pendingIntent = activity.createPendingResult(
				REQUEST_CODE_GET_ROUTINE, data, 0 /* flags */);
		final Intent getRoutineService = new Intent(activity,
				GetRoutineService.class);
		getRoutineService.putExtra(GetRoutineService.EXTRA_ROUTINE_ID,
				getRoutineIdFromArgs());
		getRoutineService.putExtra(GetRoutineService.EXTRA_PENDING_INTENT,
				pendingIntent);
		activity.startService(getRoutineService);

	}

	/**
	 * On week day change.
	 * 
	 * @param weekId
	 *            the week id
	 * @param dayId
	 *            the day id
	 * @param weekPickerValue
	 *            the week picker value
	 * @param dayPickerValue
	 *            the day picker value
	 */
	public void onWeekDayChange(long weekId, long dayId, int weekPickerValue,
			int dayPickerValue) {
		showFragmentsDialog(false);
		currDay = dayPickerValue;
		currWeek = weekPickerValue;
		changeList = true;
		mBtnChangeWeekDay.setText(String.format(getString(R.string.week_day),
				weekPickerValue, dayPickerValue));

		if (SportsWorldPreferences.getCurrentWeekId(getActivity()) == weekId
				&& SportsWorldPreferences.getCurrentDayId(getActivity()) == dayId) {

			btnComplete.setVisibility(View.VISIBLE);
			SportsWorldPreferences.setInfoRoutine(getActivity(), null);
			res = null;
			ExerciseAdapter.showCheckboxes = true;

			getRoutine();

		} else {
			ExerciseAdapter.showCheckboxes = false;
			btnComplete.setVisibility(View.GONE);
			getRoutine(weekId, dayId);
		}
	}

	/**
	 * On week day change.
	 * 
	 * @param weekPickerValue
	 *            the week picker value
	 * @param dayPickerValue
	 *            the day picker value
	 */
	public void onWeekDayChange(String weekPickerValue, String dayPickerValue) {
		currDay = Integer.parseInt(dayPickerValue);
		currWeek = Integer.parseInt(weekPickerValue);
		mBtnChangeWeekDay.setText(String.format(getString(R.string.week_day),
				weekPickerValue, dayPickerValue));
	}

	/**
	 * Gets the routine.
	 * 
	 * @param weekId
	 *            the week id
	 * @param dayId
	 *            the day id
	 * @return the routine
	 */
	private void getRoutine(long weekId, long dayId) {

		RoutinePojo pojo = new RoutinePojo();
		pojo.setIdRoutine(getRoutineIdFromArgs() + "");

		if (SportsWorldPreferences.getRoutineId(getActivity()) != 0)
			pojo.setId_user(mAccountMngr.getCurrentUserId());
		else
			pojo.setId_user("0");

		pojo.setWeek_id(weekId + "");
		pojo.setDay_id(dayId + "");

		RoutineTask task = new RoutineTask(new ResponseInterface() {

			@SuppressWarnings("serial")
			@Override
			public void onResultResponse(Object obj) {
				res = (Routine) obj;

				if (isResumed()) {
					setListShownNoAnimation(true);
				} else {
					setListShown(true);
				}
				ExerciseAdapter.group = 0;
				ExerciseAdapter.child = 0;

				mAdapter.setExerciseGroups(null);
				if (res != null && res.isStatus()) {
					List<ExerciseGroup> groups = new ArrayList<ExerciseGroup>(
							res.getmTraining().size());
					for (int a = 0; a < res.getmTraining().size(); a++) {

						int contador = 1;
						for (int j = 0; j < res.getmTraining().get(a)
								.getListExcercise().size(); j++) {

							res.getmTraining()
									.get(a)
									.getListExcercise()
									.get(j)
									.setmExerciseName(
											"S"
													+ contador
													+ " "
													+ res.getmTraining().get(a)
															.getListExcercise()
															.get(j).getName());
							contador++;

						}
						ExerciseGroup gorup = new ExerciseGroup(res
								.getmTraining().get(a).getListExcercise());

						groups.add(gorup);
					}
					List<Exercise> reco;
					reco = new ArrayList<Exercise>(1) {
					};

					reco.clear();
					mAdapter.setExerciseGroups(groups);
					mAdapter.notifyDataSetChanged();

					expandableListView.expandGroup(0);
					populateIndicationStrip(true, 0);

					getExpandableListView().setSelection(0);
					changeList = false;
					SportsWorldPreferences
							.setInfoRoutine(
									getActivity(),
									res.getmTraining().get(0)
											.getListExcercise().get(0).mInstructions);
				} else

					Toast.makeText(
							getActivity(),
							getResources().getString(
									R.string.error_connection_server),
							Toast.LENGTH_SHORT).show();

				new Handler().post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						progress.dismissAllowingStateLoss();
					}
				});

			}
		});

		task.ctx = (Context) getActivity();
		task.execute(pojo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android
	 * .support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.setExerciseGroups(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockFragment#onCreateOptionsMenu(android
	 * .view.Menu, android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.routine, menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockFragment#onOptionsItemSelected(android
	 * .view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_info_routine:
			openTourInfoFragment();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	/**
	 * Active routine.
	 */
	private void activeRoutine() {

		showFragmentsDialog(false);

		CheckRoutineTask checkRoutine = new CheckRoutineTask(
				new ResponseInterface() {

					@Override
					public void onResultResponse(Object obj) {
						// TODO Auto-generated method stub
						RoutinePojo result = (RoutinePojo) obj;
						if (result != null)
							if (result.getIdRoutine().equals("0")) {

								RoutinePojo pojo = new RoutinePojo();
								pojo.setIdRoutine(getRoutineIdFromArgs() + "");
								pojo.setId_user(mAccountMngr.getCurrentUserId());
								ActiveRoutineTask task = new ActiveRoutineTask(
										new ResponseInterface() {

											@Override
											public void onResultResponse(
													Object obj) {
												RoutinePojo pojo = (RoutinePojo) obj;
												if (pojo.isStatus()) {

													Toast.makeText(
															getActivity(),
															getResources()
																	.getString(
																			R.string.routine_success_active),
															Toast.LENGTH_SHORT)
															.show();

													FacebookUtils.wherePost = "He activado una rutina en Upster";
													FacebookUtils.activity = getSherlockActivity();
													alertFace = FacebookUtils
															.buldAlertFace(
																	"Activ� la rutina "
																			+ SportsWorldPreferences
																					.getRoutineName(getActivity()
																							.getApplicationContext())
																			+ " desde la app Upster Android",
																	getActivity(),
																	true);

													Session session = Session
															.getActiveSession();

													if (session == null)
														session = Session
																.openActiveSessionFromCache(getActivity());
													if (session != null
															|| SportsWorldPreferences
																	.getTwitterAct(getActivity())) {

														waitface = true;
														alertFace.show();

													} else
														FacebookUtils
																.backMenu(getActivity());

												} else
													Toast.makeText(
															getActivity() /* context */,
															getResources()
																	.getString(
																			R.string.error_connection_server),
															Toast.LENGTH_SHORT)
															.show();

											}

										});

								ActiveRoutineTask.mContext = (Context) getActivity();

								task.execute(pojo);
							} else {
								Toast.makeText(
										getActivity(),
										getResources()
												.getString(
														R.string.error_duplicate_routine),
										Toast.LENGTH_SHORT).show();
								Intent goToMainboard = new Intent(
										getActivity(), MainboardActivity.class);
								goToMainboard
										.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								goToMainboard
										.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								getActivity().startActivity(goToMainboard);

							}
						else
							Toast.makeText(
									getActivity() /* context */,
									getResources().getString(
											R.string.error_connection_server),
									Toast.LENGTH_SHORT).show();
					}
				});
		checkRoutine.execute(SportsWorldPreferences
				.getCurrentUserId(getActivity()));

	}

	/**
	 * Update routine.
	 */
	public void updateRoutine() {

		showFragmentsDialog(false);

		RoutinePojo pojo = new RoutinePojo();
		mAccountMngr = new SportsWorldAccountManager((Context) getActivity());
		pojo.setIdRoutine(getRoutineIdFromArgs() + "");
		pojo.setId_user(mAccountMngr.getCurrentUserId());
		UpdateRoutineTask task = new UpdateRoutineTask(new ResponseInterface() {

			@Override
			public void onResultResponse(Object obj) {
				RoutinePojo pojo = (RoutinePojo) obj;

				if (pojo.isStatus()) {

					Toast.makeText(
							getActivity(),
							getResources().getString(
									R.string.routine_success_deactive),
							Toast.LENGTH_SHORT).show();

					SportsWorldPreferences.setRoutineId(getActivity(), 0);
					backMenu(getActivity());
				} else if (!pojo.isStatus()) {
					getActivity().finish();

					Toast.makeText(
							getActivity(),
							getResources().getString(
									R.string.error_routine_info),
							Toast.LENGTH_SHORT).show();

					if (progress != null)
						progress.dismissAllowingStateLoss();

				}

				new Handler().post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (progress != null)
							progress.dismissAllowingStateLoss();
					}
				});

			}

		});

		task.mContext = (Context) getActivity();
		task.execute(pojo);
	}

	/**
	 * Toggle gender images.
	 * 
	 * @return true, if successful
	 */
	private boolean toggleGenderImages() {
		if ((mSelectedExerciseGroupPosition == -1)
				|| (mSelectedExerciseChildPosition == -1)) {

			Toast.makeText(
					getActivity(),
					getResources().getString(
							R.string.error_exercise_not_selected),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		mCurrentlyShowingAMaleOnImages = !mCurrentlyShowingAMaleOnImages;
		populateIndicationStrip(false, 0);

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sportsworld.android.app.SherlockWorkingExpandableListFragment#
	 * onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		cleanReferences();
		view = null;
		super.onDestroyView();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_change_week_day:

			if (isNetworkAvailable())
				showPickerDialog(currDay, currWeek);
			break;
		case R.id.btn_show_instructions:
			if (isNetworkAvailable())
				goToInstructions();
			break;

		case R.id.btnActivar:
			if (isNetworkAvailable())
				if (!isMember()) {

					Toast.makeText(
							getActivity(),
							getResources().getString(
									R.string.error_not_a_member),
							Toast.LENGTH_SHORT).show();
				} else {
					if (!statusRoutine)
						activeRoutine();
					else
						updateRoutine();
				}
			break;
		case R.id.btnCompletar:
			checkAll(completeAll);
			if (isNetworkAvailable())
				checkAllRoutines();
			break;
		case R.id.pager:
			mPager.getCurrentItem();
			break;
		case R.id.btn_icon_gender:
			if (imgMenuFemale == false) {
				btn_icon_gender.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.fotos_hombre));
				imgMenuFemale = true;
			} else {
				btn_icon_gender.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.fotos_mujer));
				imgMenuFemale = false;
			}
			if (blockGender == false)
				toggleGenderImages();
			imgHeaderFemale = imgMenuFemale;
			break;
		case R.id.btn_icon_weight:
			if (blockMenuUnits == false) {
				if (unitsKG) {
					btn_icon_weight.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.btn_kg));
					unitsKG = false;
				} else {
					btn_icon_weight.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.btn_lbs));
					unitsKG = true;
				}
				if (changeUnits)
					changeUnits();
			}
			break;
		default:
			if (tourFragment != null)
				tourFragment.dismiss();
			break;
		}
	}

	/**
	 * Show picker dialog.
	 * 
	 * @param day
	 *            the day
	 * @param week
	 *            the week
	 */
	private void showPickerDialog(int day, int week) {
		mExpandedGroups.clear();
		final DialogFragment df = WeekDayPickerDialogFragment.newInstance(day,
				week);
		df.show(getChildFragmentManager(), FRAG_TAG_WEEK_DAY_PICKER);
	}

	/**
	 * Go to instructions.
	 */
	private void goToInstructions() {
		if ((mInfoSelectedExerciseGroupPosition == -1)
				|| (mInfoSelectedExerciseChildPosition == -1)) {

			Toast.makeText(
					getActivity(),
					getResources().getString(
							R.string.error_exercise_not_selected),
					Toast.LENGTH_SHORT).show();
			return;
		}
		mListener.onInstructionsClicked(mAdapter.getChildId(
				mInfoSelectedExerciseGroupPosition,
				mInfoSelectedExerciseChildPosition));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sportsworld.android.app.SherlockWorkingExpandableListFragment#
	 * onListGroupExpandListener(int)
	 */
	@Override
	public void onListGroupExpandListener(int groupPosition) {
		mLnrIndicationStrip.setVisibility(View.VISIBLE);

		ExerciseAdapter.group = groupPosition;
		ExerciseAdapter.child = 0;

		populateIndicationStrip(true, groupPosition);
		mInfoSelectedExerciseGroupPosition = groupPosition;
		mInfoSelectedExerciseChildPosition = 0;

		if (res != null)
			SportsWorldPreferences.setInfoRoutine(getActivity(), res
					.getmTraining().get(groupPosition).getListExcercise()
					.get(0).mInstructions);

		// }

		if (!mExpandedGroups.contains(groupPosition)) {
			mExpandedGroups.add(groupPosition);
		}
		blockGender = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sportsworld.android.app.SherlockWorkingExpandableListFragment#
	 * onListGroupCollapseListener(int)
	 */
	@Override
	public void onListGroupCollapseListener(int groupPosition) {
		blockGender = true;
		mExpandedGroups.remove((Integer) groupPosition);
		mPager.setBackgroundResource(R.drawable.img_gray);
		if (mExpandedGroups.size() == 0) {
			mPager.setBackgroundResource(R.drawable.empty_photo);
			mPager.removeAllViews();
			mLnrIndicationStrip.setVisibility(View.GONE);
			mInfoSelectedExerciseGroupPosition = -1;
			mInfoSelectedExerciseChildPosition = -1;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sportsworld.android.app.SherlockWorkingExpandableListFragment#
	 * onListGroupClick(android.widget.ExpandableListView, android.view.View,
	 * int, long)
	 */
	@Override
	public void onListGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		// TODO Auto-generated method stub

		super.onListGroupClick(parent, v, groupPosition, id);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sportsworld.android.app.SherlockWorkingExpandableListFragment#
	 * onListChildClick(android.widget.ExpandableListView, android.view.View,
	 * int, int, long)
	 */
	@Override
	public void onListChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		mSelectedExerciseGroupPosition = groupPosition;
		mSelectedExerciseChildPosition = childPosition;
		mInfoSelectedExerciseGroupPosition = groupPosition;
		mInfoSelectedExerciseChildPosition = childPosition;

		ExerciseAdapter.group = groupPosition;
		ExerciseAdapter.child = childPosition;

		if (res != null)
			SportsWorldPreferences.setInfoRoutine(getActivity(),
					res.getmTraining().get(groupPosition).getListExcercise()
							.get(childPosition).mInstructions);

		v.setBackgroundColor(getResources().getColor(R.color.transparent_black));

		mAdapter.notifyDataSetChanged();

		parentSelected = groupPosition;
		childSelected = childPosition;

		SportsWorldPreferences.setChckBoxAdvice(getActivity(), true);

		populateIndicationStrip(false, 0);
	}

	/**
	 * Populate indication strip.
	 * 
	 * @param firstTime
	 *            the first time
	 * @param expandList
	 *            the expand list
	 */
	private void populateIndicationStrip(boolean firstTime, int expandList) {

		if (firstTime) {
			mSelectedExerciseGroupPosition = expandList;
			mSelectedExerciseChildPosition = 0;

		}
		if ((mSelectedExerciseGroupPosition == -1)
				|| (mSelectedExerciseChildPosition == -1)) {
			return;
		}

		final Exercise exercise = mAdapter.getChild(
				mSelectedExerciseGroupPosition, mSelectedExerciseChildPosition);

		mLnrIndicationStrip.setVisibility(View.VISIBLE);

		mTxtMax.setText(Math.round(exercise.getMaximumValue()) + "");

		kgMax = Math.round(exercise.getMaximumValue()) + "";
		lbMax = Math.round(exercise.getMaximumWeightLb()) + "";

		mTxtMin.setText(Math.round(exercise.getMinimumValue()) + "");
		kgMin = (int) exercise.getMinimumValue() + "";
		lbMin = (int) exercise.getMinimumWeightLb() + "";
		if (exercise.getUnit().equals("min")) {
			blockMenuUnits = true;

			mMgvUnitTypeMin.setVisibility(View.VISIBLE);
			mMgvUnitTypeMax.setVisibility(View.VISIBLE);
			mMgvUnitTypeMin.setImageDrawable(getResources().getDrawable(
					R.drawable.hr_min_h));
			mMgvUnitTypeMax.setImageDrawable(getResources().getDrawable(
					R.drawable.hr_max_h));
			mMgvUnitType.setImageDrawable(getResources().getDrawable(
					R.drawable.tiempo));

		}

		if (exercise.getUnit().equals("dia")) {
			mTxtMax.setText("");
			mTxtMin.setText("");
			changeUnits = false;
			mMgvUnitTypeMin.setVisibility(View.GONE);
			mMgvUnitTypeMax.setVisibility(View.GONE);
			mMgvUnitType.setImageDrawable(getResources().getDrawable(
					R.drawable.descanso_id));
		}
		if (exercise.getUnit().equals("repeticiones")) {
			changeUnits = true;
			if (unitsKG) {
				mTxtMax.setText(kgMax);
				mTxtMin.setText(kgMin);
				mMgvUnitTypeMin.setVisibility(View.VISIBLE);
				mMgvUnitTypeMax.setVisibility(View.VISIBLE);
				mMgvUnitTypeMin.setImageDrawable(getResources().getDrawable(
						R.drawable.img_pesominimo));
				mMgvUnitTypeMax.setImageDrawable(getResources().getDrawable(
						R.drawable.img_pesomaximo));
				mMgvUnitType.setImageDrawable(getResources().getDrawable(
						R.drawable.repeticiones));
				blockMenuUnits = false;
			} else {
				mTxtMax.setText(lbMax);
				mTxtMin.setText(lbMin);
				mMgvUnitTypeMin.setVisibility(View.VISIBLE);
				mMgvUnitTypeMax.setVisibility(View.VISIBLE);
				mMgvUnitTypeMin.setImageDrawable(getResources().getDrawable(
						R.drawable.img_pesomin_lbs));
				mMgvUnitTypeMax.setImageDrawable(getResources().getDrawable(
						R.drawable.img_pesomax_lbs));
				mMgvUnitType.setImageDrawable(getResources().getDrawable(
						R.drawable.repeticiones));
				blockMenuUnits = false;
			}

		}

		mTxtTimes.setText(Math.round(exercise.getSeries()) + "");
		mPager.setBackgroundResource(R.drawable.img_gray);
		if (imgMenuFemale == true) {
			if (exercise.getExampleImagesWomenUrls().length == 0)
				mImageGalleryAdapter
						.setUrls(exercise.getExampleImagesMenUrls());
			else
				mImageGalleryAdapter.setUrls(exercise
						.getExampleImagesWomenUrls());
		} else {
			if (exercise.getExampleImagesMenUrls().length == 0)
				mImageGalleryAdapter.setUrls(exercise
						.getExampleImagesWomenUrls());
			else
				mImageGalleryAdapter
						.setUrls(exercise.getExampleImagesMenUrls());
		}

		mPager.setCurrentItem(0);

	}

	/**
	 * Header strip.
	 * 
	 * @param groupPosition
	 *            the group position
	 */
	public void HeaderStrip(int groupPosition) {
		final Exercise exercise = mAdapter.getChild(groupPosition, 0);
		if (exercise != null) {
			if (imgHeaderFemale == true) {
				if (exercise.getExampleImagesWomenUrls().length == 0)
					mImageGalleryAdapter.setUrls(exercise
							.getExampleImagesMenUrls());
				else
					mImageGalleryAdapter.setUrls(exercise
							.getExampleImagesWomenUrls());
			} else {

				if (exercise.getExampleImagesMenUrls().length == 0)
					mImageGalleryAdapter.setUrls(exercise
							.getExampleImagesWomenUrls());
				else
					mImageGalleryAdapter.setUrls(exercise
							.getExampleImagesMenUrls());

			}

		}
	}

	/**
	 * The listener interface for receiving onMoreDetailsClick events. The class
	 * that is interested in processing a onMoreDetailsClick event implements
	 * this interface, and the object created with that class is registered with
	 * a component using the component's
	 * <code>addOnMoreDetailsClickListener<code> method. When
	 * the onMoreDetailsClick event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see OnMoreDetailsClickEvent
	 */
	public static interface OnMoreDetailsClickListener {

		/**
		 * On nutrition advice clicked.
		 */
		void onNutritionAdviceClicked();

		/**
		 * On instructions clicked.
		 * 
		 * @param exerciseId
		 *            the exercise id
		 */
		void onInstructionsClicked(long exerciseId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sportsworld.android.adapter.ExerciseAdapter.
	 * OnExerciseValueChangedListener
	 * #onExerciseGroupChangedListener(com.sportsworld
	 * .android.model.ExerciseGroup, boolean)
	 */
	@Override
	public void onExerciseGroupChangedListener(
			final ExerciseGroup exerciseGroup, final boolean checked) {

		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				final ContentValues values = new ContentValues();
				values.put(SportsWorldContract.Exercise.DONE, checked);
				final int rowsUpdated = getActivity().getContentResolver()
						.update(SportsWorldContract.Exercise.CONTENT_URI,
								values,
								SportsWorldContract.Exercise.MUSCLE_WORKED
										+ "=?",
								new String[] { exerciseGroup.getName() });

				return (rowsUpdated > 0);

			}

			@Override
			protected void onPostExecute(Boolean success) {
				exerciseGroup.setDone(checked);
				checkAllRoutines();

				mAdapter.notifyDataSetChanged();
				if (changeList)
					getExpandableListView().setSelection(0);

				changeList = false;
				// XXX Not great, but works.
			};

		}.execute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sportsworld.android.adapter.ExerciseAdapter.
	 * OnExerciseValueChangedListener
	 * #onExerciseChangedListener(com.sportsworld.android.model.Exercise,
	 * boolean)
	 */
	@Override
	public void onExerciseChangedListener(final Exercise exercise,
			final boolean checked) {
		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {

				final ContentValues values = new ContentValues();
				values.put(SportsWorldContract.Exercise.DONE, checked);
				final int rowsUpdated = getActivity()
						.getContentResolver()
						.update(SportsWorldContract.Exercise.buildExerciseUri(String
								.valueOf(exercise.getId())), values,
								null /* selection */, null /* selectionArgs */);

				return (rowsUpdated > 0);

			}

			@Override
			protected void onPostExecute(Boolean success) {
				exercise.setDone(checked);
				mAdapter.notifyDataSetChanged(); // XXX Not great, but works.
			};

		}.execute();
	}

	/**
	 * Check all.
	 * 
	 * @param checked
	 *            the checked
	 */
	public void checkAll(boolean checked) {
		for (ExerciseGroup group : exerciseGroups) {

			final ContentValues values = new ContentValues();
			values.put(SportsWorldContract.Exercise.DONE, checked);
			final int rowsUpdated = getActivity().getContentResolver().update(
					SportsWorldContract.Exercise.CONTENT_URI, values,
					SportsWorldContract.Exercise.MUSCLE_WORKED + "=?",
					new String[] { group.getName() });

			if (!(rowsUpdated > 0))
				Log.i("kokuhso", "No se seleccionaron las rutinas");

			group.setDone(checked);

			mAdapter.notifyDataSetChanged();

		}

		if (completeAll)
			completeAll = false;
		else
			completeAll = true;

	}

	/**
	 * Check all routines.
	 */
	public void checkAllRoutines() {
		boolean allChecked = true;
		for (ExerciseGroup group : exerciseGroups) {
			if (!group.isDone())
				allChecked = false;
		}

		if (allChecked) {

			showFragmentsDialog(false);

			RoutinePojo pojo = new RoutinePojo();
			mAccountMngr = new SportsWorldAccountManager(
					(Context) getActivity());
			pojo.setIdRoutine(getRoutineIdFromArgs() + "");

			if (SportsWorldPreferences.getRoutineId(getActivity()) != 0)
				pojo.setId_user(mAccountMngr.getCurrentUserId());
			else
				pojo.setId_user("0");

			pojo.setWeek_id(SportsWorldPreferences
					.getNewRoutineWeek((Context) getActivity()) + "");
			pojo.setDay_id(SportsWorldPreferences
					.getNewRoutinetDay((Context) getActivity()) + "");

			NewRoutineTask task = new NewRoutineTask(new ResponseInterface() {

				@Override
				public void onResultResponse(Object obj) {
					RoutinePojo resPojo = (RoutinePojo) obj;

					if (resPojo.isStatus()) {
						getRoutine();
						completeAll = true;
					} else
						Toast.makeText(
								getActivity(),
								getResources().getString(
										R.string.error_routine_info),
								Toast.LENGTH_SHORT).show();

				}
			});
			NewRoutineTask.mContext = (Context) getActivity();
			if (isMember()
					&& SportsWorldPreferences.getRoutineId(getActivity()) != 0)
				task.execute(pojo);
			else {
				if (progress != null)
					progress.dismissAllowingStateLoss();
			}

		}

	}

	/**
	 * Back menu.
	 * 
	 * @param activity
	 *            the activity
	 */
	public static void backMenu(Activity activity) {
		final Intent mainBoard = new Intent(
				activity.getApplicationContext() /* context */,
				MainboardActivity.class);
		mainBoard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mainBoard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(mainBoard);
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
	 * Change units.
	 */
	public void changeUnits() {

		if ((mSelectedExerciseGroupPosition == -1)
				|| (mSelectedExerciseChildPosition == -1)) {

			Toast.makeText(
					getActivity(),
					getResources().getString(
							R.string.error_exercise_not_selected),
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (unitsKG) {
			mTxtMin.setText(kgMin);
			mTxtMax.setText(kgMax);
			mMgvUnitTypeMin.setImageDrawable(getResources().getDrawable(
					R.drawable.img_pesominimo));
			mMgvUnitTypeMax.setImageDrawable(getResources().getDrawable(
					R.drawable.img_pesomaximo));
		} else {
			mTxtMin.setText(lbMin);
			mTxtMax.setText(lbMax);
			mMgvUnitTypeMin.setImageDrawable(getResources().getDrawable(
					R.drawable.img_pesomin_lbs));
			mMgvUnitTypeMax.setImageDrawable(getResources().getDrawable(
					R.drawable.img_pesomax_lbs));
		}

	}

	/**
	 * Show fragments dialog.
	 * 
	 * @param savedInstanceState
	 *            the saved instance state
	 */
	public void showFragmentsDialog(boolean savedInstanceState) {
		dialgoFragment = getFragmentManager().findFragmentByTag(
				ProgressDialogFragment.progressDialogTag);

		if (savedInstanceState == true) {
			dialgoFragment = getFragmentManager().findFragmentByTag(
					ProgressDialogFragment.progressDialogTag);
			progress = (ProgressDialogFragment) dialgoFragment;
			imageFragment = getFragmentManager().findFragmentByTag(
					ImageFragment.TAG);
			fullImageFragment = (FragmentFullImageDialog) imageFragment;
			if (imageFragment != null) {
				getFragmentManager().beginTransaction()
						.remove(fullImageFragment).commit();
			}
		} else {

			if (dialgoFragment != null) {
				getFragmentManager().beginTransaction().remove(dialgoFragment)
						.commit();
			}
			if (imageFragment != null) {
				getFragmentManager().beginTransaction()
						.remove(fullImageFragment).commit();
			}
			progress = ProgressDialogFragment.newInstance(getActivity());
			progress.setCancelable(false);
			progress.show(getFragmentManager(),
					ProgressDialogFragment.progressDialogTag);
		}

	}

	/**
	 * Send email.
	 */
	public void sendEmail() {

		EmailTask task = new EmailTask(new ResponseInterface() {

			@Override
			public void onResultResponse(Object obj) {
				EmailPojo result = (EmailPojo) obj;
				if (result.isStatus())

					Toast.makeText(
							getActivity(),
							getResources()
									.getString(R.string.routine_sendemail),
							Toast.LENGTH_SHORT).show();
				else

					Toast.makeText(
							getActivity(),
							getResources().getString(
									R.string.error_routine_sendemail),
							Toast.LENGTH_SHORT).show();
			}
		});
		EmailTask.ctx = getActivity();
		EmailPojo pojo = new EmailPojo();
		pojo.setId_user(mAccountMngr.getCurrentUserId());
		pojo.setMemunic_id(memunic_id);
		task.execute(pojo);

	}

	/**
	 * Checks if is network available.
	 * 
	 * @return true, if is network available
	 */
	public boolean isNetworkAvailable() {
		boolean network = ConnectionUtils.isNetworkAvailable(getActivity());
		if (!network)
			Toast.makeText(getActivity(),
					getResources().getString(R.string.error_connection),
					Toast.LENGTH_SHORT).show();
		return network;
	}

	/**
	 * Clean references.
	 */
	public void cleanReferences() {
		expandableListView = null;
		parentViewPressed = null;
		fullImageFragment = null;
		progress = null;
		exerciseGroups = null;
		mImageGalleryAdapter = null;
		mMgvUnitType = null;
		mLnrIndicationStrip = null;

	}

	/**
	 * Post facebook.
	 */
	public void postFacebook() {
		Session session = Session.getActiveSession();

		if (session == null)
			session = Session.openActiveSessionFromCache(getActivity());

		if (session != null && session.isOpened()) {

			PERMISSIONS = Arrays.asList("publish_actions",
					PERMISSION_PUBLISH_STREAM);

			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						getActivity(), PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}
			waitface = true;
			alertFace.show();
		}
	}

	/**
	 * Checks if is subset of.
	 * 
	 * @param subset
	 *            the subset
	 * @param superset
	 *            the superset
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

	public void openTourInfoFragment() {
		tourFragment = FragmentDialogRoutineInfo.newInstance(this);
		tourFragment.setStyle(DialogFragment.STYLE_NO_TITLE,
				android.R.style.Theme_Black_NoTitleBar);
		tourFragment.show(getFragmentManager(), "tourInfoDialog");
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		tourFragment.dismiss();
	}

}
