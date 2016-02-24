package mx.com.sportsworld.sw.net.resource;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.com.sportsworld.sw.model.Branch;
import mx.com.sportsworld.sw.net.RequestResultParser;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.RestClient.Response;
import mx.com.sportsworld.sw.net.SportsWorldRequestMaker;
import mx.com.sportsworld.sw.parser.BranchParser;
import mx.com.sportsworld.sw.parser.JsonParser;

/**
 * 
 * Executes web api calls related to Branches.
 * 
 * 
 */
public class BranchResource {

    /** The Constant URL_FETCH_BRANCHES. */
    private static final String URL_FETCH_BRANCHES = Resource.URL_API_BASE
            + "/clubsupster/%1$f/%2$f/%3$d/";

    /** The Constant URL_SHOW_BRANCH. */
    private static final String URL_SHOW_BRANCH = Resource.URL_API_BASE
            + "/club/details_upster/%1$d/%2$d/";

    /** The Constant URL_MARK_BRANCH_AS_FAVORITE. */
    private static final String URL_MARK_BRANCH_AS_FAVORITE = Resource.URL_API_BASE
            + "/club/favorite/set/";

    /** The Constant URL_FETCH_FAVORITE. */
    private static final String URL_FETCH_FAVORITE = Resource.URL_API_BASE + "/club/favorite_upster/%1$d";

    /** The Constant KEY_VALUE_BRANCH_ID. */
    private static final String KEY_VALUE_BRANCH_ID = "club_id";
    
    /** The Constant KEY_VALUE_USER_ID. */
    private static final String KEY_VALUE_USER_ID = "user_id";
    
    /** The Constant KEY_VALUE_STATUS. */
    private static final String KEY_VALUE_STATUS = "status";

    /**
	 * Fetch branches.
	 * 
	 * @param context
	 *            the context
	 * @param latitude
	 *            the latitude
	 * @param longitude
	 *            the longitude
	 * @param userId
	 *            the user id
	 * @return the request result
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws JSONException
	 *             the jSON exception
	 */
    public static RequestResult<Branch> fetchBranches(Context context, double latitude,
            double longitude, long userId) throws IOException, JSONException {

        final String completeUrl = String.format(Locale.US, URL_FETCH_BRANCHES, latitude,
                longitude, userId);

        final SportsWorldRequestMaker requestMaker = new SportsWorldRequestMaker(context);
        ;
        
        final Response response = requestMaker
                .get(completeUrl, null /* keyValues */, true /* useAuthToken */);

        
        
        final BranchParser parser = new BranchParser();
        final CheatRequestResultParser<Branch> resultParser = new CheatRequestResultParser<Branch>();
        final RequestResult<Branch> result = resultParser.parseWith(response, parser);
        return result;

    }

    /**
	 * Gets the branch overview.
	 * 
	 * @param context
	 *            the context
	 * @param branchId
	 *            the branch id
	 * @param userId
	 *            the user id
	 * @return the branch overview
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws JSONException
	 *             the jSON exception
	 */
    public static RequestResult<Branch> getBranchOverview(Context context, long branchId,
            long userId) throws IOException, JSONException {

        final String completeUrl = String.format(Locale.US, URL_SHOW_BRANCH, branchId, userId);

        final SportsWorldRequestMaker requestMaker = new SportsWorldRequestMaker(context);
        final Response response = requestMaker
                .get(completeUrl, null /* keyValues */, true /* useAuthToken */);

        final BranchParser parser = new BranchParser();
        final RequestResultParser<Branch> resultParser = new RequestResultParser<Branch>();
        final RequestResult<Branch> result = resultParser.parseWith(response, parser);
        return result;

    }

