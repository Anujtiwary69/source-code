package co.stutzen.shopzen.bo;

import android.graphics.Bitmap;

public class ParentBo {
	private String name;
	private String text1;
	private String text2;
	private Bitmap image;
	private String id;
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

}
