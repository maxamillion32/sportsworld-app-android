package mx.com.sportsworld.sw.loader;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.os.Parcel;
import android.os.Parcelable;

// TODO: Auto-generated Javadoc

/**
 * The Class Club.
 */
public class Club implements Parcelable {
    
    /** The clave. */
    public String clave;
    
    /** The direccion. */
    public String direccion;
    
    /** The horario. */
    public String horario;
    
    /** The idun. */
    public String idun;
    
    /** The lat. */
    public String lat;
    
    /** The lon. */
    public String lon;
    
    /** The nombre. */
    public String nombre;
    
    /** The preventa. */
    public String preventa;
    
    /** The resource uri. */
    public String resourceUri;
    
    /** The ruta video. */
    public String rutaVideo;
    
    /** The ruta video360. */
    public String ruta360;
    
    /** The telefono. */
    public String telefono;
    
    /** The distancia. */
    public String distancia;

    /**
     * Instantiates a new club.
     *
     * @param in the in
     */
    public Club(Parcel in) {
        readFromParcel(in);
    }

    /**
     * Instantiates a new club.
     */
    public Club(){
        nombre = "nom";

    }

    /**
     * Read from parcel.
     *
     * @param in the in
     */
    private void readFromParcel(Parcel in) {

        clave = in.readString();
        direccion = in.readString();
        horario = in.readString();
        idun = in.readString();
        lat = in.readString();
        lon = in.readString();
        nombre = in.readString();
        preventa = in.readString();
        resourceUri = in.readString();
        rutaVideo = in.readString();
        ruta360 = in.readString();
        telefono = in.readString();
        distancia = in.readString();
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return nombre;
    }

    /* (non-Javadoc)
     * @see android.os.Parcelable#describeContents()
     */
    @Override
    public int describeContents() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /* (non-Javadoc)
     * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(clave);
        dest.writeString(direccion);
        dest.writeString(horario);
        dest.writeString(idun);
        dest.writeString(lat);
        dest.writeString(lon);
        dest.writeString(nombre);
        dest.writeString(preventa);
        dest.writeString(resourceUri);
        dest.writeString(rutaVideo);
        dest.writeString(ruta360);
        dest.writeString(telefono);
        dest.writeString(distancia);

    }

    /** The Constant CREATOR. */
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Club createFromParcel(Parcel in) {
                    return new Club(in);
                }

                public Club[] newArray(int size) {
                    return new Club[size];
                }
            };
}
