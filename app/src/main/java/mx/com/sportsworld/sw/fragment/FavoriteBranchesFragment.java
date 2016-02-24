package mx.com.sportsworld.sw.fragment;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.GymClassesActivity;
import mx.com.sportsworld.sw.activity.events.EmptyClubsInterface;
import mx.com.sportsworld.sw.activity.events.FavoriteBranchInterface;
import mx.com.sportsworld.sw.adapter.EmptyClubsAdapter;
import mx.com.sportsworld.sw.adapter.FavoriteBranchAdapter;
import mx.com.sportsworld.sw.fragment.dialog.ProgressDialogFragment;
import mx.com.sportsworld.sw.loader.FavoriteBranchesLoader;
import mx.com.sportsworld.sw.model.Branch;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.service.MarkBranchAsFavoriteService;
import mx.com.sportsworld.sw.utils.ConnectionUtils;

// TODO: Auto-generated Javadoc

/**
 * The Class FavoriteBranchesFragment.
 */
public class FavoriteBranchesFragment extends SherlockListFragment implements
		LoaderCallbacks<RequestResult<Branch>>, FavoriteBranchInterface,DatePickerDialog.OnDateSetListener {
    public int Post ;
	/** The m adapter. */
	private ArrayAdapter<Branch> mAdapter;

	/** The m listener. */
	private OnFavoriteBranchClickListener mListener;

	/** The progress. */
	private ProgressDialogFragment progress;

	/** The dialgo fragment. */
	Fragment dialgoFragment;

	private EmptyClubsInterface emptyEvent;

	private int REQUEST_CODE_REMOVE_MARK_BRANCH_AS_FAVORITE = 2;

	public static final String DATEPICKER_TAG = "datepicker";
	/**
	 * Instantiates a new favorite branches fragment.
	 */
	public FavoriteBranchesFragment() {
	}

	/**
	 * New instance.
	 * 
	 * @return the favorite branches fragment
	 */
	public static FavoriteBranchesFragment newInstance() {
		return new FavoriteBranchesFragment();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockListFragment#onAttach(android.app.Activity
	 * )
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFavoriteBranchClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.getClass().getName()
					+ " must impleement "
					+ OnFavoriteBranchClickListener.class.getName());
		}
	}
	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
		String date = day + "/" + month +"/" + year;
		String parts[] = date.split("/");

		int day1 = Integer.parseInt(parts[0]);
		int month1 = Integer.parseInt(parts[1]);
		int year1 = Integer.parseInt(parts[2]);

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		long milliTime = calendar.getTimeInMillis();

		final Intent goToShowClasses = new Intent(getActivity()
				.getApplicationContext() /* context */,
				GymClassesActivity.class);
		goToShowClasses.putExtra(
				GymClassesActivity.EXTRA_BRANCH_ID, mAdapter.getItem(Post).getUnId());
		goToShowClasses.putExtra(
				GymClassesActivity.EXTRA_DATE_ID,
				milliTime);
		if (isNetworkAvailable())
			startActivity(goToShowClasses);

	}
	public boolean isNetworkAvailable() {
		boolean network = ConnectionUtils.isNetworkAvailable(getActivity()
				.getApplicationContext());
		if (!network)
			Toast.makeText(getActivity().getApplicationContext(),
					getResources().getString(R.string.error_connection),
					Toast.LENGTH_SHORT).show();
		return network;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListShown(false);

		/* If we pass a null object, this constructor becomes ambiguous. */

		getSherlockActivity().getSupportActionBar().setTitle(
				getResources().getString(R.string.classes_fav));

		if (savedInstanceState != null)

			showFragmentDialog(true);
		else
			showFragmentDialog(false);

		changeAdapter(true);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int,
	 * android.os.Bundle)
	 */
	@Override
	public Loader<RequestResult<Branch>> onCreateLoader(int id, Bundle args) {
		return new FavoriteBranchesLoader(getActivity() /* context */);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android
	 * .support.v4.content.Loader, java.lang.Object)
	 */
	@SuppressLint("NewApi")
	// We check which build version we are running
	@Override
	public void onLoadFinished(Loader<RequestResult<Branch>> loader,
			RequestResult<Branch> result) {
		mAdapter.clear();
		if (isResumed()) {
			setListShownNoAnimation(true);
		} else {
			setListShown(true);
		}

		if (result == null) {
			setEmptyText(getString(R.string.error_connection));
			if (progress != null)
				progress.dismissAllowingStateLoss();
			return;
		}

		List<Branch> branches = result.getData();
		Collections.sort(branches);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mAdapter.addAll(branches);
		} else {
			final int count = branches.size();
			for (int i = 0; i < count; i++) {
				mAdapter.add(branches.get(i));
			}
		}

		if (branches.size() == 0) {
			mAdapter = new ArrayAdapter<Branch>(getActivity() /* context */,
					R.layout.list_item_branch, android.R.id.text1,
					new ArrayList<Branch>());

			EmptyClubsAdapter adapterDummy = new EmptyClubsAdapter(
					getActivity());
			emptyEvent.onBranchesEmty(true);
			adapterDummy.add("Elemento dummy");
			setListAdapter(adapterDummy);
			getListView().setDivider(null);
			getListView().setFocusable(false);

		} else {
			emptyEvent.onBranchesEmty(false);
			setListAdapter(mAdapter);
			getListView().setDivider(
					getActivity().getResources().getDrawable(
							android.R.drawable.divider_horizontal_bright));
			// getListView().setFocusable(true);
		}
		mAdapter.notifyDataSetChanged();
		if (progress != null)
			progress.dismissAllowingStateLoss();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android
	 * .support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<RequestResult<Branch>> loader) {
		/* Do nothing */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView
	 * , android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		//mListener.onFavoriteBranchClick(mAdapter.getItem(position).getUnId());

		final Calendar calendar = Calendar.getInstance();
		final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
		//final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(v)
		datePickerDialog.setVibrate(false);
		datePickerDialog.setYearRange(1985, 2028);
		datePickerDialog.setCloseOnSingleTapDay(false);
		datePickerDialog.show(getSherlockActivity().getSupportFragmentManager(), DATEPICKER_TAG);
		Post = position;

	}

	/**
	 * The listener interface for receiving onFavoriteBranchClick events. The
	 * class that is interested in processing a onFavoriteBranchClick event
	 * implements this interface, and the object created with that class is
	 * registered with a component using the component's
	 * <code>addOnFavoriteBranchClickListener<code> method. When
	 * the onFavoriteBranchClick event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see OnFavoriteBranchemptyEvent
	 */
	public static interface OnFavoriteBranchClickListener {

		/**
		 * On favorite branch click.
		 * 
		 * @param id
		 *            the id
		 */
		public void onFavoriteBranchClick(long id);
	}

	/**
	 * Show fragment dialog.
	 * 
	 * @param savedInstanceState
	 *            the saved instance state
	 */
	public void showFragmentDialog(boolean savedInstanceState) {
		dialgoFragment = getFragmentManager().findFragmentByTag(
				ProgressDialogFragment.progressDialogTag);

		if (savedInstanceState == true) {
			dialgoFragment = getFragmentManager().findFragmentByTag(
					ProgressDialogFragment.progressDialogTag);
			progress = (ProgressDialogFragment) dialgoFragment;

		} else {

			if (dialgoFragment != null) {
				getFragmentManager().beginTransaction().remove(dialgoFragment)
						.commit();
			}
			progress = ProgressDialogFragment.newInstance(getActivity());
			progress.show(getFragmentManager(),
					ProgressDialogFragment.progressDialogTag);
		}

	}

	public void refreshListBranches() {
		getLoaderManager().restartLoader(0 /* loaderId */,
				null /* loaderArgs */, this /* loaderCallback */);
	}

	public void changeAdapter(boolean isNormal) {
		if (isNormal)
			mAdapter = new ArrayAdapter<Branch>(getActivity() /* context */,
					R.layout.list_item_branch, android.R.id.text1,
					new ArrayList<Branch>());
		else
			mAdapter = new FavoriteBranchAdapter(getActivity(), this);

		setListAdapter(mAdapter);
		getLoaderManager().initLoader(0 /* loaderId */, null /* loaderArgs */,
				this /* loaderCallback */);
	}

	@Override
	public void removeItem(long itemId) {
		// TODO Auto-generated method stub
		removeMarkBranchAsFavoriteService(itemId);
	}

	public void setOnBranchListener(EmptyClubsInterface mEvent) {
		emptyEvent = mEvent;

	}

	private void removeMarkBranchAsFavoriteService(long idBranch) {

		final Intent data = new Intent();
		final Activity activity = getActivity();
		final PendingIntent pendingIntent = activity
				.createPendingResult(
						REQUEST_CODE_REMOVE_MARK_BRANCH_AS_FAVORITE, data, 0 /* flags */);
		final Intent markBranchAsFavoriteService = new Intent(activity,
				MarkBranchAsFavoriteService.class);
		markBranchAsFavoriteService.putExtra(
				MarkBranchAsFavoriteService.EXTRA_BRANCH_ID, idBranch);
		markBranchAsFavoriteService.putExtra(
				MarkBranchAsFavoriteService.EXTRA_FAVORITE, false);
		markBranchAsFavoriteService
				.putExtra(MarkBranchAsFavoriteService.EXTRA_PENDING_INTENT,
						pendingIntent);
		activity.startService(markBranchAsFavoriteService);
	}
}
