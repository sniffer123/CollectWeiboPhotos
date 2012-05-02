package com.pigandtiger.photocollector.weibotools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.pigandtiger.utility.Utility;
import com.pigandtiger.utility._Log;

import entites.AppData;
import entites.WeiboAlbumEntity;
import entites.WeiboGalleryEntity;

public class PhotoCollector implements WeiboConfig {

	public static final String tag = "PhotoCollector.java";

	private WeiboContext context = null;
	

	public PhotoCollector(final WeiboContext context) {
		if( context.getUniqueId() == null ){
			throw new IllegalArgumentException("Invalid weibo context provided.You should perfom login first.");
		}
		this.context = context;
	}
	
	public void request(final int targetId) throws ClientProtocolException, IOException{
		
		String result = WeiboTools.requestData(context, "http://photo.weibo.com/" + targetId + "/albums");
		final WeiboAlbumEntity album = AppData.getInstance().getAlbum();
		// parse user name
		final String nameRegx = "<a href=\"http://weibo.com/" + targetId + "/profile\".* title=\"(.*)\">";
		final String userName = Utility.pickByRegx(nameRegx,result);
		album.setUserName(userName);
		_Log.i(tag, "User Name:" + userName);
		// parse gallery src
		final String gallerySrcRegx = "<dd class=\"name\"><a href=\"(/" + targetId + "[^\"]+)";
		final ArrayList<String> gallerySrcList = Utility.pickGroupByRegx(gallerySrcRegx, result);
		// parse gallery name
		final String galleryNameRegx = "<dd class=\"name\"><a[^>]+>([^<]+)</a></dd>";
		final ArrayList<String> galleryNameList = Utility.pickGroupByRegx(galleryNameRegx, result);
		// parse gallery size
		final String gallerySizeRegx = "<dd class=\"gray\">[^<]*\\(([0-9]+)\\)[^<]+\\(";
		final ArrayList<String> gallerySizeList = Utility.pickGroupByRegx(gallerySizeRegx, result);
		
		// generate galleries in the album
		Map<String, WeiboGalleryEntity> galleryMap = album.getGalleryMap();
		for(int i = 0; i < gallerySrcList.size(); i++){
			WeiboGalleryEntity gallery = new WeiboGalleryEntity();
			gallery.setId(gallerySrcList.get(i));
			gallery.setSrc(gallerySrcList.get(i));
			gallery.setName(galleryNameList.get(i));
			gallery.setSize(Integer.parseInt(gallerySizeList.get(i)));
			galleryMap.put(gallery.getId(), gallery);
		}
		
		for(WeiboGalleryEntity gallery : galleryMap.values()){
			_Log.i(tag, "Name:%1$s,Src:%2$s,Size:%3$s",gallery.getName(),gallery.getSrc(),gallery.getSize());
		}
		
	}

}
