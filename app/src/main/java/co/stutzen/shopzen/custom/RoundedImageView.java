package co.stutzen.shopzen.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundedImageView extends ImageView {

    public RoundedImageView(Context context) {
        super(context);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        BitmapDrawable drawable = (BitmapDrawable) getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Bitmap fullSizeBitmap = drawable.getBitmap();
        if (fullSizeBitmap.getWidth() >= fullSizeBitmap.getHeight()){

        	fullSizeBitmap = Bitmap.createBitmap(
        			fullSizeBitmap, 
        			fullSizeBitmap.getWidth()/2 - fullSizeBitmap.getHeight()/2,
			     0,
			     fullSizeBitmap.getHeight(), 
			     fullSizeBitmap.getHeight()
			     );

			}else{

				fullSizeBitmap = Bitmap.createBitmap(
						fullSizeBitmap,
			     0, 
			     fullSizeBitmap.getHeight()/2 - fullSizeBitmap.getWidth()/2,
			     fullSizeBitmap.getWidth(),
			     fullSizeBitmap.getWidth() 
			     );
			}
        int scaledWidth = getMeasuredWidth();
        int scaledHeight = getMeasuredHeight();

        Bitmap mScaledBitmap;
        if (scaledWidth == fullSizeBitmap.getWidth()
                && scaledHeight == fullSizeBitmap.getHeight()) {
            mScaledBitmap = fullSizeBitmap;
        } else {
            mScaledBitmap = Bitmap.createScaledBitmap(fullSizeBitmap,
                    scaledWidth, scaledHeight, true /* filter */);
        }
        Bitmap circleBitmap = getCircledBitmap(mScaledBitmap);

        canvas.drawBitmap(circleBitmap, 0, 0, null);

    }

    Bitmap getCircledBitmap(Bitmap bitmap) {

        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);

        
        Canvas canvas = new Canvas(result);

        int color = Color.BLUE;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getHeight()/2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        


        return result;
    }

}