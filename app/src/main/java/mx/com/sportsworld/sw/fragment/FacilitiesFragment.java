package mx.com.sportsworld.sw.fragment;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.SparseArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.fragment.dialog.FragmentFacilitiesImgDialog;
import mx.com.sportsworld.sw.model.Branch;
import mx.com.sportsworld.sw.provider.SportsWorldContract;
import mx.com.sportsworld.sw.utils.ConnectionUtils;

// TODO: Auto-generated Javadoc

/**
 * The Class FacilitiesFragment.
 */
public class FacilitiesFragment extends SherlockListFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

	/** The Constant LOADER_ID_FACILITIES. */
	private static final int LOADER_ID_FACILITIES = 0;
	
	/** The Constant FRAG_ARG_BRANCH_ID. */
	private static final String FRAG_ARG_BRANCH_ID = "frag_arg_branch_id";
	
	/** The m adapter. */
	private ArrayAdapter<String> mAdapter;
	
	/** The Constant COL_INDEX_FACILITIES. */
	private static final int COL_INDEX_FACILITIES = 0;
	
	/** The Constant COL_INDEX_URLS. */
	private static final int COL_INDEX_URLS = 1;
	
	/** The Constant COL_INDEX_NAME. */
	private static final int COL_INDEX_NAME = 2;
	
	/** The Constant COLS. */
	private static final String[] COLS = buildColumns();
	
	/** The new fragment. */
	public static FragmentFacilitiesImgDialog newFragment;
	
	/** The facilities imgs. */
	public static String[] facilitiesImgs;
	
	/** The facilities names. */
	public static String[] facilitiesNames;

	/**
	 * Builds the columns.
	 *
	 * @return the string[]
	 */
	private static String[] buildColumns() {

		final SparseArray<String> colsMap = new SparseArray<String>();
		colsMap.put(COL_INDEX_FACILITIES, SportsWorldContract.Branch.FACILITIES);
		colsMap.put(COL_INDEX_URLS, SportsWorldContract.Branch.FAC_URL_IMGS);
		colsMap.put(COL_INDEX_NAME, SportsWorldContract.Branch.FACILITIES);
		final int colCount = colsMap.size();
		final String[] cols = new String[colCount];
		for (int i = 0; i < colCount; i++) {
			cols[i] = colsMap.get(i);
		}

		return cols;

	}

	/**
	 * Instantiates a new facilities fragment.
	 */
	public FacilitiesFragment() {
	}

	/**
	 * New instance.
	 *
	 * @param branchId the branch id
	 * @return the facilities fragment
	 */
	public static FacilitiesFragment newInstance(long branchId) {
		final FacilitiesFragment f = new FacilitiesFragment();
		final Bundle args = new Bundle();
		args.putLong(FRAG_ARG_BRANCH_ID, branchId);
		f.setArguments(args);
		return f;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mAdapter = new ArrayAdapter<String>(getActivity() /* context */,
				 R.layout.mytextview);

		setListAdapter(mAdapter);
		setListShown(false);
		getLoaderManager().initLoader(LOADER_ID_FACILITIES,
				null /* loaderArgs */, this /* loadercallback */);
	}

	/**
	 * Gets the branch id from args.
	 *
	 * @return the branch id from args
	 */
	private long getBranchIdFromArgs() {
		return getArguments().getLong(FRAG_ARG_BRANCH_ID);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		final Uri branchUri = SportsWorldContract.Branch.buildBranchUri(String
				.valueOf(getBranchIdFromArgs()));
		return new CursorLoader(getActivity() /* null */, branchUri, COLS,
				null /* selection */, null /* selectionArgs */, null /* sortOrder */);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader, java.lang.Object)
	 */
	@SuppressLint("NewApi")
	// We check which version we are running.
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		if (isResumed()) {
			setListShownNoAnimation(true);
		} else {
			setListShown(true);
		}

		if (!cursor.moveToFirst()) {
			return;
		}

		final String facilities = cursor.getString(COL_INDEX_FACILITIES);
		final String urls = cursor.getString(COL_INDEX_URLS);
		final String names = cursor.getString(COL_INDEX_NAME);
		if (facilities == null) {
			return;
		}

		final String[] facilitiesArray = facilities.split(Branch.DELIMITER);
		facilitiesImgs = urls.split(Branch.DELIMITER);
		facilitiesNames = names.split(Branch.DELIMITER);
		mAdapter.clear();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mAdapter.addAll(facilitiesArray);
		} else {
			final int count = facilitiesArray.length;
			for (int i = 0; i < count; i++) {
				mAdapter.add(facilitiesArray[i]);
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		if (isNetworkAvailable())
			if (facilitiesImgs[position] != null) {
				newFragment = FragmentFacilitiesImgDialog.newInstance(
						facilitiesImgs[position], facilitiesNames[position]);
				newFragment.setStyle(DialogFragment.STYLE_NO_TITLE,
						android.R.style.Theme_DeviceDefault);
				newFragment.show(getFragmentManager(), "facilitiesImgDialog");
			} else

				Toast.makeText(
						getActivity(),
						getResources().getString(
								R.string.error_connection_server),
						Toast.LENGTH_SHORT).show();
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

}
