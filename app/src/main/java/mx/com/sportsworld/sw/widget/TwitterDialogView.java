package mx.com.sportsworld.sw.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.net.http.SslError;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.events.TwitterResponseInterface;

/**
 * The Class TwitterDialogView.
 */
@SuppressLint("JavascriptInterface")
public class TwitterDialogView extends Dialog {

	/** The web view oauth. */
	protected WebView webViewOauth = null;
	
	/** The ctx. */
	public static Context ctx;
	
	/** The web url. */
	public static String webUrl;
	
	/** The Constant TWITTER_CALLBACK_URL. */
	static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
	
	/** The response interface. */
	private static TwitterResponseInterface responseInterface;

	/**
	 * Instantiates a new twitter dialog view.
	 * 
	 * @param context
	 *            the context
	 * @param url
	 *            the url
	 * @param mInterface
	 *            the m interface
	 */
	public TwitterDialogView(Context context, String url,
			TwitterResponseInterface mInterface) {
		super(context, R.style.dialog);
		ctx = context;
		webUrl = url;
		responseInterface = mInterface;
		init(context);
	}

	/**
	 * Inits the.
	 * 
	 * @param context
	 *            the context
	 */
	@SuppressLint("SetJavaScriptEnabled")
	public void init(Context context) {
		View v = LayoutInflater.from(context).inflate(
				R.layout.alert_dialog_twitter, null);
		webViewOauth = (WebView) v.findViewById(R.id.web_oauth);
		WebSettings mWebSettings = webViewOauth.getSettings();
		mWebSettings.setBuiltInZoomControls(true);
		webViewOauth.loadUrl(webUrl);
		webViewOauth.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webViewOauth.setScrollbarFadingEnabled(false);

		Log.i("kokusho", webUrl);
		// activates JavaScript (just in case)
		webViewOauth.setWebViewClient(new WebViewClient() {
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				handler.proceed();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub

				if (url.startsWith(TWITTER_CALLBACK_URL)) {
					// Parse further to extract function and do custom action
					Log.i("kokusho", url);
					responseInterface.urlCallback(url);
				} else {
					// Load the page via the webview
					view.loadUrl(url);
				}
				return true;

			}

		});
		webViewOauth.getSettings().setJavaScriptEnabled(true);
		addContentView(v, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

	}

}