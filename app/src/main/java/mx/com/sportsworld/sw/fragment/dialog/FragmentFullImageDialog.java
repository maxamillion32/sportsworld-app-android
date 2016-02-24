package mx.com.sportsworld.sw.fragment.dialog;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.io.File;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.imgloader.ImageFetcher;

// TODO: Auto-generated Javadoc

/**
 * The Class FragmentFullImageDialog.
 */
@SuppressLint("ValidFragment")
public class FragmentFullImageDialog extends DialogFragment {
	
	/** The bit image. */
	private static Bitmap bitImage;
	
	/** The url img. */
	private static String urlImg;
	
	/** The img facilitie. */
	private File imgFacilitie;
	
	/** The imagen. */
	private ImageView imagen;
	
	/** The progress. */
	private ProgressDialogFragment progress;
	
	/** The dialgo fragment. */
	Fragment dialgoFragment;

	/**
	 * New instance.
	 *
	 * @param url the url
	 * @param bitmage the bitmage
	 * @return the fragment full image dialog
	 */
	public static FragmentFullImageDialog newInstance(String url, Bitmap bitmage) {
		urlImg = url;
		bitImage = bitmage;
		FragmentFullImageDialog frag = new FragmentFullImageDialog();
		return frag;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_full_image, null);

		imagen = ((ImageView) view.findViewById(R.id.imageView2));
		if (savedInstanceState == null) {
			showFragmentDialog(false);
			if (bitImage == null)
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						imgFacilitie = ImageFetcher.downloadBitmapToFile(
								getActivity(), urlImg, "imgFacilitie");
						Log.i("kokusho", urlImg);
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inPreferredConfig = Bitmap.Config.ARGB_8888;
						bitImage = BitmapFactory.decodeFile(
								imgFacilitie.getAbsolutePath(), options);
						if (getActivity() != null)
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									imagen.setImageBitmap(bitImage);
									progress.dismissAllowingStateLoss();
								}
							});

					}
				}).start();
			else {
				imagen.setImageBitmap(bitImage);
				progress.dismissAllowingStateLoss();
			}

		} else {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					imagen.setImageBitmap(bitImage);

				}
			});

		}

		return view;

	}

	/**
	 * Show fragment dialog.
	 *
	 * @param savedInstanceState the saved instance state
	 */
	public void showFragmentDialog(boolean savedInstanceState) {
		dialgoFragment = getFragmentManager().findFragmentByTag(
				ProgressDialogFragment.progressDialogTag);

		if (savedInstanceState == true) {
			dialgoFragment = getFragmentManager().findFragmentByTag(
					ProgressDialogFragment.progressDialogTag);
			progress = (ProgressDialogFragment) dialgoFragment;

		} else {

			if (dialgoFragment != null) {
				getFragmentManager().beginTransaction().remove(dialgoFragment)
						.commit();
			}
			progress = ProgressDialogFragment.newInstance(getActivity());
			progress.show(getFragmentManager(),
					ProgressDialogFragment.progressDialogTag);
		}

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
