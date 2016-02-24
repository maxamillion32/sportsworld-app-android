package mx.com.sportsworld.sw.service;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

import org.json.JSONException;

import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.resource.MemberProfileResource;
import mx.com.sportsworld.sw.provider.SportsWorldContract;

/**
 * The Class MemberProfileUpdateService.
 */
public class MemberProfileUpdateService extends SportsWorldWebApiService<Void> {

    /** The Constant EXTRA_HEIGHT. */
    public static final String EXTRA_HEIGHT = "com.upster.extra.HEIGHT";
    
    /** The Constant EXTRA_WEIGHT. */
    public static final String EXTRA_WEIGHT = "com.upster.extra.WEIGHT";
    
    /** The Constant EXTRA_AGE. */
    public static final String EXTRA_AGE = "com.upster.extra.AGE";

    /**
	 * Instantiates a new member profile update service.
	 */
    public MemberProfileUpdateService() {
        super(MemberProfileUpdateService.class.getName());
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

        final double height = intent.getDoubleExtra(EXTRA_HEIGHT, -1d);
        final double weight = intent.getDoubleExtra(EXTRA_WEIGHT, -1d);
        final int age = intent.getIntExtra(EXTRA_AGE, -1);

        if ((height == -1d) || (weight == -1d) || (age == -1)) {
            throw new IllegalArgumentException(
                    "You forgot to pass one or more of these: EXTRA_HEIGHT, EXTRA_WEIGHT,"
                            + " EXTRA_AGE, EXTRA_MEM_UNIQ_ID");
        }

        final long userId = Long.parseLong(accountMngr.getCurrentUserId());

        final Uri userUri = SportsWorldContract.User.buildUserUri(String.valueOf(userId));
        long memUniqId = -1L;
        Cursor cursor = null;
        final ContentResolver cr = getContentResolver();
        try {
            cursor = cr.query(userUri, new String[] { SportsWorldContract.User.MEM_UNIQ_ID },
                    null /* selection */, null /* selectionArgs */, null/* sortOrder */);
            if (cursor.moveToFirst()) {
                memUniqId = cursor.getLong(0);
            }
        } 
        catch(Exception ex){
        	Log.i("LogIron", ex.toString());
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        if (memUniqId == -1L) {
            /* Something really bad happened, logout and then crash */
            accountMngr.logOut();
            throw new IllegalArgumentException(
                    "There was no user in database, but a session was started."
                            + " Current user was logged out.");
        }

        return MemberProfileResource.updateProfile(getApplicationContext(), userId, height, weight,
                age, memUniqId);

    }

    /* (non-Javadoc)
     * @see com.sportsworld.android.service.SportsWorldWebApiService#proccessRequestResult(android.content.Intent, com.sportsworld.android.net.RequestResultParser.RequestResult)
     */
    @Override
    public boolean proccessRequestResult(Intent intent, RequestResult<Void> result) {

        final double height = intent.getDoubleExtra(EXTRA_HEIGHT, -1d);
        final double weight = intent.getDoubleExtra(EXTRA_WEIGHT, -1d);
        final int age = intent.getIntExtra(EXTRA_AGE, -1);

        final ContentValues values = new ContentValues();
        values.put(SportsWorldContract.User.HEIGHT, height);
        values.put(SportsWorldContract.User.WEIGHT, weight);
        values.put(SportsWorldContract.User.AGE, age);

        final SportsWorldAccountManager accountMngr = getAccountManager();
        final long userId = Long.parseLong(accountMngr.getCurrentUserId());
        final Uri userUri = SportsWorldContract.User.buildUserUri(String.valueOf(userId));
        final ContentResolver cr = getContentResolver();
        cr.update(userUri, values, null /* selection */, null /* selectionArgs */);

        return true;

    }

}
