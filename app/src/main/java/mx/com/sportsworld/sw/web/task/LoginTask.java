package mx.com.sportsworld.sw.web.task;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import java.security.NoSuchAlgorithmException;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.facebook.Session;
import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.json.JsonParser;
import mx.com.sportsworld.sw.json.UserProfileJson;
import mx.com.sportsworld.sw.net.resource.Resource;
import mx.com.sportsworld.sw.pojo.UserPojo;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.provider.SportsWorldContract;
import mx.com.sportsworld.sw.web.HttpPostClient;

/**
 * The Class LoginTask.
 */
public class LoginTask extends AsyncTask<UserPojo, Void, UserPojo> {

	/** The acount mang. */
	SportsWorldAccountManager acountMang = null;
	
	/** The post. */
	HttpPostClient post = null;
	
	/** The json parse. */
	UserProfileJson jsonParse = null;
	
	/** The pojo. */
	UserPojo pojo = null;
	
	/** The m context. */
	public static Context mContext;
	
	/** The response. */
	ResponseInterface response = null;
	
	/** The parser. */
	JsonParser parser = null;
	
	/** The clar data. */
	public boolean clarData = true;

	/**
	 * Instantiates a new login task.
	 * 
	 * @param response
	 *            the response
	 */
	public LoginTask(ResponseInterface response) {
		this.response = response;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected UserPojo doInBackground(UserPojo... params) {
		parser = new JsonParser();

		if (clarData)
			logOut();

		for (UserPojo datos : params) {
			String value = cifrarPass(datos);
			post = new HttpPostClient(Resource.URL_API_BASE + "/login_upster/");
			datos.setJson(post.postExecute(value));

			if (datos.getJson().equals("TimeOut")) {
				pojo = new UserPojo();
				pojo.setStatus(false);
				pojo.setMessage("TimeOut");
				break;
			}

			pojo = parser.parseJson(datos);

			if (!pojo.isStatus())
				break;

			jsonParse = new UserProfileJson(pojo.getData());
			datos = jsonParse.parse();

			String userId = createMember(datos);

			/* Finally, we save our current user id and his/her authToken... */
			setCurrentUserId(userId);

		}
		return pojo;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(UserPojo result) {
		response.onResultResponse(result);
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {

	}

	/**
	 * Cifrar pass.
	 * 
	 * @param pojo
	 *            the pojo
	 * @return the string
	 */
	public String cifrarPass(UserPojo pojo) {
		String authKey = "";
		try {
			authKey = SportsWorldAccountManager.buildAuthKey(
					pojo.getUsername(), pojo.getPassword());
			Log.i("LogIron", "Authentication: " + authKey);
		} catch (NoSuchAlgorithmException e) {

		}

		return authKey;
	}

	// Creamos el usuario
	/**
	 * Creates the member.
	 * 
	 * @param userProfile
	 *            the user profile
	 * @return the string
	 */
	private String createMember(UserPojo userProfile) {

		final ContentValues values = new ContentValues();
		values.put(SportsWorldContract.User._ID, userProfile.getmUserId());
		values.put(SportsWorldContract.User.MEMBER_NUMBER,
				userProfile.getmMemberNumber());
		values.put(SportsWorldContract.User.NAME, userProfile.getmName());
		values.put(SportsWorldContract.User.AGE, userProfile.getmAge());
		values.put(SportsWorldContract.User.GENDER_ID,
				userProfile.getmGenderId());
		values.put(SportsWorldContract.User.GENDER, userProfile.getmGender());
		values.put(SportsWorldContract.User.HEIGHT, userProfile.getmHeight());
		values.put(SportsWorldContract.User.WEIGHT, userProfile.getmWeight());
		values.put(SportsWorldContract.User.ROUTINE_ID,
				userProfile.getmRoutineId());
		values.put(SportsWorldContract.User.REGISTRATION_DATE,
				userProfile.getmRegisterDate());
		values.put(SportsWorldContract.User.BIRTH_DATE,
				userProfile.getmBirthDate());
		values.put(SportsWorldContract.User.EMAIL, userProfile.getmEmail());
		values.put(SportsWorldContract.User.MEMBER_TYPE,
				userProfile.getmMemberType());
		values.put(SportsWorldContract.User.MAINTEINMENT,
				userProfile.getmMainteinment());
		values.put(SportsWorldContract.User.CLUB_ID, userProfile.getmIdClub());
		values.put(SportsWorldContract.User.CLUB_NAME,
				userProfile.getmClubName());
		values.put(SportsWorldContract.User.MEM_UNIQ_ID,
				userProfile.getmMemUniqId());

		final ContentResolver resolver = mContext.getContentResolver();
		final Uri newUri = resolver.insert(
				SportsWorldContract.User.CONTENT_URI, values);

		SportsWorldPreferences.setAuthToken(mContext,
				userProfile.getSecret_key());
		SportsWorldPreferences.setGuestGender(mContext,
				userProfile.getmGender());
		SportsWorldPreferences.setIdClub(mContext, userProfile.getmIdClub());
		return SportsWorldContract.User.getUserId(newUri);

	}

	/**
	 * Sets the current user id.
	 * 
	 * @param userId
	 *            the new current user id
	 */
	private void setCurrentUserId(String userId) {
		SportsWorldPreferences.setCurrentUserId(mContext, userId);
	}

	/**
	 * Log out.
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
}
