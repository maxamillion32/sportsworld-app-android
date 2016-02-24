package mx.com.sportsworld.sw.net.resource;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.io.IOException;
import java.util.Locale;

import org.json.JSONException;

import android.content.Context;

import mx.com.sportsworld.sw.model.Routine;
import mx.com.sportsworld.sw.net.RequestResultParser;
import mx.com.sportsworld.sw.net.RequestResultParser.RequestResult;
import mx.com.sportsworld.sw.net.RestClient.Response;
import mx.com.sportsworld.sw.net.SportsWorldRequestMaker;
import mx.com.sportsworld.sw.parser.RoutineParser;

/**
 * The Class RoutineResource.
 */
public class RoutineResource {

	/** The Constant URL_FETCH_ROUTINES. */
	private static final String URL_FETCH_ROUTINES = Resource.URL_API_BASE
			+ "/routines/%1$d/%2$d/";

	/** The Constant URL_GET_ROUTINE. */
	private static final String URL_GET_ROUTINE = Resource.URL_API_BASE
	// + "/routine/details/%1$d/%2$d/%3$d/%4$d/%5$d/";
			+ "/routine/details/%1$d/%2$d/%3$d/%4$d/13/";

	/** The Constant URL_ENABLE_ROUTINE. */
	private static final String URL_ENABLE_ROUTINE = Resource.URL_API_BASE
			+ "/routine/save/";

	/** The Constant URL_UPDATE_ROUTINE_STATUS. */
	private static final String URL_UPDATE_ROUTINE_STATUS = Resource.URL_API_BASE
			+ "/routine/status/update/";

	/** The Constant URL_UPDATE_ROUTINE_PROGRESS. */
	private static final String URL_UPDATE_ROUTINE_PROGRESS = Resource.URL_API_BASE
			+ "/routine/%1$d/%2$d/update/";

	/**
	 * Fetch routines.
	 * 
	 * @param context
	 *            the context
	 * @param goalId
	 *            the goal id
	 * @param levelId
	 *            the level id
	 * @return the request result
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws JSONException
	 *             the jSON exception
	 */
	public static RequestResult<Routine> fetchRoutines(Context context,
			long goalId, long levelId) throws IOException, JSONException {

		final String completeUrl = String.format(Locale.US, URL_FETCH_ROUTINES,
				goalId, levelId);

		final SportsWorldRequestMaker requestMaker = new SportsWorldRequestMaker(
				context);
		final Response response = requestMaker.get(completeUrl,
				null /* keyValues */, true /* useAuthToken */);

		final RoutineParser parser = new RoutineParser();
		final RequestResultParser<Routine> resultParser = new RequestResultParser<Routine>();
		final RequestResult<Routine> result = resultParser.parseWith(response,
				parser);
		return result;

	}

	/**
	 * Gets the routine.
	 * 
	 * @param context
	 *            the context
	 * @param userId
	 *            the user id
	 * @param routineId
	 *            the routine id
	 * @param age
	 *            the age
	 * @param weight
	 *            the weight
	 * @param genderId
	 *            the gender id
	 * @return the routine
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws JSONException
	 *             the jSON exception
	 */
	public static RequestResult<Routine> getRoutine(Context context,
			long userId, long routineId, int age, double weight, long genderId)
			throws IOException, JSONException {

		final String completeUrl = Resource.URL_API_BASE + "/routine/details/"
				+ userId + "/" + routineId + "/" + age + "/" + (int) weight
				+ "/" + genderId;

		final SportsWorldRequestMaker requestMaker = new SportsWorldRequestMaker(
				context);
		final Response response = requestMaker.get(completeUrl,
				null /* keyValues */, true /* useAuthToken */);

		final RoutineParser parser = new RoutineParser();
		final RequestResultParser<Routine> resultParser = new RequestResultParser<Routine>();
		final RequestResult<Routine> result = resultParser.parseWith(response,
				parser);
		return result;
	}

}
