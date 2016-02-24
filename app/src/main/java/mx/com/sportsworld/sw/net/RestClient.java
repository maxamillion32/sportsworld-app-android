package mx.com.sportsworld.sw.net;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;
import android.util.Log;

/**
 * Creates the appropriate {@link Request Request} and executes it.
 * 
 */
public class RestClient {

    /** The Constant EXCEPTION_MSG_BAD_URL. */
    private static final String EXCEPTION_MSG_BAD_URL = "Bad url: %1$s";

    /**
	 * Gets the.
	 * 
	 * @param url
	 *            the url
	 * @param header
	 *            the header
	 * @param body
	 *            the body
	 * @return the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public Response get(String url, Map<String, List<String>> header, byte[] body)
            throws IOException {
        Response response = null;
        try {
        	Log.i("LogIron", "get " + url + " " + header + " " + body);
            response = executeRequest(new Get(url, header, body));
            
            
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(buildBadUrlMessage(url));
        }
        return response;
    }

    /**
	 * Post.
	 * 
	 * @param url
	 *            the url
	 * @param header
	 *            the header
	 * @param body
	 *            the body
	 * @return the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public Response post(String url, Map<String, List<String>> header, byte[] body)
            throws IOException {
        Response response = null;
        try {
        	Log.i("LogIron", "post " + url + " " + header + " " + body);
            response = executeRequest(new Post(url, header, body));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(buildBadUrlMessage(url));
        }
        return response;
    }

    /**
	 * Put.
	 * 
	 * @param url
	 *            the url
	 * @param header
	 *            the header
	 * @param body
	 *            the body
	 * @return the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public Response put(String url, Map<String, List<String>> header, byte[] body)
            throws IOException {
        Response response = null;
        try {
            response = executeRequest(new Put(url, header, body));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(buildBadUrlMessage(url));
        }
        return response;
    }

    /**
	 * Delete.
	 * 
	 * @param url
	 *            the url
	 * @param header
	 *            the header
	 * @param body
	 *            the body
	 * @return the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public Response delete(String url, Map<String, List<String>> header, byte[] body)
            throws IOException {
        Response response = null;
        try {
            response = executeRequest(new Delete(url, header, body));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(buildBadUrlMessage(url));
        }
        return response;
    }

    /**
	 * Builds the bad url message.
	 * 
	 * @param url
	 *            the url
	 * @return the string
	 */
    private String buildBadUrlMessage(String url) {
        return String.format(Locale.US, EXCEPTION_MSG_BAD_URL, url);
    }

    /**
	 * Execute request.
	 * 
	 * @param request
	 *            the request
	 * @return the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    private Response executeRequest(Request request) throws IOException {
        return request.execute();
    }

    /**
     * Describes and executes a request. Currently, it does not follow redirects.
     * 
     * @author Jos� Torres Fuentes 02/10/2013
     *
     */
    public static abstract class Request {
        /*
         * Uses HttpURLConnection to connect with Internet.
         */

        /** The Constant HEADER_EXPIRES. */
        private static final String HEADER_EXPIRES = "Expires";

        /** The Constant HEADER_LAST_MODIFIED. */
        private static final String HEADER_LAST_MODIFIED = "Last-Modified";

        /** The m header. */
        private final Map<String, List<String>> mHeader;

        /** The m body. */
        private final byte[] mBody;

        /** The m url. */
        private final URL mUrl;

        /**
		 * Instantiates a new request.
		 *
		 * @param url
		 *            the url
		 * @param header
		 *            the header
		 * @param body
		 *            the body
		 * @throws MalformedURLException
		 *             the malformed url exception
		 */
        public Request(String url, Map<String, List<String>> header, byte[] body)
                throws MalformedURLException {
            mUrl = new URL(url);
            mHeader = header;
            mBody = body;
        }

        /**
		 * Checks if is body empty.
		 *
		 * @return true, if is body empty
		 */
        public boolean isBodyEmpty() {
            return mBody.length == 0;
        }

        /**
		 * Gets the request mehod name.
		 *
		 * @return the request mehod name
		 */
        protected abstract String getRequestMehodName();

        /**
		 * Disable connection reuse if necessary.
		 */
        private void disableConnectionReuseIfNecessary() {
            // HTTP connection reuse which was buggy pre-froyo
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
                System.setProperty("http.keepAlive", "false");
            }
        }

