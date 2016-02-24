package mx.com.sportsworld.sw.pojo;

import java.util.List;

/**
 * @author Jose Torres Fuentes Ironbit
 * 
 */
public class BranchPojo extends MainPojo {
	private List<BranchItemPojo> listBranch;
	private double mLatitude;
	private double mLongitude;

	public List<BranchItemPojo> getListBranch() {
		return listBranch;
	}

	public void setListBranch(List<BranchItemPojo> listBranch) {
		this.listBranch = listBranch;
	}

	public double getmLatitude() {
		return mLatitude;
	}

	public void setmLatitude(double mLatitude) {
		this.mLatitude = mLatitude;
	}

	public double getmLongitude() {
		return mLongitude;
	}

	public void setmLongitude(double mLongitude) {
		this.mLongitude = mLongitude;
	}

}
