package mx.com.sportsworld.sw.parser;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.com.sportsworld.sw.model.NewsArticle;
import mx.com.sportsworld.sw.parser.FetchNewsDataJsonParser.FetchNewsData;

/**
 * The Class FetchNewsDataJsonParser.
 */
public class FetchNewsDataJsonParser implements JsonParser<FetchNewsData> {

    /** The Constant ARRAY_PERMANENT_NEWS. */
    private static final String ARRAY_PERMANENT_NEWS = "by_permanent";
    
    /** The Constant ARRAY_BY_DATE_NEWS. */
    private static final String ARRAY_BY_DATE_NEWS = "by_date";

    /* (non-Javadoc)
     * @see com.sportsworld.android.parser.JsonParser#parse(org.json.JSONObject)
     */
    @Override
    public FetchNewsData parse(JSONObject object) throws JSONException {
        
        final NewsArticleJsonParser parser = new NewsArticleJsonParser();
        JSONArray array = object.optJSONArray(ARRAY_PERMANENT_NEWS);
        final List<NewsArticle> permanenNewsArticles = parser.parse(array);
        
        array = object.optJSONArray(ARRAY_BY_DATE_NEWS);
        
        final List<NewsArticle> variableNewsArticles = parser.parse(array);
        if (variableNewsArticles != null) {
            permanenNewsArticles.addAll(variableNewsArticles);
        }
        
        return new FetchNewsData(permanenNewsArticles);
        
    }

    /* (non-Javadoc)
     * @see com.sportsworld.android.parser.JsonParser#parse(org.json.JSONArray)
     */
    @Override
    public List<FetchNewsData> parse(JSONArray array) throws JSONException {
        throw new UnsupportedOperationException();
    }

    /**
	 * The Class FetchNewsData.
	 */
    public static class FetchNewsData {

        /** The m news articles. */
        private final List<NewsArticle> mNewsArticles;

        /**
		 * Instantiates a new fetch news data.
		 * 
		 * @param newsArticles
		 *            the news articles
		 */
        public FetchNewsData(List<NewsArticle> newsArticles) {
            mNewsArticles = newsArticles;
        }
        
        /**
		 * Gets the news articles.
		 * 
		 * @return the news articles
		 */
        public List<NewsArticle> getNewsArticles() {
            return mNewsArticles;
        }

    }

}
