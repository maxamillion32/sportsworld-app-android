package mx.com.sportsworld.sw.fragment;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import mx.com.sportsworld.sw.app.WebViewSherlockFragment;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

// TODO: Auto-generated Javadoc

/**
 * The Class ExerciseInstructionsFragment.
 */
public class ExerciseInstructionsFragment extends WebViewSherlockFragment
		implements LoaderManager.LoaderCallbacks<Cursor> {

	/** The Constant LOADER_ID_INSTRUCTION. */
	private static final int LOADER_ID_INSTRUCTION = 0;
	
	/** The Constant FRAG_ARG_EXERCISE_ID. */
	private static final String FRAG_ARG_EXERCISE_ID = "frag_arg_exercise_id";
	
	/** The Constant COL_INDEX_INSTRUCTIONS. */
	private static final int COL_INDEX_INSTRUCTIONS = 0;
	
	/** The Constant COLS. */
	private static final String[] COLS = new String[] { SportsWorldContract.Exercise.INSTRUCTIONS };

	/**
	 * Instantiates a new exercise instructions fragment.
	 */
	public ExerciseInstructionsFragment() {
	}

	/**
	 * New instance.
	 *
	 * @param exerciseId the exercise id
	 * @return the exercise instructions fragment
	 */
	public static ExerciseInstructionsFragment newInstance(long exerciseId) {
		final ExerciseInstructionsFragment f = new ExerciseInstructionsFragment();
		final Bundle args = new Bundle();
		args.putLong(FRAG_ARG_EXERCISE_ID, exerciseId);
		f.setArguments(args);
		return f;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		final LoaderManager loaderMngr = getLoaderManager();
		if ((savedInstanceState == null)
				|| (loaderMngr.getLoader(LOADER_ID_INSTRUCTION) != null)) {
			loaderMngr.initLoader(LOADER_ID_INSTRUCTION, null /* loaderArgs */,
					this /* loaderCallback */);
		}
	}

	/**
	 * Gets the exercise id from args.
	 *
	 * @return the exercise id from args
	 */
	private long getExerciseIdFromArgs() {
		return getArguments().getLong(FRAG_ARG_EXERCISE_ID);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		final String exerciseId = String.valueOf(getExerciseIdFromArgs());
		final Uri exerciseUri = SportsWorldContract.Exercise
				.buildExerciseUri(exerciseId);
		return new CursorLoader(getActivity() /* context */, exerciseUri, COLS,
				null /* selection */, null /* selectionArgs */, null /* sortOrder */);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (SportsWorldPreferences.getInfoRoutine(getActivity()) != null) {
			getWebView().loadDataWithBaseURL("",
					SportsWorldPreferences.getInfoRoutine(getActivity()),
					"text/html", "utf-8", "");
		} else if (cursor.moveToFirst()) {
			getWebView().loadDataWithBaseURL("",
					cursor.getString(COL_INDEX_INSTRUCTIONS), "text/html",
					"utf-8", "");
		}

		getLoaderManager().destroyLoader(LOADER_ID_INSTRUCTION);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		/* Do nothing */
	}

}
