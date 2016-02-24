package mx.com.sportsworld.sw.activity;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import java.util.Calendar;
import java.util.List;

import net.simonvt.calendarview.CalendarView;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.adapter.ClaseAdapter;
import mx.com.sportsworld.sw.loader.Clase;
import mx.com.sportsworld.sw.loader.Club;
import mx.com.sportsworld.sw.provider.ClaseProvider;

// TODO: Auto-generated Javadoc

/**
 * The Class ClasesActivity.
 */
public class ClasesActivity extends SherlockFragmentActivity {

	/** The calendar. */
	CalendarView calendar;
	
	/** The lista clases. */
	ListView listaClases;
	
	/** The adapter. */
	ArrayAdapter<Clase> adapter;
	
	/** The clases. */
	List<Clase> clases;
	
	/** The club. */
	Club club;
	
	/** The no hay elementos. */
	TextView noHayElementos;
	
	/** The last selected date. */
	Calendar lastSelectedDate;

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clases);
		club = getIntent().getExtras().getParcelable("club");

		getSupportActionBar().setTitle("Upster " + club.nombre);

		calendar = (CalendarView) findViewById(R.id.calendar);
		listaClases = (ListView) findViewById(R.id.lista);
		noHayElementos = (TextView) findViewById(R.id.no_hay);

		adapter = new ClaseAdapter(this);
		listaClases.setAdapter(adapter);
		lastSelectedDate = Calendar.getInstance();

		Calendar date = Calendar.getInstance();
		fetchClases(date);

		calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
			@Override
			public void onSelectedDayChange(CalendarView view, final int year,
					final int month, final int dayOfMonth) {
				noHayElementos.setVisibility(View.VISIBLE);
				adapter.clear();
				adapter.notifyDataSetChanged();
				lastSelectedDate.set(year, month, dayOfMonth);
				fetchClases(lastSelectedDate);

			}
		});

		listaClases.setOnItemClickListener(new OnClaseClickListener(this));
	}

	/** The m handler. */
	Handler mHandler = new Handler() {
		@SuppressLint("NewApi")
		// We check which build version we are using.
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (clases != null) {
				if (clases.size() > 0) {
					adapter.clear();
					if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
						adapter.addAll(clases);
					} else {
						for (Clase c : clases) {
							adapter.add(c);
						}
					}
					adapter.notifyDataSetChanged();
					noHayElementos.setVisibility(View.GONE);
				} else {
					noHayElementos.setVisibility(View.VISIBLE);
					noHayElementos.setText("No hay clases para este d�a");
				}

			}
		}
	};

	/**
	 * Fetch clases.
	 *
	 * @param date the date
	 */
	private void fetchClases(final Calendar date) {
		noHayElementos.setText("Buscando clases�");
		Runnable requestClases = new Runnable() {
			@Override
			public void run() {
				try {
					clases = new ClaseProvider().fetchClasesForClubAndDate(
							club, date);
					mHandler.sendEmptyMessage(0);

				} catch (JSONException e) {
					e.printStackTrace(); // To change body of catch statement
											// use File | Settings | File
											// Templates.
				}
			}
		};
		new Thread(requestClases).start();
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_clases, menu);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		showBuscarClase();
		return true;
	}

	/**
	 * Show buscar clase.
	 */
	private void showBuscarClase() {
		onSearchRequested();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onSearchRequested()
	 */
	@Override
	public boolean onSearchRequested() {
		Bundle appData = new Bundle();
		appData.putString("club", club.nombre);
		startSearch(null, false, appData, false);
		return true;
	}

	/**
	 * Agregar clase.
	 *
	 * @param clase the clase
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void agregarClase(Clase clase) {
		String[] projection = new String[] { "_id", "name" };

		Uri calendarsUri = Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB ? CalendarContract.Calendars.CONTENT_URI
				: Uri.parse("content://com.android.calendar/calendars");
		Cursor managedCursor = this.getContentResolver().query(calendarsUri,
				projection, null, null, null);
		if (managedCursor.moveToFirst()) {
			String calId = managedCursor.getString(managedCursor
					.getColumnIndex("_id"));

			String[] inicio = clase.inicio.split(":");
			String[] fin = clase.fin.split(":");

			lastSelectedDate.set(Calendar.HOUR_OF_DAY,
					Integer.parseInt(inicio[0]));
			lastSelectedDate.set(Calendar.MINUTE, Integer.parseInt(inicio[1]));

			ContentValues calendarEvent = new ContentValues();
			calendarEvent.put("calendar_id", calId);
			calendarEvent.put("title", clase.clase);
			calendarEvent.put("description", "Instructor: " + clase.instructor);
			calendarEvent.put("eventLocation", "Upster " + clase.club
					+ ", Sal�n:" + clase.salon);
			calendarEvent.put("dtstart", lastSelectedDate.getTimeInMillis());

			lastSelectedDate
					.set(Calendar.HOUR_OF_DAY, Integer.parseInt(fin[0]));
			lastSelectedDate.set(Calendar.MINUTE, Integer.parseInt(fin[1]));

			calendarEvent.put("dtend", lastSelectedDate.getTimeInMillis());
			calendarEvent.put("allDay", 0);
			calendarEvent.put("hasAlarm", 1);
			calendarEvent.put(CalendarContract.Events.EVENT_TIMEZONE,
					"America/Mexico_City");

		}

		if (managedCursor != null)
			managedCursor.close();
	}
}
