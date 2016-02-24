package mx.com.sportsworld.sw.fragment;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.LoyaltyActivity;
import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.adapter.AwardAdapter;
import mx.com.sportsworld.sw.app.SherlockWorkingListFragment;
import mx.com.sportsworld.sw.fragment.dialog.ProgressDialogFragment;
import mx.com.sportsworld.sw.pojo.AwardItemPojo;
import mx.com.sportsworld.sw.pojo.AwardPojo;
import mx.com.sportsworld.sw.pojo.BranchItemPojo;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.utils.GeneralUtils;
import mx.com.sportsworld.sw.web.task.BranchTask;
import mx.com.sportsworld.sw.widget.FooterAwardView;
import mx.com.sportsworld.sw.widget.HeaderAward;

// TODO: Auto-generated Javadoc

/**
 * The Class AwardFragment.
 */
public class AwardFragment extends SherlockWorkingListFragment implements
		View.OnClickListener {

	/** The dialgo fragment. */
	Fragment dialgoFragment;
	
	/** The btn finish. */
	public Button btnFinish;
	
	/** The m adapter. */
	public AwardAdapter mAdapter;
	
	/** The header. */
	private HeaderAward header;
	
	/** The footer. */
	private FooterAwardView footer;
	
	/** The list pojos. */
	public static AwardPojo listPojos;
	
	/** The progress. */
	public static ProgressDialogFragment progress;

	/**
	 * New instance.
	 *
	 * @return the award fragment
	 */
	public static final AwardFragment newInstance() {
		return new AwardFragment();
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.app.SherlockWorkingListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.fragment_awards,
				null /* root */, false /* attachToRoot */);
		btnFinish = (Button) view.findViewById(R.id.btn_award_finalizar);
		btnFinish.setOnClickListener(this);
		mAdapter = new AwardAdapter(getActivity());

		return view;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getSherlockActivity().getSupportActionBar().setTitle(R.string.award);
		header = new HeaderAward(getActivity());
		footer = new FooterAwardView(getActivity());

		getListView().addHeaderView(header);
		getListView().addFooterView(footer);
		setListAdapter(mAdapter);
		fillAdapter();
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_award_finalizar:
			final Intent mainBoard = new Intent(getActivity() /* context */,
					LoyaltyActivity.class);
			mainBoard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			mainBoard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mainBoard.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(mainBoard);
			getActivity().finish();
			break;

		default:
			break;
		}
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
	 * Fill adapter.
	 */
	public void fillAdapter() {
		showFragmentsDialog(false);
		footer.setSaldo(GeneralUtils.thousandFormat(SportsWorldPreferences
				.getSaldoActual(getActivity())));
		footer.setRedimidos(GeneralUtils.thousandFormat(SportsWorldPreferences
				.getPuntosRedimidos(getActivity())));
		for (AwardItemPojo pojo : listPojos.getItems())
			mAdapter.add(pojo);
		mAdapter.notifyDataSetChanged();

		BranchItemPojo pojo = new BranchItemPojo();
		pojo.setId_user(SportsWorldPreferences.getCurrentUserId(getActivity()));
		pojo.setId_club(SportsWorldPreferences.getIdClub(getActivity()) + "");
		BranchTask addressTask = new BranchTask(new ResponseInterface() {

			@Override
			public void onResultResponse(Object obj) {
				progress.dismissAllowingStateLoss();
				BranchItemPojo resPojo = (BranchItemPojo) obj;

				if (resPojo.isStatus()) {
					setAddres(resPojo);
				} else
					Toast.makeText(
							getActivity() /* context */,
							getResources().getString(
									R.string.error_connection_server),
							Toast.LENGTH_SHORT).show();
			}
		}, getActivity());
		addressTask.execute(pojo);

	}

	/**
	 * Sets the addres.
	 *
	 * @param pojo the new addres
	 */
	public void setAddres(BranchItemPojo pojo) {
		String address = "La recepci�n del Club " + pojo.getmName()
				+ ", ubicado en " + pojo.getmAddress()
				+ ". \n\nDentro de los Horarios de Operaci�n.";

		footer.setAddress(address);

	}
}
