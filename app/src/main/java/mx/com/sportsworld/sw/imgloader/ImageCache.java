package mx.com.sportsworld.sw.imgloader;
/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import mx.com.sportsworld.sw.app.RetainFragment;

// TODO: Auto-generated Javadoc

/**
 * The Class ImageCache.
 *
 * @author Pablo Enrique Morales Breck
 */
public class ImageCache {
    // TODO Add suport for clearDiskCacheOnStart

    // private static final String TAG = makeLogTag( ImageCache.class );
    /** The Constant DEFAULT_DISK_CACHE_FOLDER_NAME. */
    private static final String DEFAULT_DISK_CACHE_FOLDER_NAME = "thumbs";
    
    /** The Constant DEFAULT_DISK_CACHE_SIZE. */
    private static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
    
    /** The Constant DEFAULT_COMPRESS_QUALITY. */
    private static final int DEFAULT_COMPRESS_QUALITY = 20;
    
    /** The Constant DEFAULT_COMPRESS_FORMAT. */
    private static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.PNG;
    // Constants to easily toggle various caches
    /** The Constant DEFAULT_MEM_CACHE_ENABLED. */
    private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
    
    /** The Constant DEFAULT_DISK_CACHE_ENABLED. */
    private static final boolean DEFAULT_DISK_CACHE_ENABLED = true;
    
    /** The Constant DEFAULT_CLEAR_DISK_CACHE_ON_START. */
    private static final boolean DEFAULT_CLEAR_DISK_CACHE_ON_START = true;
    
    /** The m mem cache. */
    private BitmapLruCache mMemCache;
    
    /** The m disk cache. */
    private DiskLruImageCache mDiskCache;

    /** The Constant CACHE_FILENAME_PREFIX. */
    private static final String CACHE_FILENAME_PREFIX = "cache_";

    /**
     * Instantiates a new image cache.
     *
     * @param context the context
     * @param params the params
     */
    private ImageCache(Context context, ImageCacheParams params) {
        init(context, params);
    }

    /**
     * Instantiates a new image cache.
     *
     * @param context the context
     * @param folderName the folder name
     */
    private ImageCache(Context context, String folderName) {
        init(context, new ImageCacheParams(folderName));
    }

    /**
     * Find or create cache.
     *
     * @param activity the activity
     * @param folderName the folder name
     * @return the image cache
     */
    public static ImageCache findOrCreateCache(final FragmentActivity activity,
            final String folderName) {
        return findOrCreateCache(activity, new ImageCacheParams(folderName));
    }

    /**
     * Find or create cache.
     *
     * @param activity the activity
     * @param params the params
     * @return the image cache
     */
    private static ImageCache findOrCreateCache(FragmentActivity activity, ImageCacheParams params) {
        final FragmentManager fm = activity.getSupportFragmentManager();
        final RetainFragment imgCacheFragment = (RetainFragment) RetainFragment
                .findOrCreateInstance(fm);
        ImageCache imgCache = (ImageCache) imgCacheFragment.getObject();
        if (imgCache == null) {
            imgCache = new ImageCache(activity, params);
            imgCacheFragment.setObject(imgCache);
        }
        return imgCache;
    }

    /**
     * Inits the.
     *
     * @param context the context
     * @param cacheParams the cache params
     */
    private void init(Context context, ImageCacheParams cacheParams) {

        if (cacheParams.diskCacheEnabled) {
            try { // TODO what if storage is full?
                mDiskCache = new DiskLruImageCache(context, cacheParams.diskCacheFolderName,
                        cacheParams.diskCacheSize, cacheParams.compressFormat,
                        cacheParams.compressQuality);
            } catch (final IOException ignored) {
                // LOGE( TAG, ignored.toString() );
            }
        }

        // Setup memory cache
        if (cacheParams.memoryCacheEnabled) {
            if (cacheParams.memCacheSize == 0) {
                mMemCache = new BitmapLruCache(context);
            } else {
                mMemCache = new BitmapLruCache(cacheParams.memCacheSize);
            }
        }

    }

