package mx.com.sportsworld.sw.adapter;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.events.ClickEventInterface;

// TODO: Auto-generated Javadoc

/**
 * The Class TourAdapter.
 */
public class TourAdapter extends PagerAdapter {

	/** The context. */
	public static Activity context;

	/** The m images. */
	public static int[] mImages = new int[] { R.drawable.and01,
			R.drawable.and02, R.drawable.and03, R.drawable.and04,
			R.drawable.and05, R.drawable.and06, R.drawable.and07,
			R.drawable.and08, R.drawable.and09 };
	private ClickEventInterface clickInterface;

	/**
	 * Instantiates a new tour adapter.
	 * 
	 * @param context
	 *            the context
	 * @param imagenes
	 *            the imagenes
	 */
	public TourAdapter(Activity context, int[] imagenes,
			ClickEventInterface cInterface) {
		TourAdapter.context = context;
		clickInterface = cInterface;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#destroyItem(android.view.View,
	 * int, java.lang.Object)
	 */
	@Override
	public void destroyItem(View container, int arg1, Object object) {
		((ViewPager) container).removeView((ImageView) object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#finishUpdate(android.view.View)
	 */
	@Override
	public void finishUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return mImages.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.PagerAdapter#instantiateItem(android.view.View,
	 * int)
	 */
	@Override
	public Object instantiateItem(View container, final int position) {
		ImageView imageView = new ImageView(context);
		imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		imageView.setBackgroundResource(mImages[position]);
		if (position == 8)
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					clickInterface.onCustomClickListener();
				}
			});
		((ViewPager) container).addView(imageView, 0);
		return imageView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.PagerAdapter#isViewFromObject(android.view.View,
	 * java.lang.Object)
	 */
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((ImageView) object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.PagerAdapter#restoreState(android.os.Parcelable,
	 * java.lang.ClassLoader)
	 */
	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#saveState()
	 */
	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#startUpdate(android.view.View)
	 */
	@Override
	public void startUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

}
