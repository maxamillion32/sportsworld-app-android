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
import mx.com.sportsworld.sw.loader.Club;

import java.util.List;

// TODO: Auto-generated Javadoc

/**
 * The Class ClubAdapter.
 */
public class ClubAdapter extends ArrayAdapter<Club> {
	
	/** The inflater. */
	LayoutInflater inflater;

	/**
	 * Instantiates a new club adapter.
	 *
	 * @param context the context
	 * @param textViewResourceId the text view resource id
	 * @param objects the objects
	 */
	public ClubAdapter(Context context, int textViewResourceId,
			List<Club> objects) {
		super(context, textViewResourceId, objects);
		inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = null;
		if (convertView == null) {

			v = inflater.inflate(R.layout.club_list_element, null, false);
		} else {
			v = convertView;
		}

		Club currentclub = getItem(position);
		((TextView) v.findViewById(R.id.nombre)).setText(currentclub.nombre);
		((TextView) v.findViewById(R.id.direccion))
				.setText(currentclub.direccion);
		if (currentclub.distancia != null) {
			float dist = Float.parseFloat(currentclub.distancia) / 1000;
			((TextView) v.findViewById(R.id.distancia)).setText(String.format(
					"%.2f Km", dist));
		}

		return v;
	}
}
