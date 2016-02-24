package mx.com.sportsworld.sw.activity;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.AdapterView;

import mx.com.sportsworld.sw.loader.Clase;

import java.util.Calendar;

// TODO: Auto-generated Javadoc

/**
 * The listener interface for receiving onClaseClick events.
 * The class that is interested in processing a onClaseClick
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addOnClaseClickListener<code> method. When
 * the onClaseClick event occurs, that object's appropriate
 * method is invoked.
 *
 * @see OnClaseClickEvent
 */
public class OnClaseClickListener implements AdapterView.OnItemClickListener {

	/** The context. */
	Context context;
	
	/** The event uri. */
	@SuppressWarnings("unused")
	private Uri eventUri;

	/**
	 * Instantiates a new on clase click listener.
	 *
	 * @param context the context
	 */
	public OnClaseClickListener(Context context) {
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(final AdapterView<?> parent, View view,
			final int position, long id) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancelar",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Agregar",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						agregarClase((Clase) parent.getAdapter().getItem(
								position));
					}
				});
		alertDialog.setMessage("�Deseas agregar esta clase a tu calendario?");
		alertDialog.show();

	}

	/**
	 * Agregar clase.
	 *
	 * @param clase the clase
	 */
	@SuppressLint("NewApi")
	private void agregarClase(Clase clase) {
		String[] projection = new String[] { "_id", "name" };
		Calendar selectedDate = clase.getFechaDeClase();

		Uri calendarsUri = Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB ? CalendarContract.Calendars.CONTENT_URI
				: Uri.parse("content://com.android.calendar/calendars");
		Uri eventsUri = Uri.parse("content://com.android.calendar/events");
		Cursor managedCursor = context.getContentResolver().query(calendarsUri,
				projection, null, null, null);
		if (managedCursor.moveToFirst()) {
			String calId = managedCursor.getString(managedCursor
					.getColumnIndex("_id"));

			String[] inicio = clase.inicio.split(":");
			String[] fin = clase.fin.split(":");

			selectedDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(inicio[0]));
			selectedDate.set(Calendar.MINUTE, Integer.parseInt(inicio[1]));

			ContentValues calendarEvent = new ContentValues();
			calendarEvent.put("calendar_id", calId);
			calendarEvent.put("title", clase.clase);
			calendarEvent.put("description", "Instructor: " + clase.instructor);
			calendarEvent.put("eventLocation", "Upster " + clase.club
					+ ", Sal�n:" + clase.salon);
			calendarEvent.put("dtstart", selectedDate.getTimeInMillis());

			selectedDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(fin[0]));
			selectedDate.set(Calendar.MINUTE, Integer.parseInt(fin[1]));

			calendarEvent.put("dtend", selectedDate.getTimeInMillis());
			calendarEvent.put("allDay", 0);
			calendarEvent.put("hasAlarm", 1);
			calendarEvent.put(CalendarContract.Events.EVENT_TIMEZONE,
					"America/Mexico_City");

			eventUri = context.getContentResolver().insert(eventsUri,
					calendarEvent);
		}

		if (managedCursor != null)
			managedCursor.close();
	}
}
