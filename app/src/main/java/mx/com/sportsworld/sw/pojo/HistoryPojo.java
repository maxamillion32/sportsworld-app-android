package mx.com.sportsworld.sw.pojo;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.util.Calendar;

public class HistoryPojo {

	private String nombreEvento;
	private String club;
	private String puntos;
	private String idPersona;
	private Calendar fechaEvento;
	private String importe;

	public String getNombreEvento() {
		return nombreEvento;
	}

	public void setNombreEvento(String nombreEvento) {
		this.nombreEvento = nombreEvento;
	}

	public String getClub() {
		return club;
	}

	public void setClub(String club) {
		this.club = club;
	}

	public String getPuntos() {
		return puntos;
	}

	public void setPuntos(String puntos) {
		this.puntos = puntos;
	}

	public String getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(String idPersona) {
		this.idPersona = idPersona;
	}

	public Calendar getFechaEvento() {
		return fechaEvento;
	}

	public void setFechaEvento(Calendar fechaEvento) {
		this.fechaEvento = fechaEvento;
	}

	public String getImporte() {
		return importe;
	}

	public void setImporte(String importe) {
		this.importe = importe;
	}

}
