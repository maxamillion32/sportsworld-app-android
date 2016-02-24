package mx.com.sportsworld.sw.pojo;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.Calendar;
import java.util.List;

public class UserHistoryPojo extends MainPojo {

	private List<HistoryPojo> listItems;
	private Calendar fechaInicio;
	private Calendar fechaFin;

	public List<HistoryPojo> getListItems() {
		return listItems;
	}

	public void setListItems(List<HistoryPojo> listItems) {
		this.listItems = listItems;
	}

	public Calendar getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Calendar fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Calendar getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Calendar fechaFin) {
		this.fechaFin = fechaFin;
	}

}
