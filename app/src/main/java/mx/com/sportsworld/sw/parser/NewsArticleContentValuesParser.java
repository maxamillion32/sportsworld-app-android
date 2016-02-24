package mx.com.sportsworld.sw.parser;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.ContentValues;

import mx.com.sportsworld.sw.model.NewsArticle;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

/**
 * The Class NewsArticleContentValuesParser.
 */
public class NewsArticleContentValuesParser implements ContentValuesParser<NewsArticle> {

    /* (non-Javadoc)
     * @see com.sportsworld.android.parser.ContentValuesParser#parse(java.lang.Object, android.content.ContentValues)
     */
    @Override
    public ContentValues parse(NewsArticle newsArticle, ContentValues values) {
        if (values == null) {
            values = new ContentValues();
        }
        
        values.put(SportsWorldContract.NewsArticle._ID, newsArticle.getServerId());
        values.put(SportsWorldContract.NewsArticle.PERMANENT, newsArticle.isPermanent());
        values.put(SportsWorldContract.NewsArticle.AUTHOR_NAME, newsArticle.getAuthorName());
        values.put(SportsWorldContract.NewsArticle.AUTHOR_ID, newsArticle.getAuthorId());
        values.put(SportsWorldContract.NewsArticle.UN_ID, newsArticle.getUnId());
        values.put(SportsWorldContract.NewsArticle.IMAGE_URL, newsArticle.getImageUrl());
        values.put(SportsWorldContract.NewsArticle.CONTENT, newsArticle.getContent());
        values.put(SportsWorldContract.NewsArticle.AVAILABLE_TODAY, newsArticle.isAvailableToday());
        values.put(SportsWorldContract.NewsArticle.END_OF_AVAILABILITY, newsArticle.getEndOfAvailability());
        values.put(SportsWorldContract.NewsArticle.RESUME, newsArticle.getResume());
        values.put(SportsWorldContract.NewsArticle.START_OF_AVAILABILITY, newsArticle.getStartOfAvailability());
        values.put(SportsWorldContract.NewsArticle.CATEGORY_ID, newsArticle.getCategoryId());
        values.put(SportsWorldContract.NewsArticle.CATEGORY_NAME, newsArticle.getCategoryName());
        values.put(SportsWorldContract.NewsArticle.TITLE, newsArticle.getTitle());
        
        return values;
    }
    
}
