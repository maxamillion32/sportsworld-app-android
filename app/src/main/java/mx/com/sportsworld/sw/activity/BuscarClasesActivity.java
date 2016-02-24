package mx.com.sportsworld.sw.activity;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import java.util.List;

import org.json.JSONException;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.adapter.ClaseAdapter;
import mx.com.sportsworld.sw.loader.Clase;
import mx.com.sportsworld.sw.loader.Club;
import mx.com.sportsworld.sw.provider.ClaseProvider;

// TODO: Auto-generated Javadoc

/**
 * The Class BuscarClasesActivity.
 */
public class BuscarClasesActivity extends SherlockActivity {

    /** The club. */
    String club;
    
    /** The clases. */
    List<Clase> clases;
    
    /** The adapter. */
    ClaseAdapter adapter;
    
    /** The query. */
    String query;
    
    /** The list view. */
    ListView listView;
    
    /** The no hay resultados. */
    View noHayResultados;
    
    /** The buscando. */
    View buscando;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_clases);
        Intent intent = getIntent();
        listView = (ListView) findViewById(R.id.lista);
        noHayResultados = findViewById(R.id.no_hay_resultados);
        buscando = findViewById(R.id.buscando);

        buscando.setVisibility(View.VISIBLE);

        Bundle appData = intent.getBundleExtra(SearchManager.APP_DATA);
        if (appData != null) {
            club = appData.getString("club");
        }
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            new FetchClasesTask().execute(club);
        }
        ((TextView) noHayResultados).setText("No hay resultados con la palabra: "+query);

        listView.setOnItemClickListener(new mx.com.sportsworld.sw.activity.OnClaseClickListener(this));

    }

    /**
     * The Class FetchClasesTask.
     */
    private class FetchClasesTask extends AsyncTask<String,Void,Void> {

        /* (non-Javadoc)
         * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
         */
        @Override
        protected Void doInBackground(String... params) {
            String club = params[0];
            Club c = new Club();
            c.nombre = club;
            ClaseProvider provider = new ClaseProvider();
            try {
                 clases = provider.fetchClasesForClub(c);
            } catch (JSONException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            return null;
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new ClaseAdapter(BuscarClasesActivity.this,R.layout.clase_list_element,clases);
            adapter.getFilter().filter(query);
            if(adapter.getCount()>0){
                listView.setAdapter(adapter);
                buscando.setVisibility(View.GONE);
            }
            else{
                noHayResultados.setVisibility(View.VISIBLE);
                buscando.setVisibility(View.GONE);
            }

        }
    }
}
