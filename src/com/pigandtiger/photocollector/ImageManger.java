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
	
	private static final String PHOTO_SAVE_PATH = Environment.getExternalStorageDirectory() + "/cached_photos/";
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
		if( imageMap.contains(name) ){
			return true;
		}else{
			final String imgDetailSrc = String.format(PHOTO_DETAIL_IMAGE_SRC, (int)(Math.random()*3) + 1,name,suffix);
			final String imgThumbSrc = String.format(PHOTO_THUMB_IMAGE_SRC, (int)(Math.random()*3) + 1,name,suffix);
			InputStream is = null;
			boolean isSaved = false;
			Bitmap bitmap = null;
			final String imgPath = PHOTO_SAVE_PATH + name;
			try {
				File file = new File(imgPath);
				if( file.exists() ){
					bitmap = BitmapFactory.decodeFile(imgPath);
					isSaved = true;
				}else{
					is = getImageStream(imgThumbSrc);
					bitmap = BitmapFactory.decodeStream(is);
					isSaved = saveBitmap(bitmap,name);
				}
				CachedImage image = new CachedImage(name);
				image.setSaved(isSaved);
				image.setBitmap(bitmap);
				
				imageMap.put(name, image);
				
				_Log.i(tag, "loaded image %1$s.",name);
				
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
	}
	
	public static Bitmap getImageById(String imgId){
		if( imageMap.containsKey(imgId) ){
			return imageMap.get(imgId).getBitmap();
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
	
	public static boolean saveBitmap(Bitmap bm, String fileName) {  
		boolean isSuccess = false;
        File dirFile = new File(PHOTO_SAVE_PATH);  
        if(!dirFile.exists()){  
            dirFile.mkdir();  
        }  
        File myCaptureFile = new File(PHOTO_SAVE_PATH + fileName);  
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
