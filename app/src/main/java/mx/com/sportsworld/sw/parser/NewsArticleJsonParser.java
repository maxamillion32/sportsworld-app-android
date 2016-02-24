package mx.com.sportsworld.sw.parser;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.com.sportsworld.sw.model.NewsArticle;

/**
 * The Class NewsArticleJsonParser.
 */
public class NewsArticleJsonParser implements JsonParser<NewsArticle> {

    /** The Constant KEY_PERMANENT. */
    private static final String KEY_PERMANENT = "permanente";
    
    /** The Constant KEY_AUTHOR_NAME. */
    private static final String KEY_AUTHOR_NAME = "columnista";
    
    /** The Constant KEY_AUTHOR_ID. */
    private static final String KEY_AUTHOR_ID = "idpersona";
    
    /** The Constant KEY_UN_ID. */
    private static final String KEY_UN_ID = "idun";
    
    /** The Constant KEY_IMAGE_URL. */
    private static final String KEY_IMAGE_URL = "rutaimagen";
    
    /** The Constant KEY_CONTENT. */
    private static final String KEY_CONTENT = "descripcion";
    
    /** The Constant KEY_AVAILABLE_TODAY. */
    private static final String KEY_AVAILABLE_TODAY = "vigentehoy";
    
    /** The Constant KEY_SERVER_ID. */
    private static final String KEY_SERVER_ID = "idnoticias";
    
    /** The Constant KEY_END_OF_AVAILABILITY. */
    private static final String KEY_END_OF_AVAILABILITY = "finvigencia";
    
    /** The Constant KEY_RESUME. */
    private static final String KEY_RESUME = "resumen";
    
    /** The Constant KEY_START_OF_AVAILABILITY. */
    private static final String KEY_START_OF_AVAILABILITY = "iniciovigencia";
    
    /** The Constant KEY_CATEGORY_ID. */
    private static final String KEY_CATEGORY_ID = "idnoticiascategoria";
    
    /** The Constant KEY_CATEGORY_NAME. */
    private static final String KEY_CATEGORY_NAME = "categorianoticia";
    
    /** The Constant KEY_TITLE. */
    private static final String KEY_TITLE = "titulo";
    
    /** The Constant DATE_FORMAT. */
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    /* (non-Javadoc)
     * @see com.sportsworld.android.parser.JsonParser#parse(org.json.JSONObject)
     */
    @Override
    public NewsArticle parse(JSONObject object) throws JSONException {

        final boolean permanent = (object.optInt(KEY_PERMANENT) == 1);
        final String authorName = object.optString(KEY_AUTHOR_NAME);
        final long authorId = object.optLong(KEY_AUTHOR_ID);
        final long unId = object.optLong(KEY_UN_ID);
        final String imageUrl = object.optString(KEY_IMAGE_URL);
        final String content = object.optString(KEY_CONTENT);
        final boolean availableToday = (object.optInt(KEY_AVAILABLE_TODAY) == 1);
        final long serverId = object.optLong(KEY_SERVER_ID);
        final String endOfAvailability = object.optString(KEY_END_OF_AVAILABILITY);
        final String resume = object.optString(KEY_RESUME);
        final long categoryId = object.optLong(KEY_CATEGORY_ID);
        final String categoryName = object.optString(KEY_CATEGORY_NAME);
        final String title = object.optString(KEY_TITLE);

        Date startOfAvailability;
        try {
            startOfAvailability = new SimpleDateFormat(DATE_FORMAT, Locale.US).parse(object
                    .optString(KEY_START_OF_AVAILABILITY));
        } catch (ParseException e) {
            startOfAvailability = null;
        }

        return new NewsArticle(permanent, authorName, authorId, unId, imageUrl, content,
                availableToday, serverId, endOfAvailability, resume, startOfAvailability,
                categoryId, categoryName, title);
    }

    /* (non-Javadoc)
     * @see com.sportsworld.android.parser.JsonParser#parse(org.json.JSONArray)
     */
    @Override
    public List<NewsArticle> parse(JSONArray array) throws JSONException {
        final int count = array.length();
        final List<NewsArticle> newsArticles = new ArrayList<NewsArticle>(count);
        for (int i = 0; i < count; i++) {
            newsArticles.add(parse(array.getJSONObject(i)));
        }
        return newsArticles;
    }

}
