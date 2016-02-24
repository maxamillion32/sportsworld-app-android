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
import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
import mx.com.sportsworld.sw.fragment.GymClassesFragment;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

// TODO: Auto-generated Javadoc

/**
 * The Class GymClassesAdapter.
 */
public class GymClassesAdapter extends BaseExpandableListAdapter implements
		View.OnClickListener {

	/** The m cursor. */
	private Cursor mCursor;
	
	/** The m context. */
	private Context mContext;
	
	/** The m inflater. */
	private LayoutInflater mInflater;
	
	/** The m row id column. */
	private int mRowIdColumn;
	
	/** The m row facility programed activity id column. */
	private int mRowFacilityProgramedActivityIdColumn;
	
	/** The m row id club. */
	private int mRowIdClub;
	
	/** The m row id salon. */
	private int mRowIdSalon;
	
	/** The m data valid. */
	private boolean mDataValid;
	
	/** The m begin hour. */
	private int mBeginHour;
	
	/** The m row agendar. */
	private int mRowAgendar;
	
	/** The m on book class click listener. */
	private OnBookClassClickListener mOnBookClassClickListener;

	/**
	 * Instantiates a new gym classes adapter.
	 *
	 * @param cursor the cursor
	 * @param context the context
	 */
	public GymClassesAdapter(Cursor cursor, Context context) {
		mCursor = cursor;
		mContext = context;
		mInflater = LayoutInflater.from(context);
		swapCursor(cursor);
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
		return mDataValid ? 1 : 0;
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
	 * Bind group view.
	 *
	 * @param view the view
	 * @param cursor the cursor
	 * @param isExpanded the is expanded
	 */
	protected void bindGroupView(View view, Cursor cursor, boolean isExpanded) {
		final String className = cursor.getString(cursor
				.getColumnIndex(SportsWorldContract.GymClass.NAME));
		final String coachName = cursor.getString(cursor
				.getColumnIndex(SportsWorldContract.GymClass.COACH_NAME));
		
		@SuppressWarnings("deprecation")
		final String startsAt = DateUtils
				.formatDateTime(
						mContext,
						cursor.getLong(cursor
								.getColumnIndex(SportsWorldContract.GymClass.STARTS_AT)),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_24HOUR/* flags */);
		final boolean inHighDemand = (cursor.getInt(cursor
				.getColumnIndex(SportsWorldContract.GymClass.IN_HIGH_DEMAND)) == 1);

		final ScheduleGroupViewHolder viewHolder = (ScheduleGroupViewHolder) view
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
		final View convertView = mInflater
				.inflate(R.layout.list_item_book_class, null /* root */, false /* attachToRoot */);
		final ScheduleChildViewHolder viewHolder = new ScheduleChildViewHolder();
		viewHolder.txtBranchName = (TextView) convertView
				.findViewById(R.id.txt_branch_name);
		
		viewHolder.txtSchedule = (TextView) convertView
				.findViewById(R.id.txt_schedule);
		viewHolder.txtSalon = (TextView) convertView
				.findViewById(R.id.txt_sala);
		viewHolder.btnBook = (Button) convertView.findViewById(R.id.btn_book);
		viewHolder.btnBook.setOnClickListener(this);
		viewHolder.chcNotifyClub = (CheckBox) convertView
				.findViewById(R.id.chc_notify_club);
		viewHolder.chcNotifyClub.setChecked(true);
		viewHolder.chcNotifyClub.setOnClickListener(this);
		viewHolder.txtReservar = (TextView) convertView.findViewById(R.id.txt_notify_branch);
		convertView.setTag(viewHolder);
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
		final ScheduleChildViewHolder viewHolder = (ScheduleChildViewHolder) view
				.getTag();
		final String branchName = cursor.getString(cursor
				.getColumnIndex(SportsWorldContract.GymClass.CLUB));

		final String salonName = cursor.getString(cursor
				.getColumnIndex(SportsWorldContract.GymClass.SALON));
		@SuppressWarnings("deprecation")
		final String finishAt = DateUtils
				.formatDateTime(
						mContext,
						cursor.getLong(cursor
								.getColumnIndex(SportsWorldContract.GymClass.FINISH_AT)),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_24HOUR/* flags */);
		@SuppressWarnings("deprecation")
		final String startsAt = DateUtils
				.formatDateTime(
						mContext,
						cursor.getLong(cursor
								.getColumnIndex(SportsWorldContract.GymClass.STARTS_AT)),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_24HOUR /* flags */);

		final int agendarClases = cursor.getInt(cursor
				.getColumnIndex(SportsWorldContract.GymClass.AGENDAR_CLASES));

		viewHolder.txtBranchName.setText("Club: " + branchName);
		//Log.d("Club", "club "+branchName);
		
		viewHolder.txtSchedule.setText(startsAt + " a " + finishAt + " hrs");
		viewHolder.btnBook.setTag(cursor.getPosition());
		
		if(agendarClases == 1){
			final SportsWorldAccountManager accountMngr = new SportsWorldAccountManager(
					this.mContext /* context */);
			if(accountMngr.isLoggedInAsMember()){
				viewHolder.chcNotifyClub.setChecked(true);
				viewHolder.chcNotifyClub.setVisibility(View.INVISIBLE);
			}else{
				viewHolder.chcNotifyClub.setChecked(true);
				viewHolder.txtReservar.setText(null);
				viewHolder.chcNotifyClub.setVisibility(View.INVISIBLE);
			}
		}else{
			viewHolder.chcNotifyClub.setChecked(true);
			viewHolder.txtReservar.setText(null);
			viewHolder.chcNotifyClub.setVisibility(View.INVISIBLE);
		}
		
		viewHolder.txtSalon.setText("Sal�n: " + salonName);
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
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
		
		/** The txt salon. */
		TextView txtSalon;
		
		TextView txtReservar;
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
					.getColumnIndexOrThrow(SportsWorldContract.GymClass._ID);
			mRowFacilityProgramedActivityIdColumn = newCursor
					.getColumnIndexOrThrow(SportsWorldContract.GymClass.FACILITY_PROGRAMED_ACTIVITY_ID);
			mBeginHour = newCursor
					.getColumnIndexOrThrow(SportsWorldContract.GymClass.STARTS_AT);
			mRowIdClub = newCursor
					.getColumnIndexOrThrow(SportsWorldContract.GymClass.CLUB);
			mRowIdSalon = newCursor
					.getColumnIndexOrThrow(SportsWorldContract.GymClass.SALON);
			mRowAgendar = newCursor.getColumnIndex(SportsWorldContract.GymClass.AGENDAR_CLASES);
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
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_book:
			final int oldPosition = mCursor.getPosition();
			final int newPosition = (Integer) view.getTag();

			if (mCursor.moveToPosition(newPosition)) {
				final long facilityProgramedActivityId = mCursor
						.getLong(mRowFacilityProgramedActivityIdColumn);
				final String hourBegins = mCursor.getString(mBeginHour);
				final long classId = mCursor.getLong(mRowIdColumn);

				GymClassesFragment.clubName = mCursor.getString(mRowIdClub);
				GymClassesFragment.salonName = mCursor.getString(mRowIdSalon);
				if(mCursor.getString(mRowAgendar).equals("1")){
					
				}else{
					SportsWorldPreferences.setChckBoxAdvice(mContext, false);
				}
				if (mOnBookClassClickListener != null) {
					mOnBookClassClickListener.onBookClassClick(
							
							facilityProgramedActivityId, classId, newPosition,
							hourBegins);
				}
			}
			mCursor.moveToPosition(oldPosition);
			break;
		case R.id.chc_notify_club:
			if (((CheckBox) view).isChecked())
				SportsWorldPreferences.setChckBoxAdvice(mContext, true);
			else
				SportsWorldPreferences.setChckBoxAdvice(mContext, false);
			break;
		default:
			throw new IllegalArgumentException("Unknown view: " + view);
			
			
		}
		//Log.d("Club ", "chk "+mCursor.getString(mRowIdClub));
		if(mCursor.getString(mRowAgendar).equals("1")){
			
		}else{
			SportsWorldPreferences.setChckBoxAdvice(mContext, false);
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
		 * @param facilityProgramedActivityId the facility programed activity id
		 * @param gymClassId the gym class id
		 * @param position the position
		 * @param mBeginHour the m begin hour
		 */
		void onBookClassClick(long facilityProgramedActivityId,
							  long gymClassId, int position, String mBeginHour);
	}

}
