package mx.com.sportsworld.sw.pojo;

import mx.com.sportsworld.sw.model.Branch;

/**
 * @author Jose Torres Fuentes Ironbit
 * 
 */
public class BranchItemPojo extends MainPojo {

	public static final String DELIMITER = ",";
	public static final String TYPE_FAVORITE = "favorites";
	public static final String TYPE_PROVINCE = "m_area";
	public static final String TYPE_COMING_SOON = "prev";
	public static final String TYPE_DF = "df";
	private String mAddress;
	private String id_club;
	private String mSchedule;
	private String mName;
	private double mLatitude;
	private double mLongitude;
	private double mDistance;
	private long mStateId;
	private long mUnId;
	private String mKey;
	private int mDCount;
	private String mVideoUrl;
	private int mPreOrder;
	private String mStateName;
	private String mPhone;
	private String mType;
	private boolean mFavorite;
	private String[] mFacilities;
	private String[] mImagesFacilities;
	private String[] mImagesUrls;

	public String getmAddress() {
		return mAddress;
	}

	public void setmAddress(String mAddress) {
		this.mAddress = mAddress;
	}

	public String getId_club() {
		return id_club;
	}

	public void setId_club(String id_club) {
		this.id_club = id_club;
	}

	public String getmSchedule() {
		return mSchedule;
	}

	public void setmSchedule(String mSchedule) {
		this.mSchedule = mSchedule;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public double getmLatitude() {
		return mLatitude;
	}

	public void setmLatitude(double mLatitude) {
		this.mLatitude = mLatitude;
	}

	public double getmLongitude() {
		return mLongitude;
	}

	public void setmLongitude(double mLongitude) {
		this.mLongitude = mLongitude;
	}

	public double getmDistance() {
		return mDistance;
	}

	public void setmDistance(double mDistance) {
		this.mDistance = mDistance;
	}

	public long getmStateId() {
		return mStateId;
	}

	public void setmStateId(long mStateId) {
		this.mStateId = mStateId;
	}

	public long getmUnId() {
		return mUnId;
	}

	public void setmUnId(long mUnId) {
		this.mUnId = mUnId;
	}

	public String getmKey() {
		return mKey;
	}

	public void setmKey(String mKey) {
		this.mKey = mKey;
	}

	public int getmDCount() {
		return mDCount;
	}

	public void setmDCount(int mDCount) {
		this.mDCount = mDCount;
	}

	public String getmVideoUrl() {
		return mVideoUrl;
	}

	public void setmVideoUrl(String mVideoUrl) {
		this.mVideoUrl = mVideoUrl;
	}

	public int getmPreOrder() {
		return mPreOrder;
	}

	public void setmPreOrder(int mPreOrder) {
		this.mPreOrder = mPreOrder;
	}

	public String getmStateName() {
		return mStateName;
	}

	public void setmStateName(String mStateName) {
		this.mStateName = mStateName;
	}

	public String getmPhone() {
		return mPhone;
	}

	public void setmPhone(String mPhone) {
		this.mPhone = mPhone;
	}

	public String getmType() {
		return mType;
	}

	public void setmType(String mType) {
		this.mType = mType;
	}

	public boolean ismFavorite() {
		return mFavorite;
	}

	public void setmFavorite(boolean mFavorite) {
		this.mFavorite = mFavorite;
	}

	public String[] getmFacilities() {
		return mFacilities;
	}

	public void setmFacilities(String[] mFacilities) {
		this.mFacilities = mFacilities;
	}

	public String[] getmImagesFacilities() {
		return mImagesFacilities;
	}

	public void setmImagesFacilities(String[] mImagesFacilities) {
		this.mImagesFacilities = mImagesFacilities;
	}

	public String[] getmImagesUrls() {
		return mImagesUrls;
	}

	public void setmImagesUrls(String[] mImagesUrls) {
		this.mImagesUrls = mImagesUrls;
	}

	public String toString() {
		return mName;
	}

	public int compareTo(Branch another) {
		return mName.compareTo(another.getName());
	}
}
