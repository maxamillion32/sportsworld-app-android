package mx.com.sportsworld.sw.pojo;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.List;

public class AwardProfilePojo extends MainPojo {

	private List<AwardProfileItemPojo> listItems;

	public List<AwardProfileItemPojo> getListItems() {
		return listItems;
	}

	public void setListItems(List<AwardProfileItemPojo> listItems) {
		this.listItems = listItems;
	}

}
