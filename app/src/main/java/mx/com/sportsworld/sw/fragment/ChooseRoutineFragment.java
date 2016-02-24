package mx.com.sportsworld.sw.fragment;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.MainboardActivity;
import mx.com.sportsworld.sw.fragment.dialog.ProgressDialogFragment;
import mx.com.sportsworld.sw.model.Routine;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.resource.RoutineResource;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.utils.ConnectionUtils;
import mx.com.sportsworld.sw.widget.HeaderView;

// TODO: Auto-generated Javadoc

/**
 * 
 * Shows routines (based on a goal and a level).
 * 
 */
public class ChooseRoutineFragment extends SherlockListFragment implements
		LoaderManager.LoaderCallbacks<RequestResult<Routine>> {

	/** The Constant FRAG_ARG_GOAL_ID. */
	private static final String FRAG_ARG_GOAL_ID = "frag_arg_goal_id";
	
	/** The Constant FRAG_ARG_LEVEL_ID. */
	private static final String FRAG_ARG_LEVEL_ID = "frag_arg_routine_id";
	
	/** The m listener. */
	private OnRoutineClickListener mListener;
	
	/** The m adapter. */
	private RoutineAdapter mAdapter;
	
	/** The progress. */
	public static ProgressDialogFragment progress;
	
	/** The dialgo fragment. */
	Fragment dialgoFragment;

	/**
	 * Instantiates a new choose routine fragment.
	 */
	public ChooseRoutineFragment() {
		/* Do nothing */
	}

	/**
	 * New instance.
	 *
	 * @param goalId the goal id
	 * @param levelId the level id
	 * @return the choose routine fragment
	 */
	public static ChooseRoutineFragment newInstance(long goalId, long levelId) {
		final ChooseRoutineFragment f = new ChooseRoutineFragment();
		final Bundle args = new Bundle();
		args.putLong(FRAG_ARG_GOAL_ID, goalId);
		args.putLong(FRAG_ARG_LEVEL_ID, levelId);

		f.setArguments(args);
		return f;
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockListFragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnRoutineClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.getClass().getName()
					+ " must implement "
					+ OnRoutineClickListener.class.getName());
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getSherlockActivity().getSupportActionBar().setTitle(R.string.routine);
		setListAdapter(null);
		HeaderView header = new HeaderView(getActivity());
		header.setName(SportsWorldPreferences.getGuestName(getActivity()));
		header.setObjetive(SportsWorldPreferences
				.getSelectedGoal(getActivity()));
		header.setLevel(SportsWorldPreferences.getSelectedLevel(getActivity()));
		header.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

		showFragmentsDialog(false);
		getListView().addHeaderView(header);
		setListShown(false);
		getLoaderManager().initLoader(0 /* loaderId */, null /* loaderArgs */,
				this /* loaderCallback */);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<RequestResult<Routine>> onCreateLoader(int id, Bundle args) {
		return new RoutinesLoader(getActivity() /* context */,
				getGoalIdFromArguments(), getLevelIdFromArguments());
	}

	/**
	 * Gets the goal id from arguments.
	 *
	 * @return the goal id from arguments
	 */
	private long getGoalIdFromArguments() {
		return getArguments().getLong(FRAG_ARG_GOAL_ID);
	}

	/**
	 * Gets the level id from arguments.
	 *
	 * @return the level id from arguments
	 */
	private long getLevelIdFromArguments() {
		return getArguments().getLong(FRAG_ARG_LEVEL_ID);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<RequestResult<Routine>> loader,
			RequestResult<Routine> result) {
		mAdapter = new RoutineAdapter(getActivity() /* context */);
		setListAdapter(mAdapter);
		if (isResumed()) {
			setListShownNoAnimation(true);
		} else {
			setListShown(true);
		}
		if (result == null) {
			Intent mainboardActivity = new Intent(getActivity(),
					MainboardActivity.class);
			mainboardActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			mainboardActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(mainboardActivity);

			Toast.makeText(getActivity(),
					getResources().getString(R.string.error_connection_server),
					Toast.LENGTH_SHORT).show();

		} else
			mAdapter.setGoals(result.getData());

		progress.dismissAllowingStateLoss();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<RequestResult<Routine>> loader) {
		mAdapter.setGoals(null);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		mListener.onRoutineClick(id);
	}

	/**
	 * The listener interface for receiving onRoutineClick events.
	 * The class that is interested in processing a onRoutineClick
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnRoutineClickListener<code> method. When
	 * the onRoutineClick event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnRoutineClickEvent
	 */
	public static interface OnRoutineClickListener {
		
		/**
		 * On routine click.
		 *
		 * @param id the id
		 */
		void onRoutineClick(long id);
	}

	/**
	 * The Class RoutineAdapter.
	 */
	private static class RoutineAdapter extends BaseAdapter {

		/** The m routines. */
		private List<Routine> mRoutines;
		
		/** The m inflater. */
		private LayoutInflater mInflater;

		/**
		 * Instantiates a new routine adapter.
		 *
		 * @param context the context
		 */
		public RoutineAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			return (mRoutines == null) ? 0 : mRoutines.size();
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int position) {
			return (mRoutines == null) ? null : mRoutines.get(position);
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
			return (mRoutines == null) ? -1 : mRoutines.get(position).getId();
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(
						android.R.layout.simple_list_item_1, null /* root */,
						false /* attachToRoot */);
				holder = new ViewHolder();
				holder.txtName = (TextView) convertView
						.findViewById(android.R.id.text1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.txtName.setText(mRoutines.get(position).getName());

			return convertView;

		}

		/**
		 * The Class ViewHolder.
		 */
		private static class ViewHolder {
			/* package *//** The txt name. */
			TextView txtName;
		}

		/**
		 * Sets the goals.
		 *
		 * @param goals the new goals
		 */
		public void setGoals(List<Routine> goals) {
			mRoutines = goals;
			if (goals == null) {
				notifyDataSetInvalidated();
			} else {
				notifyDataSetChanged();
			}
		}

	}

	/**
	 * The Class RoutinesLoader.
	 */
	private static class RoutinesLoader extends
			AsyncTaskLoader<RequestResult<Routine>> {

		/** The m result. */
		private RequestResult<Routine> mResult;
		
		/** The m goal id. */
		private final long mGoalId;
		
		/** The m level id. */
		private final long mLevelId;

		/**
		 * Instantiates a new routines loader.
		 *
		 * @param context the context
		 * @param goalId the goal id
		 * @param levelId the level id
		 */
		public RoutinesLoader(Context context, long goalId, long levelId) {
			super(context);
			mGoalId = goalId;
			mLevelId = levelId;
		}

		/* (non-Javadoc)
		 * @see android.support.v4.content.Loader#onStartLoading()
		 */
		@Override
		protected void onStartLoading() {
			if (mResult != null) {
				deliverResult(mResult);
			}

			/* We don't need to monitor data, so we skip creating an observer */

			if (takeContentChanged() || mResult == null) {
				forceLoad();
			}
		}

		/* (non-Javadoc)
		 * @see android.support.v4.content.AsyncTaskLoader#loadInBackground()
		 */
		@Override
		public RequestResult<Routine> loadInBackground() {
			if (!ConnectionUtils.isNetworkAvailable(getContext())) {
				return null;
			}

			RequestResult<Routine> result = null;
			try {
				result = RoutineResource.fetchRoutines(getContext(), mGoalId,
						mLevelId);
			} catch (IOException e) {
				return null;
			} catch (JSONException e) {
				throw new RuntimeException(); // This should never happen.
			}
			return result;

		}

		/* (non-Javadoc)
		 * @see android.support.v4.content.Loader#deliverResult(java.lang.Object)
		 */
		@Override
		public void deliverResult(RequestResult<Routine> data) {
			if (isReset()) {

				/*
				 * Since this is a simple list, we don't need to release any
				 * resources
				 */
				return;
			}

			/*
			 * We don't hold a reference to old data because we don't need to
			 * release any resources
			 */

			mResult = data;

			if (isStarted()) {
				super.deliverResult(data);
			}

			/*
			 * Since this is a simple list, we don't need to release any
			 * resources
			 */
		}

		/* (non-Javadoc)
		 * @see android.support.v4.content.Loader#onStopLoading()
		 */
		@Override
		protected void onStopLoading() {
			cancelLoad();
		}

		/* (non-Javadoc)
		 * @see android.support.v4.content.Loader#onReset()
		 */
		@Override
		protected void onReset() {
			onStopLoading(); // Ensure the loader has been stopped.
			if (mResult != null) {

				/*
				 * Since this is a simple list, we don't need to release any
				 * resources
				 */
				mResult = null;
			}

			/* We don't use an observer, so we skip unregistering the observer */
		}

		/* (non-Javadoc)
		 * @see android.support.v4.content.AsyncTaskLoader#onCanceled(java.lang.Object)
		 */
		@Override
		public void onCanceled(RequestResult<Routine> data) {
			super.onCanceled(data);

			/*
			 * Since this is a simple list, we don't need to release any
			 * resources
			 */
		}

	}

	/**
	 * Show fragments dialog.
	 *
	 * @param savedInstanceState the saved instance state
	 */
	public void showFragmentsDialog(boolean savedInstanceState) {
		dialgoFragment = getActivity().getSupportFragmentManager()
				.findFragmentByTag(ProgressDialogFragment.progressDialogTag);

		if (savedInstanceState == true) {
			dialgoFragment = getActivity()
					.getSupportFragmentManager()
					.findFragmentByTag(ProgressDialogFragment.progressDialogTag);
			progress = (ProgressDialogFragment) dialgoFragment;

		} else {

			if (dialgoFragment != null) {
				getActivity().getSupportFragmentManager().beginTransaction()
						.remove(dialgoFragment).commit();
			}

			progress = ProgressDialogFragment.newInstance(getActivity());
			progress.setCancelable(false);
			progress.show(getActivity().getSupportFragmentManager(),
					ProgressDialogFragment.progressDialogTag);
		}

	}

}
