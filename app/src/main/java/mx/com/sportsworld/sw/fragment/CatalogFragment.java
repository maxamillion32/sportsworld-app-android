package mx.com.sportsworld.sw.fragment;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.AwardActivity;
import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.adapter.CatalogAdapter;
import mx.com.sportsworld.sw.adapter.CatalogAdapter.OnCatalogClickListener;
import mx.com.sportsworld.sw.app.SherlockWorkingListFragment;
import mx.com.sportsworld.sw.fragment.dialog.FragmentDialogDetail;
import mx.com.sportsworld.sw.fragment.dialog.ProgressDialogFragment;
import mx.com.sportsworld.sw.imgloader.ImageFetcher;
import mx.com.sportsworld.sw.pojo.AwardItemPojo;
import mx.com.sportsworld.sw.pojo.AwardPojo;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.utils.ConnectionUtils;
import mx.com.sportsworld.sw.utils.GeneralUtils;
import mx.com.sportsworld.sw.web.task.AwardTask;
import mx.com.sportsworld.sw.web.task.ChangePointsTask;

// TODO: Auto-generated Javadoc

/**
 * The Class CatalogFragment.
 */
public class CatalogFragment extends SherlockWorkingListFragment implements
		View.OnClickListener, OnCatalogClickListener {
	
	/** The m adapter. */
	CatalogAdapter mAdapter;
	// FooterCatalogView footer;
	/** The btn_catalog_continue. */
	Button btn_catalog_continue;
	
	/** The saldo. */
	int saldo = 0;
	
	/** The redimir. */
	int redimir = 0;
	
	/** The restante. */
	int restante = 0;
	
	/** The progress. */
	private ProgressDialogFragment progress;
	
	/** The dialgo fragment. */
	Fragment dialgoFragment;
	
	/** The txt_footer_saldo. */
	TextView txt_footer_saldo;
	
	/** The txt_saldo_redimir. */
	TextView txt_saldo_redimir;
	
	/** The txt_saldo_restante. */
	TextView txt_saldo_restante;
	
	/** The fragment detail. */
	public FragmentDialogDetail fragmentDetail;
	
	/** The award task. */
	AwardTask awardTask;
	
	/** The lis award items. */
	public List<AwardItemPojo> lisAwardItems = new ArrayList<AwardItemPojo>();

	/**
	 * New instance.
	 *
	 * @return the catalog fragment
	 */
	public static final CatalogFragment newInstance() {
		return new CatalogFragment();
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.app.SherlockWorkingListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.fragment_catalog,
				null /* root */, false /* attachToRoot */);
		btn_catalog_continue = (Button) view
				.findViewById(R.id.btn_catalog_continue);
		btn_catalog_continue.setOnClickListener(this);
		mAdapter = new CatalogAdapter(getActivity());
		mAdapter.setOnCatalogClickListener(this);
		txt_footer_saldo = (TextView) view.findViewById(R.id.txt_catalog_saldo);
		txt_saldo_redimir = (TextView) view
				.findViewById(R.id.txt_catalog_redimir);
		txt_saldo_restante = (TextView) view
				.findViewById(R.id.txt_catalog_restante);
		return view;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		getSherlockActivity().getSupportActionBar().setTitle(R.string.catalog);

		// footer = new FooterCatalogView(getActivity());
		saldo = SportsWorldPreferences.getSaldoActual(getActivity());
		txt_footer_saldo.setText(GeneralUtils.thousandFormat(saldo));
		txt_saldo_restante.setText(GeneralUtils.thousandFormat(saldo));
		txt_saldo_redimir.setText(GeneralUtils.thousandFormat(redimir));
		// getListView().addFooterView(footer);
		setListAdapter(mAdapter);
		showFragmentDialog(false);
		fillList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		awardTask.cancel(true);
		mAdapter.clear();
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_catalog_continue:
			if (restante < 0)
				Toast.makeText(
						getActivity(),
						getResources().getText(
								R.string.error_puntos_insuficientes),
						Toast.LENGTH_SHORT).show();
			if (restante > 0)
				changePoints();
			if (redimir == 0)
				Toast.makeText(
						getActivity(),
						getResources().getText(
								R.string.error_catalog_no_selected),
						Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}

	/*
	 * Fill te catalog list
	 */
	/**
	 * Fill list.
	 */
	public void fillList() {

		AwardPojo awardPojo = new AwardPojo();

		awardPojo.setId_user(SportsWorldPreferences
				.getCurrentUserId(getActivity()));
		awardTask = new AwardTask(new ResponseInterface() {
			@Override
			public void onResultResponse(Object obj) {
				// TODO Auto-generated method stub
				progress.dismissAllowingStateLoss();
				AwardPojo result = (AwardPojo) obj;
				if (result.isStatus()) {
					for (AwardItemPojo pojo : result.getItems()) {
						mAdapter.add(pojo);
					}
					mAdapter.notifyDataSetChanged();

				} else
					Toast.makeText(
							getActivity() /* context */,
							getResources().getString(
									R.string.error_connection_server),
							Toast.LENGTH_SHORT).show();

			}
		}, getActivity());
		awardTask.execute(awardPojo);
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.adapter.CatalogAdapter.OnCatalogClickListener#onCatalogClick(int)
	 */
	@Override
	public void onCatalogClick(int position) {
		// TODO Auto-generated method stub
		boolean isPressed = mAdapter.getItem(position).isPressed();
		if (isPressed) {
			redimir = redimir
					+ Integer.parseInt(mAdapter.getItem(position).getPuntos());
		} else
			redimir = redimir
					- Integer.parseInt(mAdapter.getItem(position).getPuntos());

		restante = saldo - redimir;
		txt_saldo_redimir.setText(GeneralUtils.thousandFormat(redimir));
		txt_saldo_restante.setText(GeneralUtils.thousandFormat(restante));

	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.app.SherlockWorkingListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		loadDataDetail(mAdapter.getItem(position));
	}

	/**
	 * The listener interface for receiving onDetailClick events.
	 * The class that is interested in processing a onDetailClick
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnDetailClickListener<code> method. When
	 * the onDetailClick event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnDetailClickEvent
	 */
	public static interface OnDetailClickListener {
		
		/**
		 * On detail click.
		 *
		 * @param id the id
		 */
		void onDetailClick(long id);
	}

	/**
	 * Change points.
	 */
	public void changePoints() {
		lisAwardItems.clear();

		for (int a = 0; a < mAdapter.getCount(); a++) {

			if (mAdapter.getItem(a).isPressed()) {
				lisAwardItems.add(mAdapter.getItem(a));

			}

		}
		if (lisAwardItems.size() != 0)
			confirmDialog();
	}

	/**
	 * Change task.
	 */
	public void changeTask() {
		showFragmentDialog(false);
		AwardPojo pojo = new AwardPojo();
		pojo.setId_user(SportsWorldPreferences.getCurrentUserId(getActivity()));
		ChangePointsTask pointsTask = new ChangePointsTask(
				new ResponseInterface() {

					@Override
					public void onResultResponse(Object obj) {
						// TODO Auto-generated method stub
						progress.dismissAllowingStateLoss();
						AwardPojo resPojo = (AwardPojo) obj;
						int rSaldo = SportsWorldPreferences.getSaldoActual(getActivity());
						int rRedimir = 0;
						if (resPojo.getItems().size() != 0) {
							for (AwardItemPojo pojo : resPojo.getItems()) {
								{
									int puntos = Integer.parseInt(pojo
											.getPuntos());
									rSaldo -= puntos;
									rRedimir += puntos;
								}

							}
							AwardFragment.listPojos = resPojo;
							SportsWorldPreferences.setSaldoActual(
									getActivity(), rSaldo);
							SportsWorldPreferences.setPuntosRedimidos(
									getActivity(), rRedimir);
							Intent awardAct = new Intent(getActivity(),
									AwardActivity.class);
							if (isNetworkAvailable()) {
								startActivity(awardAct);

								getActivity().finish();
							}
						}

					}
				}, getActivity());
		pojo.setItems(lisAwardItems);
		pointsTask.execute(pojo);
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

	/**
	 * Confirm dialog.
	 */
	public void confirmDialog() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					if (isNetworkAvailable())
						changeTask();
					else
						failTransaction();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					dialog.dismiss();
					break;
				}
			}
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(
				getResources().getString(R.string.catalog_dialog_confirm))
				.setPositiveButton("Si", dialogClickListener)
				.setNegativeButton("No", dialogClickListener).show();
	}

	/**
	 * Fail transaction.
	 */
	public void failTransaction() {
		String text = "No fue posible canjear los siguientes premios: \n";

		for (AwardItemPojo itmemPojo : lisAwardItems) {
			text += "* " + itmemPojo.getPremio() + "\n";
		}
		text += "Por favor verifica tu conexi�n a Internet.";

		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Load data detail.
	 *
	 * @param itemPojo the item pojo
	 */
	public void loadDataDetail(final AwardItemPojo itemPojo) {
		new Thread(new Runnable() {
			private File imgDetail;
			public Bitmap bitmap;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				imgDetail = ImageFetcher.downloadBitmapToFile(getActivity(),
						itemPojo.getImagen(), "imgDetail");
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				if (bitmap == null && imgDetail != null)
					bitmap = BitmapFactory.decodeFile(
							imgDetail.getAbsolutePath(), options);

				fragmentDetail = FragmentDialogDetail.newInstance(itemPojo,
						bitmap);
				Log.i("kokusho", "apreto un item");
				fragmentDetail.show(getFragmentManager(),
						"detailDialogFragment");

				progress.dismissAllowingStateLoss();

			}
		}).start();
		showFragmentDialog(false);
	}
}
