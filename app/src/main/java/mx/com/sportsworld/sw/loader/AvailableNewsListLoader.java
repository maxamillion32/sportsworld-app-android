package mx.com.sportsworld.sw.loader;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import mx.com.sportsworld.sw.model.NewsArticle;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.resource.NewsArticleResource;
import mx.com.sportsworld.sw.parser.FetchNewsDataJsonParser.FetchNewsData;
import mx.com.sportsworld.sw.parser.NewsArticleContentValuesParser;
import mx.com.sportsworld.sw.provider.BatchOperation;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

// TODO: Auto-generated Javadoc

/**
 * The Class AvailableNewsListLoader.
 */
public class AvailableNewsListLoader extends CursorLoader {

	/**
	 * Instantiates a new available news list loader.
	 *
	 * @param context the context
	 */
	public AvailableNewsListLoader(Context context) {
		super(context);
	}

	/**
	 * Instantiates a new available news list loader.
	 *
	 * @param context the context
	 * @param projection the projection
	 * @param sortOrder the sort order
	 */
	public AvailableNewsListLoader(Context context, String[] projection,
			String sortOrder) {
		super(context, SportsWorldContract.NewsArticle.CONTENT_URI, projection,
				(SportsWorldContract.NewsArticle.AVAILABLE_TODAY + " = 1"),
				null /* selectionArgs */, sortOrder);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.content.AsyncTaskLoader#onLoadInBackground()
	 */
	@Override
	protected Cursor onLoadInBackground() {

		RequestResult<FetchNewsData> result = null;
		try {

			result = NewsArticleResource.fetchNewsArticles(getContext());

			// TODO Uncomment after our api implements correctly the status
			// value.

			final List<FetchNewsData> fetchNewsDataUnits = result.getData();

			if (fetchNewsDataUnits == null || fetchNewsDataUnits.size() == 0) {
				return null;
			}

			final FetchNewsData fetchNewsData = fetchNewsDataUnits.get(0);
			final List<NewsArticle> newsArticles = fetchNewsData
					.getNewsArticles();

			final ContentResolver cr = getContext().getContentResolver();
			final BatchOperation batchOperation = new BatchOperation(
					SportsWorldContract.CONTENT_AUTHORITY, cr);

			final ContentProviderOperation deleteOperation = ContentProviderOperation
					.newDelete(SportsWorldContract.NewsArticle.CONTENT_URI)
					.build();
			batchOperation.add(deleteOperation);

			final ContentValues values = new ContentValues();
			final NewsArticleContentValuesParser contentValuesParser = new NewsArticleContentValuesParser();
			final int newsArticlesCount = newsArticles.size();
			for (int i = 0; i < newsArticlesCount; i++) {
				final NewsArticle newsArticle = newsArticles.get(i);
				contentValuesParser.parse(newsArticle, values);
				final ContentProviderOperation insertOperation = ContentProviderOperation
						.newInsert(SportsWorldContract.NewsArticle.CONTENT_URI)
						.withYieldAllowed(true).withValues(values).build();
				batchOperation.add(insertOperation);
				values.clear();
			}

			batchOperation.execute();
		} catch (IOException e) {
			return null;
		} catch (JSONException e) {
			throw new RuntimeException(
					"Something is wrong with your json parser!");
		}

		return super.onLoadInBackground();

	}

}
