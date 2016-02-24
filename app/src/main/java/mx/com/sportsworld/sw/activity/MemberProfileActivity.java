package mx.com.sportsworld.sw.activity;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.app.AuthSherlockFragmentActivity;
import mx.com.sportsworld.sw.fragment.dialog.DatePickerFragment;
import mx.com.sportsworld.sw.fragment.dialog.ProgressDialogFragment;
import mx.com.sportsworld.sw.pojo.ProfileUserPojo;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.provider.SportsWorldContract;
import mx.com.sportsworld.sw.utils.ConnectionUtils;
import mx.com.sportsworld.sw.utils.GeneralUtils;
import mx.com.sportsworld.sw.web.task.MemberProfileUpdateTask;

// TODO: Auto-generated Javadoc

/**
 * The Class MemberProfileActivity.
 */
public class MemberProfileActivity extends AuthSherlockFragmentActivity
		implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener,
		View.OnFocusChangeListener {

	/** The Constant METERS. */
	private static final String METERS = "m";
	
	/** The Constant KILO. */
	private static final String KILO = "Kg";
	
	/** The Constant REQUEST_CODE_UPDATE_MEMBER_PROFILE. */
	private static final int REQUEST_CODE_UPDATE_MEMBER_PROFILE = 0;
	
	/** The Constant LOADER_ID_USER_PROFILE. */
	private static final int LOADER_ID_USER_PROFILE = 0;
	
	/** The Constant COLS. */
	private static final String[] COLS = buildColumns();
	
	/** The Constant COL_INDEX_NAME. */
	private static final int COL_INDEX_NAME = 0;
	
	/** The Constant COL_INDEX_MEMBER_NUMBER. */
	private static final int COL_INDEX_MEMBER_NUMBER = 1;
	
	/** The Constant COL_INDEX_CLUB_NAME. */
	private static final int COL_INDEX_CLUB_NAME = 2;
	
	/** The Constant COL_INDEX_GENDER. */
	private static final int COL_INDEX_GENDER = 3;
	
	/** The Constant COL_INDEX_AGE. */
	private static final int COL_INDEX_AGE = 4;
	
	/** The Constant COL_INDEX_HEIGHT. */
	private static final int COL_INDEX_HEIGHT = 5;
	
	/** The Constant COL_INDEX_WEIGHT. */
	private static final int COL_INDEX_WEIGHT = 6;
	
	/** The Constant COL_INDEX_ID. */
	private static final int COL_INDEX_ID = 7;
	
	/** The Constant COL_INDEX_BIRTHDAY. */
	private static final int COL_INDEX_BIRTHDAY = 8;
	
	/** The Constant COL_INDEX_MEMBER_TYPE. */
	private static final int COL_INDEX_MEMBER_TYPE = 9;
	
	/** The Constant COL_INDEX_MAINTEIMENT. */
	private static final int COL_INDEX_MAINTEIMENT = 10;
	
	/** The Constant DATE_DIALOG_ID. */
	static final int DATE_DIALOG_ID = 0;
	
	/** The m txt name. */
	private TextView mTxtName;
	
	/** The m txt membership number. */
	private TextView mTxtMembershipNumber;
	
	/** The m txt membership type. */
	private TextView mTxtMembershipType;
	
	/** The m txt club name. */
	private TextView mTxtClubName;
	
	/** The m txt maintenance. */
	private TextView mTxtMaintenance;
	
	/** The m txt gender. */
	private TextView mTxtGender;
	
	/** The m dtt bd. */
	private EditText mDttBD;
	
	/** The m dtt height. */
	private EditText mDttHeight;
	
	/** The m dtt weight. */
	private EditText mDttWeight;
	
	/** The m btn show policies. */
	private Button mBtnShowPolicies;
	
	/** The m btn save profile. */
	private Button mBtnSaveProfile;
	
	/** The age str. */
	String ageStr;
	
	/** The update task. */
	MemberProfileUpdateTask updateTask;
	
	/** The fist time. */
	public boolean fistTime;
	
	/** The progress. */
	public static ProgressDialogFragment progress;
	
	/** The dialgo fragment. */
	Fragment dialgoFragment;

	/**
	 * Builds the columns.
	 *
	 * @return the string[]
	 */
	private static String[] buildColumns() {

		final SparseArray<String> colsMap = new SparseArray<String>();
		colsMap.put(COL_INDEX_NAME, SportsWorldContract.User.NAME);
		colsMap.put(COL_INDEX_MEMBER_NUMBER,
				SportsWorldContract.User.MEMBER_NUMBER);
		colsMap.put(COL_INDEX_CLUB_NAME, SportsWorldContract.User.CLUB_NAME);
		colsMap.put(COL_INDEX_GENDER, SportsWorldContract.User.GENDER);
		colsMap.put(COL_INDEX_AGE, SportsWorldContract.User.AGE);
		colsMap.put(COL_INDEX_HEIGHT, SportsWorldContract.User.HEIGHT);
		colsMap.put(COL_INDEX_WEIGHT, SportsWorldContract.User.WEIGHT);
		colsMap.put(COL_INDEX_ID, SportsWorldContract.User._ID);
		colsMap.put(COL_INDEX_BIRTHDAY, SportsWorldContract.User.BIRTH_DATE);
		colsMap.put(COL_INDEX_MEMBER_TYPE, SportsWorldContract.User.MEMBER_TYPE);
		colsMap.put(COL_INDEX_MAINTEIMENT,
				SportsWorldContract.User.MAINTEINMENT);

		final int colCount = colsMap.size();
		final String[] cols = new String[colCount];
		for (int i = 0; i < colCount; i++) {
			cols[i] = colsMap.get(i);
		}

		return cols;

	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.app.AuthSherlockFragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (isFinishing()) {
			return;
		}
		//getSupportActionBar().setTitle("Perfil");
		setContentView(R.layout.activity_member_profile);

		mBtnShowPolicies = (Button) findViewById(R.id.btn_show_policies);
		mBtnSaveProfile = (Button) findViewById(R.id.btn_save_profile);

		mBtnShowPolicies.setOnClickListener(this);
		mBtnSaveProfile.setOnClickListener(this);

		mTxtName = (TextView) findViewById(R.id.txt_name);
		mTxtMembershipNumber = (TextView) findViewById(R.id.txt_membership_number);
		mTxtMembershipType = (TextView) findViewById(R.id.txt_membership_type);
		mTxtClubName = (TextView) findViewById(R.id.txt_branch_name);
		mTxtMaintenance = (TextView) findViewById(R.id.txt_maintenance_type);
		mTxtGender = (TextView) findViewById(R.id.fake_dtt_gender);
		mDttBD = (EditText) findViewById(R.id.edit_birth_profile);
		mDttHeight = (EditText) findViewById(R.id.dtt_height);
		mDttWeight = (EditText) findViewById(R.id.dtt_weight);

		mDttWeight.setOnFocusChangeListener(this);
		mDttHeight.setOnFocusChangeListener(this);
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Planer_Reg.ttf");
		mBtnShowPolicies.setTypeface(font);
		mBtnSaveProfile.setTypeface(font);
		mTxtName.setTypeface(font);
		mTxtMembershipNumber.setTypeface(font);
		mTxtMembershipType.setTypeface(font);
		mTxtClubName.setTypeface(font);
		mTxtMaintenance.setTypeface(font);
		mTxtGender.setTypeface(font);
		mDttBD.setTypeface(font);
		mDttHeight.setTypeface(font);
		mDttWeight.setTypeface(font);

		//lblCabezera
		TextView lblCabezera = (TextView) findViewById(R.id.lblCabezera);
		TextView textView8 = (TextView) findViewById(R.id.textView8);
		TextView textView9 = (TextView) findViewById(R.id.textView9);
		TextView textView10 = (TextView) findViewById(R.id.textView10);
		TextView textView11 = (TextView) findViewById(R.id.textView11);
		TextView textView12 = (TextView) findViewById(R.id.textView12);

		lblCabezera.setTypeface(font);
		textView8.setTypeface(font);
		textView9.setTypeface(font);
		textView10.setTypeface(font);
		textView11.setTypeface(font);
		textView12.setTypeface(font);

		fistTime = SportsWorldPreferences.isFirstTime(getApplicationContext());

		if (fistTime)
			mBtnShowPolicies.setVisibility(View.GONE);

		final LoaderManager loaderMngr = getSupportLoaderManager();
		if ((savedInstanceState == null)
				|| (loaderMngr.getLoader(LOADER_ID_USER_PROFILE) != null)) {
			loaderMngr.initLoader(LOADER_ID_USER_PROFILE,
					null /* loaderArgs */, this /* loaderCallback */);

		}

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		final SportsWorldAccountManager accountMngr = getAccountManager();
		final String currentUserId = String.valueOf(accountMngr
				.getCurrentUserId());
		final Uri userUri = SportsWorldContract.User
				.buildUserUri(currentUserId);

		return new CursorLoader(this /* context */, userUri, COLS,
				null/* selection */, null/* selectionArgs */, null/* sortOrder */);

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		if (cursor.moveToFirst()) {
			final String name = cursor.getString(COL_INDEX_NAME);
			final String memberNumber = cursor
					.getString(COL_INDEX_MEMBER_NUMBER);
			final String clubName = cursor.getString(COL_INDEX_CLUB_NAME);
			final String gender = cursor.getString(COL_INDEX_GENDER);
			final String height = String.valueOf((cursor
					.getDouble(COL_INDEX_HEIGHT) / 100d)) + METERS;
			final String weight = cursor.getString(COL_INDEX_WEIGHT) + KILO;
			final String age = cursor.getString(COL_INDEX_AGE);
			final String bd = cursor.getString(COL_INDEX_BIRTHDAY);
			final String member_type = cursor.getString(COL_INDEX_MEMBER_TYPE);
			final String mainteiment = cursor.getString(COL_INDEX_MAINTEIMENT);
			final String userId = cursor.getString(COL_INDEX_ID);

			mTxtName.setText(name);

			//mTxtMembershipNumber.setText(memberNumber.equals("0") ? "" : memberNumber);
			mTxtMembershipNumber.setText(userId);

			mTxtClubName.setText(clubName.equals("null") ? "" : clubName);
			mTxtGender.setText(gender);
			mDttBD.setText(bd);
			mDttHeight.setText(height);
			mDttWeight.setText(weight);
			ageStr = age;
			mTxtMembershipType.setText(member_type.equals("null") ? ""
					: member_type);
			mTxtMaintenance.setText(mainteiment);
			SportsWorldPreferences.setGuestAge(getApplicationContext(), age);

			SportsWorldPreferences.setGuestGender(this, gender);
			SportsWorldPreferences.setGuestName(this, name);

		}

		getSupportLoaderManager().destroyLoader(LOADER_ID_USER_PROFILE);

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mTxtName.setText(null);
		mTxtMembershipNumber.setText(null);
		mTxtClubName.setText(null);
		mDttBD.setText(null);
		mDttHeight.setText(null);
		mDttWeight.setText(null);
		mTxtMembershipType.setText(null);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_show_policies:
			if (isNetworkAvailable())
				showPolicies();
			break;
		case R.id.btn_save_profile:
			if (isNetworkAvailable())
				maySaveProfile();
			break;
		case R.id.edit_birth_profile:
			showDatePicker(mDttBD.getText().toString());
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
		if (TextUtils.isEmpty(mDttBD.getText().toString())
				|| TextUtils.isEmpty(heightStr) || TextUtils.isEmpty(weightStr)) {

			Toast.makeText(this,
					getResources().getString(R.string.error_empty_values),
					Toast.LENGTH_SHORT).show();
			return;
		}

		/* Height in editText is in meters */
		final double height = Double.parseDouble(heightStr) * 100d;
		final String birthDay = mDttBD.getText().toString();
		final double weight = Double.parseDouble(weightStr);

		if (!GeneralUtils.checkHeight(height / 100)) {
			Toast.makeText(this,
					getResources().getString(R.string.error_alert_height),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!GeneralUtils.checkWeight(weight)) {
			Toast.makeText(this,
					getResources().getString(R.string.error_alert_weight),
					Toast.LENGTH_SHORT).show();
			return;
		}

		SportsWorldPreferences.setGuestAge(this, ageStr);

		/* Start a loading dialog */
		showFragmentsDialog(false);

		/* Start a service to save data on server and then on database */

		updateTask = new MemberProfileUpdateTask(new ResponseInterface() {

			@Override
			public void onResultResponse(Object obj) {

				ProfileUserPojo usrPojo = (ProfileUserPojo) obj;
				if (usrPojo.isStatus()) {
					if (!fistTime)
						Toast.makeText(
								getApplicationContext(),
								getResources().getString(
										R.string.success_save_changes),
								Toast.LENGTH_SHORT).show();

				} else

					Toast.makeText(
							getApplicationContext(),
							getResources().getString(R.string.error_connection),
							Toast.LENGTH_SHORT).show();

				SportsWorldPreferences.setFirstTime(getApplicationContext(),
						false);

				final Intent mainBoard = new Intent(
						MemberProfileActivity.this /* context */,
						MainboardActivity.class);
				mainBoard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mainBoard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(mainBoard);

				finish();
			}
		});

		MemberProfileUpdateTask.mContext = this;
		ProfileUserPojo pojo = new ProfileUserPojo();
		pojo.setHeight(height);
		pojo.setWeight(weight);
		pojo.setDob(birthDay);
		updateTask.execute(pojo);

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode != REQUEST_CODE_UPDATE_MEMBER_PROFILE) {
			return;
		}

		if (progress != null)
			progress.dismissAllowingStateLoss();

		if (resultCode == Activity.RESULT_OK && !fistTime) {
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.success_save_changes),
					Toast.LENGTH_SHORT).show();

			final Intent mainBoard = new Intent(this /* context */,
					MainboardActivity.class);
			mainBoard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			mainBoard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(mainBoard);
			finish();

		} else { // We don't expect Activity.RESULT_CANCELED_BY_USER, so it's
					// safe to do this
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.error_save_changes),
					Toast.LENGTH_SHORT).show();

		}

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
	 * Show date picker.
	 *
	 * @param dateText the date text
	 */
	private void showDatePicker(String dateText) {
		DatePickerFragment date = new DatePickerFragment();
		/**
		 * Set Up Current Date Into dialog
		 */

		int[] temp;
		temp = getDate(dateText);

		Bundle args = new Bundle();

		args.putInt("year", temp[0]);
		args.putInt("month", temp[1] - 1);
		args.putInt("day", temp[2]);

		date.setArguments(args);

		date.setCallBack(ondate);
		date.show(getSupportFragmentManager(), DatePickerFragment.TAG);
	}

	/**
	 * Gets the date.
	 *
	 * @param dateString the date string
	 * @return the date
	 */
	public int[] getDate(String dateString) {
		String[] temp;
		temp = dateString.split("-");
		int[] dateInt = new int[temp.length];
		dateInt[0] = Integer.parseInt(temp[0]);
		dateInt[1] = Integer.parseInt(temp[1]);
		dateInt[2] = Integer.parseInt(temp[2]);
		return dateInt;
	}

	/** The ondate. */
	OnDateSetListener ondate = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, final int year,
				final int monthOfYear, final int dayOfMonth) {
			new Handler().post(new Runnable() {

				@Override
				public void run() {
					mDttBD.setText(year + "-" + (monthOfYear + 1) + "-"
							+ dayOfMonth);
				}
			});
		}
	};

	/**
	 * Checks if is network available.
	 *
	 * @return true, if is network available
	 */
	public boolean isNetworkAvailable() {
		boolean network = ConnectionUtils
				.isNetworkAvailable(getApplicationContext());
		if (!network)
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.error_connection),
					Toast.LENGTH_SHORT).show();
		return network;
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
					.newInstance(MemberProfileActivity.this);
			progress.show(getSupportFragmentManager(),
					ProgressDialogFragment.progressDialogTag);
		}

	}
}
