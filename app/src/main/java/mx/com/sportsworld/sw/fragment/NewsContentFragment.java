package mx.com.sportsworld.sw.fragment;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.io.UnsupportedEncodingException;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Base64;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockFragment;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

// TODO: Auto-generated Javadoc

/**
 * The Class NewsContentFragment.
 */
public class NewsContentFragment extends SherlockFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

	/** The Constant FRAG_ARG_NEWS_ID. */
	private static final String FRAG_ARG_NEWS_ID = "frag_args_news_id";
	
	/** The Constant NEWS_CONTENT_MIME_TYPE. */
	private static final String NEWS_CONTENT_MIME_TYPE = "text/html; charset=utf-8";
	
	/** The Constant NEWS_CONTENT_ENCODING. */
	private static final String NEWS_CONTENT_ENCODING = "base64";
	
	/** The Constant LOADER_ID_NEWS_CONTENT. */
	private static final int LOADER_ID_NEWS_CONTENT = 0;
	
	/** The Constant LOADER_TITLE_NEWS. */
	private static final int LOADER_TITLE_NEWS = 1;
	
	/** The Constant COL_INDEX_CONTENT. */
	private static final int COL_INDEX_CONTENT = 0;
	
	/** The Constant COLS. */
	private static final String[] COLS = buildColumns();
	
	/** The m wbv content. */
	private WebView mWbvContent;

	/**
	 * Builds the columns.
	 *
	 * @return the string[]
	 */
	private static String[] buildColumns() {

		final SparseArray<String> colsMap = new SparseArray<String>();
		colsMap.put(COL_INDEX_CONTENT, SportsWorldContract.NewsArticle.CONTENT);
		colsMap.put(LOADER_TITLE_NEWS, SportsWorldContract.NewsArticle.TITLE);

		final int colCount = colsMap.size();
		final String[] cols = new String[colCount];
		for (int i = 0; i < colCount; i++) {
			cols[i] = colsMap.get(i);
		}

		return cols;

	}

	/**
	 * Instantiates a new news content fragment.
	 */
	public NewsContentFragment() {
	}

	/**
	 * New instance.
	 *
	 * @param id the id
	 * @return the news content fragment
	 */
	public static NewsContentFragment newInstance(long id) {
		final NewsContentFragment f = new NewsContentFragment();
		final Bundle args = new Bundle();
		args.putLong(FRAG_ARG_NEWS_ID, id);
		f.setArguments(args);
		return f;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_news_content,
				null /* root */, false /* attachToRoot */);
		mWbvContent = (WebView) view.findViewById(R.id.wbv_content);
		mWbvContent.getSettings().setLoadWithOverviewMode(true);
		mWbvContent.getSettings().setUseWideViewPort(true);
		mWbvContent.getSettings().setLayoutAlgorithm(
				LayoutAlgorithm.SINGLE_COLUMN);
		return view;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getLoaderManager().initLoader(LOADER_ID_NEWS_CONTENT,
				null /* loaderArgs */, this /* loaderCallback */);
	}

	/**
	 * Gets the id from arguments.
	 *
	 * @return the id from arguments
	 */
	public long getIdFromArguments() {
		return getArguments().getLong(FRAG_ARG_NEWS_ID);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		final String newsArticleId = String.valueOf(getIdFromArguments());
		final Uri newsArticleUri = SportsWorldContract.NewsArticle
				.buildNewsArticleUri(newsArticleId);
		return new CursorLoader(getActivity() /* context */, newsArticleUri,
				COLS, null /* selection */, null /* selectionArgs */, null /* sortOrder */);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (!cursor.moveToFirst()) {
			return;
		}

		final String content = cursor.getString(COL_INDEX_CONTENT);
		final String title = cursor.getString(LOADER_TITLE_NEWS);
		getSherlockActivity().getSupportActionBar().setTitle(title);
		try {
			final String webPageAsBase64 = Base64.encodeToString(
					content.getBytes("utf-8"), Base64.DEFAULT);
			mWbvContent.loadData(webPageAsBase64, NEWS_CONTENT_MIME_TYPE,
					NEWS_CONTENT_ENCODING);
		} catch (UnsupportedEncodingException ignore) {
			/* Should not happen */
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		/* Do nothing as we don't hold any reference to the cursor */
	}

}
