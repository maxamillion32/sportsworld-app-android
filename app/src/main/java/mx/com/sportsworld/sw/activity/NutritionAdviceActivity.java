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
import mx.com.sportsworld.sw.fragment.NutritionAdviceFragment;

// TODO: Auto-generated Javadoc

/**
 * Hosts NutritionAdviceFragment.
 */
public class NutritionAdviceActivity extends AuthSherlockFragmentActivity {

	/** The Constant FRAG_TAG_NUTRITION_ADVICE. */
	private static final String FRAG_TAG_NUTRITION_ADVICE = "frag_tag_nutrition_advice";

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
			final FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			final Fragment f = NutritionAdviceFragment.newInstace();
			ft.add(android.R.id.content, f, FRAG_TAG_NUTRITION_ADVICE);
			ft.commit();
		}

	}

}
