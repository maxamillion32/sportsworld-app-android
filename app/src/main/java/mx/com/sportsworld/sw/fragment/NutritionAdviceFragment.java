	package mx.com.sportsworld.sw.fragment;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import mx.com.sportsworld.sw.app.WebViewSherlockFragment;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

// TODO: Auto-generated Javadoc

/**
 * Shows nutrition advice.
 * 
 * @author Jos� Torres Fuentes 02/10/2013
 * 
 */
public class NutritionAdviceFragment extends WebViewSherlockFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

	/** The Constant LOADER_ID_ROUTINE. */
	private static final int LOADER_ID_ROUTINE = 0;
	
	/** The Constant COL_INDEX_NUTRITION_ADVICE. */
	private static final int COL_INDEX_NUTRITION_ADVICE = 0;
	
	/** The Constant COLS. */
	private static final String[] COLS = new String[] { SportsWorldContract.Routine.NUTRITION_ADVICE };

	/**
	 * Instantiates a new nutrition advice fragment.
	 */
	public NutritionAdviceFragment() {
	}

	/**
	 * New instace.
	 *
	 * @return the nutrition advice fragment
	 */
	public static NutritionAdviceFragment newInstace() {
		return new NutritionAdviceFragment();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		final LoaderManager loaderMngr = getLoaderManager();
		if ((savedInstanceState == null)
				|| (loaderMngr.getLoader(LOADER_ID_ROUTINE) != null)) {
			loaderMngr
					.initLoader(LOADER_ID_ROUTINE, null /* loaderArgs */, this /* loaderCallback */);
		}

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getActivity() /* context */,
				SportsWorldContract.Routine.CONTENT_URI, COLS,
				null /* selection */, null /* selectionArgs */, null /* sortOrder */);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (cursor.moveToFirst()) {
			getWebView().loadDataWithBaseURL("",
					cursor.getString(COL_INDEX_NUTRITION_ADVICE), "text/html",
					"utf-8", "");

		}
		getLoaderManager().destroyLoader(LOADER_ID_ROUTINE);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		/* Do nothing */
	}

}
