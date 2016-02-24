package mx.com.sportsworld.sw.activity;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import mx.com.sportsworld.sw.account.LoginActivity;
import mx.com.sportsworld.sw.fragment.PoliciesFragment;
import mx.com.sportsworld.sw.fragment.PoliciesFragment.PolicyEventListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;

// TODO: Auto-generated Javadoc

/**
 * Host activity for PoliciesFragment.
 */
public class PoliciesActivity extends SherlockFragmentActivity implements
		PolicyEventListener {

	/** The Constant FRAG_TAG_POLICIES. */
	private static final String FRAG_TAG_POLICIES = "frag_tag_policies";

	private boolean mOnlyShowPolicies = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {

			final String action = getIntent().getAction();
			mOnlyShowPolicies = Intent.ACTION_VIEW.equals(action);

			final FragmentManager fm = getSupportFragmentManager();
			final FragmentTransaction ft = fm.beginTransaction();
			final Fragment f = PoliciesFragment.newInstance(mOnlyShowPolicies);
			ft.add(android.R.id.content, f, FRAG_TAG_POLICIES);
			ft.commit();
		}
	}

	/*
	 * Open tour activity
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sportsworld.android.fragment.PoliciesFragment.PolicyEventListener
	 * #onTakeTourClick()
	 */
	@Override
	public void onTakeTourClick() {
		final Intent takeTour = new Intent(this /* context */,
				TourActivity.class);
		startActivity(takeTour);
	}

	/*
	 * Opens Login Activity
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sportsworld.android.fragment.PoliciesFragment.PolicyEventListener
	 * #onAgree()
	 */
	@Override
	public void onAgree() {
		final Intent login = new Intent(this /* context */, LoginActivity.class);
		startActivity(login);
		finish();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onKeyDown(int,
	 * android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && !mOnlyShowPolicies) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}

}