    /**
     * Saves bitmap to memory and disk cache (if available). Call it outside of ui thread.
     *
     * @param id the id
     * @param bitmap the bitmap
     */
    public synchronized void addBitmapToCache(String id, Bitmap bitmap) {

        if (id == null || bitmap == null) {
            return;
        }

        // Add to memory cache
        if (mMemCache != null && mMemCache.get(id) == null) {
            mMemCache.put(id, bitmap);
            // LOGD( TAG, "image put on mem cache " + id );
        }

        // Add to disk cache
        if (mDiskCache != null) {
            final String key = hashKeyForDisk(id);
            if (!mDiskCache.containsKey(key)) {
                mDiskCache.put(key, bitmap);
            }
        }

    }

    /**
     * Get bitmap from memory. Can be called on ui thread.
     *
     * @param id the id
     * @return the bitmap from mem cache
     */
    public Bitmap getBitmapFromMemCache(String id) {
        if (mMemCache == null) {
            return null;
        }
        final Bitmap memBitmap = mMemCache.get(id);
        if (memBitmap != null) {
            // LOGD( TAG, "image read from memory " + id );
            return memBitmap;
        }
        return null;
    }

    /**
     * Get bitmap from disk.Do not call this method on ui thread.
     *
     * @param id the id
     * @return the bitmap from disk cache
     */
    public Bitmap getBitmapFromDiskCache(String id) {
        final String key = hashKeyForDisk(id);
        if (mDiskCache == null) {
            return null;
        }
        return mDiskCache.getBitmap(key);
    }

    /**
     * Close.
     */
    public void close() {
        if (mDiskCache != null) {
            mDiskCache.closeDiskCache();
        }
    }

    /**
     * Clear memory cache. Can be called on ui thread.
     */
    public void clearCaches() {
        if (mDiskCache != null) {
            mDiskCache.clearCache();
        }
        if (mMemCache != null) {
            mMemCache.evictAll();
        }
    }

    /**
     * Gets the disk cache folder.
     *
     * @return the disk cache folder
     */
    public File getDiskCacheFolder() {
        if (mDiskCache != null) {
            return mDiskCache.getCacheFolder();
        }
        return null;
    }

    /**
     * Sets the pause disk cache.
     *
     * @param pause the new pause disk cache
     */
    public void setPauseDiskCache(boolean pause) {
        if (mDiskCache != null) {
            mDiskCache.setPauseDiskCache(pause);
        }
    }

    // XXX From iosched 2012
    /**
     * A hashing method that changes a string (like a URL) into a hash suitable for using as a disk
     * filename.
     *
     * @param key the key
     * @return the string
     */
    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }

        return cacheKey;
    }

    // XXX From iosched 2012
    /**
     * Bytes to hex string.
     *
     * @param bytes the bytes
     * @return the string
     */
    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * The Class ImageCacheParams.
     */
    public static class ImageCacheParams {

        /** The mem cache size. */
        public int memCacheSize = 0;
        
        /** The disk cache folder name. */
        public final String diskCacheFolderName;
        
        /** The disk cache size. */
        public int diskCacheSize = DEFAULT_DISK_CACHE_SIZE;
        
        /** The compress quality. */
        public int compressQuality = DEFAULT_COMPRESS_QUALITY;
        
        /** The cache filename prefix. */
        public String cacheFilenamePrefix = CACHE_FILENAME_PREFIX;
        
        /** The compress format. */
        public CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;
        
        /** The memory cache enabled. */
        public boolean memoryCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;
        
        /** The disk cache enabled. */
        public boolean diskCacheEnabled = DEFAULT_DISK_CACHE_ENABLED;
        
        /** The clear disk cache on start. */
        public boolean clearDiskCacheOnStart = DEFAULT_CLEAR_DISK_CACHE_ON_START;
        
        /** The memory class. */
        public int memoryClass = 0;

        /**
         * Instantiates a new image cache params.
         *
         * @param diskCacheFolderName the disk cache folder name
         */
        public ImageCacheParams(String diskCacheFolderName) {
            this.diskCacheFolderName = diskCacheFolderName == null ? DEFAULT_DISK_CACHE_FOLDER_NAME
                    : diskCacheFolderName;
        }

    }

}
