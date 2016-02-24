/**
 * 
 */
package mx.com.sportsworld.sw.web.task;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.utils.SocialKeys;

/**
 * The Class PostTwitterTask.
 * 
 * @author Jose Torres Fuentes 27/09/2013 Ironbit
 */
public class PostTwitterTask extends AsyncTask<String, Void, Boolean> {

	/** The twitter consumer key. */
	static String TWITTER_CONSUMER_KEY = SocialKeys.TW_OAUTH_CONSUMER_KEY;
	
	/** The twitter consumer secret. */
	static String TWITTER_CONSUMER_SECRET = SocialKeys.TW_OAUTH_CONSUMER_SECRET;

	// Preference Constants
	/** The preference name. */
	static String PREFERENCE_NAME = "twitter_oauth";
	
	/** The Constant PREF_KEY_OAUTH_TOKEN. */
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	
	/** The Constant PREF_KEY_OAUTH_SECRET. */
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	
	/** The Constant PREF_KEY_TWITTER_LOGIN. */
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

	/** The Constant TWITTER_CALLBACK_URL. */
	static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

	// Twitter oauth urls
	/** The Constant URL_TWITTER_AUTH. */
	static final String URL_TWITTER_AUTH = "auth_url";
	
	/** The Constant URL_TWITTER_OAUTH_VERIFIER. */
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	
	/** The Constant URL_TWITTER_OAUTH_TOKEN. */
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
	
	/** The success. */
	public boolean success;
	
	/** The act. */
	public Activity act;

	/**
	 * Instantiates a new post twitter task.
	 * 
	 * @param activity
	 *            the activity
	 */
	public PostTwitterTask(Activity activity) {
		act = activity;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected Boolean doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		success = false;
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
		builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);

		// Access Token
		String access_token = SportsWorldPreferences.getKeyOauthToken(act);
		// Access Token Secret
		String access_token_secret = SportsWorldPreferences
				.getKeyOauthSecret(act);

		AccessToken accessToken = new AccessToken(access_token,
				access_token_secret);
		Twitter twitter = new TwitterFactory(builder.build())
				.getInstance(accessToken);

		// Update status
		try {
			twitter4j.Status response = twitter.updateStatus(arg0[0]);
			success = true;
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return success;
	}

	/* */
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(final Boolean result) {
		// TODO Auto-generated method stub
		act.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(result)
				Toast.makeText(act, "Publicaci�n exitosa en Twitter",
						Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(act, "Hubo un error al realizar el post en Twitter",
							Toast.LENGTH_SHORT).show();
			}
		});
	}

}