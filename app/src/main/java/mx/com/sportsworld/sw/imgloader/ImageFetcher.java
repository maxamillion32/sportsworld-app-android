package mx.com.sportsworld.sw.imgloader;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

// TODO: Auto-generated Javadoc

/**
 * The Class ImageFetcher.
 */
public class ImageFetcher extends ImageLoader {

    // private static final String TAG = makeLogTag( ImageFetcher.class );
    // Default fetcher params
    /** The Constant DEFAULT_MAX_THUMBNAIL_BYTES. */
    private static final int DEFAULT_MAX_THUMBNAIL_BYTES = 70 * 1024; // 70KB
    
    /** The Constant DEFAULT_MAX_IMAGE_HEIGHT. */
    private static final int DEFAULT_MAX_IMAGE_HEIGHT = 1024;
    
    /** The Constant DEFAULT_MAX_IMAGE_WIDTH. */
    private static final int DEFAULT_MAX_IMAGE_WIDTH = 1024;
    
    /** The Constant DEFAULT_HTTP_CACHE_SIZE. */
    private static final int DEFAULT_HTTP_CACHE_SIZE = 5 * 1024 * 1024; // 5MB
    
    /** The Constant DEFAULT_HTTP_CACHE_DIR. */
    private static final String DEFAULT_HTTP_CACHE_DIR = "http";
    
    /** The m fetcher params. */
    private ImageFetcherParams mFetcherParams;
    
    /** The m context. */
    private Context mContext;

    /**
     * Instantiates a new image fetcher.
     *
     * @param context the context
     * @param cache the cache
     * @param loadingDrawable the loading drawable
     * @param params the params
     */
    public ImageFetcher(Context context, ImageCache cache, int loadingDrawable,
            ImageFetcherParams params) {
        super(context, cache, loadingDrawable);
        setFetcherParams(params);
        mContext = context;
    }

    /**
     * Instantiates a new image fetcher.
     *
     * @param context the context
     * @param cache the cache
     * @param loadingDrawable the loading drawable
     */
    public ImageFetcher(Context context, ImageCache cache, int loadingDrawable) {
        super(context, cache, loadingDrawable);
        setFetcherParams(new ImageFetcherParams());
        mContext = context;
    }


    /**
     * Load thumbnail image.
     *
     * @param key the key
     * @param imageView the image view
     */
    public void loadThumbnailImage(String key, ImageView imageView) {
        loadBitmap(new ImageData(key, ImageData.IMAGE_TYPE_THUMBNAIL), imageView);
    }

    /**
     * Load sampled thumbnail image.
     *
     * @param key the key
     * @param imageView the image view
     */
    public void loadSampledThumbnailImage(String key, ImageView imageView) {
        loadBitmap(new ImageData(key, ImageData.IMAGE_TYPE_SAMPLED_THUMBNAIL), imageView);
    }

    /**
     * Sets the fetcher params.
     *
     * @param params the new fetcher params
     */
    public void setFetcherParams(ImageFetcherParams params) {
        mFetcherParams = params;
        setLoaderParams(params);
    }

    /* (non-Javadoc)
     * @see com.sportsworld.android.imgloader.ImageLoader#processBitmap(android.content.Context, java.lang.Object)
     */
    @Override
    public Bitmap processBitmap(Context context, Object key) {
        final ImageData imgData = (ImageData) key;
        return processBitmap(imgData.key, imgData.type);
    }

    /**
     * The main process method, which will be called by the ImageWorker in the AsyncTask background
     * thread.
     *
     * @param key The key to load the bitmap, in this case, a regular http URL
     * @param type the type
     * @return The downloaded and resized bitmap
     */
    public Bitmap processBitmap(String key, int type) {
        // XXX From iosched2012
        // LOGD(TAG, "processBitmap - " + key);

        switch (type) {
        case ImageData.IMAGE_TYPE_SAMPLED_THUMBNAIL:
            // fallsthrough
        case ImageData.IMAGE_TYPE_NORMAL:

            final File f = downloadBitmapToFile(mContext, key, mFetcherParams.httpCacheDir);
            if (f != null) {
                // Return a sampled down version
                final Bitmap bitmap = decodeSampledBitmapFromFile(f.toString(),
                        mFetcherParams.imageWidth, mFetcherParams.imageHeight);
                final boolean success = f.delete();
                return bitmap;
            }
            break;

        case ImageData.IMAGE_TYPE_THUMBNAIL:
            final byte[] bitmapBytes = downloadBitmapToMemory(mContext, key,
                    mFetcherParams.maxThumbnailBytes);

            if (bitmapBytes != null) {
                // Caution: we don't check the size of the bitmap here, we are relying on the output
                // of downloadBitmapToMemory to not exceed our memory limits and load a huge bitmap
                // into memory.
                return BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            }
            break;

        default:
            throw new IllegalArgumentException();

        }

        return null;

    }

