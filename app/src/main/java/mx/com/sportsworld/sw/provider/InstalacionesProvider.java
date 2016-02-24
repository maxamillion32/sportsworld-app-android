package mx.com.sportsworld.sw.provider;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.com.sportsworld.sw.loader.Club;
import mx.com.sportsworld.sw.loader.Instalacion;
import mx.com.sportsworld.sw.utils.NetClient;

/**
 * Created with IntelliJ IDEA.
 * User: JL
 * Date: 2/25/13
 * Time: 1:07 PM
 * To change this template use File | Settings | File Templates.
 */

public class InstalacionesProvider {
    
    /** The instalacion url. */
    final private String INSTALACION_URL = "http://201.159.135.111:8000/api/vwuninstalaciones";

    /**
	 * Fetch instalaciones.
	 * 
	 * @param club
	 *            the club
	 * @return the list
	 * @throws JSONException
	 *             the jSON exception
	 */
    public List<Instalacion> fetchInstalaciones(Club club) throws JSONException {

        List<NameValuePair> params = new ArrayList<NameValuePair>(3);
        params.add(new BasicNameValuePair("nombre",club.nombre));
        params.add(new BasicNameValuePair("format","json"));
        params.add(new BasicNameValuePair("limit","0"));

        JSONObject response = NetClient.get(INSTALACION_URL, params);
        JSONArray clubesJsonArray = response.getJSONArray("objects");
        int arraySize = clubesJsonArray.length();

        List<Instalacion> instalaciones = new ArrayList<Instalacion>(response.getJSONObject("meta").getInt("total_count"));

        for(int i=0; i < arraySize; i++){
            JSONObject currentJsonObject = clubesJsonArray.getJSONObject(i);
            Instalacion instalacion = new Instalacion();
            instalacion.capacidadMaxima = currentJsonObject.getString("capacidadmaxima");
            instalacion.descripcion = currentJsonObject.getString("descripcion");
            instalacion.idInstalacion = currentJsonObject.getString("idinstalacion");
            instalacion.idun = currentJsonObject.getString("idun");
            instalacion.idunInstalacion = currentJsonObject.getString("iduninstalacion");
            instalacion.nombre = currentJsonObject.getString("nombre");
            instalacion.resource_uri = currentJsonObject.getString("resource_uri");


            instalaciones.add(instalacion);
        }

        return instalaciones;

    }
}
