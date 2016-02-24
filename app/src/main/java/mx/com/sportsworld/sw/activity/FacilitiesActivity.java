package mx.com.sportsworld.sw.activity;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import mx.com.sportsworld.sw.app.AuthSherlockFragmentActivity;
import mx.com.sportsworld.sw.fragment.FacilitiesFragment;

// TODO: Auto-generated Javadoc

/**
 * The Class FacilitiesActivity.
 */
public class FacilitiesActivity extends AuthSherlockFragmentActivity {

	/** The Constant EXTRA_BRANCH_ID. */
	public static final String EXTRA_BRANCH_ID = "com.upster.extra.BRANCH_ID";
	
	/** The Constant FRAG_TAG_FACILITIES. */
	private static final String FRAG_TAG_FACILITIES = "frag_tag_facilities";

	/* (non-Javadoc)
	 * @see com.sportsworld.android.app.AuthSherlockFragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (isFinishing()) {
			return;
		}

		if (savedInstanceState == null) {
			final FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			final long branchId = getIntent()
					.getLongExtra(EXTRA_BRANCH_ID, -1L);
			final Fragment f = FacilitiesFragment.newInstance(branchId);
			ft.add(android.R.id.content, f, FRAG_TAG_FACILITIES);
			ft.commit();
		}

	}

}
