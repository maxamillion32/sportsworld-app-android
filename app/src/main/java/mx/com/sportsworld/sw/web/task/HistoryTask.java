package mx.com.sportsworld.sw.web.task;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import java.util.Calendar;

import android.app.Activity;
import android.os.AsyncTask;

import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.json.JsonParser;
import mx.com.sportsworld.sw.net.resource.Resource;
import mx.com.sportsworld.sw.parser.UserHistoryParser;
import mx.com.sportsworld.sw.pojo.UserHistoryPojo;
import mx.com.sportsworld.sw.web.HttpGetClient;

/**
 * The Class HistoryTask.
 */
public class HistoryTask extends
		AsyncTask<UserHistoryPojo, Void, UserHistoryPojo> {

	/** The result pojo. */
	UserHistoryPojo resultPojo;
	
	/** The url. */
	String url = "";
	
	/** The http client. */
	HttpGetClient httpClient = null;
	
	/** The response. */
	ResponseInterface response = null;
	
	/** The history parser. */
	UserHistoryParser historyParser;
	
	/** The parser. */
	JsonParser parser;
	
	/** The ctx. */
	Activity ctx;

	/**
	 * Instantiates a new history task.
	 * 
	 * @param response
	 *            the response
	 * @param activity
	 *            the activity
	 */
	public HistoryTask(ResponseInterface response, Activity activity) {
		this.response = response;
		ctx = activity;
	}

	// Obtenemos el historial de transacci�nes
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected UserHistoryPojo doInBackground(UserHistoryPojo... arg0) {

		for (UserHistoryPojo pojo : arg0) {
			httpClient = new HttpGetClient(createUrl(pojo), ctx);
			historyParser = new UserHistoryParser();
			JsonParser parser = new JsonParser();

			pojo.setJson(httpClient.excuteGet());
			resultPojo = new UserHistoryPojo();
			if (pojo.getJson().equals("TimeOut")) {

				resultPojo.setStatus(false);
				resultPojo.setMessage("TimeOut");
				break;
			}

			pojo = parser.parseJson(pojo);

			if (!pojo.isStatus()) {
				resultPojo.setStatus(false);
				resultPojo.setMessage("TimeOut");
				break;
			}
			resultPojo = historyParser.parse(pojo.getData());
			resultPojo.setStatus(pojo.isStatus());

		}

		return resultPojo;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(UserHistoryPojo result) {
		response.onResultResponse(result);
	}

	/**
	 * Creates the url.
	 * 
	 * @param pojo
	 *            the pojo
	 * @return the string
	 */
	public String createUrl(UserHistoryPojo pojo) {

		String inicioMes = setCorrectDate(pojo.getFechaInicio().get(
				Calendar.MONTH) + 1);
		String inicioDia = setCorrectDate(pojo.getFechaInicio().get(
				Calendar.DAY_OF_MONTH));
		String finMes = setCorrectDate(pojo.getFechaFin().get(Calendar.MONTH) + 1);
		String finDia = setCorrectDate(pojo.getFechaFin().get(
				Calendar.DAY_OF_MONTH));

		url = Resource.URL_API_BASE + "/loyalty/user/history/";
//		url += "194142/";
		url += pojo.getId_user()+"/";
		url += pojo.getFechaInicio().get(Calendar.YEAR) + 1 + "/";
		url += inicioMes + "/";
		url += inicioDia + "/";
		url += pojo.getFechaFin().get(Calendar.YEAR) + "/";
		url += finMes + "/";
		url += finDia + "/";

		return url;
	}

	/**
	 * Sets the correct date.
	 * 
	 * @param num
	 *            the num
	 * @return the string
	 */
	public String setCorrectDate(int num) {
		String number = "";
		if (num < 10) {
			number = "0" + num;
		} else {
			number = num + "";
		}
		return number;
	}
}
