package mx.com.sportsworld.sw.model;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.List;

import mx.com.sportsworld.sw.parser.ExerciseDay;

/**
 * The Class ExerciseWeek.
 */
public class ExerciseWeek {

    /** The m id. */
    private final long mId;
    
    /** The m day ids. */
    private final List<ExerciseDay> mDayIds;
    
    /** The m active. */
    private boolean mActive;
    
    /**
	 * Instantiates a new exercise week.
	 * 
	 * @param id
	 *            the id
	 * @param dayIds
	 *            the day ids
	 */
    public ExerciseWeek(long id, List<ExerciseDay> dayIds) {
        mId = id;
        mDayIds = dayIds;
    }

    /**
	 * Sets the active.
	 * 
	 * @param active
	 *            the new active
	 */
    public void setActive(boolean active) {
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
	 * Gets the day ids.
	 * 
	 * @return the day ids
	 */
    public List<ExerciseDay> getDayIds() {
        return mDayIds;
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
