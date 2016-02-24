package mx.com.sportsworld.sw.account;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.facebook.Session;
import com.facebook.model.GraphUser;
import mx.com.sportsworld.sw.activity.PoliciesActivity;
import mx.com.sportsworld.sw.model.UserProfile;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.resource.LoginResource;
import mx.com.sportsworld.sw.pojo.UserPojo;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.provider.SportsWorldContract;
import mx.com.sportsworld.sw.utils.GeneralUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// TODO: Auto-generated Javadoc

/**
 * Manages whatever is related to accounts. Knows who and how is logged in.
 * 
 * @author Pablo Enrique Morales Breck <pablo@touchtatsic.mx>
 * 
 */
public class SportsWorldAccountManager {

	/** The Constant MESSAGE_INVALID_CREDENTIALS. */
	public static final String MESSAGE_INVALID_CREDENTIALS = "Wrong username or password";
	
	/** The Constant SHA_1_ALGORYTHM. */
	private static final String SHA_1_ALGORYTHM = "SHA-1";
	
	/** The Constant USER_PASSWORD_SHA1_SEPARATOR. */
	private static final String USER_PASSWORD_SHA1_SEPARATOR = ".";
	
	/** The Constant MESSAGE_INVALID_CREDENTIALS_FORMAT. */
	private static final String MESSAGE_INVALID_CREDENTIALS_FORMAT = "Credencial Invalida";
	
	/** The Constant ANONYMOUS_USER_ID. */
	private static final String ANONYMOUS_USER_ID = "0";
	
	/** The m context. */
	private Context mContext;

	/**
	 * Instantiates a new sports world account manager.
	 *
	 * @param context the context
	 */
	public SportsWorldAccountManager(Context context) {
		mContext = context.getApplicationContext();
	}

	/**
	 * Start authentication flow.
	 *
	 * @param activity the activity
	 */
	@SuppressLint("NewApi")
	// We check which build version we are using.
	public void startAuthenticationFlow(Activity activity) {

		final Intent authFlow = new Intent(activity /* context */,
				PoliciesActivity.class);

		int flags;
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.HONEYCOMB) {
			flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK;
		} else {
			flags = Intent.FLAG_ACTIVITY_NEW_TASK;
		}

