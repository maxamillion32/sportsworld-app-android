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
import android.view.KeyEvent;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.fragment.AwardFragment;

// TODO: Auto-generated Javadoc

/**
 * The Class AwardActivity.
 */
public class AwardActivity extends SherlockFragmentActivity {

	/** The Constant FRAG_TAG_AWARD. */
	private static final String FRAG_TAG_AWARD = "frag_tag_award";

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFinishing()) {
			return;
		}

		getSupportActionBar().setTitle(R.string.loyalty);

		if (savedInstanceState == null) {
			final FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			final Fragment f = AwardFragment.newInstance();
			ft.add(android.R.id.content, f, FRAG_TAG_AWARD);
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
				FRAG_TAG_AWARD);
		f.onActivityResult(requestCode, resultCode, data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onKeyDown(int,
	 * android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
