package mx.com.sportsworld.sw.fragment;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.app.Activity;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

// TODO: Auto-generated Javadoc

/**
 * The Class DashboardFragment.
 */
public class DashboardFragment extends SherlockFragment implements
		View.OnClickListener, LoaderCallbacks<Cursor>,DatePickerDialog.OnDateSetListener {

	/** The Constant COLS. */
	private static final String[] COLS = new String[] { SportsWorldContract.User.ROUTINE_ID };

	/** The m shortcut click listener. */
	private OnShortcutClickListener mShortcutClickListener;

	/** The m mgb go to routine. */
	//private ImageButton mMgbGoToRoutine;

	/** The m account mngr. */
	private SportsWorldAccountManager mAccountMngr;

	/** The m routine id. */
	private long mRoutineId;

	/** The m mgv doing routine. */
	//private ImageView mMgvDoingRoutine;

	/*	Declaracion de Bottones	*/
	private LinearLayout mgbGoToClasses;
	private LinearLayout mgbGoToclubs;
	private LinearLayout mgbGoToRutinas;
	private LinearLayout mgbGoToNoticias;
	private Button mgbGoToUpster;
	/*	Declaracion de Bottones	*/

	public static final String DATEPICKER_TAG = "datepicker";
	public static final String TIMEPICKER_TAG = "timepicker";
	public static View layout;

	/**
	 * Instantiates a new dashboard fragment.
	 */
	public DashboardFragment() {
	}

	/**
	 * New instance.
	 *
	 * @return the dashboard fragment
	 */
	public static DashboardFragment newInstance() {
		return new DashboardFragment();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mShortcutClickListener = (OnShortcutClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.getClass().getName()
					+ " must implement "
					+ OnShortcutClickListener.class.getName() + ".");
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		boolean idActive = SportsWorldPreferences.getRoutineId(getActivity()) != 0;
		/*
		if (idActive)
			mMgvDoingRoutine.setVisibility(View.VISIBLE);
		else
			mMgvDoingRoutine.setVisibility(View.GONE);
			*/
		super.onResume();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_dashboard,
				container, false /* attachToRoot */);
/*
		mMgvDoingRoutine = (ImageView) view
				.findViewById(R.id.mgv_doing_routine);
*/

		/*	Extrae el boton del XML y lo liga con el del java	*/

		mgbGoToclubs = (LinearLayout) view.findViewById(R.id.mgb_go_to_clubs);

		mgbGoToClasses = (LinearLayout) view.findViewById(R.id.btn_clases);

		mgbGoToRutinas = (LinearLayout) view.findViewById(R.id.btn_rutinas);

		mgbGoToNoticias = (LinearLayout) view.findViewById(R.id.btn_noticias);

		mgbGoToUpster = (Button) view.findViewById(R.id.btn_upster);

		/*	Extrae el boton del XML y lo liga con el del java	*/

		/*
		mMgbGoToRoutine = (ImageButton) view
				.findViewById(R.id.mgb_go_to_my_routine);
				*/
		/*
		final ImageButton mgbGoToLoyaltyProgram = (ImageButton) view
				.findViewById(R.id.mgb_go_to_loyalty_program);
		final ImageButton mgbGoToNews = (ImageButton) view
				.findViewById(R.id.mgb_go_to_news);
*/
		/*	Agrega eventos a botones	*/

		mgbGoToclubs.setOnClickListener(this);
		mgbGoToClasses.setOnClickListener(this);
		mgbGoToUpster.setOnClickListener(this);
		mgbGoToRutinas.setOnClickListener(this);
		mgbGoToNoticias.setOnClickListener(this);

		/*	Agrega eventos a botones	*/

		//mMgbGoToRoutine.setOnClickListener(this);
		//mgbGoToLoyaltyProgram.setOnClickListener(this);
		//mgbGoToNews.setOnClickListener(this);

		View parent = inflater.inflate(R.layout.custom_layout_toast, container, false);
		layout = inflater.inflate(R.layout.custom_layout_toast,(ViewGroup)parent.findViewById(R.id.toast_layout_root));
		return view;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mAccountMngr = new SportsWorldAccountManager(getActivity() /* context */);

		if (mAccountMngr.isLoggedInAsMember()) {
			getLoaderManager().initLoader(0 /* loaderId */,
					null /* loaderArgs */, this /* loaderCallback */);
		} else {
			mRoutineId = 0;

		}
		//mMgbGoToRoutine.setVisibility(View.VISIBLE);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.mgb_go_to_clubs:
				mShortcutClickListener.OnGoToClubsClick();
				break;
			case R.id.btn_clases:
				mShortcutClickListener.OnGoToClassesClick();
				break;
			case R.id.btn_rutinas:
				mShortcutClickListener.OnGoToMyRoutineClick(mRoutineId);
				break;
			case R.id.btn_noticias:
				mShortcutClickListener.OnGoToNewsClick();
				break;
			case  R.id.btn_upster:
				mShortcutClickListener.OnGoToUpster();
				break;
			/*
			case R.id.mgb_go_to_my_routine:
				mShortcutClickListener.OnGoToMyRoutineClick(mRoutineId);
				break;
			case R.id.mgb_go_to_loyalty_program:
				mShortcutClickListener.OnGoToLoyaltyProgramClick();
				break;
			case R.id.mgb_go_to_news:
				mShortcutClickListener.OnGoToNewsClick();
				break;
			case R.id.mgb_go_to_virtual_tour:
				mShortcutClickListener.OnGoToVirtualTourClick();
				break;
				*/
			default:
				throw new UnsupportedOperationException("View " + v.getId()
						+ " not recognized.");
		}
	}


	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
		//Toast(this, "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();
		//Toast.makeText(DashboardFragment.this,"new date:" + year + "-" + month + "-" + day, Toast.LENGTH_SHORT);
		//Toast.makeText(DashboardFragment.this, "STRING MESSAGE", Toast.LENGTH_LONG).show();

	}
	/**
	 * The listener interface for receiving onShortcutClick events.
	 * The class that is interested in processing a onShortcutClick
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnShortcutClickListener<code> method. When
	 * the onShortcutClick event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see
	 */
	public static interface OnShortcutClickListener {

		/**
		 * On go to clubs click.
		 */
		public abstract void OnGoToClubsClick();

		/**
		 * On go to classes click.
		 */
		public abstract void OnGoToClassesClick();

		public abstract void OnGoToUpster();
		/**
		 * On go to my routine click.
		 *
		 * @param routineId the routine id
		 */
		public abstract void OnGoToMyRoutineClick(long routineId);

		/**
		 * On go to loyalty program click.
		 */
		public abstract void OnGoToLoyaltyProgramClick();

		/**
		 * On go to news click.
		 */
		public abstract void OnGoToNewsClick();

		/**
		 * On go to virtual tour click.
		 */
		public abstract void OnGoToVirtualTourClick();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		final String currentUserId = String.valueOf(mAccountMngr
				.getCurrentUserId());
		final Uri userUri = SportsWorldContract.User
				.buildUserUri(currentUserId);

		return new CursorLoader(getActivity() /* context */, userUri, COLS,
				null/* selection */, null/* selectionArgs */, null/* sortOrder */);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (!cursor.moveToFirst()) {
			mAccountMngr.logOut();

		}

		mRoutineId = SportsWorldPreferences.getRoutineId(getActivity());
		/*
		mMgbGoToRoutine.setVisibility(View.VISIBLE);

		mMgvDoingRoutine.setVisibility((mRoutineId == 0) ? View.GONE
				: View.VISIBLE);

		boolean idActive = SportsWorldPreferences.getRoutineId(getActivity()) != 0;
		if (idActive)
			mMgvDoingRoutine.setVisibility(View.VISIBLE);
		else
			mMgvDoingRoutine.setVisibility(View.GONE);
*/
		SportsWorldPreferences.setRoutine(getActivity(), (mRoutineId != -1));

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		/* Do nothing */
	}

	/**
	 * Checks if is member.
	 *
	 * @return true, if is member
	 */
	public boolean isMember() {
		final SportsWorldAccountManager accountMngr = new SportsWorldAccountManager(
				getActivity() /* context */);
		return accountMngr.isLoggedInAsMember();

	}

}
