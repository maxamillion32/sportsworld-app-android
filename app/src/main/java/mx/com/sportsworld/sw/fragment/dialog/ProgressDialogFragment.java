package mx.com.sportsworld.sw.fragment.dialog;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import mx.com.sportsworld.sw.R;

// TODO: Auto-generated Javadoc

/**
 * The Class ProgressDialogFragment.
 */
public class ProgressDialogFragment extends DialogFragment {

	/** The progress dialog tag. */
	public static String progressDialogTag = "FragmentDialog";
	
	/** The timer. */
	public static CountDownTimer timer;
	
	/** The activity. */
	public static Activity activity;
	
	/** The dialog. */
	public static Dialog dialog;

	/**
	 * New instance.
	 *
	 * @param cxt the cxt
	 * @return the progress dialog fragment
	 */
	public static ProgressDialogFragment newInstance(Activity cxt) {
		ProgressDialogFragment frag = new ProgressDialogFragment();
		createTimer();
		activity = cxt;
		return frag;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
/*
		dialog = new ProgressDialog(getActivity());
		dialog.setMessage(getString(R.string.dialog_download_message));
		dialog.setTitle(getString(R.string.dialog_download_title));
		dialog.setIcon(R.drawable.icono_upster);
		dialog.setIndeterminate(true);
		dialog.setCanceledOnTouchOutside(false);
*/

		Context mContext = getActivity();
		dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog);
		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText(getString(R.string.dialog_download_message));
		dialog.setCanceledOnTouchOutside(false);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Planer_Reg.ttf");
		text.setTypeface(font);

		/*
		ImageView image = (ImageView) dialog.findViewById(R.id.image);
		image.setImageResource(R.drawable.hr_max);
		*/
		return dialog;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onStart()
	 */
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		if (timer != null)
			timer.start();
		super.onStart();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (timer != null)
			timer.cancel();
		super.onDestroy();
	}

	/**
	 * Creates the timer.
	 */
	public static void createTimer() {
		timer = new CountDownTimer(20000, 5000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				Log.i("IronTag", "Se tardo mucho el loader");
			}

			@Override
			public void onFinish() {
				dialog.setCancelable(true);
			}
		};
	}

}
