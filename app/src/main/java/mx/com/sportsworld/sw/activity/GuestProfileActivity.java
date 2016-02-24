package mx.com.sportsworld.sw.activity;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import java.util.Arrays;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
import mx.com.sportsworld.sw.app.LoadingDialogFragment;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.utils.GeneralUtils;

// TODO: Auto-generated Javadoc

/**
 * The Class GuestProfileActivity.
 */
public class GuestProfileActivity extends SherlockFragmentActivity implements
		View.OnClickListener, View.OnFocusChangeListener {

	/** The Constant METERS. */
	private static final String METERS = "m";
	
	/** The Constant KILO. */
	private static final String KILO = "Kg";
	
	/** The Constant ACTIVITY_TAG_MENOR_EDAD. */
	public static final String ACTIVITY_TAG_MENOR_EDAD = "act_menor_edad";
	
	/** The Constant FRAG_TAG_LOADING. */
	private static final String FRAG_TAG_LOADING = "frag_tag_loading";
	
	/** The m txt name. */
	private TextView mTxtName;
	
	/** The m txt gender. */
	private TextView mTxtGender;
	
	/** The m txt gender e. */
	private TextView mTxtGenderE;
	
	/** The m txt age. */
	private TextView mTxtAge;
	
	/** The m dtt age. */
	private EditText mDttAge;
	
	/** The m dtt height. */
	private EditText mDttHeight;
	
	/** The m dtt weight. */
	private EditText mDttWeight;
	
	/** The m btn show policies. */
	private Button mBtnShowPolicies;
	
	/** The m btn save profile. */
	private Button mBtnSaveProfile;
	
	/** The m prf avatar. */
	private ProfilePictureView mPrfAvatar;
	
	/** The m facebook ui helper. */
	private UiLifecycleHelper mFacebookUiHelper;
	
	/** The fist time. */
	private boolean fistTime;

	/** The m facebook session callback. */
	private Session.StatusCallback mFacebookSessionCallback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state,
				final Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guest_profile);

		mFacebookUiHelper = new UiLifecycleHelper(this /* context */,
				mFacebookSessionCallback);
		mFacebookUiHelper.onCreate(savedInstanceState);

		mPrfAvatar = (ProfilePictureView) findViewById(R.id.prf_avatar);
		if (mPrfAvatar != null)
			mPrfAvatar.setCropped(true);

		mBtnShowPolicies = (Button) findViewById(R.id.btn_show_policies);
		mBtnSaveProfile = (Button) findViewById(R.id.btn_save_profile);

		mBtnShowPolicies.setOnClickListener(this);
		mBtnSaveProfile.setOnClickListener(this);

		mTxtName = (TextView) findViewById(R.id.txt_name);
		mTxtGender = (TextView) findViewById(R.id.txt_gender);
		mTxtGender.setVisibility(View.VISIBLE);
		mTxtGenderE = (TextView) findViewById(R.id.fake_dtt_gender);
		mTxtAge = (TextView) findViewById(R.id.txt_age);
		mTxtAge.setText("Edad");

		mDttAge = (EditText) findViewById(R.id.edit_birth_profile);
		mDttAge.setInputType(InputType.TYPE_CLASS_NUMBER);
		mDttHeight = (EditText) findViewById(R.id.dtt_height);
		mDttWeight = (EditText) findViewById(R.id.dtt_weight);

		mDttWeight.setOnFocusChangeListener(this);
		mDttHeight.setOnFocusChangeListener(this);

		fistTime = SportsWorldPreferences.isFirstTime(getApplicationContext());

		if (fistTime)
			mBtnShowPolicies.setVisibility(View.GONE);
		completeGuestAccount();
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mFacebookUiHelper.onPause();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mFacebookUiHelper.onResume();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResumeFragments()
	 */
	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		mFacebookUiHelper.onResume();
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mFacebookUiHelper.onSaveInstanceState(outState);
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
		if ((session != null) && (session.isOpened())) {
			makeMeRequest(session);
		}
	}

	/**
	 * Make me request.
	 *
	 * @param session the session
	 */
	private void makeMeRequest(final Session session) {
		Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
				this, Arrays.asList("user_birthday"));

		session.requestNewReadPermissions(newPermissionsRequest);

		final Request request = Request.newMeRequest(session,
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						// If the response is successful
						if (session == Session.getActiveSession()) {
							if (user != null) {
								new AsyncTask<GraphUser, Void, Void>() {
									@Override
									protected Void doInBackground(
											GraphUser... params) {
										SportsWorldPreferences.setFirstTime(
												getApplicationContext(), false);
										final SportsWorldAccountManager accountMgr = new SportsWorldAccountManager(
												GuestProfileActivity.this);
										accountMgr.updateGuest(params[0]);

										return null;
									}
								}.execute(user);
							}
						}
						/*
						 * We won't do anything if there is an error. It's not
						 * that important since we still have info from the
						 * database and it is not critical data.
						 */
					}
				});
		request.executeAsync();
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.txt_age:
			break;
		case R.id.txt_height:
			break;
		case R.id.txt_weight:
			break;
		case R.id.btn_show_policies:
			showPolicies();
			break;
		case R.id.btn_save_profile:
			maySaveProfile();
			break;

		default:
			throw new UnsupportedOperationException();
		}

	}

	/**
	 * Show policies.
	 */
	private void showPolicies() {
		final Intent showPolicies = new Intent(this /* context */,
				PoliciesActivity.class);
		showPolicies.setAction(Intent.ACTION_VIEW);
		startActivity(showPolicies);
	}

	/**
	 * May save profile.
	 */
	private void maySaveProfile() {

		/* Validate */

		final String heightStr = mDttHeight.getText().toString()
				.replace(METERS, "");
		final String weightStr = mDttWeight.getText().toString()
				.replace(KILO, "");
		final String ageStr = mDttAge.getText().toString();

		if (TextUtils.isEmpty(mDttAge.getText().toString())
				|| TextUtils.isEmpty(heightStr) || TextUtils.isEmpty(weightStr)) {
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.error_empty_values),
					Toast.LENGTH_SHORT).show();
			return;
		}

		/* Height in editText is in meters */
		final double height = Double.parseDouble(heightStr) * 100d;
		final int birthDay = Integer.parseInt(mDttAge.getText().toString());
		final double weight = Double.parseDouble(weightStr);

		if (!GeneralUtils.checkHeight(height / 100)) {
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.error_alert_height),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!GeneralUtils.checkWeight(weight)) {
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.error_alert_weight),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!GeneralUtils.checkAge(birthDay)) {
			//Toast.makeText(getApplicationContext(),
			//		getResources().getString(R.string.error_alert_age),
			//		Toast.LENGTH_SHORT).show();
		}

		/* Start a loading dialog */

		final DialogFragment df = LoadingDialogFragment
				.newInstance(getString(R.string.loading));

		df.setCancelable(true);
		df.show(getSupportFragmentManager(), FRAG_TAG_LOADING);

		SportsWorldPreferences
				.setGuestHeigh(getApplicationContext(), heightStr);
		SportsWorldPreferences.setGuestWeight(getApplicationContext(),
				weightStr);
		SportsWorldPreferences.setGuestAge(getApplicationContext(), ageStr);

		/* Start a service to save data on server and then on database */
		if (!fistTime)
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.success_save_changes),
					Toast.LENGTH_SHORT).show();

		final Intent mainBoard = new Intent(
				getApplicationContext() /* context */, MainboardActivity.class);
		mainBoard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mainBoard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(mainBoard);
		finish();

		if (df != null)
			df.dismiss();

	}

	/* (non-Javadoc)
	 * @see android.view.View.OnFocusChangeListener#onFocusChange(android.view.View, boolean)
	 */
	@Override
	public void onFocusChange(View view, boolean hasFocus) {
		if (hasFocus) {

			switch (view.getId()) {

			case R.id.dtt_height:
				mDttHeight.setText(mDttHeight.getText().toString()
						.replace(METERS, ""));
				break;

			case R.id.dtt_weight:
				mDttWeight.setText(mDttWeight.getText().toString()
						.replace(KILO, ""));
				break;

			default:
				throw new UnsupportedOperationException("Unknown view: "
						+ view.getId());
			}

		} else {

			switch (view.getId()) {

			case R.id.dtt_height:
				mDttHeight.setText(mDttHeight.getText() + METERS);
				break;

			case R.id.dtt_weight:
				mDttWeight.setText(mDttWeight.getText() + KILO);
				break;

			default:
				throw new UnsupportedOperationException("Unknown view: "
						+ view.getId());
			}

		}

	}

	/**
	 * Complete guest account.
	 */
	public void completeGuestAccount() {
		mPrfAvatar.setProfileId(SportsWorldPreferences
				.getGuestId(getApplicationContext()));
		mTxtGenderE.setText(SportsWorldPreferences
				.getGuestGender(getApplicationContext()));
		mTxtName.setText(SportsWorldPreferences
				.getGuestName(getApplicationContext()));
		mDttHeight.setText(SportsWorldPreferences
				.getGuestHeigh(getApplicationContext()) + "");
		mDttWeight.setText(SportsWorldPreferences
				.getGuestWeight(getApplicationContext()) + "");
		mDttAge.setText(SportsWorldPreferences
				.getGuestAge(getApplicationContext()));
	}
}
