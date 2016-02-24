package mx.com.sportsworld.sw.fragment;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.app.SherlockWorkingListFragment;
import mx.com.sportsworld.sw.utils.ConnectionUtils;

// TODO: Auto-generated Javadoc

/**
 * The Class RouteFragment.
 */
public class RouteFragment extends SherlockWorkingListFragment implements
		View.OnClickListener {
	
	/** The Constant HAMBURG. */
	static final LatLng HAMBURG = new LatLng(53.558, 9.927);
	
	/** The Constant KIEL. */
	static final LatLng KIEL = new LatLng(53.551, 9.993);

	/**
	 * New instance.
	 *
	 * @return the route fragment
	 */
	public static final RouteFragment newInstance() {
		return new RouteFragment();
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.app.SherlockWorkingListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.fragment_map_route,
				null /* root */, false /* attachToRoot */);

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
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
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

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
