package mx.com.sportsworld.sw.fragment.dialog;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.GymClassesActivity;
import mx.com.sportsworld.sw.utils.ConnectionUtils;

// TODO: Auto-generated Javadoc

/**
 * The Class FragmentCalendar.
 */
public class FragmentCalendar extends SherlockFragment implements DatePickerDialog.OnDateSetListener{

	/** The m cln calendar. */
	CalendarView mClnCalendar;
	
	/** The act. */
	private static FragmentActivity act;
	
	/** The branch id f. */
	public static long branchIdF;
	
	/** The Constant FRAG_ARG_BRANCH_ID. */
	private static final String FRAG_ARG_BRANCH_ID = "frag_arg_branch_id";
	
	/** The c. */
	public static Calendar c = null;
	
	/** The ft. */
	public static FragmentTransaction ft;
	
	/** The f. */
	public static Fragment f;
	public static final String DATEPICKER_TAG = "datepicker";
	/**
	 * New instance.
	 *
	 * @return the fragment calendar
	 */
	public static final FragmentCalendar newInstance() {
		return new FragmentCalendar();
	}

	/**
	 * New instance.
	 *
	 * @param branchId the branch id
	 * @param ctx the ctx
	 * @return the fragment calendar
	 */
	public static final FragmentCalendar newInstance(long branchId,
			FragmentActivity ctx) {
		final FragmentCalendar f = new FragmentCalendar();
		final Bundle args = new Bundle();
		branchIdF = branchId;
		args.putLong(FRAG_ARG_BRANCH_ID, branchId);
		f.setArguments(args);
		act = ctx;
		return f;

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.fragment_calendar_view,
				null /* root */, false /* attachToRoot */);

		final Calendar calendar = Calendar.getInstance();
		final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
		//final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(v)
		datePickerDialog.setVibrate(false);
		datePickerDialog.setYearRange(1985, 2028);
		datePickerDialog.setCloseOnSingleTapDay(false);
		datePickerDialog.show(getSherlockActivity().getSupportFragmentManager(), DATEPICKER_TAG);

		return view;
	}
	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		long milliTime = calendar.getTimeInMillis();

		final Intent goToShowClasses = new Intent(act
				.getApplicationContext() ,
				GymClassesActivity.class);
		goToShowClasses.putExtra(
				GymClassesActivity.EXTRA_BRANCH_ID, branchIdF);
		goToShowClasses.putExtra(
				GymClassesActivity.EXTRA_DATE_ID,
				milliTime);

		if (isNetworkAvailable())
			startActivity(goToShowClasses);

	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(null);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(null);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(null);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onViewStateRestored(android.os.Bundle)
	 */
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewStateRestored(null);
	}

	/**
	 * Checks if is network available.
	 *
	 * @return true, if is network available
	 */
	public boolean isNetworkAvailable() {
		boolean network = ConnectionUtils.isNetworkAvailable(getActivity()
				.getApplicationContext());
		if (!network)
			Toast.makeText(getActivity().getApplicationContext(),
					getResources().getString(R.string.error_connection),
					Toast.LENGTH_SHORT).show();
		return network;
	}
}
