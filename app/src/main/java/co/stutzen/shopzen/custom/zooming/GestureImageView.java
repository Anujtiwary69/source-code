
package co.stutzen.shopzen.custom.zooming;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class GestureImageView extends ImageView {

	public static final String GLOBAL_NS = "http://schemas.android.com/apk/res/android";
	public static final String LOCAL_NS = "http://schemas.polites.com/android";

	private final Semaphore drawLock = new Semaphore(0);
	private Animator animator;

	private Drawable drawable;

	private float x = 0, y = 0;

	private boolean layout = false;

	private float scaleAdjust = 1.0f;
	private float startingScale = -1.0f;

	private float scale = 1.0f;
	private float maxScale = 5.0f;
	private float minScale = 0.75f;
	private float fitScaleHorizontal = 1.0f;
	private float fitScaleVertical = 1.0f;
	private float rotation = 0.0f;

	private float centerX;
	private float centerY;
	
	private Float startX, startY;

	private int hWidth;
	private int hHeight;

	private int resId = -1;
	private boolean recycle = false;
	private boolean strict = false;

	private int displayHeight;
	private int displayWidth;

	private int alpha = 255;
	private ColorFilter colorFilter;

	private int deviceOrientation = -1;
	private int imageOrientation;

	private GestureImageViewListener gestureImageViewListener;
	private GestureImageViewTouchListener gestureImageViewTouchListener;
	
	private OnTouchListener customOnTouchListener;
	private OnClickListener onClickListener;
	private OnLongClickListener onLongClickListener;
	private Context cont;

	public GestureImageView(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
		Log.i("1", "1");
		cont = context;
	}

	public GestureImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		cont = context;
		String scaleType = attrs.getAttributeValue(GLOBAL_NS, "scaleType");
		
		if(scaleType == null || scaleType.trim().length() == 0) {
			setScaleType(ScaleType.CENTER_INSIDE);
			Log.i("1", "2");
		}
		
		String strStartX = attrs.getAttributeValue(LOCAL_NS, "start-x");
		String strStartY = attrs.getAttributeValue(LOCAL_NS, "start-y");
		
		if(strStartX != null && strStartX.trim().length() > 0) {
			startX = Float.parseFloat(strStartX);
			Log.i("1", "3");
		}
		
		if(strStartY != null && strStartY.trim().length() > 0) {
			startY = Float.parseFloat(strStartY);
			Log.i("1", "4");
		}
		
		setStartingScale(attrs.getAttributeFloatValue(LOCAL_NS, "start-scale", startingScale));
		setMinScale(attrs.getAttributeFloatValue(LOCAL_NS, "min-scale", minScale));
		setMaxScale(attrs.getAttributeFloatValue(LOCAL_NS, "max-scale", maxScale));
		setStrict(attrs.getAttributeBooleanValue(LOCAL_NS, "strict", strict));
		setRecycle(attrs.getAttributeBooleanValue(LOCAL_NS, "recycle", recycle));

		initImage();
	}

	public GestureImageView(Context context) {
		super(context);
		cont = context;
		setScaleType(ScaleType.CENTER_INSIDE);
		Log.i("1", "5");
		initImage();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		if(drawable != null) {
			Log.i("1", "6");
			int orientation = getResources().getConfiguration().orientation;
			if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
				displayHeight = MeasureSpec.getSize(heightMeasureSpec);

				if(getLayoutParams().width == LayoutParams.WRAP_CONTENT) {
					Log.i("1", "7");
					float ratio = (float) getImageWidth() / (float) getImageHeight();
					displayWidth = Math.round((float) displayHeight * ratio) ;
				}
				else {
					Log.i("1", "8");
					displayWidth = MeasureSpec.getSize(widthMeasureSpec);
				}
			}
			else {
				Log.i("1", "9");
				displayWidth = MeasureSpec.getSize(widthMeasureSpec);
				if(getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
					Log.i("1", "10");
					float ratio = (float) getImageHeight() / (float) getImageWidth();
					displayHeight = Math.round((float) displayWidth * ratio) ;
				}
				else {
					Log.i("1", "11");
					displayHeight = MeasureSpec.getSize(heightMeasureSpec);
				}				
			}
		}
		else {
			Log.i("1", "12");
			displayHeight = MeasureSpec.getSize(heightMeasureSpec);
			displayWidth = MeasureSpec.getSize(widthMeasureSpec);
		}

		setMeasuredDimension(displayWidth, displayHeight);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if(changed || !layout) {
			Log.i("1", "13");
			setupCanvas(displayWidth, displayHeight, getResources().getConfiguration().orientation);
		}
	}

	protected void setupCanvas(int measuredWidth, int measuredHeight, int orientation) {

		if(deviceOrientation != orientation) {
			Log.i("1", "14");
			layout = false;
			deviceOrientation = orientation;
		}

		if(drawable != null && !layout) {
			Log.i("1", "15");
			int imageWidth = getImageWidth();
			int imageHeight = getImageHeight();

			hWidth = Math.round(((float) imageWidth / 2.0f));
			hHeight = Math.round(((float) imageHeight / 2.0f));
			
			measuredWidth -= (getPaddingLeft() + getPaddingRight());
			measuredHeight -= (getPaddingTop() + getPaddingBottom());
			
			computeCropScale(imageWidth, imageHeight, measuredWidth, measuredHeight);
			
			if(startingScale <= 0.0f) {
				computeStartingScale(imageWidth, imageHeight, measuredWidth, measuredHeight);
			}

			scaleAdjust = startingScale;

			this.centerX = (float) measuredWidth / 2.0f;
			this.centerY = (float) measuredHeight / 2.0f;
			
			if(startX == null) {
				x = centerX;
			}
			else {
				x = startX;
			}

			if(startY == null) {
				y = centerY;
			}
			else {
				y = startY;
			}	

			gestureImageViewTouchListener = new GestureImageViewTouchListener(this, measuredWidth, measuredHeight, cont);
			
			if(isLandscape()) {
				gestureImageViewTouchListener.setMinScale(minScale * fitScaleHorizontal);
			}
			else {
				gestureImageViewTouchListener.setMinScale(minScale * fitScaleVertical);
			}
			
			
			gestureImageViewTouchListener.setMaxScale(maxScale * startingScale);
			
			gestureImageViewTouchListener.setFitScaleHorizontal(fitScaleHorizontal);
			gestureImageViewTouchListener.setFitScaleVertical(fitScaleVertical);
			gestureImageViewTouchListener.setCanvasWidth(measuredWidth);
			gestureImageViewTouchListener.setCanvasHeight(measuredHeight);
			gestureImageViewTouchListener.setOnClickListener(onClickListener);
			gestureImageViewTouchListener.setOnLongClickListener(onLongClickListener);
			drawable.setBounds(-hWidth,-hHeight,hWidth,hHeight);

			super.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(customOnTouchListener != null) {
						customOnTouchListener.onTouch(v, event);
					}
					return gestureImageViewTouchListener.onTouch(v, event);
				}
			});	

			layout = true;
		}
	}
	
	protected void computeCropScale(int imageWidth, int imageHeight, int measuredWidth, int measuredHeight) {
		Log.i("1", "16");
		fitScaleHorizontal = (float) measuredWidth / (float) imageWidth;
		fitScaleVertical = (float) measuredHeight / (float) imageHeight;
	}
	
	protected void computeStartingScale(int imageWidth, int imageHeight, int measuredWidth, int measuredHeight) {
		switch(getScaleType()) {

			case CENTER:
				// Center the image in the view, but perform no scaling.
				startingScale = 1.0f;
				break;
				
			case CENTER_CROP:
				// Scale the image uniformly (maintain the image's aspect ratio) so that both dimensions
				// (width and height) of the image will be equal to or larger than the corresponding dimension of the view (minus padding).
				startingScale = Math.max((float) measuredHeight / (float) imageHeight, (float) measuredWidth / (float) imageWidth);
				break;
				
			case CENTER_INSIDE:

				// Scale the image uniformly (maintain the image's aspect ratio) so that both dimensions
				// (width and height) of the image will be equal to or less than the corresponding dimension of the view (minus padding).
				float wRatio = (float) imageWidth / (float) measuredWidth;
				float hRatio = (float) imageHeight / (float) measuredHeight;

				if(wRatio > hRatio) {
					Log.i("1", "17");
					startingScale = fitScaleHorizontal;
				}
				else {
					Log.i("1", "18");
					startingScale = fitScaleVertical;
				}

				break;
		}
	}

	protected boolean isRecycled() {

		if(drawable != null && drawable instanceof BitmapDrawable) {
			Log.i("1", "19");
			Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
			if(bitmap != null) {
				Log.i("1", "20");
				return bitmap.isRecycled();
			}
		}
		return false;
	}

	protected void recycle() {
		if(recycle && drawable != null && drawable instanceof BitmapDrawable) {
			Log.i("1", "21");
			Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
			if(bitmap != null) {
				Log.i("1", "22");
				bitmap.recycle();
			}
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(layout) {
			if(drawable != null && !isRecycled()) {
				canvas.save();
				Log.i("1", "22");
				float adjustedScale = scale * scaleAdjust;

				canvas.translate(x, y);

				if(rotation != 0.0f) {
					Log.i("1", "23");
					canvas.rotate(rotation);
				}

				if(adjustedScale != 1.0f) {
					Log.i("1", "24");
					canvas.scale(adjustedScale, adjustedScale);
				}

				drawable.draw(canvas);

				canvas.restore();
			}

			if(drawLock.availablePermits() <= 0) {
				Log.i("1", "25");
				drawLock.release();
			}
		}
	}

	public boolean waitForDraw(long timeout) throws InterruptedException {
		Log.i("1", "26");
		return drawLock.tryAcquire(timeout, TimeUnit.MILLISECONDS);
	}

	@Override
	protected void onAttachedToWindow() {
		animator = new Animator(this, "GestureImageViewAnimator");
		animator.start();

		if(resId >= 0 && drawable == null) {
			Log.i("1", "27");
			setImageResource(resId);
		}

		super.onAttachedToWindow();
	}

	public void animationStart(Animation animation) {
		if(animator != null) {
			animator.play(animation);
			Log.i("1", "28");
		}
	}

	public void animationStop() {
		if(animator != null) {
			Log.i("1", "29");
			animator.cancel();
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		if(animator != null) {
			animator.finish();
		}
		if(recycle && drawable != null && !isRecycled()) {
			recycle();
			drawable = null;
		}
		super.onDetachedFromWindow();
	}

	protected void initImage() {
		if(this.drawable != null) {
			Log.i("1", "30");
			this.drawable.setAlpha(alpha);
			this.drawable.setFilterBitmap(true);
			if(colorFilter != null) {
				Log.i("1", "31");
				this.drawable.setColorFilter(colorFilter);
			}
		}
		
		if(!layout) {
			Log.i("1", "32");
			requestLayout();
			redraw();
		}
	}

	public void setImageBitmap(Bitmap image) {
		this.drawable = new BitmapDrawable(getResources(), image);
		Log.i("1", "33");
		initImage();

	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		this.drawable = drawable;
		Log.i("1", "34");
		initImage();
	}

	public void setImageResource(int id) {
		if(this.drawable != null) {
			Log.i("1", "35");
			this.recycle();
		}
		if(id >= 0) {
			this.resId = id;
			Log.i("1", "36");
			setImageDrawable(getContext().getResources().getDrawable(id));
		}
	}

	public int getScaledWidth() {
		return Math.round(getImageWidth() * getScale());
	}
	
	public int getScaledHeight() {
		return Math.round(getImageHeight() * getScale());
	}
	
	public int getImageWidth() {
		if(drawable != null) {
			return drawable.getIntrinsicWidth();
		}
		return 0;
	}

	public int getImageHeight() {
		if(drawable != null) {
			return drawable.getIntrinsicHeight();
		}
		return 0;
	}

	public void moveBy(float x, float y) {
		this.x += x;
		this.y += y;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void redraw() {
		postInvalidate();
	}

	public void setMinScale(float min) {
		this.minScale = min;
		if(gestureImageViewTouchListener != null) {
			gestureImageViewTouchListener.setMinScale(min * fitScaleHorizontal);
		}
	}

	public void setMaxScale(float max) {
		this.maxScale = max;
		if(gestureImageViewTouchListener != null) {
			gestureImageViewTouchListener.setMaxScale(max * startingScale);
		}
	}

	public void setScale(float scale) {
		scaleAdjust = scale;
	}

	public float getScale() {
		return scaleAdjust;
	}

	public float getImageX() {
		return x;
	}

	public float getImageY() {
		return y;
	}

	public boolean isStrict() {
		return strict;
	}

	public void setStrict(boolean strict) {
		this.strict = strict;
	}

	public boolean isRecycle() {
		return recycle;
	}

	public void setRecycle(boolean recycle) {
		this.recycle = recycle;
	}

	public void reset() {
		x = centerX;
		y = centerY;
		scaleAdjust = startingScale;
		if (gestureImageViewTouchListener != null) {
		    gestureImageViewTouchListener.reset();
		}
		redraw();
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public void setGestureImageViewListener(GestureImageViewListener pinchImageViewListener) {
		this.gestureImageViewListener = pinchImageViewListener;
	}

	public GestureImageViewListener getGestureImageViewListener() {
		return gestureImageViewListener;
	}

	@Override
	public Drawable getDrawable() {
		return drawable;
	}

	@Override
	public void setAlpha(int alpha) {
		Log.i("1", "37");
		this.alpha = alpha;
		if(drawable != null) {
			Log.i("1", "38");
			drawable.setAlpha(alpha);
		}
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		this.colorFilter = cf;
		Log.i("1", "39");
		if(drawable != null) {
			Log.i("1", "40");
			drawable.setColorFilter(cf);
		}
	}

	@Override
	public void setImageURI(Uri mUri) {
		if ("content".equals(mUri.getScheme())) {
			try {
				String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
				
				Cursor cur = getContext().getContentResolver().query(mUri, orientationColumn, null, null, null);
				
				if (cur != null && cur.moveToFirst()) {
					imageOrientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
				}  
				
				InputStream in = null;
				
				try {
					in = getContext().getContentResolver().openInputStream(mUri);
					Bitmap bmp = BitmapFactory.decodeStream(in);
					
					if(imageOrientation != 0) {
						Matrix m = new Matrix();
						m.postRotate(imageOrientation);
						Bitmap rotated = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), m, true);
						bmp.recycle();
						setImageDrawable(new BitmapDrawable(getResources(), rotated));
					}
					else {
						setImageDrawable(new BitmapDrawable(getResources(), bmp));
					}
				}
				finally {
					if(in != null) {
						in.close();
					}
					
					if(cur != null) {
						cur.close();
					}
				}
			}
			catch (Exception e) {
				Log.w("GestureImageView", "Unable to open content: " + mUri, e);
			}
		}
		else {
			setImageDrawable(Drawable.createFromPath(mUri.toString()));
		}

		if (drawable == null) {
			Log.e("GestureImageView", "resolveUri failed on bad bitmap uri: " + mUri);
			// Don't try again.
			mUri = null;
		}
	}

	@Override
	public Matrix getImageMatrix() {
		if(strict) {
			throw new UnsupportedOperationException("Not supported");
		}		
		return super.getImageMatrix();
	}

	@Override
	public void setScaleType(ScaleType scaleType) {
		if(scaleType == ScaleType.CENTER ||
			scaleType == ScaleType.CENTER_CROP ||
			scaleType == ScaleType.CENTER_INSIDE) {
			
			super.setScaleType(scaleType);
		}
		else if(strict) {
			throw new UnsupportedOperationException("Not supported");
		}
	}

	@Override
	public void invalidateDrawable(Drawable dr) {
		if(strict) {
			throw new UnsupportedOperationException("Not supported");
		}
		super.invalidateDrawable(dr);
	}

	@Override
	public int[] onCreateDrawableState(int extraSpace) {
		if(strict) {
			throw new UnsupportedOperationException("Not supported");
		}
		return super.onCreateDrawableState(extraSpace);
	}

	@Override
	public void setAdjustViewBounds(boolean adjustViewBounds) {
		if(strict) {
			throw new UnsupportedOperationException("Not supported");
		}
		super.setAdjustViewBounds(adjustViewBounds);
	}

	@Override
	public void setImageLevel(int level) {
		if(strict) {
			throw new UnsupportedOperationException("Not supported");
		}
		super.setImageLevel(level);
	}

	@Override
	public void setImageMatrix(Matrix matrix) {
		if(strict) {
			throw new UnsupportedOperationException("Not supported");
		}
	}

	@Override
	public void setImageState(int[] state, boolean merge) {
		if(strict) {
			throw new UnsupportedOperationException("Not supported");
		}
	}

	@Override
	public void setSelected(boolean selected) {
		if(strict) {
			throw new UnsupportedOperationException("Not supported");
		}
		super.setSelected(selected);
	}

	@Override
	public void setOnTouchListener(OnTouchListener l) {
		this.customOnTouchListener = l;
	}
	
	public float getCenterX() {
		return centerX;
	}
	
	public float getCenterY() {
		return centerY;
	}
	
	public boolean isLandscape() {
		return getImageWidth() >= getImageHeight();
	}
	
	public boolean isPortrait() {
		return getImageWidth() <= getImageHeight();
	}
	
	public void setStartingScale(float startingScale) {
		this.startingScale = startingScale;
	}
	
	public void setStartingPosition(float x, float y) {
		this.startX = x;
		this.startY = y;
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		this.onClickListener = l;
		
		if(gestureImageViewTouchListener != null) {
			gestureImageViewTouchListener.setOnClickListener(l);
		}
	}

	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		this.onLongClickListener = l;

		if(gestureImageViewTouchListener != null) {
			gestureImageViewTouchListener.setOnLongClickListener(l);
		}
	}

	/**
	 * Returns true if the image dimensions are aligned with the orientation of the device.
	 * @return
	 */
	public boolean isOrientationAligned() {
		if(deviceOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			return isLandscape();
		}
		else if(deviceOrientation == Configuration.ORIENTATION_PORTRAIT) {
			return isPortrait();
		}
		return true;
	}
	
	public int getDeviceOrientation() {
		return deviceOrientation;
	}
}
