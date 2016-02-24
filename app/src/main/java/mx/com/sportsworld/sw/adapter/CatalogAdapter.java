package mx.com.sportsworld.sw.adapter;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.imgloader.ImageCache;
import mx.com.sportsworld.sw.imgloader.ImageFetcher;
import mx.com.sportsworld.sw.imgloader.ImageFetcher.ImageFetcherParams;
import mx.com.sportsworld.sw.pojo.AwardItemPojo;
import mx.com.sportsworld.sw.utils.GeneralUtils;

// TODO: Auto-generated Javadoc

/**
 * The Class CatalogAdapter.
 */
public class CatalogAdapter extends ArrayAdapter<AwardItemPojo> {

	/** The m img fetcher. */
	private ImageFetcher mImgFetcher;
	
	/** The m on catalog click listener. */
	private OnCatalogClickListener mOnCatalogClickListener;

	/**
	 * Instantiates a new catalog adapter.
	 *
	 * @param context the context
	 */
	public CatalogAdapter(FragmentActivity context) {
		super(context, 0);

		int size = context.getResources().getDimensionPixelSize(
				R.dimen.catalog_item_image_height);

		mImgFetcher = new ImageFetcher(context, ImageCache.findOrCreateCache(
				context, "catalog_items_thumbs"), R.drawable.dummy_logo_sw);
		final ImageFetcherParams params = new ImageFetcherParams();
		params.imageWidth = size;
		params.imageHeight = size;
		mImgFetcher.setFetcherParams(params);
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {

			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.list_item_catalog, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.txt_catalog_premio);
			viewHolder.description = (TextView) convertView
					.findViewById(R.id.txt_catalog_description);
			viewHolder.points = (TextView) convertView
					.findViewById(R.id.txt_catalog_puntos);
			viewHolder.thumbnail = (ImageView) convertView
					.findViewById(R.id.img_catalog_item);
			viewHolder.btn_award = (Button) convertView
					.findViewById(R.id.btn_catalog_carrito);
			convertView.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) convertView.getTag();

		viewHolder.btn_award.setTag(position);
		viewHolder.btn_award.setFocusable(false);
		viewHolder.btn_award.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean isPressed = getItem(position).isPressed();
				getItem(position).setPressed(!isPressed);
				if (!isPressed)
					v.setBackgroundResource(R.drawable.btn_catalog_carrito_mas);
				else
					v.setBackgroundResource(R.drawable.btn_catalog_carrito);

				if (mOnCatalogClickListener != null) {
					mOnCatalogClickListener.onCatalogClick(position);
				}
			}
		});

		if (getItem(position).isPressed())
			viewHolder.btn_award
					.setBackgroundResource(R.drawable.btn_catalog_carrito_mas);
		else
			viewHolder.btn_award
					.setBackgroundResource(R.drawable.btn_catalog_carrito);

		viewHolder.name.setText(getItem(position).getPremio());
		viewHolder.description.setText(getItem(position).getDescription());
		viewHolder.points.setText(GeneralUtils.thousandFormat(Integer
				.parseInt(getItem(position).getPuntos())));
		String url = getItem(position).getImagen();
		if (!url.equals(null))
			mImgFetcher.loadSampledThumbnailImage(url, viewHolder.thumbnail);

		return convertView;
	}

	/**
	 * The Class ViewHolder.
	 */
	class ViewHolder {
		
		/** The name. */
		public TextView name;
		
		/** The description. */
		public TextView description;
		
		/** The points. */
		public TextView points;
		
		/** The thumbnail. */
		public ImageView thumbnail;
		
		/** The btn_award. */
		public Button btn_award;
	}

	/**
	 * Sets the on catalog click listener.
	 *
	 * @param listener the new on catalog click listener
	 */
	public void setOnCatalogClickListener(OnCatalogClickListener listener) {
		mOnCatalogClickListener = listener;
	}

	/**
	 * The listener interface for receiving onCatalogClick events.
	 * The class that is interested in processing a onCatalogClick
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnCatalogClickListener<code> method. When
	 * the onCatalogClick event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnCatalogClickEvent
	 */
	public static interface OnCatalogClickListener {
		
		/**
		 * On catalog click.
		 *
		 * @param position the position
		 */
		void onCatalogClick(int position);
	}
}
