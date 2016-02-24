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
import android.widget.Filter;
import android.widget.TextView;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.loader.Clase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// TODO: Auto-generated Javadoc

/**
 * The Class ClaseAdapter.
 */
public class ClaseAdapter extends ArrayAdapter<Clase> {

	/** The inflater. */
	LayoutInflater inflater;

	/** The filter. */
	ClaseFilter filter = new ClaseFilter();

	/*
	 * Gets the filter type
	 */
	@Override
	public Filter getFilter() {
		return filter;
	}

	/**
	 * Instantiates a new clase adapter.
	 * 
	 * @param context
	 *            the context
	 */
	public ClaseAdapter(Context context) {
		super(context, R.layout.clase_list_element);
		inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		filter = new ClaseFilter();
	}

	/**
	 * Instantiates a new clase adapter.
	 * 
	 * @param context
	 *            the context
	 * @param textViewResourceId
	 *            the text view resource id
	 * @param objects
	 *            the objects
	 */
	public ClaseAdapter(Context context, int textViewResourceId,
			List<Clase> objects) {
		super(context, textViewResourceId, objects);
		inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = null;
		if (convertView == null) {

			v = inflater.inflate(R.layout.clase_list_element, null, false);
		} else {
			v = convertView;
		}

		Clase currentClase = getItem(position);
		((TextView) v.findViewById(R.id.fecha)).setText(currentClase.dia
				.substring(0, 3).toUpperCase()
				+ " "
				+ currentClase.getFechaDeClase().get(Calendar.DAY_OF_MONTH));
		((TextView) v.findViewById(R.id.nombre_clase))
				.setText(currentClase.clase);
		((TextView) v.findViewById(R.id.instructor))
				.setText(currentClase.instructor);
		((TextView) v.findViewById(R.id.inicio)).setText(currentClase.inicio
				.substring(0, currentClase.inicio.length() - 3));
		((TextView) v.findViewById(R.id.fin)).setText(currentClase.fin
				.substring(0, currentClase.fin.length() - 3));
		((TextView) v.findViewById(R.id.salon)).setText(currentClase.salon);

		return v;
	}

	/**
	 * The Class ClaseFilter.
	 */
	private class ClaseFilter extends Filter {

		/*
		 * Filter Classes
		 */
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			if (constraint.length() > 0) {

				String query = constraint.toString().toLowerCase();
				FilterResults filterResults = new FilterResults();
				ArrayList<Clase> filteredClases = new ArrayList<Clase>();
				int clasesLen = getCount();

				for (int i = 0; i < clasesLen; i++) {
					Clase currentClase = getItem(i);
					String instructor = currentClase.instructor.toLowerCase();
					String nombreClase = currentClase.clase.toLowerCase();
					String salon = currentClase.salon.toLowerCase();
					if (instructor.contains(query)
							|| nombreClase.contains(query)
							|| salon.contains(query)) {
						filteredClases.add(currentClase);
					}
				}

				filterResults.values = filteredClases;
				filterResults.count = filteredClases.size();
				return filterResults;

			}
			return null;

		}

		/*
		 * Publish resutls
		 */
		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			if (constraint.length() > 0) {
				clear();
				addAll((List<Clase>) results.values);
				notifyDataSetChanged();
			}
		}
	}
}
