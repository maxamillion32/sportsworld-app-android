package mx.com.sportsworld.sw.widget;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import mx.com.sportsworld.sw.R;

/**
 * The Class HeaderAward.
 */
public class HeaderAward extends LinearLayout {
	
	/** The root view. */
	private View rootView = null;

	/**
	 * Instantiates a new header award.
	 * 
	 * @param context
	 *            the context
	 */
	public HeaderAward(Context context) {
		super(context);
		init();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Inits the.
	 */
	public void init() {
		rootView = LayoutInflater.from(getContext()).inflate(
				R.layout.header_award, null);
		addView(rootView);
	}

}
