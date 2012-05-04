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
import entites.WeiboPhotoEntity;

public class PhotoCollector implements WeiboConfig {

	public static final String tag = "PhotoCollector.java";
	
	private static final int PHOTO_PER_PAGE = 20;

	private WeiboContext context = null;
	

	public PhotoCollector(final WeiboContext context) {
		if( context.getUniqueId() == null ){
			throw new IllegalArgumentException("Invalid weibo context provided.You should perfom login first.");
		}
		this.context = context;
	}
	
	public void request(final int targetId) throws ClientProtocolException, IOException{
		final String albumsURL = "http://photo.weibo.com/" + targetId + "/albums";
		String result = WeiboTools.requestData(context, albumsURL);
		_Log.i(tag, "To request albums through URL: " + albumsURL);
		
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
			gallery.setSrc("http://photo.weibo.com" + gallerySrcList.get(i));
			gallery.setName(galleryNameList.get(i));
			gallery.setSize(Integer.parseInt(gallerySizeList.get(i)));
			galleryMap.put(gallery.getId(), gallery);
		}
		
		for(WeiboGalleryEntity gallery : galleryMap.values()){
			_Log.i(tag, "Name:%1$s,Src:%2$s,Size:%3$s",gallery.getName(),gallery.getSrc(),gallery.getSize());
			parseGalleryPage(gallery);
		}
		
	}
	
	private void parseGalleryPage(WeiboGalleryEntity gallery) throws ClientProtocolException, IOException{
		final int pageSize = (int) Math.ceil((double)gallery.getSize()/(double)PHOTO_PER_PAGE);
		for(int i = 1; i <= pageSize; i++ ){
			__parseGalleryPage(gallery,i);
		}
		
	}
	
	private void __parseGalleryPage(WeiboGalleryEntity gallery,int page) throws ClientProtocolException, IOException{
		final String targetURL = gallery.getSrc() + "/index/page/" + page;
		_Log.i(tag, "to get photo: " + targetURL);
		String result = WeiboTools.requestData(context, targetURL);
		// parse photo info
		final String photoInfoRegx = "<dl>\\s+<dt><a href=\"([^\"]+)\"><img src=\"([^\"]+)\" /></a></dt>\\s+<dd>([^<]+)</dd>\\s+</dl>";
		final ArrayList<ArrayList<String>> photoInfoList = Utility.pickMultiGroupByRegx(photoInfoRegx, result);
		for( ArrayList<String> photoInfo :  photoInfoList ){
//			_Log.i(tag, "detail src:%1$s,thumb src:%2$s,desc:%3$s",photoInfo.get(0),photoInfo.get(1),photoInfo.get(2));
			final String pageSrc = photoInfo.get(0);
			final String thumbSrc = photoInfo.get(1);
			final ArrayList<String> params = Utility.pickMutilByRegx("http://ww[0-9]+.sinaimg.cn/thumb150/([^\\.]+).([png|jpg|jpeg|gif]+)", thumbSrc);
			final String imageName = params.get(0);
			final String imageSuffix = params.get(1);
			final String desc = photoInfo.get(2);
			WeiboPhotoEntity photo = new WeiboPhotoEntity();
			photo.setPageSrc(pageSrc);
			photo.setId(imageName);
			photo.setSuffix(imageSuffix);
			photo.setDesc(desc);
			gallery.getPhotos().put(photo.getId(), photo);
		}
		
		
	}

}
