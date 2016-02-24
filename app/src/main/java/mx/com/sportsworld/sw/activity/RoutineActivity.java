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
import android.support.v4.app.FragmentTransaction;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.app.AuthSherlockFragmentActivity;
import mx.com.sportsworld.sw.fragment.RoutineFragment;
import mx.com.sportsworld.sw.fragment.RoutineFragment.OnMoreDetailsClickListener;
import mx.com.sportsworld.sw.fragment.dialog.WeekDayPickerDialogFragment.OnWeekDayselectListener;

// TODO: Auto-generated Javadoc

/**
 * The Class RoutineActivity.
 */
public class RoutineActivity extends AuthSherlockFragmentActivity implements
		OnMoreDetailsClickListener, OnWeekDayselectListener {

	/** The Constant EXTRA_ROUTINE_ID. */
	public static final String EXTRA_ROUTINE_ID = "com.upster.extra.ID_ROUTINE";

	/** The Constant FRAG_TAG_ROUTINE. */
	public static final String FRAG_TAG_ROUTINE = "frag_tag_routine";

	/** The m handler. */
	private Handler mHandler = new Handler();

	/** The routine active. */
	public static boolean routineActive;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sportsworld.android.app.AuthSherlockFragmentActivity#onCreate(android
	 * .os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (isFinishing()) {
			return;
		}

		mHandler.post(new Runnable() {
			@Override
			public void run() {

			}
		});

		getSupportActionBar().setTitle(R.string.routine_detail);

		if (savedInstanceState == null) {
			final long routineId = getIntent().getLongExtra(EXTRA_ROUTINE_ID,
					-1);
			final boolean haveRoutine = getIntent().getBooleanExtra(
					"haveRoutine", false);
			if (routineId == -1) {
				throw new IllegalArgumentException(
						"You must pass a EXTRA_ROUTINE_ID long");
			}
			final FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			final Fragment f = RoutineFragment.newInstance(routineId,
					haveRoutine);
			ft.add(android.R.id.content, f, FRAG_TAG_ROUTINE);
			ft.disallowAddToBackStack();
			ft.commit();

		}

	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		FragmentManager fm = getSupportFragmentManager();
		for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
			fm.popBackStack();
			fm.beginTransaction().commit();
		}
		super.onDestroy();
	}

	/*
	 * Open Nutrition Activity
	 */
	/* (non-Javadoc)
	 * @see com.sportsworld.android.fragment.RoutineFragment.OnMoreDetailsClickListener#onNutritionAdviceClicked()
	 */
	@Override
	public void onNutritionAdviceClicked() {
		final Intent showNutritionAdvice = new Intent(this /* context */,
				NutritionAdviceActivity.class);
		startActivity(showNutritionAdvice);
	}

	/*
	 * Opens the instuctions Activity
	 */
	/* (non-Javadoc)
	 * @see com.sportsworld.android.fragment.RoutineFragment.OnMoreDetailsClickListener#onInstructionsClicked(long)
	 */
	@Override
	public void onInstructionsClicked(long exerciseId) {
		final Intent showExerciseInstructions = new Intent(this /* context */,
				ExerciseInstructionsActivity.class);
		showExerciseInstructions.putExtra(
				ExerciseInstructionsActivity.EXTRA_EXERCISE_ID, exerciseId);
		startActivity(showExerciseInstructions);
	}

	/*
	 * Choose a day
	 */
	/* (non-Javadoc)
	 * @see com.sportsworld.android.fragment.dialog.WeekDayPickerDialogFragment.OnWeekDayselectListener#onWeekDaySelect(long, long, int, int)
	 */
	@Override
	public void onWeekDaySelect(long weekId, long dayId, int weekPickerValue,
			int dayPickerValue) {
		final RoutineFragment f = (RoutineFragment) getSupportFragmentManager()
				.findFragmentByTag(FRAG_TAG_ROUTINE);
		f.onWeekDayChange(weekId, dayId, weekPickerValue, dayPickerValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/*
		 * XXX We will manually forward it to the fragment, since our activity
		 * is not doing it automatically. This has the unfortunate side effect
		 * of calling fragment's onActivityResult twice.
		 */
		final Fragment f = getSupportFragmentManager().findFragmentByTag(
				FRAG_TAG_ROUTINE);
		f.onActivityResult(requestCode, resultCode, data);
	}

}
