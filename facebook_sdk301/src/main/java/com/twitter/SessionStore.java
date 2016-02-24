/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * Administra la sesi??n de un usuario logueado en Twitter
 * 
 * @author Angel L??pez
 */
public class SessionStore {
    
    private static final String ACCESS_TOKEN = "tw_access_token";
    private static final String ACCESS_TOKEN_SECRET = "tw_access_token_secret";
    private static final String REQUEST_TOKEN = "tw_request_token";
    private static final String REQUEST_TOKEN_SECRET = "tw_request_token_secret";
    private static final String KEY_REQUEST = "twitter-session-request";
    private static final String KEY_ACCESS = "twitter-session-access";
    
    
    /**
     * Almacena el <code>AccessToken</code> en memoria persistente
     * 
     * @param context context necesario para poder almacenar informaci??n
     * @param at <code>AccessToken</code> a almacenar
     * @return <code>true</code> si el <code>AccessToken</code> se almacen?? satisfactoriamente. De lo contrario regresa <code>false</code> 
     */
    public static boolean saveAccessToken(Context context, AccessToken at) {
        Editor editor = context.getSharedPreferences(KEY_ACCESS, Context.MODE_PRIVATE).edit();
        editor.putString(ACCESS_TOKEN, at.getToken());
        editor.putString(ACCESS_TOKEN_SECRET, at.getTokenSecret());
        return editor.commit();
    }

    
    /**
     * Obtiene un acess token de memoria persistente y lo establece en las configuraciones de Twitter
     * 
     * @param context contexto necesario para acceder a la memoria persistente
     * @param twitter instancia de <code>Twitter</code> en donde se reestablecer?? el <code>AccessToken</code> obtenido
     * 
     * @return <code>true</code> si se obtuvo un <code>AccessToken</code> correctamente. De lo contrario regresa <code>false</code>
     * y elimina las cookies del navegador
     */
    public static boolean restoreAccessToken(Context context, Twitter twitter) {
        SharedPreferences savedSession = context.getSharedPreferences(KEY_ACCESS, Context.MODE_PRIVATE);
        
        String token = savedSession.getString(ACCESS_TOKEN, null);
        String tokenSecret = savedSession.getString(ACCESS_TOKEN_SECRET, null);
        
        if (token == null || tokenSecret == null){
        	Log.i("SessionStore", "restoreAccessToken No hay accessToken.");
        	//si no hay accesstoken, borramos cookies para asegurarnos que el user no este iniciado en sesi???n
        	borrarCookies(context);
        	
        	//tambi???n borramos el requestToken almacenado
        	clearRequestToken(context);
        	return false;
        }
        
        twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));
        return true;
    }
    
    
    /**
     * Almacena el <code>RequestToken</code> en memoria persistente
     * 
     * @param context context necesario para poder almacenar informaci??n
     * @param rt <code>RequestToken</code> a almacenar
     * @return <code>true</code> si el <code>RequestToken</code> se almacen?? satisfactoriamente. De lo contrario regresa <code>false</code> 
     */
    public static boolean saveRequestToken(Context context, RequestToken rt) {
        Editor editor = context.getSharedPreferences(KEY_REQUEST, Context.MODE_PRIVATE).edit();
        editor.putString(REQUEST_TOKEN, rt.getToken());
        editor.putString(REQUEST_TOKEN_SECRET, rt.getTokenSecret());
        return editor.commit();
    }

    
    /**
     * Obtiene un request token de memoria persistente y lo establece en las configuraciones de Twitter
     * 
     * @param context contexto necesario para acceder a la memoria persistente
     * @param twitter instancia de <code>Twitter</code> en donde se reestablecer?? el <code>RequestToken</code> obtenido
     * 
     * @return <code>RequestToken</code> obtenido. Si el <code>RequestToken</code> no exist??a, borramos las cookies del navegador
     * y obtenemos un nuevo <code>RequestToken</code> de la instancia de <code>Twitter</code>
     */
    public static RequestToken restoreRequestToken(Context context) throws TwitterException {
    	RequestToken rToken = null;
        SharedPreferences savedSession = context.getSharedPreferences(KEY_REQUEST, Context.MODE_PRIVATE);
        String token = savedSession.getString(REQUEST_TOKEN, null);
        String tokenSecret = savedSession.getString(REQUEST_TOKEN_SECRET, null);
        if (token == null || tokenSecret == null){
        	return rToken;
        }
        rToken = new RequestToken(token, tokenSecret);
        return rToken;
    }

    
    /**
     * Elimina el <code>AccessToken</code> previamente almacenado
     * 
     * @param context contexto necesario para acceder a la memoria persistente
     */
    public static void clearAccessToken(Context context) {
    	//borramos solo el access tokenci
        Editor editor = context.getSharedPreferences(KEY_ACCESS, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
    
    
    /**
     * Elimina el <code>RequestToken</code> previamente almacenado
     * 
     * @param context contexto necesario para acceder a la memoria persistente
     */
    public static void clearRequestToken(Context context) {
    	//borramos solo el access token
        Editor editor = context.getSharedPreferences(KEY_REQUEST, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
    
    public static void borrarCookies(Context ctx){
		//linea obligatoria
		CookieSyncManager.createInstance(ctx);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
	}
}