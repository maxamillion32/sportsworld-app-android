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
 * The Class HeaderTableView.
 */
public class HeaderTableView extends LinearLayout {
	
	/** The root view. */
	private View rootView = null;

	/**
	 * Instantiates a new header table view.
	 * 
	 * @param context
	 *            the context
	 */
	public HeaderTableView(Context context) {
		super(context);
		init();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Inits the.
	 */
	public void init() {
		rootView = LayoutInflater.from(getContext()).inflate(
				R.layout.header_loyalty_transactions, null);
		addView(rootView);
	}

}
