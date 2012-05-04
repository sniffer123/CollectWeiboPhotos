package com.pigandtiger.photocollector;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.pigandtiger.ui.IListItemActionListener;
import com.pigandtiger.ui.ListItemViewTemplate;

import entites.WeiboPhotoEntity;

public class WeiboGalleryListItemView extends ListItemViewTemplate<WeiboPhotoEntity>{

	private ImageView imgPhoto = null;
	private TextView labelPhotoDesc = null;
	
	protected WeiboGalleryListItemView(Context context) {
		super(context, R.layout.weibo_gallery_photo_list_item);
		setupView();
	}

	@Override
	protected void setupView() {
		// TODO Auto-generated method stub
		imgPhoto = (ImageView)findViewById(R.id.imgPhoto);
		labelPhotoDesc = (TextView)findViewById(R.id.labelPhotoDesc);
		
		this.setOnClickListener(this);
	}
	
	@Override
	public void updateView(WeiboPhotoEntity e,
			IListItemActionListener<WeiboPhotoEntity> l, int position) {
		// TODO Auto-generated method stub
		super.updateView(e, l, position);
		
		imgPhoto.setImageBitmap(ImageManger.getImageById(e.getId()));
		labelPhotoDesc.setText(e.getDesc());
	}
	
}
