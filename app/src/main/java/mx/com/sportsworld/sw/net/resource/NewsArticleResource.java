package mx.com.sportsworld.sw.net.resource;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.Context;

import java.io.IOException;

import org.json.JSONException;

import mx.com.sportsworld.sw.net.RequestResultParser;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.RestClient.Response;
import mx.com.sportsworld.sw.net.SportsWorldRequestMaker;
import mx.com.sportsworld.sw.parser.FetchNewsDataJsonParser;
import mx.com.sportsworld.sw.parser.FetchNewsDataJsonParser.FetchNewsData;

/**
 * The Class NewsArticleResource.
 */
public class NewsArticleResource {

    /** The Constant URL_FETCH_NEWS_ARTICLES. */
    private static final String URL_FETCH_NEWS_ARTICLES = Resource.URL_API_BASE + "/news/";

    /**
	 * Fetch news articles.
	 * 
	 * @param context
	 *            the context
	 * @return the request result
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws JSONException
	 *             the jSON exception
	 */
    public static RequestResult<FetchNewsData> fetchNewsArticles(Context context)
            throws IOException, JSONException {

        final SportsWorldRequestMaker requestMaker = new SportsWorldRequestMaker(context);
        final Response response = requestMaker.get(URL_FETCH_NEWS_ARTICLES, null/* keyValues */,
                true /* useAuthToken */);

        final RequestResultParser<FetchNewsData> resultParser = new RequestResultParser<FetchNewsData>();
        final RequestResult<FetchNewsData> result = resultParser.parseWith(response,
                new FetchNewsDataJsonParser());

        return result;

    }

}
