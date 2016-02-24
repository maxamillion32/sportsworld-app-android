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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.GymClassesActivity;
import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.fragment.dialog.ProgressDialogFragment;
import mx.com.sportsworld.sw.pojo.BranchItemPojo;
import mx.com.sportsworld.sw.pojo.BranchPojo;
import mx.com.sportsworld.sw.utils.ConnectionUtils;
import mx.com.sportsworld.sw.web.task.ClubTask;

// TODO: Auto-generated Javadoc

/**
 * The Class FacebookBranchesFragment.
 */
public class FacebookBranchesFragment extends SherlockListFragment implements DatePickerDialog.OnDateSetListener {

	public int Post ;

	/** The m adapter. */
	private ArrayAdapter<BranchItemPojo> mAdapter;
	
	/** The m listener. */
	private OnFavoriteFaceBranchClickListener mListener;
	
	/** The progress. */
	private ProgressDialogFragment progress;
	
	/** The dialgo fragment. */
	Fragment dialgoFragment;
	
	/** The nombre. */
	private ArrayList<String> nombre;

	public static final String DATEPICKER_TAG = "datepicker";

	/**
	 * Instantiates a new facebook branches fragment.
	 */
	public FacebookBranchesFragment() {
	}

	/**
	 * New instance.
	 *
	 * @return the facebook branches fragment
	 */
	public static FacebookBranchesFragment newInstance() {
		return new FacebookBranchesFragment();
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockListFragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFavoriteFaceBranchClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.getClass().getName()
					+ " must impleement "
					+ OnFavoriteFaceBranchClickListener.class.getName());
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		/* If we pass a null object, this constructor becomes ambiguous. */

		getSherlockActivity().getSupportActionBar().setTitle(
				getResources().getString(R.string.clubs));
		showFragmentDialog(false);
		BranchPojo pojo = new BranchPojo();
		pojo.setmLatitude(0);
		pojo.setmLongitude(0);
		ClubTask task = new ClubTask(new ResponseInterface() {

			@Override
			public void onResultResponse(Object obj) {
				// TODO Auto-generated method stub
				BranchPojo resPojo = (BranchPojo) obj;
				progress.dismissAllowingStateLoss();
				nombre = new ArrayList<String>();
				List<BranchItemPojo> listDummy = new ArrayList<BranchItemPojo>();

				for (BranchItemPojo temp : resPojo.getListBranch()) {
					nombre.add(temp.getmName());
				}

				Collections.sort(nombre);

				for (int a = 0; a < nombre.size(); a++) {
					for (BranchItemPojo temp : resPojo.getListBranch()) {
						if (temp.getmName().equals(nombre.get(a))) {
							listDummy.add(temp);
							break;
						}

					}
				}
				resPojo.setListBranch(listDummy);

				if (resPojo.isStatus())
					/*mAdapter = new ArrayAdapter<BranchItemPojo>(
							getActivity() /* context *//*,
							android.R.layout.simple_list_item_1,
							android.R.id.text1, resPojo.getListBranch());*/
				mAdapter = new ArrayAdapter<BranchItemPojo>(
						getActivity() /* context */,
						R.layout.list_item_branch, android.R.id.text1, resPojo.getListBranch());
				setListAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				setListShown(true);
				progress.dismiss();
			}
		}, getActivity());
		task.execute(pojo);

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		//mListener.onFavoriteFaceBranchClick(mAdapter.getItem(position)
		//		.getmUnId());
		final Calendar calendar = Calendar.getInstance();
		final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
		//final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(v)
		datePickerDialog.setVibrate(false);
		datePickerDialog.setYearRange(1985, 2028);
		datePickerDialog.setCloseOnSingleTapDay(false);
		datePickerDialog.show(getSherlockActivity().getSupportFragmentManager(), DATEPICKER_TAG);
		Post = position;
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
				GymClassesActivity.EXTRA_BRANCH_ID, mAdapter.getItem(Post).getmUnId());
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

	/**
	 * The listener interface for receiving onFavoriteFaceBranchClick events.
	 * The class that is interested in processing a onFavoriteFaceBranchClick
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnFavoriteFaceBranchClickListener<code> method. When
	 * the onFavoriteFaceBranchClick event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnFavoriteFaceBranchClickEvent
	 */
	public static interface OnFavoriteFaceBranchClickListener {
		
		/**
		 * On favorite face branch click.
		 *
		 * @param id the id
		 */
		public void onFavoriteFaceBranchClick(long id);
	}

	/**
	 * Show fragment dialog.
	 *
	 * @param savedInstanceState the saved instance state
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
			progress.setCancelable(false);
			progress.show(getFragmentManager(),
					ProgressDialogFragment.progressDialogTag);
		}

	}

	/**
	 * Face clubs.
	 */
	public void faceClubs() {

	}
}
