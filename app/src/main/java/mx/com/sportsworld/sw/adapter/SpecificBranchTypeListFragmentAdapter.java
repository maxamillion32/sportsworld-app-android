package mx.com.sportsworld.sw.adapter;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.events.FavoriteBranchInterface;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SpecificBranchTypeListFragmentAdapter extends SimpleCursorAdapter {

	private Cursor localCursor;
    private Context localContext;
    private FavoriteBranchInterface mInterface;

	
	public SpecificBranchTypeListFragmentAdapter(Context context, int layout,
			Cursor c, String[] from, int[] to, int flags, FavoriteBranchInterface favInterface) {
		super(context, layout, c, from, to, flags);
		// TODO Auto-generated constructor stub
		this.localCursor = c;
        this.localContext = context;
        this.mInterface = favInterface;
        this.localCursor.moveToFirst();
	}

	@Override
	  public View getView(final int position, View convertView, ViewGroup parent) {
		localCursor.moveToPosition(position);
		ViewHolder viewHolder = new ViewHolder();
       if (convertView == null) {
    	   View convertView1 = LayoutInflater.from(localContext).inflate(R.layout.list_item_branch_edit2, null);
    	   
			viewHolder.txt_branch_name = (TextView) convertView1
					.findViewById(R.id.texto_name);
			viewHolder.txt_branch_name2 = (TextView) convertView1
					.findViewById(R.id.texto_distnce);
			viewHolder.btn_remove_branch = (Button) convertView1
					.findViewById(R.id.btn_remove_branch);
			convertView1.setTag(viewHolder);
			convertView = convertView1;
       }else{
    	   viewHolder = (ViewHolder) convertView.getTag();

       }
     
       
       
       viewHolder.txt_branch_name.setText(localCursor.getString(0));
       viewHolder.txt_branch_name2.setText(localCursor.getString(1));
		
       viewHolder.btn_remove_branch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("position id", "" + getItem(position).toString());
				Log.d("position id", "" + localCursor.getString(2));
				
				mInterface.removeItem(Integer.parseInt(localCursor.getString(2)));
				//getCursor().close();
				//localCursor.moveToNext();
				//localCursor.close();
			}
		});
		//localCursor.close();
		
		return convertView;
	  }
	
	/**
	 * The Class ViewHolder.
	 */
	class ViewHolder {

		/** The article. */
		public TextView txt_branch_name;
		
		public TextView txt_branch_name2;

		/** The points. */
		public Button btn_remove_branch;
	}
}
