package mx.com.sportsworld.sw.fragment.dialog;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.imgloader.ImageFetcher;

// TODO: Auto-generated Javadoc

/**
 * The Class FragmentFacilitiesImgDialog.
 */
public class FragmentFacilitiesImgDialog extends DialogFragment {

	/** The url img. */
	public static String urlImg = "";
	
	/** The name img. */
	public static String nameImg = "";
	
	/** The img facilitie. */
	private File imgFacilitie;
	
	/** The img show. */
	private ImageView imgShow;
	
	/** The txt facilitie. */
	private TextView txtFacilitie;
	
	/** The df. */
	public DialogFragment df;
	
	/** The bitmap. */
	public Bitmap bitmap;
	
	/** The progress. */
	private ProgressDialogFragment progress;
	
	/** The dialgo fragment. */
	Fragment dialgoFragment;

	/**
	 * New instance.
	 *
	 * @param url the url
	 * @param name the name
	 * @return the fragment facilities img dialog
	 */
	public static FragmentFacilitiesImgDialog newInstance(String url,
			String name) {
		urlImg = url;
		nameImg = name;
		return new FragmentFacilitiesImgDialog();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onStart()
	 */
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			final Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_facilities_img, null);
		imgShow = (ImageView) view.findViewById(R.id.imgFacilitie);
		txtFacilitie = (TextView) view.findViewById(R.id.txtViewFacilities);
		if (savedInstanceState == null) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					imgFacilitie = ImageFetcher.downloadBitmapToFile(
							getActivity(), urlImg, "imgFacilitie");
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inPreferredConfig = Bitmap.Config.ARGB_8888;
					if (bitmap == null)
						bitmap = BitmapFactory.decodeFile(
								imgFacilitie.getAbsolutePath(), options);
					if (getActivity() != null)
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								imgShow.setImageBitmap(bitmap);
								txtFacilitie.setText(nameImg);
								progress.dismissAllowingStateLoss();
							}
						});

				}
			}).start();
			showFragmentDialog(false);
		} else {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					imgShow.setImageBitmap(bitmap);
					txtFacilitie.setText(nameImg);

				}
			});

			showFragmentDialog(true);
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
		progress = null;
		dialgoFragment = null;
		bitmap = null;
		df = null;
		imgShow = null;
		imgFacilitie = null;
		System.gc();
		super.onDestroy();
	}

}
