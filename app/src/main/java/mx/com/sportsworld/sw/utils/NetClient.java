package mx.com.sportsworld.sw.utils;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;


/**
 * The Class NetClient.
 */
public class NetClient {


    /**
     * Ejecuta una request HTTP GET de manera sincrona.
     *
     * @param url la url base + endpoint que se quiere ejecutar
     * @param params los parametros sin incluir timestamp para la request. Este metodo
     *               firma la request automaticamente
     * @return el objeto JSON conteniendo la respuesta
     */
    public static JSONObject get(String url, List<NameValuePair> params){

        final String finalUrl = formatRequest(url,params);
        return executeGET(finalUrl);

    }


    /**
	 * Execute get.
	 * 
	 * @param url
	 *            the url
	 * @return the jSON object
	 */
    private static JSONObject executeGET(String url){

        try {

            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpParams httpParameters = new BasicHttpParams();
            //hacemos que el timeout de la request sea de un minuto
            HttpConnectionParams.setConnectionTimeout(httpParameters, 60000);
            HttpConnectionParams.setSoTimeout(httpParameters, 60000);
            httpclient.setParams(httpParameters);
            //hacemos que el cliente acepter redirects
//            httpclient.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS,true);

            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = null;

            response = httpclient.execute(httpGet);
            String parseString = new java.util.Scanner(response.getEntity().getContent()).useDelimiter("\\A").next();
            return new JSONObject(parseString);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
	 * Format request.
	 * 
	 * @param url
	 *            the url
	 * @param params
	 *            the params
	 * @return the string
	 */
    public static String formatRequest(String url,List<NameValuePair> params){

        return url+"?"+ URLEncodedUtils.format(params, "utf-8");
    }
}
