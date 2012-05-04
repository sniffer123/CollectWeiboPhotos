package com.pigandtiger.photocollector;

import android.content.Context;
import android.widget.TextView;

import com.pigandtiger.ui.IListItemActionListener;
import com.pigandtiger.ui.ListItemViewTemplate;
import com.pigandtiger.utility._Log;

import entites.WeiboGalleryEntity;

public class WeiboAlbumListItemView extends ListItemViewTemplate<WeiboGalleryEntity>{

	private static final String tag = "WeiboAlbumListItemView.java";
	
	private TextView labelGalleryName = null;
	
	protected WeiboAlbumListItemView(Context context) {
		super(context, R.layout.weibo_albums_gallery_list_item);
		setupView();
	}

	@Override
	protected void setupView() {
		// TODO Auto-generated method stub
		labelGalleryName = (TextView)findViewById(R.id.labelGalleryName);
		
		this.setOnClickListener(this);
	}
	
	@Override
	public void updateView(WeiboGalleryEntity e,
			IListItemActionListener<WeiboGalleryEntity> l, int position) {
		// TODO Auto-generated method stub
		super.updateView(e, l, position);
		_Log.i(tag, "gallery name: " + e.getName());
		labelGalleryName.setText(e.getName());
	}
	
}
