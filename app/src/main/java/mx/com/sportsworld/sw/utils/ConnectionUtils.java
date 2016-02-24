package mx.com.sportsworld.sw.utils;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * The Class ConnectionUtils.
 */
public final class ConnectionUtils {

    /**
	 * Instantiates a new connection utils.
	 */
    private ConnectionUtils() {
    }
    
    /**
	 * Checks if is network available.
	 * 
	 * @param context
	 *            the context
	 * @return true, if is network available
	 */
    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return ((networkInfo != null) && networkInfo.isConnected());
    }
    
}
