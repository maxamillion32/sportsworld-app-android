package com.util;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.twitter.TwitterApp;

public class TwitterUtil {
	public static boolean isDebug = true;
	public static TwitterApp twitterApp = null;
	String consumer_key="o3soqbeBv5PWpCMVYhw";
	String consumer_secret="XPunu2bWED9V0f21f7TJwxRejcxd5lihJrLd7XmniIU";

	public TwitterUtil(FragmentActivity activity) {
			twitterApp = new TwitterApp(activity,consumer_key,consumer_secret);
	}
	
	public static String recortarTexto(String txt, int numMax){
		if (txt == null){
			txt = "";
		}else if (txt.length() > numMax){
			txt = txt.substring(0, numMax - 3)+"...";
		}
		
		return txt;
	}
	 
		public static void borrarCookies(Context ctx){
			//linea obligatoria
			CookieSyncManager.createInstance(ctx);
	        CookieManager cookieManager = CookieManager.getInstance();
	        cookieManager.removeAllCookie();
		}
}

