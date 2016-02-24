package mx.com.sportsworld.sw.activity;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.fragment.NewsListFragment;
import mx.com.sportsworld.sw.fragment.NewsListFragment.OnNewsClickListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;

// TODO: Auto-generated Javadoc

/**
 * Hosts a list of news articles that could be of interest to the customers.
 * 
 * @author Jos� Torres Fuentes 02/10/2013
 * 
 */
public class NewsListActivity extends SherlockFragmentActivity implements
		OnNewsClickListener {
	/*
	 * Currently it only hosts the list, but it could modified to host the
	 * content of the news article if there is enough space.
	 */

	/** The Constant FRAG_TAG_NEWS. */
	private static final String FRAG_TAG_NEWS = "frag_tag_news";

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setTitle(R.string.news);

		if (savedInstanceState == null) {
			final FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			final Fragment f = NewsListFragment.newInstance();
			ft.add(android.R.id.content, f, FRAG_TAG_NEWS);
			ft.commit();
		}

	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.fragment.NewsListFragment.OnNewsClickListener#onNewsClick(long)
	 */
	@Override
	public void onNewsClick(long id) {
		final Intent readNewsArticle = new Intent(this /* context */,
				NewsContentActivity.class);
		readNewsArticle.putExtra(NewsContentActivity.EXTRA_NEWS_ID, id);
		startActivity(readNewsArticle);
	}

}
