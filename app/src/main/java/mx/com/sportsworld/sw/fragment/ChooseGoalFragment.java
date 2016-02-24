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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.MainboardActivity;
import mx.com.sportsworld.sw.app.SherlockWorkingListFragment;
import mx.com.sportsworld.sw.fragment.dialog.ProgressDialogFragment;
import mx.com.sportsworld.sw.model.Goal;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.resource.GoalResource;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.utils.ConnectionUtils;
import mx.com.sportsworld.sw.widget.HeaderView;

// TODO: Auto-generated Javadoc

/**
 * 
 * Shows goals (based on the first two).
 * 
 * 
 */
public class ChooseGoalFragment extends SherlockWorkingListFragment implements
		LoaderManager.LoaderCallbacks<RequestResult<Goal>> {

	/** The m listener. */
	private OnGoalClickListener mListener;
	
	/** The m adapter. */
	private GoalAdapter mAdapter;
	
	/** The progress. */
	public static ProgressDialogFragment progress;
	
	/** The dialgo fragment. */
	Fragment dialgoFragment;

	/**
	 * Instantiates a new choose goal fragment.
	 */
	public ChooseGoalFragment() {
		/* Do nothing */
	}

	/**
	 * New instance.
	 *
	 * @return the choose goal fragment
	 */
	public static ChooseGoalFragment newInstance() {
		return new ChooseGoalFragment();
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnGoalClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.getClass().getName()
					+ " must implement " + OnGoalClickListener.class.getName());
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getSherlockActivity().getSupportActionBar()
				.setTitle(R.string.objective);
		setListAdapter(null);
		showFragmentsDialog(false);
		HeaderView header = new HeaderView(getActivity());
		header.setName(SportsWorldPreferences.getGuestName(getActivity()));
		getListView().addHeaderView(header);
		getLoaderManager().initLoader(0 /* loaderId */, null /* loaderArgs */,
				this /* loaderCallback */);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<RequestResult<Goal>> onCreateLoader(int id, Bundle args) {
		return new GoalLoader(getActivity() /* context */);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<RequestResult<Goal>> loader,
			RequestResult<Goal> result) {

		mAdapter = new GoalAdapter(getActivity() /* context */);
		setListAdapter(mAdapter);
		setListShown(false);
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

		if (progress != null)
			progress.dismissAllowingStateLoss();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<RequestResult<Goal>> loader) {
		mAdapter.setGoals(null);
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.app.SherlockWorkingListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (v.getTag() != null) {
			SportsWorldPreferences.setSelectedGoal(getActivity(),
					((TextView) v).getText().toString());
			mListener.onGoalClick(id);
		}
	}

	/**
	 * The listener interface for receiving onGoalClick events.
	 * The class that is interested in processing a onGoalClick
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnGoalClickListener<code> method. When
	 * the onGoalClick event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnGoalClickEvent
	 */
	public static interface OnGoalClickListener {
		
		/**
		 * On goal click.
		 *
		 * @param id the id
		 */
		void onGoalClick(long id);
	}

	/**
	 * The Class GoalAdapter.
	 */
	private static class GoalAdapter extends BaseAdapter {

		/** The m goals. */
		private List<Goal> mGoals;
		
		/** The m inflater. */
		private LayoutInflater mInflater;

		/**
		 * Instantiates a new goal adapter.
		 *
		 * @param context the context
		 */
		public GoalAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			return (mGoals == null) ? 0 : mGoals.size();
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int position) {
			return (mGoals == null) ? null : mGoals.get(position);
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
			return (mGoals == null) ? -1 : mGoals.get(position).getId();
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
				holder.txtDescription = (TextView) convertView
						.findViewById(android.R.id.text1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.txtDescription
					.setText(mGoals.get(position).getDescription());

			return convertView;

		}

		/**
		 * The Class ViewHolder.
		 */
		private static class ViewHolder {
			/* package *//** The txt description. */
			TextView txtDescription;
		}

		/**
		 * Sets the goals.
		 *
		 * @param goals the new goals
		 */
		public void setGoals(List<Goal> goals) {
			mGoals = goals;
			if (goals == null) {
				notifyDataSetInvalidated();
			} else {
				notifyDataSetChanged();
			}
		}

	}

	/**
	 * The Class GoalLoader.
	 */
	private static class GoalLoader extends
			AsyncTaskLoader<RequestResult<Goal>> {

		/** The m result. */
		private RequestResult<Goal> mResult;

		/**
		 * Instantiates a new goal loader.
		 *
		 * @param context the context
		 */
		public GoalLoader(Context context) {
			super(context);
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
		public RequestResult<Goal> loadInBackground() {
			if (!ConnectionUtils.isNetworkAvailable(getContext())) {
				return null;
			}

			RequestResult<Goal> result = null;
			try {
				result = GoalResource.fetchGoals(getContext());
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
		public void deliverResult(RequestResult<Goal> data) {
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
		public void onCanceled(RequestResult<Goal> data) {
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
