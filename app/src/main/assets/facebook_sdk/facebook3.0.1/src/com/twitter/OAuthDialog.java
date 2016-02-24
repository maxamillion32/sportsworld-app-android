package com.twitter;

import twitter4j.TwitterException;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.facebook.android.R;
import com.util.TwitterUtil;

@SuppressLint("JavascriptInterface")
public class OAuthDialog extends DialogFragment {
 
	protected WebView webViewOauth = null;
	public static TwitterWebViewClient webclient = null;
	public static OnUserAutorizoListener listener = null;
	public static final String JS_INTERFACE = "JsInterface";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }
 
    @SuppressLint("SetJavaScriptEnabled")
	@Override
    public void onViewCreated(View arg0, Bundle arg1) {
        super.onViewCreated(arg0, arg1);
        // load the url of the oAuth login page
		try {
//			
			webViewOauth.loadUrl(SessionStore.restoreRequestToken(getActivity()).getAuthenticationURL());
			//set the web client
			webViewOauth.setWebViewClient(webclient);
			//activates JavaScript (just in case)
			webViewOauth.getSettings().setJavaScriptEnabled(true);
			webViewOauth.addJavascriptInterface(new MyJavaScriptInterface(), JS_INTERFACE);
		} catch (TwitterException e) {
			if(TwitterUtil.isDebug)
				e.printStackTrace();
		}
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        //Retrieve the webview
        View v = inflater.inflate(R.layout.oauth_screen, container, false);
        webViewOauth = (WebView) v.findViewById(R.id.web_oauth);
        webclient = new TwitterWebViewClient(getActivity());
        return v;
    }
    
    
    public class MyJavaScriptInterface {
		public void getOAuthPin(String oauthPin) {
			Log.i("TwitterWebBrowserDialog", "getOAuthPin OAUTH_PIN: " + oauthPin);
			OAuthDialog.this.dismiss();
			if(listener != null){
				listener.onUserAutorizo(oauthPin);
			}
		}
    }
    
}