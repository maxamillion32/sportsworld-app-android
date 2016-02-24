package mx.com.sportsworld.sw.imgloader;

/**
 * @author Jose Torres Fuentes 
 * Ironbit
 *
 */
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

// TODO: Auto-generated Javadoc

/**
 * This class wraps up completing some arbitrary long running work when loading
 * a bitmap to an ImageView. It handles things like using a memory and disk
 * cache, running the work in a background thread and setting a placeholder
 * image.
 */
public abstract class ImageLoader {

	// private static final String TAG = makeLogTag( ImageLoader.class );
	/** The Constant DEFAULT_FADE_EFFECT. */
	private static final boolean DEFAULT_FADE_EFFECT = true;
	
	/** The Constant DEFAULT_FADE_IN_TIME. */
	private static final int DEFAULT_FADE_IN_TIME = 250;
	
	/** The m img cache. */
	private final ImageCache mImgCache;
	
	/** The m loading bitmap. */
	private final Bitmap mLoadingBitmap;
	
	/** The m context. */
	private final Context mContext;
	
	/** The m exit task early. */
	private boolean mExitTaskEarly;
	
	/** The m loader params. */
	private ImageLoaderParams mLoaderParams;

	/**
	 * Instantiates a new image loader.
	 *
	 * @param context the context
	 * @param cache the cache
	 * @param loadingDrawable the loading drawable
	 */
	public ImageLoader(Context context, ImageCache cache, int loadingDrawable) {
		mContext = context;
		mImgCache = cache;
		setLoaderParams(new ImageLoaderParams());
		final Resources res = context.getResources();
		mLoadingBitmap = ((BitmapDrawable) res.getDrawable(loadingDrawable))
				.getBitmap();
	}

	/**
	 * Sets the loader params.
	 *
	 * @param params the new loader params
	 */
	public void setLoaderParams(ImageLoaderParams params) {
		mLoaderParams = params;
	}

	/**
	 * Load bitmap.
	 *
	 * @param data the data
	 * @param imageView the image view
	 */
	protected void loadBitmap(Object data, ImageView imageView) {

		Bitmap bitmap = null;
		if (mImgCache != null) {
			bitmap = mImgCache.getBitmapFromMemCache(String.valueOf(data));
		}

		if (bitmap != null) {
			// Bitmap found in memory cache
			imageView.setImageBitmap(bitmap);
		} else if (cancelPotentialWork(imageView, data)) {
			final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
			final AsyncDrawable asyncDrawable = new AsyncDrawable(
					mContext.getResources(), mLoadingBitmap, task);
			imageView.setImageDrawable(asyncDrawable);
			executeTaskInParallel(data, task);
		}

	}

