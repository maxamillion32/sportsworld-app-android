package mx.com.sportsworld.sw.adapter;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

// TODO: Auto-generated Javadoc

/**
 * The Class ExerciseCursorAdapter.
 */
public class ExerciseCursorAdapter extends BaseExpandableListAdapter {

	/** The m cursor. */
	private Cursor mCursor;
	
	/** The m inflater. */
	private LayoutInflater mInflater;
	
	/** The m row id column. */
	private int mRowIdColumn;
	
	/** The m data valid. */
	private boolean mDataValid;

	/**
	 * Instantiates a new exercise cursor adapter.
	 *
	 * @param cursor the cursor
	 * @param context the context
	 */
	public ExerciseCursorAdapter(Cursor cursor, Context context) {
		mCursor = cursor;
		mInflater = LayoutInflater.from(context);
		swapCursor(cursor);
	}

	/**
	 * Swap cursor.
	 *
	 * @param newCursor the new cursor
	 * @return the cursor
	 */
	public Cursor swapCursor(Cursor newCursor) {
		if (newCursor == mCursor) {
			return null;
		}
		Cursor oldCursor = mCursor;
		mCursor = newCursor;
		if (newCursor != null) {
			mRowIdColumn = newCursor
					.getColumnIndexOrThrow(SportsWorldContract.Exercise._ID);
			mDataValid = true;
			// notify the observers about the new cursor
			notifyDataSetChanged();
		} else {
			mRowIdColumn = -1;
			mDataValid = false;
			// notify the observers about the lack of a data set
			notifyDataSetInvalidated();
		}
		return oldCursor;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroupCount()
	 */
	@Override
	public int getGroupCount() {
		return mDataValid ? mCursor.getCount() : 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		return 0;// mDataValid ? 1 : 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroup(int)
	 */
	@Override
	public Cursor getGroup(int groupPosition) {
		if (mDataValid && (getGroupCount() > 0)) {
			mCursor.moveToPosition(groupPosition);
			return mCursor;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChild(int, int)
	 */
	@Override
	public Cursor getChild(int groupPosition, int childPosition) {
		return getGroup(groupPosition);
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroupId(int)
	 */
	@Override
	public long getGroupId(int groupPosition) {
		long id = 0;
		if (mDataValid && (getGroupCount() > 0)) {
			final int oldPosition = mCursor.getPosition();
			mCursor.moveToPosition(groupPosition);
			id = mCursor.getLong(mRowIdColumn);
			mCursor.moveToPosition(oldPosition);
		}
		return id;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChildId(int, int)
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return getGroupId(groupPosition);
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#hasStableIds()
	 */
	@Override
	public boolean hasStableIds() {
		return true;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		mCursor.moveToPosition(groupPosition);
		if (convertView == null) {
			convertView = newGroupView(mCursor, isExpanded, parent);
		}
		bindGroupView(convertView, mCursor, isExpanded);
		return convertView;
	}

	/**
	 * New group view.
	 *
	 * @param cursor the cursor
	 * @param isExpanded the is expanded
	 * @param parent the parent
	 * @return the view
	 */
	protected View newGroupView(Cursor cursor, boolean isExpanded,
			ViewGroup parent) {
		final View convertView = mInflater.inflate(R.layout.list_item_exercise,
				null /* root */, false /* attachToRoot */);
		final ViewHolder holder = new ViewHolder();
		holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
		holder.chcDone = (CheckBox) convertView.findViewById(R.id.chc_done);
		convertView.setTag(holder);
		return convertView;
	}

	/**
	 * Bind group view.
	 *
	 * @param view the view
	 * @param cursor the cursor
	 * @param isExpanded the is expanded
	 */
	protected void bindGroupView(View view, Cursor cursor, boolean isExpanded) {
		final ViewHolder holder = (ViewHolder) view.getTag();
		holder.txtName.setText(cursor.getString(cursor
				.getColumnIndex(SportsWorldContract.Exercise.MUSCLE_WORKED)));
		holder.chcDone.setChecked(false);
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		mCursor.moveToPosition(groupPosition);
		if (convertView == null) {
			convertView = newChildView(mCursor, isLastChild, parent);
		}
		bindChildView(convertView, mCursor, isLastChild);
		return convertView;
	}

	/**
	 * New child view.
	 *
	 * @param cursor the cursor
	 * @param isLastChild the is last child
	 * @param parent the parent
	 * @return the view
	 */
	protected View newChildView(Cursor cursor, boolean isLastChild,
			ViewGroup parent) {
		final View convertView = mInflater.inflate(R.layout.list_item_exercise,
				null /* root */, false /* attachToRoot */);
		final ViewHolder holder = new ViewHolder();
		holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
		holder.chcDone = (CheckBox) convertView.findViewById(R.id.chc_done);
		convertView.setTag(holder);

		return convertView;
	}

	/**
	 * Bind child view.
	 *
	 * @param view the view
	 * @param cursor the cursor
	 * @param isLastChild the is last child
	 */
	protected void bindChildView(View view, Cursor cursor, boolean isLastChild) {
		final ViewHolder holder = (ViewHolder) view.getTag();
		holder.txtName.setText(cursor.getString(cursor
				.getColumnIndex(SportsWorldContract.Exercise.EXERCISE_NAME)));
		holder.chcDone.setChecked((cursor.getInt(cursor
				.getColumnIndex(SportsWorldContract.Exercise.DONE)) == 1));
	}

	/**
	 * The Class ViewHolder.
	 */
	private static class ViewHolder {
		/* package *//** The txt name. */
		TextView txtName;
		/* package *//** The chc done. */
		CheckBox chcDone;
	}

}
