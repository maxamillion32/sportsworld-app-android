package mx.com.sportsworld.sw.model;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.Date;

/**
 * The Class Policy.
 */
public class Policy {
    
    /** The m type. */
    private final String mType;
    
    /** The m content. */
    private final String mContent;
    
    /** The m date. */
    private final Date mDate;
    
    /**
	 * Instantiates a new policy.
	 * 
	 * @param type
	 *            the type
	 * @param content
	 *            the content
	 * @param date
	 *            the date
	 */
    public Policy(String type, String content, Date date) {
        mType = type;
        mContent = content;
        mDate = date;
    }
    
    /**
	 * Gets the type.
	 * 
	 * @return the type
	 */
    public String getType() {
        return mType;
    }
    
    /**
	 * Gets the content.
	 * 
	 * @return the content
	 */
    public String getContent() {
        return mContent;
    }
    
    /**
	 * Gets the date.
	 * 
	 * @return the date
	 */
    public Date getDate() {
        return mDate;
    }
    
}
