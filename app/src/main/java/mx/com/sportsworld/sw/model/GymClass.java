package mx.com.sportsworld.sw.model;

/**
 * The Class GymClass.
 * 
 * @author Jose Torres Fuentes Ironbit
 */
public class GymClass {

    /** The m ideal capacity. */
    private final int mIdealCapacity;
    
    /** The m in high demand. */
    private final boolean mInHighDemand;
    
    /** The m name. */
    private final String mName;
    
    /** The m salon. */
    private final String mSalon;
    
    /** The m club. */
    private final String mClub;
    
    /** The m reservations count. */
    private final int mReservationsCount;
    
    /** The m current capacity. */
    private final int mCurrentCapacity;
    
    /** The m maximum capacity. */
    private final int mMaximumCapacity;
    
    /** The m available from. */
    private final long mAvailableFrom;
    
    /** The m available until. */
    private final long mAvailableUntil;
    
    /** The m confirmed reservations count. */
    private final int mConfirmedReservationsCount;
    
    /** The m starts at. */
    private final long mStartsAt;
    
    /** The m finish at. */
    private final long mFinishAt;
    
    /** The m coach name. */
    private final String mCoachName;
    
    /** The m facility programed activity id. */
    private final long mFacilityProgramedActivityId;
    
    /**�The m agendarClase */
    private final String mAgendarClase;


	public String getmAgendarClase() {
		return mAgendarClase;
	}

	/**
	 * Instantiates a new gym class.
	 * 
	 * @param idealCapacity
	 *            the ideal capacity
	 * @param inHighDemand
	 *            the in high demand
	 * @param name
	 *            the name
	 * @param salon
	 *            the salon
	 * @param club
	 *            the club
	 * @param reservationsCount
	 *            the reservations count
	 * @param currentCapacity
	 *            the current capacity
	 * @param maximumCapacity
	 *            the maximum capacity
	 * @param availableFrom
	 *            the available from
	 * @param availableUntil
	 *            the available until
	 * @param confirmedReservationsCount
	 *            the confirmed reservations count
	 * @param startsAt
	 *            the starts at
	 * @param finishAt
	 *            the finish at
	 * @param coachName
	 *            the coach name
	 * @param facilityProgramedActivityId
	 *            the facility programed activity id
	 */
    public GymClass(int idealCapacity, boolean inHighDemand, String name,String salon,String club, int reservationsCount,
            int currentCapacity, int maximumCapacity, long availableFrom, long availableUntil,
            int confirmedReservationsCount, long startsAt,long finishAt, String coachName,
            long facilityProgramedActivityId
            , String agendarClases
            ) {
        mIdealCapacity = idealCapacity;
        mInHighDemand = inHighDemand;
        mName = name;
        mSalon=salon;
        mClub=club;
        mReservationsCount = reservationsCount;
        mCurrentCapacity = currentCapacity;
        mMaximumCapacity = maximumCapacity;
        mAvailableFrom = availableFrom;
        mAvailableUntil = availableUntil;
        mConfirmedReservationsCount = confirmedReservationsCount;
        mStartsAt = startsAt;
        mFinishAt= finishAt;
        mCoachName = coachName;
        mFacilityProgramedActivityId = facilityProgramedActivityId;
        mAgendarClase = agendarClases;
    }

    /**
	 * Gets the ideal capacity.
	 * 
	 * @return the ideal capacity
	 */
    public int getIdealCapacity() {
        return mIdealCapacity;
    }

    /**
	 * Checks if is in high demand.
	 * 
	 * @return true, if is in high demand
	 */
    public boolean isInHighDemand() {
        return mInHighDemand;
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
	 * Gets the reservations count.
	 * 
	 * @return the reservations count
	 */
    public int getReservationsCount() {
        return mReservationsCount;
    }

    /**
	 * Gets the current capacity.
	 * 
	 * @return the current capacity
	 */
    public int getCurrentCapacity() {
        return mCurrentCapacity;
    }

    /**
	 * Gets the maximun capacity.
	 * 
	 * @return the maximun capacity
	 */
    public int getMaximunCapacity() {
        return mMaximumCapacity;
    }

    /**
	 * Gets the available from.
	 * 
	 * @return the available from
	 */
    public long getAvailableFrom() {
        return mAvailableFrom;
    }
    
    /**
	 * Gets the available until.
	 * 
	 * @return the available until
	 */
    public long getAvailableUntil() {
        return mAvailableUntil;
    }

    /**
	 * Gets the confirmed reservations count.
	 * 
	 * @return the confirmed reservations count
	 */
    public int getConfirmedReservationsCount() {
        return mConfirmedReservationsCount;
    }

    /**
	 * Gets the starts at.
	 * 
	 * @return the starts at
	 */
    public long getStartsAt() {
        return mStartsAt;
    }

    /**
	 * Gets the coach name.
	 * 
	 * @return the coach name
	 */
    public String getCoachName() {
        return mCoachName;
    }

    /**
	 * Gets the facility programed activity id.
	 * 
	 * @return the facility programed activity id
	 */
    public long getFacilityProgramedActivityId() {
        return mFacilityProgramedActivityId;
    }

	/**
	 * Gets the m salon.
	 * 
	 * @return the m salon
	 */
	public String getmSalon() {
		return mSalon;
	}

	/**
	 * Gets the m finish at.
	 * 
	 * @return the m finish at
	 */
	public long getmFinishAt() {
		return mFinishAt;
	}

	/**
	 * Gets the m club.
	 * 
	 * @return the m club
	 */
	public String getmClub() {
		return mClub;
	}

}
