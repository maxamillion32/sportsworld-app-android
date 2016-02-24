package mx.com.sportsworld.sw.app;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.NutritionAdviceActivity;
import mx.com.sportsworld.sw.fragment.GymClassesFragment;

public class SherlockWorkingExpandableListFragment extends SherlockFragment {

	static final int INTERNAL_EMPTY_ID = R.id.internal_empty;
	static final int INTERNAL_PROGRESS_CONTAINER_ID = R.id.internal_progress_container;
	static final int INTERNAL_LIST_CONTAINER_ID = R.id.internal_list_container;
	static final int INTERNAL_EXPANDABLE_LIST_ID = R.id.internal_expandable_list;

	private boolean mExclusiveExpand;
	final private Handler mHandler = new Handler();

	final private Runnable mRequestFocus = new Runnable() {
		public void run() {
			mList.focusableViewAvailable(mList);
		}
	};

	final private OnChildClickListener mOnChildClickListener = new OnChildClickListener() {
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			onListChildClick(parent, v, groupPosition, childPosition, id);
			return false;
		}
	};

	final private OnGroupClickListener mOnGroupClickListener = new OnGroupClickListener() {

		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
			onListGroupClick(parent, v, groupPosition, id);

			addNamesValues(v);
			return false;
		}

	};

	final private OnGroupExpandListener mOnGroupExpandListener = new OnGroupExpandListener() {
		@Override
		public void onGroupExpand(int groupPosition) {
			if (mExclusiveExpand && mAdapter != null) {
				final int count = mAdapter.getGroupCount();
				for (int i = 0; i < count; i++) {
					if (i != groupPosition) {
						mList.collapseGroup(i);
					}
				}
			}
			onListGroupExpandListener(groupPosition);

		}
	};

	final private OnGroupCollapseListener mOnGroupCollapseListener = new OnGroupCollapseListener() {

		@Override
		public void onGroupCollapse(int groupPosition) {
			onListGroupCollapseListener(groupPosition);
		}

	};

	ExpandableListAdapter mAdapter;
	ExpandableListView mList;
	View mEmptyView;
	TextView mStandardEmptyView;
	View mProgressContainer;
	View mListContainer;
	CharSequence mEmptyText;
	boolean mListShown;

	public SherlockWorkingExpandableListFragment() {
	}

	/**
	 * Provide default implementation to return a simple list view. Subclasses
	 * can override to replace with their own layout. If doing so, the returned
	 * view hierarchy <em>must</em> have a ListView whose id is
	 * {@link android.R.id#list android.R.id.list} and can optionally have a
	 * sibling view id {@link android.R.id#empty android.R.id.empty} that is to
	 * be shown when the list is empty.
	 * 
	 * <p>
	 * If you are overriding this method with your own custom content, consider
	 * including the standard layout {@link android.R.layout#list_content} in
	 * your layout file, so that you continue to retain all of the standard
	 * behavior of ListFragment. In particular, this is currently the only way
	 * to have the built-in indeterminant progress state be shown.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final Context context = getActivity();

		FrameLayout root = new FrameLayout(context);

		// ------------------------------------------------------------------

		LinearLayout pframe = new LinearLayout(context);
		pframe.setId(INTERNAL_PROGRESS_CONTAINER_ID);
		pframe.setOrientation(LinearLayout.VERTICAL);
		pframe.setVisibility(View.GONE);
		pframe.setGravity(Gravity.CENTER);

		ProgressBar progress = new ProgressBar(context, null /* attributeSet */,
				android.R.attr.progressBarStyleLarge);
		pframe.addView(progress, new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));

		root.addView(pframe, new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));

		// ------------------------------------------------------------------

		FrameLayout lframe = new FrameLayout(context);
		lframe.setId(INTERNAL_LIST_CONTAINER_ID);

		TextView tv = new TextView(getActivity());
		tv.setId(INTERNAL_EMPTY_ID);
		tv.setGravity(Gravity.CENTER);

		lframe.addView(tv, new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));

		ExpandableListView lv = new ExpandableListView(getActivity());
		lv.setId(INTERNAL_EXPANDABLE_LIST_ID);
		lv.setDrawSelectorOnTop(false);
		lframe.addView(lv, new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));

		root.addView(lframe, new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));

		// ------------------------------------------------------------------

		root.setLayoutParams(new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));

		return root;
	}

	/**
	 * Attach to list view once the view hierarchy has been created.
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ensureList();
	}

	/**
	 * Detach from list view.
	 */
	@Override
	public void onDestroyView() {
		mHandler.removeCallbacks(mRequestFocus);
		mList = null;
		mListShown = false;
		mEmptyView = mProgressContainer = mListContainer = null;
		mStandardEmptyView = null;
		super.onDestroyView();
	}

	/**
	 * Callback method to be invoked when a child in this expandable list has
	 * been clicked.
	 * 
	 * @param parent
	 *            The ExpandableListView where the click happened
	 * @param v
	 *            The view within the expandable list/ListView that was clicked
	 * @param groupPosition
	 *            The group position that contains the child that was clicked
	 * @param childPosition
	 *            The child position within the group
	 * @param id
	 *            The row id of the child that was clicked
	 * @return True if the click was handled
	 */
	public void onListChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
	}

	/**
	 * Callback method to be invoked when a group in this expandable list has
	 * been clicked.
	 * 
	 * @param parent
	 *            The ExpandableListConnector where the click happened
	 * @param v
	 *            The view within the expandable list/ListView that was clicked
	 * @param groupPosition
	 *            The group position that was clicked
	 * @param id
	 *            The row id of the group that was clicked
	 * @return True if the click was handled
	 */
	public void onListGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
	}

	/**
	 * Callback method to be invoked when a group in this expandable list has
	 * been clicked.
	 * 
	 * @param parent
	 *            The ExpandableListConnector where the click happened
	 * @param v
	 *            The view within the expandable list/ListView that was clicked
	 * @param groupPosition
	 *            The group position that was clicked
	 * @param id
	 *            The row id of the group that was clicked
	 * @return True if the click was handled
	 */
	public void onListGroupExpandListener(int groupPosition) {
	}

	/**
	 * Callback method to be invoked when a group in this expandable list has
	 * been collapsed.
	 * 
	 * @param groupPosition
	 *            The group position that was collapsed
	 */
	public void onListGroupCollapseListener(int groupPosition) {
	}

	/**
	 * Provide the cursor for the list view.
	 */
	public void setExpandableListAdapter(ExpandableListAdapter adapter,
			boolean showFoot) {
		boolean hadAdapter = mAdapter != null;
		mAdapter = adapter;
		if (mList != null) {
			// FooterView footerView = new FooterView(getActivity());
			if (showFoot) {
				View footerView = LayoutInflater.from(getActivity()).inflate(
						R.layout.footer_detalle_rutina, null);
				View txtNutricion = footerView
						.findViewById(R.id.textoNutricion);
				txtNutricion.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent showNutritionAdvice = new Intent(getActivity(),
								NutritionAdviceActivity.class);
						getActivity().startActivity(showNutritionAdvice);
					}
				});
				mList.addFooterView(footerView);
			}
			mList.setAdapter(adapter);
			if (!mListShown && !hadAdapter) {
				// The list was hidden, and previously didn't have an
				// adapter. It is now time to show it.
				setListShown(true, getView().getWindowToken() != null);
			}
		}
	}

	/**
	 * Set the currently selected list item to the specified position with the
	 * adapter's data
	 * 
	 * @param position
	 */
	public void setSelection(int position) {
		ensureList();
		mList.setSelection(position);
	}

	/**
	 * Get the position of the currently selected list item.
	 */
	public int getSelectedItemPosition() {
		ensureList();
		return mList.getSelectedItemPosition();
	}

	/**
	 * Get the cursor row ID of the currently selected list item.
	 */
	public long getSelectedItemId() {
		ensureList();
		return mList.getSelectedItemId();
	}

	/**
	 * Get the activity's list view widget.
	 */
	public ExpandableListView getExpandableListView() {
		ensureList();
		return mList;
	}

	/**
	 * The default content for a ListFragment has a TextView that can be shown
	 * when the list is empty. If you would like to have it shown, call this
	 * method to supply the text it should use.
	 */
	public void setEmptyText(CharSequence text) {
		ensureList();
		if (mStandardEmptyView == null) {
			throw new IllegalStateException(
					"Can't be used with a custom content view");
		}
		mStandardEmptyView.setText(text);
		if (mEmptyText == null) {
			mList.setEmptyView(mStandardEmptyView);
		}
		mEmptyText = text;
	}

	/**
	 * Control whether the list is being displayed. You can make it not
	 * displayed if you are waiting for the initial data to show in it. During
	 * this time an indeterminant progress indicator will be shown instead.
	 * 
	 * <p>
	 * Applications do not normally need to use this themselves. The default
	 * behavior of ListFragment is to start with the list not being shown, only
	 * showing it once an adapter is given with
	 * {@link #setExpandableListAdapter(ListAdapter)}. If the list at that point
	 * had not been shown, when it does get shown it will be do without the user
	 * ever seeing the hidden state.
	 * 
	 * @param shown
	 *            If true, the list view is shown; if false, the progress
	 *            indicator. The initial value is true.
	 */
	public void setListShown(boolean shown) {
		setListShown(shown, true /* animate */);
	}

	/**
	 * Like {@link #setListShown(boolean)}, but no animation is used when
	 * transitioning from the previous state.
	 */
	public void setListShownNoAnimation(boolean shown) {
		setListShown(shown, false /* animate */);
	}

	/**
	 * Control whether the list is being displayed. You can make it not
	 * displayed if you are waiting for the initial data to show in it. During
	 * this time an indeterminant progress indicator will be shown instead.
	 * 
	 * @param shown
	 *            If true, the list view is shown; if false, the progress
	 *            indicator. The initial value is true.
	 * @param animate
	 *            If true, an animation will be used to transition to the new
	 *            state.
	 */
	private void setListShown(boolean shown, boolean animate) {
		ensureList();

		if (mProgressContainer == null) {
			throw new IllegalStateException(
					"Can't be used with a custom content view");
		}
		if (mListShown == shown) {
			return;
		}
		mListShown = shown;
		if (shown) {
			if (animate) {
				mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_out));
				mListContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_in));
			} else {
				mProgressContainer.clearAnimation();
				mListContainer.clearAnimation();
			}
			mProgressContainer.setVisibility(View.GONE);
			mListContainer.setVisibility(View.VISIBLE);
		} else {
			if (animate) {
				mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_in));
				mListContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_out));
			} else {
				mProgressContainer.clearAnimation();
				mListContainer.clearAnimation();
			}
			mProgressContainer.setVisibility(View.VISIBLE);
			mListContainer.setVisibility(View.GONE);
		}
	}

	/**
	 * Get the ListAdapter associated with this activity's ListView.
	 */
	public ExpandableListAdapter getListAdapter() {
		return mAdapter;
	}

	private void ensureList() {
		if (mList != null) {
			return;
		}
		View root = getView();
		if (root == null) {
			throw new IllegalStateException("Content view not yet created");
		}
		if (root instanceof ExpandableListView) {
			mList = (ExpandableListView) root;
		} else {
			mStandardEmptyView = (TextView) root
					.findViewById(INTERNAL_EMPTY_ID);
			if (mStandardEmptyView == null) {
				mEmptyView = root.findViewById(android.R.id.empty);
			} else {
				mStandardEmptyView.setVisibility(View.GONE);
			}
			mProgressContainer = root
					.findViewById(INTERNAL_PROGRESS_CONTAINER_ID);
			mListContainer = root.findViewById(INTERNAL_LIST_CONTAINER_ID);
			View rawListView = root.findViewById(INTERNAL_EXPANDABLE_LIST_ID);
			if (!(rawListView instanceof ExpandableListView)) {
				if (rawListView == null) {
					throw new RuntimeException(
							"Your content must have a ExpandableListView whose id attribute is "
									+ "'android.R.id.list'");
				}
				throw new RuntimeException(
						"Content has view with id attribute 'android.R.id.list' "
								+ "that is not a ExpandableListView class");
			}
			mList = (ExpandableListView) rawListView;
			if (mEmptyView != null) {
				mList.setEmptyView(mEmptyView);
			} else if (mEmptyText != null) {
				mStandardEmptyView.setText(mEmptyText);
				mList.setEmptyView(mStandardEmptyView);
			}
		}
		mListShown = true;
		mList.setOnChildClickListener(mOnChildClickListener);
		mList.setOnGroupClickListener(mOnGroupClickListener);
		mList.setOnGroupExpandListener(mOnGroupExpandListener);
		mList.setOnGroupCollapseListener(mOnGroupCollapseListener);
		if (mAdapter != null) {
			ExpandableListAdapter adapter = mAdapter;
			mAdapter = null;
			setExpandableListAdapter(adapter, false);
		} else {
			// We are starting without an adapter, so assume we won't
			// have our data right away and start with the progress indicator.
			if (mProgressContainer != null) {
				setListShown(false /* shown */, false /* animate */);
			}
		}
		mHandler.post(mRequestFocus);
	}

	public void setExclusiveExpand(boolean exclusive) {
		mExclusiveExpand = exclusive;
	}

	public void addNamesValues(View v) {
		TextView txtClass = (TextView) v.findViewById(R.id.txt_class_name);
		TextView txtCoach = (TextView) v.findViewById(R.id.txt_coach_name);
		TextView txtStarts = (TextView) v.findViewById(R.id.txt_starts_at);
		try {
			GymClassesFragment.className = (String) txtClass.getText();
			GymClassesFragment.coachName = (String) txtCoach.getText();
			GymClassesFragment.hourClass = (String) txtStarts.getText();
		} catch (Exception ex) {

		}
	}

	public void expandSelection(int position) {
		mList.expandGroup(position);
	}

}
