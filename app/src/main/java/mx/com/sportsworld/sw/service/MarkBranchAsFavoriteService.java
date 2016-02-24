package mx.com.sportsworld.sw.service;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
import mx.com.sportsworld.sw.model.Branch;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.resource.BranchResource;
import mx.com.sportsworld.sw.parser.BranchParser;
import mx.com.sportsworld.sw.provider.BatchOperation;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

/**
 * Sets a branch as favorite.
 * 
 */
public class MarkBranchAsFavoriteService extends SportsWorldWebApiService<Void> {

    /** The Constant EXTRA_BRANCH_ID. */
    public static final String EXTRA_BRANCH_ID = "com.upster.extra.BRANCH_ID";
    
    /** The Constant EXTRA_FAVORITE. */
    public static final String EXTRA_FAVORITE = "com.upster.extra.FAVORITE";
    
    /** The Constant SELECTION_TYPE. */
    private static final String SELECTION_TYPE = SportsWorldContract.Branch.TYPE + "= ?";
    
    /** The Constant SELECTION_TYPE_AND_ID. */
    private static final String SELECTION_TYPE_AND_ID = SportsWorldContract.Branch.TYPE
            + "= ? AND " + SportsWorldContract.Branch._ID + "= ?";
    
    /** The Constant SELECTION_ARGS_TYPE. */
    private static final String[] SELECTION_ARGS_TYPE = new String[] { Branch.TYPE_FAVORITE };

    /**
	 * Instantiates a new mark branch as favorite service.
	 */
    public MarkBranchAsFavoriteService() {
        super(MarkBranchAsFavoriteService.class.getName());
    }

    /* (non-Javadoc)
     * @see com.sportsworld.android.service.SportsWorldWebApiService#makeApiCall(android.content.Intent)
     */
    @Override
    public RequestResult<Void> makeApiCall(Intent intent) throws IOException, JSONException {

        final SportsWorldAccountManager accountMngr = getAccountManager();

        if (!accountMngr.isLoggedInAsMember()) {
            return null;
        }

        final long branchId = intent.getLongExtra(EXTRA_BRANCH_ID, -1L);
        final boolean markAsfavorite = intent.getBooleanExtra(EXTRA_FAVORITE, false);

        if (branchId == -1L) {
            throw new IllegalArgumentException("You must pass a EXTRA_BRANCH_ID long extra");
        }

        return BranchResource.markAsFavorite(getApplicationContext(), branchId,
                Long.parseLong(accountMngr.getCurrentUserId()), markAsfavorite);

    }

    /* (non-Javadoc)
     * @see com.sportsworld.android.service.SportsWorldWebApiService#proccessRequestResult(android.content.Intent, com.sportsworld.android.net.RequestResultParser.RequestResult)
     */
    @Override
    public boolean proccessRequestResult(Intent intent, RequestResult<Void> result) {

        final ContentResolver cr = getContentResolver();
        final BatchOperation batchOperation = new BatchOperation(
                SportsWorldContract.CONTENT_AUTHORITY, cr);

        final long branchId = intent.getLongExtra(EXTRA_BRANCH_ID, -1L);
        final boolean markAsfavorite = intent.getBooleanExtra(EXTRA_FAVORITE, false);

        /*
         * Do whatever it takes to not make another call to the web api. It will be a little
         * complex, so bear with me!
         */

        /*
         * First, we update every row with _id == branchId.
         */
        final Uri branchUri = SportsWorldContract.Branch.buildBranchUri(String.valueOf(branchId));
        final ContentValues values = new ContentValues();
        values.put(SportsWorldContract.Branch.FAVORITE, markAsfavorite);
        final ContentProviderOperation updateOperation = ContentProviderOperation
                .newUpdate(branchUri).withYieldAllowed(true).withValues(values).build();
        batchOperation.add(updateOperation);
        values.clear();

        /*
         * Then, we ask if branchId branch is already in favorites.
         */
        Cursor cursor = null;
        boolean favoriteExists = false;
        try {

            cursor = cr.query(branchUri, new String[] { SportsWorldContract.Branch._ID },
                    SELECTION_TYPE, SELECTION_ARGS_TYPE, null /* sortOrder */);

            favoriteExists = ((cursor != null) && (cursor.getCount() > 0));

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        if (favoriteExists && !markAsfavorite) {

            /*
             * If favorite exists and the new favorite value is false, just delete that branch of
             * type Branch.TYPE_FAVORITE.
             */

            final ContentProviderOperation deleteOperation = ContentProviderOperation
                    .newDelete(SportsWorldContract.Branch.CONTENT_URI)
                    .withYieldAllowed(true)
                    .withSelection(SELECTION_TYPE_AND_ID,
                            new String[] { Branch.TYPE_FAVORITE, String.valueOf(branchId) })
                    .build();
            batchOperation.add(deleteOperation);

        } else if (!favoriteExists && markAsfavorite) {

            /*
             * If favorite does not exist and the new favorite value is true, the we need to insert
             * a branch with same values that branchId, but wit type Branch.TYPE_FAVORITE
             */

            try {

                /*
                 * We get any entity of branchId. We are interested on duplicate it, but with type
                 * Branch.TYPE_FAVORITE. Here we can be sure there is not an entity with branchId
                 * and type BRANCH.TYPE_FAVORITE.
                 */

                /*
                 * With a null projection we are telling our contentProvider that we want him to
                 * give us all columns.
                 */

                cursor = cr.query(branchUri, null /* projection */, null /* selection */,
                        null /* selectionArgs */, null /* sortOrder */);

                final BranchParser parser = new BranchParser();
                final List<Branch> branches = parser.parse(cursor);

                final int count = branches.size();
                if (count > 0) { // Just to be sure

                    /* We just need to insert one, so let's take the first one */

                    final Branch oldBranch = branches.get(0);

                    /*
                     * Make a copy with Branch.TYPE_FAVORITE and the appropriate favorite value.
                     */

                    final Branch modifiedBranch = new Branch(oldBranch.getLatitude(),
                            oldBranch.getLongitude(), oldBranch.getDistance(),
                            oldBranch.getStateId(), oldBranch.getAddress(),
                            oldBranch.getSchedule(), oldBranch.getUnId(), oldBranch.getKey(),
                            oldBranch.getDCount(), oldBranch.getName(), oldBranch.getVideoUrl(),
                            oldBranch.getPreOrder(), oldBranch.getStateName(),
                            oldBranch.getPhone(), Branch.TYPE_FAVORITE, true /* favorite */,
                            oldBranch.getFacilities(),oldBranch.getmImagesFacilities(), oldBranch.getImagesUrl()
                            ,oldBranch.getUrl360()
                            );

                    parser.parse(modifiedBranch, values);
                    final ContentProviderOperation insertOperation = ContentProviderOperation
                            .newInsert(SportsWorldContract.Branch.CONTENT_URI)
                            .withYieldAllowed(true).withValues(values).build();
                    batchOperation.add(insertOperation);
                    values.clear();
                }

            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                	
                }
            }

        }

        /*
         * execute this bunch of operations.
         */
        batchOperation.execute();

        /*
         * And tell our activity that the update on server was successful. Our local copy should be
         * the same as the one on the server.
         */
        return true;
    }

}
