package entites;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class WeiboGalleryEntity {

	private String name = "";
	private String id = "";
	private String src = "";
	private String thumbSrc = "";
	private String thumbFileName = "";
	private int size = 0;
	
	private Map<String, WeiboPhotoEntity> photos = null;
	
	public WeiboGalleryEntity() {
		photos = Collections.synchronizedMap(new LinkedHashMap<String, WeiboPhotoEntity>());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getThumbSrc() {
		return thumbSrc;
	}

	public void setThumbSrc(String thumbSrc) {
		this.thumbSrc = thumbSrc;
	}

	public String getThumbFileName() {
		return thumbFileName;
	}

	public void setThumbFileName(String thumbFileName) {
		this.thumbFileName = thumbFileName;
	}

	public Map<String, WeiboPhotoEntity> getPhotos() {
		return photos;
	}

	private void setPhotos(Map<String, WeiboPhotoEntity> photos) {
		this.photos = photos;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	
}
