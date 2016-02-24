package mx.com.sportsworld.sw.model;

/**
 * The Class Goal.
 * 
 * @author Jose Torres Fuentes Ironbit
 */
public class Goal {

    /** The m description. */
    private final String mDescription;
    
    /** The m id. */
    private final long mId;
    
    /**
	 * Instantiates a new goal.
	 * 
	 * @param description
	 *            the description
	 * @param id
	 *            the id
	 */
    public Goal(String description, long id) {
        mDescription = description;
        mId = id;
    }
    
    /**
	 * Gets the description.
	 * 
	 * @return the description
	 */
    public String getDescription() {
        return mDescription;
    }
    
    /**
	 * Gets the id.
	 * 
	 * @return the id
	 */
    public long getId() {
        return mId;
    }
    
}
