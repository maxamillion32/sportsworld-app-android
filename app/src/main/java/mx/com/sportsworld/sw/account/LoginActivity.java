/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

package mx.com.sportsworld.sw.account;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.GuestProfileActivity;
import mx.com.sportsworld.sw.activity.MemberProfileActivity;
import mx.com.sportsworld.sw.activity.PoliciesActivity;
import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.pojo.DevicePojo;
import mx.com.sportsworld.sw.pojo.UserPojo;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.utils.ConnectionUtils;
import mx.com.sportsworld.sw.web.task.CheckUpdateTask;
import mx.com.sportsworld.sw.web.task.LoginTask;

import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

// TODO: Auto-generated Javadoc

/**
 * The Class LoginActivity.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LoginActivity extends SherlockFragmentActivity implements
		View.OnClickListener {

	/** The Constant GOOGLE_PLAY_QUERY_WEB_BROWSER. */
	private static final String GOOGLE_PLAY_QUERY_WEB_BROWSER = "Web browser";
	
	/** The Constant URL_FORGOT_PASSWORD. */
	private static final String URL_FORGOT_PASSWORD = "https://socios.sportsworld.com.mx/user/reiniciarPassword";
	
	/** The Constant URL_REGISTER. */
	private static final String URL_REGISTER = "https://atletas.upster.com.mx/usuario/registroApp";
	
	/** The Constant STATE_MEMBER_LOG_IN_IN_PROGRESS. */
	private static final String STATE_MEMBER_LOG_IN_IN_PROGRESS = "member_login_in_progress";
	
	/** The Constant STATE_GUEST_LOG_IN_IN_PROGRESS. */
	private static final String STATE_GUEST_LOG_IN_IN_PROGRESS = "guest_login_in_progress";
	
	/** The m dtt username. */
	private EditText mDttUsername;
	
	/** The m dtt password. */
	private EditText mDttPassword;
	
	/** The m guest log in task. */
	private GuestLogInTask mGuestLogInTask;
	
	/** The m loggin in. */
	private View mLogginIn;
	
	/** The m log in content. */
	private View mLogInContent;
	
	/** The m member log in in progress. */
	private boolean mMemberLogInInProgress;
	
	/** The m guest log in in progress. */
	private boolean mGuestLogInInProgress;
	
	/** The m facebook ui helper. */
	private UiLifecycleHelper mFacebookUiHelper;
	
	/** The login task. */
	private LoginTask loginTask;
	
	/** The start acitivty. */
	private boolean startAcitivty = true;

	/** The m facebook session callback. */
	private Session.StatusCallback mFacebookSessionCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {

			if(exception != null)
	        {
	            // Handle fail case here.
				Toast.makeText(getApplicationContext(),
						"Su conexión de red no permite el acceso a Facebook",
						Toast.LENGTH_SHORT).show();
	            return;
	        }

	        // If session is just opened...
	        if(state == SessionState.OPENED)
	        {
	            // Handle success case here.
	        	onSessionStateChange(session, state, exception);

	        }
		}
	};

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		hasFace();
		setContentView(R.layout.activity_login);

		mFacebookUiHelper = new UiLifecycleHelper(
				LoginActivity.this /* context */, mFacebookSessionCallback);
		mFacebookUiHelper.onCreate(savedInstanceState);

		final ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(R.string.action_bar_title_login);
		actionBar.setDisplayShowHomeEnabled(false);

		mDttUsername = (EditText) findViewById(R.id.dtt_username);
		mDttPassword = (EditText) findViewById(R.id.dtt_password);
		mLogInContent = findViewById(R.id.log_in_content);
		mLogginIn = findViewById(R.id.logging_in);
		LoginButton authButton = (LoginButton) findViewById(R.id.btn_facebook_connect);
		authButton.setReadPermissions(Arrays.asList("user_likes",
				"user_status", "user_location", "user_birthday"));

		checkUpdate();

		final Button btnLogIn = (Button) findViewById(R.id.btn_log_in);
		final Button btnRestoreUser = (Button) findViewById(R.id.btnRestoreUser);
		//final Button btnRememberMyPassword = (Button) findViewById(R.id.btn_remember_my_password);
		final Button btnRegister = (Button) findViewById(R.id.btn_register);

		btnLogIn.setOnClickListener(this);

		//btnRememberMyPassword.setOnClickListener(this);
		btnRestoreUser.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Planer_Reg.ttf");
		TextView lblText = (TextView) findViewById(R.id.lblTitulo);
		TextView textView = (TextView) findViewById(R.id.textView);
		TextView textView7 = (TextView) findViewById(R.id.textView7);
		TextView txtRecordatorio = (TextView) findViewById(R.id.txtRecordatorio);
		lblText.setTypeface(font);
		textView.setTypeface(font);
		textView7.setTypeface(font);
		//btnRememberMyPassword.setTypeface(font);
		btnLogIn.setTypeface(font);
		btnRegister.setTypeface(font);
		txtRecordatorio.setTypeface(font);
		mDttUsername.setTypeface(font);
		if (savedInstanceState == null) {
			return;
		}

		mMemberLogInInProgress = savedInstanceState
				.getBoolean(STATE_MEMBER_LOG_IN_IN_PROGRESS);

		mGuestLogInInProgress = savedInstanceState
				.getBoolean(STATE_GUEST_LOG_IN_IN_PROGRESS);


	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mFacebookUiHelper != null)
			mFacebookUiHelper.onDestroy();

		final GuestLogInTask guestLogInTask = mGuestLogInTask;
		if (guestLogInTask != null
				&& guestLogInTask.getStatus() == AsyncTask.Status.RUNNING) {
			guestLogInTask.cancel(true);
			mGuestLogInTask = null;
		}
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		if (mFacebookUiHelper != null)
			mFacebookUiHelper.onPause();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mFacebookUiHelper.onResume();
		// mResumed = true;

		final SportsWorldAccountManager accoounManager = new SportsWorldAccountManager(
				this /* context */);
		final boolean loggedInAsMember = accoounManager.isLoggedInAsMember();
		if (loggedInAsMember) {
			onSuccessfulMemberLogIn();

			return;
		} else if (mMemberLogInInProgress) {
			mayLoginAsMember();
			return;
		}

		final boolean loggedInAsGuest = accoounManager.isLoggedInAsGuest();
		if (loggedInAsGuest) {
			onSuccessfulGuestLogIn();
			return;
		} else if (mGuestLogInInProgress) {
			final Session session = Session.getActiveSession();
			if (session != null && session.isOpened()) {
				showOnLoggingProgressBar(true /* show */, false /* animate */);
				makeMeRequest(session);
			}
			return;
		}

		final boolean loggedInAsAnonymous = accoounManager
				.isLoggedAsAnonymous();
		if (loggedInAsAnonymous) {
			onSuccessfulAnonymousLogin();
			return;
		}

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub

		super.onStart();
	}

	/**
	 * Checks for face.
	 */
	public void hasFace() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"mx.com.sportsworld.sw", PackageManager.GET_SIGNATURES);

			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.i("KeyHash:",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (Exception ex) {
			Log.i("KeyHash:", ex.toString());
		}
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mFacebookUiHelper != null)
			mFacebookUiHelper.onSaveInstanceState(outState);

		final GuestLogInTask guestLogInTask = mGuestLogInTask;
		mGuestLogInInProgress = ((guestLogInTask != null) && (guestLogInTask
				.getStatus() != AsyncTask.Status.FINISHED));
		if (mGuestLogInInProgress) {
			guestLogInTask.cancel(true);
			outState.putBoolean(STATE_GUEST_LOG_IN_IN_PROGRESS, true);
			mGuestLogInTask = null;
		}

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

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.btn_log_in:
			mayLoginAsMember();
			break;

			case R.id.btnRestoreUser:
				loadRestoreUser();
				break;

		/*case R.id.btn_remember_my_password:
			openWebBrowser(URL_FORGOT_PASSWORD);
			break;*/

		case R.id.btn_register:
			//openWebView();
			loadCreateUser();
			break;

		default:
			throw new UnsupportedOperationException("Unsupported operation");

		}

	}

	private void loadRestoreUser() {
		final Intent RestoreUser = new Intent(
				LoginActivity.this /* context */,
				RestoreUserActivity.class);

		startActivity(RestoreUser);
	}

	private void loadCreateUser(){

		final Intent CreateUser = new Intent(
				LoginActivity.this /* context */,
				CreateUserActivity.class);

		startActivity(CreateUser);

	}

	private void openWebView()
	{
		final Intent openWeb = new Intent(
				LoginActivity.this /* context */,
				RegisterActivity.class);

		startActivity(openWeb);
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
		final Intent register = makeWebPageIntent(url);
		if (isIntentAvailable(register)) {
			startActivity(register);
		} else {
			final Intent googlePlay = makeGooglePlayQueryIntent(GOOGLE_PLAY_QUERY_WEB_BROWSER);
			if (isIntentAvailable(googlePlay)) {
				startActivity(googlePlay);
			}
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.error_no_web_browser),
					Toast.LENGTH_SHORT).show();
		}
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
	 * May login as member.
	 */
	private void mayLoginAsMember() {
		final String username = mDttUsername.getText().toString().trim().toLowerCase();
		final String password = mDttPassword.getText().toString().trim();

		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			Toast.makeText(
					getApplicationContext(),
					getResources().getString(
							R.string.error_empty_password_username),
					Toast.LENGTH_SHORT).show();

			return;
		}

		if (ConnectionUtils.isNetworkAvailable(this /* context */)) {
			if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.error_connection),
						Toast.LENGTH_SHORT).show();
				return;
			}

			showOnLoggingProgressBar(true, true);

			loginTask = new LoginTask(new ResponseInterface() {

				@Override
				public void onResultResponse(Object obj) {
					UserPojo usrPojo = (UserPojo) obj;
					if (usrPojo != null) {
						if (!usrPojo.isStatus()
								&& usrPojo.getMessage().equals("TimeOut")) {
							Toast.makeText(
									getApplicationContext(),
									getResources().getString(
											R.string.error_connection_server),
									Toast.LENGTH_SHORT).show();
						} else {
							if (usrPojo.isStatus()
									&& usrPojo.getMessage().equals(
											"User accepted")) {
								onSuccessfulMemberLogIn();
								SportsWorldPreferences.setPassword(
										LoginActivity.this, password);
								SportsWorldPreferences.setUsername(
										LoginActivity.this, username);

							} else
								Toast.makeText(
										getApplicationContext(),
										getResources()
												.getString(
														R.string.error_invalid_username_password),
										Toast.LENGTH_SHORT).show();

						}
					} else {
						Toast.makeText(
								getApplicationContext(),
								getResources().getString(
										R.string.error_connection_server),
								Toast.LENGTH_SHORT).show();
					}
					showOnLoggingProgressBar(false, true);

				}
			});

			LoginTask.mContext = this;
			UserPojo pojo = new UserPojo();

			pojo.setUsername(username);
			pojo.setPassword(password);

			loginTask.execute(pojo);
		}
	}

	/**
	 * Show on logging progress bar.
	 *
	 * @param show the show
	 * @param animate the animate
	 */
	public void showOnLoggingProgressBar(boolean show, boolean animate) {
		if (show) {
			if (animate) {
				mLogInContent.startAnimation(AnimationUtils.loadAnimation(
						this /* context */, android.R.anim.fade_out));
				mLogginIn.startAnimation(AnimationUtils.loadAnimation(
						this /* context */, android.R.anim.fade_in));
			} else {
				mLogInContent.clearAnimation();
				mLogginIn.clearAnimation();
			}
			mLogginIn.setVisibility(View.VISIBLE);
			mLogInContent.setVisibility(View.GONE);
		} else {
			if (animate) {
				mLogInContent.startAnimation(AnimationUtils.loadAnimation(
						this /* context */, android.R.anim.fade_in));
				mLogginIn.startAnimation(AnimationUtils.loadAnimation(
						this /* context */, android.R.anim.fade_out));
			} else {
				mLogInContent.clearAnimation();
				mLogginIn.clearAnimation();
			}
			mLogginIn.setVisibility(View.GONE);
			mLogInContent.setVisibility(View.VISIBLE);
		}
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
		if (exception == null) {
			if (Session.getActiveSession() == null)
				session = Session
						.openActiveSessionFromCache(getApplicationContext());
			if (state != null) {
				if (state.isOpened()) {
					Log.i("LogIron", "Logged in...");
					makeMeRequest(session);
				} else if (state.isClosed()) {
					Log.i("LogIron", "Logged out...");
				} else if (state.name().equals("OPENING")) {
					// makeMeRequest(session);
				}
				mFacebookUiHelper.onResume();
			}
		}
	}

	/**
	 * Make me request.
	 *
	 * @param session the session
	 */
	private void makeMeRequest(final Session session) {

		final Request request = Request.newMeRequest(session,
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						// If the response is successful
						if (session != null) {
							if (user != null) {
								if (startAcitivty) {
									mGuestLogInTask = new GuestLogInTask(
											LoginActivity.this /* activity */);
									mGuestLogInTask.execute(user);
									startAcitivty = false;
								}
							}
						}
						if (response.getError() != null) {
							if (session != null) {
								session.closeAndClearTokenInformation();
							}
						}
					}
				});
		request.executeAsync();
	}

	/**
	 * On successful member log in.
	 */
	public void onSuccessfulMemberLogIn() {
		final Intent memberProfile = new Intent(this /* context */,
				MemberProfileActivity.class);
		startActivity(memberProfile);
		finish();
	}

	/**
	 * On successful guest log in.
	 */
	public void onSuccessfulGuestLogIn() {
		final Intent guestProfile = new Intent(this /* context */,
				GuestProfileActivity.class);
		guestProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		guestProfile.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(guestProfile);
		finish();
	}

	/**
	 * On successful anonymous login.
	 */
	private void onSuccessfulAnonymousLogin() {
		final Intent guestProfile = new Intent(this /* context */,
				GuestProfileActivity.class);
		startActivity(guestProfile);
		finish();
	}

	/**
	 * The Class GuestLogInTask.
	 */
	private static class GuestLogInTask extends
			AsyncTask<GraphUser, Void, Boolean> {

		/** The m activity weak ref. */
		private final WeakReference<LoginActivity> mActivityWeakRef;

		/**
		 * Instantiates a new guest log in task.
		 *
		 * @param activity the activity
		 */
		public GuestLogInTask(LoginActivity activity) {
			mActivityWeakRef = new WeakReference<LoginActivity>(activity);
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			final LoginActivity activity = mActivityWeakRef.get();
			if (activity == null) {
				return;
			}

			activity.showOnLoggingProgressBar(true /* show */, false /* animate */);
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected Boolean doInBackground(GraphUser... params) {

			final Activity activity = mActivityWeakRef.get();
			if (activity == null) {
				return false;
			}

			final SportsWorldAccountManager accountMgr = new SportsWorldAccountManager(
					activity /* context */);
			accountMgr.logInAsGuest(params[0]);
			return true;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Boolean success) {
			final LoginActivity activity = mActivityWeakRef.get();
			if (activity == null) {
				return;
			}

			if (success) {
				activity.onSuccessfulGuestLogIn();
			} else {
				showOnLoggingProgressBar(false);

				Toast.makeText(
						activity,
						activity.getResources().getString(
								R.string.error_save_guest_on_db),
						Toast.LENGTH_SHORT).show();

				final Session session = Session.getActiveSession();
				if (session != null) {
					session.close();
				}
			}

		}

		/**
		 * Show on logging progress bar.
		 *
		 * @param show the show
		 */
		private void showOnLoggingProgressBar(boolean show) {

			final LoginActivity activity = mActivityWeakRef.get();
			if (activity == null) {
				return;
			}

			activity.showOnLoggingProgressBar(show, true /* animate */);
		}

	}

	/**
	 * Test login.
	 */
	public void testLogin() {
		mDttUsername.setText(" gerardo.daza@sportsworld.com.mx");
		mDttPassword.setText("1948");

	}

	/**
	 * Check update.
	 */
	public void checkUpdate() {

		CheckUpdateTask newVersionTask = new CheckUpdateTask(
				new ResponseInterface() {

					@Override
					public void onResultResponse(Object obj) {
						DevicePojo result = (DevicePojo) obj;
						if (result.isStatus())
							updateNotification();
					}
				});

		CheckUpdateTask.mContext = this;

		try {
			DevicePojo devPojo = new DevicePojo();
			devPojo.setDevice("android");
			devPojo.setVersion(""
					+ getPackageManager().getPackageInfo(
							"mx.com.sportsworld.sw", 0).versionCode);
			newVersionTask.execute(devPojo);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.i("LogIron", e.toString());
		}
	}

	/**
	 * Update notification.
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void updateNotification() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("market://search?q=pname:com.sportsworld.android"));

		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;

		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			try {
				Notification noti = new Notification.Builder(this)
						.setContentTitle("Hay una nueva versión disponible.")
						.setContentText("Ir a la tienda.")
						.setSmallIcon(R.drawable.icon)
						.setContentIntent(pIntent).build();

				// Hide the notification after its selected
				noti.flags |= Notification.FLAG_AUTO_CANCEL;

				notificationManager.notify(0, noti);
			} catch (Exception ex) {

			}
		} else {
			Notification notif = new Notification(R.drawable.icon,
					"Hay una nueva versión disponible.",
					System.currentTimeMillis());
			notif.flags |= Notification.FLAG_AUTO_CANCEL;
			notif.setLatestEventInfo(this, "Hay una nueva versión disponible.",
					"Ir a la tienda.", pIntent);
			notificationManager.notify(0, notif);
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d("Prueba ", "prueba " + keyCode);
		Log.d("Prueba ", "prueba " + KeyEvent.KEYCODE_BACK);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(new Intent(this, PoliciesActivity.class));
		}
		return super.onKeyDown(keyCode, event);
	}

}
