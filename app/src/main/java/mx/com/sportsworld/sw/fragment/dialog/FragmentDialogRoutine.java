package mx.com.sportsworld.sw.fragment.dialog;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.adapter.ImageGalleryAdapter;
import mx.com.sportsworld.sw.imgloader.ImageCache;

// TODO: Auto-generated Javadoc

/**
 * The Class FragmentDialogRoutine.
 */
public class FragmentDialogRoutine extends DialogFragment {
	
	/** The m image gallery adapter. */
	private ImageGalleryAdapter mImageGalleryAdapter;
	
	/** The m img cache. */
	private ImageCache mImgCache;
	
	/** The Constant THUMB_FOLDER. */
	private static final String THUMB_FOLDER = "exercise_thumbs";
	
	/** The m example image width. */
	int mExampleImageWidth = 0;
	
	/** The m example image height. */
	int mExampleImageHeight = 0;
	
	/** The m pager. */
	private ViewPager mPager;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_dialog_routine, null);
		mImgCache = ImageCache.findOrCreateCache(getActivity(), THUMB_FOLDER);
		mPager = (ViewPager) view.findViewById(R.id.pager);

		final ViewTreeObserver pagerViewTreeObserver = mPager
				.getViewTreeObserver();
		if (pagerViewTreeObserver.isAlive()) {
			pagerViewTreeObserver
					.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

						@SuppressWarnings("deprecation")
						// We use the new method when supported
						@SuppressLint("NewApi")
						// We check which build version we are using.
						@Override
						public void onGlobalLayout() {
							if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
								mPager.getViewTreeObserver()
										.removeGlobalOnLayoutListener(this);
							} else {
								mPager.getViewTreeObserver()
										.removeOnGlobalLayoutListener(this);
							}

							mExampleImageWidth = mPager.getWidth();
							mExampleImageHeight = mPager.getHeight();

							mImageGalleryAdapter = new ImageGalleryAdapter(
									getChildFragmentManager(), getActivity(),
									mImgCache, mExampleImageWidth,
									mExampleImageHeight,true);

							mPager.setAdapter(mImageGalleryAdapter);
						}

					});
		}

	
		return view;
	}
	
	/**
	 * Adds the urls.
	 *
	 * @param urls the urls
	 */
	public void addUrls(List<String> urls){
		mImageGalleryAdapter.setUrls(urls);
	}
}