		authFlow.setFlags(flags);
		activity.startActivity(authFlow);
		activity.finish();

	}

	/**
	 * This class must not be used on UI thread.
	 *
	 * @param username the username
	 * @param password the password
	 * @return the request result
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JSONException the jSON exception
	 * @throws IncompatibleDeviceException the incompatible device exception
	 */
	public RequestResult<UserProfile> logInAsMember(String username,
			String password) throws IOException, JSONException,
			IncompatibleDeviceException {

		logOut(); // Just to be sure we are starting with a clean environment

		/* First, we generate the auth token */
		String authKey;
		try {
			authKey = buildAuthKey(username, password);
		} catch (NoSuchAlgorithmException e) {
			throw new IncompatibleDeviceException();
		}

		/*
		 * And then, we send our authToken to the server. If it is correct, we
		 * get the user Profile. Otherwise, we get an error message and throw
		 * the appropriate exceptions.
		 */
		final RequestResult<UserProfile> result = LoginResource.logInAsMember(
				mContext, authKey);

		if (result.getResponseCode() == HttpURLConnection.HTTP_OK) {

			final String message = result.getMessage();
			if (MESSAGE_INVALID_CREDENTIALS.equals(message)) {
				return result;
			} else if (MESSAGE_INVALID_CREDENTIALS_FORMAT.equals(message)) {
				throw new InvalidCredentialsFormatException();
			}

			/*
			 * Save user profile on the database. We only have one userProfile
			 * on the result's data, so we can safely ask for the first object
			 * on the list.
			 */
			final UserProfile userProfile = result.getData().get(0);
			final String userId = createMember(userProfile);

			/* Finally, we save our current user id and his/her authToken... */
			final Context context = mContext;
			setCurrentUserId(userId);
			SportsWorldPreferences.setAuthToken(context, authKey);

			/* ...and send the result to our caller */

		}

		return result;

	}

	/**
	 * Builds the auth key.
	 *
	 * @param username the username
	 * @param password the password
	 * @return the string
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	public static String buildAuthKey(String username, String password)
			throws NoSuchAlgorithmException {

		/* First, encrypt the password with SHA-1 */
		final String passwordSha1;
		MessageDigest md = null;
		md = MessageDigest.getInstance(SHA_1_ALGORYTHM);
		final byte[] passwordAsByteArray = password.getBytes();
		md.update(passwordAsByteArray, 0, passwordAsByteArray.length);
		final byte[] digested = md.digest();
		passwordSha1 = byteArrayToHexString(digested);

		/* Then, concatenate username, a dot and password */
		final int userPasswordSha1TotalLength = passwordSha1.length()
				+ username.length() + USER_PASSWORD_SHA1_SEPARATOR.length();
		final StringBuilder strBuilder = new StringBuilder(
				userPasswordSha1TotalLength);
		final String userPasswordSha1 = strBuilder.append(username)
				.append(USER_PASSWORD_SHA1_SEPARATOR).append(passwordSha1)
				.toString();

		/* Finally, encrypt in base64 the new string from the last step */
		final String authKey = Base64
				.encodeToString(userPasswordSha1.getBytes(), Base64.URL_SAFE
						| Base64.NO_WRAP/* flags */);

		Log.i("LogIron", authKey);
		return authKey;

	}

	/**
	 * Converts given byte array to a hex string.
	 *
	 * @param bytes the bytes
	 * @return the string
	 */
	private static String byteArrayToHexString(byte[] bytes) {
		StringBuffer buffer = new StringBuffer();
		final int bytesCount = bytes.length;
		for (int i = 0; i < bytesCount; i++) {
			final int bytesAnd = (int) (bytes[i] & 0xff);
			if (bytesAnd < 0x10) {
				buffer.append("0");
			}
			buffer.append(Long.toString(bytesAnd, 16));
		}
		return buffer.toString();
	}

	/**
	 * Creates the member.
	 *
	 * @param userProfile the user profile
	 * @return the string
	 */
	private String createMember(UserProfile userProfile) {

		final ContentValues values = new ContentValues();
		values.put(SportsWorldContract.User._ID, userProfile.getUserId());
		values.put(SportsWorldContract.User.MEMBER_NUMBER,
				userProfile.getMemberNumber());
		values.put(SportsWorldContract.User.NAME, userProfile.getName());
		values.put(SportsWorldContract.User.AGE, userProfile.getAge());
		values.put(SportsWorldContract.User.GENDER_ID,
				userProfile.getGenderId());
		values.put(SportsWorldContract.User.GENDER, userProfile.getGender());
		values.put(SportsWorldContract.User.HEIGHT, userProfile.getHeight());
		values.put(SportsWorldContract.User.WEIGHT, userProfile.getWeight());
		values.put(SportsWorldContract.User.ROUTINE_ID,
				userProfile.getRoutineId());
		values.put(SportsWorldContract.User.REGISTRATION_DATE,
				userProfile.getRegistrationDate());
		values.put(SportsWorldContract.User.EMAIL, userProfile.getEmail());
		values.put(SportsWorldContract.User.CLUB_ID, userProfile.getClubId());
		values.put(SportsWorldContract.User.CLUB_NAME,
				userProfile.getClubName());
		values.put(SportsWorldContract.User.MEM_UNIQ_ID,
				userProfile.getMemUniqId());

		final ContentResolver resolver = mContext.getContentResolver();
		final Uri newUri = resolver.insert(
				SportsWorldContract.User.CONTENT_URI, values);
		return SportsWorldContract.User.getUserId(newUri);

	}

	/**
	 * Creates the guest.
	 *
	 * @param user the user
	 * @return the string
	 */
	private String createGuest(GraphUser user) {
		final String userId = user.getId();
		final ContentValues values = new ContentValues();
		values.put(SportsWorldContract.User._ID, userId);
		values.put(SportsWorldContract.User.NAME, user.getName());

		int age = GeneralUtils.generateAge(user.getBirthday(), false);

		values.put(SportsWorldContract.User.AGE, age);

		SportsWorldPreferences.setGuestId(mContext, userId);
		SportsWorldPreferences.setGuestName(mContext, user.getName());
		SportsWorldPreferences.setGuestAge(mContext, age + "");

		String gender = (String) user.getProperty("gender");

		if (gender.equals("female")) {
			values.put(SportsWorldContract.Guest.GENDER_ID, 1);
			SportsWorldPreferences.setGuestGender(mContext, "Femenino");
		}
		if (gender.equals("male")) {
			values.put(SportsWorldContract.Guest.GENDER_ID, 0);
			SportsWorldPreferences.setGuestGender(mContext, "Masculino");
		}

		final ContentResolver resolver = mContext.getContentResolver();
		final Uri newUri = resolver.insert(
				SportsWorldContract.User.CONTENT_URI, values);

		setCurrentUserId(userId);

		return SportsWorldContract.User.getUserId(newUri);
	}

	/**
	 * Sets the current user id.
	 *
	 * @param userId the new current user id
	 */
	private void setCurrentUserId(String userId) {
		SportsWorldPreferences.setCurrentUserId(mContext, userId);
	}

	/**
	 * Gets the current user id.
	 *
	 * @return the current user id
	 */
	public String getCurrentUserId() {
		return SportsWorldPreferences.getCurrentUserId(mContext);
	}

	/**
	 * Log in as guest.
	 *
	 * @param user the user
	 */
	public void logInAsGuest(GraphUser user) {
		clearUserData(); // Just to be sure we are starting with a clean
							// environment
		createGuest(user);
	}

	/**
	 * Update guest.
	 *
	 * @param user the user
	 * @return true, if successful
	 */
	public boolean updateGuest(GraphUser user) {
		final String userId = user.getId();
		final ContentValues values = new ContentValues();
		values.put(SportsWorldContract.User._ID, userId);
		values.put(SportsWorldContract.User.NAME, user.getName());
		values.put(SportsWorldContract.User.AGE, user.getBirthday());

		final Uri uri = SportsWorldContract.User.buildUserUri(userId);
		final ContentResolver resolver = mContext.getContentResolver();
		final int rows = resolver
				.update(uri, values, null /* where */, null /* whereArgs */);
		return rows > 0;
	}

	/**
	 * Update guest.
	 *
	 * @param user the user
	 * @return true, if successful
	 */
	public boolean updateGuest(UserPojo user) {
		final String userId = user.getId_user();
		final ContentValues values = new ContentValues();
		values.put(SportsWorldContract.Guest.HEIGHT, user.getmAge());
		values.put(SportsWorldContract.Guest.WEIGHT, user.getmAge());

		final Uri uri = SportsWorldContract.Guest.buildGuestUri(userId);
		final ContentResolver resolver = mContext.getContentResolver();
		final int rows = resolver
				.update(uri, values, null /* where */, null /* whereArgs */);
		return rows > 0;
	}

	/**
	 * Log in as anonymous.
	 */
	public void logInAsAnonymous() {
		logOut(); // Just to be sure we are starting with a clean environment
		setCurrentUserId(ANONYMOUS_USER_ID);
	}

	/**
	 * This just clears our user's preferences and database. Do not use it on UI
	 * thread.
	 */
	public void logOut() {

		Session session = Session.getActiveSession();
		if ((session != null) && session.isOpened()) {
			session.closeAndClearTokenInformation();
		}

		clearUserData();

	}

	/**
	 * Clear user data.
	 */
	public void clearUserData() {
		final Context context = mContext;
		SportsWorldPreferences.clearAllPreferences(context);
	}

	/**
	 * Checks if is logged in as member.
	 *
	 * @return true, if is logged in as member
	 */
	public boolean isLoggedInAsMember() {
		return (getAuthToken() != null);
	}

	/**
	 * Checks if is logged in as guest.
	 *
	 * @return true, if is logged in as guest
	 */
	public boolean isLoggedInAsGuest() {
		final Session session = Session.getActiveSession();
		final boolean sessionOpened = ((session != null) && (session.isOpened()));
		final boolean savedOnDb = (getCurrentUserId() != null);
		//return sessionOpened && savedOnDb;
		return savedOnDb;
	}

	/**
	 * Checks if is logged as anonymous.
	 *
	 * @return true, if is logged as anonymous
	 */
	public boolean isLoggedAsAnonymous() {
		return ANONYMOUS_USER_ID.equals(SportsWorldPreferences
				.getCurrentUserId(mContext));
	}

	/**
	 * Checks if is logged in.
	 *
	 * @return true, if is logged in
	 */
	public boolean isLoggedIn() {
		return (isLoggedInAsMember() || isLoggedInAsGuest() || isLoggedAsAnonymous());
	}

	/**
	 * Gets the auth token.
	 *
	 * @return the auth token
	 */
	public String getAuthToken() {
		return SportsWorldPreferences.getAuthToken(mContext);
	}

	/**
	 * The Class InvalidCredentialsFormatException.
	 */
	public static class InvalidCredentialsFormatException extends
			RuntimeException {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1139634753533865163L;

	}

	/**
	 * The Class IncompatibleDeviceException.
	 */
	public class IncompatibleDeviceException extends Exception {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 705587559271861550L;

	}

}
