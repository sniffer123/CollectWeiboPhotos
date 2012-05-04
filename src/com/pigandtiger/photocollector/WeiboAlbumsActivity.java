package com.pigandtiger.photocollector;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.pigandtiger.frameworks.asyncjob.CallBackTaskResult;
import com.pigandtiger.ui.IListItemActionListener;
import com.pigandtiger.ui.ListAdapterTemplate;
import com.pigandtiger.utility._Log;

import entites.AppData;
import entites.WeiboGalleryEntity;

public class WeiboAlbumsActivity extends Activity implements IListItemActionListener<WeiboGalleryEntity>{

	private static final String tag = "WeiboAlbumsActivity.java";
	
	private ListView listGallery = null;
	private ListAdapterTemplate<WeiboGalleryEntity, WeiboAlbumListItemView> adapterGallery = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weibo_albums);

        setupView();
        updateView();
        
    }
    
    private void setupView(){
    	
    	listGallery = (ListView)findViewById(R.id.listGallery);
    	adapterGallery = new ListAdapterTemplate<WeiboGalleryEntity, WeiboAlbumListItemView>(this) {
			
			@Override
			public WeiboAlbumListItemView buildView(Context context) {
				// TODO Auto-generated method stub
				return new WeiboAlbumListItemView(WeiboAlbumsActivity.this);
			}
			
			@Override
			public List<WeiboGalleryEntity> buildItems() {
				// TODO Auto-generated method stub
				return new ArrayList<WeiboGalleryEntity>();
			}
		};
		listGallery.setAdapter(adapterGallery);
    }
    
    public void updateView(){
    	ArrayList<WeiboGalleryEntity> galleryList = new ArrayList<WeiboGalleryEntity>();
    	for( WeiboGalleryEntity gallery : AppData.getInstance().getAlbum().getGalleryMap().values() ){
    		galleryList.add(gallery);
    	}
    	adapterGallery.setItems(galleryList);
    	adapterGallery.notifyDataSetChanged();
    }
    

	public void onClickItem(WeiboGalleryEntity entity, int position, View v) {
		Intent intent = new Intent();
		intent.putExtra("GALLERY_ID", entity.getId());
		intent.setClass(this, WeiboGalleryActivity.class);
		startActivity(intent);
	}
}
