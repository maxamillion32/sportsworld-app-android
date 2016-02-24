package mx.com.sportsworld.sw.utils;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * The Class InputStreamUtils.
 */
public final class InputStreamUtils {

    /**
	 * To string.
	 * 
	 * @param stream
	 *            the stream
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public final static synchronized String toString(InputStream stream) throws IOException {

        if (stream == null) {
            return null;
        }

        final ArrayList<Byte> arrayListBuffer = new ArrayList<Byte>(stream.available());
        byte byteRead;
        boolean keepReading = true;
        while (keepReading) {
            byteRead = (byte) stream.read();
            keepReading = (byteRead != -1);
            if (keepReading) {
                arrayListBuffer.add(byteRead);
            }
        }

        final int size = arrayListBuffer.size();
        final byte[] buffer = new byte[size];

        for (int i = 0; i < size; i++) {
            buffer[i] = arrayListBuffer.get(i);
        }

        return new String(buffer);

    }

    /**
	 * To string.
	 * 
	 * @param stream
	 *            the stream
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    public final static synchronized String toString(BufferedInputStream stream)
            throws IOException {

        if (stream == null) {
            return null;
        }

        final ArrayList<Byte> arrayListBuffer = new ArrayList<Byte>(stream.available());
        byte byteRead;
        boolean keepReading = true;
        while (keepReading) {
            byteRead = (byte) stream.read();
            keepReading = (byteRead != -1);
            if (keepReading) {
                arrayListBuffer.add(byteRead);
            }
        }

        final int size = arrayListBuffer.size();
        final byte[] buffer = new byte[size];

        for (int i = 0; i < size; i++) {
            buffer[i] = arrayListBuffer.get(i);
        }

        return new String(buffer);

    }

}
