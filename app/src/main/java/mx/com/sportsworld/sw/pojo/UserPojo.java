package mx.com.sportsworld.sw.pojo;

/**
 * @author Jose Torres Fuentes Ironbit
 * 
 */
public class UserPojo extends MainPojo {

	String username;
	String password;
	public static int GENDER_FEMALE = 12;
	public static int GENDER_MALE = 13;
	String mUserId;
	int mIdClub;
	double mWeight;
	String mClubName;
	int mMemberNumber;
	int mRoutineId;
	String mName;
	String mGender;
	String mMemberType;
	String mMainteinment;
	int mAge;
	String mRegisterDate;
	String mBirthDate;
	int mGenderId;
	String mEmail;
	double mHeight;
	long mMemUniqId;
	String secret_key;

	public String getSecret_key() {
		return secret_key;
	}

	public void setSecret_key(String secret_key) {
		this.secret_key = secret_key;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static int getGENDER_FEMALE() {
		return GENDER_FEMALE;
	}

	public static void setGENDER_FEMALE(int gENDER_FEMALE) {
		GENDER_FEMALE = gENDER_FEMALE;
	}

	public static int getGENDER_MALE() {
		return GENDER_MALE;
	}

	public static void setGENDER_MALE(int gENDER_MALE) {
		GENDER_MALE = gENDER_MALE;
	}

	public String getmUserId() {
		return mUserId;
	}

	public void setmUserId(String mUserId) {
		this.mUserId = mUserId;
	}

	public int getmIdClub() {
		return mIdClub;
	}

	public void setmIdClub(int mIdClub) {
		this.mIdClub = mIdClub;
	}

	public double getmWeight() {
		return mWeight;
	}

	public void setmWeight(double mWeight) {
		this.mWeight = mWeight;
	}

	public String getmClubName() {
		return mClubName;
	}

	public void setmClubName(String mClubName) {
		this.mClubName = mClubName;
	}

	public int getmMemberNumber() {
		return mMemberNumber;
	}

	public void setmMemberNumber(int mMemberNumber) {
		this.mMemberNumber = mMemberNumber;
	}

	public int getmRoutineId() {
		return mRoutineId;
	}

	public void setmRoutineId(int mRoutineId) {
		this.mRoutineId = mRoutineId;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmGender() {
		return mGender;
	}

	public void setmGender(String mGender) {
		this.mGender = mGender;
	}

	public int getmAge() {
		return mAge;
	}

	public void setmAge(int mAge) {
		this.mAge = mAge;
	}

	public int getmGenderId() {
		return mGenderId;
	}

	public void setmGenderId(int mGenderId) {
		this.mGenderId = mGenderId;
	}

	public String getmEmail() {
		return mEmail;
	}

	public void setmEmail(String mEmail) {
		this.mEmail = mEmail;
	}

	public double getmHeight() {
		return mHeight;
	}

	public void setmHeight(double mHeight) {
		this.mHeight = mHeight;
	}

	public long getmMemUniqId() {
		return mMemUniqId;
	}

	public void setmMemUniqId(long mMemUniqId) {
		this.mMemUniqId = mMemUniqId;
	}

	public String getmMemberType() {
		return mMemberType;
	}

	public void setmMemberType(String mMemberType) {
		this.mMemberType = mMemberType;
	}

	public String getmMainteinment() {
		return mMainteinment;
	}

	public void setmMainteinment(String mMainteinment) {
		this.mMainteinment = mMainteinment;
	}

	public String getmBirthDate() {
		return mBirthDate;
	}

	public void setmBirthDate(String mBirthDate) {
		this.mBirthDate = mBirthDate;
	}

	public String getmRegisterDate() {
		return mRegisterDate;
	}

	public void setmRegisterDate(String mRegisterDate) {
		this.mRegisterDate = mRegisterDate;
	}

}
