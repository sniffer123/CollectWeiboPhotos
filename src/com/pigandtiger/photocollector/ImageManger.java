package com.pigandtiger.photocollector;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.pigandtiger.frameworks.asyncjob.AsyncTaskManager;
import com.pigandtiger.frameworks.asyncjob.CallBackTaskResult;
import com.pigandtiger.frameworks.asyncjob.ICallbackListener;
import com.pigandtiger.frameworks.asyncjob.IJobExecutor;
import com.pigandtiger.utility._Log;


public class ImageManger {
	
	private static final String tag = "ImageManger.java";
	
	private static final String PHOTO_DETAIL_SAVE_PATH = Environment.getExternalStorageDirectory() + "/cached_photos/detail/";
	private static final String PHOTO_THUMB_SAVE_PATH = Environment.getExternalStorageDirectory() + "/cached_photos/thumb/";
	private static final String PHOTO_DETAIL_IMAGE_SRC = "http://ww%1$d.sinaimg.cn/bmiddle/%2$s.%3$s";
	private static final String PHOTO_THUMB_IMAGE_SRC = "http://ww%1$d.sinaimg.cn/thumb150/%2$s.%3$s";
	
	private static ImageManger instance = null;
	private static int[] lock = new int[0];
	
	private static ConcurrentHashMap<String, CachedImage> imageMap = new ConcurrentHashMap<String, CachedImage>();

	private ImageManger(){
		imageMap.clear();
	}
	
	public static ImageManger getInstance(){
		if( instance == null ){
			synchronized (lock) {
				if( instance == null ){
					instance = new ImageManger();
				}
			}
		}
		return instance;
	}
	
	public static void asyncLoadImage(final String name,final String suffix){
		
		AsyncTaskManager.doJob(new ICallbackListener() {
			
			public void onCallbackFailed(CallBackTaskResult result) {
				// TODO Auto-generated method stub
				_Log.i(tag, "load image %1$s failed.",name);
			}
			
			public void onCallbackComplete(CallBackTaskResult result) {
				_Log.i(tag, "load image %1$s successfully.",name);
			}
		}, new IJobExecutor() {
			
			public void execute(CallBackTaskResult result, Object... params)
					throws Exception {
				boolean isSuccess = loadImage(name, suffix);
				result.isSuccess = isSuccess;
			}
		});
		
	}
	
	public static boolean loadImage(final String name,final String suffix){
		if( imageMap.containsKey(name) ){
			return true;
		}else{
			boolean isSuccess = true;
			final String imgDetailSrc = String.format(PHOTO_DETAIL_IMAGE_SRC, (int)(Math.random()*3) + 1,name,suffix);
			final String imgThumbSrc = String.format(PHOTO_THUMB_IMAGE_SRC, (int)(Math.random()*3) + 1,name,suffix);
			CachedImage image = null;
			if( imageMap.containsKey(name) ){
				image = imageMap.get(name);
			}else{
				image = new CachedImage(name);
				imageMap.put(name, image);
			}
			try{
				image.setBitmap(__loadImage(name,PHOTO_DETAIL_SAVE_PATH,imgDetailSrc));
				image.setThumbBitmap(__loadImage(name,PHOTO_THUMB_SAVE_PATH,imgThumbSrc));
				_Log.i(tag, "loaded image %2$s.",name);
			}catch(Exception e){
				isSuccess = false;
			}
			return isSuccess;
		}
	}
	
	private static Bitmap __loadImage(final String name,final String path,final String src) throws Exception{
		InputStream is = null;
		Bitmap bitmap = null;
		final String imgPath = path + name;
		File file = new File(imgPath);
		if( file.exists() ){
			bitmap = BitmapFactory.decodeFile(imgPath);
		}else{
			is = getImageStream(src);
			bitmap = BitmapFactory.decodeStream(is);
			saveBitmap(bitmap,path,name);
			
		}
		return bitmap;
		
	}
	
	public static CachedImage getImageById(String imgId){
		if( imageMap.containsKey(imgId) ){
			return imageMap.get(imgId);
		}else{
			throw new IllegalArgumentException(String.format("Can not find image[%1$s] in image cache.",imgId));
		}
	}
	
	public static InputStream getImageStream(String src) throws Exception{     
        URL url = new URL(src);     
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();     
        conn.setConnectTimeout(5 * 1000);     
        conn.setRequestMethod("GET");  
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){     
            return conn.getInputStream();
        }     
        return null;   
    }
	
	public static boolean saveBitmap(Bitmap bm, String path,String fileName) {  
		boolean isSuccess = false;
        File dirFile = new File(path);  
        if(!dirFile.exists()){  
            dirFile.mkdir();  
        }  
        File myCaptureFile = new File(path + fileName);  
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
			fos = new FileOutputStream(myCaptureFile);
			bos = new BufferedOutputStream(fos); 
	        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);  
	        bos.flush();  
	        isSuccess = true;
	        _Log.i(tag, "save file %1$s successfully.",fileName);
		} catch (IOException e) {
			e.printStackTrace();
			isSuccess = false;
		}finally{
			if( fos != null ){
				try {
					fos.close();
				} catch (IOException e) {
					_Log.i(tag, "Can not close file.");
				}
			}
			fos = null;
			
			if( bos != null ){
				try {
					bos.close();
				} catch (IOException e) {
					_Log.i(tag, "Can not close file.");
				}
			}
			bos = null;
			
		}
        return isSuccess;
    }
}
