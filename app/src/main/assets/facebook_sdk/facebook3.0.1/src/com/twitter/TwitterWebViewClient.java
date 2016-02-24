package com.twitter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.util.TwitterUtil;

public class TwitterWebViewClient extends WebViewClient{

	public TwitterWebViewClient(Context ctx) {
		super();
	}

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    public void onReceivedError(WebView view, int errorCode,
            String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if(TwitterUtil.isDebug)
        	Log.i("TwitterWebViewClient", "onPageFinished Tweets url: " + url);
        if (url != null && (url.trim().equals("https://api.twitter.com/oauth/authorize") || 
        		url.trim().equals("https://api.twitter.com/oauth/authenticate"))){
        		view.loadUrl("javascript:" +
		        	"if(document.getElementsByTagName('code')[0] != undefined && document.getElementsByTagName('code')[0] != null){" +
		    			"window." + OAuthDialog.JS_INTERFACE + ".getOAuthPin(" +
		    				"document.getElementsByTagName('code')[0].innerHTML" +
		    			");" +
		    		"}"
        	    );
        	if(TwitterUtil.isDebug)
        		Log.i("TwitterWebViewClient", "onPageFinished He cargado la pagina...");
        }
    }
}