package mx.com.sportsworld.sw.web.task;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import mx.com.sportsworld.sw.account.SportsWorldAccountManager;
import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.json.JsonParser;
import mx.com.sportsworld.sw.json.UserProfileJson;
import mx.com.sportsworld.sw.net.resource.Resource;
import mx.com.sportsworld.sw.pojo.ProfileUserPojo;
import mx.com.sportsworld.sw.pojo.UserPojo;
import mx.com.sportsworld.sw.provider.SportsWorldContract;
import mx.com.sportsworld.sw.web.HttpPostClient;

/**
 * The Class MemberProfileUpdateTask.
 */
public class MemberProfileUpdateTask extends
		AsyncTask<ProfileUserPojo, Void, ProfileUserPojo> {

	/** The acount mang. */
	SportsWorldAccountManager acountMang = null;
	
	/** The post. */
	HttpPostClient post = null;
	
	/** The json parse. */
	UserProfileJson jsonParse = null;
	
	/** The pojo. */
	ProfileUserPojo pojo = null;
	
	/** The m context. */
	public static Context mContext;
	
	/** The response. */
	ResponseInterface response = null;
	
	/** The parser. */
	JsonParser parser = null;
	
	/** The pojo res. */
	UserPojo pojoRes = null;

	/**
	 * Instantiates a new member profile update task.
	 * 
	 * @param response
	 *            the response
	 */
	public MemberProfileUpdateTask(ResponseInterface response) {
		this.response = response;
	}

	// Actualizamos en Servidor

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected ProfileUserPojo doInBackground(ProfileUserPojo... arg0) {

		parser = new JsonParser();
		acountMang = getAccountManager();

		for (ProfileUserPojo datos : arg0) {

			post = new HttpPostClient(Resource.URL_API_BASE
					+ "/profile/update/");
			post.postData("user_id", acountMang.getCurrentUserId());
			post.postData("height", datos.getHeight() + "");
			post.postData("weight", datos.getWeight() + "");
			post.postData("dob", datos.getDob().trim());
			post.postData("age", "4");
			post.postData("memunic_id", gerMemUnicId());
			datos.setJson(post.postExecute(mContext));

			if (datos.getJson().equals("TimeOut")) {
				pojo = new ProfileUserPojo();
				pojo.setStatus(false);
				pojo.setMessage("TimeOut");
				break;
			}

			pojo = parser.parseJson(datos);

			if (pojo.isStatus())
				updateUser(datos);

		}
		return pojo;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(ProfileUserPojo result) {
		response.onResultResponse(result);
	}

	/**
	 * Gets the account manager.
	 * 
	 * @return the account manager
	 */
	protected SportsWorldAccountManager getAccountManager() {
		if (acountMang == null) {
			acountMang = new SportsWorldAccountManager(mContext);
		}
		return acountMang;
	}

	/**
	 * Ger mem unic id.
	 * 
	 * @return the string
	 */
	public String gerMemUnicId() {

		final long userId = Long.parseLong(acountMang.getCurrentUserId());
		Long memUniqId = -1L;
		final Uri userUri = SportsWorldContract.User
				.buildUserUri((userId + ""));
		Cursor cursor = null;
		ContentResolver cr = mContext.getContentResolver();
		try {
			cursor = cr
					.query(userUri,
							new String[] { SportsWorldContract.User.MEM_UNIQ_ID },
							null /* selection */, null /* selectionArgs */, null/* sortOrder */);
			if (cursor.moveToFirst()) {
				memUniqId = cursor.getLong(0);
			}
		} catch (Exception ex) {
			Log.i("LogIron", ex.toString());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		if (memUniqId == -1L) {

		}

		return memUniqId + "";

	}

	// Actualizamos en BD
	/**
	 * Update user.
	 * 
	 * @param pojo
	 *            the pojo
	 * @return true, if successful
	 */
	public boolean updateUser(ProfileUserPojo pojo) {

		final ContentValues values = new ContentValues();
		values.put(SportsWorldContract.User.HEIGHT, pojo.getHeight());
		values.put(SportsWorldContract.User.WEIGHT, pojo.getWeight());
		values.put(SportsWorldContract.User.BIRTH_DATE, pojo.getDob());

		final SportsWorldAccountManager accountMngr = getAccountManager();
		final long userId = Long.parseLong(accountMngr.getCurrentUserId());
		final Uri userUri = SportsWorldContract.User.buildUserUri(String
				.valueOf(userId));
		final ContentResolver cr = mContext.getContentResolver();
		cr.update(userUri, values, null /* selection */, null /* selectionArgs */);

		return true;

	}
}