        /**
		 * Execute.
		 *
		 * @return the response
		 * @throws IOException
		 *             Signals that an I/O exception has occurred.
		 */
        public Response execute() throws IOException {

            disableConnectionReuseIfNecessary();

            Response response = null;
            HttpURLConnection conn = null;
            try {
                conn = createAndConfigureConnection();
                mayAddHeaders(conn);
                maySendBody(conn);

                response = getResponse(conn);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            return response;

        }

        /**
		 * Creates the and configure connection.
		 *
		 * @return the http url connection
		 * @throws IOException
		 *             Signals that an I/O exception has occurred.
		 */
        private HttpURLConnection createAndConfigureConnection() throws IOException {

            final HttpURLConnection conn;
            if (mUrl.getProtocol().startsWith("https")) {
                conn = (HttpURLConnection) mUrl.openConnection();
            } else {
                conn = (HttpURLConnection) mUrl.openConnection();
            }

            conn.setRequestMethod(getRequestMehodName());
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            return conn;

        }

        /**
		 * May add headers.
		 *
		 * @param conn
		 *            the conn
		 */
        private void mayAddHeaders(HttpURLConnection conn) {

            final Map<String, List<String>> header = mHeader;

            if ((header == null) || (header.size() == 0)) {
                return;
            }

            for (Map.Entry<String, List<String>> entry : header.entrySet()) {
                if (entry.getValue().size() == 0) {
                    continue;
                }
                for (String value : entry.getValue()) {
                    conn.setRequestProperty(entry.getKey(), value);
                    Log.i("LogIron", entry.getKey() + ":" + value);

                }
            }

        }

        /**
		 * May send body.
		 *
		 * @param conn
		 *            the conn
		 * @throws IOException
		 *             Signals that an I/O exception has occurred.
		 */
        private void maySendBody(HttpURLConnection conn) throws IOException {

            final byte[] body = mBody;

            if ((body == null) || (body.length == 0)) {
                conn.setChunkedStreamingMode(0);
                return;
            }

            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(body.length);

            BufferedOutputStream buffOut = null;
            try {
                buffOut = new BufferedOutputStream(conn.getOutputStream());
                buffOut.write(body);
                Log.d("kokusho", body.toString());
            } finally {
                if (buffOut != null) {
                    try {
                        buffOut.close();
                    } catch (IOException ignore) {
                        /* We can't do anything else */
                    }
                }
            }

        }

        /**
		 * Gets the response.
		 *
		 * @param conn
		 *            the conn
		 * @return the response
		 * @throws IOException
		 *             Signals that an I/O exception has occurred.
		 */
        private Response getResponse(HttpURLConnection conn) throws IOException {

            final int responseCode = conn.getResponseCode();
            long currentTime = System.currentTimeMillis();
            long expires = conn.getHeaderFieldDate(HEADER_EXPIRES, currentTime);
            long lastModified = conn.getHeaderFieldDate(HEADER_LAST_MODIFIED, currentTime);

            BufferedInputStream buffIn = null;
            byte[] responseBody;
            try {
                if (responseCode / 100 == 2) {
                    buffIn = new BufferedInputStream(conn.getInputStream());
                    responseBody = readStream(buffIn);
                } else {
                    buffIn = new BufferedInputStream(conn.getErrorStream());
                    responseBody = readStream(buffIn);
                }
                return new Response(conn.getHeaderFields(), conn.getResponseCode(), expires,
                        lastModified, responseBody);
            } finally {
                if (buffIn != null) {
                    try {
                        buffIn.close();
                    } catch (IOException ignore) {
                        /* We can't do anything else */
                    }
                }
            }

        }

        /**
		 * Read stream.
		 *
		 * @param buffIn
		 *            the buff in
		 * @return the byte[]
		 * @throws IOException
		 *             Signals that an I/O exception has occurred.
		 */
        private byte[] readStream(BufferedInputStream buffIn) throws IOException {
            byte[] buf = new byte[1024];
            int count = 0;
            ByteArrayOutputStream out = null;
            try {
                out = new ByteArrayOutputStream(1024);
                while ((count = buffIn.read(buf)) != -1) {
                    out.write(buf, 0, count);
                }
                return out.toByteArray();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ignore) {
                        /* We can't do anything else */
                    }
                }
            }
        }

    }

    /**
	 * The Class Get.
	 */
    private static class Get extends Request {

        /** The Constant METHOD. */
        private static final String METHOD = "GET";

        /**
		 * Instantiates a new gets the.
		 *
		 * @param url
		 *            the url
		 * @param header
		 *            the header
		 * @param body
		 *            the body
		 * @throws MalformedURLException
		 *             the malformed url exception
		 */
        public Get(String url, Map<String, List<String>> header, byte[] body)
                throws MalformedURLException {
            super(url, header, body);
        }

        /* (non-Javadoc)
         * @see com.sportsworld.android.net.RestClient.Request#getRequestMehodName()
         */
        @Override
        protected String getRequestMehodName() {
            return METHOD;
        }
    }

    /**
	 * The Class Post.
	 */
    private static class Post extends Request {

        /** The Constant METHOD. */
        private static final String METHOD = "POST";

        /**
		 * Instantiates a new post.
		 *
		 * @param url
		 *            the url
		 * @param header
		 *            the header
		 * @param body
		 *            the body
		 * @throws MalformedURLException
		 *             the malformed url exception
		 */
        public Post(String url, Map<String, List<String>> header, byte[] body)
                throws MalformedURLException {
            super(url, header, body);
        }

        /* (non-Javadoc)
         * @see com.sportsworld.android.net.RestClient.Request#getRequestMehodName()
         */
        @Override
        protected String getRequestMehodName() {
            return METHOD;
        }

    }

    /**
	 * The Class Put.
	 */
    private static class Put extends Request {

        /** The Constant METHOD. */
        private static final String METHOD = "PUT";

        /**
		 * Instantiates a new put.
		 *
		 * @param url
		 *            the url
		 * @param header
		 *            the header
		 * @param body
		 *            the body
		 * @throws MalformedURLException
		 *             the malformed url exception
		 */
        public Put(String url, Map<String, List<String>> header, byte[] body)
                throws MalformedURLException {
            super(url, header, body);
        }

        /* (non-Javadoc)
         * @see com.sportsworld.android.net.RestClient.Request#getRequestMehodName()
         */
        @Override
        protected String getRequestMehodName() {
            return METHOD;
        }
    }

    /**
	 * The Class Delete.
	 */
    private static class Delete extends Request {

        /** The Constant METHOD. */
        private static final String METHOD = "DELETE";

        /**
		 * Instantiates a new delete.
		 *
		 * @param url
		 *            the url
		 * @param header
		 *            the header
		 * @param body
		 *            the body
		 * @throws MalformedURLException
		 *             the malformed url exception
		 */
        public Delete(String url, Map<String, List<String>> header, byte[] body)
                throws MalformedURLException {
            super(url, header, body);

        }

        /* (non-Javadoc)
         * @see com.sportsworld.android.net.RestClient.Request#getRequestMehodName()
         */
        @Override
        protected String getRequestMehodName() {
            return METHOD;
        }
    }

    /**
     * Response from server.
     *
     * @author Jos� Torres Fuentes 02/10/2013
     * 
     */
    public static class Response {

        /** The m header. */
        private final Map<String, List<String>> mHeader;
        
        /** The m response code. */
        private final int mResponseCode;
        
        /** The m expires. */
        private final long mExpires;
        
        /** The m last modified. */
        private final long mLastModified;
        
        /** The m body. */
        private final byte[] mBody;

        /**
		 * Instantiates a new response.
		 * 
		 * @param header
		 *            the header
		 * @param responseCode
		 *            the response code
		 * @param expires
		 *            the expires
		 * @param lastModified
		 *            the last modified
		 * @param body
		 *            the body
		 */
        public Response(Map<String, List<String>> header, int responseCode, long expires,
                long lastModified, byte[] body) {
            mHeader = header;
            mResponseCode = responseCode;
            mExpires = expires;
            mLastModified = lastModified;
            mBody = body;
        }

        /**
		 * Gets the body.
		 * 
		 * @return the body
		 */
        public byte[] getBody() {
            return mBody;
        }

        /**
		 * Gets the body as string.
		 * 
		 * @return the body as string
		 */
        public String getBodyAsString() {
            return new String(mBody);
        }

        /**
		 * Gets the body as json object.
		 * 
		 * @return the body as json object
		 * @throws JSONException
		 *             the jSON exception
		 */
        public JSONObject getBodyAsJsonObject() throws JSONException {
            return new JSONObject(new String(mBody));
        }

        /**
		 * Checks for body.
		 * 
		 * @return true, if successful
		 */
        public boolean hasBody() {
            return ((mBody != null) && (mBody.length > 0));
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
		 * Gets the header.
		 * 
		 * @return the header
		 */
        public Map<String, List<String>> getHeader() {
            return mHeader;
        }

    }

}
