package com.twitter;

/**
 * Notifica cuando el usuario autoriz?? a una tercer aplicaci??n trabajar con la API y publicar status en el timeline del usuario
 * 
 * @author Angel L??pez
 */
public interface OnUserAutorizoListener {
	
	/**
	 * Notifica cuando el usuario autoriz?? a una tercer aplicaci??n trabajar con la API y publicar status en el timeline del usuario
	 * @param oauthPin pin regresa por el OAuth de twitter
	 */
	public void onUserAutorizo(String oauthPin);
}