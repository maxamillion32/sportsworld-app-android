package mx.com.sportsworld.sw.provider;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.com.sportsworld.sw.loader.Clase;
import mx.com.sportsworld.sw.loader.Club;
import mx.com.sportsworld.sw.utils.NetClient;


/**
 * The Class ClaseProvider.
 */
public class ClaseProvider {
    
    /** The clase url. */
    private final String CLASE_URL = "http://201.159.135.111:8000/api/vwclases/";

    /**
	 * Fetch clases for club.
	 * 
	 * @param club
	 *            the club
	 * @return the list
	 * @throws JSONException
	 *             the jSON exception
	 */
    public List<Clase> fetchClasesForClub(Club club) throws JSONException {

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("format","json"));
        params.add(new BasicNameValuePair("limit","0"));
        params.add(new BasicNameValuePair("club",club.nombre));


        JSONObject response = NetClient.get(CLASE_URL, params);
        return parseClasesFromJson(response);

    }

    /**
	 * Fetch clases for club and date.
	 * 
	 * @param club
	 *            the club
	 * @param calendar
	 *            the calendar
	 * @return the list
	 * @throws JSONException
	 *             the jSON exception
	 */
    public List<Clase> fetchClasesForClubAndDate(Club club, Calendar calendar) throws JSONException {
        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("format","json"));
        params.add(new BasicNameValuePair("limit","0"));
        params.add(new BasicNameValuePair("club",club.nombre));
        params.add(new BasicNameValuePair("dia",calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG,new Locale("es","MX"))));
        params.add(new BasicNameValuePair("semana", String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR))));


        JSONObject response = NetClient.get(CLASE_URL, params);
        return parseClasesFromJson(response);
    }

    /**
	 * Parses the clases from json.
	 * 
	 * @param response
	 *            the response
	 * @return the list
	 * @throws JSONException
	 *             the jSON exception
	 */
    private List<Clase> parseClasesFromJson(JSONObject response) throws JSONException {
        JSONArray clasesJsonArray = response.getJSONArray("objects");
        int arraySize = clasesJsonArray.length();

        List<Clase> clases = new ArrayList<Clase>(response.getJSONObject("meta").getInt("total_count"));

        for(int i=0; i < arraySize; i++){
            JSONObject currentJsonObject = clasesJsonArray.getJSONObject(i);
            Clase currentClase = new Clase();
            currentClase.clase = currentJsonObject.getString("clase");
            currentClase.club = currentJsonObject.getString("club");
            currentClase.dia = currentJsonObject.getString("dia");
            currentClase.fin = currentJsonObject.getString("fin");
            currentClase.idinstalacionactividadprogramada = currentJsonObject.getString("idinstalacionactividadprogramada");
            currentClase.inicio = currentJsonObject.getString("inicio");
            currentClase.instructor = currentJsonObject.getString("instructor");
            currentClase.resource_uri = currentJsonObject.getString("resource_uri");
            currentClase.salon = currentJsonObject.getString("salon");
            currentClase.semana = currentJsonObject.getString("semana");

            clases.add(currentClase);
        }

        return clases;
    }
}
