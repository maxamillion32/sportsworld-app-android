package mx.com.sportsworld.sw.adapter;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

// TODO: Auto-generated Javadoc

/**
 * The Class ComplexGymClassesAdapter.
 */
public class ComplexGymClassesAdapter extends BaseExpandableListAdapter
		implements View.OnClickListener {

	/** The Constant GROUP_TYPE_COUNT. */
	private static final int GROUP_TYPE_COUNT = 3;
	
	/** The Constant GROUP_TYPE_BRANCHES. */
	private static final int GROUP_TYPE_BRANCHES = 0;
	
	/** The Constant GROUP_TYPE_CALENDAR. */
	private static final int GROUP_TYPE_CALENDAR = 1;
	
	/** The Constant GROUP_TYPE_SCHEDULE. */
	private static final int GROUP_TYPE_SCHEDULE = 2;
	
	/** The Constant CHILD_TYPE_COUNT. */
	private static final int CHILD_TYPE_COUNT = 3;
	
	/** The Constant CHILD_TYPE_BRANCHES. */
	private static final int CHILD_TYPE_BRANCHES = 0;
	
	/** The Constant CHILD_TYPE_CALENDAR. */
	private static final int CHILD_TYPE_CALENDAR = 1;
	
	/** The Constant CHILD_TYPE_SCHEDULE. */
	private static final int CHILD_TYPE_SCHEDULE = 2;
	
	/** The m inflater. */
	private final LayoutInflater mInflater;
	
	/** The m show favorite branches. */
	private final boolean mShowFavoriteBranches;
	
	/** The m on book class click listener. */
	private OnBookClassClickListener mOnBookClassClickListener;
	
	/** The m gym classes row id column. */
	private int mGymClassesRowIdColumn;
	
	/** The m classes cursor. */
	private Cursor mClassesCursor;
	
	/** The m gym classes data valid. */
	private boolean mGymClassesDataValid;
	
	/** The m context. */
	private Context mContext;

	/**
	 * Instantiates a new complex gym classes adapter.
	 *
	 * @param context the context
	 * @param showFavoriteBranches the show favorite branches
	 */
	public ComplexGymClassesAdapter(Context context,
			boolean showFavoriteBranches) {
		mInflater = LayoutInflater.from(context);
		mShowFavoriteBranches = showFavoriteBranches;
		mContext = context.getApplicationContext();
	}

	/* (non-Javadoc)
	 * @see android.widget.BaseExpandableListAdapter#getGroupTypeCount()
	 */
	@Override
	public int getGroupTypeCount() {
		return mShowFavoriteBranches ? GROUP_TYPE_COUNT
				: (GROUP_TYPE_COUNT - 1);
	}

	/* (non-Javadoc)
	 * @see android.widget.BaseExpandableListAdapter#getGroupType(int)
	 */
	@Override
	public int getGroupType(int groupPosition) {

		if (mShowFavoriteBranches) {
			switch (groupPosition) {
			case 0:
				return GROUP_TYPE_BRANCHES;
			case 1:
				return GROUP_TYPE_CALENDAR;
			default:
				return GROUP_TYPE_SCHEDULE;
			}
		}

		switch (groupPosition) {
		case 0:
			return GROUP_TYPE_CALENDAR;
		default:
			return GROUP_TYPE_SCHEDULE;
		}

	}

	/* (non-Javadoc)
	 * @see android.widget.BaseExpandableListAdapter#getChildTypeCount()
	 */
	@Override
	public int getChildTypeCount() {
		return mShowFavoriteBranches ? CHILD_TYPE_COUNT
				: (CHILD_TYPE_COUNT - 1);
	}

	/* (non-Javadoc)
	 * @see android.widget.BaseExpandableListAdapter#getChildType(int, int)
	 */
	@Override
	public int getChildType(int groupPosition, int childPosition) {

		if (mShowFavoriteBranches) {
			switch (groupPosition) {
			case 0:
				return CHILD_TYPE_BRANCHES;
			case 1:
				return CHILD_TYPE_CALENDAR;
			default:
				return CHILD_TYPE_SCHEDULE;
			}
		}

		switch (groupPosition) {
		case 0:
			return CHILD_TYPE_CALENDAR;
		default:
			return CHILD_TYPE_SCHEDULE;
		}

	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final int childType = getChildType(groupPosition, childPosition);

		if (convertView == null) {

			switch (childType) {
			case CHILD_TYPE_BRANCHES:
				convertView = mInflater.inflate(
						android.R.layout.simple_list_item_1, null /* root */,
						false /* attachToRoot */);
				break;
			case CHILD_TYPE_CALENDAR:
				convertView = mInflater.inflate(R.layout.list_item_calendar,
						null /* root */, false /* attachToRoot */);
				break;
			case CHILD_TYPE_SCHEDULE:
				mClassesCursor.moveToPosition(groupPosition
						- (mShowFavoriteBranches ? 2 : 1));
				convertView = newChildScheduleView(mClassesCursor, isLastChild,
						parent);
				break;
			default:
				throw new IllegalArgumentException("Unknown childType: "
						+ childType);
			}

		}

		switch (childType) {
		case CHILD_TYPE_BRANCHES:
			break;
		case CHILD_TYPE_CALENDAR:
			break;
		case CHILD_TYPE_SCHEDULE:
			bindChildScheduleView(convertView, mClassesCursor, isLastChild);
			break;
		default:
			throw new IllegalArgumentException("Unknown childType: "
					+ childType);
		}

		return convertView;
	}

	/**
	 * New child schedule view.
	 *
	 * @param cursor the cursor
	 * @param isLastChild the is last child
	 * @param parent the parent
	 * @return the view
	 */
	private View newChildScheduleView(Cursor cursor, boolean isLastChild,
			ViewGroup parent) {
		final View convertView = mInflater
				.inflate(R.layout.list_item_book_class, null /* root */, false /* attachToRoot */);
		final ScheduleChildViewHolder viewHolder = new ScheduleChildViewHolder();
		viewHolder.txtBranchName = (TextView) convertView
				.findViewById(R.id.txt_branch_name);
		viewHolder.txtSchedule = (TextView) convertView
				.findViewById(R.id.txt_schedule);
		viewHolder.txtSala = (TextView) convertView.findViewById(R.id.txt_sala);
		viewHolder.btnBook = (Button) convertView.findViewById(R.id.btn_book);
		viewHolder.btnBook.setOnClickListener(this);
		viewHolder.chcNotifyClub = (CheckBox) convertView
				.findViewById(R.id.chc_notify_club);
		convertView.setTag(viewHolder);
		return convertView;
	}

	/**
	 * Bind child schedule view.
	 *
	 * @param convertView the convert view
	 * @param cursor the cursor
	 * @param isLastChild the is last child
	 */
	private void bindChildScheduleView(View convertView, Cursor cursor,
			boolean isLastChild) {
		final ScheduleChildViewHolder viewHolder = (ScheduleChildViewHolder) convertView
				.getTag();
		viewHolder.txtBranchName.setText(null);
		viewHolder.txtSchedule.setText(null);
		viewHolder.txtSala.setText(null);
		viewHolder.btnBook.setTag(-1L);
		viewHolder.chcNotifyClub.setChecked(false);
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChild(int, int)
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChildId(int, int)
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		if (groupPosition > ((mShowFavoriteBranches ? 1 : 0))) {
			if (mGymClassesDataValid) {
				final int oldPosition = mClassesCursor.getPosition();
				mClassesCursor.moveToPosition(groupPosition
						- (mShowFavoriteBranches ? 2 : 1));
				final long id = mClassesCursor.getLong(mGymClassesRowIdColumn);
				mClassesCursor.moveToPosition(oldPosition);
				return id;
			}
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroupCount()
	 */
	@Override
	public int getGroupCount() {
		return mShowFavoriteBranches ? GROUP_TYPE_COUNT
				: (GROUP_TYPE_COUNT - 1);
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroup(int)
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroupId(int)
	 */
	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		final int groupType = getGroupType(groupPosition);

		if (convertView == null) {

			switch (groupType) {
			case GROUP_TYPE_BRANCHES:
				convertView = mInflater.inflate(
						android.R.layout.simple_list_item_1, null /* root */,
						false /* attachToRoot */);
				break;
			case GROUP_TYPE_CALENDAR:
				convertView = mInflater.inflate(
						R.layout.list_item_calendar_handler, null /* root */,
						false /* attachToRoot */);
				break;
			case GROUP_TYPE_SCHEDULE:
				mClassesCursor.moveToPosition(groupPosition
						- (mShowFavoriteBranches ? 2 : 1));
				convertView = newGroupScheduleView(mClassesCursor, isExpanded,
						parent);
				break;
			default:
				throw new IllegalArgumentException("Unknown childType: "
						+ groupType);
			}

		}

		switch (groupType) {
		case GROUP_TYPE_BRANCHES:
			break;
		case GROUP_TYPE_CALENDAR:
			break;
		case GROUP_TYPE_SCHEDULE:
			bindGroupScheduleView(convertView, mClassesCursor, isExpanded);
			break;
		default:
			throw new IllegalArgumentException("Unknown childType: "
					+ groupType);
		}

		return convertView;

	}

	/**
	 * New group schedule view.
	 *
	 * @param cursor the cursor
	 * @param isExpanded the is expanded
	 * @param parent the parent
	 * @return the view
	 */
	private View newGroupScheduleView(Cursor cursor, boolean isExpanded,
			ViewGroup parent) {
		final View convertView = mInflater.inflate(R.layout.list_item_class,
				null /* root */, false /* attachToRoot */);
		final ScheduleGroupViewHolder viewHolder = new ScheduleGroupViewHolder();
		viewHolder.txtClassName = (TextView) convertView
				.findViewById(R.id.txt_class_name);
		viewHolder.txtCoach = (TextView) convertView
				.findViewById(R.id.txt_coach_name);
		viewHolder.txtStartsAt = (TextView) convertView
				.findViewById(R.id.txt_starts_at);
		/*
		viewHolder.sprInHighDemand = convertView
				.findViewById(R.id.spr_in_high_demand);
				*/
		convertView.setTag(viewHolder);
		return convertView;
	}

	/**
	 * Bind group schedule view.
	 *
	 * @param convertView the convert view
	 * @param cursor the cursor
	 * @param isExpanded the is expanded
	 */
	private void bindGroupScheduleView(View convertView, Cursor cursor,
			boolean isExpanded) {

		final String className = cursor.getString(cursor
				.getColumnIndex(SportsWorldContract.GymClass.NAME));
		final String coachName = cursor.getString(cursor
				.getColumnIndex(SportsWorldContract.GymClass.COACH_NAME));
		final String startsAt = DateUtils
				.formatDateTime(
						mContext,
						cursor.getLong(cursor
								.getColumnIndex(SportsWorldContract.GymClass.STARTS_AT)),
						DateUtils.FORMAT_SHOW_TIME /* flags */);
		final boolean inHighDemand = (cursor.getInt(cursor
				.getColumnIndex(SportsWorldContract.GymClass.IN_HIGH_DEMAND)) == 1);

		final ScheduleGroupViewHolder viewHolder = (ScheduleGroupViewHolder) convertView
				.getTag();
		viewHolder.txtClassName.setText(className);
		viewHolder.txtCoach.setText(coachName);
		viewHolder.txtStartsAt.setText(startsAt);
		/*
		viewHolder.sprInHighDemand.setVisibility(inHighDemand ? View.VISIBLE
				: View.GONE);
*/
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
		return false;
	}

	/**
	 * Swap gym classes cursor.
	 *
	 * @param newGymClassesCursor the new gym classes cursor
	 * @return the cursor
	 */
	public Cursor swapGymClassesCursor(Cursor newGymClassesCursor) {
		if (newGymClassesCursor == mClassesCursor) {
			return null;
		}
		Cursor oldCursor = mClassesCursor;
		mClassesCursor = newGymClassesCursor;
		if (newGymClassesCursor != null) {
			mGymClassesRowIdColumn = newGymClassesCursor
					.getColumnIndexOrThrow("_id");
			mGymClassesDataValid = true;
			// notify the observers about the new cursor
			notifyDataSetChanged();
		} else {
			mGymClassesRowIdColumn = -1;
			mGymClassesDataValid = false;
			// notify the observers about the lack of a data set
			notifyDataSetInvalidated();
		}
		return oldCursor;
	}

	/**
	 * The Class ScheduleGroupViewHolder.
	 */
	private static class ScheduleGroupViewHolder {
		
		/** The spr in high demand. */
		//View sprInHighDemand;
		
		/** The txt starts at. */
		TextView txtStartsAt;
		
		/** The txt class name. */
		TextView txtClassName;
		
		/** The txt coach. */
		TextView txtCoach;
	}

	/**
	 * The Class ScheduleChildViewHolder.
	 */
	private static class ScheduleChildViewHolder {
		
		/** The btn book. */
		Button btnBook;
		
		/** The chc notify club. */
		CheckBox chcNotifyClub;
		
		/** The txt branch name. */
		TextView txtBranchName;
		
		/** The txt schedule. */
		TextView txtSchedule;
		
		/** The txt sala. */
		TextView txtSala;
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_book:
			final long classId = (Long) view.getTag();
			if (mOnBookClassClickListener != null) {
				mOnBookClassClickListener.onBookClassClick(classId);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown view: " + view);
		}

	}

	/**
	 * Sets the on boock click listener.
	 *
	 * @param listener the new on boock click listener
	 */
	public void setOnBoockClickListener(OnBookClassClickListener listener) {
		mOnBookClassClickListener = listener;
	}

	/**
	 * The listener interface for receiving onBookClassClick events.
	 * The class that is interested in processing a onBookClassClick
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnBookClassClickListener<code> method. When
	 * the onBookClassClick event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnBookClassClickEvent
	 */
	public static interface OnBookClassClickListener {
		
		/**
		 * On book class click.
		 *
		 * @param classId the class id
		 */
		void onBookClassClick(long classId);
	}

}
