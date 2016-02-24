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
import mx.com.sportsworld.sw.fragment.LoyaltyPointFragment;

// TODO: Auto-generated Javadoc

/**
 * The Class LoyaltyActivity.
 */
public class LoyaltyActivity extends SherlockFragmentActivity {

	/** The Constant FRAG_TAG_LOYALTY_POINTS. */
	private static final String FRAG_TAG_LOYALTY_POINTS = "frag_tag_loyalty_points";

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
			final Fragment f = LoyaltyPointFragment.newInstance();
			ft.add(android.R.id.content, f, FRAG_TAG_LOYALTY_POINTS);
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
				FRAG_TAG_LOYALTY_POINTS);
		f.onActivityResult(requestCode, resultCode, data);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			final Intent mainBoard = new Intent(this /* context */,
					MainboardActivity.class);
			startActivity(mainBoard);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
