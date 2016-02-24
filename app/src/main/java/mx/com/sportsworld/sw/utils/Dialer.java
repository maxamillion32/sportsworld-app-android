package mx.com.sportsworld.sw.utils;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.Context;
import android.content.Intent;
import android.net.Uri;


/**
 * The Class Dialer.
 */
public class Dialer {
    
    /**
	 * Dial.
	 * 
	 * @param context
	 *            the context
	 * @param phone
	 *            the phone
	 */
    public static void dial(Context context, String phone){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));
        context.startActivity(callIntent);
    }
}
