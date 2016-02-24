package mx.com.sportsworld.sw.imgloader;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import mx.com.sportsworld.sw.BuildConfig;

// TODO: Auto-generated Javadoc

/**
 * The Class DiskLruImageCache.
 */
public class DiskLruImageCache {

    /** The Constant APP_VERSION. */
    private static final int APP_VERSION = 1;
    
    /** The Constant VALUE_COUNT. */
    private static final int VALUE_COUNT = 1;
    
    /** The m disk cache. */
    private DiskLruCache mDiskCache;
    
    /** The m compress format. */
    private final CompressFormat mCompressFormat;
    
    /** The m compress quality. */
    private final int mCompressQuality;

    /** The m pause disk access. */
    private boolean mPauseDiskAccess = false;

    /**
     * Instantiates a new disk lru image cache.
     *
     * @param context the context
     * @param uniqueName the unique name
     * @param diskCacheSize the disk cache size
     * @param compressFormat the compress format
     * @param quality the quality
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public DiskLruImageCache(Context context, String uniqueName, int diskCacheSize,
            CompressFormat compressFormat, int quality) throws IOException {
        final File diskCacheDir = getDiskCacheDir(context, uniqueName);
        mDiskCache = DiskLruCache.open(diskCacheDir, APP_VERSION, VALUE_COUNT, diskCacheSize);
        mCompressFormat = compressFormat;
        mCompressQuality = quality;
    }

    /**
     * Write bitmap to file.
     *
     * @param bitmap the bitmap
     * @param editor the editor
     * @return true, if successful
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws FileNotFoundException the file not found exception
     */
    private boolean writeBitmapToFile(Bitmap bitmap, DiskLruCache.Editor editor)
            throws IOException, FileNotFoundException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(editor.newOutputStream(0),
                    ImageLoaderUtils.IO_BUFFER_SIZE_BYTES);
            return bitmap.compress(mCompressFormat, mCompressQuality, out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * Get a usable cache directory (external if available, internal otherwise).
     * 
     * @param context
     *            The context to use
     * @param uniqueName
     *            A unique directory name to append to the cache dir
     * @return The cache dir
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {

        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
                || !ImageLoaderUtils.isExternalStorageRemovable() ? ImageLoaderUtils
                .getExternalCacheDir(context).getPath() : context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * Put.
     *
     * @param key the key
     * @param data the data
     */
    public void put(String key, Bitmap data) {

        if (mDiskCache.isClosed()) {
            return;
        }

        DiskLruCache.Editor editor = null;
        try {
            editor = mDiskCache.edit(key);
            if (editor == null) {
                return;
            }

            if (writeBitmapToFile(data, editor)) {
                editor.commit();
                // LOGD( TAG, "image put on disk cache " + key );
            } else {
                editor.abort();
                // LOGD( TAG, "ERROR on: image put on disk cache " + key );

            }

        } catch (IOException e) {

            // LOGD( TAG, "ERROR on: image put on disk cache " + key );

            try {
                if (editor != null) {
                    editor.abort();
                }
            } catch (final IOException ignore) {
                // We can�t read it. Just ignore it.
            }
        }

    }

    /**
     * Gets the bitmap.
     *
     * @param key the key
     * @return the bitmap
     */
    public Bitmap getBitmap(String key) {

        if (mDiskCache.isClosed()) {
            return null;
        }

        Bitmap bitmap = null;
        DiskLruCache.Snapshot snapshot = null;
        try {

            snapshot = mDiskCache.get(key);
            if (snapshot == null) { // No existe o no puede leerse por el momento
                return null;
            }
            while (mPauseDiskAccess) {
            }
            final InputStream in = snapshot.getInputStream(0);
            if (in != null) {
                final BufferedInputStream buffIn = new BufferedInputStream(in,
                        ImageLoaderUtils.IO_BUFFER_SIZE_BYTES);
                bitmap = BitmapFactory.decodeStream(buffIn);
            }

        } catch (IOException e) {
            // LOGE( TAG, "getBitmapfromDiskCache - " + e );
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }

        return bitmap;

    }

    /**
     * Contains key.
     *
     * @param key the key
     * @return true, if successful
     */
    public boolean containsKey(String key) {

        if (mDiskCache.isClosed()) {
            return false;
        }

        boolean contained = false;
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = mDiskCache.get(key);
            contained = snapshot != null;
        } catch (IOException e) {
            if (BuildConfig.DEBUG) {
                // LOGE( TAG, "containsKey - " + e );
            }
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }

        return contained;

    }

    /**
     * Clear cache.
     */
    public void clearCache() {
        if (mDiskCache.isClosed()) {
            return;
        }
        try {
            mDiskCache.delete();
        } catch (IOException e) {
            // LOGE( TAG, "clearCache - " + e );
        }
    }

    /**
     * Gets the cache folder.
     *
     * @return the cache folder
     */
    public File getCacheFolder() {
        return mDiskCache.getDirectory();
    }

    /**
     * Close disk cache.
     */
    public void closeDiskCache() {
        try {
            if (!mDiskCache.isClosed()) {
                // Should really close() here but need to synchronize up other methods that
                // access mICSDiskCache first.
                mDiskCache.flush();
            }
        } catch (final IOException ignored) {
        }
    }

    /**
     * Check how much usable space is available at a given path.
     * 
     * @param path
     *            The path to check
     * @return The space available in bytes
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static long getUsableSpace(File path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }

    /**
     * Sets the pause disk cache.
     *
     * @param pause the new pause disk cache
     */
    public void setPauseDiskCache(boolean pause) {
        mPauseDiskAccess = pause;
    }

}
