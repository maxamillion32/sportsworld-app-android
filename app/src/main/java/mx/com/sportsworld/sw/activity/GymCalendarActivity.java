package mx.com.sportsworld.sw.activity;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.app.AuthSherlockFragmentActivity;
import mx.com.sportsworld.sw.fragment.dialog.FragmentCalendar;

// TODO: Auto-generated Javadoc

/**
 * The Class GymCalendarActivity.
 */
public class GymCalendarActivity extends AuthSherlockFragmentActivity {

	/** The Constant EXTRA_BRANCH_ID. */
	public static final String EXTRA_BRANCH_ID = "com.upster.extra.BRANCH_ID";
	
	/** The Constant EXTRA_DATE_ID. */
	public static final String EXTRA_DATE_ID = "com.upster.extra.DATE_ID";
	
	/** The Constant FRAG_TAG_GYM_CALENDAR. */
	private static final String FRAG_TAG_GYM_CALENDAR = "frag_tag_calendar_gym";

	/* (non-Javadoc)
	 * @see com.sportsworld.android.app.AuthSherlockFragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (isFinishing()) {
			return;
		}

		getSupportActionBar().setTitle(R.string.classes);

		if (savedInstanceState == null) {
			final FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			final long branchId = getIntent().getLongExtra(EXTRA_BRANCH_ID, -1);
			final Fragment f = (branchId == -1) ? FragmentCalendar
					.newInstance() : FragmentCalendar.newInstance(branchId,
					this);

			ft.add(android.R.id.content, f, FRAG_TAG_GYM_CALENDAR);
			ft.commit();
		}

	}

}
