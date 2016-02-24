package mx.com.sportsworld.sw.activity;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.SparseArray;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.app.AuthSherlockFragmentActivity;
import mx.com.sportsworld.sw.fragment.ChooseGoalFragment;
import mx.com.sportsworld.sw.fragment.ChooseGoalFragment.OnGoalClickListener;
import mx.com.sportsworld.sw.fragment.ChooseLevelFragment;
import mx.com.sportsworld.sw.fragment.ChooseLevelFragment.OnLevelClickListener;
import mx.com.sportsworld.sw.fragment.ChooseRoutineFragment;
import mx.com.sportsworld.sw.fragment.ChooseRoutineFragment.OnRoutineClickListener;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

// TODO: Auto-generated Javadoc

/**
 * 
 * Shows goals, levels and routines (based on the first two).
 * 
 * 
 */
public class RoutineSelectorActivity extends AuthSherlockFragmentActivity
		implements OnGoalClickListener, OnLevelClickListener,
		OnRoutineClickListener, LoaderManager.LoaderCallbacks<Cursor> {

	/** The Constant FRAG_TAG_CHOOSE_GOAL. */
	private static final String FRAG_TAG_CHOOSE_GOAL = "frag_tag_goal";

	/** The Constant FRAG_TAG_CHOOSE_LEVEL. */
	private static final String FRAG_TAG_CHOOSE_LEVEL = "frag_tag_level";

	/** The Constant FRAG_TAG_CHOOSE_ROUTINE. */
	private static final String FRAG_TAG_CHOOSE_ROUTINE = "frag_tag_routine";

	/** The Constant STATE_SELECTED_GOAL_ID. */
	private static final String STATE_SELECTED_GOAL_ID = "state_selected_goal_id";

	/** The Constant STATE_SELECTED_LEVEL_ID. */
	private static final String STATE_SELECTED_LEVEL_ID = "state_selected_level_id";

	/** The Constant COLS. */
	private static final String[] COLS = buildColumns();

	/** The Constant COL_INDEX_NAME. */
	private static final int COL_INDEX_NAME = 0;

	/** The m selected goal id. */
	private long mSelectedGoalId;

	/** The m selected level id. */
	private long mSelectedLevelId;

	/**
	 * Builds the columns.
	 * 
	 * @return the string[]
	 */
	private static String[] buildColumns() {
		final SparseArray<String> colsMap = new SparseArray<String>();
		colsMap.put(COL_INDEX_NAME, SportsWorldContract.User.NAME);

		final int colCount = colsMap.size();
		final String[] cols = new String[colCount];
		for (int i = 0; i < colCount; i++) {
			cols[i] = colsMap.get(i);
		}

		return cols;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sportsworld.android.app.AuthSherlockFragmentActivity#onCreate(android
	 * .os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (isFinishing()) {
			return;
		}

		getSupportActionBar().setTitle(R.string.objective);

		if (savedInstanceState != null) {
			mSelectedGoalId = savedInstanceState
					.getLong(STATE_SELECTED_GOAL_ID);
			mSelectedLevelId = savedInstanceState
					.getLong(STATE_SELECTED_LEVEL_ID);
		}

		getSupportLoaderManager().initLoader(0 /* loaderId */,
				null /* loaderArgs */, this /* loaderCallback */);

		addChooseGoalFragment();

	}

	/*
	 * Choose the level of your rutine
	 */
	/* (non-Javadoc)
	 * @see com.sportsworld.android.fragment.ChooseLevelFragment.OnLevelClickListener#onLevelClick(long)
	 */
	@Override
	public void onLevelClick(long id) {
		mSelectedLevelId = id;
		showChooseRoutineFragment();
	}

	/*
	 * Set the goal
	 */
	/* (non-Javadoc)
	 * @see com.sportsworld.android.fragment.ChooseGoalFragment.OnGoalClickListener#onGoalClick(long)
	 */
	@Override
	public void onGoalClick(long id) {
		mSelectedGoalId = id;
		showChooseLevelFragment();
	}

	/*
	 * Opens Routine Activity
	 */
	/* (non-Javadoc)
	 * @see com.sportsworld.android.fragment.ChooseRoutineFragment.OnRoutineClickListener#onRoutineClick(long)
	 */
	@Override
	public void onRoutineClick(long id) {
		final Intent routine = new Intent(this /* context */,
				RoutineActivity.class);
		routine.putExtra("haveRoutine", false);
		routine.putExtra(RoutineActivity.EXTRA_ROUTINE_ID, id);
		startActivity(routine);
	}

	/**
	 * Adds the choose goal fragment.
	 */
	private void addChooseGoalFragment() {
		final FragmentManager fm = getSupportFragmentManager();
		if (fm.findFragmentByTag(FRAG_TAG_CHOOSE_GOAL) == null) {
			final FragmentTransaction ft = fm.beginTransaction();
			final Fragment f = ChooseGoalFragment.newInstance();
			ft.add(android.R.id.content, f, FRAG_TAG_CHOOSE_GOAL);
			ft.commit();
		}
	}

	/**
	 * Show choose level fragment.
	 */
	private void showChooseLevelFragment() {
		final FragmentTransaction ft = getSupportFragmentManager()
				.beginTransaction();
		final Fragment f = ChooseLevelFragment.newInstance();
		ft.replace(android.R.id.content, f, FRAG_TAG_CHOOSE_LEVEL);
		ft.addToBackStack(FRAG_TAG_CHOOSE_GOAL /* name */);
		ft.commit();
	}

	/**
	 * Show choose routine fragment.
	 */
	private void showChooseRoutineFragment() {
		final FragmentTransaction ft = getSupportFragmentManager()
				.beginTransaction();
		final Fragment f = ChooseRoutineFragment.newInstance(mSelectedGoalId,
				mSelectedLevelId);
		ft.replace(android.R.id.content, f, FRAG_TAG_CHOOSE_ROUTINE);
		ft.addToBackStack(FRAG_TAG_CHOOSE_LEVEL /* name */);
		ft.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockFragmentActivity#onSaveInstanceState
	 * (android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(STATE_SELECTED_GOAL_ID, mSelectedGoalId);
		outState.putLong(STATE_SELECTED_LEVEL_ID, mSelectedLevelId);
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
		final Uri userUri = SportsWorldContract.User
				.buildUserUri(getAccountManager().getCurrentUserId());
		return new CursorLoader(this /* context */, userUri, COLS,
				null /* selection */, null /* selectionArgs */, null /* sortOrder */);
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

		if (!cursor.moveToFirst()) {
			getAccountManager().logOut();
			throw new RuntimeException(
					"We don't have user data. Log out and then crash."
							+ " This should never happen!");
		}

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
		/* Do nothing */
	}

}
