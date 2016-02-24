package mx.com.sportsworld.sw.activity;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.adapter.ClubAdapter;
import mx.com.sportsworld.sw.loader.Club;
import mx.com.sportsworld.sw.provider.ClubProvider;
import mx.com.sportsworld.sw.utils.Dialer;

// TODO: Auto-generated Javadoc

/**
 * The Class SportsWorldMainActivity.
 */
public class SportsWorldMainActivity extends SherlockFragmentActivity implements
		LocationListener, AdapterView.OnItemClickListener,
		View.OnClickListener, ActionBar.OnNavigationListener {

	/** The map. */
	GoogleMap map;
	
	/** The map fragment. */
	SupportMapFragment mapFragment;

	/** The widget. */
	View widget;
	
	/** The button bar. */
	View buttonBar;
	
	/** The sociales layout. */
	View socialesLayout;
	
	/** The clubes layout. */
	View clubesLayout;
	
	/** The loading. */
	View loading;
	
	/** The llamar. */
	TextView llamar;
	
	/** The direcciones. */
	TextView direcciones;
	
	/** The detalle. */
	TextView detalle;
	
	/** The ayuda. */
	TextView ayuda;
	
	/** The nombre. */
	TextView nombre;
	
	/** The direccion. */
	TextView direccion;
	
	/** The telefono. */
	TextView telefono;
	
	/** The list. */
	ListView list;
	
	/** The facebook. */
	Button facebook;
	
	/** The twitter. */
	Button twitter;

	/** The marker dictionary. */
	Map<Marker, Club> markerDictionary;
	
	/** The clubes. */
	List<Club> clubes;
	
	/** The current club. */
	Club currentClub;
	
	/** The club provider. */
	ClubProvider clubProvider;
	
	/** The adapter. */
	ArrayAdapter<Club> adapter;
	
	/** The opciones sort adapter. */
	ArrayAdapter<String> opcionesSortAdapter;
	
	/** The is help shown. */
	boolean isHelpShown = true;
	
	/** The is showing list. */
	boolean isShowingList = true;
	
	/** The current menu. */
	int currentMenu;

	/** The current location. */
	Location currentLocation;
	
	/** The location manager. */
	LocationManager locationManager;

	/** The is alpha ordered. */
	boolean isAlphaOrdered = false;

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		widget = findViewById(R.id.widget);
		llamar = (TextView) widget.findViewById(R.id.llamar);
		direcciones = (TextView) widget.findViewById(R.id.direcciones);
		detalle = (TextView) widget.findViewById(R.id.mas);
		nombre = (TextView) widget.findViewById(R.id.nombre);
		direccion = (TextView) widget.findViewById(R.id.direccion);
		list = (ListView) findViewById(R.id.list);
		ayuda = (TextView) findViewById(R.id.ayuda);
		buttonBar = findViewById(R.id.button_bar);
		socialesLayout = findViewById(R.id.sociales_layout);
		clubesLayout = findViewById(R.id.clubes_layout);
		facebook = (Button) findViewById(R.id.facebook);
		twitter = (Button) findViewById(R.id.twitter);
		loading = findViewById(R.id.indicador_loading);
		telefono = (TextView) findViewById(R.id.telefono);

		list.setOnItemClickListener(this);
		llamar.setOnClickListener(this);
		direcciones.setOnClickListener(this);
		detalle.setOnClickListener(this);
		facebook.setOnClickListener(this);
		twitter.setOnClickListener(this);
		currentMenu = R.menu.menu_mostrar_mapa;

		clubes = new ArrayList<Club>();
		markerDictionary = new HashMap<Marker, Club>();
		clubProvider = new ClubProvider();

		adapter = new ClubAdapter(SportsWorldMainActivity.this,
				android.R.layout.simple_list_item_1, clubes);

		list.setAdapter(adapter);
		opcionesSortAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, new String[] {
						"Alfabeticamente", "M�s Cercano" });

		setUpLocationUpdate();
		setUpMapIfNeeded();
		fetchClubes();
		showHelp();
		showClubes();
		setUpOrderSpinner();

		getSupportFragmentManager().beginTransaction().hide(mapFragment)
				.commit();

		ayuda.setVisibility(View.GONE);

	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {
		if (map != null)
			map.setMyLocationEnabled(false);
		locationManager.removeUpdates(this);
		super.onPause();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		if (map != null)
			map.setMyLocationEnabled(true);
		super.onResume();
	}

	/**
	 * Show clubes.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void showClubes() {
		socialesLayout.setVisibility(View.GONE);
		clubesLayout.setVisibility(View.VISIBLE);
		currentMenu = isShowingList ? R.menu.menu_mostrar_mapa
				: R.menu.menu_mostrar_lista;
		if (isShowingList) {
			getSupportActionBar().setNavigationMode(
					ActionBar.NAVIGATION_MODE_LIST);
			list.setVisibility(View.VISIBLE);
		} else {
			getSupportActionBar().setNavigationMode(
					ActionBar.NAVIGATION_MODE_STANDARD);
		}
		invalidateOptionsMenu();

	}

	/**
	 * Show social networks.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void showSocialNetworks() {
		socialesLayout.setVisibility(View.VISIBLE);
		clubesLayout.setVisibility(View.GONE);
		list.setVisibility(View.GONE);
		getSupportActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_STANDARD);
		currentMenu = R.menu.menu_vacio;
		invalidateOptionsMenu();
	}

	/**
	 * Fetch clubes.
	 */
	private void fetchClubes() {
		loading.setVisibility(View.VISIBLE);
		new ClubTask().execute();
	}

	/**
	 * Sets the up location update.
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private void setUpLocationUpdate() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		currentLocation = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,
					this, null);
	}

	/**
	 * Cambiar lista o mapa.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void cambiarListaOMapa() {
		if (isShowingList) {
			isShowingList = false;
			currentMenu = R.menu.menu_mostrar_lista;
			invalidateOptionsMenu();
			getSupportFragmentManager().beginTransaction().show(mapFragment)
					.commit();
			list.setVisibility(View.GONE);
			widget.setVisibility(View.VISIBLE);
			getSupportActionBar().setNavigationMode(
					ActionBar.NAVIGATION_MODE_STANDARD);
			if (currentClub == null) {
				ayuda.setVisibility(View.VISIBLE);
			} else {
				ayuda.setVisibility(View.GONE);
			}
		} else {
			isShowingList = true;
			currentMenu = R.menu.menu_mostrar_mapa;
			invalidateOptionsMenu();
			getSupportFragmentManager().beginTransaction().hide(mapFragment)
					.commit();
			getSupportActionBar().setNavigationMode(
					ActionBar.NAVIGATION_MODE_LIST);
			list.setVisibility(View.VISIBLE);
			widget.setVisibility(View.GONE);
			setUpOrderSpinner();
			ayuda.setVisibility(View.GONE);
		}
	}

	/**
	 * Sets the up order spinner.
	 */
	private void setUpOrderSpinner() {
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getSupportActionBar().setListNavigationCallbacks(opcionesSortAdapter,
				this);
	}

	/**
	 * Order by distance.
	 */
	private void orderByDistance() {
		adapter.sort(new Comparator<Club>() {
			@Override
			public int compare(Club club, Club club2) {
				Location loc1 = new Location("");
				loc1.setLatitude(Double.parseDouble(club.lat));
				loc1.setLongitude(Double.parseDouble(club.lon));

				Location loc2 = new Location("");
				loc2.setLatitude(Double.parseDouble(club2.lat));
				loc2.setLongitude(Double.parseDouble(club2.lon));

				float d1 = loc1.distanceTo(currentLocation);
				float d2 = loc2.distanceTo(currentLocation);

				if (d1 > d2)
					return 1;
				else
					return -1;
			}
		});
		adapter.notifyDataSetChanged();
	}

	/**
	 * Order by alpha.
	 */
	private void orderByAlpha() {
		adapter.sort(new Comparator<Club>() {
			@Override
			public int compare(Club club, Club club2) {
				return club.nombre.compareToIgnoreCase(club2.nombre);
			}
		});
		adapter.notifyDataSetChanged();
	}

	/**
	 * Sets the up map if needed.
	 */
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (map == null) {
			mapFragment = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map));
			map = mapFragment.getMap();
			// Check if we were successful in obtaining the map.
			if (map != null) {
				// The Map is verified. It is now safe to manipulate the map.
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				map.setMyLocationEnabled(true);
				if (currentLocation != null) {
					LatLng latLng = new LatLng(currentLocation.getLatitude(),
							currentLocation.getLongitude());
					map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
				}
			}
		}
	}

	/**
	 * Place pins.
	 */
	private void placePins() {

		for (Club current : clubes) {

			LatLng latLng = new LatLng(Double.parseDouble(current.lat),
					Double.parseDouble(current.lon));
			Marker m = map.addMarker(new MarkerOptions().position(latLng)
					.title("Upster " + current.nombre));
			markerDictionary.put(m, current);
		}
		map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				if (isHelpShown) {
					hideHelp();
				}

				Club selectedClub = markerDictionary.get(marker);
				nombre.setText("Upster " + selectedClub.nombre);
				direccion.setText(selectedClub.direccion);
				telefono.setText(selectedClub.telefono);
				moveMapToClub(selectedClub);
				currentClub = selectedClub;
				return false;
			}
		});

	}

	/**
	 * Move map to club.
	 *
	 * @param club the club
	 */
	private void moveMapToClub(Club club) {
		LatLng latLng = new LatLng(Double.parseDouble(club.lat),
				Double.parseDouble(club.lon));
		map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
	}

	/**
	 * Start club overview activity.
	 *
	 * @param club the club
	 */
	private void startClubOverviewActivity(Club club) {
		Intent clubDetailIntent = new Intent(this, BranchOverviewActivity.class);
		clubDetailIntent.putExtra("club", club);
		startActivity(clubDetailIntent);
	}

	/**
	 * Show help.
	 */
	private void showHelp() {
		isHelpShown = true;
		ayuda.setVisibility(View.VISIBLE);
		buttonBar.setVisibility(View.GONE);
		nombre.setVisibility(View.GONE);
		direccion.setVisibility(View.GONE);
		telefono.setVisibility(View.GONE);

	}

	/**
	 * Hide help.
	 */
	private void hideHelp() {
		isHelpShown = false;
		ayuda.setVisibility(View.GONE);
		buttonBar.setVisibility(View.VISIBLE);
		nombre.setVisibility(View.VISIBLE);
		direccion.setVisibility(View.VISIBLE);
		telefono.setVisibility(View.VISIBLE);

	}

	// listener de la lista de clubes
	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Club clickedClub = clubes.get(position);
		startClubOverviewActivity(clickedClub);
	}

	// listener de los botones del mapa
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		String url;
		int id = v.getId();
		switch (id) {
		case R.id.llamar:
			Dialer.dial(this, currentClub.telefono);
			break;
		case R.id.direcciones:
			String uri = "geo:" + currentClub.lat + "," + currentClub.lon
					+ "?q=" + currentClub.lat + "," + currentClub.lon
					+ "(Upster " + currentClub.nombre + ")";
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse(uri));
			startActivity(intent);
			break;
		case R.id.mas:
			startClubOverviewActivity(currentClub);
			break;

		case R.id.twitter:
			url = "https://twitter.com/upstermex";
			openURL(url);
			break;
		case R.id.facebook:
			url = "https://www.facebook.com/UpsterMex";
			openURL(url);
			break;
		}
	}

	/**
	 * Open url.
	 *
	 * @param url the url
	 */
	private void openURL(String url) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}

	/*
	 * Get the location change event
	 */
	/* (non-Javadoc)
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			currentLocation = location;
			LatLng latLng = new LatLng(currentLocation.getLatitude(),
					currentLocation.getLongitude());
			map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
			clubes = clubProvider.recalculateDistance(clubes, currentLocation);
			adapter.notifyDataSetChanged();
		}
	}

	/* (non-Javadoc)
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/* (non-Javadoc)
	 * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
	 */
	@Override
	public void onProviderEnabled(String provider) {
	}

	/* (non-Javadoc)
	 * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
	 */
	@Override
	public void onProviderDisabled(String provider) {
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.ActionBar.OnNavigationListener#onNavigationItemSelected(int, long)
	 */
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		Log.v("sports", "cambio");
		switch (itemPosition) {
		case 0:
			orderByAlpha();
			break;
		case 1:
			if (currentLocation != null)
				orderByDistance();
			else {
				Toast.makeText(
						getApplicationContext(),
						getResources().getString(
								R.string.error_connection_location),
						Toast.LENGTH_SHORT).show();

				getSupportActionBar().setSelectedNavigationItem(0);

			}
			break;
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mostrar_lista
				|| item.getItemId() == R.id.mostrar_mapa) {
			cambiarListaOMapa();
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(currentMenu, menu);
		return true;
	}


	/**
	 * The Class ClubTask.
	 */
	private class ClubTask extends AsyncTask<Void, Void, Void> {

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected Void doInBackground(Void... params) {
			try {
				if (currentLocation == null)
					clubes = clubProvider.fetchClubs();
				else
					clubes = clubProvider.fetchClubs(currentLocation);

			} catch (JSONException e) {
				Log.v("sports", "fallo al parsear el json de los clubes");
				e.printStackTrace();
			}
			return null;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Void aVoid) {
			if (isShowingList)
				list.setVisibility(View.VISIBLE);
			adapter = new ClubAdapter(SportsWorldMainActivity.this,
					android.R.layout.simple_list_item_1, clubes);
			list.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			loading.setVisibility(View.GONE);

			if (map != null)
				placePins();
		}
	}
}
