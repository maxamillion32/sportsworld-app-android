package mx.com.sportsworld.sw.parser;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The Interface JsonParser.
 * 
 * @param <E>
 *            the element type
 */
public interface JsonParser<E> {
    
    /**
	 * Parses the.
	 * 
	 * @param object
	 *            the object
	 * @return the e
	 * @throws JSONException
	 *             the jSON exception
	 */
    public abstract E parse(JSONObject object) throws JSONException;
    
    /**
	 * Parses the.
	 * 
	 * @param array
	 *            the array
	 * @return the list
	 * @throws JSONException
	 *             the jSON exception
	 */
    public abstract List<E> parse(JSONArray array) throws JSONException;
}
