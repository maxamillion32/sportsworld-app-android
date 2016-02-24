package mx.com.sportsworld.sw.pojo;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.List;

public class AwardPojo extends MainPojo {

	private List<AwardItemPojo> items;

	public List<AwardItemPojo> getItems() {
		return items;
	}

	public void setItems(List<AwardItemPojo> items) {
		this.items = items;
	}

}
