package mx.com.sportsworld.sw.loader;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;

import org.json.JSONException;

import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
import mx.com.sportsworld.sw.account.SportsWorldAccountManager.IncompatibleDeviceException;
import mx.com.sportsworld.sw.model.UserProfile;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;

// TODO: Auto-generated Javadoc

/**
 * The Class LoginLoader.
 */
public class LoginLoader extends AsyncTaskLoader<RequestResult<UserProfile>> {

    /** The m log in result. */
    private RequestResult<UserProfile> mLogInResult;
    
    /** The m username. */
    private String mUsername;
    
    /** The m password. */
    private String mPassword;

    /**
     * Instantiates a new login loader.
     *
     * @param context the context
     * @param username the username
     * @param password the password
     */
    public LoginLoader(Context context, String username, String password) {
        super(context);
        mUsername = username;
        mPassword = password;
    }

    /* (non-Javadoc)
     * @see android.support.v4.content.Loader#onStartLoading()
     */
    @Override
    protected void onStartLoading() {
        if (mLogInResult != null) {
            deliverResult(mLogInResult);
        }

        /* We don't need to monitor data, so we skip creating an observer */

        if (takeContentChanged() || mLogInResult == null) {
            forceLoad();
        }
    }

    /* (non-Javadoc)
     * @see android.support.v4.content.AsyncTaskLoader#loadInBackground()
     */
    @Override
    public RequestResult<UserProfile> loadInBackground() {

        SystemClock.sleep(5000); // TODO Remove now!
        
        final SportsWorldAccountManager accountManager = new SportsWorldAccountManager(getContext());
        
        RequestResult<UserProfile> result = null;

        final ConnectivityManager connMgr = (ConnectivityManager) getContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if ((networkInfo != null) && networkInfo.isConnected()) { // TODO use ConnectionUtils.isNetworkAvailable

            try {
                result = accountManager.logInAsMember(mUsername, mPassword);
            } catch (IOException ignore) {
                return null;                    // If something goes wrong, we just return null.
            } catch (JSONException e) {
                throw new RuntimeException();   // This should never happen.
            } catch (IncompatibleDeviceException e) {
                throw new RuntimeException();   // XXX I'm not sure this is the right thing to do.
            } finally {
              Log.e("LoginLoader", "We are done!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }

        }

        return result;
    }

    /* (non-Javadoc)
     * @see android.support.v4.content.Loader#deliverResult(java.lang.Object)
     */
    @Override
    public void deliverResult(RequestResult<UserProfile> data) {
        if (isReset()) {

            /* Since this is a simple list, we don't need to release any resources */
            return;
        }

        /* We don't hold a reference to old data because we don't need to release any resources */

        mLogInResult = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        /* Since this is a simple list, we don't need to release any resources */
    }

    /* (non-Javadoc)
     * @see android.support.v4.content.Loader#onStopLoading()
     */
    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    /* (non-Javadoc)
     * @see android.support.v4.content.Loader#onReset()
     */
    @Override
    protected void onReset() {        
        onStopLoading();        // Ensure the loader has been stopped.

        if (mLogInResult != null) {

            /* Since this is a simple list, we don't need to release any resources */
            mLogInResult = null;
        }

        /* We don't use an observer, so we skip unregistering the observer */
    }

    /* (non-Javadoc)
     * @see android.support.v4.content.AsyncTaskLoader#onCanceled(java.lang.Object)
     */
    @Override
    public void onCanceled(RequestResult<UserProfile> data) {
        super.onCanceled(data);

        /* Since this is a simple list, we don't need to release any resources */
    }
    
}
