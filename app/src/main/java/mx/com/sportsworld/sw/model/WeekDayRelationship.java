package mx.com.sportsworld.sw.model;

/**
 * The Class WeekDayRelationship.
 * 
 * @author Jose Torres Fuentes Ironbit
 */
public class WeekDayRelationship {

    /** The m week id. */
    private final long mWeekId;
    
    /** The m day id. */
    private final long mDayId;
    
    /** The m active. */
    private final boolean mActive;
    
    /**
	 * Instantiates a new week day relationship.
	 * 
	 * @param weekId
	 *            the week id
	 * @param dayId
	 *            the day id
	 * @param active
	 *            the active
	 */
    public WeekDayRelationship(long weekId, long dayId, boolean active) {
        mWeekId = weekId;
        mDayId = dayId;
        mActive = active;
    }
    
    /**
	 * Gets the week id.
	 * 
	 * @return the week id
	 */
    public long getWeekId() {
        return mWeekId;
    }
    
    /**
	 * Gets the day id.
	 * 
	 * @return the day id
	 */
    public long getDayId() {
        return mDayId;
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
