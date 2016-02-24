package mx.com.sportsworld.sw.parser;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.com.sportsworld.sw.model.Branch;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

/**
 * The Class BranchParser.
 */
public class BranchParser implements ContentValuesParser<Branch>, JsonParser<Branch>,
        CursorParser<Branch> {

    /** The Constant KEY_LATITUDE. */
    private static final String KEY_LATITUDE = "latitud";
    
    /** The Constant KEY_LONGITUDE. */
    private static final String KEY_LONGITUDE = "longitud";
    
    /** The Constant KEY_DISTANCE. */
    private static final String KEY_DISTANCE = "distance";
    
    /** The Constant KEY_PHONE. */
    private static final String KEY_PHONE = "telefono";
    
    /** The Constant KEY_STATE_ID. */
    private static final String KEY_STATE_ID = "idestado";
    
    /** The Constant KEY_ADDRESS. */
    private static final String KEY_ADDRESS = "direccion";
    
    /** The Constant KEY_SCHEDULE. */
    private static final String KEY_SCHEDULE = "horario";
    
    /** The Constant KEY_UN_ID. */
    private static final String KEY_UN_ID = "idun";
    
    /** The Constant ALIAS_KEY_UN_ID. */
    private static final String ALIAS_KEY_UN_ID = "club_id";
    
    /** The Constant KEY_KEY. */
    private static final String KEY_KEY = "clave";
    
    /** The Constant KEY_D_COUNT. */
    private static final String KEY_D_COUNT = "dcount";
    
    /** The Constant KEY_NAME. */
    private static final String KEY_NAME = "nombre";
    
    /** The Constant ALIAS_KEY_NAME. */
    private static final String ALIAS_KEY_NAME = "club";
    
    /** The Constant KEY_VIDEO_URL. */
    private static final String KEY_VIDEO_URL = "rutavideo";
    
    /** The Constant KEY_360_URL. */
    private static final String KEY_URL_360 = "ruta360";
    
    /** The Constant KEY_PRE_ORDER. */
    private static final String KEY_PRE_ORDER = "preventa";
    
    /** The Constant KEY_STATE_NAME. */
    private static final String KEY_STATE_NAME = "estado";
    
    /** The Constant KEY_TYPE. */
    private static final String KEY_TYPE = "type";
    
    /** The Constant KEY_FAVORITE. */
    private static final String KEY_FAVORITE = "favorito";
    
    /** The Constant KEY_FACILITY_DESCRIPTION. */
    private static final String KEY_FACILITY_DESCRIPTION = "descripcion";
    
    /** The Constant EY_FACILITY_IMAGE. */
    private static final String EY_FACILITY_IMAGE="rutaimagen";
    
    /** The Constant ARRAY_FACILITIES. */
    private static final String ARRAY_FACILITIES = "instalaciones";
    
    /** The Constant ARRAY_IMAGES_URL. */
    private static final String ARRAY_IMAGES_URL = "imagenes_club";

    /* (non-Javadoc)
     * @see com.sportsworld.android.parser.JsonParser#parse(org.json.JSONObject)
     */
    @Override
    public Branch parse(JSONObject object) throws JSONException {

        final double latitude = object.optDouble(KEY_LATITUDE);
        final double longitude = object.optDouble(KEY_LONGITUDE);
        final double distance = object.optDouble(KEY_DISTANCE);
        final String phone = object.optString(KEY_PHONE);
        final long stateId = object.optLong(KEY_STATE_ID);
        final String address = object.optString(KEY_ADDRESS);
        final String schedule = object.optString(KEY_SCHEDULE);
        long unId = object.optLong(KEY_UN_ID, 0L);
        if (unId == 0) {
            unId = object.optLong(ALIAS_KEY_UN_ID);                    
        }
        final String key = object.optString(KEY_KEY);
        final int dCount = object.optInt(KEY_D_COUNT);
        final String videoUrl = object.optString(KEY_VIDEO_URL);
        final String url360 = object.optString(KEY_URL_360);
        final int preOrder = object.optInt(KEY_PRE_ORDER);
        final String stateName = object.optString(KEY_STATE_NAME);
        final String type = object.optString(KEY_TYPE);
        final boolean favorite = object.optBoolean(KEY_FAVORITE);
        final String[] facilities = getFacilities(object);
        final String[] facilitiesImgs = getfacilitiesImgs(object);
        final String[] imagesUrl = getImagesUrl(object);

        String name = object.optString(KEY_NAME);
        if (TextUtils.isEmpty(name)) {
            name = object.optString(ALIAS_KEY_NAME);
        }

        return new Branch(latitude, longitude, distance, stateId, address, schedule, unId, key,
                dCount, name, videoUrl, preOrder, stateName, phone, type, favorite, facilities,
                facilitiesImgs,imagesUrl
                ,url360
                );
    }

    /**
	 * Gets the facilities.
	 * 
	 * @param object
	 *            the object
	 * @return the facilities
	 * @throws JSONException
	 *             the jSON exception
	 */
    private String[] getFacilities(JSONObject object) throws JSONException {
        final JSONArray array = object.optJSONArray(ARRAY_FACILITIES);
        if (array == null) {
            return new String[0];
        }
        final int count = array.length();
        final String[] facilities = new String[count];
        for (int i = 0; i < count; i++) {
            facilities[i] = array.getJSONObject(i).optString(KEY_FACILITY_DESCRIPTION);
        }
        return facilities;
    }
    
    /**
	 * Gets the facilities imgs.
	 * 
	 * @param object
	 *            the object
	 * @return the facilities imgs
	 * @throws JSONException
	 *             the jSON exception
	 */
    private String[] getfacilitiesImgs(JSONObject object) throws JSONException {
        final JSONArray array = object.optJSONArray(ARRAY_FACILITIES);
        if (array == null) {
            return new String[0];
        }
        final int count = array.length();
        final String[] facilities = new String[count];
        for (int i = 0; i < count; i++) {
            facilities[i] = array.getJSONObject(i).optString(EY_FACILITY_IMAGE);
        }
        return facilities;
    }

    /**
	 * Gets the images url.
	 * 
	 * @param object
	 *            the object
	 * @return the images url
	 * @throws JSONException
	 *             the jSON exception
	 */
    private String[] getImagesUrl(JSONObject object) throws JSONException {
        final JSONArray array = object.optJSONArray(ARRAY_IMAGES_URL);
        if (array == null) {
            return new String[0];
        }
        final int count = array.length();
        final String[] imagesUrls = new String[count];
        for (int i = 0; i < count; i++) {
            imagesUrls[i] = array.optString(i);
        }
        return imagesUrls;
    }

    /* (non-Javadoc)
     * @see com.sportsworld.android.parser.JsonParser#parse(org.json.JSONArray)
     */
    @Override
    public List<Branch> parse(JSONArray array) throws JSONException {
        final int count = array.length();
        final List<Branch> branches = new ArrayList<Branch>(count);
        for (int i = 0; i < count; i++) {
            branches.add(parse(array.getJSONObject(i)));
        }
        return branches;
    }

    /* (non-Javadoc)
     * @see com.sportsworld.android.parser.ContentValuesParser#parse(java.lang.Object, android.content.ContentValues)
     */
    @Override
    public ContentValues parse(Branch object, ContentValues values) {

        if (values == null) {
            values = new ContentValues();
        }

        values.put(SportsWorldContract.Branch._ID, object.getUnId());
        values.put(SportsWorldContract.Branch.LATITUDE, object.getLatitude());
        values.put(SportsWorldContract.Branch.LONGITUDE, object.getLongitude());
        values.put(SportsWorldContract.Branch.DISTANCE, object.getDistance());
        values.put(SportsWorldContract.Branch.STATE_ID, object.getStateId());
        values.put(SportsWorldContract.Branch.ADDRESS, object.getAddress());
        values.put(SportsWorldContract.Branch.SCHEDULE, object.getSchedule());
        values.put(SportsWorldContract.Branch.UN_ID, object.getUnId());
        values.put(SportsWorldContract.Branch.KEY, object.getKey());
        values.put(SportsWorldContract.Branch.D_COUNT, object.getDCount());
        values.put(SportsWorldContract.Branch.NAME, object.getName());
        values.put(SportsWorldContract.Branch.VIDEO_URL, object.getVideoUrl());
        values.put(SportsWorldContract.Branch.PRE_ORDER, object.getPreOrder());
        values.put(SportsWorldContract.Branch.STATE_NAME, object.getStateName());
        values.put(SportsWorldContract.Branch.PREVENTA, object.getPreOrder());
        values.put(SportsWorldContract.Branch.PHONE, object.getPhone());
        values.put(SportsWorldContract.Branch.TYPE, object.getType());
        values.put(SportsWorldContract.Branch.FAVORITE, object.isFavorite());
        values.put(SportsWorldContract.Branch.FACILITIES, object.getFacilitiesJoined());
        values.put(SportsWorldContract.Branch.IMAGES_URLS, object.getImagesUrlsJoined());
        //values.put(SportsWorldContract.Branch.URL_360, object.getUrl360());
        return values;

    }

    /**
	 * This <strong>won't</strong> close the cursor.
	 * 
	 * @param cursor
	 *            the cursor
	 * @return the list
	 */
    @Override
    public List<Branch> parse(Cursor cursor) {
        cursor.moveToPosition(-1); // Ensure we are before first entry

        final List<Branch> branches = new ArrayList<Branch>();

        while (cursor.moveToNext()) {
            final double latitude = cursor.getDouble(cursor
                    .getColumnIndexOrThrow(SportsWorldContract.Branch.LATITUDE));
            final double longitude = cursor.getDouble(cursor
                    .getColumnIndexOrThrow(SportsWorldContract.Branch.LONGITUDE));
            final double distance = cursor.getDouble(cursor
                    .getColumnIndexOrThrow(SportsWorldContract.Branch.DISTANCE));
            final String phone = cursor.getString(cursor
                    .getColumnIndexOrThrow(SportsWorldContract.Branch.PHONE));
            final long stateId = cursor.getLong(cursor
                    .getColumnIndexOrThrow(SportsWorldContract.Branch.STATE_ID));
            final String address = cursor.getString(cursor
                    .getColumnIndexOrThrow(SportsWorldContract.Branch.ADDRESS));
            final String schedule = cursor.getString(cursor
                    .getColumnIndexOrThrow(SportsWorldContract.Branch.SCHEDULE));
            final long unId = cursor.getLong(cursor
                    .getColumnIndexOrThrow(SportsWorldContract.Branch.UN_ID));
            final String key = cursor.getString(cursor
                    .getColumnIndexOrThrow(SportsWorldContract.Branch.KEY));
            final int dCount = cursor.getInt(cursor
                    .getColumnIndexOrThrow(SportsWorldContract.Branch.D_COUNT));
            final String videoUrl = cursor.getString(cursor
                    .getColumnIndexOrThrow(SportsWorldContract.Branch.VIDEO_URL));
            final int preOrder = cursor.getInt(cursor
                    .getColumnIndexOrThrow(SportsWorldContract.Branch.PRE_ORDER));
            final String stateName = cursor.getString(cursor
                    .getColumnIndexOrThrow(SportsWorldContract.Branch.STATE_NAME));
            final String type = cursor.getString(cursor
                    .getColumnIndexOrThrow(SportsWorldContract.Branch.TYPE));
            final boolean favorite = (cursor.getInt(cursor
                    .getColumnIndexOrThrow(SportsWorldContract.Branch.FAVORITE)) == 1);
            final String[] facilities = getFacilities(cursor);
            final String[] facilitiesImgs = getfacilitiesImgs(cursor);
            final String[] imagesUrl = getImagesUrl(cursor);
            final String name = cursor.getString(cursor
                    .getColumnIndexOrThrow(SportsWorldContract.Branch.NAME));
            final String url360 = cursor.getString(cursor
                    .getColumnIndexOrThrow(SportsWorldContract.Branch.URL_360));
            final Branch branch = new Branch(latitude, longitude, distance, stateId, address,
                    schedule, unId, key, dCount, name, videoUrl, preOrder, stateName, phone, type,
                    favorite, facilities,facilitiesImgs, imagesUrl
                   ,url360
                    );
            branches.add(branch);
        }

        return branches;
    }

    /**
	 * Gets the facilities.
	 * 
	 * @param cursor
	 *            the cursor
	 * @return the facilities
	 */
    private String[] getFacilities(Cursor cursor) {
        final String facilities = cursor.getString(cursor
                .getColumnIndexOrThrow(SportsWorldContract.Branch.FACILITIES));
        if (facilities == null) {
            return new String[0];
        }
        return TextUtils.split(facilities, Branch.DELIMITER);
    }
    
    /**
	 * Gets the facilities imgs.
	 * 
	 * @param cursor
	 *            the cursor
	 * @return the facilities imgs
	 */
    private String[] getfacilitiesImgs(Cursor cursor) {
        final String facilities = cursor.getString(cursor
                .getColumnIndexOrThrow(SportsWorldContract.Branch.FAC_URL_IMGS));
        if (facilities == null) {
            return new String[0];
        }
        return TextUtils.split(facilities, Branch.DELIMITER);
    }

    /**
	 * Gets the images url.
	 * 
	 * @param cursor
	 *            the cursor
	 * @return the images url
	 */
    private String[] getImagesUrl(Cursor cursor) {
        final String imagesUrl = cursor.getString(cursor
                .getColumnIndexOrThrow(SportsWorldContract.Branch.IMAGES_URLS));
        if (imagesUrl == null) {
            return new String[0];
        }
        return TextUtils.split(imagesUrl, Branch.DELIMITER);
    }

}
