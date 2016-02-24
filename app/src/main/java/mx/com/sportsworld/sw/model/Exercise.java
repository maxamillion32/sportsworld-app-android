package mx.com.sportsworld.sw.model;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.text.TextUtils;

/**
 * The Class Exercise.
 */
public class Exercise {

    /** The no current day id. */
    public static  long NO_CURRENT_DAY_ID = 0L;
    
    /** The no current week id. */
    public static  long NO_CURRENT_WEEK_ID = 0L;
    
    /** The unit repetitions. */
    public static String UNIT_REPETITIONS = "repeticiones";
    
    /** The unit time. */
    public static String UNIT_TIME = "min";
    
    /** The delimiter. */
    public static String DELIMITER = ",";
    
    /** The m id. */
    public long mId;
    
    /** The m week id. */
    public  long mWeekId;
    
    /** The m day id. */
    public  long mDayId;
    
    /** The m muscle worked. */
    public String mMuscleWorked;
    
    /** The m active. */
    public  boolean mActive;
    
    /** The m circuit id. */
    public  long mCircuitId;
    
    /** The m example images men urls. */
    public  String[] mExampleImagesMenUrls;
    
    /** The m example images women urls. */
    public  String[] mExampleImagesWomenUrls;
    
    /** The m minimum value. */
    public  double mMinimumValue;
    
    /** The m maximum value. */
    public  double mMaximumValue;
    
    /** The m minimum weight lb. */
    public  double mMinimumWeightLb;
    
    /** The m maximum weight lb. */
    public  double mMaximumWeightLb;
    
    /** The m unit. */
    public String mUnit;
    
    /** The m sports category id. */
    public  long mSportsCategoryId;
    
    /** The m exercise name. */
    public String mExerciseName;
    
    /** The m instructions. */
    public String mInstructions;
    
    /** The m measurement unit type id. */
    public  long mMeasurementUnitTypeId;
    
    /** The m circuit name. */
    public String mCircuitName;
    
    /** The m video url id. */
    public  long mVideoUrlId;
    
    /** The m series. */
    public  double mSeries;
    
    /** The m sport id. */
    public  long mSportId;
    
    /** The m done. */
    public boolean mDone;
    
    /** The m listener. */
    public OnDoneChangedListener mListener;

    /**
	 * Instantiates a new exercise.
	 * 
	 * @param id
	 *            the id
	 * @param muscleWorked
	 *            the muscle worked
	 * @param active
	 *            the active
	 * @param circuitId
	 *            the circuit id
	 * @param exampleImagesMenUrls
	 *            the example images men urls
	 * @param exampleImagesWomenUrls
	 *            the example images women urls
	 * @param minimumValue
	 *            the minimum value
	 * @param maximumValue
	 *            the maximum value
	 * @param minimumWeightLb
	 *            the minimum weight lb
	 * @param maximumWeightLb
	 *            the maximum weight lb
	 * @param unit
	 *            the unit
	 * @param sportsCategoryId
	 *            the sports category id
	 * @param exerciseName
	 *            the exercise name
	 * @param instructions
	 *            the instructions
	 * @param measurementUnitTypeId
	 *            the measurement unit type id
	 * @param circuitName
	 *            the circuit name
	 * @param videoUrlId
	 *            the video url id
	 * @param series
	 *            the series
	 * @param sportId
	 *            the sport id
	 * @param weekId
	 *            the week id
	 * @param dayId
	 *            the day id
	 */
    public Exercise(long id, String muscleWorked, boolean active, long circuitId,
            String[] exampleImagesMenUrls, String[] exampleImagesWomenUrls, double minimumValue,
            double maximumValue, double minimumWeightLb, double maximumWeightLb, String unit,
            long sportsCategoryId, String exerciseName, String instructions,
            long measurementUnitTypeId, String circuitName, long videoUrlId, double series,
            long sportId, long weekId, long dayId) {
        mMuscleWorked = muscleWorked;
        mActive = active;
        mCircuitId = circuitId;
        mExampleImagesMenUrls = exampleImagesMenUrls;
        mExampleImagesWomenUrls = exampleImagesWomenUrls;
        mMinimumValue = minimumValue;
        mMaximumValue = maximumValue;
        mMinimumWeightLb = minimumWeightLb;
        mMaximumWeightLb = maximumWeightLb;
        mUnit = unit;
        mSportsCategoryId = sportsCategoryId;
        mExerciseName = exerciseName;
        mInstructions = instructions;
        mMeasurementUnitTypeId = measurementUnitTypeId;
        mCircuitName = circuitName;
        mVideoUrlId = videoUrlId;
        mSeries = series;
        mSportId = sportId;
        mWeekId = weekId;
        mDayId = dayId;
        mId = id;
    }
    
