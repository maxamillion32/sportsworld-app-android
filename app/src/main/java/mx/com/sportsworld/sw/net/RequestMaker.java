package mx.com.sportsworld.sw.net;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import mx.com.sportsworld.sw.net.RestClient.Response;

/**
 * Creates and executes a request by building an appropriate body (POST and PUT) or url (GET or
 * DELETE) with the provided {@code keyValues}. This request maker assumes:
 * <ul>
 * <li>You don't send a file.</li>
 * <li>Your GET or DELETE doesn't send anything as the body of the request.</li>
 * <li>Your POST or PUT doesn't add anything to the URL.</li>
 * <li>Your header <strong>replaces</strong> the values on the default header.
 * </ul>
 * 
 * @author Jos� Torres Fuentes 02/10/2013
 * 
 */
public class RequestMaker {

    /**
	 * Gets the.
	 * 
	 * @param url
	 *            the url
	 * @param header
	 *            the header
	 * @param keyValues
	 *            the key values
	 * @return the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public Response get(String url, Map<String, List<String>> header, Map<String, String> keyValues)
            throws IOException {
        final RestClient restClient = new RestClient();
        return restClient.get(buildUrl(url, keyValues), header, null /* body */);
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
	 * @return the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public Response post(String url, Map<String, List<String>> header, Map<String, String> keyValues)
            throws IOException {
        final RestClient restClient = new RestClient();
//        return restClient.post(url, header, valuesToByteArray(keyValues) /* body */);
        return restClient.post(url, header, valuesToByteArray(keyValues) /* body */);
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
	 * @return the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public Response put(String url, Map<String, List<String>> header, Map<String, String> keyValues)
            throws IOException {
        final RestClient restClient = new RestClient();
        return restClient.put(url, header, valuesToByteArray(keyValues) /* body */);
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
	 * @return the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public Response delete(String url, Map<String, List<String>> header,
            Map<String, String> keyValues) throws IOException {
        final RestClient restClient = new RestClient();
        return restClient.delete(buildUrl(url, keyValues), header, null /* body */);
    }

    /**
	 * Builds the url.
	 * 
	 * @param url
	 *            the url
	 * @param keyValues
	 *            the key values
	 * @return the string
	 */
    private String buildUrl(String url, Map<String, String> keyValues) {

        if ((keyValues == null) || (keyValues.size() == 0)) {
            return url;
        }

        final StringBuilder strBuilder = new StringBuilder(url);

        if (!url.endsWith("?")) {
            strBuilder.append("?");
        }

        concatenateValues(keyValues, strBuilder);

        String newUrl = null;
        try {
            newUrl = URLEncoder.encode(strBuilder.toString(), "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
            /* Should not happen */
        }

        return newUrl;

    }

    /**
	 * Concatenate values.
	 * 
	 * @param keyValues
	 *            the key values
	 * @param builder
	 *            the builder
	 * @return the string builder
	 */
    private StringBuilder concatenateValues(Map<String, String> keyValues, StringBuilder builder) {

        final StringBuilder strBuilder;
        if (builder == null) {
            strBuilder = new StringBuilder();
        } else {
            strBuilder = builder;
        }

        if ((keyValues == null) || (keyValues.size() == 0)) {
            return strBuilder;
        }
        try {
            for (Map.Entry<String, String> entry : keyValues.entrySet()) {
                strBuilder.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                strBuilder.append("&");
            }
        } catch (UnsupportedEncodingException e) {
            /* Should not happen */
        }
        return strBuilder.deleteCharAt(strBuilder.length() - 1);

    }

    /**
	 * Values to byte array.
	 * 
	 * @param keyValues
	 *            the key values
	 * @return the byte[]
	 */
    private byte[] valuesToByteArray(Map<String, String> keyValues) {

        final String values = concatenateValues(keyValues, null /* stringBuilder */).toString();
        byte[] valuesAsByteArray = null;
        try {
            valuesAsByteArray = values.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ignore) {
            /* Should not happen */
        }
        return valuesAsByteArray;
    }

}
