package mx.com.sportsworld.sw.utils;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.Context;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

/**
 * The Class HttpUtils.
 */
public final class HttpUtils {

    /**
	 * Instantiates a new http utils.
	 */
    private HttpUtils() {
    }

    /**
	 * May enable http cache.
	 * 
	 * @param context
	 *            the context
	 */
    public static void mayEnableHttpCache(Context context) {
        try {
            File httpCacheDir = new File(context.getCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            Class.forName("android.net.http.HttpResponseCache")
                    .getMethod("install", File.class, long.class)
                    .invoke(null, httpCacheDir, httpCacheSize);
        } catch (IllegalArgumentException ignore) {
            /* We can't do anything if we are running in a device that does not support it. */
        } catch (IllegalAccessException ignore) {
            /* We can't do anything if we are running in a device that does not support it. */
        } catch (InvocationTargetException ignore) {
            /* We can't do anything if we are running in a device that does not support it. */
        } catch (NoSuchMethodException ignore) {
            /* We can't do anything if we are running in a device that does not support it. */
        } catch (ClassNotFoundException ignore) {
            /* We can't do anything if we are running in a device that does not support it. */
        }

    }
}
