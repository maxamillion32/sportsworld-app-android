/*
 * BranchLocationActivity.java 1.0 2013/05/29
 * 
 * Ironbit
 */
package mx.com.sportsworld.sw.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.app.AuthSherlockFragmentActivity;
import mx.com.sportsworld.sw.app.ErrorDialogFragment;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

// TODO: Auto-generated Javadoc

/**
 * Shows on a map the position of a branch.
 * 
 * @author Jos� Torres Fuentes 02/10/2013
 * 
 */
public class BranchLocationActivity extends AuthSherlockFragmentActivity
		implements LoaderManager.LoaderCallbacks<Cursor>,
		DialogInterface.OnCancelListener {

	/** The Constant EXTRA_BRANCH_ID. */
	public static final String EXTRA_BRANCH_ID = "com.upster.extra.BRANCH_ID";
	
	/** The Constant STATE_FIRST_TIME_MARKER_PLACED. */
	private static final String STATE_FIRST_TIME_MARKER_PLACED = "state_first_time_marker_placed";
	
	/** The Constant REQUEST_CODE_INSTALL_GOOGLE_PLAY_SERVICES. */
	private static final int REQUEST_CODE_INSTALL_GOOGLE_PLAY_SERVICES = 0;
	
	/** The Constant LOADER_ID_LOCATION. */
	private static final int LOADER_ID_LOCATION = 0;
	
	/** The Constant FRAG_TAG_MAP. */
	private static final String FRAG_TAG_MAP = "frag_tag_map";
	
	/** The Constant FRAG_TAG_ERROR_DIALOG. */
	private static final String FRAG_TAG_ERROR_DIALOG = "frag_tag_error_dialog";
	
	/** The Constant COL_INDEX_LATITUDE. */
	private static final int COL_INDEX_LATITUDE = 0;
	
	/** The Constant COL_INDEX_LONGITUDE. */
	private static final int COL_INDEX_LONGITUDE = 1;
	
	/** The Constant COL_INDEX_NAME. */
	private static final int COL_INDEX_NAME = 2;
	
	/** The Constant COL_INDEX_ADDRESS. */
	private static final int COL_INDEX_ADDRESS = 3;
	
	/** The Constant COLS. */
	private static final String[] COLS = buildColumns();
	
	/** The m map fragment. */
	private SupportMapFragment mMapFragment;
	
	/** The m map. */
	private GoogleMap mMap;
	
	/** The m first time marker placed. */
	private boolean mFirstTimeMarkerPlaced;
	
	/** The m branch id. */
	private long mBranchId;
	
	/** The latitude. */
	public double latitude;
	
	/** The longitude. */
	public double longitude;

	/**
	 * Builds the columns.
	 *
	 * @return the string[]
	 */
	private static String[] buildColumns() {

		final SparseArray<String> colsMap = new SparseArray<String>();
		colsMap.put(COL_INDEX_LATITUDE, SportsWorldContract.Branch.LATITUDE);
		colsMap.put(COL_INDEX_LONGITUDE, SportsWorldContract.Branch.LONGITUDE);
		colsMap.put(COL_INDEX_NAME, SportsWorldContract.Branch.NAME);
		colsMap.put(COL_INDEX_ADDRESS, SportsWorldContract.Branch.ADDRESS);

		final int colCount = colsMap.size();
		final String[] cols = new String[colCount];
		for (int i = 0; i < colCount; i++) {
			cols[i] = colsMap.get(i);
		}

		return cols;

	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.app.AuthSherlockFragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final boolean firstTime = (savedInstanceState == null);

		/*if (isFinishing()
				|| (firstTime && !checkGooglePlayServicesAvailability())) {
			return;
		}*/

		getSupportActionBar().setTitle(R.string.clubs);

		mBranchId = getIntent().getLongExtra(EXTRA_BRANCH_ID, -1);
		if (mBranchId == -1) {
			throw new IllegalArgumentException(
					"You must pass a EXTRA_BRANCH_ID long extra");
		}

		if (!firstTime) {
			mFirstTimeMarkerPlaced = savedInstanceState
					.getBoolean(STATE_FIRST_TIME_MARKER_PLACED);
		}

		final FragmentManager fm = getSupportFragmentManager();
		mMapFragment = (SupportMapFragment) fm.findFragmentByTag(FRAG_TAG_MAP);
		if (mMapFragment == null) {
			final FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			mMapFragment = SupportMapFragment.newInstance();
			mMapFragment.setRetainInstance(true);
			ft.add(android.R.id.content, mMapFragment, FRAG_TAG_MAP);
			ft.commit();
		}
		maySetupMap();

	}

	/**
	 * Check google play services availability.
	 *
	 * @return true, if successful
	 */
	private boolean checkGooglePlayServicesAvailability() {
		final int connectionResult = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext());
		return !showGooglePlayServicesErrorDialog(connectionResult);
	}

	/**
	 * Show google play services error dialog.
	 *
	 * @param connectionResult the connection result
	 * @return true, if successful
	 */
	private boolean showGooglePlayServicesErrorDialog(int connectionResult) {
		final Dialog googlePlayDialog = GooglePlayServicesUtil
				.getErrorDialog(connectionResult, this /* activity */,
						REQUEST_CODE_INSTALL_GOOGLE_PLAY_SERVICES, this /* onCancelListener */);
		if (googlePlayDialog == null) {
			return false;
		}
		ErrorDialogFragment.newInstance(googlePlayDialog).show(
				getSupportFragmentManager(), FRAG_TAG_ERROR_DIALOG);
		return true;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		maySetupMap();
		// checkLocationProvidersEnabled();
	}

	/**
	 * May setup map.
	 */
	private void maySetupMap() {
		// We get a null mMapFragment if GooglePlayServices are not available
		if (mMap != null || mMapFragment == null) {
			return;
		}
		mMap = mMapFragment.getMap();
		if (mMap != null) {
			setupMap();
		}
	}

	/**
	 * Setup map.
	 */
	private void setupMap() {
		mMap.setMyLocationEnabled(true);
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		getSupportLoaderManager().initLoader(LOADER_ID_LOCATION, null /* args */,
				this /* loaderCallback */);
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(STATE_FIRST_TIME_MARKER_PLACED,
				mFirstTimeMarkerPlaced);
	}

	/* (non-Javadoc)
	 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
	 */
	@Override
	public void onCancel(DialogInterface dialog) {
		finish();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode != REQUEST_CODE_INSTALL_GOOGLE_PLAY_SERVICES) {
			return;
		}
		Toast.makeText(
				getApplicationContext(),
				getResources().getString(
						R.string.success_google_play_services_install),
				Toast.LENGTH_SHORT).show();

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		final Uri branchUri = SportsWorldContract.Branch.buildBranchUri(String
				.valueOf(mBranchId));
		return new CursorLoader(this /* context */, branchUri, COLS,
				null /* selection */, null /* selectionArgs */, null /* sortOrder */);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		addMarkers(cursor);
	}

	/**
	 * Adds the markers.
	 *
	 * @param cursor the cursor
	 */
	private void addMarkers(Cursor cursor) {

		if (!cursor.moveToFirst()) {
			return;
		}

		latitude = cursor.getDouble(COL_INDEX_LATITUDE);
		longitude = cursor.getDouble(COL_INDEX_LONGITUDE);

		/*
		 * XXX It's a bad practice to compare floats or doubles with == , but we
		 * can be sure that in our case we will get exactly 0.0d if there is no
		 * data.
		 */
		if (latitude == 0.0d || longitude == 0.0d) {
			Toast.makeText(
					getApplicationContext(),
					getResources().getString(
							R.string.error_no_location_for_branch),
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		final LatLng branchPosition = new LatLng(latitude, longitude);

		final MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.title(cursor.getString(COL_INDEX_NAME));
		markerOptions.snippet(cursor.getString(COL_INDEX_ADDRESS));

		//markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_branch));

		markerOptions.position(branchPosition);
		mMap.addMarker(markerOptions);

		mMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {

				String uri = "geo:" + latitude + "," + longitude + "?q="
						+ latitude + "," + longitude + "(Upster "
						+ arg0.getTitle() + ")";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(uri));
				startActivity(i);
				return false;
			}
		});
		if (!mFirstTimeMarkerPlaced) {

			mFirstTimeMarkerPlaced = true;

			/*
			 * We need to move the camera to get the max zoom level on branch
			 * position
			 */
			final CameraUpdate moveToBranchPosition = CameraUpdateFactory
					.newLatLng(branchPosition);
			mMap.animateCamera(moveToBranchPosition);

			final float maxZoomLevel = 14;

			/*
			 * If we just try to zoom, we lose the position. Then, we need to
			 * apply a "move and zoom" camera update.
			 */
			final CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(
					branchPosition, maxZoomLevel);
			mMap.animateCamera(zoom);

		}

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		/* Do nothing */
	}

}
