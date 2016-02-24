package mx.com.sportsworld.sw.fragment;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.fragment.dialog.FragmentFullImageDialog;
import mx.com.sportsworld.sw.imgloader.ImageCache;
import mx.com.sportsworld.sw.imgloader.ImageFetcher;
import mx.com.sportsworld.sw.imgloader.ImageFetcher.ImageFetcherParams;
import mx.com.sportsworld.sw.utils.ConnectionUtils;

// TODO: Auto-generated Javadoc

/**
 * The Class ImageFragment.
 */
public class ImageFragment extends Fragment {

	/** The Constant FRAG_IMG_WIDTH_PX. */
	private static final String FRAG_IMG_WIDTH_PX = "frag_img_width_px";
	
	/** The Constant FRAG_IMG_HEIGHT_PX. */
	private static final String FRAG_IMG_HEIGHT_PX = "frag_img_height_px";
	
	/** The Constant FRAG_ARG_URL. */
	private static final String FRAG_ARG_URL = "frag_arg_url";
	
	/** The Constant THUMB_FOLDER. */
	private static final String THUMB_FOLDER = "branch_images_thumbs";
	
	/** The Constant TAG. */
	public static final String TAG = "fragment_edit_name_2";
	
	/** The m mgv img. */
	private ImageView mMgvImg;
	
	/** The m img fetcher. */
	private static ImageFetcher mImgFetcher;
	
	/** The m img cache. */
	private ImageCache mImgCache;
	
	/** The m click. */
	private static boolean mClick;

	/**
	 * Instantiates a new image fragment.
	 */
	public ImageFragment() {
	}

	/**
	 * New instance.
	 *
	 * @param url the url
	 * @param dimenHeight the dimen height
	 * @param dimenWidth the dimen width
	 * @param click the click
	 * @return the image fragment
	 */
	public static ImageFragment newInstance(String url, int dimenHeight,
			int dimenWidth, boolean click) {
		final ImageFragment f = new ImageFragment();
		final Bundle args = new Bundle();
		args.putString(FRAG_ARG_URL, url);
		args.putInt(FRAG_IMG_WIDTH_PX, dimenWidth);
		args.putInt(FRAG_IMG_HEIGHT_PX, dimenHeight);
		mClick = click;
		f.setArguments(args);
		return f;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.fragment_image, null /* root */,
				false /* attachToRoot */);
		mMgvImg = (ImageView) v.findViewById(R.id.mgv_image);
		mMgvImg.setTag(getArguments().getString(FRAG_ARG_URL));

		if (mClick)
			mMgvImg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					try {

						if (isNetworkAvailable())
							showFragmentImage(mMgvImg.getTag() + "", null);
						else {
							v.setDrawingCacheEnabled(true);
							Bitmap bitmap = Bitmap.createBitmap(v
									.getDrawingCache());
							v.setDrawingCacheEnabled(false);
							showFragmentImage(mMgvImg.getTag() + "", bitmap);
						}

					} catch (Exception ex) {

					}
				}
			});
		return mMgvImg;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mImgCache = ImageCache.findOrCreateCache(getActivity(), THUMB_FOLDER);

		final Context context = getActivity().getApplicationContext();
		if (mImgFetcher == null)
			mImgFetcher = new ImageFetcher(context, mImgCache,
					R.drawable.back_transparente);
		final ImageFetcherParams params = new ImageFetcherParams();
		params.imageWidth = getWidthFromArgs();
		params.imageHeight = getHeightFromArgs();
		mImgFetcher.setFetcherParams(params);

		mImgFetcher.loadSampledThumbnailImage(getUrlFromArgs(), mMgvImg);
	}

	/**
	 * Gets the url from args.
	 *
	 * @return the url from args
	 */
	private String getUrlFromArgs() {
		return getArguments().getString(FRAG_ARG_URL);
	}

	/**
	 * Gets the width from args.
	 *
	 * @return the width from args
	 */
	private int getWidthFromArgs() {
		return getArguments().getInt(FRAG_IMG_WIDTH_PX);
	}

	/**
	 * Gets the height from args.
	 *
	 * @return the height from args
	 */
	private int getHeightFromArgs() {
		return getArguments().getInt(FRAG_IMG_HEIGHT_PX);
	}

	/**
	 * Show fragment image.
	 *
	 * @param urlsImg the urls img
	 * @param bitmap the bitmap
	 */
	private void showFragmentImage(String urlsImg, Bitmap bitmap) {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		FragmentFullImageDialog editNameDialog = FragmentFullImageDialog
				.newInstance(urlsImg, bitmap);
		editNameDialog.setStyle(DialogFragment.STYLE_NO_TITLE,
				android.R.style.Theme_DeviceDefault);
		editNameDialog.show(fm, TAG);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/**
	 * Checks if is network available.
	 *
	 * @return true, if is network available
	 */
	public boolean isNetworkAvailable() {
		return ConnectionUtils.isNetworkAvailable(getActivity());
	}
}