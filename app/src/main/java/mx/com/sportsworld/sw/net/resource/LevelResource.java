package mx.com.sportsworld.sw.net.resource;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.Context;

import java.io.IOException;

import org.json.JSONException;

import mx.com.sportsworld.sw.model.Level;
import mx.com.sportsworld.sw.net.RequestResultParser;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.RestClient.Response;
import mx.com.sportsworld.sw.net.SportsWorldRequestMaker;
import mx.com.sportsworld.sw.parser.LevelParser;

/**
 * The Class LevelResource.
 */
public class LevelResource {

    /** The Constant URL_FETCH_LEVELS. */
    private static final String URL_FETCH_LEVELS = Resource.URL_API_BASE + "/levels/";

    /**
	 * Fetch levels.
	 * 
	 * @param context
	 *            the context
	 * @return the request result
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws JSONException
	 *             the jSON exception
	 */
    public static RequestResult<Level> fetchLevels(Context context) throws IOException,
            JSONException {
        final SportsWorldRequestMaker requestMaker = new SportsWorldRequestMaker(context);
        final Response response = requestMaker
                .get(URL_FETCH_LEVELS, null /* keyValues */, true /* useAuthToken */);
        final RequestResultParser<Level> parser = new RequestResultParser<Level>();
        return parser.parseWith(response, new LevelParser());
    }

}