    /**
	 * Instantiates a new exercise.
	 * 
	 * @param id
	 *            the id
	 * @param muscleWorked
	 *            the muscle worked
	 * @param active
	 *            the active
	 * @param circuitId
	 *            the circuit id
	 * @param exampleImagesMenUrls
	 *            the example images men urls
	 * @param exampleImagesWomenUrls
	 *            the example images women urls
	 * @param minimumValue
	 *            the minimum value
	 * @param maximumValue
	 *            the maximum value
	 * @param minimumWeightLb
	 *            the minimum weight lb
	 * @param maximumWeightLb
	 *            the maximum weight lb
	 * @param unit
	 *            the unit
	 * @param sportsCategoryId
	 *            the sports category id
	 * @param exerciseName
	 *            the exercise name
	 * @param instructions
	 *            the instructions
	 * @param measurementUnitTypeId
	 *            the measurement unit type id
	 * @param circuitName
	 *            the circuit name
	 * @param videoUrlId
	 *            the video url id
	 * @param series
	 *            the series
	 * @param sportId
	 *            the sport id
	 * @param weekId
	 *            the week id
	 * @param dayId
	 *            the day id
	 * @param done
	 *            the done
	 */
    public Exercise(long id, String muscleWorked, boolean active, long circuitId,
            String[] exampleImagesMenUrls, String[] exampleImagesWomenUrls, double minimumValue,
            double maximumValue, double minimumWeightLb, double maximumWeightLb, String unit,
            long sportsCategoryId, String exerciseName, String instructions,
            long measurementUnitTypeId, String circuitName, long videoUrlId, double series,
            long sportId, long weekId, long dayId, boolean done) {
        mMuscleWorked = muscleWorked;
        mActive = active;
        mCircuitId = circuitId;
        mExampleImagesMenUrls = exampleImagesMenUrls;
        mExampleImagesWomenUrls = exampleImagesWomenUrls;
        mMinimumValue = minimumValue;
        mMaximumValue = maximumValue;
        mMinimumWeightLb = minimumWeightLb;
        mMaximumWeightLb = maximumWeightLb;
        mUnit = unit;
        mSportsCategoryId = sportsCategoryId;
        mExerciseName = exerciseName;
        mInstructions = instructions;
        mMeasurementUnitTypeId = measurementUnitTypeId;
        mCircuitName = circuitName;
        mVideoUrlId = videoUrlId;
        mSeries = series;
        mSportId = sportId;
        mId = id;
        mDone = done;
        mWeekId = weekId;
        mDayId = dayId;
    }

    /**
	 * Gets the muscle worked.
	 * 
	 * @return the muscle worked
	 */
    public String getMuscleWorked() {
        return mMuscleWorked;
    }

    /**
	 * Checks if is active.
	 * 
	 * @return true, if is active
	 */
    public boolean isActive() {
        return mActive;
    }

    /**
	 * Gets the circuit id.
	 * 
	 * @return the circuit id
	 */
    public long getCircuitId() {
        return mCircuitId;
    }

    /**
	 * Gets the example images men urls.
	 * 
	 * @return the example images men urls
	 */
    public String[] getExampleImagesMenUrls() {
        return mExampleImagesMenUrls;
    }

    /**
	 * Gets the example images men urls joined.
	 * 
	 * @return the example images men urls joined
	 */
    public String getExampleImagesMenUrlsJoined() {
        return TextUtils.join(DELIMITER, mExampleImagesMenUrls);
    }

    /**
	 * Gets the example images women urls.
	 * 
	 * @return the example images women urls
	 */
    public String[] getExampleImagesWomenUrls() {
        return mExampleImagesWomenUrls;
    }

