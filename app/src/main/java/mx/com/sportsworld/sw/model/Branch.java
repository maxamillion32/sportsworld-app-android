package mx.com.sportsworld.sw.model;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.text.TextUtils;

/**
 * 
 * If a branch has latitude 0.0d and longitude 0.0d, then that branch does not have a location.
 * 
 */
public class Branch implements Comparable<Branch> {

    /** The Constant DELIMITER. */
    public static final String DELIMITER = ",";
    
    /** The Constant TYPE_FAVORITE. */
    public static final String TYPE_FAVORITE = "favorites";
    
    /** The Constant TYPE_PROVINCE. */
    public static final String TYPE_PROVINCE = "m_area";
    
    /** The Constant TYPE_COMING_SOON. */
    public static final String TYPE_COMING_SOON = "prev";
    
    /** The Constant TYPE_DF. */
    public static final String TYPE_DF = "df";
    
    /** The m latitude. */
    private final double mLatitude;
    
    /** The m longitude. */
    private final double mLongitude;
    
    /** The m distance. */
    private final double mDistance;
    
    /** The m state id. */
    private final long mStateId;
    
    /** The m address. */
    private final String mAddress;
    
    /** The m schedule. */
    private final String mSchedule;
    
    /** The m un id. */
    private final long mUnId;
    
    /** The m key. */
    private final String mKey;
    
    /** The m d count. */
    private final int mDCount;
    
    /** The m name. */
    private final String mName;
    
    /** The m video url. */
    private final String mVideoUrl;
   
    /** The m url 360. */
    private final String mUrl360;
    
    /** The m pre order. */
    private final int mPreOrder;
    
    /** The m state name. */
    private final String mStateName;
    
    /** The m phone. */
    private final String mPhone;
    
    /** The m type. */
    private final String mType;
    
    /** The m favorite. */
    private final boolean mFavorite;
    
    /** The m facilities. */
    private final String[] mFacilities;
    
    /** The m images facilities. */
    private final String[] mImagesFacilities;
    
    /** The m images urls. */
    private final String[] mImagesUrls;

    /**
	 * Instantiates a new branch.
	 * 
	 * @param latitude
	 *            the latitude
	 * @param longitude
	 *            the longitude
	 * @param distance
	 *            the distance
	 * @param stateId
	 *            the state id
	 * @param address
	 *            the address
	 * @param schedule
	 *            the schedule
	 * @param unId
	 *            the un id
	 * @param key
	 *            the key
	 * @param dCount
	 *            the d count
	 * @param name
	 *            the name
	 * @param videoUrl
	 *            the video url
	 * @param preOrder
	 *            the pre order
	 * @param stateName
	 *            the state name
	 * @param phone
	 *            the phone
	 * @param type
	 *            the type
	 * @param favorite
	 *            the favorite
	 * @param facilities
	 *            the facilities
	 * @param imagesFacilities
	 *            the images facilities
	 * @param imagesUrls
	 *            the images urls
	 */
    public Branch(double latitude, double longitude, double distance, long stateId, String address,
            String schedule, long unId, String key, int dCount, String name, String videoUrl,
            int preOrder, String stateName, String phone, String type, boolean favorite,
            String[] facilities,String[] imagesFacilities, String[] imagesUrls
            ,String url360
            ) {
        mLatitude = latitude;
        mLongitude = longitude;
        mDistance = distance;
        mStateId = stateId;
        mAddress = address;
        mSchedule = schedule;
        mUnId = unId;
        mKey = key;
        mDCount = dCount;
        mName = name;
        mVideoUrl = videoUrl;
        mPreOrder = preOrder;
        mStateName = stateName;
        mPhone = phone;
        mType = type;
        mFavorite = favorite;
        mFacilities = facilities;
        mImagesUrls = imagesUrls;
        mImagesFacilities= imagesFacilities;
        mUrl360 = url360;
    }

    /**
	 * Gets the latitude.
	 * 
	 * @return the latitude
	 */
    public double getLatitude() {
        return mLatitude;
    }

    /**
	 * Gets the longitude.
	 * 
	 * @return the longitude
	 */
    public double getLongitude() {
        return mLongitude;
    }

    /**
	 * Gets the distance.
	 * 
	 * @return the distance
	 */
    public double getDistance() {
        return mDistance;
    }

    /**
	 * Gets the state id.
	 * 
	 * @return the state id
	 */
    public long getStateId() {
        return mStateId;
    }

    /**
	 * Gets the address.
	 * 
	 * @return the address
	 */
    public String getAddress() {
        return mAddress;
    }

    /**
	 * Gets the schedule.
	 * 
	 * @return the schedule
	 */
    public String getSchedule() {
        return mSchedule;
    }

    /**
	 * Gets the un id.
	 * 
	 * @return the un id
	 */
    public long getUnId() {
        return mUnId;
    }

    /**
	 * Gets the key.
	 * 
	 * @return the key
	 */
    public String getKey() {
        return mKey;
    }

    /**
	 * Gets the d count.
	 * 
	 * @return the d count
	 */
    public int getDCount() {
        return mDCount;
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
	 * Gets the video url.
	 * 
	 * @return the video url
	 */
    public String getVideoUrl() {
        return mVideoUrl;
    }
    
    /**
	 * Gets the  url 360.
	 * 
	 * @return the  url 360
	 */
    public String getUrl360() {
        return mUrl360;
    }

    /**
	 * Gets the pre order.
	 * 
	 * @return the pre order
	 */
    public int getPreOrder() {
        return mPreOrder;
    }

    /**
	 * Gets the state name.
	 * 
	 * @return the state name
	 */
    public String getStateName() {
        return mStateName;
    }

    /**
	 * Gets the phone.
	 * 
	 * @return the phone
	 */
    public String getPhone() {
        return mPhone;
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
	 * Checks if is favorite.
	 * 
	 * @return true, if is favorite
	 */
    public boolean isFavorite() {
        return mFavorite;
    }

    /**
	 * Gets the facilities.
	 * 
	 * @return the facilities
	 */
    public String[] getFacilities() {
        return mFacilities;
    }

    /**
	 * Gets the facilities joined.
	 * 
	 * @return the facilities joined
	 */
    public String getFacilitiesJoined() {
        return TextUtils.join(DELIMITER, mFacilities);
    }

    /**
	 * Gets the images url.
	 * 
	 * @return the images url
	 */
    public String[] getImagesUrl() {
        return mImagesUrls;
    }

    /**
	 * Gets the images urls joined.
	 * 
	 * @return the images urls joined
	 */
    public String getImagesUrlsJoined() {
        return TextUtils.join(DELIMITER, mImagesUrls);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return mName;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Branch another) {
        return mName.compareTo(another.getName());        
    }

	/**
	 * Gets the m images facilities.
	 * 
	 * @return the m images facilities
	 */
	public String[] getmImagesFacilities() {
		return mImagesFacilities;
	}
	
	/**
	 * Gets the m images fac joined.
	 * 
	 * @return the m images fac joined
	 */
	public String getmImagesFacJoined() {
		return TextUtils.join(DELIMITER, mImagesFacilities);
	}

}
