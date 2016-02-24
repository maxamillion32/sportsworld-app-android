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
import android.text.format.Time;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import mx.com.sportsworld.sw.model.GymClass;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.resource.GymClassResource;
import mx.com.sportsworld.sw.parser.GymClassParser;
import mx.com.sportsworld.sw.provider.BatchOperation;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

/**
 * The Class FetchBranchGymClassesService.
 */
public class FetchBranchGymClassesService extends SportsWorldWebApiService<GymClass> {

    /** The Constant EXTRA_BRANCH_ID. */
    public static final String EXTRA_BRANCH_ID = "com.upster.extra.BRANCH_ID";
    
    /** The Constant EXTRA_DATE_MILLIS. */
    public static final String EXTRA_DATE_MILLIS = "com.upster.extra.DATE_MILLIS";

    /**
	 * Instantiates a new fetch branch gym classes service.
	 */
    public FetchBranchGymClassesService() {
        super(FetchBranchGymClassesService.class.getName());
    }

    /* (non-Javadoc)
     * @see com.sportsworld.android.service.SportsWorldWebApiService#makeApiCall(android.content.Intent)
     */
    @Override
    public RequestResult<GymClass> makeApiCall(Intent intent) throws IOException, JSONException {
        final long branchId = intent.getLongExtra(EXTRA_BRANCH_ID, -1L);
        final long dateMillis = intent.getLongExtra(EXTRA_DATE_MILLIS, -1L);

        if ((branchId == -1) || (dateMillis == -1)) {
            throw new IllegalArgumentException(
                    "You must pass ShowBranchService.EXTRA_BRANCH_ID, EXTRA_DATE_MILLIS"
                            + " as extras");
        }

        final Time time = new Time();
        time.set(dateMillis);

        final int dayOfMonth = time.monthDay;
        final int month = (time.month + 1); // On Time class, month is 0 based, but not on server
        final int year = time.year;

        return GymClassResource.fetchGymClasses(getApplicationContext(), branchId, year, month,
                dayOfMonth);
    }


    /* (non-Javadoc)
     * @see com.sportsworld.android.service.SportsWorldWebApiService#proccessRequestResult(android.content.Intent, com.sportsworld.android.net.RequestResultParser.RequestResult)
     */
    @Override
    public boolean proccessRequestResult(Intent intent, RequestResult<GymClass> result) {
        final List<GymClass> gymClasses = result.getData();

        final ContentResolver cr = getContentResolver();
        final BatchOperation batchOperation = new BatchOperation(
                SportsWorldContract.CONTENT_AUTHORITY, cr);

        final ContentProviderOperation deleteAllGymClassesOperation = ContentProviderOperation
                .newDelete(SportsWorldContract.GymClass.CONTENT_URI).build();
        batchOperation.add(deleteAllGymClassesOperation);
        
        if (gymClasses == null) {
        	 batchOperation.execute();
            maySendResult(intent, Activity.RESULT_CANCELED, null, ERROR_CONNECTION);
            return false;
            
        }

        if (gymClasses.size() == 0) {
        	 batchOperation.execute();
            return true;
        }

        batchOperation.add(deleteAllGymClassesOperation);

        final GymClassParser parser = new GymClassParser();
        final ContentValues values = new ContentValues();
        final int branchCount = gymClasses.size();
        for (int i = 0; i < branchCount; i++) {
            final GymClass item = gymClasses.get(i);
            parser.parse(item, values);
            final ContentProviderOperation inserOperation = ContentProviderOperation
                    .newInsert(SportsWorldContract.GymClass.CONTENT_URI).withYieldAllowed(true)
                    .withValues(values).build();
            batchOperation.add(inserOperation);
            values.clear();
        }

        batchOperation.execute();
        return true;
    }

}
