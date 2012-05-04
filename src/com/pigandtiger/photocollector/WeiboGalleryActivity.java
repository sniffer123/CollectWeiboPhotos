package com.pigandtiger.photocollector;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.pigandtiger.frameworks.asyncjob.AsyncTaskManager;
import com.pigandtiger.frameworks.asyncjob.CallBackTaskResult;
import com.pigandtiger.frameworks.asyncjob.ICallbackListener;
import com.pigandtiger.frameworks.asyncjob.IJobExecutor;
import com.pigandtiger.ui.IListItemActionListener;
import com.pigandtiger.ui.ListAdapterTemplate;
import com.pigandtiger.utility._Log;

import entites.AppData;
import entites.WeiboGalleryEntity;
import entites.WeiboPhotoEntity;

public class WeiboGalleryActivity extends Activity implements IListItemActionListener<WeiboPhotoEntity>,ICallbackListener{

	private static final String tag = "WeiboGalleryActivity.java";
	
	private GridView photoGrid = null;
	private ListAdapterTemplate<WeiboPhotoEntity, WeiboGalleryListItemView> adapterPhotos = null;
	
	private String galleryId = "";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weibo_gallery);

        galleryId = getIntent().getStringExtra("GALLERY_ID");
        
        setupView();
//        updateView();
        
        _Log.i(tag, "To fetch photos...");
        AsyncTaskManager.doJob(this.getMainLooper(),this, new IJobExecutor() {
			
			@Override
			public void execute(CallBackTaskResult result, Object... params)
					throws Exception {
				final WeiboGalleryEntity gallery = AppData.getInstance().getAlbum().getGalleryMap().get(galleryId);
				for( WeiboPhotoEntity photo : gallery.getPhotos().values() ){
					ImageManger.loadImage(photo.getId(), photo.getSuffix());
				}
			}
		});
        
    }
    
    private void setupView(){
    	
    	photoGrid = (GridView)findViewById(R.id.photoGrid);
    	adapterPhotos = new ListAdapterTemplate<WeiboPhotoEntity, WeiboGalleryListItemView>(this) {
			
			@Override
			public WeiboGalleryListItemView buildView(Context context) {
				// TODO Auto-generated method stub
				return new WeiboGalleryListItemView(WeiboGalleryActivity.this);
			}
			
			@Override
			public List<WeiboPhotoEntity> buildItems() {
				// TODO Auto-generated method stub
				return new ArrayList<WeiboPhotoEntity>();
			}
		};
		photoGrid.setAdapter(adapterPhotos);
    }
    
    public void updateView(){
    	WeiboGalleryEntity gallery = AppData.getInstance().getAlbum().getGalleryMap().get(galleryId);
//    	ArrayList<WeiboPhotoEntity> photoList = new ArrayList<WeiboPhotoEntity>();
//    	for( WeiboPhotoEntity photo : gallery.getPhotos().values() ){
//    		photoList.add(photo);
//    	}
    	adapterPhotos.setItems(gallery.getPhotos());
    	adapterPhotos.notifyDataSetChanged();
    }
    

	public void onCallbackComplete(CallBackTaskResult result) {
		_Log.i(tag, "call back successfully");
		updateView();
	}

	public void onCallbackFailed(CallBackTaskResult result) {
		_Log.i(tag, "call back failed");
		
	}

	public void onClickItem(WeiboPhotoEntity entity, int position, View v) {
		
	}
}

