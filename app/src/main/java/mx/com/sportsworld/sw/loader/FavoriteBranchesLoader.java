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

import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
import mx.com.sportsworld.sw.model.Branch;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.resource.BranchResource;
import mx.com.sportsworld.sw.utils.ConnectionUtils;

// TODO: Auto-generated Javadoc

/**
 * The Class FavoriteBranchesLoader.
 */
public class FavoriteBranchesLoader extends AsyncTaskLoader<RequestResult<Branch>> {

    /** The m fetch favorite branches. */
    private RequestResult<Branch> mFetchFavoriteBranches;

    /**
     * Instantiates a new favorite branches loader.
     *
     * @param context the context
     */
    public FavoriteBranchesLoader(Context context) {
        super(context);
    }

    /* (non-Javadoc)
     * @see android.support.v4.content.Loader#onStartLoading()
     */
    @Override
    protected void onStartLoading() {
        if (mFetchFavoriteBranches != null) {
            deliverResult(mFetchFavoriteBranches);
        }

        /* We don't need to monitor data, so we skip creating an observer */

        if (takeContentChanged() || mFetchFavoriteBranches == null) {
            forceLoad();
        }
    }

    /* (non-Javadoc)
     * @see android.support.v4.content.AsyncTaskLoader#loadInBackground()
     */
    @Override
    public RequestResult<Branch> loadInBackground() {

        if (!ConnectionUtils.isNetworkAvailable(getContext())) {
            return null;
        }

        RequestResult<Branch> result = null;
        try {
            final SportsWorldAccountManager accountMngr = new SportsWorldAccountManager(
                    getContext());
            final long userId = Long.valueOf(accountMngr.getCurrentUserId());
            result = BranchResource.fetchFavoriteBranches(getContext(), userId);
        } catch (final IOException ignore) {
            return null; // If something goes wrong, we just return null.
        } catch (final JSONException ignore) {
            throw new RuntimeException(); // This should never happen.
        }

        return result;
    }

    /* (non-Javadoc)
     * @see android.support.v4.content.Loader#deliverResult(java.lang.Object)
     */
    @Override
    public void deliverResult(RequestResult<Branch> data) {
        if (isReset()) {

            /* Since this is a simple list, we don't need to release any resources */
            return;
        }

        /* We don't hold a reference to old data because we don't need to release any resources */

        mFetchFavoriteBranches = data;

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
        if (mFetchFavoriteBranches != null) {

            /* Since this is a simple list, we don't need to release any resources */
            mFetchFavoriteBranches = null;
        }

        /* We don't use an observer, so we skip unregistering the observer */
    }

    /* (non-Javadoc)
     * @see android.support.v4.content.AsyncTaskLoader#onCanceled(java.lang.Object)
     */
    @Override
    public void onCanceled(RequestResult<Branch> data) {
        super.onCanceled(data);

        /* Since this is a simple list, we don't need to release any resources */
    }

}
