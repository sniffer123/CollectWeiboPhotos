package com.pigandtiger.ui;

import android.view.View;

public interface IListItemActionListener<E> {

	public void onClickItem(E entity,int position,View v);
	
}
