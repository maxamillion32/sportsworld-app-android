package com.upster.app.widget;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.NutritionAdviceActivity;

/**
 * The Class FooterView.
 */
public class FooterView extends LinearLayout {

	
	/** The activity. */
	private Activity activity;
	
	/** The root view. */
	private View rootView = null;
	
	/** The txt nutricion. */
	private LinearLayout txtNutricion = null;
	
	/**
	 * Instantiates a new footer view.
	 * 
	 * @param activity
	 *            the activity
	 */
	public FooterView(Activity activity) {
		super(activity);
		this.activity = activity;
		init();
	}
	
	/**
	 * Inits the.
	 */
	public void init(){
		rootView = LayoutInflater.from(getContext()).inflate(R.layout.footer_award, null);
		txtNutricion = (LinearLayout)rootView.findViewById(R.id.textoNutricion);
		txtNutricion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent showNutritionAdvice = new Intent(getContext(), NutritionAdviceActivity.class);
				activity.startActivity(showNutritionAdvice);
			}
		});
		addView(rootView);
	}
}
