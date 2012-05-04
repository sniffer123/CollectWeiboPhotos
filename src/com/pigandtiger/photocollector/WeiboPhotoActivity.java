package com.pigandtiger.photocollector;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

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

public class WeiboPhotoActivity extends Activity{

	private static final String tag = "WeiboPhotoActivity.java";
	
	private ImageView imgPhoto = null;
	
	private String photoId = "";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weibo_photo);

        photoId = getIntent().getStringExtra("PHOTO_ID");
        
        setupView();
        updateView();
        
    }
    
    private void setupView(){
    	imgPhoto = (ImageView)findViewById(R.id.imgPhoto);
    }
    
    public void updateView(){
    	CachedImage img = ImageManger.getImageById(photoId);
    	imgPhoto.setImageBitmap(img.getBitmap());
    }
    
}

