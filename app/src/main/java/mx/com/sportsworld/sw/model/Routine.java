package mx.com.sportsworld.sw.model;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.List;

import mx.com.sportsworld.sw.pojo.MainPojo;
import mx.com.sportsworld.sw.pojo.TraininPojo;

/**
 * The Class Routine.
 */
public class Routine extends MainPojo{

    /** The m id. */
    private final long mId;
    
    /** The m name. */
    private final String mName;
    
    /** The m nutrition advice. */
    private final String mNutritionAdvice;
    
    /** The m training. */
    private final List<TraininPojo> mTraining;
    
    /** The m week day relationship. */
    private final List<WeekDayRelationship> mWeekDayRelationship;
    
    /** The m routine_name. */
    private final String mRoutine_name;

    /**
	 * Instantiates a new routine.
	 * 
	 * @param id
	 *            the id
	 * @param name
	 *            the name
	 * @param mTraining
	 *            the m training
	 * @param nutritionAdvice
	 *            the nutrition advice
	 * @param weekDayRelationship
	 *            the week day relationship
	 * @param routine_name
	 *            the routine_name
	 */
    public Routine(long id, String name, List<TraininPojo> mTraining, String nutritionAdvice,
            List<WeekDayRelationship> weekDayRelationship, String routine_name) {
        mId = id;
        mName = name;
        this.mTraining = mTraining;
        mNutritionAdvice = nutritionAdvice;
        mWeekDayRelationship = weekDayRelationship;
        mRoutine_name= routine_name;
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
	 * Gets the name.
	 * 
	 * @return the name
	 */
    public String getName() {
        return mName;
    }

  
    /**
	 * Gets the nutrition advice.
	 * 
	 * @return the nutrition advice
	 */
    public String getNutritionAdvice() {
        return mNutritionAdvice;
    }

    /**
	 * Gets the week day relationship.
	 * 
	 * @return the week day relationship
	 */
    public List<WeekDayRelationship> getWeekDayRelationship() {
        return mWeekDayRelationship;
    }

	/**
	 * Gets the m routine_name.
	 * 
	 * @return the m routine_name
	 */
	public String getmRoutine_name() {
		return mRoutine_name;
	}

	/**
	 * Gets the m training.
	 * 
	 * @return the m training
	 */
	public List<TraininPojo> getmTraining() {
		return mTraining;
	}

}
