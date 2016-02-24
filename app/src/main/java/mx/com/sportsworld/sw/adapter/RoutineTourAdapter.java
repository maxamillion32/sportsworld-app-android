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

// TODO: Auto-generated Javadoc

/**
 * The Class TourAdapter.
 */
public class RoutineTourAdapter extends PagerAdapter {

	/** The context. */
	public Activity context;
	public OnClickListener clickListener;

	/** The m images. */
	public static int[] mImages = new int[] { R.drawable.tour_rutine_01,
			R.drawable.tour_rutine_02, R.drawable.tour_rutine_03,
			R.drawable.tour_rutine_04 };

	/**
	 * Instantiates a new tour adapter.
	 * 
	 * @param context
	 *            the context
	 * @param imagenes
	 *            the imagenes
	 */
	public RoutineTourAdapter(Activity mcontext, int[] imagenes,
			OnClickListener clickL) {
		context = mcontext;
		clickListener = clickL;
	}

	@Override
	public void destroyItem(View container, int arg1, Object object) {
		((ViewPager) container).removeView((ImageView) object);
	}

	@Override
	public void finishUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCount() {
		return mImages.length;
	}

	@Override
	public Object instantiateItem(View container, final int position) {
		ImageView imageView = new ImageView(context);
		imageView.setScaleType(ImageView.ScaleType.MATRIX);
		imageView.setAdjustViewBounds(true);
		imageView.setBackgroundResource(mImages[position]);
//		imageView.setOnClickListener(clickListener);
		((ViewPager) container).addView(imageView, 0);
		return imageView;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((ImageView) object);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

}
