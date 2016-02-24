package mx.com.sportsworld.sw.service;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;

import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
import mx.com.sportsworld.sw.model.Branch;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.resource.BranchResource;
import mx.com.sportsworld.sw.parser.BranchParser;
import mx.com.sportsworld.sw.provider.BatchOperation;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

/**
 * 
 * Fetches nearby branches.
 * 
 */
public class FetchBranchService extends SportsWorldWebApiService<Branch> {

	/** The Constant EXTRA_LOCATION_LATITUDE. */
	public static final String EXTRA_LOCATION_LATITUDE = "com.upster.extra.LOCATION_LATITUDE";
	
	/** The Constant EXTRA_LOCATION_LONGITUDE. */
	public static final String EXTRA_LOCATION_LONGITUDE = "com.upster.extra.LOCATION_LONGITUDE";

	/**
	 * Instantiates a new fetch branch service.
	 */
	public FetchBranchService() {
		super(FetchBranchService.class.getName());
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.service.SportsWorldWebApiService#makeApiCall(android.content.Intent)
	 */
	@Override
	public RequestResult<Branch> makeApiCall(Intent intent) throws IOException,
			JSONException {
		final double latitude = intent.getDoubleExtra(EXTRA_LOCATION_LATITUDE,
				-1d);
		final double longitude = intent.getDoubleExtra(
				EXTRA_LOCATION_LONGITUDE, -1d);

		final SportsWorldAccountManager accountMngr = getAccountManager();
		final boolean isLoggedInAsMember = accountMngr.isLoggedInAsMember();
		final long serverUserId = isLoggedInAsMember ? Long.valueOf(accountMngr
				.getCurrentUserId()) : 0L;

		return BranchResource.fetchBranches(getApplicationContext(), latitude,
				longitude, serverUserId);
	}

	/* (non-Javadoc)
	 * @see com.sportsworld.android.service.SportsWorldWebApiService#proccessRequestResult(android.content.Intent, com.sportsworld.android.net.RequestResultParser.RequestResult)
	 */
	@Override
	public boolean proccessRequestResult(Intent intent,
			RequestResult<Branch> result) {

		final List<Branch> branchesOnServer = result.getData();

		if (branchesOnServer == null) {
			maySendResult(intent, Activity.RESULT_CANCELED, result,
					ERROR_CONNECTION);
			return false;
		}

		if (branchesOnServer.size() == 0) {
			return true;
		}

		final ContentResolver cr = getContentResolver();
		final BatchOperation batchOperation = new BatchOperation(
				SportsWorldContract.CONTENT_AUTHORITY, cr);

		final ContentProviderOperation markForDeleteOperation;
		final boolean isLoggedInAsMember = getAccountManager()
				.isLoggedInAsMember();
		if (isLoggedInAsMember) {

			/*
			 * We'll mark every branch for later deletion.
			 */
			markForDeleteOperation = ContentProviderOperation
					.newUpdate(SportsWorldContract.Branch.CONTENT_URI)
					.withValue(SportsWorldContract.Branch.WILL_DELETE, true)
					.build();
		} else {

			/*
			 * We'll mark branch that are not of favorite type for later
			 * deletion because we don't get these from server. We'll manage
			 * favorite branches locally.
			 */
			markForDeleteOperation = ContentProviderOperation
					.newUpdate(SportsWorldContract.Branch.CONTENT_URI)
					.withValue(SportsWorldContract.Branch.WILL_DELETE, true)
					.withSelection(SportsWorldContract.Branch.TYPE + " != ?",
							new String[] { Branch.TYPE_FAVORITE }).build();
		}

		batchOperation.add(markForDeleteOperation);

		batchOperation.execute();

		/*
		 * Get all branch ids. We'll get them as strings to make more readable
		 * the binary search
		 */

		String[] idsOnLocal = null;
		Cursor cursor = null;
		try {
			cursor = cr.query(SportsWorldContract.Branch.CONTENT_URI,
					new String[] { SportsWorldContract.Branch._ID,
							SportsWorldContract.Branch.TYPE },
					SportsWorldContract.Branch.TYPE + " != ?",
					new String[] { Branch.TYPE_FAVORITE },
					SportsWorldContract.Branch._ID + " ASC");

			if (cursor != null) {
				final int count = cursor.getCount();
				idsOnLocal = new String[count];
				for (int i = 0; i < count; i++) {
					cursor.moveToPosition(i);
					idsOnLocal[i] = cursor.getString(0);
				}

			}

		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		/* We will update even if a branch has not changed at all */

		final ContentValues values = new ContentValues();
		final BranchParser parser = new BranchParser();

		if (idsOnLocal == null || idsOnLocal.length == 0) {

			/* Clean environment. Just insert everything. */

			final int branchCount = branchesOnServer.size();
			for (int i = 0; i < branchCount; i++) {
				final Branch item = branchesOnServer.get(i);
				
				parser.parse(item, values /* contentValue */);
				final ContentProviderOperation insertOperation = ContentProviderOperation
						.newInsert(SportsWorldContract.Branch.CONTENT_URI)
						.withYieldAllowed(true).withValues(values).build();
				batchOperation.add(insertOperation);
				values.clear();
			}

		} else {

			final int branchCount = branchesOnServer.size();
			for (int i = 0; i < branchCount; i++) {

				final Branch branchOnServer = branchesOnServer.get(i);


					/* Insert */

					parser.parse(branchOnServer, values /* contentValue */);
					final ContentProviderOperation insertOperation = ContentProviderOperation
							.newInsert(SportsWorldContract.Branch.CONTENT_URI)
							.withYieldAllowed(true).withValues(values).build();
					batchOperation.add(insertOperation);
					values.clear();


			}

		}

		/*
		 * Delete every branch that was not updated or inserted. (This means
		 * that the server has deleted these branches, thus we should delete
		 * them on our local copy).
		 */

		batchOperation.execute();

		final ContentProviderOperation deleteOperation = ContentProviderOperation
				.newDelete(SportsWorldContract.Branch.CONTENT_URI)
				.withSelection(SportsWorldContract.Branch.WILL_DELETE + "=?",
						new String[] { "1" }).build();
		batchOperation.add(deleteOperation);

		batchOperation.execute();
		return true;
	}

}
