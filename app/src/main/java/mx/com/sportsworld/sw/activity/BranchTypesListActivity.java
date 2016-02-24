package mx.com.sportsworld.sw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import mx.com.sportsworld.sw.app.AuthSherlockFragmentActivity;
import mx.com.sportsworld.sw.fragment.BranchTypesListFragment;
// TODO: Auto-generated Javadoc

/**
 * The Class BranchTypesListActivity.
 *
 * @author Jose Torres Fuentes
 * Ironbit
 */


public class BranchTypesListActivity extends AuthSherlockFragmentActivity {

	/** The Constant FRAG_TAG_BRANCH_TYPES. */
	private static final String FRAG_TAG_BRANCH_TYPES = "frag_tag_branch_types";

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
				FragmentTransaction ft = getSupportFragmentManager()
						.beginTransaction();
				Fragment f = BranchTypesListFragment.newInstance();
				ft.add(android.R.id.content, f, FRAG_TAG_BRANCH_TYPES);
				ft.commit();
		}

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
				FRAG_TAG_BRANCH_TYPES);
		f.onActivityResult(requestCode, resultCode, data);
		//String result = data.getStringExtra("result");
		//Toast.makeText(this, "ParametrosActivity devolvi�: " + result, Toast.LENGTH_LONG).show();
	}

}
