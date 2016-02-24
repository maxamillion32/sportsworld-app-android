package mx.com.sportsworld.sw.fragment;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
import mx.com.sportsworld.sw.activity.events.ClickEventInterface;
import mx.com.sportsworld.sw.fragment.dialog.FragmentDialogTour;

// TODO: Auto-generated Javadoc

/**
 * The Class SportsWorldSettingsFragment.
 */
public class SportsWorldSettingsFragment extends SherlockFragment implements
		View.OnClickListener, ClickEventInterface {

	/** The m btn edit profile. */
	private Button mBtnEditProfile;

	/** The m btn enable social networks. */
	private Button mBtnEnableSocialNetworks;

	/** The m btn log out. */
	private Button mBtnLogOut;
	private Button btn_ayuda;

	/** The m listener. */
	private OnSettingsClickListener mListener;
	/** The Tour fragment. */
	public static FragmentDialogTour tourFragment;

	/**
	 * Instantiates a new sports world settings fragment.
	 */
	public SportsWorldSettingsFragment() {
	}

	/**
	 * New instance.
	 * 
	 * @return the sports world settings fragment
	 */
	public static SportsWorldSettingsFragment newInstance() {
		return new SportsWorldSettingsFragment();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockFragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnSettingsClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.getClass().getName()
					+ " must implement "
					+ OnSettingsClickListener.class.getName() + ".");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View view = inflater.inflate(
				R.layout.fragment_sports_world_settings, container, false);
		mBtnEditProfile = (Button) view.findViewById(R.id.btn_edit_profile);

		if (new SportsWorldAccountManager(getActivity()).isLoggedAsAnonymous()) {
			mBtnEditProfile.setVisibility(View.GONE);
		} else {
			mBtnEditProfile.setOnClickListener(this);
		}

		mBtnEnableSocialNetworks = (Button) view
				.findViewById(R.id.btn_enable_social_networks);
		mBtnEnableSocialNetworks.setOnClickListener(this);

		btn_ayuda = (Button) view.findViewById(R.id.btn_ayuda);
		btn_ayuda.setOnClickListener(this);

		mBtnLogOut = (Button) view.findViewById(R.id.btn_logOut_profile);
		mBtnLogOut.setOnClickListener(this);

		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.btn_edit_profile:
			mListener.onEditProfileClick();
			break;

		case R.id.btn_enable_social_networks:
			mListener.onEnableSocialNetworksClick();
			break;

		case R.id.btn_logOut_profile:
			mListener.onLogOutClick();
			break;
		case R.id.btn_ayuda:
			createTourDialog();
			break;
		default:
			throw new UnsupportedOperationException();

		}
	}

	/**
	 * The listener interface for receiving onSettingsClick events. The class
	 * that is interested in processing a onSettingsClick event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's
	 * <code>addOnSettingsClickListener<code> method. When
	 * the onSettingsClick event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see OnSettingsClickEvent
	 */
	public static interface OnSettingsClickListener {

		/**
		 * On edit profile click.
		 */
		void onEditProfileClick();

		/**
		 * On enable social networks click.
		 */
		void onEnableSocialNetworksClick();

		/**
		 * On log out click.
		 */
		void onLogOutClick();
	}

	/**
	 * Welcome dialog.
	 */
	public void createTourDialog() {
		tourFragment = FragmentDialogTour.newInstance();
		tourFragment.setStyle(DialogFragment.STYLE_NO_TITLE,
				android.R.style.Theme_Black_NoTitleBar);
		tourFragment.setClickInterface(this);
		tourFragment.show(getFragmentManager(), "tourDialog");

	}

	@Override
	public void onCustomClickListener() {
		// TODO Auto-generated method stub
		if (tourFragment != null)
			tourFragment.dismiss();
	}
}
