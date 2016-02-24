package mx.com.sportsworld.sw.fragment.dialog;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import com.actionbarsherlock.app.SherlockDialogFragment;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.model.ExercisePeriod;
import mx.com.sportsworld.sw.model.ExerciseWeek;
import mx.com.sportsworld.sw.model.WeekDayRelationship;
import mx.com.sportsworld.sw.parser.ExercisePeriodTranslator;
import mx.com.sportsworld.sw.parser.WeekDayRelationshipParser;
import mx.com.sportsworld.sw.provider.SportsWorldContract;
import mx.com.sportsworld.sw.widget.NumberPicker;
import mx.com.sportsworld.sw.widget.NumberPicker.OnChangedListener;

// TODO: Auto-generated Javadoc

/**
 * The Class WeekDayPickerDialogFragment.
 */
public class WeekDayPickerDialogFragment extends SherlockDialogFragment
		implements LoaderCallbacks<Cursor>, OnChangedListener,
		DialogInterface.OnClickListener {

	/** The Constant STATE_CURRENT_DAY_PICKER_VALUE. */
	private static final String STATE_CURRENT_DAY_PICKER_VALUE = "state_current_day_picker_value";
	
	/** The Constant STATE_CURRENT_WEEK_PICKER_VALUE. */
	private static final String STATE_CURRENT_WEEK_PICKER_VALUE = "state_current_week_picker_value";
	
	/** The Constant LOADER_ID_WEEK_DAY_RELATIONSHIP. */
	private static final int LOADER_ID_WEEK_DAY_RELATIONSHIP = 0;
	
	/** The Constant COL_INDEX_WEEK_ID. */
	private static final int COL_INDEX_WEEK_ID = 0;
	
	/** The Constant COL_INDEX_DAY_ID. */
	private static final int COL_INDEX_DAY_ID = 1;
	
	/** The Constant COL_INDEX_ACTIVE. */
	private static final int COL_INDEX_ACTIVE = 2;
	
	/** The Constant COLS. */
	private static final String[] COLS = buildColumns();
	
	/** The m exercise period. */
	private ExercisePeriod mExercisePeriod;
	
	/** The m nmb week id. */
	private NumberPicker mNmbWeekId;
	
	/** The m nmb day id. */
	private NumberPicker mNmbDayId;
	
	/** The m cur day picker value. */
	private static int mCurDayPickerValue;
	
	/** The m cur week picker value. */
	private static int mCurWeekPickerValue;
	
	/** The m first. */
	private boolean mFirst;
	
	/** The m listener. */
	private OnWeekDayselectListener mListener;

	/**
	 * Builds the columns.
	 *
	 * @return the string[]
	 */
	private static String[] buildColumns() {
		final SparseArray<String> colsMap = new SparseArray<String>();
		colsMap.put(COL_INDEX_WEEK_ID,
				SportsWorldContract.WeekDayRelationship.WEEK_ID);
		colsMap.put(COL_INDEX_DAY_ID,
				SportsWorldContract.WeekDayRelationship.DAY_ID);
		colsMap.put(COL_INDEX_ACTIVE,
				SportsWorldContract.WeekDayRelationship.ACTIVE);

		final int colCount = colsMap.size();
		final String[] cols = new String[colCount];
		for (int i = 0; i < colCount; i++) {
			cols[i] = colsMap.get(i);
		}

		return cols;
	}

	/**
	 * Instantiates a new week day picker dialog fragment.
	 */
	public WeekDayPickerDialogFragment() {
		/* Do nothing */
	}

	/**
	 * New instance.
	 *
	 * @param curDayPickerValue the cur day picker value
	 * @param curWeekPickerValue the cur week picker value
	 * @return the week day picker dialog fragment
	 */
	public static WeekDayPickerDialogFragment newInstance(
			int curDayPickerValue, int curWeekPickerValue) {
		mCurWeekPickerValue = curWeekPickerValue;
		mCurDayPickerValue = curDayPickerValue;
		return new WeekDayPickerDialogFragment();
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockDialogFragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnWeekDayselectListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.getClass().getName()
					+ " must implement "
					+ OnWeekDayselectListener.class.getName());
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final Context context = getActivity();

		final View view = LayoutInflater.from(context)
				.inflate(R.layout.fragment_week_day_picker, null /* root */,
						false /* attachToRoot */);
		mNmbWeekId = (NumberPicker) view.findViewById(R.id.nmb_week_id);
		mNmbDayId = (NumberPicker) view.findViewById(R.id.nmb_day_id);

		mNmbWeekId.setOnChangeListener(this);

		final AlertDialog alert = new AlertDialog.Builder(context)
				.setTitle(R.string.instructions_choose_week_day)
				.setView(view)
				.setNegativeButton(android.R.string.cancel, this /* onClickListener */)
				.setPositiveButton(android.R.string.ok, this /* onClickListener */)
				.create();
		return alert;

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mFirst = (savedInstanceState == null);
		if (!mFirst) {
			mCurDayPickerValue = savedInstanceState
					.getInt(STATE_CURRENT_DAY_PICKER_VALUE);
			mCurWeekPickerValue = savedInstanceState
					.getInt(STATE_CURRENT_WEEK_PICKER_VALUE);
		}

		getLoaderManager().initLoader(LOADER_ID_WEEK_DAY_RELATIONSHIP,
				null/* loaderArgs */, this /* loaderCallback */);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_CURRENT_DAY_PICKER_VALUE, mNmbDayId.getCurrent());
		outState.putInt(STATE_CURRENT_WEEK_PICKER_VALUE,
				mNmbWeekId.getCurrent());
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getActivity() /* context */,
				SportsWorldContract.WeekDayRelationship.CONTENT_URI, COLS,
				null /* selection */, null /* selectionArgs */, null/* sortOrder */);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		/*
		 * Our weekDays are already ordered by week and day.
		 */

		final WeekDayRelationshipParser parser = new WeekDayRelationshipParser();
		final List<WeekDayRelationship> weekDayRelationshipList = parser
				.parse(cursor);

		final ExercisePeriodTranslator translator = new ExercisePeriodTranslator();
		final ExercisePeriod exercisePeriod = translator
				.translate(weekDayRelationshipList);

		mNmbWeekId.setRange(1, exercisePeriod.size());
		//

		mNmbWeekId.setCurrent(mCurWeekPickerValue);
		mNmbDayId.setRange(1, exercisePeriod.get((mCurWeekPickerValue - 1))
				.getDayIds().size());
		mNmbDayId.setCurrent(mCurDayPickerValue);
		// }

		mExercisePeriod = exercisePeriod;

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		/* Do nothing */
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.widget.NumberPicker.OnChangedListener#onChanged(com.sportsworld.android.widget.NumberPicker, int, int)
	 */
	@Override
	public void onChanged(NumberPicker picker, int oldVal, int newVal) {

		mNmbDayId.setRange(1, mExercisePeriod.get(newVal - 1).getDayIds()
				.size());
	}

	/* (non-Javadoc)
	 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
	 */
	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_NEGATIVE:
			dismiss();
			break;
		case DialogInterface.BUTTON_POSITIVE:
			sendSelectedWeekDay();
			dismiss();
			break;
		default:
			throw new IllegalArgumentException("Unsupported button: " + which);
		}
	}

	/**
	 * Send selected week day.
	 */
	private void sendSelectedWeekDay() {
		final int weekPickerValue = mNmbWeekId.getCurrent();
		final int dayPickerValue = mNmbDayId.getCurrent();
		final ExerciseWeek selectedExerciseWeek = mExercisePeriod
				.get(weekPickerValue - 1);
		final long weekId = selectedExerciseWeek.getId();
		final long dayId = selectedExerciseWeek.getDayIds()
				.get(dayPickerValue - 1).getId();
		mListener.onWeekDaySelect(weekId, dayId, weekPickerValue,
				dayPickerValue);
	}

	/**
	 * The listener interface for receiving onWeekDayselect events.
	 * The class that is interested in processing a onWeekDayselect
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnWeekDayselectListener<code> method. When
	 * the onWeekDayselect event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnWeekDayselectEvent
	 */
	public static interface OnWeekDayselectListener {
		
		/**
		 * On week day select.
		 *
		 * @param weekId the week id
		 * @param dayId the day id
		 * @param weekPickerValue the week picker value
		 * @param dayPickerValue the day picker value
		 */
		public void onWeekDaySelect(long weekId, long dayId,
									int weekPickerValue, int dayPickerValue);
	}

}