    /**
	 * Mark as favorite.
	 * 
	 * @param context
	 *            the context
	 * @param branchId
	 *            the branch id
	 * @param userId
	 *            the user id
	 * @param favorite
	 *            the favorite
	 * @return the request result
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws JSONException
	 *             the jSON exception
	 */
    public static RequestResult<Void> markAsFavorite(Context context, long branchId, long userId,
            boolean favorite) throws IOException, JSONException {

        final Map<String, String> keyValues = new HashMap<String, String>();
        keyValues.put(KEY_VALUE_STATUS, (favorite ? "1" : "0"));
        keyValues.put(KEY_VALUE_USER_ID, String.valueOf(userId));
        keyValues.put(KEY_VALUE_BRANCH_ID, String.valueOf(branchId));

        final SportsWorldRequestMaker requestMaker = new SportsWorldRequestMaker(context);
        ;
        final Response response = requestMaker
                .post(URL_MARK_BRANCH_AS_FAVORITE, keyValues, true /* useAuthToken */);

        final RequestResultParser<Void> resultParser = new RequestResultParser<Void>();
        final RequestResult<Void> requestResult = resultParser
                .parseWith(response, null /* jsonParser */);

        return requestResult;

    }

    /**
	 * Fetch favorite branches.
	 * 
	 * @param context
	 *            the context
	 * @param userId
	 *            the user id
	 * @return the request result
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws JSONException
	 *             the jSON exception
	 */
    public static RequestResult<Branch> fetchFavoriteBranches(Context context, long userId)
            throws IOException, JSONException {
        final String completeUrl = String.format(Locale.US, URL_FETCH_FAVORITE, userId);

        final SportsWorldRequestMaker requestMaker = new SportsWorldRequestMaker(context);
        ;
        final Response response = requestMaker
                .get(completeUrl, null /* keyValues */, true /* useAuthToken */);

        final BranchParser parser = new BranchParser();
        final RequestResultParser<Branch> resultParser = new RequestResultParser<Branch>();
        final RequestResult<Branch> result = resultParser.parseWith(response, parser);
        return result;
    }

    /**
	 * The Class CheatRequestResultParser.
	 * 
	 * @param <E>
	 *            the element type
	 */
    private static class CheatRequestResultParser<E> {

        /** The Constant KEY_SUCCESS. */
        private static final String KEY_SUCCESS = "status";
        
        /** The Constant KEY_MESSAGE. */
        private static final String KEY_MESSAGE = "message";
        
        /** The Constant ARRAY_DATA. */
        private static final String ARRAY_DATA = "data";
        
        /** The Constant OBJECT_DATA. */
        private static final String OBJECT_DATA = ARRAY_DATA;

        /**
		 * Creates a RequestResult using the provided response and parser.
		 * 
		 * @param response
		 *            the response
		 * @param parser
		 *            Cannot be null.
		 * @return the request result
		 * @throws JSONException
		 *             If there is not any jsonObject or array
		 */
        public RequestResult<E> parseWith(Response response, JsonParser<E> parser) throws JSONException {

            if (parser == null) {
                throw new IllegalArgumentException("Parser cannot be null");
            }

            final int responseCode = response.getResponseCode();
            final long expires = response.getExpires();
            final long lastModified = response.getLastModified();

            List<E> data = null;
            boolean gotData = true;
            JSONObject object = null;
            try {
                object = response.getBodyAsJsonObject();
            } catch (JSONException e) {
                gotData = false;
            }

            boolean status = false;
            String message = null;

            if (gotData) {
                status = object.optBoolean(KEY_SUCCESS);
                message = object.optString(KEY_MESSAGE);

                /*
                 * We are gonna cheat here. Since a json object or json array cannot know what's its
                 * key, we will put this key as the attribute "type" of every object in the array.
                 * Our parser expects this key value pair. Horrible, but we don't have any choice.
                 */

                final JSONObject jsonData = object.getJSONObject(OBJECT_DATA);
                final Iterator<String> iterator = jsonData.keys();
                final List<String> types = new ArrayList<String>();
                while (iterator.hasNext()) {
                    types.add(iterator.next());
                }

                data = new ArrayList<E>();
                final int keysCount = types.size();
                for (int i = 0; i < keysCount; i++) {
                    final String type = types.get(i);
                    final JSONArray array = jsonData.getJSONArray(types.get(i));
                    final int arraySize = array.length();
                    for (int j = 0; j < arraySize; j++) {
                        array.getJSONObject(j).put("type", type);
                    }
                    data.addAll(parser.parse(array));
                }

            }

            return new RequestResult<E>(responseCode, status, message, expires, lastModified, data);

        }

    }

}
