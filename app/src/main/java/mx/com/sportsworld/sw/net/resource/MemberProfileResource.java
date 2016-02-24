package mx.com.sportsworld.sw.net.resource;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.Context;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import mx.com.sportsworld.sw.net.RequestResultParser;
import mx.com.sportsworld.sw.net.SportsWorldRequestMaker;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.RestClient.Response;

/**
 * The Class MemberProfileResource.
 */
public class MemberProfileResource {

    /** The Constant URL_UPDATE. */
    private static final String URL_UPDATE = Resource.URL_API_BASE + "/profile/update/";
    
    /** The Constant KEY_VALUE_USER_ID. */
    private static final String KEY_VALUE_USER_ID = "user_id";
    
    /** The Constant KEY_VALUE_HEIGHT. */
    private static final String KEY_VALUE_HEIGHT = "height";
    
    /** The Constant KEY_VALUE_WEIGHT. */
    private static final String KEY_VALUE_WEIGHT = "weight";
    
    /** The Constant KEY_VALUE_AGE. */
    private static final String KEY_VALUE_AGE = "age";
    
    /** The Constant KEY_VALUE_MEM_UNIQ_ID. */
    private static final String KEY_VALUE_MEM_UNIQ_ID = "memunic_id";

    /**
	 * Update profile.
	 * 
	 * @param context
	 *            the context
	 * @param userId
	 *            the user id
	 * @param height
	 *            the height
	 * @param weight
	 *            the weight
	 * @param age
	 *            the age
	 * @param memUniqId
	 *            the mem uniq id
	 * @return the request result
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws JSONException
	 *             the jSON exception
	 */
    public static RequestResult<Void> updateProfile(Context context, long userId, double height,
            double weight, int age, long memUniqId) throws IOException, JSONException {

        final Map<String, String> keyValues = new HashMap<String, String>();
        keyValues.put(KEY_VALUE_USER_ID, String.valueOf(userId));
        keyValues.put(KEY_VALUE_HEIGHT, String.valueOf(height));
        keyValues.put(KEY_VALUE_WEIGHT, String.valueOf(weight));
        keyValues.put(KEY_VALUE_AGE, String.valueOf(age));
        keyValues.put(KEY_VALUE_MEM_UNIQ_ID, String.valueOf(memUniqId));

        final SportsWorldRequestMaker requestMaker = new SportsWorldRequestMaker(context);
        final Response response = requestMaker.post(URL_UPDATE, keyValues, true /* useAuthToken */);

        final RequestResultParser<Void> resultParser = new RequestResultParser<Void>();
        final RequestResult<Void> requestResult = resultParser
                .parseWith(response, null /* jsonParser */);

        return requestResult;

    }

}
