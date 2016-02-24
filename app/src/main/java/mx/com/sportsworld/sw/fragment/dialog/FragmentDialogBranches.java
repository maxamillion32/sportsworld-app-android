package mx.com.sportsworld.sw.fragment.dialog;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import mx.com.sportsworld.sw.R;
import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.pojo.BranchItemPojo;
import mx.com.sportsworld.sw.pojo.BranchPojo;
import mx.com.sportsworld.sw.provider.SportsWorldContract;
import mx.com.sportsworld.sw.service.MarkBranchAsFavoriteService;
import mx.com.sportsworld.sw.web.task.ClubTask;

// TODO: Auto-generated Javadoc

/**
 * The Class FragmentDialogDetail.
 */
public class FragmentDialogBranches extends DialogFragment {

	/** The Constant LOADER_ID_BRANCHES. */
	private static final int LOADER_ID_BRANCHES = 0;

	/** The Constant REQUEST_CODE_UPDATE_BRANCH. */
	private static final int REQUEST_CODE_MARK_BRANCH_AS_FAVORITE = 1;

	/** The Constant COL_INDEX_BRANCH_NAME. */
	private static final int COL_INDEX_BRANCH_NAME = 0;

	/** The Constant COL_INDEX_BRANCH_ID. */
	private static final int COL_INDEX_BRANCH_ID = 1;

	/** The m adapter. */
	private ArrayAdapter<BranchItemPojo> mAdapter;

	/** The Constant COLS. */
	private static final String[] COLS = buildColumns();
	/** The nombre. */
	private ArrayList<String> nombre;

	/** The Constant TO. */
	private static final int[] TO = new int[] { android.R.id.text1 };

	private ListView list_view_brances;

	private Cursor loadedCursor;

	/** The progress. */
	private ProgressDialogFragment progress;

	/** The dialgo fragment. */
	Fragment dialgoFragment;

	public static FragmentDialogBranches newInstance() {

		return new FragmentDialogBranches();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_dialog_branches, null);

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		list_view_brances = (ListView) view
				.findViewById(R.id.list_view_brances);

		list_view_brances.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				startMarkBranchAsFavoriteService(mAdapter.getItem(position)
						.getmUnId());
				startMarkBranchAsFavoriteService(mAdapter.getItem(position)
						.getmUnId());
				getDialog().dismiss();
			}
		});

		// mAdapter = new SimpleCursorAdapter(getActivity() /* context */,
		// R.layout.list_item_branch, null /* cursor */, COLS, TO, 0 /* flags
		// */);
		//
		// list_view_brances.setAdapter(mAdapter);

		// getLoaderManager().initLoader(LOADER_ID_BRANCHES,
		// null /* loaderArgs */, this /* loaderCallbacks */);
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
					mAdapter = new ArrayAdapter<BranchItemPojo>(
							getActivity() /* context */,
							android.R.layout.simple_list_item_1,
							android.R.id.text1, resPojo.getListBranch());
				//TextView textView=(TextView) view.findViewById(android.R.id.text1);
				list_view_brances.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				progress.dismiss();
			}
		}, getActivity());
		task.execute(pojo);

		return view;
	}

	/**
	 * Builds the columns.
	 * 
	 * @return the string[]
	 */
	private static String[] buildColumns() {

		final SparseArray<String> colsMap = new SparseArray<String>();
		colsMap.put(COL_INDEX_BRANCH_NAME, SportsWorldContract.Branch.NAME);
		colsMap.put(COL_INDEX_BRANCH_ID, SportsWorldContract.Branch._ID);

		final int colCount = colsMap.size();
		final String[] cols = new String[colCount];
		for (int i = 0; i < colCount; i++) {
			cols[i] = colsMap.get(i);
		}

		return cols;

	}

	// @Override
	// public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
	// // TODO Auto-generated method stub
	// return new CursorLoader(getActivity() /* context */,
	// SportsWorldContract.Branch.CONTENT_URI, COLS,
	// SportsWorldContract.Branch.TYPE + " = ? ",
	// new String[] { Branch.TYPE_DF },
	// SportsWorldPreferences.getSortListBranchPref(getActivity()
	// .getApplicationContext()));
	// }
	//
	// @Override
	// public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
	// // TODO Auto-generated method stub
	// loadedCursor = cursor;
	// mAdapter.swapCursor(cursor);
	// }
	//
	// @Override
	// public void onLoaderReset(Loader<Cursor> arg0) {
	// // TODO Auto-generated method stub
	// mAdapter.swapCursor(null);
	// }

	/**
	 * Start mark branch as favorite service.
	 */
	private void startMarkBranchAsFavoriteService(long idBranch) {

		final Intent data = new Intent();
		final Activity activity = getActivity();
		final PendingIntent pendingIntent = activity.createPendingResult(
				REQUEST_CODE_MARK_BRANCH_AS_FAVORITE, data, 0 /* flags */);
		final Intent markBranchAsFavoriteService = new Intent(activity,
				MarkBranchAsFavoriteService.class);
		markBranchAsFavoriteService.putExtra(
				MarkBranchAsFavoriteService.EXTRA_BRANCH_ID, idBranch);
		markBranchAsFavoriteService.putExtra(
				MarkBranchAsFavoriteService.EXTRA_FAVORITE, true);
		markBranchAsFavoriteService
				.putExtra(MarkBranchAsFavoriteService.EXTRA_PENDING_INTENT,
						pendingIntent);
		activity.startService(markBranchAsFavoriteService);
	}

	private long getIdFromCursor(int position) {
		long id = 0;
		int cursorPosition = 0;
		if (loadedCursor.moveToFirst()) {
			do {
				if (position == cursorPosition) {
					id = loadedCursor.getLong(COL_INDEX_BRANCH_ID);
					break;
				} else
					cursorPosition++;
			} while (loadedCursor.moveToNext());
		}
		return id;
	}

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
}
