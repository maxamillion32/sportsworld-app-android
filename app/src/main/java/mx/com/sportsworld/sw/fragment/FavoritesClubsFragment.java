package mx.com.sportsworld.sw.fragment;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;

import com.actionbarsherlock.app.SherlockFragment;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.events.ClickEventInterface;
import mx.com.sportsworld.sw.fragment.dialog.FragmentDialogTour;
import mx.com.sportsworld.sw.loader.PolicyLoader;
import mx.com.sportsworld.sw.model.Policy;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;

// TODO: Auto-generated Javadoc

/**
 * Show Policies and let the user accept them or take a tour (watch some images
 * of the app).
 */
public class FavoritesClubsFragment extends SherlockFragment implements
		View.OnClickListener, CompoundButton.OnCheckedChangeListener,
		LoaderManager.LoaderCallbacks<RequestResult<Policy>>,
		ClickEventInterface {

	/** The Constant FRAG_ARG_ONLY_SHOW_POLICIES. */
	private static final String FRAG_ARG_ONLY_SHOW_POLICIES = "frag_tag_only_show_policies";

	/** The Constant LOADER_ID_POLICIES. */
	private static final int LOADER_ID_POLICIES = 0;

	/** The Constant STATE_PROGRESS_CONTAINER_SHOWN. */
	private static final String STATE_PROGRESS_CONTAINER_SHOWN = "state_progress_container_shown";

	/** The Constant STATE_ERROR_CONTAINER_SHOWN. */
	private static final String STATE_ERROR_CONTAINER_SHOWN = "state_error_container_shown";

	/** The Constant POLICIES_MIME_TYPE. */
	private static final String POLICIES_MIME_TYPE = "text/html; charset=utf-8";

	/** The Constant POLICIES_ENCODING. */
	private static final String POLICIES_ENCODING = "base64";

	/** The Constant POLICIES_HTML_SEPARATOR. */
	private static final String POLICIES_HTML_SEPARATOR = "<br/><br/>";

	/** The m chc agree. */
	//private CheckBox mChcAgree;

	/** The m btn take tour. */
	//private Button mBtnTakeTour;

	/** The m btn continue. */
	private Button mBtnContinue;

	/** The m btn try again. */
	private Button mBtnTryAgain;

	/** The m wbv policies. */
	private WebView mWbvPolicies;

	/** The m listener. */
	private PolicyEventListener mListener;

	/** The m lnr progress container. */
	private View mLnrProgressContainer;

	/** The m rlt error container. */
	private View mRltErrorContainer;

	/** The m lnr options. */
	private View mLnrOptions;

	/** The m progress container shown. */
	private boolean mProgressContainerShown;

	/** The m web view shown. */
	private boolean mWebViewShown;

	/** The m error container shown. */
	private boolean mErrorContainerShown;

	/** The m handler. */
	final private Handler mHandler = new Handler();

	/** The new fragment. */
	public static FragmentDialogTour tourFragment;

	/** The m request focus. */
	final private Runnable mRequestFocus = new Runnable() {
		public void run() {
			mWbvPolicies.focusableViewAvailable(mWbvPolicies);
		}
	};

	/**
	 * Instantiates a new policies fragment.
	 */
	public FavoritesClubsFragment() {
	}

	/**
	 * New instance.
	 * 
	 * @param onlyShowPolicies
	 *            the only show policies
	 * @return the policies fragment
	 */
	public static FavoritesClubsFragment newInstance(boolean onlyShowPolicies) {
		final FavoritesClubsFragment f = new FavoritesClubsFragment();
		final Bundle args = new Bundle();
		args.putBoolean(FRAG_ARG_ONLY_SHOW_POLICIES, onlyShowPolicies);
		f.setArguments(args);
		return f;
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
			mListener = (PolicyEventListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.getClass().getName()
					+ " must implement " + PolicyEventListener.class.getName()
					+ ".");
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

		final View view = inflater.inflate(R.layout.fragment_policies,
				container, false /* attachToRoot */);

		mLnrOptions = view.findViewById(R.id.footer);

		//mBtnTakeTour = (Button) view.findViewById(R.id.btn_take_tour);
		mBtnContinue = (Button) view.findViewById(R.id.btn_continue);

		if (getOnlyShowPoliciesFromArgs()) {
			mLnrOptions.setVisibility(View.GONE);
		} else {
			//mChcAgree.setOnCheckedChangeListener(this);
			//mBtnTakeTour.setOnClickListener(this);
			mBtnContinue.setOnClickListener(this);
		}

		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View,
	 * android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ensureWebView();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState != null) {
			tourFragment = (FragmentDialogTour) getFragmentManager()
					.findFragmentByTag("tourDialog");

			final boolean progressContainerShown = savedInstanceState
					.getBoolean(STATE_PROGRESS_CONTAINER_SHOWN);
			final boolean errorContainerShown = savedInstanceState
					.getBoolean(STATE_ERROR_CONTAINER_SHOWN);
			if (progressContainerShown) {
				showLoadingProgress(false /* animate */);
			} else if (errorContainerShown) {
				showErrorView(false /* animate */);
			}
		} else {
			showLoadingProgress(false /* animate */);
		}

		getLoaderManager().initLoader(LOADER_ID_POLICIES,
				null /* loaderArgs */, this /* loaderCallback */);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(STATE_PROGRESS_CONTAINER_SHOWN,
				mProgressContainerShown);
		outState.putBoolean(STATE_ERROR_CONTAINER_SHOWN, mErrorContainerShown);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		mHandler.removeCallbacks(mRequestFocus);
		mWbvPolicies = null;
		mWebViewShown = false;
		mLnrProgressContainer = null;
		mRltErrorContainer = null;
		super.onDestroyView();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int,
	 * android.os.Bundle)
	 */
	@Override
	public Loader<RequestResult<Policy>> onCreateLoader(int id, Bundle args) {
		return new PolicyLoader(getActivity());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android
	 * .support.v4.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<RequestResult<Policy>> loader,
			RequestResult<Policy> result) {

		if (result == null
				|| result.getResponseCode() != HttpURLConnection.HTTP_OK
				|| !result.isSuccesful()) {
			showErrorView(isResumed() /* animate */);
			return;
		}

		mWbvPolicies.loadData(buildPoliciesHtml(result.getData()),
				POLICIES_MIME_TYPE, POLICIES_ENCODING);
		//mChcAgree.setEnabled(true);
		//mBtnTakeTour.setVisibility(View.VISIBLE);

		showWebView(isResumed() /* animate */);

	}

	/**
	 * Builds the policies html.
	 * 
	 * @param policies
	 *            the policies
	 * @return The web page as a Base64 string or null if the utf-8 encoding is
	 *         not supported.
	 */
	private String buildPoliciesHtml(List<Policy> policies) {

		int totalLength = 0;
		final int count = policies.size();
		final String[] contents = new String[count];
		for (int i = 0; i < count; i++) {
			contents[i] = policies.get(i).getContent();
			totalLength += contents[i].length();
		}
		totalLength += (count * POLICIES_HTML_SEPARATOR.length());
		final StringBuilder strBuilder = new StringBuilder(totalLength);

		boolean first = true;
		for (int i = 0; i < count; i++) {
			strBuilder.append(contents[i]);
			if (first) {
				first = false;
				continue;
			} else {
				strBuilder.append(POLICIES_HTML_SEPARATOR);
			}
		}

		try {
			return Base64.encodeToString(strBuilder.toString()
					.getBytes("UTF-8"), android.util.Base64.DEFAULT);
		} catch (UnsupportedEncodingException ignore) {
			/* Should not happen */
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android
	 * .support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<RequestResult<Policy>> policies) {
		/* do nothing */
	}

	/**
	 * Gets the only show policies from args.
	 * 
	 * @return the only show policies from args
	 */
	private boolean getOnlyShowPoliciesFromArgs() {
		return getArguments().getBoolean(FRAG_ARG_ONLY_SHOW_POLICIES);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged
	 * (android.widget.CompoundButton, boolean)
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		mBtnContinue.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
/*
		case R.id.btn_take_tour:
			// mListener.onTakeTourClick();
			welcomeDialog();
			break;
*/
		case R.id.btn_continue:
			agreeWithPolicies();
			break;

		case R.id.btn_try_again:
			retryFetch();
			break;

		default:
			throw new UnsupportedOperationException("Unsupported operation");

		}
	}

	/**
	 * Agree with policies.
	 */
	private void agreeWithPolicies() {
		mListener.onAgree();
	}

	/**
	 * Retry fetch.
	 */
	private void retryFetch() {
		showLoadingProgress(isResumed() /* animate */);
		getLoaderManager().restartLoader(LOADER_ID_POLICIES,
				null /* loaderArgs */, this /* loaderCallback */);
	}

	/**
	 * The listener interface for receiving policyEvent events. The class that
	 * is interested in processing a policyEvent event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's
	 * <code>addPolicyEventListener<code> method. When
	 * the policyEvent event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see PolicyEventEvent
	 */
	public static interface PolicyEventListener {

		/**
		 * On take tour click.
		 */
		void onTakeTourClick();

		/**
		 * On agree.
		 */
		void onAgree();
	}

	/**
	 * Show web view.
	 * 
	 * @param animate
	 *            the animate
	 */
	private void showWebView(boolean animate) {

		ensureWebView();

		if (mWebViewShown) {
			return;
		}

		mWebViewShown = true;

		if (animate) {

			mRltErrorContainer.startAnimation(AnimationUtils.loadAnimation(
					getActivity(), android.R.anim.fade_in));

			if (mProgressContainerShown) {
				mLnrProgressContainer.startAnimation(AnimationUtils
						.loadAnimation(getActivity(), android.R.anim.fade_out));
			} else {
				mLnrProgressContainer.clearAnimation();
			}

			if (mErrorContainerShown) {
				mRltErrorContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_out));
			} else {
				mRltErrorContainer.clearAnimation();
			}

		} else {
			mLnrProgressContainer.clearAnimation();
			mRltErrorContainer.clearAnimation();
		}

		mErrorContainerShown = false;
		mProgressContainerShown = false;

		mLnrProgressContainer.setVisibility(View.GONE);
		mRltErrorContainer.setVisibility(View.GONE);
		mWbvPolicies.setVisibility(View.VISIBLE);

	}

	/**
	 * Show error view.
	 * 
	 * @param animate
	 *            the animate
	 */
	private void showErrorView(boolean animate) {
		ensureWebView();
		if (mErrorContainerShown) {
			return;
		}

		mErrorContainerShown = true;

		if (animate) {

			mRltErrorContainer.startAnimation(AnimationUtils.loadAnimation(
					getActivity(), android.R.anim.fade_in));

			if (mProgressContainerShown) {
				mLnrProgressContainer.startAnimation(AnimationUtils
						.loadAnimation(getActivity(), android.R.anim.fade_out));
			} else {
				mLnrProgressContainer.clearAnimation();
			}

			if (mWebViewShown) {
				mWbvPolicies.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_out));
			} else {
				mWbvPolicies.clearAnimation();
			}

		} else {
			mLnrProgressContainer.clearAnimation();
			mWbvPolicies.clearAnimation();
		}

		mWebViewShown = false;
		mProgressContainerShown = false;

		mLnrProgressContainer.setVisibility(View.GONE);
		mRltErrorContainer.setVisibility(View.VISIBLE);
		mWbvPolicies.setVisibility(View.GONE);

	}

	/**
	 * Show loading progress.
	 * 
	 * @param animate
	 *            the animate
	 */
	private void showLoadingProgress(boolean animate) {

		ensureWebView();

		if (mProgressContainerShown) {
			return;
		}

		mProgressContainerShown = true;

		if (animate) {

			mLnrProgressContainer.startAnimation(AnimationUtils.loadAnimation(
					getActivity(), android.R.anim.fade_in));

			if (mErrorContainerShown) {
				mRltErrorContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_out));
			} else {
				mRltErrorContainer.clearAnimation();
			}

			if (mWebViewShown) {
				mWbvPolicies.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_out));
			} else {
				mWbvPolicies.clearAnimation();
			}

		} else {
			mWbvPolicies.clearAnimation();
			mRltErrorContainer.clearAnimation();
		}

		mWebViewShown = false;
		mErrorContainerShown = false;

		mLnrProgressContainer.setVisibility(View.VISIBLE);
		mRltErrorContainer.setVisibility(View.GONE);
		mWbvPolicies.setVisibility(View.GONE);
	}

	/**
	 * Ensure web view.
	 */
	private void ensureWebView() {
		if (mWbvPolicies != null) {
			return;
		}
		View root = getView();
		if (root == null) {
			throw new IllegalStateException("Content view not yet created");
		}

		mLnrProgressContainer = root.findViewById(R.id.lnr_progress_container);
		mRltErrorContainer = root.findViewById(R.id.rlt_on_error_container);
		mBtnTryAgain = (Button) root.findViewById(R.id.btn_try_again);
		mBtnTryAgain.setOnClickListener(this);

		mWbvPolicies = (WebView) root.findViewById(R.id.wbv_policies);

		mWebViewShown = true;
		if (mLnrProgressContainer != null) {
			showWebView(false /* animate */);
		}

		mHandler.post(mRequestFocus);
	}

	/**
	 * Welcome dialog.
	 */
	public void welcomeDialog() {
		tourFragment = FragmentDialogTour.newInstance();
		tourFragment.setStyle(DialogFragment.STYLE_NO_TITLE,
				android.R.style.Theme_Black_NoTitleBar);
		tourFragment.setClickInterface(this);
		tourFragment.show(getFragmentManager(), "tourDialog");

	}

	@Override
	public void onCustomClickListener() {
		// TODO Auto-generated method stub
		tourFragment.dismiss();
	}

}
