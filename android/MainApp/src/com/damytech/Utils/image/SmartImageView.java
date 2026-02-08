package com.damytech.Utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.damytech.Misc.Global;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SmartImageView extends ImageView {
	private static final int LOADING_THREADS = 4;
	private static ExecutorService threadPool = Executors.newFixedThreadPool(LOADING_THREADS);

	private SmartImageTask currentTask;

	public boolean isCircular = false;
	public boolean isRoundRect = false;
	public int roundSize = 0;

	public SmartImageView(Context context) {
		super(context);
//		setScaleType(ScaleType.FIT_XY);
	}

	public SmartImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
//		setScaleType(ScaleType.FIT_XY);
	}

	public SmartImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
//		setScaleType(ScaleType.FIT_XY);
	}


	// Helpers to set image by URL
	public void setImageUrl(String url) {
		setImage(new WebImage(url));
	}

	public void setImageUrl(String url, SmartImageTask.OnCompleteListener completeListener) {
		setImage(new WebImage(url), completeListener);
	}

	public void setImageUrl(String url, final Integer fallbackResource) {
		setImage(new WebImage(url), fallbackResource);
	}

	public void setImageUrl(String url, final Integer fallbackResource, SmartImageTask.OnCompleteListener completeListener) {
		setImage(new WebImage(url), fallbackResource, completeListener);
	}

	public void setImageUrl(String url, final Integer fallbackResource, final Integer loadingResource) {
		setImage(new WebImage(url), fallbackResource, loadingResource);
	}

	public void setImageUrl(String url, final Integer fallbackResource, final Integer loadingResource, SmartImageTask.OnCompleteListener completeListener) {
		setImage(new WebImage(url), fallbackResource, loadingResource, completeListener);
	}


	// Helpers to set image by contact address book id
	public void setImageContact(long contactId) {
		setImage(new ContactImage(contactId));
	}

	public void setImageContact(long contactId, final Integer fallbackResource) {
		setImage(new ContactImage(contactId), fallbackResource);
	}

	public void setImageContact(long contactId, final Integer fallbackResource, final Integer loadingResource) {
		setImage(new ContactImage(contactId), fallbackResource, fallbackResource);
	}


	// Set image using SmartImage object
	public void setImage(final SmartImage image) {
		setImage(image, null, null, null);
	}

	public void setImage(final SmartImage image, final SmartImageTask.OnCompleteListener completeListener) {
		setImage(image, null, null, completeListener);
	}

	public void setImage(final SmartImage image, final Integer fallbackResource) {
		setImage(image, fallbackResource, fallbackResource, null);
	}

	public void setImage(final SmartImage image, final Integer fallbackResource, SmartImageTask.OnCompleteListener completeListener) {
		setImage(image, fallbackResource, fallbackResource, completeListener);
	}

	public void setImage(final SmartImage image, final Integer fallbackResource, final Integer loadingResource) {
		setImage(image, fallbackResource, loadingResource, null);
	}

	public void setImage(final SmartImage image, final Integer fallbackResource, final Integer loadingResource, final SmartImageTask.OnCompleteListener completeListener) {
		// Set a loading resource
		if(loadingResource != null){
			setImage(new SmartImage() {
				@Override
				public Bitmap getBitmap(Context context) {
					return BitmapFactory.decodeResource(getResources(), loadingResource);
				}
			});
		}

		// Cancel any existing tasks for this image view
		if(currentTask != null)
		{
			currentTask.cancel();
			currentTask = null;
		}

		// Set up the new task
		currentTask = new SmartImageTask(getContext(), image);
		currentTask.setOnCompleteHandler(new SmartImageTask.OnCompleteHandler()
		{
			@Override
			public void onComplete(Bitmap bitmap) {
				if(bitmap != null)
				{
					if (isCircular)
					{
						setImageBitmap(Global.getCroppedRoundBitmap(bitmap));
					}
					else
					{
						if (isRoundRect)
							setImageBitmap(Global.getRoundedCornerBitmap(bitmap, roundSize));
						else
							setImageBitmap(bitmap);
					}
				}
				else
				{
					// Set fallback resource
					if(fallbackResource != null) {
						setImage(new SmartImage() {
							@Override
							public Bitmap getBitmap(Context context) {
								return BitmapFactory.decodeResource(getResources(), fallbackResource);          // Customized by KHM. 2014-09-29
							}
						});
					}
				}

				if(completeListener != null){
					completeListener.onComplete();
				}
			}
		});

		// Run the task in a threadpool
		threadPool.execute(currentTask);
	}

	public static void cancelAllTasks() {
		threadPool.shutdownNow();
		threadPool = Executors.newFixedThreadPool(LOADING_THREADS);
	}
}