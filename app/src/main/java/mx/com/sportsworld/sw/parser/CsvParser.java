package mx.com.sportsworld.sw.parser;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import org.json.JSONException;

/**
 * The Interface CsvParser.
 * 
 * @param <E>
 *            the element type
 */
public interface CsvParser<E> {
    
    /**
	 * Parses the.
	 * 
	 * @param csv
	 *            the csv
	 * @return the e
	 * @throws JSONException
	 *             the jSON exception
	 */
    public abstract E parse(String csv) throws JSONException;
}
