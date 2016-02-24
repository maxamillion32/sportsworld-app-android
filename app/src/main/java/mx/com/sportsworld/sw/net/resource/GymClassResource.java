package mx.com.sportsworld.sw.net.resource;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.Context;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;

import mx.com.sportsworld.sw.BuildConfig;
import mx.com.sportsworld.sw.model.GymClass;
import mx.com.sportsworld.sw.net.RequestResultParser;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.RestClient.Response;
import mx.com.sportsworld.sw.net.SportsWorldRequestMaker;
import mx.com.sportsworld.sw.parser.GymClassParser;

/**
 * Executes web api calls related to gym classes.
 * 
 */
public class GymClassResource {

//    private static final String URL_FETCH_GYM_CLASSES = "https://app.sportsworld.com.mx/api/v2"
    	    /** The Constant URL_FETCH_GYM_CLASSES. */
private static final String URL_FETCH_GYM_CLASSES = Resource.URL_API_BASE
    		
            + "/class/%1$d/%2$d/%3$02d/%4$02d/";

//    private static final String URL_BOOK_GYM_CLASS = "https://app.sportsworld.com.mx/api/v2"
    	    /** The Constant URL_BOOK_GYM_CLASS. */
private static final String URL_BOOK_GYM_CLASS = Resource.URL_API_BASE
    		+ "/class/reservation/";

    /** The Constant KEY_VALUE_FACILITY_PROGAMED_ACTIVITY_ID. */
    private static final String KEY_VALUE_FACILITY_PROGAMED_ACTIVITY_ID = "idinstactprg";
    
    /** The Constant KEY_VALUE_USER_ID. */
    private static final String KEY_VALUE_USER_ID = "user_id";
    
    /** The Constant KEY_VALUE_EMPLOYEE_ID. */
    private static final String KEY_VALUE_EMPLOYEE_ID = "employed_id";
    
    /** The Constant KEY_VALUE_CLASS_DATE. */
    private static final String KEY_VALUE_CLASS_DATE = "classdate";
    
    /** The Constant KEY_VALUE_ORIGIN. */
    private static final String KEY_VALUE_ORIGIN = "origin";
    
    /** The Constant KEY_VALUE_CONFIRM. */
    private static final String KEY_VALUE_CONFIRM = "confirm";
    
    /** The Constant KEY_VALUE_CONFIRM_ID. */
    private static final String KEY_VALUE_CONFIRM_ID = "idconfirm";

    /**
	 * Fetch gym classes.
	 * 
	 * @param context
	 *            the context
	 * @param branchId
	 *            the branch id
	 * @param year
	 *            the year
	 * @param month
	 *            the month
	 * @param dayOfMonth
	 *            the day of month
	 * @return the request result
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws JSONException
	 *             the jSON exception
	 */
    public static RequestResult<GymClass> fetchGymClasses(Context context, long branchId, int year,
            int month, int dayOfMonth) throws IOException, JSONException {
        final String completeUrl = String.format(Locale.US, URL_FETCH_GYM_CLASSES, branchId, year,
                month, dayOfMonth);

        final SportsWorldRequestMaker requestMaker = new SportsWorldRequestMaker(context);
        final Response response = requestMaker
                .get(completeUrl, null /* keyValues */, true /* useAuthToken */);

        final GymClassParser parser = new GymClassParser();
        final RequestResultParser<GymClass> resultParser = new RequestResultParser<GymClass>();
        final RequestResult<GymClass> result = resultParser.parseWith(response, parser);

        return result;
   }

    /**
	 * Book gym class.
	 * 
	 * @param context
	 *            the context
	 * @param facilityProgramedActivityId
	 *            the facility programed activity id
	 * @param userId
	 *            the user id
	 * @param classDate
	 *            the class date
	 * @return the request result
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws JSONException
	 *             the jSON exception
	 */
    public static RequestResult<Void> bookGymClass(Context context,
            long facilityProgramedActivityId, long userId, long classDate) throws IOException,
            JSONException {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        final String classDateStr = dateFormatter.format(new Date(classDate));
        final int origin = (BuildConfig.DEBUG ? 3 : 4);

        final Map<String, String> keyValues = new HashMap<String, String>();
        keyValues.put(KEY_VALUE_FACILITY_PROGAMED_ACTIVITY_ID,
                String.valueOf(facilityProgramedActivityId));
        keyValues.put(KEY_VALUE_USER_ID, String.valueOf(userId));
        keyValues.put(KEY_VALUE_EMPLOYEE_ID, "0");
        keyValues.put(KEY_VALUE_CLASS_DATE, classDateStr);
        keyValues.put(KEY_VALUE_ORIGIN, String.valueOf(origin));
        keyValues.put(KEY_VALUE_CONFIRM, "0");
        keyValues.put(KEY_VALUE_CONFIRM_ID, "0");

        final SportsWorldRequestMaker requestMaker = new SportsWorldRequestMaker(context);
        final Response response = requestMaker
                .post(URL_BOOK_GYM_CLASS, keyValues, true /* useAuthToken */);
        final RequestResultParser<Void> resultParser = new RequestResultParser<Void>();
        final RequestResult<Void> result = resultParser.parseWith(response, null /* jsonParser */);

        return result;
    }

}
