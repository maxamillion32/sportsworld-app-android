package com.twitter;

import java.io.File;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.android.R;
import com.util.FileExplorer;
import com.util.FileExplorer.OnFileSelectedListener;
import com.util.TwitterUtil;

public class TwitterApp {
	
	protected Twitter twitter = null;
	protected boolean ususarioInicioSesion = false;
	protected FragmentActivity ctx = null;
	protected Thread threadPublicacion = null;
	protected AlertDialog alertDialog = null;
	protected EditText txtPost = null;
	protected Button btnPost = null;
	protected ImageView imagenPreview = null;
	protected String mStatus = "";
	protected OAuthDialog twitterWebBrowserDialog = null;
	protected OAuthDialogView twitterWebBrowserView = null;
	protected OnSessionEventsListener mListener = null;
	protected static boolean haveImage = false;
	protected static String imagePath = "";
	private ProgressDialog mSpinner;
	
	
	public TwitterApp(FragmentActivity ctx,String consumer_key,String consumer_secret){
		mSpinner = new ProgressDialog(ctx);
		mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    mSpinner.setMessage("Loading...");
		this.ctx = ctx;
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(consumer_key, consumer_key);
		restaurarSesion();
	}
	
	@SuppressLint("CommitTransaction")
	public void iniciarSession(final OnUserLogged listenerlog){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ctx.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						mSpinner.show();
						
					}
				});
				final RequestToken rt = getRequestToken();
				if(rt != null){
					ctx.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							OAuthDialogView.listener = new OnUserAutorizoListener() {
								
								@Override
								public void onUserAutorizo(String oauthPin) {
									// TODO Auto-generated method stub
									try{
										AccessToken accessToken = twitter.getOAuthAccessToken(rt, oauthPin);
										guardarSesion(accessToken);
										if(listenerlog != null)
											listenerlog.onUserSessionStart();
	
									}catch(TwitterException e){
										if(e.getStatusCode() == 401){
											Toast.makeText(ctx, "Hubo un problema de conexion y no se pudo iniciar sesion. Por favor, trata de nuevo", Toast.LENGTH_SHORT).show();
								        }
										
										Log.i("kokusho Request Token", e.toString());
									}
								}
							};
							mSpinner.dismiss();
							FragmentTransaction ft = ctx.getSupportFragmentManager().beginTransaction();
						    ft.addToBackStack(null);
						    twitterWebBrowserView = new OAuthDialogView(ctx);
						    twitterWebBrowserView.show();
						    // Create and show the dialog.
//						    OAuthDialog newFragment = new OAuthDialog();
//						    newFragment.show(ft, "dialog");
						}
					});
				}
			}
		}).start();
	}
	
	public void publicarStatus(final String status,final OnSessionEventsListener listener){
		
		StatusUpdate stat = new StatusUpdate(status);
		publicarStatus(stat, listener);
		
	}
	
	public void publicarStatus(final StatusUpdate status,final OnSessionEventsListener listener){
		if(usuarioInicioSesion()){
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						ctx.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(ctx, "Publicando...", Toast.LENGTH_SHORT).show();
							}
						});
						
						Status stat = twitter.updateStatus(status);
						if(listener != null)
							listener.onStatusUpdated();
						
						ctx.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(ctx, "Publicacion exitosa!!", Toast.LENGTH_SHORT).show();
							}
						});
						
						if(TwitterUtil.isDebug)
							Log.i("STATUS", stat.getUser() + " > " + stat.getText());
					} catch (TwitterException e) {
						// TODO Auto-generated catch block
						if(TwitterUtil.isDebug){
							Log.i("STATUS", "FAIL");
							e.printStackTrace();
						}
					}
				}
			}).start();
		}else{
			iniciarSession(new OnUserLogged() {
				@Override
				public void onUserSessionStart() {
					// TODO Auto-generated method stub
					publicarStatus(status, listener);
				}
			});
		}
	}
	
	public void publicarStatusConImagen(String text, String path,final OnSessionEventsListener listener){
		
		StatusUpdate status = new StatusUpdate(text);
		// using java.io.File
		File imageFile = new File(path);
		status.setMedia(imageFile);
		// or using java.io.InputStream
//		InputStream is = getImageInputStream(); // needs to implement this method by yourself
//		status.setMedia("Name", is);
		publicarStatus(status, listener);
		
	}
	
	public void publicarStatusConImagenSeleccionada(final String text, final String path, final OnSessionEventsListener listener){
		imagePath = path;
		publicarStatusConImagenSeleccionada(text,listener);
		
	} 
	
	public void publicarStatusConImagenSeleccionada(final String text, final OnSessionEventsListener listener){
		
		if (usuarioInicioSesion()){
			//posteamos status
			try{
				haveImage = true;
				mostrarDialogUser(text, listener);
			}catch(Exception e){
				if(TwitterUtil.isDebug){
					Log.e("TwitterApp","postearStatusIntroducido Error al mostrar el dialogo de actualizacion de status.");
					e.printStackTrace();
				}
			}
		}else{
			iniciarSession(new OnUserLogged() {
				@Override
				public void onUserSessionStart() {
					// TODO Auto-generated method stub
					publicarStatusConImagenSeleccionada(text,listener);
				}
			});
		}
		
	} 
	
	public void publicarStatusIntroducido(final String status, final OnSessionEventsListener listener){
		if (usuarioInicioSesion()){
			//posteamos status
			try{
				haveImage = false;
				mostrarDialogUser(status, listener);
			}catch(Exception e){
				if(TwitterUtil.isDebug){
					Log.e("TwitterApp","postearStatusIntroducido Error al mostrar el dialogo de actualizacion de status.");
					e.printStackTrace();
				}
			}
		}else{
			iniciarSession(new OnUserLogged() {
				@Override
				public void onUserSessionStart() {
					// TODO Auto-generated method stub
					publicarStatusIntroducido(status, listener);
				}
			});
		}
	}
	
	public void mostrarDialogUser(String status, OnSessionEventsListener listener){
		mListener = listener;
		mStatus = status;
		if (mStatus == null){
			mStatus = "";
		}
		
		AlertDialog alertDlg = getAlertDialog(haveImage);
		txtPost.setText(mStatus);
		
		if (!alertDlg.isShowing()){
			alertDlg.show();
		}
	}
	
	@SuppressLint("CommitTransaction")
	protected AlertDialog getAlertDialog(boolean haveImage){
		if (alertDialog == null){
			AlertDialog.Builder builder = new Builder(ctx);
			builder.setTitle("Publica en Twitter");
			ViewGroup linLay = (ViewGroup)ctx.getLayoutInflater().inflate(R.layout.enter_status_dialog, null);
			imagenPreview = (ImageView)linLay.findViewById(R.id.imagepost);
			txtPost = (EditText)linLay.findViewById(R.id.txt_dialog_post);
			btnPost = (Button)linLay.findViewById(R.id.dlg_btn_enviar_post);
			imagenPreview.setTag("");
			if(!imagePath.equals("")){
				imagenPreview.setImageBitmap(BitmapFactory.decodeFile(imagePath));
				imagenPreview.setTag(imagePath);
			}
			btnPost.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					enviarPostDialog();
				}
			});
			imagenPreview.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					FileExplorer.listener = new OnFileSelectedListener() {
						@Override
						public void OnFileSelected(final File file) {
							// TODO Auto-generated method stub
							ctx.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									imagenPreview.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
									imagenPreview.setTag(file.getAbsolutePath());
								}
							});
						}
					};
					FragmentTransaction ft = ctx.getSupportFragmentManager().beginTransaction();
				    ft.addToBackStack(null);
				    // Create and show the dialog.
				    FileExplorer newFragment = new FileExplorer();
				    newFragment.show(ft, "dialog");
				}
			});
			
			
			
			builder.setView(linLay);
			
			alertDialog = builder.create();
		}
		if(haveImage){
			imagenPreview.setVisibility(View.VISIBLE);
		}else{
			imagenPreview.setVisibility(View.GONE);
		}
		return alertDialog;
	}
	
	protected void enviarPostDialog(){
		if (txtPost != null){
			String post = txtPost.getText().toString().trim();
			if(!imagenPreview.getTag().toString().equals(""))
				publicarStatusConImagen(TwitterUtil.recortarTexto(post, 137), imagenPreview.getTag().toString(), mListener);
			else
				publicarStatus(TwitterUtil.recortarTexto(post, 137), mListener);
			
			ctx.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					getAlertDialog(haveImage).dismiss();
				}
			});
			alertDialog = null;
		}
	}
	
	/**
	 * Elimina las cookies del navegador y posteriormente elimina la sesi��n del usuario previamente logueado
	 * @return referencia a esta misma instancia
	 */
	public TwitterApp cerrarSesion(){
		//borramos cookies
		TwitterUtil.borrarCookies(ctx);
		borrarSesion();
        return this;
	}
	
	
	/**
     * Obtiene un acess token de memoria persistente y lo establece en las configuraciones de esta instancia
     *  
     * @return <code>true</code> si se obtuvo un <code>AccessToken</code> correctamente. De lo contrario regresa <code>false</code>
     * y elimina las cookies del navegador
     */
	protected boolean restaurarSesion(){
		return SessionStore.restoreAccessToken(ctx, twitter);
	}


	/**
	 * Almacena el <code>AccessToken</code> en memoria persistente
	 * @param at <code>AccessToken</code> a almacenar
	 * @return referencia a esta misma instancia 
	 */
	protected TwitterApp guardarSesion(AccessToken at){
		SessionStore.saveAccessToken(ctx, at);
		return this;
	}
	
	
	/**
	 * Elimina la sesi��n del usuario previamente logueado
	 * @return referencia a esta misma instancia
	 */
	protected TwitterApp borrarSesion(){
		SessionStore.clearAccessToken(ctx);
		SessionStore.clearRequestToken(ctx);
		return this;
	}
	
	
	/**
	 * Verifica si el usuario ya hab��a iniciado sesi��n en Twitter
	 * @return <code>true</code> si el usuario ya hab��a iniciado sesi��n en Twitter, de lo contrario regresa <code>false</code>
	 */
	public boolean usuarioInicioSesion(){
		return restaurarSesion();
	}
	

	
	public RequestToken getRequestToken(){
		RequestToken requestToken = null;
		try {
				requestToken = twitter.getOAuthRequestToken();
				SessionStore.saveRequestToken(ctx, requestToken);
				if(TwitterUtil.isDebug){
					Log.i("getAuthenticationURL()", requestToken.getAuthenticationURL());
					Log.i("getAuthorizationURL()", 	requestToken.getAuthorizationURL());
					Log.i("getToken()", 			requestToken.getToken());
					Log.i("getTokenSecret()", 		requestToken.getTokenSecret());
				}
		} catch (Exception e) {
			if(TwitterUtil.isDebug)
				e.printStackTrace();
			Log.i("kokusho Request Token", e.toString());
		}
		return requestToken;
	}
}