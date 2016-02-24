package mx.com.sportsworld.sw.utils;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.MainboardActivity;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.web.task.PostTwitterTask;

/**
 * The Class FacebookUtils.
 */
public class FacebookUtils {

	/** The permissions. */
	public static List<String> PERMISSIONS = null;
	
	/** The chk face. */
	private static CheckBox chkFace = null;
	
	/** The chk twitter. */
	private static CheckBox chkTwitter = null;

	/** The activity. */
	public static FragmentActivity activity = null;
	
	/** The where post. */
	public static String wherePost;
	
	/** The return main class. */
	public static boolean returnMainClass;
	
	/** The alert_face_layout. */
	public static LinearLayout alert_face_layout;
	
	/** The alert_twitter_layout. */
	public static LinearLayout alert_twitter_layout;

	/**
	 * Buld alert face.
	 * 
	 * @param mensaje
	 *            the mensaje
	 * @param activity
	 *            the activity
	 * @param returnMain
	 *            the return main
	 * @return the alert dialog
	 */
	public static AlertDialog buldAlertFace(String mensaje,
			final Activity activity, boolean returnMain) {
		AlertDialog.Builder builder;
		final AlertDialog alertDialog;
		returnMainClass = returnMain;
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		// Nos guardamos una referencia a nuestro layout

		View layout = inflater.inflate(R.layout.alert_facebook_dialog,
				(ViewGroup) activity.findViewById(R.id.relative_facebook));

		final EditText textMessage = (EditText) layout
				.findViewById(R.id.txtFaceMessage);
		chkFace = (CheckBox) layout.findViewById(R.id.chkBoxFace);
		chkTwitter = (CheckBox) layout.findViewById(R.id.chkBoxTwitter);
		alert_face_layout = (LinearLayout) layout
				.findViewById(R.id.alert_face_layout);
		alert_twitter_layout = (LinearLayout) layout
				.findViewById(R.id.alert_twitter_layout);

		if (!isFaceLogin())
			alert_face_layout.setVisibility(View.INVISIBLE);
		if (!isTwitterLogin())
			alert_twitter_layout.setVisibility(View.INVISIBLE);

		textMessage.setText(mensaje);
		builder = new AlertDialog.Builder(activity);
		// Asignamos la vista del AlertDialog a nuestro propio layout
		builder.setView(layout);
		alertDialog = builder.create();
		// Asignamos el evento click a nuestro bot�n de aceptar (en este caso
		// cierra el alertdialog)

		Button aceptarBtn = (Button) layout.findViewById(R.id.btnFaceOk);

		aceptarBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (chkFace.isChecked())
						publishIntoWall(textMessage.getText().toString(),
								activity);
					if (chkTwitter.isChecked())
						postTwitter(textMessage.getText().toString());

				} catch (Exception ex) {
					Log.i("LogIron", ex.toString());
				}
				alertDialog.dismiss();
				if (returnMainClass)
					backMenu(activity);
			}
		});

		Button cancelBtn = (Button) layout.findViewById(R.id.btnFaceCancel);
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				if (returnMainClass)
					backMenu(activity);
			}
		});
		return alertDialog;
	}

	/**
	 * Publish into wall.
	 *
	 * @param msj
	 *            the msj
	 * @param activity
	 *            the activity
	 */
	public static void publishIntoWall(String msj, final Activity activity) {
		postStatusUpdate(msj);

	}

	/**
	 * Back menu.
	 *
	 * @param activity
	 *            the activity
	 */
	public static void backMenu(Activity activity) {
		final Intent mainBoard = new Intent(
				activity.getApplicationContext() /* context */,
				MainboardActivity.class);
		mainBoard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mainBoard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(mainBoard);
	}

	/**
	 * Post twitter.
	 *
	 * @param msg
	 *            the msg
	 */
	public static void postTwitter(String msg) {

		PostTwitterTask task = new PostTwitterTask(activity);
		task.execute(msg);
	}

	/**
	 * Checks if is face login.
	 *
	 * @return true, if is face login
	 */
	public static boolean isFaceLogin() {
		boolean res = false;
		Session session = Session.getActiveSession();

		if (session != null && session.isOpened()) {
			res = true;
		}
		return res;
	}

	/**
	 * Checks if is twitter login.
	 *
	 * @return true, if is twitter login
	 */
	public static boolean isTwitterLogin() {
		return SportsWorldPreferences.isLoggedTwitter(activity);
	}

	/**
	 * Post status update.
	 *
	 * @param message
	 *            the message
	 */
	public static void postStatusUpdate(String message) {
		Bundle postParams = new Bundle();
		postParams
				.putString(
						"name",
						"Upster: Clubes deportivos, Gimnasios, Fitness and Wellness, Programas Deportivos");
		postParams.putString("caption", "www.upster.com.mx");
		postParams
				.putString("description",
						"Clubes deportivos, Gimnasios y programas deportivos, Fitness and Wellness");// wherePost);
		postParams.putString("message", message);
		postParams.putString("link", "http://upster.com.mx/");
		// TODO Colocar una imagen de upster en lugar de sportsworl
		/*postParams
				.putString(
						"picture",
						"https://quiosco.sportsworld.com.mx/imagenes/android/portAppAndroid170x170_1.jpg");*/

		Request.Callback callback = new Request.Callback() {
			public void onCompleted(Response response) {
				Log.i("post response", response.toString());

				FacebookRequestError error = response.getError();
				if (error != null) {
					Log.i("kokusho face error", "Not null");
					Log.i("kokusho", error.getErrorMessage());
					Toast.makeText(activity,
							"Hubo un error al realizar el post en Facebook",
							Toast.LENGTH_LONG).show();
				} else {
					Log.i("kokusho face error", "null");
					Toast.makeText(activity, "Publicaci�n exitosa en Facebook",
							Toast.LENGTH_LONG).show();
					JSONObject graphResponse = response.getGraphObject()
							.getInnerJSONObject();
					String postId = null;
					try {
						postId = graphResponse.getString("id");
					} catch (Exception e) {
						Log.i("kokusho error face json", "JSON error ::"
								+ postId + " :: " + e.getMessage());
					}

				}

			}
		};

		Request request = new Request(Session.getActiveSession(), "me/feed",
				postParams, HttpMethod.POST, callback);

		RequestAsyncTask task = new RequestAsyncTask(request);
		task.execute();
	}

}
