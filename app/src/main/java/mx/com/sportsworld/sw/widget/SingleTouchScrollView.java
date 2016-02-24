package com.upster.app.widget;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * The Class SingleTouchScrollView.
 */
public class SingleTouchScrollView extends ScrollView {

	/** The m gesture detector. */
	private GestureDetector mGestureDetector;
	
	/** The m gesture listener. */
	View.OnTouchListener mGestureListener;

	/**
	 * Instantiates a new single touch scroll view.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public SingleTouchScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(context, new YScrollDetector());
		setFadingEdgeLength(0);
	}

	/* (non-Javadoc)
	 * @see android.widget.ScrollView#onInterceptTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev)
				&& mGestureDetector.onTouchEvent(ev);
	}

	// Return false if we're scrolling in the x direction
	/**
	 * The Class YScrollDetector.
	 */
	class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
		
		/* (non-Javadoc)
		 * @see android.view.GestureDetector.SimpleOnGestureListener#onScroll(android.view.MotionEvent, android.view.MotionEvent, float, float)
		 */
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (Math.abs(distanceY) > Math.abs(distanceX)) {
				return true;
			}
			return false;
		}
	}
}
