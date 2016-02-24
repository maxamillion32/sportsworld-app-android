package mx.com.sportsworld.sw.adapter;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.pojo.AwardItemPojo;
import mx.com.sportsworld.sw.utils.GeneralUtils;

// TODO: Auto-generated Javadoc

/**
 * The Class AwardAdapter.
 */
public class AwardAdapter extends ArrayAdapter<AwardItemPojo> {

	/** The par. */
	boolean par = false;
	
	/** The background. */
	int background;

	/**
	 * Instantiates a new award adapter.
	 *
	 * @param context the context
	 */
	public AwardAdapter(Context context) {
		super(context, 0);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (position % 2 == 0) {
			background = R.drawable.celda_one_award;
		} else
			background = R.drawable.celda_odd_transaction;

		if (convertView == null) {

			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.list_item_award, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.article = (TextView) convertView
					.findViewById(R.id.txt_award_article);
			viewHolder.points = (TextView) convertView
					.findViewById(R.id.txt_award_points);
			convertView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.article.setBackgroundResource(background);
		holder.points.setBackgroundResource(background);

		holder.article.setText(getItem(position).getPremio());
		holder.points.setText(GeneralUtils.thousandFormat(Integer
				.parseInt(getItem(position).getPuntos())));

		return convertView;
	}

	/**
	 * The Class ViewHolder.
	 */
	class ViewHolder {
		
		/** The article. */
		public TextView article;
		
		/** The points. */
		public TextView points;
	}

}
