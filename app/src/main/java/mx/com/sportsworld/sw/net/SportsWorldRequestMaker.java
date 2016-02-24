package mx.com.sportsworld.sw.net;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import mx.com.sportsworld.sw.net.RestClient.Response;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;

/**
 * Creates a RequestMaker with the keyValues and url specified. Adds appropriate
 * app related header.
 * 
 */
public class SportsWorldRequestMaker {

	/** The Constant HEADER_AUTH_KEY. */
	private static final String HEADER_AUTH_KEY = "secret_key";
	
	/** The m context. */
	private final Context mContext;

	/**
	 * Instantiates a new sports world request maker.
	 * 
	 * @param context
	 *            the context
	 */
	public SportsWorldRequestMaker(Context context) {
		mContext = context;
	}

	/**
	 * Gets the.
	 * 
	 * @param url
	 *            the url
	 * @param header
	 *            the header
	 * @param keyValues
	 *            the key values
	 * @param useAuthToken
	 *            the use auth token
	 * @return the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Response get(String url, Map<String, List<String>> header,
			Map<String, String> keyValues, boolean useAuthToken)
			throws IOException {
		final Map<String, List<String>> originalHeader = buildHeader(useAuthToken);
		originalHeader.putAll(header);
		final RequestMaker requestMaker = new RequestMaker();
		return requestMaker.get(url, originalHeader, keyValues);
	}

	/**
	 * Gets the.
	 * 
	 * @param url
	 *            the url
	 * @param keyValues
	 *            the key values
	 * @param useAuthToken
	 *            the use auth token
	 * @return the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Response get(String url, Map<String, String> keyValues,
			boolean useAuthToken) throws IOException {

		final Map<String, List<String>> header = buildHeader(useAuthToken);
		final RequestMaker requestMaker = new RequestMaker();
		return requestMaker.get(url, header, keyValues);

	}

	/**
	 * Post.
	 * 
	 * @param url
	 *            the url
	 * @param keyValues
	 *            the key values
	 * @param useAuthToken
	 *            the use auth token
	 * @return the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Response post(String url, Map<String, String> keyValues,
			boolean useAuthToken) throws IOException {
		final Map<String, List<String>> header = buildHeader(useAuthToken);
		final RequestMaker requestMaker = new RequestMaker();
		Log.i("LogIron", url);
		Log.i("LogIron", header.toString());
		Log.i("LogIron", keyValues.toString());
		return requestMaker.post(url, header, keyValues);
	}

	/**
	 * Post.
	 * 
	 * @param url
	 *            the url
	 * @param header
	 *            the header
	 * @param keyValues
	 *            the key values
	 * @param useAuthToken
	 *            the use auth token
	 * @return the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Response post(String url, Map<String, List<String>> header,
			Map<String, String> keyValues, boolean useAuthToken)
			throws IOException {
		final Map<String, List<String>> originalHeader = buildHeader(useAuthToken);
		originalHeader.putAll(header);
		final RequestMaker requestMaker = new RequestMaker();
		return requestMaker.post(url, header, keyValues);
	}

	/**
	 * Put.
	 * 
	 * @param url
	 *            the url
	 * @param header
	 *            the header
	 * @param keyValues
	 *            the key values
	 * @param useAuthToken
	 *            the use auth token
	 * @return the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Response put(String url, Map<String, List<String>> header,
			Map<String, String> keyValues, boolean useAuthToken)
			throws IOException {
		final Map<String, List<String>> originalHeader = buildHeader(useAuthToken);
		originalHeader.putAll(header);
		final RequestMaker requestMaker = new RequestMaker();
		return requestMaker.put(url, originalHeader, keyValues);
	}

	/**
	 * Put.
	 * 
	 * @param url
	 *            the url
	 * @param keyValues
	 *            the key values
	 * @param useAuthToken
	 *            the use auth token
	 * @return the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Response put(String url, Map<String, String> keyValues,
			boolean useAuthToken) throws IOException {
		final Map<String, List<String>> header = buildHeader(useAuthToken);
		final RequestMaker requestMaker = new RequestMaker();
		return requestMaker.put(url, header, keyValues);
	}

	/**
	 * Delete.
	 * 
	 * @param url
	 *            the url
	 * @param header
	 *            the header
	 * @param keyValues
	 *            the key values
	 * @param useAuthToken
	 *            the use auth token
	 * @return the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Response delete(String url, Map<String, List<String>> header,
			Map<String, String> keyValues, boolean useAuthToken)
			throws IOException {
		final Map<String, List<String>> originalHeader = buildHeader(useAuthToken);
		originalHeader.putAll(header);
		final RequestMaker requestMaker = new RequestMaker();
		return requestMaker.delete(url, originalHeader, keyValues);
	}

	/**
	 * Delete.
	 * 
	 * @param url
	 *            the url
	 * @param keyValues
	 *            the key values
	 * @param useAuthToken
	 *            the use auth token
	 * @return the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Response delete(String url, Map<String, String> keyValues,
			boolean useAuthToken) throws IOException {
		final Map<String, List<String>> header = buildHeader(useAuthToken);
		final RequestMaker requestMaker = new RequestMaker();
		return requestMaker.delete(url, header, keyValues);
	}

	/**
	 * Adds auth token and app key on header.
	 * 
	 * @param useAuthToken
	 *            the use auth token
	 * @return A header with app key and auth token.
	 */
	private Map<String, List<String>> buildHeader(boolean useAuthToken) {
		/*
		 * TODO Currently it just sends fake values, but it must send real ones
		 * before it is implemented on server
		 */

		final Map<String, List<String>> header = new HashMap<String, List<String>>();

		// final List<String> appKeyValues = new ArrayList<String>();
		// appKeyValues.add(APP_KEY);
		// header.put(HEADER_APP_KEY, appKeyValues);

		if (useAuthToken) {
			final List<String> authKeyValues = new ArrayList<String>();
			if (SportsWorldPreferences.getAuthToken(mContext) == null)
				authKeyValues.add("02b7f1c16c25c3d156756163a8d1f6db4da2ff34");
			else
				authKeyValues.add(SportsWorldPreferences.getAuthToken(mContext)
						.trim());
			header.put(HEADER_AUTH_KEY, authKeyValues);
		}

		return header;
	}

}
