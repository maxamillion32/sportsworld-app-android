package mx.com.sportsworld.sw.fragment;

		import java.util.Arrays;
		import java.util.Calendar;
		import java.util.Locale;

		import android.annotation.SuppressLint;
		import android.app.Activity;
		import android.app.PendingIntent;
		import android.content.Intent;
		import android.database.Cursor;
		import android.graphics.Bitmap;
		import android.net.Uri;
		import android.net.http.SslError;
		import android.os.Bundle;
		import android.support.v4.app.Fragment;
		import android.support.v4.app.LoaderManager;
		import android.support.v4.content.CursorLoader;
		import android.support.v4.content.Loader;
		import android.support.v4.view.ViewPager;
		import android.text.TextUtils;
		import android.util.Log;
		import android.util.SparseArray;
		import android.view.LayoutInflater;
		import android.view.View;
		import android.view.ViewGroup;
		import android.webkit.HttpAuthHandler;
		import android.webkit.SslErrorHandler;
		import android.webkit.WebView;
		import android.webkit.WebViewClient;
		import android.widget.Button;
		import android.widget.TextView;
		import android.widget.Toast;
		import android.widget.ToggleButton;

		import com.actionbarsherlock.app.SherlockFragment;
		import com.fourmob.datetimepicker.date.DatePickerDialog;
		import mx.com.sportsworld.sw.R;
		import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
		import mx.com.sportsworld.sw.activity.FacilitiesActivity;
		import mx.com.sportsworld.sw.activity.GymClassesActivity;
		import mx.com.sportsworld.sw.activity.WebActivity;
		import mx.com.sportsworld.sw.adapter.ImageGalleryAdapter;
		import mx.com.sportsworld.sw.fragment.dialog.ProgressDialogFragment;
		import mx.com.sportsworld.sw.imgloader.ImageCache;
		import mx.com.sportsworld.sw.model.Branch;
		import mx.com.sportsworld.sw.provider.SportsWorldContract;
		import mx.com.sportsworld.sw.service.GetBranchOverviewService;
		import mx.com.sportsworld.sw.service.MarkBranchAsFavoriteService;
		import mx.com.sportsworld.sw.utils.ConnectionUtils;
		import mx.com.sportsworld.sw.utils.Dialer;

// TODO: Auto-generated Javadoc

/**
 * Shows details of a branch.
 *
 * @author Jose Torres Fuentes Ironbit
 *
 */
