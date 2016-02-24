package mx.com.sportsworld.sw.model;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.Date;

/**
 * The Class UserProfile.
 */
public class UserProfile {

	/** The Constant GENDER_FEMALE. */
	public static final int GENDER_FEMALE = 12;
	
	/** The Constant GENDER_MALE. */
	public static final int GENDER_MALE = 13;
	
	/** The m user id. */
	private final String mUserId;
	
	/** The m id club. */
	private final int mIdClub;
	
	/** The m weight. */
	private final double mWeight;
	
	/** The m club name. */
	private final String mClubName;
	
	/** The m member number. */
	private final int mMemberNumber;
	
	/** The m routine id. */
	private final int mRoutineId;
	
	/** The m name. */
	private final String mName;
	
	/** The m gender. */
	private final String mGender;
	
	/** The m age. */
	private final int mAge;
	
	/** The m registration date. */
	private final String mRegistrationDate;
	
	/** The m gender id. */
	private final int mGenderId;
	
	/** The m email. */
	private final String mEmail;
	
	/** The m height. */
	private final double mHeight;
	
	/** The m mem uniq id. */
	private final long mMemUniqId;

	/**
	 * Instantiates a new user profile.
	 * 
	 * @param userId
	 *            the user id
	 * @param idClub
	 *            the id club
	 * @param weight
	 *            the weight
	 * @param clubName
	 *            the club name
	 * @param memberNumber
	 *            the member number
	 * @param idRoutine
	 *            the id routine
	 * @param name
	 *            the name
	 * @param gender
	 *            the gender
	 * @param age
	 *            the age
	 * @param registrationDate
	 *            the registration date
	 * @param genderId
	 *            the gender id
	 * @param email
	 *            the email
	 * @param height
	 *            the height
	 * @param memUniqId
	 *            the mem uniq id
	 */
	@SuppressWarnings("deprecation")
	public UserProfile(String userId, int idClub, double weight,
			String clubName, int memberNumber, int idRoutine, String name,
			String gender, int age, Date registrationDate, int genderId,
			String email, double height, long memUniqId) {
		mUserId = userId;
		mIdClub = idClub;
		mWeight = weight;
		mClubName = clubName;
		mMemberNumber = memberNumber;
		mRoutineId = idRoutine;
		mName = name;
		mGender = gender;
		mAge = age;
		mRegistrationDate = registrationDate.getDay() + " "
				+ getMonth(registrationDate.getMonth()) + " "
				+ (registrationDate.getYear() + 1990);
		mGenderId = genderId;
		mEmail = email;
		mHeight = height;
		mMemUniqId = memUniqId;
	}

	/**
	 * Gets the user id.
	 * 
	 * @return the user id
	 */
	public String getUserId() {
		return mUserId;
	}

	/**
	 * Gets the club id.
	 * 
	 * @return the club id
	 */
	public int getClubId() {
		return mIdClub;
	}

	/**
	 * Gets the weight.
	 * 
	 * @return the weight
	 */
	public double getWeight() {
		return mWeight;
	}

	/**
	 * Gets the club name.
	 * 
	 * @return the club name
	 */
	public String getClubName() {
		return mClubName;
	}

	/**
	 * Gets the member number.
	 * 
	 * @return the member number
	 */
	public int getMemberNumber() {
		return mMemberNumber;
	}

	/**
	 * Gets the routine id.
	 * 
	 * @return the routine id
	 */
	public int getRoutineId() {
		return mRoutineId;
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
	 * Gets the gender.
	 * 
	 * @return the gender
	 */
	public String getGender() {
		return mGender;
	}

	/**
	 * Gets the age.
	 * 
	 * @return the age
	 */
	public int getAge() {
		return mAge;
	}

	// public long getBirthday() {
	// final Time today = new Time();
	// today.setToNow();
	// final Time birthday = new Time();
	// birthday.monthDay = today.monthDay;
	// birthday.month = today.month;
	// birthday.year = today.year - mAge;
	// return birthday.normalize(true /* ignoreDst */);
	// }

	/**
	 * Gets the registration date.
	 * 
	 * @return the registration date
	 */
	public String getRegistrationDate() {
		return mRegistrationDate;
	}

	/**
	 * Gets the gender id.
	 * 
	 * @return the gender id
	 */
	public int getGenderId() {
		return mGenderId;
	}

	/**
	 * Gets the email.
	 * 
	 * @return the email
	 */
	public String getEmail() {
		return mEmail;
	}

	/**
	 * Gets the mem uniq id.
	 * 
	 * @return the mem uniq id
	 */
	public long getMemUniqId() {
		return mMemUniqId;
	}

	/**
	 * Gets the height.
	 * 
	 * @return the height
	 */
	public double getHeight() {
		return mHeight;
	}

	/**
	 * Checks if is man.
	 * 
	 * @return true, if is man
	 */
	public boolean isMan() {
		return (mGenderId == GENDER_MALE);
	}

	/**
	 * Checks if is woman.
	 * 
	 * @return true, if is woman
	 */
	public boolean isWoman() {
		return (mGenderId == GENDER_FEMALE);
	}

	/**
	 * Gets the month.
	 * 
	 * @param mont
	 *            the mont
	 * @return the month
	 */
	public String getMonth(int mont) {
		String month = "";
		switch (mont) {
		case 1:
			month = "Enero";
			break;
		case 2:
			month = "Febrero";
			break;
		case 3:
			month = "Marzo";
			break;
		case 4:
			month = "Abril";
			break;
		case 5:
			month = "Mayo";
			break;
		case 6:
			month = "Junio";
			break;
		case 7:
			month = "Julio";
			break;
		case 8:
			month = "Agosto";
			break;
		case 9:
			month = "Septiembre";
			break;
		case 10:
			month = "Octubre";
			break;
		case 11:
			month = "Noviembre";
			break;
		case 12:
			month = "Diciembre";
			break;

		default:
			month = "";
			break;
		}
		return month;
	}

}
