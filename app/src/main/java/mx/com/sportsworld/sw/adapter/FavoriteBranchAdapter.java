package mx.com.sportsworld.sw.adapter;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.events.FavoriteBranchInterface;
import mx.com.sportsworld.sw.model.Branch;

// TODO: Auto-generated Javadoc

/**
 * The Class AwardAdapter.
 */
public class FavoriteBranchAdapter extends ArrayAdapter<Branch> {

	private FavoriteBranchInterface mInterface;

	public FavoriteBranchAdapter(Context context,
			FavoriteBranchInterface fbInterface) {
		super(context, 0);
		// TODO Auto-generated constructor stub
		mInterface = fbInterface;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {

			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.list_item_branch_edit, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.txt_branch_name = (TextView) convertView
					.findViewById(R.id.txt_branch_name);
			viewHolder.btn_remove_branch = (Button) convertView
					.findViewById(R.id.btn_remove_branch);
			convertView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.txt_branch_name.setText(getItem(position).getName());
		holder.btn_remove_branch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Log.d("id position ", ""+getItem(position).getUnId());
				mInterface.removeItem(getItem(position).getUnId());
			}
		});

		return convertView;
	}

	/**
	 * The Class ViewHolder.
	 */
	class ViewHolder {

		/** The article. */
		public TextView txt_branch_name;

		/** The points. */
		public Button btn_remove_branch;
	}

}
