package mx.com.sportsworld.sw.fragment;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.Html;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.adapter.NewsAdapter;
import mx.com.sportsworld.sw.app.SherlockWorkingListFragment;
import mx.com.sportsworld.sw.fragment.dialog.ProgressDialogFragment;
import mx.com.sportsworld.sw.imgloader.ImageCache;
import mx.com.sportsworld.sw.imgloader.ImageFetcher;
import mx.com.sportsworld.sw.imgloader.ImageFetcher.ImageFetcherParams;
import mx.com.sportsworld.sw.loader.FinancialInfoListLoader;
import mx.com.sportsworld.sw.model.NewsArticle;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.resource.NewsArticleResource;
import mx.com.sportsworld.sw.parser.FetchNewsDataJsonParser.FetchNewsData;
import mx.com.sportsworld.sw.parser.NewsArticleContentValuesParser;
import mx.com.sportsworld.sw.provider.BatchOperation;
import mx.com.sportsworld.sw.provider.SportsWorldContract;
import mx.com.sportsworld.sw.utils.ConnectionUtils;

// TODO: Auto-generated Javadoc

/**
 * Shows a list of news articles that could be of interest to the customers.
 * 
 * @author Jos� Torres Fuentes 02/10/2013
 * 
 */
public class NewsListFragment extends SherlockWorkingListFragment implements
		LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

	/** The Constant THUMB_FOLDER. */
	private static final String THUMB_FOLDER = "news_articles_thumbs";
	
	/** The Constant LOADER_ID_NEWS. */
	private static final int LOADER_ID_NEWS = 0;
	
	/** The Constant LOADER_ID_FINANCIAL_INFO. */
	private static final int LOADER_ID_FINANCIAL_INFO = 1;
	
	/** The Constant COL_INDEX_NEWS_TITLE. */
	private static final int COL_INDEX_NEWS_TITLE = 0;
	
	/** The Constant COL_INDEX_NEWS_RESUME. */
	private static final int COL_INDEX_NEWS_RESUME = 1;
	
	/** The Constant COL_INDEX_NEWS_IMAGE_URL. */
	private static final int COL_INDEX_NEWS_IMAGE_URL = 2;
	
	/** The Constant COL_INDEX_NEWS_ID. */
	private static final int COL_INDEX_NEWS_ID = 3;
	
	/** The Constant COL_INDEX_FINANCIAL_INFO_LAST_TRADE. */
	private static final int COL_INDEX_FINANCIAL_INFO_LAST_TRADE = 0;
	
	/** The Constant COL_INDEX_FINANCIAL_INFO_CHANGE. */
	private static final int COL_INDEX_FINANCIAL_INFO_CHANGE = 1;
	
	/** The Constant COL_INDEX_FINANCIAL_INFO_RESULT. */
	private static final int COL_INDEX_FINANCIAL_INFO_RESULT = 2;
	
	/** The Constant COLS_NEWS. */
	private static final String[] COLS_NEWS = buildNewsColumns();
	
	/** The Constant COLS_FINANCIAL_INFO. */
	private static final String[] COLS_FINANCIAL_INFO = buildFinancialInfoColumns();
	
	/** The Constant TO_NEWS_ITEM. */
	private static final int[] TO_NEWS_ITEM = new int[] { R.id.txt_title,
			R.id.txt_resume, R.id.mgv_news_image };
	
	/** The m btn retry news fetch. */
	private Button mBtnRetryNewsFetch;
	
	/** The m txts financial info last trade. */
	private TextSwitcher mTxtsFinancialInfoLastTrade;
	
	/** The m txts financial info change and result. */
	private TextSwitcher mTxtsFinancialInfoChangeAndResult;
	
	/** The m listener. */
	private OnNewsClickListener mListener;
	
	/** The m adapter. */
	private SimpleCursorAdapter mAdapter;
	
	/** The m img cache. */
	private ImageCache mImgCache;
	
	/** The m btn retry financial info. */
	private Button mBtnRetryFinancialInfo;
	
	/** The m lnr financial info content. */
	private View mLnrFinancialInfoContent;
	
	/** The new adapter. */
	private NewsAdapter newAdapter;
	
	/** The handler. */
	private Handler handler;
	
	/** The recharge list. */
	private boolean rechargeList;
	
	/** The progress. */
	public static ProgressDialogFragment progress;
	
	/** The dialgo fragment. */
	Fragment dialgoFragment;

	/**
	 * Builds the news columns.
	 *
	 * @return the string[]
	 */
	private static String[] buildNewsColumns() {

		final SparseArray<String> colsMap = new SparseArray<String>();
		colsMap.put(COL_INDEX_NEWS_TITLE, SportsWorldContract.NewsArticle.TITLE);
		colsMap.put(COL_INDEX_NEWS_RESUME,
				SportsWorldContract.NewsArticle.RESUME);
		colsMap.put(COL_INDEX_NEWS_IMAGE_URL,
				SportsWorldContract.NewsArticle.IMAGE_URL);
		colsMap.put(COL_INDEX_NEWS_ID, SportsWorldContract.NewsArticle._ID);

		final int colCount = colsMap.size();
		final String[] cols = new String[colCount];
		for (int i = 0; i < colCount; i++) {
			cols[i] = colsMap.get(i);
		}

		return cols;

	}

	/**
	 * Builds the financial info columns.
	 *
	 * @return the string[]
	 */
	private static String[] buildFinancialInfoColumns() {

		final SparseArray<String> colsMap = new SparseArray<String>();
		colsMap.put(COL_INDEX_FINANCIAL_INFO_LAST_TRADE,
				SportsWorldContract.FinancialInfo.LAST_TRADE);
		colsMap.put(COL_INDEX_FINANCIAL_INFO_CHANGE,
				SportsWorldContract.FinancialInfo.CHANGE);
		colsMap.put(COL_INDEX_FINANCIAL_INFO_RESULT,
				SportsWorldContract.FinancialInfo.RESULT);

		final int colCount = colsMap.size();
		final String[] cols = new String[colCount];
		for (int i = 0; i < colCount; i++) {
			cols[i] = colsMap.get(i);
		}

		return cols;

	}

	/**
	 * Instantiates a new news list fragment.
	 */
	public NewsListFragment() {
	}

	/**
	 * New instance.
	 *
	 * @return the news list fragment
	 */
	public static NewsListFragment newInstance() {
		return new NewsListFragment();
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnNewsClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.getClass().getName()
					+ " must implement " + OnNewsClickListener.class.getName()
					+ ".");
		}
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.app.SherlockWorkingListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_news_list,
				null /* root */, false /* attachToRoot */);

		newAdapter = new NewsAdapter(getActivity());
		handler = new Handler();

		final TextView txtFinancialInfoTitle = (TextView) view
				.findViewById(R.id.txt_financial_info_title);
		txtFinancialInfoTitle.setText(Html
				.fromHtml(getString(R.string.financial_info_title)));

		mLnrFinancialInfoContent = view
				.findViewById(R.id.lnr_financial_info_content);

		mTxtsFinancialInfoLastTrade = (TextSwitcher) view
				.findViewById(R.id.txt_financial_info_last_trade);
		mTxtsFinancialInfoChangeAndResult = (TextSwitcher) view
				.findViewById(R.id.txt_financial_info_change_and_result);
		mBtnRetryNewsFetch = (Button) view
				.findViewById(R.id.btn_retry_news_fetch);
		mBtnRetryFinancialInfo = (Button) view
				.findViewById(R.id.btn_retry_fetch_financial_info);

		mBtnRetryNewsFetch.setOnClickListener(this);
		mBtnRetryFinancialInfo.setOnClickListener(this);

		return view;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		showFragmentsDialog(false);
		mImgCache = ImageCache.findOrCreateCache(getActivity(), THUMB_FOLDER);
		mAdapter = new SimpleCursorAdapter(getActivity() /* context */,
				R.layout.list_item_news_article, null /* cursor */, COLS_NEWS,
				TO_NEWS_ITEM, 0 /* flags */);
		mAdapter.setViewBinder(new NewsListFragmentViewBinder(
				getActivity() /* context */, mImgCache));
		setListAdapter(mAdapter);
		setListShown(false);

		final LoaderManager loaderMgr = getLoaderManager();
		if (savedInstanceState == null)
			if ((loaderMgr.getLoader(LOADER_ID_NEWS) != null)
					|| (ConnectionUtils
							.isNetworkAvailable(getActivity() /* context */))) {
				rechargeList = true;
				loaderMgr
						.initLoader(LOADER_ID_NEWS, null /* loaderArgs */, this /* loaderCallback */);
				loaderMgr.initLoader(LOADER_ID_FINANCIAL_INFO,
						null /* loaderArgs */, this /* loaderCallback */);
			} else {
				setEmptyText(getString(R.string.error_connection));
				setListShownNoAnimation(true);
				mBtnRetryNewsFetch.setVisibility(View.VISIBLE);
				showFinancialInfoRetryButton(true);
			}

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		super.onStop();
		mImgCache.close();
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.app.SherlockWorkingListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		NewsArticle news = newAdapter.getItem(position);
		Log.i("DATA", news.getAuthorName());
		mListener.onNewsClick(news.getServerId());
	}

	/**
	 * The listener interface for receiving onNewsClick events.
	 * The class that is interested in processing a onNewsClick
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnNewsClickListener<code> method. When
	 * the onNewsClick event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnNewsClickEvent
	 */
	public static interface OnNewsClickListener {
		
		/**
		 * On news click.
		 *
		 * @param id the id
		 */
		void onNewsClick(long id);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		switch (id) {

		case LOADER_ID_NEWS:
		
			if (rechargeList)
				new Thread(new Runnable() {
					@Override
					public void run() {
						RequestResult<FetchNewsData> result;
						rechargeList = false;
						try {
							result = NewsArticleResource
									.fetchNewsArticles(getActivity());
							final List<FetchNewsData> fetchNewsDataUnits = result
									.getData();
							if (getActivity() != null)
								handler.post(new Runnable() {
									@Override
									public void run() {
										if (fetchNewsDataUnits == null) {
											setEmptyText(getString(R.string.error_connection));
											mBtnRetryNewsFetch
													.setVisibility(View.VISIBLE);
										} else {
											setEmptyText(getString(R.string.empty_news_list));
											mBtnRetryNewsFetch
													.setVisibility(View.GONE);
										}
										setListAdapter(newAdapter);
										final FetchNewsData fetchNewsData = fetchNewsDataUnits
												.get(0);
										final List<NewsArticle> newsArticles = fetchNewsData
												.getNewsArticles();

										final ContentResolver cr = getActivity()
												.getContentResolver();
										final BatchOperation batchOperation = new BatchOperation(
												SportsWorldContract.CONTENT_AUTHORITY,
												cr);

										final ContentProviderOperation deleteOperation = ContentProviderOperation
												.newDelete(
														SportsWorldContract.NewsArticle.CONTENT_URI)
												.build();
										batchOperation.add(deleteOperation);

										final NewsArticleContentValuesParser contentValuesParser = new NewsArticleContentValuesParser();
										final int newsArticlesCount = newsArticles
												.size();
										newAdapter.clear();
										for (int i = 0; i < newsArticlesCount; i++) {
											final ContentValues values = new ContentValues();
											final NewsArticle newsArticle = newsArticles
													.get(i);
											newAdapter.add(newsArticle);
											contentValuesParser.parse(
													newsArticle, values);
											final ContentProviderOperation insertOperation = ContentProviderOperation
													.newInsert(
															SportsWorldContract.NewsArticle.CONTENT_URI)
													.withYieldAllowed(true)
													.withValues(values).build();
											batchOperation.add(insertOperation);
											values.clear();
										}
										batchOperation.execute();

										if (isResumed()) {
											setListShown(true);
										} else {
											setListShownNoAnimation(true);
										}
										if (progress != null)
											progress.dismissAllowingStateLoss();

									}

								});

						} catch (IOException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (progress != null)
							progress.dismissAllowingStateLoss();
					}
				}).start();

			return null;

		case LOADER_ID_FINANCIAL_INFO:
			return new FinancialInfoListLoader(getActivity() /* context */,
					COLS_FINANCIAL_INFO, null /* sortOrder */);

		default:
			throw new UnsupportedOperationException();

		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		switch (loader.getId()) {

		case LOADER_ID_NEWS:
			populateNewsList(cursor);
			break;

		case LOADER_ID_FINANCIAL_INFO:
			populateFinancialInfo(cursor);
			break;

		default:
			throw new UnsupportedOperationException();

		}

	}

	/**
	 * Populate news list.
	 *
	 * @param cursor the cursor
	 */
	private void populateNewsList(Cursor cursor) {
		if (cursor == null) {
			setEmptyText(getString(R.string.error_connection));
			mBtnRetryNewsFetch.setVisibility(View.VISIBLE);
		} else {
			setEmptyText(getString(R.string.empty_news_list));
			mBtnRetryNewsFetch.setVisibility(View.GONE);
		}

		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}

		mAdapter.swapCursor(cursor);
	}

	/**
	 * Populate financial info.
	 *
	 * @param cursor the cursor
	 */
	private void populateFinancialInfo(Cursor cursor) {

		if (cursor == null) {
			showFinancialInfoRetryButton(true);
			return;
		}

		if (!cursor.moveToFirst()) {
			showFinancialInfoRetryButton(false);
			mTxtsFinancialInfoLastTrade
					.setText(getString(R.string.empty_financial_info));
			mTxtsFinancialInfoChangeAndResult.setText(null);
			return;
		}

		showFinancialInfoRetryButton(false);

		final double lastTrade = cursor
				.getDouble(COL_INDEX_FINANCIAL_INFO_LAST_TRADE);
		final double change = cursor.getDouble(COL_INDEX_FINANCIAL_INFO_CHANGE);
		final double result = cursor.getDouble(COL_INDEX_FINANCIAL_INFO_RESULT);

		final String financialInfoLastTrade = String.format(Locale.US,
				getString(R.string.financial_info_last_trade), lastTrade);

		String financialInfoChangeAndResult = String.format(Locale.US,
				getString(R.string.financial_info_change_and_result), change);

		DecimalFormat decim = new DecimalFormat("0.00");

		financialInfoChangeAndResult += "  " + decim.format(result) + "%";

		mTxtsFinancialInfoLastTrade.setText(financialInfoLastTrade);
		mTxtsFinancialInfoChangeAndResult.setText(financialInfoChangeAndResult);

		final TextView txtFinancialInfoChangeAndResult = (TextView) mTxtsFinancialInfoChangeAndResult
				.getCurrentView();

		int arrow = 0; // 0 means no drawable.
		if (result < 0) {
			arrow = R.drawable.ic_financial_result_down;
		} else if (result > 0) {
			arrow = R.drawable.ic_financial_result_up;
		}

		txtFinancialInfoChangeAndResult
				.setCompoundDrawablesWithIntrinsicBounds(arrow,
						0 /* topDrawable */, 0 /* rightDrawable */, 0 /* bottomDrawable */);

	}

	/**
	 * Show financial info retry button.
	 *
	 * @param show the show
	 */
	private void showFinancialInfoRetryButton(boolean show) {
		if (show) {
			mLnrFinancialInfoContent.setVisibility(View.GONE);
			mBtnRetryFinancialInfo.setVisibility(View.VISIBLE);
		} else {
			mLnrFinancialInfoContent.setVisibility(View.VISIBLE);
			mBtnRetryFinancialInfo.setVisibility(View.GONE);
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

		switch (loader.getId()) {

		case LOADER_ID_NEWS:
			mAdapter.swapCursor(null);
			break;

		case LOADER_ID_FINANCIAL_INFO:
			/* do nothing */
			break;

		default:
			throw new UnsupportedOperationException();

		}

	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_retry_news_fetch:
			retryNewsFetch();
			break;
		case R.id.btn_retry_fetch_financial_info:
			retryFinancialInfoFetch();
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Retry news fetch.
	 */
	private void retryNewsFetch() {
		if (mayShowConnectionErrorToast()) {
			return;
		}
		setListShown(false);
		getLoaderManager()
				.restartLoader(LOADER_ID_NEWS, null /* args */, this /* loaderCallback */);
	}

	/**
	 * Retry financial info fetch.
	 */
	private void retryFinancialInfoFetch() {
		if (mayShowConnectionErrorToast()) {
			return;
		}
		showFinancialInfoRetryButton(false);
		mTxtsFinancialInfoLastTrade.setText(getString(R.string.loading));
		mTxtsFinancialInfoChangeAndResult.setText(null);
		getLoaderManager().restartLoader(LOADER_ID_FINANCIAL_INFO,
				null /* args */, this /* loaderCallback */);
	}

	/**
	 * May show connection error toast.
	 *
	 * @return true, if successful
	 */
	private boolean mayShowConnectionErrorToast() {
		if (!ConnectionUtils.isNetworkAvailable(getActivity() /* context */)) {
			Toast.makeText(getActivity() /* context */,
					R.string.error_connection, Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}

	/**
	 * The Class NewsListFragmentViewBinder.
	 */
	private static class NewsListFragmentViewBinder implements ViewBinder {

		/** The m img fetcher. */
		private ImageFetcher mImgFetcher;

		/**
		 * Instantiates a new news list fragment view binder.
		 *
		 * @param context the context
		 * @param cache the cache
		 */
		public NewsListFragmentViewBinder(Context context, ImageCache cache) {
			final int size = context.getResources().getDimensionPixelSize(
					R.dimen.news_image_thumb_size);
			mImgFetcher = new ImageFetcher(context, cache,
					R.drawable.empty_photo);
			final ImageFetcherParams params = new ImageFetcherParams();
			params.imageWidth = size;
			params.imageHeight = size;
			mImgFetcher.setFetcherParams(params);

		}

		/* (non-Javadoc)
		 * @see android.support.v4.widget.SimpleCursorAdapter.ViewBinder#setViewValue(android.view.View, android.database.Cursor, int)
		 */
		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if (view.getId() != R.id.mgv_news_image) {
				return false;
			}

			final String imageUrl = cursor.getString(columnIndex);
			mImgFetcher.loadSampledThumbnailImage(imageUrl, (ImageView) view);
			return true;
		}

	}

	/**
	 * Show fragments dialog.
	 *
	 * @param savedInstanceState the saved instance state
	 */
	public void showFragmentsDialog(boolean savedInstanceState) {
		dialgoFragment = getActivity().getSupportFragmentManager()
				.findFragmentByTag(ProgressDialogFragment.progressDialogTag);

		if (savedInstanceState == true) {
			dialgoFragment = getActivity()
					.getSupportFragmentManager()
					.findFragmentByTag(ProgressDialogFragment.progressDialogTag);
			progress = (ProgressDialogFragment) dialgoFragment;

		} else {

			if (dialgoFragment != null) {
				getActivity().getSupportFragmentManager().beginTransaction()
						.remove(dialgoFragment).commit();
			}

			progress = ProgressDialogFragment.newInstance(getActivity());
			progress.show(getActivity().getSupportFragmentManager(),
					ProgressDialogFragment.progressDialogTag);
		}

	}

}
