package co.stutzen.shopzen.custom.zooming;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class FlingListener extends SimpleOnGestureListener {
	
	private float velocityX;
	private float velocityY;
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		return true;
	}

	public float getVelocityX() {
		return velocityX;
	}
	
	public float getVelocityY() {
		return velocityY;
	}
}
