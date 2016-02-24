package mx.com.sportsworld.sw.service;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
import mx.com.sportsworld.sw.model.Branch;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.resource.BranchResource;
import mx.com.sportsworld.sw.provider.BatchOperation;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

/**
 * The Class GetBranchOverviewService.
 */
public class GetBranchOverviewService extends SportsWorldWebApiService<Branch> {

    /** The Constant EXTRA_BRANCH_ID. */
    public static final String EXTRA_BRANCH_ID = "com.upster.extra.BRANCH_ID";

    /**
	 * Instantiates a new gets the branch overview service.
	 */
    public GetBranchOverviewService() {
        super(GetBranchOverviewService.class.getName());
    }

    /* (non-Javadoc)
     * @see com.sportsworld.android.service.SportsWorldWebApiService#makeApiCall(android.content.Intent)
     */
    @Override
    public RequestResult<Branch> makeApiCall(Intent intent) throws IOException, JSONException {

        final long clubId = intent.getLongExtra(EXTRA_BRANCH_ID, -1L);

        if (clubId == -1) {
            throw new IllegalArgumentException("You must pass ShowBranchService.EXTRA_CLUB_ID");
        }

        final SportsWorldAccountManager accountMngr = getAccountManager();
        return BranchResource.getBranchOverview(getApplicationContext(), clubId,
                Long.parseLong(accountMngr.getCurrentUserId()));

    }

    /* (non-Javadoc)
     * @see com.sportsworld.android.service.SportsWorldWebApiService#proccessRequestResult(android.content.Intent, com.sportsworld.android.net.RequestResultParser.RequestResult)
     */
    @Override
    public boolean proccessRequestResult(Intent intent, RequestResult<Branch> result) {
        final List<Branch> branches = result.getData();
        Log.d("branches ", "list  = " + branches);
        if (branches == null) {
            maySendResult(intent, Activity.RESULT_CANCELED, result, ERROR_CONNECTION);
            return false;
        }

        if (branches.size() == 0) {
            return true;
        }

        final SportsWorldAccountManager accountMngr = getAccountManager();
        final boolean isMember = accountMngr.isLoggedInAsMember();

        final ContentResolver cr = getContentResolver();
        final BatchOperation batchOperation = new BatchOperation(
                SportsWorldContract.CONTENT_AUTHORITY, cr);

        final ContentValues values = new ContentValues();
        final int branchCount = branches.size();
        for (int i = 0; i < branchCount; i++) {
            final Branch item = branches.get(i);
            final Uri branchUri = SportsWorldContract.Branch.buildBranchUri(String.valueOf(item
                    .getUnId()));

            /*
             * We can't rely on the parser because we don't get from server a complete
             * representation of a Branch. Then, we update only the values we can't get when we
             * fetch all branches. A guest user does not upload its favorite branches, thus, we
             * don't update the favorite status of the branches.
             */

            if (isMember) {
                values.put(SportsWorldContract.Branch.FAVORITE, item.isFavorite());
            }
            values.put(SportsWorldContract.Branch.FACILITIES, item.getFacilitiesJoined());
            values.put(SportsWorldContract.Branch.FAC_URL_IMGS, item.getmImagesFacJoined());
            values.put(SportsWorldContract.Branch.IMAGES_URLS, item.getImagesUrlsJoined());
            values.put(SportsWorldContract.Branch.PHONE, item.getPhone());
            values.put(SportsWorldContract.Branch.VIDEO_URL, item.getVideoUrl());
            values.put(SportsWorldContract.Branch.URL_360, item.getUrl360());
            final ContentProviderOperation updateOperation = ContentProviderOperation
                    .newUpdate(branchUri).withYieldAllowed(true).withValues(values).build();
            batchOperation.add(updateOperation);
            values.clear();
        }

        batchOperation.execute();
        return true;
    }

}
