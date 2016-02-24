package mx.com.sportsworld.sw.fragment;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.events.EmptyClubsInterface;
import mx.com.sportsworld.sw.activity.events.FavoriteBranchInterface;
import mx.com.sportsworld.sw.adapter.EmptyFavoritesAdapter;
import mx.com.sportsworld.sw.adapter.SpecificBranchTypeListFragmentAdapter;
import mx.com.sportsworld.sw.fragment.dialog.ProgressDialogFragment;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.provider.SportsWorldContract;
import mx.com.sportsworld.sw.service.MarkBranchAsFavoriteService;

// TODO: Auto-generated Javadoc

/**
 * The Class SpecificBranchTypeListFragment.
 */
public class SpecificBranchTypeListFragment extends SherlockListFragment
		implements LoaderManager.LoaderCallbacks<Cursor>, FavoriteBranchInterface {

	/** The Constant LOADER_ID_BRANCHES. */
	private static final int LOADER_ID_BRANCHES = 0;
	
	/** The Constant COL_INDEX_BRANCH_NAME. */
	private static final int COL_INDEX_BRANCH_NAME = 0;
	
	/** The Constant COL_INDEX_BRANCH_ID. */
	private static final int COL_INDEX_BRANCH_ID = 2;
	
	/** The Constant COL_INDEX_BRANCH_DISTANCE. */
	private static final int COL_INDEX_BRANCH_DISTANCE = 1;
	
	private int REQUEST_CODE_REMOVE_MARK_BRANCH_AS_FAVORITE = 2;
	private static final int REQUEST_CODE_MARK_BRANCH_AS_FAVORITE = 1;
	/** The Constant COLS. */
	private static final String[] COLS = buildColumns();
	
	/** The Constant TO. */
	private static final int[] TO = new int[] { android.R.id.text1,
			android.R.id.text2 };
	
	/** The Constant FRAG_ARG_TYPE. */
	private static final String FRAG_ARG_TYPE = "frag_arg_type";
	
	/** The m adapter. */
	public static SimpleCursorAdapter mAdapter;
	/** The m adapter. */
	//private ArrayAdapter<Branch> mAdapter;
	
	/** The m listener. */
	private OnBranchClickListener mListener;
	
	/** The act. */
	public static Activity act;
	
	/** The favorite. */
	public static boolean favorite = false;
	private EmptyClubsInterface emptyEvent;
	public Cursor cursor_source;
	public FavoriteBranchInterface interfaceFavorite;
	Fragment dialgoFragment;
	/** The progress. */
	private ProgressDialogFragment progress;
	public static boolean activo =false;
	public FavoriteBranchInterface eliminar;
	/**
	 * Builds the columns.
	 *
	 * @return the string[]
	 */
	private static String[] buildColumns() {

		final SparseArray<String> colsMap = new SparseArray<String>();
		colsMap.put(COL_INDEX_BRANCH_NAME, SportsWorldContract.Branch.NAME);
		colsMap.put(COL_INDEX_BRANCH_DISTANCE,
				SportsWorldContract.Branch.DISTANCE);
		colsMap.put(COL_INDEX_BRANCH_ID, SportsWorldContract.Branch._ID);

		final int colCount = colsMap.size();
		final String[] cols = new String[colCount];
		for (int i = 0; i < colCount; i++) {
			cols[i] = colsMap.get(i);
		}
		Log.d("Log Sistemas", "resultado " + cols);
		return cols;

	}

	/**
	 * Instantiates a new specific branch type list fragment.
	 */
	public SpecificBranchTypeListFragment() {
	}

	/**
	 * New instance.
	 *
	 * @param type the type
	 * @return the specific branch type list fragment
	 */
	public static SpecificBranchTypeListFragment newInstance(String type,FavoriteBranchInterface fvInterFace) {
		final SpecificBranchTypeListFragment f = new SpecificBranchTypeListFragment();
		final Bundle args = new Bundle();
		f.eliminar = fvInterFace;
		args.putString(FRAG_ARG_TYPE, type);
		if (type.equals("favorites"))
			favorite = true;
		else
			favorite = false;

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
			mListener = (OnBranchClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.getClass().getName()
					+ " must implement "
					+ OnBranchClickListener.class.getName() + ".");
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListShown(false);
		if (favorite){
			

			changeAdapter(true);
		}else{
		
		mAdapter = new SimpleCursorAdapter(getActivity() /* context */,
				R.layout.list_item_clubs, null /* cursor */, COLS, TO, 0 /* flags */);
		setListAdapter(mAdapter);
		setListShown(false);
		if (!favorite)
			setEmptyText(getString(R.string.empty_specific_type_branches));
        /*if(mAdapter.getCount() == 0){
			Context context = getActivity().getApplicationContext();
			CharSequence text = (getString(R.string.empty_specific_type_branches));
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}*/
		getLoaderManager().initLoader(LOADER_ID_BRANCHES,
				null /* loaderArgs */, this /* loaderCallbacks */);
		}
	}

	/**
	 * Gets the type from arguments.
	 *
	 * @return the type from arguments
	 */
	private String getTypeFromArguments() {
		return getArguments().getString(FRAG_ARG_TYPE);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle loaderArgs) {

		return new CursorLoader(getActivity() /* context */,
				SportsWorldContract.Branch.CONTENT_URI, COLS,
				SportsWorldContract.Branch.TYPE + " = ?",
				new String[] { getTypeFromArguments() },
				SportsWorldPreferences.getSortListBranchPref(getActivity()
						.getApplicationContext()));
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.swapCursor(null);
		//cursor.moveToFirst();
		Log.d("Cerrado ", "cursor" + cursor.isClosed());
		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}
		
		if(favorite)
		if (cursor.getCount() == 0){
			EmptyFavoritesAdapter adapterDummy = new EmptyFavoritesAdapter(getActivity());
			adapterDummy.add("Elemento dummy");
			setListAdapter(adapterDummy);
			emptyEvent.onBranchesEmty(true);
			getListView().setDivider(null);
			getListView().setFocusable(false);
			
			
		}else{
			emptyEvent.onBranchesEmty(false);
			setListAdapter(mAdapter);
			getListView().setDivider(
					getActivity().getResources().getDrawable(
							android.R.drawable.divider_horizontal_bright));
			
		}
		mAdapter.notifyDataSetChanged();
		mAdapter.swapCursor(cursor);
		cursor_source = cursor;
		
		Log.d("Cerrado ", "cursor" + cursor.isClosed());
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

		mAdapter.swapCursor(null);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		mListener.onBranchClick(id);
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
	/**
	 * The listener interface for receiving onBranchClick events.
	 * The class that is interested in processing a onBranchClick
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnBranchClickListener<code> method. When
	 * the onBranchClick event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnBranchClickEvent
	 */
	public interface OnBranchClickListener {
		
		/**
		 * On branch click.
		 *
		 * @param id the id
		 */
		void onBranchClick(long id);
	}
	public void refreshListBranches() {
		mAdapter.notifyDataSetChanged();
		mAdapter.getCursor().requery();
		mAdapter.getCursor().deactivate();
		onLoadFinished(new CursorLoader(getActivity() /* context */,
				SportsWorldContract.Branch.CONTENT_URI, COLS,
				SportsWorldContract.Branch.TYPE + " = ?",
				new String[] { getTypeFromArguments() },
				SportsWorldPreferences.getSortListBranchPref(getActivity()
						.getApplicationContext())), mAdapter.getCursor());
	}
	/**
	 * On sort changed.
	 */
	public void onSortChanged() {
		getLoaderManager().restartLoader(0, null, this);
	}
	public void setOnBranchListener(EmptyClubsInterface mEvent) {
		emptyEvent = mEvent;

	}

	public void changeAdapter(boolean isNormal) {
		
		if (isNormal){
			mAdapter = new SimpleCursorAdapter(getActivity() /* context */,
					R.layout.list_item_clubs, null /* cursor */, COLS, TO, 0 /* flags */);
		}
		else{
			if(cursor_source == null){
				cursor_source = mAdapter.swapCursor(cursor_source);
			}
			mAdapter = new SpecificBranchTypeListFragmentAdapter(getActivity(),R.layout.list_item_clubs, cursor_source /* cursor */, COLS, TO, 0 /* flags */,this);
		}
		
		setListAdapter(mAdapter);
		getLoaderManager().initLoader(0 /* loaderId */, null /* loaderArgs */,
				this /* loaderCallback */);
	}
	
	@Override
	public void removeItem(long itemId) {
		// TODO Auto-generated method stub
		removeMarkBranchAsFavoriteService(itemId);
		eliminar.removeItem(itemId); 
		
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
		
		mAdapter.notifyDataSetChanged();
		mAdapter.getCursor().requery();
		mAdapter.getCursor().deactivate();
		
	}
	
}
