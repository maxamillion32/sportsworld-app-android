package mx.com.sportsworld.sw.adapter;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mx.com.sportsworld.sw.fragment.ImageFragment;
import mx.com.sportsworld.sw.imgloader.ImageCache;

// TODO: Auto-generated Javadoc

/**
 * The Class ImageGalleryAdapter.
 */
public class ImageGalleryAdapter extends FragmentStatePagerAdapter {

	/** The m urls. */
	private List<String> mUrls;
	
	/** The m width px. */
	private final int mWidthPx;
	
	/** The m height px. */
	private final int mHeightPx;
	
	/** The m click. */
	private final boolean mClick;

	/**
	 * Instantiates a new image gallery adapter.
	 *
	 * @param fm the fm
	 * @param context the context
	 * @param cache the cache
	 * @param widthPx the width px
	 * @param heightPx the height px
	 * @param click the click
	 */
	public ImageGalleryAdapter(FragmentManager fm, Context context,
			ImageCache cache, int widthPx, int heightPx, boolean click) {
		super(fm);
		mUrls = new ArrayList<String>();
		mWidthPx = widthPx;
		mHeightPx = heightPx;
		mClick = click;
	}


	/* (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getItemPosition(java.lang.Object)
	 */
	@Override
	public int getItemPosition(Object object) {
		/*
		 * This way we remove old fragments and create new ones for the fresh
		 * urls on mUrls everytime we call notifyDataSetChanged()
		 */
		return PagerAdapter.POSITION_NONE;
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentStatePagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {

		return ImageFragment.newInstance(mUrls.get(position), mWidthPx,
				mHeightPx, mClick);
	}


	/* (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return mUrls.size();
	}

	/**
	 * Sets the urls.
	 *
	 * @param urls the new urls
	 */
	public void setUrls(List<String> urls) {
		mUrls.clear();
		if (urls == null) {
			return;
		}
		mUrls.addAll(urls);
		notifyDataSetChanged();
	}

	/**
	 * Sets the urls.
	 *
	 * @param urls the new urls
	 */
	public void setUrls(String[] urls) {
		mUrls.clear();
		if (urls == null) {
			return;
		}
		Collections.addAll(mUrls, urls);
		notifyDataSetChanged();
	}

}
