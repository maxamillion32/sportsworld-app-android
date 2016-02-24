package mx.com.sportsworld.sw.widget;

/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */


import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ImageButton;

import mx.com.sportsworld.sw.R;

/**
 * This class exists purely to cancel long click events, that got started in
 * NumberPicker.
 */
public class NumberPickerButton extends ImageButton {

    /** The m number picker. */
    private NumberPicker mNumberPicker;

    /**
	 * Instantiates a new number picker button.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 * @param defStyle
	 *            the def style
	 */
    public NumberPickerButton(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
	 * Instantiates a new number picker button.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
    public NumberPickerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
	 * Instantiates a new number picker button.
	 * 
	 * @param context
	 *            the context
	 */
    public NumberPickerButton(Context context) {
        super(context);
    }

    /**
	 * Sets the number picker.
	 * 
	 * @param picker
	 *            the new number picker
	 */
    public void setNumberPicker(NumberPicker picker) {
        mNumberPicker = picker;
    }

    /* (non-Javadoc)
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        cancelLongpressIfRequired(event);
        return super.onTouchEvent(event);
    }

    /* (non-Javadoc)
     * @see android.view.View#onTrackballEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        cancelLongpressIfRequired(event);
        return super.onTrackballEvent(event);
    }

    /* (non-Javadoc)
     * @see android.view.View#onKeyUp(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
                || (keyCode == KeyEvent.KEYCODE_ENTER)) {
            cancelLongpress();
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
	 * Cancel longpress if required.
	 * 
	 * @param event
	 *            the event
	 */
    private void cancelLongpressIfRequired(MotionEvent event) {
        if ((event.getAction() == MotionEvent.ACTION_CANCEL)
                || (event.getAction() == MotionEvent.ACTION_UP)) {
            cancelLongpress();
        }
    }

    /**
	 * Cancel longpress.
	 */
    private void cancelLongpress() {
        if (R.id.increment == getId()) {
            mNumberPicker.cancelIncrement();
        } else if (R.id.decrement == getId()) {
            mNumberPicker.cancelDecrement();
        }
    }
}
