package mx.com.sportsworld.sw.loader;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.io.IOException;

import org.json.JSONException;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import mx.com.sportsworld.sw.model.Policy;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.resource.PolicyResource;
import mx.com.sportsworld.sw.utils.ConnectionUtils;

// TODO: Auto-generated Javadoc

/**
 * 
 * This class gets the policies from our api and deliver them to the
 * LoaderCallbacks#onLoadFinished() method.
 * 
 * 
 */
public class PolicyLoader extends AsyncTaskLoader<RequestResult<Policy>> {

    /** The m fetch policies result. */
    private RequestResult<Policy> mFetchPoliciesResult;

    /**
     * Instantiates a new policy loader.
     *
     * @param context the context
     */
    public PolicyLoader(Context context) {
        super(context);
    }

    /* (non-Javadoc)
     * @see android.support.v4.content.Loader#onStartLoading()
     */
    @Override
    protected void onStartLoading() {
        if (mFetchPoliciesResult != null) {
            deliverResult(mFetchPoliciesResult);
        }

        /* We don't need to monitor data, so we skip creating an observer */

        if (takeContentChanged() || mFetchPoliciesResult == null) {
            forceLoad();
        }
    }

    /* (non-Javadoc)
     * @see android.support.v4.content.AsyncTaskLoader#loadInBackground()
     */
    @Override
    public RequestResult<Policy> loadInBackground() {

        if (!ConnectionUtils.isNetworkAvailable(getContext())) {
            return null;
        }

        RequestResult<Policy> result = null;
        try {
            result = PolicyResource.fetchPolicies(getContext());
        } catch (final IOException ignore) {
            return null;                    // If something goes wrong, we just return null.
        } catch (final JSONException ignore) {
            throw new RuntimeException();   // This should never happen.
        }

        return result;
    }

    /* (non-Javadoc)
     * @see android.support.v4.content.Loader#deliverResult(java.lang.Object)
     */
    @Override
    public void deliverResult(RequestResult<Policy> data) {
        if (isReset()) {

            /* Since this is a simple list, we don't need to release any resources */
            return;
        }

        /* We don't hold a reference to old data because we don't need to release any resources */

        mFetchPoliciesResult = data;

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
        onStopLoading(); // Ensure the loader has been stopped.
        if (mFetchPoliciesResult != null) {

            /* Since this is a simple list, we don't need to release any resources */
            mFetchPoliciesResult = null;
        }

        /* We don't use an observer, so we skip unregistering the observer */
    }

    /* (non-Javadoc)
     * @see android.support.v4.content.AsyncTaskLoader#onCanceled(java.lang.Object)
     */
    @Override
    public void onCanceled(RequestResult<Policy> data) {
        super.onCanceled(data);

        /* Since this is a simple list, we don't need to release any resources */
    }

}
