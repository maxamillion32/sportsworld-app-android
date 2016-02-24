package mx.com.sportsworld.sw.service;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.io.IOException;

import org.json.JSONException;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Intent;
import android.util.Log;

import mx.com.sportsworld.sw.BuildConfig;
import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.utils.ConnectionUtils;

/**
 * The Class SportsWorldWebApiService.
 * 
 * @param <E>
 *            the element type
 */
public abstract class SportsWorldWebApiService<E> extends IntentService {

	/** The Constant ERROR_NONE. */
	public static final int ERROR_NONE = 0;
	
	/** The Constant ERROR_CONNECTION. */
	public static final int ERROR_CONNECTION = 1;
	
	/** The Constant ERROR_RESPONSE. */
	public static final int ERROR_RESPONSE = 2;
	
	/** The Constant EXTRA_PENDING_INTENT. */
	public static final String EXTRA_PENDING_INTENT = "com.upster.extra.PENDING_INTENT";
	
	/** The Constant EXTRA_RESULT_ERROR_CODE. */
	public static final String EXTRA_RESULT_ERROR_CODE = "com.upster.extra.ERROR_CODE";
	
	/** The Constant EXTRA_RESULT_SERVER_RESPONSE_CODE. */
	public static final String EXTRA_RESULT_SERVER_RESPONSE_CODE = "com.upster.extra.RESPONSE_CODE";
	
	/** The Constant EXTRA_RESULT_MESSAGE. */
	public static final String EXTRA_RESULT_MESSAGE = "com.upster.extra.RESULT_MESSAGE";
	
	/** The Constant TAG. */
	private static final String TAG = SportsWorldWebApiService.class.getName();
	
	/** The m account mngr. */
	private SportsWorldAccountManager mAccountMngr;

	/**
	 * Instantiates a new sports world web api service.
	 * 
	 * @param name
	 *            the name
	 */
	public SportsWorldWebApiService(String name) {
		super(name);
		setIntentRedelivery(true);
	}

	/**
	 * A free lazy loaded SportsWorldAccountManager. Do not call this method on
	 * your constructor.
	 * 
	 * @return A SportsWorldAccountManager.
	 */
	protected SportsWorldAccountManager getAccountManager() {
		if (mAccountMngr == null) {
			mAccountMngr = new SportsWorldAccountManager(
					getApplicationContext());
		}
		return mAccountMngr;
	}

	/* (non-Javadoc)
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "START!");
		}

		if (!ConnectionUtils.isNetworkAvailable(getApplicationContext())) {
			maySendResult(intent, Activity.RESULT_CANCELED, null,
					ERROR_CONNECTION);
			return;
		}

		try {

			final RequestResult<E> result = makeApiCall(intent);

			if (result != null) {

				if (result.getResponseCode() / 100 != 2) {
					maySendResult(intent, Activity.RESULT_CANCELED, result,
							ERROR_RESPONSE);
					return;
				}

				if (!result.isSuccesful()) {
					// throw new RuntimeException(
					// "Result should be success. There may be something wrong with your code");

					Log.i("LogIron",
							"Result should be success. There may be something wrong with your code");
				}

			}

			if (proccessRequestResult(intent, result)) {
				maySendResult(intent, Activity.RESULT_OK, result, ERROR_NONE);
			}

		} catch (IOException e) {
			maySendResult(intent, Activity.RESULT_CANCELED, null,
					ERROR_CONNECTION);
		} catch (JSONException e) {
			throw new RuntimeException(
					"Something is wrong with your json parser.");
		} finally {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "DONE!");

			}
		}

	}

	/**
	 * Make api call.
	 * 
	 * @param intent
	 *            the intent
	 * @return the request result
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws JSONException
	 *             the jSON exception
	 */
	public abstract RequestResult<E> makeApiCall(Intent intent)
			throws IOException, JSONException;

	/**
	 * Proccess request result.
	 * 
	 * @param intent
	 *            the intent
	 * @param result
	 *            the result
	 * @return <code>true</code> if we should send a success message to the
	 *         caller. If not, you can still send a message in your class by
	 *         calling {@link SportsWorldWebApiService#maySendResult()
	 *         maySendResult(Intent, int, RequestResult<E>, int)}.
	 */
	public abstract boolean proccessRequestResult(Intent intent,
			RequestResult<E> result);

	/**
	 * This class sends appropriate messages on success (if you pass
	 * <code>true</code> on.
	 * 
	 * @param intent
	 *            the intent
	 * @param code
	 *            the code
	 * @param result
	 *            the result
	 * @param errorCode
	 *            the error code
	 *            {@link SportsWorldWebApiService#proccessRequestResult()
	 *            proccessRequestResult(Intent, RequestResult<E>)} ) and
	 *            <code>IOExceptions</code>. For everything else, use this
	 *            method on <code>proccessRequestResult()</code> and return
	 *            false on that method. A JsonException always throws
	 *            RuntimeExceptions.
	 */
	protected void maySendResult(Intent intent, int code,
			RequestResult<E> result, int errorCode) {

		final PendingIntent pendingIntent = intent
				.getParcelableExtra(EXTRA_PENDING_INTENT);

		if (pendingIntent == null) {
			return;
		}

		try {

			String resultMsg = null;
			int responseCode = -1;
			if (result != null) {
				responseCode = result.getResponseCode();
				resultMsg = result.getMessage();
			}

			final Intent data = new Intent();
			data.putExtra(EXTRA_RESULT_ERROR_CODE, errorCode);
			data.putExtra(EXTRA_RESULT_SERVER_RESPONSE_CODE, responseCode);
			data.putExtra(EXTRA_RESULT_MESSAGE, resultMsg);

			if (BuildConfig.DEBUG) {
				Log.d(TAG, "EXTRA_ERROR_CODE = " + errorCode);
				Log.d(TAG, "EXTRA_RESPONSE_CODE = " + responseCode);
				Log.d(TAG, "EXTRA_RESULT_MESSAGE = " + resultMsg);
			}

			pendingIntent.send(getApplicationContext(), code, data);
		} catch (CanceledException ignore) {
			/* We can't do anything else */
			Log.e(TAG, SportsWorldWebApiService.class.getName()
					+ " - CanceledException");
		}
	}

}