public class BranchOverviewFragment extends SherlockFragment implements
		LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener,DatePickerDialog.OnDateSetListener{

	/** The Constant TAG. */
	private static final String TAG = BranchOverviewFragment.class.getName();

	/** The Constant THUMB_FOLDER. */
	private static final String THUMB_FOLDER = "branch_images_thumbs";

	/** The Constant STATE_RETRY_SHOWN. */
	private static final String STATE_RETRY_SHOWN = "state_retry_shown";

	/** The Constant STATE_CURRENT_IMAGE_INDEX. */
	private static final String STATE_CURRENT_IMAGE_INDEX = "state_current_image_index";

	/** The Constant STATE_GOT_BRANCH_DATA. */
	private static final String STATE_GOT_BRANCH_DATA = "state_got_branch_data";

	/** The Constant REQUEST_CODE_UPDATE_BRANCH. */
	private static final int REQUEST_CODE_UPDATE_BRANCH = 0;

	/** The Constant REQUEST_CODE_MARK_BRANCH_AS_FAVORITE. */
	private static final int REQUEST_CODE_MARK_BRANCH_AS_FAVORITE = 1;

	/** The Constant LOADER_ID_BRANCH_OVERVIEW. */
	private static final int LOADER_ID_BRANCH_OVERVIEW = 0;

	/** The Constant LOADER_ARG_CLUB_ID. */
	private static final String LOADER_ARG_CLUB_ID = "loader_arg_club_id";

	/** The Constant COL_INDEX_BRANCH_NAME. */
	private static final int COL_INDEX_BRANCH_NAME = 0;

	/** The Constant COL_INDEX_BRANCH_ADDRESS. */
	private static final int COL_INDEX_BRANCH_ADDRESS = 1;

	/** The Constant COL_INDEX_BRANCH_PHONE. */
	private static final int COL_INDEX_BRANCH_PHONE = 2;

	/** The Constant COL_INDEX_BRANCH_DISTANCE. */
	private static final int COL_INDEX_BRANCH_DISTANCE = 3;

	/** The Constant COL_INDEX_BRANCH_FAVORITE. */
	private static final int COL_INDEX_BRANCH_FAVORITE = 4;

	/** The Constant COL_INDEX_BRANCH_FACILITIES. */
	private static final int COL_INDEX_BRANCH_FACILITIES = 5;

	/** The Constant COL_INDEX_BRANCH_IMAGES_URLS. */
	private static final int COL_INDEX_BRANCH_IMAGES_URLS = 6;

	/** The Constant COL_INDEX_BRANCH_VIDEO_URL. */
	private static final int COL_INDEX_BRANCH_VIDEO_URL = 7;

	/** The Constant COL_INDEX_BRANCH_PREVENTA. */
	private static final int COL_INDEX_BRANCH_PREVENTA = 8;

	/** The Constant COL_INDEX_BRANCH_URL_360. */
	private static final int COL_INDEX_BRANCH_URL_360 = 9;

	/** The m current image index. */
	private int mCurrentImageIndex;

	/** The m tgg favorite. */
	private ToggleButton mTggFavorite;

	/** The m btn show facilities. */
	private Button mBtnShowFacilities;

	/** The m btn show classes. */
	private Button mBtnShowClasses;

	/** The m btn call. */
	private Button mBtnCall;

	/** The m btn virtual tour. */
	private Button mBtnVirtualTour;

	/** The m txt branch name. */
	private TextView mTxtBranchName;

	/** The m txt distance. */
	private TextView mTxtDistance;

	/** The m txt address. */
	private TextView mTxtAddress;

	/** The m pager. */
	private ViewPager mPager;

	/** The m phone. */
	private String mPhone;

	/** The m images url. */
	private String mImagesUrl;

	/** The m img cache. */
	private ImageCache mImgCache;

	/** The m adapter. */
	private ImageGalleryAdapter mAdapter;

	/** The m retry shown. */
	private boolean mRetryShown;

	/** The m got branch data. */
	private boolean mGotBranchData;

	/** The m url youtube. */
	private static String mUrlYoutube;

	/** The m url 360. */
	private static String mUrlRecorrido360;

	/** The progress. */
	private ProgressDialogFragment progress;

	/** The dialgo fragment. */
	Fragment dialgoFragment;

	/** The view. */
	private View view;
	private View view2;

	/** The has video url. */
	public boolean hasVideoUrl;

	/** The has  url 360. */
	public boolean hasUrl360;

	/** The preventa. */
	public boolean preventa;

	/** The has facilities. */
	public boolean hasFacilities;

	/** The fist time. */
	public boolean fistTime = true;

	/** The Constant COLS. */
	private static final String[] COLS = buildColumns();

	/** The m listener. */
	private OnShowLocationListener mListener;

	public WebView myWebView;

	public static final String DATEPICKER_TAG = "datepicker";
	/**
	 * Builds the columns.
	 *
	 * @return the string[]
	 */
	private static String[] buildColumns() {

		final SparseArray<String> colsMap = new SparseArray<String>();
		colsMap.put(COL_INDEX_BRANCH_NAME, SportsWorldContract.Branch.NAME);
		colsMap.put(COL_INDEX_BRANCH_ADDRESS,
				SportsWorldContract.Branch.ADDRESS);
		colsMap.put(COL_INDEX_BRANCH_PHONE, SportsWorldContract.Branch.PHONE);
		colsMap.put(COL_INDEX_BRANCH_DISTANCE,
				SportsWorldContract.Branch.DISTANCE);
		colsMap.put(COL_INDEX_BRANCH_FAVORITE,
				SportsWorldContract.Branch.FAVORITE);
		colsMap.put(COL_INDEX_BRANCH_FACILITIES,
				SportsWorldContract.Branch.FACILITIES);
		colsMap.put(COL_INDEX_BRANCH_IMAGES_URLS,
				SportsWorldContract.Branch.IMAGES_URLS);
		colsMap.put(COL_INDEX_BRANCH_VIDEO_URL,
				SportsWorldContract.Branch.VIDEO_URL);
		colsMap.put(COL_INDEX_BRANCH_PREVENTA,
				SportsWorldContract.Branch.PREVENTA);
		colsMap.put(COL_INDEX_BRANCH_URL_360,
				SportsWorldContract.Branch.URL_360);

		final int colCount = colsMap.size();
		final String[] cols = new String[colCount];
		for (int i = 0; i < colCount; i++) {
			cols[i] = colsMap.get(i);
		}

		return cols;

	}

	/**
	 * Instantiates a new branch overview fragment.
	 */
	public BranchOverviewFragment() {
		/* Do nothing */
	}

	/**
	 * New instance.
	 *
	 * @param id the id
	 * @return the branch overview fragment
	 */
	public static BranchOverviewFragment newInstance(long id) {
		final BranchOverviewFragment f = new BranchOverviewFragment();
		final Bundle args = new Bundle();
		args.putLong(LOADER_ARG_CLUB_ID, id);
		f.setArguments(args);
		branchIdF = id;
		return f;
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnShowLocationListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.getClass().getName()
					+ " must implement "
					+ OnShowLocationListener.class.getName() + ".");
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_branch_overview,
				null /* root */, false /* attachToRoot */);

		mTggFavorite = (ToggleButton) view.findViewById(R.id.tgg_favorite);
		mBtnShowFacilities = (Button) view
				.findViewById(R.id.btn_show_facilities);
		mBtnShowClasses = (Button) view.findViewById(R.id.btn_show_classes);
		mBtnCall = (Button) view.findViewById(R.id.btn_call);
		mBtnVirtualTour = (Button) view.findViewById(R.id.btn_virtual_tour);
		final Button btnShowLocation = (Button) view
				.findViewById(R.id.btn_show_location);
		mTxtBranchName = (TextView) view.findViewById(R.id.txt_branch_name);
		mTxtDistance = (TextView) view.findViewById(R.id.txt_proximity);
		mTxtDistance = (TextView) view.findViewById(R.id.txt_proximity);
		mTxtAddress = (TextView) view.findViewById(R.id.txt_address);
		mPager = (ViewPager) view.findViewById(R.id.pager);

		mBtnShowFacilities.setOnClickListener(this);
		mBtnShowClasses.setOnClickListener(this);
		mBtnCall.setOnClickListener(this);
		mBtnVirtualTour.setOnClickListener(this);
		btnShowLocation.setOnClickListener(this);
		mTggFavorite.setOnClickListener(this);

		view2 = inflater.inflate(R.layout.fragment_branch_webview,
				null /* root */, false /* attachToRoot */);

		myWebView = (WebView) view2.findViewById(R.id.web_engine);
		//myWebView.getSettings().setJavaScriptEnabled(true);

		return view;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		mImgCache = ImageCache.findOrCreateCache(getActivity(), THUMB_FOLDER);
		final int size = getActivity().getResources().getDimensionPixelSize(
				R.dimen.branch_image_height);
		mAdapter = new ImageGalleryAdapter(getChildFragmentManager(),
				getActivity(), mImgCache, size, size, false);
		mPager.setAdapter(mAdapter);

		if (savedInstanceState == null) {
			updateBranch();
			showFragmentDialog(false);
		} else {
			showFragmentDialog(true);
			mRetryShown = savedInstanceState.getBoolean(STATE_RETRY_SHOWN);
			mGotBranchData = savedInstanceState
					.getBoolean(STATE_GOT_BRANCH_DATA);

			/*
			 * We are losing the current page index. I'm not sure if this is a
			 * bug on ViewPager, on our code or this is the way it should work.
			 * Anyway, we'll save it on onSaveInstanceState, recover it here and
			 * set it on onLoadFinished.
			 */
			mCurrentImageIndex = savedInstanceState
					.getInt(STATE_CURRENT_IMAGE_INDEX);
		}

		/*
		 * setHasOptionsMenu MUST be called AFTER we get mCurrentImageIndex or
		 * else our refresh icon won't be displayed if it should be shown.
		 */
		setHasOptionsMenu(true);

		mTggFavorite.setVisibility(mGotBranchData ? View.VISIBLE
				: View.INVISIBLE);

		getLoaderManager().initLoader(LOADER_ID_BRANCH_OVERVIEW,
				null /* loaderArgs */, this /* loaderCallbacks */);
	}

	/**
	 * Retry update branch.
	 */
	@SuppressLint("NewApi")
	// invalidateOptionsMenu() does work on API 9 or up.
	private void retryUpdateBranch() {
		mGotBranchData = false;
		mRetryShown = false;
		mTggFavorite.setVisibility(View.INVISIBLE);
		getSherlockActivity().invalidateOptionsMenu();
		updateBranch();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		disableButtons();
		super.onResume();
	}

	/**
	 * Update branch.
	 */
	private void updateBranch() {
		final Intent data = new Intent();
		final Activity activity = getActivity();
		final PendingIntent pendingIntent = activity.createPendingResult(
				REQUEST_CODE_UPDATE_BRANCH, data, 0 /* flags */);
		final Intent showBranchService = new Intent(activity,
				GetBranchOverviewService.class);
		showBranchService.putExtra(GetBranchOverviewService.EXTRA_BRANCH_ID,
				getBranchIdFromArgs());
		showBranchService.putExtra(
				GetBranchOverviewService.EXTRA_PENDING_INTENT, pendingIntent);
		activity.startService(showBranchService);
	}

	/**
	 * Gets the branch id from args.
	 *
	 * @return the branch id from args
	 */
	private long getBranchIdFromArgs() {
		return getArguments().getLong(LOADER_ARG_CLUB_ID);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		super.onStop();
		mImgCache.close();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		Log.i(TAG, "onStart OverviewFragment");

		disableButtons();
		super.onStart();

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_CURRENT_IMAGE_INDEX, mPager.getCurrentItem());
		outState.putBoolean(STATE_RETRY_SHOWN, mRetryShown);
		outState.putBoolean(STATE_GOT_BRANCH_DATA, mGotBranchData);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@SuppressLint("NewApi")
	// invalidateOptionsMenu() does work on API 9 or up.
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (progress != null)
			progress.dismissAllowingStateLoss();

		switch (requestCode) {

			case REQUEST_CODE_MARK_BRANCH_AS_FAVORITE:

				if (progress != null)
					progress.dismissAllowingStateLoss();

				if (resultCode != Activity.RESULT_OK) {

					Toast.makeText(
							getActivity(),
							getResources().getString(
									R.string.error_connection_server),
							Toast.LENGTH_SHORT).show();

					Log.d(TAG,
							"Error al intentar actualizar. C�digo de error: "
									+ data.getIntExtra(
									MarkBranchAsFavoriteService.EXTRA_RESULT_ERROR_CODE,
									-1)
									+ " - Respuesta de servidor: "
									+ data.getIntExtra(
									MarkBranchAsFavoriteService.EXTRA_RESULT_SERVER_RESPONSE_CODE,
									-1));

					mTggFavorite.setChecked(!mTggFavorite.isChecked());

				} else {
					if (mTggFavorite.isChecked())
						Toast.makeText(
								getActivity(),
								getResources().getString(
										R.string.success_favorite_changes),
								Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(
								getActivity(),
								getResources().getString(
										R.string.error_favorite_changes),
								Toast.LENGTH_SHORT).show();
					mTggFavorite.toggle();
				}

				break;

			case REQUEST_CODE_UPDATE_BRANCH:

				if (requestCode != REQUEST_CODE_UPDATE_BRANCH) {
					super.onActivityResult(requestCode, resultCode, data);
					return;
				}

				mRetryShown = (resultCode == Activity.RESULT_CANCELED);
				if (mRetryShown) {

					Toast.makeText(
							getActivity(),
							getResources().getString(
									R.string.error_connection_server),
							Toast.LENGTH_SHORT).show();
					getActivity().finish();
				}
				mGotBranchData = !mRetryShown;
				mTggFavorite.setVisibility(mGotBranchData ? View.VISIBLE
						: View.INVISIBLE);

				getSherlockActivity().invalidateOptionsMenu();

				break;

			default:
				super.onActivityResult(requestCode, resultCode, data);
				break;

		}

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		final Uri branchUri = SportsWorldContract.Branch.buildBranchUri(String
				.valueOf(getBranchIdFromArgs()));
		return new CursorLoader(getActivity() /* this */, branchUri, COLS,
				null/* selection */, null/* selectionArgs */, null/* sortOrder */);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		final boolean found = cursor.moveToFirst();
		if (found) {

			/*
			 * There is no cursor.getBoolean() method. Booleans are stored as
			 * integers by SQLite
			 */
			final boolean favorite = (cursor.getInt(COL_INDEX_BRANCH_FAVORITE) == 1);
			preventa = (cursor.getInt(COL_INDEX_BRANCH_PREVENTA) == 1);

			hasFacilities = !TextUtils.isEmpty(cursor
					.getString(COL_INDEX_BRANCH_FACILITIES));

			hasVideoUrl = !TextUtils.isEmpty(cursor
					.getString(COL_INDEX_BRANCH_VIDEO_URL));

			hasUrl360 = !TextUtils.isEmpty(cursor
					.getString(COL_INDEX_BRANCH_URL_360));

			if (hasUrl360) {
				mUrlRecorrido360 = cursor.getString(COL_INDEX_BRANCH_URL_360);
				Log.d("36000000 ", "link : " + cursor.getString(COL_INDEX_BRANCH_URL_360));
				mBtnVirtualTour.setPressed(false);
				mBtnVirtualTour.setClickable(true);
			} else {
				if(hasVideoUrl){
					mUrlYoutube = cursor.getString(COL_INDEX_BRANCH_VIDEO_URL);
					Log.d("youtube ", "link : " + mUrlYoutube);
					mBtnVirtualTour.setPressed(false);
					mBtnVirtualTour.setClickable(true);
				}else{
					mBtnVirtualTour.setPressed(true);
				}
			}

			mImagesUrl = cursor.getString(COL_INDEX_BRANCH_IMAGES_URLS);

			final String branchName = cursor.getString(COL_INDEX_BRANCH_NAME);
			final String address = cursor.getString(COL_INDEX_BRANCH_ADDRESS);
			final double distance = cursor.getDouble(COL_INDEX_BRANCH_DISTANCE);
			mPhone = cursor.getString(COL_INDEX_BRANCH_PHONE);

			if (mPhone.equals("")) {
				mBtnCall.setText("Tel�fono");
				mBtnCall.setPressed(false);
				mBtnCall.setClickable(false);
			} else {
				mBtnCall.setText(mPhone);
				mBtnCall.setPressed(false);
				mBtnCall.setClickable(true);
			}

			if (hasFacilities) {
				mBtnShowFacilities.setPressed(false);
				mBtnShowFacilities.setClickable(true);
			} else {
				mBtnShowFacilities.setPressed(true);
				mBtnShowFacilities.setClickable(false);
			}
			mBtnShowFacilities.setEnabled(hasFacilities);

			mTxtBranchName.setText(branchName);
			getSherlockActivity().getSupportActionBar().setTitle(branchName);
			mTxtAddress.setText(address);
			mTxtDistance.setText(String
					.format(Locale.US, "%1$.0f Km", distance));
			mTggFavorite.setChecked(favorite);

			if (!TextUtils.isEmpty(mImagesUrl) && fistTime) {
				final String[] urlArray = mImagesUrl.split(Branch.DELIMITER);
				mAdapter.setUrls(Arrays.asList(urlArray));
				mPager.setCurrentItem(mCurrentImageIndex);
			}
			if(hasUrl360){
				mBtnVirtualTour.setEnabled(hasUrl360);
			}else{
				mBtnVirtualTour.setEnabled(hasVideoUrl);
			}


			mBtnShowClasses.setEnabled(!preventa);

			if (preventa) {
				mBtnShowClasses.setClickable(false);
				mBtnShowClasses.setPressed(true);
			} else {
				mBtnShowClasses.setClickable(true);
				mBtnShowClasses.setPressed(false);

			}

		} else {
			mBtnShowFacilities.setEnabled(false);
			mBtnShowFacilities.setVisibility(View.INVISIBLE);
		}

		mTggFavorite.setVisibility(mGotBranchData ? View.VISIBLE
				: View.INVISIBLE);

		mBtnCall.setEnabled(found);

		mTxtBranchName.setEnabled(found);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		/* Do nothing. This won't happen. */
	}

	/**
	 * The listener interface for receiving onShowLocation events.
	 * The class that is interested in processing a onShowLocation
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnShowLocationListener<code> method. When
	 * the onShowLocation event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnShowLocationEvent
	 */
	public static interface OnShowLocationListener {

		/**
		 * On show location click.
		 *
		 * @param id the id
		 */
		void onShowLocationClick(long id);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {

		switch (view.getId()) {

			case R.id.tgg_favorite:
				if (!isMember() || !isNetworkAvailable()) {

					mTggFavorite.setChecked(!mTggFavorite.isChecked());
					if (!isMember())
						Toast.makeText(
								getActivity(),
								getResources().getString(
										R.string.error_not_a_member),
								Toast.LENGTH_SHORT).show();
				} else
					startMarkBranchAsFavoriteService();
				break;

			case R.id.btn_show_facilities:
				if (isNetworkAvailable()) {
					disableButtons();
					goToShowFacilities();
				}
				break;

			case R.id.btn_show_classes:
				if (isNetworkAvailable()) {
					disableButtons();
					goToShowClasses();
				}
				break;

			case R.id.btn_call:
				disableButtons();
				callBranch();
				break;

			case R.id.btn_virtual_tour:
				if (isNetworkAvailable()) {
					if(!hasUrl360){
						openVideo();
						disableButtons();
						Log.d("mensaje", "video");
					}else{
						Log.d("mensaje", "360");
						open360();
						disableButtons();
					}
				}
				break;

			case R.id.btn_show_location:
				if (isNetworkAvailable()) {
					disableButtons();
					mListener.onShowLocationClick(getBranchIdFromArgs());
				}
				break;

			default:
				throw new UnsupportedOperationException("Unknown view "
						+ view.getId());

		}

	}
	public static long branchIdF;

	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		long milliTime = calendar.getTimeInMillis();

		final Intent goToShowClasses = new Intent(getActivity()
				.getApplicationContext() ,
				GymClassesActivity.class);
		goToShowClasses.putExtra(
				GymClassesActivity.EXTRA_BRANCH_ID, branchIdF);
		goToShowClasses.putExtra(
				GymClassesActivity.EXTRA_DATE_ID,
				milliTime);

		if (isNetworkAvailable())
			startActivity(goToShowClasses);

	}
	/**
	 * Go to show classes.
	 */
	private void goToShowClasses() {

	/*	final Intent goToShowClasses = new Intent(getActivity(),
				GymCalendarActivity.class);
		goToShowClasses.putExtra(GymCalendarActivity.EXTRA_BRANCH_ID,
				getBranchIdFromArgs());
		startActivity(goToShowClasses);
	*/

		final Calendar calendar = Calendar.getInstance();
		final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
		//final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(v)
		datePickerDialog.setVibrate(false);
		datePickerDialog.setYearRange(1985, 2028);
		datePickerDialog.setCloseOnSingleTapDay(false);
		datePickerDialog.show(getSherlockActivity().getSupportFragmentManager(), DATEPICKER_TAG);

	}

	/**
	 * Start mark branch as favorite service.
	 */
	private void startMarkBranchAsFavoriteService() {
		fistTime = false;
		if (!isMember()) {

			Toast.makeText(getActivity(),
					getResources().getString(R.string.error_not_mark_favorite),
					Toast.LENGTH_SHORT).show();
			return;
		}

		showFragmentDialog(false);
		final Intent data = new Intent();
		final Activity activity = getActivity();
		final PendingIntent pendingIntent = activity.createPendingResult(
				REQUEST_CODE_MARK_BRANCH_AS_FAVORITE, data, 0 /* flags */);
		final Intent markBranchAsFavoriteService = new Intent(activity,
				MarkBranchAsFavoriteService.class);
		markBranchAsFavoriteService.putExtra(
				MarkBranchAsFavoriteService.EXTRA_BRANCH_ID,
				getBranchIdFromArgs());
		markBranchAsFavoriteService.putExtra(
				MarkBranchAsFavoriteService.EXTRA_FAVORITE,
				mTggFavorite.isChecked());
		markBranchAsFavoriteService
				.putExtra(MarkBranchAsFavoriteService.EXTRA_PENDING_INTENT,
						pendingIntent);
		activity.startService(markBranchAsFavoriteService);
	}

	/**
	 * Go to show facilities.
	 */
	private void goToShowFacilities() {
		final Intent showFacilities = new Intent(getActivity() /* context */,
				FacilitiesActivity.class);
		showFacilities.putExtra(FacilitiesActivity.EXTRA_BRANCH_ID,
				getBranchIdFromArgs());
		startActivity(showFacilities);
	}

	/**
	 * Call branch.
	 */
	private void callBranch() {
		Dialer.dial(getActivity(), mPhone);
	}

	/**
	 * Open video.
	 */
	public void openVideo() {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mUrlYoutube)));
		disableButtons();
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
			progress.show(getFragmentManager(),
					ProgressDialogFragment.progressDialogTag);
		}

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		view = null;
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
	 * Disable buttons.
	 */
	public void disableButtons() {
		if (preventa) {

			mBtnShowClasses.setPressed(true);
			mBtnShowClasses.setClickable(false);
		} else {
			mBtnVirtualTour.setPressed(false);
			mBtnShowClasses.setPressed(false);
			mBtnShowClasses.setClickable(true);
		}

		if (hasVideoUrl || hasUrl360) {
			mBtnVirtualTour.setPressed(false);
			mBtnVirtualTour.setClickable(true);
		} else {
			mBtnVirtualTour.setPressed(true);
			mBtnVirtualTour.setClickable(false);
		}
		if (hasFacilities) {
			mBtnShowFacilities.setPressed(false);
			mBtnShowFacilities.setClickable(true);
		} else {

			mBtnShowFacilities.setPressed(true);
			mBtnShowFacilities.setClickable(false);
		}
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
	 * Open video.
	 */
	public void open360() {
		Log.d("URL ", "360 " + mUrlRecorrido360);

		final Intent inten0 = new Intent(getActivity() /* context */,
				WebActivity.class);
		inten0.putExtra(mx.com.sportsworld.sw.activity.WebActivity.URL,
				mUrlRecorrido360);
		startActivity(inten0);
		disableButtons();

	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (Uri.parse(url).getHost().equals("http://www.sportsworld.com.mx/vr360/SWAmores/")) {
				// This is your web site, so do not override; let the WebView to load the page
				return false;
			}
			// Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent);
			return true;
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			super.onReceivedSslError(view, handler, error);
			Log.i("Log Santa", "Received SSL error: " + error.toString());
			// this will ignore the Ssl error and will go forward to your site
			handler.proceed();
		}

		@Override
		public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host,
											  String realm) {
			// cancel http auth request
			handler.cancel();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {

			Log.i("Log Santa", "onPageStarted: " + url);
		}
	}

}
