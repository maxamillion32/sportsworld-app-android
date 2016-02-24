package mx.com.sportsworld.sw.parser;

/**
 * The Class ExerciseDay.
 * 
 * @author Jose Torres Fuentes Ironbit
 */

public class ExerciseDay {

    /** The m id. */
    private final long mId;
    
    /** The m active. */
    private final boolean mActive;
    
    /**
	 * Instantiates a new exercise day.
	 * 
	 * @param id
	 *            the id
	 * @param active
	 *            the active
	 */
    public ExerciseDay(long id, boolean active) {
        mId = id;
        mActive = active;
    }

    /**
	 * Gets the id.
	 * 
	 * @return the id
	 */
    public long getId() {
        return mId;
    }    
    
    /**
	 * Checks if is active.
	 * 
	 * @return true, if is active
	 */
    public boolean isActive() {
        return mActive;
    }
    
}