	/**
	 * Executes the given {@link BitmapWorkerTask} in parallel. On Honeycomb the
	 * default executor is a serial executor so we explicitly use a parallel
	 * (thread pool) executor.
	 *
	 * @param data the data
	 * @param task the task
	 */
	// XXX From ioSched 2012
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void executeTaskInParallel(Object data, BitmapWorkerTask task) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Execute in parallel
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
		} else {
			task.execute(data);
		}
	}

	/**
	 * Sets the exit task early.
	 *
	 * @param exitTaskEarly the new exit task early
	 */
	public void setExitTaskEarly(boolean exitTaskEarly) {
		mExitTaskEarly = exitTaskEarly;
	}

	/**
	 * Cancel potential work.
	 *
	 * @param img the img
	 * @param data the data
	 * @return true, if successful
	 */
	private boolean cancelPotentialWork(ImageView img, Object data) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(img);
		if (bitmapWorkerTask != null) {
			final Object bitmapData = bitmapWorkerTask.mData;
			if (bitmapData == null || !bitmapData.equals(data)) {
				// Cancel previous task
				bitmapWorkerTask.cancel(true /* mayInterruptIfRunning */);
				// LOGV( TAG, "cancelPotentialWork - cancelled work for " + data
				// );
			} else {
				// The same work is already in progress
				return false;
			}
		}
		// No task associated with the ImageView, or an existing task was
		// cancelled
		return true;
	}

	/**
	 * Gets the bitmap worker task.
	 *
	 * @param img the img
	 * @return the bitmap worker task
	 */
	private BitmapWorkerTask getBitmapWorkerTask(ImageView img) {

		if (img != null) {
			final Drawable drawable = img.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;

	}

	/**
	 * Sets the pause disk cache.
	 *
	 * @param pause the new pause disk cache
	 */
	public void setPauseDiskCache(boolean pause) {
		if (mImgCache != null) {
			mImgCache.setPauseDiskCache(pause);
		}
	}

	/**
	 * Close cache.
	 */
	public void closeCache() {
		if (mImgCache != null) {
			mImgCache.close();
		}
	}

	/**
	 * Calculate in sample size.
	 *
	 * @param options the options
	 * @param reqHeight the req height
	 * @param reqWidth the req width
	 * @return the int
	 */
	protected static int calculateInSampleSize(BitmapFactory.Options options,
			int reqHeight, int reqWidth) {

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}

			// This offers some additional logic in case the image has a
			// strange
			// aspect ratio. For example, a panorama may have a much larger
			// width than height. In these cases the total pixels might
			// still
			// end up being too large to fit comfortably in memory, so we
			// should
			// be more aggressive with sample down the image (=larger
			// inSampleSize).

			final float totalPixels = width * height;

			// Anything more than 2x the requested pixels we'll sample down
			// further.
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;

	}

	/**
	 * Subclasses should override this to define any processing or work that
	 * must happen to produce the final bitmap. This will be executed in a
	 * background thread and be long running. For example, you could resize a
	 * large bitmap here, or pull down an image from the network.
	 *
	 * @param context the context
	 * @param data the data
	 * @return the bitmap
	 */
	public abstract Bitmap processBitmap(Context context, Object data);

	/**
	 * The actual AsyncTask that will asynchronously process the image.
	 */
	private class BitmapWorkerTask extends AsyncTask<Object, Void, Bitmap> {

		/** The m data. */
		private Object mData;
		
		/** The img view ref. */
		private final WeakReference<ImageView> imgViewRef;

		/**
		 * Instantiates a new bitmap worker task.
		 *
		 * @param imgView the img view
		 */
		public BitmapWorkerTask(ImageView imgView) {
			imgViewRef = new WeakReference<ImageView>(imgView);
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected Bitmap doInBackground(Object... params) {

			mData = params[0];
			final String dataString = String.valueOf(mData);
			Bitmap bitmap = null;

			/*
			 * If the image cache is available and this task has not been
			 * cancelled by another thread and the ImageView that was originally
			 * bound to this task is still bound back to this task and our
			 * "exit early" flag is not set then try and fetch the bitmap from
			 * the cache
			 */
			if (!isCancelled() && mImgCache != null
					&& getAttachedImageView() != null && !mExitTaskEarly) {
				bitmap = mImgCache.getBitmapFromDiskCache(dataString);
			}

			/*
			 * If the bitmap was not found in the cache and this task has not
			 * been cancelled by another thread and the ImageView that was
			 * originally bound to this task is still bound back to this task
			 * and our "exit early" flag is not set, then call the main process
			 * method (as implemented by a subclass)
			 */
			if (!isCancelled() && bitmap == null
					&& getAttachedImageView() != null && !mExitTaskEarly) {
				bitmap = processBitmap(mContext, params[0]);
			}

			/*
			 * If the bitmap was processed and the image cache is available,
			 * then add the processed bitmap to the cache for future use. Note
			 * we don't check if the task was cancelled here, if it was, and the
			 * thread is still running, we may as well add the processed bitmap
			 * to our cache as it might be used again in the future
			 */
			if (bitmap != null && mImgCache != null) {
				mImgCache.addBitmapToCache(dataString, bitmap);
			}

			return bitmap;

		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			// XXX Comments from iosched 2012
			/*
			 * if cancel was called on this task or the "exit early" flag is set
			 * then we're done
			 */
			if (isCancelled() || mExitTaskEarly) {
				bitmap = null;
			}

			final ImageView imageView = getAttachedImageView();
			if (bitmap != null && imageView != null) {
				setImageBitmap(imageView, bitmap);
			}

		}

		/**
		 * Gets the bitmap worker task.
		 *
		 * @param img the img
		 * @return the bitmap worker task
		 */
		private BitmapWorkerTask getBitmapWorkerTask(ImageView img) {
			if (img != null) {
				final Drawable drawable = img.getDrawable();
				if (drawable instanceof AsyncDrawable) {
					final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
					return asyncDrawable.getBitmapWorkerTask();
				}
			}
			return null;
		}

		/**
		 * Returns the ImageView associated with this task as long as the
		 * ImageView's task still points to this task as well. Returns null
		 * otherwise.
		 *
		 * @return the attached image view
		 */
		private ImageView getAttachedImageView() {
			final ImageView imageView = imgViewRef.get();
			final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

			if (this == bitmapWorkerTask) {
				return imageView;
			}

			return null;
		}

		/**
		 * Called when the processing is complete and the final bitmap should be
		 * set on the ImageView.
		 *
		 * @param imageView the image view
		 * @param bitmap the bitmap
		 */

		// Currently, this is the only way
		private void setImageBitmap(ImageView imageView, Bitmap bitmap) {
			if (mLoaderParams.fadeEffect) {
				// Transition drawable with a transparent drawable and the final
				// bitmap
				final TransitionDrawable td = new TransitionDrawable(
						new Drawable[] {
								new ColorDrawable(android.R.color.transparent),
								new BitmapDrawable(mContext.getResources(),
										bitmap) });
				imageView.setImageDrawable(td);
				td.startTransition(mLoaderParams.fadeInTimeMillis);
			} else {
				imageView.setImageBitmap(bitmap);
			}
		}

	}

	/**
	 * The Class ImageLoaderParams.
	 */
	public static class ImageLoaderParams {
		
		/** The fade effect. */
		public boolean fadeEffect = DEFAULT_FADE_EFFECT;
		
		/** The fade in time millis. */
		public int fadeInTimeMillis = DEFAULT_FADE_IN_TIME;
	}

	/**
	 * The Class AsyncDrawable.
	 */
	private static class AsyncDrawable extends BitmapDrawable {

		/** The m async task ref. */
		private final WeakReference<BitmapWorkerTask> mAsyncTaskRef;

		/**
		 * Instantiates a new async drawable.
		 *
		 * @param res the res
		 * @param bitmap the bitmap
		 * @param task the task
		 */
		public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask task) {
			super(res, bitmap);
			mAsyncTaskRef = new WeakReference<BitmapWorkerTask>(task);
		}

		/**
		 * Gets the bitmap worker task.
		 *
		 * @return the bitmap worker task
		 */
		public BitmapWorkerTask getBitmapWorkerTask() {
			return mAsyncTaskRef.get();
		}

	}
}
