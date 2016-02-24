package com.upster.app.widget;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import mx.com.sportsworld.sw.R;

/**
 * The Class ImageTextCardView.
 */
public class ImageTextCardView extends LinearLayout {

	/** The image. */
	public ImageView image;
	
	/** The text. */
	public TextView text;

	/**
	 * Instantiates a new image text card view.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public ImageTextCardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflateLayout(context);

	}

	/**
	 * Instantiates a new image text card view.
	 * 
	 * @param context
	 *            the context
	 */
	public ImageTextCardView(Context context) {
		super(context);
		inflateLayout(context);
	}

	/**
	 * Inflate layout.
	 * 
	 * @param context
	 *            the context
	 */
	private void inflateLayout(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.image_text_card, this);
		image = (ImageView) view.findViewById(R.id.image);
		text = (TextView) view.findViewById(R.id.description);
	}

}
