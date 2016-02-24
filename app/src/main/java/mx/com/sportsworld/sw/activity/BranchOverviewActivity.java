package mx.com.sportsworld.sw.activity;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.app.AuthSherlockFragmentActivity;
import mx.com.sportsworld.sw.fragment.BranchOverviewFragment;
import mx.com.sportsworld.sw.fragment.BranchOverviewFragment.OnShowLocationListener;

// TODO: Auto-generated Javadoc

/**
 * The Class BranchOverviewActivity.
 */
public class BranchOverviewActivity extends AuthSherlockFragmentActivity
		implements OnShowLocationListener {

	/** The Constant EXTRA_BRANCH_ID. */
	public static final String EXTRA_BRANCH_ID = "com.upster.extra.BRANCH_ID";
	
	/** The Constant FRAG_TAG_BRANCH_OVERVIEW. */
	private static final String FRAG_TAG_BRANCH_OVERVIEW = "frag_tag_branch_overview";
	
	/** The m branch id. */
	private long mBranchId;

	/* (non-Javadoc)
	 * @see com.sportsworld.android.app.AuthSherlockFragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (isFinishing()) {
			return;
		}

		getSupportActionBar().setTitle(R.string.clubs);

		mBranchId = getIntent().getLongExtra(EXTRA_BRANCH_ID, -1L);
		if (mBranchId == -1L) {
			throw new IllegalArgumentException(
					"You must pass a EXTRA_BRANCH_ID long extra");
		}

		if (savedInstanceState == null) {
			final FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			final Fragment f = BranchOverviewFragment.newInstance(mBranchId);
			ft.add(android.R.id.content, f, FRAG_TAG_BRANCH_OVERVIEW);
			ft.commit();
		}

	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.fragment.BranchOverviewFragment.OnShowLocationListener#onShowLocationClick(long)
	 */
	@Override
	public void onShowLocationClick(long id) {
		final Intent showLocationOnMap = new Intent(this /* context */,
				BranchLocationActivity.class);
		showLocationOnMap.putExtra(FacilitiesActivity.EXTRA_BRANCH_ID,
				mBranchId);
		startActivity(showLocationOnMap);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/*
		 * XXX We will manually forward it to the fragment, since our activity
		 * is not doing it automatically. This has the unfortunate side effect
		 * of calling fragment's onActivityResult twice.
		 */
		final Fragment f = getSupportFragmentManager().findFragmentByTag(
				FRAG_TAG_BRANCH_OVERVIEW);
		f.onActivityResult(requestCode, resultCode, data);

	}

}
