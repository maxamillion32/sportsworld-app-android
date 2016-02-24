package mx.com.sportsworld.sw.net.resource;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.Context;

import java.io.IOException;

import org.json.JSONException;

import mx.com.sportsworld.sw.model.Goal;
import mx.com.sportsworld.sw.net.RequestResultParser;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.RestClient.Response;
import mx.com.sportsworld.sw.net.SportsWorldRequestMaker;
import mx.com.sportsworld.sw.parser.GoalParser;

/**
 * The Class GoalResource.
 */
public class GoalResource {

    /** The Constant URL_FETCH_GOALS. */
    private static final String URL_FETCH_GOALS = Resource.URL_API_BASE + "/objectives/";

    /**
	 * Fetch goals.
	 * 
	 * @param context
	 *            the context
	 * @return the request result
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws JSONException
	 *             the jSON exception
	 */
    public static RequestResult<Goal> fetchGoals(Context context) throws IOException, JSONException {
        final SportsWorldRequestMaker requestMaker = new SportsWorldRequestMaker(context);
        final Response response = requestMaker
                .get(URL_FETCH_GOALS, null /* keyValues */, true /* useAuthToken */);
        final RequestResultParser<Goal> parser = new RequestResultParser<Goal>();
        return parser.parseWith(response, new GoalParser());
    }

}
