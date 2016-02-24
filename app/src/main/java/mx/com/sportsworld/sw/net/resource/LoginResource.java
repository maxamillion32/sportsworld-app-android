package mx.com.sportsworld.sw.net.resource;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.content.Context;

import mx.com.sportsworld.sw.model.UserProfile;
import mx.com.sportsworld.sw.net.RequestResultParser;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.RestClient.Response;
import mx.com.sportsworld.sw.net.SportsWorldRequestMaker;
import mx.com.sportsworld.sw.parser.UserProfileJsonParser;

/**
 * The Class LoginResource.
 */
public class LoginResource {

    /** The Constant URL_LOG_IN. */
    private static final String URL_LOG_IN = Resource.URL_API_BASE + "/login_upster/";
    
    /** The Constant HEADER_KEY_LOG_IN_AUTH_TOKEN. */
    private static final String HEADER_KEY_LOG_IN_AUTH_TOKEN = "auth-key";
    

    /**
	 * Log in as member.
	 * 
	 * @param context
	 *            the context
	 * @param authKey
	 *            the auth key
	 * @return the request result
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws JSONException
	 *             the jSON exception
	 */
    public static RequestResult<UserProfile> logInAsMember(Context context, String authKey)
            throws IOException, JSONException {

        /*
         * We are going to use the authKey on the params, then, we need to create a map to add our
         * auth-key to the header of this request.
         */
        final Map<String, List<String>> header = new HashMap<String, List<String>>(1);
        final List<String> authKeyValue = new ArrayList<String>(1);
        authKeyValue.add(authKey);
        header.put(HEADER_KEY_LOG_IN_AUTH_TOKEN, authKeyValue);
        
        final SportsWorldRequestMaker requestMaker = new SportsWorldRequestMaker(context);
        final Response response = requestMaker
                .post(URL_LOG_IN, header, null /* keyValues */, false /* useAuthToken */);

       
        
        final RequestResultParser<UserProfile> resultParser = new RequestResultParser<UserProfile>();
        final RequestResult<UserProfile> result = resultParser.parseWith(response,
                new UserProfileJsonParser());
        return result;

    }
    

}
