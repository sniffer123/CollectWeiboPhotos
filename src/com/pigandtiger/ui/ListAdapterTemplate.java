package com.pigandtiger.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.pigandtiger.photocollector.R;

/***
 * 
 * @author kevin
 *
 * @param <E>
 * E refs Element type
 * @param <V>
 * V refs View type
 * @param <T>
 */
public abstract class ListAdapterTemplate<E,V extends ListItemViewTemplate<E>>  extends BaseAdapter{

	protected List<E> mItems = null;
//	private Context mContext = null;
	private IListItemActionListener<E> mListItemActionListener = null;
	
	private int mSelectedPosition = -1;
	private boolean mRequiredHighLight = false;
	private int selectedBgResId = 0;
	private ArrayList<View> mViewList = new ArrayList<View>();
	
	public ListAdapterTemplate(IListItemActionListener<E> l,boolean requiredHighLight,int selectedBgResId) {
		mItems = buildItems();
		mListItemActionListener = l;
		mRequiredHighLight = requiredHighLight;
		if( selectedBgResId == 0 ){
			this.selectedBgResId = R.drawable.ic_shape_list_item_bg_chosen;
		}else{
			this.selectedBgResId = selectedBgResId;
		}
	}
	
	public ListAdapterTemplate(IListItemActionListener<E> l,boolean requiredHighLight) {
		this(l,requiredHighLight,0);
	}
	
	public ListAdapterTemplate(IListItemActionListener<E> l) {
		this(l,false,0);
	}
	
	public void setItems(List<? extends E> items) {
		mItems.clear();
		if( items != null ){
			mItems.addAll(items);
		}
	}
	
	public void setItems(Map items) {
		mItems.clear();
		if( items != null ){
			for( Object entity : items.values() ){
				mItems.add((E)entity);
			}
		}
	}
	
	public abstract V buildView(Context context);
	
	public abstract List<E> buildItems();
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if( mItems != null)
		{
			return mItems.size();
		}
		else
		{
			return 0;
		}
	}

	@Override
	public E getItem(int index) {
		if( mItems != null )
		{
			return mItems.get(index);
		}
		else
		{
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		V view = null;
		if (convertView == null) {
			view = buildView(parent.getContext());
			mViewList.add(view);
		} else {
			view = (V) convertView;
		}
		
		if( mRequiredHighLight ){
			view.updateView(getItem(position),mListItemActionListener,position,mOnClickItemListener);
		}else{
			view.updateView(getItem(position),mListItemActionListener,position);
		}
		
		if( mRequiredHighLight )
		{
			if( position == mSelectedPosition )
			{
				view.setBackgroundDrawable(view.getContext().getResources().getDrawable(selectedBgResId));
			}
			else
			{
				view.setBackgroundDrawable(null);
			}
			
			view.setTag(position);
		}
		
		
		return view;
	}
	
	
	
	private OnClickListener mOnClickItemListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if( mRequiredHighLight ){
				
				clearHighLightBg();
				if( v != null)
				{
					v.setBackgroundDrawable(v.getContext().getResources().getDrawable(selectedBgResId));
					mSelectedPosition = (Integer)v.getTag();
				}
			}
//			else{
//				if( v.getTag() != null)
//				{
//					mSelectedPosition = (Integer)v.getTag();
//					if( mListItemActionListener != null)
//					{
//						mListItemActionListener.onClickItem(getItem(mSelectedPosition), mSelectedPosition, v);
//					}
//				}
//			}
			
		}
	};
	
	private void clearHighLightBg()
	{
		for(View v : mViewList)
		{
			v.setBackgroundDrawable(null);
		}
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		if( mRequiredHighLight )
		{
			clearHighLightBg();
			mSelectedPosition = -1;
		}
		super.notifyDataSetChanged();
		
	} 
	
	
	

}
