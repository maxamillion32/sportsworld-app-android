package mx.com.sportsworld.sw.activity;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.Session;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.app.AuthSherlockFragmentActivity;
import mx.com.sportsworld.sw.fragment.DashboardFragment;
import mx.com.sportsworld.sw.fragment.DashboardFragment.OnShortcutClickListener;
import mx.com.sportsworld.sw.fragment.SportsWorldSettingsFragment;
import mx.com.sportsworld.sw.fragment.SportsWorldSettingsFragment.OnSettingsClickListener;
import mx.com.sportsworld.sw.fragment.dialog.ProgressDialogFragment;
import mx.com.sportsworld.sw.pojo.MainPojo;
import mx.com.sportsworld.sw.pojo.RoutinePojo;
import mx.com.sportsworld.sw.pojo.UserPojo;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.utils.ConnectionUtils;
import mx.com.sportsworld.sw.web.task.CheckRoutineTask;
import mx.com.sportsworld.sw.web.task.CloseSessionTask;
import mx.com.sportsworld.sw.web.task.LoginTask;

// TODO: Auto-generated Javadoc

/**
 * The Class MainboardActivity.
 */
public class MainboardActivity extends AuthSherlockFragmentActivity implements
		OnShortcutClickListener, OnSettingsClickListener {

	/** The padding. */
	public static int padding = 20;
	
	/** The progress. */
	public static ProgressDialogFragment progress;
	
	/** The dialgo fragment. */
	Fragment dialgoFragment;
	
	/** The login task. */
	LoginTask loginTask;
	
	/** The pressed routine. */
	public boolean pressedRoutine = true;

	private WindowManager mWindowManager;


	/* (non-Javadoc)
	 * @see com.sportsworld.android.app.AuthSherlockFragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Display getOrient = getWindowManager().getDefaultDisplay();
		if (getOrient.getRotation() == Surface.ROTATION_0
				|| getOrient.getRotation() == Surface.ROTATION_180)
			padding = 20;
		else
			padding = 40;

		setContentView(R.layout.activity_mainboard);
		final PagerAdapter pagerAdpater = new MainboardPagerAdpater(
				getSupportFragmentManager());
		final ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(pagerAdpater);
		pager.setOffscreenPageLimit(pagerAdpater.getCount());
		pager.setClipChildren(false);

		showFragmentsDialog(true);
	}



	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		SportsWorldPreferences.setFirstTime(getApplicationContext(), false);
		super.onStart();

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		showFragmentsDialog(true);
		pressedRoutine = true;
		super.onResume();
	}

	/* 
	 * Opens Club Activity
	 */
	/* (non-Javadoc)
	 * @see com.sportsworld.android.fragment.DashboardFragment.OnShortcutClickListener#OnGoToClubsClick()
	 */
	@Override
	public void OnGoToClubsClick() {
		if (isNetworkAvailable()) {
			final Intent goToClubs = new Intent(
					MainboardActivity.this /* context */,
					BranchTypesListActivity.class);
			startActivity(goToClubs);
		}
	}

	/* 
	 * Opens clases activity
	 */
	/* (non-Javadoc)
	 * @see com.sportsworld.android.fragment.DashboardFragment.OnShortcutClickListener#OnGoToClassesClick()
	 */
	@Override
	public void OnGoToClassesClick() {
		if (isNetworkAvailable()) {
			final Intent goToDummy = new Intent(this /* context */,
					DummyActivity.class);
			startActivity(goToDummy);
		}
	}
	@Override
	public void OnGoToUpster() {
			final Intent goToDummy = new Intent(this /* context */,
					ViewPagerActivity.class);
			startActivity(goToDummy);

	}
	/**
	 * Checks if is network available.
	 *
	 * @return true, if is network available
	 */
	public boolean isNetworkAvailable() {
		boolean isConnection = ConnectionUtils
				.isNetworkAvailable(getApplicationContext());
		if (!isConnection)
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.error_connection),
					Toast.LENGTH_SHORT).show();

		return isConnection;

	}

	/*
	 * Opens rutine Activity
	 */
	/* (non-Javadoc)
	 * @see com.sportsworld.android.fragment.DashboardFragment.OnShortcutClickListener#OnGoToMyRoutineClick(long)
	 */
	@Override
	public void OnGoToMyRoutineClick(long routineId) {
		if (isNetworkAvailable()) {
			if (pressedRoutine) {
				pressedRoutine = false;

				int age = Integer.parseInt(SportsWorldPreferences
						.getGuestAge(this));
				if (age >= 18 || isMember()) {

					showFragmentsDialog(false);
					if (isMember()) {

						CheckRoutineTask routine = new CheckRoutineTask(
								new ResponseInterface() {

									@Override
									public void onResultResponse(Object obj) {
										// TODO Auto-generated method stub

										RoutinePojo result = (RoutinePojo) obj;
										Intent goToRoutine;
										if (progress != null)
											progress.dismissAllowingStateLoss();

										if (result.isStatus()) {
											pressedRoutine = true;
											long updateId = Long
													.parseLong(result
															.getIdRoutine());

											SportsWorldPreferences
													.setRoutineId(
															getApplicationContext(),
															updateId);

											if (updateId == 0) {
												goToRoutine = new Intent(
														MainboardActivity.this /* context */,
														RoutineSelectorActivity.class);

											} else {
												goToRoutine = new Intent(
														MainboardActivity.this /* context */,
														RoutineActivity.class);
												goToRoutine
														.putExtra(
																RoutineActivity.EXTRA_ROUTINE_ID,
																updateId);
												goToRoutine.putExtra(
														"haveRoutine", true);

											}

											startActivity(goToRoutine);
										} else {

											if (result.getMessage().equals(
													"TimeOut")) {
												Toast.makeText(
														getApplicationContext(),
														getResources()
																.getString(
																		R.string.error_connection_server),
														Toast.LENGTH_SHORT)
														.show();
											} else {
												SportsWorldPreferences
														.setRoutineId(
																getApplicationContext(),
																0);
												goToRoutine = new Intent(
														MainboardActivity.this /*
																				 * context
																				 */,
														RoutineSelectorActivity.class);

												startActivity(goToRoutine);

											}
											pressedRoutine = true;
										}
									}
								});
						CheckRoutineTask.mContext = getApplicationContext();
						routine.execute(SportsWorldPreferences
								.getCurrentUserId(getApplicationContext()));
					} else {

						if (SportsWorldPreferences.getGuestWeight(
								getApplicationContext()).equals("0")
								|| SportsWorldPreferences.getGuestHeigh(
										getApplicationContext()).equals("0")) {
							Toast.makeText(
									getApplicationContext(),
									getResources().getString(
											R.string.error_guest_no_value),
									Toast.LENGTH_SHORT).show();

						}
						if (progress != null)
							progress.dismissAllowingStateLoss();

						pressedRoutine = true;
						Intent goToRoutine = new Intent(
								MainboardActivity.this /* context */,
								RoutineSelectorActivity.class);
						startActivity(goToRoutine);
					}

				}
			} else {

				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.error_alert_age),
						Toast.LENGTH_SHORT).show();
				pressedRoutine = true;
			}
		}
	}

	/* 
	 * Opens Loyalty Activity
	 */
	/* (non-Javadoc)
	 * @see com.sportsworld.android.fragment.DashboardFragment.OnShortcutClickListener#OnGoToLoyaltyProgramClick()
	 */
	@Override
	public void OnGoToLoyaltyProgramClick() {
		// TODO Got o Loyalty program Activity
		if (isNetworkAvailable()) {
			if (isMember()) {
				Intent goToLoyalty = new Intent(
						MainboardActivity.this /* context */,
						LoyaltyActivity.class);
				startActivity(goToLoyalty);
			} else
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.error_not_a_member),
						Toast.LENGTH_SHORT).show();
		}
	}

	/* 
	 * Opens News Activity
	 * */
	/* (non-Javadoc)
	 * @see com.sportsworld.android.fragment.DashboardFragment.OnShortcutClickListener#OnGoToNewsClick()
	 */
	@Override
	public void OnGoToNewsClick() {
		if (isNetworkAvailable()) {
			final Intent news = new Intent(this /* context */,
					NewsListActivity.class);
			startActivity(news);
		}
	}

	/* 
	 * Opens Virtual Activity
	 * */
	/* (non-Javadoc)
	 * @see com.sportsworld.android.fragment.DashboardFragment.OnShortcutClickListener#OnGoToVirtualTourClick()
	 */
	@Override
	public void OnGoToVirtualTourClick() {
		// TODO Go to Virtual Tour activity
	}

	/*
	 * Open Guest profile Activity
	 * */
	/* (non-Javadoc)
	 * @see com.sportsworld.android.fragment.SportsWorldSettingsFragment.OnSettingsClickListener#onEditProfileClick()
	 */
	@Override
	public void onEditProfileClick() {
		final SportsWorldAccountManager accountMngr = getAccountManager();

		Class<?> clazz = null;
		if (accountMngr.isLoggedInAsMember()) {
			logInTask();
			// clazz = MemberProfileActivity.class;

		} else if (accountMngr.isLoggedInAsGuest()) {
			clazz = GuestProfileActivity.class;
		}

		if (clazz != null) {
			final Intent editProfile = new Intent(this /* context */, clazz);
			startActivity(editProfile);
		}
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.fragment.SportsWorldSettingsFragment.OnSettingsClickListener#onEnableSocialNetworksClick()
	 */
	@Override
	public void onEnableSocialNetworksClick() {

		final Intent enableSocialNetworks = new Intent(this /* context */,
				SocialNetworksActivity.class);
		startActivity(enableSocialNetworks);
	}

	/* 
	 * Logout 
	 * */
	/* (non-Javadoc)
	 * @see com.sportsworld.android.fragment.SportsWorldSettingsFragment.OnSettingsClickListener#onLogOutClick()
	 */
	@Override
	public void onLogOutClick() {

		if (!isNetworkAvailable()) {

			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.error_logout),
					Toast.LENGTH_SHORT).show();
			return;
		}

		showFragmentsDialog(false);

		if (!isMember()) {

			new Handler().post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (progress != null)
						progress.dismissAllowingStateLoss();
				}
			});

			exitApp();
		} else {
			CloseSessionTask closeTask = new CloseSessionTask(
					new ResponseInterface() {

						@Override
						public void onResultResponse(Object obj) {
							// TODO Auto-generated method stub

							new Handler().post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									progress.dismissAllowingStateLoss();
								}
							});

							MainPojo resPojo = (MainPojo) obj;
							if (resPojo.getMessage() != null) {
								if (resPojo.isStatus()) {
									exitApp();

								} else {

									Toast.makeText(
											getApplicationContext(),
											getResources().getString(
													R.string.error_logout),
											Toast.LENGTH_SHORT).show();
								}
							}

						}
					});

			CloseSessionTask.mContext = this;
			closeTask.execute(SportsWorldPreferences.getAuthToken(this));

		}
	}

	/**
	 * The Class MainboardPagerAdpater.
	 */
	private static class MainboardPagerAdpater extends FragmentPagerAdapter {

		/** The Constant FRAGMENTS_COUNT. */
		private static final int FRAGMENTS_COUNT = 2;

		/** The Constant FRAGMENT_POSITION_DASHBOARD. */
		private static final int FRAGMENT_POSITION_DASHBOARD = 0;

		/** The Constant FRAGMENT_POSITION_SETTINGS. */
		private static final int FRAGMENT_POSITION_SETTINGS = 1;

		/**
		 * Instantiates a new mainboard pager adpater.
		 *
		 * @param fm the fm
		 */
		public MainboardPagerAdpater(FragmentManager fm) {
			super(fm);
		}

		/* (non-Javadoc)
		 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
		 */
		@Override
		public Fragment getItem(int position) {

			switch (position) {
			case FRAGMENT_POSITION_DASHBOARD:
				return DashboardFragment.newInstance();
			case FRAGMENT_POSITION_SETTINGS:
				return SportsWorldSettingsFragment.newInstance();
			default:
				throw new UnsupportedOperationException();
			}

		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#getCount()
		 */
		@Override
		public int getCount() {
			return FRAGMENTS_COUNT;
		}
	}



	/**
	 * Exit app.
	 */
	public void exitApp() {

		Session session = Session.getActiveSession();

		if (session == null)// code panda
			session = Session
					.openActiveSessionFromCache(MainboardActivity.this);

		if (session != null) {
			session.closeAndClearTokenInformation();
		}
		SportsWorldPreferences.clearAllPreferences(this);

		Intent startMain = new Intent(this /* context */,
				PoliciesActivity.class);
		startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		startActivity(startMain);
		finish();

	}

	/**
	 * Show fragments dialog.
	 *
	 * @param savedInstanceState the saved instance state
	 */
	public void showFragmentsDialog(boolean savedInstanceState) {
		dialgoFragment = getSupportFragmentManager().findFragmentByTag(
				ProgressDialogFragment.progressDialogTag);

		if (savedInstanceState == true) {
			dialgoFragment = getSupportFragmentManager().findFragmentByTag(
					ProgressDialogFragment.progressDialogTag);
			progress = (ProgressDialogFragment) dialgoFragment;

		} else {

			if (dialgoFragment != null) {
				getSupportFragmentManager().beginTransaction()
						.remove(dialgoFragment).commit();
			}

			progress = ProgressDialogFragment
					.newInstance(MainboardActivity.this);
			progress.show(getSupportFragmentManager(),
					ProgressDialogFragment.progressDialogTag);
		}

	}

	/**
	 * Log in task.
	 */
	public void logInTask() {

		if (!isNetworkAvailable()) {
			final Intent editProfile = new Intent(this /* context */,
					MemberProfileActivity.class);
			startActivity(editProfile);
			return;
		}
		showFragmentsDialog(false);
		loginTask = new LoginTask(new ResponseInterface() {

			@Override
			public void onResultResponse(Object obj) {
				progress.dismissAllowingStateLoss();

				final Intent editProfile = new Intent(
						MainboardActivity.this /* context */,
						MemberProfileActivity.class);
				startActivity(editProfile);

			}
		});
		loginTask.clarData = false;
		LoginTask.mContext = this;
		UserPojo pojo = new UserPojo();
		pojo.setUsername(SportsWorldPreferences.getUsername(this));
		pojo.setPassword(SportsWorldPreferences.getPassword(this));
		loginTask.execute(pojo);

	}

	/**
	 * Checks if is member.
	 *
	 * @return true, if is member
	 */
	public boolean isMember() {
		final SportsWorldAccountManager accountMngr = new SportsWorldAccountManager(
				MainboardActivity.this /* context */);
		return accountMngr.isLoggedInAsMember();

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		return super.onKeyDown(keyCode, event);
	}

}
