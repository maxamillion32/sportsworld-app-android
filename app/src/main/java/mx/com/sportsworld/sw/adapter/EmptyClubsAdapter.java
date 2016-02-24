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

import mx.com.sportsworld.sw.R;

// TODO: Auto-generated Javadoc

/**
 * The Class EmptyFavoritesAdapter.
 */
public class EmptyClubsAdapter extends ArrayAdapter<String> {

	/**
	 * Instantiates a new empty favorites adapter.
	 *
	 * @param context the context
	 */
	public EmptyClubsAdapter(Context context) {
		super(context, 0);
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = LayoutInflater.from(getContext()).inflate(
				R.layout.item_empty_club, null);

		return convertView;
	}

	/* (non-Javadoc)
	 * @see android.widget.BaseAdapter#isEnabled(int)
	 */
	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return false;
	}
}
