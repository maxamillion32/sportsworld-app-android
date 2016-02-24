package mx.com.sportsworld.sw.app;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.res.Configuration;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import mx.com.sportsworld.sw.account.SportsWorldAccountManager;

/**
 * Checks if the user is logged in. Closes current activity and starts the
 * authentication flow if user is not logged in. Every
 * <code>FragmentActivity</code> accessible after login should extend this
 * class. In your child FragmentActivity, you should check if your activity is
 * being finished on onCreate after the super call. If that is the case, call
 * return.
 * 
 * You can access to a free SportsWorldAccountManager using
 * <code>getAccountManager()</code>.
 * 
 * 
 */
public class AuthSherlockFragmentActivity extends SherlockFragmentActivity {

	private SportsWorldAccountManager mAccountMngr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAccountMngr = new SportsWorldAccountManager(this /* context */);
		SportsWorldAccountManager accountMngr = mAccountMngr;

		if (!accountMngr.isLoggedIn()) {
			accountMngr.startAuthenticationFlow(this /* activity */);
		}

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	protected SportsWorldAccountManager getAccountManager() {
		return mAccountMngr;
	}

}
