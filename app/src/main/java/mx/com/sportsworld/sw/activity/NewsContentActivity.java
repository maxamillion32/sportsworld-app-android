package mx.com.sportsworld.sw.activity;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import mx.com.sportsworld.sw.fragment.NewsContentFragment;

import com.actionbarsherlock.app.SherlockFragmentActivity;

// TODO: Auto-generated Javadoc

/**
 * The Class NewsContentActivity.
 */
public class NewsContentActivity extends SherlockFragmentActivity {

	/** The Constant EXTRA_NEWS_ID. */
	public static final String EXTRA_NEWS_ID = "com.upster.extra.NEWS_ID";
	
	/** The Constant FRAG_TAG_NEWS_CONTENT. */
	private static final String FRAG_TAG_NEWS_CONTENT = "frag_tag_news_content";

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {

			final long newsId = getIntent().getLongExtra(EXTRA_NEWS_ID, -1);
			if (newsId == -1) {
				throw new IllegalArgumentException(
						"You must pass a news id as an EXTRA_NEWS_ID extra");
			}

			final FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			final Fragment f = NewsContentFragment.newInstance(newsId);
			ft.add(android.R.id.content, f, FRAG_TAG_NEWS_CONTENT);
			ft.commit();

		}
	}

}
