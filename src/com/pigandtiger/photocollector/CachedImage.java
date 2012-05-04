package com.pigandtiger.photocollector;

import android.graphics.Bitmap;

public class CachedImage {

	private String name = "";
	private boolean isSaved = false;
	private Bitmap bitmap = null;
	
	public CachedImage(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSaved() {
		return isSaved;
	}

	public void setSaved(boolean isSaved) {
		this.isSaved = isSaved;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	
	
}