    /**
     * Download a bitmap from a URL, write it to a disk and return the File pointer.
     * 
     * @param context
     *            The context to use
     * @param urlString
     *            The URL to fetch
     * @param maxBytes
     *            The maximum number of bytes to read before returning null to protect against
     *            OutOfMemory exceptions.
     * @return A File pointing to the fetched bitmap
     */
    public static byte[] downloadBitmapToMemory(Context context, String urlString, int maxBytes) {
        // XXX From iosched2012

        disableConnectionReuseIfNecessary();
        HttpURLConnection urlConnection = null;
        ByteArrayOutputStream out = null;
        InputStream in = null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
            in = new BufferedInputStream(urlConnection.getInputStream(),
                    ImageLoaderUtils.IO_BUFFER_SIZE_BYTES);
            out = new ByteArrayOutputStream(ImageLoaderUtils.IO_BUFFER_SIZE_BYTES);

            final byte[] buffer = new byte[128];
            int total = 0;
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                total += bytesRead;
                if (total > maxBytes) {
                    return null;
                }
                out.write(buffer, 0, bytesRead);
            }
            return out.toByteArray();

        } catch (final IOException e) {
            // LOGE(TAG, "Error in downloadBitmapToMemory - " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                // LOGE(TAG, "Error in downloadBitmapToMemory - " + e);
            }
        }
        return null;
    }

    /**
     * Download a bitmap from a URL, write it to a disk and return the File pointer. This
     * implementation uses a simple disk cache.
     *
     * @param context The context to use
     * @param urlString The URL to fetch
     * @param uniqueName the unique name
     * @return A File pointing to the fetched bitmap
     */
    public static File downloadBitmapToFile(Context context, String urlString, String uniqueName) {
        // XXX From iosched2012

        final File cacheDir = DiskLruImageCache.getDiskCacheDir(context, uniqueName);

        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }

        // LOGV(TAG, "downloadBitmap - downloading - " + urlString);

        disableConnectionReuseIfNecessary();
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;

        try {
            final File tempFile = File.createTempFile("bitmap", null, cacheDir);

            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
            final InputStream in = new BufferedInputStream(urlConnection.getInputStream(),
                    ImageLoaderUtils.IO_BUFFER_SIZE_BYTES);
            out = new BufferedOutputStream(new FileOutputStream(tempFile),
                    ImageLoaderUtils.IO_BUFFER_SIZE_BYTES);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }

            return tempFile;

        } catch (Exception e) {
            Log.i("kokusho error", e.toString());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (final IOException e) {
                    // LOGE(TAG, "Error in downloadBitmap - " + e);
                }
            }
        }

        return null;
    }

    /**
     * Workaround for bug pre-Froyo, see here for more info:
     * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
     */
    public static void disableConnectionReuseIfNecessary() {
        // XXX From iosched2012
        // HTTP connection reuse which was buggy pre-froyo
        if (hasHttpConnectionBug()) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    /**
     * Check if OS version has a http URLConnection bug. See here for more information:
     * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
     * 
     * @return true if this OS version is affected, false otherwise
     */
    public static boolean hasHttpConnectionBug() {
        // XXX From iosched2012
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO);
    }

    /**
     * Decode and sample down a bitmap from a file to the requested width and height.
     * 
     * @param filename
     *            The full path of the file to decode
     * @param reqWidth
     *            The requested width of the resulting bitmap
     * @param reqHeight
     *            The requested height of the resulting bitmap
     * @return A bitmap sampled down from the original with the same aspect ratio and dimensions
     *         that are equal to or greater than the requested width and height
     */
    public static synchronized Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth,
            int reqHeight) {
        // XXX From iosched 2012
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filename, options);
    }

    /**
     * The Class ImageData.
     */
    private static class ImageData {
        
        /** The Constant IMAGE_TYPE_THUMBNAIL. */
        public static final int IMAGE_TYPE_THUMBNAIL = 0;
        
        /** The Constant IMAGE_TYPE_NORMAL. */
        public static final int IMAGE_TYPE_NORMAL = 1;
        
        /** The Constant IMAGE_TYPE_SAMPLED_THUMBNAIL. */
        public static final int IMAGE_TYPE_SAMPLED_THUMBNAIL = 2;
        
        /** The key. */
        public String key;
        
        /** The type. */
        public int type;

        /**
         * Instantiates a new image data.
         *
         * @param key the key
         * @param type the type
         */
        public ImageData(String key, int type) {
            this.key = key;
            this.type = type;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return key;
        }
    }

    /**
     * The Class ImageFetcherParams.
     */
    public static class ImageFetcherParams extends ImageLoaderParams {
        
        /** The image width. */
        public int imageWidth = DEFAULT_MAX_IMAGE_WIDTH;
        
        /** The image height. */
        public int imageHeight = DEFAULT_MAX_IMAGE_HEIGHT;
        
        /** The max thumbnail bytes. */
        public int maxThumbnailBytes = DEFAULT_MAX_THUMBNAIL_BYTES;
        
        /** The http cache size. */
        public int httpCacheSize = DEFAULT_HTTP_CACHE_SIZE;
        
        /** The http cache dir. */
        public String httpCacheDir = DEFAULT_HTTP_CACHE_DIR;
    }

}
