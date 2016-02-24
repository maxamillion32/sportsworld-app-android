package mx.com.sportsworld.sw.parser;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.database.Cursor;

import java.util.List;

/**
 * The Interface CursorParser.
 * 
 * @param <E>
 *            the element type
 */
public interface CursorParser<E> {
    
    /**
	 * Parses the.
	 * 
	 * @param cursor
	 *            the cursor
	 * @return the list
	 */
    List<E> parse(Cursor cursor);
}
