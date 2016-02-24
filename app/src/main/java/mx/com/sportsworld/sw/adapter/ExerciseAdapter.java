package mx.com.sportsworld.sw.adapter;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.NutritionAdviceActivity;
import mx.com.sportsworld.sw.model.Exercise;
import mx.com.sportsworld.sw.model.ExerciseGroup;

// TODO: Auto-generated Javadoc

/**
 * The Class ExerciseAdapter.
 */
public class ExerciseAdapter extends BaseExpandableListAdapter {

	/** The m exercise groups. */
	private static List<ExerciseGroup> mExerciseGroups;

	/** The m inflater. */
	private LayoutInflater mInflater;

	/** The m circuit routine format. */
	private final String mCircuitRoutineFormat;

	/** The m listener. */
	private OnExerciseValueChangedListener mListener;

	/** The show checkboxes. */
	public static boolean showCheckboxes = true;

	/** The count series. */
	public int countSeries = 0;

	/** The group. */
	public static int group = 0;

	/** The child. */
	public static int child = 0;

	/** The layout params. */
	LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);

	/** The m group checkboxlistener. */
	private final View.OnClickListener mGroupCheckboxlistener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			if (mListener != null) {
				final CheckBox chk = (CheckBox) view;
				final ExerciseGroup exerciseGroup = (ExerciseGroup) chk
						.getTag();
				mListener.onExerciseGroupChangedListener(exerciseGroup,
						chk.isChecked());
			}
		}
	};

	/** The m group click listener. */
	@SuppressWarnings("unused")
	private final View.OnClickListener mGroupClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			if (mListener != null) {
				final CheckBox chk = (CheckBox) view;
				chk.setVisibility(View.GONE);
			}
		}
	};

	/** The m layout click listener. */
	@SuppressWarnings("unused")
	private final View.OnClickListener mLayoutClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			if (mListener != null) {
				final LinearLayout layout = (LinearLayout) view;
				CheckBox chk = (CheckBox) layout.getChildAt(1);
				chk.setVisibility(View.GONE);
			}
		}
	};

	/** The m child nutrition listener. */
	@SuppressWarnings("unused")
	private final View.OnClickListener mChildNutritionListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {

			final Intent showNutritionAdvice = new Intent(
					view.getContext() /* context */,
					NutritionAdviceActivity.class);
			view.getContext().startActivity(showNutritionAdvice);

		}
	};

	/** The m child checkboxlistener. */
	private final View.OnClickListener mChildCheckboxlistener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			if (mListener != null) {
				final CheckBox chk = (CheckBox) view;
				final Exercise exercise = (Exercise) chk.getTag();
				mListener.onExerciseChangedListener(exercise, chk.isChecked());
			}
		}
	};

	/**
	 * Instantiates a new exercise adapter.
	 * 
	 * @param context
	 *            the context
	 */
	public ExerciseAdapter(Context context) {
		mCircuitRoutineFormat = context.getResources().getString(
				R.string.is_circuit);
		mInflater = LayoutInflater.from(context);
	}

	/**
	 * Sets the exercise groups.
	 * 
	 * @param exerciseGroups
	 *            the new exercise groups
	 */
	public void setExerciseGroups(List<ExerciseGroup> exerciseGroups) {
		mExerciseGroups = exerciseGroups;
		if (exerciseGroups == null) {
			notifyDataSetInvalidated();
		} else {
			notifyDataSetChanged();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupCount()
	 */
	@Override
	public int getGroupCount() {
		return (mExerciseGroups == null) ? 0 : mExerciseGroups.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
	 */
	@Override
	public int getChildrenCount(int groupPosition) {

		if (mExerciseGroups == null) {
			return 0;
		}
		if (mExerciseGroups.size() < groupPosition) {
			groupPosition = mExerciseGroups.size() - 1;
		}

		final List<Exercise> exercises = mExerciseGroups.get(groupPosition)
				.getExercises();

		return (exercises == null) ? 0 : exercises.size();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroup(int)
	 */
	@Override
	public ExerciseGroup getGroup(int groupPosition) {
		return mExerciseGroups.get(groupPosition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChild(int, int)
	 */
	@Override
	public Exercise getChild(int groupPosition, int childPosition) {
		if (mExerciseGroups == null) {
			return null;
		}

		final List<Exercise> exercises = mExerciseGroups.get(groupPosition)
				.getExercises();
		return (exercises == null) ? null : exercises.get(childPosition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupId(int)
	 */
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildId(int, int)
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {

		if (mExerciseGroups == null) {
			return -1;
		}

		final List<Exercise> exercises = mExerciseGroups.get(groupPosition)
				.getExercises();
		if (exercises.get(0).getName().equals(""))
			return -1;
		else
			return (exercises == null) ? -1 : exercises.get(childPosition)
					.getId();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#hasStableIds()
	 */
	@Override
	public boolean hasStableIds() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean,
	 * android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		final ExerciseGroup exerciseGroup = getGroup(groupPosition);
		if (groupPosition == 0)
			isExpanded = true;
		if (convertView == null) {
			convertView = newGroupView(exerciseGroup, isExpanded, parent);
		}

		bindGroupView(convertView, exerciseGroup, isExpanded);
		return convertView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean,
	 * android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final Exercise exercise = getChild(groupPosition, childPosition);

		if (convertView == null) {
			convertView = newChildView(exercise, isLastChild, parent);
		}
		if (groupPosition == group && childPosition == child) {
			convertView.setBackgroundColor(convertView.getContext()
					.getResources().getColor(R.color.transparent_black));
		} else {
			convertView.setBackgroundColor(convertView.getContext()
					.getResources().getColor(R.color.transparente));
		}
		bindChildView(convertView, exercise, isLastChild);
		return convertView;
	}

	/**
	 * New group view.
	 * 
	 * @param cursor
	 *            the cursor
	 * @param isExpanded
	 *            the is expanded
	 * @param parent
	 *            the parent
	 * @return the view
	 */
	protected View newGroupView(ExerciseGroup cursor, boolean isExpanded,
			ViewGroup parent) {
		final View convertView = mInflater.inflate(R.layout.list_item_exercise,
				null /* root */, false /* attachToRoot */);

		// kokusho Se crean las listas para las rutinas
		final ViewHolder holder = new ViewHolder();
		holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
		holder.txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		holder.txtName.setTypeface(null, Typeface.BOLD);
		holder.txtName.setTextColor(Color.WHITE);
		
		holder.chcDone = (CheckBox) convertView.findViewById(R.id.chc_done);

		holder.chcDone.setOnClickListener(mGroupCheckboxlistener);
		if (!showCheckboxes)
			holder.chcDone.setVisibility(View.GONE);
		else
			holder.chcDone.setVisibility(View.VISIBLE);
		convertView.setTag(holder);
		return convertView;
	}

	/**
	 * Bind group view.
	 * 
	 * @param view
	 *            the view
	 * @param exerciseGroup
	 *            the exercise group
	 * @param isExpanded
	 *            the is expanded
	 */
	protected void bindGroupView(View view, ExerciseGroup exerciseGroup,
			boolean isExpanded) {

		final String name;
		if (exerciseGroup.isCircuit()) {
			name = String
					.format(mCircuitRoutineFormat, exerciseGroup.getName());
		} else {
			name = exerciseGroup.getName();
		}

		final ViewHolder holder = (ViewHolder) view.getTag();
		holder.txtName.setText(name);
		holder.chcDone.setChecked(exerciseGroup.isDone());
		holder.chcDone.setTag(exerciseGroup);
		if (!showCheckboxes)
			holder.chcDone.setVisibility(View.GONE);
		else
			holder.chcDone.setVisibility(View.VISIBLE);

		if (exerciseGroup.getName().equals("Recomendaciones")) {
			holder.chcDone.setVisibility(View.GONE);

		} else
			holder.chcDone.setVisibility(View.VISIBLE);
	}

	/**
	 * New child view.
	 * 
	 * @param exercise
	 *            the exercise
	 * @param isLastChild
	 *            the is last child
	 * @param parent
	 *            the parent
	 * @return the view
	 */
	protected View newChildView(Exercise exercise, boolean isLastChild,
			ViewGroup parent) {
		final View convertView = mInflater.inflate(R.layout.list_item_exercise,
				null /* root */, false /* attachToRoot */);

		final ViewHolder holder = new ViewHolder();
		holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
		holder.txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
		holder.chcDone = (CheckBox) convertView.findViewById(R.id.chc_done);
		holder.chcDone.setOnClickListener(mChildCheckboxlistener);

		if (exercise.getName().equals("Nutrici�n"))
			holder.chcDone.setVisibility(View.GONE);
		else
			holder.chcDone.setVisibility(View.VISIBLE);

		convertView.setTag(holder);

		return convertView;
	}

	/**
	 * Bind child view.
	 *
	 * @param view
	 *            the view
	 * @param exercise
	 *            the exercise
	 * @param isLastChild
	 *            the is last child
	 */
	protected void bindChildView(View view, Exercise exercise,
			boolean isLastChild) {
		final ViewHolder holder = (ViewHolder) view.getTag();
		holder.txtName.setText(exercise.getName());
		holder.txtName.setTextColor(Color.WHITE);
		holder.chcDone.setChecked(exercise.isDone());
		holder.chcDone.setTag(exercise);
		if (exercise.getName().equals("Nutrici�n"))
			holder.chcDone.setVisibility(View.GONE);
		else
			holder.chcDone.setVisibility(View.VISIBLE);

	}

	/**
	 * Sets the on exercise value changed listener.
	 * 
	 * @param listener
	 *            the new on exercise value changed listener
	 */
	public void setOnExerciseValueChangedListener(
			OnExerciseValueChangedListener listener) {
		mListener = listener;
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

	/**
	 * The listener interface for receiving onExerciseValueChanged events. The
	 * class that is interested in processing a onExerciseValueChanged event
	 * implements this interface, and the object created with that class is
	 * registered with a component using the component's
	 * <code>addOnExerciseValueChangedListener<code> method. When
	 * the onExerciseValueChanged event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see OnExerciseValueChangedEvent
	 */
	public static interface OnExerciseValueChangedListener {

		/**
		 * On exercise group changed listener.
		 * 
		 * @param exerciseGroup
		 *            the exercise group
		 * @param checked
		 *            the checked
		 */
		public void onExerciseGroupChangedListener(ExerciseGroup exerciseGroup,
												   boolean checked);

		/**
		 * On exercise changed listener.
		 * 
		 * @param exercise
		 *            the exercise
		 * @param checked
		 *            the checked
		 */
		public void onExerciseChangedListener(Exercise exercise, boolean checked);
	}

}
