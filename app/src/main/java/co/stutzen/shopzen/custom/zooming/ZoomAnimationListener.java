package co.stutzen.shopzen.custom.zooming;

public interface ZoomAnimationListener {
	public void onZoom(float scale, float x, float y);
	public void onComplete();
}
