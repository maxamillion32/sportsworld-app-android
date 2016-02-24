package mx.com.sportsworld.sw.activity;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import com.actionbarsherlock.app.SherlockActivity;
import mx.com.sportsworld.sw.R;

// TODO: Auto-generated Javadoc

/**
 * The Class SplashScreenActivity.
 */
public class SplashScreenActivity extends SherlockActivity {

	private boolean reproducido = false;
	/** The s handler. */
	private static Handler sHandler = new Handler();
	
	/** The m app starter. */
	private Runnable mAppStarter;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);

		if(1 != 1) {
			try {
				VideoView videoHolder = (VideoView) this.findViewById(R.id.videoView1);
				Uri video = Uri.parse("android.resource://" + getPackageName()
						+ "/" + R.raw.splashupsterfull);
				videoHolder.setVideoURI(video);

				videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

					public void onCompletion(MediaPlayer mp) {
						startApp();
						reproducido = true;
					}

				});
				videoHolder.start();
			} catch (Exception ex) {
				startApp();
				reproducido = true;
			}
		}else{
			startApp();
			reproducido = true;
		}
		/*
		if (savedInstanceState == null) {
			startApp();
		} else {
			startApp();
		}*/
	}

	/**
	 * Start app.
	 */
	public void startApp() {
		final Intent mainboard = new Intent(this /* context */,
				MainboardActivity.class);
		mainboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mainboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(mainboard);
		finish();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();

		if (mAppStarter == null && reproducido) {
			startApp();
		}
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockActivity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		if (mAppStarter != null) {
			sHandler.removeCallbacks(mAppStarter);
			//mAppStarter = null;
		}
	}

}
