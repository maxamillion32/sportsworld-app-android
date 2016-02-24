package mx.com.sportsworld.sw.adapter;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.imgloader.ImageCache;
import mx.com.sportsworld.sw.imgloader.ImageFetcher;
import mx.com.sportsworld.sw.imgloader.ImageFetcher.ImageFetcherParams;
import mx.com.sportsworld.sw.model.NewsArticle;

// TODO: Auto-generated Javadoc

/**
 * The Class NewsAdapter.
 */
public class NewsAdapter extends ArrayAdapter<NewsArticle> {

	/** The m img fetcher. */
	private ImageFetcher mImgFetcher;

	/**
	 * Instantiates a new news adapter.
	 *
	 * @param context the context
	 */
	public NewsAdapter(FragmentActivity context) {
		super(context, 0);
		final int size = context.getResources().getDimensionPixelSize(
				R.dimen.news_image_thumb_size);
		mImgFetcher = new ImageFetcher(context, ImageCache.findOrCreateCache(
				context, "news_articles_thumbs"), R.drawable.empty_photo);
		final ImageFetcherParams params = new ImageFetcherParams();
		params.imageWidth = size;
		params.imageHeight = size;
		mImgFetcher.setFetcherParams(params);
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.list_item_news_article, null);
			holder.thumb = (ImageView) convertView
					.findViewById(R.id.mgv_news_image);
			holder.tittle = (TextView) convertView.findViewById(R.id.txt_title);
			holder.text = (TextView) convertView.findViewById(R.id.txt_resume);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		mImgFetcher.loadSampledThumbnailImage(getItem(position).getImageUrl(),
				holder.thumb);
		holder.tittle.setText(getItem(position).getTitle());
		holder.text.setText(getItem(position).getResume());
		return convertView;
	}

	/**
	 * The Class ViewHolder.
	 */
	class ViewHolder {
		
		/** The thumb. */
		public ImageView thumb;
		
		/** The tittle. */
		public TextView tittle;
		
		/** The text. */
		public TextView text;
	}

}
