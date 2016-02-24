package mx.com.sportsworld.sw.provider;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;

/**
 * This class handles execution of batch mOperations on a provider.
 */
public class BatchOperation {

	/** The m resolver. */
	private final ContentResolver mResolver;
	
	/** The m operations. */
	ArrayList<ContentProviderOperation> mOperations;
	
	/** The m authority. */
	private final String mAuthority;

	/**
	 * Instantiates a new batch operation.
	 * 
	 * @param authority
	 *            the authority
	 * @param resolver
	 *            the resolver
	 */
	public BatchOperation(String authority, ContentResolver resolver) {
		mResolver = resolver;
		mOperations = new ArrayList<ContentProviderOperation>();
		mAuthority = authority;
	}

	/**
	 * Size.
	 * 
	 * @return the int
	 */
	public int size() {
		return mOperations.size();
	}

	/**
	 * Adds the.
	 * 
	 * @param cpo
	 *            the cpo
	 */
	public void add(ContentProviderOperation cpo) {
		mOperations.add(cpo);
	}

	/**
	 * Execute.
	 */
	public void execute() {
		Log.i("LogIron ss", "execute");
		if (mOperations.size() == 0) {
			return;
		}
		// Apply the mOperations to the content provider
		try {
			mResolver.applyBatch(mAuthority, mOperations);
		} catch (final OperationApplicationException e1) {
			/* ignore, we just can't do anything about it. */
		} catch (final RemoteException e2) {
			/* ignore, we just can't do anything about it. */
		} catch (Exception ex) {
			Log.i("LogIron ss", ex.toString());
		}
		mOperations.clear();
	}

}
