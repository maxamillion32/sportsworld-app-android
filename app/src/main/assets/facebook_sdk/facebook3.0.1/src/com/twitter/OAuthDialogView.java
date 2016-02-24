package com.twitter;

import twitter4j.TwitterException;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;

import com.facebook.android.R;


@SuppressLint("JavascriptInterface")
public class OAuthDialogView extends Dialog {
 
	protected WebView webViewOauth = null;
	public static TwitterWebViewClient webclient = null;
	public static OnUserAutorizoListener listener = null;
	public static final String JS_INTERFACE = "JsInterface";
	public static Context ctx;
	
	public OAuthDialogView(Context context) {
		super(context, R.style.dialog);
		ctx= context;
		init(context);
	}
	
	public void init(Context context){
		try {
			
			View v = LayoutInflater.from(context).inflate(R.layout.oauth_screen, null);
	        webViewOauth = (WebView) v.findViewById(R.id.web_oauth);
	        webclient = new TwitterWebViewClient(context);
			webViewOauth.loadUrl(SessionStore.restoreRequestToken(context).getAuthenticationURL());
			//set the web client
			webViewOauth.setWebViewClient(webclient);
			//activates JavaScript (just in case)
			webViewOauth.getSettings().setJavaScriptEnabled(true);
			webViewOauth.addJavascriptInterface(new MyJavaScriptInterface(), JS_INTERFACE);
			addContentView(v, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			Log.i("LogIron", e.toString());
		}
	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//	    	webViewOauth= null;
//	    	webclient=null;
//	    	ctx=null;
//	    	listener=null;
//			Log.i("LogIron", "Se apreto atras");
//	    }
//	    return super.onKeyDown(keyCode, event);
//	}
    
    public class MyJavaScriptInterface {
		public void getOAuthPin(String oauthPin) {
			Log.i("TwitterWebBrowserDialog", "getOAuthPin OAUTH_PIN: " + oauthPin);
			OAuthDialogView.this.dismiss();
			if(listener != null){
				listener.onUserAutorizo(oauthPin);
			}
		}
    }
}