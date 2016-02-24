package mx.com.sportsworld.sw.web.task;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */

import android.app.Activity;
import android.os.AsyncTask;

import mx.com.sportsworld.sw.activity.events.ResponseInterface;
import mx.com.sportsworld.sw.json.JsonParser;
import mx.com.sportsworld.sw.net.resource.Resource;
import mx.com.sportsworld.sw.pojo.ClassPojo;
import mx.com.sportsworld.sw.preferences.SportsWorldPreferences;
import mx.com.sportsworld.sw.web.HttpPostClient;

/**
 * The Class BookClassTask.
 */
public class BookClassTask extends AsyncTask<ClassPojo, Void, ClassPojo> {

	/** The post. */
	HttpPostClient post = null;
	
	/** The result. */
	String result = "";
	
	/** The parser. */
	JsonParser parser = null;
	
	/** The result pojo. */
	ClassPojo resultPojo = new ClassPojo();
	
	/** The response. */
	ResponseInterface response = null;
	
	/** The act. */
	public static Activity act;

	/**
	 * Instantiates a new book class task.
	 * 
	 * @param response
	 *            the response
	 */
	public BookClassTask(ResponseInterface response) {
		this.response = response;
	}

	//Reservamos la clase en el servidor
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected ClassPojo doInBackground(ClassPojo... pojo) {
		// TODO Auto-generated method stub
		post = new HttpPostClient(Resource.URL_API_BASE + "/class/reservation/");

		for (ClassPojo datos : pojo) {
			post.postData("origin", "4");
			post.postData("idconfirm", datos.getIdconfirm());
			post.postData("employed_id", datos.getEmployed_id());
			post.postData("user_id", datos.getId_user());
			post.postData("idinstactprg", datos.getIdinstactprg());
			post.postData("confirm", datos.getConfirm());
			post.postData("classdate", datos.getClassdate());

			if (SportsWorldPreferences.isChckBoxAdvice(act)) {
				datos.setJson(post.postExecute(act));

				if (datos.getJson().equals("TimeOut")) {
					resultPojo = new ClassPojo();
					resultPojo.setStatus(false);
					resultPojo.setMessage("TimeOut");
					break;
				}
				resultPojo = parseResponse(datos);
			} else {
				resultPojo.setStatus(false);
				resultPojo.setMessage("interno");
			}

		}

		return resultPojo;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(ClassPojo result) {
		response.onResultResponse(result);
	}

	/**
	 * Parses the response.
	 * 
	 * @param pojo
	 *            the pojo
	 * @return the class pojo
	 */
	public ClassPojo parseResponse(ClassPojo pojo) {
		parser = new JsonParser();
		pojo = parser.parseJson(pojo);
		return pojo;
	}


}