    /**
	 * Gets the example images women urls joined.
	 * 
	 * @return the example images women urls joined
	 */
    public String getExampleImagesWomenUrlsJoined() {
        return TextUtils.join(DELIMITER, mExampleImagesWomenUrls);
    }

    /**
	 * Gets the minimum value.
	 * 
	 * @return the minimum value
	 */
    public double getMinimumValue() {
        return mMinimumValue;
    }

    /**
	 * Gets the maximum value.
	 * 
	 * @return the maximum value
	 */
    public double getMaximumValue() {
        return mMaximumValue;
    }

    /**
	 * Gets the minimum weight lb.
	 * 
	 * @return the minimum weight lb
	 */
    public double getMinimumWeightLb() {
        return mMinimumWeightLb;
    }

    /**
	 * Gets the maximum weight lb.
	 * 
	 * @return the maximum weight lb
	 */
    public double getMaximumWeightLb() {
        return mMaximumWeightLb;
    }

    /**
	 * Gets the unit.
	 * 
	 * @return the unit
	 */
    public String getUnit() {
        return mUnit;
    }

    /**
	 * Gets the sports category id.
	 * 
	 * @return the sports category id
	 */
    public long getSportsCategoryId() {
        return mSportsCategoryId;
    }

    /**
	 * Gets the name.
	 * 
	 * @return the name
	 */
    public String getName() {
        return mExerciseName;
    }

    /**
	 * Gets the instructions.
	 * 
	 * @return the instructions
	 */
    public String getInstructions() {
        return mInstructions;
    }

    /**
	 * Gets the measurement unit type id.
	 * 
	 * @return the measurement unit type id
	 */
    public long getMeasurementUnitTypeId() {
        return mMeasurementUnitTypeId;
    }

    /**
	 * Checks if is in circuit.
	 * 
	 * @return true, if is in circuit
	 */
    public boolean isInCircuit() {
        return mCircuitId == 0;
    }

    /**
	 * Gets the circuit name.
	 * 
	 * @return the circuit name
	 */
    public String getCircuitName() {
        return mCircuitName;
    }

    /**
	 * Gets the video url id.
	 * 
	 * @return the video url id
	 */
    public long getVideoUrlId() {
        return mVideoUrlId;
    }

    /**
	 * Gets the series.
	 * 
	 * @return the series
	 */
    public double getSeries() {
        return mSeries;
    }

    /**
	 * Gets the sport id.
	 * 
	 * @return the sport id
	 */
    public long getSportId() {
        return mSportId;
    }

    /**
	 * Checks if is done.
	 * 
	 * @return true, if is done
	 */
    public boolean isDone() {
        return mDone;
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
	 * Sets the m muscle worked.
	 * 
	 * @param mMuscleWorked
	 *            the new m muscle worked
	 */
	public void setmMuscleWorked(String mMuscleWorked) {
		this.mMuscleWorked = mMuscleWorked;
	}
	

	/**
	 * Sets the m exercise name.
	 * 
	 * @param mExerciseName
	 *            the new m exercise name
	 */
	public void setmExerciseName(String mExerciseName) {
		this.mExerciseName = mExerciseName;
	}

	/**
	 * Sets the m circuit name.
	 * 
	 * @param mCircuitName
	 *            the new m circuit name
	 */
	public void setmCircuitName(String mCircuitName) {
		this.mCircuitName = mCircuitName;
	}

	/**
	 * Sets the done.
	 * 
	 * @param done
	 *            the new done
	 */
	public synchronized void setDone(boolean done) {
        mDone = done;
        mListener.onDoneChanged(Exercise.this, done);
    }

    /**
	 * Sets the on done changedlistener.
	 * 
	 * @param listener
	 *            the new on done changedlistener
	 */
    public void setOnDoneChangedlistener(OnDoneChangedListener listener) {
        mListener = listener;
    }

    /**
	 * The listener interface for receiving onDoneChanged events. The class that
	 * is interested in processing a onDoneChanged event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's
	 * <code>addOnDoneChangedListener<code> method. When
	 * the onDoneChanged event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see OnDoneChangedEvent
	 */
    public static interface OnDoneChangedListener {
        
        /**
		 * On done changed.
		 * 
		 * @param exercise
		 *            the exercise
		 * @param newValue
		 *            the new value
		 */
        void onDoneChanged(Exercise exercise, boolean newValue);
    }

}
