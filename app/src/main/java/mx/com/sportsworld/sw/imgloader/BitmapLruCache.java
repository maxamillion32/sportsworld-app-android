package mx.com.sportsworld.sw.imgloader;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;

// TODO: Auto-generated Javadoc

/**
 * The Class BitmapLruCache.
 */
public class BitmapLruCache extends LruCache<String, Bitmap> {

    // private static final String TAG = makeLogTag( BitmapLruCache.class );
    /** The Constant DEFAULT_MEM_CACHE_SIZE. */
    private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 1024 * 2; // 2MB
    
    /** The Constant DEFAULT_MEM_CACHE_DIVIDER. */
    private static final int DEFAULT_MEM_CACHE_DIVIDER = 8; // memory class/this = mem cache size

    /**
     * Instantiates a new bitmap lru cache.
     */
    public BitmapLruCache() {
        super(DEFAULT_MEM_CACHE_SIZE);
    }

    /**
     * Instantiates a new bitmap lru cache.
     *
     * @param context the context
     */
    public BitmapLruCache(Context context) {
        super(getCacheSize(context));
    }

    /**
     * Instantiates a new bitmap lru cache.
     *
     * @param maxSize the max size
     */
    public BitmapLruCache(int maxSize) {
        super(maxSize);
    }

    /* (non-Javadoc)
     * @see android.support.v4.util.LruCache#sizeOf(java.lang.Object, java.lang.Object)
     */
    @Override
    protected int sizeOf(String key, Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return HCsizeOf(bitmap);
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * H csize of.
     *
     * @param bitmap the bitmap
     * @return the int
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    protected int HCsizeOf(Bitmap bitmap) {
        return bitmap.getByteCount();
    }

    /**
     * Gets the cache size.
     *
     * @param context the context
     * @return the cache size
     */
    private final static int getCacheSize(Context context) {
        final ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        final int mem = am.getMemoryClass();
        final int memCache = (mem * 1024 * 1024) / DEFAULT_MEM_CACHE_DIVIDER;
        return memCache;
    }

}
