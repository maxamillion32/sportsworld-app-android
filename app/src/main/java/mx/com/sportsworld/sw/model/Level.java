package mx.com.sportsworld.sw.model;

/**
 * The Class Level.
 * 
 * @author Jose Torres Fuentes Ironbit
 */
public class Level {

    /** The m description. */
    private final String mDescription;
    
    /** The m id. */
    private final long mId;
    
    /**
	 * Instantiates a new level.
	 * 
	 * @param description
	 *            the description
	 * @param id
	 *            the id
	 */
    public Level(String description, long id) {
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
