package mx.com.sportsworld.sw.fragment;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.CatalogActivity;
import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.adapter.TransactionAdapter;
import mx.com.sportsworld.sw.app.SherlockWorkingListFragment;
import mx.com.sportsworld.sw.fragment.dialog.ProgressDialogFragment;
import mx.com.sportsworld.sw.pojo.AwardProfilePojo;
import mx.com.sportsworld.sw.pojo.HistoryPojo;
import mx.com.sportsworld.sw.pojo.UserHistoryPojo;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.utils.ConnectionUtils;
import mx.com.sportsworld.sw.utils.GeneralUtils;
import mx.com.sportsworld.sw.web.task.AwardProfileTask;
import mx.com.sportsworld.sw.web.task.HistoryTask;
import mx.com.sportsworld.sw.widget.FooterTableView;
import mx.com.sportsworld.sw.widget.HeaderTableView;

// TODO: Auto-generated Javadoc

/**
 * The Class LoyaltyPointFragment.
 */
public class LoyaltyPointFragment extends SherlockWorkingListFragment implements
		View.OnClickListener {

	/** The txt_name. */
	public TextView txt_name;
	
	/** The txt_points. */
	public TextView txt_points;
	
	/** The btn catalog. */
	public Button btnCatalog;
	
	/** The m adapter. */
	private TransactionAdapter mAdapter;
	
	/** The dialgo fragment. */
	Fragment dialgoFragment;
	
	/** The progress. */
	public static ProgressDialogFragment progress;
	
	/** The next fragment. */
	private boolean nextFragment = true;
	
	/** The profile task. */
	AwardProfileTask profileTask;

	/**
	 * New instance.
	 *
	 * @return the loyalty point fragment
	 */
	public static final LoyaltyPointFragment newInstance() {
		return new LoyaltyPointFragment();
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.app.SherlockWorkingListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.fragment_loyalty_profile,
				null /* root */, false /* attachToRoot */);
		txt_name = (TextView) view.findViewById(R.id.txt_loy_name);
		txt_name.setText(SportsWorldPreferences.getGuestName(getActivity()));

		txt_points = (TextView) view.findViewById(R.id.txt_loy_points);

		btnCatalog = (Button) view.findViewById(R.id.btn_loy_catalog);
		btnCatalog.setOnClickListener(this);
		mAdapter = new TransactionAdapter(getActivity());

		return view;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getSherlockActivity().getSupportActionBar().setTitle(R.string.loyalty);
		HeaderTableView header = new HeaderTableView(getActivity());
		getListView().addHeaderView(header);
		FooterTableView footer = new FooterTableView(getActivity());
		getListView().addFooterView(footer);
		setListAdapter(mAdapter);
		showFragmentsDialog(false);
		fillView();
	}

	// Llenamos la lista que tenemos
	/**
	 * Fill list.
	 */
	public void fillList() {

		UserHistoryPojo pojo = new UserHistoryPojo();
		pojo.setId_user(SportsWorldPreferences.getCurrentUserId(getActivity()));
		HistoryTask historyTask = new HistoryTask(new ResponseInterface() {

			@Override
			public void onResultResponse(Object obj) {
				// TODO Auto-generated method stub

				progress.dismissAllowingStateLoss();
				UserHistoryPojo history = (UserHistoryPojo) obj;

				if (history.isStatus() && history != null) {
					nextFragment = true;
					for (HistoryPojo listItem : history.getListItems()) {
						mAdapter.add(listItem);
					}

					for (mAdapter.getCount(); mAdapter.getCount() < 20; mAdapter
							.getCount()) {
						mAdapter.add(dummyPojo());
					}

					mAdapter.notifyDataSetChanged();

				} else {
					for (int a = 0; a < 20; a++) {
						mAdapter.add(dummyPojo());
					}

					mAdapter.notifyDataSetChanged();

					Toast.makeText(
							getActivity(),
							getResources().getString(
									R.string.error_connection_server),
							Toast.LENGTH_SHORT).show();
					nextFragment = false;
				}
			}
		}, getActivity());

		Calendar c = Calendar.getInstance();
		Calendar cc = Calendar.getInstance();
		cc.set(c.get(Calendar.YEAR) - 10, c.get(Calendar.MONTH) + 1,
				c.get(Calendar.DAY_OF_MONTH));
		pojo.setFechaInicio(cc);
		pojo.setFechaFin(c);
		historyTask.execute(pojo);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_loy_catalog:
			Intent catalog = new Intent(getActivity(), CatalogActivity.class);
			if (isNetworkAvailable() && nextFragment)
				startActivity(catalog);
			break;

		default:
			break;
		}

	}

	/**
	 * Fill view.
	 */
	public void fillView() {
		AwardProfilePojo pojo = new AwardProfilePojo();
		pojo.setId_user(SportsWorldPreferences.getCurrentUserId(getActivity()));
		profileTask = new AwardProfileTask(new ResponseInterface() {

			@Override
			public void onResultResponse(Object obj) {
				// TODO Auto-generated method stub
				AwardProfilePojo profile = (AwardProfilePojo) obj;

				if (profile.isStatus()) {
					int saldo = Integer.parseInt(profile.getListItems().get(0)
							.getPuntos());
					txt_points.setText(GeneralUtils.thousandFormat(saldo));
					SportsWorldPreferences.setSaldoActual(getActivity(), saldo);

					fillList();
				} else {
					for (int a = 0; a < 20; a++) {
						mAdapter.add(dummyPojo());
					}

					mAdapter.notifyDataSetChanged();

					progress.dismissAllowingStateLoss();
				}
			}
		}, getActivity());
		profileTask.execute(pojo);

	}

	/**
	 * Show fragments dialog.
	 *
	 * @param savedInstanceState the saved instance state
	 */
	public void showFragmentsDialog(boolean savedInstanceState) {
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
	 * Dummy pojo.
	 *
	 * @return the history pojo
	 */
	public HistoryPojo dummyPojo() {
		HistoryPojo pojo = new HistoryPojo();
		Calendar c = Calendar.getInstance();
		c.set(1, 1, 1);
		pojo.setFechaEvento(c);
		pojo.setNombreEvento("---");
		pojo.setPuntos("---");
		return pojo;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		profileTask.cancel(true);
		super.onDestroy();
	}

	/**
	 * Checks if is network available.
	 *
	 * @return true, if is network available
	 */
	public boolean isNetworkAvailable() {
		boolean network = ConnectionUtils.isNetworkAvailable(getActivity());
		if (!network)
			Toast.makeText(getActivity(),
					getResources().getString(R.string.error_connection),
					Toast.LENGTH_SHORT).show();
		return network;
	}

}
