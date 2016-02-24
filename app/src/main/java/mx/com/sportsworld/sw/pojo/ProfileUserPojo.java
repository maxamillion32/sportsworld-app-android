package mx.com.sportsworld.sw.pojo;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
public class ProfileUserPojo extends MainPojo{

	public double height;
	public double weight;
	public String dob;
	long memUniqId;

	public long getMemUniqId() {
		return memUniqId;
	}

	public void setMemUniqId(long memUniqId) {
		this.memUniqId = memUniqId;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}



}
