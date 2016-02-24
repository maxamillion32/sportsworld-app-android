package mx.com.sportsworld.sw.activity;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import java.util.List;

import org.json.JSONException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.SherlockListActivity;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.loader.Club;
import mx.com.sportsworld.sw.loader.Instalacion;
import mx.com.sportsworld.sw.provider.InstalacionesProvider;

// TODO: Auto-generated Javadoc

/**
 * The Class InstalacionesActivity.
 */
public class InstalacionesActivity extends SherlockListActivity {
	
	/** The club. */
	Club club;
	
	/** The instalaciones. */
	List<Instalacion> instalaciones;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_instalaciones);

		club = new Club();
		club.nombre = getIntent().getExtras().getString("club");
		new InstalacionesFetcherTask().execute();

	}

	/**
	 * The Class InstalacionesFetcherTask.
	 */
	private class InstalacionesFetcherTask extends AsyncTask<Void, Void, Void> {

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected Void doInBackground(Void... params) {
			InstalacionesProvider provider = new InstalacionesProvider();
			try {
				instalaciones = provider.fetchInstalaciones(club);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Void aVoid) {
			findViewById(R.id.loading).setVisibility(View.GONE);
			setListAdapter(new ArrayAdapter<Instalacion>(
					InstalacionesActivity.this, R.layout.instalacion_list_item,
					instalaciones));
		}
	}

}
