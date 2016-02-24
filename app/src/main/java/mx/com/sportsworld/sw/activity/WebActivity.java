package mx.com.sportsworld.sw.activity;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.app.AuthSherlockFragmentActivity;
import mx.com.sportsworld.sw.fragment.dialog.ProgressDialogFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.VideoView;


@SuppressLint("SetJavaScriptEnabled")
public class WebActivity extends AuthSherlockFragmentActivity{
	
	private WebView web_view;
	public static final String URL = "";
    private static final String TAG = "Class WebActivity";
    Fragment dialgoFragment;
    public static ProgressDialogFragment progress;
    
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (isFinishing()) {
			return;
		}
		getSupportActionBar().setTitle(R.string.recorrido);
		showFragmentsDialog(false);
		setContentView(R.layout.web_view);
		web_view = (WebView) this.findViewById(R.id.webView1);
		/*web_view.getSettings().setJavaScriptEnabled(true);
		web_view.getSettings().setPluginState(PluginState.OFF);
		web_view.setWebChromeClient(new WebChromeClient()); 
		web_view.setWebViewClient(new WebViewClient()); 
		//web_view.getSettings().setAllowFileAccess(false);
		//web_view.getSettings().setPluginsEnabled(true);
		web_view.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
            }
        });
		
		//web_view.getSettings().setAppCacheEnabled(true);
		String turl = getIntent().getStringExtra(URL);
		web_view.loadUrl(turl);
		ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		android.app.ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo(); 
		activityManager.getMemoryInfo(memoryInfo);

		boolean onLowMemory = memoryInfo.lowMemory;*/
		WebSettings ws = web_view.getSettings();
		//WebView mWebView = (WebView) findViewById(R.id.wvVideo);
		web_view.setWebViewClient(new WebViewClient());
		web_view.setWebChromeClient(new WebChromeClient());
		web_view.getSettings().setJavaScriptEnabled(true);
		//web_view.getSettings().setPluginsEnabled(true);
		web_view.getSettings().setRenderPriority(RenderPriority.HIGH);
		web_view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		web_view.getSettings().getJavaScriptCanOpenWindowsAutomatically();
		web_view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		web_view.getSettings().setRenderPriority(RenderPriority.HIGH);
		web_view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		String turl = getIntent().getStringExtra(URL);
		//String turl = "http://html5demos.com/canvas-grad";
		web_view.loadUrl(turl);
		web_view.setWebViewClient(new WebViewClient() {

			   public void onPageFinished(WebView view, String url) {
			        // do your stuff here
				   Log.d("entra", "URL");
				   progress.dismiss();
			    }
			});
	}
	private WebChromeClient chromeClient = new WebChromeClient(){

	    @Override
	    public void onShowCustomView(View view, CustomViewCallback callback) {
	        // TODO Auto-generated method stub
	        super.onShowCustomView(view, callback); 
	        if(view instanceof FrameLayout){
	            FrameLayout frame  = (FrameLayout)view;
	            if(frame.getFocusedChild()instanceof VideoView){
	            VideoView video =  (VideoView)frame.getFocusedChild();
	                frame.removeView(video);
	                           video.start();
	                           

	            }
	        }

	    }

	};
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	//Sirve para guardar dentro del Bundle cierto estado, esto es la posicion actual en donde estamos
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
	}
	
	public void onReceivedError(WebView view, int errorCod,String description, String failingUrl) {
        Toast.makeText(getApplicationContext(), "Your Internet Connection May not be active Or " + description, Toast.LENGTH_LONG).show();
    }
	
	/*web_view = (WebView) this.findViewById(R.id.webView1);
		
		url ="https://www.sportsworld.com.mx/";
		
		web_view.loadUrl(url);*/
	
	/**
	 * Show fragments dialog.
	 *
	 * @param savedInstanceState the saved instance state
	 */
	public void showFragmentsDialog(boolean savedInstanceState) {
		dialgoFragment = getSupportFragmentManager().findFragmentByTag(
				ProgressDialogFragment.progressDialogTag);

		if (savedInstanceState == true) {
			dialgoFragment = getSupportFragmentManager().findFragmentByTag(
					ProgressDialogFragment.progressDialogTag);
			progress = (ProgressDialogFragment) dialgoFragment;

		} else {

			if (dialgoFragment != null) {
				getSupportFragmentManager().beginTransaction()
						.remove(dialgoFragment).commit();
			}

			progress = ProgressDialogFragment
					.newInstance(WebActivity.this);
			progress.show(getSupportFragmentManager(),
					ProgressDialogFragment.progressDialogTag);
		}

	}
}
