package mx.com.sportsworld.sw.widget;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.utils.TextJustify;

//Footer de la lista de recompensas

/**
 * The Class FooterAwardView.
 */
public class FooterAwardView extends LinearLayout {

	/** The root view. */
	private View rootView = null;
	
	/** The txt redimidos. */
	private TextView txtRedimidos = null;
	
	/** The txt saldo. */
	private TextView txtSaldo = null;
	
	/** The txt address. */
	private TextView txtAddress = null;

	/**
	 * Instantiates a new footer award view.
	 * 
	 * @param activity
	 *            the activity
	 */
	public FooterAwardView(Activity activity) {
		super(activity);
		init();
	}

	/**
	 * Inits the.
	 */
	public void init() {
		rootView = LayoutInflater.from(getContext()).inflate(
				R.layout.footer_award, null);
		txtRedimidos = (TextView) rootView
				.findViewById(R.id.txt_award_redimidos);
		txtSaldo = (TextView) rootView.findViewById(R.id.txt_view_saldo);
		txtAddress = (TextView) rootView.findViewById(R.id.txt_award_adress);
		TextJustify.run(((TextView)rootView.findViewById(R.id.txt_award_adress)), 305f);

		addView(rootView);
	}

	/**
	 * Sets the redimidos.
	 * 
	 * @param text
	 *            the new redimidos
	 */
	public void setRedimidos(String text) {
		txtRedimidos.setText(text);
	}

	/**
	 * Sets the saldo.
	 * 
	 * @param text
	 *            the new saldo
	 */
	public void setSaldo(String text) {
		txtSaldo.setText(text);
	}

	/**
	 * Sets the address.
	 * 
	 * @param text
	 *            the new address
	 */
	public void setAddress(String text) {
		txtAddress.setText(text);
	}

}
