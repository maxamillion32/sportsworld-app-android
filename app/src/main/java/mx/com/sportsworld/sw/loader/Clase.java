package mx.com.sportsworld.sw.loader;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

// TODO: Auto-generated Javadoc

/**
 * The Class Clase.
 */
public class Clase implements Parcelable {
	
	/** The clase. */
	public String clase;
	
	/** The club. */
	public String club;
	
	/** The dia. */
	public String dia;
	
	/** The fin. */
	public String fin;
	
	/** The idinstalacionactividadprogramada. */
	public String idinstalacionactividadprogramada;
	
	/** The inicio. */
	public String inicio;
	
	/** The instructor. */
	public String instructor;
	
	/** The resource_uri. */
	public String resource_uri;
	
	/** The salon. */
	public String salon;
	
	/** The semana. */
	public String semana;

	/* (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		return 0; // To change body of implemented methods use File | Settings |
					// File Templates.
	}

	/**
	 * Instantiates a new clase.
	 *
	 * @param in the in
	 */
	public Clase(Parcel in) {
		readFromParcel(in);
	}

	/**
	 * Instantiates a new clase.
	 */
	public Clase() {

	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(clase);
		dest.writeString(club);
		dest.writeString(dia);
		dest.writeString(fin);
		dest.writeString(idinstalacionactividadprogramada);
		dest.writeString(inicio);
		dest.writeString(instructor);
		dest.writeString(resource_uri);
		dest.writeString(salon);
		dest.writeString(semana);
	}

	/**
	 * Read from parcel.
	 *
	 * @param in the in
	 */
	private void readFromParcel(Parcel in) {
		clase = in.readString();
		club = in.readString();
		dia = in.readString();
		fin = in.readString();
		idinstalacionactividadprogramada = in.readString();
		inicio = in.readString();
		instructor = in.readString();
		resource_uri = in.readString();
		salon = in.readString();
		semana = in.readString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return clase;
	}

	/**
	 * Gets the fecha de clase.
	 *
	 * @return the fecha de clase
	 */
	public Calendar getFechaDeClase() {
		Calendar cal = Calendar.getInstance();
		cal.roll(Calendar.WEEK_OF_YEAR,
				Integer.parseInt(semana) - cal.get(Calendar.WEEK_OF_YEAR));
		if (dia.equals("Lunes")) {
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		} else if (dia.equals("Martes")) {
			cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		} else if (dia.equals("Miercoles")) {
			cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		} else if (dia.equals("Jueves")) {
			cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		} else if (dia.equals("Viernes")) {
			cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		} else if (dia.equals("Sabado")) {
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		} else if (dia.equals("Domingo")) {
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		}
		return cal;
	}

	/** The Constant CREATOR. */
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Clase createFromParcel(Parcel in) {
			return new Clase(in);
		}

		public Clase[] newArray(int size) {
			return new Clase[size];
		}
	};
}
