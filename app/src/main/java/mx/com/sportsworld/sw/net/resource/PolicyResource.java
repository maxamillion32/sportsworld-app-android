package mx.com.sportsworld.sw.net.resource;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.Context;

import java.io.IOException;

import org.json.JSONException;

import mx.com.sportsworld.sw.model.Policy;
import mx.com.sportsworld.sw.net.RequestResultParser;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.RestClient.Response;
import mx.com.sportsworld.sw.net.SportsWorldRequestMaker;
import mx.com.sportsworld.sw.parser.PolicyJsonParser;

/**
 * A collection of methods that use the resources on the api that are related to a policy.
 * 
 * @author Jos� Torres Fuentes 02/10/2013
 * 
 */
public class PolicyResource {

    /** The Constant URL_FETCH_POLICIES. */
    private static final String URL_FETCH_POLICIES = Resource.URL_API_BASE + "/privacy/";

    /**
	 * Fetch policies.
	 * 
	 * @param context
	 *            the context
	 * @return the request result
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws JSONException
	 *             the jSON exception
	 */
    public static RequestResult<Policy> fetchPolicies(Context context) throws IOException,
            JSONException {
        final SportsWorldRequestMaker requestMaker = new SportsWorldRequestMaker(context);
        final Response response = requestMaker
                .get(URL_FETCH_POLICIES, null/* keyValues */, true /* useAuthToken */);

        final RequestResultParser<Policy> resultParser = new RequestResultParser<Policy>();
        final RequestResult<Policy> result = resultParser.parseWith(response,
                new PolicyJsonParser());
        return result;
    }

}
