package mx.com.sportsworld.sw.web.task;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.facebook.Response;
import com.facebook.Session;

import org.apache.http.params.HttpParams;

import java.security.NoSuchAlgorithmException;

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
 * Created by Qualtop on 24/02/2016.
 */
public class CreateUserTask extends AsyncTask <UserPojo, Void, UserPojo> {

    JsonParser parser = null;

    public boolean clearData;

    public static Context mContext;

    HttpPostClient post = null;
    
    UserPojo pojo = null;
    
    UserProfileJson jsonParse = null;
    private String mCurrentUserID;

    public CreateUserTask(ResponseInterface responseInterface) {

    }

    @Override
    protected UserPojo doInBackground(UserPojo... params) {
        parser = new JsonParser();

        if (clearData)
            logOut();

        for(UserPojo datos : params){
            String value = cifrarPass(datos);
            post = new HttpPostClient(Resource.URL_API_BASE + "/login_upster/");  ///metodo nuevo para la generacion de Usuario.
            datos.setJson(post.postExecute(value));
            
            if(datos.getJson().equals("TimeOut")){
                pojo = new UserPojo();
                pojo.setStatus(false);
                pojo.setMessage("TimeOut");
                break;
            }
            
            pojo = parser.parseJson(datos);
            
            if(!pojo.isStatus())break;
            
            jsonParse = new UserProfileJson(pojo.getData());
            
            datos = jsonParse.parse();
            
            String userID = createMember(datos);
            
            setCurrentUserID(userID);
            
        }
        
        

        return null;
    }

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

    private String cifrarPass(UserPojo pojo) {
        String authKey = "";
        try {
            authKey = SportsWorldAccountManager.buildAuthKey(
                    pojo.getUsername(), pojo.getPassword());
            Log.i("LogIron", "Authentication: " + authKey);
        } catch (NoSuchAlgorithmException e) {

        }

        return authKey;
    }

    private void logOut() {
        Session session = Session.getActiveSession();
        if ((session != null) && session.isOpened()) {
            session.closeAndClearTokenInformation();
        }

        clearUserData();
    }

    private void clearUserData() {
        final Context context = mContext;
        SportsWorldPreferences.clearAllPreferences(context);
    }


    public void setCurrentUserID(String userId) {
        SportsWorldPreferences.setCurrentUserId(mContext, userId);
    }

    public String getCurrentUserID() {
        return mCurrentUserID;
    }
}
