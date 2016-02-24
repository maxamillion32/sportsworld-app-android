package mx.com.sportsworld.sw.fragment.dialog;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.adapter.RoutineTourAdapter;

// TODO: Auto-generated Javadoc

/**
 * The Class FragmentDialogTour.
 */
public class FragmentDialogRoutineInfo extends DialogFragment {

	/** The img view. */
	ImageView imgView;

	private static OnClickListener clickListener;
	private RoutineTourAdapter rutineAdapter;

	/** The view pager. */
	private ViewPager viewPager;

	/** The m images. */
	public int[] mImages;

	/**
	 * Gets the m images.
	 * 
	 * @return the m images
	 */
	public int[] getmImages() {
		return mImages;
	}

	/**
	 * Sets the m images.
	 * 
	 * @param mImages
	 *            the new m images
	 */
	public void setmImages(int[] mImages) {
		this.mImages = mImages;
	}

	/**
	 * New instance.
	 * 
	 * @return the fragment dialog tour
	 */
	public static FragmentDialogRoutineInfo newInstance(OnClickListener clickL) {
		clickListener = clickL;
		return new FragmentDialogRoutineInfo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_dialog_tour, null);
		imgView = (ImageView) view.findViewById(R.id.imgViewTranslateGuide);
		imgView.setImageResource(R.drawable.pnts_rutina_01);
		viewPager = (ViewPager) view.findViewById(R.id.viewPageTour);
		rutineAdapter = new RoutineTourAdapter(getActivity(), getmImages(),
				clickListener);
		viewPager.setAdapter(rutineAdapter);

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageSelected(int position) {
				if (position == 0)
					imgView.setImageResource(R.drawable.pnts_rutina_01);

				if (position == 1)
					imgView.setImageResource(R.drawable.pnts_rutina_02);

				if (position == 2)
					imgView.setImageResource(R.drawable.pnts_rutina_03);

				if (position == 3)
					imgView.setImageResource(R.drawable.pnts_rutina_04);

			}

		});

		return view;
	}
}
