package mx.com.sportsworld.sw.pojo;

/**
 * @author Jose Torres Fuentes Ironbit
 * 
 */
public class AwardItemPojo extends MainPojo {

	private String puntos;
	private String disponibilidad;
	private String idLealtadPremios;
	private String imagen;
	private String premio;
	private String description;
	private boolean pressed = false;
	private int position;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isPressed() {
		return pressed;
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPuntos() {
		return puntos;
	}

	public void setPuntos(String puntos) {
		this.puntos = puntos;
	}

	public String getDisponibilidad() {
		return disponibilidad;
	}

	public void setDisponibilidad(String disponibilidad) {
		this.disponibilidad = disponibilidad;
	}

	public String getIdLealtadPremios() {
		return idLealtadPremios;
	}

	public void setIdLealtadPremios(String idLealtadPremios) {
		this.idLealtadPremios = idLealtadPremios;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getPremio() {
		return premio;
	}

	public void setPremio(String premio) {
		this.premio = premio;
	}

}
