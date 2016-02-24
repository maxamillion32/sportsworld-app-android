package mx.com.sportsworld.sw.adapter;

import java.util.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.pojo.HistoryPojo;
import mx.com.sportsworld.sw.utils.GeneralUtils;

// TODO: Auto-generated Javadoc

/**
 * The Class TransactionAdapter.
 */
public class TransactionAdapter extends ArrayAdapter<HistoryPojo> {

	/** The par. */
	boolean par = false;
	
	/** The background. */
	int background;

	/**
	 * Instantiates a new transaction adapter.
	 *
	 * @param context the context
	 */
	public TransactionAdapter(Context context) {
		super(context, 0);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (position % 2 == 0) {
			background = R.drawable.celda_even_transaction;
		} else
			background = R.drawable.celda_odd_transaction;

		if (convertView == null) {

			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.list_item_transaction, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.accion = (TextView) convertView
					.findViewById(R.id.txtViewAccion);
			viewHolder.date = (TextView) convertView
					.findViewById(R.id.txtViewDate);
			viewHolder.points = (TextView) convertView
					.findViewById(R.id.txtViewPoints);
			convertView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.date.setBackgroundResource(background);
		holder.accion.setBackgroundResource(background);
		holder.points.setBackgroundResource(background);

		holder.accion.setText(getItem(position).getNombreEvento());
		holder.date.setText(getStringDate(getItem(position).getFechaEvento()));
		if (getItem(position).getPuntos().equals("---"))
			holder.points.setText(getItem(position).getPuntos());
		else
			holder.points.setText(GeneralUtils.thousandFormat(Integer
					.parseInt(getItem(position).getPuntos())));

		return convertView;
	}

	/**
	 * Gets the string date.
	 *
	 * @param cal the cal
	 * @return the string date
	 */
	public String getStringDate(Calendar cal) {
		String strDate = "";
		if (cal.get(Calendar.YEAR) == 1) {
			strDate = "---";
		} else {
			strDate += setCorrectDate(cal.get(Calendar.DAY_OF_MONTH)) + "-";
			strDate += setCorrectDate(cal.get(Calendar.MONTH) + 1) + "-";
			strDate += cal.get(Calendar.YEAR);
		}
		return strDate;
	}

	/**
	 * The Class ViewHolder.
	 */
	class ViewHolder {
		
		/** The accion. */
		public TextView accion;
		
		/** The date. */
		public TextView date;
		
		/** The points. */
		public TextView points;
	}

	/**
	 * Sets the correct date.
	 *
	 * @param num the num
	 * @return the string
	 */
	public String setCorrectDate(int num) {
		String number = "";
		if (num < 10) {
			number = "0" + num;
		} else {
			number = num + "";
		}
		return number;
	}

}
