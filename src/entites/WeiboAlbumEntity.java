package entites;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class WeiboAlbumEntity {

	private String userName = "";
	private Map<String, WeiboGalleryEntity> galleryMap = null;
	
	public WeiboAlbumEntity() {
		galleryMap = Collections.synchronizedMap(new LinkedHashMap<String, WeiboGalleryEntity>());
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Map<String, WeiboGalleryEntity> getGalleryMap() {
		return galleryMap;
	}
	private void setGalleryMap(LinkedHashMap<String, WeiboGalleryEntity> galleryMap) {
		this.galleryMap = galleryMap;
	}
	
	
}
