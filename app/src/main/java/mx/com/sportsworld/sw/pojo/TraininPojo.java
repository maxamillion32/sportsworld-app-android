package mx.com.sportsworld.sw.pojo;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.List;

import mx.com.sportsworld.sw.model.Exercise;

public class TraininPojo {
	
	public List<Exercise> listExcercise;
	public String urlImg;
	public String mExcercise;
	
	public TraininPojo(List<Exercise> listExcercise,String urlImg,String mExcercise){

		this.listExcercise=listExcercise;
		this.urlImg=urlImg;
		this.mExcercise=mExcercise;
	}

	public List<Exercise> getListExcercise() {
		return listExcercise;
	}

	public void setListExcercise(List<Exercise> listExcercise) {
		this.listExcercise = listExcercise;
	}

	public String getUrlImg() {
		return urlImg;
	}

	public void setUrlImg(String urlImg) {
		this.urlImg = urlImg;
	}

	public String getmExcercise() {
		return mExcercise;
	}

	public void setmExcercise(String mExcercise) {
		this.mExcercise = mExcercise;
	}
	
}
