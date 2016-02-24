package mx.com.sportsworld.sw.fragment;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
import mx.com.sportsworld.sw.activity.MainboardActivity;
import mx.com.sportsworld.sw.activity.SpecificBranchTypeListActivity;
import mx.com.sportsworld.sw.app.ErrorDialogFragment;
import mx.com.sportsworld.sw.app.SherlockWorkingListFragment;
import mx.com.sportsworld.sw.fragment.dialog.ProgressDialogFragment;
import mx.com.sportsworld.sw.model.Branch;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.service.FetchBranchService;
import mx.com.sportsworld.sw.utils.ConnectionUtils;


/**
 * The Class BranchTypesListFragment.
 */
public class BranchTypesListFragment extends SherlockWorkingListFragment
		implements DialogInterface.OnCancelListener, ConnectionCallbacks,
		OnConnectionFailedListener, LocationListener, View.OnClickListener {

	/** The Constant TAG. */
	private static final String TAG = BranchTypesListFragment.class.getName();

	/*
	 * Times and priority as suggested by the docs:
	 * http://developer.android.com/
	 * reference/com/google/android/gms/location/LocationRequest.html
	 */
	/** The Constant REQUEST_LOC_PRIORITY. */
	private static final int REQUEST_LOC_PRIORITY = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;

	/** The Constant REQUEST_LOC_FASTEST_INTERVAL. */
	private static final long REQUEST_LOC_FASTEST_INTERVAL = 5L * 1000L; // 5
																			// seconds
	/** The Constant REQUEST_LOC_INTERVAL. */
	private static final long REQUEST_LOC_INTERVAL = 30L * 1000L; // 30 seconds

	/** The Constant REQUEST_LOC_NUM_UPDATES. */
	private static final int REQUEST_LOC_NUM_UPDATES = 1;

	/** The Constant STATE_BRANCHES_FETCHED. */
	private static final String STATE_BRANCHES_FETCHED = "state_branches_fethced";

	/** The Constant STATE_FETCHING_BRANCHES. */
	private static final String STATE_FETCHING_BRANCHES = "state_branches_fetched";

	/** The Constant FRAG_TAG_ERROR_DIALOG. */
	private static final String FRAG_TAG_ERROR_DIALOG = "frag_tag_error_dialog";

	/** The Constant REQUEST_CODE_INSTALL_GOOGLE_PLAY_SERVICES. */
	private static final int REQUEST_CODE_INSTALL_GOOGLE_PLAY_SERVICES = 0;

	/** The Constant REQUEST_CODE_FETCH_BRANCHES. */
	private static final int REQUEST_CODE_FETCH_BRANCHES = 1;

	/** The Constant STATE_LOCATION. */
	private static final String STATE_LOCATION = "state_location";

	/** The m btn retry. */
	private Button mBtnRetry;

	/** The m location. */
	private Location mLocation;

	/** The m location client. */
	private LocationClient mLocationClient;

	/** The m adapter. */
	private ArrayAdapter<String> mAdapter;

	/** The m branches fetched. */
	private boolean mBranchesFetched;

	/** The m fetching branches. */
	private boolean mFetchingBranches;

	/** The progress. */
	private ProgressDialogFragment progress;

	/** The dialgo fragment. */
	Fragment dialgoFragment;

	ImageView iArea, iClubes, iForaneos, iAperturas;

	Context cont;

	boolean bandera = false;

	/**
	 * Instantiates a new branch types list fragment.
	 */
	public BranchTypesListFragment() {
		/* Do nothing */
	}



	/**
	 * New instance.
	 * 
	 * @return the branch types list fragment
	 */
	public static BranchTypesListFragment newInstance() {
		return new BranchTypesListFragment();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sportsworld.android.app.SherlockWorkingListFragment#onCreateView(
	 * android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.fragment_branch_types_list,
				null /* root */, false /* attachToRoot */);
		mBtnRetry = (Button) view.findViewById(R.id.btn_retry);
		mBtnRetry.setOnClickListener(this);
        iClubes = (ImageView) view.findViewById(R.id.clubes_fav);
        iForaneos = (ImageView) view.findViewById(R.id.foraneos);
        iArea = (ImageView) view.findViewById(R.id.area_metro);
        iAperturas = (ImageView) view.findViewById(R.id.prox_pertura);

        //mBtnRetry.setOnClickListener(this);
        iClubes.setOnClickListener(this);
        iForaneos.setOnClickListener(this);
        iArea.setOnClickListener(this);
        iAperturas.setOnClickListener(this);

		loadImages();

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

		mFetchingBranches = true;
		getSherlockActivity().getSupportActionBar().setTitle("Clubes");

		if (savedInstanceState != null ) {
			mLocation = savedInstanceState.getParcelable(STATE_LOCATION);
			mBranchesFetched = savedInstanceState
					.getBoolean(STATE_BRANCHES_FETCHED);
			mFetchingBranches = savedInstanceState
					.getBoolean(STATE_FETCHING_BRANCHES);

			showFragmentDialog(true);
		} else {
			showFragmentDialog(false);
		}
		mAdapter = new ArrayAdapter<String>(getActivity() /* activity */,
				android.R.layout.simple_list_item_1 );
		setListAdapter(mAdapter);
		setListShown(false);

		if (!mFetchingBranches) {
			setListContent();
		}
		//progress.dismiss();

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
		outState.putParcelable(STATE_LOCATION, mLocation);
		outState.putBoolean(STATE_BRANCHES_FETCHED, mBranchesFetched);
		outState.putBoolean(STATE_FETCHING_BRANCHES, mFetchingBranches);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		Log.d(TAG, "onStop");
		if (mLocationClient != null) {
			Log.d(TAG, "mLocationClient.disconnect()");
			mLocationClient.disconnect();
		}
		super.onStop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		Log.i(TAG, "onStart BranchTypes");
		super.onStart();
		tryToGetLocation();
	}

	/**
	 * Try to get location.
	 */
	private void tryToGetLocation() {

		if (!isNetworkLocationProviderAvailable()) {
			setEmptyText(getString(R.string.error_connection));
			// showRetryButton(true);
			if (isResumed()) {
				setListShownNoAnimation(true);
			} else {
				setListShown(true);
			}
			return;
		}

		if (mLocation == null) {
			mLocationClient = new LocationClient(getActivity() /* context */,
					this /* connectionCallbacks */, this /* connectionFailedListener */);
		}

		if (mLocationClient == null) {
			mLocationClient = new LocationClient(getActivity() /* context */,
					this /* connectionCallbacks */, this /* connectionFailedListener */);
		}
		if (mLocationClient == null) {
			return;
		}
		mLocationClient.connect();

	}

	/**
	 * Checks if is network location provider available.
	 * 
	 * @return true, if is network location provider available
	 */
	public boolean isNetworkLocationProviderAvailable() {
		final LocationManager locationMngr = (LocationManager) getActivity()
				.getSystemService(Context.LOCATION_SERVICE);
		return locationMngr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
				&& ConnectionUtils
						.isNetworkAvailable(getActivity() /* context */);

	}

	/**
	 * Check google play services availability.
	 * 
	 * @return true, if successful
	 */
	private boolean checkGooglePlayServicesAvailability() {
		final int connectionResult = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getActivity()
						.getApplicationContext());
		return !showGooglePlayServicesErrorDialog(connectionResult);
	}

	/**
	 * Show google play services error dialog.
	 * 
	 * @param connectionResult
	 *            the connection result
	 * @return true, if successful
	 */
	private boolean showGooglePlayServicesErrorDialog(int connectionResult) {
		final Dialog googlePlayDialog = GooglePlayServicesUtil
				.getErrorDialog(connectionResult, getActivity() /* activity */,
						REQUEST_CODE_INSTALL_GOOGLE_PLAY_SERVICES, this /* onCancelListener */);
		if (googlePlayDialog == null) {
			return false;
		}
		ErrorDialogFragment.newInstance(googlePlayDialog).show(
				getFragmentManager(), FRAG_TAG_ERROR_DIALOG);
		bandera = true;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.content.DialogInterface.OnCancelListener#onCancel(android.content
	 * .DialogInterface)
	 */
	@Override
	public void onCancel(DialogInterface dialog) {
		getActivity().finish();
	}



	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	/*@Override
	public void onResume() {
		// TODO Auto-generated method stub
		showFragmentDialog(true);
		//pressedRoutine = true;
	//	loadImages();
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		try {
			progress.dismiss();

		}catch(Exception ex) {
			System.out.println("Excepcion ->"+ex);
		}

		switch (requestCode) {

		case REQUEST_CODE_INSTALL_GOOGLE_PLAY_SERVICES:
			if (resultCode == Activity.RESULT_OK) {
				tryToGetLocation();
			}
			break;

		case REQUEST_CODE_FETCH_BRANCHES:
			if (resultCode == 0) {
				Toast.makeText(
						getActivity(),
						getResources().getString(
								R.string.error_connection_server),
						Toast.LENGTH_SHORT).show();

				final Intent mainBoard = new Intent(getActivity() /* context */,
						MainboardActivity.class);
				mainBoard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mainBoard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(mainBoard);

			} else {
				mFetchingBranches = false;
				mBranchesFetched = (resultCode == Activity.RESULT_OK);
				setListContent();
			}
			break;

		default:
			super.onActivityResult(requestCode, resultCode, data);
			break;

		}


	}

	/**
	 * Sets the list content.
	 */
	@SuppressLint("NewApi")
	// We check which version we are running
	private void setListContent() {

		if (isResumed()) {
			setListShownNoAnimation(true);
		} else {
			setListShown(true);
		}

		showRetryButton(!mBranchesFetched);

		if (mBranchesFetched) {
			setEmptyText(getString(R.string.empty_branches));

			final String[] branchTypes = getResources().getStringArray(
					R.array.branch_types);

			mAdapter.clear();
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				mAdapter.addAll(branchTypes);
			} else {
				final int branchTypeCount = branchTypes.length;
				for (int i = 0; i < branchTypeCount; i++) {
					mAdapter.add(branchTypes[i]);
				}
			}
		} else {
			setEmptyText(getString(R.string.error_connection));
		}
	//	showFragmentDialog(true);
	}

	/**
	 * Show retry button.
	 * 
	 * @param show
	 *            the show
	 */
	private void showRetryButton(boolean show) {
		mBtnRetry.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	/**
	 * Fetch branches.
	 * 
	 * @param location
	 *            the location
	 */
	private void fetchBranches(final Location location) {
		final Intent data = new Intent();
		final PendingIntent pendingIntent = getActivity().createPendingResult(
				REQUEST_CODE_FETCH_BRANCHES, data, 0 /* flags */);
		final Intent fetchBranchesService = new Intent(getActivity(),
				FetchBranchService.class);
		fetchBranchesService.putExtra(FetchBranchService.EXTRA_PENDING_INTENT,
				pendingIntent);
		fetchBranchesService.putExtra(
				FetchBranchService.EXTRA_LOCATION_LATITUDE,
				location.getLatitude());
		fetchBranchesService.putExtra(
				FetchBranchService.EXTRA_LOCATION_LONGITUDE,
				location.getLongitude());
		getActivity().startService(fetchBranchesService);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.gms.common.GooglePlayServicesClient.
	 * OnConnectionFailedListener
	 * #onConnectionFailed(com.google.android.gms.common.ConnectionResult)
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.d(TAG, "onConnectionFailed");
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(
						getActivity() /* activity */,
						REQUEST_CODE_INSTALL_GOOGLE_PLAY_SERVICES);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException ignore) {
				Log.e(TAG, ignore.getMessage());
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
			showGooglePlayServicesErrorDialog(connectionResult.getErrorCode());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks
	 * #onConnected(android.os.Bundle)
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		Log.d(TAG, "onConnected");
		final LocationRequest request = LocationRequest.create()
				.setFastestInterval(REQUEST_LOC_FASTEST_INTERVAL)
				.setInterval(REQUEST_LOC_INTERVAL)
				.setNumUpdates(REQUEST_LOC_NUM_UPDATES)
				.setPriority(REQUEST_LOC_PRIORITY);
		mLocationClient
				.requestLocationUpdates(request, this /* locationListener */);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks
	 * #onDisconnected()
	 */
	@Override
	public void onDisconnected() {
		Log.d(TAG, "onDisconnected");
		/* Do nothing */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.android.gms.location.LocationListener#onLocationChanged(android
	 * .location.Location)
	 */
	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "onLocationChanged");
		fetchBranches(location);
		mLocationClient.disconnect();
		mLocationClient = null;
		mLocation = location;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.sportsworld.android.app.SherlockWorkingListFragment#onListItemClick
	 * (android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		final String selected = mAdapter.getItem(position);
		final Intent specificBranchTypeList = new Intent(
				getActivity() /* context */, SpecificBranchTypeListActivity.class);
		if (getString(R.string.type_coming_soon).equals(selected)) {
			SportsWorldPreferences.setBranchType(getActivity(),
					"Pr�ximas Aperturas");
			specificBranchTypeList.putExtra(
					SpecificBranchTypeListActivity.EXTRA_TYPE,
					Branch.TYPE_COMING_SOON);
		} else if (getString(R.string.type_df).equals(selected)) {
			SportsWorldPreferences.setBranchType(getActivity(),
					"D.F. y �rea metropolitana");
			specificBranchTypeList.putExtra(
					SpecificBranchTypeListActivity.EXTRA_TYPE, Branch.TYPE_DF);
		} else if (getString(R.string.type_favorite).equals(selected)) {
			if (!isMember()) {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.error_not_a_member),
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				SportsWorldPreferences
						.setBranchType(getActivity(), "Favoritos");
				specificBranchTypeList.putExtra(
						SpecificBranchTypeListActivity.EXTRA_TYPE,
						Branch.TYPE_FAVORITE);
			}
		} else if (getString(R.string.type_province).equals(selected)) {
			SportsWorldPreferences.setBranchType(getActivity(), "For�neo");
			specificBranchTypeList.putExtra(
					SpecificBranchTypeListActivity.EXTRA_TYPE,
					Branch.TYPE_PROVINCE);
		}

		startActivity(specificBranchTypeList);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		/* We only listen to btn_retry */
        final Intent specificBranchTypeList = new Intent(
				getActivity() /* context */, SpecificBranchTypeListActivity.class);
        switch(v.getId()){
            case R.id.clubes_fav:
                if (!isMember()) {
                    Toast.makeText(getActivity(),
							getResources().getString(R.string.error_not_a_member),
							Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    SportsWorldPreferences
                            .setBranchType(getActivity(), "Favoritos");
                    specificBranchTypeList.putExtra(
                            SpecificBranchTypeListActivity.EXTRA_TYPE,
                            Branch.TYPE_FAVORITE);
                }
                break;
            case R.id.prox_pertura:
                SportsWorldPreferences.setBranchType(getActivity(),
                        "Pr�ximas Aperturas");
                specificBranchTypeList.putExtra(
                        SpecificBranchTypeListActivity.EXTRA_TYPE,
                        Branch.TYPE_COMING_SOON);
                break;
            case R.id.area_metro:
                SportsWorldPreferences.setBranchType(getActivity(),
                        "D.F. y �rea metropolitana");
                specificBranchTypeList.putExtra(
                        SpecificBranchTypeListActivity.EXTRA_TYPE, Branch.TYPE_DF);
                break;
            case R.id.foraneos:
                SportsWorldPreferences.setBranchType(getActivity(), "For�neo");
                specificBranchTypeList.putExtra(
                        SpecificBranchTypeListActivity.EXTRA_TYPE,
                        Branch.TYPE_PROVINCE);
                break;

        }
        startActivity(specificBranchTypeList);
        //retry();
	}

	/**
	 * Retry.
	 */
	private void retry() {
		mBranchesFetched = false;
		mFetchingBranches = true;
		setListShown(false);
		mLocation = null;
		tryToGetLocation();
	}

	/**
	 * Show fragment dialog.
	 * 
	 * @param savedInstanceState
	 *            the saved instance state
	 */
	public void showFragmentDialog(boolean savedInstanceState) {

		dialgoFragment = getFragmentManager().findFragmentByTag(
				ProgressDialogFragment.progressDialogTag);

		if (savedInstanceState == true) {
			dialgoFragment = getFragmentManager().findFragmentByTag(
					ProgressDialogFragment.progressDialogTag);
			progress = (ProgressDialogFragment) dialgoFragment;

			//loadImages();

		} else {

			if (dialgoFragment != null) {
				getFragmentManager().beginTransaction().remove(dialgoFragment)
						.commit();
			}
			progress = ProgressDialogFragment.newInstance(getActivity());
			progress.show(getFragmentManager(),
					ProgressDialogFragment.progressDialogTag);
			//loadImages();
		}

	}

	public void loadImages(){

		if(getRotation().equals("landscape") || getRotation().equals("portrait")) {
			iClubes.setImageResource(R.drawable.studios_favoritos);
			iAperturas.setImageResource(R.drawable.prox_aperturas);
			iForaneos.setImageResource(R.drawable.foraneos);
			iArea.setImageResource(R.drawable.area_metro);
		}
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

	public String getRotation(){
		/*final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
		switch (rotation) {
			case Surface.ROTATION_0:
				return "portrait";
			case Surface.ROTATION_90:
				return "landscape";
			case Surface.ROTATION_180:
				return "reverse portrait";
			default:
				return "reverse landscape";
		}*/
		int ot = getResources().getConfiguration().orientation;
		switch(ot)
		{

			case  Configuration.ORIENTATION_LANDSCAPE:
				Log.d("my orient", "ORIENTATION_LANDSCAPE");
				return "landscape";
			case Configuration.ORIENTATION_PORTRAIT:
				Log.d("my orient", "ORIENTATION_PORTRAIT");
				return "portrait";
			default:
				Log.d("my orient", "default val");
				return "reverse landscape";
		}
	}
}
