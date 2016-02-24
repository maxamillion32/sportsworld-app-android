package mx.com.sportsworld.sw.activity;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import mx.com.sportsworld.sw.app.AuthSherlockFragmentActivity;
import mx.com.sportsworld.sw.fragment.ExerciseInstructionsFragment;

// TODO: Auto-generated Javadoc

/**
 * The Class ExerciseInstructionsActivity.
 */
public class ExerciseInstructionsActivity extends AuthSherlockFragmentActivity {

	/** The Constant EXTRA_EXERCISE_ID. */
	public static final String EXTRA_EXERCISE_ID = "com.upster.extra.EXERCISE_ID";
	
	/** The Constant FRAG_TAG_EXERCISE_INSTRUCTIONS. */
	private static final String FRAG_TAG_EXERCISE_INSTRUCTIONS = "frag_tag_exercise_instructions";

	/* (non-Javadoc)
	 * @see com.sportsworld.android.app.AuthSherlockFragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (isFinishing()) {
			return;
		}

		if (savedInstanceState == null) {
			final long exerciseId = getIntent().getLongExtra(EXTRA_EXERCISE_ID,
					-1);
			if (exerciseId == -1) {
				throw new IllegalArgumentException(
						"You must pass an EXTRA_EXERCISE_ID long extra");
			}

			final FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			final Fragment f = ExerciseInstructionsFragment
					.newInstance(exerciseId);
			ft.add(android.R.id.content, f, FRAG_TAG_EXERCISE_INSTRUCTIONS);
			ft.commit();
		}

	}

}
