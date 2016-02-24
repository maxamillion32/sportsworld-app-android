package mx.com.sportsworld.sw.net;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import mx.com.sportsworld.sw.net.RestClient.Response;
import mx.com.sportsworld.sw.parser.JsonParser;

/**
 * A parser for simple request. Make sure the parser's generic type is the same
 * as the class of the children in data.
 * 
 * @param <E>
 *            The class of the children in data.
 * @author Jos� Torres Fuentes 02/10/2013
 */
public class RequestResultParser<E> {

	/** The Constant KEY_SUCCESS. */
	private static final String KEY_SUCCESS = "status";

	/** The Constant KEY_MESSAGE. */
	private static final String KEY_MESSAGE = "message";

	/** The Constant ARRAY_DATA. */
	private static final String ARRAY_DATA = "data";

	/** The Constant OBJECT_DATA. */
	private static final String OBJECT_DATA = ARRAY_DATA;

	/**
	 * Creates a RequestResult using the provided response and parser.
	 *
	 * @param response
	 *            the response
	 * @param jsonParser
	 *            If you pass <code>null</code>, you'll always get
	 *            <code>null</code> on
	 * @return the request result
	 * @throws JSONException
	 *             If there is not any jsonObject or array
	 *             {@link com.sportsworld.android.net.RequestResultParser.RequestResult#getData()
	 *             RequestResult.getData()}
	 */
	public RequestResult<E> parseWith(Response response,
			JsonParser<E> jsonParser) throws JSONException {

		final int responseCode = response.getResponseCode();
		final long expires = response.getExpires();
		final long lastModified = response.getLastModified();

		List<E> data = null;
		boolean gotData = true;
		JSONObject object = null;
		try {
			object = response.getBodyAsJsonObject();
			Log.i("LogIron", object.toString());
		} catch (JSONException e) {
			gotData = false;
		}

		boolean status = false;
		String message = null;

		if (gotData) {
			status = object.optBoolean(KEY_SUCCESS);
			message = object.optString(KEY_MESSAGE);

			if (jsonParser != null) {
				final JSONArray dataArray = object.optJSONArray(ARRAY_DATA);
				if (dataArray != null) {
					data = jsonParser.parse(dataArray);
					Log.i("LogIron", data.toString());
				}

				final JSONObject dataObject = object.optJSONObject(OBJECT_DATA);
				if (dataObject != null) {
					data = new ArrayList<E>();
					data.add(jsonParser.parse(dataObject));
				}
			}

		}

		return new RequestResult<E>(responseCode, status, message, expires,
				lastModified, data);

	}

	/**
	 * A request whose data is made of children of the same class.
	 *
	 * @param <E>
	 *            The class of the children in data.
	 * @author Jos� Torres Fuentes 02/10/2013
	 */
	public static class RequestResult<E> {

		/** The m response code. */
		private int mResponseCode;
		
		/** The m status. */
		private final boolean mStatus;
		
		/** The m message. */
		private final String mMessage;
		
		/** The m expires. */
		private final long mExpires;
		
		/** The m last modified. */
		private final long mLastModified;
		
		/** The m data. */
		private final List<E> mData;

		/**
		 * Instantiates a new request result.
		 * 
		 * @param responseCode
		 *            the response code
		 * @param success
		 *            the success
		 * @param message
		 *            the message
		 * @param expires
		 *            the expires
		 * @param lastModidifed
		 *            the last modidifed
		 * @param data
		 *            the data
		 */
		public RequestResult(int responseCode, boolean success, String message,
				long expires, long lastModidifed, List<E> data) {
			mResponseCode = responseCode;
			mStatus = success;
			mMessage = message;
			mExpires = expires;
			mLastModified = lastModidifed;
			mData = data;
		}

		/**
		 * Gets the response code.
		 * 
		 * @return the response code
		 */
		public int getResponseCode() {
			return mResponseCode;
		}

		/**
		 * Checks if is succesful.
		 * 
		 * @return true, if is succesful
		 */
		public boolean isSuccesful() {
			return mStatus;
		}

		/**
		 * Gets the message.
		 * 
		 * @return the message
		 */
		public String getMessage() {
			return mMessage;
		}

		/**
		 * Gets the expires.
		 * 
		 * @return the expires
		 */
		public long getExpires() {
			return mExpires;
		}

		/**
		 * Gets the last modified.
		 * 
		 * @return the last modified
		 */
		public long getLastModified() {
			return mLastModified;
		}

		/**
		 * Gets the data.
		 * 
		 * @return the data
		 */
		public List<E> getData() {
			return mData;
		}

	}

}
