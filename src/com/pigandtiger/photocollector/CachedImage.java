package com.pigandtiger.photocollector;

import android.graphics.Bitmap;

public class CachedImage {

	private String name = "";
	private boolean isSavedDetail = false;
	private boolean isSavedThumb = false;
	private Bitmap bitmap = null;
	private Bitmap thumbBitmap = null;
	
	public CachedImage(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSavedDetail() {
		return isSavedDetail;
	}

	public void setSavedDetail(boolean isSavedDetail) {
		this.isSavedDetail = isSavedDetail;
	}

	public boolean isSavedThumb() {
		return isSavedThumb;
	}

	public void setSavedThumb(boolean isSavedThumb) {
		this.isSavedThumb = isSavedThumb;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public Bitmap getThumbBitmap() {
		return thumbBitmap;
	}

	public void setThumbBitmap(Bitmap thumbBitmap) {
		this.thumbBitmap = thumbBitmap;
	}

	
	
}
