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
import mx.com.sportsworld.sw.fragment.GymClassesFragment;

// TODO: Auto-generated Javadoc

/**
 *
 * Container for GymClassesFragment.
 *
 *
 */
public class GymClassesActivity extends AuthSherlockFragmentActivity {

	/** The Constant EXTRA_BRANCH_ID. */
	public static final String EXTRA_BRANCH_ID = "com.upster.extra.BRANCH_ID";

	/** The Constant FRAG_TAG_GYM_CLASSES. */
	private static final String FRAG_TAG_GYM_CLASSES = "frag_tag_gym_classes";

	/** The Constant EXTRA_DATE_ID. */
	public static final String EXTRA_DATE_ID = "com.upster.extra.DATE_ID";

	/* (non-Javadoc)
	 * @see com.sportsworld.android.app.AuthSherlockFragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFinishing()) {
			return;
		}

		getSupportActionBar().setTitle(R.string.classes);

		if (savedInstanceState == null) {
			final FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			final long branchId = getIntent().getLongExtra(EXTRA_BRANCH_ID, -1);
			final long dateId = getIntent().getLongExtra(EXTRA_DATE_ID, -1);
			final Fragment f = (branchId == -1) ? GymClassesFragment
					.newInstance() : GymClassesFragment.newInstance(branchId,
					dateId);
			ft.add(android.R.id.content, f, FRAG_TAG_GYM_CLASSES);
			ft.commit();
		}


	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		final Fragment f = getSupportFragmentManager().findFragmentByTag(
				FRAG_TAG_GYM_CLASSES);
		f.onActivityResult(requestCode, resultCode, data);
	}

}

