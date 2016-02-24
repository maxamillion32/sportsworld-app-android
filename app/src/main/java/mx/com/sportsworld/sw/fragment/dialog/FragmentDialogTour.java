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
import android.view.ViewGroup;
import android.widget.ImageView;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.events.ClickEventInterface;
import mx.com.sportsworld.sw.adapter.TourAdapter;

// TODO: Auto-generated Javadoc

/**
 * The Class FragmentDialogTour.
 */
public class FragmentDialogTour extends DialogFragment {

	/** The img view. */
	ImageView imgView;

	/** The view pager. */
	private ViewPager viewPager;

	/** The m images. */
	public int[] mImages;

	private ClickEventInterface clickInterface;

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
	public static FragmentDialogTour newInstance() {
		return new FragmentDialogTour();
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
		imgView.setImageResource(R.drawable.pnts_01);
		viewPager = (ViewPager) view.findViewById(R.id.viewPageTour);
		TourAdapter imgAdapter = new TourAdapter(getActivity(), getmImages(),
				clickInterface);
		viewPager.setAdapter(imgAdapter);

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
					imgView.setImageResource(R.drawable.pnts_01);

				if (position == 1)
					imgView.setImageResource(R.drawable.pnts_02);

				if (position == 2)
					imgView.setImageResource(R.drawable.pnts_03);

				if (position == 3)
					imgView.setImageResource(R.drawable.pnts_04);

				if (position == 4)
					imgView.setImageResource(R.drawable.pnts_05);

				if (position == 5)
					imgView.setImageResource(R.drawable.pnts_06);

				if (position == 6)
					imgView.setImageResource(R.drawable.pnts_07);

			}

		});

		return view;
	}

	public void setClickInterface(ClickEventInterface cInterface) {
		clickInterface = cInterface;
	}
}
