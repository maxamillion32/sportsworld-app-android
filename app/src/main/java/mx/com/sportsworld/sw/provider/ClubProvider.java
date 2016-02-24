package mx.com.sportsworld.sw.provider;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import mx.com.sportsworld.sw.loader.Club;
import mx.com.sportsworld.sw.utils.NetClient;

/**
 * The Class ClubProvider.
 */
public class ClubProvider {

    /** The club url. */
    private final String CLUB_URL = "http://201.159.135.111:8000/api/vwun/";


    /**
	 * Fetch clubs.
	 * 
	 * @return the list
	 * @throws JSONException
	 *             the jSON exception
	 */
    public List<Club> fetchClubs() throws JSONException {

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("format","json"));
        params.add(new BasicNameValuePair("limit","0"));

        JSONObject response = NetClient.get(CLUB_URL, params);
        JSONArray clubesJsonArray = response.getJSONArray("objects");
        int arraySize = clubesJsonArray.length();

        List<Club> clubes = new ArrayList<Club>(response.getJSONObject("meta").getInt("total_count"));

        for(int i=0; i < arraySize; i++){
            JSONObject currentJsonObject = clubesJsonArray.getJSONObject(i);
            Club currentClub = new Club();
            currentClub.clave = currentJsonObject.getString("clave");
            currentClub.direccion = currentJsonObject.getString("direccion");
            currentClub.horario = currentJsonObject.getString("horario");
            currentClub.idun = currentJsonObject.getString("idun");
            currentClub.lat = currentJsonObject.getString("latitud");
            currentClub.lon = currentJsonObject.getString("longitud");
            currentClub.nombre = currentJsonObject.getString("nombre");
            currentClub.preventa = currentJsonObject.getString("preventa");
            currentClub.resourceUri = currentJsonObject.getString("resource_uri");
            currentClub.rutaVideo = currentJsonObject.getString("rutavideo");
            currentClub.telefono = currentJsonObject.getString("telefono");
            clubes.add(currentClub);
        }

        return clubes;

    }


    /**
	 * Fetch clubs.
	 * 
	 * @param loc
	 *            the loc
	 * @return the list
	 * @throws JSONException
	 *             the jSON exception
	 */
    public List<Club> fetchClubs(Location loc) throws JSONException {

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("format","json"));
        params.add(new BasicNameValuePair("limit","0"));

        JSONObject response = NetClient.get(CLUB_URL, params);
        JSONArray clubesJsonArray = response.getJSONArray("objects");
        int arraySize = clubesJsonArray.length();

        List<Club> clubes = new ArrayList<Club>(response.getJSONObject("meta").getInt("total_count"));

        for(int i=0; i < arraySize; i++){
            JSONObject currentJsonObject = clubesJsonArray.getJSONObject(i);
            Club currentClub = new Club();
            Location clubLocation = new Location("");
            currentClub.clave = currentJsonObject.getString("clave");
            currentClub.direccion = currentJsonObject.getString("direccion");
            currentClub.horario = currentJsonObject.getString("horario");
            currentClub.idun = currentJsonObject.getString("idun");
            currentClub.lat = currentJsonObject.getString("latitud");
            clubLocation.setLatitude(Double.parseDouble(currentClub.lat));
            currentClub.lon = currentJsonObject.getString("longitud");
            clubLocation.setLongitude(Double.parseDouble(currentClub.lon));
            currentClub.nombre = currentJsonObject.getString("nombre");
            currentClub.preventa = currentJsonObject.getString("preventa");
            currentClub.resourceUri = currentJsonObject.getString("resource_uri");
            currentClub.rutaVideo = currentJsonObject.getString("rutavideo");
            currentClub.telefono = currentJsonObject.getString("telefono");
            currentClub.distancia = String.valueOf(loc.distanceTo(clubLocation));
            clubes.add(currentClub);
        }

        return clubes;

    }

    /**
	 * Recalculate distance.
	 * 
	 * @param clubes
	 *            the clubes
	 * @param location
	 *            the location
	 * @return the list
	 */
    public List<Club> recalculateDistance(List<Club> clubes, Location location){
        for(Club current: clubes){
            Location loc = new Location("");
            loc.setLatitude(Double.parseDouble(current.lat));
            loc.setLongitude(Double.parseDouble(current.lon));
            current.distancia = String.valueOf(location.distanceTo(loc));
        }
        return clubes;
    }

    /**
	 * Fetch you tube thumbnail for club.
	 * 
	 * @param club
	 *            the club
	 * @return the drawable
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public Drawable fetchYouTubeThumbnailForClub(Club club) throws IOException {

        final String rutaVideo = club.rutaVideo;

        if(rutaVideo.length()>0){

            Pattern pattern = Pattern.compile("v=[A-Za-z0-9-_]*");
            Matcher matcher = pattern.matcher(rutaVideo);
            final String videoCode = matcher.group();
            final String address = "http://img.youtube.com/vi/"+videoCode.substring(2)+"/0.jpg";
            URL url = new URL(address);
            InputStream content = (InputStream)url.getContent();
            Drawable d = Drawable.createFromStream(content, "src");
            return d;
        }
        return null;

    }

    /**
	 * Fetch map thumbnail for club.
	 * 
	 * @param context
	 *            the context
	 * @param club
	 *            the club
	 * @return the drawable
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public Drawable fetchMapThumbnailForClub(Context context, Club club) throws IOException {
        Resources r = context.getResources();
        DisplayMetrics metrics = r.getDisplayMetrics();
        int px = metrics.widthPixels/2 - (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 9, r.getDisplayMetrics());
        int pxh =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, r.getDisplayMetrics());


        Log.v("sports", "Fetching thumbnail de " + px);


        final String lat = club.lat;
        final String lon = club.lon;
        final String address = "http://maps.google.com/maps/api/staticmap?center="+lat+","+lon+
                "&zoom=17&size="+px+"x"+pxh+
                "&markers=color:red%7Ccolor:red%7C+"+lat+","+lon+
                "&sensor=false";

        URL url = new URL(address);
        InputStream content = (InputStream)url.getContent();
        Drawable d = Drawable.createFromStream(content, "src");
        return d;
    }



}
