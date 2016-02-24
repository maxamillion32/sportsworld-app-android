package mx.com.sportsworld.sw.parser;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.ContentValues;

/**
 * The Interface ContentValuesParser.
 * 
 * @param <E>
 *            the element type
 */
public interface ContentValuesParser<E> {
    
    /**
	 * Parses the.
	 * 
	 * @param object
	 *            the object
	 * @param values
	 *            the values
	 * @return the content values
	 */
    ContentValues parse(E object, ContentValues values);
}
