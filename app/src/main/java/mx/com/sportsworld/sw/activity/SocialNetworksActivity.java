package mx.com.sportsworld.sw.activity;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.events.TwitterResponseInterface;
import mx.com.sportsworld.sw.app.AuthSherlockFragmentActivity;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.utils.ConnectionUtils;
import mx.com.sportsworld.sw.utils.SocialKeys;
import mx.com.sportsworld.sw.widget.TwitterDialogView;

// TODO: Auto-generated Javadoc

/**
 * The Class SocialNetworksActivity.
 */
public class SocialNetworksActivity extends AuthSherlockFragmentActivity
		implements View.OnClickListener, TwitterResponseInterface {

	/** The Constant GOOGLE_PLAY_QUERY_WEB_BROWSER. */
	private static final String GOOGLE_PLAY_QUERY_WEB_BROWSER = "Web browser";
	
	/** The Constant URL_LIKE. */
	private static final String URL_LIKE = "https://www.facebook.com/UpsterMex";
	
	/** The Constant URL_FOLLOW. */
	private static final String URL_FOLLOW = "https://twitter.com/upstermex";
	
	/** The m facebook ui helper. */
	private UiLifecycleHelper mFacebookUiHelper = null;

	// Twitter
	/** The twitter. */
	private static Twitter twitter;
	
	/** The request token. */
	private static RequestToken requestToken;

	/** The btn twitter. */
	Button btnTwitter = null;
	
	/** The btn face. */
	LoginButton btnFace = null;

	/** The alpha face. */
	float alphaFace = (float) 0.5;
	
	/** The Constant PERMISSION_PUBLISH_STREAM. */
	private static final String PERMISSION_PUBLISH_STREAM = "publish_stream";
	
	/** The permissions. */
	public static List<String> PERMISSIONS = null;

	/** The twitter consumer key. */
	static String TWITTER_CONSUMER_KEY = SocialKeys.TW_OAUTH_CONSUMER_KEY;
	
	/** The twitter consumer secret. */
	static String TWITTER_CONSUMER_SECRET = SocialKeys.TW_OAUTH_CONSUMER_SECRET;

	// Preference Constants
	/** The preference name. */
	static String PREFERENCE_NAME = "twitter_oauth";
	
	/** The Constant PREF_KEY_OAUTH_TOKEN. */
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	
	/** The Constant PREF_KEY_OAUTH_SECRET. */
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	
	/** The Constant PREF_KEY_TWITTER_LOGIN. */
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

	/** The Constant TWITTER_CALLBACK_URL. */
	static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

	// Twitter oauth urls
	/** The Constant URL_TWITTER_AUTH. */
	static final String URL_TWITTER_AUTH = "auth_url";
	
	/** The Constant URL_TWITTER_OAUTH_VERIFIER. */
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	
	/** The Constant URL_TWITTER_OAUTH_TOKEN. */
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

	/** The twitter dialog. */
	private TwitterDialogView twitterDialog;

	/** The m facebook session callback. */
	private Session.StatusCallback mFacebookSessionCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {

			if(exception != null)
	        {

	            // Handle fail case here.
				/*Toast.makeText(getApplicationContext(),
						"Su conexi�n de red no permite el acceso a Facebook",
						Toast.LENGTH_SHORT).show();*/
	            return;
	        }
			onSessionStateChange(session, state, exception);
		}
	};

	/* (non-Javadoc)
	 * @see com.sportsworld.android.app.AuthSherlockFragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (isFinishing()) {
			return;
		}

		//getSupportActionBar().setTitle(R.string.social_networks);

		setContentView(R.layout.activity_social_networks);

		btnFace = (LoginButton) findViewById(R.id.btn_facebook_connect);
		if (SportsWorldPreferences.getUsername(getApplicationContext()) == null) {
			btnFace.setEnabled(false);
			AlphaAnimation alpha = new AlphaAnimation(0.7F, 0.7F);
			alpha.setDuration(0);
			// Make animation instant
			alpha.setFillAfter(true);
			// Tell it to persist after the animation ends
			btnFace.startAnimation(alpha);
		}

		findViewById(R.id.btn_like).setOnClickListener(this);
		btnTwitter = (Button) findViewById(R.id.btn_twitter_log_in);

		if (SportsWorldPreferences.isLoggedTwitter(getApplicationContext()))
			btnTwitter.setVisibility(View.GONE);

		btnTwitter.setOnClickListener(this);
		findViewById(R.id.btn_follow).setOnClickListener(this);

		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}

		// TODO Arreglar face
		/*
		mFacebookUiHelper = new UiLifecycleHelper(this ,
				mFacebookSessionCallback);
		mFacebookUiHelper.onCreate(savedInstanceState);*/

		if (isTwitterLoggedInAlready()) {

			btnTwitter.setVisibility(View.GONE);
		}
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Planer_Reg.ttf");
		final Button btnTitulo = (Button) findViewById(R.id.lblTitulo1);
		final TextView lblTitlulo = (TextView) findViewById(R.id.lblTitulo);
		btnTitulo.setTypeface(font);
		lblTitlulo.setTypeface(font);
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mFacebookUiHelper != null)
			mFacebookUiHelper.onDestroy();
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		// mResumed = false;
		if (mFacebookUiHelper != null)
			mFacebookUiHelper.onPause();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (mFacebookUiHelper != null)
			mFacebookUiHelper.onResume();
		// mResumed = true;
		if (SportsWorldPreferences.isLoggedTwitter(getApplicationContext()))
			btnTwitter.setVisibility(View.GONE);

	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mFacebookUiHelper != null)
			mFacebookUiHelper.onSaveInstanceState(outState);

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

	/**
	 * On session state change.
	 *
	 * @param session the session
	 * @param state the state
	 * @param exception the exception
	 */
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {

		// if (exception != null) {
		if (state != null && exception == null) {
			if (state.isOpened()) {
				Log.i("LogIron", "Logged in...");
				if (!state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
					addRequest();
				}

			} else if (state.isClosed()) {
				Log.i("LogIron", "Logged out...");
				// }
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_like:

			openWebBrowser(URL_LIKE);
			break;
		case R.id.btn_twitter_log_in:
			if (isNetworkAvailable())
				//loginToTwitter();
				new Thread(new Runnable() {
					public void run() {
						Looper.prepare();
						loginToTwitter();
						Log.d("Loop", "mensaje" + Looper.myQueue());
						Looper.loop();
					}
				}).start();
			else
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.error_connection),
						Toast.LENGTH_SHORT).show();
			break;
		case R.id.btn_follow:
			openWebBrowser(URL_FOLLOW);
			loginToTwitter();
			break;
		default:
			throw new IllegalArgumentException("Unknow view: " + view.getId());
		}
	}

	/**
	 * Opens <code>url</code> on the external web browser. If there is no web
	 * browser installed, it attempts a query on Google Play. If there is not
	 * Google Play app installed, it just shows a Toast that urges the user to
	 * get an appropriate app.
	 *
	 * @param url the url
	 */
	public void openWebBrowser(String url) {
		if (isNetworkLocationProviderAvailable()) {
			final Intent register = makeWebPageIntent(url);
			if (isIntentAvailable(register)) {
				startActivity(register);
			} else {
				final Intent googlePlay = makeGooglePlayQueryIntent(GOOGLE_PLAY_QUERY_WEB_BROWSER);
				if (isIntentAvailable(googlePlay)) {
					startActivity(googlePlay);
				}
				Toast.makeText(
						getApplicationContext(),
						getResources().getString(R.string.error_no_web_browser),
						Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.error_connection),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Creates an Intent that launches the Google Play search activity.
	 *
	 * @param query the query
	 * @return Google Play Intent.
	 */
	public Intent makeGooglePlayQueryIntent(String query) {
		final Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market:search?q=" + query));
		intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
				| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		return intent;
	}

	/**
	 * Creates an Intent that launches the web browser.
	 *
	 * @param webPage the web page
	 * @return A web page Intent.
	 */
	public Intent makeWebPageIntent(String webPage) {
		if (!webPage.startsWith("http://") && !webPage.startsWith("https://")) {
			throw new IllegalArgumentException(
					"webPage should start with \"http://\" or \"https://\"");
		}

		final Intent intent = new Intent();
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(webPage));
		intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
				| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		return intent;
	}

	/**
	 * Indicates whether the specified intent can be used. If no suitable
	 * package is found, this method returns false.
	 *
	 * @param intent The Intent action to check for availability.
	 * @return True if an Intent with the specified action can be sent and
	 * responded to, false otherwise.
	 */
	public boolean isIntentAvailable(Intent intent) {
		final PackageManager packageManager = getPackageManager();
		final List<ResolveInfo> resolveInfo = packageManager
				.queryIntentActivities(intent,
						PackageManager.MATCH_DEFAULT_ONLY);
		return resolveInfo.size() > 0;
	}

	/**
	 * Checks if is network location provider available.
	 *
	 * @return true, if is network location provider available
	 */
	public boolean isNetworkLocationProviderAvailable() {
		final LocationManager locationMngr = (LocationManager) getApplicationContext()
				.getSystemService(Context.LOCATION_SERVICE);
		return locationMngr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
				&& ConnectionUtils
						.isNetworkAvailable(getApplicationContext() /* context */);

	}

	/**
	 * Adds the request.
	 */
	public void addRequest() {
		Session session = Session.getActiveSession();

		if (session == null)
			session = Session.openActiveSessionFromCache(this);

		if (session != null && session.isOpened()) {

			PERMISSIONS = Arrays.asList("publish_actions",
					PERMISSION_PUBLISH_STREAM);


			List<String> permissions = session.getPermissions();

			for(String perm : permissions) {
				Log.i("permiso", perm);
			}
			// Quitar
			List<String> tempPerm = Arrays.asList("publish_actions");

			if (!isSubsetOf(tempPerm, permissions)) {

				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						this, tempPerm);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}
		}
	}

	/**
	 * Checks if is subset of.
	 *
	 * @param subset the subset
	 * @param superset the superset
	 * @return true, if is subset of
	 */
	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if is network available.
	 *
	 * @return true, if is network available
	 */
	public boolean isNetworkAvailable() {
		return ConnectionUtils.isNetworkAvailable(getApplicationContext());
	}

	/**
	 * Login to twitter.
	 */
	private void loginToTwitter() {
		// Check if already logged in

		if (!isTwitterLoggedInAlready()) {
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
			builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
			Configuration configuration = builder.build();

			TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();

			try {

				requestToken = twitter
						.getOAuthRequestToken(TWITTER_CALLBACK_URL);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						twitterDialog = new TwitterDialogView(
								SocialNetworksActivity.this,
								requestToken.getAuthenticationURL(),
								SocialNetworksActivity.this);
						twitterDialog.show();
					}
				});

				// requestToken.getAuthenticationURL());
				// this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
				// .parse(requestToken.getAuthenticationURL())));
			} catch (Exception e) {

				//e.printStackTrace();
				//Log.e("Twitter Login Error", "> " + e.getMessage());
				/*Toast.makeText(getApplicationContext(),
						"Su conexi�n de red no permite el acceso a Twitter", Toast.LENGTH_LONG).show();*/
				
			}
		} else {
			// user already logged into twitter
			Toast.makeText(getApplicationContext(),
					"Already Logged into twitter", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Checks if is twitter logged in already.
	 *
	 * @return true, if is twitter logged in already
	 */
	private boolean isTwitterLoggedInAlready() {
		// return twitter login status from Shared Preferences
		return SportsWorldPreferences.isLoggedTwitter(getApplicationContext());
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			final Intent mainBoard = new Intent(this /* context */,
					MainboardActivity.class);
			mainBoard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			mainBoard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(mainBoard);
			FragmentManager manager = getSupportFragmentManager();
			manager.popBackStack();
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Show twitter d ialog.
	 *
	 * @param url the url
	 */
	public void showTwitterDIalog(String url) {

		Uri uri = Uri.parse(url);

		if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
			// oAuth verifier
			final String verifier = uri
					.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

			// Get the access token
			new Thread(new Runnable() {
				public void run() {
					AccessToken accessToken = null;
					try {
						accessToken = twitter.getOAuthAccessToken(requestToken,
								verifier);
						SportsWorldPreferences.setKeyOauthToken(
								getApplicationContext(), accessToken.getToken());
						SportsWorldPreferences.setKeyOauthSecret(
								getApplicationContext(),
								accessToken.getTokenSecret());
						SportsWorldPreferences.setLoggedTwitter(
								getApplicationContext(), true);

						Log.i("Twitter OAuth Token",
								"> " + accessToken.getToken());

						// Hide login button
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								btnTwitter.setVisibility(View.GONE);
							}
						});
					} catch (Exception e) {
						// Check log for login errors
						Log.e("Twitter Login Error", "> " + e.toString());
					}

				}
			}).start();

		}
	}

	/*
	 */
	/* (non-Javadoc)
	 * @see com.sportsworld.android.activity.events.TwitterResponseInterface#urlCallback(java.lang.String)
	 */
	@Override
	public void urlCallback(String url) {
		// TODO Auto-generated method stub
		showTwitterDIalog(url);
		twitterDialog.dismiss();
	}

}
