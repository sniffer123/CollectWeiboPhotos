package com.pigandtiger.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public abstract class ListItemViewTemplate<E> extends LinearLayout implements OnClickListener{
	
	private IListItemActionListener<E> mListItemActionListener = null;
	private OnClickListener mOnClickItemListener = null;
	private E mEntity = null;
	private int mPosition = -1;
	

	protected ListItemViewTemplate(Context context,AttributeSet attrs,int resLayoutID) {
		super(context,attrs);
		init( context, resLayoutID);
	}
	
	protected ListItemViewTemplate(Context context,int resLayoutID) {
		super(context);
		init( context, resLayoutID);
	}
	
	private void init(Context context,int resLayoutID)
	{
		LayoutInflater inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		inflater.inflate(resLayoutID, this, true);
		
	}
	
	/***
	 * NOTICE:
	 *   You must override it and invoke it explicitly before running updateView.
	 */
	protected abstract void setupView();
	
	public void updateView(E e,IListItemActionListener<E> l,int position,OnClickListener onClickItemListener) {
		updateView(e,l,position);
		mOnClickItemListener = onClickItemListener;
	}
	
	public void updateView(E e,IListItemActionListener<E> l,int position) {
		mListItemActionListener = l;
		mEntity = e;
		mPosition = position;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if( mListItemActionListener != null && mEntity != null)
		{
			mListItemActionListener.onClickItem(mEntity, mPosition,v);
			
			if( mOnClickItemListener != null ){
				mOnClickItemListener.onClick(this);
			}
		}
	}
	

}
